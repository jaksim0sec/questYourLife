# SEED r3 이력 보관소

> **history** 전용. 이전 정본의 형식과 문장을 아래 코드 블록에 원문 그대로 보존한다. 해당 블록은 현행 규칙이 아니다. 현행은 INDEX→각 '## 현행 계약' 참조.

### INDEX.md

````markdown
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

1. 기존 부품의 국소 수정은 해당 Component와 직접 의존성만 읽는다. 새 페이지는 제품 과업·콘텐츠·행동을 먼저 결정한다.
2. 새 페이지는 P-HIERARCHY와 관련 Pattern에서 객체·그룹·표현을 결정한 뒤 S-PAGE와 필요한 Component를 읽는다. 아트의 역할을 선택할 때 A-ART를 추가한다.
3. 제품 결정은 DECISIONS/OPEN을 확인하고, 원본·판단 충돌 때 R-INPUTS를 읽는다. 수치 선택 전·완료 보고 전에는 V-CHECKS를 따른다.
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
| 보상 아이템 진열 | P-HIERARCHY의 정보 객체 → C-IMAGE + C-TEXT + 필요한 C-SURFACE/C-STATUS/C-CREDIT + A-ART |
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

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## 공통 계약 r1 · 개편의 읽기 경로

[D-013](../../DECISIONS.md)의 위임 범위에서 선택·조합 계약을 개편 기준으로 결정했다. 의미의 정본은 각 기존 파일이며 새 계층을 추가하지 않았다.

- 페이지 판단·정보 객체: [P-HIERARCHY](patterns/content-hierarchy.md).
- 텍스트 역할/수치: [C-TEXT](components/text-hierarchy.md) / [F-TYPE](foundations/typography.md).
- 경계·미디어·아트: [C-SURFACE](components/surface-media.md) / [C-IMAGE](components/image-frame.md) / [A-ART](assets/reward-art.md).
- 행동·상태·목록: [C-ACTION](components/action-button.md) / [C-STATUS](components/badge-progress.md) / [C-LIST](components/list-item.md).
- 헤더·이력·safe inset: [NAV-TOP](../TOP_NAVIGATION.md) / [S-PAGE](shells/page-shells.md).
- A/B 고정 클래스·지역 상태·이력 인터페이스: [v0.15 웹 어댑터](implementation/v015.md).
- 반례와 인수: [V-CHECKS](VALIDATION.md). 원격 등록·브라우저·Native 검증의 상태는 각각 별도다.
````

---

### VALIDATION.md

````markdown
---
id: V-CHECKS
category: governance
status: needs-validation
read_when: 문서 검증·승인 범위·실제 UI 검수·후속 인수인계
depends_on: [R-INPUTS]
references: [U-02, D-002, D-003, D-004, D-005, D-011]
---

# 검증과 미결 경계

## 기존 v0.1 납품 범위 (아래 이력은 당시 결과)
컨셉 4장과 사전 자료 3종에서 재사용 문법·토큰 제안·컴포넌트·조합·페이지 골격·아트 표시 규칙을 추출한 v0.1 문서 묶음이다. 앱 코드·DB·기존 에셋·main은 수정 대상이 아니다. 색·치수·새 상태를 최종 승인한 것으로 간주하지 않는다.

## 요구 추적

| 요청 ID | 완료 기준 | 산출물 |
|---|---|---|
| S1 | 원본·영역·의도·관찰·확신도와 교정 근거를 분리 | R-INPUTS의 REF-001~007, U-01~03, 나쁜 예 01~10 |
| S2 | 문법·철학·정보 계층·범용 슬롯·상태·조합·Shell·3D 경계를 정의 | F-* / C-* / P-* / S-PAGE / A-ART |
| S3 | 역할별 분리, 실제 경로·의존성·상태를 INDEX에서 선택, 확정 정본 보존 | INDEX + 이 문서 + OPEN의 O-022 |

## 문서 수준 검증
정적 검사는 경로·의존성·등록 누락·토큰 참조·문서 원본 보존·기본 색 조합을 확인한다. 정적 통과와 실제 시각 품질을 구분한다. 검사 결과의 기준은 동일 커밋의 문서이며 새 파일·토큰을 추가하면 필요한 검사만 다시 수행한다.

- INDEX에 모든 시드 문서가 등록되고 링크가 실존한다. 상단 정본은 참조만 한다.
- ID 중복·없는 dependency·순환 dependency·미등록 reference가 없다.
- 각 Component에 Purpose, Anatomy, Variants, States, Tokens, Guidelines, Accessibility, Evidence, Impact에 대응하는 내용이 있다.
- 새 HEX는 F-COLOR, 숫자·간격은 F-TYPE/F-GEOMETRY 정본에 둔다. 아트의 상대 비율·SVG 규격·모션 시간은 역할별 정본에 둔다.
- 대표 텍스트·버튼·semantic·조작 경계·focus 조합의 불투명 sRGB 대비를 계산한다. 반투명 overlay·이미지·pressed 합성의 실제 렌더는 후속 검증이다.
- 기존 DECISIONS, TOP_NAVIGATION, AUTHORING_GUIDE, 제품 문서 원본을 보존한다. README에는 라우팅만, OPEN에는 제안·충돌만 추가한다.
- 기능·경제·내비게이션의 미결을 컨셉 근거만으로 승인하지 않는다.

## 이번 정적 검사 결과 · 2026-11-05
- 시드 문서 20개, INDEX 등록 20개(외부 상단 정본 포함), Foundation 토큰 95개를 확인했다.
- 경로·등록·직접 의존성·순환·메타데이터·컴포넌트 필수 내용·토큰 참조 검사에서 오류 0개.
- 불투명 sRGB 대비 56개 조합 검사: 일반 텍스트/버튼/의미색은 4.5:1 이상, 필수 경계/focus는 3:1 이상. Light 낮은 강조 글자색은 muted 표면 위 대비 실패를 발견해 보정했다.
- 원본 첨부 4개의 SHA-256과 REF-005의 교정 10개 추적을 확인했다. 제품 AVATAR 링크는 로컬 부분 스냅샷이 아닌 원격 new 트리의 실제 경로와 대조했다.
- 별도 문서 검토에서 확정 승격·컴포넌트/상태·INDEX 구조의 필수 수정 사항은 발견되지 않았다.
- 기존 상단/가이드/확정사항/제품 정본은 보존했다. 문서 검사는 렌더·실기기·동작 검수의 통과를 뜻하지 않는다.

## U-04 후속 조정의 검증 범위
버튼 높이를 유지하며 글자/줄높이/내부 아이콘을 소폭 축소하고 padding을 늘렸다. 역할별 radius와 브랜드 색상 매핑을 조정했다. v0.1 이후 사용자 후속값의 당시 대비는 Light Solid 4.59:1, Light Weak 6.45:1, Dark Solid 7.35:1, Dark Weak 7.79:1이다. 문서 경로·토큰 참조와 전체 기본 대비 조합을 재검사한다. 기존 검수판은 v0.1의 시각 자료이며 이 후속 조정의 렌더 검증 근거가 아니다.

## 실제 UI 검증: v0.1 당시 미수행이었음 · v0.2 웹 갤러리에서 일부 수행

| 검증 | 후속 확인 기준 |
|---|---|
| 320/360/390/430 폭·200% 글자 | 버튼 글자 축소·억지 줄바꿈·padding 붕괴·가로 넘침 없음 |
| 동일 컴포넌트의 여러 화면 재사용 | 같은 tone/size/state가 같은 높이·radius·글자·아이콘 비례 |
| 라이트·다크·긴 한글·긴 숫자 | 역할별 대비·줄바꿈·정렬 유지, 다크 헤더는 D-011 충돌 해소 전 보류 |
| Loading·Disabled·Focus·Selected·Error·Empty·Offline | 실제 state fixture와 문서 표현이 일치 |
| fixed CTA·하단바·키보드·safe-area | inset 이중 계산 없음, 마지막 항목·포커스 target 가림 없음 |
| Outline SVG | 실제 glyph 면적·선·cap/join·기준선 일치, 상단은 Outline 유지 |
| UI/3D 분리 | 일상 버튼 단색, 보상 물성·광원·프레임 유지, 아트가 문자를 침범하지 않음 |
| 스크린리더·키보드·동작 줄이기 | 이름·순서·현재 상태·결과·초점 복귀 확인 |
| 실제 기능·보상·동기화 | 기존 코드·API의 확인된 결과만 표현; 이번 문서 작업에서 기능 검증 없음 |

## 보류·승인 주체
기본 테마, 정확 토큰, 글래스, 하단 목적지/활성 스타일, 새로운 상태 동작, 원본 이미지의 저장소 바이너리 보관은 미확정 또는 후속 작업이다. [OPEN O-022](../../OPEN.md)에서 관리한다. 특정 값이 문서에 적혔다고 approved로 승격하지 않는다. 실험 결과와 사용자 명시 합의를 DECISIONS 정본에 기록한 뒤 해당 문서 status만 갱신한다.

## 문서 증가·변경 원칙
Foundation은 전역 시각 토큰, Component는 슬롯·행동·상태, Pattern은 조합·비중, Shell은 글로벌 프레임, Asset은 물체 표현을 소유한다. 한 페이지의 콘텐츠 개수·문구·배치 요구는 해당 화면 설계로 남긴다. 같은 구조를 제품명으로 복제하지 않는다. 정본 변경은 직접 의존성까지 확인하고 INDEX·근거·검증 기록을 함께 갱신한다.


## v0.2 · 2026-11-05 실제 실행 및 검증 결과

**수행 범위:** 사용자 첨부 S2-01~08 총 8장 원본의 파일명·크기·SHA-256을 대조해 R-INPUTS에 식별표 등록. **원본 자체는 GitHub에 올리지 않았음.** S2-02(광고)는 UI로 복제하지 않고 검증 방법에만 참고, S2-04는 잘못된 UI 반례로만 사용.

