---
id: C-SELECTION
category: component
status: proposed
read_when: Tabs·세그먼트·필터칩·선택·체크·스위치
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005, SRC-SEED-SEGMENT]
---

# 탐색과 선택 컨트롤

## Purpose / Non-goals
비슷한 알약 모양을 동작이 다른 컴포넌트로 구분한다. 이 문서는 관련 선택 family를 함께 라우팅하지만 하나의 만능 pill API로 합치지 않는다. 하단 목적지는 S-PAGE의 미결 사항이다.

## 책임 분리 · v0.2
이 파일은 Checkbox/Radio/Switch 등 선택 family의 **공통 원칙만** 소유한다. 독립된 역할·상태·접근성 계약은 [C-TABS](tabs.md) · [C-SEGMENT](segmented-control.md) · [C-CHECK](checkbox.md)가 정본이다. 같은 속성을 여기에서 다시 정의하지 않고 각 명세를 링크한다.

## v0.6 책임 분리
- 상호 배타적 Radio Choice의 Group/Indicator/Checked·Focus·Disabled는 [C-RADIO](radio-choice.md)가 정본이다.
- Checkbox는 [C-CHECK](checkbox.md), 상위 탐색은 [C-TABS](tabs.md), 기간/보기는 [C-SEGMENT](segmented-control.md) 정본을 따른다.
- Segmented는 Chip을 두 겹으로 감싸지 않는다. 하나의 컨테이너 + 선택 Thumb + Label. 실용적인 선택값 2~4개, 콘텐츠 바로 위에 위치. `Tabs`의 네비게이션과 목적이 다르다.
- 다크 OFF Switch는 독립 `disabled` 값이 아니라 off 상태의 대비를 보장해야 하며, 라이트 전용 회색을 그대로 재사용하지 않는다.

## Anatomy / Properties
| 종류 | Anatomy·속성 | 의미·배치 |
|---|---|---|
| Tabs → C-TABS | Tab List + Label + Indicator, selectedKey | 관련 섹션 탐색, 아래 콘텐츠 영역과 직접 연결; 상세 계약은 독립 정본 |
| Segmented → C-SEGMENT | Track + Segment Label + Thumb, 단일 value | 같은 데이터의 보기·기간 등 즉시 전환; 상세 계약은 독립 정본 |
| Filter Chip | Label + 선택적 Close/Icon, selected/value | 조건 적용, 복수 선택·해제 가능 여부 명시 |
| Checkbox → C-CHECK | Indicator + Label + 선택적 설명, checked/mixed | 사용자 수동 완료/독립 선택; 상세 계약은 독립 정본 |
| Radio | Indicator + Label, group value | 상호 배타적 선택 |
| Switch | Track + Thumb + Label, checked | 즉시 적용되는 켜기/끄기 설정 |

## Tokens / Size
공통 type.body/support, radius.control/pill, 공간·터치는 Foundation 정본 사용. Tabs는 배경 상자 대신 선택 밑줄을 기본 제안한다. Segmented는 bg.muted Track과 bg.surface Thumb, fg.primary 라벨을 사용한다. 좌우 padding은 segment.paddingX이며 불필요하게 늘리지 않는다. 내용이 짧으면 hug, fill은 실제 동일 폭 선택 구조일 때만 쓴다. 긴 라벨은 눌러 넣거나 작은 글자로 바꾸지 않는다. Track 높이와 target은 F-GEOMETRY를 따른다.

## Filter Chip · 현재 웹 공통 구현/사용 경계

**기존 `C-SELECTION`의 Filter Chip 역할만 구현한다.** 새로운 Pill Navigation, Chip 가족이나 Page Type을 추가하지 않는다. 공통 CSS 소유자는 `web/preview/seed-standard.css`의 `.seed-filter-chip`이다. 적용 조건을 바꾸는 요소로서 현재 선택은 `aria-pressed`, 비활성은 `disabled`로 표시하며, 실제 결과 모음과 동기화한다. 키보드 접근과 48px 이상의 비중첩 클릭 영역을 유지한다.

