import {normalizeProgress} from "./progress.mjs";
const $=(s,parent=document)=>parent.querySelector(s);
const $$=(s,parent=document)=>Array.from(parent.querySelectorAll(s));
const fmt=n=>new Intl.NumberFormat("ko-KR").format(n);
const ICON_ROOT="../../assets/icons/soft/v0.14/";
const root=document.documentElement;
function updatePressed(buttons,selected,key){buttons.forEach(b=>b.setAttribute("aria-pressed",String(b.dataset[key]===selected)));}
function selectPanel(name,focus=false){
  if(!$$("[data-panel-target]").some(t=>t.dataset.panelTarget===name))return;
  $$("[data-panel-target]").forEach(t=>{
    const active=t.dataset.panelTarget===name;
    t.setAttribute("aria-selected",String(active));
    t.tabIndex=active?0:-1;
    if(active&&focus)t.focus();
  });
  $$("[data-panel]").forEach(p=>p.hidden=p.dataset.panel!==name);
}
const tabs=$$("[data-panel-target]");
tabs.forEach((tab,index)=>{
  tab.addEventListener("click",()=>selectPanel(tab.dataset.panelTarget));
  tab.addEventListener("keydown",event=>{
    let next=index;
    if(event.key==="ArrowRight")next=(index+1)%tabs.length;
    else if(event.key==="ArrowLeft")next=(index+tabs.length-1)%tabs.length;
    else if(event.key==="Home")next=0;
    else if(event.key==="End")next=tabs.length-1;
    else return;
    event.preventDefault();selectPanel(tabs[next].dataset.panelTarget,true);
  });
});
const requestedPanel=new URLSearchParams(window.location.search).get("panel");
selectPanel(["map","foundation","components","layout","shells","state","assets"].includes(requestedPanel)?requestedPanel:"map");
$("#theme-toggle").addEventListener("click",()=>{
  const dark=root.dataset.theme!=="dark";
  root.dataset.theme=dark?"dark":"light";
  const button=$("#theme-toggle");
  button.setAttribute("aria-label",dark?"라이트 모드 켜기":"다크 모드 켜기");
  $(".seed-icon",button).style.setProperty("--seed-icon",'url("../../assets/icons/preview/'+(dark?"sun":"moon")+'.svg")');
});
const descriptions={
  grid:"격자: 여러 이미지를 동시에 비교할 때. 동일한 이미지 프레임과 정보 슬롯을 사용합니다.",
  rail:"가로 진열: 하나씩 순차 탐색할 때. 내용은 유지하고 스크롤 관계만 바꿉니다.",
  rows:"세로 목록: 텍스트와 상태를 빠르게 읽을 때. 이미지가 보조 정보가 됩니다."
};
const layoutButtons=$$("[data-layout-choice]");
layoutButtons.forEach(button=>button.addEventListener("click",()=>{
  const value=button.dataset.layoutChoice;
  $("#layout-specimens").dataset.layout=value;
  updatePressed(layoutButtons,value,"layoutChoice");
  $("#layout-rationale").textContent=descriptions[value];
}));