### 실제로 수정한 부분
- R-INPUTS의 **50:50 지시 뒤집힘 교정**, 기존 C-ACTION과 P-ACTIONS의 모순 문구 제거. 해당 두 하단 버튼의 동일 너비·높이·정렬은 의도적 교정이며 전역 강제 규칙이 아님.
- 기존 F-TYPE의 section/item/button/hero 과장 수치, F-GEOMETRY 내부 icon/프로필 비율, F-ICON-MOTION의 stroke 후보, F-COLOR의 좀 더 따뜻한 레드를 수정. 정확 수치는 proposed이며 D-011 수치는 덮어쓰지 않음.
- 기존 C-ACTION / C-SELECTION / C-SURFACE의 내용 중 독립 동작이 있는 Icon Button, Tabs, Segmented, Checkbox, Avatar, Image Frame, Bottom Navigation을 별도 계약으로 분리. 나머지 가족 문서를 삭제하지 않고 공통/원칙 라우팅으로 축소.
- P-ACTIONS·P-HIERARCHY·P-STATES·S-PAGE에 **상황→선택 규칙→전환→예외**를 보강. A-ART는 일반 UI와 3D 보상 재질의 경계 보존.
- [AUDIT](AUDIT.md)로 **8장 모두의 Keep/Refine/Split/Remove 판단**, [COVERAGE](COVERAGE.md)로 SEED 카탈로그 대응 및 미사용/후순위 구분.

### 웹 갤러리 검증 (현재 대화에서 생성된 별도 단일 HTML)
**아티팩트:** `gatsaengpatch-seed-v02-review.html` (대화 샌드박스 배포 파일, **new 브랜치에는 코드로 커밋하지 않음**). 원본 8장 축소 자료를 HTML에 직접 내장해 네트워크 없이 참조 가능하게 함. 임의 화면 fixture는 제품 구현 완료나 최종 탭 정보구조가 아님.

| 검사 | 실제 실행/관찰 결과 | 범위·한계 |
|---|---|---|
| 원본 입력 | 8개 S2 reference-card + 8개 이미지 naturalWidth > 0 | 원본 저장소 바이너리는 별도 미등록 |
| Chromuim 렌더 320/360/390/430 | viewport 4개에서 DOM 가로 스크롤 초과 0 | 화면의 모든 시각적 미세 정렬 보증은 아님 |
| 글자 배율 100%, 200% | 양 극단에서 페이지 문서 폭 초과 0 | TalkBack, 실제 OS 글자 배율 검증은 아님 |
| 조작 | 브랜드색 변경, 토큰 변경, Tabs 선택, 라이트/다크 전환, JSON 생성 확인 | 실데이터/서버 기능 아님 |
| 색상 접근성 | v0.2 Light `#D93B3B` + 흰 라벨 ≈ 4.53:1. `#F0444B` 선택 시 약 3.73:1 위험 경고 구현 | 이미지 위 텍스트·반투명 합성·전체 semantic 조합 별도 |
| 다크 | Weak는 dark 전용 배경색으로 전환하도록 웹 갤러리 로직 수정 | **완전한 F-COLOR Dark palette 매핑 대조는 미완료** |
| 50:50 교정 | 웹 그룹을 동일 폭(1fr/1fr) fixture로 렌더링 | 수락/넘기기 그룹의 실제 Android 레이아웃은 미검증 |
| Root vs Detail | Root에는 Bottom Nav, Detail에는 fixed CTA만 표시하는 fixture | IME 키보드/OS safe-area 실기기 미검증 |

