import test from "node:test";
import assert from "node:assert/strict";
import {readFileSync} from "node:fs";

const read=path=>readFileSync(new URL(path,import.meta.url),"utf8");
const shared=read("../../seed/styles/seed-standard.css");
const legacyCss=read("../preview/style.css");
const legacyHtml=read("../preview/index.html");
const labHtml=read("./index.html");

test("canonical SEED Action does not share selectors with legacy gallery buttons",()=>{
  assert.doesNotMatch(shared,/\.gallery-button(?:[.\s,{]|$)/m);
  assert.doesNotMatch(legacyCss,/\.seed-action(?:[.\s\[]|$)/m);
  assert.doesNotMatch(legacyHtml,/class="gallery-button seed-action/);
  assert.match(labHtml,/class="seed-action"/);
});
test("canonical action target owns no visible stroke and face owns visual stroke",()=>{
  const base=shared.match(/\.seed-action\s*\{([^}]*)\}/)?.[1];
  assert.ok(base,"missing canonical target");
  assert.match(base,/border:\s*0(?:px)?\b/);
  assert.match(base,/padding:\s*0(?:px)?\b/);
  assert.match(shared,/\.seed-action\[data-variant="neutral-outline"\]\s+\.seed-action__face\s*\{[^}]*border-color:\s*var\(--seed-stroke-control/);
  assert.doesNotMatch(shared,/\.seed-action\[data-variant="neutral-outline"\]\s*\{[^}]*border:/);
});
test("button text size belongs to C-ACTION role and stays stable across variants",()=>{
  const base=shared.match(/\.seed-action\s*\{([^}]*)\}/)?.[1];
  assert.match(base,/font-size:\s*\.875rem\s*;/);
  assert.match(shared,/\.seed-action\[data-size="small"\]\s*\{[^}]*font-size:\s*\.75rem\s*;/);
  assert.doesNotMatch(base,/font:\s*inherit/);
});
test("legacy demo retains its own gallery button presentation without overriding canonical buttons",()=>{
  assert.match(legacyCss,/\.gallery-button\s*\{[^}]*font-size:\s*0\.79rem/);
  assert.match(legacyCss,/\.gallery-button\.secondary\s*\{[^}]*border:\s*1px solid var\(--line\)/);
  assert.match(legacyCss,/\.gallery-button\.reward\s*\{[^}]*margin-top:\s*0\.75rem/);
  assert.doesNotMatch(legacyCss,/\.gallery-button\s*,\s*\.seed-action/);
});
test("approved action sizes and roles remain in shared CSS; no page-level adjustment for the fix",()=>{
  assert.match(shared,/\.seed-action__face\s*\{[^}]*min-height:\s*43px/);
  assert.match(shared,/\.seed-action\[data-size="small"\]\s+\.seed-action__face\s*\{[^}]*min-height:\s*34px/);
  assert.match(shared,/\.seed-action\[data-size="large"\]\s+\.seed-action__face\s*\{[^}]*min-height:\s*48px/);
  assert.match(shared,/\.seed-action\[data-variant="brand-solid"\]\s+\.seed-action__face/);
  assert.match(shared,/\.seed-action\[data-variant="reward-secondary"\]\s+\.seed-action__face/);
  assert.match(read("./lab.css"),/\.lab-actions\s*\{[^}]*gap:/);
});
