---
id: C-BOTTOM
category: component
status: proposed
read_when: 전역 루트 목적지·상태·세이프에어리어 협약
depends_on: [F-ICON-MOTION, F-TYPE, F-GEOMETRY, F-COLOR, NAV-TOP]
references: [S2-04, S2-05, S2-07, S2-08, D-011]
---

# Bottom Navigation

## 현행 계약

### Selection
- 하단 전역 목적지는 [page-types](../pages/page-types.md)의 **글로벌 및 글로벌이너에서만 노출**하고, 글로벌이너 활성 표시는 부모 글로벌의 탭이다. 논글로벌에서는 숨기며 바텀시트페이지는 바탕을 그대로 유지한다.
- 현재 preview의 5탭은 기존 시연 구성이며 제품 전역 IA 승인값이 아니다. 라벨 최대 5자 후보, 실제 Glyph 출력 약27/24 viewBox, 활성 전용 Fill 원본만 사용. 독립 Fill이 없으면 임의 fill 금지.
- 높이는 기존 실측 약65.2px + safe와 76px 제안을 **fixture에 병치**, 값 확정 전 candidate 유지. Safe inset/콘텐츠 공간 소유자는 S-PAGE. 모든 탭 타깃 48px 이상.

### Don't
1. 클랜 보상 상세는 글로벌이너인데 `display:none`으로 바텀바를 숨겨 부모 클랜 탐색을 끊음.
2. 정보 위계 개선을 핑계로 바텀바를 76px로 강제해 320px 첫 화면에서 사람 활동 첫 행이 밀림; 실측 없이 값 승인 금지.
3. Active Outline SVG를 CSS fill로 덮어도 동일한 Fill 자산이라고 주장, 11px 미만 라벨을 고중량으로 덮음.

### Relations
- **허용:** page-types가 visible/active 소유, S-PAGE가 bottom safe/scroll 확보, C-SVG가 outline/fill source, C-ICON은 상단 독립 행동만.
- **금지:** C-BOTTOM이 Back/Close, Route 종류, Header height, 제품 탭 목적지 임의 소유; Fixed CTA가 위를 겹침.

### Verification
- 글로벌/글로벌이너/논글로벌/바텀시트 4유형 fixture의 display와 active tab 비교, 실제 인접 탭 hitbox 비중첩, SVG source 판정.
- 320/390/430 light/dark에서 실측65.2+safe vs 76 후보를 나란히 보여 첫 뷰포트 가용 높이·label 5자·200% 글자 overflow 측정. 실제 76 적용은 승인 전 보류.

## 이력

> 이전 정본·후보·승인 근거를 원문 그대로 보존하며 중복 규범으로 적용하지 않는다.

### Purpose / Non-goals
루트 간 이동과 현재 위치를 표시한다. **정확한 탭 수·이름·중앙 + 액션·목적지는 미확정**이다. 단 아이콘에 **독립 Fill 자산이 있는 경우**의 active Fill / inactive Outline 시각 규칙은 D-012로 승인됐다. 실제 네비게이션 목적지 구현 명세가 아님.

### Anatomy / Properties
Root destination items(icon + concise label + optional unread badge), selected destination, Bottom safe zone. 조작할 수 있는 항목만 표시, 단순 '+' 행동을 새 루트 목적지로 간주하지 않음.

### States
Active / Inactive / Pressed / Focus / Disabled(uncommon) / Unread. 비활성은 muted Outline, 활성은 명확한 색 + 라벨 상태를 우선 후보로 함. 별도 Fill 자산이 없는 픽토그램을 임의 CSS fill하지 않는다.

### v0.6 활성/비활성 SVG 정의
- **비활성 = Outline SVG 기본형**, **활성 = Fill SVG 별도 도형 자산**. 하나의 Outline 경로에 fill만 입혀 모양을 망가뜨리지 않음.
- 24 viewBox를 유지하고 보이는 크기는 약 27dp, label 11–12sp. Glyph와 Label의 시각 균형·선택 상태의 명확함을 함께 검사한다.
- Active/Inactive/Unread/Focus는 별도 상태이며 비활성은 지나치게 희미하지 않게. 본 규칙은 O-003의 목적지·순서를 확정하지 않는다.
- 정본 [C-SVG](svg-icon.md)를 활용하며 Root/Standard 상단은 D-011에 따른다.

### Tokens
size.bottomBarMin, iconNav(24 슬롯), type.caption/support, bg.surface, fg.secondary, accent.solid. 정확한 높이·safe inset 소유는 S-PAGE. 최상단 Header는 승인 NAV-TOP 정본.

### Guidelines
S2-04 반례 02/10: 대형 고정 CTA가 bottom bar를 밀어올리거나 겹치게 하지 않는다. 같은 기능의 Icon은 Header와 선 굵기·round join을 맞추고, 아이콘 중심/라벨 기준선 통일. 강조색 사용은 활성 대상만.

### Accessibility
navigator/label/current state를 명시. 48 이상의 비중첩 target, OS gesture inset은 Shell에서 한 번만 처리. 긴 한국어 목적지는 개수·정보 구조 재검토, 글자 축소 금지.

### Evidence / Impact
S2-05/07/08 하단바는 서로 구성이 달라 **시각 문법만 비교**. 하단 IA·활성 표현은 OPEN O-003. S-PAGE와 실제 합의 후 정본 승격.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

### v0.15 상태 정본

별도 Fill을 제공하는 nav asset은 활성=Fill + 강조색 + 라벨, 비활성=Outline + 중립. SVG 출력 27dp, `viewBox=24`, 원본 획 1.8. 사용자의 승인된 Soft SVG 소스를 사용하되 **앱의 기존 클랜 탭 나침반 교체 금지**. 탭 목적지 및 특정 아이콘 매핑 결정은 O-003. **하단바 노출·비노출/활성 부모 탭 판정은 [4가지 페이지 유형](../pages/page-types.md)의 단일 소유**이고, 높이·Safe inset 및 Fixed CTA의 공간 계산만 [S-PAGE](../shells/page-shells.md)가 소유한다. Root/Detail은 상단 Shell 구조이지 바텀바 노출 정책이 아니다.

### 2026-11-07 클랜 시연 아이콘 보정

`new/web/preview`에서 클랜 탭은 최초 MHTML에 보존된 **책갈피**이며 SEED v0.14 `bookmark.svg`와 같은 외곽 경로다. 선택 시 활성용 Fill 경로로 전환하되 같은 실루엣을 유지한다. 이전에 `main` 앱의 나침반을 가져온 시도는 잘못된 해석으로 철회했다. `main`의 기존 나침반 SVG와 제품용 디자인 결정은 손대지 않으며 웹 시연에서만 책갈피를 사용한다.
