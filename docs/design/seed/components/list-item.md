---
id: C-LIST
category: component
status: proposed
read_when: 목록·랭킹·친구·설정·채팅 진입 행·섹션 헤더
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005]
---

# C-LIST · Flat List와 Surface 연결

## 현행 계약

사람·활동·설정은 **Flat List** 기본. 독립 그룹이 필요할 때만 하나의 Surface로 묶고 **행마다 개별 그림자 금지**. Avatar/Identity/Meta가 한 줄이면 정렬 center, 여러 줄이면 top. 행 대표 타깃 1개와 독립 응원/설정 버튼 1개를 초과할 수 없고 nested 링크 금지.

Divider는 P-HIERARCHY의 3단계 중 의미에 맞게. 마지막 불필요 Divider 금지. 정적인 행에는 chevron, pressed, 버튼처럼 보이는 배경을 쓰지 않는다. 동심 Radius 16/6/10은 **candidate**, P2 비교 전 제품 승인값으로 승격하지 않는다.

**반례:** 사람 목록의 행별 카드 그림자, 짧은 정보에도 타일 Grid 채택, 2줄 메타가 Avatar center로 어색하게 정렬된 사례.


반복하는 사람·활동·설정은 **Flat List**가 기본. 관련 항목을 묶는 독립 Surface가 필요한 때만 C-SURFACE를 호출하고 행별 shadow는 금지한다. O-PERSON-ACTIVITY는 사람/활동 슬롯 의미의 소유자다.

### Selection

| 콘텐츠 | 선택 | 배제 |
|---|---|---|
| 사람·활동, 설정, 반복 메타 | Flat List Item + inset Divider | 3열 이미지 타일 |
| 독립 콘텐츠 결과/홍보 | Media/Object + 필요한 Surface | List로 썸네일 맥락 상실 |
| 1줄 Identity+Meta | row align=center | 높이만 키워 억지 top |
| 2줄 이상 Meta/Description | row align=top | Avatar 강제 center |
| 정적인 기록 | non-interactive row + no chevron | 클릭처럼 보이는 단단한 카드 |

클랜에서 `6명` 같은 동일 데이터 반복은 G2로 제거. 동심 반경 16/6/10은 후보이며, 확정되기 전 지역 border-radius로 찍어내지 않는다.

### Don't

1. 사람 이름·활동·시간을 아이템 3열 타일 문법에 밀어넣어 활동 단서를 잃음.
2. 모든 행을 Solid+shadow 개별 Card로 만들어 정보 묶음이 쪼개짐.
3. 정적 행 끝에 chevron을 넣어 눌릴 것처럼 만들거나, 행 전체 클릭과 응원 버튼을 nested click으로 구현.

### Relations

- **부모:** Plain Section, 필요시 독립 Solid Surface **하나**.
- **자식:** Leading Avatar/Image? + Identity/Meta + trailing Value/Badge? + optional **독립 행동 최대1**. O-PERSON-ACTIVITY는 그 슬롯 구조를 따른다.
- **형제:** Inset Divider/Surface Gap 중 목적에 맞는 단일 경계; Header/Filter는 관련 결과 바로 위.
- **금지:** 카드마다 다른 radius/shadow, 부모 row Link 안에 secondary Button 중첩, Static Row를 Interactive Surface로 위장.

### Verification

- 1줄 center·2줄 top 정렬의 실제 bounding centers/top, 정적 행 cursor/chevron 없음, 행 click 1+독립 action 1 이하.
- 320/390/430×light/dark, 200% 글자·긴 이름·이미지 실패에서 overflow/중첩 없는지 확인.
- 반례 Grid/그림자/과다 suffix가 정상 Flat List와 확실히 구분되는지 병치 검증.

## 이력

- 이전 계약은 [HISTORY](../HISTORY.md)에 원본 보존; 현재 연결 규칙이 우선한다.
