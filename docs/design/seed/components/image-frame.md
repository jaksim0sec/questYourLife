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

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K06 미디어와 아트

**정본 소유자:** 프레임은 `components/image-frame.md`, 아트 강도는 `assets/reward-art.md`.

투명 아이템·마스코트는 contain, 배경이 있는 썸네일·장면은 안전영역을 확인한 cover를 사용한다. item의 기본 프레임은 1:1이다. thumbnail/scene의 비율은 원본과 콘텐츠 목적에서 정하고 같은 모음 안에서 일관되게 사용한다. 기존 3D 원본·얼굴·장비 좌표를 바꾸지 않는다.

미디어 상태 loading/loaded/error와 소유/장착/선택 상태는 서로 독립이다. 로딩/오류에서도 프레임 비율과 외부 텍스트를 유지한다. 이미지 실패 때문에 객체 전체를 지우거나 빈 회색 박스로만 남기지 않는다.

**우리 개편의 Overlay 기본 한도**는 모서리 슬롯당 1개·전체 2개다. 외부 서비스의 보편적 강제 규칙이라는 뜻은 아니다. 선택은 외곽선/체크, 장착은 짧은 외부 상태, 희귀도는 보조 프레임/메타를 우선한다. 표시가 충돌하면 현재 조작/사용 가능성 → 장착 사실 → 희귀도/이벤트 순으로 정리하고 나머지는 Information으로 옮긴다. 부족한 정보는 삭제하지 않는다.


아트 강도 A1/A2/A3는 [A-ART](../assets/reward-art.md)의 현행 적용 절을 따른다.
