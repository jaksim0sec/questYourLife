import test from 'node:test';
import assert from 'node:assert/strict';
import {normalizeProgress} from './progress.mjs';

test('same data controls value and fill width',()=>{
  assert.deepEqual(normalizeProgress(620,1000),{state:'partial',value:620,max:1000,percent:62});
});
test('over-contribution preserves actual value and caps fill',()=>{
  const p=normalizeProgress(1200,1000);
  assert.equal(p.value,1200);assert.equal(p.percent,100);assert.equal(p.state,'over');
});
test('zero, unavailable, and loading never collapse into a success state',()=>{
  assert.equal(normalizeProgress(0,1000).state,'zero');
  assert.equal(normalizeProgress(null,1000).state,'unavailable');
  assert.equal(normalizeProgress(0,0).state,'unavailable');
  assert.equal(normalizeProgress(0,1000,{loading:true}).state,'loading');
});
test('invalid input cannot create a fake percentage',()=>{
  for(const value of [NaN,Infinity,-2,'620']) assert.equal(normalizeProgress(value,1000).state,'unavailable');
  assert.equal(normalizeProgress(101,100).state,'over');
});
