import {applyPageType,selectWebNav} from '../../seed/runtime/seed-page-contract.mjs';
import {mountMascotAvatar} from '../../seed/runtime/seed-mascot-avatar.mjs';
import {createStore} from './store.mjs?v=home-insights-1';
import {renderPage,renderHomeAlerts,searchResults,cash,icon} from './views.mjs?v=home-insights-1';
import {homeAlerts} from './mock-data.mjs?v=home-insights-1';
import {readRoute,navMarkup} from './routes.mjs?v=home-insights-1';

const get=selector=>document.querySelector(selector);
const root=document.documentElement;
const shell=get('.preview-shell');
const nav=get('.seed-bottom-nav');
const main=get('#main');
const header=get('.seed-top-nav__inner');
const leading=get('.seed-top-nav__leading');
const trailing=get('.seed-top-nav__trailing');
const notice=get('.preview-notice');
const alertSheet=get('#home-alerts');
const ui={quest:'active',shop:'전체',period:'current',search:''};
const scrollPositions=new Map();
const storage=(()=>{try{return window.localStorage;}catch{return undefined;}})();
const store=createStore(storage);
const feedback={accept:'퀘스트를 수락했어요.',reject:'제안을 거절했어요.',defer:'퀘스트를 미뤘어요.',resume:'퀘스트를 다시 시작했어요.',complete:'퀘스트 완료! 보상을 받았어요.',undo:'완료를 취소했어요.',claim:'상자를 보상함에 담았어요.',open:'상자 보상을 받았어요.',purchase:'아이템을 보유함에 담았어요.'};
let noticeTimer;
let nextKey=0;
let route=readRoute(location.hash);
let currentEntry={key:nextKey,depth:0};
history.replaceState(currentEntry,'',location.hash||'#/home');
history.scrollRestoration='manual';
nav.innerHTML=navMarkup();
try { root.dataset.theme=storage?.getItem('hyunsaengpatch-preview-theme')==='dark'?'dark':'light'; } catch {}

