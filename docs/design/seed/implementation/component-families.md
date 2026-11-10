---
id: SEED-COMPONENT-FAMILIES
category: implementation-reference
status: proposed
read_when: 타이포그래피·버튼·목록·입력·상태 등 기존 SEED의 속성을 계열별로 찾아 비교하거나 공통 CSS 구현을 연결할 때
depends_on: [SEED-V015-ACCEPTED, SEED-SHARED-STYLE, C-TEXT, C-ACTION, C-STATUS, C-LIST, C-FIELD, C-ICON, C-SURFACE]
references: [D-011, D-012, D-013, K03, K07, K08, K09]
---

# SEED 컴포넌트 패밀리 · 속성/표현/소유권 지도 (v1 보완)

> **목표:** 기존 디자인의 색·Radius·폰트 스케일·버튼 높이·SVG를 재설계하지 않고, 부족했던 사용 디테일을 **공통 구현과 계열별 탐색**에 연결한다. 21개 신규 컴포넌트를 발명하거나 새로운 Composite/Page Type을 만들지 않는다. 기존 정본을 덮지 않는 **읽기·구현 라우팅 자료**다.
>
> **구분:** D-012/v0.15/K03·K07 확정값은 보존. Brand Weak/Brand Outline 같이 명세에 후보로 남은 항목은 **proposed**, 실험 환경에서만 비교한다. 문서에 추가됐다는 이유로 자동 승인하지 않는다.

## 원칙: 네 가지 질문

1. **의미는?** 텍스트 의미 계층, Action의 실제 목적, 상태 의미를 먼저 결정한다.
2. **표현 축은?** Tone / Emphasis / Treatment / Size / State를 개별로 판단하되 무효한 조합은 늘리지 않는다.
3. **정본은?** F-TYPE·F-GEOMETRY는 숫자, C-*는 Anatomy·행동, P-*는 조합, S-PAGE는 전체 영역, 웹 CSS는 실제 표현을 맡는다.
4. **검증은?** 문서→공통 코드→서로 다른 소비자→실제 화면. 기존 화면의 표현과 새 계약의 차이는 기록하고, 페이지 override로 숨기지 않는다.

## 1. Typography 계열 · C-TEXT + F-TYPE

**의미 역할 5개**는 Display / Title / Section / Body / Caption이고, **시각 스케일은 별개**다. HTML h1/h2도 스타일 크기와 독립이다. 별도 폰트를 추가하지 않는다.

| 의미 역할 | CSS style / 기존 scale | 크기/줄높이/weight (기본 16 기준) | 대표 사용 |
|---|---|---|---|
| Title | root / type.root | 26/34/700 | Root 페이지명 |
| Title | standard / type.detail | 18/26/600 | Standard 헤더 제목 |
| Display | hero / type.hero | 24/32/700 | 목적이 명확한 짧은 핵심 문구 |
| Display | number / type.number | 24/32/700 | 강조 수치 한 개 |
| Section | section / type.section | 18/26/700 | 정보상 독립 섹션 제목 |
| Body | item / type.item | 16/24/600 | 반복 카드·목록의 이름 |
| Body | body / type.body | 16/24/400 | 일반 설명 |
| Caption | support / type.support | 14/20/400 | 기간·메타·도움말 |
| Caption | caption / type.caption | 12/18/500 | 매우 짧은 부차 라벨 |
| Body | number-small / type.numberSmall | 16/24/600 | 가격·랭킹 수치 |

**구현:** `.seed-text[data-text-role][data-text-style]`; style이 실제 스케일을 결정한다. 선택적 `data-text-weight=regular|medium|strong`은 굵기 조합만, `data-text-tone=secondary`는 색 역할만 바꾼다. `number`/ `number-small`은 tabular-nums. Style 미지정 시 5개 역할의 명시적인 기본 스케일을 적용하되, 위치(헤더/행/본문)에 맞는 style을 명시하는 것이 표준이다.

