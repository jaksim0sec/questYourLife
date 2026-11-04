# Design Seed Reconciliation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox syntax for tracking.
> 상태: proposed · 승인 전 실행 금지. 이 파일의 기록 자체는 코드를 수정하거나 디자인 승인을 의미하지 않는다.

**Goal:** 기존 갓생패치 시드의 오류·과잉·부조화를 개선하고, 실제로 부족한 컴포넌트·패턴만 확장하며, 8개 컨셉/반례로 웹에서 개선 전후를 검증해 새 페이지가 임의 스타일을 발명하지 않게 한다.

**Architecture:** INDEX 기반 선택적 독해와 기존 F/C/P/S/A의 **정본 소유권**을 유지하되, 그 안의 미승인 내용은 유지 대상으로 간주하지 않는다. **모든 요소를 Keep / Refine / Merge·Remove / Split / Add로 감사한 뒤** 변경한다. 컴포넌트의 anatomy·state가 독립적일 때만 문서를 분리하고, 배치는 Pattern, 페이지 뼈대는 Shell로 처리한다. 실험 수치는 proposed로 두고 컴포넌트 갤러리의 실제 렌더 검증 후 승인한다.

**Tech Stack:** GitHub docs/new(문서), Markdown/디자인 토큰, 임시 모바일 웹 HTML/CSS/JavaScript 플레이그라운드(실험 아티팩트, new에 앱 코드 추가 금지). 실제 Compose/Android 이식은 이후 별도 계획.

**Spec:** docs/superpowers/specs/2026-11-05-design-seed-reconciliation-design.md

## Global Constraints

- docs/AGENTS.md, docs/DECISIONS.md, docs/OPEN.md, docs/design/SEED_AUTHORING_GUIDE.md 먼저 읽기.
- D-011 상단 헤더 정본 보존, 하단 탭/색·치수/테마 미승인 유지.
- Android/DB/기존 에셋/main 변경·병합 금지. new는 docs-only 유지.
- 3D·게임 특수 효과는 에셋/이벤트 레이어로 격리. 별도 화폐·XP·보스·유료 패스 등 제품 기능을 시안에서 새로 만들지 않음.
- 실제 렌더 미검증 값은 무조건 proposed. 이미지 출처와 사용자 의도는 별도 기록.
- 같은 제품 역할에는 하나의 정본. 다른 이미지에서 등장했다는 이유만으로 새 컴포넌트 생성하지 않음.
- 기존 시드 규칙은 보호 대상이 아니라 **검토·개선 대상**. 최소한 현재 규칙의 시각적 실패를 기록하고, 추가 전에 개선·삭제·통합 가능성을 점검.
- 문서량 증가, 단순 렌더 성공, 토큰 숫자 확정을 개선 완료의 증거로 삼지 않는다. 같은 fixture를 통한 기존안 vs 변경안 비교를 보존한다.

## Review Focus — 실패 가능성이 높은 다섯 경계

1. 특정 두 버튼의 **50:50 원 지시**가 전역적으로 금지/강제된 문장으로 바뀌는 문제. Task 1의 양방향 반례 검사로 방지.
2. 긴 한글/200% 확대에서 2버튼이 겹치거나 글자를 줄이는 문제. Task 4·7의 가로/세로 전환 fixture로 확인.
3. Root fixed CTA + Bottom Nav + IME의 중첩/이중 inset. Task 5·7에서 viewport/스크롤 검증.
4. 동일 의미의 SVG가 헤더·버튼·하단에서 서로 다른 stroke/크기/광학 무게를 갖는 문제. Task 3·7 비교판.
5. 아바타 투명 에셋의 모자·귀 crop 또는 큰 보조 인사말이 주 콘텐츠를 압도하는 문제. Task 2·6·7의 profile fixture.

---

### Task 1: 기존 품질 감사와 원본 교정 의미 고정

**Files:** Modify docs/design/seed/references/inputs.md, docs/design/seed/patterns/action-layout.md, docs/design/seed/components/action-button.md. Preserve docs/DECISIONS.md and original source docs.

**Interfaces:** S2-01~S2-08 references; REF-005의 원문과 사용자의 후속 직접 교정.

