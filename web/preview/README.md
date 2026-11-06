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