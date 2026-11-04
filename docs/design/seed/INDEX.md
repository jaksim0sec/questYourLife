---
id: SEED-INDEX
category: router
status: proposed
read_when: 디자인 시드 진입·필요 문서 선택
depends_on: []
references: [U-01, U-02, D-011]
---

# 갓생패치 · 디자인 시드 INDEX

v0.1 · 2026-11-05 Asia/Seoul · **컨셉을 교정해 추출한 제안 사양**. 문서화는 완료 대상이며 토큰의 최종 승인·실제 UI 검증·앱 구현 완료를 뜻하지 않는다. 정책은 [DECISIONS](../../DECISIONS.md) / [OPEN](../../OPEN.md), 추출 방식은 [AUTHORING GUIDE](../SEED_AUTHORING_GUIDE.md) 정본을 따른다.

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
| C-ACTION | 버튼·CTA·텍스트/아이콘 버튼 | component | 핵심/보조/위험/로딩 행동 | [components/action-button.md](components/action-button.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-005/006/007, SRC-SEED-BUTTON, SRC-TDS-BUTTON |
| C-SELECTION | Tabs·세그먼트·칩·체크 | component | 탐색/기간/필터/선택/설정 | [components/selection-controls.md](components/selection-controls.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-001/002/004/005, SRC-SEED-SEGMENT |
| C-LIST | List Item·Header·순위·행 | component | 반복 행/멤버/채팅/설정 | [components/list-item.md](components/list-item.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-001/002/004/005 |
| C-SURFACE | Card·Surface·Avatar·프레임 | component | 그룹/미디어/배너/진열 | [components/surface-media.md](components/surface-media.md) | F-COLOR, F-TYPE, F-GEOMETRY | proposed | REF-001–004 |
| C-STATUS | Badge·Tag·Progress·값 | component | 진행/상태/비교 수치 | [components/badge-progress.md](components/badge-progress.md) | F-COLOR, F-TYPE, F-GEOMETRY | proposed | REF-001–004 |
| C-FIELD | Field·Input·검색·Textarea | component | 입력/검증/오류/키보드 | [components/field.md](components/field.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION | proposed | REF-005, SRC-TDS-SYSTEM |
| C-OVERLAY | Sheet·Dialog·Snackbar·Skeleton | component | 모달/피드백/빈 상태/로딩 | [components/overlay-feedback.md](components/overlay-feedback.md) | F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION, C-ACTION | proposed | REF-003, SRC-TDS-SYSTEM |
| P-ACTIONS | 버튼 그룹·수락/거절·Bottom CTA | pattern | 폭 비중/조합/하단 행동 | [patterns/action-layout.md](patterns/action-layout.md) | C-ACTION, C-SELECTION, F-GEOMETRY | proposed | REF-005/006/007, SRC-TDS-BOTTOM |
| P-HIERARCHY | 콘텐츠 조합·비중·밀도 | pattern | 프로필/섹션/필터와 정렬 | [patterns/content-hierarchy.md](patterns/content-hierarchy.md) | C-LIST, C-SURFACE, C-STATUS, C-SELECTION, F-TYPE, F-GEOMETRY | proposed | REF-001–005 |
| P-STATES | 빈·오류·오프라인·대기·복구 | pattern | 상태별 표현/실제 결과 | [patterns/state-feedback.md](patterns/state-feedback.md) | C-OVERLAY, C-STATUS, C-ACTION, C-FIELD | proposed | REF-001/003, U-02 |
| S-PAGE | Root·Detail·Utility·Overlay | shell | 헤더/스크롤/하단바/안전영역 | [shells/page-shells.md](shells/page-shells.md) | F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP | proposed | REF-001/002/004/005, D-011, SRC-TDS-BOTTOM |
| A-ART | 3D·보상·마스코트·광원 | asset | 아트 표시/소품/표현 레벨 | [assets/reward-art.md](assets/reward-art.md) | F-PRINCIPLES, C-SURFACE, P-HIERARCHY | proposed | REF-001–004 |
| NAV-TOP | 확정 상단·Root/Standard | shell | 상단 구조/Back/Close/액션 | [../TOP_NAVIGATION.md](../TOP_NAVIGATION.md) | — | approved | D-011; 세부 수치 제외 |
| R-INPUTS | 원본·평가·나쁜 예·출처 | reference | 근거/채택/제외/확신도 조회 | [references/inputs.md](references/inputs.md) | — | needs-validation | REF-001–007, U-01–03, SRC-* |
| V-CHECKS | 검증·제안/미결·변경 영향 | governance | 완료 판단/시각 검수/승인 전 | [VALIDATION.md](VALIDATION.md) | R-INPUTS | needs-validation | U-02, D-002–005/011 |

## 최소 경로 예시

| 작업 | 읽기 시작점 |
|---|---|
| 버튼 크기·색만 교정 | C-ACTION → 관련 Foundation |
| 수락/거절의 비례·줄바꿈 | P-ACTIONS → C-ACTION/관련 Foundation |
| 새 입력 페이지 | S-PAGE + C-FIELD + C-ACTION → 직접 의존성 |
| 프로필·기록의 과장 교정 | P-HIERARCHY + C-LIST/C-SURFACE |
| 보상 아이템 진열 | C-SURFACE + A-ART, 실제 상태면 C-STATUS |
| 오프라인/실패 표현 | P-STATES → 관련 피드백 재료 |

파일을 모두 만드는 것이 목적이 아니다. 새 변형은 기존 슬롯·토큰·조합으로 해결되는지 먼저 확인한다. 페이지마다 제품 데이터 이름을 붙인 컴포넌트를 늘리지 않는다.

