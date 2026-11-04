---
id: C-IMAGE
category: component
status: proposed
read_when: 아이템/미디어/배너 공통 이미지 컨테이너와 이미지 상태
depends_on: [F-GEOMETRY, F-COLOR]
references: [S2-03, S2-05, S2-06, S2-07, S2-08]
---

# Image Frame

## Purpose / Non-goals
콘텐츠 이미지가 안전하게 들어갈 **모양/비율/로딩/폴백 컨테이너**. 이벤트 조명 자체나 브랜드 색상 정의는 A-ART.

## Anatomy / Properties
Frame + Image + optional Status/Badge + Fallback; fit=contain|cover, role=item|thumbnail|hero, ratio=content-defined. 배경을 가진 콘텐츠 썸네일은 cover, 투명 아이템/마스코트는 contain.

## States
Loading: 같은 종횡비 placeholder; Loaded; Error: 폴백+필요한 설명. Owned/Selected/Equipped는 외부 상태 라벨로, 모든 이미지 위에 배지 억지 부착 금지.

## Tokens
radius.image, bg.muted/surface, stroke.subtle. 같은 그리드의 이미지 프레임은 일관된 비율과 안전 영역을 유지한다. 단, 문구가 많은 홍보 이미지의 일률적 crop 금지.

## Guidelines
3D 물체의 금색 광택은 오직 물체/특별 배너에만 사용. 일반 카드 경계나 버튼을 금색/네온으로 칠하지 않음. S2-06은 화려한 보상 순간, S2-08은 일상 진열이라는 서로 다른 맥락이다.

## Accessibility
이미지 대체 텍스트는 중요한 시각 정보만, 가짜 내장 글자는 실제 텍스트로 중복 제공. zoom에서는 그리드 열 감소·줄바꿈 허용.

## Evidence / Impact
S2-03/05/06/07/08 5개 자료를 이미지 목적별로 비교. A-ART·C-SURFACE·P-HIERARCHY 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.
