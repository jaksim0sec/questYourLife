# 클랜 UI 시연 v3 · 레퍼런스 분석 및 SEED 재사용 기록

기준: 2026-11-08 사용자 첨부 (`11425.jpg` 현재 다크 시연, `11418.jpg` 웹툰, `11421.jpg` 보상 사례, `11019.png` 게임성 참고, `11024.png`/`11022.png` 소셜·상점, `11036.png`/`10903.png`/`10904.png`/`10905.png` 갓생패치 콘셉트). **화면 설계 시험 결과**이며 Android 화면, 서버 연동, 경제 정책 승인 아님.

## 이전 v2에서 확인한 문제와 이번 교정

| 문제(관찰) | 참고 레퍼런스의 장점 | v3 변경 | SEED 정본 재사용 |
|---|---|---|---|
| 플랫 섹션 안에 비슷한 텍스트 행이 반복됨 | 웹툰처럼 콘텐츠마다 미디어·정보 위계가 다름 | 클랜 대표는 아이덴티티, 멤버는 투명 아바타, 기록은 숫자 행, 소식은 타임라인, 탐색은 커버 | P-HIERARCHY, C-LIST, C-TEXT, C-SURFACE |
| 공동 기록이 평범한 진행 바와 한 줄 안내문이었음 | 상자의 물성·수령 가치 표현 | A2 보상 단일 히어로 + **기존 금색 상자 원본** + C-STATUS 10px 진행 바(620/1000, 62%) | A-ART A2, C-IMAGE, C-STATUS, C-SURFACE Reward |
| 멤버가 사람이 아니라 이름/상태처럼 보임 | 얼굴에 착용 아이템으로 개성 | Android AvatarComposer 동일 face/equipment 좌표 재사용, 원형 크롭 금지 | C-AVATAR, C-IMAGE, docs/assets/cosmetics.md |
| 다크/라이트 모두 무의미한 회색 상자 반복 | 게임 그래픽은 강조 콘텐츠에만 집중 | 부모 모든 section은 헤더와 같은 `var(--surface)`; 개별 보상 Media 영역만 컬러/광원 허용 | F-COLOR, C-SURFACE Plain/Reward, A-ART |
| 이벤트 스타일을 전면 복제할 위험 | 스크린 자체의 정보 밀도·강조 구분 | 클랜전·상점은 작은 기능 진입만, 시즌패스·보스 HP·가챠·유료권은 도입하지 않음 | F-PRINCIPLES, P-HIERARCHY, OPEN |
| 버튼·아이콘·라벨 비례와 터치 구분 부족 | 명확한 CTA/행동 단계 | 가입 CTA 단일 Brand, 보상 탐색 Gold; 나머지는 일반 텍스트 액션, SVG 24 viewBox/round stroke 재사용 | C-ACTION, C-SVG, P-BTN-ICON |

## 화면별 컴포넌트 구성

- **가입 `#clan`:** `C-AVATAR` 멤버 겹침과 클랜 프로필 → 바로가기(`클랜 채팅`은 기존 `#dm`, 초대 `#clan-invite`) → `C-SURFACE Reward` 단일 상자 히어로 → `C-STATUS` Progress → 숫자 기여/참여 리스트 → `C-LIST` 피드와 응원 → `C-RARITY`와 구분된 **작은 업적 아이콘** → 클랜전/상점 독립 진입 → 미디어 커버형 공개 클랜.
- **미가입 `#clan`:** 새 클랜/초대 액션 + 투명 마스코트 3명 → 추천 공개 클랜 Media Card → 공개 랭킹 → 간결한 클랜 안내. 기존 `previewMenu()` 상태 스위치는 상단 메뉴 Bottom Sheet에서만 유지.
- **`#clan-create`, `#clan-created`:** 원래 깃발 색상 5개, 이름 최소 2자, 80자 이내 소개, 생성 시연과 복귀. DB 생성 없음.
- **`#clan-my`, `#clan-activity`, `#clan-reward`, `#clan-discover`, `#clan-rank`, `#clan-detail`:** Standard/Global Inner 구조와 독립 화면 진입 유지. 
- **`#clan-war`, `#clan-shop`, `#clan-invite`:** 미확정 상세 규칙을 발명하지 않고 안내만 표시. 실제 보상 수령·교환·클랜 가입·실시간 랭킹을 구현했다고 주장하지 않음.

## 디자인 규칙과 자산

- 일반 `.clan-section`의 background는 `var(--surface)` 하나뿐, 헤더 `#header`와 동일. 부모 섹션의 경계는 캔버스 간격; 모든 기본 콘텐츠를 Card로 감싸지 않음.
- 업적 아이콘의 단순 미세 그라데이션은 제품 내 **실제 아이템 등급 확정**을 의미하지 않는다. 상자 아트는 A2, 업적은 A1에 해당.
- 글로벌 네비게이션은 기존 책갈피 active/outline 상태 유지. 탭 순서·Root/Standard 헤더·RootFontSize·홈·다른 페이지는 변경하지 않음.
- `assets/box_gold.webp`: 기존 `main/app/src/main/res/drawable-nodpi/box_gold.webp` Git blob 그대로 재사용. `assets/mascot_face_stamp.png`와 장비 파일 역시 기존 원본. 새로운 이미지 생성/저장소 중복 없음.
- 데이터(`느슨한 갓생단`, 8명, 620/1000, 순위, 멤버 활동)는 **구조 확인용 fixture**. 공유 동의 없이 개인 목표/위치/실사용 정보 노출을 구현하지 않음. 

