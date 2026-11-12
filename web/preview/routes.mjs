import {WEB_NAV_ITEMS} from '../../seed/runtime/seed-page-contract.mjs';
import {threads} from './mock-data.mjs?v=dm-chat-v3';

const definitions={
  home:{type:'global',title:'홈',tab:'home'},
  quests:{type:'global',title:'퀘스트',tab:'quests'},
  dm:{type:'global',title:'디엠',tab:'dm'},
  clan:{type:'global',title:'클랜',tab:'clan'},
  me:{type:'global',title:'마이',tab:'me'},
  quest:{type:'global-inner',title:'퀘스트 상세',tab:'quests',parentTabs:['home','quests','dm'],hasId:true},
  book:{type:'global-inner',title:'퀘스트북',tab:'quests',parentTabs:['quests','home'],hasId:true},
  search:{type:'non-global',title:'검색',tab:'home',parentTabs:['home','quests']},
  ranking:{type:'global-inner',title:'주간 기여',tab:'clan'},
  shop:{type:'global-inner',title:'상점',tab:'me',align:'start'},
  chat:{type:'non-global',title:'대화방',tab:'dm',parentTabs:['dm','clan','home','quests'],hasId:true},
  item:{type:'non-global',title:'상품 상세',tab:'me',hasId:true,fallback:'shop'},
  decorate:{type:'non-global',title:'꾸미기',tab:'me'},
  rewards:{type:'non-global',title:'보상함',tab:'me',parentTabs:['me','clan','home']},
};

export function readRoute(hash) {
  const [pathname,query]=hash.replace(/^#\/?/,'').split('?');
  const [name='home',id,extra]=pathname?pathname.split('/'):['home'];
  if(!Object.hasOwn(definitions,name)||extra!==undefined||Boolean(id)!==Boolean(definitions[name].hasId)) {
    return {...definitions.home,name:'home',id:undefined};
  }
  const def=definitions[name];
  const from=new URLSearchParams(query).get('tab');
  const tab=def.parentTabs?.includes(from)?from:def.tab;
  const title=name==='chat'&&Object.hasOwn(threads,id)?threads[id].title:def.title;
  return {...def,name,id,tab,title};
}

export function navMarkup() {
  return WEB_NAV_ITEMS.map(item=>`<a class="seed-bottom-nav__link" data-nav-key="${item.key}" href="#/${item.key}"><span class="seed-icon" aria-hidden="true"></span><span>${item.label}</span></a>`).join('');
}
