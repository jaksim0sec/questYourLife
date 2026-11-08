// Run with Playwright available in NODE_PATH; BASE_URL selects the served preview.
const { chromium } = require('playwright');
const assert = require('node:assert/strict');
(async () => {
  const browser = await chromium.launch({headless:true, executablePath:process.env.CHROME_PATH || undefined, args:['--no-sandbox']});
  const page = await browser.newPage({viewport:{width:390,height:844}});
  await page.goto((process.env.BASE_URL || 'http://localhost:8020/source/web/preview/')+'#clan');
  const positions=await page.locator('.tools .icon-btn').evaluateAll(buttons=>buttons.map(button=>{
    const a=button.getBoundingClientRect(), b=button.querySelector('.glyph').getBoundingClientRect();
    return {dx:Math.abs(a.x+a.width/2-b.x-b.width/2),dy:Math.abs(a.y+a.height/2-b.y-b.height/2),width:a.width,height:a.height};
  }));
  await browser.close();
  assert.ok(positions.length===2,'clan root retains two header actions');
  assert.ok(positions.every(p=>p.dx<.5&&p.dy<.5),'visible glyph must stay centered in its own touch target: '+JSON.stringify(positions));
  assert.ok(positions.every(p=>p.width>=48&&p.height>=48),'minimum touch dimensions');
  console.log('PASS header centers and touch targets');
})().catch(e=>{console.error(e);process.exitCode=1});
