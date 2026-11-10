# SEED · Component Family Integration / 2026-11-10

## 범위·목표
- 브랜치: `feat/seed-layout-composition-20261110`.
- 요청: 승인된 디자인을 재설계하지 않고 Typography의 정밀 사용 규칙, Action Variant/Size/State/보조 브랜드 Gold, 나머지 Component 슬롯 디테일을 **같은 계열에 묶어 정본 규격과 실제 공통 스타일**에 연결.
- 우선 순서: **승인/수정 프로토콜 확인 → 당근 SEED 공식 사례 조사 → 기존 C-*/K03/K07/K09에 규격 반영 → `seed-standard.css` 단일 소유권 구현 → 사용처 시연/검증.**
- 금지: 새 Component 계층/컴포넌트 에디터, 새 폰트 배포, SVG 56 원본·PNG 8 원본 변경, 기존 브랜드 Red/Gold·토큰·버튼 visible height 변경, Android/main/new/DB/클랜 페이지 재구성.

## 근거 자료와 소유권
- 기존 정본: `docs/DECISIONS.md` D-011/D-012/D-013; `components/text-hierarchy.md` K03; `components/action-button.md` K07; `components/list-item.md` K09; `implementation/v015.md` K12; `implementation/shared-style-contract.md`의 2026-11-10 CSS ownership/RED→GREEN.
- 참고 자료: https://seed-design.io/foundations/typography (semantic vs scale), https://seed-design.io/react/components/typography/text (as/style/weight 분리), https://seed-design.io/components/action-button (Variant/Size/Layout/State/Width 분리), https://seed-design.io/updates/how-seed-evolved (불필요한 스타일 수를 줄이면서 역할 명확화).
- 원칙: 당근의 값/폰트/색/컴포넌트 전체를 복제하지 않음. 우리 정본의 목적과 수치가 우선.

## 변경 내용

| ID | 바뀐 것 | 정본/구현 | 승인 경계 |
|---|---|---|---|
| T1 | 5개 의미 역할/10개 style 스케일을 font-size/line-height/weight CSS 변수 매핑으로 연결. 헤더 root/standard, item/body/support/caption/number 구분 | C-TEXT + F-TYPE → `seed-standard.css` | 기존 수치만 사용, 과대 Hero clamp 미사용 |
| T2 | `data-text-weight` 강조와 `data-text-tone=secondary`, number tabular, opt-in 2행 ellipsis | C-TEXT / 공통 CSS | 필요 데이터 접근 경로 없는 truncation 금지 |
| A1 | K07 Action 6종 유지. Small34/Default43/Large48, target48, icon17/gap7/Radius16 보존 | C-ACTION / CSS | 기존 색/크기 변경 없음 |
| A2 | 기존 Gold `reward-secondary`를 서브브랜드 계열로 명시; `aria-busy=true` spinner overlay로 Label/Icon 측정 폭 보존 | C-ACTION / CSS | 호출자 중복 실행 차단·오류 복구 별도 |
| A3 | Brand Weak/Outline의 CSS를 명시적 `data-seed-family-preview` scope로만 구현 | 기존 C-ACTION 후보 / 실험 CSS | K07 승인 6종 확대 아님; Critical 미구현 |
| C1 | Field Label/Help 그룹, readOnly/disabled와 기존 단일 focus border 계약 | C-FIELD / CSS | 새로운 Field 시각값/토큰 없음 |
| C2 | List Prefix/Content/Suffix, top/center alignment, 최소56px, 정적 행과 링크의 상호작용 경계 | C-LIST / CSS | 화면/콘텐츠 전용 List 컴포넌트 없음 |
| C3 | Icon Action 48px target + 기존 24px SVG glyph, 기존 Badge7/Progress3/Surface10/Media 재사용 | C-ICON / 기존 C-*/CSS | SVG 원본·아트/경제 UI 변경 없음 |
| P1 | `web/seed-lab/families.html`에 계열별 비교 사례 | fixture HTML + `families.css/js` | Preview/제품 화면이 아니라 실험 |
| V1 | 공통 CSS 테스트 확장, Family 정적 테스트/모의 DOM 조작 확인 | `*.test.mjs` + 독립 검증 | 브라우저/실기기 통과 아님 |

