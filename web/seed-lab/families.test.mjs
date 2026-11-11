import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync,existsSync} from 'node:fs';
const read=file=>readFileSync(new URL(file,import.meta.url),'utf8');
const html=read('./index.html'),legacy=read('./families.html'),css=read('./families.css'),js=read('./families.js'),standard=read('../preview/seed-standard.css');
test('family catalog uses one standard CSS; local CSS is layout only',()=>{
  assert.match(html,/href="\.\.\/preview\/seed-standard\.css"/);
  assert.match(html,/href="\.\/families\.css"/);
  assert.doesNotMatch(css,/(?:^|\n)\s*\.seed-(?:text|action__face|field|badge)\s*\{/);
});
test('C-TEXT styles are present with the fixed approved scale',()=>{
  for(const style of ['root','standard','hero','number','section','item','body','support','caption','number-small'])
    assert.ok(html.includes('data-text-style="'+style+'"'),style);
  assert.match(standard,/\.seed-text\[data-text-style="root"\][^}]*1\.625rem/);
  assert.match(standard,/\.seed-text\[data-text-style="caption"\][^}]*\.75rem/);
});
test('six approved Action variants, separate proposal scope, and state demonstrations',()=>{
  for(const variant of ['brand-solid','neutral-solid','neutral-weak','neutral-outline','ghost','reward-secondary'])
    assert.ok(html.includes('data-variant="'+variant+'"'),variant);
  assert.match(html,/data-seed-family-preview/);
  assert.ok(html.includes('data-variant="brand-weak"'));
  assert.ok(html.includes('data-variant="brand-outline"'));
  for(const size of ['small','default','large'])assert.ok(html.includes('data-family-size="'+size+'"'));
  assert.ok(html.includes('aria-busy="true"'));
  assert.ok(html.includes(' disabled'));
  assert.match(js,/actionSamples\.forEach\(action=>action\.dataset\.size=size\)/);
});
test('Status inputs map to the same progress normalization and ARIA',()=>{
  assert.match(html,/role="radiogroup" aria-label="진행률 예제"/);
  assert.match(js,/progressButtons\.forEach\(button=>button\.checked=button\.dataset\.value===key\)/);
  assert.match(js,/import \{normalizeProgress\} from "\.\/progress\.mjs"/);
  for(const value of ['0','620','1000','1200','loading','unavailable'])assert.ok(html.includes('data-value="'+value+'"'));
  for(const thickness of ['micro','default','featured'])assert.ok(html.includes('data-thickness="'+thickness+'"'));
  assert.match(js,/track\.removeAttribute\("aria-valuenow"\)/);
});
test('Input/List/Media/SVG examples preserve native semantics and original assets',()=>{
  assert.match(html,/for="family-field"/);
  assert.match(html,/name="family-radio"/);
  assert.match(html,/class="seed-list__item"/);
  assert.match(html,/data-media-fit="contain"/);
  for(const asset of ['01_school_locker.png','06_wooden_chest.png','08_premium_glowing_chest.png'])
    assert.ok(html.includes(asset)&&existsSync(new URL('../../assets/art/rewards/'+asset,import.meta.url)));
  assert.match(html,/soft\/v0\.14\/search\.svg/);
  assert.doesNotMatch(js,/supabase|fetch\(|localStorage|sessionStorage/);
});
test('component gallery is the first tab of the canonical Lab and the previous URL redirects',()=>{
  assert.match(html,/id="tab-components"[^>]*aria-selected="true"/);
  assert.match(html,/id="panel-components"[^>]*data-panel="components"/);
  assert.match(html,/src="\.\/families\.js"/);
  assert.match(legacy,/url=\.\/\?panel=components/);
  for(const name of ['layout','state','assets'])assert.ok(html.includes('id="panel-'+name+'"'));
});

test('C-CREDIT is rendered from the original raster inside an existing Surface panel',()=>{
  assert.match(standard,/\.seed-credit\s*\{/);
  assert.match(standard,/\.seed-credit__coin\s*\{/);
  assert.match(standard,/\.seed-credit\[data-size="small"\]/);
  assert.match(standard,/\.seed-credit\[data-size="large"\]/);
  assert.match(html,/id="family-credit-panel" class="seed-surface" data-surface="subtle"/);
  assert.match(html,/class="seed-credit" data-size="medium" data-state="available"/);
  assert.match(html,/class="seed-credit__coin" src="\.\.\/\.\.\/assets\/art\/rewards\/cash-coin-single\.png" alt=""/);
  assert.match(html,/class="seed-credit__value">3,240<\/span>/);
  assert.ok(existsSync(new URL('../../assets/art/rewards/cash-coin-single.png',import.meta.url)));
  assert.doesNotMatch(html,/class="seed-credit__coin"[^>]*(?:coins\.svg|ⓒ|🪙)/);
});

test('Filter Chip uses C-SELECTION, one real selection group and no navigation shortcut disguised as a filter',()=>{
  assert.match(standard,/\.seed-filter-chip\s*\{/);
  assert.match(standard,/\.seed-filter-chip\[aria-pressed="true"\]/);
  assert.match(html,/id="family-filter-group"[^>]*role="group" aria-label="아이템 종류 필터"/);
  for(const name of ["all","hats","audio"])assert.match(html,new RegExp('data-family-filter="'+name+'"'));
  assert.doesNotMatch(html,/id="family-filter-group"[^>]*[\s\S]*?data-family-filter="rewards"/);
  assert.match(js,/\$\$\("\[data-family-filter\]"\)/);
  assert.match(js,/setChoice\(filterButtons,"familyFilter",button\.dataset\.familyFilter\)/);
  assert.match(html,/id="family-filter-feedback"[^>]*aria-live="polite"/);
});

test('composition gate binds source identity to credit, filters, and restrained typography',()=>{
  const compose=read('../..\/docs/design/seed/patterns/page-composition.md');
  const credit=read('../..\/docs/design/seed/components/credit-value.md');
  const selection=read('../..\/docs/design/seed/components/selection-controls.md');
  assert.match(compose,/컴포넌트 출처/);
  assert.match(compose,/C-CREDIT/);
  assert.match(compose,/주목 그룹/);
  assert.match(credit,/data-surface="subtle"/);
  assert.match(credit,/seed-credit/);
  assert.match(selection,/seed-filter-chip/);
  assert.match(selection,/aria-pressed/);
});
