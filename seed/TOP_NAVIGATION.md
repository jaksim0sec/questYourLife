# 갓생패치 · Top Navigation 시각 기준

> **확정: 2026-11-04.** 제품/디자인 결정: [D-011](../docs/DECISIONS.md).
> 기준은 사용자가 **같은 날 직접 고른 SEED Top Navigation 스크린샷 3장**.
> 공식 참고 문서: https://seed-design.io/components/top-navigation
> **중요:** 화면별 구조와 시각 언어가 확정된 것이며, 픽셀 단위 구현값·모든 화면의 액션 조합을 확정한 것은 아님.

## 1. Root · 최상위 화면

- **좌측 정렬의 크고 굵은 타이틀**을 기본 정체성으로 삼는다.
- 필요하면 제목을 **선택 가능한 버튼**으로 표현하고, 동작을 나타내는 화살표 또는 보조 라벨을 붙인다. 사용자가 눌러도 아무 일도 일어나지 않는 가짜 버튼은 금지.
- 오른쪽은 **Outline 아이콘 기반 액션 슬롯**이다. 검색, 알림(도트 표시), 메뉴 등의 조합은 원문 시각 예시일 뿐, **모든 화면에 고정하지 않는다**.
- 제목과 아이콘 사이에는 넉넉하고 정제된 공간을 둔다. 레드/옐로우로 헤더를 억지로 꾸미지 않는다.
- Root는 **현재 최상위 목적지**를 보여주는 헤더이며 단순히 Standard의 뒤로가기 아이콘만 숨긴 형태가 아니다.

## 2. Standard · 상세/하위 화면

- **좌측 Back 또는 Close**: 이전 화면으로 돌아가는 조작과 상세 플로우/레이어를 닫는 조작은 기능적으로 구별한다.
- **중앙의 명료한 타이틀**: 레이블을 기준으로 제목 위치를 안정적으로 유지한다. 필요한 경우 소형 정보 또는 보조 표시를 조합할 수 있으나 내용에 근거해야 한다.
- **우측 보조 액션**: 현재 화면에 의미 있는 공유, 알림, 더보기 등의 Outline 아이콘이나 텍스트 버튼을 배치한다.
- 루트와 상세의 헤더 레이아웃이 **서로 다른 이유**를 존중한다. 모든 화면에 동일한 좌측 타이틀을 복붙하지 않는다.

## 3. 공통 시각 문법

| 구성 | 정해진 방향 |
|---|---|
| Base | 밝은 무채색(흰색 중심) 표면, 과한 색·장식 배제 |
| Icons | 균일한 시각적 무게의 **Outline / stroke** 스타일, 적절한 클릭 영역 |
| Typography | 큰 Root 타이틀 / 절제된 Standard 중앙 타이틀, 명료한 계층 |
| Spacing | 공통 좌우 여백·아이콘 정렬·슬롯 간 간격의 일관성 |
| Layout | SEED의 **Root / Standard**로 구별하고 별도 변형을 남발하지 않음 |
| Signals | 알림 점·상태 강조는 정보 의미가 있을 때만 |

## 4. 구현·검수 기준

- 처음 설계하는 화면은 **Root / Standard 중 어느 역할인지 먼저 명시**한다.
- 상단 제목이 버튼인지, 단순 텍스트인지, Back인지 Close인지, 우측 각 액션이 왜 존재하는지 설명 가능해야 한다.
- 화면별 헤더를 복사한 후 예외만 계속 추가하지 말고 **공통 구성과 상태 규칙**을 우선한다.
- 작은 화면/긴 한글 제목/동적 글자 크기에서 말줄임·레이아웃·터치 영역을 확인한다.
- 실제 웹 시연에서 첨부 스크린샷의 **위계·정렬·굵기·간격·아이콘 스타일**을 나란히 비교한다. 컴파일 성공을 시각 일치로 간주하지 않는다.
- 원본 이미지 3장은 현재 **2026-11-04 사용자 대화 첨부**로 제공됨. **이 커밋에는 이미지 바이너리를 복제·업로드하지 않았음.** 공식 문서 URL 및 이 구조 명세는 저장되었지만, 이후 픽셀 단위 대조를 위해선 이미지 파일을 저장소 자산으로 별도 등록해야 함.

