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

## 이력

- 이전 계약은 [HISTORY](../HISTORY.md)에 원본 보존; 현재 연결 규칙이 우선한다.