**검증 실행 방식:** Python Playwright + headless Chromium의 `page.set_content()` 사용(샌드박스 보안상 file:// 직접 접근 거부). HTML 내부 image 12개가 정상 디코드됨. 전용 QA에서 합격한 범위만 기록한다.

### 아직 검증하지 않은 것
- 실제 Android 앱, 에셋의 투명 배경 원본 재조립, TalkBack, IME·OS Safe Area, 실제 네트워크/오프라인/보상 E2E.
- 사용자 최종 시각 승인. HTML의 '기존안'은 **나쁜 예를 재현한 실험 대조군**이지 v0.1 시드를 픽셀 동일하게 렌더링한 증거가 아님.
- 320 폭에서 200%의 모든 컴포넌트 개별 요소가 최적의 정보량을 보여주는지(문서 폭 검사가 단어 단위 미학 검수는 아님).
- 미확정: 브랜드/색상 라운딩 실제 값, bottom 목적지/활성 스타일, 테마 채택, UI 모션 수치.

**승인 게이트:** 위 결과는 기술 검증의 일부 통과만 의미한다. 사용자 시각 합의 전 기존 토큰과 새 컴포넌트 `status: proposed`를 유지한다. DECISIONS와 D-011은 수정하지 않는다.

## v0.6 시연 및 문서 검증 · 2026-11-05

### 사용자 피드백 보정
- 기본 Checkbox/Radio를 커스텀 Native Input/Indicator로 교체. checked, unchecked, mixed, disabled, focus 상태.
- **Progress 2종 중복 제거:** C-STATUS 한 개 + thickness 6/10/12 변형. Track/Indicator 함께 증가.
- 첨부 사용자 코인 이미지(투명 PNG)를 디코딩·축소하여 오프라인 HTML에 내장. 가격·보유량·획득량을 C-CREDIT 하나로 표시. **GitHub docs-only에는 원본 바이너리 저장하지 않음.**
- 햄버거 SVG 세로 높이, 둥근 Home 외형, 하단 아이콘 27dp; 하단 기본 Outline과 활성 Fill을 각각 별도 SVG symbol로 관리.
- 등급별 프레임은 미세 그라데이션, 내부 등급 이름 없음, 브랜드색 변경은 선택 outline에만 반영. 등급/아이템 경제 정책 확정 아님.
- Brand Weak, Brand Outline, Secondary Gold와 다크 mode 대응, Skeleton/Switch Off/Badge의 테마별 색 분리.
- Segmented를 중첩 Chip 기반에서 단일 트랙 + selected Thumb으로 변경.
- C-TEXT의 배치 5종, P-BTN-ICON의 인라인 SVG 규칙, C-GESTURE와 P-SHEET의 min/mid/full Snap/고정 Footer/닫기·스크롤 규칙 등록.
- 밝은 프로모션 아트의 전경 텍스트가 Dark Theme 본문색을 잘못 상속하던 문제 보정.

### 브라우저 실행 확인
- 단일 HTML v0.6 디자인 실험에서 Chromium headless: 스크립트 오류 없음, **컴포넌트 30개/패턴 10개**, 시연 화면 7개 보존.
- Checkbox 선택/Radio 배타 선택/Segmented 단일 active/coin 이미지 디코드/Progress 10·6·12 높이/다크 테마 토큰/Sheet min→mid→full→닫기/Bottom Nav Fill-Outline 분리/Detail Bottom Nav 숨김/Backdrop 닫힘, 320·360·390·430·768px 가로 넘침 0 검사.
- **29/29 브라우저 검수 항목 통과**, 포인터 Grip swipe 상향→mid / 하향→min 추가 확인.
- 실제 웹 렌더와 Android Native 동작·접근성은 다르다. 200% 글자·TalkBack·실제 OS/IME safe-area·성능·데이터 E2E 및 사용자 최종 시각 승인 **미완료**. 수치/스타일 status=proposed 유지.
- 설계 원칙 검증: [SEED Bottom Sheet](https://seed-design.io/components/bottom-sheet) / [SEED Segmented](https://seed-design.io/components/segmented-control) 참조. SEED 숫자를 그대로 복제했다는 뜻은 아니다.

## v0.15 최종 검증 · 2026-11-05

**아티팩트**: `gatsaengpatch-seed-v15-final-review.html` (대화 첨부, 저장소 미등록), SHA-256 `172670119092e8a3246cdad0ca9e112efbbb401fd3e5f8cd062ecc44baac96f3`. **SVG 공식 원본**: `assets/icons/v0.14/*.svg` 56개, 사용자 ZIP SHA-256 `247ed44a7ba18920a7fd5c575c38cb7e6d7d00ac47eb1800bba56d2b87891ee9`.

| 검증 ID | 내용·범위 | 결과 |
|---|---|---|
| V15-01 | SVG 56개 ZIP 원본 대비 Git SHA-1 일치, XML·CairoSVG 렌더 | 통과 |
| V15-02 | Chromium 4 폭(320·390·430·1440) × 2 테마 × 7 카탈로그 화면 | 56 조합, 가로 넘침 0, JS pageerror 0 |
| V15-03 | SVG 필터 Fill 5 / Outline 51, 이름 검색, 독립 소스 생성 | 통과 |
| V15-04 | Sheet handle 드래그 후 근접 snap, ArrowUp Min→Mid→Full, Escape 닫기 | 통과 |
| V15-05 | 긴 본문에서 native wheel로 콘텐츠 스크롤 시 시트 고정; 스크롤 최상단에서 아래로 드래그→Min, 최하단에서 위로 드래그→Full | 통과, Chromium 마우스 |
| V15-06 | Dark Secondary CTA 글자·아이콘 `#FFF`, 배경 dark gold, Field Focus 두꺼운 외곽 링 없음 | CSS 계산값 검증 |
| V15-07 | Android APK 빌드·Compose 실제 렌더·물리 터치·IME·TalkBack·데이터·네트워크 | **미검증/범위 외** |

**정책:** 사용자 D-012에 따라 이 시드의 규격은 디자인 측면에서 채택. 정확한 앱 품질 검증은 네이티브 적용 후 별도로 진행. 특히 `check-circle-fill.svg` mask는 VectorDrawable 변환 불가 가능성이 있어 검증 필요. 이전 v0.1~v0.6 보고를 최신 검증처럼 재사용하지 않는다.


## 2026-11-08 · 6장 사진 분류와 시연 확인 게이트 (SEED-PROPOSAL)

- 11444 당근 헤더/칩, 11446 당근 미디어 목록, 11447 당근 상세/댓글, 11448 당근 커뮤니티 텍스트 피드, 11445 우리 헤더/정체성 크롭, 11449 우리 클랜 홈 전체.
- 비교시 `P-HIERARCHY`의 주연(텍스트/숫자/아트)을 각 화면에서 먼저 식별. 레퍼런스를 여백·색만 추출하지 말고 컨텐츠형 · 행동형 · 미디어형으로 구분한다.
- `A-ART`는 `img`의 존재만으로 합격 처리하지 않는다. 시각적인 물체 중심성, 텍스트/CTA 충돌, 실제 이미지 해상도/크롭을 캡처로 따진다.
- 검증: JS 페이지 생성 성공 != 브라우저 픽셀 검증 != Android Compose QA. 스크린샷 판독과 앱 동작 판독을 분리.
- 세부 비교 및 테스트 상태는 [CLAN_REFERENCE_AUDIT_2026-11-08.md](CLAN_REFERENCE_AUDIT_2026-11-08.md)를 기준으로 함.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K13 검증·반례·인수 조건

**정본 소유자:** `VALIDATION.md`.

| 검수 사례 | 계약이 요구하는 결과 | 실제 실행 상태 |
|---|---|---|
| 큰 상자+홍보+진행+구성 링크가 한 Hero에서 경쟁 | 목적별 정보 그룹으로 분리, 기존 아트 유지, 홈/상세 책임 구분 | 후속 P2/P3 검증 |
| 이미지+작은 이름뿐인 아이템 | Media/Identity/필요한 State·Value·Action이 맥락상 해석 가능 | 후속 fixture 검증 |
| 620/1000인데 회색 막대 | 같은 입력으로 62% 실제 fill·ARIA 생성 | 후속 브라우저 검증 |
| 목표보다 많이 기여 | 초과값 보존·fill 상한100·지급완료 암시 없음 | 후속 경계값 검증 |
| 0·조회 불가·로딩이 같은 모양 | 유효0 / 조회불가텍스트 / indeterminate를 구분 | 후속 상태 검증 |
| 타이틀·Caption만 모두 Bold | 역할과 스케일 매핑 준수, 필수 정보 가독성 | 후속 화면 판독 |
| 버튼 Glyph만 우측 이동 | Glyph 박스/target 중심 일치, 비중첩 최소48 | 후속 geometry 검증 |
| 내 클랜에서 탭 후 상세 왕복 | 해당 history entry의 탭·scroll·포커스 맥락 복원 | 후속 동작 검증 |
| 보상·상점·보관함의 같은 아이템 | 같은 자산/기초 표현, 다른 정보·행동의 의미 유지 | 후속 독립 fixture 검증 |

검증은 **실행 / 규격 / 객체·조합 / 페이지 목적 / 행동·회귀** 5축을 따로 판정한다. 320/360/390/430 × 2테마의 핵심 3화면24조합, 대표 조건의200%글자·긴 이름·오류·키보드·상세왕복, 768/1440 smoke를 기존 계획대로 실행한다. 필요 없는 상태의 전조합이나 문서 문구를 그대로 되풀이하는 테스트는 만들지 않는다.

정량 기준은 실제 fill 비율(반올림1 CSS px 이내), 비중첩 타깃, 가로 넘침·필수 정보 잘림, 실제 패널/목적지 일치다. 첫2초의 중심·아트 존재감·객체 해석은 독립 화면 판독으로 기록하며 사용자 실험을 했다고 쓰지 않는다.

실패 시 계약에 문제가 있으면 리드, 공통 표현은 A, 지역 구성은 B로 복귀한다. 후속 작업자가 임의 예외를 누적하지 않으며 근거와 영향 범위를 리드에게 전달한다. 시각 부적합이 입증되면 계약은 개정할 수 있으나 이미 검증한 다른 규칙까지 다시 열지 않는다.
````

---

### shells/page-shells.md

````markdown
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
````

---

### patterns/content-hierarchy.md

````markdown
---
id: P-HIERARCHY
category: pattern
status: proposed
read_when: 아트·텍스트 비중·섹션 밀도·필터/정렬·프로필 조합
depends_on: [C-LIST, C-SURFACE, C-STATUS, C-SELECTION, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004, REF-005]
---

# 콘텐츠 조합과 주목도

## 재사용 문법
페이지 과업과 주목 정보를 먼저 결정하고 목적에 필요한 콘텐츠와 관련 행동의 관계를 드러낸다. Section Header 선행은 가능한 구성 중 하나이며 모든 섹션의 의무가 아니다. 섹션마다 Card·Banner·배지·그림자를 덧붙여 경계를 증폭하지 않는다. 페이지 구성 순서·홈 콘텐츠 개수는 별도 화면 설계다.

| 조합 | 기본 재료 | 위계 기준 |
|---|---|---|
| 프로필 요약 | Avatar + 이름 + 짧은 상태 + 관련 Action | AvatarProfile과 type.item/section, 아트가 정보를 밀어내지 않음 |
| 비교 가능한 기록 | List Header + 반복 List Item + Numeric suffix | 동일 슬롯과 숫자 기준선, 순위마다 임의 높이 변경 금지 |
| 콘텐츠 발견 | Media Card + Title + Metadata | 썸네일로 맥락, 제목으로 목적, 보조는 절제 |
| 보상 진열 | Image Frame + 이름 + 실제 상태/값 | 균일 프레임·물체 점유율·상태 위치 |
| 진행 요약 | Title + 실제 값 + Progress + 관련 Low Action | 진행과 실행을 구분, 큰 숫자는 한 대표값 |
| 소셜 진입 | 공통 List Item + 현재 맥락 | 채팅 진입만 과한 배너/아이콘으로 따로 꾸미지 않음 |
| 강조 배너 | A-ART + 짧은 제목 + 선택적 행동 | 한 주목 그룹, 인접 CTA와 경쟁 검토 |

## Context-aware composition · 컴포넌트를 조합하는 방법

| 페이지 맥락 | 첫 시선 | 본문 구조 | 보조 행동 위치 | 피해야 할 것 |
|---|---|---|---|---|
| 일상 실행 / 퀘스트 | 오늘 상태·다음 한 행동 | 조밀하되 읽기 쉬운 List/Progress | 관련 행·섹션, 단일 완료 CTA | 카드마다 그림자·3D 코인·두 개의 상위 제목 |
| 발견 / 퀘스트북 | 관심을 끄는 실제 콘텐츠 이미지 | Media Frame + title + 작성/참여 메타 | 목록 header/카드 내 선택적 low | 모든 콘텐츠를 같은 흰 버튼 카드로 획일화 |
| 소셜 / 클랜 | 사람·활동과 의미 있는 현재 상태 | 동일 List Item + 아바타 + 시간/맥락 | row suffix 또는 section action | 클랜 채팅만 테두리 큰 시스템 메시지처럼 과장 |
| 개인 / 마이 | 사용자 정체성 + 실제 기록 | 투명 아바타 + 주요 숫자 1그룹 + 수집·링크 | 프로필 조작은 medium, 기록은 low | 120px 마스코트와 이름/인사말/보상 수치가 전부 hero |
| 진열 / 꾸미기 | 소품 이미지와 장착 여부 | 일관된 Image Frame grid + 짧은 라벨 | 컨텐츠 선택 후 필요한 행동 | 아이템 전부에 강한 그라데이션/발광 |
| 이벤트 / 수령 | 한정된 강조 아트·받은 결과 | A2/A3 Hero + 필수 정보 | 한 주 행동만 강하게 | 앱 모든 화면에 A3 배너 반복 |

**조합 전 순서:** 화면의 주목적 한 문장 → 가장 먼저 보여야 할 결과/콘텐츠 → 다음 행동 → 실제 데이터 슬롯 → 제목·보조정보 비율. 이미 고른 콘텐츠 수·카드 개수로 UI 위계를 역산하지 않는다.

## 불필요한 면 줄이기
1. Surface는 **정보가 같은 그룹**이어서 연결될 때 사용. 단순 간격으로 구분 가능하면 카드 제거.
2. 행이 반복될 때 아이템마다 개별 테두리·그림자를 넣지 말고 일관된 List/Divider를 우선.
3. Image Frame과 일반 목록의 배경을 한꺼번에 화려하게 채우지 않음. S2-03/06은 **콘텐츠 아트만** 입체적.
4. Tabs/Segmented/Sort는 데이터 영향 범위가 다르므로 UI상 같은 층에 밀어 넣지 않음.

## 비중 제안
일반 프로필은 [C-AVATAR](../components/avatar.md) size.avatarProfile, 행은 size.avatarRow를 쓴다. 최대 size.avatarHeroMax는 프로필 정체성 자체를 탐색하는 몰입 영역에서만 검토한다. 인사말·출석 설명·주간 보조 정보는 body/support로 시작하며 hero 크기를 자동 상속하지 않는다. 이미지의 큰 캐릭터·큰 인사말을 그대로 수치 사양으로 삼지 않는다.

큰 아트가 있는 배너는 문장을 짧게 하고 메시지와 액션의 안전 영역을 확보한다. 일상 페이지의 모든 섹션을 hero로 만들지 않는다. 첫 화면에서 '목적 / 현재 상태 / 다음 행동'을 설명할 수 있어야 한다. 이 세 항목을 고정된 카드 개수·순서로 오해하지 않는다.

## 탐색·필터·정렬의 층
상위 Tabs, 기간/보기 Segmented, 정렬은 서로 다른 층이다. 동일한 둥근 container 안에 모두 끼우지 않는다. 필터는 영향받는 리스트 옆에, 정렬은 리스트 Header의 보조 위치에 둔다. 모바일에서 세 종류를 한 줄에 억지로 배치하지 않는다.

## 밀도·확장
행·타일의 세부 문구가 길어지면 줄·높이를 허용하고 그리드 열 수를 줄인다. 보조 메타데이터를 줄일 수 있는지 먼저 판단하되 필수 정보는 숨기지 않는다. 동일한 슬롯으로 1개·많은 항목·긴 이름·긴 수치가 모두 유지되는지 확인한다. 서로 다른 출처 썸네일에는 Image Frame 규격만 공통 적용하고 그림을 획일화하지 않는다.

## 상태·근거·영향
빈·오류·로딩도 같은 그룹 구조를 유지하며 의미가 없는 empty Card를 여러 개 나열하지 않는다. 직접 관찰 high: 제목/아트/기록의 조합. 구체적 역할 한도·검수 조건은 proposed. REF-005 04/06/08/09의 과장·밀집·이탈을 교정한다. 데이터·메뉴 구조·친구 공개 범위는 제품 문서 정본의 미결 사항을 따른다.



## 2026-11-08 · 시연 검수에서 발견된 구성 경향 (proposed 보완)

**본 문서는 화면 배치를 복사하라는 설계도가 아님.** 서로 다른 정보 유형을 동일한 'Section Header → text rows → border → CTA' 템플릿으로 눌러 담는 구현을 막는다.

| 정보 원형 | 1차 시선 | 추천 조합 (기존 컴포넌트) | 반례/금지 |
|---|---|---|---|
| 클랜 정체성 | 깃발 + 이름 | C-AVATAR identity art + C-TEXT item + meta + C-ACTION compact | Root 타이틀 '클랜' 아래 같은 이름의 별도 대형 타이틀 반복, 인원수를 success badge로 강조 |
| 공동 보상 | 목적에 따라 값·아트·구성·결과 | 공동 기록=값+Progress, 홍보=A2 후보, 구성=정보 객체, 실제 획득=A3 후보 | 공동 보상이라는 이름만으로 A2·Reward Hero·진행 관리의 한 면 결합을 강제 |
| 구성원 활동 | 아바타/행동·시간 | C-AVATAR + C-LIST 단순 행 + 상태·메타 | 아바타 4명 요약과 같은 사실을 커다란 CTA·다중 제목으로 반복 |
| 커뮤니티 텍스트 피드 | 내용 제목 또는 질문 | C-LIST plain + Category chip + 본문/시간/반응 | 실제 콘텐츠 없는 대형 썸네일·강제 Card, 모든 피드에 같은 굵은 제목 |
| 미디어 피드 | 실재 사진·작품 | C-IMAGE + C-LIST media + item+meta | 원본 없는데 임의 이미지를 내용으로 가장하거나, 미디어·제목·상태를 모두 hero로 만듦 |
| 상세/댓글 | 대화 본문 | Identity meta + Content + Reaction + Comment, context별 Accent | 상단 헤더 제목, 본문 제목, CTA가 동급의 강조를 갖는 것 |

**반드시 서로 달라야 하는 것:** 1차 시선이 이미지인지 수치인지 텍스트인지. Plain/Media/Reward는 포장 스타일이 아니라 **정보의 역할**에서 결정한다.
**반드시 같아야 하는 것:** 기준선, Icon contract, 상태 의미, 타이포 5단 역할, 터치·접근성.

화면 리뷰에서는 1) 2초 내 주인공 한 개 2) 같은 문구 반복 3) 실제로 누르는 대상과 버튼 위계 4) 컴포넌트 선택 근거 5) 320/390/430px 캡처에서 한 번씩 따진다. 이 섹션은 기존 D-012의 수치 계약을 변경하지 않음.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K02 페이지 제작과 선택 절차

