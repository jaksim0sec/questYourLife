# SEED r3 · 2차 P2 컴포넌트 계약 완료 보고

> 실행 범위: 사용자 요청에 따라 **21개 컴포넌트의 P2 계약·Fixture·기본 브라우저 검증까지**. 기존 계획 G-P1 T2(독립 신규 세션 프롬프트 이해 시험)는 사용자가 제외. P3·P4·P5는 실행하지 않았다.
>
> 작업 브랜치 [`seed-r3`](https://github.com/jaksim0sec/questYourLife/tree/seed-r3), 실제 21개 소스 [components/](components/)와 브라우저 [Fixture](../../../web/preview/fixtures/p2.html).

## ID별 상태

| ID | 상태 | 확인 근거 |
|---|---|---|
| P2 Tier1 (9) | **작성·기본검증 통과** | Surface/Action/Image/Icon/Bottom/Sheet/Overlay/Gesture/Text 9개 계약 4절 + 정상·Don't 병치 |
| P2 Tier2 (8) | **작성·기본검증 통과** | Status/List/Selection/Tabs/Segment/Avatar/Credit/Rarity 8개 |
| P2 Tier3 (4) | **작성·기본검증 통과** | Field/Radio/SVG/Checkbox 4개, 기존 후보/의미 보존 |
| P2 계약 21개 | **통과(문서 구조)** | 각 정본에 단일 '현행 계약' / '이력', Selection·Don't(≥2)·Relations·Verification 실제 문장 존재. [Actions 42553215201](https://github.com/jaksim0sec/questYourLife/actions/runs/42553215201) |
| P2 LEDGER | **추적 완료(반영 정합성은 부분검증)** | P2 분류 380/380행 → 26개 정본 연결 [P2_TRACE](P2_TRACE.md). 후보 수치 자동 승인 금지 |
| P2 Renderer | **정상·반례 Fixture 21쌍, 6조건 통과** | [Actions 42553215201](https://github.com/jaksim0sec/questYourLife/actions/runs/42553215201), 전체 페이지 PNG 6장 및 summary.json의 Actions artifact |
| G-P2 | **부분검증 / 최종 승인 보류** | 문서/대표 정상·반례 렌더/일부 인터랙션 확인. 모든 21종 구현 상태를 종단 간 재현하는 P4급 시험과 의미적 전수판정은 미실시 |
| P3 | **미착수** | 클랜 홈/보상 상세/내 클랜/주간 기록/꾸미기 구현 전부 보존 |
| P4 / P5 | **미착수** | 사용자 이번 범위 밖 |

## 21개 계약·경로

| Tier | ID | 정본 | 작성 검사 |
|---|---|---|---|
| 1 | C-SURFACE | [surface-media.md](components/surface-media.md) | Selection · Don't · Relations · Verification |
| 1 | C-ACTION | [action-button.md](components/action-button.md) | Selection · Don't · Relations · Verification |
| 1 | C-IMAGE | [image-frame.md](components/image-frame.md) | Selection · Don't · Relations · Verification |
| 1 | C-ICON | [icon-button.md](components/icon-button.md) | Selection · Don't · Relations · Verification |
| 1 | C-BOTTOM | [bottom-navigation.md](components/bottom-navigation.md) | Selection · Don't · Relations · Verification |
| 1 | C-SHEET | [bottom-sheet.md](components/bottom-sheet.md) | Selection · Don't · Relations · Verification |
| 1 | C-OVERLAY | [overlay-feedback.md](components/overlay-feedback.md) | Selection · Don't · Relations · Verification |
| 1 | C-GESTURE | [gesture.md](components/gesture.md) | Selection · Don't · Relations · Verification |
| 1 | C-TEXT | [text-hierarchy.md](components/text-hierarchy.md) | Selection · Don't · Relations · Verification |
| 2 | C-STATUS | [badge-progress.md](components/badge-progress.md) | Selection · Don't · Relations · Verification |
| 2 | C-LIST | [list-item.md](components/list-item.md) | Selection · Don't · Relations · Verification |
| 2 | C-SELECTION | [selection-controls.md](components/selection-controls.md) | Selection · Don't · Relations · Verification |
| 2 | C-TABS | [tabs.md](components/tabs.md) | Selection · Don't · Relations · Verification |
| 2 | C-SEGMENT | [segmented-control.md](components/segmented-control.md) | Selection · Don't · Relations · Verification |
| 2 | C-AVATAR | [avatar.md](components/avatar.md) | Selection · Don't · Relations · Verification |
| 2 | C-CREDIT | [credit-value.md](components/credit-value.md) | Selection · Don't · Relations · Verification |
| 2 | C-RARITY | [item-rarity-surface.md](components/item-rarity-surface.md) | Selection · Don't · Relations · Verification |
| 3 | C-FIELD | [field.md](components/field.md) | Selection · Don't · Relations · Verification |
| 3 | C-RADIO | [radio-choice.md](components/radio-choice.md) | Selection · Don't · Relations · Verification |
| 3 | C-SVG | [svg-icon.md](components/svg-icon.md) | Selection · Don't · Relations · Verification |
| 3 | C-CHECK | [checkbox.md](components/checkbox.md) | Selection · Don't · Relations · Verification |

## 실제 Chromium 실행 근거

- 21개 Fixture 각각 **Normal / Don't** 시각 조건을 구성. 정상은 역할 선택, 반례는 동일 과업에 대한 잘못된 표현을 병치한다.
- Chromium **320/390/430 × light/dark = 6조건**: 전체 페이지 PNG 6장 생성, 런타임 JS 오류 0, 수평 overflow 0, 검사 대상 Normal 텍스트 12px 미만 0.
- Progress 실제 fill/track **0.61995~0.61999**, `aria-valuenow=620`, 계산 높이 **10px**. P0 클랜 Progress 회귀 검사도 PASS.
- 정상/반례 computed image fit `contain / cover`, 실제 이미지 decode, Action/Icon target 48px 이상 대 반례 24px, Normal 탭 2개와 **각자의 실제 패널**, Filter 결과 2→1, 기간 Segment 변경, Sheet Open/Close, Checkbox 변화, Radio 단일 선택, 키보드 탭/제스처 대체 행동 확인.
- **비교 PNG**: [Actions #42553215201](https://github.com/jaksim0sec/questYourLife/actions/runs/42553215201)의 artifact `seed-r3-p2-six-viewports-browser-screenshots` (6장/summary.json). 해당 결과는 **Fixture**이지 P3 클랜 시안 구현 결과가 아니다.

## 발견·수정한 결함

1. 첫 P2 Action에서 실문서에 선택 기준이 있는데도 특정 문자열이 없다는 이유로 QA가 실패했다. **단어 존재 검사 → 유의미한 Selection 본문 구조 검사**로 고치고 다시 CI 실행했다. 실제 사용·배제 조건은 사람이 별도로 해석할 대상이다.
2. 1차 연결 끊김 중 P2 LEDGER 추적표가 없어서 추적 380행 증거가 누락됐다. [P2_TRACE](P2_TRACE.md)를 실제 원문에서 전수 생성했다.
3. 정상 Tabs Fixture의 두 탭이 같은 DOM 패널을 소유하던 오류를 발견. **탭 2개 ↔ 패널 2개**로 수정하고 `aria-controls`/hidden/키보드 이동까지 자동 검증으로 보강했다.
4. Fixture의 Bottom Navigation 항목이 모두 같은 가짜 링크를 가리키던 오류를 원래 시연의 실제 5개 목적지로 교정. Normal 이미지가 실제로 로딩되는지 `naturalWidth` 검사 추가. 최종 CI 재실행 PASS.

## 보류한 범위·회귀 위험

- **수치 후보**: Root/Standard 제목 크기·Header 높이 58.8/60/56·Bottom 65.2+safe/76·색상 후보·동심 반경 16/6/10 등은 병치 렌더만으로 모두 합의된 값이 아니다. 기존 승인 D-012를 훼손하지 않고 candidate 유지.
- **현행 디자인 소스와 실제 페이지는 별도**: 21개는 문서 계약으로 구현했고 웹 시연에 P2 시험장만 추가했다. 클랜 실제 페이지의 O-ITEM/BOTTOM 변경은 P3가 담당한다.
- **미검증**: 21종 전체의 모바일 실기기·TalkBack·Android, Sheet의 전체 스냅 물리 추적/IME, 전 페이지 200% 텍스트 매트릭스, 배포 UI의 모든 조건. 이는 P4 검증을 대체하지 않는다.
- **전역 영향**: 기존 `web/preview/style.css`·`clan.js`·`clan.css`, Android/Room/Supabase/SVG 원본은 이번 2차 P2에서 변경하지 않았다.
- **T3**: 1차의 전체 규범 의미 중복 감사는 별개로 미완료. 사용자가 제외한 T2 독립 이해 시험을 재도입하지 않는다.

## 검증·작업 경계

- CI: [P2 42553215201](https://github.com/jaksim0sec/questYourLife/actions/runs/42553215201), Head `58625a72ae150f410929f809ce54863111527d67` PASS.
- 병합: Draft [PR #9](https://github.com/jaksim0sec/questYourLife/pull/9) 유지. `new`/`main` 미변경.
- 다음 작업은 **P3가 아니라 사용자가 요청할 때의 후속 범위**다. 본 보고서는 P2만 다룬다.
