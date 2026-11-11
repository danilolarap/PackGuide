import test from 'node:test';
import assert from 'node:assert/strict';
import { createPackingTips } from '../packing-tips.mjs';

test('ten different photos get ten different tips, then rotation restarts', () => {
  const tip = createPackingTips();
  const photos = Array.from({ length: 11 }, (_, i) => Buffer.from(`photo-${i}`).toString('base64'));
  const results = photos.map(photo => tip(photo, 'es'));
  assert.equal(new Set(results.slice(0, 10)).size, 10);
  assert.equal(results[10], results[0]);
  assert.equal(tip(photos[0], 'es'), results[0]);
  assert.match(tip(photos[0], 'en'), /^PackGuide tip \(prewritten\) 1\/10:/);
  assert.match(tip(photos[1], 'en'), /^PackGuide tip \(prewritten\) 2\/10:/);
});
