---
id: SEED-SHARED-STYLE
category: implementation-contract
status: proposed
read_when: SEED 컴포넌트/배치 규격을 웹 CSS 또는 Android Compose에 반복 없이 적용하거나 공통 스타일·페이지별 예외를 수정할 때
depends_on: [SEED-V015-ACCEPTED, F-GEOMETRY, F-TYPE, F-COLOR, C-ACTION, C-SURFACE, P-LAYOUT, P-PAGE-COMPOSE]
references: [D-011, D-012, D-013]
---

# 표준 스타일 적용 체계 · 규격을 코드에서 재사용하기

> **사용자 요구의 핵심:** SEED에 이미 정의한 값과 역할을 매 화면·매 요소에서 수동으로 대조·복사하지 않는다. 같은 컴포넌트와 배치 구조는 **공통 스타일/구현을 참조**하고, 토큰·기본 규격의 변경이 해당 사용처에 일관되게 전달되게 한다. 이 문서는 **웹의 스타일시트를 비유로** 설명하며, Android에서 CSS가 그대로 작동한다고 주장하지 않는다.
>
> **상태:** 제안 규격 및 구현 가이드. 기존 v0.15 승인값과 D-011/D-013를 변경하지 않으며 이번 문서 등록 자체가 웹·Android 공통 스타일 구현 완료를 뜻하지 않는다.

## 2026-11-10 · 웹 구현 범위 (Android 미착수)

**현재 구현 소스:** [web/preview/seed-standard.css](../../../../web/preview/seed-standard.css) → [style.css](../../../../web/preview/style.css) → [clan.css](../../../../web/preview/clan.css) 순서로 불러온다. 이것은 같은 SEED 토큰과 공통 스타일을 공유하는 **실제 웹 코드**이며, 이전의 문서상 약속만 남은 상태와 다르다.

| 웹 표준 클래스 | 현재 연결 | 보존 원칙 |
|---|---|---|
| `.seed-text[data-text-role="caption"]` | 기존 `.muted`와 같은 선언 | 글자 수치 유지. 다른 4개 텍스트 역할 전체 구현을 주장하지 않음 |
| `.seed-action[data-variant][data-size]` | v0.15 웹 어댑터의 기본 Action + 내부 `__face`/`__label`/`__icon`; 기존 `.gallery-button`과 분리 | 기본 14/20, Small 12/18, face 34/43/48, 중립 Outline의 선은 face 하나 |
| `.seed-media` + 하위 label/fallback | 기존 `.media-frame`, `.media-label`, `.media-fallback` 별칭 | 1:1 비율, 이미지 72%·보상 86% 점유 규격 보존 |
| `.seed-badge[data-tone]` | 기존 `.badge` 별칭과 3종 실제 시연 | 기존 배지 텍스트·padding 유지 |
| `.seed-progress-track` / `.seed-progress-fill` | 기존 `.progress-track` 병용 | 실제 `--progress` 33.333%/58.333% 원본 유지 |
| `.seed-icon` | 기존 공식 SVG mask 처리 | 56개 정본 경로 변경 없음 |
| 공통 Foundation/Shell | 기존 `:root` 토큰·반응형·Header/Nav/Sheet 규칙을 표준 파일로 이동 | CSS 선언값 복사·추출만 하고 수치 교정 안 함 |

**실제 전환:** 이 문단의 초기 전환(2026-11-10 오전)은 레거시 갤러리 클래스와 새 표준 클래스를 병용했다. 이후 Action의 Cascade 충돌이 확인되어 **`.gallery-button`만 `web/preview/style.css`에 분리**하고, 표준 `.seed-action`은 `seed-standard.css`에서 독점한다. 나머지 기존 클래스 Alias(`.seed-media`/`.seed-badge` 등)는 이 변경에서 손대지 않는다. 기존 콘텐츠, JS, 데이터·이미지 파일은 보존한다.

**수치 충돌의 처리:** 현행 preview 버튼 높이·색상 등과 v0.15에서 승인한 기준 사이에 차이가 남아 있다. 사용자가 **비율·데이터를 임의 변경하지 말라**고 요청했으므로, 표준 파일은 **현재 웹 시연값을 정확히 보존**했다. 문서/구현 간 차이의 조정이나 모든 Variant의 전체 구현은 별도 승인 대상이다.