## 검증 범위/한계

- Chromium 144 headless, `set_content` 스타일·스크립트 인라인 테스트에서 320/390/430/768 CSS px × light/dark × joined/guest = 16조건. 각 조건에서 `documentElement.scrollWidth-innerWidth == 0`, JS pageerror 0, `.clan-section` 배경과 Header computed background 같음, 모드 상태 분기 정상 확인.
- 테스트용 로컬 이미지는 라이브 GitHub 원본과 일부 다른 **대체 그림**이었으므로 최종 장비와 `box_gold.webp` 실화면 모양의 픽셀 단위 검증 근거로 삼지 않는다. 커밋 후 Git SHA·실제 링크 경로를 검증해야 한다.
- Browser shell은 간소화된 test fixture. 실제 GitHub Pages 배포, 전체 라우터의 시각/Back·스크롤/Android 실기기/TalkBack/IME/서버 수령 및 접근성 자동 검사는 별도 미완료.
- 신 UI는 사용자 최종 시각 승인 전 제안. SEED의 보상/아이템 경제, 공개 정책, 신규 네비게이션 확정으로 취급하지 않는다.


## v4 · 2026-11-08 · SEED 전체 컴포넌트 감사 이후 교정

[36개 SEED 역할 전수 매핑](SEED_COMPONENT_MAP.md)을 확인하고 기존 v3의 **재그림 아이콘·다크 보상 CTA·작은 클릭 영역·입력 이중 포커스·세그먼트 중첩**을 실제 파일에서 교정했다. 이전 '공식 SVG 경로를 재사용했다'는 표현은 부정확했다. 이번 v4부터는 공식 원본 16개를 직접 참조하며, v0.13 도형·임의 SVG를 사용하지 않는다. 표면 구조/AvatarComposer/RootFontSize/다른 라우트는 유지. 클랜 외 제품 영역 및 시연용 데이터는 미변경.


## v5 · 2026-11-08 · 구성 재검토 (사용자 첨부 1~5)

### 실제 비교
- 기존 1번의 장점은 간결한 정보 계층·같은 header/section 표면·일관된 멤버 아바타. 단점은 평범한 행/텍스트와 카드 반복, 시각적 주목점 부족, 깃발을 일반 Outline 아이콘으로 대체한 것.
- 2번 웹툰 화면에서는 실제 **미디어 커버**, 눈에 띄는 순위, 종류 필터, 독립 콘텐츠 단위를 배우되 광고·노골적인 복제·타사의 이미지는 가져오지 않음.
- 3번의 4열 아이콘/수치/라벨 패턴은 클랜 전용 `#clan-record`에 배치. 단일 홈에 모든 정보를 몰아넣지 않음.
- 4번 기존 밝은 클랜 컨셉의 **노란 직물 깃발+진한 별표**, 보상 상자 아트, 마스코트 조합을 중심으로 복원.
- 5번 손글씨의 핵심인 **깃발과 이름이 한 세트로 클랜 정체성을 표현**, 자유로운 색상/표식, 가입/미가입 상태는 별도 앱 탭이 아님, 각 정보를 별도 페이지로 배분하는 원칙 반영.

### 변경 상세
- `ClanIdentity`: 자체 직물형 SVG illustration (UI 픽토그램이 아님), 기둥·모양·중심 8방향 별표, 배너 상하 윤곽·잉크색 유지. 기본 라임옐로 + 컬러 선택 5종. 메인/생성/완료/내 클랜에 동일 적용.
- Root 가입 화면: 클랜/활동/3가지 액션, A2 보상, A1 사회 활동 Media, 3종 콘텐츠 커버. 기존 기여/업적/숍/클랜전 반복 블록은 하위 경로로 이동. Root 미가입은 기존 가입 CTA + 탐색/랭킹.
- `clan-record`: Standard Shell로 바로가기, SEED 4열 상태 지표 + 공동 Progress + 멤버별 누적 기여 + 업적. `clan-my`는 멤버·정체성 + 기록 진입, `clan-activity`는 피드만 표시. Detail 3화면에 C-TABS.
- `clan-discover`: 의미 있는 C-TABS category 필터 (전체/공부/습관/운동), 가로 C-SURFACE Media rail, 사용자 제공 투명 마스코트/장착물, 인기 순위, 실제 공개 클랜 API 아님.
- `C-SVG`: 공식 원본에 calendar/ranking 2개 추가. 깃발 일러스트는 사용자 메모/컨셉에 근거한 **클랜 아이덴티티 아트**로 도형 계약을 분리.
- Backend/Android/DB/기존 Bottom Nav·Root/Standard Header 변경 없음. 이 문서와 화면은 시연용 fixture로, 실제 공개 범위·랭킹·보상 지급을 승인하지 않음.

### 검증 한계
- JS 구문 파싱, 13개 라우트 템플릿 HTML 생성, SVG 매핑, Shell/앱 라우트 등록 검사. 실제 브라우저 렌더·320/390/430 CSS 폭·200% 확대·실기기는 별도 검증 필요.
