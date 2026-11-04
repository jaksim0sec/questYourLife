---
id: C-SURFACE
category: component
status: proposed
read_when: Surface·Card·Banner·Image Frame·Avatar·에셋 그리드
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004]
---

# Surface와 미디어 슬롯

## Purpose / Non-goals
정보의 의미 있는 그룹, 독립 콘텐츠, 강조 아트의 경계를 구분한다. 모든 경계가 Card는 아니다. 정적 Surface에 불필요한 버튼 affordance를 주지 않는다.

## Anatomy / Properties
| 역할 | 슬롯·속성 | 경계 |
|---|---|---|
| Surface | Header? + Body + Footer?, tone=default/muted | 관련 정보 그룹 |
| Content Card | Media? + Title + Detail? + Metadata? + Action? | 독립된 콘텐츠 단위 |
| Banner | Art + 짧은 메시지 + 선택적 Action | 한정된 강조·안내 |
| Image Frame | Image + Fallback, fit=contain/cover, ratio | 다른 조합에서 재사용하는 미디어 재료 |
| Avatar | Identity image + fallback + 선택적 status | 사람·마스코트의 정체성 |

Grid와 horizontal rail은 이 재료들의 레이아웃 조합이며 '보상 카드', '퀘스트 카드'라는 새 기본 API를 강제하지 않는다. 본문 안에 Surface가 꼭 필요한지 먼저 확인한다.

## Tokens / Variants
bg.surface/muted, radius.surface/image, surface.padding, item.gap, type.item/support 정본 참조. 일반 면은 flat. Image Frame 비율은 square(아이템·Avatar) / landscape(콘텐츠 썸네일) 후보로 정하며 구체적 hero 비율은 화면 목적에 맞춰 검토한다. 투명 아이템은 contain, 배경을 담은 썸네일은 cover를 사용한다. Avatar 원형 crop이 모자·귀를 잘라내면 crop 대신 별도 정체성 프레임을 검토한다.

## States / Behavior
Image loading은 원래 프레임 비율 유지, error는 fallback과 필요한 대체 설명 제공. Selected item은 stroke.selected + 체크·착용 중 같은 실제 상태 라벨. owned/equipped/locked/unavailable은 서로 다른 데이터 상태로 구분하고 실제 정보가 있을 때만 노출한다. locked를 단순 회색 이미지로만 표현하지 않는다.

카드가 이동하면 하나의 명확한 target을 제공한다. Bookmark/Reaction 같은 독립 조작은 별도 target으로 둔다. 선택이 서버에 반영 중이면 pending 표시 후 성공/실패를 구분한다.

## Guidelines
미디어의 금속광·파티클을 Container 배경·일반 버튼에 전파하지 않는다. 같은 모음에서 프레임 비율·물체 점유율·텍스트 기준선을 맞춘다. 이미지 위 장문·작은 가격표를 넣지 않는다. 랭킹 배지·강조 리본·선택 경계·글로우를 동시에 중첩하지 않는다.

## Accessibility
장식 이미지는 중복 설명을 생략하고 콘텐츠를 대표하면 대체 설명을 둔다. 상태·가격은 이미지 안의 가짜 글씨가 아닌 실제 텍스트로 전달한다. hover만으로 설명을 열지 않는다. 확대 시 그리드 열 수를 줄이며 텍스트 영역을 확보한다.

## Evidence & Confidence / Impact
둥근 그룹·아트 프레임·수집 슬롯은 관찰 high. contain/crop·실패·보유 상태는 proposed. 보상 이미지 표현은 A-ART, 주목도와 비중은 P-HIERARCHY에 의존한다. 정확한 아이템·열 수·카드 순서는 시드가 소유하지 않는다.