- [ ] 1.1 원본/대화 평가/AI 생성상의 오류를 구분한 참조표를 작성하고, 실제 존재하지 않는 이미지 경로를 만들지 않는다.
- [ ] 1.1a 기존 F/C/P/S/A 규칙마다 Keep / Refine / Merge·Remove / Split / Add 분류와 사유를 기록하고, 신규 추가 전 개선 가능한 기존 규칙을 우선 지목한다.
- [ ] 1.2 반례 01의 원 지시는 특정 하단 버튼의 균등 너비와 정렬이며 '50:50 일반 금지'가 아님을 수정한다.
- [ ] 1.3 별도 비대칭 행동 쌍의 경우에만 내용/중요도에 따른 비대칭·세로 전환을 허용해 반례를 함께 기록한다.
- [ ] 1.4 검증: 두 서로 다른 사례(교정 대상 equal / 일반 non-equal)가 각기 올바른 규칙으로 해석되며, 사용자 의미가 바뀌지 않았는지 문서 diff 확인.
- [ ] 1.5 변경된 파일의 참조 ID와 INDEX 연결을 검수한 뒤 문서 커밋.

### Task 2: Foundation의 소유권과 표현 계층 정리

**Files:** Modify docs/design/seed/foundations/principles.md, typography.md, geometry.md, color-surfaces.md, icons-motion.md; docs/design/seed/INDEX.md only if routes change.

**Interfaces:** 기존 F-* 토큰 이름을 우선 재사용; 변경은 제안 / 승인 분리.

- [ ] 2.1 기존 5개 Foundation을 색의 의미·텍스트 위계·간격/반경·아이콘/모션으로 점검한다. 중복·충돌뿐 아니라 **과장된 타이포, 좁은 버튼 내부 여백, 일관성 없는 radius/높이, 색상 강약, 잘못된 광학 비례**까지 개선 대상으로 삼는다.
- [ ] 2.2 본질적 제목·중간 섹션·보조 인사말·설명·숫자를 role로 분리하고 '크기는 큰데 중요하지 않은' 반례를 금지한다.
- [ ] 2.3 버튼 내부 label/icon/padding/radius 사이 관계를 size별 하나의 정본에 매핑. 스크린샷에서 절대값 추출 금지.
- [ ] 2.4 검증: 기존값/후보값을 동일한 콘텐츠·상태로 비교하고 나쁜 예 05/06/08/10의 비례 문제가 개선되었는지 기록한다. F-*에만 정량 토큰이 있고 C/P/S에 임의 새 HEX·radius가 없는지 검색. 토큰 승인 status 변경 없음.
- [ ] 2.5 문서 커밋.

### Task 3: 기존 컴포넌트 품질 개선과 필요한 부분만 분리·확장

