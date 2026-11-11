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

## S2-07/08 반복 행 공통 검수
- 클랜 공지·멤버 순위·채팅 진입·친구 활동에 같은 Prefix/Content/Suffix 축을 사용한다. **클랜 채팅만** 굵은 독립 테두리나 다른 반경으로 만들지 않는다.
- '이름 + 내용 + 시간'과 '이름 + 기여 + 순위'는 서로 다른 데이터지만 typography, padding, icon rhythm은 공유한다. 메타데이터가 길어질 때 우측 수치·시간이 밀려 사라지지 않게 줄바꿈/2단 배치.
- 아바타의 투명 표현은 C-AVATAR, 아이템 이미지는 C-IMAGE에서 결정한다. 이미지를 리스트마다 원형 배경 강제하지 않는다.

## Guidelines
채팅 진입 행만 큰 배너·특이한 테두리·큰 아이콘으로 격상하지 않는다. 기능상 중요한 진입점이면 의미 있는 제목/배치로 해결하고 공통 슬롯을 유지한다. 순위·멤버명·기여 수치는 각각 비교 축을 고정한다. 구분선은 Prefix 아래까지 무조건 연장하지 않고 Content 시작선 기준을 통일한다.

## Accessibility
읽기 순서 Prefix 의미 → Title → Detail → 값 → 행동. 장식 순위 아이콘은 중복 읽기에서 제외한다. 행 안 개별 행동은 이름과 target을 가진다. 길어진 이름·200% 확대에서 suffix를 가리면 세로 재배치하며 수치를 잘라내지 않는다.

## Evidence & Confidence
반복 Avatar/이름/값과 설정 행은 REF-001/002/004 관찰 high. REF-005의 클랜 채팅 스타일 이탈을 교정한다. 상태·슬롯 규격은 proposed.

## Dependencies & Impact
Prefix·Suffix가 바뀌는 정도는 variant로 처리한다. 새 조합이 독립 행동·상태를 제공하지 않으면 새 컴포넌트로 승격하지 않는다. 정렬 변경은 모든 목록 및 P-HIERARCHY에 영향이 있다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K09 반복 목록과 탐색 컨트롤

**정본 소유자:** `components/list-item.md`, `components/tabs.md`, 관련 선택 family 정본.

목록은 Prefix / Content / Suffix 축을 공유한다. 짧은 신원·값 한 그룹은 center, 여러 줄 활동·설명은 top으로 정렬한다. 같은 목록에서 콘텐츠 사유 없이 정렬을 바꾸지 않는다. 이름이 길면 Suffix가 정보를 가리기 전에 아래 행으로 재배치한다. 정적 행은 Pressed를 갖지 않는다.

행 전체 이동과 독립 반응 버튼이 동시에 있으면 정적 부모 안에 형제 링크/버튼을 둔다. 행 하나에 4개 이상의 독립 조작을 배치하지 않는다. 부모·자식 반경은 같은 숫자를 기계적으로 반복하지 말고 내부 패딩과 맞는 경계를 선택한다.
