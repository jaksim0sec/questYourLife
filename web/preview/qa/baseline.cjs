// P0-5: capture the immutable 'new' branch baseline, not the in-progress r3 layout.
const {chromium}=require('playwright');const fs=require('node:fs');const path=require('node:path');
const assert=require('node:assert/strict');
(async()=>{
 const url=process.env.BASELINE_URL||'http://127.0.0.1:8000/';
 const dir=process.env.BASELINE_OUTPUT||'docs/design/evidence/baseline';
 const browser=await chromium.launch({headless:true,args:['--no-sandbox']});
 const cases=[['clan-joined','clan',true],['clan-guest','clan',false],['clan-my','clan-my',true],['clan-reward','clan-reward',true],['clan-record','clan-record',true]];
 let captures=[];
 for(const [label,route,joined] of cases)for(const width of [320,360,390,430])for(const theme of ['light','dark']){
   const page=await browser.newPage({viewport:{width,height:844},deviceScaleFactor:1});
   const errors=[];page.on('pageerror',e=>errors.push(e.message));
   await page.goto(url+'#'+route,{waitUntil:'load'});
   await page.evaluate(theme=>document.documentElement.dataset.theme=theme,theme);
   if(!joined){await page.locator('[data-sheet="menu"]').first().click();await page.locator('[data-clan-view="guest"]').click();await page.locator('.clan-guest-hero').waitFor();}
   await page.evaluate(()=>document.fonts.ready);
   await page.evaluate(()=>Promise.all([...document.images].map(async i=>{i.loading='eager';try{await i.decode()}catch{}})));
   const file=path.join(dir,label+'-'+width+'-'+theme+'.png');
   fs.mkdirSync(path.dirname(file),{recursive:true});
   await page.screenshot({path:file,fullPage:true,animations:'disabled'});
   assert.equal(errors.length,0,'page errors '+label+'/'+width+'/'+theme+': '+errors.join(';'));
   captures.push({label,route,width,theme,file,source:'new',errors});
   await page.close();
 }
 assert.equal(captures.length,40,'must capture 40 distinct baseline cases');
 fs.writeFileSync(path.join(dir,'manifest.json'),JSON.stringify({source_ref:'new',source_sha:'3e59cdac4594a552aaa6bf903a7935ef71eebf21',capture_count:captures.length,captures},null,2));
 console.log('P0-5 baseline captures: '+captures.length);
 await browser.close();
})().catch(e=>{console.error(e);process.exitCode=1});
