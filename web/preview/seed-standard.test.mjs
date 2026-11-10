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
  for (const name of ['seed-media', 'seed-badge', 'seed-progress-track', 'seed-icon', 'seed-text']) {
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


test('the historical gallery buttons are separate from canonical SEED Action', () => {
  assert.match(shared, /\.seed-action\s*\{/);
  assert.doesNotMatch(html, /gallery-button seed-action/);
  assert.match(local, /\.gallery-button\s*\{/);
});

test('the typography family is owned by approved C-TEXT role and style scales',()=>{
  for(const role of ['display','title','section','body','caption'])assert.ok(shared.includes('.seed-text[data-text-role="'+role+'"]'));
  for(const style of ['root','standard','hero','number','section','item','body','support','caption','number-small'])
    assert.ok(shared.includes('data-text-style="'+style+'"'),style);
  assert.match(shared,/\.seed-text\[data-text-style="root"\][^{]*\{[^}]*1\.625rem/);
  assert.match(shared,/\.seed-text\[data-text-style="support"\][^{]*\{[^}]*\.875rem/);
  assert.match(shared,/data-text-weight="strong"/);
  assert.doesNotMatch(shared,/\.seed-text\[data-text-role="display"\][^{]*\{[^}]*clamp\(/);
});

test('existing action sizes and approved six variants stay canonical',()=>{
  for(const variant of ['brand-solid','neutral-solid','neutral-weak','neutral-outline','ghost','reward-secondary'])
    assert.ok(shared.includes('.seed-action[data-variant="'+variant+'"]'),variant);
  for(const size of ['small','large'])assert.ok(shared.includes('.seed-action[data-size="'+size+'"]'));
  assert.match(shared,/\.seed-action\s*\{[^}]*min-height:3rem/);
  assert.match(shared,/\.seed-action__face\s*\{[^}]*min-height:43px/);
  assert.match(shared,/\.seed-action\[data-size="small"\] \.seed-action__face\s*\{[^}]*min-height:34px/);
  assert.match(shared,/\.seed-action\[data-size="large"\] \.seed-action__face\s*\{[^}]*min-height:48px/);
});

test('proposed brand treatments are scoped and action loading does not alter its face box',()=>{
  assert.match(shared,/:root\[data-seed-family-preview\] \.seed-action\[data-variant="brand-weak"\]/);
  assert.match(shared,/:root\[data-seed-family-preview\] \.seed-action\[data-variant="brand-outline"\]/);
  assert.match(shared,/\.seed-action\[aria-busy="true"\] \.seed-action__label/);
  assert.match(shared,/\.seed-action\[aria-busy="true"\] \.seed-action__face::after/);
  assert.doesNotMatch(local,/\.seed-action__face\s*\{/);
});

test('existing List, Field and Icon contract slots are shared, not page overrides',()=>{
  for(const part of ['seed-list__item','seed-list__prefix','seed-list__content','seed-list__suffix','seed-field-group','seed-field-label','seed-field-help','seed-icon-action'])
    assert.ok(shared.includes('.'+part),part);
  assert.doesNotMatch(local,/\.seed-list__item\s*\{/);
});
