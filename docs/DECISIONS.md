# 갓생패치 · 확정된 제품·디자인 합의

> 2026-11-05 기준. 제품 철학·상단 내비 정본과 **D-012가 명시한 디자인 시드 표현 규칙만** 확정. 앱 기능·제품 IA·경제·Android 구현 완료를 뜻하지 않음.
> 1~2페이지를 넘기지 않고 상세 내용은 [product/](./product/README.md)로 분리.
> 현행 Android의 기술·기능 규칙은 [AGENT.md](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md) / [IDEA.md](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md)가 담당. 새로운 제품 철학과 충돌하는 항목은 **구현 전에 별도 조정**.

## 제품 정체성

**D-001 · 현실 자체가 게임 [확정]**
- AI가 실제 상황·목표를 퀘스트로 바꾸고 현실의 행동을 '공략'하게 만든다.
- “**내 인생이 공략 대상이자 게임임**”. 현실과 무관한 게임 시스템을 더하다 본질을 흐리지 않는다.
- **“퀘스트는 수단임. 단, 동시에 이 앱의 본질임.”** 퀘스트 수행은 기반이고, 마스코트·마이페이지 꾸미기는 사용자가 체감하는 큰 즐거움. 하나를 다른 하나로 대체하지 않음.
- 상세: [VISION](./product/VISION.md), [QUESTS](./product/QUESTS.md).

**D-006 · 퀘스트북의 콘텐츠화 [확정된 지향점]**
- 퀘스트북은 개인 목표 묶음인 동시에 공유·참여·수정·참고·경쟁·소통할 수 있는 콘텐츠를 지향.
- 플랫폼 안에서 사람들이 만들어 내는 **트렌드**를 목표로 한다. 구체적 공개 범위와 랭킹 로직은 미정.
- 상세: [QUESTBOOKS](./product/QUESTBOOKS.md).

**D-007 · 친구·클랜과 소셜 연결 [확정된 지향점]**
- 친구와 클랜은 경쟁과 동료 의식, 공동 참여의 원천이다. 프로필·꾸미기와 퀘스트 경험을 연결한다.
- '감시자'라는 원문 표현은 **관계의 비유**이지 강제 감시·개인 데이터 공개 정책으로 확정한 것이 아님.
- 상세: [SOCIAL](./product/SOCIAL.md).

**D-008 · 캐시·상자·꾸미기의 연결 [확정된 지향점]**
- 통일된 **캐시**를 중심으로 상자, 장식, 캐릭터 꾸미기가 연결되는 보상 경험을 설계.
- 현실 행동에 기반한 인앱 보상·성취감이 목적. 지급 규칙·확률형 보상·유료화는 미정.
- 상세: [REWARDS](./product/REWARDS.md).

**D-009 · 마스코트·마이페이지는 중심 경험 [확정]**
- 단순한 붉은 도장 얼굴은 사용자의 개성을 담을 **그릇**이며 실제 패션 감각의 모자·안경 등을 착용.
- 목표는 꾸미기에서 **“진짜 받은 것 같은 느낌”**과 자기표현. 얼굴 정체성은 일관되게 유지.
- 마이페이지는 설정 화면의 부속이 아님. 상세: [AVATAR](./product/AVATAR.md).

**D-010 · 명칭과 타깃 [확정/가설 구분]**
- 현행 공개 이름은 **갓생패치**. 이번 제품 정의에서도 사용자가 이 이름을 명시했고 저장소 문서와 일치.
- 한국 청소년 및 Z/MZ 중심은 **사용자가 지정한 지향 대상**이며 해당 집단의 선호를 실증한 것은 아님.

**D-011 · Top Navigation 시각·구조 기준 [확정 · 2026-11-04]**
- 사용자 제공 SEED 스크린샷 3종의 `Root / Standard` 헤더 구성 채택. 화면마다 새 헤더를 발명하지 않는다.
- Root: **좌측 굵은 타이틀**(필요할 때 전환/선택 버튼), 우측 **Outline 액션 아이콘**. 검색·알림·메뉴는 예시일 뿐 전 화면 고정 필수는 아님.
- Standard: 좌측 **Back 또는 Close**, **중앙 제목**, 우측 화면 맥락별 보조 액션. 중간 제목의 설명 배지·버튼은 화면 필요에 따라 사용.
- 헤더는 정제된 중립 표면, 선 중심 아이콘, 일관된 정렬과 여백 유지. 게임 장식·브랜드색 강제 삽입 금지.
- **Bottom Navigation, 상세 숫자 토큰 및 글래스 효과는 미확정.** 구현/시연용 기준: [TOP_NAVIGATION.md](../seed/TOP_NAVIGATION.md).

## 진행 방식과 보존 규칙

**D-002 · 기존 시스템 자산 보존**
- Android 앱, DB, 계정·동기화 인프라, RPC/API 및 기존 그래픽은 디자인 탐색 중 변경하지 않음.
- 신규 구현·마이그레이션·메인 브랜치 반영은 별도 검토.

