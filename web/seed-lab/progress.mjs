/** Numeric fixture normalization. Never implies payment or completion on the server. */
export function normalizeProgress(value,max,{loading=false}={}) {
  if (loading) return {state:'loading',value:null,max:null,percent:null};
  if (typeof value !== 'number' || !Number.isFinite(value) || value < 0 ||
      typeof max !== 'number' || !Number.isFinite(max) || max <= 0)
    return {state:'unavailable',value:null,max:null,percent:null};
  const percent=Math.min(100,Math.max(0,value/max*100));
  return {state:value>max?'over':value===max?'full':value===0?'zero':'partial',value,max,percent};
}
