import {books,catalog,chestLoot,clan,homeAlerts,rewards,threads} from './mock-data.mjs';
import {clanRows,clanTotal} from './store.mjs';
import {MASCOT_EQUIPMENT} from '../../seed/runtime/seed-mascot-avatar.mjs';

const escape=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const fmt=value=>new Intl.NumberFormat('ko-KR').format(value);
const statuses={offered:'제안됨',active:'진행 중',deferred:'미룸',done:'완료',rejected:'거절함'};
const filterNames={all:'전체',active:'진행 중',offered:'제안',done:'완료'};
const types=['전체','모자','오디오'];

export const icon=(name,preview=false)=>`<span class="seed-icon" aria-hidden="true" style="--seed-icon:url('../../assets/icons/${preview?'preview':'soft/v0.14'}/${name}.svg')"></span>`;
export const cash=(amount,size='small')=>`<span class="seed-credit" data-size="${size}" role="img" aria-label="${fmt(amount)} 캐시"><img class="seed-credit__coin" src="${rewards}cash-coin-single.png" alt=""><span class="seed-credit__value">${fmt(amount)}</span></span>`;
const title=(label,style='section',tag='h2')=>`<${tag} class="seed-text" data-text-role="section" data-text-style="${style}">${escape(label)}</${tag}>`;
const support=text=>`<p class="seed-text" data-text-role="caption" data-text-style="support" data-text-tone="secondary">${escape(text)}</p>`;
const badge=(label,tone='neutral')=>`<span class="seed-badge" data-tone="${tone}" data-size="xs">${escape(label)}</span>`;
const path=(url,tab)=>`#/${url}${tab?'?tab='+tab:''}`;
const actionFace=label=>`<span class="seed-action__face"><span class="seed-action__label">${escape(label)}</span></span>`;
const act=(label,name,id,variant='brand-solid',extra='')=>`<button type="button" class="seed-action" data-variant="${variant}" data-size="default" data-action="${name}" data-id="${escape(id)}" ${extra}>${actionFace(label)}</button>`;
const cta=(label,url,tab,variant='brand-solid')=>`<a class="seed-action" data-variant="${variant}" data-size="default" href="${path(url,tab)}">${actionFace(label)}</a>`;
const submit=label=>`<button type="submit" class="seed-action" data-variant="brand-solid" data-size="default">${actionFace(label)}</button>`;
const frame=(src,label='')=>`<div class="seed-media" data-media-kind="item" data-media-fit="contain" data-media-state="loaded"><img class="seed-media__image" src="${src}" alt="${escape(label)}"><span class="seed-media__fallback">${escape(label||'이미지를 불러올 수 없어요')}</span></div>`;
const rewardLine=q=>`<span class="reward-line">${q.xp?`<span class="xp"><img src="${rewards}xp-gem-single.png" alt="">${q.xp} XP</span>`:''}${cash(q.cash)}</span>`;
const list=(content,label)=>`<div class="preview-list" aria-label="${escape(label)}">${content}</div>`;
const section=(heading,content,more='')=>`<section class="seed-page-shell__section"><div class="section-heading">${title(heading)}${more}</div>${content}</section>`;
const more=(label,url,tab)=>`<a class="quiet-link" href="${path(url,tab)}">${escape(label)} ${icon('chevron')}</a>`;
function row({label,sub,url,tab,leading='',trailing='',chevron=true,alertId}) {
  return `<a class="seed-list__item" data-interaction="navigate" href="${path(url,tab)}" ${alertId?`data-alert-id="${escape(alertId)}"`:''}>${leading}${`<div class="row-copy">${title(label,'item','span')}${sub?support(sub):''}</div>`}${trailing}${chevron?icon('chevron'):''}</a>`;
}
const questRow=(q,tab)=>row({label:q.title,sub:`${q.kind} · ${q.status==='done'?'완료':q.due}`,url:'quest/'+q.id,tab,
  leading:`<span class="row-icon">${icon(q.status==='done'?'check-circle':q.kind==='메인'?'flag':'target')}</span>`,trailing:rewardLine(q)});
