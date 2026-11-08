/* Clan preview v3: SEED-guided composition. Fictional data; no API, economy, or storage effects. */
(() => {
  'use strict';
  const draft = { joined:true, name:'느슨한 갓생단', about:'조금씩이라도 함께 꾸준히', flag:'#E9EF77', selectedPublic:'study', clanFilter:'all', hearts:Object.create(null),myTab:'members' };
  // Source-only icon geometry; all 16 distinct glyph files are verbatim SEED v0.14 blobs.
  // Exact user-approved SEED v0.14 icon files. Use their unmodified mask alpha
  // to apply currentColor in the UI. Never retrace geometry in this preview.
  const seedIconNames = Object.freeze({"flag":"flag","users":"users","book":"book","heart":"heart","check":"check","gift":"gift","chevron":"chevron","plus":"plus","message":"chat","activity":"activity","flame":"flame","trophy":"trophy","shop":"shopping-bag","target":"target","userPlus":"user-plus","clock":"clock","calendar":"calendar","ranking":"ranking"});
  const svg = (key, cls='') => {
    const icon=seedIconNames[key];
    if(!icon)return '';
    const path='assets/seed-icons/'+icon+'.svg';
    return `<span class="clan-svg ${cls}" data-seed-icon="${icon}" aria-hidden="true" style="-webkit-mask-image:url('${path}');mask-image:url('${path}')"></span>`;
  };
  const safe = t => String(t).replace(/[&<>"']/g, c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  const title = () => safe(draft.name.trim() || '클랜 이름');
  const heading = (text, route, action='전체 보기') => `<div class="clan-heading"><h2>${text}</h2>${route?`<a class="clan-more" href="#${route}">${action}${svg('chevron')}</a>`:''}</div>`;
  // ClanIdentity art: pale fabric pennant, not the generic 24px UI flag pictogram.
  const emblem = (color=draft.flag) => `<span class="clan-emblem" role="img" aria-label="클랜 깃발" style="--clan-flag:${color}"><svg class="clan-flag-graphic" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 72 72" aria-hidden="true">
    <path d="M11.5 9v47" stroke="#665F5B" stroke-width="3" stroke-linecap="round"/>
    <path d="M15 13Q24 9 35 12Q49 15 62 10V27L57 33L62 40V52Q49 56 36 51Q25 47 15 51Z" fill="var(--clan-flag)" stroke="#302D2B" stroke-opacity=".18" stroke-width="1.1" stroke-linejoin="round"/>
    <path d="M17 15Q25 13 34 16Q47 18 59 14" fill="none" stroke="white" stroke-width="2" opacity=".25"/>
    <g stroke="#2D2B2B" stroke-width="3.4" stroke-linecap="round"><path d="M38 22v21"/><path d="M27.5 32.5h21"/><path d="M30.4 24.9l15.2 15.2"/><path d="M45.6 24.9L30.4 40.1"/></g>
  </svg></span>`;
  // Same 1x1 layout contract as app/.../AvatarComposition.kt. All users with an item share identical coordinates.
  const equipments = Object.freeze({
    '이모리':{id:0,file:'cosmetic_beanie.png',x:.075,y:.055,w:.85,h:.612},
    '밤산책':{id:1003,file:'cosmetic_black_headphones.png',x:.030,y:.175,w:.94,h:.685},
    '모찌':{id:1,file:'cosmetic_cream_cap.png',x:.045,y:.060,w:.91,h:.582},
    '다온':{id:2,file:'cosmetic_blue_cap.png',x:.065,y:.015,w:.87,h:.582}
  });
  const avatar = (member,{online=false,size='row'}={}) => {
    const eq=equipments[member];const item=eq?`<img src="assets/${eq.file}" class="clan-avatar-item" alt="" loading="lazy" draggable="false" style="left:${eq.x*100}%;top:${eq.y*100}%;width:${eq.w*100}%;height:${eq.h*100}%">`:'';
    return `<span class="clan-avatar clan-avatar-${size}${online?' is-online':''}" data-avatar-member="${safe(member)}"${eq?` data-demo-equipment-id="${eq.id}"`:''} aria-hidden="true"><img src="assets/mascot_face_stamp.png" class="clan-avatar-face" alt="" loading="lazy" draggable="false">${item}</span>`;
  };
  const members = [{name:'이모리',sub:'오늘 3개 완료',pts:220},{name:'밤산책',sub:'오늘 2개 완료',pts:180},{name:'모찌',sub:'이번 주 4일 참여',pts:160},{name:'다온',sub:'오늘 참여',pts:60}];
  const clubs = [
    {id:'study',name:'갓생 스터디단',flag:'#CAB5F5',kind:'공부',members:24,active:12,icon:'book',tone:'violet',blurb:'조금씩 쌓는 공부 습관'},
    {id:'daily',name:'매일 1% 더',flag:'#9DDFC8',kind:'습관',members:18,active:9,icon:'activity',tone:'mint',blurb:'매일 한 걸음의 변화'},
    {id:'fitness',name:'주말 운동 모임',flag:'#F3C17E',kind:'운동',members:12,active:6,icon:'target',tone:'peach',blurb:'함께 만드는 가벼운 루틴'}
  ];
  const selectedClub=()=>clubs.find(c=>c.id===draft.selectedPublic)||clubs[0];
  const memberStack=()=>`<div class="clan-avatars-group" aria-label="이번 주 참여 멤버 6명 중 4명 예시: 이모리, 밤산책, 모찌, 다온">${members.map(m=>avatar(m.name,{online:true,size:'stack'})).join('')}<span class="clan-avatar-overflow" aria-label="추가 2명">+2</span></div>`;
  const action=(href,ic,txt)=>`<a class="clan-mini-action" href="#${href}">${svg(ic)}<span>${txt}</span></a>`;

  const identity=()=>`<section class="clan-section clan-top-section"><div class="clan-profile-line">
    <a href="#clan-my" class="clan-main-identity">${emblem()}<span class="clan-identity-copy"><strong>${title()}</strong><small>멤버 8명</small></span></a>
    <a href="#clan-invite" class="clan-inline-invite">${svg('userPlus')}<span>초대</span></a>
  </div></section>`;
  const rewardVisual=()=>`<img src="assets/box_gold.webp" class="clan-chest-img" alt="" loading="lazy" draggable="false">`;
  // Single source for the progress number, visual fill, and accessible value.
  const collectiveRecord = Object.freeze({current:620,target:1000});
  const progress=({compact=false,showLabel=true}={})=>{
    const {current,target}=collectiveRecord;
    const fraction=target>0?Math.max(0,Math.min(current/target,1)):0;
    const formatted=n=>new Intl.NumberFormat('ko-KR').format(n);
    return `<div class="clan-progress-meta"><div>${showLabel?'<span>공동 기록</span>':''}<strong>${formatted(current)} <small>/ ${formatted(target)}</small></strong></div><span class="clan-remaining">완료까지 ${formatted(Math.max(target-current,0))}</span></div>
      <div class="progress-track ${compact?'is-compact':''}" role="progressbar" aria-label="이번 주 공동 기록" aria-valuemin="0" aria-valuemax="${target}" aria-valuenow="${Math.min(current,target)}" aria-valuetext="${formatted(current)} / ${formatted(target)}"><span style="--progress:${fraction*100}%"></span></div>`;
  };

  const rewardItems=()=>`<div class="clan-preview-items" aria-label="공동 보상 미리보기"><div class="clan-preview-item"><div class="clan-preview-media"><img src="assets/cosmetic_blue_cap.png" alt="" loading="lazy"></div><div class="clan-preview-copy"><strong>블루 캡</strong><small>공동 목표 달성 시 예정 · 지급 조건 미확정</small></div></div>
<div class="clan-preview-item"><div class="clan-preview-media"><img src="assets/cosmetic_black_headphones.png" alt="" loading="lazy"></div><div class="clan-preview-copy"><strong>헤드셋</strong><small>공동 목표 달성 시 예정 · 지급 조건 미확정</small></div></div>
<div class="clan-preview-item"><div class="clan-preview-media"><img src="assets/cosmetic_beanie.png" alt="" loading="lazy"></div><div class="clan-preview-copy"><strong>비니</strong><small>공동 목표 달성 시 예정 · 지급 조건 미확정</small></div></div></div>`;
  const rewardSection=()=>`<section class="clan-section clan-record-home" aria-label="이번 주 공동 기록">
  <div class="clan-heading"><h2>이번 주 공동 기록</h2><a class="clan-more" href="#clan-reward">보상 구성 보기 ${svg('chevron')}</a></div>
  ${progress({showLabel:false})}
  <div class="clan-progress-foot"><span>이번 주 일요일까지</span><span>6명 참여</span></div>
</section>`;
  const socialSection=()=>`<section class="clan-section clan-social-section" aria-label="오늘 함께한 사람">
  <div class="clan-heading"><h2>오늘 함께한 사람</h2><a class="clan-more" href="#clan-activity">전체 소식 ${svg('chevron')}</a></div>
  <div class="clan-social-activity-list">${feedRows()}</div>
  <div class="clan-social-foot">${action('dm','message','클랜 채팅')}</div>
</section>`;
  const rankingHome=()=>`<section class="clan-section clan-ranking-home">
  <h2>이번 주 순위</h2>
  <a class="clan-ranking-link" href="#clan-record"><strong>12위</strong><span>지난주 대비 ↑ 3</span>${svg('chevron','clan-subchev')}</a>
</section>`;
  const recordMetrics=()=>`<div class="clan-record-metrics" role="group" aria-label="이번 주 클랜 기록">
    <div class="clan-record-metric">${svg('calendar')}<strong>7일</strong><span>집계 기간</span></div>
    <div class="clan-record-metric">${svg('check')}<strong>24개</strong><span>완료 도전</span></div>
    <div class="clan-record-metric">${svg('ranking')}<strong>12위</strong><span>주간 순위</span></div>
    <div class="clan-record-metric">${svg('users')}<strong>6명</strong><span>참여 멤버</span></div>
  </div>`;
  const contribution=()=>`<section class="clan-section clan-contribution-section">${heading('멤버 기여 기록','clan-my','멤버 전체')}
    <div class="clan-mini-heading"><strong>이번 주 누적 기여</strong><span>공개 동의한 기록 예시</span></div>
    <div class="clan-contributors">${members.slice(0,3).map((m,i)=>`<div class="clan-contributor"><span class="clan-contributor-rank">${i+1}</span>${avatar(m.name)}<span class="clan-contributor-name">${m.name}</span><strong>+${m.pts}</strong></div>`).join('')}</div></section>`;
  const reactions={ '밤산책':3,'모찌':5,'이모리':2 };
  const feedItem=(member,what,when,extra='')=>{const yes=!!draft.hearts[member];return `<article class="clan-feed-item">${avatar(member)}<div class="clan-feed-content"><div class="clan-feed-name"><strong>${member}</strong><time>${when}</time></div><p>${what}</p>${extra?`<span class="clan-feed-tag">${svg('check')} ${extra}</span>`:''}</div><button type="button" class="clan-react" data-clan-heart="${member}" aria-pressed="${yes}" aria-label="${member} 응원 ${yes?'취소':'하기'}">${svg('heart')}<small>${reactions[member]+(yes?1:0)}</small></button></article>`;};
  const feedRows=()=>feedItem('밤산책','오늘의 퀘스트 2개 완료','12분 전','일일 도전')+feedItem('모찌','4일 연속으로 기록을 남겼어요','38분 전','연속 참여')+feedItem('이모리','이번 주 첫 공동 기록을 달성했어요','2시간 전','공동 도전');
  const feedSection=()=>`<section class="clan-section clan-feed-section">${heading('방금 올라온 클랜 소식','clan-activity','모두 보기')}
    ${feedItem('밤산책','오늘의 퀘스트 2개 완료','12분 전','일일 도전')}
    ${feedItem('모찌','4일 연속으로 기록을 남겼어요','38분 전')}
  </section>`;
  const achievements=()=>`<div class="clan-medal-rail"><div class="clan-medal"><span class="clan-medal-art medal-gold">${svg('flame')}</span><strong>첫 주 완주</strong><small>주간 기록</small></div><div class="clan-medal"><span class="clan-medal-art medal-blue">${svg('trophy')}</span><strong>100회 달성</strong><small>누적 실천</small></div><div class="clan-medal"><span class="clan-medal-art medal-red">${svg('flag')}</span><strong>첫 클랜</strong><small>함께한 시작</small></div></div>`;
  const trailSection=()=>`<section class="clan-section">${heading('함께 쌓은 발자국','clan-my','기록 보기')}${achievements()}</section>`;
  const extraSection=()=>`<section class="clan-section">${heading('함께 즐기는 콘텐츠')}
    <div class="clan-feature-links"><a href="#clan-war"><span class="clan-feature-icon">${svg('target')}</span><strong>클랜전</strong><small>클랜 간 공동 도전</small>${svg('chevron','clan-subchev')}</a><a href="#clan-shop"><span class="clan-feature-icon">${svg('shop')}</span><strong>클랜 상점</strong><small>보상과 꾸미기</small>${svg('chevron','clan-subchev')}</a></div></section>`;
  const cover=(c,i=0)=>`<a class="clan-cover clan-cover-${c.tone}" href="#clan-detail" data-clan-public="${c.id}">
    <div class="clan-cover-art" role="img" aria-label="${c.name} 클랜 콘텐츠 이미지"><span class="clan-cover-number">${i+1}위</span><span class="clan-cover-flag">${emblem(c.flag)}</span>
      <span class="clan-cover-symbol">${svg(c.icon)}</span>
      <span class="clan-cover-portrait" aria-hidden="true">${avatar(['이모리','밤산책','다온'][i%3],{size:'hero'})}</span>
      <span class="clan-cover-overlay"><small>${c.kind} 클랜</small><strong>${c.name}</strong></span>
    </div>
    <div class="clan-cover-caption"><strong>${c.name}</strong><small>${c.members}명 함께 · 오늘 ${c.active}명 활동</small></div>
  </a>`;
  const discoverSection=()=>`<section class="clan-section clan-discover-section">${heading('요즘 활동적인 클랜','clan-discover','전체 보기')}<div class="clan-cover-rail">${clubs.map(cover).join('')}</div></section>`;
  const rankings=()=>`<div class="clan-ranking-list">${clubs.map((c,i)=>`<a href="#clan-detail" data-clan-public="${c.id}" class="clan-ranking-row"><span class="clan-order ${i===0?'is-first':''}">${i+1}</span><span class="clan-rank-icon">${emblem(c.flag)}</span><span class="clan-rank-info"><strong>${c.name}</strong><small>${c.kind} · ${c.members}명</small></span><span class="clan-rank-value">${i===0?'1위':(i+1)+'위'}</span>${svg('chevron','clan-subchev')}</a>`).join('')}</div>`;
  const rankingSection=()=>`<section class="clan-section">${heading('공개 클랜 랭킹','clan-rank','전체 랭킹')}${rankings()}</section>`;
  const guestHero=()=>`<section class="clan-section clan-guest-hero"><div class="clan-guest-copy"><span class="clan-eyebrow">함께하는 퀘스트</span><h2>혼자 시작한 도전도<br>함께하면 더 재밌어</h2><p>친구들과 퀘스트를 실천하고<br>우리만의 기록을 만들어봐</p></div><div class="clan-guest-illustration" aria-hidden="true">${avatar('이모리',{size:'hero'})}${avatar('밤산책',{size:'hero'})}${avatar('다온',{size:'hero'})}</div><div class="clan-guest-actions"><a href="#clan-create" class="clan-btn clan-btn-main">${svg('plus')} 클랜 만들기</a><a href="#clan-invite" class="clan-btn clan-btn-alt">초대로 참여</a></div></section>`;
  const guestHelp=()=>`<section class="clan-section">${heading('클랜에서 함께하는 것')}<div class="clan-guest-benefits"><span>${svg('users')}<strong>함께 도전</strong></span><span>${svg('heart')}<strong>서로 응원</strong></span><span>${svg('gift')}<strong>공동 보상</strong></span></div></section>`;
  const root=()=>`<div class="clan-layout clan-root">${draft.joined?identity()+rewardSection()+socialSection()+rankingHome()+extraSection():guestHero()+discoverSection()+rankingSection()+guestHelp()}<div class="clan-mock-note">표시된 활동·수치·보상은 디자인 확인용 예시입니다</div></div>`;
  const inner=(h,body,action='')=>`<div class="clan-layout clan-inner"><section class="clan-section clan-inner-panel"><h2 class="clan-inner-title">${h}</h2>${body}${action?`<div class="clan-inner-action">${action}</div>`:''}</section></div>`;
  const cta=(url,text,kind='alt')=>`<a href="#${url}" class="clan-btn clan-btn-${kind}">${text}${svg('chevron')}</a>`;
  const renderCreate=()=>{const colors=[['#E9EF77','연두'],['#F6A9B2','분홍'],['#9FC2F7','파랑'],['#A3E4CE','민트'],['#C9B5F4','보라']];return `<div class="clan-layout clan-inner"><section class="clan-section clan-create"><span class="clan-eyebrow">CREATE CLAN</span><h2>우리만의 클랜을 만들어봐</h2><p class="clan-helper">클랜의 깃발과 이름을 정해봐</p><div class="clan-create-preview">${emblem()}<div><strong data-clan-preview-name>${title()}</strong><small>클랜 프로필 미리보기</small></div></div><form id="clan-create-form"><fieldset class="clan-color-field"><legend>깃발 천 색상</legend><div class="clan-color-options">${colors.map(([c,l])=>`<button type="button" class="clan-color" data-clan-color="${c}" aria-pressed="${draft.flag===c}" aria-label="${l}" style="--swatch:${c}"><span></span></button>`).join('')}</div></fieldset><label class="clan-field"><span>클랜 이름 <em>필수</em></span><input id="clan-name" required minlength="2" maxlength="20" placeholder="이름을 입력해줘" value="${safe(draft.name)}"><small>2~20자</small></label><label class="clan-field"><span>한 줄 소개 <em>선택</em></span><textarea id="clan-about" rows="2" maxlength="80" placeholder="우리 클랜을 소개해봐">${safe(draft.about)}</textarea></label><p class="clan-form-hint">${svg('users')} 친구는 만든 뒤 초대해도 돼요</p><button type="submit" class="clan-btn clan-btn-main clan-submit">클랜 만들기${svg('chevron')}</button></form></section></div>`;};
  const renderCreated=()=>`<div class="clan-layout clan-inner"><section class="clan-section clan-created"><div class="clan-created-emblem">${emblem()}</div><span class="clan-created-check">${svg('check')} 준비 완료</span><h2>${title()}</h2><p>함께 도전할 공간이 준비됐어</p><small>실제 생성·서버 저장은 이루어지지 않는 시연입니다</small>${cta('clan','클랜 홈으로','main').replace('href="#clan"','href="#clan" data-clan-activate')}${cta('clan-invite','친구 초대하기')}</section></div>`;
  const publicRows=()=>clubs.map((c,i)=>`<a href="#clan-detail" data-clan-public="${c.id}" class="clan-public-line"><span class="clan-public-line-icon">${emblem(c.flag)}</span><span><strong>${c.name}</strong><small>${c.blurb} · ${c.members}명</small></span>${svg('chevron','clan-subchev')}</a>`).join('');
  const myTabs=Object.freeze([{id:'members',label:'멤버'},{id:'record',label:'기록'},{id:'activity',label:'소식'}]);
  const myPanel=(kind=draft.myTab)=>{
    if(kind==='record')return `<div class="clan-my-panel-heading"><strong>이번 주 우리 기록</strong><small>함께 실천한 7일</small></div>
      ${recordMetrics()}
      <h3 class="clan-subheading">공동 보상 진행</h3>${progress()}
      <div class="clan-progress-foot"><span>이번 주 일요일까지</span><a href="#clan-reward">보상 상세 ${svg('chevron')}</a></div>
      <div class="clan-my-panel-heading clan-contrib-title"><strong>멤버 기여도</strong><small>이번 주</small></div>
      <div class="clan-contributors">${members.slice(0,3).map((m,i)=>`<div class="clan-contributor"><span class="clan-contributor-rank">${i+1}</span>${avatar(m.name)}<span class="clan-contributor-name">${m.name}</span><strong>+${m.pts}</strong></div>`).join('')}</div>
      <h3 class="clan-subheading">우리의 업적</h3>${achievements()}`;
    if(kind==='activity')return `<div class="clan-my-panel-heading"><strong>지금 우리 클랜</strong><small>최근 활동</small></div>
      <p class="clan-helper">공유에 동의한 활동만 표시하는 예시예요</p>${feedRows()}`;
    return `<a href="#clan-record" class="clan-members-presence">
       ${memberStack()}<span><strong>이번 주 6명이 함께했어요</strong><small>오늘 4명 활동 중</small></span>${svg('chevron','clan-subchev')}
    </a>
    <div class="clan-my-panel-heading"><strong>함께하는 멤버</strong><small>8명 중 4명 표시</small></div>
    <div class="clan-member-list">${members.map(m=>`<div class="clan-member-detail">${avatar(m.name)}<span><strong>${m.name}</strong><small>${m.sub}</small></span></div>`).join('')}</div>
    <div class="clan-inner-action">${cta('clan-invite','친구 초대')}</div>`;
  };
  const renderMy=()=>`<div class="clan-layout clan-inner"><section class="clan-section clan-inner-panel clan-my-page">
    <div class="clan-my-detail">${emblem()}<div class="clan-my-copy"><strong>${title()}</strong><p>${safe(draft.about||'함께 꾸준히 성장하는 모임')}</p><span class="clan-status-badge">${svg('users')} 멤버 8명</span></div></div>
    <div class="clan-content-tabs" role="tablist" aria-label="내 클랜 정보">
      ${myTabs.map(t=>`<button type="button" role="tab" id="clan-tab-${t.id}" aria-selected="${draft.myTab===t.id}" aria-controls="clan-tabpanel" tabindex="${draft.myTab===t.id?'0':'-1'}" data-clan-tab="${t.id}">${t.label}</button>`).join('')}
    </div>
    <section id="clan-tabpanel" class="clan-content-panel" role="tabpanel" aria-labelledby="clan-tab-${draft.myTab}" tabindex="0">${myPanel()}</section>
  </section></div>`;
  const renderReward=()=>inner('이번 주 공동 보상',`<div class="clan-reward-detail-head">${rewardVisual()}<div><strong>공동 상자</strong><p>목표 달성 후 제공될 예정인 구성</p></div></div>
  <h3 class="clan-subheading">공동 진행</h3>${progress()}
  <p class="clan-detail-note">일요일까지 · 실제 지급 시점과 조건은 미확정</p>
  <h3 class="clan-subheading">예정 꾸미기 아이템</h3>${rewardItems()}
  <p class="clan-detail-note">표시된 아이템은 시연 예시이며 보유·장착·구매 상태가 아닙니다.</p>`);
  const renderRecord=()=>`<div class="clan-layout clan-inner"><section class="clan-section clan-record-section">
    <div class="clan-record-primary"><span>공동 기록</span>${progress({showLabel:false})}<div class="clan-progress-foot"><span>일요일까지</span><a href="#clan-reward">보상 상세 ${svg('chevron')}</a></div></div>
    <h3 class="clan-subheading">주간 지표</h3>${recordMetrics()}
  </section>${contribution()}</div>`;
  function renderDetails(page){
    if(page==='clan-my')return renderMy();
    if(page==='clan-record')return renderRecord();
    if(page==='clan-activity')return inner('클랜 소식',`<p class="clan-helper">공유에 동의한 활동을 보여주는 예시예요</p>${feedRows()}`);
    if(page==='clan-discover')return inner('클랜 둘러보기',`<p class="clan-helper">함께 도전할 클랜을 찾아봐</p><div class="clan-category-tabs" role="group" aria-label="클랜 유형">${[['all','전체'],['공부','공부'],['습관','습관'],['운동','운동']].map(([v,l])=>`<button type="button" aria-pressed="${draft.clanFilter===v}" data-clan-filter="${v}">${l}</button>`).join('')}</div><div class="clan-cover-rail clan-discover-results">${clubs.filter(c=>draft.clanFilter==='all'||c.kind===draft.clanFilter).map(cover).join('')}</div>`,cta('clan-create','클랜 만들기'));
    if(page==='clan-rank')return inner('이번 주 공개 랭킹',`<p class="clan-helper">실제 점수가 아닌 화면 구성용 예시</p>${rankings()}`);
    if(page==='clan-detail'){const c=selectedClub();return inner(c.name,`<div class="clan-public-detail-banner clan-cover-${c.tone}">${emblem(c.flag)}<span><strong>${c.name}</strong><small>${c.blurb}</small></span></div><div class="clan-detail-stats"><span>${c.kind}</span><span>멤버 ${c.members}명</span><span>오늘 ${c.active}명 활동</span></div><p class="clan-helper">공개 범위와 가입 방식은 아직 결정되지 않았어요</p>`,cta('clan-invite','참여 안내 보기'))}
    if(page==='clan-reward')return renderReward();
    if(page==='clan-invite')return inner('친구 초대',`<p class="clan-helper">같이 도전할 친구에게 클랜을 소개해봐</p><div class="clan-invite-preview">${memberStack()}<strong>친구들과 함께 기록을 쌓는 공간</strong></div><p class="clan-form-hint">${svg('userPlus')} 초대 링크·QR 생성은 아직 시연에 포함되지 않았어요</p>`,cta('clan','클랜 홈으로'));
    if(page==='clan-war')return inner('클랜전',`<div class="clan-coming-soon">${svg('target')}<strong>다른 클랜과 함께 겨루는 도전</strong><p>매칭·점수·승리 조건은 아직 기획 중이에요</p></div>`);
    if(page==='clan-shop')return inner('클랜 상점',`<div class="clan-coming-soon">${svg('shop')}<strong>함께 받은 보상으로 꾸미기</strong><p>아이템·지급·교환 정책은 아직 기획 중이에요</p></div>`);
    return '';
  }
  const supported=new Set(['clan','clan-create','clan-created','clan-my','clan-record','clan-activity','clan-discover','clan-rank','clan-detail','clan-reward','clan-invite','clan-shop','clan-war']);
  window.PreviewClan={
    supports:page=>supported.has(page),
    render:page=>page==='clan'?root():page==='clan-create'?renderCreate():page==='clan-created'?renderCreated():renderDetails(page),
    previewMenu:()=>`<div class="clan-preview-menu"><strong>클랜 가입 상태 · 시연 전용</strong><div class="clan-preview-choices"><button type="button" data-clan-view="joined" aria-pressed="${draft.joined}">가입 중</button><button type="button" data-clan-view="guest" aria-pressed="${!draft.joined}">미가입</button></div></div>`
  };
  function rerender(){const p=decodeURIComponent(location.hash.slice(1)||'clan');if(!supported.has(p))return;const main=document.getElementById('main');if(main)main.innerHTML=window.PreviewClan.render(p);if(typeof window.scrollTo==='function')window.scrollTo({top:0,behavior:'instant'});}
  // SEED C-TABS: only the corresponding panel changes; hash and scroll are untouched.
  function selectMyTab(next,focus=false){
    if(!myTabs.some(t=>t.id===next))return;
    draft.myTab=next;
    const panel=document.getElementById('clan-tabpanel');
    if(!panel)return;
    panel.innerHTML=myPanel(next);
    panel.setAttribute('aria-labelledby','clan-tab-'+next);
    document.querySelectorAll('[data-clan-tab]').forEach(tab=>{
      const active=tab.dataset.clanTab===next;
      tab.setAttribute('aria-selected',String(active));
      tab.setAttribute('tabindex',active?'0':'-1');
      if(active&&focus)tab.focus();
    });
  }
  document.addEventListener('keydown',event=>{
    const tab=event.target.closest?.('[data-clan-tab]');
    if(!tab)return;
    if(!['ArrowLeft','ArrowRight','Home','End'].includes(event.key))return;
    event.preventDefault();
    const old=myTabs.findIndex(t=>t.id===tab.dataset.clanTab);
    const next=event.key==='Home'?0:event.key==='End'?myTabs.length-1:(old+(event.key==='ArrowRight'?1:-1)+myTabs.length)%myTabs.length;
    selectMyTab(myTabs[next].id,true);
  });
  document.addEventListener('click',event=>{
    const el=event.target.closest('[data-clan-view],[data-clan-color],[data-clan-heart],[data-clan-activate],[data-clan-public],[data-clan-filter],[data-clan-tab]');if(!el)return;
    if(el.hasAttribute('data-clan-view')){draft.joined=el.dataset.clanView==='joined';const sheet=document.getElementById('sheet');if(sheet?.open)sheet.close();if(location.hash!=='#clan')location.hash='#clan';else rerender();}
    else if(el.hasAttribute('data-clan-tab')){selectMyTab(el.dataset.clanTab);}
    else if(el.hasAttribute('data-clan-filter')){draft.clanFilter=el.dataset.clanFilter;document.querySelectorAll('[data-clan-filter]').forEach(b=>b.setAttribute('aria-pressed',String(b.dataset.clanFilter===draft.clanFilter)));const rail=document.querySelector('.clan-discover-results');if(rail)rail.innerHTML=clubs.filter(c=>draft.clanFilter==='all'||c.kind===draft.clanFilter).map(cover).join('');}
    else if(el.hasAttribute('data-clan-color')){draft.flag=el.dataset.clanColor;document.querySelectorAll('[data-clan-color]').forEach(b=>b.setAttribute('aria-pressed',String(b.dataset.clanColor===draft.flag)));document.querySelector('.clan-create-preview .clan-emblem')?.style.setProperty('--clan-flag',draft.flag);}
    else if(el.hasAttribute('data-clan-heart')){const key=el.dataset.clanHeart;draft.hearts[key]=!draft.hearts[key];const active=draft.hearts[key];document.querySelectorAll(`[data-clan-heart="${key}"]`).forEach(btn=>{btn.setAttribute('aria-pressed',String(active));btn.setAttribute('aria-label',`${key} 응원 ${active?'취소':'하기'}`);const count=btn.querySelector('small');if(count)count.textContent=String(reactions[key]+(active?1:0));});}
    else if(el.hasAttribute('data-clan-activate'))draft.joined=true;
    else if(el.hasAttribute('data-clan-public'))draft.selectedPublic=el.dataset.clanPublic;
  });
  document.addEventListener('input',event=>{if(event.target.id==='clan-name'){draft.name=event.target.value;event.target.setCustomValidity('');const el=document.querySelector('[data-clan-preview-name]');if(el)el.textContent=draft.name.trim()||'클랜 이름';}else if(event.target.id==='clan-about')draft.about=event.target.value;});
  document.addEventListener('submit',event=>{if(event.target.id!=='clan-create-form')return;event.preventDefault();if(!event.target.reportValidity())return;const field=event.target.querySelector('#clan-name');const n=field.value.trim();if(n.length<2){field.setCustomValidity('두 글자 이상 입력해줘');field.reportValidity();return;}draft.name=n;location.hash='#clan-created';});
})();
