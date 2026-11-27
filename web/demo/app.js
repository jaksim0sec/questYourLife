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

 // Session-only prototype; real AI, scheduling and rewards are not connected.
const goals=[
 {id:"g1",title:"일본어 기초 다지기",tasks:[{id:"g1t1",title:"단어 15개 복습하기",detail:"단어장",today:true,done:false},{id:"g1t2",title:"짧은 일본어 영상 듣기",detail:"10분",today:true,done:true},{id:"g1t3",title:"이번 주 배운 표현 정리하기",detail:"다음 단계",today:false,done:false}]},
 {id:"g2",title:"매일 조금씩 독서하기",tasks:[{id:"g2t1",title:"책 10쪽 읽기",detail:"독서",today:true,done:false},{id:"g2t2",title:"기억에 남는 문장 기록하기",detail:"독서 기록",today:true,done:false}]}
];
let nextGoal=3;
const esc=s=>String(s??"").replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
const allTasks=()=>goals.flatMap(g=>g.tasks.map(t=>({g,t})));
const todayTasks=()=>allTasks().filter(x=>x.t.today);
const stat=items=>({total:items.length,done:items.filter(t=>t.done).length});
const progress=(done,total)=>'<progress class="quest-progress" value="'+done+'" max="'+Math.max(1,total)+'" aria-label="진행률"></progress>';
const section=(title,body,link="")=>'<section><div class="section-head"><h2 class="section-title">'+title+'</h2>'+link+'</div>'+body+'</section>';
const textAction=(title,action)=>'<button class="text-action" data-action="'+action+'">'+title+icon("chevron")+'</button>';
const demoNote=()=>'<p class="support demo-note">시연용 예시 데이터 · 새로고침하면 초기화돼요.</p>';
function taskRow(g,t,showGoal=false){
 return '<div class="quest-task'+(t.done?' is-done':'')+'"><button class="task-check" aria-label="'+esc(t.title)+(t.done?' 완료 취소':' 완료')+'" aria-pressed="'+t.done+'" data-action="toggle:'+t.id+'">'+icon(t.done?"check-circle-fill":"check-circle")+'</button><button class="task-content" data-action="task:'+t.id+'"><span>'+esc(t.title)+'</span><small>'+esc(showGoal?g.title:t.detail||"퀘스트")+'</small>'+icon("chevron")+'</button></div>';
}
function taskGroup(items,showGoal=false){return '<div class="surface task-group">'+items.map(({g,t})=>taskRow(g,t,showGoal)).join("")+'</div>'}