**정본 소유자:** `patterns/content-hierarchy.md`, 진입 경로는 `INDEX.md`.

새 페이지는 **사용자 과업 → 콘텐츠와 행동 → 정보 객체·그룹 → 표현 패턴 → 필요한 Component/Shell → 구현 → 검증** 순서로 결정한다. 기존 부품을 수정하는 국소 작업은 관련 계약만 읽는다. 전체 문서 전수 독해를 모든 작업의 의무로 만들지 않는다.

| 기록 | 반드시 답할 것 | 실패하면 |
|---|---|---|
| 목적 | 누가 무엇을 확인/실행하는가, 이 화면 밖 책임은 무엇인가 | 제품·목적 결정으로 복귀 |
| 콘텐츠·행동 | 필수/보조 데이터, 중복/파생 관계, 실제 목적지와 상태 | 의미·인벤토리 정리 |
| 구조·매핑 | 함께 읽을 객체, 나눌 그룹, 채택·제외 패턴의 이유 | 객체와 표현 선택 수정 |
| 위계 | 첫 시선, 아트/수치/텍스트의 역할, 강조·면적의 이유 | 선택 근거와 배분 재검토 |
| 근거·결과 | 실제 데이터/예시 구분, 화면·동작·회귀 결과, 미확인 | 필요한 검증 보충 |

이 기록은 기존 작업 문서나 PR의 한 묶음으로 유지한다. 별도 단계 파일·새 관리 계층을 만들지 않는다. 첫 시선의 중심은 의미 있는 정보 그룹이며, 화면의 모든 섹션을 같은 카드·같은 큰 제목으로 만드는 규칙이 아니다.

공동 보상은 이름으로 표현을 고르지 않는다. **공동 기록 확인은 값+Progress, 이벤트 홍보는 목적 있는 A2 장면, 보상 구성은 정보 객체, 실제 획득 결과는 A3 후보**로 나눈다. 같은 데이터의 요약과 상세는 허용하되 한 영역 안에서 같은 사실·목적지·행동을 표현만 바꾸어 반복하지 않는다.



## K05 정보 객체의 슬롯과 맥락

**정본 소유자:** `patterns/content-hierarchy.md`.

객체의 후보 슬롯은 Media / Identity / Value / State / Action이다. **Identity는 읽을 수 있는 이름을 포함하고, 나머지는 맥락과 실제 데이터가 필요할 때만 사용한다.** 미디어가 실패해도 이름·상태·행동을 해석할 수 있어야 한다.

| 맥락 | 필수 슬롯 | 조건부 슬롯 | 배제 |
|---|---|---|---|
| 보상 구성 | Media, Identity, 보상 구성이라는 상위 맥락 | 정의된 수량·조건·실제 상세 이동 | 구매 가격·장착 행동의 자동 삽입 |
| 상점 | Media, Identity, 실제 가격과 통화 의미 | 재고·구매 가능 상태·실제 구매/상세 | 장착 상태의 무조건 노출 |
| 보관함 | Media, Identity, 소유/장착 상태 | 로컬 선택·사용 제한·상세 | 반복 구매 가격 |
| 획득 결과 | 획득 사실, Media, Identity | 수량·확인/다음 행동 | 목록용 메타 전부 반복 |

이번 실증의 실제 제품 화면은 보상 구성이다. 상점·보관함은 예시 데이터가 명시된 독립 검수 fixture로만 사용한다. 예시 가격·소유·장착을 실제 정책이나 저장 데이터로 승격하지 않는다.

Media와 Information을 DOM 슬롯·간격·정렬로 구분한다. 분리선·서로 다른 배경색은 필수가 아니다. 같은 반복 모음에서는 미디어 비율·이름 시작선·값 기준선을 공유하고, 필요한 텍스트의 높이를 허용한다. 확대 시 열 수를 줄인다.

정적, 이동, 선택은 하나의 시점에 대표 상호작용 하나를 갖는다. 선택은 `aria-pressed` 또는 해당 선택 위젯의 상태, 이동은 링크 목적지로 표현한다. 소유·장착은 도메인 사실이며 선택 상태와 다르다. 원격 반영이 있다면 Pending/Confirmed/Failed를 별도 작업 상태로 다루고 선택만으로 장착 성공을 표시하지 않는다.

범용 ItemCard와 Composite 계층은 만들지 않는다. 실제 반복성과 독립 행동이 확인되면 기존 미디어/텍스트/액션 조합의 작은 구현 단위만 추출한다.
````

---

### foundations/typography.md

````markdown
---
id: F-TYPE
category: foundation
status: proposed
read_when: 제목·본문·숫자 위계, 한글 줄바꿈, 글자 확대
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-002, REF-004, REF-005]
---

# 타이포그래피와 정보 위계

## 제안 스케일
서체는 고정하지 않는다. 웹 검토용 시스템 sans-serif, Android는 기존 서체를 먼저 확인한다. 같은 family·weight가 없어 대체되는 경우 줄바꿈과 실제 글리프 폭을 비교한다. 표의 단위는 논리 크기이며 웹은 기본 16 기준 rem, Android 글자는 sp로 검토한다. 이미지에서 폰트·수치를 확정한 것이 아니다.

| Token | 크기 / 줄높이 | Weight | 역할 |
|---|---|---|---|
| type.root | 26 / 34 | 700 | 최상위 제목, 세부 수치는 D-011 승인 아님 |
| type.detail | 18 / 26 | 600 | Standard 중앙 제목 |
| type.hero | 24 / 32 | 700 | 화면의 주목적·한정된 강조 콘텐츠 |
| type.section | 18 / 26 | 700 | 섹션 제목 |
| type.item | 16 / 24 | 600 | 목록·카드 제목 |
| type.body | 16 / 24 | 400 | 본문 |
| type.support | 14 / 20 | 400 | 보조 설명·시간·기간 |
| type.caption | 12 / 18 | 500 | 짧은 태그·매우 부차적인 수치 |
| type.button | 14 / 20 | 600 | 기본·큰 버튼 |
| type.buttonCompact | 12 / 18 | 600 | 작은 버튼 |
| type.number | 24 / 32 | 700 | 핵심 수치 한 개 |
| type.numberSmall | 16 / 24 | 600 | 목록·가격·기여 수치 |

## 역할별 비례
- 한 화면의 중심은 목적을 대표하는 제목/숫자/아트 중 하나의 그룹으로 만든다. 보조 인사말에 hero를 쓰지 않는다.
- **v0.2 개선:** section 18, item 16, body 16, support 14로 과한 중간 타이틀 크기를 줄인다. section/item이 항상 크기로만 구분되는 것은 아니며 weight·여백도 함께 사용한다. 제목과 수치가 모두 24 이상인 밀집 그룹은 지양한다.
- 큰 아트가 이미 초점을 만들면 인접 문자는 item/body로 낮춘다. 굵기는 유지할 수 있으며 크기·면적·위치로 계층을 조절한다.
- 마스코트 옆 이름·상태·행동은 한 덩어리로 읽힌다. 프로필 아트 크기는 P-HIERARCHY의 역할별 한도를 따른다.
- 가격·보유량·이번 주 획득량을 같은 의미로 합치지 않는다. 단위는 support보다 낮추지 않으며 숫자와 기준선을 맞춘다. 정렬 비교가 필요하면 tabular 숫자 사용을 제안한다.

## 텍스트 사용 금지 사례
- S2-04 반례 08: 출석 체크의 보조 인사말에 `type.hero`를 할당해 달력/실제 수행 상태보다 주목도가 높아지는 것 금지. **인사말은 item/body, 출석 결과는 section 또는 number** 역할로 먼저 평가한다.
- S2-08 마이: 마스코트와 이름·행동·주간 기록 모두 큰 헤드라인이 되어 서로 경쟁하지 않는다.
- S2-05 콘텐츠 탐색: 그림 안에 삽입된 가짜 텍스트를 실제 UI 라벨로 복제하지 않는다.
- 제목 2줄 가능성이 높으면 배지를 같은 줄에 억지로 넣거나 한글을 줄이지 말고 행 높이를 늘린다.

