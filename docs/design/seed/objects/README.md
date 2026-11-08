---
id: O-README
category: object
status: proposed
read_when: 의미 단위 정보 객체의 슬롯·클릭·실패
depends_on: [F-GEOMETRY, F-TYPE, C-TEXT]
references: [D-012, D-013]
---

# Object 공통 계약

## 현행 계약

**의미 단위 다섯 슬롯**은 Media / Identity / Value / State / Action이다. Media는 실제/장식 이미지, Identity는 알아볼 수 있는 이름, Value는 숫자·단위·조건, State는 도메인 사실·선택·확정 상태, Action은 이동 또는 작업 소유다. 필요 없는 슬롯을 채우지 않는다.

- **대표 타깃은 객체마다 하나**이며, 그 안에 독립된 부가 행동은 최대 하나. 타깃과 버튼은 DOM 형제로 둬 **중첩 링크·버튼 금지**. 클릭 불가능한 객체는 chevron/pressed/클릭 암시 그림자 금지.
- Identity는 기본 **2줄**, 3줄부터 말줄임 검토. 단 정적 객체에 전체 이름을 확인할 경로가 없으면 이름을 자르지 않고 높이 증가. 사용자가 글자를 200%로 키웠을 때도 읽을 수 있어야 한다.
- Media 실패·로딩 상태에서도 **Identity·Value는 그대로 유지**. 이미지 로드 실패를 가격 0, 보유, 잠금으로 오인하면 안 된다.
- 한 객체의 같은 슬롯 구조가 **2개 이상 페이지 맥락**에 반복될 때만 새 O-*를 승격한다. 만능 ItemCard/상태 20개 옵션식 상속은 금지.
- State는 실제 보유/장착과 현재 선택을 구별한다. 이 UI 시연의 보유·지급·구매·장착은 명시된 Fixture 외에는 미구현이다.

## 이력

- r1 K05에서 사용하던 Media/Identity/Value/State/Action 후보는 이곳을 공통 소유자로 이동했다.
