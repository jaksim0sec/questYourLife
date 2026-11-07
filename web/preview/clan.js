/* Clan UI-only prototype. All fixtures are illustrative and stay in session memory. */
(() => {
  'use strict';
  const draft = { joined: true, name: '느슨한 갓생단', about: '조금씩이라도 함께 꾸준히', flag: '#DB3040', selectedPublic: 'study' };
  const icons = {
    flag:'<path d="M4.5 21V4.5c3.5-3 7 1.9 10.5-.5 1.5-1 3-1 4.5-.5v10c-1.5-.5-3-.5-4.5.5-3.5 2.4-7-2.5-10.5.5"/>',
    users:'<circle cx="9" cy="7.5" r="3"/><path d="M3 20v-1.4C3 15.7 5.2 14 9 14s6 1.7 6 4.6V20M16 5.2a3 3 0 0 1 0 5.6M18 14.2c2 .6 3 2 3 4.4V20"/>',
    book:'<path d="M12 21V6.6C10.75 4.73 9.16 3.34 7.55 2.92Q6.6 2.66 6.6 3.72V17.25C8.62 17.55 10.42 18.77 12 21Z"/><path d="M6.6 6.48 4.35 5.98Q2.8 5.63 2.8 7.2V18.9Q2.8 19.98 3.9 19.99C7.2 20.01 9.8 20.62 12 21 15.05 19.25 17.22 18.93 20.12 18.82Q21.2 18.79 21.2 17.7V6.35Q21.2 5.25 20.1 5.38C16.9 5.74 14.5 6.58 12 8.45"/>',
    heart:'<path d="M12 6.1C7.8.6 1.5 4.8 3.1 10.3c1.1 3.6 5.1 7.5 8.9 10 3.8-2.5 7.8-6.4 8.9-10C22.5 4.8 16.2.6 12 6.1Z"/>',
    check:'<circle cx="12" cy="12" r="9.2"/><path d="m7.7 12 2.8 2.8 5.8-5.8"/>',
    gift:'<rect x="3" y="8.5" width="18" height="4.2" rx="1.3"/><path d="M5 12.7v6.1q0 1.7 1.7 1.7h10.6q1.7 0 1.7-1.7v-6.1M12 8.5v12M12 8.4C7 8.4 5.2 6.8 5.5 4.8c.4-2.6 5.8-2.6 6.5 3.6ZM12 8.4c5 0 6.8-1.6 6.5-3.6-.4-2.6-5.8-2.6-6.5 3.6Z"/>',
    chevron:'<path d="m9 5.5 6.5 6.5L9 18.5"/>',
    plus:'<path d="M12 4.5v15M4.5 12h15"/>',
    message:'<path d="M6.73 19.83C8.14 20.61 9.93 21 11.91 21 17.1 21 21 17.1 21 12S17.1 3 12 3 3 6.9 3 12C3 13.94 3.49 15.5 4.29 16.86L3.55 20.13Q3.39 20.82 4.08 20.65Z"/>',
    activity:'<path d="M3 12h4l2.4-6.5 4.1 13 2.5-6.5h5"/>',
    flame:'<path d="M12.4 2.8c.5 4.1 4.1 5 4.1 8.1 1.1-.8 1.7-1.8 1.9-3.1 2 2.5 2.6 4.7 2 7.1-.8 3.9-3.6 6.3-7.9 6.3-4.8 0-8-3.3-8-7.6 0-2.4 1.3-4.7 3.2-6.1-.1 2 .5 3.2 1.6 4 0-4.7 3.1-5.1 3.1-8.7Z"/>',
    trophy:'<path d="M7 3.5h10V9a5 5 0 0 1-10 0ZM7 5H3.5v2.5Q3.5 12 8 12M17 5h3.5v2.5q0 4.5-4.5 4.5M12 14v6M7.5 20.5h9"/>',
    shop:'<rect x="4" y="7.5" width="16" height="13" rx="2.5"/><path d="M8 8V6a4 4 0 0 1 8 0v2"/>',
    target:'<circle cx="12" cy="12" r="9.2"/><circle cx="12" cy="12" r="5"/><circle cx="12" cy="12" r="1.1"/>',
    userPlus:'<circle cx="8.5" cy="7.2" r="2.9"/><path d="M2.5 20v-1.2c0-2.8 2.2-4.5 6-4.5s6 1.7 6 4.5V20M19 8v5M16.5 10.5h5"/>',
    clock:'<circle cx="12" cy="12" r="9.2"/><path d="M12 6.8v5.5l-3.3 2.5"/>',
    ranking:'<path d="M2.5 20.5h19M4 20.5v-8q0-1.5 1.5-1.5H9M9 20.5V5q0-1.5 1.5-1.5h3Q15 3.5 15 5v15.5M15 14h3.5q1.5 0 1.5 1.5v5"/>'
  };
  const svg = (key,cls='') => `<svg class="clan-svg ${cls}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${icons[key]||''}</svg>`;
  const safe = text => String(text).replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch]));
  const title = () => safe(draft.name.trim()||'클랜 이름');
  const emblem = (color=draft.flag) => `<span class="clan-emblem" style="--clan-flag:${color}">${svg('flag')}</span>`;
  const heading = (label,url,text='전체 보기') => `<div class="clan-heading"><h2>${label}</h2>${url?`<a href="#${url}" class="clan-more">${text}${svg('chevron')}</a>`:''}</div>`;
  // Equipment IDs, files and normalized coordinates mirror Android AvatarComposer/CosmeticArtwork.
  // In this fixture the user names select example outfits, never colors or avatar shapes.
  const avatarEquipment = Object.freeze({
    '이모리':  {id:0,    file:'cosmetic_beanie.png',             x:.075, y:.055, width:.85,  height:.612},
    '밤산책':  {id:1003, file:'cosmetic_black_headphones.png', x:.030, y:.175, width:.94,  height:.685},
    '모찌':    {id:1,    file:'cosmetic_cream_cap.png',         x:.045, y:.060, width:.91,  height:.582},
    '다온':    {id:2,    file:'cosmetic_blue_cap.png',          x:.065, y:.015, width:.87,  height:.582}
  });
  const avatar = (member,active=false) => {
    const item = avatarEquipment[member];
    const equip = item ? `<img class="clan-avatar-item" src="assets/${item.file}" alt="" loading="lazy" draggable="false" style="left:${item.x*100}%;top:${item.y*100}%;width:${item.width*100}%;height:${item.height*100}%">` : '';
    return `<span class="clan-avatar${active?' clan-avatar-active':''}" aria-hidden="true" data-avatar-member="${safe(member)}"${item?` data-demo-equipment-id="${item.id}"`:''}><img class="clan-avatar-face" src="assets/mascot_face_stamp.png" alt="" loading="lazy" draggable="false">${equip}</span>`;
  };
  const clubData = [
    {id:'study',name:'갓생 스터디단',label:'공부',members:24,active:12,icon:'book'},
    {id:'daily',name:'매일 1% 더',label:'습관',members:18,active:9,icon:'activity'},
    {id:'fitness',name:'주말 운동 모임',label:'운동',members:12,active:6,icon:'target'}
  ];
  const publicRow = (entry,compact=false) => `<a href="#clan-detail" data-clan-public="${entry.id}" class="clan-public-row"><span class="clan-public-symbol">${svg(entry.icon)}</span><span class="clan-row-copy"><strong>${entry.name}</strong><small>${entry.label} · 멤버 ${entry.members}명</small></span><span class="clan-public-presence"><i aria-hidden="true"></i>${compact?entry.active+'명 활동':'오늘 '+entry.active+'명 활동 중'}</span>${svg('chevron','clan-row-chevron')}</a>`;
  const badge = (icon,label,caption,tone) => `<span class="clan-achievement"><span class="clan-achievement-symbol clan-tone-${tone}">${svg(icon)}</span><strong>${label}</strong><small>${caption}</small></span>`;
  const feedRow = (who,what,when,reaction) => `<div class="clan-feed-row">${avatar(who)}<div class="clan-feed-copy"><div><strong>${who}</strong><span>${when}</span></div><p>${what}</p></div><button type="button" data-clan-heart class="clan-reaction" aria-pressed="false" aria-label="${who}에게 응원 보내기">${svg('heart')}<span>${reaction}</span></button></div>`;
  const feed = () => feedRow('밤산책','오늘의 퀘스트 2개 완료','12분 전','3')+
    feedRow('모찌','4일 연속 도전 성공','38분 전','5')+
    feedRow('이모리','첫 공동 보상에 기여했어요','2시간 전','2');
  const progressBody = () => `<div class="clan-reward-summary"><div class="clan-reward-icon">${svg('gift')}</div><div class="clan-reward-copy"><strong>함께 만든 기록이 보상으로</strong><small>이번 주 6명이 힘을 보태고 있어요</small></div><span class="clan-reward-percent">62%</span></div>
    <div class="clan-meter-head"><span>공동 기록</span><strong>620 <small>/ 1,000</small></strong></div>
    <div class="clan-meter" role="progressbar" aria-label="이번 주 공동 기록" aria-valuemin="0" aria-valuemax="1000" aria-valuenow="620"><span></span></div>
    <div class="clan-meter-foot"><span>완료까지 380 남음</span><span>${svg('clock')} 일요일까지</span></div>`;
  const progressSection = () => `<section class="clan-section clan-progress-section">${heading('이번 주 함께 여는 상자','clan-reward','보상 보기')}${progressBody()}</section>`;
  const ownedTop = () => `<section class="clan-section clan-my-top"><a href="#clan-my" class="clan-identity">${emblem()}<span class="clan-row-copy"><strong class="clan-club-name">${title()}</strong><small>멤버 8명 · 이번 주 6명 참여</small></span>${svg('chevron','clan-row-chevron')}</a>
    <div class="clan-presence"><div class="clan-presence-faces">${avatar('이모리',true)}${avatar('밤산책',true)}${avatar('모찌',true)}${avatar('다온',true)}</div><div class="clan-presence-info"><strong>오늘도 같이 도전 중</strong><small>4명이 기록을 남겼어요</small></div><a href="#clan-activity" aria-label="클랜 활동 보기">${svg('chevron')}</a></div>
    <div class="clan-quick-actions"><a href="#dm">${svg('message')}<span>클랜 채팅</span></a><a href="#clan-invite">${svg('userPlus')}<span>친구 초대</span></a></div></section>`;
  const activitySection = () => `<section class="clan-section">${heading('방금 올라온 클랜 소식','clan-activity','소식 전체')}
    ${feedRow('밤산책','오늘의 퀘스트 2개 완료','12분 전','3')}
    ${feedRow('모찌','4일 연속 도전 성공','38분 전','5')}
    <a href="#clan-activity" class="clan-text-link">최근 활동 더 보기 ${svg('chevron')}</a></section>`;
  const badgeRail = () => `<div class="clan-badge-rail">${badge('flame','7일 연속','함께 출석','orange')}${badge('trophy','100회 달성','누적 도전','blue')}${badge('flag','첫 클랜','함께한 시작','purple')}</div>`;
  const achievementSection = () => `<section class="clan-section">${heading('우리 클랜의 발자국','clan-my','기록 보기')}${badgeRail()}</section>`;
  const publicSection = () => `<section class="clan-section">${heading('요즘 활동적인 클랜','clan-discover')}${publicRow(clubData[0],true)}${publicRow(clubData[1],true)}</section>`;
  const rankingSection = () => `<section class="clan-section clan-ranking-section">${heading('공개 클랜 랭킹','clan-rank','전체 랭킹')}
    <a class="clan-rank-row" data-clan-public="study" href="#clan-detail"><span class="clan-rank-num clan-rank-first">1</span><span>갓생 스터디단</span><small>이번 주 1위</small>${svg('chevron')}</a>
    <a class="clan-rank-row" data-clan-public="daily" href="#clan-detail"><span class="clan-rank-num">2</span><span>매일 1% 더</span><small>이번 주 2위</small>${svg('chevron')}</a></section>`;
  const extraSection = () => `<section class="clan-section">${heading('같이 즐기는 클랜')}
    <div class="clan-extra-grid"><a href="#clan-war">${svg('target')}<span><strong>클랜전</strong><small>다른 클랜과 겨뤄보기</small></span>${svg('chevron')}</a><a href="#clan-shop">${svg('shop')}<span><strong>클랜 상점</strong><small>함께 모은 보상 활용하기</small></span>${svg('chevron')}</a></div></section>`;
  const guestIntro = () => `<section class="clan-section clan-guest-intro"><div class="clan-guest-lead">${emblem()}<div><h2>같이할 때 더 재밌는 도전</h2><p>친구들과 작은 목표부터 함께 시작해봐</p></div></div><div class="clan-guest-actions"><a class="clan-btn clan-btn-main" href="#clan-create">${svg('plus')}클랜 만들기</a><a class="clan-btn clan-btn-alt" href="#clan-invite">친구 클랜 참여</a></div></section>`;
  const guestHelp = () => `<section class="clan-section">${heading('클랜에서는 이런 걸 해요')}
    <div class="clan-intro-features"><div><span>${svg('users')}</span><strong>같이 도전</strong><small>서로 응원하기</small></div><div><span>${svg('gift')}</span><strong>공동 보상</strong><small>함께 모으기</small></div><div><span>${svg('trophy')}</span><strong>우리의 기록</strong><small>업적 쌓기</small></div></div></section>`;
  const renderRoot = () => `<div class="clan-layout clan-root">${draft.joined?ownedTop()+progressSection()+activitySection()+achievementSection()+extraSection():guestIntro()+publicSection()+rankingSection()+guestHelp()}${draft.joined?publicSection()+rankingSection():''}</div>`;
  const renderCreate = () => {const colors=[['#DB3040','빨강'],['#3987FF','파랑'],['#19C98A','초록'],['#FFAD32','주황'],['#A078FF','보라']];return `<div class="clan-layout clan-inner"><section class="clan-section clan-create"><h2>우리만의 클랜을 만들어봐</h2><p class="clan-helper">이름과 깃발만 정하면 시작할 수 있어</p>
    <div class="clan-create-preview">${emblem()}<div><strong data-clan-preview-name>${title()}</strong><small>클랜 깃발 미리보기</small></div></div>
    <form id="clan-create-form"><fieldset class="clan-color-field"><legend>깃발 색상</legend><div class="clan-color-options">${colors.map(([color,label])=>`<button type="button" class="clan-color" data-clan-color="${color}" aria-pressed="${draft.flag===color}" aria-label="${label}" style="--swatch:${color}"><span></span></button>`).join('')}</div></fieldset>
      <label class="clan-field"><span>클랜 이름 <em>필수</em></span><input id="clan-name" name="name" required minlength="2" maxlength="20" placeholder="이름을 입력해줘" value="${safe(draft.name)}"><small>2~20자</small></label>
      <label class="clan-field"><span>한 줄 소개 <em>선택</em></span><textarea id="clan-about" name="about" rows="2" maxlength="80" placeholder="우리 클랜을 소개해봐">${safe(draft.about)}</textarea></label>
      <div class="clan-form-hint">${svg('users')}친구 초대는 만든 뒤에 해도 돼</div>
      <button type="submit" class="clan-btn clan-btn-main clan-submit">클랜 만들기${svg('chevron')}</button></form></section></div>`;};
  const renderCreated = () => `<div class="clan-layout clan-inner"><section class="clan-section clan-created">${emblem()}<h2>${title()}</h2><p>클랜을 만들었어! 친구들과 함께 시작해봐</p><small>이 화면은 시연이며 실제 저장되지는 않아</small><a href="#clan" class="clan-btn clan-btn-main" data-clan-activate>내 클랜으로 이동${svg('chevron')}</a><a href="#clan-invite" class="clan-btn clan-btn-alt">친구 초대하기</a></section></div>`;
  const inner = (headingText,content,action='') => `<div class="clan-layout clan-inner"><section class="clan-section clan-inner-section"><h2 class="clan-inner-title">${headingText}</h2>${content}${action?`<div class="clan-inner-action">${action}</div>`:''}</section></div>`;
  function renderDetails(page){
    if(page==='clan-my')return inner(title(),`<div class="clan-detail-heading">${emblem()}<p>${safe(draft.about||'각자의 속도로 함께')}</p></div><div class="clan-detail-facts"><span>8명 참여</span><span>이번 주 6명 활동</span></div><h3 class="clan-detail-subheading">함께한 업적</h3>${badgeRail()}<h3 class="clan-detail-subheading">최근 활동</h3>${feed()}`,`<a href="#clan-invite" class="clan-btn clan-btn-alt">친구 초대</a>`);
    if(page==='clan-activity')return inner('클랜의 오늘',`<p class="clan-detail-sub">최근 함께 쌓아 온 기록</p>${feed()}`);
    if(page==='clan-discover')return inner('공개 클랜 둘러보기',`<p class="clan-detail-sub">다른 클랜의 활동을 찾아봐</p>${clubData.map(x=>publicRow(x)).join('')}`,`<a href="#clan-create" class="clan-btn clan-btn-alt">새 클랜 만들기</a>`);
    if(page==='clan-rank')return inner('이번 주 공개 랭킹',`<p class="clan-detail-sub">점수와 순위는 시연용 예시</p>${clubData.map((x,i)=>`<a href="#clan-detail" data-clan-public="${x.id}" class="clan-rank-row"><span class="clan-rank-num">${i+1}</span><span>${x.name}</span><small>멤버 ${x.members}명</small>${svg('chevron')}</a>`).join('')}`);
    if(page==='clan-detail'){const c=clubData.find(x=>x.id===draft.selectedPublic)||clubData[0];return inner(c.name,`<div class="clan-detail-heading"><span class="clan-emblem">${svg(c.icon)}</span><p>${c.label} · 멤버 ${c.members}명</p></div><div class="clan-detail-facts"><span>오늘 ${c.active}명 활동</span><span>공개 클랜</span></div><div class="clan-form-hint">${svg('users')}참여 및 공개 범위는 추후 설계</div>`, `<a href="#clan-discover" class="clan-btn clan-btn-alt">다른 클랜 보기</a>`);}
    if(page==='clan-reward')return inner('이번 주 공동 보상',`<p class="clan-detail-sub">6명이 함께 620의 기록을 쌓았어</p>${progressBody()}<div class="clan-form-hint">${svg('gift')}구체적인 보상 지급 규칙은 아직 정해지지 않았어</div>`);
    if(page==='clan-invite')return inner('친구 초대',`<p class="clan-detail-sub">클랜은 함께할수록 즐거워</p><div class="clan-form-hint">${svg('userPlus')}초대 링크와 QR 코드는 추후 연결 예정</div>`, `<a href="#clan" class="clan-btn clan-btn-alt">클랜 홈으로</a>`);
    if(page==='clan-shop')return inner('클랜 상점',`<div class="clan-detail-lead">${svg('shop')}<p>함께 얻은 보상을 사용할 공간</p></div><div class="clan-form-hint">아이템과 재화 정책은 아직 기획 중</div>`);
    if(page==='clan-war')return inner('클랜전',`<div class="clan-detail-lead">${svg('target')}<p>규모에 상관없이 즐기는 클랜 간 도전</p></div><div class="clan-form-hint">매칭·점수·승리 조건은 아직 기획 중</div>`);
    return '';
  }
  const supported = new Set(['clan','clan-create','clan-created','clan-my','clan-activity','clan-discover','clan-rank','clan-detail','clan-reward','clan-invite','clan-shop','clan-war']);
  window.PreviewClan = {
    supports: page => supported.has(page),
    render: page => page==='clan'?renderRoot():page==='clan-create'?renderCreate():page==='clan-created'?renderCreated():renderDetails(page),
    previewMenu: () => `<div class="clan-preview-menu"><div>클랜 시연 상태</div><div class="clan-preview-choices"><button type="button" data-clan-view="joined" aria-pressed="${draft.joined}">가입 중</button><button type="button" data-clan-view="guest" aria-pressed="${!draft.joined}">미가입</button></div></div>`
  };
  const rerender = () => {const page=decodeURIComponent(location.hash.slice(1)||'clan');if(supported.has(page)){const main=document.getElementById('main');if(main)main.innerHTML=window.PreviewClan.render(page);window.scrollTo({top:0,behavior:'instant'});}};
  document.addEventListener('click',e=>{
    const trigger=e.target.closest('[data-clan-view], [data-clan-color], [data-clan-heart], [data-clan-activate], [data-clan-public]');
    if(!trigger)return;
    if(trigger.hasAttribute('data-clan-view')){
      draft.joined = trigger.dataset.clanView==='joined';
      const sheet=document.getElementById('sheet');if(sheet?.open)sheet.close();
      if(location.hash!=='#clan'){location.hash='#clan';}else rerender();
    }else if(trigger.hasAttribute('data-clan-color')){
      draft.flag=trigger.dataset.clanColor;
      // Keep the form values and keyboard focus during swatch changes.
      document.querySelectorAll('[data-clan-color]').forEach(btn=>btn.setAttribute('aria-pressed',String(btn.dataset.clanColor===draft.flag)));
      const mark=document.querySelector('.clan-create-preview .clan-emblem');if(mark)mark.style.setProperty('--clan-flag',draft.flag);
    }else if(trigger.hasAttribute('data-clan-heart')){
      const active=trigger.getAttribute('aria-pressed')==='true';trigger.setAttribute('aria-pressed',String(!active));
      const count=trigger.querySelector('span');if(count)count.textContent=String(Number(count.textContent)+(active?-1:1));
    }else if(trigger.hasAttribute('data-clan-activate')){
      draft.joined=true;
    }else if(trigger.hasAttribute('data-clan-public')){
      draft.selectedPublic=trigger.dataset.clanPublic;
    }
  });
  document.addEventListener('input',e=>{
    if(e.target.id==='clan-name'){
      draft.name=e.target.value;e.target.setCustomValidity('');
      const preview=document.querySelector('[data-clan-preview-name]');if(preview)preview.textContent=draft.name.trim()||'클랜 이름';
    }else if(e.target.id==='clan-about')draft.about=e.target.value;
  });
  document.addEventListener('submit',e=>{
    if(e.target.id!=='clan-create-form')return;
    e.preventDefault();
    if(!e.target.reportValidity())return;
    const value=e.target.querySelector('#clan-name').value.trim();
    if(value.length<2){const field=e.target.querySelector('#clan-name');field.setCustomValidity('2글자 이상 입력해줘');field.reportValidity();return;}
    draft.name=value;
    location.hash='#clan-created';
  });
})();