## 5. 아직 미정

- **Bottom Navigation 목적지·구성·실제 라벨**, 앱 전체 미검증 색상 수치, 헤더의 미검증 세부값, 글래스 사용 여부, 화면별 우측 버튼 목록. 활성 Fill/비활성 Outline 표현은 후속 D-012 승인 범위와 구분한다.
- SEED의 모든 예시 버튼을 갓생패치 화면 전체에 강제 적용하는 것은 아님.
- 개인 마이페이지/클랜/퀘스트/DM 중 어느 화면이 Root인지의 전체 정보구조 확정은 별개 의사결정.

이 문서는 **새 웹 시연과 이후 Android 이식의 상단 헤더 기준**으로 사용한다. 기존 Android 화면이 즉시 변경된다는 의미는 아니다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## 공통 계약 r1의 웹 적용

Root/Standard의 승인 구조는 그대로 유지한다. 이번 웹 개편의 역할·수치 매핑은 [C-TEXT](components/text-hierarchy.md), 타깃·gutter·이력 계약은 [S-PAGE](shells/page-shells.md)의 K10을 따른다. Glyph 박스와 실제 target의 중심을 맞추며 icon별 translateX로 액션 간격을 보정하지 않는다. 기존 다크 Shell은 웹 trial이며 헤더의 새 전역 색상 승인으로 기록하지 않는다.

## 웹 Top Navigation Set 확정 수치 · D-015 (2026-11-10)

> D-011의 Root/Standard 구조를 유지하면서 사용자가 웹 시연의 **얇은 버전**을 선택했다. 이전 `1.3rem / 850 / -0.05em`, Standard `0.9rem`은 **과거 구현값**이며 현재 정본이 아니다.

| 속성 | Root (Global) | Standard (Global Inner / Non-Global) |
|---|---|---|
| 글꼴 | Pretendard Variable 우선, 한글 시스템 폴백 | 동일 |
| 크기 | `1.30rem` | `1.08rem` |
| 굵기 | `680` | `650` |
| 자간 | `-0.025em` | `-0.018em` |
| 제목 배치 | 좌측 정렬 | 좌측 Back/Close + 중앙 제목 |
| 아이콘·터치 | `1.59rem` Glyph, `3rem` target | 동일 |

- 헤더 최소 높이 `3.5rem`; 상단 SVG는 공식 24 viewBox, 1.8 획, round cap/join을 유지한다. `1.59rem`은 **웹에서 SVG를 그리는 크기**이지 SVG 파일 자체의 좌표 변경이 아니다.
- 화면 헤더와 콘텐츠의 좌우 기준선은 같은 gutter를 사용한다. 웹 기준 `360 CSS px 미만 16px`, 그 이상 `20px`. Glyph는 48px target 중앙 배치, 가장자리에는 `(target - glyph)/2`만큼의 공통 optical inset을 적용한다. 아이콘 번호나 순서에 따른 개별 translateX 금지.
- 헤더 Surface는 페이지와 연결된 중립 표면을 사용한다. Lab Shell은 `--canvas`이고, 기존 웹 preview에서는 콘텐츠와 동일한 `--shell-surface`를 사용한다. 밝은 배경·다크 테마를 일률적인 단일 HEX로 강제하지 않는다.
- Top Navigation 타이틀은 범용 본문용 `type.root`/`type.detail`의 초기 실험값을 무조건 가져오지 않는다. **헤더의 표현 수치는 NAV-TOP이 소유**하며 글로벌 전체 글자 크기를 함께 바꾸지 않는다.
- Bottom Sheet는 별도 Handle/Header를 갖고 부모 Root/Standard를 교체하지 않는다. Sheet 헤더도 같은 Pretendard 계열을 사용하나 `Root/Standard` 타이틀 치수로 강제 통합하지 않는다.
- 제목/액션은 화면별 의미를 유지한다. 검색·알림·메뉴를 모든 화면에 세 개씩 넣는 규칙이 아니다. `Global Inner`와 `Non-Global`은 같은 Standard 헤더 외형을 사용하지만 하단 전역바 표시 여부가 다르다.

