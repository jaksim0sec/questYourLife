export const art = '../../assets/art/';
export const rewards = art + 'rewards/';

export const catalog = [
  { id:1, name:'크림 캡', type:'모자', price:480, description:'차분한 크림색의 데일리 볼캡.' },
  { id:3, name:'차콜 캡', type:'모자', price:360 },
  { id:4, name:'백워드 캡', type:'모자', price:420 },
  { id:5, name:'버킷 햇', type:'모자', price:520 },
  { id:1001, name:'크림 헤드폰', type:'오디오', price:680 },
  { id:1003, name:'블랙 헤드폰', type:'오디오', price:620 },
  { id:1004, name:'화이트 이어버드', type:'오디오', price:320 },
];

export const books = [{
  id:'math', title:'수학 루틴 만들기', goal:'방과 후에 스스로 20분씩 문제를 풀 수 있는 습관 만들기.',
  chapter:'1장 · 매일 같은 시간에 시작하기', quests:['math-main','math-today','math-review'],
}];

// Curated prototype discovery metrics, not measured service analytics.
export const homeTrends = [
  {
    "kind": "퀘스트북",
    "title": "수학 루틴 만들기",
    "description": "매일 같은 시간에 시작하는 20분 습관",
    "metric": "1,284명 참여",
    "signal": "주간 인기",
    "icon": "book",
    "url": "book/math"
  },
  {
    "kind": "일일 도전",
    "title": "달리기 기록 갱신",
    "description": "어제의 기록에 도전하기",
    "metric": "428명 도전",
    "signal": "상승 중",
    "icon": "target",
    "url": "quest/run"
  },
  {
    "kind": "보너스",
    "title": "주황색 거울 찾기",
    "description": "지금 열려 있는 한정 도전",
    "metric": "316명 참여",
    "signal": "기간 한정",
    "icon": "sparkle",
    "url": "quest/bonus"
  }
];

export const threads = {
  ai:{ title:'퀘스트 만들기', icon:'sparkle', avatarEquipment:[] },
  friend:{ title:'다온', initials:'다', icon:'user', avatarEquipment:[0,1004] },
  clan:{ title:'방과후 원정대', initials:'방', icon:'flag' },
  friend2:{ title:'별이', initials:'별', icon:'user', avatarEquipment:[5] },
  friend3:{ title:'민재', initials:'민', icon:'user', avatarEquipment:[3,1003] },
};

export const homeAlerts = [
  {id:'daily', title:'새로운 일일 제안이 왔어요', sub:'달리기 기록 0.5초 줄이기', url:'quest/run', icon:'target'},
  {id:'bonus', title:'기간 한정 보너스가 열렸어요', sub:'동네에서 주황색 거울 찾기', url:'quest/bonus', icon:'sparkle'},
  {id:'starter', title:'첫 도전 상자가 도착했어요', sub:'캐시와 장식을 확인해 보세요', url:'rewards', icon:'gift'},
];

export const clan = {
  name:'방과후 원정대', goal:1200,
  current:[{name:'다온',xp:410},{name:'별이',xp:280},{name:'루원',xp:230},{name:'이모리',xp:200}],
  previous:[{name:'이모리',xp:410},{name:'루원',xp:320},{name:'다온',xp:280},{name:'별이',xp:220}],
};

export const chestLoot = {
  starter:{ name:'첫 도전 상자', image:'06_wooden_chest.png', cash:120, item:1004 },
  clan:{ name:'주간 클랜 상자', image:'08_premium_glowing_chest.png', cash:260, item:5 },
};

