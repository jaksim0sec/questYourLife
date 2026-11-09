# SEED r3 · 2차 P2–P3 구현/독립 자체검수 기록

> [작업 브랜치](https://github.com/jaksim0sec/questYourLife/tree/seed-r3-p2p3): `seed-r3-p2p3`, base [1차 `seed-r3`](https://github.com/jaksim0sec/questYourLife/tree/seed-r3). `new`/`main`/1차 Draft PR #9 변경 없음.
>
> 절차적 예외: 사용자가 "구현 후 시험"을 명시해, G-P1 T2·T3 미통과를 숨기지 않고 분리 브랜치에서 P2/P3를 먼저 구현·자체 시험. **G-P1 자동 통과가 아니다. P4/P5 미착수.**
>
> 최종 실측 CI: [P3 통합 Actions #42538118039](https://github.com/jaksim0sec/questYourLife/actions/runs/42538118039) success. 포함: 21계약 재검사·P2 380행·P2 21쌍×6조건·P3 40장+꾸미기 6장.

## 1. 실행 ID별 현황

| ID | 상태 | 검증 근거 |
|---|---|---|
| P2 Tier1 | **작성·렌더 통과** | Surface/Action/Image/Art/Sheet/Overlay/Gesture/Nav/Bottom/Text 계약과 각각의 반례, [P2 Fixture](../../../web/preview/qa/p2-fixtures.html) |
| P2 Tier2 | **작성·렌더 통과** | Status/List/Tabs/Segment/Selection/Avatar/Credit/Rarity 계약+Don't |
| P2 Tier3 | **작성·렌더 통과** | Field/Radio/SVG/Check 기존 의미 보존 및 계약+Don't |
| P2 계약 21개 | **21/21 형식 통과** | 각 4섹션 유일, Don't 최소 3개, 단일 현행/이력, [통합 static check](../../../web/preview/qa/p3-contract-check.cjs) |
| P2 Fixture | **6/6 Chromium 통과** | [p2 evidence](../evidence/p2/fixture-matrix.json), 320/390/430 × Light/Dark. Normal/Don't 병치 및 computed properties, Tab·Segment·Filter·응원·Radio·Check 상호작용 |
| LEDGER P2 | **380/380 추적 통과** | [P2_TRACE](P2_TRACE.md), 최초 원문 380행의 소유·status 대조; 후보 42/역사 36/제안 302를 자동 승인하지 않음 |
| G-P2 | **산출물 자체 기준 충족** | 문서·21쌍 Fixture·LEDGER 대응. 단 T2/T3 선행 승인 우회에 따른 **절차상 게이트 미승인** |
| P3 클랜 홈 | **구현·8/8 화면 통과** | [clan-home Intent](../pages/clan-home.md); 가입·미가입 각 4폭×2테마, joined 6명 중복 1회, 첫 활동 행 |
| P3 보상 상세 | **구현·8/8 화면 통과** | [clan-reward Intent](../pages/clan-reward.md); O-ITEM 3개 이미지·이름·조건, 구매·장착 금지 |
| P3 내 클랜 | **구현·8/8 화면 통과** | [clan-my Intent](../pages/clan-my.md); 3탭·ARIA·키보드·멤버 목록·초대 |
| P3 이번 주 기록 | **구현·8/8 화면 통과** | [clan-record Intent](../pages/clan-record.md); 620/1000·10px·주간 지표·기여 내역 |
| P3 꾸미기 Fixture | **6/6 맥락 비교 통과** | [dressing Intent](../pages/dressing-fixture.md); 같은 블루 캡 Asset, 예상 vs 보유/착용 가상 상태, disabled Button, 논글로벌 하단바 없음 |
| G-P3 기록 | **7필드/실패·복귀 기록 작성** | Page Record 5개에 G1~G7 개별 판정. **G6 레거시 미통과/G7 독립검수 대기**로 전체 품질 승인 불가 |
| G-P1 T2/T3 | **미완료** | [독립 신규 세션 Issue #8](https://github.com/jaksim0sec/questYourLife/issues/8) 결과 없음 / 전체 의미 규범 충돌 감사 미완료 |
| P4/P5 | **미착수** | 이번 요청 범위 밖. P4 진입 불가 |

## 2. 21개 컴포넌트 개별 시험

> 각각 Selection/Don't/Relations/Verification 문서 + 320/390/430 Light/Dark 정상/반례 렌더를 확인했다. 실제 런타임 검사는 Tab/Segment/Filter/체크/Radio/응원/Progress/target/이미지 등 자동화한 축에 한정된다. Sheet 실제 Drag·모든 부품의 키보드 상태 전체는 P4에서 별도 검증할 것.

| 부품 | 정상 vs Don't 판정 대상 | 자체 검증 |
|---|---|---|
| C-SURFACE | Surface/Flat·Reward Hero | 4계약·3반례·6화면 |
| C-ACTION | Low/Medium vs 동일 목적 Brand 중복 | 4계약·3반례·6화면 |
| C-IMAGE | 아이템 contain vs cover 크롭 | 4계약·3반례·6화면 |
| C-SHEET | 간단 선택 vs 불필요 Snap | 4계약·3반례·6화면 |
| C-OVERLAY | 행 상태 피드백 vs 자동 Modal | 4계약·3반례·6화면 |
| C-GESTURE | 시각적 대체 Close vs Gesture-only; 실제 Drag는 미실행 | 4계약·3반례·6화면 |
| C-ICON | Glyph24/Target48 vs Target24 | 4계약·3반례·6화면 |
| C-BOTTOM | 글로벌이너 클랜 탭 유지 vs 숨김 | 4계약·3반례·6화면 |
| C-TEXT | 기록 Display vs 9.7px 왜소한 수치 | 4계약·3반례·6화면 |
| C-STATUS | 620/1000·62%·ARIA620 vs 회색/ARIA0 | 4계약·3반례·6화면 |
| C-LIST | Flat Person Row vs 회색 타일 | 4계약·3반례·6화면 |
| C-TABS | 48px 연속 Tab·실제 Panel vs 26.4px Gap | 4계약·3반례·6화면 |
| C-SEGMENT | 기간 선택·410점 값 변경 vs 무반응 | 4계약·3반례·6화면 |
| C-SELECTION | 필터 조건에 따른 실제 결과 vs 무반응 | 4계약·3반례·6화면 |
| C-AVATAR | 4명+2 vs +4·Clip | 4계약·3반례·6화면 |
| C-CREDIT | 보유240/예상30 vs null=0 | 4계약·3반례·6화면 |
| C-RARITY | 장착 상태 외부 슬롯 vs 3중 Overlay | 4계약·3반례·6화면 |
| C-FIELD | Label·입력 설명 vs Placeholder-only | 4계약·3반례·6화면 |
| C-RADIO | 실제 동일 name 배타 vs 비연결 상태 | 4계약·3반례·6화면 |
| C-SVG | SVG 원본 vs 대체 glyph | 4계약·3반례·6화면 |
| C-CHECK | native checked vs 시각만 체크 | 4계약·3반례·6화면 |

## 3. 페이지별 G1~G7

G1 목적/대표행동, G2 중복, G3 객체 슬롯, G4 선택·배제, G5 첫 화면 시각, G6 토큰·지역 CSS, G7 5축 검증. **통과는 P3 자체 시험의 범위만 뜻한다.**

| 페이지 | G1 | G2 | G3 | G4 | G5 | G6 | G7 |
|---|---|---|---|---|---|---|---|
| 클랜 홈(가입/미가입) | 통과 | 통과 | 통과 | 통과 | 통과 | 미통과 | 부분 |
| 공동 보상 상세 | 통과 | 통과 | 통과 | 통과 | 통과 | 미통과 | 부분 |
| 내 클랜 | 통과 | 통과 | 통과 | 통과 | 통과 | 미통과 | 부분 |
| 이번 주 기록 | 통과 | 통과 | 통과 | 통과 | 통과 | 미통과 | 부분 |
| 꾸미기 QA Fixture | 통과 | 통과 | 통과 | 통과 | 통과 | 통과(Fixture) | 부분 |

- 홈 joined 390×844 실제: O-PROGRESS 대표값 하단 y≈258, 첫 활동 행 하단 y≈484. 첫 뷰포트에 모두 포함됨.
- 40개 조건에서 실제 계산된 최소 폰트 12px, 최소 클릭 타깃 48px, Progress fill≈0.62·ARIA620, 정상 이미지 및 하단바 클랜 활성 확인.
- 글로벌이너 전부 하단바 보임, 현재 탭 클랜. 직접 딥링크 Back→클랜, 내 클랜에서 보상 진입 후 Back→내 클랜.
- 논글로벌 꾸미기 Fixture는 실제 앱 Route가 아닌 QA HTML; 6조건에서 동일 원본 이미지를 다른 O-ITEM 슬롯으로 비교.

## 4. P0 비교 증거

[실제 before/after 비교표](../evidence/p3/COMPARISON.md) · [P3 40장+Fixture 6장 manifest](../evidence/p3/manifest.json) · [P0 원본 40장](../evidence/baseline/) · [최신 P2 6조건](../evidence/p2/fixture-matrix.json).

실제 P3 가입·미가입·보상·내클랜·기록·꾸미기 화면의 390px Light/Dark PNG를 직접 검토했다. P0 비교는 동일 상태/뷰포트의 **before/after 파일을 연결**했지만, 사용자 제공 image4/7/8/9 원본과 세 열을 나란히 보는 P4 수동 합격 판정은 **아직 하지 않았다**.

## 5. 결함·원인 계층·수정·재시험

| 발견 | 원인 계층 | 수정 | 재검증 |
|---|---|---|---|
| P2 Normal Fixture 12px 텍스트 굵기700 | F-TYPE / 컴포넌트 | 14px 미만 600 상한 적용, Don't 9.7px 사례 유지 | P2 첫 실패 후 6/6 통과 |
| 홈 클랜전/상점 링크 12.32px/700, Guest 카드·기여 Strong 12px/700 | F-TYPE / 부품 | 원본 클랜 CSS 셀렉터의 Small Weight 정정 | P3 40/40 통과 |
| 클랜명 클릭 높이44px | C-ACTION / F-GEOMETRY | 실제 앵커 min-height48 | P3 40/40 통과 |
| 내 클랜 활성 탭 12.96px/820, 클릭 폭46.78px | C-TABS / F-TYPE | 활성 굵기600, 각 탭 min-width48·gap0 | 320/360/390/430 8/8 |
| Back 회귀 시험이 이력 진입과 직접 딥링크를 혼동 | QA 가정 오류 | 두 진입 조건을 분리 테스트; 실제 동작 변경 없이 올바른 기대값 확인 | 두 조건 모두 통과 |
| 첫 화면 중복 Progress Label / 보상·기록 제목 반복 | Page Intent G2→G1 | 중복 카피 제거 후 화면 다시 생성 | P3 40/40 |
| 지역 CSS 동일 selector 4곳 재선언 | 지역 스타일 중복 | 기존 원래 선언에 결합, 후행 override 4곳 삭제 | [통합 CI](https://github.com/jaksim0sec/questYourLife/actions/runs/42538118039) 성공, selector 중복 0 |

## 6. 미해결 문제와 경계

- **G6 미통과:** `web/preview/clan.css`의 기존 색상 리터럴 약63건과 구 Hero 잔여 스타일이 남아 있다. 이번 신규 레이아웃은 공통 변수 기반, 중복 selector 0이지만 **전체 레거시 스타일의 의미별 토큰 소유·무우회 상태는 보장 불가**. 픽셀을 임의 일괄 교체해 통과 처리하지 않는다.
- **G7 부분:** Chromium 40+6 및 자동화한 실제 동작은 통과. 모든 21부품의 시트 드래그/포커스 경계, Android·TalkBack·200% 확대·큰 수치/긴 이름·이미지 실패·빈 피드는 별도 P4 요건으로 미검증.
- **G-P1 T2/T3 미완료:** 독립 신규 세션 재현 시험과 의미 규범 단일성 감사 선행 누락을 취소선 처리하거나 합격 선언하지 않는다.
- **P4 착수 불가.** 이 턴에서 P4·P5 작업/머지/네이티브 구현은 수행하지 않았다.

## 7. 커밋과 원본 보존

- P2 계약: [100182e](https://github.com/jaksim0sec/questYourLife/commit/100182efa027db8027d58cd0b66046cd65ee2980), [d4934b4](https://github.com/jaksim0sec/questYourLife/commit/d4934b40a9eb80cc9fd3c865a50c9a96706d32c0), [a5d6013](https://github.com/jaksim0sec/questYourLife/commit/a5d60136bf5c47e62aa90c89df7b4890b65a47b6), [54351cd](https://github.com/jaksim0sec/questYourLife/commit/54351cde8956f1c17afb916488e448cd9d479a2f).
- P2 fixture·P2 LEDGER: [P2_TRACE](P2_TRACE.md), [P2_STATUS](P2-STATUS.md).
- P3 코드·Page Record: [P3 브랜치](https://github.com/jaksim0sec/questYourLife/tree/seed-r3-p2p3), [최신 본문/아트 증거](../evidence/p3/).
- 별도 브랜치에만 커밋했고 원래 `new`, `main`, 1차 `seed-r3`는 수정하지 않았다. 새로운 앱 Route·데이터 스키마·지급/장착 기능은 추가하지 않았다.
