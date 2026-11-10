import {normalizeProgress} from "./progress.mjs";
const $=(selector,parent=document)=>parent.querySelector(selector);
const $$=(selector,parent=document)=>Array.from(parent.querySelectorAll(selector));
const report=message=>{$("#family-feedback").textContent=message;};
const theme=$("#family-theme");
theme.addEventListener("click",()=>{
  const dark=document.documentElement.dataset.theme!=="dark";
  document.documentElement.dataset.theme=dark?"dark":"light";
  theme.querySelector(".seed-action__label").textContent=dark?"라이트 모드":"다크 모드";
});
const sizeButtons=$$("[data-family-size]"),widthButtons=$$("[data-family-width]");
const actionSamples=$$("#family-actions-approved .seed-action");
function setChoice(buttons,attribute,value){
  for(const button of buttons)button.setAttribute("aria-pressed",String(button.dataset[attribute]===value));
}
sizeButtons.forEach(button=>button.addEventListener("click",()=>{
  const size=button.dataset.familySize;
  actionSamples.forEach(action=>action.dataset.size=size);
  setChoice(sizeButtons,"familySize",size);
  report("승인 Action 6종의 크기: "+size+" · 색/의미/역할 유지");
}));
widthButtons.forEach(button=>button.addEventListener("click",()=>{
  const width=button.dataset.familyWidth;
  actionSamples.forEach(action=>action.dataset.width=width);
  setChoice(widthButtons,"familyWidth",width);
  report("승인 Action 6종의 너비: "+width+" · 크기/톤 유지");
}));
$$(".seed-action").forEach(button=>button.addEventListener("click",()=>{
  if(button.matches("a")||button.disabled||button.getAttribute("aria-busy")==="true")return;
  if(button===theme||button.closest(".family-switches"))return;
  report("시연 동작 · "+(button.querySelector(".seed-action__label")?.textContent||"액션")+" · 실제 데이터 변경 없음");
}));
const progressValues={zero:0,"0":0,"620":620,"1000":1000,"1200":1200,loading:0,unavailable:null};
const progressButtons=$$('input[name="family-progress"]');
function renderProgress(key){
  const p=normalizeProgress(progressValues[key],1000,{loading:key==="loading"});
  const noValue=p.state==="unavailable",isLoading=p.state==="loading";
  for(const track of $$("#family-progress-list .progress-track")){
    track.hidden=noValue;
    track.dataset.progressState=isLoading?"loading":"ready";
    track.style.setProperty("--progress",p.percent===null?"0%":p.percent+"%");
    track.setAttribute("aria-busy",String(isLoading));
    if(isLoading||noValue){track.removeAttribute("aria-valuenow");track.removeAttribute("aria-valuetext");}
    else{
      track.setAttribute("aria-valuenow",String(Math.min(p.value,p.max)));
      track.setAttribute("aria-valuemax",String(p.max));
      track.setAttribute("aria-valuetext",p.value.toLocaleString("ko-KR")+" / "+p.max.toLocaleString("ko-KR")+(p.state==="over"?" · 초과":""));
    }
  }
  $("#family-progress-label").textContent=isLoading?"진행 중":noValue?"조회 불가":p.value.toLocaleString("ko-KR")+" / 1,000";
  $("#family-progress-note").textContent=noValue?"유효한 0과 조회 불가는 다릅니다. 막대를 숨깁니다.":isLoading?"값을 모를 때 확정 퍼센트를 표시하지 않습니다.":p.state==="over"?"실제 초과 기여는 보존하고 막대만 100%로 제한합니다.":"현재 표시 폭 "+p.percent.toFixed(1).replace(/\.0$/,"")+"%.";
  progressButtons.forEach(button=>button.checked=button.dataset.value===key);
}
progressButtons.forEach(button=>button.addEventListener("change",()=>renderProgress(button.dataset.value)));
const views=$$("[data-view]");
views.forEach(button=>button.addEventListener("click",()=>{setChoice(views,"view",button.dataset.view);showSelection();}));
function showSelection(){
  const view=views.find(button=>button.getAttribute("aria-pressed")==="true")?.textContent||"오늘";
  const radio=$('input[name="family-radio"]:checked')?.value==="b"?"상세":"기본";
  const on=$("#family-check").checked?"알림 켜짐":"알림 꺼짐";
  $("#family-selection-feedback").textContent=view+" · "+radio+" 정보 · "+on;
}
$("#family-check").addEventListener("change",showSelection);
$$('input[name="family-radio"]').forEach(input=>input.addEventListener("change",showSelection));
$("#family-field").addEventListener("input",event=>report("입력 중 · "+event.target.value.length+"글자. 저장·계정 변경 없음"));
$$("[data-icon-action]").forEach(button=>button.addEventListener("click",()=>report(button.dataset.iconAction+" 시연 · Glyph 24 / Target 48")));
$('[data-surface="interactive"]').addEventListener("click",()=>report("Interactive Surface 클릭됨 · 실제 페이지 이동 없음"));
renderProgress("620");