**실행 상태/검증:** `web/preview/seed-standard.test.mjs`에 클래스 연결과 현재 값/비율/데이터 보존 검사를 추가했다. 이는 문서·정적 코드 계약이며 사용자 육안 비교나 Compose 검증을 대체하지 않는다. 다음 작업에서 웹 공통 스타일을 사용하는 컴포넌트는 우선 `seed-standard.css`의 실제 구현을 인용한다.

## 2026-11-10 · Action CSS 소유권 교정 사례 (실물 반례)

**시작 증상:** SEED Lab에서 `neutral-outline` 초기화 버튼의 테두리가 이중으로 보이고, 실행 버튼 글자가 다른 UI보다 커졌다. Chromium 최소 재현에서 outer/face border가 각각 1px이고 신규 Action이 16px 상속 글자를 사용함을 확인했다. 기존 갤러리의 `.gallery-button.seed-action`도 신규 기본 스타일 `font:inherit`와 `padding:0`의 영향을 받았다.

**원인:** `seed-standard.css`에 있던 구형 `.gallery-button, .seed-action` 혼합 선택자 및 `.seed-action[data-variant="neutral-outline"]`가 외부 target의 표현을 소유했고, 뒤늦게 추가된 내부 `.seed-action__face`도 같은 표면을 그렸다. 뒤에 선언된 `.seed-action { border:0 }`만으로는 앞선 속성 선택자의 더 높은 specificity를 무효화하지 못한다.

| 책임 | 정본 / 수정 지점 | 이번 교정 |
|---|---|---|
| 의미·타이포·높이 | C-ACTION / F-TYPE / F-GEOMETRY / v0.15 | 일반 14/20, Small 12/18, face 34/43/48과 비중첩 target 48 이상 유지 |
| 표준 표현 | `web/preview/seed-standard.css` | `.seed-action`은 실제 target+폰트, `__face`는 내부 패딩·반경·선·Variant. 표현은 한 곳 |
| 구형 시연 전용 표현 | `web/preview/style.css` | `.gallery-button`만 적용. SEED 표준과 같은 요소에 중복 클래스 부여 금지 |
| 페이지별 배치 | `web/seed-lab/lab.css` | `.lab-actions`는 flex/간격만. font-size·border·padding·height override 금지 |
| 사용처 | `web/seed-lab/index.html` | 승인된 `seed-action + data-variant + data-size + __face` 조합을 그대로 사용 |
| 회귀 증거 | `web/seed-lab/action-style.test.mjs` + `web/preview/seed-standard.test.mjs` | 소유권·별칭 충돌·14/12·34/43/48·기존 예시 데이터 보존 검증 |

### 이후 모든 공통 스타일 수정의 처리 순서

1. **요구의 책임 확인:** Foundation 숫자 변경인지, C-ACTION Variant/state인지, P-LAYOUT 배치인지, 페이지 데이터인지 먼저 판정한다.
2. **기존 정본 재사용:** 승인 명세를 바꾸지 않는 버그면 공통 CSS 구현만 수정. 페이지 override나 새 버튼 variant를 만들지 않는다.
3. **충돌 영향 조사:** 같은 이름의 구형 클래스·selector specificity·CSS load order·다른 화면 사용처 확인. 역사적 실험용 클래스를 신규 SEED 정본과 섞지 않는다.
4. **RED → GREEN:** 실제 회귀를 재현하는 테스트를 먼저 실패시킨 뒤 공통 소유권에서 고친다. 최소 버튼 둘(Brand Solid / Neutral Outline), Light/Dark, Small/Default/Large와 기존 preview를 함께 검증한다.
5. **변경 범위 보고:** Node 정적 검사, Chromium computed style/겹침/버튼 hit target, 320/390/430 및 긴 문구 결과를 별개로 기록한다. 브라우저 미검수면 완료로 선언하지 않는다.

**이번 범위:** CSS 충돌 교정이지 새로운 디자인 토큰/컴포넌트의 승인이나 Android 구현 변경이 아니다. 구형 preview는 역사적 샘플이므로 자체 표현을 유지하되 SEED 클래스 사용처로 계산하지 않는다.

