---
id: S-PAGE
category: shell
status: proposed
read_when: Root·Detail·Utility·Immersive·Overlay, 헤더·하단바·키보드·스크롤
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP]
references: [REF-001, REF-002, REF-004, REF-005, D-011, SRC-TDS-BOTTOM]
---

# 페이지 골격과 안전영역

## 정본
상단 시각·슬롯은 [TOP_NAVIGATION](../../TOP_NAVIGATION.md)의 D-011 정본을 직접 참조한다. 이 문서는 이를 재정의하지 않는다. Root 좌측 굵은 제목/Outline 액션, Standard Back 또는 Close/중앙 제목/맥락별 보조 액션을 보존한다. 컨셉의 좌측 상세 제목·로고 헤더·다크 헤더는 그 자체로 승인된 예외가 아니다.

## Shell 선택
| 유형 | 헤더·탐색 | 스크롤·행동 | 지위 |
|---|---|---|---|
| Root | 확정 Root 상단, 하단 목적지는 IA 합의 시에만 | 한 세로 스크롤, 생성 등 행동은 우선 inline | 상단 구조 approved / 나머지 proposed |
| Detail | 확정 Standard 상단, Back은 이전 맥락 복귀 | 콘텐츠 + 필요 시 지속 CTA, 하단바 숨김은 플로우별 검토 | proposed, 실제 history 보존 필요 |
| Utility | 작업 맥락의 Standard, Back/Close 의미 분리 | 폼·설정, 키보드와 CTA의 관계 명시 | proposed |
| Immersive | 보상 아트 등 목적이 있는 집중 표현, 명료한 닫기 | 시각 연출과 결과 텍스트 분리, 필요 시 건너뛰기 | proposed, 새 게임 모드 의미 아님 |
| Overlay | 원래 페이지 맥락 위 Sheet/Dialog | modal 여부·초점·dismiss 규칙 명시 | proposed |

Detail/Utility를 무조건 모든 탭의 바깥으로 빼는 정책을 확정하지 않는다. 최종 정보구조·딥링크·DM 접근은 O-003에 남아 있다. 특정 화면 이름이 Root인지 Detail인지 컨셉만 보고 잠그지 않는다.

## 높이·공간 제안
상단 content 최소 높이는 size.topBarMin, 하단바 content는 size.bottomBarMin을 출발점으로 쓰되 동적 글자·플랫폼에서 실제 렌더 높이를 사용한다. Header와 OS status inset을 분리한다. ROOT 제목·오른쪽 액션의 높이를 독립 조절해 기준선이 흔들리게 하지 않는다.

콘텐츠는 page.inset과 content.maxWidth를 쓰고 화면은 한 주 세로 scroll을 기본으로 한다. 탭 패널마다 스크롤·필터 상태를 유지할지 실제 플로우에서 검토한다. 독립 horizontal rail은 허용하되 읽기·접근 순서를 유지한다.

## 하단 공간 계산의 책임
Shell 한 곳이 Bottom Navigation·fixed CTA·OS bottom inset을 소유한다.
- Root + 하단바만: 실제 하단바 content 높이 + OS bottom inset.
- fixed CTA만: 실제 CTA wrapper 높이 + OS bottom inset.
- 승인된 공존 사례: 실제 하단바 content + 실제 CTA wrapper + OS bottom inset 한 번. 마지막 콘텐츠와 포커스 target도 이 영역 위로 스크롤 가능하게 예약한다.
- 두 wrapper가 각각 safe inset을 추가하지 않는다. Root의 상시 대형 CTA 공존을 기본값으로 만들지 않는다.
- 키보드가 열리면 실제 visual viewport/IME inset을 기준으로 재계산한다. 화면이 이미 줄어드는 resize 모드에서 IME 높이를 다시 더하지 않는다. 입력과 오류·확정 조작이 보이는지 검토한다.
- Snackbar·Sheet는 CTA·키보드와 겹치지 않게 하고 빈 safe-area를 장식 콘텐츠로 채우지 않는다.

## Shell 검증 매트릭스 · 하단 간섭 문제

