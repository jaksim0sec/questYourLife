import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {PAGE_TYPES,WEB_NAV_ITEMS,getPageType,applyPageType,selectWebNav} from '../preview/seed-page-contract.mjs';
const text=p=>readFileSync(new URL(p,import.meta.url),'utf8');
const html=text('./index.html');
const css=text('../preview/seed-standard.css');
const js=text('./seed-visual.mjs');
function makeShell(){
 const header={dataset:{}},nav={hidden:false},bottom={hidden:false};
 const shell={dataset:{},querySelector(s){return s==='.seed-top-nav'?header:s==='.seed-bottom-nav'?nav:s==='.seed-page-shell__bottom'?bottom:null;}};
 return {shell,header,nav,bottom};
}
test('exact four route page types own deterministic navigation visibility',()=>{
 assert.deepEqual(Object.keys(PAGE_TYPES),['global','global-inner','non-global','bottom-sheet']);
 for(const [type,top,bottomVisible] of [['global','root',true],['global-inner','standard',true],['non-global','standard',false]]){
  const fixture=makeShell();const state=applyPageType(fixture.shell,type);
  assert.equal(state.header,top);
  assert.equal(fixture.shell.dataset.pageType,type);
  assert.equal(fixture.header.dataset.navRole,top);
  assert.equal(fixture.nav.hidden,!bottomVisible);
  assert.equal(fixture.bottom.hidden,!bottomVisible);
 }
 const edited=makeShell();applyPageType(edited.shell,'non-global',{fixedCta:true});
 assert.equal(edited.nav.hidden,true);
 assert.equal(edited.bottom.hidden,false,'a fixed CTA has its own bottom slot');
});
test('Bottom Sheet is an overlay and never overwrites its parent page classification',()=>{
 const fixture=makeShell();applyPageType(fixture.shell,'global-inner');
 assert.equal(getPageType('bottom-sheet').overlay,true);
 assert.throws(()=>applyPageType(fixture.shell,'bottom-sheet'),/overlay/);
 assert.equal(fixture.shell.dataset.pageType,'global-inner');
 assert.equal(fixture.nav.hidden,false);
 assert.throws(()=>getPageType('other'),RangeError);
});
test('five web destinations preserve canonical SVGs and preview-only active variants',()=>{
 assert.deepEqual(WEB_NAV_ITEMS.map(x=>x.key),['home','quests','dm','clan','me']);
 assert.equal(WEB_NAV_ITEMS.find(x=>x.key==='clan').active,'clan-bookmark-fill');
 assert.equal(WEB_NAV_ITEMS.find(x=>x.key==='quests').active,'check-circle-fill');
 const my=WEB_NAV_ITEMS.find(x=>x.key==='me');
 assert.equal(my.outline,'me-smile');
 assert.equal(my.active,'me-smile-fill');
 assert.equal(my.outlineBase,'../../assets/icons/preview/');
 assert.equal(my.activeBase,'../../assets/icons/preview/');
 const links=WEB_NAV_ITEMS.map(item=>{
  const style={};return{dataset:{navKey:item.key},attrs:{},
   setAttribute(k,v){this.attrs[k]=v;},removeAttribute(k){delete this.attrs[k];},
   querySelector(){return{style:{setProperty(k,v){style[k]=v;}}}},styleValues:style};
 });
 const nav={querySelectorAll:()=>links};
 selectWebNav(nav,'quests');
 assert.deepEqual(links.filter(x=>x.attrs['aria-current']==='page').map(x=>x.dataset.navKey),['quests']);
 assert.match(links[1].styleValues['--seed-icon'],/check-circle-fill.svg/);
 selectWebNav(nav,'clan');
 assert.deepEqual(links.filter(x=>x.attrs['aria-current']==='page').map(x=>x.dataset.navKey),['clan']);
 assert.match(links[3].styleValues['--seed-icon'],/assets\/icons\/preview\/clan-bookmark-fill\.svg/);
 selectWebNav(nav,'home');
 assert.match(links[3].styleValues['--seed-icon'],/assets\/icons\/soft\/v0\.14\/bookmark\.svg/);
 assert.match(links[4].styleValues['--seed-icon'],/assets\/icons\/preview\/me-smile\.svg/);
 selectWebNav(nav,'me');
 assert.match(links[4].styleValues['--seed-icon'],/assets\/icons\/preview\/me-smile-fill\.svg/);
 assert.deepEqual(links.filter(x=>x.attrs['aria-current']==='page').map(x=>x.dataset.navKey),['me']);
 assert.throws(()=>selectWebNav(nav,'invalid'),RangeError);
});
test('canonical Lab exposes all four page types and illustrates the C-IMAGE states',()=>{
 for(const name of ['global','global-inner','non-global','bottom-sheet'])assert.match(html,new RegExp('data-page-type-choice="'+name+'"'));
 for(const state of ['loading','loaded','error'])assert.match(html,new RegExp('data-media-state="'+state+'"'));
 for(const token of ['.seed-top-nav__inner','.seed-bottom-nav__link','.seed-media[data-media-kind="item"]'])assert.ok(css.includes(token));
 assert.match(js,/from "\.\.\/preview\/seed-page-contract\.mjs"/);
 assert.match(js,/applyPageType\(shell,currentRoute,\{fixedCta\}\)/);
 assert.doesNotMatch(js,/bookmark-fill.svg/);
});

