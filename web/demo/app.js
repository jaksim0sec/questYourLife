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
const roots=Object.fromEntries(destinations.map(([key,label])=>[key,{type:"global",owner:key,title:label,body:null,footer:null}]));

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
 return '<div class="quest-task'+(t.done?' is-done':'')+'"><button class="task-check" aria-label="'+esc(t.title)+(t.done?' 완료 취소':' 완료')+'" aria-pressed="'+t.done+'" data-action="toggle:'+t.id+'">'+(t.done?icon("check-circle-fill"):'<span class="unchecked-circle" aria-hidden="true"></span>')+'</button><button class="task-content" data-action="task:'+t.id+'"><span>'+esc(t.title)+'</span><small>'+esc(showGoal?g.title:t.detail||"퀘스트")+'</small>'+icon("chevron")+'</button></div>';
}
function taskGroup(items,showGoal=false){return '<div class="surface task-group">'+items.map(({g,t})=>taskRow(g,t,showGoal)).join("")+'</div>'}

// Home review v2: expression, shared activity and discovery are content, not decoration.
// Existing C-AVATAR normalized placement; demo equipment is a preview, never an inventory grant.
const avatarItems={
 beanie:{file:"cosmetic_beanie.png",label:"골지 비니",box:[7.5,5.5,85,61.2,40]},
 cap:{file:"cosmetic_blue_cap.png",label:"블루 캡",box:[6.5,1.5,87,58.2,45]},
 headphones:{file:"cosmetic_black_headphones.png",label:"블랙 헤드폰",box:[3,17.5,94,68.5,21]},
 cream:{file:"cosmetic_cream_headphones.png",label:"크림 헤드폰",box:[3,17.5,94,68.5,20]},
 creamcap:{file:"cosmetic_cream_cap.png",label:"크림 캡",box:[4.5,6,91,58.2,44]},
 charcoal:{file:"cosmetic_charcoal_cap.png",label:"차콜 캡",box:[4.5,6,91,58.2,43]},
 backward:{file:"cosmetic_backward_cap.png",label:"백워드 캡",box:[10.5,-2,78,61.2,42]},
 bucket:{file:"cosmetic_bucket_hat.png",label:"버킷 햇",box:[6.75,1.2,86.5,65.6,46]},
 earbuds:{file:"cosmetic_white_airpods_pair.png",label:"화이트 이어버드",box:[2.5,48,95,36,32]}
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
 {id:"b1",title:"일주일, 하루 한 페이지",tag:"독서",author:"모카",look:["beanie"],art:"reading.svg",description:"매일 부담 없는 분량을 읽고 인상 깊은 문장을 남기는 작은 독서 도전.",steps:["읽고 싶은 책 한 권 고르기","오늘 한 페이지 읽기","기억에 남은 문장 기록하기"]},
 {id:"b2",title:"하루 10분 일본어",tag:"언어",author:"하루",look:["cap"],art:"language.svg",description:"짧은 표현을 듣고 따라 하며 매일 조금씩 익히는 언어 도전.",steps:["오늘의 일본어 표현 하나 고르기","짧은 예문을 듣고 따라 하기","배운 표현 기록하기"]},
 {id:"b3",title:"20분 집중 루틴",tag:"공부",author:"모카",look:["beanie","cream"],art:"focus.svg",description:"집중하고 싶은 일을 하나 선택하고 짧게 몰입해 보는 도전.",steps:["집중할 일 하나 고르기","방해 요소 정리하기","20분 집중하고 쉬기"]},
 {id:"b4",title:"하루 한 줄 기록",tag:"습관",author:"하루",look:["cap"],art:"journal.svg",description:"하루에 기억하고 싶은 일을 한 줄씩 남겨 보세요.",steps:["오늘 기억하고 싶은 일 떠올리기","한 줄로 기록하기"]}
];
function homeBookTile(b){
 return '<button class="home-book cover-book" data-action="home-book:'+b.id+'"><span class="home-book-art">'+homeArt("questbooks/"+b.art)+'</span><span class="home-book-info"><strong class="book-name">'+esc(b.title)+'</strong><span class="book-author">'+mascot(b.look)+'<span>'+esc(b.author)+' · '+esc(b.tag)+'</span></span></span></button>';
}
function homeTodayBody(){
 const items=todayTasks(),{total,done}=stat(items.map(x=>x.t));
 const ordered=[...items.filter(x=>!x.t.done),...items.filter(x=>x.t.done)];
 return '<section class="today-detail"><h2 class="display">'+(total?done+' / '+total+' 완료':'오늘의 첫 퀘스트')+'</h2>'+(total?progress(done,total):cta("목표 만들기","brand","create"))+'</section>'+
 (total?taskGroup(ordered,true):'')+textAction("전체 목표","tab:quests")+demoNote();
}
function homeCatalogBody(){
 return '<p class="support">퀘스트북 예시</p><div class="home-books cover-grid">'+homeBooks.map(homeBookTile).join("")+'</div>';
}
function homeBody(){
 const items=todayTasks(),{total,done}=stat(items.map(x=>x.t));
 const next=items.find(x=>!x.t.done);
 const daily='<section class="surface home-quick"><div class="quick-line"><div class="quick-summary"><span>오늘의 퀘스트</span><strong>'+done+'/'+total+' 완료</strong></div>'+textAction("전체","home-today")+'</div>'+
 (next?'<div class="quick-action"><button class="task-check" aria-label="'+esc(next.t.title)+' 완료" aria-pressed="false" data-action="toggle:'+next.t.id+'"><span class="unchecked-circle" aria-hidden="true"></span></button><button class="quick-task" data-action="task:'+next.t.id+'">'+esc(next.t.title)+icon("chevron")+'</button></div>':total?'<p class="quick-done">'+icon("check-circle-fill")+' 오늘의 퀘스트 완료</p>':cta("목표 만들기","brand","create"))+'</section>';
 const discovery='<section class="home-discovery"><div class="section-head"><h2 class="section-title">추천 퀘스트북</h2>'+textAction("전체 보기","home-catalog")+'</div><div class="book-rail" aria-label="추천 퀘스트북 예시">'+[homeBooks[2],homeBooks[0],homeBooks[1],homeBooks[3]].map(homeBookTile).join("")+'</div></section>';
 const event='<section><button class="home-event" data-action="home-clan"><span class="event-copy"><small>공동 도전 예시</small><strong>우리의 작은 습관</strong><span>친구와 각자의 목표 실천</span><span class="event-people">'+socialExamples.map(x=>mascot(x.look,"event-person")).join("")+'</span><span class="event-action">살펴보기'+icon("chevron")+'</span></span><span class="event-art">'+homeArt("rewards/06_wooden_chest.png","event-chest")+'</span></button></section>';
 const social=section("친구들의 도전 (예시)",'<div class="social-rows">'+socialExamples.map(x=>'<div class="social-row">'+mascot(x.look,"social-mascot")+'<div class="social-copy"><strong>'+x.name+'</strong><p>'+x.text+'</p></div><button class="cheer-button" data-action="cheer:'+x.id+'" aria-label="'+x.name+' 응원'+(cheers.has(x.id)?" 취소":"")+'" aria-pressed="'+cheers.has(x.id)+'">'+icon("heart")+'<span>'+(cheers.has(x.id)?"응원함":"응원")+'</span></button></div>').join("")+'</div>',textAction("디엠","dm"));
 const expression=section("꾸미기",'<div class="home-expression"><button class="surface expression-tile" data-action="home-look">'+mascot(homeLook,"expression-mascot")+'<span>마스코트 꾸미기'+icon("chevron")+'</span></button><button class="surface expression-tile" data-action="home-rewards">'+homeArt("rewards/cash-coins-stack.png","expression-art")+'<span>보상함'+icon("chevron")+'</span></button></div>');
 return '<div class="home-feed">'+daily+discovery+event+social+expression+demoNote()+'</div>';
}
function homeLookBody(){
 return '<section class="look-preview">'+mascot(homeLook,"large-mascot")+'<p class="support">실제 보유 아이템이 아닌 착용 예시예요.</p></section>'+section("소품 선택",'<div class="look-options">'+Object.entries(avatarItems).map(([key,x])=>'<button class="surface look-option" data-action="look:'+key+'" aria-pressed="'+homeLook.includes(key)+'">'+homeArt("cosmetics/"+x.file)+'<strong>'+x.label+'</strong><small>'+(homeLook.includes(key)?"착용 중":"미리보기")+'</small></button>').join("")+'</div>');
}
function homeRewardsBody(){
 return '<section class="reward-preview-hero">'+homeArt("rewards/cash-coins-stack.png")+'<h2 class="section-title">캐시와 꾸미기 소품</h2><p class="support">퀘스트 보상 예시예요. 지급 조건과 수량은 아직 미정이에요.</p></section>'+section("소품 예시",'<div class="look-options">'+Object.values(avatarItems).slice(0,2).map(x=>'<div class="surface look-option">'+homeArt("cosmetics/"+x.file)+'<strong>'+x.label+'</strong></div>').join("")+'</div>')+cta("마스코트 미리보기","","home-look");
}
function homeBookBody(){
 const b=homeBooks.find(x=>x.id===active.bookId);if(!b)return '<p class="support">퀘스트북을 찾을 수 없어요.</p>';
 return '<section class="home-book-detail">'+homeArt("questbooks/"+b.art)+'<p class="support">'+esc(b.tag)+' · 퀘스트북 예시</p><h2 class="display">'+esc(b.title)+'</h2><div class="book-author">'+mascot(b.look)+'<strong>'+esc(b.author)+'</strong></div><p class="support">'+esc(b.description)+'</p></section>'+section("첫날의 행동",'<div class="surface review-steps">'+b.steps.map(t=>'<div>'+icon("check-circle")+'<span>'+esc(t)+'</span></div>').join("")+'</div>')+cta("내 계획으로 가져오기","brand","copy-home-book");
}
function homeClanBody(){
 return '<section class="reward-preview-hero">'+homeArt("rewards/03_delivery_parcel.png")+'<h2 class="display">우리의 작은 습관</h2><p class="support">각자 정한 행동을 실천하고 서로 응원하는 공동 도전 예시예요.</p></section>'+section("함께하는 사람들",'<div class="social-rows">'+socialExamples.map(x=>'<div class="social-row">'+mascot(x.look,"social-mascot")+'<div class="social-copy"><strong>'+x.name+'</strong><p>'+x.text+'</p><small>예시 참여자</small></div></div>').join("")+'</div>')+'<p class="support">클랜 참여와 공유 범위는 아직 실제 서비스에 연결되지 않았어요.</p>'+cta("클랜 둘러보기","weak","tab:clan");
}
function setLook(key){const item=avatarItems[key];if(!item||!ownedPreview.has(key))return;const ear=k=>["headphones","cream","earbuds"].includes(k);save();homeLook=homeLook.includes(key)?homeLook.filter(k=>k!==key):[...homeLook.filter(k=>ear(k)!==ear(key)),key];render(false);document.querySelector?.('[data-action="look:'+key+'"]')?.focus({preventScroll:true})}
function cheer(id){if(!socialExamples.some(x=>x.id===id))return;save();cheers.has(id)?cheers.delete(id):cheers.add(id);render(false);document.querySelector?.('[data-action="cheer:'+id+'"]')?.focus({preventScroll:true});$("status").textContent=cheers.has(id)?"시연 응원 표시를 추가했어요.":"시연 응원 표시를 취소했어요."}
function goalCard(g){
 const {done,total}=stat(g.tasks),next=g.tasks.find(t=>!t.done);
 return '<button class="surface goal-card" data-action="goal:'+g.id+'"><div class="goal-card-top"><div><small class="support">내 목표</small><h3>'+esc(g.title)+'</h3></div>'+icon("chevron")+'</div><div class="progress-caption"><span>'+esc(next?"다음 · "+next.title:"모든 단계 완료")+'</span><strong>'+done+'/'+total+'</strong></div>'+progress(done,total)+'</button>';
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
function previewPlan(){const d=validatePlan(readGoalInput());if(d.error){const e=$("plan-error");e.textContent=d.error;e.hidden=false;$(d.field).focus();return}route("review",{plan:d,bookId:active.bookId})}
function acceptPlan(){
 if(active.key!=="review"||!active.plan)return;
 const d=active.plan,id="g"+nextGoal++;goals.push({id,title:d.title,...(active.bookId?{bookId:active.bookId}:{}),tasks:d.steps.map((title,i)=>({id:id+"t"+(i+1),title,detail:"오늘 할 행동",today:true,done:false}))});
 const owner=active.owner;stack.splice(-2,2);stack.push({key:"goal",owner,goalId:id,scroll:0,draft:""});active=stack.at(-1);render();
}
function toggleTask(id){const item=allTasks().find(x=>x.t.id===id);if(!item)return;save();item.t.done=!item.t.done;render(false);document.querySelector?.('[data-action="toggle:'+id+'"]')?.focus({preventScroll:true});$("status").textContent=item.t.done?"퀘스트를 완료했어요.":"퀘스트 완료를 취소했어요."}


const demoIdentity="이모리";
let questFilter="active", exploreFilter="전체", exploreQuery="", clanJoined=true, clanContribution=false, clanPanel="members", lookFilter="전체";
let messageDrafts={};
const exampleMessages={s1:["오늘 책 한 페이지 읽었어. 너도 천천히 해 봐!"],s2:["새로운 일본어 퀘스트북 만들었어. 같이 구경할래?"]};
const goalBook=g=>homeBooks.find(b=>b.id===g.bookId);
function bookForGoal(g){return goalBook(g)||homeBooks.find(b=>g.id==="g1"?b.id==="b2":g.id==="g2"?b.id==="b1":false)}
const countDone=()=>allTasks().filter(x=>x.t.done).length;
const chips=(items,current,action,label)=>'<div class="filter-tabs" role="group" aria-label="'+label+'">'+items.map(([k,v])=>'<button aria-pressed="'+(current===k)+'" data-action="'+action+':'+k+'">'+v+'</button>').join("")+'</div>';
function compactGoal(g){
 const {done,total}=stat(g.tasks),b=bookForGoal(g),next=g.tasks.find(t=>!t.done);
 return '<button class="compact-goal" data-action="goal:'+g.id+'"><span class="goal-cover">'+(b?homeArt("questbooks/"+b.art):icon("book"))+'</span><span class="goal-info"><strong>'+esc(g.title)+'</strong><span class="goal-meta">'+(goalBook(g)?"참여한 퀘스트북":"내 목표")+'<b>'+done+'/'+total+'</b></span>'+progress(done,total)+'<small>'+esc(next?"다음 · "+next.title:"완료")+'</small></span>'+icon("chevron")+'</button>';
}
function questsV2(){
 const today=todayTasks(),{done,total}=stat(today.map(x=>x.t)),next=today.find(x=>!x.t.done);
 const visible=goals.filter(g=>questFilter==="all"|| (questFilter==="active"?g.tasks.some(t=>!t.done):g.tasks.every(t=>t.done)));
 const participated=goals.filter(g=>g.bookId);
 return '<div class="product-feed"><button class="daily-strip" data-action="home-today">'+mascot(homeLook,"strip-mascot")+'<span><strong>오늘 <b>'+done+'/'+total+' 완료</b></strong><small>'+esc(next?next.t.title:total?"오늘의 퀘스트 완료":"첫 퀘스트 만들기")+'</small></span>'+icon("chevron")+'</button>'+
 '<section>'+chips([["active","진행 중"],["done","완료"],["all","전체"]],questFilter,"qfilter","목표 상태")+'<div class="group">'+(visible.length?visible.map(compactGoal).join(""):'<p class="support empty-state">해당하는 목표가 없어요.</p>')+'</div></section>'+
 cta("새 목표 만들기","brand","create")+
 section("오늘 할 일",today.length?taskGroup(today,true):'<p class="support">오늘의 퀘스트가 없어요.</p>',textAction("전체","home-today"))+
 (participated.length?section("참여한 퀘스트북",'<div class="mini-books">'+participated.map(g=>'<button class="mini-book" data-action="goal:'+g.id+'">'+homeArt("questbooks/"+goalBook(g).art)+'<strong>'+esc(g.title)+'</strong></button>').join("")+'</div>'):'')+demoNote()+'</div>';
}
function exploreResults(){
 const query=exploreQuery.trim().toLocaleLowerCase("ko");
 const books=homeBooks.filter(b=>(exploreFilter==="전체"||b.tag===exploreFilter)&&(!query||(b.title+" "+b.tag+" "+b.author+" "+b.description).toLocaleLowerCase("ko").includes(query)));
 return books.length?'<div class="home-books cover-grid explore-grid">'+books.map(homeBookTile).join("")+'</div>':'<p class="support empty-state">검색 결과가 없어요.</p>';
}
function exploreBody(){
 return '<div class="product-feed"><label class="search-field">'+icon("search")+'<input id="explore-search" type="search" aria-label="퀘스트북 검색" placeholder="퀘스트북 검색" maxlength="80" value="'+esc(exploreQuery)+'"></label>'+
 '<section>'+chips([["전체","전체"],["공부","공부"],["독서","독서"],["언어","언어"],["습관","습관"]],exploreFilter,"efilter","퀘스트북 분야")+'<div id="explore-results">'+exploreResults()+'</div></section>'+
 section("함께 도전하기",'<button class="surface discovery-link" data-action="home-clan">'+homeArt("rewards/06_wooden_chest.png")+'<span><strong>우리의 작은 습관</strong><small>공동 도전 예시</small></span>'+icon("chevron")+'</button>')+demoNote()+'</div>';
}

// Archive-inspired presentation only; preview ownership and economy are not production data.
const ownedPreview=new Set(["cap","headphones","beanie","cream"]);
const shopCatalog=["creamcap","charcoal","backward","bucket","cream","headphones","earbuds"];
const itemType=k=>["headphones","cream","earbuds"].includes(k)?"오디오":"모자";
let shopFilter="전체", wardrobeFilter="전체";
const itemMedia=(k,cls="")=>'<span class="item-media '+cls+'">'+homeArt("cosmetics/"+avatarItems[k].file)+'</span>';
const itemTile=(k,action="item:")=>'<button class="catalog-tile" data-action="'+action+k+'"'+(action==="look:"?' aria-pressed="'+homeLook.includes(k)+'"':'')+'>'+itemMedia(k)+'<span class="catalog-info"><strong>'+avatarItems[k].label+'</strong><small>'+(action==="look:"?(homeLook.includes(k)?"착용 중":"선택 가능"):(ownedPreview.has(k)?"보유 예시":"상품 예시"))+'</small></span></button>';
const itemFilters=(value,action,label)=>chips([["전체","전체"],["모자","모자"],["오디오","오디오"]],value,action,label);
const rewardExamples=[
 {id:"first",title:"첫 도전 상자",art:"06_wooden_chest.png",source:"퀘스트 보상 예시"},
 {id:"clan",title:"공동 도전 상자",art:"08_premium_glowing_chest.png",source:"클랜 보상 예시"}
];
const rewardRow=b=>'<button class="reward-open-row" data-action="reward:'+b.id+'">'+homeArt("rewards/"+b.art)+'<span><strong>'+b.title+'</strong><small>'+b.source+'</small></span>'+icon("chevron")+'</button>';
function shopBody(){
 const pick="creamcap",visible=shopCatalog.filter(k=>shopFilter==="전체"||itemType(k)===shopFilter);
 return '<div class="product-feed shop-page"><section class="surface shop-feature">'+itemMedia(pick)+'<div class="shop-feature-copy"><p class="support">추천 소품</p><h2>'+avatarItems[pick].label+'</h2><button class="compact-pill" data-action="item:'+pick+'">소품 보기'+icon("chevron")+'</button></div></section>'+
 '<section><h2 class="section-title">꾸미기 아이템</h2>'+itemFilters(shopFilter,"shopfilter","상품 종류")+'<div class="catalog-grid">'+visible.map(k=>itemTile(k)).join("")+'</div></section>'+
 section("보상 상자",'<div class="reward-rail">'+rewardExamples.map(rewardRow).join("")+'</div>',textAction("보상함","home-rewards"))+
 row("퀘스트 둘러보기","실천으로 얻는 보상","check-circle","tab:quests")+demoNote()+'</div>';
}
function itemDetailBody(){
 const k=active.itemKey,x=avatarItems[k];if(!x)return '<p class="support">소품을 찾을 수 없어요.</p>';
 return '<div class="product-feed"><section class="item-detail-art">'+itemMedia(k)+'</section><section class="item-detail-copy"><p class="support">'+itemType(k)+'</p><h2 class="display">'+x.label+'</h2><p class="support">'+(ownedPreview.has(k)?"시연 보유 소품":"카탈로그 예시 · 실제 가격과 구매 조건은 미정이에요.")+'</p></section>'+
 (ownedPreview.has(k)?cta("꾸미기","","home-look"):cta("착용 미리보기","","try-item"))+'</div>';
}
function wardrobeBody(){
 const visible=[...ownedPreview].filter(k=>wardrobeFilter==="전체"||itemType(k)===wardrobeFilter);
 return '<div class="product-feed"><section class="wardrobe-stage">'+mascot(homeLook,"wardrobe-mascot")+'<div class="equipped-line">'+["모자","오디오"].map(type=>{const k=homeLook.find(k=>itemType(k)===type);return '<div><small>'+type+'</small><strong>'+(k?avatarItems[k].label:"착용 안 함")+'</strong></div>'}).join("")+'</div></section>'+
 '<section><div class="section-head"><h2 class="section-title">보유 소품 예시</h2>'+textAction("상점","shop")+'</div>'+itemFilters(wardrobeFilter,"wardrobefilter","소품 종류")+'<div class="catalog-grid">'+visible.map(k=>itemTile(k,"look:")).join("")+'</div></section>'+demoNote()+'</div>';
}
function rewardListBody(){return '<div class="product-feed">'+section("보상 상자 예시",'<div class="reward-list">'+rewardExamples.map(rewardRow).join("")+'</div>')+'<p class="support">보상 획득 조건과 지급량은 아직 정해지지 않았어요.</p>'+row("꾸미기 상점","","gift","shop")+'</div>'}
function rewardDetailBody(){
 const b=rewardExamples.find(x=>x.id===active.rewardId);if(!b)return '<p class="support">상자를 찾을 수 없어요.</p>';
 return '<div class="product-feed"><section class="reward-preview-hero">'+homeArt("rewards/"+b.art)+'<h2 class="display">'+b.title+'</h2><p class="support">'+b.source+'</p></section><p class="support">상자 내용물과 획득 조건은 미정이며 실제 지급은 없어요.</p>'+cta(b.id==="clan"?"클랜 살펴보기":"퀘스트 살펴보기","",b.id==="clan"?"tab:clan":"tab:quests")+'</div>';
}
function tryItem(){
 const k=active.itemKey;if(!avatarItems[k])return;const preview=homeLook.filter(x=>itemType(x)!==itemType(k)).concat(k);
 route("item-preview",{itemKey:k,preview});
}
function itemPreviewBody(){
 const k=active.itemKey;if(!avatarItems[k])return '<p class="support">소품을 찾을 수 없어요.</p>';
 return '<section class="look-preview">'+mascot(active.preview,"wardrobe-mascot")+'<h2 class="section-title">'+avatarItems[k].label+'</h2><p class="support">착용 모습만 확인하는 미리보기예요.</p></section>';
}

function clanMembers(){
 return [{name:demoIdentity,look:homeLook,done:clanContribution,me:true},{name:"모카",look:["beanie","cream"],done:true},{name:"하루",look:["cap"],done:false}];
}
function clanBody(){
 const members=clanMembers(),done=members.filter(m=>m.done).length;
 if(!clanJoined)return '<div class="product-feed"><section class="clan-join"><span class="clan-emblem">'+icon("bookmark")+'</span><h2 class="display">느슨한 갓생단</h2><p class="support">각자 정한 행동을 실천하고 서로 응원하는 클랜 예시예요.</p>'+cta("시연 클랜 참여","brand","clan-join")+'</section>'+demoNote()+'</div>';
 const membersList='<div class="plain-member-list">'+members.map(m=>'<div class="member-row">'+mascot(m.look,"social-mascot")+'<strong>'+esc(m.name)+(m.me?' (나)':'')+'</strong><span class="support">'+(m.done?"실천 기록":"미기록")+'</span></div>').join("")+'</div>';
 const activity='<div class="plain-member-list">'+socialExamples.map(x=>'<div class="member-row">'+mascot(x.look,"social-mascot")+'<div><strong>'+x.name+'</strong><p class="support">'+x.text+'</p></div></div>').join("")+'</div>';
 const contribution='<section class="clan-feature"><div><p>공동 도전</p><h2>우리의 작은 습관</h2><div class="clan-progress-caption"><strong>'+done+'/'+members.length+'명 실천</strong></div>'+progress(done,members.length)+'</div>'+homeArt("rewards/06_wooden_chest.png","clan-feature-art")+'</section>'+
 '<section class="clan-own-action"><div><strong>나의 실천 기록</strong><p class="support">개인 목표 내용은 공유하지 않아요.</p></div><button class="compact-pill '+(clanContribution?"":"brand")+'" data-action="clan-contribute">'+(clanContribution?"기록 취소":"실천 기록")+'</button></section>';
 const insight='<section class="clan-insight"><h2 class="section-title">공동 도전 참여 현황</h2><p class="display">'+done+'<small> / '+members.length+'명</small></p>'+progress(done,members.length)+'</section>'+section("멤버별 실천 기록",membersList)+section("활동 예시",activity);
 const panel=clanPanel==="insight"?insight:clanPanel==="rewards"?section("공동 보상 예시",rewardRow(rewardExamples[1]))+row("공동 도전","","bookmark","home-clan"):contribution+section("멤버",membersList,textAction("디엠","dm"))+section("최근 활동",activity);
 return '<div class="product-feed"><section class="clan-identity"><span class="clan-emblem">'+icon("clan-bookmark-fill")+'</span><div><h2>느슨한 갓생단</h2><p class="support">멤버 '+members.length+'명 · 시연</p></div><button class="text-action" data-action="clan-leave">나가기</button></section>'+
 '<section class="clan-tab-section">'+chips([["members","내 클랜"],["insight","인사이트"],["rewards","보상"]],clanPanel,"clan-panel","클랜 정보")+'<div class="product-feed">'+panel+'</div></section>'+demoNote()+'</div>';
}
function myBody(){
 const completed=goals.filter(g=>g.tasks.every(t=>t.done)).length;
 return '<div class="product-feed"><section class="my-profile plain-profile"><div><h2 class="display">'+demoIdentity+'</h2><p class="support">'+(clanJoined?"느슨한 갓생단 · ":"")+'시연 프로필</p><button class="compact-pill" data-action="home-look">프로필 꾸미기</button></div>'+mascot(homeLook,"profile-mascot")+'</section>'+
 '<button class="profile-record" data-action="my-record"><span><small>완료 퀘스트</small><strong>'+countDone()+'</strong></span><span><small>완료 목표</small><strong>'+completed+'</strong></span>'+icon("chevron")+'</button>'+
 section("꾸미기 소품",'<div class="item-rail">'+[...ownedPreview].map(k=>itemTile(k)).join("")+'</div>',textAction("상점","shop"))+
 section("보상함",'<div class="reward-list">'+rewardExamples.map(rewardRow).join("")+'</div>',textAction("전체","home-rewards"))+
 section("내 도전",goals.length?'<div class="plain-goal-list">'+goals.map(g=>{const s=stat(g.tasks);return '<button class="plain-goal" data-action="goal:'+g.id+'"><span><strong>'+esc(g.title)+'</strong><small>'+s.done+'/'+s.total+' 완료</small></span>'+icon("chevron")+'</button>'}).join("")+'</div>':'<p class="support">아직 목표가 없어요.</p>',textAction("전체","tab:quests"))+demoNote()+'</div>';
}
function myRecordBody(){
 const items=allTasks().filter(x=>x.t.done);
 return '<section class="today-detail"><h2 class="display">'+items.length+'개 완료</h2><p class="support">현재 시연에서 완료한 퀘스트</p></section>'+(items.length?taskGroup(items,true):'<p class="support">아직 완료 기록이 없어요.</p>');
}
function dmBody(){
 return '<div class="product-feed"><p class="support">예시 대화 · 실제 메시지를 전송하지 않아요.</p><div class="plain-member-list">'+socialExamples.map(x=>'<button class="dm-row" data-action="conversation:'+x.id+'">'+mascot(x.look,"social-mascot")+'<span><strong>'+x.name+'</strong><small>'+esc((messageDrafts[x.id]||[]).at(-1)||exampleMessages[x.id][0])+'</small></span>'+icon("chevron")+'</button>').join("")+'</div></div>';
}
function conversationBody(){
 const x=socialExamples.find(s=>s.id===active.personId);if(!x)return '<p class="support">대화 없음</p>';
 return '<div class="product-feed"><div class="conversation-person">'+mascot(x.look,"social-mascot")+'<strong>'+x.name+'</strong></div><p class="support">예시 대화 · 아래 입력은 세션에만 표시돼요.</p><div class="chat-list">'+exampleMessages[x.id].map(m=>'<p class="chat-bubble">'+esc(m)+'</p>').join("")+(messageDrafts[x.id]||[]).map(m=>'<p class="chat-bubble mine">'+esc(m)+'</p>').join("")+'</div><label class="search-field"><input id="local-message" maxlength="200" aria-label="시연 메시지" placeholder="시연 메시지 입력"></label>'+cta("시연 메시지 추가","brand","message-local")+'</div>';
}
function localMessage(){const x=socialExamples.find(s=>s.id===active.personId),value=$("local-message")?.value.trim();if(!x||!value)return;(messageDrafts[x.id]??=[]).push(value.slice(0,200));save();render(false)}
function refreshPage(){save();render(false)}

roots.home.body=homeBody;roots.quests.body=questsV2;roots.explore.body=exploreBody;roots.clan.body=clanBody;roots.me.body=myBody;
// All five roots now have product-specific session views.
const fixtures={
 "my-record":{type:"global-inner",title:"성취 기록",body:myRecordBody,footer:null},
 conversation:{type:"non-global",title:"대화",body:conversationBody,footer:null},
 "home-today":{type:"global-inner",title:"오늘의 퀘스트",body:homeTodayBody,footer:null},
 "home-catalog":{type:"global-inner",title:"추천 퀘스트북",body:homeCatalogBody,footer:null},
 "home-look":{type:"non-global",title:"꾸미기",body:wardrobeBody,footer:null},
 "home-rewards":{type:"global-inner",title:"보상함",body:rewardListBody,footer:null},
 shop:{type:"global-inner",title:"상점",body:shopBody,footer:null},
 item:{type:"non-global",title:"소품 상세",body:itemDetailBody,footer:null},
 "item-preview":{type:"non-global",title:"착용 미리보기",body:itemPreviewBody,footer:null},
 reward:{type:"non-global",title:"보상 상자",body:rewardDetailBody,footer:null},
 "home-book":{type:"global-inner",title:"퀘스트북",body:homeBookBody,footer:null},
 "home-clan":{type:"global-inner",title:"공동 도전",body:homeClanBody,footer:null},
 goal:{type:"global-inner",title:"목표 상세",body:goalBody,footer:null},
 task:{type:"global-inner",title:"퀘스트 상세",body:taskBody,footer:null},
 create:{type:"non-global",title:"목표 만들기",body:createBody,footer:{label:"계획 확인",tone:"brand",action:"preview-plan"}},
 review:{type:"non-global",title:"계획 확인",body:reviewBody,footer:{label:"목표 추가하기",tone:"brand",action:"accept-plan"}},
 dm:{type:"non-global",title:"디엠",body:dmBody,footer:null},
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
document.addEventListener("click",e=>{const b=e.target.closest("button");if(!b)return;const a=b.dataset.action;if(b.hasAttribute("data-close")){b.closest("dialog").close();return}if(!a)return;if(a.startsWith("tab:")){route(a.slice(4));return}if(a.startsWith("shopfilter:")){shopFilter=a.slice(11);refreshPage()}else if(a.startsWith("wardrobefilter:")){wardrobeFilter=a.slice(15);refreshPage()}else if(a.startsWith("item:"))route("item",{itemKey:a.slice(5)});else if(a.startsWith("reward:"))route("reward",{rewardId:a.slice(7)});else if(a==="try-item")tryItem();else if(a.startsWith("qfilter:")){questFilter=a.slice(8);refreshPage()}else if(a.startsWith("efilter:")){exploreFilter=a.slice(8);refreshPage()}else if(a==="clan-join"){clanJoined=true;refreshPage()}else if(a==="clan-leave"){clanJoined=false;refreshPage()}else if(a==="clan-contribute"&&clanJoined){clanContribution=!clanContribution;refreshPage()}else if(a.startsWith("clan-panel:")){clanPanel=a.slice(11);refreshPage()}else if(a.startsWith("conversation:"))route("conversation",{personId:a.slice(13)});else if(a==="message-local")localMessage();else if(a.startsWith("home-book:"))route("home-book",{bookId:a.slice(10)});else if(a.startsWith("cheer:"))cheer(a.slice(6));else if(a.startsWith("look:"))setLook(a.slice(5));else if(a==="copy-home-book"){const b=homeBooks.find(x=>x.id===active.bookId);if(b)route("create",{bookId:b.id,goalInput:{title:b.title,steps:b.steps.join("\n")}})}else if(a.startsWith("goal:"))route("goal",{goalId:a.slice(5)});else if(a.startsWith("task:"))route("task",{taskId:a.slice(5)});else if(a.startsWith("toggle:"))toggleTask(a.slice(7));else if(a==="sample-plan"){const d=readGoalInput();if(d.title.trim()||d.steps.trim()){const e=$("plan-error");e.textContent="작성한 내용이 있어요. 비어 있을 때만 예시를 불러올 수 있어요.";e.hidden=false;return}$("goal-title").value=samplePlan.title;$("goal-steps").value=samplePlan.steps;$("plan-error").hidden=true}else if(a==="preview-plan")previewPlan();else if(a==="accept-plan")acceptPlan();else if(a==="back")back();else if(a==="sheet"||a==="menu")showSheet(b);else if(a==="notifications")$("notifications").showModal();else if(a==="confirm")$("confirm").showModal();else if(a==="snap-mid"||a==="snap-full")$("sheet").dataset.snap=a.slice(5);else if(fixtures[a])route(a)});
document.addEventListener("input",e=>{if(e.target.id==="explore-search"){exploreQuery=e.target.value;$("explore-results").innerHTML=exploreResults()}});
$("theme").addEventListener("change",e=>document.documentElement.dataset.theme=e.target.checked?"dark":"light");
$("sheet").addEventListener("close",()=>{if(lastTrigger?.isConnected)lastTrigger.focus({preventScroll:true})});
for(const d of document.querySelectorAll("dialog"))d.addEventListener("click",e=>{if(e.target!==d)return;const r=d.getBoundingClientRect();if(e.clientY<r.top||e.clientY>r.bottom||e.clientX<r.left||e.clientX>r.right)d.close()});
let start=null,moved=false;const grip=$("grip");grip.addEventListener("pointerdown",e=>{start=e.clientY;moved=false;grip.setPointerCapture(e.pointerId)});grip.addEventListener("pointerup",e=>{if(start===null)return;const dy=e.clientY-start;start=null;if(Math.abs(dy)>24){moved=true;snap(dy<0?1:-1)}});grip.addEventListener("pointercancel",()=>{start=null;moved=false});grip.addEventListener("click",()=>{if(!moved)snap(1);moved=false});grip.addEventListener("keydown",e=>{if(e.key==="ArrowUp"||e.key==="ArrowDown"){e.preventDefault();snap(e.key==="ArrowUp"?1:-1)}});
render();
