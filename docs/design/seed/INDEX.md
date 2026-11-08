---
id: SEED-INDEX
category: router
status: proposed
read_when: 디자인 시드 진입·필요 문서 선택
depends_on: []
references: [U-01, U-02, D-011]
---

# 갓생패치 · 디자인 시드 INDEX

v0.15 채택 시드 · 2026-11-05 Asia/Seoul · **[D-012](../../DECISIONS.md)에서 명시적으로 확정한 시각 규격에 한정해 approved.** 개별 문서의 기존 proposed 항목 및 Android 앱 동작·계정·경제·IA는 계속 미검증이다. 정책은 [DECISIONS](../../DECISIONS.md) / [OPEN](../../OPEN.md), 추출 방식은 [AUTHORING GUIDE](../SEED_AUTHORING_GUIDE.md) 정본을 따른다.

기준: 중립적이고 정돈된 조작 UI + 굵기 있는 명확한 정보 계층 + 붉은 도장 정체성 + 표현력 있는 3D 보상. 컨셉의 콘텐츠·탭 수·배치를 복제하지 않는다. 새 토큰/상태는 제안, 기존 상단은 승인 정본이다.

## 선택적으로 읽기

1. 작업을 화면 골격 / 행동 / 정보 표현 / 아트로 분해하고 아래 Read when·별칭으로 찾는다.
2. 새 페이지면 S-PAGE, 필요한 Component, 해당 항목의 **직접 의존성만 재귀적으로** 읽는다. 조합 문제일 때만 Pattern, 아트 문제일 때만 A-ART를 추가한다.
3. 원본·판단 충돌 때만 R-INPUTS와 OPEN을 읽는다. 수치 확정 전·완료 보고 전에는 V-CHECKS.
4. 모든 파일을 매번 읽지 않는다. ID는 안정적으로 유지하고 경로/의존성 변경은 INDEX와 같은 변경에서 반영한다.

| ID | Name / Aliases | Category | Read when | Path | Depends on | Status | References |
|---|---|---|---|---|---|---|---|
| F-PRINCIPLES | 표현 철학·감성·게임성 | foundation | 방향·보존/교정 판단 | [foundations/principles.md](foundations/principles.md) | — | proposed | U-01/02, REF-001–005 |
| F-COLOR | 의미색·표면·다크·대비 | foundation | 배경/색/선택/강조 변경 | [foundations/color-surfaces.md](foundations/color-surfaces.md) | F-PRINCIPLES | proposed | REF-001/002/004/006, SRC-SEED-BUTTON, SRC-TDS-BUTTON, SRC-WCAG |
| F-TYPE | 글자 계층·한글·숫자 | foundation | 제목/본문/크기/줄바꿈 | [foundations/typography.md](foundations/typography.md) | F-PRINCIPLES | proposed | REF-001/002/004/005 |
| F-GEOMETRY | 간격·padding·radius·크기 | foundation | 폭/높이/여백/터치/확대 | [foundations/geometry.md](foundations/geometry.md) | F-PRINCIPLES | proposed | REF-001/004/005/006 |
| F-ICON-MOTION | Outline SVG·stroke·모션 | foundation | 아이콘/active/연출/감소 | [foundations/icons-motion.md](foundations/icons-motion.md) | F-COLOR, F-GEOMETRY | proposed | REF-001/002/004/005 |
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
| P-ACTIONS | 버튼 그룹·수락/거절·Bottom CTA | pattern | 폭 비중/조합/하단 행동 | [patterns/action-layout.md](patterns/action-layout.md) | C-ACTION, C-SELECTION, F-GEOMETRY | proposed | REF-005/006/007, SRC-TDS-BOTTOM |
| P-HIERARCHY | 콘텐츠 조합·비중·밀도 | pattern | 프로필/섹션/필터와 정렬 | [patterns/content-hierarchy.md](patterns/content-hierarchy.md) | C-LIST, C-SURFACE, C-STATUS, C-SELECTION, F-TYPE, F-GEOMETRY | proposed | REF-001–005 |
| P-STATES | 빈·오류·오프라인·대기·복구 | pattern | 상태별 표현/실제 결과 | [patterns/state-feedback.md](patterns/state-feedback.md) | C-OVERLAY, C-STATUS, C-ACTION, C-FIELD | proposed | REF-001/003, U-02 |
| P-SHEET | Bottom Sheet Flow | pattern | Sheet Footer·본문·Snap 조합 | [patterns/bottom-sheet-flow.md](patterns/bottom-sheet-flow.md) | C-SHEET, C-ACTION, C-TEXT, C-GESTURE, S-PAGE | proposed | U-05 |
| P-BTN-ICON | Button + SVG Layout | pattern | 버튼 SVG/Label/Loading의 안정 배치 | [patterns/button-icon-layout.md](patterns/button-icon-layout.md) | C-ACTION, C-SVG, F-GEOMETRY, F-TYPE | proposed | U-05 |
| S-PAGE | Root·Detail·Utility·Overlay | shell | 헤더/스크롤/하단바/안전영역 | [shells/page-shells.md](shells/page-shells.md) | F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP | proposed | REF-001/002/004/005, D-011, SRC-TDS-BOTTOM |
| A-ART | 3D·보상·마스코트·광원 | asset | 아트 표시/소품/표현 레벨 | [assets/reward-art.md](assets/reward-art.md) | F-PRINCIPLES, C-SURFACE, P-HIERARCHY | proposed | REF-001–004 |
| NAV-TOP | 확정 상단·Root/Standard | shell | 상단 구조/Back/Close/액션 | [../TOP_NAVIGATION.md](../TOP_NAVIGATION.md) | — | approved | D-011; 세부 수치 제외 |
| R-INPUTS | 원본·평가·나쁜 예·출처 | reference | 8장 전체·해시·채택/제외·반례 교정 조회 | [references/inputs.md](references/inputs.md) | — | needs-validation | REF-001–007, S2-01–08, U-01–04, SRC-* |
| V-CHECKS | 검증·제안/미결·변경 영향 | governance | 완료 판단/시각 검수/승인 전 | [VALIDATION.md](VALIDATION.md) | R-INPUTS | needs-validation | U-02, D-002–005/011 |

