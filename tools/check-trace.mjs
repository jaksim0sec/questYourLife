import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
export function validate(files){
 const errors=[], ids=new Map(), dirs=["active","archive"], valid=["planned","in_progress","blocked","done"], roots=["AGENTS.md","docs/README.md","docs/AGENTS.md","docs/core/CURRENT_STATE.md","docs/core/TODO.md","docs/core/INFRASTRUCTURE_SPEC.md","docs/tasks/INDEX.md","docs/tasks/templates/task.md","docs/validation/VALIDATION_POLICY.md","docs/knowledge/INDEX.md"];
 for(const p of roots)if(!files.has(p))errors.push("missing "+p);
 const index=files.get("docs/tasks/INDEX.md")||"";
 const normalize=(p)=>{let a=[];for(const x of p.split("/")){if(x===".." )a.pop();else if(x&&x!==".")a.push(x)}return a.join("/")};
 const checkLinks=(p,s)=>{for(const m of s.matchAll(/\]\(([^()\s]+)\)/g)){const target=m[1].split("#")[0];if(!target||/^[a-z]+:/i.test(target))continue;const n=normalize(p.split("/").slice(0,-1).concat(target).join("/"));if(!files.has(n)&&![...files.keys()].some(x=>x.startsWith(n+"/")))errors.push(`${p}: missing link ${n}`)}};
 for(const p of roots.filter(p=>!["docs/README.md","docs/AGENTS.md"].includes(p)))if(files.has(p))checkLinks(p,files.get(p));
 for(const [p,s] of files){if(!/^docs\/tasks\/(active|archive)\/[^/]+\.json$/.test(p))continue;let t;try{t=JSON.parse(s)}catch{errors.push("invalid JSON "+p);continue}const name=p.split("/").pop().slice(0,-5);const loc=p.split("/")[2];if(!/^[A-Z]+-\d+$/.test(t.id)||name!==t.id||ids.has(t.id)){errors.push("invalid/duplicate ID "+p);continue}ids.set(t.id,t);
 if(!valid.includes(t.status)||(loc==="archive")!==(t.status==="done"))errors.push("invalid status "+p);
 if(!/^[0-9a-f]{40}$/.test(t.baseline))errors.push("invalid baseline "+p);
 const expected=p.slice(0,-5)+".md";if(t.detail!==expected||!files.has(expected))errors.push("invalid detail "+p);else{const d=files.get(expected);checkLinks(expected,d);if(t.status!=="done"&&(!new RegExp("^"+t.id+"/T[1-9][0-9]*(?:\\.[0-9]+)?$").test(t.next||"")||!d.includes("## "+String(t.next).split("/")[1]+" ")))errors.push("invalid next "+p)}
 if(!index.includes(`(${loc}/${name}.md)`)||!index.includes(`(${loc}/${name}.json)`))errors.push("unindexed "+p);
 for(const key of ["scope","contracts","dependencies"])if(!Array.isArray(t[key])||t[key].some(x=>typeof x!=="string"))errors.push("invalid "+key+" "+p);
 if(Array.isArray(t.scope)){if(!t.scope.length)errors.push("empty scope "+p);for(const a of t.scope)if(!a||a.startsWith("/")||a.includes("\\")||a.split("/").some(x=>!x||x===".."||x==="."))errors.push("invalid scope "+a)}
 if(Array.isArray(t.contracts))for(const c of t.contracts)if(!files.has(c))errors.push("missing contract "+c)
 }
 for(const [p] of files)if(/^docs\/tasks\/(active|archive)\/[^/]+\.md$/.test(p)&&!files.has(p.slice(0,-3)+".json"))errors.push("unregistered "+p);
 const claimed=[...ids.values()].filter(t=>["in_progress","blocked"].includes(t.status));
 for(const t of ids.values())if(Array.isArray(t.dependencies))for(const d of t.dependencies)if(!ids.has(d)||d===t.id)errors.push("invalid dependency "+t.id+" -> "+d);
 for(let i=0;i<claimed.length;i++)for(let j=i+1;j<claimed.length;j++)for(const a of claimed[i].scope||[])for(const b of claimed[j].scope||[])if(a===b||a.startsWith(b+"/")||b.startsWith(a+"/"))errors.push("scope overlap "+claimed[i].id+" "+claimed[j].id+": "+a+" / "+b);
 return errors;
}
export function load(root) {
  const files = new Map();
  function walk(dir) { for (const e of fs.readdirSync(path.join(root,dir), {withFileTypes:true})) { if (['.git','node_modules','.gradle','build'].includes(e.name)) continue; const p=dir ? dir+'/'+e.name:e.name; if(e.isDirectory())walk(p); else if(e.isFile())files.set(p,/\.(md|json)$/.test(p)?fs.readFileSync(path.join(root,p),'utf8'):''); }}
  walk(''); return files;
}
if(process.argv[1] && path.resolve(process.argv[1])===fileURLToPath(import.meta.url)) { const errors=validate(load(fileURLToPath(new URL('../',import.meta.url)))); if(errors.length){console.error(errors.join('\n')); process.exitCode=1;}else console.log('TRACE structure, registration, links and scope: PASS'); }
