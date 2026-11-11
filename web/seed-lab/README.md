# SEED Lab · 웹 패턴 검증장

실제 클랜/상점/홈 페이지가 아닌 **디자인 SEED의 구성·컴포넌트·상태를 검증하는 임시 웹 실험 환경**이다. 앱 데이터, 보상 지급, 계정, Android 소스를 사용하지 않는다.

## 실행

저장소 루트에서 정적 서버를 실행한다.

```bash
python3 -m http.server 8000
```

브라우저에서 `http://localhost:8000/web/seed-lab/`로 접속한다. SVG와 PNG의 상대 경로가 루트의 `assets/`를 가리키므로 `web/seed-lab/` 자체를 별도 서버 루트로 열면 안 된다.

Node.js 22 이상이라면 정적 검사:

```bash
node --test web/seed-lab/*.test.mjs
```

## 시험 중인 규칙

| 실험 | 구성 | 관찰할 계약 |
| --- | --- | --- |
| 01 | 동일한 오브젝트의 Grid/Rail/Rows 변경 | C-IMAGE 소유 영역과 레이아웃 책임 분리, 콘텐츠 보존 |
| 02 | 동일 데이터의 Plain/Solid 섹션 | 섹션 ≠ 카드, 정보 계층 |
| 03 | 0/620/1000/1200/조회 불가/로딩 + 6/10/12 진행률 | K08 실제 수치와 ARIA, 초과값 보존·fill 상한 |
| 04 | 액션/체크/라디오/필드/시트 | 컴포넌트 상태, 명확한 동작 및 포커스 |
| 05 | 공식 PNG 8종 + manifest SVG 56종 | 원본 단일 출처, 투명 이미지 contain, 활성 Fill 분리 |
| 06 | Surface 역할 10종 | 표면과 공간 배치의 책임 분리 |
| 07 | Root / Detail / Utility 공통 골격 | S-PAGE의 Header/본문/하단 슬롯만 재사용, 새 Page Type 아님 |
| Sheet Footer | 없음 / 단일 / 이중 / 세로 | 기존 C-ACTION의 크기·톤을 조합. Body 스크롤과 Footer 고정 분리 |

공통 표현은 `../../seed/styles/seed-standard.css`가 소유하고, `lab.css`는 테스트 콘텐츠의 배치만 소유한다.

### 2026-11-10 · 시각 개선에 따른 실제 파일 책임

- `seed-standard.css`: `--seed-stroke-control` (조작 경계), C-CHECK/C-RADIO 스타일, `seed-sheet__footer[data-layout]`, `seed-page-shell` 및 `__header/__content/__section/__bottom`의 공통 골격. 기존 디자인 토큰과 이미지 파일은 보존한다.
- `lab.css`: 소개·섹션 헤딩·Grid/Shell 데모의 상대 배치, 반응형 실험 구성만 소유한다. 공통 버튼·Field·Shell의 색·기본 숫자를 여기서 재정의하지 않는다.
- `lab.js`: 레이아웃 전환, footer 방식 전환, snap 높이 재계산을 실험에서만 처리한다. 앱/서버 데이터는 사용하지 않는다.
- **Footer 구성:** `none`(CTA 영역 없음), `single`(주행동 하나), `paired`(중립 보조 + 브랜드 주행동), `stacked`(좁은 화면용 세로). 모두 기존 C-ACTION 조합이며 독립적인 버튼 타입이 아니다.
- **Page Shell 시연:** Root/Detail/Utility는 기존 `S-PAGE`의 표시 모드이고 실제 앱 헤더/라우팅/Navigation을 완성했다는 의미가 아니다. Slot과 gutter/본문/하단 예약 구조만 공통으로 확인한다.
- 회귀 기준: `visual-polish.test.mjs`. 코드 구조 검사와 모바일 브라우저 실화면 검수는 구분한다.

[시각 검토 및 개선 근거](../../docs/superpowers/reports/2026-11-10-seed-lab-visual-review.md). 새 전용 컴포넌트, JSON UI 빌더, 임의 아이콘 원본, 앱 기능은 만들지 않았다. 테마의 승인 토큰은 실험 환경에만 적용해 구형 preview의 변형된 색과 섞지 않는다.

## 검증 구분

- **정적 검사**: 참조 파일과 아이콘 수, 허용 마크업, 수치 상태 테스트
- **브라우저 검증 필요**: 320/360/390/430 × Light/Dark, 200% 글자, overflow, 키보드, 최소 터치 타깃, PNG/SVG 디코드, 상태·레이아웃 조작 및 시트 핸들 제스처
- **별도 미완료**: 시트 Body 스크롤 경계에서 drag로 이어지는 동작, 실제 모바일 터치/IME/Screen Reader, SEED 21개 전체 Variant/State 조합, 시각 승인

