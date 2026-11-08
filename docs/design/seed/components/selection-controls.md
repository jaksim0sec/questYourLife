---
id: C-SELECTION
category: component
status: proposed
read_when: Tabs·세그먼트·필터칩·선택·체크·스위치
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005, SRC-SEED-SEGMENT]
---

# C-SELECTION · Tabs/Filter/Segment/Link 선택 라우터

## 현행 계약

| 질문 | 선택 | 위치 | 반례 |
|---|---|---|---|
| 상위 콘텐츠 범주인가 | Tabs | 해당 Panel 바로 위 | Tab을 경로 링크로 대체 |
| 목록 결과에 조건을 걸었나 | Filter Chip/Bar | 결과 목록 바로 위 | 선택돼도 결과 변경 없음 |
| 같은 데이터의 기간/보기 변화인가 | Segment | 바뀌는 데이터 바로 위 | 다른 페이지 이동을 Segment로 위장 |
| 실제 화면/상세로 이동하나 | Section Link | 관련 섹션 안 | 독립 목적지를 Tab처럼 표현 |
| 그룹 단일 선택인가 | Radio | 옵션 그룹 | 완료 체크를 Radio로 구현 |
| 사용자가 완료/독립 선택하나 | Checkbox | 직접 체크 | 사용자의 확인 없이 자동 완료 |

C-TABS는 실제 패널 전환과 `role=tab/tabpanel` 연결을 제공한다. **Tab 가로 Padding이 간격보다 작아 2.3px vs 26.4px의 끊어진 Hit Area**가 되면 불합격. 같은 데이터에 Segment 여러 개를 나열하지 않는다. 결과가 바뀌지 않는 Filter는 정적 라벨로 내려야 한다.

각 부품의 자체 상태/키보드 세부 계약은 [tabs](tabs.md), [segmented](segmented-control.md), [checkbox](checkbox.md), [radio](radio-choice.md)가 소유한다.

### Selection
- 상태 범위부터 판단: 범주 Panel=Tabs, 결과 조건=Filter, 동일 데이터의 기간/보기=Segment, 화면 이동=Section Link, 단일 그룹 선택=Radio, 수동 완료=Checkbox.
- Chip은 필터·선택·추천·상태·분류 다섯 역할을 의미별로 구분하고 정적인 상태를 클릭 Filter처럼 표시하지 않는다. Filter는 실제 결과 바로 위.

### Don't
1. 필터 '공부'를 눌러도 목록 결과가 그대로이고 선택 Chip만 빨개짐.
2. Tabs/Segment/Sort를 한 화면에 세 줄로 중복 쌓아 핵심 정보를 밀어냄.
3. 장착 중 상태를 토글형 Chip으로 표현해 클릭 기대만 발생.

### Relations
- **허용:** C-TABS Panel, C-SEGMENT View, C-RADIO Exclusive, C-CHECK 수동 완료, P-HIERARCHY 결과 인접.
- **금지:** 타입별 키보드 소유권 중복, 선택 스타일만으로 동작 구현 주장, 데이터 필터 조건 임의 생성.

### Verification
- 정상 Tabs Panel/Filter 실제 결과/Segment 데이터 전환 각각을 독립 검사. Don't 무반응 필터·3단 컨트롤과 비교, 320/390/430 light/dark에서 클릭·aria·scroll·200% 검증.

## 이력

- 이전 계약은 [HISTORY](../HISTORY.md)에 원본 보존; 현재 연결 규칙이 우선한다.