## 실제 결과·정적 검증
- GitHub 파일·SHA로 반영 확인. 작업 기준 커밋 이후 수정은 **docs, `web/preview/seed-standard.css` 및 해당 테스트, 새로운 `web/seed-lab/families.*`**에 한정.
- HTML 태그 닫힘/고유 ID 검사 성공; 두 CSS(공통/fixture) 괄호 균형 정상; JS 모듈 import를 제거한 본문이 V8 구문 검사 통과.
- 기준 Variant6, Typography10, Progress thickness3/cases6, approved 색 및 Size34/43/48 소스 정적 계약 검사 통과.
- 실제 입력/시트와 혼동 방지: 진행률 fixture는 **native Radio group**을 사용. `0`, `620`, `1000`, `1200`, `loading`, `unavailable` 상태 조작.
- 모의 DOM에서 JS 실행 오류 없음. Size/Width 그룹 변환, 초과값 표기 + fill100 + aria-now1000, unavailable 숨김 + ARIA 제거, 620 복귀, 라디오 선택, 다른 보기 전환, checkbox, theme, icon 시연 등 **10개 시나리오 통과**. 브라우저 렌더 및 실제 DOM 엔진의 computed style 테스트는 아니다.
- `web/preview/index.html`, `web/preview/style.css`, `web/preview/clan.css` 직접 편집 없음. 따라서 **모든 legacy Preview 요소가 자동으로 새 표준을 따른다고 주장하지 않는다.** 표준 `.seed-*` 소비자에만 CSS가 자동 반영된다.

## 미검증·보류
1. **Node 프로젝트 전체 테스트:** 저장소를 체크아웃하여 `node --test web/preview/seed-standard.test.mjs web/seed-lab/*.test.mjs`를 실제 실행하지 못함. 위 검사는 GitHub에서 읽은 소스와 V8 모의 실행 기준.
2. **브라우저 실제 UI:** Pages 배포 완료/개별 경로 200 응답, 320/360/390/430/768/1440 폭×Light/Dark, 200% font, 버튼 hit target의 겹침/측정 크기·spinner label 흔들림, 색 대비 및 레이아웃 overflow는 별도 검증 필요.
3. **Native:** Compose, TalkBack, 실제 모바일 포커스/시트 제스처/IME 검증하지 않음.
4. **연결되지 않은 소비자:** legacy 갤러리의 `.gallery-button` 등은 의도적으로 분리되어 있으며 CSS 통일을 가장하기 위한 추가 override 없음.
5. **기존 승인 대비 검토:** Dark Gold CTA의 `#FFFFFF` on `#957027`은 sRGB 근사 대비 약 **4.12:1**로 보통 크기 텍스트 4.5:1 기준에 못 미칠 수 있다. 이것은 이번 작업에서 색을 바꿔 해결하지 않았으며, D-012 승인값 변경은 별도 사용자 결정 대상이다.
6. **새 후보의 승인:** Brand Weak/Outline·계열 시연장 결과가 기존 K07의 허용 조합을 자동 확대하지 않음. 사용자가 검토하기 전 제품에 배치 금지.
7. **사용 예시의 임의값:** 섹션 내부의 샘플 명칭·숫자는 fixture 예제이고 사용자/클랜/결제의 실제 데이터가 아님.

## 다음 수정 절차
1. 실제 스타일 문제가 나오면 `global token` / `Component variant-state` / `Pattern layout` / `Page content`로 분류.
2. 원래 K03/K07/F-TYPE/F-GEOMETRY와 구현 CSS 진입점 비교. 기존 계열에 있으면 새 컴포넌트 계층/override 만들지 않음.
3. 실패 재현 및 영향 소비자(기존 Preview, Family Lab)를 확인한 뒤 RED test부터 보강.
4. 공통 소유자 `seed-standard.css` 변경과 GREEN/legacy 검사. 실측 검증 여부는 구분.
5. 해당 수정이 D-012 숫자·색·SVG 원본을 바꾸는 경우 **승인 없이 진행하지 않고 보고**.

이 결과는 **SEED 패밀리 구현 1차 개편**이며 21개 전체 Composable 구현, 제품 시연 검증이나 Android 이관 완료를 의미하지 않는다.

## 보완 정정 · 기존 SEED Lab 통합 (2026-11-10)
- 사용자 요구의 시각화 대상은 **별도 `families.html` 페이지가 아니라 기존 `web/seed-lab/index.html` 자체**였음을 확인했다.
- 원래의 배치/구성, 동작/시트, 에셋 탭을 제거하지 않고, Typography·Action·Status·Input·List·Media·Surface의 동일 계열 예제를 `index.html` 첫 탭에 통합했다.
- `families.html`은 원래 링크의 호환용 이동 안내만 제공한다. 중복된 시연 페이지를 유지하지 않는다.
- 실제 디자인의 공통 구현은 변경하지 않는다. `families.css`는 배치, `lab.css`는 Lab 프레임에만 적용한다.
- Lab 첫 화면의 과도한 2행 Hero·전체 문장 브랜드 강조를 기존 승인 Root Title/Body 계층으로 대체했다. **타이포 수치·색상 토큰은 변경하지 않았다.**
- 런타임: 독립 모듈끼리 전체 DOM 이벤트를 공유하지 않도록 Family Action/Interactive 선택 범위를 컴포넌트 패널 안으로 제한했다. 테마는 원래 Lab 헤더를 사용한다.
- 검증: 통합 UI의 고유 ID, panel/tab 연동, 기존 필수 ID·asset path 보존, 두 JS module 구문 및 인터랙션 모의 실행, 기존 테스트 계약을 구분한다. 실제 Pages 배포 및 모바일 렌더는 검증 완료 전까지 미확인.

