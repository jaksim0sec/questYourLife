/* SEED Mirror: the indexed source files, not specimen markup, own the definitions. */
const $=(s,p=document)=>p.querySelector(s);
const $$=(s,p=document)=>Array.from(p.querySelectorAll(s));
const mapRoot=$("#seed-map-panel");
if(mapRoot){
const els={list:$("#seed-source-list"),search:$("#seed-source-search"),category:$("#seed-source-category"),count:$("#seed-source-count"),title:$("#seed-doc-title"),status:$("#seed-doc-status"),meta:$("#seed-doc-meta"),body:$("#seed-doc-body"),raw:$("#seed-doc-raw"),outline:$("#seed-doc-outline"),link:$("#seed-doc-link"),summary:$("#seed-map-summary"),notice:$("#seed-doc-notice")};
const kinds={foundation:"Foundation",component:"Component",pattern:"Pattern",shell:"Shell",asset:"Asset",implementation:"Implementation",governance:"Governance",reference:"Reference"};
const statuses={approved:"승인",proposed:"제안 / 미확정","needs-validation":"검증 필요","source-dependent":"정본 확인"};
let items=[],commit="",active="",request=0;
const el=(type,value,cls)=>{const node=document.createElement(type);node.textContent=value??"";if(cls)node.className=cls;return node;};
const github=path=>"https://github.com/jaksim0sec/questYourLife/blob/"+commit+"/"+path;
function readMeta(s){
 if(!s.startsWith("---\n"))return{body:s,meta:{}};
 const at=s.indexOf("\n---",4);if(at<0)return{body:s,meta:{}};
 const meta={};for(const line of s.slice(4,at).split("\n")){const k=line.indexOf(":");if(k>0)meta[line.slice(0,k)]=line.slice(k+1).trim();}
 return{meta,body:s.slice(at+4).replace(/^\s*\n/,"")};
}
function addText(parent,s){
 const pieces=s.split(/(\*\*[^*]+\*\*|\x60[^\x60]+\x60)/);
 for(const p of pieces){
  if(p.startsWith("**")&&p.endsWith("**"))parent.append(el("strong",p.slice(2,-2)));
  else if(p.startsWith("\x60")&&p.endsWith("\x60"))parent.append(el("code",p.slice(1,-1)));
  else parent.append(document.createTextNode(p));
 }
}
function documentView(source){
 const fragment=document.createDocumentFragment(),headings=[],lines=source.split(/\r?\n/);
 let paragraph=[],list=null,code=null,table=null;
 function flush(){if(paragraph.length){const p=el("p","");addText(p,paragraph.join(" "));fragment.append(p);paragraph=[];}}
 for(const line of lines){
  const x=line.trim();
  if(x.startsWith("\x60\x60\x60")){flush();list=null;table=null;if(code){fragment.append(code);code=null;}else{code=el("pre","","lab-map-code");}continue;}
  if(code){code.textContent+=line+"\n";continue;}
  if(!x){flush();list=null;table=null;continue;}
  const h=x.match(/^(#{1,5})\s+(.*)$/);
  if(h){flush();list=null;table=null;const node=el("h"+Math.max(2,Math.min(4,h[1].length)),h[2]);node.id="seed-doc-heading-"+headings.length;headings.push({id:node.id,label:h[2]});fragment.append(node);continue;}
  if(/^\|.+\|\s*$/.test(x)){
    flush();list=null;if(!table){const wrap=el("div","","lab-map-table-scroll");table=el("table","","lab-map-table");wrap.append(table);fragment.append(wrap);}
    const cells=x.replace(/^\|/,"").replace(/\|\s*$/,"").split(/\s*\|\s*/);
    if(cells.every(t=>/^:?-{3,}:?$/.test(t)))continue;
    const row=el("tr",""),head=table.children.length===0;for(const cell of cells){const td=el(head?"th":"td","");addText(td,cell);row.append(td);}table.append(row);continue;
  }
  table=null;
  const li=x.match(/^([-*+]|\d+\.)\s+(.*)$/);
  if(li){flush();const tag=/^\d/.test(li[1])?"ol":"ul";if(!list||list.tagName.toLowerCase()!==tag){list=el(tag,"");fragment.append(list);}const element=el("li","");addText(element,li[2]);list.append(element);continue;}
  list=null;
  if(x.startsWith(">")){flush();const q=el("blockquote","");addText(q,x.replace(/^>\s?/,""));fragment.append(q);continue;}
  if(x==="---"){flush();fragment.append(el("hr",""));continue;}
  paragraph.push(x);
 }
 flush();if(code)fragment.append(code);
 return{fragment,headings};
}
function showRefs(ids,host){
 const unique=[...new Set(ids)];
 for(const id of unique){if(!items.some(x=>x.id===id))continue;const b=el("button",id,"lab-map-ref");b.type="button";b.addEventListener("click",()=>show(id,true));host.append(b);}
}
function drawList(){
 const q=els.search.value.toLocaleLowerCase("ko").trim(),kind=els.category.value;
 const rows=items.filter(x=>(kind==="all"||x.category===kind)&&(!q||[x.id,x.name,x.readWhen,x.depends,x.path].join(" ").toLocaleLowerCase("ko").includes(q)));
 els.count.textContent=rows.length+" / "+items.length+"개 문서";
 els.list.replaceChildren();
 for(const item of rows){
  const b=el("button",item.id+" · "+item.name,"lab-map-source-item");b.type="button";b.setAttribute("aria-current",String(active===item.id));
  b.append(el("small",(kinds[item.category]||item.category)+" · "+(statuses[item.status]||item.status)));
  b.addEventListener("click",()=>show(item.id,true));els.list.append(b);
 }
 if(!rows.length)els.list.append(el("p","검색 결과 없음","lab-map-empty"));
}
function drawMeta(item,front){
 els.meta.replaceChildren();
 const rows=[["ID",item.id],["계열",kinds[item.category]||item.category],["사용 시점",item.readWhen],["문서 상태",front.status||item.status],["의존성",item.depends||front.depends_on||"없음"],["원본",item.path]];
 for(const [term,value] of rows){els.meta.append(el("dt",term),el("dd",value));}
 const depends=(item.depends||front.depends_on||"").match(/\b(?:F|C|P|S|A|NAV|V)-[A-Z0-9-]+\b/g)||[];
 if(depends.length){const wrap=el("div","연결 문서 ","lab-map-deps");showRefs(depends,wrap);els.meta.append(wrap);}
}
async function show(id,navigate=false){
 const item=items.find(x=>x.id===id);if(!item)return;
 active=id;drawList();els.title.textContent=item.name;
 els.status.textContent=(statuses[item.status]||item.status)+" · INDEX 표기";
 els.link.href=github(item.path);els.raw.textContent="원문 불러오는 중";els.outline.replaceChildren();
 els.body.replaceChildren(el("p","GitHub 정본을 읽고 있습니다.","lab-map-empty"));
 drawMeta(item,{});
 els.notice.textContent="문서의 status와 개별 조항의 승인 범위는 다릅니다. D-011/D-012/D-013 및 v0.15를 함께 확인해야 합니다.";
 if(navigate){$("#tab-map").click();const u=new URL(location.href);u.searchParams.set("panel","map");u.searchParams.set("spec",id);history.replaceState(null,"",u.pathname+u.search+u.hash);mapRoot.scrollIntoView({block:"start",behavior:"smooth"});}
 const serial=++request;
 try{
  const path="https://raw.githubusercontent.com/jaksim0sec/questYourLife/"+commit+"/"+item.path;
  const res=await fetch(path,{cache:"no-cache"});if(!res.ok)throw Error("HTTP "+res.status);
  const text=await res.text();if(serial!==request)return;
  els.raw.textContent=text;
  const parsed=readMeta(text);drawMeta(item,parsed.meta);
  const result=(item.path.endsWith(".css")||item.path.endsWith(".json"))?{fragment:el("pre",text,"lab-map-code"),headings:[]}:documentView(parsed.body);
  els.body.replaceChildren(result.fragment);
  for(const h of result.headings.slice(0,30)){const a=el("a",h.label);a.href="#"+h.id;a.addEventListener("click",e=>{e.preventDefault();document.getElementById(h.id)?.scrollIntoView({block:"start",behavior:"smooth"});});els.outline.append(a);}
  els.status.textContent=(statuses[parsed.meta.status||item.status]||parsed.meta.status||item.status)+" · 원문 조회";
 }catch(error){
  if(serial!==request)return;
  els.body.replaceChildren(el("p","원문을 불러올 수 없습니다. GitHub 정본 링크로 확인하세요.","lab-map-empty"));
  els.raw.textContent="원문 조회 실패: "+error.message;
  els.notice.textContent="원문 조회에 실패했으므로 이 항목의 내용을 검증했다고 표시하지 않습니다.";
 }
}
function wireExamples(){
 const anchors=[
 ["#typography",["C-TEXT","F-TYPE"]],["#actions",["C-ACTION","P-ACTIONS"]],
 ["#status",["C-STATUS"]],["#inputs",["C-FIELD","C-CHECK","C-RADIO"]],
 ["#lists",["C-LIST","C-IMAGE"]],["#surfaces",["C-SURFACE","C-ICON","C-SVG"]],
 ["#panel-layout .lab-section-head",["P-LAYOUT","P-PAGE-COMPOSE"]],
 ["#panel-state .lab-section-head",["C-SHEET","P-SHEET","P-STATES"]],
 ["#panel-assets .lab-section-head",["A-ART","C-IMAGE","C-SVG"]]
 ];
 for(const [target,ids] of anchors){const node=$(target);if(!node)continue;const links=el("div","정본: ","lab-map-demo-links");showRefs(ids,links);node.append(links);}
 for(const note of $$(".lab-section-head .lab-overline")){
  const section=note.closest(".lab-section-head");if(!section||section.querySelector(".lab-map-demo-links"))continue;
  const ids=note.textContent.match(/\b(?:F|C|P|S|A)-[A-Z0-9-]+\b/g)||[];
  if(!ids.length)continue;const links=el("div","정본: ","lab-map-demo-links");showRefs(ids,links);section.append(links);
 }
}
els.search.addEventListener("input",drawList);els.category.addEventListener("change",drawList);
(async()=>{
 try{
  const res=await fetch("./seed-sources.json",{cache:"no-cache"});
  if(!res.ok)throw Error("INDEX "+res.status);
  const data=await res.json();if(data.schema!=="seed-source-index-v1"||data.items.length<38)throw Error("잘못된 INDEX");
  items=data.items;commit=data.sourceCommit;
  els.summary.textContent="INDEX 정본 "+items.filter(x=>x.fromIndex).length+"개 + 관련 계약 "+items.filter(x=>!x.fromIndex).length+"개 · 기준 커밋 "+commit.slice(0,7);
  for(const [key,label] of Object.entries(kinds)){const option=el("option",label+" · "+items.filter(x=>x.category===key).length);option.value=key;els.category.append(option);}
  wireExamples();drawList();
  const preferred=new URLSearchParams(location.search).get("spec");
  await show(items.some(x=>x.id===preferred)?preferred:"SEED-INDEX");
 }catch(error){els.summary.textContent="SEED INDEX 조회 실패: "+error.message;els.list.replaceChildren(el("p","문서 색인 로드 실패. GitHub의 docs/design/seed/INDEX.md를 확인하세요."));}
})();
}