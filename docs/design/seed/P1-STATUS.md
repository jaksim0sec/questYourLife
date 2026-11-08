# SEED r3 P1 현황

| ID | 상태 | 검증 근거 |
|---|---|---|
| P1-1 | 작성 | INDEX, VALIDATION, S-PAGE, P-HIERARCHY, F-TYPE, C-TEXT, F-GEOMETRY 모두 현행/이력 분리 + HISTORY 전체 이관 |
| P1-2 | 작성 | `pages/page-types.md` 24개 라우트 유형 명세 / 기존 코드 불일치 P3 이관 |
| P1-3 | 작성 | `pages/page-intent.md` 9필드 + 사람·활동 포함 5목적 |
| P1-4 | 작성 | `objects/README.md`, item/progress/person-activity 3객체, R-02/R-04/사람 타일 반례 |
| P1-5 | 작성 | `patterns/content-hierarchy.md` 10문답 + 3경계 + Surface 0조건 |
| P1-6 | 작성 | NAV/S-PAGE, C-SELECTION, C-LIST, F-GEOMETRY, O-ITEM 소유 연결 |
| P1-7 | 작성 | `PROCESS.md` PCP 7단계·G1~G7·R-01 복귀·수정 위치 5단계 |
| P1-8 | 작성 | INDEX A/B 경로 및 구 입구/이력 HISTORY로 격리 |
| P1-9 | 작성 | C-TEXT 기존 K03 5역할, F-TYPE 12px 및 14px 미만 굵기 상한 |
| P1-10 | 작성 | VALIDATION R-01~R-10 검출 경로 |
| G-P1 T1 | 통과(경로검사) | [Actions 42503557395](https://github.com/jaksim0sec/questYourLife/actions/runs/42503557395) R-01~R-10 10개 대응 경로 확인 |
| G-P1 T2 | 차단 | 독립 신규 세션 재현 증거 없음. [Issue #8](https://github.com/jaksim0sec/questYourLife/issues/8)에 별도 검수 과제 등록. 등록만으로 통과 불가 |
| G-P1 T3 | 부분검증 | [Actions 42503557395](https://github.com/jaksim0sec/questYourLife/actions/runs/42503557395) 단일 소유 경로 선언 확인. 다른 정본에 남은 규범 문장 중복 여부 의미 검토 미완료 |
| G-P1 T4 | 부분검증 | [Actions 42503557395](https://github.com/jaksim0sec/questYourLife/actions/runs/42503557395) P1 178행의 담당 경로 존재 및 47절 커버리지 확인. **수치·반례 내용 반영 판정은 미완료** |
| G-P1 | 차단 | T2 독립 재현 미실시 및 T3 규범 문장·T4 개별 입력의 의미 검토 미완료. P2 진입 금지 |

## 실패/복귀

- 이전 작성 중 현행/이력 섹션 중복이라는 초안 검증 실패를 발견해 **커밋 전 수정**, r1 본문을 HISTORY로 이동한 뒤 P1-1 형식을 통과시키는 방식으로 교정함. 초안은 저장소에 반영하지 않았다.
- 현재 코드는 P3 재설계 이전 상태이며 **P1 문서 생성만으로 P3 기능이 변경됐다고 보고하지 않는다**.

## G-P1 1차 실패·복귀 기록

- [Action 42503415758](https://github.com/jaksim0sec/questYourLife/actions/runs/42503415758): T1 10/10, 24 Routes·21 Components, T3 정적 소유 위치 PASS. **T4 테스트 자체가 FAIL**: 검증 스크립트가 `'\\n'` 문자 두 개를 기준으로 분할해 LEDGER 행을 0개 인식했다.
- 원인: QA 스크립트의 이스케이프 버그. 정본이나 585개 원문 인용의 데이터 불량으로 간주하지 않음.
- 조치: 실제 줄바꿈으로 분할하고 `| L` 행 시작으로 인식, 절별 coverage도 단순 키로 점검. Action 재검증 전 T4는 여전히 미통과. T2는 별도 차단.

## 연결 복구 근거 · 2026-11-09

- 확인한 Head: `0ba610ab923173c75d4f96e3a82221928a7eaf9e`. `new`를 덮어쓰지 않고 `seed-r3` 전용 Draft PR [#9](https://github.com/jaksim0sec/questYourLife/pull/9) 생성.
- P0 Chromium 전체 성공 [42503557364](https://github.com/jaksim0sec/questYourLife/actions/runs/42503557364), P1 T1/소유·경로/커버리지 테스트 성공 [42503557395](https://github.com/jaksim0sec/questYourLife/actions/runs/42503557395).
- 게이트 요구와 이미 통과한 자동검사의 수준을 구분함. 자동 성공 = T2/T3/T4 본질 판정 완료가 아님.

## T3 실제 규범 충돌 발견과 국소 교정 · 2026-11-09

| 확인한 실제 충돌 | 교정 | 영향 |
|---|---|---|
| D-013 r1을 강등했지만 승인 `implementation/v015.md`에 K12 history/focus 고정 인터페이스가 활성 계약으로 남음 | r1 블록 전체 HISTORY로 비파괴 이관, D-012 원 승인 표·수치는 유지 | P5 보류된 상태 API를 현재 의무로 오인하지 않게 함 |
| `TOP_NAVIGATION.md`의 r1 K10 링크는 S-PAGE 개편 뒤 존재하지 않음 | r1 블록 HISTORY로, D-011 Root/Standard 본문 보존 | 깨진 규범 링크 제거 |
| C-BOTTOM이 Root/Detail 여부에 따른 하단바 가시성을 S-PAGE에 위임 | `page-types` 단일 소유, S-PAGE는 safe/공간 계산으로 한정 | 기존 Android 미변경, P3 웹 수정 대기 |
| O-PROGRESS의 초과 `aria-valuenow=current`와 C-STATUS의 `now=min(value,max)` 충돌 | Progress bar/aria-valuenow는 clamp, 실제 초과량은 화면 글자/aria-valuetext | 620/1000 P0 렌더 영향 없음, 1200/1000 실제 경계 검증은 P4 대기 |

T3 자동화는 구체적 충돌의 재발 방지 검사까지 추가했지만, **새 CI 실행/전 범위 의미적 유일성 검토 전 전체 T3 통과를 주장하지 않는다**. T2는 여전히 외부 독립 세션 이슈 [#8](https://github.com/jaksim0sec/questYourLife/issues/8)에 차단.

## 실패/복귀 · P1 CI 규범 구역 형식 검사

- [Actions 42506894025](https://github.com/jaksim0sec/questYourLife/actions/runs/42506894025) 실패: INDEX의 **현행 계약**에 넣은 승인 규격 링크의 표시 문자와 경로에 `v0.`가 있어 P1-1 '구 규칙 표기 0개' 검증 위반.
- 원인: 규칙 자체 충돌이 아니라 표시 문자열/경로의 과거 버전 토큰이 현행 절에 포함된 형식 위반. 구현을 우회하지 않고 승인 규격 진입을 `implementation/v015.md`로 통일. 변경 후 CI 재실행 전 미통과 유지.

## T4 입력 정합성 보정 · 2026-11-09

- `P1_TRACE.md` 신규: P1 `159`행 각각의 처리/반례/담당 규칙 추적. 585개 통합 인용 행과 초기 계획서 L01–L38은 유지.
- `6.5` 관계 소유 개별 분류, `9.3` Tier1/2 행은 P2로 이동, `4.5` 미확정 Title·Tabs 수치도 P2로 이동. 판단 변경 29건의 기존/새 소유 대응은 LEDGER와 trace 양쪽에서 확인 가능.
- 경로·행 수 CI 재검증 전에는 T4 최종 통과로 표시하지 않는다. 실제 구현·실측·T2는 여전히 미완료.

## T3 중복 제거 추가 · C-STATUS

- Progress 두께 6/10/12 규범이 동일 C-STATUS 안에 v0.6 · D-012 · K08 세 곳에 중복되어 있던 것을 확인. 기존 D-012 승인값과 데이터/ARIA 경계는 유지하면서 `## 현행 계약` 1개로 통합하고 중복 원문을 HISTORY에 그대로 보존.
- 기존 D-013 r1이 proposed로 강등된 이상 K08 전체를 현재 적용 규범으로 놓지 않는다. 다른 객체(O-PROGRESS)는 트랙 두께를 소유하지 않고 C-STATUS를 참조하도록 유지.
- T3 CI 검증 성공 전에는 규범 유일성 완료로 기록하지 않는다.
