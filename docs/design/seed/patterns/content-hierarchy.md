---
id: P-HIERARCHY
category: pattern
status: proposed
read_when: 아트·텍스트 비중·섹션 밀도·필터/정렬·프로필 조합
depends_on: [C-LIST, C-SURFACE, C-STATUS, C-SELECTION, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004, REF-005]
---

# P-HIERARCHY · 선택·경계 계약

## 현행 계약

콘텐츠 섹션의 5개 목적은 [P-INTENT](../pages/page-intent.md), 정보 객체의 5개 슬롯은 [O-README](../objects/README.md)가 소유한다. **진열/꾸미기를 이미지 Grid와 짧은 라벨만으로 처방한 이전 행은 삭제**했다. State·Value·Action 누락을 권고하지 않는다.

| 질문 | 우선 후보 | 배제 후보 | 반례 |
|---|---|---|---|
| Q1 간격만으로 구별 가능한가 | Flat/Plain Section | 의미 없는 Card | 제목 하나를 임시 Surface로 격리 |
| Q2 그룹 내 약한 선이 필요한가 | Inset Divider | 무조건 Full Divider | 마지막 행까지 선 반복 |
| Q3 독립 정보 묶음인가 | Solid Surface | 임시 그림자 박스 | 관련 목록을 행별 카드화 |
| Q4 이미지가 주연인가 | Media | 관리 수치를 큰 배너에 종속 | image7 Hero가 Progress 압도 |
| Q5 특별 보상 표현이 필요한가 | Reward + 적합한 A-ART | Reward 안 Reward | 그라데이션만으로 A2 대체 |
| Q6 반복되는 사람/활동/설정인가 | Flat List + O-PERSON-ACTIVITY | 아이템 타일 Grid | 사람을 상품 3열 진열 |
| Q7 짧은 실제 상태인가 | Badge/Meta | 장식 Badge | 의미 없는 과한 배지 |
| Q8 상위 콘텐츠 범주 전환인가 | Tabs | Segment | 멤버/기록/소식을 기간처럼 취급 |
| Q9 같은 데이터의 기간·표현 변경인가 | Segment | 라우트 이동 | 화면을 바꾸는 가짜 보기 전환 |
| Q10 결과의 조건 변경인가 | Filter Bar/Chip | 떨어진 Filter | 눌러도 목록 불변 |

**경계 3강도:** Inset Divider(같은 그룹 약한 분리), Full Divider(그룹/행동 중간 분리), Surface Gap(의미가 다른 묶음의 강한 분리). **아무 Surface도 사용하지 않을 조건**은 연속 정보 흐름/Flat List에서 배경·그림자가 정보의 의미를 돕지 않을 때다.

30%·50%는 원문 과잉 사례의 설명이지 전역 최대 비중이 아니다. 보조 아트가 주연을 가릴 때 높이부터 줄이지 말고 [PROCESS](../PROCESS.md)의 G5→G1로 복귀한다. 사람·활동, 진행, 홍보, 탐색, 획득은 P-INTENT의 콘텐츠 목적별 표현을 참조한다.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
