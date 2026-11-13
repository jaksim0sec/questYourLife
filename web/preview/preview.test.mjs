import test from 'node:test';
import assert from 'node:assert/strict';
import {existsSync,readFileSync} from 'node:fs';
import {createStore,clanTotal} from './store.mjs';
import {readRoute,navMarkup} from './routes.mjs';
import {renderPage,renderHomeAlerts,searchResults} from './views.mjs';
import {applyPageType,WEB_NAV_ITEMS} from '../../seed/runtime/seed-page-contract.mjs';
import {composeMascotAvatar} from '../../seed/runtime/seed-mascot-avatar.mjs';

const ui={quest:'all',shop:'전체',period:'current'};
const memory=()=>{
  const data=new Map();
  return {getItem:key=>data.get(key),setItem:(key,value)=>data.set(key,value)};
};
function page(store,hash,state=ui) {
  return renderPage(store.read(),readRoute(hash),state);
}

test('SEED routes keep the originating tab on details and hide navigation in standalone screens',()=>{
  const modes=new Map([
    ['global',WEB_NAV_ITEMS.map(item=>'#/'+item.key)],
    ['global-inner',['#/quest/run?tab=home','#/book/math','#/shop','#/ranking']],
    ['non-global',['#/search','#/chat/ai','#/chat/clan?tab=clan','#/item/1','#/decorate','#/rewards']],
  ]);
  for(const [mode,paths] of modes)for(const hash of paths) {
    const route=readRoute(hash);
    const shell={dataset:{}};
    const nav={hidden:false};
    const bottom={hidden:false};
    const header={dataset:{}};
    shell.querySelector=selector=>({'.seed-bottom-nav':nav,'.seed-page-shell__bottom':bottom,'.seed-top-nav':header}[selector]);
    assert.equal(route.type,mode);
    applyPageType(shell,route.type);
    assert.equal(nav.hidden,mode==='non-global');
    assert.equal(bottom.hidden,nav.hidden);
    assert.equal(header.dataset.navRole,mode==='global'?'root':'standard');
  }
  assert.equal(readRoute('#/quest/run?tab=home').tab,'home');
  assert.equal(readRoute('#/quest/run?tab=clan').tab,'quests');
  assert.equal(readRoute('#/chat/clan?tab=clan').tab,'clan');
  assert.equal(readRoute('#/quest').name,'home');
  assert.equal(readRoute('#/bad/path').name,'home');
  assert.equal((navMarkup().match(/data-nav-key=/g)||[]).length,5);
});

test('accept, defer, finish and undo share one quest and pay exactly once',()=>{
  const storage=memory();
  const store=createStore(storage);
  assert.match(page(store,'#/home'),/달리기 기록 0.5초 줄이기/);
  assert.match(page(store,'#/quest/run?tab=home'),/수락하기/);
  assert.equal(store.dispatch('complete','run'),false);
  assert.equal(store.dispatch('accept','run'),true);
  assert.equal(store.dispatch('defer','run'),true);
  assert.match(page(store,'#/quest/run'),/다시 시작/);
  assert.equal(store.dispatch('resume','run'),true);
  assert.equal(store.dispatch('complete','run'),true);
  assert.equal(store.dispatch('complete','run'),false);
  assert.equal(store.read().cash,680);
  assert.equal(store.read().xp,810);
  assert.equal(clanTotal(store.read()),1200);
  assert.equal(clanTotal(store.read(),'previous'),1230);
  assert.match(page(store,'#/clan'),/상자 받기/);
  assert.match(page(store,'#/quests',{...ui,quest:'done'}),/달리기 기록/);
  assert.equal(createStore(storage).read().cash,680);
  assert.equal(store.dispatch('undo','run'),true);
  assert.equal(store.read().cash,640);
  assert.equal(clanTotal(store.read()),1120);
});

test('questbook progress and clan rewards react to actual state changes',()=>{
  const store=createStore();
  assert.match(page(store,'#/book/math'),/aria-valuenow="1"/);
  assert.equal(store.dispatch('claim','clan'),false);
  assert.equal(store.dispatch('complete','math-today'),true);
  assert.match(page(store,'#/book/math'),/aria-valuenow="2"/);
  assert.equal(store.dispatch('complete','math-main'),true);
  assert.equal(store.dispatch('claim','clan'),true);
  assert.equal(store.dispatch('claim','clan'),false);
  assert.match(page(store,'#/rewards'),/주간 클랜 상자/);
  const prior=store.read().cash;
  assert.equal(store.dispatch('open','clan'),true);
  assert.equal(store.dispatch('open','clan'),false);
  assert.equal(store.read().cash,prior+260);
  assert.match(page(store,'#/rewards'),/버킷 햇 획득/);
});

test('chat creates and revises a real mock quest, with safe user text and shared clan conversation',()=>{
  const store=createStore();
  assert.equal(store.dispatch('message','ai','달리기 기록을 줄이고 싶어'),true);
  const first=store.read().quests[0].id;
  assert.equal(store.dispatch('message','ai','일단 유지하자'),true);
  assert.equal(store.read().quests.find(q=>q.id===first).status,'rejected');
  const second=store.read().quests[0];
  assert.match(second.title,/기록 유지하기/);
  assert.match(page(store,'#/chat/ai'),new RegExp('#/quest/'+second.id));
  assert.equal(store.dispatch('accept',second.id),true);
  assert.equal(store.dispatch('message','clan','<script>alert(1)</script>'),true);
  assert.match(page(store,'#/chat/clan?tab=clan'),/&lt;script&gt;/);
  assert.doesNotMatch(page(store,'#/chat/clan?tab=dm'),/<script>/);
});

test('bonus proof and reactions persist, and bonus cash never raises clan contribution',()=>{
  const store=createStore();
  const total=clanTotal(store.read());
  store.dispatch('accept','bonus');
  assert.equal(store.dispatch('complete','bonus','   '),false);
  assert.equal(store.dispatch('complete','bonus','문구점 앞에서 찾았어'),true);
  assert.equal(store.read().cash,720);
  assert.equal(clanTotal(store.read()),total);
  assert.match(page(store,'#/quest/bonus'),/문구점 앞에서 찾았어/);
  assert.equal(store.dispatch('cheer','0'),true);
  assert.equal(store.read().bonusPosts[0].count,4);
  assert.equal(store.dispatch('cheer','1'),false);
  assert.equal(store.dispatch('cheer','0'),true);
  assert.equal(store.read().bonusPosts[0].count,3);
});

