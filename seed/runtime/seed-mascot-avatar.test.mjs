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

test('asset bytes retain main Git blob identity for face and nine cosmetics', async()=>{
  const {readFileSync}=await import('node:fs');
  const {createHash}=await import('node:crypto');
  const sources={
    'mascot/mascot_face_stamp.png':'96c85bd1c12b5aba5795a19daec969970b7ec68d',
    'cosmetics/cosmetic_beanie.png':'c5921af83cd999315bafd2356f97ed0640740fa3',
    'cosmetics/cosmetic_cream_cap.png':'9e83179694aaa3f38d734a822668ef4a36a4952b',
    'cosmetics/cosmetic_blue_cap.png':'be5521c24cf7195b76b793d6dc2363e76edc2608',
    'cosmetics/cosmetic_charcoal_cap.png':'cf5de57bce5cf8eb0763c6815dc1e4e9e7cbb677',
    'cosmetics/cosmetic_backward_cap.png':'a62829b1dc81a86cff4bfa7bccc7e84bb41c2ac9',
    'cosmetics/cosmetic_bucket_hat.png':'ee58b1ba432ee7e409598d33ae6e92b6d6773d33',
    'cosmetics/cosmetic_cream_headphones.png':'a40d1934fbce902c4a6717392fafd9d170fb8003',
    'cosmetics/cosmetic_black_headphones.png':'81dffcf7f2127d1af63f1d4f21f6fd74d415cb89',
    'cosmetics/cosmetic_white_airpods_pair.png':'ed2b2aad60c374fec58d029b845c7bfa01f447a4'
  };
  for(const [path,sha] of Object.entries(sources)){
    const bytes=readFileSync(new URL('../../assets/art/'+path,import.meta.url));
    assert.equal(bytes.subarray(0,8).toString('hex'),'89504e470d0a1a0a',path);
    const gitBlobSha=createHash('sha1').update('blob '+bytes.length+'\0').update(bytes).digest('hex');
    assert.equal(gitBlobSha,sha,path);
  }
});
test('C-AVATAR owns appearance in shared SEED CSS, not Lab page overrides',async()=>{
  const {readFileSync}=await import('node:fs');
  const css=readFileSync(new URL('./seed-standard.css',import.meta.url),'utf8');
  const lab=readFileSync(new URL('../seed-lab/seed-visual.css',import.meta.url),'utf8');
  assert.match(css,/\.seed-mascot-avatar__layer \{[^}]*object-fit:contain/);
  assert.match(css,/\.seed-mascot-avatar\[data-scale="fluid"\]/);
  assert.match(css,/isolation:isolate/);
  assert.doesNotMatch(lab,/\.seed-mascot-avatar__layer/);
});
