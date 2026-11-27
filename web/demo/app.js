"use strict";
// S-PAGE K10: visibility is owned here, not in page content.
const PAGE_TYPES=Object.freeze({global:{header:"root",nav:true},"global-inner":{header:"standard",nav:true},"non-global":{header:"standard",nav:false}});
const destinations=[["home","홈","home","home-fill"],["quests","퀘스트","check-circle","check-circle-fill"],["explore","탐색","explore","explore-fill"],["clan","클랜","bookmark","clan-bookmark-fill"],["me","마이","me-smile","me-smile-fill"]];
const previewIcons=new Set(["clan-bookmark-fill","me-smile","me-smile-fill","bell-connected","bell-connected-root","menu-root","explore-fill"]);
const iconPath=n=>"../../assets/icons/"+(previewIcons.has(n)?"preview/":"soft/v0.14/")+n+".svg";
const icon=n=>'<span class="icon" aria-hidden="true" style="--icon:url(&quot;'+iconPath(n)+'&quot;)"></span>';
const ib=(name,label,action,round=false)=>'<button class="icon-button'+(round?' round':'')+'" aria-label="'+label+'" data-action="'+action+'">'+icon(name)+'</button>';
const row=(title,desc,glyph,action)=>'<button class="action-row" data-action="'+action+'">'+icon(glyph)+'<span>'+title+(desc?'<small>'+desc+'</small>':'')+'</span><span class="icon trailing" aria-hidden="true" style="--icon:url(&quot;'+iconPath("chevron")+'&quot;)"></span></button>';
const cta=(label,tone,action)=>'<button class="cta '+tone+'" data-action="'+action+'">'+label+'</button>';
const $=id=>document.getElementById(id);
const roots=Object.fromEntries(destinations.map(([key,label])=>[key,{type:"global",owner:key,title:label,body:()=>'<div><h2 class="display">페이지의 시작</h2><p class="support">공통 페이지 골격을 확인하는 임시 콘텐츠예요.</p></div><section><h2 class="section-title">탐색</h2><div class="group">'+row("내부 페이지","하단 메뉴와 소속 탭 유지","check-circle","inner")+row("독립 페이지","하단 메뉴 없이 작업","bookmark","focus")+row("바텀시트","현재 페이지 위에서 설정","more","sheet")+'</div></section><section class="surface"><h2 class="section-title">본문 영역</h2><p class="support">각 제품 화면의 정보와 콘텐츠는 이후 이 자리에 개별 구성합니다.</p></section>',footer:null}]));
// Replace individual body/slots later. Do not create per-page copies of the shell.
const fixtures={
 dm:{type:"non-global",title:"디엠",body:()=>'<div><h2 class="display">메시지</h2><p class="support">메시지 서비스는 아직 연결되지 않았어요.</p></div>',footer:null},
 inner:{type:"global-inner",title:"내부 페이지",body:()=>'<div><h2 class="display">이어지는 탐색</h2><p class="support">상단은 상세 헤더로 바뀌고 하단 메뉴는 유지돼요.</p></div><div class="group">'+row("표시 설정","","more","sheet")+'</div><section class="surface"><h2 class="section-title">본문 영역</h2><p class="support">고정 행동이 있어도 본문은 별도로 스크롤돼요.</p></section>',footer:{label:"다음",tone:"",action:"focus"}},
 focus:{type:"non-global",title:"독립 페이지",body:()=>'<div><h2 class="display">한 가지 작업에<br>집중해요</h2><p class="support">하단 메뉴를 숨기고 대표 행동만 남기는 틀이에요.</p></div><label class="field">제목<input id="draft" placeholder="입력해 보기" maxlength="80"></label><div class="group">'+row("표시 설정","","more","sheet")+'</div>',footer:{label:"적용하기",tone:"brand",action:"confirm"}}
};
let stack=[{key:"home",owner:"home",scroll:0,draft:""}],tabs={};let active=stack[0],lastTrigger=null;
function model(s){return roots[s.key]||Object.assign({},fixtures[s.key],{owner:s.owner})}
function save(){active.scroll=$("content").scrollTop;active.draft=$("draft")?.value||""}
function route(key){if($("sheet").open)$("sheet").close();save();const owner=active.owner;if(roots[key]){tabs[owner]=stack;stack=tabs[key]||[{key,owner:key,scroll:0,draft:""}]}else stack.push({key,owner,scroll:0,draft:""});active=stack.at(-1);render()}
function back(){save();if(stack.length>1)stack.pop();else stack=[{key:active.owner,owner:active.owner,scroll:0,draft:""}];active=stack.at(-1);render()}
function render(){const p=model(active),type=PAGE_TYPES[p.type];$("shell").dataset.pageType=p.type;
 const header=type.header==="root"?'<div class="head root"><h1>'+p.title+'</h1><div class="end">'+ib("bell-connected-root","알림","notifications")+ib("menu-root","메뉴","menu")+'</div></div>':'<div class="head standard"><div class="start">'+ib("left","뒤로 가기","back",true)+'</div><h1>'+p.title+'</h1><div class="end">'+ib("more","표시 설정","sheet",true)+'</div></div>';
 $("header").innerHTML=header;$("content").innerHTML=p.body();if($("draft"))$("draft").value=active.draft||"";
 $("content").scrollTop=active.scroll||0;$("content").classList.remove("enter");void $("content").offsetWidth;$("content").classList.add("enter");
 $("footer").hidden=!p.footer;$("footer").innerHTML=p.footer?cta(p.footer.label,p.footer.tone,p.footer.action):"";
 $("nav").hidden=!type.nav;$("nav").innerHTML=destinations.map(([key,label,off,on])=>'<button data-action="tab:'+key+'" aria-label="'+label+'" '+(key===p.owner?'aria-current="page"':'')+'>'+icon(key===p.owner?on:off)+'</button>').join("");
}
function showSheet(trigger){lastTrigger=trigger;const menu=trigger?.dataset?.action==="menu";$("sheet-title").textContent=menu?"메뉴":"표시 설정";$("sheetChoices").innerHTML=menu?row("디엠","","chat","dm"):row("중간 높이","","chevron","snap-mid")+row("전체 높이","","chevron","snap-full");$("sheet").dataset.snap="min";$("theme").checked=document.documentElement.dataset.theme==="dark";$("sheet").showModal()}
function snap(step){const d=$("sheet"),sizes=["min","mid","full"],i=sizes.indexOf(d.dataset.snap)+step;if(i<0)d.close();else d.dataset.snap=sizes[Math.min(2,i)]}
$("sheetChoices").innerHTML=row("중간 높이","","chevron","snap-mid")+row("전체 높이","","chevron","snap-full");
document.addEventListener("click",e=>{const b=e.target.closest("button");if(!b)return;const a=b.dataset.action;if(b.hasAttribute("data-close")){b.closest("dialog").close();return}if(!a)return;if(a.startsWith("tab:")){route(a.slice(4));return}if(a==="back")back();else if(a==="sheet"||a==="menu")showSheet(b);else if(a==="notifications")$("notifications").showModal();else if(a==="confirm")$("confirm").showModal();else if(a==="snap-mid"||a==="snap-full")$("sheet").dataset.snap=a.slice(5);else if(fixtures[a])route(a)});
$("theme").addEventListener("change",e=>document.documentElement.dataset.theme=e.target.checked?"dark":"light");
$("sheet").addEventListener("close",()=>{if(lastTrigger?.isConnected)lastTrigger.focus({preventScroll:true})});
for(const d of document.querySelectorAll("dialog"))d.addEventListener("click",e=>{if(e.target!==d)return;const r=d.getBoundingClientRect();if(e.clientY<r.top||e.clientY>r.bottom||e.clientX<r.left||e.clientX>r.right)d.close()});
let start=null,moved=false;const grip=$("grip");grip.addEventListener("pointerdown",e=>{start=e.clientY;moved=false;grip.setPointerCapture(e.pointerId)});grip.addEventListener("pointerup",e=>{if(start===null)return;const dy=e.clientY-start;start=null;if(Math.abs(dy)>24){moved=true;snap(dy<0?1:-1)}});grip.addEventListener("pointercancel",()=>{start=null;moved=false});grip.addEventListener("click",()=>{if(!moved)snap(1);moved=false});grip.addEventListener("keydown",e=>{if(e.key==="ArrowUp"||e.key==="ArrowDown"){e.preventDefault();snap(e.key==="ArrowUp"?1:-1)}});
render();
