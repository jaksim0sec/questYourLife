// P2/P3 independent structural audit, separate from browser evidence.
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const root=path.resolve(__dirname,'../../../docs/design');
const comps=path.join(root,'seed/components');
const names=fs.readdirSync(comps).filter(x=>x.endsWith('.md'));
assert.equal(names.length,21,'SEED must have exactly 21 existing component files');
const labels=['Selection',"Don't",'Relations','Verification'];
for(const name of names){
 const s=fs.readFileSync(path.join(comps,name),'utf8');
 assert.equal((s.match(/^## 현행 계약$/gm)||[]).length,1,name+' current count');
 assert.equal((s.match(/^## 이력$/gm)||[]).length,1,name+' history count');
 const current=s.split('## 현행 계약')[1].split('## 이력')[0];
 for(const label of labels)assert.equal((current.match(new RegExp('^### '+label+'$','gm'))||[]).length,1,name+' '+label);
 const dont=current.split("### Don't")[1].split('### Relations')[0];
 assert.ok((dont.match(/^\d+\./gm)||[]).length>=2,name+' at least 2 Do-not cases');
 const sections=current.split(/^### (?:Selection|Don't|Relations|Verification)$/gm);
 assert.ok(sections.length>=5,name+' content missing');
}
const ledger=fs.readFileSync(path.join(root,'seed/LEDGER.md'),'utf8').split('\n');
const p2=ledger.filter(s=>/^\| L\d{4} \|/.test(s)&&/\|\s*P2\s*\|/.test(s)).map(s=>s.match(/L\d{4}/)[0]);
assert.equal(p2.length,380,'P2 LEDGER count');
const trace=fs.readFileSync(path.join(root,'seed/P2_TRACE.md'),'utf8').split('\n').filter(s=>/^\| L\d{4} \|/.test(s)).map(s=>s.match(/L\d{4}/)[0]);
assert.equal(trace.length,380,'P2_TRACE count');
assert.deepEqual([...trace].sort(),[...p2].sort(),'P2 ledger mapping exactly one for each ID');
for(const p of ['clan-home','clan-reward','clan-my','clan-record','dressing-fixture']){
 const d=fs.readFileSync(path.join(root,'pages/'+p+'.md'),'utf8');
 for(const id of ['01 Page Intent','02 콘텐츠','03 객체','04 선택','05 시각','06 구현','07 검증'])
   assert.ok(d.includes('## '+id),p+' missing section '+id);
 for(const f of ['유형','과업(동사)','목적','주연 1개','대표 행동 1개','핵심 데이터','보조 데이터','다른 화면 책임','제외 목록과 이유'])
   assert.ok(d.includes('| '+f+' |'),p+' missing intent field '+f);
 for(let i=1;i<=7;i++)assert.ok(d.includes('| G'+i+' |'),p+' missing G'+i);
}
console.log('P3 independent contract audit PASS: 21 components x4/2 negative cases, P2 ledger 380 exact, 5 page records x9 fields x7 Gates.');
