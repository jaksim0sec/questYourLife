---
id: SEED-INDEX
category: router
status: proposed
read_when: 디자인 시드 진입·필요 문서 선택
depends_on: []
references: [U-01, U-02, D-011]
---

# 갓생패치 · 디자인 시드 INDEX

v0.15 채택 시드 · 2026-11-05 Asia/Seoul · **[D-012](../docs/DECISIONS.md)에서 명시적으로 확정한 시각 규격에 한정해 approved.** 개별 문서의 기존 proposed 항목 및 Android 앱 동작·계정·경제·IA는 계속 미검증이다. 정책은 [DECISIONS](../docs/DECISIONS.md) / [OPEN](../docs/OPEN.md), 추출 방식은 [AUTHORING GUIDE](SEED_AUTHORING_GUIDE.md) 정본을 따른다.

기준: 중립적이고 정돈된 조작 UI + 굵기 있는 명확한 정보 계층 + 붉은 도장 정체성 + 표현력 있는 3D 보상. 컨셉의 콘텐츠·탭 수·배치를 복제하지 않는다. 새 토큰/상태는 제안, 기존 상단은 승인 정본이다.

**2026-11-10 D-015 추가 승인:** 상단 Root/Standard의 최종 서체·폰트 크기·굵기·아이콘/여백 및 웹 하단 마이 스마일·클랜 Fill 매핑은 [NAV-TOP](TOP_NAVIGATION.md) → [C-BOTTOM](components/bottom-navigation.md) → [기계 판독값](implementation/v015-tokens.json)으로 진입한다. 공통 CSS는 `seed/styles/seed-standard.css`가 단독 소유하고 Lab A/B 시험 스타일은 종료했다. 기존 Soft 56종과 전체 제품 IA는 그대로 둔다.

**에셋 작업 진입:** SVG/PNG/WebP의 파일 정본·미래 공통 경로·preview 참조 이전은 [공통 에셋 관리 계약](../docs/assets/README.md)이 소유한다. 이 INDEX의 `A-ART`, `C-IMAGE`, `F-ICON-MOTION`은 각각 **표현·프레임·아이콘 규격**을 소유한다. 실제 파일의 위치를 여기서 중복 선언하거나 이관 전 경로를 앞서 바꾸지 않는다.

## 선택적으로 읽기

1. 기존 부품의 국소 수정은 해당 Component와 직접 의존성만 읽는다. 새 페이지는 제품 과업·콘텐츠·행동을 먼저 결정한다.
2. 새 페이지는 제품 목적·정보 객체를 확인하고 [페이지 구성](patterns/page-composition.md)에서 섹션, [공간 배치](patterns/layout-composition.md)에서 자식의 배치를 선택한다. P-HIERARCHY는 표현 비중을, S-PAGE는 Shell을, 기존 Component는 실제 UI 슬롯을 소유한다. 아트 역할 선택 시 A-ART를 읽는다.
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
| P-LAYOUT | 범용 공간 배치·섹션 구조 | pattern | 세로·가로·격자·겹침/비율·스크롤·책임 | [patterns/layout-composition.md](patterns/layout-composition.md) | F-GEOMETRY, C-SURFACE, C-IMAGE, C-LIST | proposed | D-011–013, REF-001–004 |
| P-PAGE-COMPOSE | 페이지 섹션 구성과 선택 규칙 | pattern | 목적→섹션→배치→기존 부품으로 페이지를 구성할 때 | [patterns/page-composition.md](patterns/page-composition.md) | P-HIERARCHY, P-LAYOUT, C-SURFACE, S-PAGE | proposed | D-001, D-011–013 |
| P-STATES | 빈·오류·오프라인·대기·복구 | pattern | 상태별 표현/실제 결과 | [patterns/state-feedback.md](patterns/state-feedback.md) | C-OVERLAY, C-STATUS, C-ACTION, C-FIELD | proposed | REF-001/003, U-02 |
| P-SHEET | Bottom Sheet Flow | pattern | Sheet Footer·본문·Snap 조합 | [patterns/bottom-sheet-flow.md](patterns/bottom-sheet-flow.md) | C-SHEET, C-ACTION, C-TEXT, C-GESTURE, S-PAGE | proposed | U-05 |
| P-BTN-ICON | Button + SVG Layout | pattern | 버튼 SVG/Label/Loading의 안정 배치 | [patterns/button-icon-layout.md](patterns/button-icon-layout.md) | C-ACTION, C-SVG, F-GEOMETRY, F-TYPE | proposed | U-05 |
| S-PAGE | Root·Detail·Utility·Overlay | shell | 헤더/스크롤/하단바/안전영역 | [shells/page-shells.md](shells/page-shells.md) | F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP | proposed | REF-001/002/004/005, D-011, SRC-TDS-BOTTOM |
| A-ART | 3D·보상·마스코트·광원 | asset | 아트 표시/소품/표현 레벨 | [assets/reward-art.md](assets/reward-art.md) | F-PRINCIPLES, C-SURFACE, P-HIERARCHY | proposed | REF-001–004 |
| NAV-TOP | 확정 상단·Root/Standard | shell | 상단 구조/Back/Close/액션 | [../TOP_NAVIGATION.md](TOP_NAVIGATION.md) | — | approved | D-011; 세부 수치 제외 |
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

