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

### Selection
- 동일 데이터의 기간/보기 2~4개에만 Segment. 한 Track+하나의 Thumb. 기본 hug-content, 같은 폭의 데이터 보기만 fill. 화면당 1개가 기본 후보.
- 실제 Result 데이터가 바뀌어야 한다. 높이43px은 시각 후보, target≥48은 필수. 다른 페이지 이동=C-BOTTOM 또는 Link, 범주 Panel=C-TABS.

### Don't
1. 멤버/상점 등 독립 목적지를 Segment로 포장.
2. 작은 알약을 전체 폭 Solid 카드로 이중 포장하거나 두 컨트롤을 한 데이터에 중복.
3. Thumb만 움직이고 숫자와 상태가 변하지 않거나 Border로 글자 Jump가 발생.

### Relations
- **허용:** C-SELECTION 동일 데이터 보기, 결과 데이터 바로 위, 단일 선택 의미체계 aria-pressed 또는 radio 중 하나.
- **금지:** Page Route/Bottom Nav/Tab Panel 소유, 각 세그먼트를 독립 링크로 만듦.

### Verification
- 정상 2~4개 선택에서 Result 갱신 vs Don't 무반응·이중 Container. 320/390/430 light/dark에서 target48·layout shift·200% 한글·aria state 검사.

## 이력

> P2 이전 버전과 후보 원문을 아래 보존하며 위 4계약만 현행 규범이다.

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
