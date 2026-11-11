---
id: C-ACTION
category: component
status: proposed
read_when: 일반·아이콘·텍스트 버튼, 핵심·보조 행동, 취소·수락
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-005, REF-006, REF-007, SRC-SEED-BUTTON, SRC-TDS-BUTTON]
---

# Action Button family

## Purpose / Non-goals
명시적 행동을 실행한다. 선택·탐색 탭·단순 상태 배지는 버튼 variant가 아니다. 다른 페이지로 이동하는 링크는 플랫폼의 link 의미를 유지한다. HTML 태그나 라이브러리 설치는 이 문서의 구현 지시가 아니다.

## Anatomy
Container + Label + 선택적 Prefix 또는 Suffix + Loading indicator. 아이콘은 의미를 돕는 하나만 기본으로 사용한다. 장식 배지·코인·카운터를 모두 버튼에 밀어 넣지 않는다. Icon Button의 독립 size/target·Badge·상단 규칙은 [C-ICON](icon-button.md)을 따른다. Text Button은 배경 면 없이 같은 행동 의미와 터치 기준을 유지한다.

## Properties / Variants
role(action/iconAction/textAction/link), emphasis(high/medium/low), tone(neutral/brand/critical), treatment(solid/weak/outline/ghost), size(small/medium/large), width(hug/fill)을 별개 축으로 본다. 모든 조합을 만들지 않고 아래를 기본 허용 조합으로 제한한다.

| 이름·별칭 | Emphasis | Tone / Treatment | 배경 / 전경 토큰 | 사용 |
|---|---|---|---|---|
| Primary / Brand Solid | High | brand / solid | accent.solid / accent.onSolid | 화면 목적을 완결하는 대표 행동 |
| Neutral Solid | High | neutral / solid | neutral.solid / neutral.onSolid | 중립적 확정이 대표 행동일 때, 강제 기본 아님 |
| Secondary | Medium | neutral / weak | bg.muted / fg.primary | 대부분의 일반·보조 실행 |
| Secondary Brand / Reward | Medium, 보상 시 High 후보 | secondary / solid | accent.secondary / accent.onSecondary | 보상 수령·보관 등 제한된 가치 관련 행동 |
| Brand Outline | Low | brand / outline | transparent / accent.onWeak + 독립 stroke | Brand Weak와 다른 강조의 브랜드 보조 행동 |
| Brand Weak | Medium 후보 | brand / weak | accent.weak / accent.onWeak | 브랜드 맥락의 보조 실행, REF-006 제안 유지 |
| Neutral Outline | Low | neutral / outline | transparent / fg.primary, stroke.control | 면이 많을 때 부가 행동 |
| Tertiary / Ghost | Low | neutral / ghost | transparent / fg.secondary | 전체 보기·미루기·추가 설명 등 맥락별 보조 |
| Critical | 문맥별 High 또는 Low | critical / solid 또는 ghost | critical.fg / bg.surface 또는 transparent / critical.fg | 실제 파괴·위험 행동에 한정 |

Critical solid의 전경은 해당 테마 bg.surface를 사용하며 대비 검증 후 채택한다. Critical ghost는 투명 배경 위 대비를 확인한다. 중요하지 않은 '거절/넘기기'에 자동 Critical을 쓰지 않는다.

SEED 현행 문서에는 Brand Weak가 기본 variant로 나열되지 않는다. 이 시드는 사용자 REF-006 제안 및 TDS weak의 구조를 참고한 별도 후보이며 SEED를 그대로 구현했다고 주장하지 않는다. Brand Solid와 Neutral Solid 모두 High가 될 수 있다.

## Size & Tokens
높이·padding은 F-GEOMETRY의 size.action* 및 action.padding* 정본, label은 F-TYPE의 type.button/buttonCompact, radius는 radius.control을 쓴다. Small label은 buttonCompact와 size.iconButtonCompact, Medium/Large는 button과 size.iconButtonContent를 사용한다. 아이콘을 강조하려고 label보다 과대하게 키우지 않는다. Icon Button은 [C-ICON](icon-button.md) 정본을 따르며 상단은 D-011 우선이다.

사용자 후속 피드백 U-04에 따라 Container의 높이·좌우 padding·최소 터치 영역은 유지하고 글자·아이콘을 소폭 축소한다. 줄높이 감소분은 상하 padding으로 돌려 버튼 내부의 숨 쉴 공간을 늘린다. 단독 Icon Button·상단·본문 아이콘의 크기는 함께 축소하지 않는다.