const empty=text=>`<div class="empty">${support(text)}</div>`;
const progress=(value,max,label,thickness='default')=>`<div class="progress-track" data-thickness="${thickness}" role="progressbar" aria-label="${escape(label)}" aria-valuemin="0" aria-valuemax="${max}" aria-valuenow="${Math.min(value,max)}" style="--progress:${Math.min(value/max*100,100)}%"><span class="progress-fill"></span></div>`;
const chips=(entries,selected,name)=>`<div class="filter-row" role="group" aria-label="${name==='shop'?'상품 종류':'퀘스트 상태'}">${entries.map(([value,label])=>`<button class="seed-filter-chip" type="button" data-filter="${name}" data-value="${value}" aria-pressed="${value===selected}">${label}</button>`).join('')}</div>`;
const segment=period=>`<div class="seed-segment" role="group" aria-label="기여 기간">${[['current','이번 주'],['previous','지난주']].map(([key,label])=>`<button type="button" data-period="${key}" aria-pressed="${key===period}">${label}</button>`).join('')}</div>`;
const avatar=(scale='hero')=>`<div data-avatar="${scale}"></div>`;

function home(state) {
  const today=state.quests.filter(q=>q.kind==='일일'&&q.due==='오늘 23:59'&&q.status!=='rejected');
  const dailyDone=today.filter(q=>q.status==='done').length;
  const active=state.quests.filter(q=>q.status==='active'&&q.kind!=='보너스');
  const featured=today.find(q=>q.status==='offered')||active.find(q=>q.kind==='일일')||active[0];
  const queue=active.filter(q=>q!==featured);
  const bonus=state.quests.find(q=>q.id==='bonus'&&!['done','rejected'].includes(q.status));
  const ready=Object.entries(chestLoot).filter(([id])=>state.boxes[id]==='ready');
  const latestDone=state.quests.filter(q=>q.status==='done'&&!q.baseline);
  const dailyProgress=today.length?`<div class="home-daily-progress">
    <span><strong>${dailyDone}</strong> / ${today.length} 완료</span>${progress(dailyDone,today.length,'오늘의 일일 퀘스트','micro')}</div>`:'';
  const mission=featured?`<article class="seed-surface home-mission" data-surface="solid">
    <div class="home-mission-meta">${badge(featured.status==='offered'?'일일 퀘스트 제안':featured.kind+' 퀘스트 · 진행 중','brand')}
      <span class="home-deadline">${icon('clock')}${escape(featured.due)}</span></div>
    <div class="home-mission-copy">${title(featured.title,'hero')}${support(featured.condition)}</div>
    <div class="home-mission-footer">
      <div class="home-payout">${support('완료 보상')}${rewardLine(featured)}</div>
      ${cta(featured.status==='offered'?'제안 보기':'이어서 하기','quest/'+featured.id,'home')}
    </div>
    <img class="home-mission-art" src="${rewards}xp-gems-cluster.png" alt="">
  </article>`:`<article class="seed-surface home-finish" data-surface="subtle">
    ${title(latestDone.length?'오늘의 도전을 마쳤어요':'오늘 할 도전을 정해볼까요?','hero')}
    ${latestDone.length?support('완료 '+latestDone.length+'개 · +'+fmt(latestDone.reduce((xp,q)=>xp+q.xp,0))+' XP'):''}
    ${cta('퀘스트 만들기','chat/ai','home','neutral-outline')}
  </article>`;
  const queueRows=queue.length?list(queue.slice(0,3).map(q=>questRow(q,'home')).join(''),'진행 중')
    :empty(featured?.status==='active'?'이 도전을 마치면 여기에서 다음 도전을 이어갈 수 있어요.':'수락한 퀘스트가 여기에 표시돼요.');
  const bonusCard=bonus?`<a class="seed-surface home-bonus" data-surface="reward" href="${path('quest/bonus','home')}">
    <div class="home-bonus-copy"><div class="home-bonus-meta">${badge(bonus.status==='active'?'참여 중':'기간 한정','warning')}
      <span class="home-deadline">${escape(bonus.due)}</span></div>
      ${title(bonus.title,'item')}${cash(bonus.cash)}
    </div><img class="home-bonus-art" src="${rewards}cash-coins-pouch.png" alt="">${icon('chevron')}
  </a>`:'';
  const inbox=ready.length?row({
    label:ready.length===1?ready[0][1].name:'열 수 있는 상자 '+ready.length+'개',
    sub:'열어서 캐시와 장식 받기',url:'rewards',tab:'home',
    leading:`<img class="home-chest" src="${rewards+ready[0][1].image}" alt="">`,
    trailing:badge('열기','reward'),
  }):'';
  return section('오늘의 도전',mission,dailyProgress)
    +section('진행 중',queueRows,more('모든 퀘스트','quests'))
    +(bonusCard?section('보너스 퀘스트',bonusCard):'')
    +(inbox?section('받을 보상',list(inbox,'받을 보상'),more('보상함','rewards','home')):'');
}
function quests(state,ui) {
  const items=state.quests.filter(q=>ui.quest==='all'?q.status!=='rejected':q.status===ui.quest);
  const book=books[0];
  const done=state.quests.filter(q=>q.book===book.id&&q.status==='done').length;
  return `<section class="seed-page-shell__section quest-browser">${chips(Object.entries(filterNames),ui.quest,'quest')}${items.length?list(items.map(q=>questRow(q,'quests')).join(''),'퀘스트 목록'):empty('이 상태의 퀘스트가 없어요.')}</section>`
  +section('퀘스트북',`<a class="seed-surface book-card" data-surface="outlined" href="${path('book/'+book.id,'quests')}"><div class="book-title"><span class="row-icon">${icon('book')}</span><div class="row-copy">${title(book.title,'item')}${support('1장 · '+done+' / '+book.quests.length+' 완료')}</div>${icon('chevron')}</div>${progress(done,book.quests.length,'퀘스트북 진행')}</a>`);
}
function detail(state,route) {
  const q=state.quests.find(item=>item.id===route.id);
  if(!q) return empty('이 퀘스트를 찾을 수 없어요.');
  const questSummary=`<article class="seed-surface detail-panel" data-surface="solid">
    <div class="hero-meta">${badge(q.kind,q.kind==='보너스'?'warning':'brand')}${badge(statuses[q.status],q.status==='done'?'success':'neutral')}</div>
    ${title(q.title,'hero')}${support(q.due)}<div class="quest-condition">${title('완료 조건','item','h3')}<p>${escape(q.condition)}</p></div>
    <div class="detail-reward"><span>완료 보상</span>${rewardLine(q)}</div></article>`;
  const actions={
    offered:act('수락하기','accept',q.id)+act('거절','reject',q.id,'ghost'),
    active:q.kind==='보너스'?`<form class="entry-form" data-form="complete" data-id="${q.id}"><label for="proof">어디에서 찾았나요?</label><textarea id="proof" class="seed-field" name="value" rows="3" maxlength="160" required></textarea>${submit('기록 올리고 완료')}</form>`:act('완료하기','complete',q.id)+act('미루기','defer',q.id,'ghost'),
    deferred:act('다시 시작','resume',q.id),
    done:!q.baseline&&q.kind!=='보너스'?act('완료 취소','undo',q.id,'ghost',state.cash<q.cash?'disabled':''):'',
  };
  const posts=q.kind==='보너스'?section('참여 기록',`<div class="preview-list">${state.bonusPosts.map((post,i)=>`<div class="seed-list__item"><div class="row-copy">${title(post.name,'item','span')}${support(post.text)}</div>${post.name==='루원'?support('확인 '+post.count):act('확인 '+post.count,'cheer',i,'ghost','aria-pressed="'+post.cheered+'"')}</div>`).join('')}</div>`):'';
  return questSummary+(actions[q.status]?`<div class="page-actions">${actions[q.status]}</div>`:'')+posts;
}
function book(state,route) {
  const item=books.find(book=>book.id===route.id);
  if(!item)return empty('이 퀘스트북을 찾을 수 없어요.');
  const tasks=item.quests.map(id=>state.quests.find(q=>q.id===id)).filter(Boolean);
  const done=tasks.filter(q=>q.status==='done').length;
  return `<article class="seed-surface detail-panel" data-surface="subtle">${title(item.title,'hero')}${support(item.goal)}${progress(done,tasks.length,'목표 진행')}${support(done+' / '+tasks.length+' 완료')}</article>`+section(item.chapter,list(tasks.map(q=>questRow(q,route.tab)).join(''),'챕터 퀘스트'));
}
function dm(state) {
  const message=id=>state.messages[id].at(-1)?.text;
  return `<a class="seed-surface dm-start" data-surface="subtle" href="${path('chat/ai','dm')}"><span class="row-icon">${icon('sparkle')}</span><div class="row-copy">${title('퀘스트 만들기','item')}${support('목표를 말하면 오늘 할 도전을 제안해요.')}</div>${icon('chevron')}</a>`
    +section('대화',list(['friend','clan'].map(id=>row({label:threads[id].title,sub:message(id),url:'chat/'+id,tab:'dm',leading:`<span class="row-avatar">${threads[id].initials}</span>`})).join(''),'대화 목록'));
}
function chat(state,route) {
  if(!Object.hasOwn(threads,route.id))return empty('대화방을 찾을 수 없어요.');
  const ai=route.id==='ai';
  return `<section class="thread" aria-label="대화 기록">${state.messages[route.id].map(msg=>{
    const q=state.quests.find(item=>item.id===msg.quest);
    return `<div class="bubble-row" data-from="${msg.from}"><div class="seed-surface bubble" data-surface="${msg.from==='me'?'subtle':'solid'}"><p>${escape(msg.text)}</p>
      ${q?`<div class="chat-quest">${title(q.title,'item','h3')}${support(q.condition)}${rewardLine(q)}${cta(q.status==='offered'?'제안 보기':'퀘스트 보기','quest/'+q.id,route.tab,'neutral-outline')}</div>`:''}</div></div>`;
  }).join('')}</section><form class="entry-form composer" data-form="message" data-id="${route.id}"><label class="visually-hidden" for="message">${ai?'목표 또는 수정 요청':'메시지'}</label><input id="message" class="seed-field" name="value" required maxlength="300" placeholder="${ai?'목표나 바꾸고 싶은 점 입력':'메시지 입력'}">${submit('보내기')}</form>`;
}
function rankings(state,period,count) {
  return list(clanRows(state,period).slice(0,count).map((member,i)=>`<div class="seed-list__item"><span class="rank">${i+1}</span><span class="row-avatar">${member.name[0]}</span><span class="row-copy">${title(member.name,'item','span')}</span><strong class="rank-xp">${fmt(member.xp)} XP</strong></div>`).join(''),'클랜 기여 순위');
}
function clanHome(state,ui) {
  const total=clanTotal(state,ui.period);
  const claimable=ui.period==='current'&&total>=clan.goal&&state.boxes.clan==='locked';
  return `<article class="seed-surface clan-panel" data-surface="solid">
    <div class="clan-heading"><span class="row-icon">${icon('flag')}</span><div class="row-copy">${title(clan.name,'hero')}${support('멤버 '+clan.current.length+'명')}</div>${more('대화','chat/clan','clan')}</div>
    ${segment(ui.period)}<div class="clan-reward"><div>${title('주간 상자','item','h3')}<div class="clan-score"><strong>${fmt(total)}</strong><span> / ${fmt(clan.goal)} XP</span></div></div><img src="${rewards}08_premium_glowing_chest.png" alt=""></div>
    ${progress(total,clan.goal,'주간 클랜 상자')}${claimable?act('상자 받기','claim','clan'):ui.period==='current'&&state.boxes.clan!=='locked'?more('보상함','rewards','clan'):support(total>=clan.goal?'목표 달성':'상자까지 '+fmt(clan.goal-total)+' XP')}</article>`
    +section('클랜원 기여',rankings(state,ui.period,3),more('전체 순위','ranking','clan'));
}
function me(state) {
  const level=1+Math.floor(state.xp/250);
  const boxes=Object.values(state.boxes).filter(x=>x==='ready').length;
  return `<section class="seed-surface profile-panel" data-surface="subtle"><div>${title('루원','hero')}${support('Lv. '+level+' · '+fmt(state.xp)+' XP')}${progress(state.xp%250,250,'다음 레벨까지')}${cta('꾸미기','decorate','me','neutral-outline')}</div>${avatar()}</section>`+
    section('내 보상',list([
      row({label:'보상함',sub:boxes?boxes+'개 열 수 있어요':'획득한 보상 보기',url:'rewards',tab:'me',leading:`<span class="row-icon">${icon('gift')}</span>`}),
      row({label:'상점',sub:'마스코트에 장착할 아이템',url:'shop',tab:'me',leading:`<span class="row-icon">${icon('shopping-bag')}</span>`}),
    ].join(''),'보상 목적지'));
}
function shop(state,ui) {
  const feature=catalog.find(item=>item.id===1001);
  const featured=ui.shop==='전체'?`<article class="seed-item-display seed-surface featured-item" data-surface="solid" data-item-presentation="integrated" data-item-layout="horizontal">
      ${frame(MASCOT_EQUIPMENT[feature.id].src,feature.name)}<div class="seed-item-display__info">${title(feature.name,'item','h3')}${cash(feature.price)}<div class="seed-item-display__action">${cta('상품 보기','item/'+feature.id,'me','neutral-outline')}</div></div></article>`:'';
  const items=catalog.filter(x=>ui.shop==='전체'?x!==feature:x.type===ui.shop);
  return `<div class="shop-balance"><span>보유 캐시</span>${cash(state.cash)}</div>`
    +(featured?section('추천 상품',featured):'')
    +`<section class="seed-page-shell__section shop-list">${chips(types.map(x=>[x,x]),ui.shop,'shop')}<div class="item-grid">
      ${items.map(item=>`<a class="seed-item-display" data-item-presentation="open" href="${path('item/'+item.id,'me')}">${frame(MASCOT_EQUIPMENT[item.id].src,item.name)}
      <div class="seed-item-display__info">${title(item.name,'item','h3')}${state.owned.includes(item.id)?support('보유 중'):cash(item.price)}</div></a>`).join('')}</div></section>`;
}
function itemDetail(state,route) {
  const item=catalog.find(product=>product.id===Number(route.id));
  if(!item)return empty('상품을 찾을 수 없어요.');
  const owned=state.owned.includes(item.id);
  return `<article class="seed-surface detail-panel" data-surface="solid">${frame(MASCOT_EQUIPMENT[item.id].src,item.name)}${title(item.name,'hero')}${item.description?support(item.description):''}${cash(item.price,'large')}</article><div class="page-actions">${owned?cta('꾸미기','decorate','me'):act(state.cash>=item.price?'구매하기':'캐시가 부족해요','purchase',item.id,'brand-solid',state.cash<item.price?'disabled':'')}</div>`;
}
function decorate(state) {
  return `<section class="seed-surface dress-stage" data-surface="subtle">${avatar('fluid')}</section>`+section('보유 아이템',`<div class="item-grid">${state.owned.map(id=>{
    const item=MASCOT_EQUIPMENT[id];
    if(!item)return '';
    const equipped=state.equipped.includes(id);
    return `<article class="dress-item">${frame(item.src,item.name)}${title(item.name,'item','h3')}${act(equipped?'해제':'장착','equip',id,equipped?'neutral-weak':'neutral-outline','aria-pressed="'+equipped+'"')}</article>`;
  }).join('')}</div>`);
}
function rewardRoom(state) {
  return Object.entries(chestLoot).filter(([id])=>state.boxes[id]!=='locked').map(([id,loot])=>{
    const opened=state.boxes[id]==='opened';
    return `<article class="seed-surface reward-box" data-surface="reward">${frame(rewards+loot.image,loot.name)}<div class="row-copy">
    ${title(loot.name,'item')}${opened?`<div class="loot">${cash(loot.cash)}${support(MASCOT_EQUIPMENT[loot.item].name+' 획득')}</div>`:act('상자 열기','open',id,'reward-secondary')}</div></article>`;
  }).join('')||empty('받은 보상이 없어요.');
}