const progressExamples={
  "0":{value:0,max:1000},"620":{value:620,max:1000},
  "1000":{value:1000,max:1000},"1200":{value:1200,max:1000},
  unavailable:{value:null,max:1000},loading:{value:0,max:1000,loading:true}
};
const progressButtons=$$("[data-progress-choice]");
function renderProgress(key){
  const input=progressExamples[key];
  const p=normalizeProgress(input.value,input.max,{loading:!!input.loading});
  const track=$("#progress-track"),label=$("#progress-value"),note=$("#progress-note");
  track.hidden=p.state==="unavailable";
  track.dataset.progressState=p.state==="loading"?"loading":"ready";
  track.style.setProperty("--progress",p.percent===null?"0%":p.percent+"%");
  track.setAttribute("aria-busy",String(p.state==="loading"));
  if(p.state==="loading"){
    track.removeAttribute("aria-valuenow");
    track.removeAttribute("aria-valuetext");
    label.textContent="불러오는 중";
    note.textContent="값을 모르는 동안 확정 퍼센트를 표시하지 않습니다.";
  }else if(p.state==="unavailable"){
    track.removeAttribute("aria-valuenow");
    track.removeAttribute("aria-valuetext");
    label.textContent="조회 불가";
    note.textContent="유효한 0과 조회 불가는 다릅니다. 막대를 숨깁니다.";
  }else{
    track.setAttribute("aria-valuenow",String(Math.min(p.value,p.max)));
    track.setAttribute("aria-valuemax",String(p.max));
    track.setAttribute("aria-valuetext",fmt(p.value)+" / "+fmt(p.max)+(p.state==="over"?" · 초과":""));
    label.textContent=fmt(p.value)+" / "+fmt(p.max);
    note.textContent=p.state==="over"?
      "원본 초과 기여는 보존하고 막대만 100%로 제한합니다. 지급 완료 의미는 아닙니다.":
      "원본 수치 "+fmt(p.value)+", 표시 폭 "+p.percent.toFixed(1).replace(/\.0$/,"")+"%.";
  }
  updatePressed(progressButtons,key,"progressChoice");
}
progressButtons.forEach(button=>button.addEventListener("click",()=>renderProgress(button.dataset.progressChoice)));
const thicknessButtons=$$("button[data-thickness]");
thicknessButtons.forEach(button=>button.addEventListener("click",()=>{
  $("#progress-track").dataset.thickness=button.dataset.thickness;
  updatePressed(thicknessButtons,button.dataset.thickness,"thickness");
}));
const pageModes={
  root:{title:"홈",meta:"액션 슬롯",heading:"계속 이어가는 작은 도전",description:"정보를 목적에 따라 섹션으로 나누고, 전역 탐색 공간을 예약합니다.",bottom:"하단 Navigation 슬롯 · 실제 경로 없음",rationale:"Root: 주 스크롤과 전역 탐색을 분리합니다. 미리보기는 탐색을 실행하지 않습니다."},
  detail:{title:"퀘스트 상세",meta:"뒤로 슬롯",heading:"한 가지 목표에 집중",description:"표준 헤더와 상세 본문을 분리하고, 필요할 때만 고정 CTA 영역을 확보합니다.",bottom:"고정 CTA 슬롯 · 필요할 때만 표시",rationale:"Detail: 본문과 선택적인 고정 CTA를 분리합니다. 전역 Navigation은 실제 플로우에서 결정합니다."},
  utility:{title:"기록 수정",meta:"닫기 슬롯",heading:"입력에 방해받지 않는 화면",description:"폼·키보드·확정 동작의 경계를 Shell에서 관리합니다.",bottom:"폼 확인 영역 · IME/실기기 별도 검증",rationale:"Utility: 키보드와 안전 영역의 소유권을 Shell 한 곳에 둡니다. 이 화면은 정적인 구조 시연입니다."}
};
const shellButtons=$$("button[data-shell-choice]");
function renderPageShell(kind){
  const mode=pageModes[kind];if(!mode)return;
  $("#page-shell").dataset.shellKind=kind;
  for(const [id,key] of [["#page-shell-title","title"],["#page-shell-header-meta","meta"],["#page-shell-heading","heading"],["#page-shell-description","description"],["#page-shell-rationale","rationale"]])$(id).textContent=mode[key];
  updatePressed(shellButtons,kind,"shellChoice");
}
shellButtons.forEach(button=>button.addEventListener("click",()=>renderPageShell(button.dataset.shellChoice)));
renderPageShell("root");

renderProgress("620");

let clickCount=0;
$("#action-demo").addEventListener("click",()=>{
  clickCount++;
  $("#action-feedback").textContent="버튼 클릭 "+clickCount+"회. 서버 요청이나 보상 지급 없음.";
});
$("#action-reset").addEventListener("click",()=>{
  clickCount=0;
  $("#action-feedback").textContent="초기화됨. 서비스 데이터에는 변경이 없습니다.";
});
function showChoices(){
  const check=$("#choice-check").checked?"체크됨":"체크 해제";
  const radio=$('input[name="choice-radio"]:checked').value==="a"?"기본 보기":"상세 보기";
  const input=$("#choice-field").value.trim();
  $("#choice-feedback").textContent=check+" · "+radio+(input?" · "+input:"");
}
$("#choice-check").addEventListener("change",showChoices);
$$('input[name="choice-radio"]').forEach(r=>r.addEventListener("change",showChoices));
$("#choice-field").addEventListener("input",showChoices);

