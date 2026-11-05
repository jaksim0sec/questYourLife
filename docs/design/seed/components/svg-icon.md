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