## 줄바꿈과 확장
한글 어절을 보존하되 좁은 폭에서 긴 단어는 넘칠 수 있으므로 비상 줄바꿈을 허용한다. 이미지의 이상한 줄바꿈은 재현하지 않는다. 버튼·선택 라벨은 기본 한 줄이며, 좁아지면 글자를 축소하지 않고 P-ACTIONS의 세로 재배치를 쓴다. 본문은 필요한 높이로 늘리고 고정 높이 카드로 자르지 않는다. 목록 제목은 기본 최대 2줄, 메타데이터는 짧게 하되 필수 날짜·오류 정보는 생략하지 않는다.

## 상태·접근성
Loading에서도 제목 역할과 예상 콘텐츠 폭을 유지한다. 오류 문구를 caption으로 내려 숨기지 않는다. 200% 글자 확대에서 줄높이·컨테이너 높이는 따라 늘어난다. weight만으로 선택·완료를 표시하지 않는다. 스크린리더 제목 순서도 시각적 계층과 맞춘다.

## 근거·영향
강한 제목과 억제된 보조 정보는 관찰 high. 정확한 family·weight·크기는 proposed. REF-005의 보조 인사말 과장·캐릭터 면적 과다를 교정한다. 수치 변경은 제목/행/버튼의 줄바꿈·높이까지 영향이 있으므로 F-GEOMETRY와 함께 비교한다.


## 2026-11-07 Root Font Size 시연 규칙 (모바일 크기 상향)

웹 시연의 `rem` 기반 길이는 루트 `font-size`가 변하면 **텍스트뿐 아니라 내비게이션 아이콘·여백·컨트롤도 비례**한다. 기종/화면 DPI를 직접 탐지하지 않고 **CSS viewport 너비**와 브라우저 기본 글자 크기를 기준으로 조절한다. 기종별 고정 px 매핑이나 JS `devicePixelRatio` 분기 금지.

| CSS 화면 폭 | Root 크기 (기본 16px 전제) | 처리 |
|---|---:|---|
| 320px | 약 16px | 작은 폰에서도 기본 글자 크기를 유지 |
| 360px | 약 16.57px | 선형 보간 |
| 390px | 약 17px | 모바일 기준 폭, 기존 대비 약 6% 증가 |
| 430px | 약 17.57px | 넓은 폰에서 소폭 증가 |
| 480~767px | 약 18.08px → 16px | 태블릿/데스크톱 경계로 연속 복귀 |
| 768px 이상 | 브라우저 기본값 100% | 별도 강제 고정 없음 |

구현: [web/preview/style.css](../../../../web/preview/style.css)의 `:root` 미디어 쿼리. `text-size-adjust:100%`는 브라우저 텍스트 자동 확대의 의도치 않은 변형을 피하기 위한 것으로, 사용자가 설정한 글자 크기·브라우저 확대/축소와 충돌하지 않도록 실기기 검증이 필요하다. **이 스케일은 preview-only trial이며 Compose 글꼴 크기 정책의 확정이 아니다.**

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## 공통 계약 r1의 스케일 선택

이번 웹 개편은 위 기존 스케일 표를 검수 시작값으로 사용한다. 의미 역할은 [C-TEXT](../components/text-hierarchy.md)의 Display/Title/Section/Body/Caption 5개이며 `type.item`/`type.support`는 수치 스케일 이름이다. 역할·HTML heading·실제 크기를 동일 개념으로 합치지 않는다. 기본 16 기준 rem으로 변환하고 기존 RootFont trial은 보존한다. 시각 부적합이 확인되면 리드가 영향과 반례를 갖춰 개정하며 Native 승인값으로 자동 이식하지 않는다.
````

---

### components/text-hierarchy.md

````markdown
---
id: C-TEXT
category: component
status: proposed
read_when: 범용 5단계 텍스트 배치/굵기·줄바꿈
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Text Hierarchy / Typography Roles


> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K03 텍스트 역할과 수치 스케일

**정본 소유자:** 역할·슬롯 매핑은 `components/text-hierarchy.md`, 수치 스케일은 `foundations/typography.md`.

의미 역할은 **Display / Title / Section / Body / Caption**의 5개다. Item과 Meta를 추가 의미 역할로 유지하지 않는다. `type.item`·`type.support`는 기존 수치 스케일 이름으로 존속한다. HTML heading 레벨은 문서 구조로 결정하며 글자 크기와 동일시하지 않는다.

| 실제 슬롯 | 의미 역할 | 사용 스케일 | 웹 어댑터 style |
|---|---|---|---|
| Root 화면명 | Title | type.root | root |
| Standard 화면명 | Title | type.detail | standard |
| 한정된 강조 제목 | Display | type.hero | hero |
| 화면의 핵심 수치 | Display | type.number | number |
| 정보 그룹명 | Section | type.section | section |
| 반복 객체·목록 이름 | Body | type.item | item |
| 본문 | Body | type.body | body |
| 시간·기간·보조 설명 | Caption | type.support | support |
| 짧고 부차적인 상태 라벨 | Caption | type.caption | caption |
| 반복 수치·가격 | Body | type.numberSmall | number-small |

이번 웹 개편은 기존 F-TYPE 스케일을 사용한다: root 26/34·700, detail 18/26·600, hero와 number 24/32·700, section 18/26·700, item과 numberSmall 16/24·600, body 16/24·400, support 14/20·400, caption 12/18·500, button 14/20·600, buttonCompact 12/18·600. 단위는 기본 16 기준 rem이며 실제 RootFont 실험이 적용되면 최종 CSS 크기는 달라진다. 이 선택은 웹 개편의 검수 시작 기준이다.

작은 글자를 더 굵게 만들어 필수 정보를 욱여넣지 않는다. 이름·가격·조건·오류는 읽을 공간을 확보하고 확대 시 높이를 늘린다. 반복 객체 이름은 기본 2줄 공간을 확보하되, 긴 이름을 생략했다면 같은 목적지의 상세나 명시적인 펼침 조작으로 전체 이름을 확인할 수 있어야 한다. 정적 객체에 그런 경로가 없으면 이름 전체를 표시한다. 설명·기간을 Caption이라는 이유로 모두 12로 낮추지 않는다.
````

---

### foundations/geometry.md

````markdown
---
id: F-GEOMETRY
category: foundation
status: proposed
read_when: 패딩·크기·라운딩·콘텐츠 폭·터치영역·안전영역
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-004, REF-005, REF-006]
---

# 간격·크기·형태

## 제안 토큰 정본
단위는 논리 단위다. 웹 간격은 16 기준 rem, Android는 dp로 옮겨 시각 비교한다. 물리 px나 스크린샷 측정값이 아니다.

| Token | 값 | 용도 |
|---|---:|---|
| space.1 / .2 / .3 / .4 / .5 / .6 / .8 | 4 / 8 / 12 / 16 / 20 / 24 / 32 | 공통 간격 스케일 |
| page.inset | 20 | 기본 좌우 여백 |
| page.insetCompact | 16 | 360 미만 폭에서 공통 전환 |
| content.maxWidth | 560 | 단일 모바일 읽기 열, 넓으면 중앙 정렬 |
| section.gap | 24 | 큰 그룹 사이 |
| group.gap | 16 | 관련된 하위 블록 사이 |
| item.gap | 12 | 목록·카드 사이 |
| inline.gap | 8 | 짧은 아이콘·라벨 사이 |
| surface.padding | 16 | 기본 그룹 내부 |
| row.paddingY / .paddingX | 12 / 16 | 목록 내부 |
| radius.control | 16 | 일반 버튼·입력 |
| radius.surface | 16 | 카드·그룹 면 |
| radius.image | 14 | 콘텐츠 썸네일 |
| radius.overlay | 24 | 시트·다이얼로그 |
| radius.pill | 999 | 의미상 칩·세그먼트 thumb |
| stroke.default | 1 | 경계 |
| stroke.selected | 2 | 선택 경계 |
| size.actionSmall / .actionMedium / .actionLarge | 34 / 43 / 48 | v0.15 시연의 표시 높이, 48 미만은 별도 비중첩 터치 타깃 필요 |
| size.touchMin | 48 | 클릭·터치 영역 최소 |
| size.rowMin | 56 | 목록 행의 최소 높이 |
| size.topBarMin / .bottomBarMin | 60 / 76 | OS inset 제외한 상·하단 content 최소 높이 |
| action.paddingXSmall / .paddingXMedium / .paddingXLarge | 12 / 16 / 18 | 버튼 size별 좌우 padding |
| action.paddingYSmall / .paddingYMedium / .paddingYLarge | 7 / 9 / 13 | 버튼 size별 상하 padding, 줄높이를 줄인 만큼 여백 확보; 버튼 높이는 유지 |
| segment.paddingX / .trackHeight | 12 / 43 | 세그먼트 좌우 padding·보이는 기본 높이 |
| badge.paddingX / .paddingY | 11 / 3 | 기본 Badge 내부, XS는 X=8·높이 21 |
| field.paddingX / .paddingY | 16 / 12 | 입력 내부 padding |
| progress.height | 10 | 기본 진행 막대 높이 |
| size.iconSmall / .iconDefault / .iconNav | 16 / 20 / 27 | 일반 UI 아이콘 슬롯, 버튼 내부와 구분 |
| size.iconButtonContent / .iconButtonCompact | 17 / 16 | D-012 버튼 아이콘 허용16–18에 맞춘 일반/작은 size; 구형14 후보 사용 중단 |
| size.avatarRow / .avatarProfile / .avatarHeroMax | 40 / 64 / 104 | 행 / 일반 프로필 / 명시적 몰입 영역 상한 |

## 개선 변경의 근거 (제안값)
S2-01/04/08에 나타나는 Button 내부 콘텐츠 과대·Avatar 과대를 줄이기 위해 `iconButtonContent=18→16`, `avatarProfile=80→64`, `avatarHeroMax=120→104`, `paddingXLarge=20→18`을 v0.2 후보로 조정. 전체 버튼 높이·터치 최소 48·페이지 여백은 보존한다. **수치 감소가 곧 합격은 아니며** 웹에서 동일 fixture 전후 비교가 필요하다. 48 미만 보이는 버튼은 비중첩 48 hit target을 별도로 확보한다.