- **[사용자 제공 최신 SVG 56개](../assets/icons/soft/v0.14/README.md)**: 기본 Outline 32 · Fill 5 · 추가 19, 24×24 / Stroke 1.8, 기존 37개 원본 경로 덮어쓰기
- [v0.14 구현 인수인계](implementation/v014.md): 라운딩·획·실루엣·Android 적용 주의·검증 구분
- v0.13은 롤백/비교 자료로 유지하며, main의 확정 클랜 나침반 네비는 교체하지 않는다.

## v0.15 최종 정본 · 다른 세션의 첫 진입점

1. **[v0.15 확정 디자인·검증·적용 계약](implementation/v015.md)** — 결정의 범위, 컴포넌트/제스처/컨테이너 사용 규칙, 실제 검증·미검증 분리
2. **[v0.15 디자인 토큰 JSON](implementation/v015-tokens.json)** — 앱 구현 시 숫자·의미색·아이콘 크기·Surface·Sheet 상태를 한 곳에서 읽음
3. **[사용자 제공 SVG 56종 원본](../assets/icons/soft/v0.14/README.md)** + [manifest](../assets/icons/soft/v0.14/manifest.json) — 반드시 이 소스를 인용. v0.13 및 최초 v0.14는 비교 이력
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

[D-013](../docs/DECISIONS.md)의 위임 범위에서 선택·조합 계약을 개편 기준으로 결정했다. 의미의 정본은 각 기존 파일이며 새 계층을 추가하지 않았다.

- 페이지 판단·정보 객체: [P-HIERARCHY](patterns/content-hierarchy.md).
- 텍스트 역할/수치: [C-TEXT](components/text-hierarchy.md) / [F-TYPE](foundations/typography.md).
- 경계·미디어·아트: [C-SURFACE](components/surface-media.md) / [C-IMAGE](components/image-frame.md) / [A-ART](assets/reward-art.md).
- 행동·상태·목록: [C-ACTION](components/action-button.md) / [C-STATUS](components/badge-progress.md) / [C-LIST](components/list-item.md).
- 헤더·이력·safe inset: [NAV-TOP](TOP_NAVIGATION.md) / [S-PAGE](shells/page-shells.md).
- A/B 고정 클래스·지역 상태·이력 인터페이스: [v0.15 웹 어댑터](implementation/v015.md).
- 반례와 인수: [V-CHECKS](VALIDATION.md). 원격 등록·브라우저·Native 검증의 상태는 각각 별도다.

## 2026-11-10 · 표준 스타일 적용 계약 (문서 제안, 구현 미완료)

- **SEED 규격을 실제 코드에 연결할 때:** [표준 스타일 적용 체계](implementation/shared-style-contract.md). 웹의 기존 공통 클래스·CSS 변수와 Android Compose의 Theme·재사용 UI 함수를 대응시키고, 전역 규격/컴포넌트 Variant/페이지 배치를 구분한다.
- 이는 **새 디자인 계층이나 스타일 런타임이 아님.** 승인된 v0.15와 현행 구현의 차이를 확인하고, 기존 클래스/컴포넌트를 재사용하는 운영 계약이다. 스타일시트 코드·Native 이관은 이번 문서 반영에 포함되지 않는다.

## 2026-11-12 · 컴포넌트 시각 품질 교정 경로

