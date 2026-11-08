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
| G-P1 T1 | 게이트 대기 | 브라우저/문서 정본의 경로 검사 실행 전 |
| G-P1 T2 | 차단 | **독립적으로 착수하지 않은 새 세션**의 클랜 홈/논글로벌 꾸미기 재현 아직 없음. 대체 시험만으로 통과 불가 |
| G-P1 T3 | 게이트 대기 | 역할/유형/gutter/Progress 기본 두께의 규범 중복 검사 |
| G-P1 T4 | 게이트 대기 | LEDGER P1 각 행 계약 소유 확인 |
| G-P1 | 차단 | **T2 독립 재현 미실시**, T1/T3/T4 실제 검증까지 P2 진입 금지 |

## 실패/복귀

- 이전 작성 중 현행/이력 섹션 중복이라는 초안 검증 실패를 발견해 **커밋 전 수정**, r1 본문을 HISTORY로 이동한 뒤 P1-1 형식을 통과시키는 방식으로 교정함. 초안은 저장소에 반영하지 않았다.
- 현재 코드는 P3 재설계 이전 상태이며 **P1 문서 생성만으로 P3 기능이 변경됐다고 보고하지 않는다**.

## G-P1 1차 실패·복귀 기록

- [Action 42503415758](https://github.com/jaksim0sec/questYourLife/actions/runs/42503415758): T1 10/10, 24 Routes·21 Components, T3 정적 소유 위치 PASS. **T4 테스트 자체가 FAIL**: 검증 스크립트가 `'\\n'` 문자 두 개를 기준으로 분할해 LEDGER 행을 0개 인식했다.
- 원인: QA 스크립트의 이스케이프 버그. 정본이나 585개 원문 인용의 데이터 불량으로 간주하지 않음.
- 조치: 실제 줄바꿈으로 분할하고 `| L` 행 시작으로 인식, 절별 coverage도 단순 키로 점검. Action 재검증 전 T4는 여전히 미통과. T2는 별도 차단.