test('cash purchase and loot feed real SEED avatar composition with one item per slot',()=>{
  const store=createStore();
  assert.equal(store.dispatch('purchase','1001'),false);
  assert.match(page(store,'#/item/1001'),/캐시가 부족해요/);
  assert.equal(store.dispatch('purchase','1'),true);
  assert.equal(store.dispatch('purchase','1'),false);
  assert.equal(store.read().cash,160);
  assert.equal(store.dispatch('equip','1'),true);
  assert.equal(store.read().equipped.includes(2),false);
  assert.equal(store.dispatch('open','starter'),true);
  assert.equal(store.dispatch('equip','1004'),true);
  assert.deepEqual(new Set(composeMascotAvatar(store.read().equipped).map(item=>item.slot)),new Set(['identity','headwear','earwear']));
  assert.match(page(store,'#/decorate'),/aria-pressed="true"/);
});

test('every page emits real links and available assets, and uses the shared SEED sheet-free shell',()=>{
  const store=createStore();
  const routes=['home','quests','dm','clan','me','quest/run','quest/bonus','book/math','ranking','shop','item/1','chat/ai','chat/clan','chat/friend','decorate','rewards'];
  const markup=routes.map(route=>page(store,'#/'+route)).join('')+navMarkup();
  for(const [,href] of markup.matchAll(/href="(#[^"]+)"/g)) {
    const route=readRoute(href);
    assert.ok(route.name!=='home'||href==='#/home',href);
  }
  for(const [,resource] of markup.matchAll(/(?:src="|url\(')(\.\.\/\.\.\/assets\/[^"']+)/g)) {
    assert.ok(existsSync(new URL(resource,import.meta.url)),resource);
  }
  const index=readFileSync(new URL('./index.html',import.meta.url),'utf8');
  assert.match(index,/seed\/styles\/seed-standard\.css/);
  assert.match(index,/seed-top-nav__inner/);
  assert.doesNotMatch(markup,/<dialog/);
  assert.match(page(store,'#/shop'),/data-item-layout="horizontal"/);
  assert.doesNotMatch(page(store,'#/shop',{...ui,shop:'모자'}),/크림 헤드폰/);
  assert.match(page(store,'#/clan',{...ui,clanView:'insights',period:'previous'}),/1,230/);
});

test('app event wiring changes screens, preserves parent nav and updates balances',async()=>{
  const listeners={};
  const navItems=WEB_NAV_ITEMS.map(item=>{
    const attrs=new Map();
    const glyph={style:{setProperty(){}}};
    return {dataset:{navKey:item.key},attrs,querySelector:()=>glyph,
      setAttribute:(key,value)=>attrs.set(key,value),removeAttribute:key=>attrs.delete(key)};
  });
  const node=()=>({dataset:{},focus(){},querySelectorAll:()=>[],replaceChildren(){this.innerHTML='';}});
  const nav={...node(),querySelectorAll:()=>navItems};
  const main=node(),head=node(),header=node(),bottom=node(),leading=node(),trailing=node();
  const root={dataset:{},scrollHeight:400};
  const alertSheet={open:false,showModal(){this.open=true;},close(){this.open=false;}};
  const shell={...node(),querySelector:selector=>({'.seed-top-nav':head,'.seed-bottom-nav':nav,'.seed-page-shell__bottom':bottom}[selector])};
  const elements={
    '.preview-shell':shell,'.seed-bottom-nav':nav,'#main':main,
    '.seed-top-nav__inner':header,'.seed-top-nav__leading':leading,
    '.seed-top-nav__trailing':trailing,'.preview-notice':node(),'.seed-top-nav__title':node(),
    '#home-alerts':alertSheet,'#home-alert-list':node(),'#home-search':node(),'#home-search-results':node(),
  };
  const location={hash:'#/home'};
  const historyEntries=[];
  const stateHistory={
    replaceState(state,_title,hash){historyEntries.splice(0,1,{state,hash});location.hash=hash;},
    pushState(state,_title,hash){historyEntries.push({state,hash});location.hash=hash;},
    back(){historyEntries.pop();const prior=historyEntries.at(-1);location.hash=prior.hash;listeners.popstate({state:prior.state});},
  };
  const globals={
    document:{documentElement:root,querySelector:key=>elements[key],addEventListener:(type,fn)=>listeners[type]=fn},
    location,history:stateHistory,
    window:{localStorage:memory(),scrollY:0,scrollTo(){},addEventListener:(type,fn)=>listeners[type]=fn},
    setTimeout:()=>undefined,
  };
  const originals=Object.fromEntries(Object.keys(globals).map(key=>[key,Object.getOwnPropertyDescriptor(globalThis,key)]));
  try {
    for(const [key,value] of Object.entries(globals)) Object.defineProperty(globalThis,key,{value,configurable:true});
    await import('./app.mjs');
    const click=({href=null,...dataset})=>listeners.click({target:{closest:()=>({dataset,getAttribute:()=>href})},preventDefault(){}});
    const active=()=>navItems.find(item=>item.attrs.has('aria-current'))?.dataset.navKey;
    assert.match(main.innerHTML,/일일 퀘스트 제안/);
    assert.equal(active(),'home');
    assert.match(trailing.innerHTML,/data-shell-action="search"/);
    assert.match(trailing.innerHTML,/data-shell-action="alerts"/);
    assert.match(trailing.innerHTML,/<summary[^>]+aria-label="메뉴"/);
    click({shellAction:'search'});
    assert.equal(shell.dataset.pageType,'non-global');
    listeners.input({target:{id:'home-search',value:'수학'}});
    assert.match(elements['#home-search-results'].innerHTML,/#\/book\/math/);
    click({shellAction:'back'});
    click({shellAction:'alerts'});
    assert.equal(alertSheet.open,true);
    assert.equal(active(),'home');
    assert.match(elements['#home-alert-list'].innerHTML,/data-alert-id="daily"/);
    click({href:'#/quest/run?tab=home',alertId:'daily'});
    assert.equal(alertSheet.open,false);
    assert.equal(shell.dataset.pageType,'global-inner');
    assert.equal(header.dataset.navRole,'standard');
    assert.equal(active(),'home');
    click({action:'accept',id:'run'});
    assert.match(main.innerHTML,/완료하기/);
    click({action:'complete',id:'run'});
    assert.match(main.innerHTML,/완료 취소/);
    click({href:'#/me'});
    assert.match(trailing.innerHTML,/680/);
    click({href:'#/decorate?tab=me'});
    assert.equal(nav.hidden,true);
    click({shellAction:'back'});
    assert.equal(shell.dataset.pageType,'global');
    assert.equal(active(),'me');
    click({href:'#/shop'});
    click({filter:'shop',value:'모자'});
    assert.doesNotMatch(main.innerHTML,/크림 헤드폰/);
    assert.match(main.innerHTML,/aria-pressed="true">모자/);
  } finally {
    for(const [key,descriptor] of Object.entries(originals)) {
      if(descriptor) Object.defineProperty(globalThis,key,descriptor);
      else delete globalThis[key];
    }
  }
});

test('home insights connect discovery, activity, social context and rewards',()=>{
  const store=createStore();
  const first=page(store,'#/home');
  assert.match(first,/지금 뜨는 도전/);
  assert.match(first,/#\/book\/math\?tab=home/);
  assert.match(first,/#\/chat\/friend\?tab=home/);
  assert.match(first,/80 XP 남음/);
  assert.match(first,/aria-label="오늘의 일일 퀘스트"[^>]*aria-valuenow="0"/);
  assert.match(first,/cash-coins-pouch.png/);
  assert.match(first,/첫 도전 상자/);
  store.dispatch('accept','run');
  assert.match(page(store,'#/home'),/이어서 하기/);
  store.dispatch('complete','run');
  assert.match(page(store,'#/home'),/aria-label="오늘의 일일 퀘스트"[^>]*aria-valuenow="1"/);
  assert.match(page(store,'#/home'),/상자 수령 가능/);
  store.dispatch('open','starter');
  assert.doesNotMatch(page(store,'#/home'),/받을 보상/);
  store.dispatch('complete','math-today');
  store.dispatch('complete','math-main');
  assert.match(page(store,'#/home'),/오늘의 도전을 마쳤어요/);
  assert.equal(readRoute('#/rewards?tab=home').tab,'home');
});

test('home search finds quest books and unread notifications survive a reload',()=>{
  const storage=memory();
  const store=createStore(storage);
  assert.match(searchResults(store.read(),'수학'),/퀘스트북/);
  assert.match(searchResults(store.read(),'   달리기  '),/달리기 기록/);
  assert.match(searchResults(store.read(),'존재하지 않는 목표'),/일치하는/);
  assert.equal(searchResults(store.read(),' '),'');
  assert.equal((renderHomeAlerts(store.read()).match(/읽지 않음/g)||[]).length,3);
  assert.equal(store.dispatch('read-alert','daily'),true);
  assert.equal(store.dispatch('read-alert','daily'),false);
  assert.equal(store.dispatch('read-alert','unknown'),false);
  assert.equal((renderHomeAlerts(createStore(storage).read()).match(/읽지 않음/g)||[]).length,2);
});

test('quest index filters real statuses and updates its book cover progress',()=>{
  const store=createStore();
  const active=page(store,'#/quests',{...ui,quest:'active'});
  assert.match(active,/questbook-cover[^>]*href="#\/book\/math\?tab=quests"/);
  assert.match(active,/aria-valuenow="1"/);
  assert.match(active,/data-value="active" aria-pressed="true"/);
  assert.match(active,/수학 오답 3문제 다시 풀기/);
  assert.doesNotMatch(active,/달리기 기록 0.5초 줄이기/);
  const offered=page(store,'#/quests',{...ui,quest:'offered'});
  assert.match(offered,/달리기 기록 0.5초 줄이기/);
  assert.match(offered,/동네에서 주황색 거울 찾기/);
  assert.equal(store.dispatch('defer','math-today'),true);
  assert.match(page(store,'#/quests',{...ui,quest:'deferred'}),/수학 오답 3문제 다시 풀기/);
  assert.equal(store.dispatch('resume','math-today'),true);
  assert.equal(store.dispatch('complete','math-today'),true);
  const done=page(store,'#/quests',{...ui,quest:'done'});
  assert.match(done,/aria-valuenow="2"/);
  assert.match(done,/수학 오답 3문제 다시 풀기/);
  assert.equal(readRoute('#/search?tab=quests').tab,'quests');
  assert.equal(readRoute('#/chat/ai?tab=quests').tab,'quests');
  assert.match(searchResults(store.read(),'수학','quests'),/#\/book\/math\?tab=quests/);
});

test('clan tab content separates shared reward, insight periods and catalog purchase',()=>{
 const store=createStore();
 const current=page(store,'#/clan',{...ui,clanView:'mine'});
 assert.match(current,/80 XP 남았어요/);
 assert.match(current,/260 캐시/);
 assert.match(current,/버킷 햇/);
 assert.match(current,/role="tablist"/);
 assert.match(current,/clan-intro__name/);
 assert.doesNotMatch(current,/class="seed-list__item clan-member"/);
 const past=page(store,'#/clan',{...ui,clanView:'insights',period:'previous'});
 assert.match(past,/합계 1,230 XP/);
 assert.equal((past.match(/class="seed-list__item clan-member"/g)||[]).length,4);
 assert.match(past,/aria-label="주간 공동 목표 달성률"[^>]*aria-valuenow="1120"/);
 const market=page(store,'#/clan',{...ui,clanView:'shop'});
 assert.match(market,/클랜 상점/);
 assert.match(market,/data-action="purchase"/);
 assert.equal((market.match(/class="clan-market__product"/g)||[]).length,3);
 assert.equal(store.dispatch('complete','math-main'),true);
 assert.match(page(store,'#/clan',{...ui,clanView:'mine'}),/상자 받기/);
 assert.equal(store.dispatch('claim','clan'),true);
 assert.match(page(store,'#/clan',{...ui,clanView:'mine'}),/보상함 보기/);
 assert.match(page(store,'#/clan',{...ui,clanView:'insights',period:'previous'}),/합계 1,230 XP/);
});
test('clan join and leave only change preview membership, keeping chat history and rewards',()=>{
 const s=createStore();
 const messages=s.read().messages.clan.length;
 assert.equal(s.read().clanJoined,true);
 assert.equal(s.dispatch('clan-leave','clan'),true);
 assert.equal(s.read().clanJoined,false);
 assert.match(page(s,'#/clan'),/참가하기/);
 assert.doesNotMatch(page(s,'#/dm'),/data-dm-kind="clan"/);
 assert.match(page(s,'#/home'),/클랜에 참여해 보세요/);
 assert.match(page(s,'#/chat/clan?tab=clan'),/클랜에 참여하면/);
 assert.equal(s.dispatch('message','clan','떠난 사람'),false);
 assert.equal(s.dispatch('claim','clan'),false);
 assert.equal(s.dispatch('clan-join','clan'),true);
 assert.equal(s.dispatch('clan-join','clan'),false);
 assert.equal(s.read().messages.clan.length,messages);
 assert.match(page(s,'#/clan'),/방과후 원정대/);
});
test('clan tab controls are scoped to one panel, and chat header uses a hamburger',()=>{
 const app=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(app,/control\.dataset\.clanView/);
 assert.match(app,/\['mine','insights','shop'\]/);
 assert.match(app,/event\.key==='ArrowRight'/);
 assert.match(app,/aria-label="대화방 메뉴">\$\{icon\('menu'\)\}/);
 const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(css,/\.clan-tabs button\[aria-selected="true"\]::after/);
 const panel=page(createStore(),'#/clan',{...ui,clanView:'insights'});
 assert.equal((panel.match(/role="tabpanel"/g)||[]).length,1);
 assert.doesNotMatch(panel,/class="clan-reward"/);
});
test('home compact rail shows real status and linked destinations',()=>{
  const store=createStore();
  const html=page(store,'#/home');
  assert.match(html,/class="home-glance-rail"/);
  assert.equal((html.match(/class="home-glance-card"/g)||[]).length,3);
  assert.match(html,/#\/rewards\?tab=home/);
  assert.match(html,/#\/me/);
  assert.doesNotMatch(html,/home-activity__mascot|MY ACTIVITY|DISCOVER|EVENT & REWARD/);
  const stylesheet=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(stylesheet,/\.home-glance-rail\s*\{[^}]*overflow-x:auto/);
  assert.doesNotMatch(stylesheet,/\.home-section-eyebrow\s*\{/);
});


test('My Big profile follows owned and equipped state with actual page destinations',()=>{
  const store=createStore();
  const initial=page(store,'#/me');
  assert.match(initial,/me-profile__figure/);
  assert.match(initial,/data-avatar="fluid"/);
  assert.match(initial,/블루 캡/);
  assert.match(initial,/cosmetic_blue_cap.png/);
  assert.equal((initial.match(/class="seed-item-display seed-surface me-collection__item"/g)||[]).length,2);
  for(const path of ['#/decorate?tab=me','#/shop?tab=me','#/rewards?tab=me'])assert.ok(initial.includes(path));
  assert.equal(store.dispatch('equip','0'),true);
  assert.match(page(store,'#/me'),/골지 비니/);
  assert.equal(store.dispatch('open','starter'),true);
  assert.equal((page(store,'#/me').match(/class="seed-item-display seed-surface me-collection__item"/g)||[]).length,3);
  assert.equal(store.dispatch('purchase','1001'),true);
  assert.match(page(store,'#/me'),/크림 헤드폰/);
  assert.equal(store.dispatch('complete','math-today'),true);
  assert.match(page(store,'#/me'),/790 XP/);
});

test('My Big identity is unboxed, avatar first, without level meter or duplicate edit button',()=>{
  const store=createStore();
  const html=page(store,'#/me');
  const profile=html.slice(html.indexOf('<div class="me-profile">'),html.indexOf('<section class="me-stats"'));
  assert.ok(profile.indexOf('me-profile__figure')<profile.indexOf('me-profile__identity'));
  assert.match(profile,/data-avatar="fluid"/);
  assert.doesNotMatch(profile,/seed-surface|progress-track|me-profile__meter/);
  assert.equal((html.match(/class="seed-action"[^>]*href="#\/decorate\?tab=me"/g)||[]).length,1);
  assert.match(html,/cosmetic_blue_cap\.png/);
  assert.match(html,/#\/rewards\?tab=me/);
  const styles=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(styles,/\.me-profile\s*\{[^}]*background:transparent/);
  assert.doesNotMatch(styles,/\.me-profile__meter\s*\{/);
});


test('My integrated collection and real completed record remain connected',()=>{
  const store=createStore();
  const initial=page(store,'#/me');
  assert.doesNotMatch(initial,/지금 내 스타일/);
  assert.match(initial,/me-record__text/);
  assert.match(initial,/어제 배운 개념 한 줄 정리/);
  assert.match(initial,/#\/quest\/math-review\?tab=me/);
  assert.equal((initial.match(/data-item-presentation="integrated"/g)||[]).length,2);
  assert.match(initial,/seed-item-display__info/);
  assert.doesNotMatch(initial,/data-equipped=|착용 중|보유 중/);
  const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(css,/\.me-collection\s*\{[^}]*overflow-x:auto/);
  assert.doesNotMatch(css,/\.me-profile__outfit\s*\{/);
  assert.equal(store.dispatch('complete','math-today'),true);
  const updated=page(store,'#/me');
  assert.match(updated,/수학 오답 3문제 다시 풀기/);
  assert.match(updated,/\+ 60 XP/);
  assert.equal(store.dispatch('open','starter'),true);
  assert.equal((page(store,'#/me').match(/data-item-presentation="integrated"/g)||[]).length,3);
  assert.match(page(store,'#/me'),/화이트 이어버드/);
});

test('My compact item rail and owned box shelf reflect real state',()=>{
  const store=createStore();
  let html=page(store,'#/me');
  assert.equal((html.match(/class="seed-item-display seed-surface me-collection__item"/g)||[]).length,2);
  assert.doesNotMatch(html,/data-equipped=|착용 중|보유 중/);
  assert.match(html,/class="me-box-list"[^>]*data-count="1"/);
  assert.match(html,/첫 도전 상자/);
  assert.doesNotMatch(html,/주간 클랜 상자/);
  assert.match(html,/상점 둘러보기/);
  assert.equal(store.dispatch('open','starter'),true);
  html=page(store,'#/me');
  assert.match(html,/열어본 상자/);
  assert.equal((html.match(/class="seed-item-display seed-surface me-collection__item"/g)||[]).length,3);
  assert.equal(store.dispatch('complete','math-today'),true);
  assert.equal(store.dispatch('complete','math-main'),true);
  assert.equal(store.dispatch('claim','clan'),true);
  html=page(store,'#/me');
  assert.match(html,/주간 클랜 상자/);
  assert.match(html,/class="me-box-list"[^>]*data-count="2"/);
  const style=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(style,/grid-auto-columns:clamp\(6\.35rem,30%,7\.25rem\)/);
  assert.match(style,/\.me-box-list\s*\{[^}]*display:grid/);
});

test('My below-profile composition keeps identity and separates shelf from item rail',()=>{
  const store=createStore();
  const html=page(store,'#/me');
  assert.match(html,/class="me-box-list"/);
  assert.doesNotMatch(html,/class="me-box-rail"/);
  assert.match(html,/내 상자/);
  assert.doesNotMatch(html,/옆으로 넘겨보기/);
  assert.ok(html.indexOf('me-collection')<html.indexOf('me-box-list'));
  const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(css,/grid-auto-columns:clamp\(6\.35rem,30%,7\.25rem\)/);
  assert.match(css,/\.me-box-list\s*\{[^}]*display:grid/);
  assert.doesNotMatch(css,/\.me-box-rail\s*\{/);
  assert.equal(store.dispatch('open','starter'),true);
  assert.match(page(store,'#/me'),/열어본 상자/);
});

test('My item cards stretch to the same natural row height while preserving wrapped names',()=>{
  const html=page(createStore(),'#/me');
  assert.match(html,/class="me-collection"/);
  const styles=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(styles,/\.me-collection__item\.seed-item-display\[data-item-presentation="integrated"\]\s*\{[^}]*align-self:stretch/);
  assert.doesNotMatch(styles,/\.me-collection__name\s*\{[^}]*white-space:nowrap/);
});

test('DM global inbox uses real mascot avatars and SEED Filter Chips',()=>{
 const store=createStore();
 const all=page(store,'#/dm');
 assert.equal((all.match(/class="dm-conversation"/g)||[]).length,5);
 assert.equal((all.match(/data-avatar="fluid"/g)||[]).length,4);
 assert.equal((all.match(/class="seed-filter-chip"/g)||[]).length,3);
 assert.doesNotMatch(all,/dm-ai-entry|dm-inbox__assistant|방금 전|읽지 않음/);
 for(const id of ['ai','friend','clan','friend2','friend3'])assert.ok(all.includes('#/chat/'+id+'?tab=dm'));
 for(const items of ['0,1004','5','3,1003'])assert.ok(all.includes('data-avatar-items="'+items+'"'));
 assert.match(all,/clan-crest-fill\.svg/);
 const personal=renderPage(store.read(),readRoute('#/dm'),{...ui,dmFilter:'personal'});
 assert.equal((personal.match(/class="dm-conversation"[^>]* hidden/g)||[]).length,1);
 const clan=renderPage(store.read(),readRoute('#/dm'),{...ui,dmFilter:'clan'});
 assert.equal((clan.match(/class="dm-conversation"[^>]* hidden/g)||[]).length,4);
 const search=renderPage(store.read(),readRoute('#/dm'),{...ui,dmFilter:'personal',dmSearch:'별이'});
 assert.equal((search.match(/class="dm-conversation"[^>]* hidden/g)||[]).length,4);
 const empty=renderPage(store.read(),readRoute('#/dm'),{...ui,dmFilter:'clan',dmSearch:'별이'});
 assert.match(empty,/class="dm-inbox__empty" aria-live="polite"\s*>검색 결과가 없어요/);
 assert.equal(store.dispatch('message','friend3','내일 같이 구경하자'),true);
 assert.match(page(store,'#/dm'),/내일 같이 구경하자/);
 const script=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(script,/element\.dataset\.avatarItems/);
 assert.match(script,/row\.dataset\.dmKind!==category/);
 const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(css,/\.dm-conversation__avatar\s*\{[^}]*background:transparent/);
 assert.doesNotMatch(css,/\.dm-ai-entry\s*\{/);
});
test('DM thread migration preserves older local-storage messages',()=>{
 const prior=createStore().read();
 const legacy=JSON.parse(JSON.stringify(prior));
 delete legacy.messages.friend2; delete legacy.messages.friend3;
 const st={getItem:()=>JSON.stringify(legacy),setItem:()=>{}};
 const updated=createStore(st).read();
 assert.equal(updated.cash,prior.cash);
 assert.equal(updated.messages.friend[0].text,prior.messages.friend[0].text);
 assert.ok(updated.messages.friend2?.length>0&&updated.messages.friend3?.length>0);
});

test('Chat renders a single growing composer and keeps message state and quest proposals',()=>{
 const store=createStore();
 const html=page(store,'#/chat/friend?tab=dm');
 assert.match(html,/class="chat-view"/);
 assert.match(html,/role="log" aria-label="대화 기록"/);
 assert.match(html,/data-avatar-items="0,1004"/);
 assert.match(html,/class="chat-composer" data-form="message" data-id="friend"/);
 assert.match(html,/<textarea id="message" name="value" rows="1" maxlength="300" required/);
 assert.match(html,/aria-label="메시지 전송" disabled/);
 assert.doesNotMatch(html,/class="entry-form composer"|class="seed-field" name="value"/);
 assert.doesNotMatch(page(store,'#/chat/clan?tab=dm'),/clan-bookmark-fill\.svg/);
 assert.match(page(store,'#/chat/clan?tab=dm'),/chat-row__sender/);
 assert.equal(store.dispatch('message','friend','  첫 줄\n둘째 줄  '),true);
 assert.match(page(store,'#/chat/friend?tab=dm'),/data-from="me"/);
 assert.match(page(store,'#/chat/friend?tab=dm'),/첫 줄\n둘째 줄/);
 assert.equal(store.dispatch('message','friend','    '),false);
 assert.equal(store.dispatch('message','ai','수학 10분 계획'),true);
 const ai=page(store,'#/chat/ai?tab=dm');
 assert.match(ai,/chat-quest-link/);
 assert.match(ai,/#\/quest\//);
 const sheet=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(sheet,/\.chat-history\s*\{[^}]*overflow-y:auto/);
 assert.match(sheet,/\.chat-composer__send\.seed-icon-action/);
});
test('DM header menu is shared and chat keyboard shortcuts are implemented without fake history',()=>{
 const app=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(app,/trailing\.innerHTML=globalMenu\(state,'dm'\)/);
 assert.match(app,/globalMenu\(state,'home'\)/);
 assert.match(app,/event\.target\.form\?\.requestSubmit/);
 assert.match(app,/event\.isComposing/);
 assert.match(app,/event\.shiftKey/);
 assert.match(app,/history\.scrollTop=history\.scrollHeight/);
 assert.match(app,/send\.disabled=!field\.value\.trim\(\)/);
 assert.equal(readRoute('#/chat/friend?tab=dm').type,'non-global');
});

test('Chat uses Standard Start top navigation with Middle identity and no repeated body profile',()=>{
 const store=createStore();
 for(const id of ['friend','clan','ai']){
  const route=readRoute('#/chat/'+id+'?tab=dm');
  assert.equal(route.type,'non-global');
  assert.equal(route.align,'start');
  assert.doesNotMatch(page(store,'#/chat/'+id+'?tab=dm'),/chat-intro/);
 }
 assert.equal(readRoute('#/chat/friend?tab=dm').title,'다온');
 const code=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.ok(code.includes("header.dataset.titleAlign=route.align||'center'"));
 assert.ok(code.includes("titleElement.replaceChildren(image,info)"));
 assert.ok(code.includes("description.className='chat-header__meta'"));
 assert.ok(code.includes('name.textContent=route.title'));
 assert.ok(code.includes("image.className='chat-header__avatar'"));
 assert.ok(code.includes("shell.querySelectorAll('[data-avatar]')"));
 assert.ok(code.includes("face.dataset.avatarItems=(contact.avatarEquipment||[]).join(',')"));
 const style=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(style,/\.chat-header__avatar\s*\{/);
 assert.doesNotMatch(style,/\.chat-intro\s*\{/);
 assert.match(style,/\.chat-history\s*\{[^}]*justify-content:flex-start/);
 assert.match(style,/max-height:5\.65rem/);
 assert.equal(store.dispatch('message','friend','답장'),true);
 assert.match(page(store,'#/chat/friend?tab=dm'),/답장/);
});

test('Chat header identity has real subtitle and composer is borderless',()=>{
 const store=createStore();
 const chat=page(store,'#/chat/friend3?tab=dm');
 assert.match(chat,/class="chat-history"/);
 assert.doesNotMatch(chat,/chat-intro/);
 const app=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(app,/info\.append\(name,description\)/);
 assert.match(app,/titleElement\.replaceChildren\(image,info\)/);
 assert.match(app,/description\.textContent=route\.id==='clan'/);
 const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(css,/\.chat-header__meta\s*\{/);
 assert.match(css,/\.chat-composer\s*\{[^}]*border:0/);
 assert.match(css,/\.chat-composer__field\s*\{[^}]*border:0/);
 assert.doesNotMatch(css,/\.chat-composer\s*\{[^}]*border-top:/);
 assert.match(css,/\.chat-history\s*\{[^}]*justify-content:flex-start/);
 assert.equal(store.dispatch('message','friend3','지금 확인했어'),true);
 assert.match(page(store,'#/chat/friend3?tab=dm'),/지금 확인했어/);
});

test('Clan message authors use member avatars and sender names, never the clan flag',()=>{
 const store=createStore();
 const html=page(store,'#/chat/clan?tab=dm');
 const body=html.slice(html.indexOf('class="chat-history"'));
 assert.equal((body.match(/class="chat-row"/g)||[]).length,10);
 assert.match(body,/chat-row__sender">다온/);
 assert.match(body,/chat-row__sender">별이/);
 assert.match(body,/data-avatar-items="0,1004"/);
 assert.match(body,/data-avatar-items="5"/);
 assert.doesNotMatch(body,/clan-bookmark-fill\.svg|chat-avatar__clan/);
 assert.match(body,/data-continued="true"/);
 assert.equal((body.match(/chat-row__sender">별이/g)||[]).length,3);
 assert.match(body,/data-from="me"/);
 assert.match(page(store,'#/dm'),/다시 모이자/);
});
test('Preview sample migration upgrades only pristine seed messages and preserves user writes',()=>{
 const initial=createStore().read();
 const old=JSON.parse(JSON.stringify(initial));
 for(const id of ['clan','friend','friend2','friend3'])old.messages[id]=[old.messages[id][0]];
 if(Object.hasOwn(old.messages.clan[0],'senderId'))delete old.messages.clan[0].senderId;
 const storage={getItem:()=>JSON.stringify(old),setItem:()=>{}};
 const upgraded=createStore(storage).read();
 assert.equal(upgraded.messages.clan.length,10);
 assert.equal(upgraded.messages.friend3.length,9);
 const customized=JSON.parse(JSON.stringify(old));
 customized.messages.clan.push({from:'me',text:'내가 작성한 메시지'});
 customized.messages.friend3=[{from:'them',text:'직접 수정한 대화'}];
 const preserved=createStore({getItem:()=>JSON.stringify(customized),setItem:()=>{}}).read();
 assert.equal(preserved.messages.clan.length,2);
 assert.equal(preserved.messages.clan[1].text,'내가 작성한 메시지');
 assert.equal(preserved.messages.friend3[0].text,'직접 수정한 대화');
 const unknown=JSON.parse(JSON.stringify(preserved));
 const safe=createStore({getItem:()=>JSON.stringify(unknown),setItem:()=>{}}).read();
 assert.match(renderPage(safe,readRoute('#/chat/clan?tab=dm'),ui),/클랜 멤버/);
 assert.match(readFileSync(new URL('./app.mjs',import.meta.url),'utf8'),/clan\.current\.length/);
});
test('Longer example conversations remain actionable and all other pages keep working',()=>{
 const store=createStore();
 assert.equal(store.read().messages.friend.length,6);
 assert.equal(store.read().messages.friend2.length,7);
 assert.equal(store.read().messages.friend3.length,9);
 assert.equal(store.dispatch('message','clan','새로 보낸 메시지'),true);
 assert.match(page(store,'#/chat/clan?tab=dm'),/새로 보낸 메시지/);
 assert.match(page(store,'#/dm'),/새로 보낸 메시지/);
 assert.match(page(store,'#/me'),/내 스타일/);
 assert.match(page(store,'#/home'),/오늘 한눈에/);
});

test('Chat controls use real actions, keep incoming protected, and display saved photos',()=>{
  const store=createStore();
  assert.equal(store.dispatch('toggle-chat-notifications','clan'),true);
  assert.equal(store.read().mutedThreads.clan,true);
  assert.equal(store.dispatch('delete-message','clan',{index:0}),false);
  const count=store.read().messages.clan.length;
  assert.equal(store.dispatch('message','clan','안녕하세요'),true);
  assert.equal(store.dispatch('edit-message','clan',{index:count,text:'수정했어요'}),true);
  assert.match(page(store,'#/chat/clan?tab=dm'),/수정했어요/);
  assert.equal(store.dispatch('delete-message','clan',{index:count}),true);
  assert.equal(store.read().messages.clan.length,count);
  const img='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+jje0AAAAASUVORK5CYII=';
  assert.equal(store.dispatch('send-photo','friend3',{src:img}),true);
  assert.match(page(store,'#/chat/friend3?tab=dm'),/class="chat-bubble__image"/);
  assert.match(page(store,'#/dm'),/사진을 보냈어요/);
  assert.equal(store.dispatch('send-photo','friend3',{src:'javascript:1'}),false);
  assert.equal(store.dispatch('clear-chat','friend3'),true);
  assert.match(page(store,'#/chat/friend3?tab=dm'),/아직 대화가 없어요/);
  const script=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
  assert.match(script,/chatHeaderActions\(state,route\.id\)/);
  assert.match(script,/route\.id==='clan'&&state\.clanJoined===false/);
  assert.match(script,/compressChatPhoto\(file\)/);
});
test('Existing preview messages migrate without deleting personal writes or settings',()=>{
  const old=createStore().read();delete old.mutedThreads;
  const saved=createStore({getItem:()=>JSON.stringify(old),setItem:()=>{}}).read();
  assert.deepEqual(saved.mutedThreads,{});
  assert.equal(saved.messages.clan.length,10);
});

test('Messenger only opens one shared message action sheet, never a per-bubble popup',()=>{
 const store=createStore();
 const chat=page(store,'#/chat/friend3?tab=dm');
 assert.doesNotMatch(chat,/chat-message-tools|chat-message-tools__menu/);
 assert.equal((chat.match(/data-chat-ui="message-options"/g)||[]).length,store.read().messages.friend3.filter(m=>m.from==='me').length);
 assert.match(chat,/aria-haspopup="dialog" aria-controls="chat-message-sheet"/);
 const source=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(source,/action==='message-options'/);
 assert.match(source,/get\('#chat-message-sheet-actions'\)\.innerHTML=options\.join/);
 assert.match(source,/sheet\.showModal\(\)/);
 assert.match(source,/get\('#chat-message-sheet'\)\?\.close/);
 const pageHtml=readFileSync(new URL('./index.html',import.meta.url),'utf8');
 assert.equal((pageHtml.match(/id="chat-message-sheet"/g)||[]).length,1);
 const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.doesNotMatch(css,/\.chat-message-tools/);
 assert.match(css,/\.chat-message-sheet__actions/);
 assert.match(css,/\.chat-composer\s*\{[^}]*border:0/);
 assert.doesNotMatch(css,/\.chat-composer\s*\{[^}]*border-top/);
 assert.match(css,/\.chat-history\s*\{[^}]*justify-content:flex-start/);
});

test('Redesigned chat keeps edit delete photo and only one contextual sheet',()=>{
 const store=createStore();
 const html=page(store,'#/chat/clan?tab=dm');
 assert.match(html,/chat-row__sender/);
 assert.doesNotMatch(html,/class="chat-message-tools"/);
 const initial=store.read().messages.clan.length;
 assert.equal(store.dispatch('message','clan','테스트'),true);
 assert.match(page(store,'#/chat/clan?tab=dm'),/data-chat-ui="message-options"/);
 assert.equal(store.dispatch('edit-message','clan',{index:initial,text:'수정'}),true);
 assert.match(page(store,'#/chat/clan?tab=dm'),/수정/);
 assert.equal(store.dispatch('delete-message','clan',{index:initial}),true);
 assert.equal(store.read().messages.clan.length,initial);
 const photo='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+jje0AAAAASUVORK5CYII=';
 assert.equal(store.dispatch('send-photo','clan',{src:photo}),true);
 assert.match(page(store,'#/chat/clan?tab=dm'),/class="chat-bubble__image"/);
 const script=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(script,/action==='message-options'/);
 assert.match(script,/get\('#chat-message-sheet'\)\?\.close/);
});

test('Delete confirmation belongs to the shared chat sheet, not the browser alert',()=>{
 const script=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(script,/action==='confirm-delete'/);
 assert.match(script,/data-chat-ui="confirm-delete"/);
 assert.match(script,/data-chat-ui="close-message-sheet"/);
 assert.doesNotMatch(script,/window\.confirm\('이 메시지를 삭제할까요/);
 assert.match(page(createStore(),'#/chat/friend3?tab=dm'),/메시지 옵션/);
});


test('Shop catalog filters, media and acquired chests use existing data',()=>{
 const store=createStore();
 const all=page(store,'#/shop');
 assert.match(all,/shop-feature/);
 assert.match(all,/data-item-layout="horizontal"/);
 assert.equal((all.match(/class="seed-item-display shop-product"/g)||[]).length,7);
 assert.equal((all.match(/class="shop-box"/g)||[]).length,2);
 assert.match(all,/<div class="shop-box__image"><div class="seed-media"/);
 assert.match(all,/#\/item\/1\?tab=me/);
 assert.match(all,/#\/rewards\?tab=me/);
 assert.match(all,/href="#\/clan"/);
 assert.doesNotMatch(all,/상자 구매|할인율|신규 재화/);
 const hats=page(store,'#/shop',{...ui,shop:'모자'});
 const audio=page(store,'#/shop',{...ui,shop:'오디오'});
 assert.equal((hats.match(/class="seed-item-display shop-product"/g)||[]).length,4);
 assert.equal((audio.match(/class="seed-item-display shop-product"/g)||[]).length,3);
 assert.match(audio,/data-value="오디오" aria-pressed="true"/);
});
test('Shop refreshes owned and reward states after real local actions',()=>{
 const store=createStore();
 assert.equal(store.dispatch('purchase','1'),true);
 assert.equal(store.dispatch('purchase','1'),false);
 let html=page(store,'#/shop');
 assert.match(html,/이미 가지고 있어요/);
 assert.match(html,/aria-label="160 캐시"/);
 assert.match(html,/크림 캡, 보유 중, 상품 상세/);
 assert.equal(store.dispatch('open','starter'),true);
 html=page(store,'#/shop');
 assert.match(html,/열어본 상자/);
 assert.match(html,/획득 전/);
 assert.equal(store.dispatch('complete','math-main'),true);
 assert.equal(store.dispatch('claim','clan'),true);
 html=page(store,'#/shop');
 assert.match(html,/열 수 있어요/);
 assert.doesNotMatch(html,/data-action="purchase"/);
});
test('Shop follows S06 responsive frame and scroll contracts',()=>{
 const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(css,/\.shop-products\s*\{[^}]*grid-template-columns:repeat\(2,minmax\(0,1fr\)\)/);
 assert.match(css,/\.shop-box-rail\s*\{[^}]*overflow-x:auto/);
 assert.match(css,/\.shop-product > \.seed-media \.seed-media__image\s*\{[^}]*object-fit:contain/);
 assert.match(css,/\.preview-shell\[data-screen="shop"\] \.shop-balance\s*\{\s*display: flex/);
 assert.doesNotMatch(css,/\.featured-item/);
});

test('Decorate honors owned-only gallery and live equipped slot identity',()=>{
 const store=createStore();
 const first=page(store,'#/decorate',{...ui,dressFilter:'전체'});
 assert.match(first,/class="decorate-page"/);
 assert.match(first,/aria-label="현재 장착한 장식"/);
 assert.match(first,/블루 캡/);
 assert.match(first,/선택 안 함/);
 assert.equal((first.match(/class="seed-item-display seed-surface decorate-item"/g)||[]).length,2);
 assert.equal((first.match(/data-action="equip"/g)||[]).length,2);
 assert.match(first,/data-id="2" data-equipped="true" aria-pressed="true"/);
 assert.match(first,/aria-label="보유 장식 종류"/);
 assert.doesNotMatch(first,/data-action="purchase"|class="item-grid"/);
 assert.equal(store.dispatch('equip','0'),true);
 const next=page(store,'#/decorate',{...ui,dressFilter:'전체'});
 assert.match(next,/골지 비니/);
 assert.match(next,/data-id="0" data-equipped="true" aria-pressed="true"/);
 assert.match(next,/data-id="2" data-equipped="false" aria-pressed="false"/);
 assert.equal(store.dispatch('equip','0'),true);
 assert.match(page(store,'#/decorate'),/선택 안 함/);
});
test('Decorate category filters, empty state and purchased equipment preserve shared store',()=>{
 const store=createStore();
 const hats=page(store,'#/decorate',{...ui,dressFilter:'모자'});
 assert.equal((hats.match(/data-action="equip"/g)||[]).length,2);
 const audio=page(store,'#/decorate',{...ui,dressFilter:'오디오'});
 assert.equal((audio.match(/data-action="equip"/g)||[]).length,0);
 assert.match(audio,/보유한 오디오 장식이 없어요/);
 assert.match(audio,/#\/shop\?tab=me/);
 assert.equal(store.dispatch('purchase','1004'),true);
 assert.equal(store.dispatch('equip','1004'),true);
 const equipped=page(store,'#/decorate',{...ui,dressFilter:'오디오'});
 assert.equal((equipped.match(/data-action="equip"/g)||[]).length,1);
 assert.match(equipped,/화이트 이어버드/);
 assert.match(equipped,/data-id="1004" data-equipped="true" aria-pressed="true"/);
 assert.match(page(store,'#/me'),/화이트 이어버드/);
 assert.match(page(store,'#/shop'),/보유 중/);
});
test('Decorate uses page-local layout over SEED shared media/controls, no avatar crop',()=>{
 const style=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 const script=readFileSync(new URL('./app.mjs',import.meta.url),'utf8');
 assert.match(style,/\.decorate-preview__portrait/);
 assert.match(style,/\.decorate-item > \.seed-media \.seed-media__image\s*\{[^}]*object-fit:contain/);
 assert.match(style,/\.decorate-items\s*\{[^}]*grid-template-columns:repeat\(2,minmax\(0,1fr\)\)/);
 assert.doesNotMatch(style,/\.dress-stage|\.dress-item\s*\{/);
 assert.match(script,/editMessage:null,clanView:'mine',dressFilter:'전체'/);
 assert.match(script,/aria-label="상점으로 이동"/);
});

test('Clan uses the legacy eight-ray emblem inside its new bookmark silhouette',()=>{
 const store=createStore();
 const mine=page(store,'#/clan');
 const dm=page(store,'#/dm');
 const publicView=(()=>{store.dispatch('clan-leave','clan');return page(store,'#/clan')})();
 assert.match(mine,/clan-intro__mark/);
 assert.match(mine,/clan-crest-fill\.svg/);
 assert.match(publicView,/clan-discover__symbol/);
 assert.match(publicView,/clan-crest-fill\.svg/);
 assert.match(dm,/clan-crest-fill\.svg/);
 assert.doesNotMatch(page(store,'#/dm'),/clan-crest-fill\.svg/);
 const emblem=readFileSync(new URL('../../assets/icons/preview/clan-crest-fill.svg',import.meta.url),'utf8');
 assert.match(emblem,/fill-rule="evenodd"/);
 assert.match(emblem,/M12\.000 7\.450/);
 const outline=readFileSync(new URL('../../assets/icons/preview/clan-crest-outline.svg',import.meta.url),'utf8');
 assert.match(outline,/M12 7\.9v5\.8/);
 const seed=readFileSync(new URL('../../seed/runtime/seed-page-contract.mjs',import.meta.url),'utf8');
 assert.match(seed,/outline:"clan-crest-outline",active:"clan-crest-fill"/);
 assert.match(seed,/outlineBase:"\.\.\/\.\.\/assets\/icons\/preview\/"/);
 const style=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
 assert.match(style,/\.clan-intro__mark \.seed-icon/);
 assert.doesNotMatch(style,/\.clan-intro__mark[^}]*border-radius:1\.05rem/);
});
test('Clan insights contribution bars derive only from actual periods, with no fabricated ranking',()=>{
 const store=createStore();
 const current=page(store,'#/clan',{...ui,clanView:'insights'});
 assert.equal((current.match(/class="clan-member__meter"/g)||[]).length,4);
 assert.match(current,/--clan-share:100%/);
 const past=page(store,'#/clan',{...ui,clanView:'insights',period:'previous'});
 assert.equal((past.match(/class="clan-member__meter"/g)||[]).length,4);
 assert.match(past,/합계 1,230 XP/);
 assert.match(page(store,'#/ranking'),/clan-member__meter/);
 assert.doesNotMatch(page(store,'#/clan',{...ui,clanView:'mine'}),/clan-member__meter/);
});
