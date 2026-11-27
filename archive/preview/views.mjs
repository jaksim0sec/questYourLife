import {books,catalog,chestLoot,clan,homeAlerts,homeTrends,rewards,threads} from './mock-data.mjs?v=clan-crest-v14';
import {clanRows,clanTotal} from './store.mjs?v=clan-crest-v14';
import {MASCOT_EQUIPMENT} from '../../seed/runtime/seed-mascot-avatar.mjs';

const escape=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const fmt=value=>new Intl.NumberFormat('ko-KR').format(value);
const statuses={offered:'제안됨',active:'진행 중',deferred:'미룸',done:'완료',rejected:'거절함'};
const filterNames={all:'전체',active:'진행 중',offered:'제안',deferred:'미룸',done:'완료'};
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
const chips=(entries,selected,name)=>`<div class="filter-row" role="group" aria-label="${name==='shop'?'상품 종류':name==='dressFilter'?'보유 장식 종류':'퀘스트 상태'}">${entries.map(([value,label])=>`<button class="seed-filter-chip" type="button" data-filter="${name}" data-value="${value}" aria-pressed="${value===selected}">${label}</button>`).join('')}</div>`;
const segment=period=>`<div class="seed-segment" role="group" aria-label="기여 기간">${[['current','이번 주'],['previous','지난주']].map(([key,label])=>`<button type="button" data-period="${key}" aria-pressed="${key===period}">${label}</button>`).join('')}</div>`;
const avatar=(scale='hero')=>`<div data-avatar="${scale}"></div>`;