- `전체 / 모자 / 오디오`처럼 **동일한 아이템 모음에 대한 조건**은 하나의 `role="group"` 안에 Filter Chip으로 표시한다. 단일 분류를 택하는 경우 한 번에 하나의 `aria-pressed=true`를 갖는다. 복수 선택을 허용하는 화면은 결과 로직·접근성·명확한 해제를 별도로 구현한다.
- `보상 모아보기`처럼 **다른 목적지로 이동하는 행동**은 **같은 필터 집합에 끼우지 않는다.** 실재하는 링크 목적지에 대한 `C-ACTION text/ghost` 또는 맥락상 Navigation을 사용한다. 현재 Filter를 바꿀 때 연결된 결과가 실제로 달라지지 않는 가짜 selected 시연은 구현 완료가 아니다.
- `C-STATUS`의 Status Badge는 읽기 전용 상태 표시이므로 필터 대용으로 쓰지 않는다. `C-SEGMENT`는 같은 데이터의 보기 방식/기간을 전환하므로 카테고리 필터와 바꿔 쓰지 않는다. UI가 알약형이라는 사실만으로 `C-SEGMENT`를 채택하지 않는다.
- 새로운 화면에서는 `[C-SELECTION Filter Chip] → [C-LIST/C-IMAGE 결과]`의 영향 범위를 명시한다. UI 소유자와 실제 마크업의 셀렉터가 다르면 기존 정본을 우선 재사용하고 별도 임의 디자인을 만들지 않는다.

```html
<div role="group" aria-label="아이템 종류 필터">
  <button class="seed-filter-chip" type="button" aria-pressed="true">전체</button>
  <button class="seed-filter-chip" type="button" aria-pressed="false">모자</button>
  <button class="seed-filter-chip" type="button" aria-pressed="false">오디오</button>
</div>
```

**U10 · Filter Chip 밀도와 결과 연결 (대상 정정):** 이전 'Pill Navigation'은 사용자 설명을 잘못 해석한 명칭이었다. 실제 대상은 기존 **C-SELECTION Filter Chip**이다. 텍스트 14px(기본 16px root 기준)는 유지하고, 좌우 여백은 `14px → 10px`, 상하 내부 padding은 `8px → 6px`, 아이콘–글자 간격은 `8px → 6px`로 줄인다. **실제 버튼 hit target은 최소 48px 그대로**, 보이는 pill face만 위아래 4px씩 안으로 둬 기준 40px 높이로 보이게 한다. 새로운 Size/Token/컴포넌트 없이 기존 `.seed-filter-chip`에만 적용하며, Segmented·Tabs·CTA·Bottom Navigation을 변경하지 않는다. 확대·줄바꿈 때 face와 타깃은 콘텐츠를 따라 늘어나야 한다.

**2026-11-11 · Filter Chip 시각 권장형:** 페이지 내부 필터의 기본 표현은 **개별 채움형(Filled) Pill**이다. 비선택은 중립색으로 면을 채우고 낮은 강조의 글자를, 선택은 반전된 고대비 중립색 면과 글자를 사용한다. 다크에서는 선택 칩이 밝은 면/어두운 글자, 비선택 칩이 어두운 중립 면/밝은 보조 글자가 된다. 라이트에서는 선택 칩이 어두운 면/밝은 글자로 반전된다. 선택을 알리는 테두리와 브랜드 빨강 배경은 기본값이 아니다.

- **비권장 기본형:** '전체 / 모자 / 오디오'의 각 칩을 회색 Outline으로 둘러싸고 선택 칩만 브랜드색 테두리로 강조하는 형태. 별도 맥락이 있는 예외가 아닌 한 상점·보관함 등 일반 필터에서 재사용하지 않는다.
- **기존 계약 보존:** .seed-filter-chip과 aria-pressed, 결과 연동, 콘텐츠에 따른 줄바꿈, 14px 글자, 40px 보이는 face/48px 이상 hit target을 유지한다. 새 컴포넌트·Variant·토큰을 만들지 않는다. **이 표현 권장은 C-SELECTION의 Filter Chip에만 적용**하며 C-TABS의 목적지/패널 전환, C-SEGMENT의 트랙/Thumb, 다른 액션 버튼을 변경하지 않는다.
- **선택 상태와 포커스는 별개:** 칩의 채움/텍스트 대비는 선택 상태를, 기존 focus-visible 외곽선은 키보드 포커스를 표현한다. 테두리 제거가 포커스 표시 제거를 뜻하지 않는다.

