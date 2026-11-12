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

## 2026-11-12 · 기간 선택 오용 방지

- `이번 주 / 지난주`는 별도의 Action Button 2개가 아니라 **C-SEGMENT의 단일 기간 값 선택**이다. 2개의 독립 Outline 버튼, 빨간 border만 두른 활성 버튼, 100% 너비로 늘어난 두 개의 알약 버튼을 기본형으로 노출하는 것은 **비권장**.
- 이 시안이 반복 출현한 이유는 명시적 기간 선택을 범용 `C-ACTION Neutral/Brand Outline` 버튼 조합으로 취급한 **시연 제작 판단의 오류**다. 현행 SEED는 Track 1개 + 선택 Thumb 1개 + Label 2개 구조를 이미 정의한다. 기간 선택 필요 없는 페이지에는 이 컴포넌트 자체를 삽입하지 않는다. 페이지나 독립 범주를 이동시키려는 경우에는 C-TABS/Navigation을 선택한다.
- 한 페이지에서 가독성과 결과 전환 상태를 검증하지 않은 시각용 버튼을 관성적으로 복붙하지 않는다.

## v0.15 단일 알약 세그먼트 · 확정 표현

**하나의 트랙 → 하나의 선택 알약(thumb) → 2–4개 Label.** 트랙을 다시 큰 컨테이너/카드로 감싸지 않으며 버튼별 추가 테두리/칩을 겹치지 않는다. 선택 컨트롤의 폭은 기본 `hug-content`, 같은 비중의 콘텐츠 보기 전환에서만 `fill`. 웹 기본 표시 높이는 트랙 43px, 선택 알약 37px이다. 각 옵션의 실제 버튼 영역은 최소 48×48 CSS px이고 장식 면은 버튼과 별개로 중앙에 둔다. 확대·줄바꿈 시 트랙과 버튼은 내용에 맞춰 높아진다. 좁은 영역에서 모든 타깃을 확보하기 어려우면 선택지를 세로로 배치하거나 기존 선택 패턴을 사용한다. Native touch 기준은 48dp다. selected만 thumb/foreground 차이, unselected는 중립; 선택 상태를 `aria-pressed` 또는 radio semantics 한 체계로만 노출.
