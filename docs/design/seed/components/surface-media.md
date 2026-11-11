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

## S06 · 단일 아이템 부모의 Surface 경계 (제안 보완)

**아이템 표현 B는 '이미지 카드 + 정보 카드'가 아니다.** 부모 하나가 **같은 아이템의 미디어와 정보**를 묶고, 그 내부에 이미지 프레임과 자연 높이의 정보 영역이 놓인다. [P-HIERARCHY S06](../patterns/content-hierarchy.md)의 **A(이미지 중심) / B(이미지+정보 패널)** 선택을 참조한다. 이 문서는 단지 그 부모의 표면 표현을 담당한다.

```text
하나의 의미상 아이템 부모 (기존 Surface 또는 Plain)
├─ C-IMAGE Frame + 선택적 Badge
└─ 정보 영역 (Identity → Meta? → Value/State? → Action?)
   └─ 콘텐츠 길이에 따라 높이 증가
```

- **외부 Surface는 한 번만 선택**한다. Plain이면 여백과 이미지/텍스트의 공통 소속으로, Solid/Outlined/Subtle 등이 필요하면 아이템 전체에 적용한다. 내부 정보 영역은 **독립 Surface/Card가 아니며**, 선택적 배경 구분·간격·Divider 때문에 두 번째 독립 카드로 승격하지 않는다.
- **프레임과 부모의 치수를 혼동하지 않는다.** C-IMAGE가 소유한 1:1 기본 비율·contain/cover·loading/error·Badge anchor는 이미지 영역에 한정한다. 정보 패널과 부모의 전체 높이는 이름·메타·실제 가격·선택적 Action의 자연스러운 줄바꿈에 따라 늘어난다. 고정된 하단 패널 높이·그 위 절대 위치 텍스트·이미지 뒤로 밀려나는 버튼을 금지한다.
- **상호작용은 하나의 책임:** 전체 이동형 아이템은 링크 한 개, 구매/장착 같은 독립 버튼이 필요하면 정적 부모에 링크와 버튼을 형제 조작으로 둔다. 링크 안에 구매 버튼을 중첩하거나 이미지와 정보 패널에 서로 다른 가짜 클릭을 부여하지 않는다.
- 책장형 비대칭 배열·가로 흐름형·Grid는 **부모 아이템들의 배치**를 결정할 뿐 내부가 두 카드로 갈라져도 된다는 뜻이 아니다. **숫자+라벨**만 가진 인사이트 넘버 박스는 별도의 정보 패턴으로, 같은 아이템 Surface/Media 구조를 강제하지 않는다.
- 이벤트 콘텐츠의 얇은 단일 이미지 배너/박스형 동적 표현은 **U05(S09)** 범위다. 일반 아이템 B를 이벤트 박스의 공통 구현으로 합치거나 기존 Surface 10종을 늘리지 않는다.

### S06 표면 디테일 · 통합 카드 / 오픈 진열 (2026-11-11)

- **통합 카드:** 기존 `Solid` 또는 의미에 맞는 Surface **한 개**로 전체 상품을 묶는다. 이미지 영역은 부모 폭에 밀착할 수 있고 위쪽은 `bg.muted`, 아래 정보 영역은 `bg.surface`처럼 톤을 나눌 수 있다. **중립 1px 외곽 border는 부모에만** 적용하며 내부 이미지·정보 패널에 반복 테두리/그림자를 넣지 않는다. 부모 radius(기본 16)를 기준으로 이미지 상단과 정보 하단의 외곽을 잘라 일체감 유지.
- **오픈 진열:** 부모는 `Plain`이 기본. **바깥 카드·border·그림자 없음**. `C-IMAGE`에만 낮은 강조의 배경·radius(기본 14)를 사용하고, 그 아래 정보 영역은 별도 배경·구분선 없이 페이지 위에 배치. 이미지 영역까지 꼭 border를 둘 필요는 없다.
- **사용 권장:** 상품의 구매·착용·선택 등 독립 **버튼이 있으면 통합 카드 외부 컨테이너를 우선 권장**한다. 버튼이 없는 다수 상품 비교는 오픈형 우선. 단, 정보 목적이나 행동의 강도가 다르면 예외를 허용하며 Surface 10종을 늘리지 않는다.
- **반응형:** 아이템 프레임 비율과 contain 안전 여백은 C-IMAGE, 반복 열/폭은 P-LAYOUT, 정보 영역의 유동 높이와 하단 Action 배치는 P-HIERARCHY가 소유한다. 카드 전체 고정 aspect-ratio·고정 하단 패널·absolute CTA·이중 border 금지.

## S09 · 이벤트 콘텐츠의 Surface 소유권과 일반 Banner 구별

