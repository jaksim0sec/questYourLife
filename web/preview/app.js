/* Reconstruction from two user-supplied MHTML snapshots.
 * Faithfully keeps their original HTML, CSS, inline SVG and image assets.
 * Only the hash navigation, shell swaps, and optional menu/theme demo are restored.
 * Uncaptured route bodies deliberately remain empty. */
(() => {
  'use strict';
  const header = document.getElementById('header');
  const main = document.getElementById('main');
  const nav = document.getElementById('nav');
  const sheet = document.getElementById('sheet');
  const sheetTitle = document.getElementById('sheet-title');
  const sheetBody = sheet.querySelector('.sheet-body');
  const homeHeader = header.innerHTML;
  const homeMain = main.innerHTML;
  const exploreHeader = document.getElementById('explore-header-template').innerHTML;
  const standardHeader = (() => {
    const root = document.createElement('div');
    root.innerHTML = exploreHeader;
    return root.querySelector('.head').outerHTML;
  })();

  const routes = {
    home: ['홈', 'home', 'global'],
    quests: ['퀘스트', 'quests', 'global'],
    dm: ['디엠', 'dm', 'global'],
    clan: ['클랜', 'clan', 'global'],
    'clan-create': ['클랜 만들기', 'clan', 'global-inner'],
    'clan-created': ['클랜 생성 시연', 'clan', 'global-inner'],
    'clan-invite': ['클랜 초대', 'clan', 'global-inner'],
    'clan-my': ['내 클랜', 'clan', 'global-inner'],
    'clan-record': ['이번 주 기록', 'clan', 'global-inner'],
    'clan-activity': ['클랜 활동', 'clan', 'global-inner'],
    'clan-discover': ['클랜 둘러보기', 'clan', 'global-inner'],
    'clan-rank': ['클랜 랭킹', 'clan', 'global-inner'],
    'clan-reward': ['공동 보상', 'clan', 'global-inner'],
    'clan-war': ['클랜전', 'clan', 'global-inner'],
    'clan-shop': ['클랜 상점', 'clan', 'global-inner'],
    me: ['마이', 'me', 'global'],
    explore: ['퀘스트북 탐색', 'home', 'global-inner'],
    daily: ['일일 퀘스트', 'quests', 'global-inner'],
    rank: ['랭킹', 'clan', 'global-inner'],
    'clan-detail': ['클랜 상세', 'clan', 'global-inner'],
    'friend-profile': ['프로필', 'me', 'global-inner'],
    'book/focus': ['집중력 부스터 14일', 'quests', 'global-inner'],
    'book/move': ['10분 운동 루틴', 'quests', 'global-inner'],
    'book/sleep': ['7일 수면 리셋', 'quests', 'global-inner']
  };
  let previousPage = 'home';
  let currentPage = 'home';

  const routeName = () => decodeURIComponent(location.hash.slice(1) || 'home');
  const isRoot = key => routes[key]?.[2] === 'global';

  // Official SVG assets are referenced from the shared root, not copied into preview.
  const navIcons = {home:['home','home-fill'], quests:['check-circle','check-circle-fill'], dm:['chat','chat-fill'], clan:['bookmark','clan-bookmark-fill'], me:['me-smile','me-smile-fill']};
  function updateNav(active) {
    nav.querySelectorAll('.nav-link').forEach(link => {
      const key=link.getAttribute('href').slice(1);
      if (key===active) link.setAttribute('aria-current','page'); else link.removeAttribute('aria-current');
      const node=link.querySelector('.seed-icon');
      if (!node || !navIcons[key]) return;
      const file=navIcons[key][key===active?1:0];
      const path=(key==='me'||(key==='clan'&&key===active)) ? '../../assets/icons/preview/' : '../../assets/icons/soft/v0.14/';
      node.style.setProperty('--seed-icon', "url('"+path+file+".svg')");
    });
  }

  function render() {
    const next = routeName();
    const page = routes[next] ? next : 'home';
    const [title, active, layer] = routes[page];
    if (next !== page) {
      location.replace('#home');
      return;
    }
    if (currentPage !== page) previousPage = currentPage;
    currentPage = page;
    document.body.dataset.layer = layer;

    if (page === 'home') {
      header.innerHTML = homeHeader;
      main.innerHTML = homeMain;
    } else if (window.PreviewClan && window.PreviewClan.supports(page)) {
      header.innerHTML = page === 'clan' ? homeHeader : standardHeader;
      header.querySelector('h1').textContent = title;
      if (page === 'clan') header.querySelector('[data-go="explore"]')?.remove();
      main.innerHTML = window.PreviewClan.render(page);
    } else if (page === 'explore') {
      header.innerHTML = exploreHeader;
      main.replaceChildren(); // Explicitly empty in original MHTML.
    } else if (isRoot(page)) {
      const h = document.createElement('h1');
      h.textContent = title;
      header.innerHTML = '<div class="head"><h1></h1><div class="tools"></div></div>';
      header.querySelector('h1').replaceWith(h);
      main.replaceChildren(); // No fabricated app content.
    } else {
      header.innerHTML = standardHeader;
      header.querySelector('h1').textContent = title;
      main.replaceChildren(); // Only home and explore were provided.
    }
    document.title = `${title} · 갓생패치`;
    updateNav(active);
    if (sheet.open) sheet.close();
    window.scrollTo({top: 0, behavior: 'instant'});
  }

  const icon = name => '<span class="seed-icon menu-theme-icon" aria-hidden="true" style="--seed-icon:url(\'../../assets/icons/preview/'+name+'.svg\')"></span>';

  function showSheet(type) {
    if (type === 'menu') {
      sheetTitle.textContent = '메뉴';
      sheetBody.innerHTML = `<button class="menu-row" type="button" data-theme-switch="light">${icon('sun')} 라이트 테마 <span class="menu-state">${document.documentElement.dataset.theme === 'light' ? '사용 중' : ''}</span></button>
      <button class="menu-row" type="button" data-theme-switch="dark" style="margin-top:.5rem">${icon('moon')} 다크 테마 <span class="menu-state">${document.documentElement.dataset.theme === 'dark' ? '사용 중' : ''}</span></button>`;
      // The preview-only membership switch belongs in the existing menu, not the content hierarchy.
      if (routeName() === 'clan' && window.PreviewClan?.previewMenu) {
        sheetBody.insertAdjacentHTML('beforeend', window.PreviewClan.previewMenu());
      }
    } else {
      sheetTitle.textContent = '알림';
      sheetBody.replaceChildren(); // Original snapshot contained no notification content.
    }
    if (!sheet.open) sheet.showModal();
  }

  document.addEventListener('click', event => {
    const trigger = event.target.closest('[data-go], [data-sheet], [data-close], [data-back], [data-theme-switch]');
    if (!trigger) return;
    if (trigger.hasAttribute('data-go')) location.hash = '#'+trigger.dataset.go;
    if (trigger.hasAttribute('data-sheet')) showSheet(trigger.dataset.sheet);
    if (trigger.hasAttribute('data-close') && sheet.open) sheet.close();
    if (trigger.hasAttribute('data-back')) location.hash = '#'+(previousPage !== currentPage ? previousPage : 'home');
    if (trigger.hasAttribute('data-theme-switch')) {
      const theme = trigger.dataset.themeSwitch;
      document.documentElement.dataset.theme = theme;
      document.querySelector('meta[name="theme-color"]').content = theme === 'dark' ? '#17181d' : '#fcfbfa';
      showSheet('menu');
    }
  });
  window.addEventListener('hashchange', render);
  render();
})();
