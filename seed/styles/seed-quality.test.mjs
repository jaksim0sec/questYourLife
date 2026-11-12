import test from 'node:test';
import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
const text = p => readFileSync(new URL(p, import.meta.url), 'utf8');
const css=text('./seed-standard.css');
const tokens=JSON.parse(text('../implementation/v015-tokens.json'));
const top=text('../TOP_NAVIGATION.md');
const selection=text('../components/segmented-control.md');
const item=text('../patterns/content-hierarchy.md');
const surface=text('../components/surface-media.md');
function declaration(selector){
  const i=css.indexOf(selector+' {');
  if(i<0)return '';
  const a=css.indexOf('{',i);
  return css.slice(a+1,css.indexOf('}',a));
}
test('Shared CSS supplies core Surface/Action tokens outside Lab',()=>{
  // :root is deliberately split between historic palette and shared geometry declarations.
  // Collect declarations with the exact selector; never count :root[data-seed-lab].
  const root=[...css.matchAll(/(?:^|\n):root\s*\{([^}]*)\}/g)].map(x=>x[1]).join(';');
  const dark=[...css.matchAll(/(?:^|\n):root\[data-theme="dark"\]\s*\{([^}]*)\}/g)].map(x=>x[1]).join(';');
  for(const token of ['--soft','--seed-radius-control','--seed-radius-image','--seed-radius-surface','--seed-stroke-control','--seed-space-inline']){
    assert.ok(root.includes(token+':'),'missing global '+token);
  }
  assert.match(dark,/--seed-stroke-control:/);
  assert.match(root,/font-family:"Pretendard Variable"/);
});
test('Outline action stays neutral and not implicitly pill shaped',()=>{
  const face=declaration('.seed-action__face');
  assert.match(face,/border-radius:var\(--seed-radius-control\)/);
  const outline=declaration('.seed-action[data-variant="neutral-outline"] .seed-action__face');
  assert.match(outline,/border-color:var\(--seed-stroke-control/);
  assert.match(outline,/color:color-mix\(/);
  assert.doesNotMatch(outline,/border-radius:999px/);
});
test('Horizontal integrated card fills its media panel without cropping the item',()=>{
  const horizontal=declaration('.seed-item-display[data-item-presentation="integrated"][data-item-layout="horizontal"]');
  assert.match(horizontal,/display:grid/);
  const media=declaration('.seed-item-display[data-item-presentation="integrated"][data-item-layout="horizontal"] > .seed-media[data-media-kind="item"]');
  assert.match(media,/aspect-ratio:auto/);
  assert.match(media,/height:100%/);
  assert.match(declaration('.seed-media[data-media-fit="contain"] .seed-media__image'),/object-fit:contain/);
  assert.match(item,/가로형.*이미지 패널.*전체 높이/);
});
test('Header tuning is local to navigation and matches tokens',()=>{
  const root=declaration('.seed-top-nav__title');
  const standard=declaration('.seed-top-nav__inner[data-nav-role="standard"] .seed-top-nav__title');
  assert.match(root,/font-weight:680/);
  assert.match(standard,/font-weight:650/);
  assert.equal(tokens.topNavigation.root.fontWeight,680);
  assert.equal(tokens.topNavigation.standard.fontWeight,650);
  assert.match(top,/Pretendard.*CDN.*보장/);
});
test('Start title clears the back target with a compact gap in both headers',()=>{
  const geometry=declaration('.head, .seed-top-nav__inner');
  assert.match(geometry,/--seed-top-target:3rem/);
  assert.match(geometry,/--seed-top-start-column:calc\(var\(--seed-top-target\) - min\(var\(--seed-top-optical-inset\), var\(--seed-page-gutter,20px\)\) - \.375rem\)/);
  for (const selector of ['.head.standard', '.seed-top-nav__inner[data-nav-role="standard"]']) {
    const start=declaration(selector+'[data-title-align="start"]');
    assert.match(start,/grid-template-columns:var\(--seed-top-start-column\) minmax\(0,1fr\) minmax\(0,auto\); column-gap:\.5rem/);
    assert.doesNotMatch(declaration(selector),/--seed-top-start-column/);
  }
});
test('Do not redraw banner effects in SVG or turn periods into independent action buttons',()=>{
  assert.match(item,/SVG로.*이펙트.*금지/);
  assert.match(surface,/SVG로.*이펙트.*금지/);
  assert.match(selection,/독립.*Outline.*버튼.*비권장/);
});