function homeActivity(state,today,completed,active,ready,next,heading){
  return `<section class="home-overview" aria-label="내 활동 요약">
    ${heading('내 활동','오늘 한눈에',more('퀘스트 보기','quests'))}
    <div class="seed-surface home-today-summary" data-surface="solid">
      <span class="home-today-summary__symbol">${icon('check-circle')}</span>
      <div class="home-today-summary__info">
        <strong>오늘의 일일 퀘스트</strong>
        <small>${today.length&&completed===today.length?'오늘의 도전을 마쳤어요':'하루의 성취를 쌓아가는 중'}</small>
      </div>
      <span class="home-today-summary__score"><strong>${completed}</strong> / ${today.length}</span>
      <div class="home-today-summary__meter">${today.length?progress(completed,today.length,'오늘의 일일 퀘스트','micro'):support('오늘 등록된 일일 퀘스트가 없어요')}</div>
    </div>
    <div class="home-glance-rail" role="group" aria-label="내 활동 더 살펴보기">
      <a class="home-glance-card" data-tone="blue" href="${path('quests')}">
        <span class="home-glance-card__label">진행 중 ${icon('chevron')}</span>
        <span class="home-glance-card__value"><strong>${active.length}</strong><small>개 퀘스트</small></span>
        <span class="home-glance-card__sub">나의 도전 이어가기</span>
        <span class="home-glance-card__glyph">${icon('target')}</span>
      </a>
      <a class="home-glance-card" data-tone="gold" href="${path('rewards','home')}">
        <span class="home-glance-card__label">보상함 ${icon('chevron')}</span>
        <span class="home-glance-card__value"><strong>${ready.length}</strong><small>개 열 수 있어요</small></span>
        <span class="home-glance-card__sub">${ready.length?'기다리는 보상 확인':'다음 보상 기대하기'}</span>
        <img class="home-glance-card__art" src="${rewards+(ready[0]?.[1].image||chestLoot.starter.image)}" alt="" loading="lazy">
      </a>
      <a class="home-glance-card" data-tone="violet" href="${path('me')}">
        <span class="home-glance-card__label">내 XP ${icon('chevron')}</span>
        <span class="home-glance-card__value"><strong>${fmt(state.xp)}</strong><small>XP</small></span>
        <span class="home-glance-card__sub">지금까지 쌓은 경험치</span>
        <img class="home-glance-card__art" src="${rewards}xp-gem-single.png" alt="" loading="lazy">
      </a>
    </div>
    ${next?`<a class="home-next" href="${path('quest/'+next.id,'home')}"><span class="home-next__icon">${icon(next.status==='offered'?'sparkle':'target')}</span><span><small>${next.status==='offered'?'새 제안':'이어서 하기'}</small><strong>${escape(next.title)}</strong></span>${icon('chevron')}</a>`:`<a class="home-next" href="${path('chat/ai','home')}"><span class="home-next__icon">${icon('sparkle')}</span><span><small>다음 도전</small><strong>퀘스트 만들기</strong></span>${icon('chevron')}</a>`}
  </section>`;
}
function home(state) {
  const today=state.quests.filter(q=>q.kind==='일일'&&q.due.startsWith('오늘')&&['active','offered','done'].includes(q.status));
  const completed=today.filter(q=>q.status==='done').length;
  const active=state.quests.filter(q=>q.status==='active');
  const ready=Object.entries(chestLoot).filter(([id])=>state.boxes[id]==='ready');
  const next=state.quests.find(q=>q.kind==='일일'&&q.status==='offered')||state.quests.find(q=>q.kind==='일일'&&q.status==='active')||active[0];
  const clanScore=clanTotal(state);
  const remaining=Math.max(clan.goal-clanScore,0);
  const clanState=state.boxes.clan;
  const clanHint=clanState==='opened'?'수령 완료':clanState==='ready'?'보상함에 도착':remaining===0?'상자 수령 가능':fmt(remaining)+' XP 남음';
  const friendMessage=state.messages.friend?.at(-1)?.text||'최근 대화가 없어요';
  const heading=(context,label,right='')=>`<div class="home-section-head"><div><h2 class="seed-text" data-text-role="section" data-text-style="section">${label}</h2></div>${right}</div>`;
  const activity=homeActivity(state,today,completed,active,ready,next,heading);
  const trends=homeTrends.map((trend,i)=>{
    const href=path(trend.url,'home');
    if(i===0)return `<a class="seed-surface home-trend-lead" data-surface="media" href="${href}">
      <div class="home-trend-lead__copy">
        <span class="home-trend-lead__kicker">01 · ${escape(trend.kind)}</span>
        <h3>${escape(trend.title)}</h3>
        <p>${escape(trend.description)}</p>
        <span class="home-trend-lead__metric">${escape(trend.metric)} ${icon('chevron')}</span>
      </div>
      <div class="home-trend-lead__visual" aria-hidden="true">
        <span class="home-trend-lead__num">20</span>
        <img src="${rewards}xp-gems-cluster.png" alt="" loading="lazy">
        <span class="home-trend-lead__sub">MIN / ROUTINE</span>
      </div>
    </a>`;
    return `<a class="home-trend-item" href="${href}">
      <span class="home-trend-item__icon" data-index="${i}">${icon(trend.icon)}</span>
      <span class="home-trend-item__text"><strong>${escape(trend.title)}</strong><small>${escape(trend.kind)} · ${escape(trend.metric)}</small></span>
      <span class="home-trend-item__signal">${escape(trend.signal)}</span>${icon('chevron')}
    </a>`;
  }).join('');
  const discovery=`<section class="home-discovery">${heading('트렌드','지금 뜨는 도전','<span class="home-section-aside">시연 참여 지표</span>')}<div class="home-discovery__items">${trends}</div></section>`;
  const social=state.clanJoined===false?`<section class="home-together">${heading('소셜','함께하는 기록')}<a class="home-clan-feature" href="${path('clan')}"><span class="home-clan-feature__info"><strong>클랜에 참여해 보세요</strong><span class="home-clan-feature__hint">함께하는 도전과 주간 보상을 만나보세요</span></span>${icon('chevron')}</a></section>`:`<section class="home-together">
    ${heading('소셜','함께하는 기록',more('클랜 보기','clan'))}
    <a class="seed-surface home-clan-feature" data-surface="solid" href="${path('clan')}">
      <div class="home-clan-feature__info">
        <span class="home-clan-feature__identity"><span class="home-clan-feature__mark">${icon('clan-crest-fill',true)}</span><strong>${escape(clan.name)}</strong></span>
        <span class="home-clan-feature__hint">이번 주 공동 도전 · ${escape(clanHint)}</span>
        <div class="home-clan-feature__progress">${progress(clanScore,clan.goal,'클랜 이번 주 공동 목표','micro')}<span><strong>${fmt(clanScore)}</strong> / ${fmt(clan.goal)} XP</span></div>
      </div>
      <img class="home-clan-feature__art" src="${rewards+chestLoot.clan.image}" alt="" loading="lazy">
    </a>
    <a class="home-friend" href="${path('chat/friend','home')}">
      <span class="row-avatar" aria-hidden="true">다</span>
      <span><strong>다온과의 대화</strong><small>${escape(friendMessage)}</small></span>${icon('chevron')}
    </a>
  </section>`;
  const bonus=state.quests.find(q=>q.id==='bonus'&&q.status!=='rejected');
  const event=bonus?`<a class="seed-surface home-event-feature" data-surface="media" href="${path('quest/bonus','home')}">
    <div class="home-event-feature__copy">
      <span class="home-event-feature__tag">${bonus.status==='done'?'참여 완료':'기간 한정 이벤트'}</span>
      <strong>${escape(bonus.title)}</strong>
      <small>${escape(bonus.due)} · ${bonus.status==='done'?'기록 보기':'참여하기'}</small>
      ${cash(bonus.cash)}
    </div>
    <img src="${rewards}cash-coins-pouch.png" alt="" loading="lazy">
  </a>`:'';
  const rewardsReady=ready.length?`<a class="home-ready-reward" href="${path('rewards','home')}">
    <img src="${rewards+ready[0][1].image}" alt="" loading="lazy">
    <span><strong>${ready.length===1?escape(ready[0][1].name):'열 수 있는 상자 '+ready.length+'개'}</strong><small>보상함에서 확인하기</small></span>${icon('chevron')}
  </a>`:'';
  const events=event||rewardsReady?`<section class="home-events">${heading('이벤트와 보상','놓치면 아쉬운 소식')}${event}${rewardsReady}</section>`:'';
  return activity+discovery+social+events;
}

function questbookCover(book,state) {
  const total=book.quests.length;
  const done=state.quests.filter(q=>book.quests.includes(q.id)&&q.status==='done').length;
  return `<a class="seed-surface questbook-cover" data-surface="solid" href="${path('book/'+book.id,'quests')}">
    <div class="questbook-cover__copy">
      ${title(book.title,'hero','h3')}
      ${support(book.chapter)}
    </div>
    <span class="questbook-cover__symbol">${icon('book')}</span>
    <div class="questbook-cover__progress">
      <div class="questbook-cover__value"><strong>${done}</strong><span> / ${total} 완료</span></div>
      ${progress(done,total,'퀘스트북 진행')}
    </div>
    <span class="questbook-cover__link">${done===total?'기록 보기':'이어보기'} ${icon('chevron')}</span>
  </a>`;
}

