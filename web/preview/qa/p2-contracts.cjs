// SEED r3 P2: static contract requirements, independently checked from 21 actual documents.
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const root=path.resolve(__dirname,'../../../docs/design/seed'),read=p=>fs.readFileSync(path.join(root,p),'utf8');
const index=read('INDEX.md');
const names=[...index.matchAll(/^\|\s*(C-[A-Z]+)\s*\|[^\n]*?\]\((components\/[^)]+)\)/gm)].map(m=>({id:m[1],file:m[2]}));
assert.equal(names.length,21,'exactly 21 original components preserved in INDEX');
const errors=[];
for(const {id,file} of names){
 const doc=read(file);
 const current=doc.split('## 현행 계약'),history=doc.split('## 이력');
 assert.equal(current.length,2,id+' must have one active current contract');
 assert.equal(history.length,2,id+' must have one history');
 const active=current[1].split('## 이력')[0];
 const sequence=['Selection',"Don't",'Relations','Verification'].map(t=>active.indexOf('### '+t));
 assert.ok(sequence.every(x=>x>=0),'missing four sections: '+id);
 assert.ok(sequence.every((x,i)=>i===0||x>sequence[i-1]),'wrong section order: '+id);
 const dont=active.slice(sequence[1],sequence[2]);
 assert.ok(/(?:^|\n)1\.\s/.test(dont)&&/(?:^|\n)2\.\s/.test(dont),id+' must include 2 grounded counterexamples');
 const rel=active.slice(sequence[2],sequence[3]);
 for(const k of ['부모','자식','형제','금지'])assert.ok(rel.includes(k),'relations '+id+': '+k);
 const verify=active.slice(sequence[3]);
 assert.ok(verify.includes('320/390/430'),'responsive verification: '+id);
 const select=active.slice(sequence[0],sequence[1]);
 assert.ok(/배제|금지|피할|배타|대체|대신|아니라|우선|허용하지/.test(select),'Selection must distinguish competing choices: '+id);
 assert.ok(!/^## (?!현행 계약|이력)/m.test(doc),'out-of-contract H2: '+id);
 const inHistory=history[1];assert.ok(inHistory.length>40,'history must not be empty '+id);
 console.log('P2-contract '+id+' OK '+file);
}
const ledger=read('LEDGER.md');
const rows=ledger.split('\n').filter(x=>/^\| L\d{4} \|/.test(x));
assert.equal(rows.length,585,'full source LEDGER');
const p2=rows.filter(x=>/\|\s*P2\s*\|/.test(x));assert.equal(p2.length,380,'P2 source row count');
const owners={};
for(const row of p2){
 const m=row.match(/\|\s*((?:components|foundations|patterns|objects)\/[-a-z]+\.md)#현행 계약\s*\|\s*P2\s*\|/);
 assert.ok(m,'P2 row missing canonical owner: '+row.slice(0,140));
 assert.ok(fs.existsSync(path.join(root,m[1])),'owner file missing: '+m[1]);
 owners[m[1]]=(owners[m[1]]||0)+1;
}
const r=read('P2_TRACE.md');for(const row of p2){const id=row.match(/^\|\s*(L\d{4})/)[1];assert.ok(r.includes('| '+id+' |'),'missing P2 tracking '+id);}
console.log('G-P2 document/ownership smoke PASS: 21 contracts, 21x4 clauses, 380/380 P2 trace links; this is not semantic or render acceptance');