## 최소 경로 예시

| 작업 | 읽기 시작점 |
|---|---|
| 버튼 크기·색만 교정 | C-ACTION → 관련 Foundation |
| 수락/거절의 비례·줄바꿈 | P-ACTIONS → C-ACTION/관련 Foundation |
| 새 입력 페이지 | S-PAGE + C-FIELD + C-ACTION → 직접 의존성 |
| 프로필·기록의 과장 교정 | P-HIERARCHY + C-AVATAR + C-LIST/F-TYPE |
| 보상 아이템 진열 | C-IMAGE + A-ART + C-SURFACE, 실제 상태면 C-STATUS |
| 오프라인/실패 표현 | P-STATES → 관련 피드백 재료 |
| Root 하단/상세 fixed CTA 충돌 | S-PAGE → C-BOTTOM + P-ACTIONS |
| 상위 탭과 기간 선택이 겹칠 때 | C-TABS + C-SEGMENT → P-HIERARCHY |

자세한 SEED 대조·우선순위는 [COVERAGE](COVERAGE.md), 원본 8장 전수 검토는 [AUDIT](AUDIT.md)에서 본다. **각 컴포넌트의 정본은 한 파일만 소유한다.**

파일을 모두 만드는 것이 목적이 아니다. 새 변형은 기존 슬롯·토큰·조합으로 해결되는지 먼저 확인한다. 페이지마다 제품 데이터 이름을 붙인 컴포넌트를 늘리지 않는다.


## v0.6 작업별 선택적 라우팅

- **코인·획득 금액:** C-CREDIT → C-STATUS / C-IMAGE / F-COLOR.
- **체크·라디오·기간 선택:** C-CHECK + C-RADIO + C-SEGMENT, 탭은 별도 C-TABS.
- **하단 활성 Fill / 비활성 Outline:** C-BOTTOM → C-SVG → F-ICON-MOTION.
- **시트 스냅·제스처·고정 Footer:** P-SHEET → C-SHEET + C-GESTURE + S-PAGE.
- **아이콘이 있는 버튼:** P-BTN-ICON → C-ACTION + C-SVG.
- **마이/꾸미기 아이템 등급:** C-RARITY + C-IMAGE + A-ART, 등급명·새 화폐 임의 생성 금지.
- 실제 v0.6 웹 실험은 검증용 시연이며 자동으로 approved가 되지 않는다.