## v0.6 변형 규격
- Progress는 C-STATUS **한 종류**, `micro=6`, `default=10`, `featured=12`의 높이 속성으로 표현한다. 별도 Progress 컴포넌트를 만들지 않는다.
- Top Nav 60dp(상태바 제외), glyph 24dp, hit target 48dp. Bottom Nav 76dp(inset 제외), glyph 27dp, 라벨 11–12sp, Gap 4dp. 실제 탭 수는 O-003 미결.
- Button 16dp radius를 기준으로 하고 로딩/프레스 시 Label center·Icon slot·border box 이동 금지. 내장 아이콘은 16–18dp, Label/Icon Gap 6–8dp.
- Rarity Surface에도 16dp radius를 공유하지만 등급색과 이미지 속성은 별도 C-RARITY에 소유. 3단 Bottom Sheet는 C-SHEET의 content-driven snap과 S-PAGE 안전영역 참조.

## 적용 규칙
같은 size는 tone·treatment가 바뀌어도 높이·padding·radius가 같다. radius는 의미별 family 토큰을 공유하며 같은 숫자를 모든 모양에 강제하지 않는다. 버튼과 카드 내부 패딩을 개별 화면에서 임의로 줄이지 않는다.

최소 높이는 글자 확대 시 증가할 수 있다. 32·40 높이 컨트롤은 주변을 포함한 비중첩 48 터치영역을 확보한다. 공간이 부족하면 큰 size/줄바꿈 배치로 바꾸며 투명 hitbox를 겹치지 않는다. 작은 버튼을 빽빽하게 놓고 모두 48 target이라고 주장하지 않는다.

모든 콘텐츠의 시작선은 page.inset 또는 같은 surface.padding에 맞춘다. 제목·아이콘·숫자의 실제 보이는 경계는 광학적으로 미세 보정할 수 있으나 로컬 padding 토큰을 늘리지 않는다.

## 반응형·안전영역
320/360/390/430 논리 폭과 200% 글자를 검토 기준으로 제안한다. 고정 열 개수를 이미지에서 가져오지 않는다. 2차원 에셋은 contain/crop 역할을 명시하고 텍스트는 폭과 함께 재흐름시킨다. 가로 목록은 넘치는 단서를 남기고 잘린 버튼을 주 조작으로 쓰지 않는다.

OS safe inset은 페이지 spacing과 별개다. 아래 inset·키보드·하단바는 S-PAGE가 한 번만 합산한다. iPhone 프레임·노치·OS 시계를 앱의 padding으로 추출하지 않는다.

## 상태·근거·영향
Pressed/selected/loading에서도 외곽 크기를 유지한다. 긴 라벨·오류·확대에서는 높이 증가를 허용한다. 둥근 면의 반복은 관찰 high, 숫자·폭 전환 기준은 proposed이며 실기기 미검증이다. 전역 변경 시 버튼·목록·시트·하단 고정 영역을 같이 확인한다.


## v0.15 적용 우선 규격

현재 사용자 채택 웹 시연의 기본 Button은 43px, Small 34px, Large 48px, Radius 16px. 클릭 영역은 Android에서 최소 48dp를 확보하며 hit 영역을 겹치지 않게 한다. 기본 7-tone Pill Badge 높이 26, XS 21, 아이콘 13, paddingX 기본 11 / XS 8. Sheet의 동적 min 기준·Safe Area 상세는 [C-SHEET](../components/bottom-sheet.md). SVG 획은 제공 원본의 **1.8**이 정본이며 이 문서에서 덧칠하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## 공통 계약 r1의 적용 기준

- 버튼의 기본 inline Glyph는 승인 기본17, compact는 허용범위 안의16을 사용한다. 이전 변경 근거의14는 당시 실험 기록이다.
- 표시 높이34/43/48과 실제 target을 분리하며 웹 target은 48 CSS px와3rem 중 큰 값 이상으로 비중첩 확보한다. 실제 링크/버튼이 target을 소유하고 내부 face를 중앙에 둔다.
- page.inset은 360 CSS px 미만1rem, 이상1.25rem. 같은 라우트의 헤더·본문은 같은 gutter와 가용 외곽 폭을 참조한다. OS safe inset은 별도다.
- 표시 높이는 확대 시 상한이 아니며 텍스트가 필요하면 성장한다. topBarMin/bottomBarMin 등 기존 후보 수치를 무조건 고정 높이로 사용하지 않는다.
- Glyph 박스 정렬과 타깃 중심은 [NAV-TOP](../../TOP_NAVIGATION.md), 실제 고정 영역·이력은 [S-PAGE](../shells/page-shells.md)가 소유한다.
````


---

### components/selection-controls.md [history]

````markdown
---
id: C-SELECTION
category: component
status: proposed
read_when: Tabs·세그먼트·필터칩·선택·체크·스위치
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005, SRC-SEED-SEGMENT]
---

# 탐색과 선택 컨트롤

## Purpose / Non-goals
비슷한 알약 모양을 동작이 다른 컴포넌트로 구분한다. 이 문서는 관련 선택 family를 함께 라우팅하지만 하나의 만능 pill API로 합치지 않는다. 하단 목적지는 S-PAGE의 미결 사항이다.

## 책임 분리 · v0.2
이 파일은 Checkbox/Radio/Switch 등 선택 family의 **공통 원칙만** 소유한다. 독립된 역할·상태·접근성 계약은 [C-TABS](tabs.md) · [C-SEGMENT](segmented-control.md) · [C-CHECK](checkbox.md)가 정본이다. 같은 속성을 여기에서 다시 정의하지 않고 각 명세를 링크한다.

## v0.6 책임 분리
- 상호 배타적 Radio Choice의 Group/Indicator/Checked·Focus·Disabled는 [C-RADIO](radio-choice.md)가 정본이다.
- Checkbox는 [C-CHECK](checkbox.md), 상위 탐색은 [C-TABS](tabs.md), 기간/보기는 [C-SEGMENT](segmented-control.md) 정본을 따른다.
- Segmented는 Chip을 두 겹으로 감싸지 않는다. 하나의 컨테이너 + 선택 Thumb + Label. 실용적인 선택값 2~4개, 콘텐츠 바로 위에 위치. `Tabs`의 네비게이션과 목적이 다르다.
- 다크 OFF Switch는 독립 `disabled` 값이 아니라 off 상태의 대비를 보장해야 하며, 라이트 전용 회색을 그대로 재사용하지 않는다.

## Anatomy / Properties
| 종류 | Anatomy·속성 | 의미·배치 |
|---|---|---|
| Tabs → C-TABS | Tab List + Label + Indicator, selectedKey | 관련 섹션 탐색, 아래 콘텐츠 영역과 직접 연결; 상세 계약은 독립 정본 |
| Segmented → C-SEGMENT | Track + Segment Label + Thumb, 단일 value | 같은 데이터의 보기·기간 등 즉시 전환; 상세 계약은 독립 정본 |
| Filter Chip | Label + 선택적 Close/Icon, selected/value | 조건 적용, 복수 선택·해제 가능 여부 명시 |
| Checkbox → C-CHECK | Indicator + Label + 선택적 설명, checked/mixed | 사용자 수동 완료/독립 선택; 상세 계약은 독립 정본 |
| Radio | Indicator + Label, group value | 상호 배타적 선택 |
| Switch | Track + Thumb + Label, checked | 즉시 적용되는 켜기/끄기 설정 |

## Tokens / Size
공통 type.body/support, radius.control/pill, 공간·터치는 Foundation 정본 사용. Tabs는 배경 상자 대신 선택 밑줄을 기본 제안한다. Segmented는 bg.muted Track과 bg.surface Thumb, fg.primary 라벨을 사용한다. 좌우 padding은 segment.paddingX이며 불필요하게 늘리지 않는다. 내용이 짧으면 hug, fill은 실제 동일 폭 선택 구조일 때만 쓴다. 긴 라벨은 눌러 넣거나 작은 글자로 바꾸지 않는다. Track 높이와 target은 F-GEOMETRY를 따른다.

## States / Behavior
Unselected / Selected에 Enabled·Pressed·Focus·Disabled가 조합될 수 있다. 선택은 형태+상태 의미를 제공하고 색만 바꾸지 않는다. Disabled-selected도 선택 정보를 유지한다. Tabs는 한 영역의 단일 선택과 연결 패널을 노출한다. Focus 이동과 값 확정을 플랫폼 관례대로 분리한다. 필터 적용이 지연되면 pending을 표시하며 성공 전 활성 결과처럼 단정하지 않는다.

Checkbox mixed는 일부 선택에만 사용한다. Switch는 저장 버튼이 별도 필요한 임시 선택에 쓰지 않는다. 데이터 조회 실패가 selected를 임의로 초기화하지 않도록 실패 피드백을 둔다.

## Guidelines
상위 Tabs → 관련 필터 → 콘텐츠를 구분한다. 정렬을 선택 컨트롤 Track에 끼워 넣지 않는다. 여러 Segmented를 겹쳐 어느 영역을 바꾸는지 모호하게 만들지 않는다. Tabs와 Segmented를 모양이 비슷하다는 이유로 교환하지 않는다.

## Accessibility
짧은 라벨, 현재 값, disabled, Checkbox group의 이름을 읽을 수 있어야 한다. 최소 target·포커스 ring 적용. 조작 표시 경계는 stroke.control을 사용한다. 상태 변화 후 포커스 유실·불필요한 스크롤 점프를 막는다.

## Evidence & Confidence / Impact
Tabs·기간 선택은 REF-001/002/004 관찰 high, 동일 화면의 선택 요소 밀집 문제는 REF-005 교정. Checkbox/Radio/Switch 및 keyboard·pending 상태는 이미지 미노출의 proposed 보완이다. 구체적 필터·탭 명칭·개수는 화면 데이터이며 시드가 확정하지 않는다.
````

---

### components/list-item.md [history]

````markdown
---
id: C-LIST
category: component
status: proposed
read_when: 목록·랭킹·친구·설정·채팅 진입 행·섹션 헤더
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005]
---

# List Item과 List Header

## Purpose / Non-goals
반복 정보를 동일한 축으로 읽고 필요한 행동으로 연결한다. 퀘스트·친구·랭킹·채팅 등 제품 이름으로 별도 행 스타일을 만들지 않는다. 독립적인 큰 미디어가 핵심이면 C-SURFACE를 사용한다.

