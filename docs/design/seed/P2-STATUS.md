# SEED r3 · P2 컴포넌트 계약 및 검증
> 작업 브랜치: `seed-r3-p2p3` (1차 `seed-r3`와 분리).
> **절차 변경 기록:** 사용자 2차 후속 지시에 따라 P2/P3를 실제 구현한 다음 시험한다. 기존 G-P1 T2 독립 재현·T3 전체 의미 검수는 **미통과 그대로**이며 이 변경이 자동 통과 선언을 뜻하지 않는다.

## ID별 실제 상태

| ID | 상태 | 판정 근거 |
|---|---|---|
| P2 Tier1 | 작성·Fixture 통과 | C-SURFACE/ACTION/IMAGE/SHEET/OVERLAY/GESTURE/ICON/BOTTOM/TEXT 9종 + P-ACTIONS/A-ART/F-TYPE/F-GEOMETRY 연결 |
| P2 Tier2 | 작성·Fixture 통과 | C-STATUS/LIST/TABS/SEGMENT/SELECTION/AVATAR/CREDIT/RARITY 8종 |
| P2 Tier3 | 작성·Fixture 통과 | C-FIELD/RADIO/SVG/CHECK 4종; 수동 완료/원본 SVG 보존 |
| P2 21개 4계약 | 작성·형식 확인 | 각 컴포넌트에 Selection/Don't/Relations/Verification 정확히 1세트, Don't 사례 각3개, 원문 현행/이력 분리 |
| P2 Fixture | **Chromium 통과** | [Actions #42535261431](https://github.com/jaksim0sec/questYourLife/actions/runs/42535261431), [21쌍 시험장](../../../web/preview/qa/p2-fixtures.html), [QA 코드](../../../web/preview/qa/p2-render.cjs) |
| P2 matrix | **6/6 통과** | [320/390/430 × Light/Dark 계산값](../evidence/p2/fixture-matrix.json), [6개 Screenshot](../evidence/p2/) |
| P2 LEDGER | 전수 소유 연결 | [P2_TRACE](P2_TRACE.md) 380행. 302 proposed/36 history/42 candidate이며 **과거 비교 숫자를 자동 승인하지 않음** |
| G-P2 (자체 산출물 조건) | **통과(검증 범위 한정)** | 21계약·21 정상/반례 실제 렌더·380 P2 추적. 실제 제품 사용처와 P0 전후 비교는 P3 이후 별도 검증 |
| G-P1 선행 조건 | **미통과** | T2 신규 독립 세션 결과 없음, T3 전수 의미 중복 검토 미완료. 사용자 후속 우선순위 변경에 따라 P2/P3 선구현을 분리 브랜치에서 진행함 |

## 실측·반례

- 실제 Progress 10px, fill/track **0.62**(320px) 및 **0.6199**(390/430px), aria now **620**; 의도적 Don't는 값 620 표기인데 채움 0/ARIA 0.
- 정상 Icon 버튼 타깃 ≥48px, 의도적 Don't는 실제 24~26.35px; 정상 Image contain, Don't cover; 정상 Bottom bar 보임, Don't display:none.
- 정상 12px 미만 0개, 14px 미만 font-weight >600 0개. 의도적 Don't text 9.7px/790.
- **실제 동작 검사:** Tabs panel, Segment 값, Filter 결과 집합, Reaction aria, Checkbox checked, Radio exclusivity. 실행 오류 0, 페이지 가로 overflow 0.
- 첫 실행 [Actions #42535111420](https://github.com/jaksim0sec/questYourLife/actions/runs/42535111420) **실패**: 정상 Fixture의 12px 글자들에 700 weight 적용. `fx-sample`의 잘못된 축소 규칙을 제거해 weight600 이하로 교정; 재시험 성공. 검사 조건을 약화하지 않음.

## 범위와 한계

- 이 21쌍은 **컴포넌트 계약 시험장**이다. 실제 앱 화면의 기존 Header/Bottom/클랜 페이지 전체가 합격했다는 증거는 아니다.
- Don't의 심미적 좋고 나쁨은 자동 결정할 수 없다. 실제 오류(누락 ARIA, 작은 글자·타깃, Crop/비클릭, 이벤트 무반응 등)는 렌더 속성과 동작으로 확인했다.
- 기존 UI에 남아 있는 공통 토큰 우회·지역 CSS·실제 사람 활동 정보 계층은 P3 검증을 통과해야 한다.
- 원본 참조 이미지와 3열 Before/After, Android·TalkBack, 실제 A2 생성 아트는 이번 브라우저 시험과 별개이며 미검증.

## 다음 게이트

P3에서 클랜 홈 가입/미가입, 보상 상세, 내 클랜, 주간 기록과 비기능 꾸미기 Fixture에 **각 페이지 Intent/G1~G7**을 기록하고 실제 라우트·디자인·회귀를 확인한다. P4/P5는 진행 금지.