## v0.13 재사용 리소스 (2026-11-05, proposed)

- [v0.13 구현 인수인계](implementation/v013.md): Surface 10, Badge/CTA/Sheet/아이콘 계약 요약과 Android 이식 한계
- [37개 실제 SVG 소스](assets/icons/v0.13/README.md): 24×24, Outline/Fill 및 v0.13 홈·그룹 교정
- [기계 판독 토큰](implementation/v013-tokens.json): 제안 색상·간격·radius·스냅·뱃지·아이콘 속성
- 이 항목은 기존 Component 정본을 덮지 않으며, 코드 이식 전 앱의 확정 아이콘 규칙을 확인한다.

## v0.14 SVG source · 2026-11-05 (proposed)

- **[사용자 제공 최신 SVG 56개](assets/icons/v0.14/README.md)**: 기본 Outline 32 · Fill 5 · 추가 19, 24×24 / Stroke 1.8, 기존 37개 원본 경로 덮어쓰기
- [v0.14 구현 인수인계](implementation/v014.md): 라운딩·획·실루엣·Android 적용 주의·검증 구분
- v0.13은 롤백/비교 자료로 유지하며, main의 확정 클랜 나침반 네비는 교체하지 않는다.

## v0.15 최종 정본 · 다른 세션의 첫 진입점

1. **[v0.15 확정 디자인·검증·적용 계약](implementation/v015.md)** — 결정의 범위, 컴포넌트/제스처/컨테이너 사용 규칙, 실제 검증·미검증 분리
2. **[v0.15 디자인 토큰 JSON](implementation/v015-tokens.json)** — 앱 구현 시 숫자·의미색·아이콘 크기·Surface·Sheet 상태를 한 곳에서 읽음
3. **[사용자 제공 SVG 56종 원본](assets/icons/v0.14/README.md)** + [manifest](assets/icons/v0.14/manifest.json) — 반드시 이 소스를 인용. v0.13 및 최초 v0.14는 비교 이력
4. 화면별 구현에는 C-ACTION/C-SURFACE/C-SHEET 등 필요한 **직접 의존 문서만** 추가로 읽는다. 기존 `AGENT.md`, 제품 D-001~D-011, 앱 리소스 정본과 충돌 시 자동 이식하지 않는다.

**상태 구분:** [Seed confirmed] 웹/디자인 규격 승인 · [Native pending] Compose/VectorDrawable/실기기/탭 IA 등 구현 미완료. 새 시드 문서를 늘리기보다 여기의 원칙을 재사용한다.

## 2026-11-07 · 컬러 팔레트 시연

기준색 Blue `#3987FF` · Green `#19C98A` · Orange `#FFAD32` · Purple `#A078FF`. 확장 HEX와 Light/Dark 의미색은 [F-COLOR](foundations/color-surfaces.md) → [v015-tokens.json](implementation/v015-tokens.json) `color.palette` / `color.semanticPalette`에서 관리. **웹 시연용 임시 채택**, v0.15 승인된 브랜드·보상·SVG 규격 변경 아님.

## 2026-11-08 · 시각 품질 감사 제안 (승인 전)

- [실제 시연의 정보 위계·아트·표면 관련 SEED 규칙 보완 검토](PROPOSALS_2026-11-08.md). P-HIERARCHY/A-ART의 **기존 규칙 미준수**와 F-COLOR 등 **실제 계약 누락**을 별도로 분류. 제안 문서는 approved 지위를 갖지 않으며, 다음 디자인 적용·비교 시 참고만 한다.


## 2026-11-08 · 사진 근거 시각 감사 (proposed, 승인 전)

- [당근 4장 / 현행 클랜 2장 구분과 세로 구간별 UI/정보 밀도·색·문구 검수](CLAN_REFERENCE_AUDIT_2026-11-08.md). P-HIERARCHY/A-ART 제안 보완을 함께 기록하되 D-012 정본 숫자를 변경하지 않음.