**Files:** Modify docs/design/seed/INDEX.md, existing docs/design/seed/components/*.md. Create focused component files only when anatomy/state/behavior independence is demonstrated.

**Interfaces:** 컴포넌트 ID, category, read_when, depends_on, status를 기존 INDEX 형식으로 유지.

- [ ] 3.1 SEED Component 카탈로그 대조표를 INDEX 근처에 정리(covered / bundled-underspecified / missing-required / later / irrelevant).
- [ ] 3.2 기존 Action Button/Icon Button, Tabs/Segmented/Chip/Checkbox, List Item, Avatar, Image Frame, Badge/Progress, Field, Sheet/Dialog의 목적·슬롯·상태뿐 아니라 **미관·정보 비례·아이콘 규칙·공통 size의 실제 일치 여부**를 개선한다. 현재 사용되지 않는 후보는 신규 파일로 구현하지 않는다.
- [ ] 3.3 불필요한 variant·중복 규칙은 통합하거나 제거한다. 다른 상태 계약이 있는 것만 독립 문서로 분리하고, 확인된 공백만 추가한다. 동일 API의 색상 variant는 파일을 늘리지 않는다.
- [ ] 3.4 Top Navigation은 D-011 참조만, Bottom Navigation은 후보(approved 아님)로 단독 명세 여부 검토.
- [ ] 3.5 검증: 이전 시드와 개선 시드를 동일한 '친구 리스트'/'퀘스트 보상' fixture에 적용해 계층·밀도·버튼·아이콘·상태의 오류가 줄었는지 대조한다. 동시에 INDEX에서 필요한 재료만 선택할 수 있는지 확인.
- [ ] 3.6 경로/의존성 없는 링크·순환·중복 정본을 검사한 뒤 문서 커밋.

### Task 4: 행동/정보/상태 Pattern을 실제 선택 규칙으로 심화

**Files:** Modify docs/design/seed/patterns/action-layout.md, content-hierarchy.md, state-feedback.md; 필요한 경우 loading 독립 패턴 문서. INDEX 라우팅 갱신.

**Interfaces:** Pattern 최소 필드: Problem, Applicability, Components, Decision Rules, Transitions, Exceptions, Do/Don't, Verification.

- [ ] 4.1 수락/넘기기, 동일한 두 행동, 비대칭 두 행동, 목록 suffix, 단일 CTA의 선택 트리 작성.
- [ ] 4.2 기존 피드/랭킹/프로필/퀘스트의 **박스 남발·중복 타이틀·쓸데없는 구분선·탭/필터 밀집**을 줄이기 위한 배치 패턴을 개선한다. 특정 콘텐츠/탭 개수 고정하지 않음.
- [ ] 4.3 loading/empty/error/offline/retry/undo를 상태 목록에서 실제 상태 전환/예외 기준으로 확장.
- [ ] 4.4 검증: 320 폭, 긴 한글, equal/weighted/stacked, pending→success/error의 구분이 텍스트 규칙만으로 결정 가능한가.
- [ ] 4.5 문서 커밋.

### Task 5: Shell·Navigation·Safe Area의 책임 분리

**Files:** Modify docs/design/seed/shells/page-shells.md; consult docs/design/TOP_NAVIGATION.md (read-only); create bottom-navigation component spec only if independent contract needed; INDEX 갱신.

**Interfaces:** Root/Detail/Utility/Immersive/Overlay / header type / bottom visibility / fixedCTA / safeInsets.

- [ ] 5.1 승인된 Root/Standard 헤더 유형 유지. 시안의 왼쪽 상세 제목·다크 헤더는 미결 후보로만 기록.
- [ ] 5.2 Bottom Navigation, fixed CTA, 키보드 inset 소유권과 공존 조건을 한 Shell에 정의.
- [ ] 5.3 각 Shell의 헤더/하단 노출 조건과 비표시 이유를 구성하되 실제 탭 목적지·개수 확정 금지.
- [ ] 5.4 검증: 가상 3 케이스(root+bar, detail+CTA, approved-exception bar+CTA) 및 키보드 시 마지막 항목 가시성 검토.
- [ ] 5.5 문서 커밋.

### Task 6: 에셋·이벤트 표현의 격리

**Files:** Modify docs/design/seed/assets/reward-art.md, docs/design/seed/components/surface-media.md (또는 이후 분리된 Avatar / Image Frame 문서), docs/design/seed/patterns/content-hierarchy.md.

**Interfaces:** Avatar mode=transparent/default-crop-optional, media fit=contain/cover, asset emphasis=standard/featured/celebration (실제 명명은 검증 후).

- [ ] 6.1 마스코트는 투명한 캐릭터 자체 표시를 기본 후보로, 흰 원형 디스크·원형 crop 강제 규칙 제거.
- [ ] 6.2 소품 광원·시점·물체 점유율·기본 타일 및 큰 이벤트 배너의 표현 강도를 분리.
- [ ] 6.3 강렬한 6번 이벤트가 버튼·탭·일반 리스트의 토큰을 오염시키지 않도록 Non-goals 작성.
- [ ] 6.4 검증: 모자/귀 잘림 없음, 같은 에셋이 리스트/프로필/아이템 타일에서 읽히고 UI text를 침범하지 않음.
- [ ] 6.5 문서 커밋.

### Task 7: 실제 웹 컴포넌트 갤러리로 시각화·비교

**Files:** 실행 시 사용자에게 전달하는 별도 HTML/CSS/JS 실험 아티팩트. 문서 전용 new 브랜치에는 코드 커밋하지 않음. 참조 검증 결과는 docs/design/seed/VALIDATION.md.

**Interfaces:** Foundations 토큰을 입력으로 Variant / Size / State / Theme / Shell 예제 재렌더. 허위 실제 서비스 기능 금지.

- [ ] 7.1 가장 작은 실험 구현: **현재 시드 Baseline과 개선 후보를 동일 조건에서 나란히 비교**하는 Foundation tokens + Button + Icon + List + Tabs/Segmented + Avatar/Image + Shell 갤러리를 만든다.
- [ ] 7.2 사용자가 색/라운딩/크기/강조를 조절할 수 있고 입력한 토큰을 JSON으로 복사·내보내고 다시 불러오는 기능을 검증.
- [ ] 7.3 320/360/390/430 폭, 긴 한글, 동적 글자 200%, Light/Dark, selected/pressed/loading/disabled/empty/error 비교.
- [ ] 7.4 50:50 교정 사례와 별도 비대칭 사례를 동시에 비교. fixed CTA + bottom + IME 중첩 여부 확인.
- [ ] 7.5 각 기존 컴포넌트의 문제→기존안→개선안→비교 결과→미해결을 VALIDATION에 기록. 불필요한 토큰/variant 수 감소도 확인. **실행 성공**과 **사용자 시각 합의** 별도.
- [ ] 7.6 실제 승인 없는 토큰은 proposed 유지.

### Task 8: 문서 정합성·승인 라우팅

**Files:** Modify docs/design/seed/INDEX.md, docs/design/seed/VALIDATION.md, docs/OPEN.md (미결 갱신만). docs/DECISIONS.md는 사용자 명시 승인 시에만 수정.

- [ ] 8.1 INDEX 경로/ID/depends_on/read_when/status 검증, 중복 정본·끊긴 상대 링크·없는 토큰 확인.
- [ ] 8.2 10개 반례 각각에 대해 원인→기존 실패→교정 규칙→실제 전후 preview fixture→결과 매핑. 확장으로 새 불일치가 생기면 완료 불가.
- [ ] 8.3 미검증과 차단 항목을 명시. 8장 중 광고는 UI 토큰 원본이 아님을 보존.
- [ ] 8.4 사용자 승인된 디자인 결정만 DECISIONS에 승격. 토큰 변화로 영향받는 관련 문서와 함께 커밋.
- [ ] 8.5 최종 검증: main/Android/DB/에셋에 변경 0개, 문서외 new 파일 0개, 모든 요구 ID에 결과·한계 기록.

## Execution Handoff

**권장:** 기존 시드 감사·오류 교정(1~2) → 기존 컴포넌트·패턴의 시각 품질 개선 및 필요한 부분만 확장(3~6) → **기존/개선 웹 플레이그라운드 비교**(7) → 명시된 결정만 승격(8). 화면 디자인 전체를 한꺼번에 생성하지 않는다.

계획 파일이 생성됐다는 사실은 실제 UI를 만들었다거나 시드 승인·테스트를 통과했다는 뜻이 아니다. 구현은 별도 승인 또는 명시적 진행 지시 이후 수행한다.

## Execution record · 2026-11-05 (진행 결과, 신규 승인 아님)

| Task | 상태 | 증거 | 미완료/제약 |
|---|---|---|---|
| 1 기준/원본 교정 | 문서 반영 | S2-01~08 SHA/판정 + 50:50 교정 + AUDIT | 전체 원본 GitHub 이미지 업로드 미실시 |
| 2 Foundation | 제안값 반영 | F-COLOR/TYPE/GEOMETRY/ICON-MOTION 개선 | 실제 사용자 시각 승인 전 |
| 3 Component | 확장+개선 반영 | C-ICON/TABS/SEGMENT/CHECK/AVATAR/IMAGE/BOTTOM 독립화, C-family 참조 정리 | 전체 SEED 53종 복제는 의도적으로 제외 |
| 4 Pattern | 명세 보완 | P-ACTIONS/HIERARCHY/STATES 상태·결정 규칙 | 실제 E2E 상태 전환·오프라인 검증 필요 |
| 5 Shell | 명세 보완 | S-PAGE 의존/하단 CTA+nav 책임 | 실제 Android IME/OS safe area 미검증 |
| 6 Asset | 명세 보완 | A-ART + C-AVATAR/IMAGE 책임 분리 | 실제 투명 에셋/착용 좌표 미검증 |
| 7 Web gallery | 샌드박스 단일 HTML 생성·일부 QA 통과 | 8장 로딩 + 4폭·2배율 + JSON/테마/탭 자동 검사 | 모바일 실기기 시각 승인 전, new에 HTML 코드 커밋하지 않음 |
| 8 정합성/승인 | 일부 정적 검증 | INDEX 등록/경로/의존성 1차 오류 0, VALIDATION에 실제 범위 기록 | 사용자 승인 전 DECISIONS·토큰 approved 갱신 금지 |

**Rulings:** 사용자 요청은 '확장만'이 아니라 개선 우선. UI 자료에 등장한 새로운 XP/유료 패스/레이드 등 기능은 제품 명세가 아니므로 추가하지 않음. 기존 문서와 레퍼런스 보존을 우선해 정본 링크·역할을 조정, 앱 코드/DB는 변경하지 않음.