- **가로 상품 카드 실패:** P-HIERARCHY S06 → C-IMAGE/C-SURFACE → `styles/seed-standard.css`의 `data-item-layout="horizontal"`. 이미지의 실제 실루엣은 contain 유지하고 미디어 배경을 카드 전체 높이에 맞춘다. 독립 HTML의 임의 CSS를 SEED로 착각하지 않는다.
- **버튼 색/형태:** C-ACTION의 Neutral Outline과 `stroke.control`을 사용. 버튼 자체의 기본 radius는 16, 기간 선택은 C-SEGMENT의 단일 Track/Thumb. 화면에서 불필요한 `이번 주/지난주` 가짜 Action 두 개를 생성하지 않는다.
- **배너:** P-HIERARCHY S09/C-SURFACE에서 이벤트 내부 SVG 이펙트 모사를 금지. 원본 아트 또는 기존 일반 Banner를 사용.
- **헤더:** NAV-TOP의 최신 미세 보정 Root 680 / Standard 650. Pretendard CDN 의존을 실제 font loading 보장으로 오인하지 않는다. 공통 CSS와 `implementation/v015-tokens.json`을 확인한다.

## 2026-11-11 · 상품 컨테이너 시각 표현 정교화

- **상품 목록/카드 작업:** [P-HIERARCHY S06](patterns/content-hierarchy.md)에서 통합 카드·오픈 진열 우선순위를 선택 → [C-SURFACE S06](components/surface-media.md)의 외곽선/배경/버튼 컨테이너 → [C-IMAGE](components/image-frame.md)의 비율·contain·경계 → 기존 C-TEXT/C-CREDIT/C-ACTION 및 P-LAYOUT을 사용한다. **신규 Component/Surface 종류는 없음.**
- 공통 스타일 `seed/styles/seed-standard.css`의 `.seed-item-display`은 조합 배치만 재사용한다. **유동 높이 + 조밀한 Identity/Value + Action 자연 흐름**이 기본이며 Grid/Rail의 무조건적인 stretch·CTA auto push는 비권장. 같은 이미지와 가격을 `web/seed-lab/?panel=components`에서 비교 가능. 버튼이 있는 상품은 통합 카드 우선 권장하며 실제 시각/동작 승인은 별도 검증한다.

## 2026-11-10 · 범용 공간 배치와 페이지 구성 (제안 규격, 웹/Native 미적용)

- **페이지를 새로 설계할 때:** [페이지 구성](patterns/page-composition.md) → [공간 배치](patterns/layout-composition.md) → [정보 위계](patterns/content-hierarchy.md) → 해당 기존 Component/Surface/Shell 순서로 참조. 이는 제작 순서이며 문서의 실제 계층을 추가한 것이 아니다.
- **공간 배치만 수정할 때:** [공간 배치](patterns/layout-composition.md) + [간격](foundations/geometry.md)과 관련 Component만 확인.
- **기존 버튼·배지·이미지 등 단일 부품 수정:** 이전 Component 직접 라우팅을 유지. 페이지 전체 절차를 억지로 적용하지 않는다.
- **소유권:** 기존 D-011/D-012의 Header/SVG/컴포넌트 및 D-013의 '새 Composite/Page Type 계층 없음'을 보존. 이 두 문서는 Pattern의 선택 규격이고 승인되지 않은 새 기능이나 구현 API가 아니다.
- **근거/검증:** 사용자 시안 11541/11537/11535/11539/11454/11432의 공간 관계를 참조했지만 그 안의 게임/경제 기능은 채택하지 않음. 페이지 실제 화면 검수는 별도.

## 2026-11-10 · 계열별 컴포넌트 표현 카탈로그 (보완/실험 구분)

- [계열별 규격·코드 진입점](implementation/component-families.md): C-TEXT의 의미/Scale/가중치, C-ACTION의 허용 6종과 금색 서브브랜드, C-LIST/C-FIELD/C-STATUS 등 기존 계열의 표현 책임을 **신규 계층 없이** 묶어 설명한다.
- 확정 수치와 proposed 후보를 구분하며, 재사용 표현 정본은 `seed/styles/seed-standard.css`. `web/preview/style.css`/클랜 지역 CSS에 동일 속성을 재정의하지 않는다.
- 새로운 Hero 디자인·XSmall 버튼·새 브랜드 색·SVG·아트·Android UI·페이지 템플릿은 도입하지 않는다.

## C-AVATAR 웹 구현 경로 · 2026-11-10

기존 `C-AVATAR` 항목의 단순 문서 정의를 **코드로 연결**했다. `components/avatar.md` (역할·Anatomy·상태·좌표·레이어 우선순위) → `web/preview/seed-mascot-avatar.mjs` (합성·렌더) + `seed/styles/seed-standard.css` (공통 클래스) → `web/seed-lab/?panel=components#mascot-avatar` (크기별 실사용 시연) → `web/preview/seed-mascot-avatar.test.mjs` (합성 테스트). 기존 21개 컴포넌트 집합을 늘리지 않고 C-AVATAR의 누락 구현만 보강한다.

