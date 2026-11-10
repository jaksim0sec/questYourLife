import test from 'node:test';
import assert from 'node:assert/strict';
import {MASCOT_EQUIPMENT, composeMascotAvatar, mountMascotAvatar} from './seed-mascot-avatar.mjs';

const face = '../../assets/art/mascot/mascot_face_stamp.png';
test('the identity face stays underneath individually prioritized equipment', () => {
  const layers = composeMascotAvatar([2,1003]);
  assert.deepEqual(layers.map(x=>x.itemId), [null,1003,2]);
  assert.equal(layers[0].src, face);
  assert.equal(layers[1].slot, 'earwear');
  assert.equal(layers[2].slot, 'headwear');
  assert.ok(layers[0].zIndex < layers[1].zIndex && layers[1].zIndex < layers[2].zIndex);
});
test('existing Android calibration values are used in the web source', () => {
  assert.deepEqual(MASCOT_EQUIPMENT[0].box, [.075,.055,.85,.612]);
  assert.deepEqual(MASCOT_EQUIPMENT[2].box, [.065,.015,.87,.582]);
  assert.deepEqual(MASCOT_EQUIPMENT[1001].box, [.030,.175,.94,.685]);
  assert.deepEqual(MASCOT_EQUIPMENT[1004].box, [.025,.480,.95,.360]);
  assert.equal(Object.keys(MASCOT_EQUIPMENT).length,9);
});
test('last item in an exclusive slot wins, unknown or duplicated items do not crash', () => {
  const a=composeMascotAvatar([0,1,1001,1003,1003,1002]);
  assert.deepEqual(a.map(x=>x.itemId),[null,1003,1]);
  assert.deepEqual(composeMascotAvatar([]).map(x=>x.itemId),[null]);
});
test('a caller can adjust a piece geometry and zIndex without changing the source catalog', () => {
  const prior=MASCOT_EQUIPMENT[2].box.slice();
  const layers=composeMascotAvatar([2,1003], {2:{x:-.08,y:0,width:.95,height:.6,zIndex:5}});
  assert.deepEqual(layers.map(x=>x.itemId),[2,null,1003]);
  assert.equal(layers[0].x,-.08);
  assert.equal(layers[0].width,.95);
  assert.deepEqual(MASCOT_EQUIPMENT[2].box,prior);
  assert.throws(()=>composeMascotAvatar([2],{2:{width:0}}),RangeError);
  assert.throws(()=>composeMascotAvatar('2'),TypeError);
});
test('DOM presentation uses fractional percent geometry and profile-image semantics',()=>{
  const nodes=[];
  const doc={createElement(tag){return {tagName:tag.toUpperCase(),attrs:{},style:{},dataset:{},setAttribute(k,v){this.attrs[k]=v;},addEventListener(){}}}};
  const root={ownerDocument:doc,classList:{add(x){this.name=x}},dataset:{},attrs:{},setAttribute(k,v){this.attrs[k]=v;},removeAttribute(k){delete this.attrs[k]},replaceChildren(...items){nodes.splice(0,nodes.length,...items)}};
  mountMascotAvatar(root,{equipmentIds:[2,1003],scale:'row',label:'민서의 프로필',presentation:'transparent'});
  assert.equal(root.classList.name,'seed-mascot-avatar');
  assert.equal(root.dataset.scale,'row');
  assert.equal(root.attrs.role,'img');
  assert.match(root.attrs['aria-label'],/민서의 프로필/);
  assert.equal(nodes.length,4); // 3 transparent layers + fallback
  assert.equal(nodes[0].style.left,'0%');
  assert.equal(nodes[0].style.top,'23.3%');
  assert.equal(nodes[2].style.left,'6.5%');
  assert.equal(nodes[2].style.width,'87%');
  assert.equal(nodes[2].style.zIndex,'45');
  assert.equal(nodes[0].attrs['aria-hidden'],'true');
  mountMascotAvatar(root,{equipmentIds:[0],scale:'hero',decorative:true});
  assert.equal(nodes.length,3);
  assert.equal(root.dataset.scale,'hero');
  assert.equal(root.attrs['aria-hidden'],'true');
  assert.equal(root.attrs.role,undefined);
});
