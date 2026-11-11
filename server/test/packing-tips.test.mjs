import test from 'node:test';
import assert from 'node:assert/strict';
import { createPackingTips } from '../packing-tips.mjs';

test('photos receive two distinct tips, stable on reopening, from twenty available tips', () => {
  const tip = createPackingTips();
  const seen = new Set();
  for (let i = 0; i < 200; i++) {
    const photo = Buffer.from(`photo-${i}`).toString('base64');
    const result = tip(photo, 'es');
    assert.equal(result, tip(photo, 'es'));
    const lines = result.split('\n');
    assert.equal(lines.length, 3);
    assert.notEqual(lines[1].slice(3), lines[2].slice(3));
    assert.ok(!result.includes('pregrabado'));
    seen.add(lines[1].slice(3)); seen.add(lines[2].slice(3));
    assert.ok(tip(photo, 'en').startsWith('PACKING TIPS\n1. '));
  }
  assert.equal(seen.size, 20);
});
