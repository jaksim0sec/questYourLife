# 갓생패치 내비게이션 시각 미리보기

원본: 2026-11-06 사용자가 제공한 MHTML 2개 (`#home`, `#explore`).

- `index.html`: 홈 저장 DOM·inline SVG와 원본 퀘스트북 탐색의 Standard 헤더
- `style.css`: MHTML 공통 CSS 기반, 원본 :root 유지 + 의미색 실험 규칙 후행 추가
- `assets/mascot.png`, `assets/box.webp`: 홈 MHTML에서 실제 추출한 바이너리
- `app.js`: 의도된 시연 수준의 hash 라우팅과 테마 전환 및 빈 메뉴/알림 시트

## 범위

**보존됨:** 홈의 모든 시각 요소, 루트 색상/레이아웃 CSS, 퀘스트북 탐색의 저장된 헤더 및 빈 본문, 하단 5탭과 인라인 SVG, 이미지.

**보존되지 않음:** Sites 원본 JS 및 원본에 있었더라도 MHTML에 저장되지 않은 라우트의 화면 본문. 홈 이외의 루트 화면과 `#daily`, `#rank`, `#book/*` 등의 링크는 제목/탐색 골격만 연결했다. 내용을 추정하여 채우지 않음. `#explore` 본문은 스냅샷 당시 실제로 비어 있었음.

**비범위:** Android/Room/Supabase/실데이터/경제 로직. Docs의 디자인 확정 범위나 `main`은 변경하지 않음.

## 실행

정적 서버에서 `web/preview/` 디렉터리를 루트로 서빙 (`python3 -m http.server 8000 -d web/preview`) 후 `http://localhost:8000/#home` 에 접속. 별도 빌드/패키지 설치 불필요.

`style.css` 원본 규칙은 보존하되 하단에 의미색 시연 오버라이드를 추가했음. 메뉴 시트의 테마 전환은 루트 색상 토큰 검수용. 미저장 페이지에 대한 디자인 채택이나 정보구조 확정은 이 프로토타입만으로 판단하지 않음.

## 2026-11-07 · 의미색 팔레트 시연

- 새 4계열 기준색(500): Blue `#3987FF`, Green `#19C98A`, Orange `#FFAD32`, Purple `#A078FF`; 100/300/700/900 계열 확장.
- 원본 브랜드 `--brand`와 보상 `--reward`, 원본 HTML의 보상 일러스트는 유지. 이 미리보기의 기존 브랜드값은 v0.15 JSON과 일부 다르므로 별도 전체 변경 없음.
- 순위 예시의 `+%`는 구체적 정의가 아직 없는 상승 지표 예시로 취급해 Blue semantic(`--trend-up-fg/bg`) 및 기존 v0.14 Chevron SVG 회전 사용. 실제 데이터 정의는 미확정.
- 오늘의 퀘스트 1/3에 공통 Progress 표현 추가. 원래 컴포넌트 갤러리에도 의미색 샘플 추가.
- `docs/design/seed/foundations/color-surfaces.md`와 `implementation/v015-tokens.json`에 연결되는 **시연용 임시 채택**이며 Android 제품 전체 적용 확정은 아님.
## 2026-11-07 보정 · 클랜 아이콘과 반응형 글자

- 클랜 탭은 최초 MHTML과 SEED v0.14 `bookmark.svg`의 **책갈피 Outline을 그대로 유지**하고, 누락된 활성용 Fill만 동일한 path로 추가한다. 나침반은 이 시연에 사용하지 않는다.
- `:root` 크기를 모바일에서 약 6% 상향했다. CSS viewport ≤479px에서는 `clamp(1rem, calc(1rem + 1.43vw - 4.58px), 1.13rem)`, 480~767px에서는 `clamp(1rem, calc(1.13rem + 3.46px - 0.72vw), 1.13rem)`를 적용한다. 기본 16px 전제 390px 폭에서 약 17px, 430px에서 약 17.57px이며 768px 이상은 브라우저 기본 글자 크기(100%)다. 479→480px에서 갑자기 작아지는 현상 없이 연속 변화를 목표로 한다.
- CSS 뷰포트 폭만 사용한다. 기기 이름·DPR·실제 물리적 픽셀을 이용한 감지는 하지 않는다. 디자인 시연에만 적용하며 Android 구현·기존 문서 승인 범위는 변경하지 않는다.


## 2026-11-08 클랜 시연 v3 · SEED 리빌드