| Shell | Bottom Nav | Fixed CTA | 실제 콘텐츠 패딩 계산 |
|---|---|---|---|
| Root | 표시 후보 | 일반적으로 inline | bottom bar content height + OS safe inset |
| Detail | 플로우별 숨김 후보 | 중요 완료 행동이 있으면 | CTA wrapper height + OS inset |
| Utility / 입력 | 필요 맥락에서만 | 키보드 위에서 접근 가능할 때만 | VisualViewport/IME resize 모드 중 하나를 기준으로 1회 |
| Root + 예외적 Fixed CTA | 승인된 경우만 | 있으면 | bar + CTA + OS inset **한 번** |
| Immersive / 보상 | 숨김 후보 | 이벤트 결과 받기 등 필요한 경우 | 실제 action height + inset, 모션 건너뛰기 제공 |

S2-04 반례 02와 S2-05의 하단 CTA 충돌은 Shell 책임 문제다. 버튼 테두리·라운딩만 수정해 해결하지 않는다. 하단 둘의 동시 공존 자체를 자동 규칙으로 만들지 말고 콘텐츠 목적과 safe-area에서 판단한다. 하단 아이콘·활성 상태의 독립 스펙은 [C-BOTTOM](../components/bottom-navigation.md)이다.

## v0.6 Bottom Sheet / Gesture와의 경계
Sheet는 [C-SHEET](../components/bottom-sheet.md) 및 [P-SHEET](../patterns/bottom-sheet-flow.md) 계약을 따른다. 이 Shell은 OS Safe Insets/IME/Root·Detail Bottom Nav 가시성의 **최종 소유자**다. Full Sheet가 열려도 헤더/하단바의 reserved height를 두 번 더하지 않는다. Sheet Footer는 Body 스크롤에서 제외하며, swipe는 앱/OS edge navigation을 가로채지 않는다. 내용이 짧으면 최소 높이 우선이고 3 Snap 단계를 무조건 노출하지 않는다.

## Bottom Navigation 제안 경계
기본은 간단한 Outline glyph + 짧은 Label + 의미 있는 selected 표시. 활성 Fill/비활성 Outline 표현은 D-012 승인 범위이며 탭 수·목적지·중앙 +·실제 라벨·최종 IA는 별도 미결이다. 생성 +는 목적지와 실행의 의미가 달라 중간 탭으로 자동 채택하지 않는다. 아트 속 4탭·5탭을 공통 시스템으로 복사하지 않는다.

## 충돌·검증
다크 테마 콘텐츠 후보와 D-011의 밝은 중립 헤더 사이의 차이는 O-022에 기록했다. 기존 확정안은 보존하고 다크 전역 헤더 적용은 보류한다. 웹/네이티브 inset 처리·scroll restoration·키보드·deep link는 미검증이다. 320–430 폭·200% 글자·긴 제목·Back/Close·마지막 항목·fixed CTA 조건을 실제 웹 시연에서 확인한다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K10 헤더·Shell·이력

**정본 소유자:** 구조는 `TOP_NAVIGATION.md`, 공간·이력은 `shells/page-shells.md`.

Root의 좌측 화면명/우측 Outline 액션, Standard의 Back 또는 Close/중앙 제목/우측 보조 슬롯을 유지한다. Glyph24와 최소48 target을 구분한다. 헤더와 일반 콘텐츠의 시작선은 같은 화면 gutter를 쓴다. 화면별 Glyph를 translateX로 옮겨 간격을 만들지 않는다. 원본 SVG의 광학 중심은 보존하고 Glyph 박스를 target 박스 중앙에 둔다.

웹 gutter는 기존 page.inset을 사용하여 360px 미만1rem, 이상1.25rem을 기준으로 한다. 같은 화면의 헤더·본문이 같은 gutter와 가용 콘텐츠 폭을 참조한다. 모바일 gutter와 OS safe inset은 별도다. 넓은 화면에서는 기존 home의72rem, clan의46rem 외곽 폭을 유지하되 각 화면의 헤더와 본문에 같은 외곽 폭을 적용한다. 긴 글의560 읽기 열은 내부 콘텐츠 선택이며 전체 셸을 몰래 축소하는 기본값이 아니다.

헤더 액션은 기본2개 이내, 목적이 분명하고 제목과 타깃을 보존할 때3개까지 허용한다. 들어가지 않으면 낮은 우선순위 액션부터 기존 메뉴로 옮긴다. Standard 양옆 슬롯은 실제 큰 쪽을 기준으로 균형을 맞춰 제목을 화면 중앙에 둔다. 제목이 길어도 타깃을 줄이지 않고 줄바꿈·헤더 높이 증가를 허용한다. 버튼 목록·순서는 화면의 실제 행동으로 정한다.