export function initialData() {
  return {
    xp:730, cash:640, owned:[0,2], equipped:[2], readAlerts:[], mutedThreads:{},
    boxes:{ starter:'ready', clan:'locked' },
    quests:[
      {id:'run', kind:'일일', title:'달리기 기록 0.5초 줄이기', condition:'어제와 같은 구간을 달리고, 기록을 어제보다 0.5초 이상 줄이기.', due:'오늘 23:59', xp:80, cash:40, status:'offered'},
      {id:'math-today', kind:'일일', title:'수학 오답 3문제 다시 풀기', condition:'어제 틀린 문제 중 3개를 해설 없이 풀고, 틀린 이유를 한 줄씩 적기.', due:'오늘 23:59', xp:60, cash:30, status:'active', book:'math'},
      {id:'math-main', kind:'메인', title:'방과 후 수학 20분 루틴 시작', condition:'내가 지속할 수 있는 시간과 장소를 정하고, 그 자리에서 첫 20분 공부를 마치기.', due:'이번 주 일요일', xp:100, cash:50, status:'active', book:'math'},
      {id:'math-review', kind:'일일', title:'어제 배운 개념 한 줄 정리', condition:'노트를 덮고 기억나는 개념을 한 문장으로 적기.', due:'어제 완료', xp:30, cash:15, status:'done', book:'math', baseline:true},
      {id:'bonus', kind:'보너스', title:'동네에서 주황색 거울 찾기', condition:'안전하게 다닐 수 있는 곳에서 주황색 거울을 찾고, 어디에서 봤는지 짧게 기록하기.', due:'오늘 21:00', xp:0, cash:80, status:'offered'},
    ],
    messages:{
      ai:[{from:'them',text:'어떤 목표를 이루고 싶어? 지금 할 수 있는 것부터 퀘스트로 만들어 줄게.'}],
      friend:[{"from":"them","text":"오늘 오답 정리 같이 할래? 나도 세 문제만 먼저 풀어보려고."},{"from":"me","text":"좋아 나도 세 문제만 풀어볼게"},{"from":"them","text":"지금 몇 번부터 하는 중?"},{"from":"me","text":"어제 틀렸던 문제부터 다시 보려고"},{"from":"them","text":"그럼 각자 풀고 어려운 것만 얘기하자"},{"from":"me","text":"오케이 시작!"}],
      clan:[{"from":"them","senderId":"friend","text":"주간 상자까지 이제 얼마 안 남았어. 오늘 각자 도전 하나씩 해보자!"},{"from":"them","senderId":"friend2","text":"ㅇㅇ 나 산책 보너스 도전 보고 왔어"},{"from":"them","senderId":"friend2","text":"주황색 거울 찾는 거 생각보다 어렵더라"},{"from":"me","text":"난 먼저 수학 오답 정리부터 할게"},{"from":"them","senderId":"friend","text":"좋네 나도 방과 후에 수학 루틴 체크할 예정"},{"from":"them","senderId":"friend2","text":"끝나면 우리 여기서 결과 공유하자"},{"from":"me","text":"ㅇㅋ 다 풀고 올게"},{"from":"them","senderId":"friend","text":"세 문제만 해도 오늘 목표는 성공이지"},{"from":"them","senderId":"friend2","text":"상자 열면 뭐 나왔는지도 알려줘 ㅋㅋ"},{"from":"them","senderId":"friend","text":"좋아 각자 할 거 하고 다시 모이자"}],
      friend2:[{"from":"them","text":"오늘 보너스 도전 봤어? 산책하면서 찾아봤어!"},{"from":"me","text":"주황색 거울 찾는 거 맞지?"},{"from":"them","text":"응 문구점 쪽에서 봤는데 생각보다 어렵더라"},{"from":"me","text":"나도 나갈 때 한번 찾아볼게"},{"from":"them","text":"찾으면 알려줘"},{"from":"them","text":"끝나고 클랜에도 이야기하자"},{"from":"me","text":"ㅇㅋ"}],
      friend3:[{"from":"them","text":"블루 캡 어디서 얻었어? 나도 꾸며 보고 싶네."},{"from":"me","text":"상점에서 봤어 ㅋㅋ 색 괜찮지"},{"from":"them","text":"ㅇㅇ 마스코트랑 잘 어울리더라"},{"from":"them","text":"나는 블랙 헤드폰인데 모자를 뭘 써야 할지 모르겠음"},{"from":"me","text":"일단 꾸미기에서 이것저것 씌워봐"},{"from":"them","text":"그거 괜찮다"},{"from":"me","text":"난 다음엔 비니도 한번 해볼까 생각 중"},{"from":"them","text":"비니도 잘 어울릴 듯"},{"from":"them","text":"나도 꾸미기 화면부터 다시 봐야겠다"}],
    },
    bonusPosts:[{name:'별이', text:'산책하다가 문구점 옆에서 찾았어!', count:3, cheered:false}],
  };
}

export function proposeQuest(text, id, previousTitle='') {
  const running=/달리|러닝|기록/.test(text)||(!/수학|오답|공부/.test(text)&&/유지|그대로/.test(text)&&/달리|러닝/.test(previousTitle));
  const study=/수학|오답|공부/.test(text);
  const maintain=/유지|그대로/.test(text);
  const task=running
    ? {title:maintain?'어제 달리기 기록 유지하기':'같은 구간 달리고 기록 남기기', condition:maintain?'어제와 같은 구간을 달리고, 어제 기록을 유지했는지 적기.':'평소 뛰는 구간을 한 번 달리고 기록을 적기.'}
    : study
      ? {title:'수학 20분 집중하고 오답 1개 정리',condition:'20분 동안 문제를 풀고, 막힌 문제 1개의 이유를 적기.'}
      : {title:'오늘 할 수 있는 첫 행동 10분 해보기',condition:'목표 「'+text.slice(0,90)+'」에 가까워지는 행동 하나를 정해 10분 동안 하고, 무엇을 했는지 적기.'};
  return {...task,id,kind:'일일',due:'오늘 23:59',xp:50,cash:25,status:'offered'};
}
