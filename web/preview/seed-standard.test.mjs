import test from 'node:test';
import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';

const here = dirname(fileURLToPath(import.meta.url));
const read = file => readFileSync(join(here, file), 'utf8');
const html = read('index.html');
const shared = read('seed-standard.css');
const local = read('style.css');
const clan = read('clan.css');

test('loads the shared stylesheet before the page-specific sheets', () => {
  assert.match(html, /href="seed-standard\.css"[\s\S]*href="style\.css"[\s\S]*href="clan\.css"/);
});

test('uses actual shared component classes, not only a catalogue', () => {
  for (const name of ['seed-action', 'seed-media', 'seed-badge', 'seed-progress-track', 'seed-icon', 'seed-text']) {
    assert.match(shared, new RegExp('\\.' + name + '(?![\\w-])'));
    assert.match(html, new RegExp('class="[^"]*\\b' + name + '\\b'));
  }
});

test('keeps the existing preview color values and its light/dark aliases', () => {
  for (const token of ['--canvas: #fcfbfa', '--canvas: #101115', '--brand: #f04e5b', '--brand: #f45c68', '--reward: #ffcc4d', '--reward: #957027']) {
    assert.ok(shared.includes(token), token);
  }
});

test('keeps the original card ratio and page-specific grid proportions', () => {
  assert.match(shared, /aspect-ratio:\s*1\s*\/\s*1/);
  assert.match(local, /grid-auto-columns:\s*minmax\(8\.25rem,\s*40%\)/);
  assert.match(local, /grid-auto-columns:\s*minmax\(8\.25rem,\s*29%\)/);
  assert.match(local, /\.reward-frame img\s*\{\s*width:\s*86%;\s*height:\s*86%/);
  assert.match(clan, /\.clan-chest-frame\s*\{[^}]*width:57%/);
});

test('retains the original sample data, media paths and progress values', () => {
  for (const value of ['--progress:33.333%', '--progress:58.333%', '7 / 12', '3,421명 참여', 'assets/mascot.png', 'assets/box.webp', 'href="#clan-detail"']) {
    assert.ok(html.includes(value), value);
  }
});

test('does not recreate shared component geometry inside page CSS', () => {
  for (const selector of ['.seed-action {', '.seed-media {', '.seed-badge {', '.progress-track {', '.seed-icon {']) {
    assert.ok(!local.includes(selector), selector);
  }
});