test('Root and Standard headers share a visual scale without icon-order shifts',()=>{
 assert.match(css,/\.head, \.seed-top-nav__inner\s*\{/);
 assert.match(css,/font-family:"Pretendard Variable"/);
 assert.match(css,/\.head h1\s*\{[^}]*font-size:1\.30rem/);
 assert.match(css,/\.head\.standard h1\s*\{[^}]*font-size:1\.08rem/);
 assert.match(css,/\.seed-top-nav__title\s*\{[^}]*font-size:1\.30rem/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\] \.seed-top-nav__title\s*\{[^}]*font-size:1\.08rem/);
 assert.match(css,/\.seed-top-nav\.seed-page-shell__header\s*\{[^}]*background:var\(--canvas\)/);
 assert.match(css,/\.sheet-head, \.seed-sheet__head\s*\{/);
 assert.doesNotMatch(css,/\.tools \.icon-btn:nth-last-child/);
});

test('My smile states share the preview-only source and do not mutate 56 canonical icons',()=>{
 for(const [file,needle] of [['me-smile.svg','stroke-width="1.8"'],['me-smile-fill.svg','fill-rule="evenodd"']]){
  const source=text('../../assets/icons/preview/'+file);
  assert.match(source,/<svg[^>]*viewBox="0 0 24 24"/);
  assert.ok(source.includes(needle));
 }
 const preview=text('../preview/app.js');
 assert.match(preview,/me:\['me-smile','me-smile-fill'\]/);
 assert.match(preview,/key==='me'/);
 assert.match(text('../preview/index.html'),/assets\/icons\/preview\/me-smile\.svg/);
 assert.match(js,/item\.outlineBase\|\|/);
});

test('selected smile preserves outline silhouette and uses a distinct readable cutout face',()=>{
 const outline=text('../../assets/icons/preview/me-smile.svg');
 const selected=text('../../assets/icons/preview/me-smile-fill.svg');
 const r=Number(outline.match(/<circle cx="12" cy="12" r="([\d.]+)"/)[1]);
 const stroke=Number(outline.match(/stroke-width="([\d.]+)"/)[1]);
 const filledR=Number(selected.match(/M12 1\.9a([\d.]+)/)[1]);
 assert.ok(Math.abs(filledR-(r+stroke/2))<1e-6,'Fill must match outline outer radius including half stroke');
 assert.match(selected,/fill-rule="evenodd"/);
 assert.match(selected,/M9\.2 8\.65a\.95/);
 assert.match(selected,/M14\.8 8\.65a\.95/);
 assert.match(selected,/M7\.55 12\.98 C8\.05/);
 assert.match(css,/\.seed-bottom-nav__link \.seed-icon \{ --seed-icon-size:1\.56938rem/);
});

test('approved light header is shared across Root and Standard instead of a Lab override',()=>{
 const styles=text('./seed-visual.css');
 assert.doesNotMatch(html,/data-header-weight|seed-header-weight-controls/);
 assert.doesNotMatch(js,/setHeaderWeight|weightButtons/);
 assert.doesNotMatch(styles,/data-header-weight|seed-header-weight-controls/);
 assert.match(css,/\.head h1\s*\{[^}]*font-size:1\.30rem;[^}]*font-weight:650/);
 assert.match(css,/\.head\.standard h1\s*\{[^}]*font-size:1\.08rem;[^}]*font-weight:620/);
 assert.match(css,/\.seed-top-nav__title\s*\{[^}]*font-size:1\.30rem;[^}]*font-weight:650/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\] \.seed-top-nav__title\s*\{[^}]*font-size:1\.08rem;[^}]*font-weight:620/);
 assert.match(css,/--seed-top-glyph:1\.59rem/);
});