// Home review v2: expression, shared activity and discovery are content, not decoration.
// Existing C-AVATAR normalized placement; demo equipment is a preview, never an inventory grant.
const avatarItems={
 beanie:{file:"cosmetic_beanie.png",label:"골지 비니",box:[7.5,5.5,85,61.2,40]},
 cap:{file:"cosmetic_blue_cap.png",label:"블루 캡",box:[6.5,1.5,87,58.2,45]},
 headphones:{file:"cosmetic_black_headphones.png",label:"블랙 헤드폰",box:[3,17.5,94,68.5,21]},
 cream:{file:"cosmetic_cream_headphones.png",label:"크림 헤드폰",box:[3,17.5,94,68.5,20]}
};
let homeLook=["cap","headphones"];
const homeArt=(file,cls="")=>'<img class="'+cls+'" src="../../assets/art/'+file+'" alt="" loading="lazy">';
function mascot(keys,cls=""){
 const layers=keys.map(k=>avatarItems[k]).filter(Boolean);
 return '<span class="mascot '+cls+'" aria-hidden="true"><img class="mascot-face" src="../../assets/art/mascot/mascot_face_stamp.png" alt="">'+layers.map(x=>'<img src="../../assets/art/cosmetics/'+x.file+'" alt="" style="left:'+x.box[0]+'%;top:'+x.box[1]+'%;width:'+x.box[2]+'%;height:'+x.box[3]+'%;z-index:'+x.box[4]+'">').join("")+'</span>';
}
const socialExamples=[
 {id:"s1",name:"모카",look:["beanie","cream"],text:"오늘의 독서를 마쳤어요",context:"독서 챌린지 · 활동 예시"},
 {id:"s2",name:"하루",look:["cap"],text:"새로운 일본어 목표를 만들었어요",context:"새 도전 · 활동 예시"}
];
const cheers=new Set();
const homeBooks=[
 {id:"b1",title:"일주일, 하루 한 페이지",tag:"독서",author:"모카",look:["beanie"],art:"05_ribbon_gift.png",description:"매일 부담 없는 분량을 읽고 인상 깊은 문장을 남기는 작은 독서 도전.",steps:["읽고 싶은 책 한 권 고르기","오늘 한 페이지 읽기","기억에 남은 문장 기록하기"]},
 {id:"b2",title:"하루 10분 일본어",tag:"언어",author:"하루",look:["cap"],art:"04_mint_tin.png",description:"짧은 표현을 듣고 따라 하며 매일 조금씩 익히는 언어 도전.",steps:["오늘의 일본어 표현 하나 고르기","짧은 예문을 듣고 따라 하기","배운 표현 기록하기"]}
];
function homeBookTile(b){
 return '<button class="home-book" data-action="home-book:'+b.id+'"><span class="home-book-art">'+homeArt("rewards/"+b.art)+'</span><span class="home-book-info"><small>'+esc(b.tag)+'</small><strong>'+esc(b.title)+'</strong><span class="book-author">'+mascot(b.look)+'<span>'+esc(b.author)+'</span></span></span></button>';
}
function homeBody(){
 const items=todayTasks(),{total,done}=stat(items.map(x=>x.t)),remaining=total-done;
 const ordered=[...items.filter(x=>!x.t.done),...items.filter(x=>x.t.done)];
 const completed=allTasks().filter(x=>x.t.done).length;
 const next=ordered.find(x=>!x.t.done);
 return '<section class="home-identity"><div class="home-identity-copy"><p class="support">나의 갓생</p><h2 class="display">오늘도,<br>나를 만들어 가요</h2><button class="text-action" data-action="home-look">마스코트 꾸미기'+icon("chevron")+'</button></div><button class="home-avatar-button" data-action="home-look" aria-label="마스코트 꾸미기">'+mascot(homeLook,"hero-mascot")+'</button></section>'+
 '<section class="surface home-today"><div class="home-today-top"><div><p class="support">오늘의 퀘스트</p><h2 class="home-count">'+(total?remaining+'<small>개 남았어요</small>':'첫 도전을 시작해요')+'</h2></div><div class="home-ring" style="--pct:'+(total?done/total*100:0)+'%" role="img" aria-label="'+done+'개 완료, 총 '+total+'개"><span>'+done+'<small>/'+total+'</small></span></div></div>'+
 (next?'<div class="home-next"><span class="support">다음 퀘스트</span>'+'<button class="home-next-link" data-action="task:'+next.t.id+'"><strong>'+esc(next.t.title)+'</strong>'+icon("chevron")+'</button>'+'</div>':total?'<p class="home-finished">'+icon("check-circle-fill")+' 오늘 할 일을 모두 마쳤어요.</p>':cta("목표 만들기","brand","create"))+'</section>'+
 section("오늘의 퀘스트",items.length?taskGroup(ordered,true):'<div class="surface"><p class="support">오늘 할 일을 직접 정해 보세요.</p></div>',textAction("전체 목표","tab:quests"))+
 '<section class="home-achievement"><div><p class="support">나의 성취</p><h2 class="home-count">'+completed+'<small>개의 행동을 완료했어요</small></h2><p class="support">꾸미기와 연결될 나의 기록</p><button class="text-action" data-action="home-rewards">보상과 꾸미기 미리보기'+icon("chevron")+'</button></div>'+homeArt("rewards/cash-coins-stack.png","achievement-art")+'</section>'+
 section("친구들의 도전",'<div class="social-rows">'+socialExamples.map(x=>'<div class="social-row">'+mascot(x.look,"social-mascot")+'<div class="social-copy"><strong>'+x.name+'</strong><p>'+x.text+'</p><small>'+x.context+'</small></div><button class="cheer-button" data-action="cheer:'+x.id+'" aria-label="'+x.name+' 응원'+(cheers.has(x.id)?" 취소":"")+'" aria-pressed="'+cheers.has(x.id)+'">'+icon("heart")+'<span>'+(cheers.has(x.id)?"응원함":"응원")+'</span></button></div>').join("")+'</div>',textAction("디엠","dm"))+
 '<section class="surface home-clan"><div class="home-clan-copy"><p class="support">공동 도전 예시</p><h2>우리의 작은 습관</h2><p class="support">친구들과 각자의 목표를 함께 실천해요.</p><button class="text-action" data-action="home-clan">도전 살펴보기'+icon("chevron")+'</button></div>'+homeArt("rewards/03_delivery_parcel.png","clan-art")+'</section>'+
 section("새로운 퀘스트북",'<div class="home-books">'+homeBooks.map(homeBookTile).join("")+'</div>',textAction("탐색","tab:explore"))+demoNote();
}
function homeLookBody(){
 return '<section class="look-preview">'+mascot(homeLook,"large-mascot")+'<h2 class="display">나만의 모습</h2><p class="support">예시 소품을 조합해 보세요. 실제 보유나 지급과는 무관한 미리보기예요.</p></section>'+section("착용 미리보기",'<div class="look-options">'+Object.entries(avatarItems).map(([key,x])=>'<button class="surface look-option" data-action="look:'+key+'" aria-pressed="'+homeLook.includes(key)+'">'+homeArt("cosmetics/"+x.file)+'<strong>'+x.label+'</strong><small>'+(homeLook.includes(key)?"착용 중":"미리보기")+'</small></button>').join("")+'</div>');
}
function homeRewardsBody(){
 return '<section class="reward-preview-hero">'+homeArt("rewards/cash-coins-stack.png")+'<h2 class="display">현실의 성취가<br>나만의 모습으로</h2><p class="support">캐시와 장식은 실제 행동을 꾸미기 경험에 연결할 예정이에요. 지급 조건과 수량은 아직 정해지지 않았어요.</p></section>'+section("꾸미기 예시",'<div class="look-options">'+Object.values(avatarItems).slice(0,2).map(x=>'<div class="surface look-option">'+homeArt("cosmetics/"+x.file)+'<strong>'+x.label+'</strong></div>').join("")+'</div>')+cta("마스코트 미리보기","","home-look");
}
function homeBookBody(){
 const b=homeBooks.find(x=>x.id===active.bookId);if(!b)return '<p class="support">퀘스트북을 찾을 수 없어요.</p>';
 return '<section class="home-book-detail">'+homeArt("rewards/"+b.art)+'<p class="support">'+esc(b.tag)+' · 퀘스트북 예시</p><h2 class="display">'+esc(b.title)+'</h2><div class="book-author">'+mascot(b.look)+'<strong>'+esc(b.author)+'</strong></div><p class="support">'+esc(b.description)+'</p></section>'+section("첫날의 행동",'<div class="surface review-steps">'+b.steps.map(t=>'<div>'+icon("check-circle")+'<span>'+esc(t)+'</span></div>').join("")+'</div>')+cta("내 계획으로 가져오기","brand","copy-home-book");
}
function homeClanBody(){
 return '<section class="reward-preview-hero">'+homeArt("rewards/03_delivery_parcel.png")+'<h2 class="display">우리의 작은 습관</h2><p class="support">같은 목표를 강요하지 않고, 각자 정한 행동을 응원하는 공동 도전 예시예요.</p></section>'+section("함께하는 사람들",'<div class="social-rows">'+socialExamples.map(x=>'<div class="social-row">'+mascot(x.look,"social-mascot")+'<div class="social-copy"><strong>'+x.name+'</strong><p>'+x.text+'</p><small>예시 참여자</small></div></div>').join("")+'</div>')+'<p class="support">클랜 참여와 공유 범위는 아직 실제 서비스에 연결되지 않았어요.</p>'+cta("클랜 둘러보기","weak","tab:clan");
}
function setLook(key){const item=avatarItems[key];if(!item)return;const ear=k=>["headphones","cream"].includes(k);save();homeLook=homeLook.includes(key)?homeLook.filter(k=>k!==key):[...homeLook.filter(k=>ear(k)!==ear(key)),key];render(false);document.querySelector?.('[data-action="look:'+key+'"]')?.focus({preventScroll:true})}
function cheer(id){if(!socialExamples.some(x=>x.id===id))return;save();cheers.has(id)?cheers.delete(id):cheers.add(id);render(false);document.querySelector?.('[data-action="cheer:'+id+'"]')?.focus({preventScroll:true});$("status").textContent=cheers.has(id)?"시연 응원 표시를 추가했어요.":"시연 응원 표시를 취소했어요."}
function goalCard(g){
 const {done,total}=stat(g.tasks),next=g.tasks.find(t=>!t.done);
 return '<button class="surface goal-card" data-action="goal:'+g.id+'"><div class="goal-card-top"><div><small class="support">내 목표</small><h3>'+esc(g.title)+'</h3></div>'+icon("chevron")+'</div><div class="progress-caption"><span>'+esc(next?"다음 · "+next.title:"모든 단계 완료")+'</span><strong>'+done+'/'+total+'</strong></div>'+progress(done,total)+'</button>';
}
function questsBody(){
 const current=goals.filter(g=>g.tasks.some(t=>!t.done)),completed=goals.filter(g=>g.tasks.every(t=>t.done));
 return '<section class="quest-intro"><h2 class="display">나의 목표를<br>하나씩 이루어 가요</h2><p class="support">현실의 목표를 실행할 수 있는 작은 단계로 나눠 보세요.</p>'+cta("새 목표 만들기","brand","create")+'</section>'+
 section("진행 중인 목표",current.length?'<div class="group">'+current.map(goalCard).join("")+'</div>':'<div class="surface"><p class="support">진행 중인 목표가 없어요.</p></div>')+
 section("참여 중인 퀘스트북",'<div class="surface"><p class="support">아직 참여 중인 퀘스트북이 없어요.</p>'+cta("퀘스트북 둘러보기","weak","tab:explore")+'</div>')+
 section("완료한 목표",completed.length?'<div class="group">'+completed.map(goalCard).join("")+'</div>':'<p class="support">목표를 모두 완료하면 여기에 모여요.</p>')+demoNote();
}
function goalBody(){
 const g=goals.find(g=>g.id===active.goalId);if(!g)return '<p class="support">목표를 찾을 수 없어요.</p>';
 const {done,total}=stat(g.tasks);
 return '<section><p class="support">내 목표</p><h2 class="display">'+esc(g.title)+'</h2><div class="progress-caption"><span>목표 진행</span><strong>'+done+' / '+total+'</strong></div>'+progress(done,total)+'</section>'+
 section("실행 단계",taskGroup(g.tasks.map(t=>({g,t}))))+demoNote();
}
function taskBody(){
 const item=allTasks().find(x=>x.t.id===active.taskId);if(!item)return '<p class="support">퀘스트를 찾을 수 없어요.</p>';
 const {g,t}=item;
 return '<section><p class="support">'+esc(g.title)+'</p><h2 class="display">'+esc(t.title)+'</h2><p class="support">'+esc(t.detail||"직접 수행한 뒤 완료를 체크해요.")+'</p></section><div class="surface"><div class="progress-caption"><span>현재 상태</span><strong>'+(t.done?"완료":"진행 전")+'</strong></div><p class="support">실제 행동을 마쳤다면 직접 완료를 체크해 주세요. 잘못 체크한 경우 취소할 수 있어요.</p></div>'+cta(t.done?"완료 취소":"완료 체크",t.done?"weak":"","toggle:"+t.id)+demoNote();
}
const samplePlan={title:"하루 10분 독서하기",steps:"읽을 책 고르기\n10분 동안 읽기\n인상 깊은 문장 한 줄 남기기"};
function createBody(){
 const d=active.goalInput||{title:"",steps:""};
 return '<section><h2 class="display">어떤 목표를<br>이루고 싶나요?</h2><p class="support">목표와 오늘 실행할 작은 행동을 정해요.</p></section><div class="group"><label class="field">목표<input id="goal-title" maxlength="80" value="'+esc(d.title)+'" placeholder="예: 일본어 공부하기"></label><label class="field">오늘 할 행동<textarea id="goal-steps" maxlength="600" rows="4" placeholder="한 줄에 한 가지 행동씩 적어 주세요">'+esc(d.steps)+'</textarea></label><p id="plan-error" class="field-error" role="alert" hidden></p></div><div class="surface"><h2 class="section-title">AI 계획 예시</h2><p class="support">실제 AI는 아직 연결되지 않았어요. 미리 준비한 예시를 불러와 수정할 수 있어요.</p>'+cta("예시 불러오기","weak","sample-plan")+'</div>';
}
function reviewBody(){
 const d=active.plan;return '<section><p class="support">내가 작성한 계획</p><h2 class="display">'+esc(d.title)+'</h2><p class="support">아래 행동을 오늘의 퀘스트로 추가할까요?</p></section>'+section("실행 단계",'<div class="surface review-steps">'+d.steps.map(t=>'<div>'+icon("check-circle")+'<span>'+esc(t)+'</span></div>').join("")+'</div>')+'<p class="support">확정 전에는 목표나 오늘의 목록에 추가되지 않아요.</p>';
}
function readGoalInput(){return {title:$("goal-title")?.value||"",steps:$("goal-steps")?.value||""}}
function validatePlan(d){const title=d.title.trim(),steps=d.steps.split(/\r?\n/).map(x=>x.trim()).filter(Boolean);if(!title||title.length>80)return {error:"목표를 1~80자로 입력해 주세요.",field:"goal-title"};if(!steps.length||steps.length>10||steps.some(x=>x.length>100))return {error:"행동을 한 줄에 100자 이내, 1~10개로 입력해 주세요.",field:"goal-steps"};return {title,steps}}
function previewPlan(){const d=validatePlan(readGoalInput());if(d.error){const e=$("plan-error");e.textContent=d.error;e.hidden=false;$(d.field).focus();return}route("review",{plan:d})}
function acceptPlan(){
 if(active.key!=="review"||!active.plan)return;
 const d=active.plan,id="g"+nextGoal++;goals.push({id,title:d.title,tasks:d.steps.map((title,i)=>({id:id+"t"+(i+1),title,detail:"오늘 할 행동",today:true,done:false}))});
 const owner=active.owner;stack.splice(-2,2);stack.push({key:"goal",owner,goalId:id,scroll:0,draft:""});active=stack.at(-1);render();
}
function toggleTask(id){const item=allTasks().find(x=>x.t.id===id);if(!item)return;save();item.t.done=!item.t.done;render(false);document.querySelector?.('[data-action="toggle:'+id+'"]')?.focus({preventScroll:true});$("status").textContent=item.t.done?"퀘스트를 완료했어요.":"퀘스트 완료를 취소했어요."}