---

## 1. 세 가지를 분리한다

| 구분 | 정본과 책임 | 매 페이지가 하는 일 |
|---|---|---|
| **디자인 규격** | 승인된 컴포넌트의 목적·Anatomy·Variant·상태·금지 사용·숫자. [v0.15](v015.md), [토큰 JSON](v015-tokens.json), 해당 Component/Pattern | 어떤 컴포넌트·Variant가 적합한지 선택 |
| **표준 스타일 구현** | 위 규격을 한 번 실제 코드로 표현한 재사용 단위. 웹 CSS 클래스/변수, Android의 Theme·공통 Composable/Modifier 등 | 표준 구현을 호출하고 허용된 속성만 전달 |
| **페이지 자체의 구성** | 섹션의 채택·순서·배치·크기 비율·실제 내용·화면 데이터. [페이지 구성](../patterns/page-composition.md)과 [배치 규격](../patterns/layout-composition.md) | 내부 배치 매개변수 및 데이터 지정. 공통 버튼·글자 스타일을 재정의하지 않음 |

**설계 문서와 실제 CSS·Compose는 같은 코드가 아니다.** 에이전트가 매번 문서의 padding·radius를 복사하는 대신, 실제 구현된 표준 클래스를 사용하도록 연결하는 것이 목표다. 문서에 숫자를 두 번 저장하거나 렌더 시 Markdown을 파싱할 필요는 없다.

## 2. 공통 의존성의 전파 방식

```text
[승인 SEED 계약 + 디자인 토큰(JSON)]
                    │ 구현 시 한 번 매핑
        ┌───────────┴────────────┐
        │                        │
 [웹: CSS 변수 + 공통 클래스]    [Android: Theme + 공통 Composable]
        │                        │
 [페이지: 클래스·Variant]       [페이지: 컴포넌트·Variant 인자]
        └───────────┬────────────┘
                    ▼
             각 페이지의 실제 UI
```

- 여러 컴포넌트가 **동일한 의미 토큰**을 참조하면 해당 토큰의 확정 변경이 그 참조처에 함께 전달된다. 예: radius.control, action size, brand semantic color.
- 각 컴포넌트의 기본 크기/상태/모양은 한 구현이 소유한다. 페이지마다 숫자·선택자·Modifier를 복제하지 않는다.
- 한 컴포넌트만 달라야 하는 경우 **기존 승인 Variant/size/state**를 선택한다. 새 스타일 이름이나 페이지 전용 override를 먼저 생성하지 않는다.
- Variant로 설명되지 않는 새로운 요구는 기존 계약에 속하는지, 페이지 배치에 속하는지 판단한다. Component 내부 규격 변경이면 공통 구현과 문서, 페이지 배치라면 페이지 로컬 구조를 수정한다.
- **자동 연결의 의미:** 부모 컴포넌트가 사용하는 공통 스타일·토큰·하위 슬롯을 **코드의 참조 관계**가 연결한다는 뜻이다. 문서끼리 링크되어 있다는 것만으로 CSS에 자동 반영되는 건 아니다.

## 3. 웹에서의 실제 사용 방식

### 3.1 표준 요소마다 기본 클래스 + 명시적 Variant

기존 [v0.15 웹 어댑터](v015.md)의 이름을 **바꾸지 않는다**. 이미 명세된 사례:

| 범주 | 기존 계약의 진입점 | 기준 소유자 |
|---|---|---|
| 텍스트 | `.seed-text[data-text-role][data-text-style]` | 5단계 글자 역할·색·크기 |
| 버튼 | `.seed-action[data-variant][data-size]` 및 내부 `.seed-action__*` | Action의 패딩·Radius·높이·상태·아이콘 |
| 미디어 | `.seed-media[data-media-kind][data-media-state]` | 이미지 비율·fit·로딩·폴백 |
| Progress | `.progress-track[data-thickness][data-progress-state] > .progress-fill` | 트랙/Fill/상태와 색·두께 |
| SVG | `.seed-icon` 및 공식 56개 소스 | 원본 glyph·규격/색 적용 |
| 페이지 골격 | 기존 Page Shell과 공통 gutter/max-width | 상하단·본문 폭·safe inset |
| 공간 배치 | [네 가지 기본 배치](../patterns/layout-composition.md) | 관련 구조가 반복될 때 공통 클래스/유틸리티 추출 후보, 이름·코드는 아직 미승인 |