**표현 금지:** 문단 전체를 Brand Red로 채우거나, 동일 화면에 대형 제목을 여러 개 반복하지 않는다. 긴 목적 문장을 무조건 Display로 만들지 않는다. 필수 가격/오류/날짜를 Caption 12로 감춰서도 안 된다. 한글 긴 이름을 임의 생략했다면 전체를 읽는 접근 경로가 있어야 한다. 200% 글자 확대에서는 크기를 줄이지 않고 재배치한다.

## 2. Action 계열 · C-ACTION + C-ICON + P-ACTIONS

`variant`는 **색·강조의 허용 조합**, `size`는 **같은 외곽/타이포 규격**, `state`는 **이벤트의 처리 단계**, `layout`은 **아이콘/텍스트 배치**다. 서로를 대체하지 않는다.

| Variant | 강조 및 목적 | 상태 | 실제 CSS |
|---|---|---|---|
| brand-solid | High / 주 실행 | K07 허용 | 구현 |
| neutral-solid | High / 중립 확정 | K07 허용 | 구현 |
| neutral-weak | Medium / 일반 실행 | K07 허용 | 구현 |
| neutral-outline | Low / 일반 보조 | K07 허용 | 구현 |
| ghost | Low / 추가 설명·이동 | K07 허용 | 구현 |
| reward-secondary | Medium, 과업에 따라 High / **기존 서브브랜드 Gold** | D-012·K07 허용 | 구현 |
| brand-outline | 브랜드 맥락 Low | 기존 C-ACTION 후보, K07 미승격 | **실험용 opt-in** |
| brand-weak | 브랜드 맥락 Medium | 기존 C-ACTION 후보, K07 미승격 | **실험용 opt-in** |
| critical-solid/ghost | 실제 파괴 행위 | C-ACTION 설계 후보 | 미구현, 별도 semantic/대비 승인 필요 |

**공통 크기:** Small visible 34px + 실제 터치48, Default 43px + 실제 터치48, Large 48px. 반경16, glyph16~18(기본17), gap7. Label은 default/large 14/20·600, small12/18·600. `data-width=hug|fill`은 페이지 구성이 결정한다. `__face`가 배경·padding·radius·border를 하나만 소유하고, 실제 버튼/링크는 target이다. Label/Icon은 같은 중심 그룹이다.

**상태별 원칙:** Enabled/Pressed/Focus/Loading/Disabled. Loading은 label의 측정 폭을 유지하고 시각 표시를 교체하지만, **실제 중복 실행 차단은 호출자 로직**이다. `aria-busy`만 넣고 요청 차단까지 했다고 주장하지 않는다. Focus는 중복 ring 없이 확인. Disabled는 원인이 필요하면 인접 설명을 제공한다. 클릭 가능한 부모 안에 독립 버튼을 중첩하지 않는다.

**조합:** 화면 대표 High는 기본 최대 1개, 조회 화면은 0개 가능. 두 행동의 동일 크기/정렬/비중은 P-ACTIONS의 사례별 판단을 사용한다. 3개 이상 가로 버튼을 자동 허용하지 않는다. `reward-secondary`는 일반 안내 버튼에 Gold를 남용할 이유가 아니다.

## 3. Status family · C-STATUS

- **Badge:** `tone=neutral|brand|success|info|warning|danger|reward` 7개. 기본26, XS21, Pill, label 필수, icon/dot 선택. 필터·토글이 되면 Badge가 아니라 C-SELECTION. 객체당 일반 1~2개.
- **Progress:** 하나의 `progress-track` + `progress-fill`, `thickness=micro(6)|default(10)|featured(12)`. 0/620/1000/1200·loading·unavailable를 단일 입력에서 처리하고 실제 원본 초과값을 보존한다. 완료 게이지가 지급 완료를 뜻하지 않는다.
- **Credit Value:** C-CREDIT의 실제 래스터 재화+수치. 일반 Badge 또는 `coins.svg`로 대체하지 않는다.

## 4. Input/Choice family · C-FIELD / C-CHECK / C-RADIO / C-SEGMENT / C-TABS

