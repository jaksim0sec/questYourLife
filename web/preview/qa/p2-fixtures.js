document.addEventListener('click',function(event){
  var t=event.target.closest('[data-fx-tab],[data-fx-view],[data-fx-filter],.fx-react');
  if(!t)return;
  if(t.hasAttribute('data-fx-tab')){
    var a=t.getAttribute('data-fx-tab');
    document.querySelectorAll('[data-fx-tab]').forEach(function(b){b.setAttribute('aria-selected',b.getAttribute('data-fx-tab')===a?'true':'false')});
    document.getElementById('fx-panel-a').hidden=a!=='a';document.getElementById('fx-panel-b').hidden=a!=='b';
  }
  if(t.hasAttribute('data-fx-view')){
    var v=t.getAttribute('data-fx-view');document.querySelectorAll('[data-fx-view]').forEach(function(b){b.setAttribute('aria-pressed',b.getAttribute('data-fx-view')===v?'true':'false')});
    document.getElementById('fx-view-value').textContent=v==='last'?'410점':'620점';
  }
  if(t.hasAttribute('data-fx-filter')){
    var f=t.getAttribute('data-fx-filter');document.querySelectorAll('[data-fx-filter]').forEach(function(b){b.setAttribute('aria-pressed',b.getAttribute('data-fx-filter')===f?'true':'false')});
    document.querySelectorAll('#fx-results [data-kind]').forEach(function(r){r.hidden=f!=='all'&&r.getAttribute('data-kind')!==f});
  }
  if(t.classList.contains('fx-react')){var on=t.getAttribute('aria-pressed')==='true';t.setAttribute('aria-pressed',on?'false':'true');t.textContent=on?'♡':'♥'}
});