**D-003 · 실행 가능한 웹 시연으로 디자인 합의**
- 모바일 웹 프로토타입에서 경험과 디자인 시드를 검증한 후 Android 재현의 기준으로 사용.
- D-012가 정한 **시각 규격·컴포넌트·SVG 세트**는 확정 웹 시드 기준. 다만 하단 탭 목적지, 홈 콘텐츠의 실제 데이터·우선순위, 앱 기능, 네이티브 구현·실기기 검증은 계속 미결. 상단은 D-011 정본 우선.

**D-004 · 문서 구분**
- 이 파일은 확정, [OPEN](./OPEN.md)은 가설·미결, [product/](./product/README.md)는 원 발언 중심의 구조화된 제품 설명, [research/](./research/SUMMARY.md)은 외부 자료.
- OPEN → DECISIONS 승격에는 명시적 합의가 필요.

**D-005 · `new` 브랜치의 현재 상태와 향후 역할 [방향 확정 · 2026-11-09]**
- **현재:** 문서·웹 preview 중심으로 앱 코드가 없는 상태. 현행 `main`의 실행 코드·기존 에셋·데이터·배포 환경은 이번 문서 개편에서 변경하지 않는다.
- **향후:** `new`는 `main`에 상응하는 개발 브랜치로 발전한다. '문서 전용'은 영구 금지 정책이 아니다. 실제 앱 코드 편입, 기존 리소스 이관 및 `main` 병합/배포는 작업별 영향·호환성·검증을 거쳐 별도로 진행한다.

## v0.15 디자인 시드 합의

**D-012 · SVG·컴포넌트 시각 규격 [디자인 시드 확정 · 2026-11-05]**
- 사용자가 직접 제공한 Soft SVG **56종**(32 Outline + 5 Fill + 19 추가 Outline)을 공식 시드 SVG로 채택. `24×24 viewBox`, `stroke-width=1.8`, round cap/join, `currentColor`. HTML 시연이 아니라 [개별 SVG 파일](../assets/icons/soft/v0.14/README.md)이 원본이다.
- 기본 여백·Radius 16, 버튼 내부 아이콘 16–18, 아이콘/버튼 비율, 중립 Outline, 라이트/다크 의미색, 다크 보상용 노랑 CTA의 **흰색 텍스트·아이콘**, 7톤 Pill Badge, Progress 단일 컴포넌트, 10개 Surface 역할과 5개 텍스트 위계, 단일 트랙 Segmented를 **재사용 디자인 문법**으로 채택.
- Bottom Sheet: 내용 기반 Min / Mid / Full / Closed, 손가락 실시간 추적, 놓은 위치 가까운 상태로 스냅. 내부 스크롤 상단↓/하단↑에서 이어서 시트 드래그. Footer 고정과 키보드/닫기 대체 조작 포함.
- [최종 구현·검수 계약](../seed/implementation/v015.md)과 [기계 판독 규격](../seed/implementation/v015-tokens.json)을 채택 기준으로 등록. 승인 범위는 **웹으로 확인한 디자인 규칙**이며 독립 네이티브 앱 개발·CI·실기기 검증을 자동 승인하지 않는다.
- 기존 `main` Android 앱의 클랜 **나침반** 아이콘과 마스코트/캐시 래스터, 확정 TOP_NAVIGATION은 보존한다. 실제 탭 목적지·화면 데이터·경제 정책은 변경하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## D-013 · 공통 선택·조합 계약 r1 [개편 적용 확정 · 2026-11-08]

- 사용자가 공통 계약의 결정 책임을 리드에게 위임했다. 제공 보고서·이미지·기존 정본과 현행 웹 소스를 근거로 페이지 제작 판단, 텍스트 역할 매핑, Surface·정보 객체·행동·상태의 관계와 웹 작업자 인터페이스를 결정했다.
- 분야별 기존 정본의 **공통 계약 r1 적용 절**이 해당 분야를 소유한다. [INDEX](../seed/INDEX.md)에서 읽기 경로를 확인한다. 새 Composite/Page Type 계층을 신설하지 않는다.
- 기존 D-011/D-012 승인값은 유지한다. 선택한 F-TYPE/F-GEOMETRY의 웹 표현은 이번 개편의 검수 시작 기준이며, 실제 화면 검증·제품 경제·IA·Android 승인과 구분한다.
- 실제 구현은 [웹 어댑터 계약](../seed/implementation/v015.md)의 소유권과 인터페이스를 따른다. 공통 계약 확정은 P2/P3 제품 코드 변경 완료나 main 병합·공개 배포를 뜻하지 않는다.

## D-014 · 공통 에셋 정본과 preview 이관 방향 [SVG 이관 완료 · 래스터 미완료 · 2026-11-09]