원본 자산은 `assets/art/mascot/`와 `assets/art/cosmetics/`이며 `docs/assets/README.md`가 복사 SHA·레거시 중복 보존을 기록한다. 이 구현은 웹 컴포넌트 수준이고, 실기기 비주얼 확정 및 Android 이식은 별도다.


## 2026-11-11 · U11 통합 QA 인수 경로

- **원 제안 S01~S09 / R01~R04의 인수 상태, 코드·문서·시연·실기기 분리:** [V-CHECKS · U11 QA 장부](VALIDATION.md#2026-11-11--u11-최종-통합-qa-및-인수-판단-s01s09)를 우선 확인. 아래 기존 Component·Pattern의 승인 상태는 그대로 유지한다.
- **구성 의도부터 판단할 때:** [P-PAGE-COMPOSE](patterns/page-composition.md) → [P-LAYOUT](patterns/layout-composition.md) → [P-HIERARCHY](patterns/content-hierarchy.md) → 필요한 C-SURFACE/C-IMAGE/C-AVATAR/C-OVERLAY/C-SVG·C-TEXT. 기존 4 Page Type과 일반 Banner/이벤트 A의 구별을 유지한다.
- **실제 웹 공통 클래스와 확대 문제:** [Shared Style](implementation/shared-style-contract.md), `seed/styles/seed-standard.css`, 루트 font·SVG fallback 및 기존 Button/Nav 예외. **새 런타임/새 token 없음**.
- **현재 통합 인수 보류:** 레거시 `Global Inner` 클랜의 하단 Navigation 숨김(실제 CSS 충돌), NonGlobal 긴 헤더 값 겹침, U04·U05·U07 실화면·실데이터 검증 누락. U10 Pill 대상은 사용자 지시로 **보류**. GitHub Pages 배포/분리된 Chromium fixture를 **Node 원본 테스트나 실제 앱 UI 승인으로 대체하지 않는다**.


## 2026-11-11 · U10 사용자 정정 반영

- U11 인수 시점의 "Pill Navigation 불확실·보류" 기록은 **이후 사용자 정정으로 대체**되었다. 정확한 대상은 **[C-SELECTION Filter Chip](components/selection-controls.md)**의 내부 패딩/시각 밀도와 **[P-PAGE-COMPOSE](patterns/page-composition.md)**의 필터 → 결과 위계다.
- 공통 CSS `seed/styles/seed-standard.css`의 `.seed-filter-chip`이 기존 48px hit target을 유지하면서 보이는 face·글씨 주변 여백만 줄인다. 필터 바로 아래 같은 결과를 소개하는 영문 소제목+큰 제목의 반복은 비권장, 독립 섹션은 예외 가능. `C-SEGMENT`·Tabs·하단 Navigation은 변경하지 않는다.
- 검증 및 이전 기록과의 시간 순서: [V-CHECKS · U10 후속 정정](VALIDATION.md#2026-11-11--u10-후속-정정--filter-chip-및-필터-결과-위계). 실제 서비스 페이지 반영과 전체 원본 Node/브라우저 시각 검증은 별도다.


## 2026-11-11 · Standard 제목 위치 선택 (후속 사용자 합의)

- **Standard 두 배치의 정본:** [NAV-TOP · Standard 제목 정렬](TOP_NAVIGATION.md#2026-11-11--standard-헤더-제목-정렬-선택-사용자-최신-합의)에서 **Center(기본)/Start(선택)** 및 우측 Action·C-CREDIT·비움과 트레일링 상태를 선택. 이는 새로운 Header Type이 아니라 기존 Standard의 정렬 속성이다.
- **Page Type/하단바 계약:** [S-PAGE](shells/page-shells.md)의 `global-inner`·`non-global`은 두 정렬을 모두 사용 가능. `data-title-align="start"`만 Opt-in, Center 기존 정렬/D-015 규격/Root/Sheet/Bottom Navigation은 유지.
- **구현·시연:** `seed/styles/seed-standard.css`의 기존 `.seed-top-nav__inner` 및 선택적 `.head.standard`만 추가 선택자 사용. SEED Lab Shell의 제목 위치와 우측 아이콘/코인/비움 예제로 교차 검증하며 `web/seed-lab/page-type.test.mjs`에서 기본형 보존을 확인. 실제 제품 페이지 적용은 별도.