roots.home.body=homeBody;roots.quests.body=questsBody;
// Other roots remain shell fixtures.
const fixtures={
 "home-look":{type:"global-inner",title:"꾸미기 미리보기",body:homeLookBody,footer:null},
 "home-rewards":{type:"global-inner",title:"보상 미리보기",body:homeRewardsBody,footer:null},
 "home-book":{type:"global-inner",title:"퀘스트북",body:homeBookBody,footer:null},
 "home-clan":{type:"global-inner",title:"공동 도전",body:homeClanBody,footer:null},
 goal:{type:"global-inner",title:"목표 상세",body:goalBody,footer:null},
 task:{type:"global-inner",title:"퀘스트 상세",body:taskBody,footer:null},
 create:{type:"non-global",title:"목표 만들기",body:createBody,footer:{label:"계획 확인",tone:"brand",action:"preview-plan"}},
 review:{type:"non-global",title:"계획 확인",body:reviewBody,footer:{label:"목표 추가하기",tone:"brand",action:"accept-plan"}},
 dm:{type:"non-global",title:"디엠",body:()=>'<div><h2 class="display">메시지</h2><p class="support">메시지 서비스는 아직 연결되지 않았어요.</p></div>',footer:null},
 inner:{type:"global-inner",title:"내부 페이지",body:()=>'<div><h2 class="display">이어지는 탐색</h2><p class="support">상단은 상세 헤더로 바뀌고 하단 메뉴는 유지돼요.</p></div><div class="group">'+row("표시 설정","","more","sheet")+'</div><section class="surface"><h2 class="section-title">본문 영역</h2><p class="support">고정 행동이 있어도 본문은 별도로 스크롤돼요.</p></section>',footer:{label:"다음",tone:"",action:"focus"}},
 focus:{type:"non-global",title:"독립 페이지",body:()=>'<div><h2 class="display">한 가지 작업에<br>집중해요</h2><p class="support">하단 메뉴를 숨기고 대표 행동만 남기는 틀이에요.</p></div><label class="field">제목<input id="draft" placeholder="입력해 보기" maxlength="80"></label><div class="group">'+row("표시 설정","","more","sheet")+'</div>',footer:{label:"적용하기",tone:"brand",action:"confirm"}}
};
let stack=[{key:"home",owner:"home",scroll:0,draft:""}],tabs={};let active=stack[0],lastTrigger=null;
function model(s){return roots[s.key]||Object.assign({},fixtures[s.key],{owner:s.owner})}
function save(){active.scroll=$("content").scrollTop;active.draft=$("draft")?.value||"";if(active.key==="create")active.goalInput=readGoalInput()}
function route(key,params={}){if(!Object.hasOwn(roots,key)&&!Object.hasOwn(fixtures,key))return;if($("sheet").open)$("sheet").close();save();const owner=active.owner;if(roots[key]){tabs[owner]=stack;stack=tabs[key]||[{key,owner:key,scroll:0,draft:""}]}else stack.push({key,owner,scroll:0,draft:"",...params});active=stack.at(-1);render()}
function back(){save();if(stack.length>1)stack.pop();else stack=[{key:active.owner,owner:active.owner,scroll:0,draft:""}];active=stack.at(-1);render()}
function render(animate=true){const p=model(active),type=PAGE_TYPES[p.type];$("shell").dataset.pageType=p.type;
 const header=type.header==="root"?'<div class="head root"><h1>'+p.title+'</h1><div class="end">'+ib("bell-connected-root","알림","notifications")+ib("menu-root","메뉴","menu")+'</div></div>':'<div class="head standard"><div class="start">'+ib("left","뒤로 가기","back",true)+'</div><h1>'+p.title+'</h1><div class="end">'+ib("more","표시 설정","sheet",true)+'</div></div>';
 $("header").innerHTML=header;$("content").innerHTML=p.body();if($("draft"))$("draft").value=active.draft||"";
 $("content").scrollTop=active.scroll||0;if(animate){$("content").classList.remove("enter");void $("content").offsetWidth;$("content").classList.add("enter")}
 $("footer").hidden=!p.footer;$("footer").innerHTML=p.footer?cta(p.footer.label,p.footer.tone,p.footer.action):"";
 $("nav").hidden=!type.nav;$("nav").innerHTML=destinations.map(([key,label,off,on])=>'<button data-action="tab:'+key+'" aria-label="'+label+'" '+(key===p.owner?'aria-current="page"':'')+'>'+icon(key===p.owner?on:off)+'</button>').join("");
}
function showSheet(trigger){lastTrigger=trigger;const menu=trigger?.dataset?.action==="menu";$("sheet-title").textContent=menu?"메뉴":"표시 설정";$("sheetChoices").innerHTML=menu?row("디엠","","chat","dm"):row("중간 높이","","chevron","snap-mid")+row("전체 높이","","chevron","snap-full");$("sheet").dataset.snap="min";$("theme").checked=document.documentElement.dataset.theme==="dark";$("sheet").showModal()}
function snap(step){const d=$("sheet"),sizes=["min","mid","full"],i=sizes.indexOf(d.dataset.snap)+step;if(i<0)d.close();else d.dataset.snap=sizes[Math.min(2,i)]}
$("sheetChoices").innerHTML=row("중간 높이","","chevron","snap-mid")+row("전체 높이","","chevron","snap-full");
document.addEventListener("click",e=>{const b=e.target.closest("button");if(!b)return;const a=b.dataset.action;if(b.hasAttribute("data-close")){b.closest("dialog").close();return}if(!a)return;if(a.startsWith("tab:")){route(a.slice(4));return}if(a.startsWith("home-book:"))route("home-book",{bookId:a.slice(10)});else if(a.startsWith("cheer:"))cheer(a.slice(6));else if(a.startsWith("look:"))setLook(a.slice(5));else if(a==="copy-home-book"){const b=homeBooks.find(x=>x.id===active.bookId);if(b)route("create",{goalInput:{title:b.title,steps:b.steps.join("\n")}})}else if(a.startsWith("goal:"))route("goal",{goalId:a.slice(5)});else if(a.startsWith("task:"))route("task",{taskId:a.slice(5)});else if(a.startsWith("toggle:"))toggleTask(a.slice(7));else if(a==="sample-plan"){const d=readGoalInput();if(d.title.trim()||d.steps.trim()){const e=$("plan-error");e.textContent="작성한 내용이 있어요. 비어 있을 때만 예시를 불러올 수 있어요.";e.hidden=false;return}$("goal-title").value=samplePlan.title;$("goal-steps").value=samplePlan.steps;$("plan-error").hidden=true}else if(a==="preview-plan")previewPlan();else if(a==="accept-plan")acceptPlan();else if(a==="back")back();else if(a==="sheet"||a==="menu")showSheet(b);else if(a==="notifications")$("notifications").showModal();else if(a==="confirm")$("confirm").showModal();else if(a==="snap-mid"||a==="snap-full")$("sheet").dataset.snap=a.slice(5);else if(fixtures[a])route(a)});
$("theme").addEventListener("change",e=>document.documentElement.dataset.theme=e.target.checked?"dark":"light");
$("sheet").addEventListener("close",()=>{if(lastTrigger?.isConnected)lastTrigger.focus({preventScroll:true})});
for(const d of document.querySelectorAll("dialog"))d.addEventListener("click",e=>{if(e.target!==d)return;const r=d.getBoundingClientRect();if(e.clientY<r.top||e.clientY>r.bottom||e.clientX<r.left||e.clientX>r.right)d.close()});
let start=null,moved=false;const grip=$("grip");grip.addEventListener("pointerdown",e=>{start=e.clientY;moved=false;grip.setPointerCapture(e.pointerId)});grip.addEventListener("pointerup",e=>{if(start===null)return;const dy=e.clientY-start;start=null;if(Math.abs(dy)>24){moved=true;snap(dy<0?1:-1)}});grip.addEventListener("pointercancel",()=>{start=null;moved=false});grip.addEventListener("click",()=>{if(!moved)snap(1);moved=false});grip.addEventListener("keydown",e=>{if(e.key==="ArrowUp"||e.key==="ArrowDown"){e.preventDefault();snap(e.key==="ArrowUp"?1:-1)}});
render();