- Field는 **Label → Control → Help/Error**. 기본/포커스/읽기전용/비활성/오류의 의미는 분리. Placeholder를 Label로 쓰지 않는다. 공통 `.seed-field-group`은 간격만, `.seed-field`는 48px 최소 Control + 단일 border focus.
- Checkbox는 독립 선택·수동 완료, Radio는 단일 그룹 배타, Segmented는 같은 데이터의 2~4개 보기, Tabs는 관련 영역 탐색. **생김새가 비슷해도 바꿔 쓰지 않는다.**
- Native input/label과 실제 disabled/checked/validation 상태는 보존한다. 액션처럼 보이는 Badge로 입력 상태를 흉내 내지 않는다.

## 5. Content family · C-LIST / C-IMAGE / C-SURFACE / C-ICON

- **List:** Prefix / Content(Title·Detail·Meta) / Suffix. 정적 행은 hover/pressed 없음. 56px 최소 행을 같은 공통 진입점에서 공유한다. 여러 줄이 중심이면 `data-align=top`, 짧은 값이면 center. 별도 서브 액션이 있으면 행 전체와 중첩 클릭을 금지한다.
- **Image:** contain(item/mascot) / cover(thumbnail/scene), loaded/loading/error, 원본 ratio와 상태별 외부 설명 보존. A1 물체를 늘려 A2 장면으로 부르지 않는다.
- **Surface:** Plain/Solid/Outlined/Subtle/Inset/Raised/Interactive/Media/Reward/Overlay 10개는 **표현 종류**이며 페이지 레이아웃 종류가 아니다. 의미 없는 카드 중첩 금지.
- **Icon:** 정본 56개 SVG. 단독 버튼 target48, glyph 크기는 C-ICON/K07이 정한다. 새 아이콘 도형을 만들지 않는다.

## 6. 변경 책임·테스트·중단 조건

| 바꾸려는 것 | 1차 소유 | 검증할 것 |
|---|---|---|
| 글꼴 크기·행간·굵기 | F-TYPE + C-TEXT + 공통 CSS | 5 역할과 10 스케일, Root 헤더·본문, 200% |
| 버튼 톤·높이·로딩 | C-ACTION / P-ACTIONS + 공통 CSS | 6 허용 Variant×3 Size, label 위치·48 hit·기존 gallery 격리 |
| 반복 행의 내부 축 | C-LIST + 공통 CSS | Prefix/Title/Suffix, 긴 이름, 독립 조작 |
| Field/Choice의 기본 상태 | 각 기존 C-* + 공통 CSS | label 연결, 선택 표시, focus, disabled |
| 페이지 안 섹션 배치 | P-PAGE-COMPOSE / P-LAYOUT + 페이지 CSS | 페이지 목적, Grid/Rail/Rows, Surface 독립성 |

**실행 프로토콜:** 정본/승인 여부 확인 → 바뀌는 책임 확인 → 기존 소비자·selector 충돌 조사 → RED fixture → 공통 소유자만 수정 → GREEN·legacy 회귀 → Chromium 실제 폭/테마·장치 검증 별도 기록. 문서 링크·정적 assert만 통과하면 `render-tested`라고 쓰지 않는다.

**실제 사용처:** `web/preview/seed-standard.css`가 웹 표현 정본이다. `web/preview/style.css`·`clan.css`는 레거시 지역 레이아웃 소유. 기존 표준 클래스 소비자는 자동으로 공통 개선을 적용받지만, 레거시 전용 클래스가 표준을 사용하지 않으면 적용되지 않는다. 이 차이는 임시 강제 재스타일링으로 덮지 않는다.

## 참고 · 외부 사례는 구조만 참조

- [당근 SEED Typography](https://seed-design.io/foundations/typography): 의미 텍스트 스타일과 스케일 스타일의 분리, 시스템 폰트.
- [당근 SEED Text](https://seed-design.io/react/components/typography/text): textStyle·semantic element·color·fontWeight·maxLines의 역할 분리.
- [당근 SEED Action Button](https://seed-design.io/components/action-button): Size·Layout·Variant·State·Width 독립 속성.
- [당근 SEED 개선 사례](https://seed-design.io/updates/how-seed-evolved): 필요 없는 스타일 개수를 줄이며 실제 위계 표현력을 높임.
- 참조는 다른 서비스의 값·폰트·색·XSmall 크기를 그대로 가져오라는 의미가 아니다.
