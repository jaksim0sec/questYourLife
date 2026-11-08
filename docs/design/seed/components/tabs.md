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

### Selection
- 같은 화면의 **다른 콘텐츠 Panel**만 Tabs. 기간/보기=C-SEGMENT, 결과조건=Filter, 화면이동=Section Link.
- Panel 바로 위에 배치하고 모든 탭 hitbox가 연속돼야 한다. 기존 padding2.3px·간격26.4px·라벨13.4px은 반례. Line 최대5는 후보, indicator2/stroke1은 기록된 기준과 후보 지위 유지.

### Don't
1. 탭 사이 26.4px의 비클릭 빈공간을 두고 모양만 합격 처리함.
2. 이번 주/지난주 수치 선택이나 상세 라우트 이동을 Tabs처럼 표시함.
3. 선택 CSS만 바뀌고 aria-selected·tabpanel이 갱신되지 않음.

### Relations
- **허용:** C-SELECTION 라우터→C-TABS, tablist→tab→tabpanel의 id 연결.
- **금지:** Bottom Navigation·route history 대체, 같은 데이터 위 Segment 중복, 탭 클릭 시 전체 scroll reset.

### Verification
- 정상 연속 Target+Panel 변화 vs Don't 클릭 빈 공간, rect gap 및 aria-selected·tabpanel 검사. 320/390/430 light/dark에서 한글 5개, ArrowLeft/Right/Home/End, focus와 실제 Panel 상태 확인.

## 이력

> P2 이전 버전과 후보 원문을 아래 보존하며 위 4계약만 현행 규범이다.

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
