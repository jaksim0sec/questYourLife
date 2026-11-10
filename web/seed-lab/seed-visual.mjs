/* SEED Lab: S-PAGE 5 Shell roles and the 4 route/page classifications.
 * The actual visibility decisions come from the shared web contract.
 */
import {WEB_NAV_ITEMS,applyPageType,selectWebNav,getPageType} from "../preview/seed-page-contract.mjs";
const $=(s,p=document)=>p.querySelector(s);
const $$=(s,p=document)=>Array.from(p.querySelectorAll(s));
$$("[data-seed-panel]").forEach(button=>button.addEventListener("click",()=>{
  const tab=$("#tab-"+button.dataset.seedPanel);if(!tab)return;
  tab.click();tab.scrollIntoView({block:"nearest",inline:"nearest",behavior:"smooth"});
  const url=new URL(window.location.href);
  url.searchParams.set("panel",button.dataset.seedPanel);
  history.replaceState(null,"",url.pathname+url.search+url.hash);
}));
const shell=$("#page-shell");
if(shell){
  const top=shell.querySelector(".seed-page-shell__header");
  const header=top.querySelector(".seed-page-shell__inner");
  const content=shell.querySelector(".seed-page-shell__content");
  const bottom=$("#page-shell-bottom");
  const icon=name=>'<span class="seed-icon" aria-hidden="true" style=\'--seed-icon:url("../../assets/icons/soft/v0.14/'+name+'.svg")\'></span>';
  const navIcon=item=>'<span class="seed-icon" aria-hidden="true" style=\'--seed-icon:url("'+(item.outlineBase||"../../assets/icons/soft/v0.14/")+item.outline+'.svg")\'></span>';
  top.classList.add("seed-top-nav");
  header.className="seed-page-shell__inner seed-top-nav__inner";
  header.dataset.navRole="root";
  header.innerHTML=
    '<div class="seed-top-nav__leading">'+
      '<button type="button" class="seed-icon-action" id="shell-back" aria-label="이전 화면">'+icon("left")+'</button>'+
      '<button type="button" class="seed-icon-action" id="shell-close" aria-label="닫기">'+icon("close")+'</button></div>'+
    '<h3 class="seed-top-nav__title" id="page-shell-title">홈</h3>'+
    '<div class="seed-top-nav__trailing">'+
      '<button type="button" class="seed-icon-action" id="shell-search" aria-label="검색 시연">'+icon("search")+'</button>'+
      '<button type="button" class="seed-icon-action" id="shell-bell" aria-label="알림 시연">'+icon("bell")+'</button>'+
      '<button type="button" class="seed-icon-action" id="shell-more" aria-label="메뉴 시연">'+icon("menu")+'</button>'+
    '</div>';
  const kicker=content.querySelector(".lab-overline");if(kicker)kicker.id="page-shell-header-meta";
  const common=content.querySelector(".seed-page-shell__section:last-child");
  common.id="shell-common-list";
  const extra=document.createElement("section");
  extra.className="seed-page-shell__section";extra.id="shell-special-content";
  extra.innerHTML=
    '<div id="shell-immersive-content"><div class="seed-media" data-media-kind="item" data-media-state="loaded" data-media-fit="contain">'+
    '<img class="seed-media__image" src="../../assets/art/rewards/08_premium_glowing_chest.png" alt="보상 상자"></div>'+
    '<p class="seed-text" data-text-role="caption" data-text-style="support">A1 오브젝트 · A2 장면과 구별</p></div>'+
    '<div id="shell-utility-content"><label class="seed-field-group"><span class="seed-field-label">목표 이름</span>'+
    '<input type="text" class="seed-field" placeholder="예시 입력"></label></div>'+
    '<div id="shell-overlay-content"><p class="seed-text" data-text-role="body" data-text-style="support">'+
    '현재 화면은 그대로 두고 시트만 표시합니다.</p>'+
    '<button id="shell-open-sheet" type="button" class="seed-action" data-variant="brand-solid" data-size="default">'+
    '<span class="seed-action__face"><span class="seed-action__label">Bottom Sheet 열기</span></span></button></div>';
  content.insertBefore(extra,common);
  bottom.dataset.bottomRole="navigation";
  const links=WEB_NAV_ITEMS.map(item=>
    '<button class="seed-bottom-nav__link" type="button" data-nav-key="'+item.key+'">'+
    navIcon(item)+'<span>'+item.label+'</span></button>').join("");
  bottom.innerHTML='<nav class="seed-bottom-nav" id="shell-nav" aria-label="웹 기준 전역 탐색">'+links+'</nav>'+
    '<div id="shell-fixed-cta" data-fixed-cta hidden>'+
    '<button id="shell-cta-button" class="seed-action" data-variant="brand-solid" data-size="default" data-width="fill" type="button">'+
    '<span class="seed-action__face"><span class="seed-action__label">다음 단계</span></span></button></div>';
  const oldButtons=$$("button[data-shell-choice]");
  const shellGroup=oldButtons[0]?.parentElement;
  for(const [key,label] of [["immersive","Immersive"],["overlay","Overlay"]]){
    const b=document.createElement("button");b.type="button";b.dataset.shellChoice=key;
    b.setAttribute("aria-pressed","false");b.textContent=label;shellGroup.append(b);
  }
  const shellButtons=$$("button[data-shell-choice]");
  const typeButtons=$("button[data-page-type-choice]");
  const weightButtons=$("button[data-header-weight]");
  const nav=$("#shell-nav");
  const reason=$("#page-shell-rationale");
  const contracts=document.createElement("div");contracts.className="seed-viz-contracts";
  contracts.innerHTML=
    '<div><strong id="shell-head-contract">Root Header</strong><span>D-011 기준</span></div>'+
    '<div><strong id="shell-bottom-contract">Global Navigation</strong><span>가시성: S-PAGE</span></div>'+
    '<div><strong id="shell-scroll-contract">스크롤 본문</strong><span>OS Safe Inset 한 번</span></div>';
  reason.after(contracts);
  const feedback=document.createElement("p");feedback.id="shell-nav-feedback";
  feedback.className="lab-rationale";feedback.setAttribute("role","status");
  contracts.after(feedback);
  let activeNav="home",currentRoute="global",currentShell="root";
  const modes={
    root:{title:"홈",meta:"전역 목적지",heading:"오늘의 활동",desc:"Root 헤더와 전역 네비게이션을 같은 Shell 안에서 유지합니다.",note:"Root: 좌측 페이지명 + 우측 Outline. 콘텐츠 배치는 화면 목적이 결정."},
    detail:{title:"퀘스트 상세",meta:"상세",heading:"한 가지 목표에 집중",desc:"Standard 헤더와 기존 선택 탭 유지. Global Inner는 하단바가 사라지지 않습니다.",note:"Detail: Back은 실제 이전 맥락으로. 이 실험에선 부모 탭 상태를 유지합니다."},
    utility:{title:"목표 수정",meta:"입력",heading:"내용 수정",desc:"Non-Global에서는 전역 탭을 숨기되 필요한 고정 행동은 표시합니다.",note:"Utility: 키보드와 고정 CTA의 inset을 Shell 한 곳에서 관리합니다."},
    immersive:{title:"보상 확인",meta:"집중",heading:"보상 콘텐츠",desc:"일반 내비게이션을 숨기고 닫기 행동을 제공합니다.",note:"Immersive: 보상 오브젝트를 별도 장면으로 둔갑시키지 않습니다."},
    overlay:{title:"홈",meta:"Overlay",heading:"화면 문맥 유지",desc:"Bottom Sheet는 기존 Global/Inner/Non-Global을 교체하지 않습니다.",note:"Overlay: 모달의 원래 페이지와 전역 탭 선택을 보존합니다."}
  };
  const defaultPageType={root:"global",detail:"global-inner",utility:"non-global",immersive:"non-global",overlay:"global"};
  function setPressed(items,key,field){
    for(const item of items)item.setAttribute("aria-pressed",String(item.dataset[field]===key));
  }
  function changeType(type){
    if(getPageType(type).overlay){
      setPressed(typeButtons,"bottom-sheet","pageTypeChoice");
      $("#page-type-status").textContent="Bottom Sheet: 부모 "+currentRoute+" 화면과 선택 탭 유지. Sheet 내부에는 전역 네비게이션이 없습니다.";
      $("#sheet-open").click();
      return;
    }
    const shellKind=type==="global"?"root":type==="global-inner"?"detail":"utility";
    render(shellKind,type);
  }
  function render(kind,explicitType){
    if(!Object.hasOwn(modes,kind))return;
    if(kind==="overlay"){currentShell=kind;changeType("bottom-sheet");setPressed(shellButtons,kind,"shellChoice");return;}
    currentShell=kind;
    currentRoute=explicitType||defaultPageType[kind];
    shell.dataset.shellKind=kind;
    const info=modes[kind];
    const title=kind==="root"?WEB_NAV_ITEMS.find(item=>item.key===activeNav).label:info.title;
    for(const [selector,value] of [
      ["#page-shell-title",title],["#page-shell-header-meta",info.meta],
      ["#page-shell-heading",info.heading],["#page-shell-description",info.desc],
      ["#page-shell-rationale",info.note]
    ]){const node=$(selector);if(node)node.textContent=value;}
    const rootRole=currentRoute==="global";
    $("#shell-back").hidden=rootRole||kind==="utility"||kind==="immersive";
    $("#shell-close").hidden=rootRole||kind==="detail";
    $("#shell-search").hidden=!rootRole;
    $("#shell-bell").hidden=!rootRole;
    $("#shell-more").hidden=kind==="immersive";
    const fixedCta=kind==="utility";
    $("#shell-fixed-cta").hidden=!fixedCta;
    const state=applyPageType(shell,currentRoute,{fixedCta});
    header.dataset.navRole=state.header;
    extra.hidden=kind!=="utility"&&kind!=="immersive";
    $("#shell-immersive-content").hidden=kind!=="immersive";
    $("#shell-utility-content").hidden=kind!=="utility";
    $("#shell-overlay-content").hidden=true;
    common.hidden=kind==="utility"||kind==="immersive";
    $("#shell-head-contract").textContent=state.header==="root"?"Root · 왼쪽 큰 제목 + 오른쪽 Outline":"Standard · Back/Close + 중앙 제목";
    $("#shell-bottom-contract").textContent=state.bottomNav?"전역 5탭 표시 · 활성 유지":fixedCta?"전역 5탭 숨김 · CTA 표시":"전역 5탭 숨김";
    $("#shell-scroll-contract").textContent="주 스크롤 + 하단 영역 Safe Inset은 Shell 단일 소유";
    $("#page-type-status").textContent={
      "global":"Global: 상단 Root 표시 / 전역 5탭 표시",
      "global-inner":"Global Inner: 상단 Standard 표시 / 전역 5탭 유지 / 부모 목적지 "+activeNav,
      "non-global":"Non-Global: 상단 Standard 표시 / 전역 5탭 숨김"
    }[currentRoute];
    setPressed(shellButtons,kind,"shellChoice");
    setPressed(typeButtons,currentRoute,"pageTypeChoice");
  }
  function setDestination(key){
    const item=selectWebNav(nav,key);
    activeNav=item.key;
    feedback.textContent="웹 전역 탐색 기준: "+item.label+" 선택. 실제 앱 라우트/딥링크 구성은 O-003 별도.";
    if(currentRoute==="global")render("root","global");
  }
  function setHeaderWeight(weight){
    if(weight!=="current"&&weight!=="light")return;
    shell.dataset.headerWeight=weight;
    setPressed(weightButtons,weight,"headerWeight");
  }
  for(const b of weightButtons)b.addEventListener("click",()=>setHeaderWeight(b.dataset.headerWeight));
  setHeaderWeight("current");
  for(const b of shellButtons)b.addEventListener("click",()=>render(b.dataset.shellChoice));
  for(const b of typeButtons)b.addEventListener("click",()=>changeType(b.dataset.pageTypeChoice));
  for(const b of nav.querySelectorAll(".seed-bottom-nav__link"))b.addEventListener("click",()=>setDestination(b.dataset.navKey));
  $("#lab-sheet").addEventListener("close",()=>{
    setPressed(typeButtons,currentRoute,"pageTypeChoice");
    setPressed(shellButtons,currentShell==="overlay"?"root":currentShell,"shellChoice");
    $("#page-type-status").textContent="시트 닫힘: 부모 "+currentRoute+" 화면, 전역 선택 "+activeNav+" 유지.";
    if(currentShell==="overlay")currentShell="root";
  });
  $("#shell-back").addEventListener("click",()=>render("root","global"));
  $("#shell-close").addEventListener("click",()=>render("root","global"));
  $("#shell-open-sheet").addEventListener("click",()=>changeType("bottom-sheet"));
  $("#shell-cta-button").addEventListener("click",()=>feedback.textContent="고정 CTA 구조만 확인. 실제 데이터 변경 없음.");
  for(const id of ["shell-search","shell-bell","shell-more"])$( "#"+id ).addEventListener("click",()=>{
    feedback.textContent="상단 Outline 아이콘은 역할 시연입니다. 서비스 라우터는 미연결.";
  });
  setDestination("home");
  render("root","global");
}
