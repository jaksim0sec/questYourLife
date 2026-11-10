import test from "node:test";
import assert from "node:assert/strict";
import {readFileSync} from "node:fs";
const read=(f)=>readFileSync(new URL(f,import.meta.url),"utf8");
const html=read("./index.html"),js=read("./lab.js"),style=read("./lab.css"),seed=read("../preview/seed-standard.css");

test("Page Shell remains an S-PAGE layout primitive rather than a new page type",()=>{
  assert.match(html,/class="seed-page-shell"[^>]*data-shell-kind="root"/);
  for(const slot of ["header","content","section","bottom"])assert.match(seed,new RegExp("\\.seed-page-shell__"+slot+"\\b"));
  for(const role of ["root","detail","utility"])assert.match(html,new RegExp('data-shell-choice="'+role+'"'));
  assert.match(read("./seed-visual.mjs"),/function render\(kind,explicitType\)/);
  assert.doesNotMatch(style,/\.seed-page-shell__content\s*\{/);
});

test("sheet footer demonstrates none, single, paired and stacked layouts",()=>{
  for(const layout of ["none","single","paired","stacked"])assert.match(html,new RegExp('data-footer-choice="'+layout+'"'));
  assert.match(html,/id="sheet-footer"/);
  assert.match(js,/function applyFooterLayout\(/);
  assert.match(js,/footer\.hidden\s*=/);
  assert.match(seed,/\.seed-sheet__footer\[data-layout="stacked"\]/);
  assert.match(seed,/\.seed-sheet\[open\]/);
});

test("sheet footer contains no false status button, and actions keep canonical styles",()=>{
  assert.doesNotMatch(html,/id="sheet-snap-badge"/);
  assert.match(html,/id="sheet-next" class="seed-action" data-variant="brand-solid"/);
  assert.match(html,/id="sheet-done" class="seed-action" data-variant="neutral-outline"/);
  assert.match(js,/sheetBody\.scrollHeight/);
});

test("control strokes and semantic choices are owned by shared CSS",()=>{
  assert.match(seed,/--seed-stroke-control:/);
  assert.match(seed,/\.seed-action\[data-variant="neutral-outline"\] \.seed-action__face\s*\{[^}]*--seed-stroke-control/);
  assert.match(seed,/\.seed-choice input\[type="checkbox"\]/);
  assert.match(seed,/\.seed-choice input\[type="radio"\]/);
  assert.match(seed,/\.seed-field\s*\{[^}]*--seed-stroke-control/);
  assert.doesNotMatch(style,/\.seed-choice input\s*\{/);
});

test("gallery badges use a distinct surface and original 3D assets remain",()=>{
  assert.match(seed,/\.seed-media \.seed-media__badge\[data-tone="neutral"\]/);
  for(const image of ["01_school_locker.png","06_wooden_chest.png","08_premium_glowing_chest.png"])assert.ok(html.includes(image));
  assert.match(html,/data-layout="grid"/);
});

test("mobile section headings can consume full available title width",()=>{
  assert.match(style,/@media \(max-width:700px\)[\s\S]*\.lab-section-head/);
  assert.match(style,/\.lab-section-head\s*>\s*\.seed-badge\s*\{[^}]*position:absolute/);
});
test("five Shell roles and real navigation interactions remain in canonical Lab",()=>{
 const mirror=read("./seed-visual.mjs");
 for(const role of ['root','detail','utility','immersive','overlay'])assert.ok(mirror.includes(role+':{title:'),role);
 for(const piece of ["shell-back","shell-close","shell-nav","shell-fixed-cta","shell-open-sheet"])assert.ok(mirror.includes(piece),piece);
 assert.match(mirror,/aria-current/);
 assert.match(mirror,/O-003 미확정/);
 assert.doesNotMatch(js,/\["#page-shell-bottom","bottom"\]/);
});
