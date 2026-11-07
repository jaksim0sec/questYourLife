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


## 2026-11-08 클랜 구조 시연 v2 · 소속감/활동 중심

- `#clan`: Global 클랜 허브. 가입 상태는 **내 클랜 정체성·함께 도전하는 멤버·채팅/초대 → 공동 보상 진행 → 최근 활동·응원 → 클랜 업적 → 클랜전·상점 진입 → 공개 탐색·랭킹**으로 이어진다. 미가입 상태는 생성/친구 초대 → 공개 탐색·랭킹 → 클랜 활동 안내가 보이며, 기능을 잠근 빈 상태로 만들지 않는다.
- **상태 전환은 헤더의 메뉴 바텀시트 안에서만 제공**한다. 시연용 상태 버튼이 일반 페이지 콘텐츠인 것처럼 노출되지 않게 함. 두 상태 모두 클랜 탐색·랭킹 접근 가능.
- `#clan-my`(본인 클랜 기록), `#clan-activity`, `#clan-discover`, `#clan-detail`(탐색 중 선택한 공개 클랜), `#clan-rank`, `#clan-reward`, `#clan-war`, `#clan-shop`, `#clan-invite`는 Standard/Global Inner 구조 검사용 라우트.
- `#clan-create`: 기존 짧은 이름·깃발·한 줄 소개 생성 폼 및 `#clan-created` 완료 시연. 실제 서버 생성·초대 링크·지급·경제·클랜전 매칭은 구현하지 않음.
- **시각 원칙**: 보상·장식용 새 일러스트, 그라데이션, 과한 Reward 카드 없음. 멤버 아바타는 기존 공식 마스코트와 장착 장비의 원본 PNG만 예외로 사용. 아이콘은 SEED v0.14 Outline 경로. 색은 브랜드/기존 의미색만 최소 사용하며, **모든 기본 `.clan-section` 배경은 헤더의 `--surface`와 동일**. 섹션 구획은 `--canvas`의 8px 간격으로 구분. 역할 없는 카드 중첩 없음.
- 원본 도장 얼굴과 서로 다른 4종 장착 아이템을 사용한 예시 멤버 아바타, 실시간처럼 보이는 도트, 활동과 보상 수치는 모두 **예시 데이터**다. 실제 계정 정보·위치·퀘스트 상세를 노출하지 않으며 시연 내 상태만 바뀐다.
- 원본 MHTML의 홈 및 클랜 북마크 네비 SVG, RootFontSize, 글로벌 탐색 상태는 변경하지 않음. 실제 웹/Android E2E 테스트 별도.

### 2026-11-08 · 아바타 규격 회귀 수정

- 이니셜이 들어간 원형 뱃지를 멤버 얼굴로 쓰던 시도는 잘못됨. **C-AVATAR / DressedAvatar**에 맞춰 이미지에 원형 crop이나 컬러 배경을 강제하지 않고 투명 마스코트와 장비를 포개는 방식으로 정정.
- 아래 PNG는 원본 `main/app/src/main/res/drawable-nodpi/`의 **Git Blob SHA 그대로** `new/web/preview/assets/`에 연결. 화질·재질·원본은 변경하지 않음.
  - `mascot_face_stamp.png` / `cosmetic_beanie.png` / `cosmetic_black_headphones.png` / `cosmetic_cream_cap.png` / `cosmetic_blue_cap.png`.
- 얼굴 프레임은 `AvatarComposer.face = (x=0,y=.233,width=1,height=.734)`이며 장비 위치와 크기는 `main/cosmetics/AvatarComposition.kt`의 각 아이템 ID와 1:1 대응. 화면·사람 이름에 따라 합성 좌표를 임의 변경하지 않음.
- 데모 착장은 기존 `CosmeticArt.kt`의 고정값과 일치: 이모리(0 비니), 밤산책(1003 검정 헤드폰), 모찌(1 크림 캡), 다온(2 블루 캡). 실제 계정 장착 정보를 읽는 기능은 이 시연에서 구현하지 않음.
- 클랜 페이지·피드·클랜 상세에서 같은 아바타 함수를 사용하고, 온라인 표시만 별도 dot으로 처리. 다른 페이지/기존 홈 이미지/하단 네비게이션 변경 없음.
