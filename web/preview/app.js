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

  // Official SEED v0.14 SVG shapes, sourced from docs/design/seed/assets/icons/v0.14.
  // These swap only the already-approved active/inactive bottom-nav states.
  const navIconStates = {
    home: {
      outline: '<path d="M3.2 10.1C3.2 9.25 3.48 8.75 4.18 8.2L10.65 3.16C11.48 2.51 12.52 2.51 13.35 3.16L19.82 8.2C20.52 8.75 20.8 9.25 20.8 10.1V18.7C20.8 20.05 20.12 20.8 18.8 20.8H14.5V15.4C14.5 13.9 13.5 12.85 12 12.85S9.5 13.9 9.5 15.4V20.8H5.2C3.88 20.8 3.2 20.05 3.2 18.7Z"/>',
      fill: '<g fill="currentColor"><path d="M3.2 10.1C3.2 9.25 3.48 8.75 4.18 8.2L10.65 3.16C11.48 2.51 12.52 2.51 13.35 3.16L19.82 8.2C20.52 8.75 20.8 9.25 20.8 10.1V18.7C20.8 20.05 20.12 20.8 18.8 20.8H14.5V15.4C14.5 13.9 13.5 12.85 12 12.85S9.5 13.9 9.5 15.4V20.8H5.2C3.88 20.8 3.2 20.05 3.2 18.7Z"/></g>'
    },
    quests: {
      outline: '<circle cx="12" cy="12" r="9.2"/><path d="m7.7 12 2.8 2.8 5.8-5.8"/>',
      fill: '<defs><mask id="preview-check-cut"><rect width="24" height="24" fill="white" stroke="none"/><g fill="black" stroke="black" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path fill="none" d="m7.7 12 2.8 2.8 5.8-5.8"/></g></mask></defs><g fill="currentColor" mask="url(#preview-check-cut)"><circle cx="12" cy="12" r="9.2"/></g>'
    },
    dm: {
      outline: '<path d="M6.73 19.83C8.14 20.61 9.93 21 11.91 21 17.1 21 21 17.1 21 12S17.1 3 12 3 3 6.9 3 12C3 13.94 3.49 15.5 4.29 16.86L3.55 20.13Q3.39 20.82 4.08 20.65Z"/>',
      fill: '<g fill="currentColor"><path d="M6.73 19.83C8.14 20.61 9.93 21 11.91 21 17.1 21 21 17.1 21 12S17.1 3 12 3 3 6.9 3 12C3 13.94 3.49 15.5 4.29 16.86L3.55 20.13Q3.39 20.82 4.08 20.65Z"/></g>'
    },
    clan: {
      // Preserve the original MHTML / SEED bookmark geometry, including its 24×24 canvas.
      outline: '<path d="M7 3.5h10q2 0 2 2V20q0 .8-.7.4L12 16.8l-6.3 3.6q-.7.4-.7-.4V5.5q0-2 2-2Z"/>',
      fill: '<path d="M7 3.5h10q2 0 2 2V20q0 .8-.7.4L12 16.8l-6.3 3.6q-.7.4-.7-.4V5.5q0-2 2-2Z" fill="currentColor" stroke="currentColor"/>'
    },
    me: {
      outline: '<circle cx="12" cy="12" r="9.2"/><circle cx="12" cy="8.9" r="3.1"/><path d="M5.92 18.84C6.82 15.83 9.07 14.37 12 14.37S17.18 15.83 18.08 18.84"/>',
      fill: '<circle cx="12" cy="12" r="9.2"/><g fill="currentColor" stroke="none"><circle cx="12" cy="8.9" r="3.1"/><path d="M5.9 18.85C6.9 15.75 9.1 14.5 12 14.5S17.1 15.75 18.1 18.85A9.2 9.2 0 0 1 5.9 18.85Z"/></g>'
    }
  };

  function updateNav(active) {
    nav.querySelectorAll('.nav-link').forEach(link => {
      const key = link.getAttribute('href').slice(1);
      if (key === active) link.setAttribute('aria-current', 'page');
      else link.removeAttribute('aria-current');
      const icon = link.querySelector('svg');
      if (icon && navIconStates[key]) {
        icon.innerHTML = navIconStates[key][key === active ? 'fill' : 'outline'];
      }
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

  const icon = (name) => {
    const icons = {
      sun: '<circle cx="12" cy="12" r="4"/><path d="M12 2v2m0 16v2M4.9 4.9l1.4 1.4m11.4 11.4 1.4 1.4M2 12h2m16 0h2M4.9 19.1l1.4-1.4M17.7 6.3l1.4-1.4"/>',
      moon: '<path d="M20.2 16.2A8.6 8.6 0 0 1 7.8 3.8 8.7 8.7 0 1 0 20.2 16.2Z"/>'
    };
    return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${icons[name]}</svg>`;
  };

  function showSheet(type) {
    if (type === 'menu') {
      sheetTitle.textContent = '메뉴';
      sheetBody.innerHTML = `<button class="menu-row" type="button" data-theme-switch="light">${icon('sun')} 라이트 테마 <span class="menu-state">${document.documentElement.dataset.theme === 'light' ? '사용 중' : ''}</span></button>
      <button class="menu-row" type="button" data-theme-switch="dark" style="margin-top:.5rem">${icon('moon')} 다크 테마 <span class="menu-state">${document.documentElement.dataset.theme === 'dark' ? '사용 중' : ''}</span></button>`;
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
