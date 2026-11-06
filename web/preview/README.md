# 갓생패치 내비게이션 시각 미리보기

원본: 2026-11-06 사용자가 제공한 MHTML 2개 (`#home`, `#explore`).

- `index.html`: 홈 저장 DOM·inline SVG와 원본 퀘스트북 탐색의 Standard 헤더
- `style.css`: MHTML 양쪽에서 동일하게 확인된 CSS 원본 (`:root` 라이트/다크 포함)
- `assets/mascot.png`, `assets/box.webp`: 홈 MHTML에서 실제 추출한 바이너리
- `app.js`: 의도된 시연 수준의 hash 라우팅과 테마 전환 및 빈 메뉴/알림 시트

## 범위

**보존됨:** 홈의 모든 시각 요소, 루트 색상/레이아웃 CSS, 퀘스트북 탐색의 저장된 헤더 및 빈 본문, 하단 5탭과 인라인 SVG, 이미지.

**보존되지 않음:** Sites 원본 JS 및 원본에 있었더라도 MHTML에 저장되지 않은 라우트의 화면 본문. 홈 이외의 루트 화면과 `#daily`, `#rank`, `#book/*` 등의 링크는 제목/탐색 골격만 연결했다. 내용을 추정하여 채우지 않음. `#explore` 본문은 스냅샷 당시 실제로 비어 있었음.

**비범위:** Android/Room/Supabase/실데이터/경제 로직. Docs의 디자인 확정 범위나 `main`은 변경하지 않음.

## 실행

정적 서버에서 `web/preview/` 디렉터리를 루트로 서빙 (`python3 -m http.server 8000 -d web/preview`) 후 `http://localhost:8000/#home` 에 접속. 별도 빌드/패키지 설치 불필요.

`style.css`는 **MHTML 원본 그대로**이므로 새 기능 스타일을 섣불리 덧붙이지 않음. 메뉴 시트의 테마 전환은 루트 색상 토큰 검수용. 미저장 페이지에 대한 디자인 채택이나 정보구조 확정은 이 프로토타입만으로 판단하지 않음.
