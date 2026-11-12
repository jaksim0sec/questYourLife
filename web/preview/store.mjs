import {catalog, chestLoot, clan, homeAlerts, initialData, proposeQuest} from './mock-data.mjs?v=me-open-v2';
import {MASCOT_EQUIPMENT} from '../../seed/runtime/seed-mascot-avatar.mjs';

const key='hyunsaengpatch-preview-v1';

export function clanRows(state, period='current') {
  if (period==='previous') return clan.previous;
  const gained=state.quests.filter(q=>q.status==='done'&&!q.baseline).reduce((xp,q)=>xp+q.xp,0);
  return clan.current.map(row=>row.name==='루원'?{...row,xp:row.xp+gained}:row).sort((a,b)=>b.xp-a.xp);
}

export function clanTotal(state, period) {
  return clanRows(state,period).reduce((total,row)=>total+row.xp,0);
}

export function createStore(storage) {
  let state=initialData();
  try {
    const saved=storage?.getItem(key);
    if(saved) {
      const parsed=JSON.parse(saved);
      if(Array.isArray(parsed.quests)&&Array.isArray(parsed.equipped)&&parsed.messages?.ai&&parsed.boxes&&Array.isArray(parsed.bonusPosts)&&Number.isFinite(parsed.cash)) state=parsed;
    }
  } catch {}
  function commit(next) {
    state=next;
    try { storage?.setItem(key,JSON.stringify(state)); } catch {}
  }
  function dispatch(action,id,value) {
    const next=structuredClone(state);
    const quest=next.quests.find(q=>q.id===id);
    switch(action) {
      case 'accept':
      case 'reject':
        if(quest?.status!=='offered') return false;
        quest.status=action==='accept'?'active':'rejected';
        break;
      case 'defer':
      case 'resume':
        if(quest?.status!==(action==='defer'?'active':'deferred')) return false;
        quest.status=action==='defer'?'deferred':'active';
        break;
      case 'complete':
        if(quest?.status!=='active') return false;
        if(quest.kind==='보너스') {
          if(!value?.trim()) return false;
          next.bonusPosts.push({name:'루원',text:value.trim().slice(0,160),count:0,cheered:false});
        }
        quest.status='done';
        next.xp+=quest.xp;
        next.cash+=quest.cash;
        break;
      case 'undo':
        if(quest?.status!=='done'||quest.baseline||quest.kind==='보너스'||next.cash<quest.cash) return false;
        quest.status='active';
        next.xp-=quest.xp;
        next.cash-=quest.cash;
        break;
      case 'message': {
        const message=value?.trim().slice(0,300);
        if(!message||!Object.hasOwn(next.messages,id)) return false;
        next.messages[id].push({from:'me',text:message});
        if(id==='ai') {
          const old=next.quests.findLast(q=>q.id.startsWith('ai-')&&q.status==='offered');
          if(old) old.status='rejected';
          const generated=proposeQuest(message,'ai-'+next.messages.ai.length,old?.title);
          next.quests.unshift(generated);
          next.messages.ai.push({from:'them',text:'이렇게 해보는 건 어때?',quest:generated.id});
        }
        break;
      }
      case 'claim':
        if(id!=='clan'||next.boxes.clan!=='locked'||clanTotal(next)<clan.goal) return false;
        next.boxes.clan='ready';
        break;
      case 'open': {
        const loot=chestLoot[id];
        if(!loot||next.boxes[id]!=='ready') return false;
        next.boxes[id]='opened';
        next.cash+=loot.cash;
        if(!next.owned.includes(loot.item)) next.owned.push(loot.item);
        break;
      }
      case 'purchase': {
        const item=catalog.find(item=>item.id===Number(id));
        if(!item||next.owned.includes(item.id)||next.cash<item.price) return false;
        next.cash-=item.price;
        next.owned.push(item.id);
        break;
      }
      case 'equip': {
        const item=MASCOT_EQUIPMENT[Number(id)];
        if(!item||!next.owned.includes(item.id)) return false;
        const wasEquipped=next.equipped.includes(item.id);
        next.equipped=next.equipped.filter(other=>MASCOT_EQUIPMENT[other]?.slot!==item.slot);
        if(!wasEquipped) next.equipped.push(item.id);
        break;
      }
      case 'read-alert':
        if(!homeAlerts.some(alert=>alert.id===id)||(next.readAlerts??[]).includes(id)) return false;
        next.readAlerts=[...(next.readAlerts??[]),id];
        break;
      case 'cheer': {
        const post=next.bonusPosts[Number(id)];
        if(!post||post.name==='루원') return false;
        post.cheered=!post.cheered;
        post.count+=post.cheered?1:-1;
        break;
      }
      default: return false;
    }
    commit(next);
    return true;
  }
  return {read:()=>state,dispatch};
}
