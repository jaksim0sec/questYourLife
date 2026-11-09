// P0-4: do not report green without measuring the browser's rendered fill.
const {chromium}=require('playwright');const assert=require('node:assert/strict');
(async()=>{const b=await chromium.launch({headless:true,args:['--no-sandbox']});let cases=[];
 for(const width of [320,390,430])for(const theme of ['light','dark']){
  const p=await b.newPage({viewport:{width,height:844}});await p.goto((process.env.PREVIEW_URL||'http://127.0.0.1:8001/')+'#clan');
  await p.evaluate(theme=>document.documentElement.dataset.theme=theme,theme);
  const data=await p.locator('.clan-reward-section .progress-track').first().evaluate(e=>({aria:Number(e.getAttribute('aria-valuenow')),max:Number(e.getAttribute('aria-valuemax')),track:e.getBoundingClientRect().width,fill:e.firstElementChild.getBoundingClientRect().width,height:e.getBoundingClientRect().height}));
  const ratio=data.fill/data.track;
  assert.ok(Number.isFinite(ratio)&&Math.abs(ratio-0.62)<=0.01,'P0-4 fill '+width+'/'+theme+': '+JSON.stringify(data));
  assert.equal(data.aria,620,'aria current');assert.equal(data.max,1000,'aria max');
  assert.ok(Math.abs(data.height-10)<0.5,'default track height 10px '+data.height);
  cases.push({width,theme,ratio,aria:data.aria,height:data.height});await p.close();
 }
 await b.close();console.log('P0-4 PASS '+JSON.stringify(cases));
})().catch(e=>{console.error(e);process.exitCode=1});
