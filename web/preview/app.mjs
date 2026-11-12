import {applyPageType,selectWebNav} from '../../seed/runtime/seed-page-contract.mjs';
import {mountMascotAvatar} from '../../seed/runtime/seed-mascot-avatar.mjs';
import {createStore} from './store.mjs?v=clan-global-v11';
import {renderPage,renderHomeAlerts,searchResults,cash,icon} from './views.mjs?v=clan-global-v11';
import {homeAlerts,threads,clan} from './mock-data.mjs?v=clan-global-v11';
import {readRoute,navMarkup} from './routes.mjs?v=clan-global-v11';

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
const ui={quest:'active',shop:'전체',period:'current',search:'',dmSearch:'',dmFilter:'all',editMessage:null,clanView:'mine'};
const scrollPositions=new Map();
const storage=(()=>{try{return window.localStorage;}catch{return undefined;}})();
const store=createStore(storage);
const feedback={'edit-message':'메시지를 수정했어요.','delete-message':'메시지를 삭제했어요.','clear-chat':'대화 내용을 비웠어요.','toggle-chat-notifications':'대화 알림 설정을 저장했어요.',accept:'퀘스트를 수락했어요.',reject:'제안을 거절했어요.',defer:'퀘스트를 미뤘어요.',resume:'퀘스트를 다시 시작했어요.',complete:'퀘스트 완료! 보상을 받았어요.',undo:'완료를 취소했어요.',claim:'상자를 보상함에 담았어요.',open:'상자 보상을 받았어요.',purchase:'아이템을 보유함에 담았어요.'};
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
function globalMenu(state,tab='home') {
  const dark=root.dataset.theme==='dark';
  const shortcut=(label,glyph,href)=>`<a class="menu-row" href="#/${href}">${icon(glyph)}${label}</a>`;
  return `<details class="home-nav-menu"><summary class="seed-icon-action" aria-label="메뉴">${icon('menu')}</summary>
    <div class="seed-surface home-menu-panel" data-surface="raised">
      ${shortcut('퀘스트 만들기','sparkle','chat/ai?tab='+tab)}
      ${shortcut('보상함','gift','rewards?tab='+(tab==='dm'?'me':tab))}
      ${shortcut('꾸미기','shirt','decorate')}
      <button type="button" class="menu-row" data-shell-action="theme">${icon(dark?'sun':'moon',true)}${dark?'라이트 모드':'다크 모드'}</button>
    </div>
  </details>`;
}
function homeActions(state) {
  const unread=homeAlerts.some(alert=>!(state.readAlerts??[]).includes(alert.id));
  return iconAction('검색','search','search')+
    `<span class="home-alert-trigger">${iconAction(unread?'알림 · 읽지 않은 항목 있음':'알림','bell-connected','alerts',true)}${unread?'<span class="home-alert-dot" aria-hidden="true"></span>':''}</span>`+
    globalMenu(state,'home');
}
function chatHeaderActions(state,id) {
  const muted=Boolean(state.mutedThreads?.[id]);
  return `<button type="button" class="seed-icon-action chat-header-mute" data-muted="${muted}" aria-pressed="${muted}" aria-label="${muted?'대화 알림 켜기':'대화 알림 끄기'}" data-chat-ui="mute">${icon('bell')}</button><details class="chat-header-menu"><summary class="seed-icon-action" aria-label="대화방 메뉴">${icon('menu')}</summary><div class="chat-header-menu__panel"><button type="button" data-chat-ui="room-info">${icon('info')}대화방 정보</button>${id==='ai'?'':`<button type="button" data-chat-ui="clear">${icon('close')}대화 내용 비우기</button>`}</div></details>`;
}
function render() {
  const state=store.read();
  if(route.name!=='home'&&alertSheet?.open)alertSheet.close();
  if(route.name!=='chat'){get('#chat-room-info')?.close?.();get('#chat-photo-viewer')?.close?.();get('#chat-message-sheet')?.close?.();}
  const type=applyPageType(shell,route.type);
  header.dataset.navRole=type.header;
  header.dataset.titleAlign=route.align||'center';
  shell.dataset.screen=route.name;
  const titleElement=get('.seed-top-nav__title');
  titleElement.textContent=route.title;
  if(route.name==='chat'&&threads[route.id]) {
    // Standard Start + P-HIERARCHY Middle, using the existing avatar composer.
    const contact=threads[route.id];
    const image=document.createElement('span');
    image.className='chat-header__avatar';
    image.setAttribute('aria-hidden','true');
    if(route.id==='clan') {
      image.innerHTML=icon('clan-bookmark-fill',true);
    } else {
      const face=document.createElement('span');
      face.dataset.avatar='fluid';
      face.dataset.avatarItems=(contact.avatarEquipment||[]).join(',');
      image.append(face);
    }
    const info=document.createElement('span');
    info.className='chat-header__info';
    const name=document.createElement('span');
    name.className='chat-header__name';
    name.textContent=route.title;
    const description=document.createElement('small');
    description.className='chat-header__meta';
    description.textContent=route.id==='clan'?'멤버 '+clan.current.length+'명':route.id==='ai'?'AI 퀘스트 도우미':'개인 대화';
    info.append(name,description);
    titleElement.replaceChildren(image,info);
  }
  document.title=route.title+' · 갓생패치';
  leading.innerHTML=type.header==='standard'?iconAction('뒤로 가기','left','back'):'';
  if(route.name==='home') {
    trailing.innerHTML=homeActions(state);
  } else if(route.name==='chat') {
    trailing.innerHTML=route.id==='clan'&&state.clanJoined===false?'':chatHeaderActions(state,route.id);
  } else if(route.name==='dm') {
    trailing.innerHTML=globalMenu(state,'dm');
  } else if(route.name==='quests') {
    trailing.innerHTML=iconAction('검색','search','search')+iconAction('퀘스트 만들기','plus','new-quest');
  } else if(route.name==='clan') {
    trailing.innerHTML=state.clanJoined===false?'':iconAction('클랜 대화','chat','clan-chat');
  } else if(route.name==='me'||route.name==='shop') {
    trailing.innerHTML=cash(state.cash);
  } else trailing.replaceChildren();
  trailing.dataset.slotKind=route.name==='me'||route.name==='shop'?'info':trailing.childElementCount?'action':'empty';
  if(type.bottomNav)selectWebNav(nav,route.tab);
  main.innerHTML=renderPage(state,route,ui);
  shell.querySelectorAll('[data-avatar]').forEach(element=>{
    const itemIds=element.dataset.avatarItems;
    const equipmentIds=itemIds===undefined?state.equipped:itemIds.split(',').filter(Boolean).map(Number);
    const decorative=Boolean(element.closest('.dm-conversation__avatar, .chat-header__avatar, .chat-row__avatar'));
    mountMascotAvatar(element,{equipmentIds,scale:element.dataset.avatar,label:route.name==='chat'?route.title+' 마스코트':'루원의 마스코트',decorative});
  });
  if(route.name==='chat') {
    const history=main.querySelector?.('.chat-history');
    if(history)history.scrollTop=history.scrollHeight;
  }
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
  if(['edit-message','delete-message','clear-chat'].includes(action))ui.editMessage=null;
  if(['edit-message','delete-message','clear-chat'].includes(action))get('#chat-message-sheet')?.close?.();
  render();
  const sameControl=[...main.querySelectorAll('[data-action]')].find(button=>button.dataset.action===action&&button.dataset.id===id);
  if(focusTarget) get(focusTarget)?.focus({preventScroll:true});
  else (sameControl||main).focus({preventScroll:true});
  if(action==='message'||action==='send-photo') {
    const history=main.querySelector?.('.chat-history');
    if(history)history.scrollTop=history.scrollHeight;
  }
  if(feedback[action])showFeedback(feedback[action]);
}
document.addEventListener('click',event=>{
  if(!event.target.closest('.home-nav-menu'))get('.home-nav-menu[open]')?.removeAttribute('open');
  // Only the interacted-with chat menu may remain open. Previous logic left
  // every message menu open when another message menu was tapped.
  if(!event.target.closest('.chat-header-menu'))get('.chat-header-menu[open]')?.removeAttribute('open');
  if(event.target===get('#chat-message-sheet')) {
    const box=event.target.getBoundingClientRect();
    if(event.clientY<box.top||event.clientX<box.left||event.clientX>box.right)event.target.close();
    return;
  }
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
  if(route.name==='chat'&&control.dataset.chatUi) {
    const action=control.dataset.chatUi,index=Number(control.dataset.index);
    const history=store.read().messages[route.id]||[];
    if(action==='message-options') {
      if(route.id==='ai'||!Number.isInteger(index)||history[index]?.from!=='me')return;
      const own=history[index],sheet=get('#chat-message-sheet');
      get('#chat-message-sheet-title').textContent='메시지';
      const options=[];
      if(own.image) options.push(`<button type="button" data-chat-ui="view-photo" data-index="${index}">${icon('info')}사진 보기</button>`);
      else options.push(`<button type="button" data-chat-ui="edit" data-index="${index}">${icon('edit')}수정</button>`);
      options.push(`<button type="button" data-chat-ui="delete" data-index="${index}">${icon('close')}삭제</button>`);
      get('#chat-message-sheet-actions').innerHTML=options.join('');
      sheet.showModal();
    } else if(action==='mute')apply('toggle-chat-notifications',route.id);
    else if(action==='room-info') {
      get('#chat-room-info-body').textContent=route.id==='clan'?'시연 멤버: '+clan.current.map(member=>member.name).join(' · '):route.id==='ai'?'목표를 퀘스트로 제안하는 AI 대화입니다.':threads[route.id].title+'님과의 개인 대화입니다.';
      get('#chat-room-info').showModal();
    } else if(action==='clear') {
      if(window.confirm('이 기기에 저장된 대화 내용을 모두 지울까요?'))apply('clear-chat',route.id);
    } else if(action==='attach')get('#chat-photo-file')?.click();
    else if(action==='edit'&&route.id!=='ai'&&history[index]?.from==='me'&&!history[index]?.image) {
      get('#chat-message-sheet')?.close?.();
      ui.editMessage=route.id+':'+index;
      render();get('#chat-edit-'+index)?.focus();
    } else if(action==='cancel-edit') {
      ui.editMessage=null;render();get('#message')?.focus();
    } else if(action==='delete'&&route.id!=='ai'&&history[index]?.from==='me') {
      get('#chat-message-sheet-title').textContent='메시지를 삭제할까요?';
      get('#chat-message-sheet-actions').innerHTML=
        `<button type="button" data-chat-ui="confirm-delete" data-index="${index}">${icon('close')}삭제</button>`+
        `<button type="button" data-chat-ui="close-message-sheet">취소</button>`;
    } else if(action==='confirm-delete'&&route.id!=='ai'&&history[index]?.from==='me') {
      get('#chat-message-sheet')?.close?.();
      apply('delete-message',route.id,{index});
    } else if(action==='close-message-sheet') {
      get('#chat-message-sheet')?.close?.();
    }
    else if(action==='view-photo') {
      const source=history[index]?.image?.src;
      if(typeof source==='string'&&/^data:image\/(?:jpeg|png|webp);base64,/.test(source)){
        get('#chat-message-sheet')?.close?.();
        get('#chat-photo-viewer-image').src=source;get('#chat-photo-viewer').showModal();
      }
    }
    return;
  }
  if(control.dataset.shellAction==='close-chat-room-info'){get('#chat-room-info')?.close();return;}
  if(control.dataset.shellAction==='close-chat-photo'){get('#chat-photo-viewer')?.close();return;}
  if(control.dataset.shellAction==='close-chat-message-sheet'){get('#chat-message-sheet')?.close();return;}
  if(route.name==='clan'&&control.dataset.clanView) {
    const next=control.dataset.clanView;
    if(['mine','insights','shop'].includes(next)){ui.clanView=next;render();get('#clan-tab-'+next)?.focus({preventScroll:true});}
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
  } else if(action==='clan-leave'&&route.name==='clan') {
    if(window.confirm('시연 클랜에서 나갈까요? 기록은 유지돼요.')){ui.clanView='mine';apply(action,id);}
  } else if(action==='clan-join'&&route.name==='clan') {
    ui.clanView='mine';apply(action,id);
  } else if(action) apply(action,id);
});
document.addEventListener('toggle',event=>{
  // details also opens via keyboard and programmatic interaction.
  const target=event.target;
  if(!target.matches?.('.chat-header-menu')||!target.open)return;
},true);
document.addEventListener('input',event=>{
  if(event.target.id==='message'&&route.name==='chat') {
    const field=event.target;
    if(field.style&&Number.isFinite(field.scrollHeight)) {
      field.style.height='auto';
      const natural=field.scrollHeight;
      field.style.height=Math.max(38,Math.min(natural,90))+'px';
      field.style.overflowY=natural>90?'auto':'hidden';
    }
    const send=field.form?.querySelector?.('.chat-composer__send');
    if(send)send.disabled=!field.value.trim();
    return;
  }
  if(event.target.id==='dm-search') {
    ui.dmSearch=event.target.value.slice(0,80);
    const query=ui.dmSearch.trim().toLocaleLowerCase('ko');
    const category=ui.dmFilter??'all';
    let visible=0;
    main.querySelectorAll('[data-dm-search]').forEach(row=>{
      row.hidden=(category!=='all'&&row.dataset.dmKind!==category)||(Boolean(query)&&!row.dataset.dmSearch.includes(query));
      if(!row.hidden)visible++;
    });
    const empty=main.querySelector('.dm-inbox__empty');
    if(empty){empty.hidden=visible>0;empty.textContent=query?'검색 결과가 없어요':'해당하는 대화가 없어요';}
    return;
  }
  if(event.target.id!=='home-search')return;
  ui.search=event.target.value;
  get('#home-search-results').innerHTML=searchResults(store.read(),ui.search,route.tab);
});
document.addEventListener('keydown',event=>{
  if(route.name==='clan'&&event.target?.matches?.('[role="tab"][data-clan-view]')&&['ArrowLeft','ArrowRight','Home','End'].includes(event.key)) {
    const order=['mine','insights','shop'],idx=order.indexOf(event.target.dataset.clanView);
    const chosen=event.key==='Home'?0:event.key==='End'?2:(idx+(event.key==='ArrowRight'?1:2))%3;
    event.preventDefault();ui.clanView=order[chosen];render();get('#clan-tab-'+ui.clanView)?.focus({preventScroll:true});return;
  }
  if(event.key==='Enter'&&route.name==='chat'&&event.target?.id==='message'&&!event.shiftKey&&!event.isComposing&&event.keyCode!==229) {
    event.preventDefault();
    if(event.target.value.trim())event.target.form?.requestSubmit?.();
    return;
  }
  if(event.key==='Escape'&&ui.editMessage&&route.name==='chat'){ui.editMessage=null;render();get('#message')?.focus();return;}
  if(event.key!=='Escape')return;
  const menu=get('.home-nav-menu[open]')||get('.chat-header-menu[open]');
  if(!menu)return;
  menu.open=false;
  menu.querySelector('summary').focus();
});
document.addEventListener('submit',event=>{
  const form=event.target;
  if(!form.dataset.form)return;
  event.preventDefault();
  if(form.dataset.form==='edit-message'){
    apply('edit-message',form.dataset.id,{index:Number(form.dataset.index),text:new FormData(form).get('value')});return;
  }
  apply(form.dataset.form,form.dataset.id,new FormData(form).get('value'),form.dataset.form==='message'?'#message':undefined);
});
// Browser-only image attachment: locally compressed, never uploaded to a remote user.
async function compressChatPhoto(file) {
  if(!/^image\/(?:jpeg|png|webp)$/.test(file?.type||'')||file.size>12*1024*1024)throw Error('JPEG·PNG·WebP 사진(12MB 이하)만 선택할 수 있어요.');
  const url=URL.createObjectURL(file);
  try {
    const image=await new Promise((resolve,reject)=>{
      const img=new Image();img.onload=()=>resolve(img);img.onerror=()=>reject(new Error('사진을 읽지 못했어요.'));img.src=url;
    });
    const canvas=document.createElement('canvas'),scale=Math.min(1,960/Math.max(image.naturalWidth,image.naturalHeight));
    canvas.width=Math.max(1,Math.round(image.naturalWidth*scale));canvas.height=Math.max(1,Math.round(image.naturalHeight*scale));
    const ctx=canvas.getContext('2d');if(!ctx)throw Error('사진을 처리하지 못했어요.');
    ctx.fillStyle='#fff';ctx.fillRect(0,0,canvas.width,canvas.height);ctx.drawImage(image,0,0,canvas.width,canvas.height);
    let data=canvas.toDataURL('image/jpeg',.7);
    if(data.length>900000){
      const smaller=document.createElement('canvas'),ratio=Math.min(1,650/Math.max(canvas.width,canvas.height));
      smaller.width=Math.max(1,Math.round(canvas.width*ratio));smaller.height=Math.max(1,Math.round(canvas.height*ratio));
      const small=smaller.getContext('2d');small.fillStyle='#fff';small.fillRect(0,0,smaller.width,smaller.height);small.drawImage(canvas,0,0,smaller.width,smaller.height);
      data=smaller.toDataURL('image/jpeg',.6);
    }
    if(data.length>900000)throw Error('사진이 너무 커요. 다른 사진을 선택해 주세요.');
    return data;
  } finally{URL.revokeObjectURL(url);}
}
document.addEventListener('change',async event=>{
  if(event.target.id!=='chat-photo-file'||route.name!=='chat')return;
  const element=event.target,file=element.files?.[0],chatId=route.id;element.value='';
  if(!file)return;
  try {
    const src=await compressChatPhoto(file);
    if(route.name!=='chat'||route.id!==chatId)return;
    if(!store.dispatch('send-photo',chatId,{src})){showFeedback('사진을 저장하지 못했어요. 기기 저장 공간을 확인해 주세요.');return;}
    render();get('#message')?.focus({preventScroll:true});showFeedback('이 기기의 시연 대화에 사진을 저장했어요.');
  }catch(error){showFeedback(error?.message||'사진을 처리하지 못했어요.');}
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
