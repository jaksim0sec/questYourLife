// Actual Chromium layout, computed style and interaction measurements for P2 fixtures.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const {chromium}=require('playwright');
const base=process.env.PREVIEW_URL||'http://127.0.0.1:8001/';
const folder=path.resolve(process.env.P2_EVIDENCE_DIR||'docs/design/evidence/p2-runtime');
(async()=>{
 fs.mkdirSync(folder,{recursive:true});
 const browser=await chromium.launch({headless:true,args:['--no-sandbox']});const report=[];
 try{
 for(const width of [320,390,430])for(const theme of ['light','dark']){
  const page=await browser.newPage({viewport:{width,height:844},deviceScaleFactor:1});
  const errors=[];page.on('pageerror',e=>errors.push(e.message));
  await page.goto(base+'fixtures/p2.html',{waitUntil:'networkidle'});
  await page.evaluate(t=>document.documentElement.dataset.theme=t,theme);
  assert.equal(await page.locator('.p2-fixture').count(),21,'21 browser fixtures');
  assert.equal(await page.locator('.p2-fixture[data-tier="1"]').count(),9);
  assert.equal(await page.locator('.p2-fixture[data-tier="2"]').count(),8);
  assert.equal(await page.locator('.p2-fixture[data-tier="3"]').count(),4);
  const dom=await page.evaluate(()=>{
   const css=e=>getComputedStyle(e),rect=e=>e.getBoundingClientRect(),el=s=>document.querySelector(s);
   const normal=el('[data-component="C-STATUS"] .p2-good .progress-track'),bad=el('[data-component="C-STATUS"] .p2-bad .progress-track');
   const measure=p=>({fill:rect(p.firstElementChild).width/rect(p).width,barHeight:rect(p).height,now:+p.getAttribute('aria-valuenow')});
   const goodTexts=[...document.querySelectorAll('.p2-good *')].filter(e=>e.childNodes.length&&[...e.childNodes].some(n=>n.nodeType===3&&n.textContent.trim())&&!e.closest('[hidden]'));
   const tooSmall=goodTexts.filter(e=>parseFloat(css(e).fontSize)<11.99).map(e=>({tag:e.tagName,cls:e.className,size:css(e).fontSize,text:e.textContent.slice(0,28)}));
   const action=el('[data-component="C-ACTION"] .p2-good .p2-touch'),icon=el('.p2-good .p2-icon'),wrong=el('.p2-bad .p2-icon-bad');
   return {progress:measure(normal),badProgress:measure(bad),normalImageFit:css(el('.good-image img')).objectFit,badImageFit:css(el('.bad-image img')).objectFit, imagesLoaded:[...document.querySelectorAll('.p2-good img')].map(x=>({src:x.currentSrc,complete:x.complete,width:x.naturalWidth})),
    actionHit:[rect(action).width,rect(action).height],iconHit:[rect(icon).width,rect(icon).height],badIconHit:[rect(wrong).width,rect(wrong).height],
    representativeFont:parseFloat(css(el('.p2-number')).fontSize),badDerivedFont:parseFloat(css(el('.p2-giant-derived')).fontSize),
    minFontIssues:tooSmall,surfaceFlat:el('.p2-good .p2-flat').children.length,badCards:el('.p2-bad .p2-bad-cards').children.length,
    navSelected:el('.p2-good .p2-nav-link[aria-current=page]')?.textContent,goodLinks:[...document.querySelectorAll('.p2-nav-link')].length,
    badGradient:css(el('.p2-bad-cards>div')).backgroundImage,
    badLayerCount:el('.p2-overbadges').querySelectorAll(':scope > span').length,
    normalOverlays:el('.p2-rarity-frame').querySelectorAll(':scope>span').length,
    radioChecked:document.querySelectorAll('.p2-good input[name=p2-scope]:checked').length,
    badRadioChecked:document.querySelectorAll('.p2-bad .p2-radio-bad input:checked').length,
    goodTextRolePresent:!!el('.p2-number'),fixtureScrollWidth:document.documentElement.scrollWidth,viewport:innerWidth};
  });
  assert.ok(Math.abs(dom.progress.fill-.62)<.01,'rendered fill actual '+JSON.stringify(dom.progress));
  assert.ok(Math.abs(dom.badProgress.fill)<.005,'counterexample must show a real blank track');
  assert.equal(dom.progress.now,620);assert.ok(Math.abs(dom.progress.barHeight-10)<.5);
  assert.equal(dom.normalImageFit,'contain');assert.equal(dom.badImageFit,'cover');assert.ok(dom.imagesLoaded.length>=2&&dom.imagesLoaded.every(x=>x.complete&&x.width>0),'normal fixture images must be decoded: '+JSON.stringify(dom.imagesLoaded));
  assert.ok(dom.actionHit[0]>=48&&dom.actionHit[1]>=48,'action target min48');
  assert.ok(dom.iconHit[0]>=48&&dom.iconHit[1]>=48,'icon target min48');
  assert.ok(dom.badIconHit[0]<48&&dom.badIconHit[1]<48,'bad icon target visibly too small');
  assert.ok(dom.representativeFont>dom.badDerivedFont,'Display >= bad derived numeric');
  assert.deepEqual(dom.minFontIssues,[],'no tiny text in normal fixtures');
  assert.ok(dom.badGradient.includes('gradient'),'bad surface visibly overdesigned');
  assert.equal(dom.navSelected,'클랜');assert.equal(dom.goodLinks,5);
  assert.ok(dom.badLayerCount>2&&dom.normalOverlays===0,'bad overlay crowding visible');
  assert.equal(dom.radioChecked,1);assert.equal(dom.badRadioChecked,2);
  assert.ok(dom.fixtureScrollWidth<=width+2,'horizontal overflow '+width);
  const filename='p2-components-'+width+'-'+theme+'.png';
  await page.screenshot({path:path.join(folder,filename),fullPage:true,animations:'disabled'});
  await page.locator('#p2-action-demo').click();assert.equal(await page.locator('#p2-action-output').innerText(),'실행 클릭 확인');
  const sheet=page.locator('#p2-sheet');assert.ok(await sheet.isHidden());await page.locator('#p2-sheet-opener').click();assert.ok(await sheet.isVisible());await page.locator('#p2-sheet-close').click();assert.ok(await sheet.isHidden());
  assert.equal(await page.locator('#p2-results .p2-result:visible').count(),2);
  await page.locator('#p2-filter').click();assert.equal(await page.locator('#p2-results .p2-result:visible').count(),1);
  assert.equal(await page.locator('#p2-filter').getAttribute('aria-pressed'),'true');
  await page.locator('#p2-tab-b').click();assert.equal(await page.locator('#p2-panel-b').innerText(),'멤버 패널');assert.ok(await page.locator('#p2-panel-b').isVisible());assert.ok(await page.locator('#p2-panel-a').isHidden());assert.equal(await page.locator('#p2-tab-b').getAttribute('aria-selected'),'true');assert.equal(await page.locator('#p2-tab-b').getAttribute('aria-controls'),'p2-panel-b');await page.keyboard.press('ArrowLeft');assert.ok(await page.locator('#p2-panel-a').isVisible());
  await page.locator('.p2-segment[data-period="지난 주"]').click();assert.equal(await page.locator('#p2-period-output').innerText(),'지난 주 기록 540');
  await page.locator('#p2-gesture-step').click();assert.equal(await page.locator('#p2-gesture-output').innerText(),'중간');
  await page.locator('#p2-gesture-handle').focus();await page.keyboard.press('ArrowUp');assert.equal(await page.locator('#p2-gesture-output').innerText(),'전체');
  await page.locator('#p2-check-input').check();await page.waitForFunction(()=>document.querySelector('#p2-check-status').dataset.state==='confirmed');
  assert.equal(await page.locator('#p2-check-status').innerText(),'완료 확인됨');
  const radio=page.locator('.p2-good input[name="p2-scope"]');await radio.nth(1).check();assert.equal(await page.locator('.p2-good input[name="p2-scope"]:checked').count(),1);
  assert.deepEqual(errors,[],'page script errors');
  report.push({width,theme,...dom,interaction:'pass',screenshot:filename});
  console.log('P2 Chromium PASS '+width+'/'+theme+' : '+JSON.stringify({ratio:dom.progress.fill,fontIssues:dom.minFontIssues.length,interaction:'pass',image:filename}));
  await page.close();
 }
 }finally{await browser.close();}
 fs.writeFileSync(path.join(folder,'summary.json'),JSON.stringify({head:process.env.GITHUB_SHA||'local',cases:report},null,2));
})().catch(e=>{console.error(e);process.exitCode=1});
