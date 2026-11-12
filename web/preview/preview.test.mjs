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
  assert.match(page(store,'#/clan',{...ui,period:'previous'}),/1,230/);
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
  assert.match(first,/#\\/book\\/math\\?tab=home/);
  assert.match(first,/#\\/chat\\/friend\\?tab=home/);
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

test('clan leaderboard period is independent of the current reward and own contribution',()=>{
  const store=createStore();
  const current=page(store,'#/clan',{...ui,period:'current'});
  assert.match(current,/80 XP 남음/);
  assert.match(current,/260 캐시/);
  assert.match(current,/버킷 햇/);
  assert.equal((current.match(/class="seed-list__item clan-member"/g)||[]).length,4);
  const previous=page(store,'#/clan',{...ui,period:'previous'});
  assert.match(previous,/합계 1,230 XP/);
  assert.match(previous,/aria-label="주간 클랜 상자"[^>]*aria-valuenow="1120"/);
  assert.doesNotMatch(previous,/상자 받기/);
  assert.equal(store.dispatch('complete','math-main'),true);
  const unlocked=page(store,'#/clan',{...ui,period:'previous'});
  assert.match(unlocked,/330 XP/);
  assert.match(unlocked,/상자 받기/);
  assert.match(unlocked,/합계 1,230 XP/);
  assert.equal(store.dispatch('claim','clan'),true);
  assert.match(page(store,'#/clan',{...ui,period:'current'}),/보상함 보기/);
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
  assert.match(initial,/#\\/quest\\/math-review\\?tab=me/);
  assert.equal((initial.match(/data-item-presentation="integrated"/g)||[]).length,2);
  assert.match(initial,/seed-item-display__info/);
  assert.match(initial,/data-equipped="true"/);
  const css=readFileSync(new URL('./preview.css',import.meta.url),'utf8');
  assert.match(css,/\\.me-collection\\s*\\{[^}]*overflow-x:auto/);
  assert.doesNotMatch(css,/\\.me-profile__outfit\\s*\\{/);
  assert.equal(store.dispatch('complete','math-today'),true);
  const updated=page(store,'#/me');
  assert.match(updated,/수학 오답 3문제 다시 풀기/);
  assert.match(updated,/\\+ 60 XP/);
  assert.equal(store.dispatch('open','starter'),true);
  assert.equal((page(store,'#/me').match(/data-item-presentation="integrated"/g)||[]).length,3);
  assert.match(page(store,'#/me'),/화이트 이어버드/);
});
