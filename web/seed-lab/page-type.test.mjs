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
test('five new-reference destinations use original active SVG assets',()=>{
 assert.deepEqual(WEB_NAV_ITEMS.map(x=>x.key),['home','quests','dm','clan','me']);
 assert.equal(WEB_NAV_ITEMS.find(x=>x.key==='clan').active,'clan-bookmark-fill');
 assert.equal(WEB_NAV_ITEMS.find(x=>x.key==='quests').active,'check-circle-fill');
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
 assert.match(css,/\.head h1\s*\{[^}]*font-size:1\.18rem/);
 assert.match(css,/\.head\.standard h1\s*\{[^}]*font-size:\.98rem/);
 assert.match(css,/\.seed-top-nav__title\s*\{[^}]*font-size:1\.18rem/);
 assert.match(css,/\.seed-top-nav__inner\[data-nav-role="standard"\] \.seed-top-nav__title\s*\{[^}]*font-size:\.98rem/);
 assert.match(css,/\.seed-top-nav\.seed-page-shell__header\s*\{[^}]*background:var\(--canvas\)/);
 assert.match(css,/\.sheet-head, \.seed-sheet__head\s*\{/);
 assert.doesNotMatch(css,/\.tools \.icon-btn:nth-last-child/);
});
