const {chromium}=require('playwright');
const fs=require('node:fs');
const base=process.env.QA_ORIGIN || 'http://127.0.0.1:8025';
const preview=process.env.BASE_URL || base+'/source/web/preview/';
const output=process.env.QA_OUTPUT_DIR || 'qa-output';fs.mkdirSync(output,{recursive:true});
const results=[], errors=[];
const check=(name,ok,detail='')=>{results.push({name,ok,detail});if(!ok)console.log('FAIL',name,detail)};
(async()=>{
 const b=await chromium.launch({executablePath:process.env.CHROME_PATH || undefined,headless:true,args:['--no-sandbox']});
 const p=await b.newPage();p.on('pageerror',e=>errors.push(e.message));
 async function fonts(){if(!process.env.QA_FONT_BASE)return;for(const w of [400,500,600,700,800])await p.addStyleTag({url:process.env.QA_FONT_BASE+'/'+w+'.css'});await p.evaluate(()=>document.fonts.ready)}
 async function goto(route,theme='dark',width=390,scale=1,joined=true){
  await p.setViewportSize({width,height:844});await p.goto(preview+'?qa='+Date.now()+'#'+route);await fonts();
  await p.evaluate(({theme,scale})=>{document.documentElement.dataset.theme=theme;if(scale!==1)document.documentElement.style.fontSize=(parseFloat(getComputedStyle(document.documentElement).fontSize)*scale)+'px'}, {theme,scale});
  if(!joined){await p.locator('[data-sheet="menu"]').click();await p.locator('[data-clan-view="guest"]').click();}
  await p.evaluate(()=>Promise.all([...document.images].map(i=>{i.loading='eager';return i.decode().catch(()=>{})})));
 }
 for(const width of [320,390,430,768])for(const theme of ['light','dark'])for(const joined of [true,false])for(const scale of [1,2]){
  await goto('clan',theme,width,scale,joined);
  const label=`${width}/${theme}/${joined?'joined':'guest'}/${scale*100}%`;
  if(width===320&&scale===2&&!joined){check('enlarged ranking readable width '+theme,await p.locator('.clan-rank-info').first().evaluate(e=>e.getBoundingClientRect().width>=120));}const overflow=await p.evaluate(()=>document.documentElement.scrollWidth-innerWidth);
  check('root overflow '+label,overflow<=1,overflow);
  check('images '+label,await p.evaluate(()=>[...document.images].every(i=>i.complete&&i.naturalWidth>0)));
  check('header centered '+label,await p.locator('.tools .icon-btn').evaluateAll(a=>a.every(e=>{const x=e.getBoundingClientRect(),y=e.querySelector('.glyph').getBoundingClientRect();return Math.abs(x.x+x.width/2-y.x-y.width/2)<.5})));
  if(scale===1&&joined){await p.screenshot({path:`${output}/after-${width}-${theme}.png`,fullPage:true});if(width===390)await p.screenshot({path:`${output}/after-viewport-${theme}.png`});}
  if(width===320&&scale===2&&joined&&theme==='dark'){check('enlarged feed readable width',await p.locator('.clan-feed-content').first().evaluate(e=>e.getBoundingClientRect().width>=120));}if(width===320&&scale===2&&joined&&theme==='dark')await p.screenshot({path:output+'/after-enlarged.png',fullPage:true});
 }
 const routes=['clan-create','clan-created','clan-my','clan-record','clan-activity','clan-discover','clan-rank','clan-detail','clan-reward','clan-invite','clan-shop','clan-war'];
 for(const route of routes)for(const theme of ['light','dark']){
  await goto(route,theme,320);
  check('route overflow '+route+'/'+theme,await p.evaluate(()=>document.documentElement.scrollWidth<=innerWidth+1));
  check('detail nav hidden '+route+'/'+theme,await p.locator('#nav').isHidden());
 }
 await goto('clan');await p.locator('.clan-main-identity').click();
 check('identity route',p.url().endsWith('#clan-my'));
 await p.locator('[data-clan-tab="record"]').click();
 check('record tab',await p.locator('.clan-record-metrics').count()===1&&p.url().endsWith('#clan-my'));
 await p.locator('[data-clan-tab="record"]').press('ArrowRight');
 check('keyboard local tab',await p.locator('[data-clan-tab="activity"]').getAttribute('aria-selected')==='true');
 await goto('clan-discover');await p.locator('[data-clan-filter]').last().click();
 check('filter toggles',await p.locator('[data-clan-filter]').last().getAttribute('aria-pressed')==='true');
 await goto('clan');const heart=p.locator('[data-clan-heart]').first();await heart.click();check('reaction toggles',await heart.getAttribute('aria-pressed')==='true');
 await goto('clan');await p.locator('[data-sheet="menu"]').click();await p.locator('[data-theme-switch="light"]').click();
 check('theme switches',await p.evaluate(()=>document.documentElement.dataset.theme==='light'));
 check('theme chrome matches shell',await p.evaluate(()=>document.querySelector('meta[name="theme-color"]').content===getComputedStyle(document.documentElement).getPropertyValue('--shell-surface').trim()));
 await p.keyboard.press('Escape');check('menu escape closes',await p.locator('#sheet').evaluate(e=>!e.open));
 await goto('clan-create');await p.locator('#clan-name').fill('가나다라마바사아자차카타파하우리들의클랜');
 await p.locator('#clan-name').fill('가나다라마바사아자차카타파하우리클랜');
 await p.locator('[data-clan-color]').last().click();await p.locator('[type="submit"]').click();
 check('create demo route',p.url().endsWith('#clan-created'));
 await p.locator('[data-clan-activate]').click();await p.locator('.clan-root').waitFor();
 check('long name root overflow',await p.evaluate(()=>document.documentElement.scrollWidth<=innerWidth+1));
 await p.screenshot({path:output+'/after-long-name.png',fullPage:true});
 await goto('clan','dark',390);await p.screenshot({path:output+'/after-390.png',fullPage:true});await p.screenshot({path:output+'/after-viewport.png'});
 check('runtime errors',errors.length===0,errors);
 fs.writeFileSync(output+'/verification.json',JSON.stringify({results,errors,passed:results.filter(x=>x.ok).length,failed:results.filter(x=>!x.ok).length},null,2));
 console.log(JSON.stringify({passed:results.filter(x=>x.ok).length,failed:results.filter(x=>!x.ok).length,errors}));await b.close();
})().catch(e=>{console.error(e);process.exitCode=1});