Back은 유효한 앱 내부 이전 이력으로, 직접 진입 시 해당 라우트의 기존 부모로 간다. 브라우저 Back을 별도 가짜 이력으로 대체하지 않는다. scroll은 URL 하나가 아닌 history entry별로 복원한다. Shell은 navigation·scroll·safe inset을 소유하고 클랜은 탭/필터 등 지역 상태만 소유한다. 지역 rerender가 무조건 window.scrollTo(0)를 호출하지 않게 한다. 닫기·메뉴·테마 전환도 원래 포커스와 탐색 맥락을 보존한다.

하단 공간은 실제 측정된 고정 영역 높이와 safe inset을 Shell 한 곳에서 계산한다. 하단 활성 Fill/비활성 Outline은 D-012 승인 표현이며 목적지 IA와 별개다. 실제 탭 경로·클랜 나침반/웹 책갈피의 기존 적용 범위를 혼동하지 않는다.

## 2026-11-10 · Shell과 본문 섹션 구분 (제안 보완)

Root/Detail/Utility/Immersive/Overlay는 **화면의 탐색·고정 영역·스크롤·safe-area**를 구분하는 기존 Shell이다. 페이지의 내용이 이벤트인지 상품인지 랭킹인지는 Shell 종류가 아니다.

- Shell이 Header, Bottom Navigation, fixed CTA, 스크롤 본문 경계를 먼저 소유한다. 페이지는 이 안에서 [섹션·공간 배치](../patterns/page-composition.md)를 선택한다.
- 본문 섹션은 선택적인 Header/Body/Footer와 세로·가로·격자·겹침 배치로 구성할 수 있다. 이것이 새로운 Top Navigation이나 새로운 Page Type을 뜻하지 않는다.
- 고정 하단 CTA는 일반 섹션 Footer와 다르다. 고정할 필요가 실제 과업에서 확인될 때만 S-PAGE의 inset/keyboard/scroll 계약을 따른다.
- 고정 헤더/바를 디자인할 때 페이지별 '빈 공간'을 임의 사용하지 않는다. D-011 승인 슬롯·타이틀 기준을 유지한다.
- 기존 'Root/Detail' 유형을 페이지 콘텐츠의 기본 템플릿으로 오용하지 않는다. 같은 Root라도 퀘스트·클랜·홈은 내부 구성 목적이 다를 수 있다.

공간 구조 추가만으로 기존 route·history·focus·safe-area와 D-012 하단 아이콘의 실제 구현 계약을 변경하지 않는다.

## K10 보완 · 4가지 페이지 분류와 Navigation Set 가시성 계약 (2026-11-10)

> **범위:** 갓생패치 웹 SEED 사용을 위한 **페이지와 네비게이션의 관계**를 구체화한다. 이것은 `Root/Detail/Utility/Immersive/Overlay`의 기존 Shell을 대체하는 새 디자인/컴포넌트 계층이 아니다. D-013의 계층 신설 금지를 지키며, 네비게이션 가시성 상태만 S-PAGE가 소유한다. 웹 적용 계약이고 Android 자동 이식을 뜻하지 않는다.

### 페이지 역할과 가시성 · 필수

| 페이지 역할 `data-page-type` | 대상 화면의 성격 / 예시 | 상단 Navigation Set | 하단 전역 Navigation Set | 고정 CTA / Overlay | Shell |
|---|---|---|---|---|---|
| **Global** (`global`) | 다섯 전역 목적지의 최상위 화면(홈·퀘스트·디엠·클랜·마이) | **Root 표시**: 좌측 굵은 페이지명, 우측 맥락별 Outline 액션 | **표시**, 활성 목적지 1개 | 기본 inline, 고정 CTA는 예외 | Root |
| **Global Inner** (`global-inner`) | 전역 목적지에서 들어가는 내부 화면(예: 퀘스트 상세·랭킹·상점) | **Standard 표시**: Back/Close + **중앙형 기본 / 좌측형 선택**, 필요한 우측 액션·정보 | **표시**, 현재 소속 전역 목적지 유지 | 필요할 때만 fixed CTA | Detail/Utility 등 맥락 |
| **Non-Global** (`non-global`) | 전역 바를 벗어나는 집중·작업 화면(예: 꾸미기·출석·프로필·대화방) | **Standard 표시**: Back/Close 중 실제 의미, **중앙형 기본 / 좌측형 선택** | **숨김** | 독립 CTA 또는 집중 콘텐츠 가능 | Detail/Utility/Immersive 등 맥락 |
| **Bottom Sheet** (`bottom-sheet`) | 현재 페이지 위에 표시되는 보조 흐름(예: 알림·필터) | **부모 상단 대체 금지**. Sheet 내부 Handle/Header/Close | **Sheet 내부 없음**. 부모 네비는 DOM/선택 상태를 유지하고 모달에 의해 가림 | C-SHEET/P-SHEET Snap/Focus/고정 Footer | Overlay |