**일반 Container**는 묶음의 의미적 경계를, **이벤트 콘텐츠**는 홍보/참여 유입/상태 확인이라는 목적을 말한다. S09는 새 Surface Role이나 11번째 Variant가 아니다. [P-HIERARCHY S09](../patterns/content-hierarchy.md)에서 목적·관련성·변동 데이터·행동을 판단한 다음 기존 Plain/Solid/Media/Reward 등을 필요할 때만 선택한다.

| 표현 | Surface와 DOM의 경계 | 기존 Banner와의 관계 |
|---|---|---|
| **일반 Banner** (기존 Anatomy) | **Art + 짧은 메시지 + 선택적 Action**을 페이지 목적에 맞게 조합하는 종전 계약 | 기존 Banner의 텍스트·Action 허용을 전역적으로 취소하지 않음 |
| **이벤트 A · 얇은 단일 이미지** | 완성 이미지 1장 자체가 콘텐츠, 이동이 필요하면 이미지 한 장을 감싼 **전체 링크 1개**. 별도 Title/Body/Badge/Progress/Button/동적 수치 **오버레이 0** | 일반 Banner와는 다른 **특정 내용 구성**. Reward/Media를 쓰더라도 이미지 외 UI를 넣는 순간 더는 A가 아님 |
| **이벤트 B · 박스형** | 이미지·제목·설명·수치·진행률·상태·보상·Action **각각 선택**. 독립 UI는 읽기 가능한 실제 콘텐츠·행동만 표시 | 기존 Surface+P-LAYOUT+C-IMAGE+C-STATUS+C-ACTION 조합. `이미지+Progress+Button` 고정 템플릿 아님 |

- A의 완성 이미지에는 문구가 이미지 **안에 포함**될 수 있으나, 배너 위로 별도 CSS 텍스트나 가짜 버튼을 얹지 않는다. 접근성 이름·원본 가로 비율·텍스트 안전영역은 C-IMAGE/A-ART에서 확인한다. 이미지가 아직 없으면 개별 아이템 오브젝트나 임시 색 블록을 A로 위장하지 않는다.
- B는 기존 Surface 한 개를 경계로 하고 **필요한 구성만** 담는다. 다만 '박스형'은 반드시 두툼한 테두리/그림자나 건조한 표를 뜻하지 않는다. 이미지 중심 A2 장면도 가능하며 Reward 강조는 이벤트의 실제 중요도에만 사용한다. 임의 포인트/참여 상태를 합성하지 않는다.
- **요약형은 표면 타입이 아니다.** 관련성이 낮으면 A/B의 정보량과 주목도를 낮추거나 저강도 기존 안내/링크를 고른다. 시즌이라고 진행률을 강제하지 않고, 홍보라고 A를 강제하지 않는다.
- **U04 경계 유지:** 아이템 B의 '이미지+정보가 한 아이템 부모' 소유권을 이벤트에 그대로 강제하지 않는다. 두 콘텐츠는 동일한 기존 Surface를 공유할 수 있어도 **아이템 정보 객체와 이벤트 참여/상태 정보의 목적은 다르다.** 레거시 `clan-reward-hero`에 Progress와 버튼이 있다는 사실도 모든 이벤트 B의 필수 슬롯이 아니다.

## 2026-11-10 · 공간 배치와 Surface 소유권 (제안 보완)

[공간 배치 규격](../patterns/layout-composition.md)은 자식의 세로/가로/격자/겹침 관계를 관리한다. 이 문서의 Plain/Outlined/Solid/Inset/Media/Reward 등 10종은 **표면 표현과 의미적 그룹 경계**를 관리한다. 두 체계는 직교하며 조합할 수 있지만, 레이아웃 선택이 곧 Card/Solid 선택은 아니다.

- 반복 행이 비교의 중심이면 Plain + 세로 배치 + 기존 List를 우선 검토한다. 행 전체마다 새로운 Card/Shadow는 만들지 않는다.
- 균일 상품 탐색이면 Grid 안에 기존 Image Frame과 정보 슬롯을 배치한다. 보유/가격/장착 상태를 Grid 내부 스타일로 정의하지 않는다.
- 독립 프로모션 장면이면 Media/Reward를 명시적으로 선택하고 A-ART와 C-IMAGE를 따르되, **진행 관리·아이템 탐색·수령 결과까지 한 표면에 무조건 합치지 않는다.**
- Image Frame의 모서리 Badge/State 슬롯은 C-IMAGE가 소유한다. 범용 Layer가 동일한 배치 계약을 재정의하지 않는다.
- 섹션과 Surface를 일대일로 대응시키지 않는다. Section은 목적 단위, Surface는 표현상의 경계다.

기존 10종 Surface 역할·토큰·금지 중첩 규칙은 변경하지 않는다.
