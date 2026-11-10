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