이 표는 **페이지의 형태·탐색 구조를 결정**하며, 안에 어떤 정보·섹션·A1/A2 이미지를 어떻게 배치할지는 P-PAGE-COMPOSE와 P-LAYOUT이 별도로 결정한다. Global Inner와 Non-Global 모두 Detail Shell을 사용할 수 있지만 **하단 바 가시성은 서로 다르다**.

### 보장해야 할 상태/전환
1. Global → Global Inner: 상단 Root→Standard, 하단 **유지**, 활성 전역 목적지(부모 탭) 유지. 뒤로가기는 실제 이전 상태·스크롤 복귀.
2. Global/Global Inner → Non-Global: 상단 Standard, 하단 **숨김**. 마지막 콘텐츠/고정 CTA의 하단 padding은 숨긴 네비 높이를 예약하지 않는다.
3. Non-Global → Global Inner: 기존 소속 전역 목적지와 활성 상태 복원. 새 탭을 임의 선택하지 않는다.
4. 어느 페이지에서든 Bottom Sheet: **부모의 page type/네비/scroll 위치를 바꾸지 않고**, C-SHEET를 modal overlay로 열고 닫으면 이전 포커스 복원. 부모 네비는 시트 뒤에서 별도 선택 이벤트를 처리하지 않는다.
5. Safe inset은 Shell **1회**: visible bottom nav + optional fixed CTA + OS inset. Sheet 내부 Footer와 부모 Shell이 같은 inset을 이중 계산하지 않는다.
6. 직접 진입·Back·Close·키보드·200% 텍스트에서 올바른 상단 버튼, 하단 가시성, 현재 목적지, 포커스가 유지돼야 한다.

### 페이지 분류 예제와 결정 범위

전역 5탭 이름/순서는 현재 `new/web/preview`의 **웹 시각화 기준**이다. 실제 제품 라우트·딥링크·Android에 적용할 최종 IA는 O-003과 충돌 여부를 별도 합의한다. 해당 예시 페이지들의 Global Inner/Non-Global 소속은 웹 시연 분류 기준이며 콘텐츠/제품 기능이 구현됐다는 뜻은 아니다.

**웹 계약의 공통 진입점:** `web/preview/seed-page-contract.mjs`에서 네 가지 상태를 정의하고 `applyPageType`이 표준 `.seed-page-shell[data-page-type]`을 갱신한다. `bottom-sheet`는 부모 타입으로 직접 대입할 수 없으며 기존 C-SHEET를 연다. `web/preview/seed-standard.css`가 nav 가시성과 공통 외형을 소유한다. Lab은 이 코드를 그대로 소비한다.

## S07 · NonGlobal Standard 헤더의 우측 맥락 슬롯 (기존 구조 보완)

**범위는 Non-Global의 기존 Standard Top Navigation 안쪽 trailing 슬롯**이다. 별도의 Navigation Set, Page Type, Head Profile 또는 제3의 헤더 종류를 만들지 않는다. `Global Inner`와 같은 Standard 외형을 사용하되 **Non-Global의 전역 Bottom Navigation은 숨김**을 유지한다. 왼쪽 Back/Close의 실제 복귀/닫기 의미, **기본 중앙 제목 및 후속 선택형 좌측 제목 배치**, 상단 safe inset, 고정 CTA와 하단 safe inset 계산은 기존 K10/D-015 소유권 그대로다.

```text
동작형: [Back/Close]       [페이지 제목]        [더보기 SVG 버튼]
정보형: [Back/Close]           [상점]            [보유 코인 · 1,240]
                                                   (읽기 전용)
```

| 우측 슬롯 목적 | 기존 부품 사용 | 동작·의미의 구분 |
|---|---|---|
| **보조 동작** | 기존 Outline SVG + C-ICON의 48px 이상 실제 버튼 타깃, 접근 가능한 행동명 | 더보기·공유 등 실제 행동만 버튼. 장식/가짜 클릭 금지 |
| **짧은 맥락 정보** | 필요한 C-TEXT + 실제 재화라면 [C-CREDIT](../components/credit-value.md)의 코인 이미지와 수치. 예: 상점의 **보유 캐시 1,240**(화면 구성 예시, 실제 잔액 아님) | `span` 등 **비조작 요소**. `button`/`a`/pointer cursor/hover·pressed/chevron 없음. 정보이므로 48px 버튼 target 규칙을 억지로 부여하지 않음 |
| **없음** | trailing 슬롯 빈 상태 가능 | 임의 보조 정보·더보기 버튼을 채워 넣지 않음 |