const rewards=[
  ["01_school_locker.png","학교 사물함"],["02_capsule_gacha.png","캡슐 뽑기 기계"],
  ["03_delivery_parcel.png","택배 상자"],["04_mint_tin.png","민트 틴케이스"],
  ["05_ribbon_gift.png","리본 선물상자"],["06_wooden_chest.png","나무 보물상자"],
  ["07_flower_chest.png","꽃 장식 보물상자"],
  ["08_premium_glowing_chest.png","빛나는 프리미엄 상자"]
];
for(const [file,label] of rewards){
  const item=document.createElement("article"),frame=document.createElement("div");
  item.className="lab-reward-item";frame.className="seed-media";
  frame.dataset.mediaKind="item";frame.dataset.mediaFit="contain";frame.dataset.mediaState="loaded";
  const img=document.createElement("img"),heading=document.createElement("strong");
  img.className="seed-media__image";img.src="../../assets/art/rewards/"+file;img.alt=label;heading.textContent=label;
  frame.append(img);item.append(frame,heading);$("#reward-gallery").append(item);
}
document.addEventListener("error",event=>{
  if(!(event.target instanceof HTMLImageElement)||!event.target.classList.contains("seed-media__image"))return;
  const frame=event.target.closest(".seed-media");
  if(!frame)return;
  frame.dataset.mediaState="error";
  const fallback=document.createElement("span");
  fallback.className="seed-media__fallback";fallback.textContent="이미지 로드 실패";
  frame.append(fallback);
},true);

for(const name of ["plain","solid","outlined","subtle","inset","raised","interactive","media","reward","overlay"]){
  const element=document.createElement(name==="interactive"?"button":"div");
  element.className="seed-surface lab-surface-fixture";element.dataset.surface=name;
  if(name==="interactive"){element.type="button";element.addEventListener("click",()=>element.querySelector("p").textContent="클릭됨 · 화면 이동 없음");}
  const title=document.createElement("strong"),detail=document.createElement("p");
  title.textContent=name.charAt(0).toUpperCase()+name.slice(1);
  detail.textContent=name==="interactive"?"선택할 수 있는 면":name==="plain"?"배경 없는 섹션":"Surface 역할 검토";
  element.append(title,detail);$("#surface-gallery").append(element);
}

const iconSearch=$("#icon-search"),iconCount=$("#icon-count"),iconGallery=$("#icon-gallery");
const iconFilterButtons=$$("[data-icon-filter]");
let iconItems=[],iconFilter="all";
function renderIcons(){
  const term=iconSearch.value.toLowerCase().trim();
  const filtered=iconItems.filter(x=>(iconFilter==="all"||x.group===iconFilter)&&x.name.includes(term));
  iconGallery.replaceChildren();
  for(const item of filtered){
    const tile=document.createElement("div"),glyph=document.createElement("span"),label=document.createElement("span");
    tile.className="lab-icon-tile";glyph.className="seed-icon";glyph.setAttribute("aria-hidden","true");
    glyph.style.setProperty("--seed-icon",'url("'+ICON_ROOT+item.name+'.svg")');
    label.textContent=item.name;tile.append(glyph,label);iconGallery.append(tile);
  }
  iconCount.textContent=filtered.length+" / "+iconItems.length+"종";
}
iconSearch.addEventListener("input",renderIcons);
iconFilterButtons.forEach(button=>button.addEventListener("click",()=>{
  iconFilter=button.dataset.iconFilter;
  updatePressed(iconFilterButtons,iconFilter,"iconFilter");
  renderIcons();
}));
async function loadIcons(){
  try{
    const res=await fetch(ICON_ROOT+"manifest.json");
    if(!res.ok)throw new Error("HTTP "+res.status);
    const m=await res.json();
    if(m.total!==56||m.base.length!==32||m.fill.length!==5||m.additional.length!==19)throw new Error("manifest 수량 불일치");
    iconItems=m.base.concat(m.additional).map(name=>({name,group:"outline"})).concat(m.fill.map(name=>({name,group:"fill"})));
    if(!iconItems.every(x=>/^[a-z][a-z0-9-]*$/.test(x.name)))throw new Error("잘못된 아이콘 이름");
    renderIcons();
  }catch(error){
    iconCount.textContent="불러오기 실패";
    iconGallery.textContent="SVG manifest를 읽을 수 없습니다. 저장소 루트를 정적 서버로 열었는지 확인하세요. "+error.message;
  }
}
loadIcons();