**공통 웹 구현:** `seed/styles/seed-standard.css`의 `.head`(기존 preview), `.seed-top-nav`(Lab 및 새로운 화면), 페이지 타입 `seed-page-contract.mjs`. 헤더 A/B 전용 override를 제거하고 위 값을 기본으로 채택한다. Compose 이식은 별도.

## S07 · Standard 우측 슬롯의 정보/동작 구별 (Non-Global 사례)

**D-015의 기하/네비 승인 범위 유지, 2026-11-12 타이포 미세 보정 반영:** Root `1.30rem/680/-0.025em`, Standard `1.08rem/650/-0.018em`, Glyph `1.59rem`, 동작 target `3rem`(48px 기준), 헤더 최소 높이 `3.5rem`, gutter `<360px 16px / 그 이상 20px`. Back/Close와 Standard 중앙 타이틀 및 기존 SVG 경로·색·광학 중심·하단 Navigation은 바꾸지 않는다.

기존 `Standard = Back 또는 Close + 중앙 제목 + 맥락별 우측 슬롯`을 유지하되 **Non-Global에서도 우측 슬롯의 내용이 반드시 버튼일 필요는 없다.** 실제 목적에 따라 다음을 선택한다.

| 우측 슬롯 | 의도·표현 | 접근성/동작 |
|---|---|---|
| **액션** | 실제 화면 기능이 있는 더보기/공유 등의 기존 Outline SVG 버튼 | C-ICON 타깃 최소48px, 키보드 동작, 이름, Focus; 실제 행동 없는 버튼 금지 |
| **읽기 전용 맥락 정보** | 예: 상점의 `보유 캐시 · 1,240` (가상 예시). C-CREDIT의 기존 코인 래스터+금액 또는 뜻이 명확한 짧은 텍스트 | `span` 같은 비조작 요소, 읽을 수 있는 값/단위/의미. 버튼 스타일·hover·chevron·독립 클릭 target 금지 |
| **비움** | 보조 행동이나 짧은 핵심 정보가 필요 없는 경우 | 임의 아이콘/상태 Badge를 추가하지 않음 |

**가용 공간:** 우측 값 때문에 중앙 제목과 Back/Close를 희생시키지 않는다. 긴 한글 제목·동적으로 커지는 값·200% 확대에서 겹치거나 헤더를 과도하게 늘리면 **값을 본문 정보 영역으로 내려** 전체 금액을 보존한다. 제목 위치/폰트를 임의 축소·이동하지 않는다. 기존 CSS의 좌우 균형 Grid에서 제목·값 바운딩 박스 간 교집합, 가로 overflow, 48px 동작 타깃을 검증한다. 슬롯이 있다는 사실은 모든 자릿수를 안전하게 수용한다는 뜻이 아니다.

**Middle 프로필과 역할 분리:** DM에서 Non-Global 채팅방에 진입했을 때 상대 아바타·이름·선택 상태 정보는 [P-HIERARCHY의 Middle](patterns/content-hierarchy.md) **신원 표현**이다. 우측에 붙이는 짧은 코인/액션과 혼동하거나 **헤더 전체를 프로필 카드로 재정의하지 않는다**. 이름과 아바타를 표현해도 Back/Close·Standard 제목·D-015 수치를 보호한다. 이 슬롯이 새 Header Variant가 된다는 뜻도 아니다.

**소유권·미검증:** 화면 맥락과 NonGlobal 하단바 숨김/Global Inner 하단바 유지는 [S-PAGE S07](shells/page-shells.md)과 `seed-page-contract.mjs`, 재화의 의미/원본 이미지/상태는 [C-CREDIT](components/credit-value.md), 액션은 기존 C-ICON/P-ACTIONS가 소유한다. 기존 `seed-top-nav__trailing`의 **사용 의미만 보완**하며 새 DOM 슬롯·SVG·Header Type·CSS Override를 만들지 않는다. 실제 소비 페이지에 정적 잔액 슬롯이 연결된 것은 아니므로 320~430px·200% 앱 실화면 검증은 **미완료**다.