- **이름과 데이터:** 표시 값은 해당 페이지와 직접 관련된 **짧은 현재 맥락**일 때만 고른다. 가격·보유·주간 획득·확정 수령을 같은 숫자로 뭉개지 않는다. 미확인 잔액을 `0`으로 치환하거나 표시만으로 구매 가능·지급 완료를 주장하지 않는다. 실제 코인은 C-CREDIT의 래스터 사용 계약을 따른다. 이 문서의 `🪙` 손그림은 **구조 기호일 뿐 구현용 이모지/신규 아이콘 명세가 아니다**.
- **길이·제목 충돌 우선순위:** 중앙 제목은 Header 의미의 주체다. trailing 값은 페이지/계정에 따라 자릿수가 늘어날 수 있으므로 고정 자릿수·강제 말줄임으로 처리하지 않는다. 기존 좌우 균형 그리드에서 320/360/390/430 CSS px와 **200% 글자 확대** 조건에 제목·Back/Close·trailing이 모두 겹침 없이 들어갈 때만 헤더에 표시한다. 공간이 부족하거나 값이 장문이면 **본문의 해당 정보 영역으로 이동**해 전체 값과 단위·라벨을 보존하고, Header trailing은 비우거나 실제 필요한 기존 단일 액션만 남긴다. **Back 타깃·D-015 폰트·glyph를 줄이거나 중앙 제목을 밀어내는 우회 금지**.
- **배치 검수의 실제 제약:** 현재 공통 CSS의 Standard grid는 `minmax(3rem,1fr) minmax(0,auto) minmax(3rem,1fr)`과 `seed-top-nav__trailing`을 사용한다. 이는 *기존 슬롯이 이미 있다*는 증거이지 긴 화폐 값의 자동 충돌 해소를 보장하지 않는다. **제목·우측 실제 바운딩 박스 간 교집합 0, 화면 가로 overflow 0, 48px 동작 타깃, 정확한 중앙 정렬, focus/Back 기능**을 검증한 후에만 '시각적으로 안전'이라고 기록한다. 200%에서 실패하면 실제 소비자 레이아웃에 근거한 최소 보정이 따로 필요하며 헤더 승인 수치 자체는 변경하지 않는다.
- **U03 Middle과 구분:** Non-Global DM 채팅방 헤더에서 **상대 아바타·이름·선택적 상태**를 보일 수 있다는 것은 [P-HIERARCHY의 Middle 정보 역할](../patterns/content-hierarchy.md)이다. 이는 **제목/신원 표시 영역의 콘텐츠**를 판단하는 일이며 trailing에 Cash/더보기 중 무엇을 넣는가와 **독립**이다. '채팅방이면 프로필 카드로 헤더 전체 교체'나 'Middle을 우측 정보 슬롯에 욱여넣기'로 해석하지 않는다. 실제 중앙 정렬·긴 상대 이름은 별도 실화면 검증 전까지 시연 승인되지 않았다.
- **동작과 접근성:** 정보는 읽기 가능한 라벨+값을 제공하고 장식 코인 alt는 중복 읽기를 피한다. 실제 Action은 48px hit area와 구체적인 accessible name, 키보드 Focus/Enter·Space를 제공한다. 뒤로가기는 이전 맥락 또는 직접 진입 시 정의된 부모로 돌아가야 한다. 기존 Header 액션 수 기본 2개, 목적 명확 시 최대 3개와 우선순위 규칙(K10)을 그대로 따른다. Action 개수 제한을 정보 배지 여러 개 나열할 면허로 사용하지 않는다.
- **선행/후행 검증:** 현재 `seed-page-contract.mjs`의 `non-global={header:"standard",bottomNav:false}` 및 `global-inner={header:"standard",bottomNav:true}`는 **변경하지 않는다**. 우측 정적 값은 기존 슬롯 조합의 제안일 뿐 앱에 실제 보유량 연동·새 코드 구현이 완료된 것이 아니다. `web/seed-lab/page-type.test.mjs`의 네 타입·네비 가시성 회귀 및 실제 Chromium의 제목/값/타깃/가로 넘침을 분리해 검증한다. U00에서 기록한 **레거시 `clan.css`의 Global Inner 하단바 숨김 충돌 후보**는 이번 NonGlobal 슬롯 설명만으로 해결되지 않으며 U11 통합 검수에 남긴다.

