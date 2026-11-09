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