너비는 내용과 맥락에 따라 hug/fill, 내부 정렬은 중앙이다. glyph와 label의 광학 중심을 맞춘다. 로딩·아이콘 유무·tone 변경으로 버튼 외곽을 바꾸지 않는다.

## v0.6 내부 콘텐츠 튕김 방지
- Label/Prefix/Suffix는 가운데로 정렬된 **하나의 그룹**. Button inline icon 16–18dp, gap 6–8dp, 24 viewBox 기반 C-SVG glyph 사용.
- Pressed 시 scale/font-weight/letter-spacing/padding/border-width를 바꾸지 않는다. 색상/opacity만 전환한다.
- Loading 시 Label의 너비와 중심 좌표를 유지하고 spinner만 position overlay; Icon slot 너비도 보존. Disabled 시 text+icon의 상대 위치 유지.
- 상태 전환 전후 동일 크기 버튼의 outer rect와 label center 이동은 1px 이하를 QA 기준으로 삼는다. 200% font scale에 의한 의도적 reflow는 예외. 선택 의미는 C-SELECTION.
- Button+SVG 조합의 자세한 Anatomy는 [P-BTN-ICON](../patterns/button-icon-layout.md).

## States / Behavior
| 상태 | 표현·동작 |
|---|---|
| Enabled | 역할별 기본 토큰, 명확한 동사 라벨 |
| Hover | 포인터 환경에만 F-COLOR overlay, 모바일에 가정하지 않음 |
| Pressed | 같은 면·크기에 press overlay, 중복 연출 없음 |
| Focus-visible | 외곽 focus ring, pressed·loading과 독립 |
| Loading | 폭·라벨 위치 유지, 진행 표시와 busy 의미, 중복 실행 방지 |
| Disabled | disabled 토큰·실행 불가 의미, 필요 시 가까운 이유 설명 |
| Success / Error | 완료 여부는 피드백으로 표시, 오류 후 재시도 허용 시 원래 액션 복귀 |

선택은 일반 실행 버튼의 지속 상태가 아니다. 토글 동작은 C-SELECTION을 사용한다. loading은 disabled와 동일한 의미가 아니며 스크린리더에 진행 중을 알린다.

## Guidelines
High는 한 행동 영역에서 하나를 기본으로 한다. 화면의 대표 CTA가 여러 지역 High와 경쟁하는지도 확인한다. 의미 없는 전체 너비, 맥락 없는 비대칭/대칭 강제, 모든 액션의 붉은 Solid, 버튼마다 다른 둥글기·그라데이션·선물 아이콘 확대를 금한다. 버튼 조합은 P-ACTIONS를 읽는다.

## Accessibility
텍스트 대비·48 최소 조작 영역은 Foundation을 따른다. Icon Button 이름은 동작을 설명한다. 긴 한글·200% 글자에서 padding 유지, 높이 확장 또는 그룹 재배치. loading/busy·disabled를 색만으로 알리지 않는다. 키보드 실행·포커스 이동은 의미에 맞춘다.

## Evidence & Confidence
REF-006은 제안, REF-007은 실앱 한 화면 관찰 medium. REF-005는 명시 교정 근거 high. 상태·수치·조합 API는 proposed이며 원본 화면에서 직접 관찰했다고 쓰지 않는다.

## Dependencies & Impact
Foundation만 토큰을 소유한다. 강조 규칙 변경은 P-ACTIONS, 목록 suffix, 고정 하단 CTA에 영향을 준다. 이름이 다른 기능별 버튼을 별도 컴포넌트로 만들지 않는다.


## v0.15 Outline / Reward CTA 우선 규칙 (D-012)

- `Outline` 기본은 **Neutral Foreground + Neutral Border**. Brand Red를 자동 상속하지 않는다. `Brand Weak`는 명시적 보조색 액션이고 `Brand Outline`은 별도 이름을 사용할 때만 허용; 기본 Outline과 혼동하지 않는다.
- Secondary Gold CTA: Light `#FFCC4D` + dark `#352713` label/icon, **Dark `#957027` + white label/icon**. 단독 카드/배경에 노랑 CTA 톤을 전파하지 않는다.
- Label + Icon 하나의 중앙 정렬 그룹, Icon 16–18(기본 17), 간격 6–8(기본 7), control radius 16. Pressed/Loading은 label center·외곽 크기 보존.
- Button 실제 표시 높이 Small 34 / Default 43 / Large 48. Android는 어떤 크기든 비중첩 최소 48dp touch target 필요.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K07 액션과 클릭 책임

**정본 소유자:** `components/action-button.md`, 그룹 관계는 `patterns/action-layout.md`.