실험 화면을 보여 준 사실만으로 디자인 승인이나 21개 전부 구현 완료로 표시하지 않는다. 오류가 나오면 페이지별 override보다 기존 SEED 계약과 단일 공통 CSS 소유권부터 확인한다.

관련 정본: `seed/implementation/v015.md`, `seed/patterns/layout-composition.md`, `seed/patterns/page-composition.md`.

## 패밀리별 공통 스타일 카탈로그 · 2026-11-10

[컴포넌트 패밀리 시연장](./?panel=components)은 기존 `index.html`의 **컴포넌트 탭**에 통합됐다. 첫 탭은 SEED 전체 지도이고, Foundation과 Shell·Navigation이 별도 탭으로 추가됐다. 옛 `families.html` 주소는 기존 Lab의 해당 탭으로 연결한다. 공통 외형은 `seed-standard.css`가 소유하고 기존 배치·동작·에셋 탭을 보존한다.

- [패밀리 설계·허용 조합](../../seed/implementation/component-families.md): Typography 5 의미 역할 × 기존 10 Scale, K07 승인 Action 6종 × Small/Default/Large, Gold 서브브랜드 버튼, Loading/Disabled, Status, List, Field, Media, Surface, Icon.
- [공통 CSS](../../seed/styles/seed-standard.css): 표현 규격·슬롯 소유. `families.css`는 시연장의 항목 배치·반응형 정렬만 소유. 기존 `web/preview/index.html`, `style.css`, `clan.css`는 수정하지 않았다.
- `brand-weak`, `brand-outline`은 기존 문서상 후보이므로 **`data-seed-family-preview`의 명시적 실험 scope에서만** 표현. Critical variant와 새 색상·토큰은 미승인이라 추가하지 않는다.
- 기존 Preview에서 실제로 `.seed-*` 공통 클래스를 쓰는 요소만 자동 반영된다. `.gallery-button` 같은 레거시 독립 클래스는 기존 모습 그대로 유지되고 Android Compose로 자동 복제되지는 않는다.
- 테스트: `node --test web/preview/seed-standard.test.mjs web/seed-lab/*.test.mjs`. 테스트 파일이 존재한다는 것만으로 Node/브라우저 실행 완료를 주장하지 않는다.

실제 시각 QA 및 미검증 항목은 [계열 개편 구현 보고](../../docs/superpowers/reports/2026-11-10-seed-component-families-review.md)를 참조.

## SEED Lab · 전체 정본을 보여주는 거울 (2026-11-10)

SEED Lab의 우선 목적은 컴포넌트의 외형 나열이 아니라 **현재 SEED에서 실제로 무엇이 어떻게 정의되어 있는지를 보여주는 것**이다.

- **첫 탭 SEED 정의 지도**: [SEED 전체 지도](./?panel=map). INDEX 등재 38개(Foundation 5, Component 21, Pattern 7, Shell 2, Asset 1, Reference 1, Governance 1), 주변 계약·검토 문서 16개, 총 54개 정본 경로를 탐색.
- 검색/분류 → 이름·ID·용도·문서 상태·의존 관계 → **실제 GitHub 원문**. 원문은 색인에 고정한 Git 커밋을 기준으로 읽으며, raw 텍스트로도 확인 가능.
- **다음 탭**: 컴포넌트의 구현 예제, 배치/구성, 동작 검증, 에셋/표면. 시연에서 연결된 정본 ID를 눌러 원문으로 이동 가능. 예시가 규격을 재정의하지 않는다.
- 명세 위치: `seed/INDEX.md`와 기존 C/F/P/S/Implementation. 브라우저 표시/탐색 코드: `seed-map.mjs`, 로컬 뷰어 스타일: `seed-map.css`, 로컬 색인: `seed-sources.json`.
- 승인 경계: 문서 전체가 proposed여도 D-011/D-012/v0.15의 일부 조항은 이미 확정일 수 있다. 반대로 `approved` 상태를 Android 구현/실기기 검증 완료로 해석하면 안 된다.
- 정본 원문 조회가 막히면 실패를 알리고 GitHub 원문 링크를 제공한다. 네트워크 없이 원문 전체가 저장돼 있는 오프라인 뷰어라고 주장하지 않는다.
- INDEX의 경로/상태나 정본 내용이 변경되면 색인 기준 커밋을 재생성하고 검증해야 한다. `seed-map.test.mjs`는 INDEX 항목 수와 파일 경로를 검증한다.
- 디자인 토큰, SVG, 기존 공통 컴포넌트 CSS, Android, 앱 데이터는 이 단계에서 손대지 않았다.

