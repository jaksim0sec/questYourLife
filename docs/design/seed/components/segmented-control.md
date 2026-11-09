---
id: C-SEGMENT
category: component
status: proposed
read_when: 이번 주/지난주처럼 동일 데이터의 보기·기간 즉시 변경
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [S2-04, S2-07]
---

# Segmented Control


## 현행 계약

C-SEGMENT는 **같은 데이터의 보기/기간**을 2–4개 선택 중 즉시 바꾼다. Track 하나+선택 Thumb 하나, 기본 hug-content, 내용상 동등한 경우만 fill이 D-012 승인 표현이다.

### Selection

- 이번 주/지난 주·목록/그리드 같이 **데이터의 범위·뷰만** 바꿀 때 사용. Page 경로 이동은 Section Link/Bottom Nav, 상위 섹션 변경은 Tabs.
- 같은 화면 Segment는 기본 하나만 두고, 추가가 필요하면 실제 독립 상태를 증명해야 한다. 좁은 폭에서 48px hit target이 겹치면 텍스트 축소가 아니라 다른 선택 패턴으로 변경.
- 표시 높이 약43px은 웹 디자인 기준, 물리적 실제 조작은 최소48.

### Don't

1. Track 위에 다시 Card/Chip 배경을 겹쳐 '두 번 감싼 알약'을 생성.
2. 선택을 눌러도 실제 기간/데이터가 바뀌지 않고 active 표시만 움직임.
3. 화면 폭의 100%를 쓰면서 내용은 가운데 2개 작은 Segment만 남아 좌우가 허전하거나 48px 타깃이 겹침.

### Relations

- **부모:** 기간·뷰 변경을 받는 데이터 컨테이너 바로 위, 필요시 C-SURFACE 그룹.
- **자식:** one Track + one Thumb + 2–4 Label/Option, radiogroup 또는 pressed 한 의미 체계.
- **형제:** 영향받는 목록/차트. 같은 데이터를 다른 조건으로 거르는 Filter는 별도 C-SELECTION 분기.
- **금지:** Route 이동, 선택마다 버튼 High 적용, 구조 없는 중복 Segment.

### Verification

- selected value와 표시 내용/aria semantics 일치, Track/Thumb 1개씩만, 2–4개/기본 화면당1.
- 320/390/430×light/dark, 200% 글자에서 overflow와 옵션 hit target ≥48 비중첩.
- Don't의 중복 트랙·가짜 선택·불필요 fill이 정상 예와 렌더에서 실제 구분되는지 점검.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Non-goals
상위 메뉴 탐색이 아닌 **같은 콘텐츠의 표현값**을 바꾼다. 2~4개의 짧은 선택을 우선 제안하되 콘텐츠 폭·반응형이 적합한 경우만 사용한다.

### Anatomy / Properties
Track + selected Thumb + Segment labels + current value; disabled/mounted. 컨트롤 외부 그룹에는 결과 범위 또는 레이블.

### States
selected, unselected, pressed, focus, disabled; 임시 서버 조회 중에는 pending 처리 후 실제 결과에 따라 상태 확정. 버튼 그룹과 달리 하나의 value를 갖는다.

### Tokens
bg.muted Track + bg.surface Thumb, radius.pill, segment.trackHeight / paddingX, type.support/body; 각 label 주변 간격은 비례하고 외부 gutter를 무의미하게 넓히지 않는다.

### Guidelines
이미지처럼 가운데 좁은 알약을 화면 전체 폭으로 감싸 빈 좌우 공간을 낭비하지 않는다. 선택지 라벨이 길거나 좁은 폭이면 다른 선택 패턴 또는 세로 조합을 검토하고, 텍스트 축소 금지.

### Accessibility
radio-group 또는 tab의 의미를 기능에 따라 정하되 중복 의미 선언 금지. 선택값을 이름·상태로 전달. 48 터치 타깃 충돌 점검.

### Evidence / Impact
S2-04 반례 09. Period switch는 개별 버튼의 high/low 강조 차이가 아니라 하나의 상태 토글. C-TABS/P-HIERARCHY와 영향 범위 정렬.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

### v0.15 단일 알약 세그먼트 · 확정 표현

**하나의 트랙 → 하나의 선택 알약(thumb) → 2–4개 Label.** 트랙을 다시 큰 컨테이너/카드로 감싸지 않으며 버튼별 추가 테두리/칩을 겹치지 않는다. 선택 컨트롤의 폭은 기본 `hug-content`, 같은 비중의 콘텐츠 보기 전환에서만 `fill`. 시연 visible height ≈43px; native touch는 48dp의 비중첩 target 보장. selected만 thumb/foreground 차이, unselected는 중립; 선택 상태를 `aria-pressed` 또는 radio semantics 한 체계로만 노출.
