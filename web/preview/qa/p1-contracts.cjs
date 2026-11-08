// P1 canonical rule smoke suite; T2 requires a genuinely independent, fresh session.
const fs=require('node:fs'),assert=require('node:assert/strict'),path=require('node:path');
const dir=path.resolve(__dirname,'../../../docs/design/seed');
const read=p=>fs.readFileSync(path.join(dir,p),'utf8');
const norms=p=>{const s=read(p),a=s.split('## 현행 계약'),b=(a[1]||'').split('## 이력');assert.equal(a.length,2,'exactly one current: '+p);assert.equal(b.length,2,'exactly one history: '+p);assert.ok(!/K0[0-9]|v0\.|2026-/.test(b[0]),'old version in current: '+p);return b[0]};
const fmt=['INDEX.md','VALIDATION.md','shells/page-shells.md','patterns/content-hierarchy.md','foundations/typography.md','components/text-hierarchy.md','foundations/geometry.md'];
for(const p of fmt)norms(p);
const required=[
 ['R-01','pages/page-intent.md','진행·관리'],['R-02','objects/item.md','반례 R-02'],['R-03','shells/page-shells.md','시작선'],
 ['R-04','objects/progress.md','R-04'],['R-05','PROCESS.md','G2'],['R-06','components/selection-controls.md','2.3px'],
 ['R-07','PROCESS.md','6명'],['R-08','objects/README.md','대표 타깃'],['R-09','pages/page-types.md','하단바'],
 ['R-10','foundations/typography.md','9.7px']
];
const val=norms('VALIDATION.md');
for(const [id,p,phrase] of required){assert.ok(val.includes(id),'VALIDATION missing '+id);assert.ok(read(p).includes(phrase),'T1 contract absent '+id+': '+p);}
console.log('T1 PASS: 10/10 rules connected to canonical clauses');
const idx=norms('INDEX.md');assert.ok(idx.indexOf('A 페이지 제작')<idx.indexOf('B 부품 작성'));assert.ok(idx.includes('Page Intent'));
const components=idx.match(/^\| C-[A-Z]+ \|/gm)||[];assert.equal(components.length,21,'existing component coverage');
const routes=read('pages/page-types.md');const names=['home','quests','dm','clan','me','clan-my','clan-record','clan-activity','clan-discover','clan-rank','clan-reward','clan-war','clan-shop','clan-detail','rank','explore','daily','friend-profile','book/focus','book/move','book/sleep','clan-create','clan-created','clan-invite'];
assert.equal(names.length,24);for(const name of names)assert.ok(new RegExp('(?:^|[^a-zA-Z0-9/-])'+name.replace('/','\\/')+'(?:[^a-zA-Z0-9/-]|$)').test(routes),'route not classified '+name);
console.log('P1-2/P1-8 PASS: 24 routes and 21 components in new router');
const roles=norms('components/text-hierarchy.md');const type=norms('foundations/typography.md');const geo=norms('foundations/geometry.md');const proc=norms('PROCESS.md');
for(const r of ['Display','Title','Section','Body','Caption'])assert.ok(roles.includes(r));
assert.ok(type.includes('12px')&&type.includes('600 이하'));assert.ok(geo.includes('page.inset'));assert.ok(norms('pages/page-types.md').includes('논글로벌'));assert.ok(read('components/badge-progress.md').includes('10'));
console.log('T3 structural PASS: roles/type/gutter/track have declared owner paths');
const impl=read('implementation/v015.md'), topnav=fs.readFileSync(path.resolve(dir,'../TOP_NAVIGATION.md'),'utf8'),bottom=read('components/bottom-navigation.md'),prog=read('objects/progress.md'),status=read('components/badge-progress.md');
assert.ok(!impl.includes('## K12 웹 작업자 간 고정 인터페이스'),'K12 must be history until P5');
assert.ok(!topnav.includes('K10을 따른다'),'Top Navigation must not point at removed r1 K10');
assert.ok(bottom.includes('[4가지 페이지 유형](../pages/page-types.md)'),'page type owner missing from bottom nav');
assert.ok(!bottom.includes('Bottom Nav 가시성과 Bottom CTA 충돌은 S-PAGE가 소유'),'old bottom nav visibility owner remains');
assert.ok(prog.includes('min(current,target)')&&prog.includes('aria-valuetext'),'over-goal ARIA contradiction not resolved');
assert.ok(status.includes('now1000'),'C-STATUS over-goal bound lost');
assert.ok(read('HISTORY.md').includes('K12 웹 작업자 간 고정 인터페이스'),'K12 history missing');
console.log('T3 known conflict regression PASS: old K10/K12 and over-goal ARIA conflicts removed from active rules');
const ledger=read('LEDGER.md');let coverage=0,p1=0,missing=[],stage={};
for(const row of ledger.split('\n').filter(x=>/^\| L\d{4} \|/.test(x))){
 const a=row.split(' | ');const sec=a[1],owner=a[6],phase=a[7];
 stage[phase]=(stage[phase]||0)+1;
 if(phase==='P1'){p1++;let pathname=(owner||'').split('#')[0];try{let s=read(pathname);assert.ok(s.includes('## 현행 계약'));}catch(e){missing.push({id:a[0],sec,owner,error:String(e.message)});}}
}
assert.ok(p1>100,'expected comprehensive P1 ledger');
for(let n=1;n<=38;n++){const id='L'+String(n).padStart(2,'0');assert.ok(new RegExp('^\\| '+id+' \\| ', 'm').test(ledger),'missing original plan '+id);}
assert.equal(ledger.split('\n').filter(x=>/^\| L\d{4} \|/.test(x)).length,585,'lossless report ledger rows');assert.equal(missing.length,0,'T4 missing owners: '+JSON.stringify(missing.slice(0,5)));
for(const sec of ['3.3','3.4','4.1','4.2','4.3','4.4','4.5','5.1','5.2','5.3','6.1','6.5','6.6',...Array.from({length:21},(_,i)=>'7.'+(i+1)),'8.2','8.3','8.4','8.5','8.6','9.3','9.5','10.2','10.3','A.1','A.2','A.3','A.4']){assert.ok(ledger.includes('| '+sec+' | '),'coverage '+sec);coverage++;}
console.log('T4 ownership/coverage PASS: '+p1+' P1 rows / '+coverage+' sections; stages='+JSON.stringify(stage));
console.log('T2 BLOCKED: an independent new-session design reproduction has not been performed; G-P1 NOT PASSED.');
