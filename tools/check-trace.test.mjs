import test from 'node:test';
import assert from 'node:assert/strict';
import {validate,load} from './check-trace.mjs';
import {fileURLToPath} from 'node:url';
const baseline=()=>load(fileURLToPath(new URL('../',import.meta.url)));
// Independent fixtures: archiving a real task must never break negative-case tests.
const fixture=()=>{
  const f=baseline();
  let index=f.get('docs/tasks/INDEX.md');
  for(const [id,scope] of [['TEST-900',['__trace_fixture__/a']],['TEST-901',['__trace_fixture__/b']]]){
    const detail='docs/tasks/active/'+id+'.md';
    f.set(detail,'## T1 Fixture\n');
    f.set(detail.replace(/\.md$/,'.json'),JSON.stringify({id,status:'in_progress',baseline:'0'.repeat(40),detail,scope,contracts:['AGENTS.md'],dependencies:[],next:id+'/T1'}));
    index+='\n[detail](active/'+id+'.md) [registration](active/'+id+'.json)';
  }
  f.set('docs/tasks/INDEX.md',index);
  return f;
};
test('valid repository',()=>assert.deepEqual(validate(baseline()),[]));
test('missing router rejected',()=>{const f=baseline();f.delete('AGENTS.md');assert.ok(validate(f).some(e=>e.includes('missing AGENTS.md')))});
test('missing task detail rejected',()=>{const f=fixture();f.delete('docs/tasks/active/TEST-900.md');assert.ok(validate(f).some(e=>e.includes('invalid detail')))});
test('overlapping active scopes rejected',()=>{const f=fixture();const p='docs/tasks/active/TEST-900.json';const t=JSON.parse(f.get(p));t.scope=['__trace_fixture__/b'];f.set(p,JSON.stringify(t));assert.ok(validate(f).some(e=>e.includes('scope overlap')))});
test('invalid next rejected',()=>{const f=fixture();const p='docs/tasks/active/TEST-900.json';const t=JSON.parse(f.get(p));t.next='TEST-900/T99';f.set(p,JSON.stringify(t));assert.ok(validate(f).some(e=>e.includes('invalid next')))});
