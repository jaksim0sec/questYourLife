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

Glyph 시각 크기와 터치 타깃은 독립이다. Header glyph **24px**, 실제 타깃 **48×48px**, 중심 차이 **≤1px**. 기존 승인 SVG 원본·획을 보존하고 CSS translateX로 광학 중심을 속이지 않는다.

### Selection

- 상단 검색/알림/메뉴처럼 **텍스트 없이 독립 실행**일 때 C-ICON. 라벨 가능한 일반 버튼은 C-ACTION, 장식 그림은 C-SVG만 사용.
- Header Right Slot은 **2개 권장, 최대 3개**. 동작이 많으면 우선순위를 정하고 기존 더보기/메뉴로 이동하지 타깃을 축소하지 않는다.
- 행 내부는 C-LIST suffix의 독립 행동 최대 1개를 소비한다. 클릭하지 않는 상태 표시는 Badge/Icon만 사용.

### Don't

1. Header 버튼 하나에만 `translateX(3px)` 등을 적용해 좌우 시작선 또는 아이콘 중심이 어긋남(R-03).
2. 24px glyph를 그대로 24px click target으로 쓰거나, 장식 SVG에 hover/pressed 외형을 줘 클릭을 암시함.
3. 알림/검색/메뉴를 4개 이상 한꺼번에 배치해 터치 범위가 겹침.

### Relations

- **부모:** NAV-TOP right slot, C-LIST suffix, 실제 독립 컨트롤 영역.
- **자식:** C-SVG glyph 1개 + 접근성 이름 + 조건부 unread indicator. 별도 텍스트 행동을 nested button으로 넣지 않는다.
- **형제:** NAV-TOP 제목/Back 슬롯과 단일 `page.inset` 공유, Button group의 낮은 우선순위 보조 행동.
- **금지:** C-BOTTOM의 active destination을 Icon Button으로 재정의, glyph만 transform으로 이동.

### Verification

- computed target rect ≥48×48, SVG/target 중심축 x/y 차이 ≤1px, 겹치는 target 0.
- Header title과 본문 x 시작선 차이 ≤1px(측정 대상이 동일한 gutter일 때), right slot 개수 0–3 및 click name.
- light/dark, 320/390/430, 키보드 focus와 200% 확대에서 아이콘 및 라벨이 잘리지 않아야 한다.

## 이력

- P2 이전 정본 전문은 이 아래에 보존. 구 승인값의 권위는 [D-012](../implementation/v015.md)·[토큰](../implementation/v015-tokens.json)으로 판정하며, 이 아래의 옛 제안·K/r1 문단은 새 현행 규칙이 아니다.

#### Purpose / Non-goals
텍스트가 없는 독립 행동. 큰 선물·편집 SVG가 기존 버튼 family에서 튀는 문제를 막는다. 표시를 위한 아이콘은 Button이 아니며 active인 하단 목적지는 C-BOTTOM으로 분리한다.

#### Anatomy
Hit Target + Glyph + optional Badge/Unread Dot + accessible name. Glyph는 round Outline, 기본 `viewBox 0 0 24 24`에 표시. 시각 아이콘 크기와 48 최소 터치 영역은 분리한다.

#### Properties
size=compact/default/nav; treatment=clear/weak/outline; tone=neutral/brand/critical, label(필수), disabled/loading. 색·굵기·그림자·비네팅을 아이콘별로 정의하지 않는다.

#### States
Default, Pressed, Focus-visible, Loading, Disabled. Badge는 알림 수·새 소식 등 실제 데이터에만 대응하며 툴팁만으로 의미를 제공하지 않는다.

#### Tokens
F-ICON-MOTION stroke 1.8 round, F-GEOMETRY size.iconSmall/default/nav 및 size.touchMin, F-COLOR fg.primary/secondary/accent.

#### Guidelines
상단은 승인된 D-011의 크기·배치 우선. 추가·선물·편집의 픽토그램이 타이틀 높이를 넘어서는 시안을 반례로 검수. 3D 아이템·마스코트 얼굴은 Glyph가 아니다.

#### Accessibility
단독 아이콘의 이름은 '검색', '알림', '더보기' 등 결과를 설명. hit target 겹침 금지, 포커스 시 outline 표시, 장식 아이콘 중복 읽기 금지.

#### Evidence / Impact
S2-04 반례 05, S2-05/08 작은 선형 아이콘. 독립 조작 API가 있는 Icon Button을 C-ACTION의 단순 색상 variant로 확장하지 않기 위해 별도 명세. NAV-TOP·C-LIST·C-BOTTOM과 함께 검증.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

#### 공통 계약 r1의 실제 타깃

Glyph 박스는 target 박스 중앙에 둔다. 헤더의 icon별 translateX·scale로 슬롯을 조작하지 않는다. 최소48 CSS px와3rem 중 큰 실제 비중첩 target을 확보한다. 새 Header 아이콘 수·순서는 현재 페이지의 실제 행동으로 정하며 들어가지 않으면 타깃 축소 대신 낮은 우선순위부터 기존 메뉴로 이동한다. 헤더 구조는 [NAV-TOP](../../TOP_NAVIGATION.md), 정렬/이력·safe inset은 [S-PAGE](../shells/page-shells.md)의 K10이 소유한다.
