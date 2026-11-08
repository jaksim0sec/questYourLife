---
id: V-CHECKS
category: governance
status: proposed
read_when: 문서 검증·승인 범위·실제 UI 검수·후속 인수인계
depends_on: [R-INPUTS]
references: [U-02, D-002, D-003, D-004, D-005, D-011]
---

# V-CHECKS · 실행과 목적의 독립 판정

## 현행 계약

**5축:** ① 실행(JS pageerror·목적지) ② 규격(12px 최소 글자, 48×48 타깃, x/중심 1px, Progress±1%/ARIA) ③ 객체·조합(O-* 필수 슬롯/클릭) ④ 목적(클랜명·진행 수치·첫 활동 행을 390×844 첫 뷰포트 안에) ⑤ 행동·회귀(탭/패널, Back/Scroll, 유형별 하단바, 기존 깃발·마스코트·장비·SVG·가입·응원·초대).

### 반례 R-01~R-10의 검출 계약

| ID | 반례 | 반드시 잡을 조항 | 판정 질문 |
|---|---|---|---|
| R-01 | image7 Hero 목적 혼합 | pages/page-intent.md + objects/progress.md + PROCESS.md | G1/G5 |
| R-02 | image4 아이템 진열 | objects/item.md | 맥락 슬롯 |
| R-03 | Header/Body 시작선 4.6px 차 | shells/page-shells.md + foundations/geometry.md | 1px 이하 |
| R-04 | 620/1000인데 Progress 미채움 | objects/progress.md | fill/ARIA 단일 소스 |
| R-05 | 동일 보상 상세 링크 두 개 | PROCESS.md + patterns/action-layout.md | G2 |
| R-06 | Tab padding 2.3 vs Gap 26.4 | components/tabs.md + components/selection-controls.md | 타깃/패널 |
| R-07 | 6명 참여 정보 3회 반복 | PROCESS.md | G2 |
| R-08 | 큰 상자 비클릭 작은 링크만 클릭 | objects/README.md + objects/item.md | Click Owner |
| R-09 | global-inner 하단바 숨김 | pages/page-types.md | 하단바 유형 |
| R-10 | 9.4/9.7px 과한 굵기 | foundations/typography.md | 최소12, 굵기 제한 |

### G-P1 필수 4시험

- **T1:** 반례 열 가지 모두 조항을 실제 인용하여 경로 기록. 담당 조항 없으면 계약 보강 후 재시험.
- **T2:** 작업에 참여하지 않은 **새 세션**에 정본만 제공해 '클랜 홈', '논글로벌 꾸미기'를 설계하게 한다. 유형/주연/대표 행동/제외/채택·배제가 실제 조항 인용으로 나오는지 판정. 질문은 `제품 입력 누락 / SEED 공백 / 허용 재량`으로 분류. 별도 세션을 실행하지 못했다면 미검증이며 **P2로 넘어갈 수 없다**.
- **T3:** 역할명→C-TEXT, 유형→page-types, gutter→F-GEOMETRY, Progress 두께→C-STATUS 단일 소유. HISTORY 원문은 규범 아님.
- **T4:** LEDGER의 P1 단계 행 전부 현행 조항·담당 위치에 대응.

### P4 매트릭스와 제한

**5페이지 × 320/360/390/430 × light/dark × 6조건**(기본, 글자 200%, 긴 이름, 12,480/20,000 큰 수치, 이미지 실패, 빈 피드). Playwright 실행/규격/행동, 객체·목적 수동 판정과 이전 P0/개선/원본 3열 비교는 분리 기록한다. **실기기·Android·TalkBack·실제 A2 아트는 미검증**을 유지한다. 실패 시 PROCESS의 G 단계로 복귀하고 재시험 전 통과 처리 금지.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
