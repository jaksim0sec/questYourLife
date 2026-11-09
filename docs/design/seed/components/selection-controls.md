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


이 문서는 탐색 선택 **라우터**이며 각 컨트롤의 개별 키보드/치수는 C-TABS·C-SEGMENT·C-RADIO·C-CHECK가 소유한다.

### Selection

| 질문 | 선택 | 배제 |
|---|---|---|
| 상위 관련 콘텐츠 패널 전환? | Tabs, Panel 위 | Page Route를 Tab으로 위장 |
| 결과 조건 변경? | Filter Chip, 결과 바로 위 | 클릭해도 결과 불변 |
| 같은 데이터 기간·보기 변경? | Segment, 대상 데이터 위 | 다른 페이지 이동 |
| 한 항목만 독립 선택? | Radio Group | 완료 Checkbox |
| 복수 항목/완료 확인? | Checkbox | Radio로 완료 표시 |
| 화면·상세로 이동? | Section Link | Filter가 링크처럼 동작 |

Chip의 역할은 Suggestion/Filter/Input/Nav를 구분한다. Chip에 선택 그래픽만 넣어 서로 다른 행동을 같은 컴포넌트라고 주장하지 않는다.

### Don't

1. 탭처럼 생긴 Filter를 클릭해도 실제 결과 수와 DOM 항목이 전혀 달라지지 않음.
2. 탭·Segment·Filter를 한 페이지 위에 목적 설명 없이 세 겹 배치하여 첫 콘텐츠를 아래로 밀어냄.
3. Checkbox 선택을 사용자의 완료 확정으로 바로 보상 지급 처리.

### Relations

- **부모:** 관련 Page/Section의 실제 데이터 컨테이너 또는 탐색 영역.
- **자식:** Tabs/Segment/Filter/Radio/Checkbox 고유 Controller. 이 라우터가 각 컨트롤의 세부 상태를 덮어쓰지 않는다.
- **형제:** Section Header/Filter Result, 상태 알림(상태 확정 전 별도)과 인접.
- **금지:** 특정 데이터가 바뀌지 않는 가짜 Filter, 다른 목적지로 이동하는 세그먼트, Button High를 선택표시로 대용.

### Verification

- 선택된 컨트롤과 실제 Panel/Result/List 변경이 동기화되는지 자동 인터랙션.
- Filter는 before/after 결과 집합 비교, Tabs는 role/tab/tabpanel/selected와 Arrow 이동, Segment는 value와 데이터 범위 일치.
- 320/390/430×light/dark, 200% 글자/터치 연속·겹침 없는지 정상/Don't 비교.

## 이력

- 이전 계약은 [HISTORY](../HISTORY.md)에 원본 보존; 현재 연결 규칙이 우선한다.