사용 예시 (기존 문서의 구조를 설명하는 *개념 예시*이지 현재 preview에서 동작 확인한 코드가 아님):

```html
<button class="seed-action" data-variant="brand-solid" data-size="default">
  <span class="seed-action__face"><span class="seed-action__label">시작하기</span></span>
</button>
```

표준 CSS 구현이 이 기본 클래스와 Variant를 처리하면 새 페이지는 radius/padding/height 등을 반복 기입하지 않아도 된다. 단, 실제 Variant 값은 **C-ACTION/v0.15의 승인 목록**에서만 정한다.

**배치**는 페이지의 가변적인 속성(예: Grid 열 수, Weighted 좌우 비율)과 공통 규칙(간격·overflow·전환)을 나눈다. 모든 레이아웃 구조에 새 CSS 클래스를 양산하지 않는다. 반복적으로 쓰이고 기존 CSS/컴포넌트 슬롯으로 해결되지 않을 때만 공통 유틸리티로 추출한다.

### 3.2 스타일 적용 계층

1. 디자인 토큰과 의미색(공통 변수)
2. 승인된 Component 기본 클래스
3. 승인된 Variant/size/state 선택자
4. 화면 고유 **배치**(섹션 크기·순서·열 개수·구체적 비율)

페이지 CSS는 4를 소유한다. 공통 컴포넌트의 font-size, radius, padding, color를 4에서 다시 정의하면 안 된다. 제품 맥락상 진짜로 다른 역할이라면 컴포넌트 계약의 Variant 검토가 먼저다.

**캐스케이드 충돌:** 페이지 CSS가 같은 specificity 또는 더 높은 specificity로 기본 컴포넌트를 덮는 방식은 공통화를 깨므로 피한다. 새로운 CSS Layer나 CSS-in-JS 도구를 도입해야 한다는 뜻은 아니다. 현행 정적 CSS에서 이 우선순위를 유지하면 충분하다.

## 4. Android에서는 어떻게 대응하나

Android 앱은 CSS의 `class`를 화면에 붙이는 구조가 아니다. 현재 앱의 **Jetpack Compose**에서는 다음처럼 같은 목적을 달성할 수 있다.

| 웹 개발자가 아는 개념 | Compose 대응 (설계 대응, 현행 구현 보장 아님) |
|---|---|
| `:root` 변수 / semantic token | 앱 Theme의 `ColorScheme`, `Typography`, 프로젝트 공통 `Dimensions` 같은 토큰 객체 |
| `.seed-action` 공통 버튼 클래스 | `SeedAction` 같은 재사용 `@Composable` 함수 + 승인 Variant 인자 |
| `data-size`, `data-state` | Composable의 크기/상태 매개변수(타입으로 제한 가능) |
| 공통 CSS Slot 스타일 | Composable 내부의 Icon/Label/Progress/Media Slot과 Modifier |
| `display:flex; flex-direction:column` | `Column` + `Arrangement.spacedBy(...)` 및 정렬 `Modifier` |
| Grid / Rail | `LazyVerticalGrid` / `LazyRow` 등 표준 레이아웃, 공통 간격 토큰 |
| 페이지 전용 CSS의 배치 | 개별 화면의 `Row/Column/Box` 계층 및 크기·weight 매개변수 |

**핵심:** Android도 화면마다 패딩 값을 다시 적는 게 아니라 `SeedAction(...)`을 호출하고, 그 내부 구현이 공통 Theme와 스타일을 참조한다. 버튼의 기본 패딩 변경은 공통 Composable에 반영해 사용하는 화면에 전달된다.

다만 Compose는 CSS처럼 임의의 부모 클래스가 하위 요소에 모든 스타일을 상속하지 않는다. **명시적 Theme 참조와 Composable 호출**이 실제 연결 기제이다. 기본 Compose 컴포넌트를 즉시 전부 새 Wrapper로 감쌀 필요는 없고, SEED가 독자적 Variant/상태 계약을 가진 컴포넌트부터 일관된 단일 진입점을 정한다.