## 2026-11-11 · Standard 헤더 제목 정렬 선택 (사용자 최신 합의)

**Standard는 그대로 하나의 Top Navigation**이다. `Global Inner`와 `Non-Global`에서 페이지 목적·오른쪽 정보의 양에 따라 다음 두 배치 중 하나를 **화면 작성자가 선택**한다. Page Type·Shell·새 Header Variant를 만들지 않는다.

| 선택 | 구성 | 적용 판단 |
|---|---|---|
| **Center (기본)** | `[Back/Close] [중앙 페이지 이름] [선택적 SVG Action / 빈 슬롯]` | 가벼운 상세·일반 조회, 타이틀 자체를 안정적인 중앙 기준으로 보여줄 화면. 기존 레이아웃/스타일/아이콘을 변경하지 않음 |
| **Start (선택)** | `[Back/Close] [좌측 페이지 이름] [남는 우측 공간: 실제 정보 / Action / 빈 슬롯]` | 보유 캐시처럼 화면 맥락 정보를 헤더에 표시하고 싶거나, 페이지 구성상 좌측 제목이 더 적합할 때. **정보 슬롯이 없어도 자율 선택 가능** |

**웹 선언:** 기존 `.seed-top-nav__inner[data-nav-role="standard"]`에 `data-title-align="start"`를 명시하면 좌측 제목형, 속성 생략 또는 `data-title-align="center"`면 기존 중앙형. `.head.standard[data-title-align="start"]`도 같은 **선택적 시각 계약**만 적용할 수 있다. `data-page-type`은 `global-inner`/`non-global` 그대로이며, Root나 Bottom Sheet 위치 규격은 변경하지 않는다. 배치 선택은 **페이지 작성자의 디자인 의도**이며 우측 슬롯 유무에 따라 런타임에서 자동 전환하지 않는다.

```text
Center : [<]           페이지 이름           [검색]
Start  : [<] 페이지 이름          [보유 캐시 · 금액]
Start  : [<] 페이지 이름                         [ ]  (빈 우측도 허용)
```

- **Back/Close, 타이포·아이콘 보존:** D-015 Standard `1.08rem/620/-0.018em`, Glyph `1.59rem`, 터치 타깃 `3rem`, 헤더 최소 `3.5rem`, 기존 좌우 gutter와 광학 보정을 그대로 사용. 제목만 한정된 Standard 내부 공간에서 좌측 정렬. Root 타이틀, Center형 중앙 기준, Bottom Navigation 가시성/상태는 **그대로 보존**한다.
- **좌측 제목 간격:** Back/Close의 터치 영역은 `3rem`이다. 첫 그리드 열에서 기존 optical inset과 `.375rem`을 빼고 열 간격 `.5rem`을 적용해, 제목이 터치 영역 끝에서 `.125rem` 뒤에 시작하도록 맞춘다. 기본 16px 기준 제목은 기존보다 약 17px 당겨지고, 아이콘 박스와 제목 사이는 약 13px이다. 오른쪽 슬롯과 제목 사이 열 간격은 `.5rem`이다.
- **기존 trailing 재사용:** 실제 `C-ICON` Action / 원본 코인 PNG와 수치인 `C-CREDIT` / 짧은 비조작 정보 / 없음 중 필요한 것을 선택한다. **재화를 새 아이콘·이모지로 대체하거나 정보에 가짜 버튼 효과를 주지 않는다.** 읽기 전용은 이름·값을 함께 전달하고, 액션은 기존 48 타깃을 유지한다. 필요할 때만 C-SURFACE 패널로 묶으며 과도한 Card 강제 금지.
- **남는 공간의 소유권:** Start는 제목 옆 남는 공간을 우측에 확보하되 Back·제목·trailing을 겹치게 만들거나 화면 밖으로 밀지 않는다. 긴 제목·큰 캐시·320폭·200% 글자에서 한 행이 불가능하면 **줄바꿈/헤더 행 높이 증가** 또는 **본문으로 정보 이동**을 선택한다. 값 숨김·숫자 임의 생략·글꼴/버튼 크기 축소 금지.
- **옵트인/회귀:** CSS 변경은 `[data-title-align="start"]`에만 국한한다. 속성이 없는 Standard 및 Root는 기존 Grid, 제목 위치·서체·아이콘 좌표를 유지한다. 라이트/다크, 320·360·390·430 CSSpx, 100/200% 글자, 아이콘·코인·빈 trailing, 선택 전환·Back/하단바 검수.