## 2026-11-11 · Standard 내부 제목 배치 선택 (Global Inner / Non-Global 공통)

[Top Navigation 최신 정본](../../TOP_NAVIGATION.md)의 **Center (기본) / Start (선택)** 를 기존 `S-PAGE` 사용 계약에 연결한다. 이는 기존 페이지 역할과 별개인 **Standard 내부의 제목 정렬 속성**으로서 **새 Shell/새 Page Type/별도 헤더 컴포넌트가 아니다**.

| 속성 | Center / 기본 | Start / 선택 |
|---|---|---|
| 쓰임 | 가벼운 조회·일반 상세·기존 페이지 | 상단 정보 패널 공간이 필요하거나 화면 설계자가 좌측 제목을 원할 때 |
| 제목 | Back/Close와 우측 액션에서 균형 잡힌 **중앙 정렬** | Back/Close 다음의 **좌측 제목**, 우측 남는 공간은 trailing 슬롯이 사용 |
| 선언 | `data-title-align` 생략 또는 `center` | Standard 안쪽 `.seed-top-nav__inner[data-title-align="start"]` |
| 우측 | 선택적 Icon Action 또는 비움. 매우 짧은 맥락 정보는 S07 조건을 만족할 때만 | 선택적 실제 Icon Action / 짧은 정보 / `C-CREDIT` / 비움. 우측이 비어도 Start를 임의 선택 가능 |
| 하단/뒤로 | `global-inner` 하단 Nav 표시, `non-global` 숨김, 실제 Back/Close 수행 | **완전히 동일** |

- 제목 정렬은 페이지 제작자의 선택이며 오른쪽 정보 유무에 의해 자동으로 변하지 않는다. `applyPageType`은 기존 `PAGE_TYPES`의 `header=standard` 및 하단 가시성만 설정하고 **`data-title-align`을 지우거나 자동 생성하지 않는다**.
- 두 정렬에서 **동일한 D-015 서체·크기·굵기/아이콘/터치/기본 gutter**를 유지한다. `start`는 CSS의 Opt-in 규칙만 사용해 기존 `center` 화면을 전혀 재배치하지 않는다. Root 및 Sheet는 대상이 아니다.
- 좁은 공간·긴 제목/수치/200% 확대에서는 제목과 trailing이 겹치거나 값이 사라지면 실패다. **자연스러운 높이 확대/줄바꿈 또는 정보를 본문 패널로 내리는 방식**으로 복구하며 금액 숫자·접근 가능한 이름·Back을 축소/은폐하지 않는다. 정보는 `C-CREDIT`의 원본 PNG·실제 값/상태, Action은 `C-ICON`의 실제 동작을 각각 따른다.
- Preview의 기존 `.head.standard`에는 같은 `data-title-align="start"` 선택을 지원할 수 있지만 해당 속성이 없는 기존 Preview/CSS 및 Bottom Navigation을 변경하는 것은 허용하지 않는다.
- 시연/QA에서 `Global Inner`와 `Non-Global` 각각의 Center·Start, 뒤로/닫기, 오른쪽 아이콘·캐시·비움, 320/360/390/430 CSSpx 및 100/200% 확대를 구분한다. 시연 DOM만으로 실제 기능·네이티브까지 합격으로 기록하지 않는다.

## 2026-11-10 · D-015 웹 헤더·네비 시각 동기화

위 K10에서 `Glyph24`라고 쓴 값은 원본 SVG viewBox/당시 기본 출력 기준이다. 사용자가 승인한 **현재 웹 헤더 렌더 크기**는 `1.59rem`, target `3rem`, 최소 높이 `3.5rem`, gutter `360 미만 16px / 이상 20px`이다. 실제 제목/글꼴 값과 surface 연결은 [NAV-TOP](../../TOP_NAVIGATION.md)과 `v015-tokens.json`이 소유한다. 과거 `24`나 `60` 후보를 별도 새 웹 CSS 값으로 재적용하지 않는다.
Global은 Root, Global Inner·Non-Global은 Standard 헤더를 공유한다. Bottom Sheet의 자체 Header와 부모 네비 보존 규칙은 변화 없음. 이 변경은 Shell 종류 추가나 Android 이식이 아니다.