**필터 바로 아래의 표현:** 같은 결과 집합을 거르는 필터라면 **필터 → 결과 콘텐츠**가 기본 흐름이다. 필터 바로 아래에 다시 `영문 overline → 큰 제목 → 부제` 같은 강조 타이포를 반복해 *같은 결과*를 별도 도입 섹션처럼 포장하는 건 비권장이다. 결과 건수·짧은 정렬·보조 안내가 필요하면 작게 표시할 수 있다. 필터가 적용되지 않는 **실제로 독립된 목적의 섹션**이라면 별도 제목을 가질 수 있으나, 필터 결과와 섞이지 않도록 정보 경계를 명확히 한다.

실제 선택·결과 변경 시연은 `web/seed-lab/index.html` / `families.js`에서 확인한다. 이번 U10은 **Filter Chip 한정 수정**이며 이전에 대상이 불명확해 보류한 범위를 사용자 정정에 따라 해소한다.

## States / Behavior
Unselected / Selected에 Enabled·Pressed·Focus·Disabled가 조합될 수 있다. 선택은 형태+상태 의미를 제공하고 색만 바꾸지 않는다. Disabled-selected도 선택 정보를 유지한다. Tabs는 한 영역의 단일 선택과 연결 패널을 노출한다. Focus 이동과 값 확정을 플랫폼 관례대로 분리한다. 필터 적용이 지연되면 pending을 표시하며 성공 전 활성 결과처럼 단정하지 않는다.

Checkbox mixed는 일부 선택에만 사용한다. Switch는 저장 버튼이 별도 필요한 임시 선택에 쓰지 않는다. 데이터 조회 실패가 selected를 임의로 초기화하지 않도록 실패 피드백을 둔다.

## Guidelines
**페이지 내부에서 같은 콘텐츠 집합의 조건(종류·속성·상태·추천 등)을 바꾸는 필터링 UI는 Filter Chip을 우선 권장**한다. 상점의 `전체 / 모자 / 소품 / 소모품` 같은 카테고리 필터가 대표적인 용례다. 이는 **기본 선택 우선순위**이지 모든 페이지에 Chip을 강제하거나, 페이지 전환용 Tabs와 조회 방식 변경용 Segmented를 폐기하는 규칙이 아니다. 조건이 하나뿐이거나 필터가 필요 없는 화면에는 불필요한 칩을 만들지 않는다.

상위 Tabs → 관련 필터 → 콘텐츠를 구분한다. **다른 목적지/독립 화면으로 전환**하면 Tabs 또는 실제 링크 등 기존 Navigation을 사용하고, `보관함`이 단순 소유 상태 조건이면 Filter Chip, 별도 소유품 관리 화면이면 Navigation으로 구별한다. 보기 방식·기간 전환은 C-SEGMENT 등 해당 의미를 우선한다. 필터는 실제 결과와 선택 상태를 연결하고 결과 바로 아래 중복 타이포를 만들지 않는다. 정렬을 선택 컨트롤 Track에 끼워 넣지 않는다. 여러 Segmented를 겹쳐 어느 영역을 바꾸는지 모호하게 만들지 않는다. Tabs와 Segmented를 모양이 비슷하다는 이유로 교환하지 않는다.

## Accessibility
짧은 라벨, 현재 값, disabled, Checkbox group의 이름을 읽을 수 있어야 한다. 최소 target·포커스 ring 적용. Outline이 필요한 다른 조작의 경계는 stroke.control을 사용하고, 채움형 Filter Chip에는 기존 focus-visible 윤곽을 유지한다. 상태 변화 후 포커스 유실·불필요한 스크롤 점프를 막는다.

## Evidence & Confidence / Impact
Tabs·기간 선택은 REF-001/002/004 관찰 high, 동일 화면의 선택 요소 밀집 문제는 REF-005 교정. Checkbox/Radio/Switch 및 keyboard·pending 상태는 이미지 미노출의 proposed 보완이다. 구체적 필터·탭 명칭·개수는 화면 데이터이며 시드가 확정하지 않는다.

