/* This module visualizes existing SEED definitions; it never owns canonical component appearance. */
const $=(s,p=document)=>p.querySelector(s);
const $$=(s,p=document)=>Array.from(p.querySelectorAll(s));
$$("[data-seed-panel]").forEach(button=>button.addEventListener("click",()=>{
 const key=button.dataset.seedPanel;
 const tab=$("#tab-"+key);if(!tab)return;
 tab.click();tab.scrollIntoView({block:"nearest",inline:"nearest",behavior:"smooth"});
 const url=new URL(window.location.href);url.searchParams.set("panel",key);
 history.replaceState(null,"",url.pathname+url.search+url.hash);
}));
const shell=$("#page-shell");
if(shell){
 const head=shell.querySelector(".seed-page-shell__header .seed-page-shell__inner");
 const content=shell.querySelector(".seed-page-shell__content");
 const bottom=$("#page-shell-bottom");
 const choices=$$("button[data-shell-choice]");
 const icon=name=>'<span class="seed-icon" aria-hidden="true" style="--seed-icon:url(../../assets/icons/soft/v0.14/'+name+'.svg)"></span>';
 // All generated markup uses fixed repository-owned UI labels and approved SVG filenames.
 head.className="seed-page-shell__inner seed-viz-header";
 head.dataset.navRole="root";
 head.innerHTML='<div class="seed-viz-leading"><button class="seed-icon-action" type="button" id="shell-back" aria-label="뒤로 시연">'+icon("left")+'</button><button class="seed-icon-action" type="button" id="shell-close" aria-label="닫기 시연">'+icon("close")+'</button></div><h3 class="seed-text" data-text-role="title" data-text-style="root" id="page-shell-title">홈</h3><div class="seed-viz-trailing"><button class="seed-icon-action" type="button" id="shell-search" aria-label="검색 액션 시연">'+icon("search")+'</button><button class="seed-icon-action" type="button" id="shell-more" aria-label="메뉴 액션 시연">'+icon("more")+'</button></div>';
 const kicker=content.querySelector(".lab-overline");if(kicker)kicker.id="page-shell-header-meta";
 const last=content.querySelector(".seed-page-shell__section:last-child");last.id="shell-common-list";
 const special=document.createElement("section");special.className="seed-page-shell__section";special.id="shell-special-content";
 special.innerHTML='<div id="shell-immersive-content"><div class="seed-media" data-media-kind="item" data-media-state="loaded" data-media-fit="contain"><img class="seed-media__image" src="../../assets/art/rewards/08_premium_glowing_chest.png" alt="빛나는 보상 상자"></div><p class="seed-text" data-text-role="caption" data-text-style="support">A1 단일 오브젝트. A2 장면은 별도 규격입니다.</p></div><div id="shell-utility-content"><label class="seed-field-group"><span class="seed-field-label">목표 이름 · 예제</span><input type="text" class="seed-field" placeholder="예시 목표"></label></div><div id="shell-overlay-content"><p class="seed-text" data-text-role="body" data-text-style="support">기존 화면의 맥락 위에 시트를 표시합니다.</p><button id="shell-open-sheet" type="button" class="seed-action" data-variant="brand-solid" data-size="default"><span class="seed-action__face"><span class="seed-action__label">실제 Sheet 열기</span></span></button></div>';
 content.insertBefore(special,last);
 bottom.innerHTML='<nav class="seed-viz-bottom-nav" id="shell-nav" aria-label="C-BOTTOM 시각 예시 · IA 미승인">'+
 ['home:홈','chat:DM','users:클랜','user:마이'].map(x=>{const [key,label]=x.split(":");return '<button type="button" data-shell-destination="'+key+'">'+icon(key)+(label?'<span>'+label+'</span>':"")+'</button>'}).join("")+
 '</nav><div id="shell-fixed-cta"><button id="shell-cta-button" type="button" class="seed-action" data-variant="brand-solid" data-size="default" data-width="fill"><span class="seed-action__face"><span class="seed-action__label">다음 단계</span></span></button></div>';
 const originalGroup=choices[0]?.parentElement;
 for(const [key,name] of [["immersive","Immersive"],["overlay","Overlay"]]){
  const b=document.createElement("button");b.type="button";b.dataset.shellChoice=key;b.setAttribute("aria-pressed","false");b.textContent=name;originalGroup.append(b);
 }
 const navButtons=$$("button[data-shell-destination]");
 const navIcons={home:"home",chat:"chat",users:"users",user:"user"};
 const modes={
  root:{title:"홈",meta:"오늘의 요약",heading:"계속 이어가는 작은 도전",desc:"좌측 Root 타이틀와 오른쪽 Outline 액션을 배치합니다.",note:"Root: D-011 승인 Header. Bottom Navigation의 실제 목적지는 O-003 미확정입니다."},
  detail:{title:"퀘스트 상세",meta:"상세 화면",heading:"한 가지 목표에 집중",desc:"Standard는 Back과 중앙 제목을 사용하는 다른 구조입니다.",note:"Detail: 내부 Back 이력과 선택적 고정 CTA는 S-PAGE가 소유합니다."},
  utility:{title:"기록 수정",meta:"입력 화면",heading:"내용 수정",desc:"Close와 Back의 의미를 구분하고 입력과 IME 관계를 확인합니다.",note:"Utility: 키보드/고정 CTA 영역을 두 번 더하지 않습니다. 실기기 검증 필요."},
  immersive:{title:"보상 확인",meta:"집중 표현",heading:"보상의 장면",desc:"아트와 결과 텍스트를 나누고 닫기를 분명히 합니다.",note:"Immersive: 새로운 게임 화면 유형이 아니라 집중 표현을 위한 Shell입니다."},
  overlay:{title:"홈",meta:"배경 맥락",heading:"기존 화면 위 레이어",desc:"시트는 원래 화면 맥락 위에 표시합니다.",note:"Overlay: 새 목적지/페이지가 아닙니다. C-SHEET/P-SHEET를 실제로 열어 검증합니다."}
 };
 const contracts=document.createElement("div");contracts.className="seed-viz-contracts";
 contracts.innerHTML='<div><strong id="shell-head-contract">Root Header</strong><span>NAV-TOP D-011 승인 구조</span></div><div><strong id="shell-bottom-contract">Bottom Nav</strong><span>C-BOTTOM O-003 목적지 미확정</span></div><div><strong id="shell-scroll-contract">주 스크롤</strong><span>S-PAGE가 Safe Area를 한 번 소유</span></div>';
 const reason=$("#page-shell-rationale");
 reason.after(contracts);
 const feedback=document.createElement("p");feedback.id="shell-nav-feedback";feedback.className="lab-rationale";feedback.setAttribute("role","status");feedback.textContent="하단 목적지 라벨·순서는 예시이며 확정 정보구조가 아닙니다.";contracts.after(feedback);
 function selectDestination(key){
  navButtons.forEach(b=>{
    const selected=b.dataset.shellDestination===key;
    if(selected)b.setAttribute("aria-current","page");else b.removeAttribute("aria-current");
    const source=navIcons[b.dataset.shellDestination]+(selected?"-fill":"");
    b.querySelector(".seed-icon").style.setProperty("--seed-icon",'url("../../assets/icons/soft/v0.14/'+source+'.svg")');
  });
  const active=navButtons.find(b=>b.dataset.shellDestination===key);
  feedback.textContent="활성 예시: "+(active?.textContent.trim()||key)+" · Fill 별도 SVG 사용. 실제 라우팅·목적지 IA는 O-003 미확정.";
 }
 function render(kind){
  const mode=modes[kind];if(!mode)return;
  const rootLike=kind==="root"||kind==="overlay";
  shell.dataset.shellKind=kind;
  head.dataset.navRole=rootLike?"root":"standard";
  for(const [id,value] of [["#page-shell-title",mode.title],["#page-shell-header-meta",mode.meta],["#page-shell-heading",mode.heading],["#page-shell-description",mode.desc],["#page-shell-rationale",mode.note]])$(id).textContent=value;
  $("#page-shell-title").dataset.textStyle=rootLike?"root":"standard";
  $("#shell-back").hidden=rootLike||kind==="utility"||kind==="immersive";
  $("#shell-close").hidden=rootLike||kind==="detail";
  $("#shell-search").hidden=!rootLike;
  $("#shell-more").hidden=kind==="immersive";
  $("#shell-nav").hidden=!rootLike;
  $("#shell-fixed-cta").hidden=kind!=="detail"&&kind!=="utility";
  $("#page-shell-bottom").hidden=kind==="immersive";
  special.hidden=kind!=="immersive"&&kind!=="utility"&&kind!=="overlay";
  $("#shell-immersive-content").hidden=kind!=="immersive";
  $("#shell-utility-content").hidden=kind!=="utility";
  $("#shell-overlay-content").hidden=kind!=="overlay";
  last.hidden=kind==="immersive"||kind==="utility";
  $("#shell-head-contract").textContent=rootLike?"Root · 좌측 제목/우측 Outline":"Standard · Back 또는 Close/중앙 제목";
  $("#shell-bottom-contract").textContent=rootLike?"Bottom Nav · IA 미확정":kind==="detail"||kind==="utility"?"선택적 고정 CTA · Shell 소유":"하단 바·CTA 생략";
  $("#shell-scroll-contract").textContent=kind==="overlay"?"배경 맥락 + Sheet":"스크롤 본문과 고정 영역을 분리";
  $$("button[data-shell-choice]").forEach(b=>b.setAttribute("aria-pressed",String(b.dataset.shellChoice===kind)));
 }
 $$("button[data-shell-choice]").forEach(b=>b.addEventListener("click",()=>render(b.dataset.shellChoice)));
 navButtons.forEach(b=>b.addEventListener("click",()=>selectDestination(b.dataset.shellDestination)));
 $("#shell-back").addEventListener("click",()=>render("root"));
 $("#shell-close").addEventListener("click",()=>render("root"));
 $("#shell-search").addEventListener("click",()=>feedback.textContent="검색 동작은 연결하지 않았습니다. 헤더 슬롯 규격만 시연합니다.");
 $("#shell-more").addEventListener("click",()=>feedback.textContent="헤더의 액션 수·종류는 각 화면에서 결정합니다.");
 $("#shell-cta-button").addEventListener("click",()=>feedback.textContent="시연 동작: 고정 CTA. 실제 데이터 변경 없음.");
 $("#shell-open-sheet").addEventListener("click",()=>$("#sheet-open").click());
 selectDestination("home");render("root");
}
