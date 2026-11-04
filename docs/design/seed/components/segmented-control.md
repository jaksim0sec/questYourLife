---
id: C-SEGMENT
category: component
status: proposed
read_when: 이번 주/지난주처럼 동일 데이터의 보기·기간 즉시 변경
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [S2-04, S2-07]
---

# Segmented Control

## Purpose / Non-goals
상위 메뉴 탐색이 아닌 **같은 콘텐츠의 표현값**을 바꾼다. 2~4개의 짧은 선택을 우선 제안하되 콘텐츠 폭·반응형이 적합한 경우만 사용한다.

## Anatomy / Properties
Track + selected Thumb + Segment labels + current value; disabled/mounted. 컨트롤 외부 그룹에는 결과 범위 또는 레이블.

## States
selected, unselected, pressed, focus, disabled; 임시 서버 조회 중에는 pending 처리 후 실제 결과에 따라 상태 확정. 버튼 그룹과 달리 하나의 value를 갖는다.

## Tokens
bg.muted Track + bg.surface Thumb, radius.pill, segment.trackHeight / paddingX, type.support/body; 각 label 주변 간격은 비례하고 외부 gutter를 무의미하게 넓히지 않는다.

## Guidelines
이미지처럼 가운데 좁은 알약을 화면 전체 폭으로 감싸 빈 좌우 공간을 낭비하지 않는다. 선택지 라벨이 길거나 좁은 폭이면 다른 선택 패턴 또는 세로 조합을 검토하고, 텍스트 축소 금지.

## Accessibility
radio-group 또는 tab의 의미를 기능에 따라 정하되 중복 의미 선언 금지. 선택값을 이름·상태로 전달. 48 터치 타깃 충돌 점검.

## Evidence / Impact
S2-04 반례 09. Period switch는 개별 버튼의 high/low 강조 차이가 아니라 하나의 상태 토글. C-TABS/P-HIERARCHY와 영향 범위 정렬.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.
