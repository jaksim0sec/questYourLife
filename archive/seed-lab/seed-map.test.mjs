import test from "node:test";
import assert from "node:assert/strict";
import {readFileSync,existsSync} from "node:fs";
const read=p=>readFileSync(new URL(p,import.meta.url),"utf8");
const index=read("./index.html"),catalog=JSON.parse(read("./seed-sources.json"));
const js=read("./seed-map.mjs"),css=read("./seed-map.css");
test("all INDEX definitions are represented in the Lab",()=>{
 const lines=read("../../seed/INDEX.md").split("\n").filter(x=>/^\| (?:F-|C-|P-|S-|A-|NAV-|R-|V-)/.test(x));
 assert.equal(catalog.items.filter(x=>x.fromIndex).length,lines.length);
 for(const row of lines)assert.ok(catalog.items.some(x=>x.id===row.split("|")[1].trim()));
 const expected={foundation:5,component:21,pattern:7,shell:2};
 for(const [category,n] of Object.entries(expected))assert.equal(catalog.items.filter(x=>x.category===category).length,n);
});
test("mirror references only repo files and preserves approval distinctions",()=>{
 for(const doc of catalog.items)assert.ok(existsSync(new URL("../../"+doc.path,import.meta.url)),doc.path);
 assert.equal(catalog.items.find(x=>x.id==="C-ACTION").status,"proposed");
 assert.equal(catalog.items.find(x=>x.id==="NAV-TOP").status,"approved");
});
test("mirror is first in original Lab, and rest of Lab survives",()=>{
 assert.match(index,/id="tab-map"[^>]*data-panel-target="map"/);
 assert.match(index,/id="panel-map"[^>]*data-panel="map"/);
 for(const name of ["components","layout","state","assets"])assert.ok(index.includes('id="panel-'+name+'"'));
 for(const id of ["seed-source-list","seed-doc-title","seed-doc-body","seed-doc-raw","seed-doc-outline","lab-sheet","reward-gallery","icon-gallery"])assert.ok(index.includes('id="'+id+'"'));
 assert.match(index,/src="\.\/seed-map\.mjs"/);
});
test("render uses pinned version, textContent and not unsanitized HTML",()=>{
 assert.match(js,/raw\.githubusercontent\.com/);
 assert.match(js,/sourceCommit/);
 assert.match(js,/textContent=text/);
 assert.doesNotMatch(js,/innerHTML\s*=/);
 assert.match(js,/wireExamples/);
});
test("no canonical .seed component appearance overridden by mirror CSS",()=>{
 assert.doesNotMatch(css,/(?:^|\n)\s*\.seed-(?:action|text|surface|field|badge)\s*\{/);
});
test("SEED source is linked from all visual experiments, not only listed in raw text",()=>{
 const mirror=read("./seed-visual.mjs"),visualCss=read("./seed-visual.css");
 for(const name of ['foundation','components','layout','shells','state','assets'])assert.match(index,new RegExp('data-seed-panel="'+name+'"'));
 assert.match(js,/\[data-seed-doc\]/);
 assert.match(js,/lab-source-details/);
 assert.match(mirror,/tab\.click\(\)/);
 assert.match(index,/id="panel-shells"/);
 assert.match(index,/id="panel-foundation"/);
 assert.doesNotMatch(visualCss,/(?:^|\n)\s*\.seed-(?:action|text|surface|field|badge)\s*\{/);
});
test("actual Shell roles and navigation assets are based on the canonical manifest",()=>{
 const scripts=read("./seed-visual.mjs");
 const manifest=JSON.parse(read("../../assets/icons/soft/v0.14/manifest.json"));
 const names=new Set([...manifest.base,...manifest.fill,...manifest.additional]);
 for(const icon of ["home","home-fill","chat","chat-fill","users","users-fill","user","user-fill","left","close","search","more"])assert.ok(names.has(icon),icon);
 for(const role of ["root","detail","utility","immersive","overlay"])assert.ok(scripts.includes(role+':{title:'),role);
 assert.match(scripts,/function render\(kind\)/);
 assert.match(scripts,/aria-current/);
 assert.match(scripts,/shell-open-sheet/);
 assert.match(scripts,/data-nav-key/);
 assert.match(index,/data-panel-target="shells"/);
});
