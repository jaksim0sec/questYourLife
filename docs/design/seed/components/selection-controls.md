---
id: C-SELECTION
category: component
status: proposed
read_when: Tabs·세그먼트·필터칩·선택·체크·스위치
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-001, REF-002, REF-004, REF-005, SRC-SEED-SEGMENT]
---

# 탐색과 선택 컨트롤

## Purpose / Non-goals
비슷한 알약 모양을 동작이 다른 컴포넌트로 구분한다. 이 문서는 관련 선택 family를 함께 라우팅하지만 하나의 만능 pill API로 합치지 않는다. 하단 목적지는 S-PAGE의 미결 사항이다.

## Anatomy / Properties
| 종류 | Anatomy·속성 | 의미·배치 |
|---|---|---|
| Tabs | Tab List + Label + Indicator, selectedKey | 관련 섹션 탐색, 아래 콘텐츠 영역과 직접 연결 |
| Segmented | Track + Segment Label + Thumb, 단일 value | 같은 데이터의 보기·기간 등 2–4개 즉시 전환 |
| Filter Chip | Label + 선택적 Close/Icon, selected/value | 조건 적용, 복수 선택·해제 가능 여부 명시 |
| Checkbox | Indicator + Label + 선택적 설명, checked/mixed | 독립 조건 또는 여러 항목 선택 |
| Radio | Indicator + Label, group value | 상호 배타적 선택 |
| Switch | Track + Thumb + Label, checked | 즉시 적용되는 켜기/끄기 설정 |

## Tokens / Size
공통 type.body/support, radius.control/pill, 공간·터치는 Foundation 정본 사용. Tabs는 배경 상자 대신 선택 밑줄을 기본 제안한다. Segmented는 bg.muted Track과 bg.surface Thumb, fg.primary 라벨을 사용한다. 좌우 padding은 segment.paddingX이며 불필요하게 늘리지 않는다. 내용이 짧으면 hug, fill은 실제 동일 폭 선택 구조일 때만 쓴다. 긴 라벨은 눌러 넣거나 작은 글자로 바꾸지 않는다. Track 높이와 target은 F-GEOMETRY를 따른다.

## States / Behavior
Unselected / Selected에 Enabled·Pressed·Focus·Disabled가 조합될 수 있다. 선택은 형태+상태 의미를 제공하고 색만 바꾸지 않는다. Disabled-selected도 선택 정보를 유지한다. Tabs는 한 영역의 단일 선택과 연결 패널을 노출한다. Focus 이동과 값 확정을 플랫폼 관례대로 분리한다. 필터 적용이 지연되면 pending을 표시하며 성공 전 활성 결과처럼 단정하지 않는다.

Checkbox mixed는 일부 선택에만 사용한다. Switch는 저장 버튼이 별도 필요한 임시 선택에 쓰지 않는다. 데이터 조회 실패가 selected를 임의로 초기화하지 않도록 실패 피드백을 둔다.

## Guidelines
상위 Tabs → 관련 필터 → 콘텐츠를 구분한다. 정렬을 선택 컨트롤 Track에 끼워 넣지 않는다. 여러 Segmented를 겹쳐 어느 영역을 바꾸는지 모호하게 만들지 않는다. Tabs와 Segmented를 모양이 비슷하다는 이유로 교환하지 않는다.

## Accessibility
짧은 라벨, 현재 값, disabled, Checkbox group의 이름을 읽을 수 있어야 한다. 최소 target·포커스 ring 적용. 조작 표시 경계는 stroke.control을 사용한다. 상태 변화 후 포커스 유실·불필요한 스크롤 점프를 막는다.

## Evidence & Confidence / Impact
Tabs·기간 선택은 REF-001/002/004 관찰 high, 동일 화면의 선택 요소 밀집 문제는 REF-005 교정. Checkbox/Radio/Switch 및 keyboard·pending 상태는 이미지 미노출의 proposed 보완이다. 구체적 필터·탭 명칭·개수는 화면 데이터이며 시드가 확정하지 않는다.

