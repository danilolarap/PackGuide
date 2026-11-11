import { createServer } from 'node:http';
import { pathToFileURL } from 'node:url';
import { createHash } from 'node:crypto';
import { assessWithGemini } from './gemini-review.mjs';
import { RequestError, validatePackingRequest, generatePackingImage, assessPackingPhoto } from './packing.mjs';

function reply(response, status, body) {
  if (response.destroyed) return;
  response.writeHead(status, { 'Content-Type': 'application/json; charset=utf-8', 'Cache-Control': 'no-store', 'X-Content-Type-Options': 'nosniff' });
  response.end(JSON.stringify(body));
}

export function createPackingServer({ apiKey = process.env.OPENAI_API_KEY,
  model = process.env.OPENAI_IMAGE_MODEL || 'gpt-image-2.5-sunburst',
  reviewModel = process.env.OPENAI_REVIEW_MODEL || 'gpt-5-mini',
  reviewProvider = process.env.PACKGUIDE_REVIEW_PROVIDER || 'gemini',
  geminiKey = process.env.GEMINI_API_KEY,
  geminiModel = process.env.GEMINI_REVIEW_MODEL || 'gemini-3.5-flash-lite', fetchImpl = fetch } = {}) {
  if (!['gemini', 'openai'].includes(reviewProvider)) throw new Error('Invalid review provider');
  const reviewKey = reviewProvider === 'gemini' ? geminiKey : apiKey;
  let active = 0;
  const requests = new Map();
  const reviews = new Map();
  return createServer(async (request, response) => {
    const abort = new AbortController();
    response.once('close', () => { if (!response.writableEnded) abort.abort(); });
    let admitted = false;
    try {
      const host = new URL(`http://${request.headers.host}`).hostname;
      if (!['127.0.0.1', 'localhost', '[::1]', '10.0.2.2'].includes(host) || request.headers.origin) {
        throw new RequestError(403, 'FORBIDDEN');
      }
      if (request.method === 'GET' && request.url === '/health') {
        return reply(response, 200, { configured: Boolean(reviewKey?.trim()) });
      }
      const assessment = request.url === '/v1/packing-review';
      if (request.method !== 'POST' || (!assessment && request.url !== '/v1/packing-image')) throw new RequestError(404, 'NOT_FOUND');
      if ((request.headers['content-type'] || '').split(';')[0].trim().toLowerCase() !== 'application/json') throw new RequestError(415, 'JSON_REQUIRED');
      if (!(assessment ? reviewKey : apiKey)?.trim()) throw new RequestError(503, 'NOT_CONFIGURED');
      const now = Date.now();
      for (const [address, times] of requests) if (times.every(time => now - time > 60_000)) requests.delete(address);
      const peer = `${request.socket.remoteAddress}:${assessment ? 'review' : 'generation'}`;
      const recent = (requests.get(peer) || []).filter(time => now - time < 60_000);
      if (recent.length >= (assessment ? 30 : 4) || active >= 2) throw new RequestError(429, 'BUSY');
      recent.push(now); requests.set(peer, recent);
      active++; admitted = true;
      let bytes = 0;
      const chunks = [];
      for await (const chunk of request) {
        bytes += chunk.length;
        if (bytes > (assessment ? 4_010_000 : 16_000)) throw new RequestError(413, 'BODY_TOO_LARGE');
        chunks.push(chunk);
      }
      let body;
      try { body = JSON.parse(Buffer.concat(chunks).toString('utf8')); }
      catch { throw new RequestError(400, 'INVALID_JSON'); }
      // Cache successful reviews briefly, using a digest instead of retaining the photo.
      const reviewId = assessment ? createHash('sha256').update(JSON.stringify(body)).digest('hex') : null;
      for (const [id, entry] of reviews) if (now - entry.time > 300_000) reviews.delete(id);
      if (reviewId && reviews.has(reviewId)) return reply(response, 200, reviews.get(reviewId).result);
      const signal = AbortSignal.any([abort.signal, AbortSignal.timeout(180_000)]);
      const assess = reviewProvider === 'gemini' ? assessWithGemini : assessPackingPhoto;
      const result = assessment
        ? await assess(body, { apiKey: reviewKey, model: reviewProvider === 'gemini' ? geminiModel : reviewModel, fetchImpl, signal })
        : await generatePackingImage(validatePackingRequest(body), { apiKey, model, fetchImpl, signal });
      if (reviewId) {
        if (reviews.size >= 32) reviews.delete(reviews.keys().next().value);
        reviews.set(reviewId, { time: Date.now(), result });
      }
      reply(response, 200, result);
    } catch (error) {
      const failure = error instanceof RequestError ? error : error?.name === 'TimeoutError'
        ? new RequestError(504, 'GENERATION_TIMEOUT') : new RequestError(502, 'GENERATION_FAILED');
      reply(response, failure.status, { error: { code: failure.code } });
    } finally { if (admitted) active--; }
  });
}

if (process.argv[1] && import.meta.url === pathToFileURL(process.argv[1]).href) {
  const port = Number(process.env.PORT || 8787);
  if (!Number.isInteger(port) || port < 1 || port > 65535) throw new Error('Invalid PORT');
  // Local development only. Public deployments must put an authenticated TLS gateway in front.
  const server = createPackingServer();
  server.requestTimeout = 30_000;
  server.listen(port, '127.0.0.1', () => console.log(`PackGuide AI listening on http://127.0.0.1:${port}`));
}
