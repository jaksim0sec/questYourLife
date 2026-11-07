/* Clan structure preview. All displayed activity is illustrative; no API or persistence. */
(() => {
  'use strict';
  const draft = { joined: true, name: '느슨한 갓생단', about: '각자의 속도로 꾸준히 성장하는 모임', flag: '#DB3040' };
  const shapes = {
    flag: '<path d="M4.5 21V4.5c3.5-3 7 1.9 10.5-.5 1.5-1 3-1 4.5-.5v10c-1.5-.5-3-.5-4.5.5-3.5 2.4-7-2.5-10.5.5"/>',
    users: '<circle cx="9" cy="7.5" r="3"/><path d="M3 20v-1.4C3 15.7 5.2 14 9 14s6 1.7 6 4.6V20M16 5.2a3 3 0 0 1 0 5.6M18 14.2c2 .6 3 2 3 4.4V20"/>',
    book: '<path d="M12 21V6.6C10.75 4.73 9.16 3.34 7.55 2.92Q6.6 2.66 6.6 3.72V17.25C8.62 17.55 10.42 18.77 12 21Z"/><path d="M6.6 6.48 4.35 5.98Q2.8 5.63 2.8 7.2V18.9Q2.8 19.98 3.9 19.99C7.2 20.01 9.8 20.62 12 21 15.05 19.25 17.22 18.93 20.12 18.82Q21.2 18.79 21.2 17.7V6.35Q21.2 5.25 20.1 5.38C16.9 5.74 14.5 6.58 12 8.45"/>',
    heart: '<path d="M12 6.1C7.8.6 1.5 4.8 3.1 10.3c1.1 3.6 5.1 7.5 8.9 10 3.8-2.5 7.8-6.4 8.9-10C22.5 4.8 16.2.6 12 6.1Z"/>',
    check: '<circle cx="12" cy="12" r="9.2"/><path d="m7.7 12 2.8 2.8 5.8-5.8"/>',
    gift: '<rect x="3" y="8.5" width="18" height="4.2" rx="1.3"/><path d="M5 12.7v6.1q0 1.7 1.7 1.7h10.6q1.7 0 1.7-1.7v-6.1M12 8.5v12M12 8.4C7 8.4 5.2 6.8 5.5 4.8c.4-2.6 5.8-2.6 6.5 3.6ZM12 8.4c5 0 6.8-1.6 6.5-3.6-.4-2.6-5.8-2.6-6.5 3.6Z"/>',
    chevron: '<path d="m9 5.5 6.5 6.5L9 18.5"/>',
    plus: '<path d="M12 4.5v15M4.5 12h15"/>',
    message: '<path d="M6.73 19.83C8.14 20.61 9.93 21 11.91 21 17.1 21 21 17.1 21 12S17.1 3 12 3 3 6.9 3 12C3 13.94 3.49 15.5 4.29 16.86L3.55 20.13Q3.39 20.82 4.08 20.65Z"/>',
    search: '<circle cx="10.6" cy="10.6" r="6.9"/><path d="m15.8 15.8 4.7 4.7"/>'
  };
  const icon = (name, cls = '') => '<svg class="clan-svg '+cls+'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">'+(shapes[name] || '')+'</svg>';
  const escapeText = value => String(value).replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch]));
  const name = () => escapeText(draft.name.trim() || '클랜 이름');
  const flag = () => '<span class="clan-emblem" style="--clan-flag-ink:'+draft.flag+'">'+icon('flag')+'</span>';
  const line = (left, right) => '<div class="clan-title-row"><h2>'+left+'</h2>'+right+'</div>';
  const more = (url, text) => '<a class="clan-more" href="#'+url+'">'+text+' '+icon('chevron')+'</a>';
  const note = '<p class="clan-demo-note">구조 확인용 예시 데이터 · 실제 계정 및 보상과 연결되지 않음</p>';
  const publicClans = [
    { title: '갓생 스터디단', label: '공부 · 멤버 24명', details:'이번 주 17명 활동 중', glyph:'book' },
    { title: '매일 1% 더', label: '습관 · 멤버 18명', details:'이번 주 12명 활동 중', glyph:'check' },
    { title: '주말 운동 모임', label: '운동 · 멤버 12명', details:'이번 주 9명 활동 중', glyph:'users' }
  ];
  const publicRow = entry => '<a href="#clan-detail" class="clan-list-row"><span class="clan-row-mark">'+icon(entry.glyph)+'</span><span class="clan-row-copy"><strong>'+entry.title+'</strong><small>'+entry.label+'</small></span><span class="clan-row-end">'+entry.details+icon('chevron')+'</span></a>';
  const stateBar = () => '<div class="clan-preview-switch" aria-label="시연 가입 상태 선택"><span>화면 상태</span><button type="button" data-clan-view="joined" aria-pressed="'+draft.joined+'">가입 중</button><button type="button" data-clan-view="guest" aria-pressed="'+(!draft.joined)+'">미가입</button></div>';
  const myClan = () => '<section class="clan-section clan-owned">'+
    '<a href="#clan-detail" class="clan-identity">'+flag()+'<span class="clan-identity-copy"><strong>'+name()+'</strong><small>멤버 8명 · 이번 주 6명 활동 중</small></span>'+icon('chevron')+'</a>'+
    '<div class="clan-tools-row"><a href="#dm">'+icon('message')+' 클랜 채팅</a><a href="#clan-invite">'+icon('users')+' 친구 초대</a></div>'+
    '<div class="clan-progress-head"><strong>이번 주 함께한 기록</strong><span>620 / 1,000</span></div>'+
    '<div class="clan-meter" role="progressbar" aria-label="공동 기록 진행도" aria-valuemin="0" aria-valuemax="1000" aria-valuenow="620"><span></span></div>'+
    '<div class="clan-progress-bottom"><span>공동 보상까지 380</span><a href="#clan-reward">보상 보기 '+icon('chevron')+'</a></div>'+
  '</section>';
  const guestIntro = () => '<section class="clan-section clan-guest">'+
    '<div class="clan-guest-title">'+flag()+'<div><h2>친구들과 함께할 클랜을 찾아봐</h2><p>함께 달성하고 기록을 쌓는 공간</p></div></div>'+
    '<div class="clan-action-grid"><a class="clan-btn clan-btn-primary" href="#clan-create">'+icon('plus')+' 클랜 만들기</a><a class="clan-btn clan-btn-outline" href="#clan-invite">친구 클랜 참여</a></div>'+
  '</section>';
  const activityRows = () =>
    '<div class="clan-activity-row"><span class="clan-action-mark clan-mark-check">'+icon('check')+'</span><span class="clan-row-copy"><strong>밤산책이 오늘의 퀘스트 완료</strong><small>12분 전 · 3명이 응원했어요</small></span><button class="clan-heart" type="button" aria-label="응원 표시 예시" data-clan-heart aria-pressed="false">'+icon('heart')+'</button></div>'+
    '<div class="clan-activity-row"><span class="clan-action-mark clan-mark-book">'+icon('book')+'</span><span class="clan-row-copy"><strong>이모리가 새 챕터를 시작했어요</strong><small>1시간 전 · 일본어 마스터하기</small></span><button class="clan-heart" type="button" aria-label="응원 표시 예시" data-clan-heart aria-pressed="false">'+icon('heart')+'</button></div>';
  const activities = () => '<section class="clan-section">'+line('지금 우리 클랜',more('clan-activity','활동 전체 보기'))+activityRows()+'</section>';
  const discovery = () => '<section class="clan-section">'+line('요즘 활동적인 클랜',more('clan-discover','전체 보기'))+
    publicClans.slice(0,2).map(publicRow).join('')+'</section>';
  const rankRows = () => '<a href="#clan-detail" class="clan-list-row"><span class="clan-rank">1</span><span class="clan-row-copy"><strong>갓생 스터디단</strong><small>공부 · 24명</small></span><span class="clan-rank-meta">이번 주 1위 '+icon('chevron')+'</span></a>'+
    '<a href="#clan-detail" class="clan-list-row"><span class="clan-rank">2</span><span class="clan-row-copy"><strong>매일 1% 더</strong><small>습관 · 18명</small></span><span class="clan-rank-meta">이번 주 2위 '+icon('chevron')+'</span></a>';
  const ranking = () => '<section class="clan-section">'+line('공개 클랜 랭킹',more('clan-rank','전체 랭킹'))+rankRows()+'</section>';
  function renderHome() {
    return '<div class="clan-layout clan-layout-root">'+stateBar()+
      (draft.joined ? myClan()+activities() : guestIntro())+
      discovery()+ranking()+note+'</div>';
  }
  function create() {
    const colors = [['#DB3040','빨강'],['#3987FF','파랑'],['#19C98A','초록'],['#FFAD32','주황'],['#A078FF','보라']];
    return '<div class="clan-layout clan-layout-inner">'+
      '<section class="clan-section clan-create-section">'+
      '<p class="clan-eyebrow">새 클랜</p><h2 class="clan-page-heading">우리만의 클랜을 만들어봐</h2><p class="clan-lede">깃발과 이름을 정하면 시작할 준비 끝</p>'+
      '<div class="clan-create-preview">'+flag()+'<div><strong data-clan-preview-name>'+name()+'</strong><small>클랜을 대표하는 이름과 깃발</small></div></div>'+
      '<form id="clan-create-form"><fieldset class="clan-color-fieldset"><legend>깃발 색상</legend><div class="clan-color-row">'+
        colors.map(row => '<button type="button" class="clan-color-swatch" data-clan-color="'+row[0]+'" aria-label="'+row[1]+'" aria-pressed="'+(draft.flag===row[0])+'" style="--swatch:'+row[0]+'"><span></span></button>').join('')+
      '</div></fieldset>'+
      '<label class="clan-field"><span>클랜 이름 <em>필수</em></span><input id="clan-name" type="text" required minlength="2" maxlength="20" autocomplete="off" placeholder="클랜 이름을 입력해줘" value="'+escapeText(draft.name)+'"><small>2~20자</small></label>'+
      '<label class="clan-field"><span>한 줄 소개 <em>선택</em></span><textarea id="clan-about" maxlength="80" rows="2" placeholder="우리 클랜은 어떤 모임일까?">'+escapeText(draft.about)+'</textarea><small>최대 80자 · 생성 후에도 수정할 수 있어요</small></label>'+
      '<div class="clan-quiet-hint">'+icon('users')+' 멤버 초대는 만든 뒤에 할 수 있어요</div>'+
      '<button class="clan-btn clan-btn-primary clan-submit" type="submit">클랜 만들기 '+icon('chevron')+'</button>'+
      '</form></section>'+note+'</div>';
  }
  function created() {
    return '<div class="clan-layout clan-layout-inner"><section class="clan-section clan-done">'+
      '<div class="clan-done-emblem">'+flag()+'</div><h2>'+name()+'</h2><p>클랜 화면 시연이 준비됐어</p>'+
      '<div class="clan-quiet-hint">실제 클랜 생성이나 서버 저장은 수행되지 않았어요</div>'+
      '<a class="clan-btn clan-btn-primary" href="#clan" data-clan-activate>시연 클랜으로 이동 '+icon('chevron')+'</a>'+
      '<a class="clan-btn clan-btn-outline" href="#clan-invite">초대 화면 구조 보기</a>'+
    '</section></div>';
  }
  function simple(page) {
    let title='',body='',button='';
    if(page==='clan-invite'){
      title='친구와 함께 시작하기';body='<p>친구 클랜 초대 또는 새 클랜의 멤버 초대를 위한 자리야.</p><div class="clan-list-row">'+icon('users')+'<span class="clan-row-copy"><strong>초대 링크 · QR 코드</strong><small>실제 초대 발급과 가입은 아직 미구현</small></span></div>';
      button='<a class="clan-btn clan-btn-outline" href="#clan">클랜으로 돌아가기</a>';
    }else if(page==='clan-activity'){
      title='클랜 활동';body='<p>우리가 함께 쌓아 온 기록</p>'+activityRows();button='<a class="clan-btn clan-btn-outline" href="#dm">클랜 채팅으로 이동</a>';
    }else if(page==='clan-discover'){
      title='클랜 둘러보기';body='<p>각자 다른 목표를 함께 이루는 클랜</p>'+publicClans.map(publicRow).join('');button='<a class="clan-btn clan-btn-outline" href="#clan-create">직접 만들기</a>';
    }else if(page==='clan-rank'){
      title='공개 클랜 랭킹';body='<p>순위 데이터는 화면 설계를 위한 예시야</p>'+rankRows();button='<a class="clan-btn clan-btn-outline" href="#clan-discover">클랜 둘러보기</a>';
    }else if(page==='clan-detail'){
      title='갓생 스터디단';body='<p>공부 · 멤버 24명 · 이번 주 17명 활동 중</p><div class="clan-list-row">'+icon('book')+'<span class="clan-row-copy"><strong>하루 20분씩 함께 공부하기</strong><small>공개 클랜 상세 구조 예시</small></span></div>';button='<a class="clan-btn clan-btn-outline" href="#clan-invite">참여 절차 살펴보기</a>';
    }else{
      title='공동 보상';body='<p>이번 주 클랜 기록 620 / 1,000</p><div class="clan-meter" role="progressbar" aria-label="공동 기록 진행도" aria-valuemin="0" aria-valuemax="1000" aria-valuenow="620"><span></span></div><div class="clan-quiet-hint">'+icon('gift')+' 보상 구성과 지급 규칙은 미확정</div>';button='<a class="clan-btn clan-btn-outline" href="#clan">클랜으로 돌아가기</a>';
    }
    return '<div class="clan-layout clan-layout-inner"><section class="clan-section"><h2 class="clan-page-heading">'+title+'</h2><div class="clan-simple-body">'+body+'</div><div class="clan-simple-action">'+button+'</div></section>'+note+'</div>';
  }
  const supported = new Set(['clan','clan-create','clan-created','clan-invite','clan-activity','clan-discover','clan-rank','clan-detail','clan-reward']);
  window.PreviewClan = { supports: p => supported.has(p), render: p => p==='clan'?renderHome():p==='clan-create'?create():p==='clan-created'?created():simple(p) };
  function refresh() {
    const route = decodeURIComponent(location.hash.slice(1) || 'clan');
    if(supported.has(route))document.getElementById('main').innerHTML=window.PreviewClan.render(route);
  }
  document.addEventListener('click', event => {
    const pressed = event.target.closest('[data-clan-view],[data-clan-color],[data-clan-heart],[data-clan-activate]');
    if(!pressed)return;
    if(pressed.hasAttribute('data-clan-view')){
      draft.joined = pressed.dataset.clanView === 'joined';
      refresh();
    }else if(pressed.hasAttribute('data-clan-color')){
      draft.flag = pressed.dataset.clanColor;
      refresh();
    }else if(pressed.hasAttribute('data-clan-heart')){
      pressed.setAttribute('aria-pressed',pressed.getAttribute('aria-pressed')!=='true'?'true':'false');
    }else if(pressed.hasAttribute('data-clan-activate')){
      draft.joined = true;
    }
  });
  document.addEventListener('input', event => {
    if(event.target.id==='clan-name'){
      draft.name=event.target.value;
      const label=document.querySelector('[data-clan-preview-name]');
      if(label)label.textContent=draft.name.trim()||'클랜 이름';
    }
    if(event.target.id==='clan-about')draft.about=event.target.value;
  });
  document.addEventListener('submit', event => {
    if(event.target.id!=='clan-create-form')return;
    event.preventDefault();
    if(!event.target.reportValidity())return;
    const value=document.getElementById('clan-name').value.trim();
    if(value.length<2){document.getElementById('clan-name').setCustomValidity('두 글자 이상 입력해줘');document.getElementById('clan-name').reportValidity();return;}
    draft.name=value;
    location.hash='#clan-created';
  });
  document.addEventListener('input',event=>{if(event.target.id==='clan-name')event.target.setCustomValidity('');});
})();
