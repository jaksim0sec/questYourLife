---
id: C-SELECTION
category: component
status: proposed
read_when: Tabs·세그먼트·필터칩·선택·체크·스위치
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005, SRC-SEED-SEGMENT]
---

# 탐색과 선택 컨트롤

비슷한 알약 형태여도 목적에 따라 분리한다. 하나의 만능 Pill API를 만들지 않는다.

| 역할 | 의미·구성 |
|---|---|
| [Tabs](tabs.md) | 관련 섹션/패널 탐색, Tab List·Label·Indicator |
| [Segmented](segmented-control.md) | 같은 데이터의 보기·기간, 단일 Track·Thumb |
| Filter Chip | 결과 조건 적용/해제, Label·선택적 Icon/Close |
| [Checkbox](checkbox.md) | 독립 선택/수동 완료, checked/mixed |
| [Radio](radio-choice.md) | 배타적 그룹 선택 |
| Switch | 즉시 적용되는 이진 설정, Track·Thumb·Label |

공통 type.body/support, radius, target, 간격은 Foundation을 따른다. 독립 목적지는 Navigation/실제 링크, 같은 콘텐츠 조건은 Filter Chip, 보기/기간은 Segmented. 필터가 없는 화면에 새 컨트롤을 강제하지 않는다. 선택/포커스/disabled/pending은 별개의 상태이며 실패하면 기존 값·초점과 결과를 보존한다.

## Switch · 개편 규격 (제안)

47×28 논리 단위 Track, Thumb 22, Inset 3, 이동 19. 최소 비중첩 target 48. Off는 중립 면과 구별되는 thumb, On은 전경색 면과 반대색 thumb. Off≠Disabled. 실제 즉시 적용되는 설정과 연결하고 읽을 수 있는 이름·checked·focus·disabled 상태를 제공한다. 색·thumb 전환은 F-ICON-MOTION. [후보 수치](../implementation/v015-tokens.json)의 `refinements`. 별도 저장 버튼이 필요한 임시 선택에는 사용하지 않는다. 과거 native checkbox/CSS 및 다크 모드 연결은 유물에 보존하며 새 앱 플랫폼에서는 해당 의미의 네이티브 컨트롤을 사용한다.

## Filter Chip · U10 / 2026-11-11

14 글자, 보이는 face 높이 40, 실제 target 최소 48, padding X/Y 10/6, icon gap 6. 확대 시 내용만큼 증가한다. 개별 Filled Pill이 기본: 비선택 중립 면/보조 글자, 선택 반전된 고대비 중립 면/글자. 독립적인 포커스 윤곽은 유지하며 기본 Outline 또는 브랜드 빨강 테두리를 강제하지 않는다. **독립 Outline 버튼처럼 만드는 표현은 비권장**.

단일 분류는 한 번에 하나, 복수 조건은 명확한 해제와 결과 연동을 제공한다. 그룹 이름, 선택·disabled·pending 상태를 읽을 수 있어야 한다(웹 참고 aria-pressed). Status Badge와 다른 목적지 링크를 섞지 않는다. 상위 Tabs → 필요 시 필터 → 실제 결과 순서. 동일 결과 바로 아래 반복 overline/제목/부제를 넣지 않으며 독립 섹션만 별도 제목을 사용한다.

## Expressive discrete choices (proposed)

For a meaningful choice between a few descriptions (not navigation or a filter), render the existing Radio/selection semantics as independent tiles: R22, minimum H128, padding 16, upper icon in a small circular well and strong lower label. Selected = inverted high-contrast neutral surface; retain explicit selected semantics and focus. A short time/preset choice uses independent R14 filled cells, minimum H48, rather than forcing a shared segmented track. A bounded ordered numeric value may use a wheel-like picker with a centered emphasized value and visible neighboring values; provide swipe/scroll, previous/next and keyboard/screen-reader equivalents, no clipping of values, and actual selection state. Do not introduce product options merely to use a component. The review specimen demonstrates previous/next taps; full wheel gesture implementation is deferred. [Candidate values](../implementation/v015-tokens.json) → `refinements.selection`.

## 접근성·근거

긴 라벨은 축소하지 않고 hug/fill을 목적에 맞게 적용한다. 포커스 이동·값 확정은 플랫폼 관례를 따르고 선택 후 스크롤/초점이 사라지지 않게 한다. Checkbox mixed는 일부 선택에만 적용. 이전 Filter Chip 시연·U10의 명칭 및 수치 변경은 [이전 상세·검증 이력](https://github.com/jaksim0sec/questYourLife/blob/07bdd23ac0da46033b45508767fa5e62643a1931/seed/components/selection-controls.md). 새 Switch/모션은 실제 구현·시각 승인 전 제안 상태.
