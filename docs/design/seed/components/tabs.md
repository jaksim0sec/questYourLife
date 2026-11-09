---
id: C-TABS
category: component
status: proposed
read_when: 상위 페이지 구역/관련 콘텐츠 판넬을 바꾸는 밑줄 탭
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR, F-ICON-MOTION]
references: [S2-04, S2-05, S2-07, S2-08]
---

# Tabs


## 현행 계약

C-TABS는 한 관련 콘텐츠 영역의 **상위 범주별 실제 패널 전환**만 소유한다. 조건 Filter/기간 Segment/루트 Nav는 C-SELECTION/C-BOTTOM이 구분한다.

### Selection

- 관련 Panel이 실제로 바뀌면 Tabs. **Line Fill 형태 기본**, 최대 5개는 P2 비교 후보로 유지하되 정보 구조가 많으면 Scrollable/tab 분류 재검토.
- 결과 조건만 바꾸면 Filter Chip, 주간/월간 등 같은 자료의 보기면 Segment. 단순 상세 이동이면 Section Link.
- Tab hit 영역은 라벨 가로 padding이 탭 사이 시각 gap과 분리되어 끊기지 않도록 **실제 target 연속성**을 보장한다. indicator 2px·stroke 1px은 D-012/기존 소스와 대조하고 변경하지 않는다.

### Don't

1. label 옆 padding 2.3px인데 항목 간 gap 26.4px인 화면(R-06): 무반응 구간이 커 터치 경험이 끊김.
2. 누르는 시각적 선택은 바뀌지만 해당 tabpanel 내용/aria-selected가 바뀌지 않는 가짜 탭.
3. 같은 데이터 보기를 바꿀 뿐인데 Tabs·Segment·Filter를 모두 나열해 공간을 차지.

### Relations

- **부모:** 같은 Page의 실제 관련 Panel wrapper; Bottom Navigation을 대체하지 않는다.
- **자식:** tablist → 각 Tab(button) → 연관 panel(id, aria-controls) **형제 관계**. Tab 안 nested link 금지.
- **형제:** Section Header/Filter는 필요한 경우에만 Panel 또는 결과 바로 위.
- **금지:** 탭 선택에 전체 페이지 hash/back state를 몰래 변경, Content Panel 없는 tablist.

### Verification

- 각 Tab의 actual hit box ≥48px 높이, 항목 사이 클릭 누락 구간 0 또는 인접 타깃 합리적으로 연속, label font computed≥12.
- Click/ArrowHomeEnd/Tab/aria-selected/aria-controls/tabpanel visibility 일치. 렌더 indicator가 실제 selected Tab 아래 있는지 측정.
- 320/390/430×light/dark, 글자 200%·길어진 라벨에서 clipped/overlap 없는지 판단.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Non-goals
**한 콘텐츠 영역의 탐색 범주**를 전환한다. 같은 데이터의 기간/보기를 조정하는 C-SEGMENT나 필터 Chip과 교환하지 않는다. Root Bottom Navigation을 대체하지 않는다.

### Anatomy / Properties
Tablist + Tabs(label, optional count/badge) + Indicator + matching panel. value, disabled, scrollable, equal/distributed 중 화면의 라벨 길이에 맞는 레이아웃을 선택.

### States
selected/unselected + focused/pressed/disabled. 현재 panel과 selected state가 일치해야 하며 변화가 특정 패널에만 영향을 준다는 것을 사용자가 알 수 있어야 함.

### Tokens
type.body 또는 type.item, bg.surface/transparent, 선택 밑줄 accent.solid, content inset F-GEOMETRY. 글자 자체가 선택되었다는 이유로 24px hero가 되지 않는다.

### Guidelines
S2-04 반례 09: Tabs·Segmented·정렬을 같은 세 줄에 나열하고 그 사이를 비워 화면을 압도하는 문제 금지. 필요한 탐색 층만 남기고 결과 콘텐츠의 시작 위치와 대응시킨다.

### Accessibility
tablist/tab/tabpanel 역할, aria-selected/aria-controls, 키보드 이동·포커스-visible, 긴 한글에서 탭 잘라먹지 않음.

### Evidence / Impact
S2-05/07의 카테고리 탐색, S2-08의 아이템 분류. 정확한 탭 개수·목적지는 미결. C-SELECTION family와 P-HIERARCHY에 연결.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

### K09 탐색 컨트롤의 관계

Tabs는 관련 패널, Segmented는 같은 데이터의 기간/보기, Filter는 결과 조건, Bottom Navigation은 루트 목적지를 바꾼다. 모양만으로 교환하지 않는다. 내 클랜의 현행 로컬 탭 구현과 키보드 계약은 보존한다. 현재처럼 패널이 즉시 바뀌는 경우 자동 활성화를 유지하고, 향후 느린 네트워크 전환이 생기면 수동 활성화를 검토한다. 가로 탭이 Up/Down을 가로채지 않게 한다.

탭 변경은 해시·페이지 전체 스크롤을 바꾸지 않고 해당 패널만 갱신한다. 선택 스타일·ARIA·현재 패널이 함께 바뀌어야 한다. 실제 정보 범주가 없는데 형식을 맞추려고 새 탭/필터를 추가하지 않는다.