| 이번 웹 어댑터 variant | 기본 강조 | 사용 |
|---|---|---|
| brand-solid | High | 제품의 핵심 실행 |
| neutral-solid | High | 중립적 확정 |
| neutral-weak | Medium | 일반·보조 실행 |
| neutral-outline | Low | 낮은 강조의 보조 행동 |
| ghost | Low | 상세/전체 보기 등 관련 이동. 섹션 제목 옆 보조 액션의 기본 무테·무배경 표현 |
| reward-secondary | Medium | 보상 맥락의 실제 행동. 대표 수령 과업이면 High로 사용할 근거를 기록 |

이는 이번 구현에 필요한 허용 조합이며 기존 Brand Weak/Critical 등 다른 계약을 삭제하지 않는다. 필요 없는 조합을 선제 구현하지 않는다. 화면 대표 High는 기본 최대 1개이고 조회 화면은 0개도 정상이다. 가로 3개 이상의 행동은 재배치·메뉴를 우선 검토하며, 48 타깃을 유지할 수 없으면 병렬 배치를 중단한다. 단순 초대·미리보기·일반 확인을 모두 브랜드색으로 채우지 않는다.

**섹션 제목 우측의 `더보기`·`전체보기`는 CTA Button과 다른 보조 이동 역할이다.** [P-PAGE-COMPOSE S04](../patterns/page-composition.md)의 무테 텍스트/선택적 Chevron 표현을 우선하며, 이 위치를 이유로 `neutral-outline`·`brand-solid`·`reward-secondary` 등 눈에 보이는 테두리·배경 버튼을 자동 배치하지 않는다. 이동은 실제 링크, 그 외 보조 동작은 `textAction` / `ghost`를 재사용한다. 기존 조작 타깃 48·포커스 표시·의미 있는 이름은 그대로이며, 주행동은 본문/후행 Footer 등에서 별도로 판단한다. 이 원칙은 다른 문맥에서 승인된 Outline/CTA의 사용을 금지하지 않는다.

실제 target은 최소 48 CSS px와 3rem 중 큰 값으로 비중첩 확보한다. face의 표시 높이 34/43/48, radius16, 버튼 아이콘 기본17·허용16–18, gap7을 보존한다. 48 미만 face는 같은 링크/버튼 요소가 타깃을 소유하고 내부 face를 중앙 정렬한다. 외부 투명 의사요소로 이웃을 덮지 않는다. 글자 확대 때 face 높이를 고정 상한으로 쓰지 않는다.

Pressed/Loading에서 scale·font-weight·padding·border-width를 바꾸지 않는다. focus는 크기를 밀지 않는 ring을 사용한다. 버튼 label과 아이콘은 한 정렬 그룹이다. 일반 링크도 의미 있는 이름·키보드 포커스·실제 목적지를 갖는다. 같은 영역에서 같은 목적지의 텍스트 링크와 가짜 개별 버튼을 반복하지 않는다.

아트가 장식이면 비클릭이어도 된다. 클릭처럼 보이는 카드·Chevron·Pressed는 실제 대응 동작이 있을 때만 사용한다. 서버 기능이 없는 시연에 지급 완료·구매 완료를 암시하는 버튼을 두지 않는다.

## 2026-11-10 · Action 패밀리 구현·실험 구분

[컴포넌트 패밀리 지도](../implementation/component-families.md)에 Variant / Size / Width / Icon / State의 독립 축과 예시를 정리한다. **기존 승인 6종**(`brand-solid`, `neutral-solid`, `neutral-weak`, `neutral-outline`, `ghost`, `reward-secondary`)과 Small/Default/Large(34/43/48)는 기존대로 유지한다.

**서브브랜드 Gold는 이미 `reward-secondary`로 존재**하며 기본 테마 색을 바꾸거나 새 별도 버튼 클래스를 만들지 않는다. Brand Weak / Brand Outline은 기존 본문에 언급됐지만 K07의 허용 구현 목록에는 없는 **후보**다. 필요한 경우 공통 CSS에서 명시적 실험 scope에만 활성화하고, 채택 전 전역 페이지에는 적용하지 않는다. Critical은 별도 의미색 승인 전 임의로 브랜드 빨강을 복용하지 않는다.

공통 로딩 표현은 내부 Label/Icon 슬롯의 자리·버튼 target을 보존한다. 이벤트 중복 차단 및 로딩 종료/오류 복원은 호출자가 구현·검증한다. 버튼 사이의 비율·고정 여부는 여전히 P-ACTIONS와 S-PAGE 책임이다.