**우측 슬롯 구현 메모:** 기존 `.seed-top-nav__trailing`에서 코인·라벨처럼 **비조작 정보**를 배치할 때는 `data-slot-kind="info"`, 실제로 슬롯이 비었으면 `data-slot-kind="empty"`를 사용한다. Action은 기존 슬롯·버튼 계약 그대로. Start에서 `info`이면 아이콘 버튼용 Optical End 여백을 정보 패널에 적용하지 않고, `empty`는 보이지 않는 가로 공간까지 예약하지 않는다. 320폭·200% 확대에서는 Start의 Back/Action 광학 보정이 화면 경계 밖으로 나가지 않도록 **기존 gutter 이하로 제한**한다. 이 두 슬롯 속성은 새 Component/상호작용 타입이 아니라 기존 trailing의 사용 상태 표시다.

**우선순위:** 이 섹션은 과거의 'Standard는 항상 중앙' 표현을 **중앙은 기본, 좌측은 선택 가능**으로 보완한 최신 계약이다. 승인된 D-015 수치와 Page Type 의미는 불변이다.

### 알림 Bell 하단 연결 보정 (2026-11-10)

웹 Root/Standard 알림 버튼의 도형은 `assets/icons/preview/bell-connected.svg`를 사용한다. 하단 추의 양 끝을 종 본체 밑선(`y=17.5`)에 이어 열린 모양으로 보이지 않게 교정한 별도 Outline SVG다. 기존 공식 `assets/icons/soft/v0.14/bell.svg`는 D-012의 56종 정본이므로 변경하지 않는다. 두 SVG 모두 `24×24` viewBox, `stroke-width=1.8`, `currentColor`이며 헤더 타깃·크기·기타 아이콘은 D-015 그대로다.

## 2026-11-12 · 사용자가 요청한 헤더 무게 미세 보정

- 기존 D-015의 최초 시연 선택값은 **Root 650 / Standard 620**이었다. 최근 실제 서비스 헤더 참고 이미지와 현재 `클랜` 헤더 비교 후 **Root 680 / Standard 650**으로만 보정한다. Root 크기 1.30rem·Standard 1.08rem, 자간, 높이, 헤더/아이콘 좌표, Center/Start 선택, Nav Surface는 변경하지 않는다.
- 웹 정본은 `seed/styles/seed-standard.css`의 `.head h1` 및 `.seed-top-nav__title` 두 계열을 동기화한다. 참조 이미지보다 무작정 크게/검게 만드는 것이 아니라 글자의 **밀도만 소폭 보강**한다.
- **Pretendard는 CDN에서 불러오지만 로딩을 보장하지 않는다.** `@import` URL 한 개와 CSS font-family 선언은 네트워크 차단·CDN 장애·폰트 로딩 실패 시 폴백을 사용한다. 현재 저장소에 로컬 WOFF/WOFF2/TTF가 없으므로 **오프라인 완전 보장 X**. 브라우저의 `document.fonts.check('680 20px "Pretendard Variable"')`와 Network에서 실제 사용 폰트를 확인해야 한다. 폰트 파일 없이 `font-weight`만 올려 타 글꼴의 렌더 차이를 해결했다고 주장하지 않는다.
