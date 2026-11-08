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

### Selection
- 사람·활동·설정은 Flat List 기본. 1줄 Meta=center, 2줄 이상 Meta=top. 반복 행은 Avatar/Identity/Meta/Trailing Action 정렬. 독립 그룹만 Surface 한 겹.
- 대표 행 타깃 최대1 + 보조 버튼 최대1. 정적 행에는 chevron/pressed 금지. 동심 radius16/6/10은 candidate, 관련 경계가 있을 때만.

### Don't
1. 사람 6명에 개별 회색 타일·shadow를 씌워 사람보다 면이 더 돋보임.
2. 2줄 Meta를 무조건 center로 맞춰 Name의 첫 기준선이 불규칙.
3. 정적 정보 행에 chevron, 행 링크 안에 응원 button을 중첩.

### Relations
- **허용:** O-PERSON-ACTIVITY→Avatar+Name+Meta+Time+응원, P-HIERARCHY Inset Divider, C-AVATAR.
- **금지:** 행마다 Raised/Reward, 여러 trailing actions, 중첩 clickable parent.

### Verification
- 정상 center/top 두 경우 vs Don't centered multi-line / 카드 shadow. 320/390/430 light/dark에서 Avatar-Name 정렬, 경계 수, hitbox와 이벤트 중복, 긴 이름·빈 Feed 검사.

## 이력

- 이전 계약은 [HISTORY](../HISTORY.md)에 원본 보존; 현재 연결 규칙이 우선한다.
