import test from 'node:test';
import assert from 'node:assert/strict';
import { once } from 'node:events';
import { validatePackingRequest, buildPackingPrompt, generatePackingImage } from '../packing.mjs';
import { createPackingServer } from '../server.mjs';

const jpeg = Buffer.from([0xff, 0xd8, 0xff, 0xe0, 0, 16, 0xff, 0xd9]).toString('base64');
const request = () => ({ schemaVersion: 1, language: 'es', dimensions: [55, 35, 25], items: [
  { id: 'ITEM-01', quantity: 12, packedQuantity: 11, zone: 'BASE', name: 'do not trust client item names' },
  { id: 'ITEM-02', quantity: 20, packedQuantity: 0, zone: 'CENTRO' }
] });
const providerResponse = () => new Response(JSON.stringify({ data: [{ b64_json: jpeg }] }), {
  headers: { 'Content-Type': 'application/json' }
});

test('12 selected and 11 packed produces exactly 11 authorized shirts and omits unchecked types', () => {
  const validated = validatePackingRequest(request());
  assert.equal(validated.items.length, 1);
  assert.equal(validated.items[0].quantity, 11);
  assert.equal(validated.items[0].name, 'CAMISETAS');
  const prompt = buildPackingPrompt(validated);
  assert.ok(prompt.includes('"quantity":11'));
  assert.ok(!prompt.includes('"quantity":12'));
  assert.ok(!prompt.includes('PANTALONES'));
  assert.ok(!prompt.includes('do not trust client'));
  assert.ok(prompt.includes('not a diagram'));
  assert.ok(!prompt.includes('reference photo'));
  assert.throws(() => validatePackingRequest({ ...request(), photo: {} }), { code: 'INVALID_INPUT' });
});

test('missing packed counts never fall back to selected counts; invalid and duplicate entries fail', () => {
  for (const change of [
    body => delete body.items[0].packedQuantity,
    body => { body.items[0].packedQuantity = -1; },
    body => { body.items[0].packedQuantity = 1.5; },
    body => { body.items[0].id = 'unknown'; },
    body => { body.items.push(body.items[0]); },
    body => { body.dimensions[0] = 0; }
  ]) {
    const body = request(); change(body);
    assert.throws(() => validatePackingRequest(body), { code: 'INVALID_INPUT' });
  }
  const empty = request(); empty.items[0].packedQuantity = 0;
  assert.throws(() => validatePackingRequest(empty), { code: 'NO_PACKED_ITEMS' });
});

test('packed inventory uses image generation without uploading comparison photos', async () => {
  {
    const body = request();
    let called = false;
    const result = await generatePackingImage(validatePackingRequest(body), {
      apiKey: 'server-only-test-key', model: 'test-image-model',
      fetchImpl: async (url, options) => {
        called = true;
        assert.equal(url, 'https://api.openai.com/v1/images/generations');
        assert.equal(options.headers.Authorization, 'Bearer server-only-test-key');
        const payload = JSON.parse(options.body);
        assert.equal(payload.n, 1);
        assert.equal(payload.output_format, 'jpeg');
        assert.equal(payload.model, 'test-image-model');
        assert.equal(payload.images, undefined);
        assert.ok(!payload.prompt.includes('"quantity":12'));
        return providerResponse();
      }
    });
    assert.ok(called);
    assert.deepEqual(result.packedItems, [{ id: 'ITEM-01', packedQuantity: 11 }]);
    assert.equal(result.imageBase64, jpeg);
    assert.ok(!JSON.stringify(result).includes('server-only-test-key'));
  }
});

test('no credentials or provider failure never returns a local or fabricated image', async () => {
  await assert.rejects(generatePackingImage(validatePackingRequest(request()), {}), { code: 'NOT_CONFIGURED' });
  await assert.rejects(generatePackingImage(validatePackingRequest(request()), {
    apiKey: 'test', model: 'test', fetchImpl: async () => new Response('secret provider message', { status: 401 })
  }), { code: 'PROVIDER_ACCESS' });
  await assert.rejects(generatePackingImage(validatePackingRequest(request()), {
    apiKey: 'test', model: 'test', fetchImpl: async () =>
      Response.json({ error: { code: 'insufficient_quota' } }, { status: 429 })
  }), { code: 'PROVIDER_QUOTA' });
  await assert.rejects(generatePackingImage(validatePackingRequest(request()), {
    apiKey: 'test', model: 'test', fetchImpl: async () => new Response('{"data":[]}')
  }), { code: 'INVALID_IMAGE' });
});

test('HTTP service rejects invalid/empty inventories before calling the image provider', async t => {
  let calls = 0;
  const server = createPackingServer({ apiKey: 'test', fetchImpl: async () => { calls++; return providerResponse(); } });
  server.listen(0, '127.0.0.1'); await once(server, 'listening');
  t.after(() => new Promise(resolve => server.close(resolve)));
  const url = `http://127.0.0.1:${server.address().port}/v1/packing-image`;
  const empty = request(); empty.items[0].packedQuantity = 0;
  const rejected = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(empty) });
  assert.equal(rejected.status, 422);
  assert.equal(calls, 0);
  const response = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(request()) });
  assert.equal(response.status, 200);
  assert.equal((await response.json()).packedItems[0].packedQuantity, 11);
  assert.equal(calls, 1);
  const crossOrigin = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json', Origin: 'https://untrusted.example' }, body: JSON.stringify(request()) });
  assert.equal(crossOrigin.status, 403);
  assert.equal(calls, 1);
});

test('unconfigured local server reports unavailable rather than pretending generation succeeded', async t => {
  const server = createPackingServer({ reviewProvider: 'openai', apiKey: '' });
  server.listen(0, '127.0.0.1'); await once(server, 'listening');
  t.after(() => new Promise(resolve => server.close(resolve)));
  const base = `http://127.0.0.1:${server.address().port}`;
  assert.deepEqual(await (await fetch(base + '/health')).json(), { configured: false });
  const response = await fetch(base + '/v1/packing-image', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(request()) });
  assert.equal(response.status, 503);
  assert.deepEqual(await response.json(), { error: { code: 'NOT_CONFIGURED' } });
});

test('photo assessment sends the selected JPEG to the vision model and returns its score and feedback', async t => {
  const server = createPackingServer({ reviewProvider: 'openai', apiKey: 'test', reviewModel: 'test-vision', fetchImpl: async (url, options) => {
    assert.equal(url, 'https://api.openai.com/v1/responses');
    const payload = JSON.parse(options.body);
    assert.equal(payload.model, 'test-vision');
    assert.equal(payload.store, false);
    assert.equal(payload.input[0].content[1].image_url, `data:image/jpeg;base64,${jpeg}`);
    return Response.json({ output: [{ type: 'message', content: [{ type: 'output_text',
      text: JSON.stringify({ score: 8, feedback: 'Deja lo de uso frecuente arriba.' }) }] }] });
  } });
  server.listen(0, '127.0.0.1'); await once(server, 'listening');
  t.after(() => new Promise(resolve => server.close(resolve)));
  const response = await fetch(`http://127.0.0.1:${server.address().port}/v1/packing-review`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ schemaVersion: 1, language: 'es', photoBase64: jpeg })
  });
  assert.equal(response.status, 200);
  const review = await response.json();
  assert.equal(review.schemaVersion, 1);
  assert.equal(review.score, 8);
  assert.ok(review.feedback.startsWith('Deja lo de uso frecuente arriba.\n\nCONSEJOS PARA ORGANIZAR TU MALETA\n1. '));
});