function questIndexRow(q,filter) {
  const glyph=q.status==='done'?'check-circle':{일일:'target',메인:'flag',보너스:'sparkle'}[q.kind]||'target';
  const tone={일일:'neutral',메인:'brand',보너스:'warning'}[q.kind]||'neutral';
  const time=q.status==='done'?(q.baseline?q.due:'완료 기록'):(q.status==='deferred'?'다시 시작할 수 있어요':q.due);
  return `<a class="seed-list__item quest-index-row" data-interaction="navigate" href="${path('quest/'+q.id,'quests')}">
    <span class="seed-list__prefix row-icon quest-index-icon" data-status="${q.status}">${icon(glyph)}</span>
    <div class="seed-list__content quest-index-copy">
      ${title(q.title,'item','span')}
      <div class="quest-index-meta">${badge(q.kind,tone)}<span class="seed-text" data-text-role="caption" data-text-style="caption" data-text-tone="secondary">${escape(filter==='all'&&q.status!=='done'?statuses[q.status]+' · '+time:time)}</span></div>
    </div>
    <span class="seed-list__suffix">${rewardLine(q)}</span>
    ${icon('chevron')}
  </a>`;
}

function quests(state,ui) {
  const filter=Object.hasOwn(filterNames,ui.quest)?ui.quest:'active';
  const items=state.quests.filter(q=>filter==='all'?q.status!=='rejected':q.status===filter);
  const noItems={all:'아직 퀘스트가 없어요.',active:'진행 중인 퀘스트가 없어요.',offered:'새로 받은 제안이 없어요.',deferred:'미뤄 둔 퀘스트가 없어요.',done:'완료한 퀘스트가 없어요.'};
  const body=items.length?list(items.map(q=>questIndexRow(q,filter)).join(''),'퀘스트 목록'):empty(noItems[filter]);
  return section('퀘스트북',`<div class="questbook-shelf">${books.map(book=>questbookCover(book,state)).join('')}</div>`)
    +section('내 퀘스트',`<div class="quest-browser">${chips(Object.entries(filterNames),filter,'quest')}${body}</div>`,
      `<span class="quest-result-count" role="status">${items.length}개</span>`);
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
function dm(state,ui={}) {
  const category=ui.dmFilter??'all',query=(ui.dmSearch??'').trim().toLocaleLowerCase('ko');
  const entries=Object.entries(threads).filter(([id])=>id!=='clan'||state.clanJoined!==false);
  const kind=id=>id==='clan'?'clan':'personal';
  const last=id=>state.messages[id]?.at(-1)?.image?'사진을 보냈어요':state.messages[id]?.at(-1)?.text||'대화를 시작해 보세요';
  const searchText=id=>(threads[id].title+' '+last(id)+(id==='ai'?' AI':'')).toLocaleLowerCase('ko');
  const shown=id=>(category==='all'||category===kind(id))&&(!query||searchText(id).includes(query));
  const line=([id,thread])=>{
    const isAI=id==='ai',isClan=id==='clan';
    const identity=isClan?`<span class="dm-conversation__clan-icon">${icon('clan-crest-fill',true)}</span>`:
      `<span data-avatar="fluid" data-avatar-items="${(thread.avatarEquipment||[]).join(',')}"></span>`;
    return `<a class="dm-conversation" data-dm-kind="${kind(id)}" data-dm-search="${escape(searchText(id))}" ${shown(id)?'':'hidden'} href="${path('chat/'+id,'dm')}" aria-label="${escape(thread.title)}, ${escape(last(id))}, 대화 열기">
      <span class="dm-conversation__avatar" aria-hidden="true">${identity}</span>
      <span class="dm-conversation__content"><span class="dm-conversation__name">${escape(thread.title)}${isAI?'<small>AI</small>':''}</span><span class="dm-conversation__preview">${escape(last(id))}</span></span>
    </a>`;
  };
  const visibleCount=entries.filter(([id])=>shown(id)).length;
  return `<section class="dm-inbox" aria-label="대화 목록">
    <div class="dm-inbox__search">${icon('search')}<label class="visually-hidden" for="dm-search">대화 검색</label><input id="dm-search" type="search" placeholder="대화 검색" autocomplete="off" maxlength="80" value="${escape(ui.dmSearch??'')}"></div>
    <div class="dm-inbox__filters" role="group" aria-label="대화 종류 필터">
      ${[['all','전체'],['personal','개인'],['clan','클랜']].map(([value,label])=>`<button type="button" class="seed-filter-chip" data-filter="dmFilter" data-value="${value}" aria-pressed="${category===value}">${label}</button>`).join('')}
    </div>
    <div class="dm-inbox__list" aria-label="필터링된 대화 목록">${entries.map(line).join('')}</div>
    <p class="dm-inbox__empty" aria-live="polite" ${visibleCount?'hidden':''}>${query?'검색 결과가 없어요':'해당하는 대화가 없어요'}</p>
  </section>`;
}
function chat(state,route,ui={}) {
  const thread=threads[route.id],messages=state.messages[route.id];
  if(!thread||!Array.isArray(messages))return empty('대화방을 찾을 수 없어요.');
  const isAI=route.id==='ai',isClan=route.id==='clan';
  const rows=messages.map((msg,index)=>{
    const mine=msg.from==='me',prev=messages[index-1];
    const author=isClan?(msg.senderId&&msg.senderId!=='clan'&&msg.senderId!=='ai'?threads[msg.senderId]:null):thread;
    const authorName=isClan?(author?.title||'클랜 멤버'):thread.title;
    const sameAuthor=prev&&prev.from===msg.from&&(!isClan||mine||prev.senderId===msg.senderId);
    const avatar=mine?'':`<span class="chat-row__avatar" aria-hidden="true">${sameAuthor?'':`<span data-avatar="fluid" data-avatar-items="${(author?.avatarEquipment||[]).join(',')}"></span>`}</span>`;
    const sender=isClan&&!mine&&!sameAuthor?`<span class="chat-row__sender">${escape(authorName)}</span>`:'';
    const quest=msg.quest?state.quests.find(q=>q.id===msg.quest):null;
    const proposal=quest?`<a class="chat-quest-link" href="${path('quest/'+quest.id,route.tab)}"><span class="chat-quest-link__caption">퀘스트 제안</span><strong>${escape(quest.title)}</strong><span class="chat-quest-link__bottom"><span>${quest.status==='offered'?'확인하고 수락하기':'퀘스트 보기'}</span>${icon('chevron')}</span></a>`:'';
    const imageSource=typeof msg.image?.src==='string'&&/^data:image\/(?:jpeg|png|webp);base64,[A-Za-z0-9+/=]+$/.test(msg.image.src)?msg.image.src:null;
    const image=imageSource?`<img class="chat-bubble__image" src="${escape(imageSource)}" alt="${mine?'내가 보낸 사진':'전송된 사진'}" loading="lazy">`:'';
    const editing=mine&&!isAI&&!msg.image&&ui.editMessage===route.id+':'+index;
    let bubble;
    if(editing) bubble=`<form class="chat-edit-form" data-form="edit-message" data-id="${escape(route.id)}" data-index="${index}">
        <label class="visually-hidden" for="chat-edit-${index}">메시지 수정</label>
        <textarea id="chat-edit-${index}" name="value" rows="2" maxlength="300" required>${escape(msg.text)}</textarea>
        <div class="chat-edit-form__actions"><button type="button" data-chat-ui="cancel-edit">취소</button><button type="submit">저장</button></div>
      </form>`;
    else if(mine&&!isAI) bubble=`<button type="button" class="chat-bubble chat-bubble--own" data-chat-ui="message-options" data-index="${index}" aria-haspopup="dialog" aria-controls="chat-message-sheet" aria-label="${escape((msg.text||'내가 보낸 사진').slice(0,80))} · 메시지 옵션">
        ${msg.text?`<span class="chat-bubble__text">${escape(msg.text)}</span>`:''}${image}
      </button>`;
    else if(imageSource) bubble=`<button type="button" class="chat-bubble chat-bubble--photo" data-chat-ui="view-photo" data-index="${index}" aria-label="사진 크게 보기">${image}</button>`;
    else bubble=`<div class="chat-bubble chat-bubble--incoming">${msg.text?`<p>${escape(msg.text)}</p>`:''}${proposal}</div>`;
    return `<div class="chat-row" data-from="${mine?'me':'them'}" data-group="${isClan}" data-continued="${Boolean(sameAuthor)}" ${mine?'':`aria-label="${escape(authorName)}의 메시지"`}>
      ${avatar}<div class="chat-row__content">${sender}${bubble}</div>
    </div>`;
  }).join('');
  const content=rows||'<div class="chat-empty"><strong>아직 대화가 없어요</strong><span>첫 메시지를 보내 대화를 시작해 보세요</span></div>';
  const attachment=isAI?'':`<button type="button" class="seed-icon-action chat-attach" data-chat-ui="attach" aria-label="사진 보내기">${icon('plus')}</button>
    <input id="chat-photo-file" class="visually-hidden" type="file" accept="image/jpeg,image/png,image/webp" aria-label="사진 선택" tabindex="-1">`;
  return `<div class="chat-view" aria-label="${escape(thread.title)} 대화방">
    <section class="chat-history" id="chat-history" role="log" aria-label="대화 기록" aria-live="polite" aria-relevant="additions text">${content}</section>
    <form class="chat-composer" data-form="message" data-id="${escape(route.id)}">
      <div class="chat-composer__layout">${attachment}
        <label class="visually-hidden" for="message">${isAI?'목표나 수정 요청 입력':'메시지 입력'}</label>
        <div class="chat-composer__field">
          <textarea id="message" name="value" rows="1" maxlength="300" required placeholder="${isAI?'어떤 목표를 이루고 싶어?':'메시지 입력'}" enterkeyhint="send" aria-label="${isAI?'목표나 요청 입력':'메시지 입력'}"></textarea>
          <button type="submit" class="seed-icon-action chat-composer__send" aria-label="메시지 전송" disabled>${icon('send')}</button>
        </div>
      </div>
    </form>
  </div>`;
}

function clanMemberAvatar(member) {
  if(member.name==='루원')return '<span class="clan-person__face" aria-hidden="true"><span data-avatar="fluid"></span></span>';
  const friend=Object.values(threads).find(thread=>thread.title===member.name);
  return friend?`<span class="clan-person__face" aria-hidden="true"><span data-avatar="fluid" data-avatar-items="${(friend.avatarEquipment||[]).join(',')}"></span></span>`:
    `<span class="clan-person__face clan-person__fallback" aria-hidden="true">${escape(member.name[0])}</span>`;
}
function rankings(state,period) {
  const members=clanRows(state,period),highest=Math.max(1,...members.map(member=>member.xp));
  return list(members.map((member,index)=>{
    const own=member.name==='루원';
    const share=Math.max(0,Math.min(100,Math.round(member.xp/highest*100)));
    return `<div class="seed-list__item clan-member" data-own="${own}">
      <span class="rank" aria-label="${index+1}위">${index+1}</span>${clanMemberAvatar(member)}
      <span class="clan-member__identity"><span class="clan-member-name">${title(member.name,'item','span')}${own?badge('나','brand'):''}</span>
        <span class="clan-member__meter" aria-hidden="true"><span style="--clan-share:${share}%"></span></span></span>
      <strong class="rank-xp">${fmt(member.xp)} XP</strong>
    </div>`;
  }).join(''),'클랜 기여 순위');
}
function clanHome(state,ui={}) {
  const total=clanTotal(state),members=clanRows(state);
  if(state.clanJoined===false) {
    return `<section class="clan-discover" aria-label="클랜 미참가 상태">
      <div class="clan-discover__intro">
        <span class="clan-discover__symbol" aria-hidden="true">${icon('clan-crest-fill',true)}</span>
        <small>새로운 클랜 만나기</small><h2>함께라서 더 재밌는 도전</h2>
        <p>서로의 작은 도전을 모아 주간 보상을 획득해 보세요</p>
      </div>
      <div class="clan-discover__team">
        <span class="clan-discover__label">참가 가능한 클랜 · 시연</span>
        <div class="clan-discover__name"><span aria-hidden="true">${icon('clan-crest-fill',true)}</span>
          <div><strong>${escape(clan.name)}</strong><small>${clan.current.length}명 · 이번 주 공동 도전</small></div>
        </div>
        <div class="clan-discover__progress"><small>${fmt(total)} / ${fmt(clan.goal)} XP</small>${progress(total,clan.goal,'시연 클랜 진행률','micro')}</div>
        ${act('참가하기','clan-join','clan')}
      </div>
      <small class="clan-discover__note">이 화면은 시연용이에요. 실제 클랜 가입 서버는 아직 연결되지 않았어요.</small>
    </section>`;
  }
  const view=['mine','insights','shop'].includes(ui.clanView)?ui.clanView:'mine';
  const mine=members.find(member=>member.name==='루원');
  const intro=`<section class="clan-intro" aria-label="내 클랜">
    <span class="clan-intro__mark" aria-hidden="true">${icon('clan-crest-fill',true)}</span>
    <div class="clan-intro__name">
      <small>MY CLAN · ${members.length}명 함께</small>
      <h2>${escape(clan.name)}</h2>
      <span>서로의 작은 도전을 모으는 중</span>
      <div class="clan-intro__mine"><span>이번 주 내 기여</span><strong>${fmt(mine?.xp??0)} XP</strong></div>
    </div>
  </section>`;
  const tabbar=`<div class="clan-tabs" role="tablist" aria-label="클랜 콘텐츠">
    ${[['mine','내 클랜'],['insights','인사이트'],['shop','클랜 상점']].map(([id,label])=>
      `<button type="button" role="tab" id="clan-tab-${id}" data-clan-view="${id}" aria-controls="clan-panel" aria-selected="${view===id}" tabindex="${view===id?0:-1}">${label}</button>`).join('')}
  </div>`;
  const box=chestLoot.clan,status=state.boxes.clan,canClaim=status==='locked'&&total>=clan.goal;
  const remain=Math.max(clan.goal-total,0),description=canClaim?'목표 달성 · 지금 받을 수 있어요':
    status==='ready'?'보상함에 도착했어요':status==='opened'?'이번 주 보상을 받았어요':fmt(remain)+' XP 남았어요';
  const action=canClaim?act('상자 받기','claim','clan','reward-secondary'):
    status!=='locked'?cta('보상함 보기','rewards','clan','neutral-outline'):
      `<a href="${path('quests')}" class="clan-reward__continue">퀘스트 이어가기 ${icon('chevron')}</a>`;
  const weekly=`<section class="clan-reward" aria-label="주간 클랜 상자">
    <div class="clan-reward__top"><div class="clan-reward__copy">
      <small>이번 주 보상</small><h3>주간 클랜 상자</h3><p>${escape(description)}</p>
      <div class="clan-reward__items">${cash(box.cash)}<span>· ${escape(MASCOT_EQUIPMENT[box.item].name)}</span></div>
    </div><img class="clan-reward__art" src="${rewards+box.image}" alt="" loading="lazy"></div>
    <div class="clan-reward__bottom">
      <div class="clan-reward__score"><span>함께 모은 XP</span><strong>${fmt(total)} <small>/ ${fmt(clan.goal)}</small></strong></div>
      ${progress(total,clan.goal,'주간 클랜 상자','featured')}
      <div class="clan-reward__action">${action}</div>
    </div>
  </section>`;
  const people=`<section class="clan-people" aria-label="함께하는 멤버">
    <div class="clan-section-heading"><h3>함께하는 멤버</h3><a href="${path('ranking','clan')}">기여 순위 ${icon('chevron')}</a></div>
    <div class="clan-people__row">${members.map(member=>`<div class="clan-person">${clanMemberAvatar(member)}
      <span>${escape(member.name)}${member.name==='루원'?'<small>나</small>':''}</span></div>`).join('')}</div>
  </section>`;
  const conversation=`<div class="clan-conversation"><a href="${path('chat/clan','clan')}">
    <span class="clan-conversation__icon">${icon('chat')}</span>
    <span><strong>클랜 대화</strong><small>우리의 도전을 이야기해요</small></span>${icon('chevron')}
  </a></div>`;
  const memberPanel=weekly+people+conversation+`<button type="button" class="clan-leave" data-action="clan-leave" data-id="clan">클랜 나가기</button>`;
  const prior=clanTotal(state,'previous'),delta=total-prior;
  const insights=`<section class="clan-insights">
    <div class="clan-section-heading"><h3>주간 기록</h3><span>시연 XP</span></div>
    <div class="clan-insights__totals">
      <div><small>이번 주</small><strong>${fmt(total)}<span> XP</span></strong></div>
      <div><small>지난주</small><strong>${fmt(prior)}<span> XP</span></strong></div>
    </div>
    <p>${delta===0?'지난주와 같아요':delta>0?'지난주보다 '+fmt(delta)+' XP 높아요':'지난주보다 '+fmt(-delta)+' XP 낮아요'}</p>
    <div class="clan-insights__meter">${progress(total,clan.goal,'주간 공동 목표 달성률','micro')}
      <span>공동 목표 ${fmt(clan.goal)} XP · ${Math.floor(Math.min(total/clan.goal,1)*100)}% 달성</span></div>
  </section>
  <section class="clan-leaderboard"><div class="clan-section-heading"><h3>멤버 기여도</h3><a href="${path('ranking','clan')}">전체 보기 ${icon('chevron')}</a></div>
    <div class="clan-board-period">${segment(ui.period||'current')}<small>합계 ${fmt(clanTotal(state,ui.period||'current'))} XP</small></div>
    ${rankings(state,ui.period||'current')}</section>`;
  const picks=[5,1003,1004].map(id=>catalog.find(item=>item.id===id)).filter(Boolean);
  const storePanel=`<section class="clan-market" aria-label="클랜 상점">
    <div class="clan-market__head"><div><h3>클랜 상점</h3><p>같이 꾸미는 즐거움</p></div>${cash(state.cash)}</div>
    <div class="clan-market__grid">${picks.map(item=>{
      const art=MASCOT_EQUIPMENT[item.id],owned=state.owned.includes(item.id);
      const action=owned?'<span class="clan-market__owned">보유 중</span>':
        act('구매','purchase',item.id,'neutral-outline',state.cash>=item.price?'':'disabled aria-disabled="true"');
      return `<article class="clan-market__product">${frame(art.src,'')}
        <div class="clan-market__product-copy"><strong>${escape(item.name)}</strong>
          ${cash(item.price)}${action}
        </div></article>`;
    }).join('')}</div>
    <a class="clan-market__all" href="${path('shop','me')}">전체 상점 둘러보기 ${icon('chevron')}</a>
    <p class="clan-market__note">별도 클랜 재화 없이 기존 캐시와 장식 구매 상태를 사용하는 시연 상점입니다.</p>
  </section>`;
  const content=view==='mine'?memberPanel:view==='insights'?insights:storePanel;
  return `<div class="clan-page">${intro}${tabbar}
    <div role="tabpanel" id="clan-panel" class="clan-panel" aria-labelledby="clan-tab-${view}" tabindex="-1">${content}</div>
  </div>`;
}
function myBoxRows(boxes) {
  if(!boxes.length)return `<a class="me-box-empty" href="${path('rewards','me')}">아직 받은 상자가 없어요 ${icon('chevron')}</a>`;
  return `<div class="me-box-list" role="group" aria-label="내 상자 목록" data-count="${boxes.length}">
    ${boxes.map(([id,status])=>{
      const box=chestLoot[id];
      const description=status==='ready'?'열 수 있어요':'열어본 상자';
      return `<a class="me-box" data-state="${status}" href="${path('rewards','me')}" aria-label="${escape(box.name)}, ${description}, 보상함으로 이동">
        <span class="me-box__art">${frame(rewards+box.image,'')}</span>
        <span class="me-box__copy"><strong>${escape(box.name)}</strong><small>${description}</small></span>
        ${icon('chevron')}
      </a>`;
    }).join('')}
  </div>`;
}
function me(state) {
  const level=1+Math.floor(state.xp/250);
  const completed=state.quests.filter(q=>q.status==='done').length;
  const available=Object.values(state.boxes).filter(v=>v==='ready').length;
  const items=state.owned.map(id=>MASCOT_EQUIPMENT[id]).filter(Boolean);
  // The mock has no completion timestamp, so don't label this as the latest.
  const completedRecord=state.quests.find(q=>q.status==='done');
  const profile=`<section class="seed-page-shell__section me-section" aria-label="내 프로필">
    <div class="me-profile">
      <div class="me-profile__figure">${avatar('fluid')}</div>
      <div class="me-profile__identity">
        ${title('루원','hero')}
        <span class="me-profile__level">Lv. ${level} <span aria-hidden="true">·</span> ${fmt(state.xp)} XP</span>
        ${cta('꾸미기','decorate','me','neutral-outline')}
      </div>
    </div>
  </section>`;
  const stats=`<section class="me-stats" aria-label="내 활동 기록"><div><strong>${completed}</strong><span>완료한 퀘스트</span></div><div><strong>${items.length}</strong><span>보유 아이템</span></div><div><strong>${available}</strong><span>열 수 있는 상자</span></div></section>`;
  const activity=completedRecord?`<a class="me-record" href="${path('quest/'+completedRecord.id,'me')}" aria-label="완료 기록, ${escape(completedRecord.title)}">
    <span class="me-record__icon">${icon('check-circle')}</span>
    <span class="me-record__text"><small>완료 기록</small><strong>${escape(completedRecord.title)}</strong></span>
    <span class="me-record__xp">${completedRecord.xp?'+ '+fmt(completedRecord.xp)+' XP':'완료'}</span>${icon('chevron')}
  </a>`:`<a class="me-record" href="${path('quests','me')}"><span class="me-record__icon">${icon('target')}</span><span class="me-record__text"><small>나의 기록</small><strong>첫 퀘스트를 시작해 보세요</strong></span>${icon('chevron')}</a>`;
  const owned=items.length?`<div class="me-collection" role="group" aria-label="내 장식 살펴보기">${items.slice(0,6).map(item=>`
    <a class="seed-item-display seed-surface me-collection__item" data-surface="solid" data-item-presentation="integrated" href="${path('decorate','me')}" aria-label="${escape(item.name)}, 꾸미기로 이동">
      ${frame(item.src,'')}
      <span class="seed-item-display__info"><strong class="me-collection__name">${escape(item.name)}</strong></span>
    </a>`).join('')}</div>`:`<div class="me-collection-empty">${support('아직 보유한 장식이 없어요.')}${cta('상점 둘러보기','shop','me','neutral-outline')}</div>`;
  // A locked reward is not owned. Opened boxes remain visible as history.
  const myBoxes=Object.entries(state.boxes)
    .filter(([id,status])=>chestLoot[id]&&(status==='ready'||status==='opened'))
    .sort((a,b)=>Number(b[1]==='ready')-Number(a[1]==='ready'));
  const boxPreview=myBoxRows(myBoxes);
  const shopLink=row({label:'상점 둘러보기',sub:'새로운 장식을 찾아보세요',url:'shop',tab:'me',leading:`<span class="row-icon">${icon('shopping-bag')}</span>`});
  return profile+stats+activity+
    section('내 스타일',owned)+
    section('내 상자',boxPreview+`<div class="me-shop-link">${shopLink}</div>`,more('보상함 보기','rewards','me'));
}
function shop(state,ui={}) {
  // A storefront view over the existing catalog, not a new economy.
  const selected=types.includes(ui.shop)?ui.shop:'전체';
  const pick=catalog.find(item=>item.id===1);
  const pickArt=MASCOT_EQUIPMENT[pick.id];
  const feature=`<section class="seed-item-display seed-surface shop-feature" data-surface="media" data-item-presentation="integrated" data-item-layout="horizontal" aria-label="추천 장식">
    ${frame(pickArt.src,'')}
    <div class="seed-item-display__info shop-feature__copy">
      <span class="shop-feature__eyebrow">오늘의 추천</span>
      <h2>${escape(pick.name)}</h2>
      <p>가볍게 바꾸는 마스코트의 분위기</p>
      ${state.owned.includes(pick.id)?'<span class="shop-feature__owned">이미 가지고 있어요</span>':cash(pick.price,'large')}
      <div class="seed-item-display__action">${cta(state.owned.includes(pick.id)?'꾸미러 가기':'아이템 보기',state.owned.includes(pick.id)?'decorate':'item/'+pick.id,'me','neutral-outline')}</div>
    </div>
  </section>`;
  const visible=catalog.filter(item=>selected==='전체'||item.type===selected);
  const products=visible.map(item=>{
    const art=MASCOT_EQUIPMENT[item.id];
    const owned=state.owned.includes(item.id);
    return `<a class="seed-item-display shop-product" data-item-presentation="open" data-category="${escape(item.type)}" href="${path('item/'+item.id,'me')}" aria-label="${escape(item.name)}, ${owned?'보유 중':fmt(item.price)+' 캐시'}, 상품 상세">
      ${frame(art.src,'')}
      <span class="seed-item-display__info shop-product__info">
        <strong>${escape(item.name)}</strong>
        ${owned?'<span class="shop-product__owned">보유 중</span>':cash(item.price)}
      </span>
    </a>`;
  }).join('');
  const catalogPanel=`<section class="shop-inventory" aria-label="꾸미기 아이템">
    <div class="shop-section-head"><h2>꾸미기 아이템</h2><span>${visible.length}개</span></div>
    ${chips(types.map(value=>[value,value]),selected,'shop')}
    <div class="shop-products">${products||'<p class="shop-empty">해당하는 장식이 없어요</p>'}</div>
  </section>`;
  // Boxes are earned rewards, never purchasable SKUs in this prototype.
  const boxes=Object.entries(chestLoot).map(([id,box])=>{
    const status=state.boxes[id]||'locked';
    const destination=status==='locked'?(id==='clan'?'clan':'quests'):'rewards';
    const label=status==='ready'?'열 수 있어요':status==='opened'?'열어본 상자':'획득 전';
    return `<a class="shop-box" href="${path(destination,destination==='rewards'?'me':undefined)}" aria-label="${escape(box.name)}, ${label}, ${status==='locked'?'도전 현황':'보상함'} 보기">
      <div class="shop-box__image">${frame(rewards+box.image,'')}</div>
      <span class="shop-box__info"><strong>${escape(box.name)}</strong><small>${label}</small></span>
      ${icon('chevron')}
    </a>`;
  }).join('');
  const rewardsPanel=`<section class="shop-rewards" aria-label="획득하는 보상 상자">
    <div class="shop-section-head"><div><h2>보상 상자</h2><p>퀘스트와 클랜 활동으로 얻어요</p></div>
      <a class="shop-section-head__link" href="${path('rewards','me')}">보상함 ${icon('chevron')}</a></div>
    <div class="shop-box-rail" role="group" aria-label="보상 상자 목록">${boxes}</div>
  </section>`;
  const cashHint=`<a class="shop-earn" href="${path('quests')}"><span class="shop-earn__icon">${icon('target')}</span>
    <span><strong>캐시가 더 필요하다면</strong><small>퀘스트를 완료하고 보상을 모아봐요</small></span>${icon('chevron')}</a>`;
  return `<div class="shop-page">
    <div class="shop-balance" aria-label="현재 캐시">${cash(state.cash)}</div>
    ${feature}${catalogPanel}${rewardsPanel}${cashHint}
  </div>`;
}
function itemDetail(state,route) {
  const item=catalog.find(product=>product.id===Number(route.id));
  if(!item)return empty('상품을 찾을 수 없어요.');
  const owned=state.owned.includes(item.id);
  return `<article class="seed-surface detail-panel" data-surface="solid">${frame(MASCOT_EQUIPMENT[item.id].src,item.name)}${title(item.name,'hero')}${item.description?support(item.description):''}${cash(item.price,'large')}</article><div class="page-actions">${owned?cta('꾸미기','decorate','me'):act(state.cash>=item.price?'구매하기':'캐시가 부족해요','purchase',item.id,'brand-solid',state.cash<item.price?'disabled':'')}</div>`;
}
function decorate(state,ui={}) {
  // Existing local-owned items and SEED C-AVATAR are the only data sources.
  const owned=(state.owned||[]).map(id=>MASCOT_EQUIPMENT[id]).filter(Boolean);
  const worn=(state.equipped||[]).filter(id=>state.owned.includes(id)).map(id=>MASCOT_EQUIPMENT[id]).filter(Boolean);
  const selected=types.includes(ui.dressFilter)?ui.dressFilter:'전체';
  const category=item=>item.slot==='headwear'?'모자':'오디오';
  const visible=owned.filter(item=>selected==='전체'||category(item)===selected);
  const slots=[['headwear','모자'],['earwear','오디오']].map(([key,label])=>{
    const item=worn.find(equipment=>equipment.slot===key);
    return `<div class="decorate-preview__slot"><small>${label}</small><strong>${item?escape(item.name):'선택 안 함'}</strong></div>`;
  }).join('');
  const itemMarkup=visible.map(item=>{
    const equipped=worn.some(current=>current.id===item.id);
    // The whole tile is one genuine pressable action, never a fake nested button.
    const media=`<span class="seed-media" data-media-kind="item" data-media-fit="contain" data-media-state="loaded">
      <img class="seed-media__image" src="${item.src}" alt="" loading="lazy">
      <span class="seed-media__fallback">이미지를 불러올 수 없어요</span>
      ${equipped?`<span class="seed-media__badge decorate-item__check" aria-hidden="true">${icon('check')}</span>`:''}
    </span>`;
    return `<button type="button" class="seed-item-display seed-surface decorate-item" data-item-presentation="integrated" data-surface="interactive"
      data-action="equip" data-id="${item.id}" data-equipped="${equipped}" aria-pressed="${equipped}"
      aria-label="${escape(item.name)}, ${equipped?'현재 선택됨. 누르면 해제':'선택하면 착용'}">
      ${media}<span class="seed-item-display__info"><strong>${escape(item.name)}</strong></span>
    </button>`;
  }).join('');
  const emptyContent=owned.length===0?
    `<div class="decorate-empty"><p>아직 보유한 장식이 없어요</p><a href="${path('shop','me')}">상점에서 살펴보기 ${icon('chevron')}</a></div>`:
    `<div class="decorate-empty"><p>보유한 ${escape(selected)} 장식이 없어요</p><a href="${path('shop','me')}">상점에서 살펴보기 ${icon('chevron')}</a></div>`;
  return `<div class="decorate-page">
    <section class="decorate-preview" aria-label="내 마스코트 스타일">
      <div class="decorate-preview__stage">
        <span class="decorate-preview__label">나의 마스코트</span>
        <div class="decorate-preview__portrait">${avatar('fluid')}</div>
      </div>
      <div class="decorate-preview__slots" role="group" aria-label="현재 장착한 장식" aria-live="polite">${slots}</div>
    </section>
    <section class="decorate-wardrobe" aria-label="보유한 장식 선택">
      <div class="decorate-wardrobe__heading">
        <div><h2>내 아이템</h2><span>보유 ${owned.length}개 · 누르면 바로 적용돼요</span></div>

      </div>
      ${chips(types.map(type=>[type,type]),selected,'dressFilter')}
      <div class="decorate-items">${itemMarkup||emptyContent}</div>
    </section>
  </div>`;
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

export function searchResults(state,query,tab='home') {
  const term=query.trim().toLocaleLowerCase('ko');
  if(!term)return '';
  const matches=text=>text.toLocaleLowerCase('ko').includes(term);
  const quests=state.quests.filter(q=>q.status!=='rejected'&&matches(q.title));
  const foundBooks=books.filter(book=>matches(book.title));
  const rows=quests.map(q=>questRow(q,tab)).join('')+foundBooks.map(book=>row({
    label:book.title,sub:'퀘스트북',url:'book/'+book.id,tab,leading:`<span class="row-icon">${icon('book')}</span>`,
  })).join('');
  return rows?list(rows,'검색 결과'):empty('일치하는 퀘스트나 퀘스트북이 없어요.');
}

export function renderPage(state,route,ui) {
  switch(route.name) {
    case 'home':return home(state);
    case 'search':return `<section class="seed-page-shell__section"><label class="visually-hidden" for="home-search">퀘스트나 퀘스트북 검색</label><input class="seed-field" id="home-search" type="search" placeholder="퀘스트나 퀘스트북 검색" autocomplete="off" maxlength="80" value="${escape(ui.search??'')}"><div id="home-search-results" aria-live="polite">${searchResults(state,ui.search??'',route.tab)}</div></section>`;
    case 'quests':return quests(state,ui);
    case 'dm':return dm(state,ui);
    case 'clan':return clanHome(state,ui);
    case 'me':return me(state);
    case 'quest':return detail(state,route);
    case 'book':return book(state,route);
    case 'chat':return route.id==='clan'&&state.clanJoined===false?empty('클랜에 참여하면 대화를 사용할 수 있어요.'):chat(state,route,ui);
    case 'ranking':return state.clanJoined===false?empty('클랜에 참여하면 순위를 볼 수 있어요.'):segment(ui.period)+section('기여 순위',rankings(state,ui.period));
    case 'shop':return shop(state,ui);
    case 'item':return itemDetail(state,route);
    case 'decorate':return decorate(state,ui);
    case 'rewards':return rewardRoom(state);
    default:return empty('페이지를 찾을 수 없어요.');
  }
}
