---
id: SEED-INDEX
category: router
status: proposed
read_when: 디자인 시드 진입·필요 문서 선택
depends_on: []
references: [U-01, U-02, D-011]
---

# SEED INDEX · r3 진입 계약

## 현행 계약

**A 페이지 제작:** [page-types](pages/page-types.md) → [page-intent](pages/page-intent.md) → [objects](objects/README.md) + [P-HIERARCHY](patterns/content-hierarchy.md) → 필요한 Components → Foundations → [PROCESS](PROCESS.md) → [VALIDATION](VALIDATION.md).

**B 부품 작성:** [AUTHORING GUIDE](../SEED_AUTHORING_GUIDE.md) → Foundation → Component → 직접 연결 계약 → VALIDATION.

페이지 제작은 모든 경우 **Page Intent부터** 시작한다. 구 INDEX의 `새 입력 페이지 → S-PAGE + C-FIELD + C-ACTION`은 경로에서 제거. 제품 정책은 DECISIONS/OPEN으로 구분한다.

| 계층 | 소유 | 기준 |
|---|---|---|
| L0 Foundations | 토큰·gutter·글자 하한·터치 | [foundations/](foundations/) |
| L1 Components | 부품·선택/배제·Variant·State | [components/](components/) |
| L2 Objects | 슬롯·맥락·클릭 소유 | [objects/README](objects/README.md), [item](objects/item.md), [progress](objects/progress.md), [person-activity](objects/person-activity.md) |
| L3 Pages | 네 유형과 Intent·콘텐츠 목적 | [page-types](pages/page-types.md), [page-intent](pages/page-intent.md) |
| L4 Process | G1~G7 및 5축 검수 | [PROCESS](PROCESS.md), [VALIDATION](VALIDATION.md) |
| 사례 기록 | 실제 페이지의 배제·실측·실패 | `docs/design/pages/<page>.md` |

### 기존 Component 21개 · 등록 경로 보존

| C-ACTION | 버튼·CTA·텍스트 액션 | component | 핵심/보조/위험/로딩 행동 | [components/action-button.md](components/action-button.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-005/006/007, SRC-SEED-BUTTON, SRC-TDS-BUTTON |
| C-SELECTION | 선택 family 라우터·Chip·Radio·Switch | component | 탐색/기간/필터/선택/설정 | [components/selection-controls.md](components/selection-controls.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-001/002/004/005, SRC-SEED-SEGMENT |
| C-LIST | List Item·Header·순위·행 | component | 반복 행/멤버/채팅/설정 | [components/list-item.md](components/list-item.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-001/002/004/005 |
| C-SURFACE | Surface·Card·Banner 공통 표면 | component | 그룹/미디어/배너/진열 | [components/surface-media.md](components/surface-media.md) | F-COLOR, F-TYPE, F-GEOMETRY | proposed | REF-001–004 |
| C-STATUS | Badge·Tag·Progress·값 | component | 진행/상태/비교 수치 | [components/badge-progress.md](components/badge-progress.md) | F-COLOR, F-TYPE, F-GEOMETRY | proposed | REF-001–004 |
| C-FIELD | Field·Input·검색·Textarea | component | 입력/검증/오류/키보드 | [components/field.md](components/field.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-005, SRC-TDS-SYSTEM |
| C-ICON | Icon Button | component | 독립 아이콘 행동·상단 액션 | [components/icon-button.md](components/icon-button.md) | F-ICON-MOTION, F-GEOMETRY, F-COLOR, C-ACTION | proposed | S2-01/04/05/08 |
| C-TABS | Tabs | component | 상위 콘텐츠 탐색 탭/밑줄 | [components/tabs.md](components/tabs.md) | F-TYPE, F-GEOMETRY, F-COLOR, F-ICON-MOTION | proposed | S2-04/05/07/08 |
| C-SEGMENT | Segmented Control | component | 기간·뷰 변경의 좁은 선택 묶음 | [components/segmented-control.md](components/segmented-control.md) | F-TYPE, F-GEOMETRY, F-COLOR | proposed | S2-04/07 |
| C-CHECK | Checkbox | component | 수동 완료/독립 목록 선택 | [components/checkbox.md](components/checkbox.md) | F-GEOMETRY, F-COLOR, F-ICON-MOTION | proposed | S2-04/05 |
| C-AVATAR | Avatar / Transparent Mascot | component | 프로필·친구 얼굴·투명 캐릭터 | [components/avatar.md](components/avatar.md) | F-GEOMETRY, F-TYPE, F-COLOR | proposed | S2-04/07/08 |
| C-IMAGE | Image Frame | component | 아이템 contain·콘텐츠 썸네일 cover | [components/image-frame.md](components/image-frame.md) | F-GEOMETRY, F-COLOR | proposed | S2-03/05/06/07/08 |
| C-BOTTOM | Bottom Navigation (proposed) | component | 하단 전역 목적지·활성·크기 | [components/bottom-navigation.md](components/bottom-navigation.md) | F-ICON-MOTION, F-TYPE, F-GEOMETRY, F-COLOR, NAV-TOP | proposed | S2-04/05/07/08; O-003 |
| C-CREDIT | Credit Value | component | 캐시 코인 이미지+금액 한 덩어리 | [components/credit-value.md](components/credit-value.md) | F-TYPE, F-COLOR, F-GEOMETRY, C-IMAGE | proposed | U-05, S2-08 |
| C-RADIO | Radio Choice | component | 상호 배타적 선택/그룹 | [components/radio-choice.md](components/radio-choice.md) | F-COLOR, F-GEOMETRY, F-TYPE, C-SELECTION | proposed | U-05, S2-08 |
| C-TEXT | Text Hierarchy | component | 5단계 텍스트 표현 배치 | [components/text-hierarchy.md](components/text-hierarchy.md) | F-TYPE, F-GEOMETRY, F-COLOR | proposed | U-05, S2-08 |
| C-SVG | SVG Icon Contract | component | 둥근 Outline 및 활성 Fill의 별도 경로 | [components/svg-icon.md](components/svg-icon.md) | F-ICON-MOTION, F-GEOMETRY, F-TYPE, F-COLOR | proposed | U-05, S2-08 |
| C-GESTURE | Gesture Contract | component | 스와이프·드래그·탭·취소·키보드 대안 | [components/gesture.md](components/gesture.md) | F-ICON-MOTION, F-GEOMETRY | proposed | U-05, S2-08 |
| C-SHEET | Bottom Sheet | component | 컨텐츠 가변 높이·3단 스냅·Footer | [components/bottom-sheet.md](components/bottom-sheet.md) | F-GEOMETRY, F-TYPE, F-COLOR, C-GESTURE, C-ACTION | proposed | U-05, S2-08 |
| C-RARITY | Item Rarity Surface | component | 등급별 은은한 미세 그라데이션 프레임 | [components/item-rarity-surface.md](components/item-rarity-surface.md) | F-COLOR, F-GEOMETRY, C-IMAGE, A-ART | proposed | U-05, S2-08 |
| C-OVERLAY | Sheet·Dialog·Snackbar·Skeleton | component | 모달/피드백/빈 상태/로딩 | [components/overlay-feedback.md](components/overlay-feedback.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION, C-ACTION | proposed | REF-003, SRC-TDS-SYSTEM |

**원문 수치·반례 입력:** [LEDGER](LEDGER.md) · **이력:** [HISTORY](HISTORY.md). D-011/D-012와 SVG 56종, Android/경제·IA 미결을 문서 개정만으로 재승인하지 않는다.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
