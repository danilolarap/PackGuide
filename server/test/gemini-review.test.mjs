import test from 'node:test';
import assert from 'node:assert/strict';
import { once } from 'node:events';
import { assessWithGemini } from '../gemini-review.mjs';
import { createPackingServer } from '../server.mjs';

const photoBase64 = Buffer.from([255, 216, 255, 224, 0, 16, 255, 217]).toString('base64');
const body = { schemaVersion: 1, language: 'es', photoBase64 };
const result = (score = 8) => Response.json({ candidates: [{ finishReason: 'STOP', content: {
  parts: [{ text: JSON.stringify({ score, feedback: 'Enrolla las camisetas ligeras juntas.' }) }]
} }] });

test('Gemini receives only the chosen photo and returns score and advice through the Android endpoint', async t => {
  const server = createPackingServer({ geminiKey: 'test-key', reviewProvider: 'gemini', fetchImpl: async (url, options) => {
    assert.equal(url, 'https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent');
    assert.equal(options.headers['x-goog-api-key'], 'test-key');
    const payload = JSON.parse(options.body);
    assert.deepEqual(payload.contents[0].parts[1].inlineData, { mimeType: 'image/jpeg', data: photoBase64 });
    assert.equal(payload.generationConfig.responseMimeType, 'application/json');
    assert.equal(payload.tools, undefined);
    return result();
  } });
  server.listen(0, '127.0.0.1'); await once(server, 'listening');
  t.after(() => new Promise(resolve => server.close(resolve)));
  const response = await fetch(`http://127.0.0.1:${server.address().port}/v1/packing-review`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
  });
  assert.equal(response.status, 200);
  assert.deepEqual(await response.json(), { schemaVersion: 1, score: 8, feedback: 'Enrolla las camisetas ligeras juntas.' });
});

test('unjudgeable photo can return null; fabricated or incomplete scores are rejected', async () => {
  const options = { apiKey: 'test', model: 'test', fetchImpl: async () => result(null) };
  assert.equal((await assessWithGemini(body, options)).score, null);
  for (const score of [0, 11, 7.5, '8']) {
    await assert.rejects(assessWithGemini(body, { ...options, fetchImpl: async () => result(score) }), { code: 'INVALID_REVIEW' });
  }
  await assert.rejects(assessWithGemini(body, { ...options, fetchImpl: async () => Response.json({ candidates: [] }) }), { code: 'INVALID_REVIEW' });
});

test('free quota exhaustion does not retry or call another provider', async () => {
  let calls = 0;
  await assert.rejects(assessWithGemini(body, { apiKey: 'test', model: 'test', fetchImpl: async () => {
    calls++;
    return new Response('{}', { status: 429 });
  } }), { code: 'PROVIDER_QUOTA' });
  assert.equal(calls, 1);
  await assert.rejects(assessWithGemini(body, {}), { code: 'NOT_CONFIGURED' });
});

test('five successive assessments succeed and repeated photos reuse a successful review', async t => {
  let calls = 0;
  const server = createPackingServer({ geminiKey: 'test', reviewProvider: 'gemini', fetchImpl: async () => {
    calls++; return result();
  } });
  server.listen(0, '127.0.0.1'); await once(server, 'listening');
  t.after(() => new Promise(resolve => server.close(resolve)));
  for (let i = 0; i < 5; i++) {
    const response = await fetch(`http://127.0.0.1:${server.address().port}/v1/packing-review`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
    });
    assert.equal(response.status, 200);
    assert.equal((await response.json()).score, 8);
  }
  assert.equal(calls, 1);
});
