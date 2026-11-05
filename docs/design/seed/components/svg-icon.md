---
id: C-SVG
category: component
status: proposed
read_when: 기본 Outline·활성 Fill SVG와 크기·텍스트 비례 검수
depends_on: [F-ICON-MOTION, F-GEOMETRY, F-TYPE, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# SVG Icon / Optical Contract

## Purpose
UI 픽토그램의 **한 군(群)의 도형 규격**, outline과 fill의 독립 자산 경로, 사용 맥락별 출력 크기를 정의. 보상 코인·마스코트·3D 소품은 C-SVG가 아님.

## Anatomy / Properties
`name`, `mode:outline|fill`, `size:badge|inline|row|header|bottom`, `semanticColor`, `decorative`. 모든 아이콘 `viewBox=0 0 24 24`, `fill=none` / 기본 `stroke=currentColor` / `stroke-width=1.95` / `stroke-linecap=round` / `stroke-linejoin=round` 후보. Fill 자산은 별도의 `*-fill` 경로로 작성하고 기존 outline에 단순 CSS 채우기를 강제하지 않는다.

## Context / Output Size
Badge 12–14, Inline Button 16–18, Row 20, Top 24, Bottom 27dp (같은 24 viewBox). 실제 도형의 optical bounding box·기준선·획 면적이 모든 픽토그램에서 가까워야 한다. Icon Button hit target은 48dp. 하단 inactive는 Outline, active는 Fill 후보로 검수; 실제 탭 목적지는 O-003 미결. 하단 외 페이지의 모든 selected에 Fill을 강제하지 않는다.

## SVG Shape Rules
3개의 햄버거 가로선 y≈4.8/12/19.2로 높이 균형; Home 외곽은 둥근 코너/지붕, 기호 의미가 사라질 정도의 젤리형 단순화 금지. 검색/알림/메뉴/홈/체크/DM/클랜/마이 8개를 동일 24그리드에 배열해 판단.

## States & Accessibility
Default/Active/Disabled/Pressed/Focused. 장식 SVG는 읽기에서 숨기고, 단독 클릭 버튼에는 행동 기반 accessible label. 활성은 색 외에도 Fill/weight/label로 구분. 스크린리더/키보드/광학적 중심/다크 대비·200% 검수.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.

## v0.13 SVG source (proposed)

[37개 실제 SVG 파일](../assets/icons/v0.13/README.md)을 제공한다. v0.13 `home/users`의 Outline·Fill은 각각 같은 지붕/인물 실루엣을 공유한다. 모든 아이콘을 같은 모양으로 만들기보다 24×24 그리드에서 시각적 크기·여백을 개별 보정. 기존 앱의 확정 나침반 클랜 네비는 **변경하지 않음**. `stroke-width=2.0`은 해당 아이콘 소스의 후보값이며 기존 Android 자산의 확정치와 혼동하지 않는다.

## v0.14 reference-derived geometry (proposed)

[37개 원본 SVG](../assets/icons/v0.14/README.md). 사용자 제공 4장 한국 서비스 사이드바 시안에 맞춰 선 끝/여백/실루엣을 다시 그렸다. `viewBox=24×24`, 기본 `stroke-width=2.15`, `round` cap/join 후보. v0.13의 ID와 활성 Fill 쌍은 보존. 단순 가로/세로 늘림 금지. 실제 앱 적용 전에 `C-BOTTOM`의 확정 클랜 나침반 등 기존 승인 자산과 충돌하지 않도록 검토한다.