이 문서는 Android 새 클래스/파일 경로를 확정하지 않는다. `new`에는 아직 Android 앱 코드가 이관되지 않았으며, `main`의 실제 UI 구조와 충돌을 확인한 뒤 연결해야 한다.

## 5. 변경 요청을 받았을 때의 위치 판단

| 요청 | 먼저 고칠 곳 | 잘못된 대응 |
|---|---|---|
| “모든 기본 버튼의 패딩을 바꿔” | SEED 승인 범위 확인 → 공통 버튼 구현·해당 토큰 | 페이지 버튼 수십 개 각각 수정 |
| “이 화면에서 버튼을 오른쪽에 배치해” | 페이지의 가로/세로 배치·정렬 | Button 자체의 패딩·스타일을 변경 |
| “일부 버튼은 작게” | 기존 small size 사용 | 해당 페이지에서 버튼 높이 강제 override |
| “그림 위의 배지를 모서리에 붙여” | Image Frame의 기존 Overlay/Anchor 슬롯 | 새 범용 Layer를 무작정 중첩 |
| “상점 카드가 너무 빽빽해” | Grid의 열/간격·셀이 감당할 정보 슬롯 | 모든 Card의 내부 기본 padding 축소 |
| “확정한 브랜드 색상 변경” | 승인 토큰 정본 → 웹 Theme/Compose Theme 매핑 | 개별 CSS 색상 문자열 대량 치환 |
| “클랜만 전혀 다른 카드 외형으로” | 콘텐츠 목적과 기존 Surface Variant부터 재검토 | 페이지 전용 CSS에서 전역 Card 덮기 |

변경 전 한 번 판단할 질문: **이는 (1) 전역 규격, (2) 컴포넌트 Variant/State, (3) 레이아웃 배치, (4) 페이지 콘텐츠 중 어디의 책임인가?** 영역이 다르면 수정 지점도 달라진다.

## 6. 실제 저장소 현황과 남은 간극 (2026-11-10 조사)

- **있음:** [v0.15](v015.md)에 `.seed-text`, `.seed-action`, `.seed-media`, `.progress-track`의 **웹 표준 마크업 계약**이 명시되어 있다.
- **일부 있음:** `web/preview/style.css`에는 공통 CSS 변수와 `.seed-icon` 구현이 있다.
- **현재 조사에서 찾지 못함:** 같은 `style.css`, `clan.css`에는 `.seed-action`, `.seed-media`, `.seed-text` 선택자 구현이 없다. **문서상 계약을 완전한 구현으로 간주하지 않는다.**
- **실제 값 간 차이도 있음:** `v015-tokens.json`의 Light Brand 색상 `#DB3040`과 현행 `web/preview/style.css`의 `--brand: #f04e5b`은 일치하지 않는다. 모두 고쳐졌다고 주장할 근거가 없다.
- **이번 범위 밖:** Android 화면들의 실제 공통 Composable 연결, 웹 CSS 정비, 정본 토큰과 preview 테마의 차이 해소, 컴포넌트별 전 상태 렌더. 후속 구현에서는 문서와 실제 클래스가 한 쌍인지 확인해야 한다.

이 차이는 **추가 규격을 임의 발명할 이유가 아니라, 기존 승인 계약과 현행 구현을 연결해야 한다는 근거**이다. 서로 다른 값이 의도된 미승인 미리보기인지 버그인지는 [DECISIONS](../../../DECISIONS.md)와 [OPEN](../../../OPEN.md)의 승인 상태를 함께 판정한다.

## 7. 구현할 때 필요한 최소 대응표

문서의 '공통화'를 실행 가능한 변경으로 연결할 때, 모든 값을 수동 복사한 장부 대신 **컴포넌트 한 줄당 소유권 하나**만 기록한다.

