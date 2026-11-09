import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync,readdirSync,existsSync} from 'node:fs';
const text=path=>readFileSync(new URL(path,import.meta.url),'utf8');
const html=text('./index.html');
const css=text('../preview/seed-standard.css');
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
  for(const name of ['layout','state','assets']){
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