const sheet=$("#lab-sheet"),handle=$(".seed-sheet__handle",sheet),sheetBody=$(".seed-sheet__body",sheet);
let snap="min",lastFocus=null,dragging=null,footerLayout="paired";
const footer=$("#sheet-footer"),footerButtons=$$("button[data-footer-choice]");
const footerDescriptions={
  none:"없음: 입력·정보 열람 시. 상단 닫기와 핸들만 사용합니다.",
  single:"단일: 다음 단계 하나만 표시. 군더더기 없는 단일 CTA입니다.",
  paired:"이중: 보조 행동과 다음 단계가 같은 높이로 정렬됩니다.",
  stacked:"세로: 긴 문구나 좁은 화면에서 행동을 위아래로 분리합니다."
};
function applyFooterLayout(layout){
  if(!Object.hasOwn(footerDescriptions,layout))return;
  footerLayout=layout;footer.dataset.layout=layout;
  footer.hidden=layout==="none";
  $("#sheet-done").hidden=layout==="single";
  $("#footer-explanation").textContent=footerDescriptions[layout];
  updatePressed(footerButtons,layout,"footerChoice");
  if(sheet.open)setSnap(snap);
}
footerButtons.forEach(button=>button.addEventListener("click",()=>applyFooterLayout(button.dataset.footerChoice)));
applyFooterLayout("paired");
function getHeights(){
  const vh=window.visualViewport?.height||window.innerHeight;
  const chrome=handle.offsetHeight+$(".seed-sheet__head",sheet).offsetHeight+footer.offsetHeight;
  const content=sheetBody.scrollHeight+chrome;
  const min=Math.min(vh,Math.max(185,chrome+64,Math.min(vh*.4,content,380)));
  const mid=Math.max(min,Math.round(vh*.65));
  return {min,mid,full:Math.max(mid,Math.round(vh))};
}
function setHeight(value){sheet.style.setProperty("--sheet-height",Math.round(value)+"px");}
function setSnap(which){
  const entries=Object.entries(getHeights()).filter((pair,i,all)=>i===0||pair[1]>all[i-1][1]+1);
  const choice=entries.find(pair=>pair[0]===which)||entries[entries.length-1];
  snap=choice[0];setHeight(choice[1]);sheet.dataset.snap=snap;
  $("#sheet-state").textContent=({min:"최소",mid:"중간",full:"전체"})[snap]+" 높이 · Footer 분리";
  $("#sheet-next-label").textContent=snap==="full"?"최소 높이로":"다음 단계";
}
function closeSheet(){if(sheet.open)sheet.close();}
for(let i=1;i<=16;i++){
  const row=document.createElement("div"),left=document.createElement("span"),right=document.createElement("span");
  row.className="lab-sheet-line";left.textContent="반복 항목 "+i;right.textContent="스크롤 테스트";
  row.append(left,right);$("#sheet-lines").append(row);
}
$("#sheet-open").addEventListener("click",()=>{lastFocus=document.activeElement;sheet.showModal();applyFooterLayout(footerLayout);setSnap("min");handle.focus();});
$("#sheet-close").addEventListener("click",closeSheet);
$("#sheet-done").addEventListener("click",closeSheet);
$("#sheet-next").addEventListener("click",()=>setSnap(snap==="min"?"mid":snap==="mid"?"full":"min"));
sheet.addEventListener("close",()=>{if(lastFocus&&lastFocus.isConnected)lastFocus.focus();});
sheet.addEventListener("click",event=>{
  if(event.target!==sheet)return;
  const rect=sheet.getBoundingClientRect();
  if(event.clientY<rect.top||event.clientX<rect.left||event.clientX>rect.right)closeSheet();
});
sheet.addEventListener("keydown",event=>{
  if(!sheet.open||!["ArrowUp","ArrowDown"].includes(event.key)||event.target===sheetBody)return;
  event.preventDefault();
  if(event.key==="ArrowUp")setSnap(snap==="min"?"mid":"full");
  else setSnap(snap==="full"?"mid":"min");
});
window.addEventListener("resize",()=>{if(sheet.open)setSnap(snap);});
handle.addEventListener("pointerdown",event=>{
  if(event.button!==0)return;
  dragging={id:event.pointerId,startY:event.clientY,startHeight:sheet.getBoundingClientRect().height,stable:snap};
  handle.setPointerCapture(event.pointerId);
});
handle.addEventListener("pointermove",event=>{
  if(!dragging||dragging.id!==event.pointerId)return;
  const heights=getHeights();
  setHeight(Math.min(heights.full,Math.max(heights.min,dragging.startHeight+dragging.startY-event.clientY)));
});
handle.addEventListener("pointerup",event=>{
  if(!dragging||dragging.id!==event.pointerId)return;
  const height=sheet.getBoundingClientRect().height;
  const entries=Object.entries(getHeights()).sort((a,b)=>Math.abs(a[1]-height)-Math.abs(b[1]-height));
  dragging=null;setSnap(entries[0][0]);
});
handle.addEventListener("pointercancel",()=>{if(dragging){const previous=dragging.stable;dragging=null;setSnap(previous);}});