- SVG UI 아이콘과 래스터/3D 아트를 역할별로 구분하되, **동일한 소스 파일의 정본은 한 곳**에서 관리한다. 장차 저장소 루트 `assets/`를 공통 원본 위치로 사용한다. 세부 경로와 단계별 전환은 [에셋 관리 계약](./assets/README.md)을 따른다.
- 기존 56개 Soft SVG는 원본 Git blob SHA를 보존해 `assets/icons/soft/v0.14/`로 **이전 완료**. `web/preview/assets/seed-icons/`의 중복 18개를 삭제하고 참조를 공통 위치로 교체했다.
- preview의 래스터와 별도 SVG/inline 도형도 사용처·원본·예외를 구분한다. 실제 경로 변경 시 CSS mask·JS/HTML·정적 서버 루트·GitHub Pages 경로를 동시에 갱신해 깨진 참조를 남기지 않는다.
- Android `drawable`/`drawable-nodpi`는 플랫폼용 연결·변환 경로다. 기존 클랜 나침반, 마스코트·캐시·장착 자산 및 사용자 데이터의 식별자를 임의 변경하지 않는다.
- **2026-11-09 실현 상태:** SVG 이동·중복 삭제·preview HTML/CSS/JS 참조 변경만 수행했다 (`c0d360a`). PNG/WebP 파일 등록·이동, Android 코드 이관·배포는 미완료이며 별도 검증 대상이다.

## D-015 · 웹 Navigation 시각 규격 확정 [사용자 승인 · 2026-11-10]

**승인 범위:** `feat/seed-layout-composition-20261110` 웹 시연에서 직접 비교해 채택한 **얇은 헤더(미세 확대 후)** 및 웹 하단 아이콘. SEED 재사용 규격에 반영하며 Android 자동 이식·실제 제품 IA 승인과는 구분한다.

- **Root:** Pretendard Variable 우선(시스템 한글 폴백), `1.30rem / 650 / -0.025em / line-height 1.35`. 왼쪽 페이지 제목, 오른쪽 맥락별 Outline 아이콘.
- **Standard:** 같은 서체, `1.08rem / 620 / -0.018em`. 왼쪽 Back 또는 Close, 중앙 제목, 오른쪽 맥락별 액션. Global Inner와 Non-Global 공통.
- **기하·표면:** Header 최소 높이 `3.5rem`, 터치 타깃 `3rem`(48px 기준), 헤더 SVG 렌더 박스 `1.59rem`, 공식 `24×24 / stroke 1.8` 원본은 유지. 같은 화면의 좌우 gutter는 360px 미만 16px, 그 이상 20px. 아이콘 순서별 `translateX` 보정 금지. 헤더는 바로 아래 페이지 배경과 자연스럽게 연결한다.
- **하단 Web Navigation:** 기존 5개 시연 슬롯, 비활성 Outline / 활성 독립 Fill+색·라벨을 유지. 클랜 활성 `assets/icons/preview/clan-bookmark-fill.svg`, 마이 비활성/활성 `assets/icons/preview/me-smile.svg`, `me-smile-fill.svg`를 채택. 공식 Soft SVG 56개 파일과 manifest는 변경하지 않는다.
- **페이지 타입:** Global / Global Inner / Non-Global / Bottom Sheet 가시성 계약을 유지. 시트는 부모 페이지 네비 상태를 대체하지 않으며 자체 Header만 가진다.
- **소유권:** 디자인 정본은 [TOP_NAVIGATION](../seed/TOP_NAVIGATION.md), [C-BOTTOM](../seed/components/bottom-navigation.md), [S-PAGE](../seed/shells/page-shells.md), [웹 토큰](../seed/implementation/v015-tokens.json), 구현 정본은 `seed/styles/seed-standard.css`와 `seed-page-contract.mjs`. Lab A/B 덮어쓰기는 종료한다.

실제 루트의 구성·경로/딥링크 O-003, 기존 Android의 클랜 나침반 및 D-012 Soft 56종 채택은 그대로 보존한다.

## D-016 · 웹 마스코트 아바타 조합 계약 [사용자 요청에 따른 SEED 적용 · 2026-11-10]

- 기존 `C-AVATAR`가 **사용자 프로필 사진에 상응하는 기본 신원 표시**임을 명시한다. My·클랜·DM·출석 등의 큰/작은 프로필에 동일한 마스코트 얼굴과 장착 데이터를 재사용한다. 캐릭터를 꼭 원형 프로필 사진 크롭에 넣지 않는다.
- `main`의 원본 얼굴 1개·임시 장비 9개를 Git blob 그대로 공통 `assets/art/mascot/` 및 `assets/art/cosmetics/`로 이관. 공식 UI SVG 56종, Android 리소스·ID·장착 기능은 보존한다.
- 기존 Android의 정규화 1:1 좌표를 웹 C-AVATAR의 시작값으로 재사용하고, 장비별 위치·크기·zIndex를 설정 가능하게 한다. 같은 슬롯은 하나만 선택, 모자+귀장비 등 다른 슬롯은 함께 합성할 수 있다. 모든 화면 크기에서 비율을 보존한다.
- 공통 웹 C-AVATAR의 표현·계약은 `web/preview/seed-mascot-avatar.mjs` 및 `seed-standard.css` 한 곳이 소유한다. 별도의 디자인 시스템이나 새 페이지 유형을 만들지 않는다. 사용자 데이터 저장/서비스 장착·Native 구현 및 기기별 아트 시각 승인은 별개다.

세부 명세와 초깃값은 [C-AVATAR](../seed/components/avatar.md), 파일 소유권은 [에셋 계약](./assets/README.md)에 둔다.
