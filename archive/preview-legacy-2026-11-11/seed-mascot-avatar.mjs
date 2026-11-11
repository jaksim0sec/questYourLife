/** C-AVATAR: normalized placements traced to main/AvatarComposition.kt. */
const FACE = Object.freeze({itemId:null,slot:'identity',name:'마스코트',src:'../../assets/art/mascot/mascot_face_stamp.png',box:Object.freeze([0,.233,1,.734]),zIndex:10});
const equipment = [
  [0,'골지 비니','headwear','cosmetic_beanie.png',[.075,.055,.85,.612],40],
  [1,'크림 캡','headwear','cosmetic_cream_cap.png',[.045,.060,.91,.582],44],
  [2,'블루 캡','headwear','cosmetic_blue_cap.png',[.065,.015,.87,.582],45],
  [3,'차콜 캡','headwear','cosmetic_charcoal_cap.png',[.045,.060,.91,.582],43],
  [4,'백워드 캡','headwear','cosmetic_backward_cap.png',[.105,-.020,.78,.612],42],
  [5,'버킷 햇','headwear','cosmetic_bucket_hat.png',[.0675,.012,.865,.656],46],
  [1001,'크림 헤드폰','earwear','cosmetic_cream_headphones.png',[.030,.175,.94,.685],20],
  [1003,'블랙 헤드폰','earwear','cosmetic_black_headphones.png',[.030,.175,.94,.685],21],
  [1004,'화이트 이어버드','earwear','cosmetic_white_airpods_pair.png',[.025,.480,.95,.360],32]
];
export const MASCOT_EQUIPMENT = Object.freeze(Object.fromEntries(equipment.map(([id,name,slot,file,box,zIndex])=>[
  id,Object.freeze({id,name,slot,src:'../../assets/art/cosmetics/'+file,box:Object.freeze(box),zIndex})
])));

/** Same coordinates at 40px or 240px. Only items in distinct slots coexist. */
export function composeMascotAvatar(equipmentIds=[],placementById={}) {
  if (!Array.isArray(equipmentIds)) throw new TypeError('equipmentIds must be an array');
  const slots=new Map();
  for(const id of equipmentIds){
    const item=MASCOT_EQUIPMENT[id];
    if(item)slots.set(item.slot,item); // last equipped item in a slot wins
  }
  const all=[FACE,...slots.values()];
  return all.map((item,index)=>{
    const itemId=item.id??item.itemId??null;
    const own=itemId===null?{}:(placementById[itemId]||{});
    const [baseX,baseY,baseW,baseH]=item.box;
    const x=own.x??baseX,y=own.y??baseY,width=own.width??baseW,height=own.height??baseH,zIndex=own.zIndex??item.zIndex;
    if(![x,y,width,height,zIndex].every(Number.isFinite)||width<=0||height<=0||!Number.isInteger(zIndex)) throw new RangeError('Invalid avatar layer placement');
    return {itemId,slot:item.slot,name:item.name,src:item.src,x,y,width,height,zIndex,order:index};
  }).sort((a,b)=>a.zIndex-b.zIndex||a.order-b.order);
}

const percent=value=>String(+(value*100).toFixed(4))+'%';
/** Reusable image identity. The parent controls size; layers always use percentages. */
export function mountMascotAvatar(element,{equipmentIds=[],placementById={},scale='profile',presentation='transparent',label='마스코트 아바타',decorative=false}={}) {
  if(!element?.ownerDocument?.createElement||!element.replaceChildren)throw new TypeError('Avatar mount element required');
  if(!['row','profile','hero','fluid'].includes(scale))throw new RangeError('Unknown avatar scale');
  if(!['transparent','framed'].includes(presentation))throw new RangeError('Unknown avatar presentation');
  const layers=composeMascotAvatar(equipmentIds,placementById);
  element.classList.add('seed-mascot-avatar');
  element.dataset.scale=scale;
  element.dataset.presentation=presentation;
  element.dataset.state='loaded';
  if(decorative){element.removeAttribute('role');element.removeAttribute('aria-label');element.setAttribute('aria-hidden','true');}
  else {
    element.removeAttribute('aria-hidden');element.setAttribute('role','img');
    element.setAttribute('aria-label',label);
  }
  const doc=element.ownerDocument;
  const images=layers.map(layer=>{
    const image=doc.createElement('img');
    image.className='seed-mascot-avatar__layer';
    image.src=layer.src;
    image.alt=''; image.setAttribute('aria-hidden','true');
    image.dataset.itemId=layer.itemId===null?'face':String(layer.itemId);
    image.style.left=percent(layer.x);image.style.top=percent(layer.y);
    image.style.width=percent(layer.width);image.style.height=percent(layer.height);
    image.style.zIndex=String(layer.zIndex);
    image.addEventListener('error',()=>{
      image.hidden=true;
      if(layer.itemId===null)element.dataset.state='error';
    });
    return image;
  });
  const fallback=doc.createElement('span');fallback.className='seed-mascot-avatar__fallback';
  fallback.textContent='마스코트';fallback.setAttribute('aria-hidden','true');
  element.replaceChildren(...images,fallback);
  return layers;
}