export function renderHomeAlerts(state) {
  return list(homeAlerts.map(alert=>row({
    label:alert.title,sub:alert.sub,url:alert.url,tab:'home',alertId:alert.id,
    leading:`<span class="row-icon">${icon(alert.icon)}</span>`,
    trailing:(state.readAlerts??[]).includes(alert.id)?'':'<span class="home-alert-dot" aria-label="읽지 않음"></span>',
  })).join(''),'홈 알림');
}

export function searchResults(state,query) {
  const term=query.trim().toLocaleLowerCase('ko');
  if(!term)return '';
  const matches=text=>text.toLocaleLowerCase('ko').includes(term);
  const quests=state.quests.filter(q=>q.status!=='rejected'&&matches(q.title));
  const foundBooks=books.filter(book=>matches(book.title));
  const rows=quests.map(q=>questRow(q,'home')).join('')+foundBooks.map(book=>row({
    label:book.title,sub:'퀘스트북',url:'book/'+book.id,tab:'home',leading:`<span class="row-icon">${icon('book')}</span>`,
  })).join('');
  return rows?list(rows,'검색 결과'):empty('일치하는 퀘스트나 퀘스트북이 없어요.');
}

export function renderPage(state,route,ui) {
  switch(route.name) {
    case 'home':return home(state);
    case 'search':return `<section class="seed-page-shell__section"><label class="visually-hidden" for="home-search">퀘스트나 퀘스트북 검색</label><input class="seed-field" id="home-search" type="search" placeholder="퀘스트나 퀘스트북 검색" autocomplete="off" maxlength="80" value="${escape(ui.search??'')}"><div id="home-search-results" aria-live="polite">${searchResults(state,ui.search??'')}</div></section>`;
    case 'quests':return quests(state,ui);
    case 'dm':return dm(state);
    case 'clan':return clanHome(state,ui);
    case 'me':return me(state);
    case 'quest':return detail(state,route);
    case 'book':return book(state,route);
    case 'chat':return chat(state,route);
    case 'ranking':return segment(ui.period)+section('기여 순위',rankings(state,ui.period));
    case 'shop':return shop(state,ui);
    case 'item':return itemDetail(state,route);
    case 'decorate':return decorate(state);
    case 'rewards':return rewardRoom(state);
    default:return empty('페이지를 찾을 수 없어요.');
  }
}