function iconAction(label,name,action,preview=false) {
  return `<button type="button" class="seed-icon-action" aria-label="${label}" data-shell-action="${action}" ${action==='alerts'?'aria-haspopup="dialog" aria-controls="home-alerts"':''}>${icon(name,preview)}</button>`;
}
function homeActions(state) {
  const unread=homeAlerts.some(alert=>!(state.readAlerts??[]).includes(alert.id));
  const dark=root.dataset.theme==='dark';
  const shortcut=(label,glyph,href)=>`<a class="menu-row" href="#/${href}">${icon(glyph)}${label}</a>`;
  return iconAction('검색','search','search')+
    `<span class="home-alert-trigger">${iconAction(unread?'알림 · 읽지 않은 항목 있음':'알림','bell-connected','alerts',true)}${unread?'<span class="home-alert-dot" aria-hidden="true"></span>':''}</span>`+
    `<details class="home-nav-menu"><summary class="seed-icon-action" aria-label="메뉴">${icon('menu')}</summary>
      <div class="seed-surface home-menu-panel" data-surface="raised">
        ${shortcut('퀘스트 만들기','sparkle','chat/ai?tab=home')}
        ${shortcut('보상함','gift','rewards?tab=home')}
        ${shortcut('꾸미기','shirt','decorate')}
        <button type="button" class="menu-row" data-shell-action="theme">${icon(dark?'sun':'moon',true)}${dark?'라이트 모드':'다크 모드'}</button>
      </div>
    </details>`;
}
function render() {
  const state=store.read();
  if(route.name!=='home'&&alertSheet?.open)alertSheet.close();
  const type=applyPageType(shell,route.type);
  header.dataset.navRole=type.header;
  header.dataset.titleAlign=route.align||'center';
  shell.dataset.screen=route.name;
  get('.seed-top-nav__title').textContent=route.title;
  document.title=route.title+' · 갓생패치';
  leading.innerHTML=type.header==='standard'?iconAction('뒤로 가기','left','back'):'';
  if(route.name==='home') {
    trailing.innerHTML=homeActions(state);
  } else if(route.name==='quests') {
    trailing.innerHTML=iconAction('검색','search','search')+iconAction('퀘스트 만들기','plus','new-quest');
  } else if(route.name==='clan') {
    trailing.innerHTML=iconAction('클랜 대화','chat','clan-chat');
  } else if(route.name==='me'||route.name==='shop') {
    trailing.innerHTML=cash(state.cash);
  } else trailing.replaceChildren();
  trailing.dataset.slotKind=route.name==='me'||route.name==='shop'?'info':trailing.childElementCount?'action':'empty';
  if(type.bottomNav)selectWebNav(nav,route.tab);
  main.innerHTML=renderPage(state,route,ui);
  main.querySelectorAll('[data-avatar]').forEach(element=>mountMascotAvatar(element,{equipmentIds:state.equipped,scale:element.dataset.avatar,label:'루원의 마스코트'}));
  main.querySelectorAll('.seed-media__image').forEach(img=>{
    img.addEventListener('error',()=>{img.closest('.seed-media').dataset.mediaState='error';});
    if(img.complete&&!img.naturalWidth)img.closest('.seed-media').dataset.mediaState='error';
  });
}
function showFeedback(text) {
  clearTimeout(noticeTimer);
  notice.textContent=text;
  noticeTimer=setTimeout(()=>{notice.textContent='';},3000);
}
function navigate(hash) {
  if(hash===location.hash) return;
  scrollPositions.set(currentEntry.key,window.scrollY);
  currentEntry={key:++nextKey,depth:currentEntry.depth+1};
  history.pushState(currentEntry,'',hash);
  route=readRoute(hash);
  render();
  main.focus({preventScroll:true});
  window.scrollTo(0,0);
}
function apply(action,id,value,focusTarget) {
  if(!store.dispatch(action,id,value)) {
    showFeedback('지금은 이 동작을 할 수 없어요.');
    return;
  }
  render();
  const sameControl=[...main.querySelectorAll('[data-action]')].find(button=>button.dataset.action===action&&button.dataset.id===id);
  if(focusTarget) get(focusTarget)?.focus({preventScroll:true});
  else (sameControl||main).focus({preventScroll:true});
  if(action==='message') window.scrollTo(0,document.documentElement.scrollHeight);
  if(feedback[action])showFeedback(feedback[action]);
}
document.addEventListener('click',event=>{
  if(!event.target.closest('.home-nav-menu'))get('.home-nav-menu[open]')?.removeAttribute('open');
  if(event.target===alertSheet) {
    const bounds=alertSheet.getBoundingClientRect();
    if(event.clientY<bounds.top||event.clientX<bounds.left||event.clientX>bounds.right) alertSheet.close();
    return;
  }
  const control=event.target.closest('button,a');
  if(!control)return;
  const href=control.getAttribute('href');
  if(href?.startsWith('#/')&&!event.metaKey&&!event.ctrlKey&&!event.shiftKey&&!event.altKey&&!event.button) {
    event.preventDefault();
    if(control.dataset.alertId) {
      store.dispatch('read-alert',control.dataset.alertId);
      alertSheet.close();
    }
    navigate(href);
    return;
  }
  const {shellAction,filter,value,period,action,id}=control.dataset;
  if(shellAction==='back') {
    if(currentEntry.depth>0)history.back();
    else navigate('#/'+(route.fallback||route.tab));
  } else if(shellAction==='search') {
    navigate('#/search?tab='+route.tab);
    get('#home-search')?.focus();
  } else if(shellAction==='new-quest') {
    navigate('#/chat/ai?tab=quests');
  } else if(shellAction==='clan-chat') {
    navigate('#/chat/clan?tab=clan');
  } else if(shellAction==='alerts') {
    get('#home-alert-list').innerHTML=renderHomeAlerts(store.read());
    alertSheet.showModal();
  } else if(shellAction==='close-alerts') {
    alertSheet.close();
  } else if(shellAction==='theme') {
    root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';
    try { storage?.setItem('hyunsaengpatch-preview-theme',root.dataset.theme); } catch {}
    render();
    get('.home-nav-menu summary')?.focus();
  } else if(filter||period) {
    if(filter)ui[filter]=value;
    else ui.period=period;
    render();
    [...main.querySelectorAll('button')].find(button=>filter?button.dataset.filter===filter&&button.dataset.value===value:button.dataset.period===period)?.focus({preventScroll:true});
  } else if(action) apply(action,id);
});
document.addEventListener('input',event=>{
  if(event.target.id!=='home-search')return;
  ui.search=event.target.value;
  get('#home-search-results').innerHTML=searchResults(store.read(),ui.search,route.tab);
});
document.addEventListener('keydown',event=>{
  if(event.key!=='Escape')return;
  const menu=get('.home-nav-menu[open]');
  if(!menu)return;
  menu.open=false;
  menu.querySelector('summary').focus();
});
document.addEventListener('submit',event=>{
  const form=event.target;
  if(!form.dataset.form)return;
  event.preventDefault();
  apply(form.dataset.form,form.dataset.id,new FormData(form).get('value'),form.dataset.form==='message'?'#message':undefined);
});
window.addEventListener('popstate',event=>{
  scrollPositions.set(currentEntry.key,window.scrollY);
  currentEntry=event.state&&Number.isInteger(event.state.key)?event.state:{key:++nextKey,depth:0};
  route=readRoute(location.hash);
  render();
  main.focus({preventScroll:true});
  window.scrollTo(0,scrollPositions.get(currentEntry.key)||0);
});
render();