## Anatomy / Properties
Prefix(아이콘/Avatar/순위 중 필요한 조합) + Content(Title, Detail, Metadata) + Suffix(수치/상태/보조 Action/Chevron). 클릭 가능한 행과 정적 행을 구분한다. Header는 Section Title + 선택적 count + Low Action이며 행 본문과 다른 역할이다.

variant는 oneLine/twoLine/mediaLeading, interaction은 static/navigate/select/actionable. Prefix가 없으면 Content 시작선만 공통 gutter에 정렬한다. 리스트 안에 버튼이 있으면 부모 행과 별개 조작으로 모델링하고 중첩 클릭 영역을 만들지 않는다.

## Tokens
row.paddingX/Y, size.rowMin, size.avatarRow, item.gap, type.item/body/support, fg.primary/secondary, stroke.subtle을 참조한다. 수치는 type.numberSmall로 우측 기준선 정렬한다. Avatar와 조작 아이콘은 다른 크기 역할이다. 행마다 카드 그림자를 붙이지 않는다. List Header 전체 보기 버튼은 C-ACTION의 Low를 쓴다.

## States / Behavior
Default·Pressed·Focus·Disabled·Selected를 지원할 수 있으며 조작 없는 정적 행에는 pressed를 주지 않는다. 선택 행은 체크/현재 상태를 함께 표시한다. 로딩은 동일한 prefix/title/suffix 슬롯의 Skeleton을 사용한다. 빈 리스트·조회 오류·오프라인은 P-STATES의 그룹 피드백을 사용하며 가짜 멤버·수치를 채우지 않는다.

## S2-07/08 반복 행 공통 검수
- 클랜 공지·멤버 순위·채팅 진입·친구 활동에 같은 Prefix/Content/Suffix 축을 사용한다. **클랜 채팅만** 굵은 독립 테두리나 다른 반경으로 만들지 않는다.
- '이름 + 내용 + 시간'과 '이름 + 기여 + 순위'는 서로 다른 데이터지만 typography, padding, icon rhythm은 공유한다. 메타데이터가 길어질 때 우측 수치·시간이 밀려 사라지지 않게 줄바꿈/2단 배치.
- 아바타의 투명 표현은 C-AVATAR, 아이템 이미지는 C-IMAGE에서 결정한다. 이미지를 리스트마다 원형 배경 강제하지 않는다.

## Guidelines
채팅 진입 행만 큰 배너·특이한 테두리·큰 아이콘으로 격상하지 않는다. 기능상 중요한 진입점이면 의미 있는 제목/배치로 해결하고 공통 슬롯을 유지한다. 순위·멤버명·기여 수치는 각각 비교 축을 고정한다. 구분선은 Prefix 아래까지 무조건 연장하지 않고 Content 시작선 기준을 통일한다.

## Accessibility
읽기 순서 Prefix 의미 → Title → Detail → 값 → 행동. 장식 순위 아이콘은 중복 읽기에서 제외한다. 행 안 개별 행동은 이름과 target을 가진다. 길어진 이름·200% 확대에서 suffix를 가리면 세로 재배치하며 수치를 잘라내지 않는다.

## Evidence & Confidence
반복 Avatar/이름/값과 설정 행은 REF-001/002/004 관찰 high. REF-005의 클랜 채팅 스타일 이탈을 교정한다. 상태·슬롯 규격은 proposed.

## Dependencies & Impact
Prefix·Suffix가 바뀌는 정도는 variant로 처리한다. 새 조합이 독립 행동·상태를 제공하지 않으면 새 컴포넌트로 승격하지 않는다. 정렬 변경은 모든 목록 및 P-HIERARCHY에 영향이 있다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K09 반복 목록과 탐색 컨트롤

**정본 소유자:** `components/list-item.md`, `components/tabs.md`, 관련 선택 family 정본.

목록은 Prefix / Content / Suffix 축을 공유한다. 짧은 신원·값 한 그룹은 center, 여러 줄 활동·설명은 top으로 정렬한다. 같은 목록에서 콘텐츠 사유 없이 정렬을 바꾸지 않는다. 이름이 길면 Suffix가 정보를 가리기 전에 아래 행으로 재배치한다. 정적 행은 Pressed를 갖지 않는다.

행 전체 이동과 독립 반응 버튼이 동시에 있으면 정적 부모 안에 형제 링크/버튼을 둔다. 행 하나에 4개 이상의 독립 조작을 배치하지 않는다. 부모·자식 반경은 같은 숫자를 기계적으로 반복하지 말고 내부 패딩과 맞는 경계를 선택한다.
````


---

### implementation/v015.md [K12 · r1 history only]

````markdown
> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K12 웹 작업자 간 고정 인터페이스

**정본 소유자:** `implementation/v015.md`의 개편 웹 어댑터 절. 아래 이름은 이번 작업부터 고정한다. TypeScript 표기는 기존 정적 JS 작업을 위한 문서 계약이며 새 빌드 체계를 뜻하지 않는다.

### 표현 소유권

| 소유자 | 구현 범위 | 계약 |
|---|---|---|
| A | style.css, app.js, index.html | 공통 CSS·Shell·이력·정적 기존 마크업의 연결 |
| B | clan.js, clan.css | 클랜 데이터→마크업·지역 레이아웃·지역 이벤트 |
| 리드 | 기존 docs 정본 | 역할·의미·규칙·값의 결정과 변경 영향 |
| C | 독립 검수 fixture·결과 | 읽기/실행 검수. 제품 파일 동시 수정 금지 |

B는 공통 클래스의 font-size/color/height/radius를 지역 선택자로 덮어쓰지 않는다. A는 클랜 이름·수치·아이템 정보 구조를 공통 CSS에 하드코딩하지 않는다. 독립 공통 runtime JS 모듈이나 전역 ItemCard API를 추가하지 않는다.

### 공통 CSS/마크업

| 표면 | 고정 이름/속성 | 책임 |
|---|---|---|
| 텍스트 | `.seed-text[data-text-role][data-text-style]` | role/style은 K03 표의 조합만 사용 |
| 액션 | `.seed-action[data-variant][data-size]`, 내부 `.seed-action__face`, `__label`, `__icon` | size=small/default/large, variant는 K07. 타깃은 바깥 실제 링크/버튼, face는 내부 표시 |
| 미디어 | `.seed-media[data-media-kind][data-media-state]`, 내부 `.seed-media__image`, `__fallback` | kind=item/thumbnail/scene, state=loading/loaded/error. 비율은 `--media-ratio`, fit은 `data-media-fit=contain/cover` |
| Progress | `.progress-track[data-thickness][data-progress-state] > .progress-fill` | 기존 클래스 재사용. thickness=micro/default/featured, state=ready/loading. `--progress`는 0%–100%의 퍼센트 문자열 |
| Progress 조회 불가 | 일반 텍스트 `.seed-text` | `.progress-track`과 range role을 생성하지 않음 |
| 셸 | `--page-gutter`, `--page-max` | A만 설정. 헤더와 해당 본문이 공유 |

기존 `.progress-track span` fill 규칙은 `.progress-fill`로 범위를 명확히 하되 기존 정적 마크업을 A가 함께 연결한다. `.clan-progress-track`은 호환용 레이아웃 이름만 남길 수 있고 fill의 기본 표시를 소유하지 않는다. `.gallery-button`은 원래 갤러리의 레이아웃 별칭으로 남길 수 있으나 실제 액션 표현은 `.seed-action`으로 연결한다. 선택 상태는 실제 ARIA와 K05에 따르고 단순 `is-active` 클래스만으로 만들지 않는다.

data-text-role의 실제 속성값은 JSON과 같은 소문자 `display|title|section|body|caption`이며 data-text-style은 K03의 마지막 열을 그대로 사용한다. `--media-ratio`는 CSS aspect-ratio 형식이며 item은 기본 `1 / 1`, thumbnail/scene은 호출자가 해당 모음의 비율을 명시한다.

### 클랜 내부 데이터 계약

```ts
type ProgressInput = {
  label: string
  value: number | null
  max: number | null
  state?: 'ready' | 'loading' | 'unavailable' // 기본 ready
  freshness?: 'current' | 'stale'            // 기본 current
  thickness?: 'micro' | 'default' | 'featured' // 기본 default
  compact?: boolean                        // 기본 false, 메타 배치만 변경
}
// clan.js 내부 helper. HTML 텍스트/속성은 기존 safe()로 이스케이프한다.
progress(input: ProgressInput): string

type ClanViewState = {
  myTab: 'members' | 'record' | 'activity'
  clanFilter: 'all' | '공부' | '습관' | '운동'
}
// 기존 supports/render/previewMenu는 유지하고 아래 두 메서드만 추가한다.
PreviewClan.captureViewState(): ClanViewState
PreviewClan.restoreViewState(state: Partial<ClanViewState> | null): void
```

capture는 복사본을 반환한다. restore는 허용된 값만 반영하고 null·잘못된 값은 기존 유효 지역 상태를 유지한다. restore가 DOM·해시·스크롤·클랜명/소개/깃발/응원 상태를 변경하지 않는다. A는 render 전에 지역 상태를 복원하고 render 후 scroll을 복원한다.

**B→A 통지:** 정상 사용자 조작으로 myTab/clanFilter가 실제로 바뀐 직후 B는 `document`에 이름이 `seed:clan-viewstate`인 Event를 dispatch한다. detail은 없다. A가 이 이벤트를 받아 captureViewState()로 현재 entry의 snapshot을 replaceState에 반영한다. restore는 이벤트를 발생시키지 않고 A도 캡처에 대한 변경 이벤트를 되보내지 않는다. A가 B의 클릭/키보드 구현 세부를 추적하거나 B가 history에 직접 쓰지 않는다.

`history.state.seedPreview`는 A가 소유한다. 입력은 `entryId`, `route`, `parent`, `scrollY`, `focusKey: string|null`, `clan: ClanViewState|null`이다. 기존 다른 history.state 필드는 보존하고 entryId는 같은 history entry의 replaceState에서 유지한다. 같은 URL의 서로 다른 entry도 구분한다. 이탈 직전 캡처하고 복귀 시 해당 entry 상태를 사용한다. B가 직접 history.state에 쓰지 않는다.

