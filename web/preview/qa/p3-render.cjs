// P3 browser verification: real DOM/computed CSS and interactions; before source P0 immutable.
const {chromium}=require('playwright');
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const base=process.env.P3_ORIGIN||'http://127.0.0.1:8001/';
const output=path.resolve('docs/design/evidence/p3');
const baseline=path.resolve('docs/design/evidence/baseline');
fs.mkdirSync(output,{recursive:true});
const cases=[['clan-joined','clan',true],['clan-guest','clan',false],['clan-my','clan-my',true],['clan-reward','clan-reward',true],['clan-record','clan-record',true]];
async function guest(page){
 const menu=page.locator('[data-sheet="menu"]').first();assert.ok(await menu.count(),'menu missing');
 await menu.click();
 const opt=page.locator('[data-clan-view="guest"]');assert.ok(await opt.count(),'guest toggle missing');await opt.click();
 assert.equal(await page.locator('.clan-guest-hero').count(),1,'guest render absent');
}
(async()=>{
 const browser=await chromium.launch({headless:true,args:['--no-sandbox']});
 const matrix=[];
 try{
 for(const [label,route,joined] of cases)for(const width of [320,360,390,430])for(const theme of ['light','dark']){
  const page=await browser.newPage({viewport:{width,height:844},deviceScaleFactor:1});
  const errors=[];page.on('pageerror',e=>errors.push(e.message));
  const response=await page.goto(base+'#'+route,{waitUntil:'load'});assert.equal(response.status(),200,'HTTP');
  await page.evaluate(theme=>{document.documentElement.dataset.theme=theme},theme);
  if(!joined)await guest(page);
  await page.evaluate(async()=>{await Promise.all([...document.querySelectorAll('#main img')].map(async e=>{e.loading='eager';try{await e.decode()}catch(_){}}));await document.fonts.ready});
  const m=await page.evaluate(()=>{
    const main=document.getElementById('main'),nav=document.getElementById('nav');
    const fonts=[...document.querySelectorAll('.clan-layout h1,.clan-layout h2,.clan-layout h3,.clan-layout p,.clan-layout a,.clan-layout button,.clan-layout span,.clan-layout strong,.clan-layout small,.clan-layout time,#nav .nav-link')].filter(e=>e.getClientRects().length && getComputedStyle(e).display!=='none' && getComputedStyle(e).visibility!=='hidden').map(e=>({text:e.textContent.trim().slice(0,23),size:parseFloat(getComputedStyle(e).fontSize),weight:parseInt(getComputedStyle(e).fontWeight)||400})).filter(e=>e.text);
    const targets=[...document.querySelectorAll('#main .clan-layout a,#main .clan-layout button,#header a,#header button,#nav a')].filter(e=>e.getClientRects().length).map(e=>{const r=e.getBoundingClientRect();return{text:e.textContent.trim().slice(0,25),width:r.width,height:r.height}});
    const progress=[...main.querySelectorAll('.progress-track')].map(e=>({width:e.getBoundingClientRect().width,fill:e.firstElementChild?.getBoundingClientRect().width||0,height:e.getBoundingClientRect().height,now:e.getAttribute('aria-valuenow'),max:e.getAttribute('aria-valuemax'),text:e.getAttribute('aria-valuetext')}));
    const rect=sel=>{let e=main.querySelector(sel);if(!e)return null;let r=e.getBoundingClientRect();return{top:r.top,bottom:r.bottom,left:r.left,right:r.right}};
    const images=[...main.querySelectorAll('img')].map(e=>({src:e.getAttribute('src'),loaded:e.complete&&e.naturalWidth>0}));
    return{route:location.hash,layer:document.body.dataset.layer,navDisplay:getComputedStyle(nav).display,navActive:nav.querySelector('[aria-current=page]')?.getAttribute('href'),overflow:document.documentElement.scrollWidth-innerWidth,fonts,targets,progress,images,heroCount:main.querySelectorAll('.clan-reward-hero').length,itemCount:main.querySelectorAll('.clan-preview-item').length,cheerCount:main.querySelectorAll('[data-clan-heart]').length,joinedRecord:rect('.clan-record-home .clan-progress-meta strong'),firstFeed:rect('.clan-social-activity-list .clan-feed-item'),identity:rect('.clan-top-section'),sixCount:(main.textContent.match(/6명/g)||[]).length,myTabs:main.querySelectorAll('[data-clan-tab]').length,rootText:main.innerText.slice(0,700)};
  });
  assert.equal(errors.length,0,label+' JS errors '+JSON.stringify(errors));
  assert.ok(m.overflow<=1,label+' overflow '+width+'/'+theme+': '+m.overflow);
  assert.notEqual(m.navDisplay,'none',label+' bottom nav hidden');
  assert.equal(m.navActive,'#clan',label+' active nav');
  const badFonts=m.fonts.filter(t=>t.size<11.98||(t.size<13.98&&t.weight>600));
  assert.equal(badFonts.length,0,label+' font mismatch '+width+'/'+theme+' '+JSON.stringify(badFonts.slice(0,18)));
  assert.ok(m.targets.length>0,label+' no interactive targets inspected');
  const badTargets=m.targets.filter(t=>t.width<47.8||t.height<47.8);
  assert.equal(badTargets.length,0,label+' bad hitboxes '+width+'/'+theme+' '+JSON.stringify(badTargets.slice(0,18)));
  const badImgs=m.images.filter(e=>!e.loaded);
  assert.equal(badImgs.length,0,label+' broken assets '+JSON.stringify(badImgs.slice(0,18)));
  assert.equal(m.heroCount,0,label+' old reward hero remains');
  for(const p of m.progress){
    assert.ok(p.width>0&&Math.abs(p.fill/p.width-.62)<=.01,label+' track/fill '+JSON.stringify(p));
    assert.ok(Math.abs(p.height-10)<.6,label+' track height '+p.height);
    assert.equal(p.now,'620');assert.equal(p.max,'1000');
  }
  if(label==='clan-joined'){
    assert.ok(m.identity&&m.joinedRecord&&m.firstFeed,'joined core missing');
    if(width===390)assert.ok(m.joinedRecord.bottom<844&&m.firstFeed.bottom<844,'G5 first viewport fail '+JSON.stringify({record:m.joinedRecord,feed:m.firstFeed}));
    assert.equal(m.sixCount,1,'G2 count repeated '+m.sixCount);
    assert.ok(m.cheerCount>=2,'cheer row missing');assert.equal(m.itemCount,0,'home has reward items');
  }else if(label==='clan-guest')assert.ok(m.rootText.includes('클랜 만들기'),'guest CTA missing');
  else if(label==='clan-reward'){
    assert.equal(m.itemCount,3,'preview item count');
    assert.ok(m.rootText.includes('블루 캡')&&m.rootText.includes('조건 미확정'),'preview item state missing');
  }else if(label==='clan-my')assert.equal(m.myTabs,3,'existing tabs lost');
  else if(label==='clan-record')assert.ok(m.progress.length&&m.rootText.includes('12위'),'record values lost');
  const image=label+'-'+width+'-'+theme+'.png';
  assert.ok(fs.existsSync(path.join(baseline,image)),'P0 reference not found '+image);
  await page.screenshot({path:path.join(output,image),fullPage:true});
  const r={label,route,joined,width,theme,image,before:image,nav:m.navActive,layer:m.layer,fontsMin:Math.min(...m.fonts.map(t=>t.size)),targetMinWidth:Math.min(...m.targets.map(t=>t.width)),targetMinHeight:Math.min(...m.targets.map(t=>t.height)),progress:m.progress,firstFeedBottom:m.firstFeed?.bottom||null,recordBottom:m.joinedRecord?.bottom||null,sixCount:m.sixCount,loadedImages:m.images.length};
  matrix.push(r);console.log('P3 PASS '+label+' '+width+'/'+theme+' font '+r.fontsMin+' targetH '+r.targetMinHeight);
  await page.close();
 }
 const page=await browser.newPage({viewport:{width:390,height:844}});
 const errs=[];page.on('pageerror',e=>errs.push(e.message));
 await page.goto(base+'#clan');
 const cheer=page.locator('[data-clan-heart]').first();assert.ok(await cheer.count(),'cheer absent');
 const before=await cheer.getAttribute('aria-pressed');await cheer.click();assert.notEqual(await cheer.getAttribute('aria-pressed'),before,'reaction toggle');
 await page.locator('.clan-inline-invite').click();assert.equal(new URL(page.url()).hash,'#clan-invite','invite navigation');
 await page.goto(base+'#clan-my');
 assert.equal(await page.locator('[data-clan-tab]').count(),3,'my tab count');
 await page.locator('[data-clan-tab="record"]').click();
 assert.ok((await page.locator('#clan-tabpanel').innerText()).includes('620'),'my record panel');
 await page.locator('[data-clan-tab="activity"]').focus();
 await page.keyboard.press('ArrowLeft');
 assert.equal(await page.locator('[data-clan-tab="record"]').getAttribute('aria-selected'),'true','keyboard tab select');
 await page.goto(base+'#clan-reward');await page.locator('[data-back]').first().click();
 assert.equal(new URL(page.url()).hash,'#clan-my','Back must honor actual entry history from clan-my');
 const deep=await browser.newPage({viewport:{width:390,height:844}});
 await deep.goto(base+'#clan-reward');await deep.locator('[data-back]').first().click();
 assert.equal(new URL(deep.url()).hash,'#clan','Direct deep link Back defaults to clan parent');
 await deep.close();
 await page.goto(base+'#clan-create');
 assert.ok(await page.locator('#clan-name').count(),'create form missing');
 await page.locator('#clan-name').fill('우리 테스트 클랜');
 await page.locator('#clan-create-form button[type=submit]').click();
 assert.equal(new URL(page.url()).hash,'#clan-created','create form flow');
 assert.equal(errs.length,0,'interaction page errors '+errs.join(';'));
 await page.close();
 const d=await browser.newPage({viewport:{width:390,height:844}});
 await d.goto(base+'qa/p3-dressing.html');
 assert.equal(await d.locator('[data-context]').count(),2,'dressing contexts');
 const srcs=await d.locator('.dress-media img').evaluateAll(els=>els.map(e=>e.getAttribute('src')));
 assert.equal(srcs[0],srcs[1],'same blue cap asset required');
 assert.ok((await d.locator('[data-context=reward-preview]').innerText()).includes('획득 예정'));
 assert.ok(!(await d.locator('[data-context=reward-preview]').innerText()).includes('장착 중'));
 assert.ok((await d.locator('[data-context=wardrobe]').innerText()).includes('장착 중'));
 assert.ok(await d.locator('[data-context=wardrobe] button').isDisabled(),'equip must be disabled');
 assert.equal(await d.locator('#nav').count(),0,'non-global bottom nav absent');
 for(const width of [320,390,430])for(const theme of ['light','dark']){
   await d.setViewportSize({width,height:844});
   await d.evaluate(theme=>document.documentElement.dataset.theme=theme,theme);
   const check=await d.evaluate(()=>{
      const nodes=[...document.querySelectorAll('.dress-shell h1,.dress-shell h2,.dress-shell p,.dress-shell strong,.dress-shell small,.dress-shell span,.dress-shell button,.dress-header a')];
      const fonts=nodes.filter(e=>e.getClientRects().length).map(e=>({text:e.textContent.trim().slice(0,25),size:parseFloat(getComputedStyle(e).fontSize),weight:parseInt(getComputedStyle(e).fontWeight)||400}));
      return{overflow:document.documentElement.scrollWidth-innerWidth,fonts,assetOk:[...document.querySelectorAll('.dress-media img')].every(e=>e.complete&&e.naturalWidth>0)};
   });
   assert.ok(check.overflow<=1,'dressing overflow '+width+'/'+theme+' '+check.overflow);
   assert.ok(check.assetOk,'dressing cap image load');
   const f=check.fonts.filter(t=>t.size<11.98||(t.size<13.98&&t.weight>600));
   assert.equal(f.length,0,'dressing typography '+JSON.stringify(f));
   await d.screenshot({path:path.join(output,'dressing-'+width+'-'+theme+'.png'),fullPage:true});
 }
 await d.close();
 assert.equal(matrix.length,40);
 fs.writeFileSync(path.join(output,'manifest.json'),JSON.stringify({status:'passed',sourceBranch:'seed-r3-p2p3',baselineSha:'3e59cdac4594a552aaa6bf903a7935ef71eebf21',capturedAt:new Date().toISOString(),captureCount:matrix.length,dressingCount:6,matrix},null,2)+'\n');
 const md=['# P3 Chromium Before/After 실제 화면 비교','','Before P0 기준 커밋: new@3e59cda. 사용자 원본 레퍼런스 이미지 4/7/8/9는 현 브랜치에 없어 3열 시각 판정에서 미검증.','', '| 조건 | Before P0 | After P3 |','|---|---|---|'];
 for(const m of matrix.filter(x=>x.width===390))md.push('| '+m.label+' '+m.width+' '+m.theme+' | [PNG](../baseline/'+m.before+') | [PNG]('+m.image+') |');
 md.push('','- 전체 40개 근거: [manifest.json](manifest.json).','- 꾸미기 맥락: [라이트](dressing-390-light.png), [다크](dressing-390-dark.png).');
 fs.writeFileSync(path.join(output,'COMPARISON.md'),md.join('\n')+'\n');
 console.log('P3 PASS: 40 browser screenshots and functional regressions, 6 item-context comparisons');
 }finally{await browser.close()}
})().catch(e=>{console.error('P3 QA FAILURE',e);process.exitCode=1});
