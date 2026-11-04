---
id: C-LIST
category: component
status: proposed
read_when: 목록·랭킹·친구·설정·채팅 진입 행·섹션 헤더
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005]
---

# List Item과 List Header

## Purpose / Non-goals
반복 정보를 동일한 축으로 읽고 필요한 행동으로 연결한다. 퀘스트·친구·랭킹·채팅 등 제품 이름으로 별도 행 스타일을 만들지 않는다. 독립적인 큰 미디어가 핵심이면 C-SURFACE를 사용한다.

## Anatomy / Properties
Prefix(아이콘/Avatar/순위 중 필요한 조합) + Content(Title, Detail, Metadata) + Suffix(수치/상태/보조 Action/Chevron). 클릭 가능한 행과 정적 행을 구분한다. Header는 Section Title + 선택적 count + Low Action이며 행 본문과 다른 역할이다.

variant는 oneLine/twoLine/mediaLeading, interaction은 static/navigate/select/actionable. Prefix가 없으면 Content 시작선만 공통 gutter에 정렬한다. 리스트 안에 버튼이 있으면 부모 행과 별개 조작으로 모델링하고 중첩 클릭 영역을 만들지 않는다.

## Tokens
row.paddingX/Y, size.rowMin, size.avatarRow, item.gap, type.item/body/support, fg.primary/secondary, stroke.subtle을 참조한다. 수치는 type.numberSmall로 우측 기준선 정렬한다. Avatar와 조작 아이콘은 다른 크기 역할이다. 행마다 카드 그림자를 붙이지 않는다. List Header 전체 보기 버튼은 C-ACTION의 Low를 쓴다.

## States / Behavior
Default·Pressed·Focus·Disabled·Selected를 지원할 수 있으며 조작 없는 정적 행에는 pressed를 주지 않는다. 선택 행은 체크/현재 상태를 함께 표시한다. 로딩은 동일한 prefix/title/suffix 슬롯의 Skeleton을 사용한다. 빈 리스트·조회 오류·오프라인은 P-STATES의 그룹 피드백을 사용하며 가짜 멤버·수치를 채우지 않는다.

## Guidelines
채팅 진입 행만 큰 배너·특이한 테두리·큰 아이콘으로 격상하지 않는다. 기능상 중요한 진입점이면 의미 있는 제목/배치로 해결하고 공통 슬롯을 유지한다. 순위·멤버명·기여 수치는 각각 비교 축을 고정한다. 구분선은 Prefix 아래까지 무조건 연장하지 않고 Content 시작선 기준을 통일한다.

## Accessibility
읽기 순서 Prefix 의미 → Title → Detail → 값 → 행동. 장식 순위 아이콘은 중복 읽기에서 제외한다. 행 안 개별 행동은 이름과 target을 가진다. 길어진 이름·200% 확대에서 suffix를 가리면 세로 재배치하며 수치를 잘라내지 않는다.

## Evidence & Confidence
반복 Avatar/이름/값과 설정 행은 REF-001/002/004 관찰 high. REF-005의 클랜 채팅 스타일 이탈을 교정한다. 상태·슬롯 규격은 proposed.

## Dependencies & Impact
Prefix·Suffix가 바뀌는 정도는 variant로 처리한다. 새 조합이 독립 행동·상태를 제공하지 않으면 새 컴포넌트로 승격하지 않는다. 정렬 변경은 모든 목록 및 P-HIERARCHY에 영향이 있다.

