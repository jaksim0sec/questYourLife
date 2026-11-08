---
id: C-ICON
category: component
status: proposed
read_when: 헤더·목록·버튼 주변 단독 아이콘 행동과 동그란 버튼
depends_on: [F-ICON-MOTION, F-GEOMETRY, F-COLOR, C-ACTION]
references: [S2-01, S2-04, S2-05, S2-08, D-011]
---

# Icon Button

## 현행 계약

### Selection
- 독립 아이콘 한 개로 의미가 명확한 Header/Row 행동에만 적용. 일반 아이콘+라벨은 C-ACTION, 상태 표시만 하는 픽토그램은 C-SVG, 바텀 목적지는 C-BOTTOM.
- **24 viewBox Glyph의 광학 중심과 48px 이상 비중첩 Target 중심을 1px 이내**로 맞춘다. 개별 아이콘의 `translateX`, `scale`로 Header Slot 위치 교정 금지. 상단 Right Slot 권고 2·최대 3은 NAV-TOP에서 결정한다.

### Don't
1. 헤더의 선물/더보기 아이콘마다 `translateX(-3px)`로 강제 정렬: 페이지 시작선 및 클릭 중심 불일치.
2. SVG가 24px이라서 클릭 영역도 24px인 상태. 색상·아이콘 종류와 무관하게 target 48 유지.
3. 이름 없는 아이콘 버튼 또는 조작 기능이 없는 장식 아이콘에 hover·pressed/chevron 추가.

### Relations
- **허용:** NAV-TOP Right Slot > C-ICON, Row suffix 독립 버튼 1개, C-SVG 윤곽 1.8 round 자산.
- **금지:** C-ICON이 페이지 Gutter·Route·Bottom Nav 목적지 소유, 다른 button/a 안에 button을 중첩.

### Verification
- 정상 48 Target/24 Glyph vs Don't 24 Target, translated Glyph를 병치. DOM Rect 중심 차 1px, target width/height >=48 및 교차 0.
- 320/390/430 light/dark에서 focus ring·label, glyph path 소스 동일성, 200% 글자 확대에도 동심 정렬.

## 이력

> P2 이전 제안·구버전·수치 근거를 원문 보존한다. 현행 판단은 위 네 계약에서만 수행하며 이전 후보를 자동 승인하지 않는다.

### Purpose / Non-goals
텍스트가 없는 독립 행동. 큰 선물·편집 SVG가 기존 버튼 family에서 튀는 문제를 막는다. 표시를 위한 아이콘은 Button이 아니며 active인 하단 목적지는 C-BOTTOM으로 분리한다.

### Anatomy
Hit Target + Glyph + optional Badge/Unread Dot + accessible name. Glyph는 round Outline, 기본 `viewBox 0 0 24 24`에 표시. 시각 아이콘 크기와 48 최소 터치 영역은 분리한다.

### Properties
size=compact/default/nav; treatment=clear/weak/outline; tone=neutral/brand/critical, label(필수), disabled/loading. 색·굵기·그림자·비네팅을 아이콘별로 정의하지 않는다.

### States
Default, Pressed, Focus-visible, Loading, Disabled. Badge는 알림 수·새 소식 등 실제 데이터에만 대응하며 툴팁만으로 의미를 제공하지 않는다.

### Tokens
F-ICON-MOTION stroke 1.8 round, F-GEOMETRY size.iconSmall/default/nav 및 size.touchMin, F-COLOR fg.primary/secondary/accent.

### Guidelines
상단은 승인된 D-011의 크기·배치 우선. 추가·선물·편집의 픽토그램이 타이틀 높이를 넘어서는 시안을 반례로 검수. 3D 아이템·마스코트 얼굴은 Glyph가 아니다.

### Accessibility
단독 아이콘의 이름은 '검색', '알림', '더보기' 등 결과를 설명. hit target 겹침 금지, 포커스 시 outline 표시, 장식 아이콘 중복 읽기 금지.

### Evidence / Impact
S2-04 반례 05, S2-05/08 작은 선형 아이콘. 독립 조작 API가 있는 Icon Button을 C-ACTION의 단순 색상 variant로 확장하지 않기 위해 별도 명세. NAV-TOP·C-LIST·C-BOTTOM과 함께 검증.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

### 공통 계약 r1의 실제 타깃

Glyph 박스는 target 박스 중앙에 둔다. 헤더의 icon별 translateX·scale로 슬롯을 조작하지 않는다. 최소48 CSS px와3rem 중 큰 실제 비중첩 target을 확보한다. 새 Header 아이콘 수·순서는 현재 페이지의 실제 행동으로 정하며 들어가지 않으면 타깃 축소 대신 낮은 우선순위부터 기존 메뉴로 이동한다. 헤더 구조는 [NAV-TOP](../../TOP_NAVIGATION.md), 정렬/이력·safe inset은 [S-PAGE](../shells/page-shells.md)의 K10이 소유한다.