## SEED Lab 시각화 교정 · 정본 → 실제 프레임 → 검증

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

## 페이지 분류·Navigation Set·Image Frame (2026-11-10)

- **페이지 유형 4종:** Global / Global Inner / Non-Global / Bottom Sheet. 이들은 기존 5개 S-PAGE Shell 역할을 대체하지 않고 상·하단 Navigation의 **표시·보존 상태만 결정**한다. Global은 Root+하단 표시, Global Inner는 Standard+하단 표시, Non-Global은 Standard+하단 숨김, Bottom Sheet는 부모 상태를 유지하는 Overlay다.
- **공통 웹 실행 계약:** `../preview/seed-page-contract.mjs`의 `PAGE_TYPES`, `applyPageType`, `selectWebNav`; `../../seed/styles/seed-standard.css`가 실제 Top/Bottom Nav 외형과 hidden/safe inset을 소유한다. Lab의 `seed-visual.mjs`는 재구현이 아니라 해당 계약을 사용한다.
- **D-015 확정 시각 기준:** Root `1.30rem/650`, Standard `1.08rem/620`, Pretendard Variable, 상단 Glyph `1.59rem`/target `3rem`, 20px(좁은 화면 16px) gutter. 하단 5개 시연 슬롯에서 클랜은 독립 `clan-bookmark-fill.svg`, 마이는 `me-smile.svg`/`me-smile-fill.svg`로 웹 매핑 확정. 공식 56종은 보존한다. 탭 경로/최종 제품 IA는 O-003에 남긴다.
- **C-IMAGE:** 기존 `.seed-media` 1:1 `item` contain, thumbnail/scene 비율, loading/loaded/error/폴백, 이미지 외부의 가격·이름·상태 정렬. 기존 3D PNG/SVG 원본을 변경하지 않는다.
- **Lab:** [페이지·네비 시연](./?panel=shells)과 [컴포넌트·이미지](./?panel=components)에서 상태와 외형을 함께 본다. 기존 Sheet·Pattern·에셋 시연은 보존한다.
- 검증 범위: Page Type 상태·하단 선택·시트 부모 보존 모의 DOM 통과, HTML/JS/CSS 정적 구조 확인, GitHub Pages 빌드 성공. Node 전체 테스트·Chromium 픽셀/모바일 실기기·Android Compose 이식은 미검증이다.

## D-015 웹 시각 승인과 A/B 종료 (2026-11-10)

- 사용자가 얇은 A/B 헤더를 미세 확대 후 최종 선택했다. Lab의 비교 토글·덮어쓰기 스타일을 제거하고 기존 preview 및 Lab의 **공통** `seed-standard.css`에 등록했다.
- 헤더 Root/Standard 값과 Bottom Navigation 웹 아이콘 매핑의 정본은 `seed/TOP_NAVIGATION.md`, `seed/components/bottom-navigation.md` 및 `v015-tokens.json`의 `topNavigation`·`webNavigationSet`이다.
- 기존 Global/Global Inner/Non-Global/Bottom Sheet 분류, 시트/페이지 동작 구조, 일반 컴포넌트 타입/56종 공식 SVG는 변경하지 않았다. Android 앱 이식·실기기 검증은 별도다.

## C-AVATAR 착장 시연 (2026-11-10)

컴포넌트 탭의 `#mascot-avatar`에는 **기본/모자/헤드폰/동시 착용** 시연이 있다. 실제 `main` 원본과 SHA가 같은 이미지 10개를 `assets/art/mascot/` + `assets/art/cosmetics/`에서 불러와 Feature(부모 폭)/Profile(64)/Row(40) 캔버스로 렌더한다. 크기를 바꿔도 같은 정규화 좌표와 장비별 zIndex가 적용된다. 이미지를 단독 상품 카드로 표시하는 C-IMAGE와 달리 이 컴포넌트는 사용자의 **프로필 사진에 해당하는 정체성 표현**이다.

웹 CSS는 기존 `../../seed/styles/seed-standard.css`의 `.seed-mascot-avatar`가 소유한다. 이 Lab의 `seed-visual.css`는 배열 간격만 담당한다. 시험용 버튼은 실제 장착 데이터·보유량을 수정하지 않는다. 구체 API는 `seed/components/avatar.md`를 참조.
