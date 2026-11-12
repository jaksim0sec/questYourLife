import {applyPageType,selectWebNav} from '../../seed/runtime/seed-page-contract.mjs';
import {mountMascotAvatar} from '../../seed/runtime/seed-mascot-avatar.mjs';
import {createStore} from './store.mjs';
import {renderPage,cash,icon} from './views.mjs';
import {readRoute,navMarkup} from './routes.mjs';

const get=selector=>document.querySelector(selector);
const root=document.documentElement;
const shell=get('.preview-shell');
const nav=get('.seed-bottom-nav');
const main=get('#main');
const header=get('.seed-top-nav__inner');
const leading=get('.seed-top-nav__leading');
const trailing=get('.seed-top-nav__trailing');
const notice=get('.preview-notice');
const ui={quest:'all',shop:'전체',period:'current'};
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
  return `<button type="button" class="seed-icon-action" aria-label="${label}" data-shell-action="${action}">${icon(name,preview)}</button>`;
}
function render() {
  const state=store.read();
  const type=applyPageType(shell,route.type);
  header.dataset.navRole=type.header;
  header.dataset.titleAlign=route.align||'center';
  shell.dataset.screen=route.name;
  get('.seed-top-nav__title').textContent=route.title;
  document.title=route.title+' · 현생패치';
  leading.innerHTML=type.header==='standard'?iconAction('뒤로 가기','left','back'):'';
  if(route.name==='home') {
    const dark=root.dataset.theme==='dark';
    trailing.innerHTML=iconAction(dark?'라이트 모드':'다크 모드',dark?'sun':'moon','theme',true);
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
  const control=event.target.closest('button,a');
  if(!control)return;
  const href=control.getAttribute('href');
  if(href?.startsWith('#/')&&!event.metaKey&&!event.ctrlKey&&!event.shiftKey&&!event.altKey&&!event.button) {
    event.preventDefault();
    navigate(href);
    return;
  }
  const {shellAction,filter,value,period,action,id}=control.dataset;
  if(shellAction==='back') {
    if(currentEntry.depth>0)history.back();
    else navigate('#/'+(route.fallback||route.tab));
  } else if(shellAction==='theme') {
    root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';
    try { storage?.setItem('hyunsaengpatch-preview-theme',root.dataset.theme); } catch {}
    render();
    get('[data-shell-action="theme"]').focus();
  } else if(filter||period) {
    if(filter)ui[filter]=value;
    else ui.period=period;
    render();
    [...main.querySelectorAll('button')].find(button=>filter?button.dataset.filter===filter&&button.dataset.value===value:button.dataset.period===period)?.focus({preventScroll:true});
  } else if(action) apply(action,id);
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