test('approved header gutter remains shared with preview home and clan content',()=>{
 const preview=text('../preview/style.css');
 const clan=text('../preview/clan.css');
 assert.match(css,/--seed-page-gutter:20px/);
 assert.match(css,/@media \(max-width:359px\) \{ :root \{ --seed-page-gutter:16px;/);
 assert.match(css,/\.head \{[^}]*padding-inline:var\(--seed-page-gutter,20px\)/);
 assert.match(css,/\.seed-top-nav__inner \{[^}]*padding-inline:var\(--seed-page-gutter,1\.25rem\)/);
 assert.match(preview,/\.home-content \{[^}]*var\(--seed-page-gutter,20px\)/);
 assert.match(clan,/\.clan-section\{[^}]*var\(--seed-page-gutter,20px\)/);
});

test('header bell is visually closed at its bottom and preview/Lab reuse the same source',()=>{
 const bell=text('../../assets/icons/preview/bell-connected.svg');
 assert.match(bell,/viewBox="0 0 24 24"/);
 assert.match(bell,/stroke-width="1\.8"/);
 assert.match(bell,/M9\.5 17\.5q2\.5 5\.5 5 0/);
 assert.match(bell,/H4\.3q-1\.4 0 -\.7-1\.2l1\.6-3Z/);
 assert.match(text('../preview/index.html'),/assets\/icons\/preview\/bell-connected\.svg/);
 assert.match(js,/assets\/icons\/preview\/bell-connected\.svg/);
 assert.match(js,/icon\("bell"\)/);
});


test('Standard title start alignment is opt-in while Center and page roles remain unchanged',()=>{
 assert.equal(PAGE_TYPES['global-inner'].header,'standard');
 assert.equal(PAGE_TYPES['global-inner'].bottomNav,true);
 assert.equal(PAGE_TYPES['non-global'].header,'standard');
 assert.equal(PAGE_TYPES['non-global'].bottomNav,false);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\]\[data-title-align="start"\]\s*\{/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\] \.seed-top-nav__title\s*\{[^}]*text-align:center/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\]\[data-title-align="start"\] \.seed-top-nav__title\s*\{[^}]*text-align:start/);
 assert.match(css,/\.head\.standard\[data-title-align="start"\]\s*\{/);
 assert.match(css,/\.head\.standard\[data-title-align="start"\] h1\s*\{[^}]*text-align:start/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\]\[data-title-align="start"\] \.seed-top-nav__trailing\[data-slot-kind="info"\]/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\]\[data-title-align="start"\] \.seed-top-nav__leading\s*\{[^}]*min\(/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\]\[data-title-align="start"\] \.seed-top-nav__trailing\[data-slot-kind="empty"\]\s*\{[^}]*display:none/);
 assert.match(js,/headerTrailing\.dataset\.slotKind="empty"/);
 assert.match(css,/--seed-top-target:3rem/);
 assert.match(css,/--seed-top-glyph:1\.59rem/);
});

test('Lab can independently select centered/left title and info/action/empty trailing',()=>{
 assert.match(html,/data-title-align-choice="center" aria-pressed="true"/);
 assert.match(html,/data-title-align-choice="start" aria-pressed="false"/);
 for(const name of ['action','credit','empty'])assert.match(html,new RegExp('data-header-trailing-choice="'+name+'"'));
 assert.match(js,/header\.dataset\.titleAlign=titleAlign/);
 assert.match(js,/headerTrailing\.dataset\.slotKind="info"/);
 assert.match(js,/data-title-align-choice/);
 assert.match(js,/data-header-trailing-choice/);
 assert.match(js,/cash-coin-single\.png/);
 assert.match(js,/seed-credit__coin/);
 assert.match(js,/aria-label="예시 보유 캐시 3,240"/);
 assert.match(js,/\.seed-top-nav__trailing/);
 assert.match(js,/setPressed\(titleAlignButtons,titleAlign,"titleAlignChoice"\)/);
 assert.match(js,/setPressed\(trailingChoiceButtons,trailingChoice,"headerTrailingChoice"\)/);
});