- **가입**: 클랜·투명 아바타 멤버·기본 액션 → 금색 상자 원본을 사용한 A2 주간 보상·Progress → 주간 기록·기여 → 활동 피드·응원 → 업적·클랜전/상점 진입 → 공개 클랜 탐색.
- **미가입**: 투명 아바타 소개·클랜 만들기/초대 참여 → 미디어 커버형 탐색 → 랭킹·활동 안내. 상태 비교는 기존 메뉴 Bottom Sheet에서만 함.
- 12개 기존 클랜 해시 경로 유지. 실제 API/저장/보상 지급/클랜전 매칭은 구현하지 않은 **시연**임.
- 모든 일반 `.clan-section`은 Header와 같은 `var(--surface)`. 일반 UI에 게임 재질을 확산하지 않고 보상 A2에만 3D·그라데이션을 제한. 작은 메달 A1, 멤버 C-AVATAR, 수치 C-STATUS, 피드 C-LIST 사용.
- 기존 금색 상자 `main/app/src/main/res/drawable-nodpi/box_gold.webp`를 Git 객체 그대로 `web/preview/assets/box_gold.webp`에 재사용. 도장 얼굴/장비/RootFontSize/글로벌 헤더/네비게이션/홈 변경 없음.
- **첨부 참고 분석, SEED 계약 대조, 범위/검증**: [CLAN_UI_AUDIT.md](CLAN_UI_AUDIT.md). 원본 레퍼런스의 유료 시즌패스·레이드·아이템 경제는 기능으로 추가하지 않았음.

검증: 인라인 브라우저 320·390·430·768 × 라이트/다크 × 가입/미가입 16조건 가로 넘침 0, JS 오류 0. 원본 장비·보상 그림의 실제 Pages 렌더와 Android/E2E는 별도 미검증.


### 2026-11-08 · 클랜 v4 · SEED 전체 이해 후 교정

- [SEED_COMPONENT_MAP.md](SEED_COMPONENT_MAP.md): INDEX **36개 전부**를 역할·의존성·클랜 사용/비사용으로 매핑, Surface 10/Badge 7/Text 5/원본 SVG 56 정리.
- 기존 JS 임의 SVG 도형을 제거하고 SEED **16개 공식 SVG 원본 SHA**를 `assets/seed-icons/`에 연결. CSS mask로 UI `currentColor`를 적용해 도형 소스는 손대지 않음.
- Gold Reward Secondary light/dark, Field border focus, Segmented demo menu, 48px 터치 목표, 아이콘/버튼 치수를 C-ACTION/C-FIELD/C-SVG에 맞춤.
- Root/Header/Nav/RootFontSize/아바타 합성·기존 12개 경로·예시 시뮬레이션 로직 불변. 실제 Pages/Android UX는 별도 검증.


### 클랜 v5 · 2026-11-08

클랜 깃발을 독립형 **컬러 직물 배너 + 진한 별표**로 교정. 홈은 일상·보상·발견 우선으로 정리하고, `#clan-record` 화면에 4칸 지표/기여/업적을 분리. 탐색 `#clan-discover`는 미디어 커버·카테고리 탭으로 구성. 참고 이미지 1~5 분석은 `CLAN_UI_AUDIT.md`. 실제 서버 연결 없음.


### v5.1

클랜 정체성 일관화: 공개 목록까지 공통 천 깃발 사용, 생성 색상 live preview 수정, 카테고리 필터 접근성·포커스 보존.


### v5.2 · 내 클랜 위계/탭 의미 보정

- Standard 헤더 `내 클랜`은 유지, 본문에서 중복된 클랜 제목 제거. **깃발/이름/소개 → 탭 → 단일 패널** 순서.
- C-TABS(멤버·기록·소식)는 `#clan-my` 안에서 내용만 변경. Tablist/Tab/Tabpanel/aria-selected·controls와 좌우/Home/End 키 지원. 탭은 hash 이동 안 함.
- 별도의 `#clan-record` 및 `#clan-activity`는 홈 링크용 기존 상세 경로 그대로 보존, 그 화면의 가짜 페이지 이동 탭은 삭제.
- 깃발의 내부 수직 점선 재봉선과 깃봉 상단 원형 끝 장식 제거. 원본 별표·배너 비율·색상 미리보기 유지.
- 네비/RootFontSize/홈/계정·DB·실제 클랜 시스템 변경 없음. 브라우저 시각 검수 별도.


### 2026-11-08 · v5.3 · Shell 3단 표면 계층 (시각 시험)
- 3단 `canvas → shellSurface → bottomNavSurface`로 역할 분리. 상단/일반 콘텐츠를 동일한 `--shell-surface`, 고정 하단 네비를 한 단계 밝은 `--nav-surface`로 매핑.
- Dark: `#17181D / #1D1D22 / #242329`. Light: `#FCFBFA / #F8F7F9 / #FFFFFF`. 실제 카드/시트용 기존 `--surface`는 유지.
- `#main` 배경을 Header와 공유하되 `.clan-layout`은 기존 구획용 `--canvas` 간격을 유지해 Flat Section 사이의 경계가 사라지지 않게 함.
- 정확한 색값은 [F-COLOR](../../docs/design/seed/foundations/color-surfaces.md)의 Web Preview trial. 기존 D-011 헤더 구성, C-BOTTOM SVG/경로, RootFontSize/레이아웃·보상/마스코트·기능 미변경.
- 실제 Pages 모바일/라이트·다크 육안 검수는 미완료.