초기 로드와 새 해시 진입에서 entryId를 붙여도 같은 entry에 기존 상태가 있으면 유지한다. scroll과 지역 상태는 변경될 때 현재 entry에 replaceState로 함께 갱신한다. popstate 후에 방금 떠난 entry의 scroll을 쓸 수 있다고 가정하지 않는다. 뒤로가기에서 떠난 entry를 덮어쓰지 않고 도착 entry의 저장값을 읽는다. 기존 링크의 새 해시 이동을 처리하면서 pushState를 추가 호출하여 중복 이력을 만들지 않는다.

**포커스 복원:** A는 focusin에서 현재 entry의 마지막 유효 focusKey를 저장한다. 기존 고유 id는 `id:<id>`, 그 밖의 조작은 `key:<data-focus-key>` 형식이다. A가 Shell 조작의 키를, B가 클랜 내 이동/탭/필터 조작의 안정적인 id 또는 data-focus-key를 유지하며 이름·문구·배열 인덱스로 키를 생성하지 않는다. 같은 페이지 안에서 중복 키를 허용하지 않는다. 복귀 순서는 지역 상태 복원 → render → 저장 타깃 focus({preventScroll:true}) → 저장 scrollY 복원이다. 타깃이 사라졌으면 현재 활성 탭, 그것도 없으면 main의 첫 heading을 임시 tabindex=-1로 포커스한다. 원래 위치로 복원하면 타깃이 가려지는 경우 타깃 접근 가능성을 우선해 최소 추가 스크롤한다. scrollY는 유한한0이상 값만 받고 실제 스크롤 범위로 제한한다. 사용자의 포커스를 초기화하는 전체 rerender 후에도 이 계약을 따른다.

Progress compact는 더 얇은 별도 Progress가 아니다. 값·목표·label은 호출자가 명시한다. B는 내부 helper를 사용하고 A의 정적 홈 예제도 같은 마크업·수치 규칙을 따른다. 필수 label이 비었으면 개발/검수 오류로 판정하고 이름 없는 range를 노출하지 않는다.
````

---

### design/TOP_NAVIGATION.md [r1 history only]

````markdown
> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## 공통 계약 r1의 웹 적용

Root/Standard의 승인 구조는 그대로 유지한다. 이번 웹 개편의 역할·수치 매핑은 [C-TEXT](seed/components/text-hierarchy.md), 타깃·gutter·이력 계약은 [S-PAGE](seed/shells/page-shells.md)의 K10을 따른다. Glyph 박스와 실제 target의 중심을 맞추며 icon별 translateX로 액션 간격을 보정하지 않는다. 기존 다크 Shell은 웹 trial이며 헤더의 새 전역 색상 승인으로 기록하지 않는다.
````


---

### components/badge-progress.md [r3 P1 canonical cleanup history]

````markdown
---
id: C-STATUS
category: component
status: proposed
read_when: Badge·Tag·진행률·숫자·완료 상태 표시
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004]
---

# 상태 표시: Badge와 Progress

## Purpose / Non-goals
Badge는 짧은 속성·상태, Progress는 기준에 대한 진행을 표시한다. 둘은 동작이 다른 별도 재료이며 이 문서에 작은 상태 표시 family로 함께 문서화한다. 눌러 필터가 바뀌면 Badge가 아니라 C-SELECTION이다.

## Anatomy / Properties
Badge: Container + Label + 선택적 Icon, tone(neutral/brand/success/info/warning/danger/reward), emphasis(weak/outline).
Progress: Track + Indicator + Value label + 선택적 목표 설명, mode(determinate/indeterminate), value/max. 현재 값·목표·비교 기준을 슬롯 데이터로 받는다. 제품 재화·XP를 기본값으로 고정하지 않는다.

## Tokens
Badge는 type.caption/support, radius.pill, badge.paddingX/Y 사용. 무채색이 기본, 의미 있을 때 semantic 색 사용. Badge의 작은 면과 글자가 주변 제목보다 튀지 않게 한다. Progress는 progress.height, radius.pill, bg.muted track과 accent.solid indicator를 기본 제안한다. 이벤트의 금색·보라는 A-ART의 맥락별 표현이며 전역 진행률 기본색이 아니다.

## Progress v0.6 정리
독립 컴포넌트 **Progress 한 개**가 `thickness=micro(6)|default(10)|featured(12)`을 속성으로 제공. Track과 Indicator의 높이·rounded cap을 함께 맞춘다. 0/49/99/100%, indeterminate, offline stale의 퍼센트 표기를 의미에 따라 구별. 등급색을 진행률 기본색에 혼입하지 않는다.

Credit Value는 [C-CREDIT](credit-value.md)에서 이미지를 포함한 숫자 슬롯을 정의한다. Badge/Progress를 코인+수치의 정본으로 겸용하지 않는다.

## States / Behavior
Badge는 정보 표시라 focus/pressed를 갖지 않는다. Progress는 zero / partial / complete / indeterminate / unavailable을 구분한다. 데이터가 없거나 오프라인 값이 오래되었으면 완주처럼 채우지 않는다. value는 유효 범위로 표시하되 이상 데이터 자체를 숨기지 않고 별도 오류를 남긴다. complete는 실제 완료 상태와 일치할 때만 표시한다.

## S2-06/08 상태 강조의 선
- 일상적인 '진행 중/완료/착용 중'은 작은 Badge와 실제 text로, 한정된 보상 순간의 강조는 A-ART로 분리한다. 아이템의 금색·보라를 모든 일반 UI의 semantic status로 복제하지 않음.
- 퍼센트나 보유 캐시는 실제 데이터의 label을 붙이고 비교 가능해야 한다. 이미지를 보고 가상의 XP/Level을 만들지 않는다.

## Guidelines
색만으로 진행·착용·제한을 구분하지 않는다. 보유 캐시와 획득 캐시는 별도 라벨을 둔다. 컨셉의 XP/시즌 레벨/HP/보석/가격 표시는 표현 예시이며 제품 요구를 추가하는 근거가 아니다. 진행 막대만으로 완료/수락 버튼 역할을 대신하지 않는다.

## Accessibility
Progress의 이름·현재 값·최댓값을 전달한다. indeterminate는 알 수 없는 퍼센트를 만들지 않는다. 짧은 Badge도 읽을 수 있는 대비를 갖는다. 지속 변화는 스크린리더에 과도하게 반복 알리지 않는다.

## Evidence & Confidence / Impact
진행·수치·작은 태그는 관찰 high. 정확한 상태 및 토큰은 proposed. 기본 값·모양 변경은 목록·배너·수집 화면의 비교 가능성에 영향이 있다.


## v0.15 Badge/Progress 확정 시각 규격 (D-012)

Badge 톤 = `neutral|brand|success|info|warning|danger|reward` 7종. `label` 필수, `icon|dot`은 선택적. 높이 26px, XS 21px, Radius Pill; 기본 paddingX=11, XS=8, 아이콘 13px, 미세 outline(시연 기준 Light 약 12% / Dark 약 24% 전경색 혼합). 의미색 토큰을 적용하고 한 화면의 모든 Badge를 Brand로 채우지 않음. Badge는 정보 표시, 필터/선택은 C-SELECTION을 사용.

Progress는 **공통 컴포넌트 하나**, thickness=6/10(default)/12(feature) 속성. 같은 도메인 값을 다른 Progress component로 중복 선언 금지. 실재 수치·완료 여부 및 스크린리더 Value는 앱 데이터에서 전달.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K08 상태·Progress·재화

**정본 소유자:** 상태/Progress는 `components/badge-progress.md`, 금액은 `components/credit-value.md`.

Badge는 정보 표시이며 필터·선택 버튼이 아니다. 톤은 승인된 neutral/brand/success/info/warning/danger/reward 7종을 사용한다. 객체당 기본 1–2개로 제한하고 일반 인원수·설명을 모두 Badge로 바꾸지 않는다. 이는 정보량 기본 기준이며 필수 사실을 지우는 규칙이 아니다.

Progress는 공통 표현 하나에 thickness micro6/default10/featured12를 사용한다. 값·목표·표시값·남은 값·채움·ARIA는 같은 입력에서 만든다. 숫자와 채움 퍼센트를 별도 하드코딩하지 않는다.

| 입력/상태 | 실제 표현 | 접근성/판정 |
|---|---|---|
| ready, value0, max1000 | 0 / 1,000, 채움0 | 유효한 0 |
| ready, 620/1000 | 620 / 1,000, 채움62%, 필요한 경우 남은380 | min0/max1000/now620 |
| ready, 1000/1000 | 채움100%, 목표 달성 | 지급·수령 완료로 해석하지 않음 |
| ready, 1200/1000 | 실제1,200 / 1,000, 채움100%, 목표 초과 의미 | now1000, 실제 초과값은 외부 텍스트와 valuetext로 전달 |
| loading | 퍼센트 없는 진행 중 표시 | now 생략, 무모션 대안 |
| unavailable, 값 없음, NaN/Infinity/음수, max≤0 | ‘기록 확인 불가’와 가능한 복구 경로 | 0%나 완료로 위장하지 않고 range 위젯을 노출하지 않음 |
| ready + stale, 마지막 유효 값 | 마지막 확인 기록임을 밝히고 해당 값 표시 | 현재 완료·새 수령 가능으로 단정하지 않음 |

목표 초과는 정상적인 초과 성취일 수 있으므로 비정상 데이터와 구분한다. 외부 값은 숨기지 않고 시각 채움만 100%로 제한한다. loading은 처리 상태이며 데이터 오류를 indeterminate로 무한 대기시키지 않는다. 이 표가 이전 계획의 ‘범위 초과는 모두 오류’로 읽힐 수 있는 부분을 대체한다.

판정 순서는 **label과 enum 유효성 확인 → 명시 unavailable → 명시 loading → ready의 숫자 검증**이다. loading은 value/max가 null이어도 정상이며 숫자값을 표시하지 않는다. 숫자 누락·오류 행은 ready에 적용한다. ready는 number 타입의 유한 value≥0과 max>0을 요구하고 문자열 숫자를 묵시 변환하지 않는다. freshness=stale은 ready의 유효 값에만 적용한다. 비율은 `min(value,max) / max × 100`으로 계산하여 매우 큰 유한 초과값도 중간 곱셈에서 Infinity가 되지 않게 한다. 잘못된 label/enum은 개발·검수 오류로 남기고 이름 없는 range를 만들지 않는다.
````