| 확인할 속성 | 기록 대상 |
|---|---|
| 의미적 이름 | Button / Media / List / Progress / Layout 등 |
| 승인 규격의 링크 | v0.15와 기존 Component/Pattern |
| 웹 진입점 | 클래스 및 Variant 속성·현재 파일 |
| Android 진입점 | 실제 확인된 Composable/Theme 경로. 미이관이면 `pending` |
| 사용한 공통 토큰 | 색·패딩·반경·타이포 등 의미 키 |
| 예외 | 승인 Variant, 플랫폼 적응, 사용자 승인 미결 |
| 구현 상태 | `documented` / `implemented` / `render-tested`는 구분 |

이는 각 페이지 요소를 일일이 문서와 맞춰 적으라는 것이 아니다. **표준 구현을 한 번 연결하고 페이지는 그것을 사용**하게 하려는 최소 인터페이스 기록이다.

## 8. 기대하는 결과와 비목표

**성공:** 공통 버튼을 쓰는 두 화면 중 하나에서 개별 패딩 코드를 제거해도 같은 스타일로 렌더된다. 토큰/컴포넌트 규격이 수정되면 해당 공통 사용처에 함께 반영되고, 페이지 고유의 Grid 비율은 유지된다. 문서만 고쳤다고 UI가 자동으로 바뀌었다고 주장하지 않는다.

**실패:** 페이지마다 버튼의 padding/radius를 다시 쓰는 구조; '같은 디자인이니까'라는 이유로 모든 Surface를 하나의 거대한 Wrapper로 합침; 예외 때마다 새 공통 Variant 추가; 기존 SVG/원본 아트 임시 대체; CSS Preview 동작을 Compose 구현 완료로 오인.

이번 문서는 기존 SEED 구조를 유지하면서 **표준 스타일을 코드에 연결하는 계약**만 추가한다. 새 라이브러리/코드 생성기/컴포넌트 레지스트리/에디터는 요구하지 않으며 사용자가 별도로 요청하기 전에는 만들지 않는다.


---

## 2026-11-10 · SEED Lab 시각 교정과 S-PAGE 골격 웹 매핑

이 후속 구현은 **새 Page Type/Composite를 만들지 않고**, 기존 `S-PAGE`의 공간 책임을 CSS 기본 단위로 재사용한다. `P-PAGE-COMPOSE`는 목적·섹션 선택을, `S-PAGE`는 헤더·본문·하단 슬롯 경계를, `C-SURFACE`는 개별 그룹의 표현을 계속 소유한다.

| 영역 | 실제 웹 인터페이스 | 금지/경계 |
|---|---|---|
| Shell 공간 기본형 | `.seed-page-shell`, `__header`, `__inner`, `__content`, `__section`, `__bottom` | Header 아이콘·라우트·네이게이션 목적지는 구현하지 않음 |
| Bottom Sheet Footer | `.seed-sheet__footer[data-layout="paired|stacked|single"]` 및 `hidden` | Action 버튼 타입이 아닌 기존 버튼의 조합·배치 |
| 조작 경계 | `--seed-stroke-control` / C-ACTION Outline, C-FIELD, C-CHECK, C-RADIO | 장식용 `--line`을 조작 경계에 재사용하지 않음 |
| 이미지 상태 배지 | `.seed-media .seed-media__badge[data-tone="neutral"]` | 기존 7개 Status Badge 의미색 외 새 임의 variant 없음 |
| Lab 지역 배치 | `web/seed-lab/lab.css` | 공통 Action 내부 padding/font/색 오버라이드 금지 |

**근거/검증:** [시각 평가 보고서](../../../superpowers/reports/2026-11-10-seed-lab-visual-review.md), [정적 회귀 테스트](../../../../web/seed-lab/visual-polish.test.mjs). 해당 페이지의 Root/Detail/Utility 선택은 실제 앱 라우팅이나 네이티브 시각 승인으로 승격하지 않는다.

## 2026-11-10 · 계열별 구현 수렴 (기존 시드 외형 보존)

[Component Family Map](component-families.md)에서 기존 C-TEXT/C-ACTION/C-LIST/C-FIELD/C-STATUS/C-IMAGE/C-SURFACE를 계열별로 찾아간다. 실제 공통 웹 진입점은 `web/preview/seed-standard.css` 한 곳이며 지역 CSS는 섹션 배치만 소유한다.

