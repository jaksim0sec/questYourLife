import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync,readdirSync,existsSync} from 'node:fs';
const text=path=>readFileSync(new URL(path,import.meta.url),'utf8');
const html=text('./index.html');
const css=text('../../seed/styles/seed-standard.css');
const labCss=text('./lab.css');
const js=text('./lab.js');

test('the lab references the existing common CSS rather than a second component stylesheet',()=>{
  assert.match(html,/href="\.\.\/preview\/seed-standard\.css"/);
  assert.match(html,/href="\.\/lab\.css"/);
  assert.match(html,/type="module" src="\.\/lab\.js"/);
  assert.match(css,/\.seed-action\[data-variant="brand-solid"\]/);
  assert.match(css,/\.progress-track\[data-thickness="featured"\]/);
  assert.doesNotMatch(labCss,/\.seed-action__face\s*\{/);
});
test('each declared lab tab has a real panel',()=>{
  for(const name of ['map','foundation','components','layout','shells','state','assets']){
    assert.ok(html.includes('id="tab-'+name+'"'));
    assert.ok(html.includes('id="panel-'+name+'"'));
    assert.ok(html.includes('aria-controls="panel-'+name+'"'));
    assert.ok(html.includes('data-panel="'+name+'"'));
  }
});
test('registered original PNG set contains exactly eight images with PNG signatures',()=>{
  const directory=new URL('../../assets/art/rewards/',import.meta.url);
  const files=readdirSync(directory).filter(file=>file.endsWith('.png'));
  assert.equal(files.length,8);
  for(const file of files){
    const bytes=readFileSync(new URL(file,directory));
    assert.equal(bytes.subarray(0,8).toString('hex'),'89504e470d0a1a0a',file);
    assert.ok(bytes.length>1000);
  }
});
test('the shared SVG manifest points to all 56 original files',()=>{
  const dir=new URL('../../assets/icons/soft/v0.14/',import.meta.url);
  const m=JSON.parse(readFileSync(new URL('manifest.json',dir),'utf8'));
  assert.deepEqual([m.base.length,m.fill.length,m.additional.length,m.total],[32,5,19,56]);
  for(const name of [...m.base,...m.fill,...m.additional]){
    assert.match(name,/^[a-z][a-z0-9-]*$/);
    assert.ok(existsSync(new URL(name+'.svg',dir)),name);
  }
});
test('the progress lab has all K08 boundary conditions and keeps the real shared progress component',()=>{
  for(const name of ['0','620','1000','1200','unavailable','loading']){
    assert.ok(js.includes(name+':')||js.includes('"'+name+'":'));
  }
  assert.match(js,/normalizeProgress\(/);
  assert.match(html,/class="progress-track"/);
  for(const value of ['micro','default','featured'])assert.match(html,new RegExp('data-thickness="'+value+'"'));
});
test('body of the lab does not import the clan preview or app state',()=>{
  assert.doesNotMatch(html,/clan\.js|app\.js|supabase/);
  assert.match(js,/fetch\(ICON_ROOT\+"manifest.json"\)/);
});

test('thickness selection targets controls only and never writes aria-pressed to the progressbar',()=>{
  assert.match(js,/const thicknessButtons=\$\$\("button\[data-thickness\]"\)/);
});
test('unavailable progress clears any old accessible value text',()=>{
  const section=js.split('else if(p.state==="unavailable"){')[1].split('}else{')[0];
  assert.match(section,/track\.removeAttribute\("aria-valuetext"\)/);
});

test('page controls have a single shared owner and sheet footer still initializes',()=>{
  const viewer=text('./seed-visual.mjs');
  assert.equal((html.match(/data-shell-choice=/g)||[]).length,3);
  assert.equal((html.match(/data-page-type-choice=/g)||[]).length,4);
  assert.equal((html.match(/data-footer-choice=/g)||[]).length,4);
  assert.match(viewer,/const shellButtons=\$\$\("button\[data-shell-choice\]"\)/);
  assert.doesNotMatch(js,/const shellButtons=\$\$\("button\[data-shell-choice\]"\)/);
  assert.match(js,/footerButtons=\$\$\("button\[data-footer-choice\]"\)/);
  assert.match(js,/\$\("#sheet-open"\)\.addEventListener\("click"/);
});

test('S06 item B uses one parent with two actual presentations and responsive shared geometry',()=>{
  const contract=text('../../seed/patterns/content-hierarchy.md');
  const surface=text('../../seed/components/surface-media.md');
  const frame=text('../../seed/components/image-frame.md');
  assert.match(contract,/버튼이 들어가면 통합 카드형 외부 컨테이너 사용을 권장/);
  assert.match(contract,/1:1 비율과 contain/);
  assert.match(surface,/바깥 카드·border·그림자 없음/);
  assert.match(frame,/프레임 border는 기본 없음/);
  assert.match(css,/\.seed-item-display\s*\{[^}]*flex-direction:column/);
  const itemRoot=css.match(/\.seed-item-display\s*\{([^}]*)\}/)?.[1]||'';
  const itemInfo=css.match(/\.seed-item-display__info\s*\{([^}]*)\}/)?.[1]||'';
  const itemAction=css.match(/\.seed-item-display__action\s*\{([^}]*)\}/)?.[1]||'';
  assert.doesNotMatch(itemRoot,/\bheight:100%/,'item cards should default to content height');
  assert.doesNotMatch(itemInfo,/flex:1\s+1\s+auto/,'information must not absorb unrelated column height');
  assert.doesNotMatch(itemAction,/margin-block-start:auto/,'buttons must remain adjacent to prices');
  assert.match(itemAction,/margin-block-start:var\(--seed-space-inline\)/);
  assert.match(labCss,/\.lab-item-presentation-items\s*\{[^}]*align-items:start/);
  assert.match(labCss,/\.lab-item-presentation-grid\s*\{[^}]*align-items:start/);
  assert.match(css,/\.seed-item-display\[data-item-presentation="integrated"\]\s*\{[^}]*padding:0/);
  assert.match(css,/\.seed-item-display\[data-item-presentation="open"\]/);
  assert.match(css,/\.seed-media\[data-media-kind\][^{]*\{[^}]*aspect-ratio:var\(--media-ratio,1 \/ 1\)/);
  assert.match(css,/\.seed-media\[data-media-fit="contain"\][^{]*\{[^}]*padding:8%/);
  assert.match(html,/data-item-presentation="integrated"/);
  assert.match(html,/data-item-presentation="open"/);
  assert.equal((html.match(/data-item-preview=/g)||[]).length,2);
  assert.match(js,/\$\$\("\[data-item-preview\]"\)/);
  assert.doesNotMatch(labCss,/\.seed-item-display__info\s*\{/);
  assert.match(labCss,/grid-template-columns:repeat\(2,minmax\(0,1fr\)\)/);
  for(const image of ['cosmetic_cream_headphones.png','cosmetic_blue_cap.png','cosmetic_charcoal_cap.png','cosmetic_black_headphones.png']){
    assert.ok(html.includes(image),image);
    assert.ok(existsSync(new URL('../../assets/art/cosmetics/'+image,import.meta.url)),image);
  }
});
