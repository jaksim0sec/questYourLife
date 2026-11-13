/**
 * SEED web page classification / navigation visibility contract.
 * Route class != S-PAGE Shell role. Bottom Sheet never replaces its host page.
 * Contents and destination URLs belong to the consuming application router.
 */
export const PAGE_TYPES=Object.freeze({
  "global":Object.freeze({header:"root",bottomNav:true,overlay:false}),
  "global-inner":Object.freeze({header:"standard",bottomNav:true,overlay:false}),
  "non-global":Object.freeze({header:"standard",bottomNav:false,overlay:false}),
  "bottom-sheet":Object.freeze({header:"sheet",bottomNav:false,overlay:true})
});
/* The five web navigation destinations remain unchanged. Clan mark is a
 * preview-only eight-ray crest in the familiar bookmark silhouette, keeping
 * the approved Soft 56 icon sources untouched. Product IA remains O-003. */
export const WEB_NAV_ITEMS=Object.freeze([
  Object.freeze({key:"home",label:"홈",outline:"home",active:"home-fill"}),
  Object.freeze({key:"quests",label:"퀘스트",outline:"check-circle",active:"check-circle-fill"}),
  Object.freeze({key:"dm",label:"디엠",outline:"chat",active:"chat-fill"}),
  Object.freeze({key:"clan",label:"클랜",outline:"clan-crest-outline",active:"clan-crest-fill",outlineBase:"../../assets/icons/preview/",activeBase:"../../assets/icons/preview/"}),
  Object.freeze({key:"me",label:"마이",outline:"me-smile",active:"me-smile-fill",outlineBase:"../../assets/icons/preview/",activeBase:"../../assets/icons/preview/"})
]);
export function getPageType(type){
  if(!Object.hasOwn(PAGE_TYPES,type))throw new RangeError("Unknown SEED page type: "+String(type));
  return PAGE_TYPES[type];
}
/**
 * Applies only route-page state. For a sheet call the existing C-SHEET dialog
 * on top of the parent; do not change its type, selected destination, or scroll.
 */
export function applyPageType(shell,type,{fixedCta=false}={}){
  const state=getPageType(type);
  if(state.overlay)throw new TypeError("bottom-sheet is an overlay, not a replacement route");
  if(!shell)throw new TypeError("A SEED page shell is required");
  shell.dataset.pageType=type;
  const header=shell.querySelector(".seed-top-nav");
  const nav=shell.querySelector(".seed-bottom-nav");
  const bottom=shell.querySelector(".seed-page-shell__bottom");
  if(header)header.dataset.navRole=state.header;
  if(nav)nav.hidden=!state.bottomNav;
  if(bottom)bottom.hidden=!(state.bottomNav||fixedCta);
  return state;
}
export function selectWebNav(nav,key,iconBase="../../assets/icons/soft/v0.14/"){
  const chosen=WEB_NAV_ITEMS.find(x=>x.key===key);
  if(!chosen)throw new RangeError("Unknown SEED destination: "+String(key));
  if(!nav)throw new TypeError("A bottom navigation element is required");
  for(const link of nav.querySelectorAll(".seed-bottom-nav__link[data-nav-key]")){
    const item=WEB_NAV_ITEMS.find(x=>x.key===link.dataset.navKey);
    if(!item)continue;
    const active=item.key===key;
    if(active)link.setAttribute("aria-current","page");else link.removeAttribute("aria-current");
    const icon=link.querySelector(".seed-icon");
    if(icon){
      const base=active?(item.activeBase||iconBase):(item.outlineBase||iconBase);
      icon.style.setProperty("--seed-icon",'url("'+base+(active?item.active:item.outline)+'.svg")');
    }
  }
  return chosen;
}