- 텍스트: K03의 **role / style / HTML heading 분리**. 5의미 역할을 각기 승인된 스케일로 매핑하고 동일 속성 재정의는 공통 코드에서만 한다. 실험용 큰 도입 문구를 새로운 Font Token이라고 부르지 않는다.
- 버튼: K07 허용 6색/강도, size3, width2, 실제 상태 분리. 기존 Secondary Gold를 재사용하며 Brand Weak/Outline은 **실험용 후보 scope**로만 제공한다.
- List/Field: 기존 K09·C-FIELD Anatomy에 맞춰 표준 class가 슬롯·간격·Control 상태를 소유하도록 보완한다. 새로운 화면별 ListCard나 검색 화면을 정의하지 않는다.
- Badge/Progress/Media/Surface/Choice: 이미 승인된 7 Tone, 3 Thickness, 10 Surface, fit·상태 등 속성을 같은 계열로 묶는다. 추가 스타일 복제보다 기존 속성의 정확한 연결을 우선한다.

**자동 반영의 범위:** 페이지에서 `seed-*` 정본 클래스를 실제로 쓰는 부분은 공통 CSS 변경의 영향을 받는다. `.gallery-button` 같은 독립 레거시 표현과 Android Compose는 자동 변경되지 않는다. 프리뷰 지역 파일에 공통 폰트·Radius·색 Override를 추가해 "동일해 보이게" 만드는 방법은 금지한다.

프로토콜: 책임·현재 값 확인 → 의존 관계·Cascade 조사 → RED 재현 → CSS 정본 교정 → GREEN/legacy 회귀 → 실제 렌더 검증 별도. 이번 추가 문서는 기존 D-011~013 및 Android 구현 승인을 확대하지 않는다.

## 2026-11-10 · 페이지 유형/Navigation Set/Image Frame 단일 소유권

- **Page Type 4종**(Global/Global Inner/Non-Global/Bottom Sheet)은 [S-PAGE](../shells/page-shells.md)의 가시성 상태 계약. Shell 5역할의 별도 표시. 공통 `seed-page-contract.mjs`와 `seed-standard.css`를 참조하며 페이지별 임의 Bottom Nav on/off를 제거한다.
- **Top/Bottom Navigation**은 [TOP_NAVIGATION](../../../TOP_NAVIGATION.md)와 [C-BOTTOM](../components/bottom-navigation.md)을 사용. `new` 웹 시각값 보존, 새 Glyph 생성 금지.
- **Item Image Frame**은 기존 [C-IMAGE](../components/image-frame.md)에서 담당, Loading/Loaded/Error와 아이템 상태를 분리. Lab은 정본의 시연 소비자다.
- 계약의 보장은 이 파일을 참조하는 **웹 소비자 범위**. Android Compose는 동일한 분류/네비게이션/프레임에 대한 별도 구현·검증이 필요하다.

## D-015 승인 헤더·네비 동기화 (2026-11-10)

이전의 상단 헤더 `850 / -.05em` 및 화면별 간격 보정은 웹 공통 구현값이 아니다. **단일 스타일 소유자**는 `web/preview/seed-standard.css`이고, `.head`(기존 preview)와 `.seed-top-nav__title`(SEED Lab/신규 웹 화면)이 같은 Pretendard Variable·Title 수치를 소비한다. 헤더 구성 선택은 `seed-page-contract.mjs`의 네 페이지 타입 가시성 계약과 D-011 Root/Standard 역할에 따른다.
최종 Root `1.30rem/650`, Standard `1.08rem/620`, Glyph `1.59rem`, Target `3rem`, 최소 높이 `3.5rem`을 별도 페이지 CSS에 복제하지 않는다. Lab의 얇은 A/B variant CSS는 확정 후 삭제됐다. 웹 하단 탭의 아이콘 경로는 `WEB_NAV_ITEMS`가 소유하며 Lab과 preview가 같은 파일 세트를 참조한다. 기존 일반 `C-TEXT` 스케일/버튼·이미지·카드 규격을 헤더 때문에 바꾸지 않는다.
규격은 [NAV-TOP](../../TOP_NAVIGATION.md) 및 [v015-tokens.json](v015-tokens.json)에 기록했다. Node 회귀 검사는 `web/seed-lab/page-type.test.mjs`로, 실제 화면의 광학적 판단과 Native는 별도다.
