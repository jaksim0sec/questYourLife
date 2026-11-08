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

## 책임 분리 · v0.2
기본 Surface/Card/Banner 슬롯은 여기서 유지한다. 원형 프레임 여부/마스코트 신원/장비 레이어는 [C-AVATAR](avatar.md), 미디어 fit·loading·fallback·ratio는 [C-IMAGE](image-frame.md)가 **정본**이다. 구형 요약표는 역할 식별을 위한 설명이고 독립 상태·크기를 이곳에서 재정의하지 않는다.

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
bg.surface/muted, radius.surface/image, surface.padding, item.gap, type.item/support 정본 참조. 일반 면은 flat. Image Frame 비율은 square(아이템·Avatar) / landscape(콘텐츠 썸네일) 후보로 정하며 구체적 hero 비율은 화면 목적에 맞춰 검토한다. 투명 아이템은 contain, 배경을 담은 썸네일은 cover를 사용한다. **마스코트의 기본 표시 후보는 투명 캐릭터 자체이며 흰 원형 배경은 필수가 아니다.** 세부표현은 C-AVATAR를 따르고, 모든 실제 소품은 잘리지 않게 contain.

## States / Behavior
Image loading은 원래 프레임 비율 유지, error는 fallback과 필요한 대체 설명 제공. Selected item은 stroke.selected + 체크 및 실제 선택 ARIA로 표시한다. 착용 중은 실제 equipped 사실이 있을 때 별도의 상태 텍스트로 표시하며 선택과 동일시하지 않는다. owned/equipped/locked/unavailable은 서로 다른 데이터 상태로 구분하고 실제 정보가 있을 때만 노출한다. locked를 단순 회색 이미지로만 표현하지 않는다.

카드가 이동하면 하나의 명확한 target을 제공한다. Bookmark/Reaction 같은 독립 조작은 별도 target으로 둔다. 선택이 서버에 반영 중이면 pending 표시 후 성공/실패를 구분한다.

## Guidelines
미디어의 금속광·파티클을 Container 배경·일반 버튼에 전파하지 않는다. 같은 모음에서 프레임 비율·물체 점유율·텍스트 기준선을 맞춘다. 이미지 위 장문·작은 가격표를 넣지 않는다. 랭킹 배지·강조 리본·선택 경계·글로우를 동시에 중첩하지 않는다.

## Accessibility
장식 이미지는 중복 설명을 생략하고 콘텐츠를 대표하면 대체 설명을 둔다. 상태·가격은 이미지 안의 가짜 글씨가 아닌 실제 텍스트로 전달한다. hover만으로 설명을 열지 않는다. 확대 시 그리드 열 수를 줄이며 텍스트 영역을 확보한다.

## Evidence & Confidence / Impact
둥근 그룹·아트 프레임·수집 슬롯은 관찰 high. contain/crop·실패·보유 상태는 proposed. 보상 이미지 표현은 A-ART, 주목도와 비중은 P-HIERARCHY에 의존한다. 정확한 아이템·열 수·카드 순서는 시드가 소유하지 않는다.


## v0.15 Surface 10역할과 중첩 규칙 (D-012)

| Role | 구조적 의미 | 허용 경계 |
|---|---|---|
| Plain | 섹션 간 간격으로만 구분 | 카드/배경 없음 |
| Solid | 독립된 정보 묶음 | 중립 1px 경계·padding 16·radius 16 |
| Outlined | 배경보다 선으로 낮게 구분 | 배경 투명·중립 테두리 |
| Subtle | 힌트/설명 완충 | 연한 의미면, 그림자 금지 |
| Inset | 카드 내부 2차 내용 | padding 12, 내부 중복 border 금지 |
| Raised | 독립적으로 떠 있는 의미 있는 블록 | 그림자 단일 단계, raised→raised 금지 |
| Interactive | 카드 전체가 실제 조작 대상 | 이동은 Link, 실행/선택은 Button; 중첩 독립 조작 금지 |
| Media | 이미지/아트 프레임 | 썸네일 cover, 투명 마스코트·상품 contain |
| Reward | 보상·이벤트에서만 표현력 있는 면 | 한 섹션의 한정 강조, reward→reward 금지 |
| Overlay | Sheet/Dialog의 별도 층 | Focus/스크림/닫기/안전영역 |

**금지:** 무의미한 Solid/Card 반복 중첩, 부모 Raised 내부 Raised, Reward 내부 Reward, 텍스트를 배경 상자로 감싸기만 하는 임시 구현. 각 Surface는 하나의 의미/행동을 가져야 한다. 실제 페이지/데이터 구성을 이 표에서 발명하지 않는다. 자세한 속성 정본은 [v0.15](../implementation/v015.md).

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K04 Surface와 의미 경계

**정본 소유자:** `components/surface-media.md`.

| 판단 | 선택 | 피할 것 |
|---|---|---|
| 여백만으로 관계를 읽을 수 있음 | Plain | 의미 없는 박스 |
| 같은 그룹 안의 약한 경계가 필요 | 필요한 위치의 Divider | 마지막까지 반복하는 선 |
| 독립된 정보 묶음 | 기존 Solid/Outlined/Subtle 등 목적에 맞는 Surface | 중첩 카드·같은 강조 반복 |
| 이미지 자체가 정보의 중심 | Media | 이름·상태를 이미지에 모두 밀어넣기 |
| 실제 보상·이벤트의 한정 강조 | Reward | Reward 안 Reward, 일상 관리 데이터를 광고 카피에 종속 |
| 떠 있는 조작·층의 의미가 실제 존재 | Raised/Overlay | 깊이감만을 위한 그림자 |

Surface 역할은 기본 경계·재질을 정한다. Interactive는 HTML button을 강제하는 역할명이 아니다. 이동이면 링크, 실행/선택이면 버튼, 정적이면 비조작 요소를 사용한다. 부모 안에 독립 행동이 필요하면 정적 그룹에 형제 링크/버튼을 둔다. 전체 링크 안에 다른 버튼을 넣지 않는다.

강도는 정보 관계로 정한다. Plain→Divider→독립 면은 의사결정 순서이며 모든 화면을 세 형태로만 제한하는 새로운 Surface 목록이 아니다. 기존 10개 역할을 보존한다.