## 최종 요구 교정 · SEED Lab은 정본의 거울 (2026-11-10)

잘못된 접근: 별도 families.html → 기존 Lab 첫 탭에 컴포넌트 갤러리 → 여전히 SEED 전반을 빠뜨림.

교정된 구조: **SEED 정의 전체 지도 → 특정 규격의 공식 원문 → 관련 Component/Pattern/Shell/Assets/Implementation 시연**.

- INDEX에 등록된 38개 정본과 16개 부가 문서, 총 54개 항목을 GitHub 파일 경로·INDEX 상태·depends_on·sourceCommit 기준으로 표시.
- 시연은 정의를 설명하는 근거일 뿐, 페이지 생성기나 새 계층이 아니다. 기존 Layout/State/Asset 기능과 실제 시트 UI 보존.
- 원본 읽기와 클릭 테스트는 별개. 원문 네트워크 요청 및 GitHub Pages/실기기 화면은 최종 검증 전에 완료로 선언하지 않는다.
- 공통 스타일시트는 이 UI 변경에 의해 수정되지 않는다. `seed-map.css`는 Lab 문서 뷰어만 스타일링하며 승인값을 소유하지 않는다.

## 사용자 교정 반영 · SEED Lab 시각화 · 정본 → 실제 프레임 → 검증

사용자 교정: SEED Lab은 Markdown 본문을 보여주는 문서 뷰어가 아니다. 어떤 규격과 역할이 **실제 화면에 어떤 차이로 나타나는지** 보여주는 검사면이다.

- **전체 지도:** 6개 정의 영역(Foundation, Component, Pattern, Shell/Navigation, State/Gesture, Assets/Evidence)을 선택해 실제 시연 탭으로 이동. 정본 원문 54개는 보조적인 접이식 브라우저로 이동.
- **Foundation:** 기존 `seed-standard.css` 변수의 실제 Canvas/Surface/Brand/Reward/Foreground/Secondary, K03 Typography, Geometry 간격/터치, Outline/Fill SVG를 표현. 새로운 색상·수치를 제안하지 않음.
- **Pattern:** 세로/가로/격자/겹침 네 가지 공간 관계와 `목적 → 정보·행동 → 의미 섹션 → 배치·기존 컴포넌트` 결정 절차를 시각적으로 표시. 레이아웃은 콘텐츠명이 아니다.
- **Shell & Navigation:** 기존 Shell 3종 UI를 독립 탭으로 이동. `seed-visual.mjs`가 Root/Detail/Utility/Immersive/Overlay 5개 역할을 시연하고, 상단 Root/Standard 헤더, 오른쪽 Outline 아이콘 액션, Bottom Navigation Outline/Active Fill, 선택적 고정 CTA, C-SHEET 실제 팝업을 연결.
- **승인 경계:** Root/Standard 상단 구조 D-011은 확정. Bottom Navigation의 활성 Fill 시각은 D-012로 확정됐으나 탭 수·순서·목적지는 O-003 미결. 시연상의 홈/DM/클랜/마이는 **제품 확정 IA가 아닌 예시**.
- **소유권:** 버튼·타이포·토큰·SVG는 정본 그대로. `seed-visual.css`는 새로운 Lab 시연의 배치만 소유. `lab.js`에서 기존 Shell이 `#page-shell-bottom`을 문자로 덮는 회귀를 차단. `seed-map.mjs`는 시연별 정본 링크를 접이식 원문 뷰어로 연결.
- **기존 기능 보존:** Badge/Progress, Button/Input, Grid/Rail/Rows, Sheet Footer/Drag, 원본 PNG 8 / SVG 56, Surface, 공통 CSS 유지.

### 실행 및 검증 경계

- 브랜치 소스 확인 및 정적 검수: 7개 탭, 원래 Shell 단일 인스턴스, 기존 sheet/asset ID 유지, JavaScript 파싱, HTML 태그/ID 및 CSS 괄호 검증.
- 브라우저/Node 전체 실행, 포커스·모바일 터치, 320/360/390/430 폭·테마/200% 확대, 실제 PNG 디코드 및 Sheet 스크롤 제스처는 **별도 확인해야 함**.
- 페이지 이름과 실제 도메인 플로우의 Shell 소속을 새로 확정하지 않는다. 시연 샘플과 승인 계약을 구분한다.
