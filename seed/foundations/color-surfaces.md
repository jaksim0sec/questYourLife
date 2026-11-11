---
id: F-COLOR
category: foundation
status: proposed
read_when: 색상 역할, 라이트·다크, 배경·경계·강조·대비
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-002, REF-004, REF-006, SRC-SEED-BUTTON, SRC-TDS-BUTTON, SRC-WCAG]
---

# 색상과 표면

## 범위·지위
아래 HEX는 원본 픽셀 추출값이 아니라 **검토 가능한 제안값**이다. 라이트·다크를 서로 다른 화면의 로컬 예외가 아닌 같은 의미 토큰의 두 매핑으로 둔다. 어떤 테마를 기본으로 제공할지는 미결이다. 외부 시스템의 색을 복사하지 않았다.

## 토큰 정본

| Token | Light | Dark | 용도 |
|---|---|---|---|
| bg.canvas | #FCFBFA | #17171B | 페이지 바탕, Light를 조금 더 중립적으로 교정 |
| bg.surface | #FFFFFF | #232329 | 기본 그룹 면 |
| bg.muted | #F1F0F3 | #303038 | 약한 그룹·보조 행동 |
| bg.floating | #FFFFFF | #2C2C34 | 시트·팝오버 |
| fg.primary | #17171B | #F4F2F5 | 본문·중요 제목 |
| fg.secondary | #5E5A65 | #B8B5BF | 설명·메타데이터 |
| fg.muted | #6E6A74 | #ABA7B2 | 낮은 강조, 읽혀야 하는 정보 |
| stroke.subtle | #E8E4E7 | #44414B | 장식·그룹 구분, 조작 식별에 단독 사용 금지 |
| stroke.control | #827A87 | #938B9C | 입력·선택의 필수 경계 |
| accent.solid | #DB3040 | #FF8173 | 핵심 Brand 행동, 더 따뜻한 레드 v0.2 후보 |
| accent.onSolid | #FFFFFF | #17171B | Brand Solid 라벨·아이콘 |
| accent.weak | #FFF0F2 | #452831 | 브랜드 보조 면 |
| accent.onWeak | #A71C2E | #FFADBA | 브랜드 보조 전경·현재 선택 텍스트 |
| neutral.solid | #33313A | #E6E2EB | 중립적 High 행동, 필요할 때만 |
| neutral.onSolid | #FFFFFF | #17171B | Neutral Solid 전경 |
| positive.fg / .bg | #087E55 / #E6FFF4 | #76EABD / #0B5038 | 성공·완료 (시연 팔레트) |
| warning.fg / .bg | #985500 / #FFF6E5 | #FFD27D / #653700 | 주의 (시연 팔레트) |
| critical.fg / .bg | #A92135 / #FFF0F2 | #FF9CA7 / #3A2228 | 오류·파괴 행동 |
| info.fg / .bg | #0B63D8 / #EAF3FF | #99C4FF / #123B79 | 안내·상승 (시연 팔레트) |
| focus.ring | #0B63D8 | #99C4FF | 키보드 포커스 (시연 팔레트) |
| disabled.bg / .fg | #EFEDF0 / #827D88 | #302D35 / #8E8896 | 비활성 전용 |
| overlay.scrim | #000000, 40% | #000000, 56% | 모달 뒤 배경 |

브랜드 빨강은 오류·미완료를 뜻하지 않는다. critical과 브랜드가 비슷한 색을 써도 아이콘·문구·맥락으로 구분한다. 캐시 코인 금색·클랜 지정색·이벤트색은 이 표의 전역 역할을 덮어쓰지 않는다.

사용자 후속 피드백 U-04에 따라 브랜드 빨강에 주황기를 소폭 더한 따뜻한 매핑을 제안한다. 오류·파괴 행동의 critical 색은 독립된 의미 토큰으로 유지한다. 새 수치는 방향 조정에 대한 후보이며 최종 시각 승인을 뜻하지 않는다.

## 기존안 대비 개선 · S2-01/03/05/06/08
- v0.1 브랜드 후보 `#D63D36` → v0.2 `#D93B3B`: 조금 더 밝고 따뜻한 붉은색. 계산상 흰 글자 대비 약 **4.53:1**, 최소 대비를 유지하는 범위에서만 테스트한다. 더 밝은 `#F0444B`와 흰 글자 조합은 약 3.73:1로 일반 텍스트에 실패하므로 기본 Primary 제안에서 제외한다.
- Neutral Solid는 브랜드 기본 행동을 대신하지 않으며, 일반 업무/리스트의 대부분은 Neutral Weak 또는 약한 Text/Outline이 먼저다.
- 배경·카드·버튼의 그라데이션을 기본 구성에서 제거한다. A2/A3의 보상 이미지 자체에만 3D 조명·광택을 보존한다.
- 새로운 HEX 매핑은 모두 proposed. 실제 웹 테마·장시간 화면 사용·브랜드 일관성 검증 후 승인한다.

## v0.6 보상 2차 브랜드색 및 등급색 계약 (모두 proposed)
| Token | Light | Dark | 규칙 |
|---|---|---|---|
| accent.secondary | #FFCC4D | #957027 | **보상용 Secondary CTA** 전용 면. 다크는 흰 글자 대비 확보를 위해 톤 다운 |
| accent.onSecondary | #352713 | #FFFFFF | 라이트는 진한 텍스트·SVG, **다크는 흰색 텍스트·SVG** |
| itemRarity.common | #FAFAFA→#EDEDF1 | dark-specific mapping pending | 등급값 후보, 브랜드 변경과 독립 |
| itemRarity.uncommon | #F5FCF7→#E2F2E8 | pending | 미세 그라데이션 |
| itemRarity.rare | #F6F9FF→#E0ECFF | pending | 미세 그라데이션 |
| itemRarity.epic | #FCF8FF→#EDDEFA | pending | 미세 그라데이션 |
| itemRarity.legendary | #FFFCF3→#FFE9BD | pending | 미세 그라데이션 |
| itemRarity.special | #FFF7F7→#FFE2E7 | pending | 미세 그라데이션 |

Dark에서는 일률 brightness/filter로 처리하지 말고 전경/배경 대비를 함께 조절한다. 코인 이미지는 사용자 제공 투명 골드 원본을 별도 에셋으로 소유하며 HEX 토큰으로 재생성하지 않는다. 밝은 아트 배경 위 글자는 전체 다크 테마 본문색을 상속시키지 않는다. Brand Weak·Brand Outline·Switch OFF·Skeleton은 다크 전용 의미 토큰을 적용한다.

## 표면·깊이
Surface는 정보 그룹이고 그 자체로 버튼이 아니다. 카드 여부는 독립된 콘텐츠 단위·클릭 동작으로 판단한다. 그룹 내부 행마다 별도 카드·그림자를 중첩하지 않는다.
- elevation.flat: 그림자 없음, 기본.
- elevation.floating: 검정 10% / y=4 / blur=16의 제안. 모달·팝오버에서만 사용하며 경계·위치로도 구분한다.
- 일반 버튼은 그림자·그라데이션 없음. 보상 물체의 금속광·발광은 A-ART 범위.
- 글래스·블러는 채택하지 않은 후보다. 기본 정본은 불투명 표면이며 D-011의 상단 무채색 면을 우선한다. 다크 헤더를 전역으로 쓰는 것은 현재 확정안과 충돌하므로 O-022에서 보류한다.

## 상태 색
Pressed는 원래 tone을 유지한 채 Light 검정 6%, Dark 흰색 8% overlay의 제안. Hover는 포인터 환경에서만 4% overlay. Focus는 색 변경과 독립된 외곽 2 논리 단위 ring + 2 offset. Disabled는 전용 토큰을 쓰며 부모 전체 opacity를 내려 본문까지 흐리게 하지 않는다. 선택은 강조 배경·체크·밑줄 중 해당 컴포넌트의 규칙으로 표현한다.

## 접근성·근거
일반 텍스트 4.5:1, 큰 텍스트 3:1, 필요한 조작 경계·상태 표시 3:1을 검증 목표로 한다. 장식 경계와 disabled 제외를 접근 가능한 조작 경계의 핑계로 쓰지 않는다. 아래는 불투명 sRGB 매핑 계산 결과이며 실제 합성·압축·실기기 검증은 별도다.

| 조합 | 계산 대비 |
|---|---:|
| Light accent.onSolid / accent.solid | 약 4.66:1 (v0.6 후보 계산값) |
| Light accent.onWeak / accent.weak | 약 6.67:1 (v0.6 후보) |
| Dark accent.onSolid / accent.solid | 7.35:1 |
| Dark accent.onWeak / accent.weak | 약 7.46:1 (v0.6 후보) |

배경 이미지 위 텍스트는 별도 불투명 받침 또는 충분한 명암 영역을 확보한다. 직접 관찰 high: 두 테마의 중립 면과 빨강. 매핑·상태 overlay는 제안이며 렌더 검증 전이다.

## 영향
여기서만 전역 색을 변경한다. 컴포넌트 문서에서 HEX를 재정의하지 않는다. 대비가 실패하면 tone을 유지하며 역할 토큰을 조정하고 전체 사용처를 확인한다.


## v0.15 사용자 승인·대비 규격 (D-012)

`accent.secondary` 위 표는 **CTA의 면**만 가리킨다. 금속 보상 이미지 및 의미 없는 노란 배경은 별도 A-ART. 웹 v0.15에서 Secondary CTA는 Light `#FFCC4D / #352713`, Dark `color-mix(#FFCC4D 40%,#4E330D 60%)` ≈ `#957027 / #FFFFFF`; 불투명 sRGB 기준 흰 글자 대비 약 **4.54:1** (반올림), 라이트 어두운 글자 대비 약 **10.7:1**. 실제 hover/반투명 합성은 배경별 추가 검증.

현재 시연의 기본 표면: canvas Light `#FCFBFA` / Dark `#17181D`, surface `#FFFFFF` / `#242329`, soft `#FFF7F2` / `#2D292C`, subtle line `#F0E8E5` / `#373338`, primary text `#242126` / `#F6F3F3`. 나머지 의미색은 기존 제안 테이블의 역할을 유지한다.

**브랜드 빨강은 Outline의 기본 foreground가 아님.** Neutral Outline은 중립 텍스트+중립 테두리, Brand Weak/Brand Outline만 명시적 브랜드 보조 액션. Switch OFF / Skeleton / Brand Weak는 Light 변형에 `opacity`를 일괄 덧씌우는 대신 Dark 전용 표면·경계·전경 사용. 보다 자세한 최종 범위는 [v0.15](../implementation/v015.md).


## 2026-11-07 선명한 컬러 팔레트 · 웹 시연에 임시 채택

기존 승인된 Brand Red `#DB3040`와 Reward Gold `#FFCC4D`는 **그대로 보존**한다. Blue/Green/Orange/Purple의 기준색 500은 이번 대화에서 **우선 적용할 값**으로 선택했으며, 100/300/700/900은 시각·대비 검증을 위한 파생 후보이다. 최종 Android/제품 전역 확정과 구분한다.

| 계열 | 100 | 300 | **500 기준색** | 700 | 900 |
|---|---|---|---|---|---|
| blue | #EAF3FF | #99C4FF | **#3987FF** | #0B63D8 | #123B79 |
| green | #E6FFF4 | #76EABD | **#19C98A** | #087E55 | #0B5038 |
| orange | #FFF6E5 | #FFD27D | **#FFAD32** | #985500 | #653700 |
| purple | #F3ECFF | #C5ADFF | **#A078FF** | #7445D6 | #48238F |

- `palette.{family}.{level}` → `semanticPalette.{role}.{theme}.{foreground|background|accent}` → 컴포넌트. 페이지별 직접 HEX 입력 금지.
- 시각 의미: Blue=`trendUp`/정보, Green=`success`/완료, Orange=`warning`/주의, Purple=`special`/이벤트. Red=브랜드, Gold=보상으로 분리.
- 라이트: foreground 700 + background 100 + 작은 장식/도트 accent 500. 다크: foreground 300 + background 900 + accent 500. 배경은 필요한 상태에서만 사용.
- 채도가 높은 500을 라이트 테마 작은 본문에 바로 사용하면 대비 부족이므로 텍스트에는 의미색 foreground를 사용한다.
- 색상은 보조 신호로만 쓰고 상승 화살표·숫자·진행률처럼 형태/텍스트도 함께 사용한다. 상승률의 산식과 대상은 미확정.
- 토큰의 단일 데이터 원본은 [v015-tokens.json](../implementation/v015-tokens.json)의 `color.palette` 및 `color.semanticPalette`이다. 기존 의미색 표의 success/warning/info/focus는 이번 웹 시연 trial mapping으로 업데이트했다. 이전 제안은 Git 이력에 남는다.


## 2026-11-08 · 웹 시연의 3단 Shell 표면 분리 (trial, 사용자 피드백)

관찰: 기존 웹 시연의 상단 `#header`와 하단 `#nav`가 같은 `--surface`를 사용해 상단·콘텐츠·하단바의 독립적인 깊이가 보이지 않았다. 참고 화면은 하단바가 **상단 헤더 및 일반 콘텐츠보다 한 단계 밝은 표면**이다.

| 역할 | Light trial | Dark trial | 실제 적용 범위 |
|---|---|---|---|
| `canvas` | `#FCFBFA` | `#17181D` | 페이지 바깥·Flat Section 사이 간격 |
| `shellSurface` | `#F8F7F9` | `#1D1D22` | Top Navigation + 일반 `#main` + Clan Flat Section |
| `bottomNavSurface` | `#FFFFFF` | `#242329` | 고정 Bottom Navigation |

- 웹에서는 `--shell-surface`, `--nav-surface`로 역할을 분리. 이 두 값을 기존 `--surface` 카드/시트와 혼동하지 않음.
- **기존 v0.15의 기본 `color.light/dark.canvas/surface`값은 그대로 보존**하고, 승인 전 숫자를 `color.previewShellLayers`에만 기록. 범위: `new/web/preview`. Android 및 현재 TopNav의 구조·SVG는 변경하지 않음.
- 다크 계층은 배경(`#17181D`) → 상단/본문(`#1D1D22`) → 하단 네비(`#242329`). 라이트도 상단/본문이 하단 네비보다 약간 어둡게 표현됨.
- 앱 콘텐츠 중 일반 카드/컨트롤은 기존 `--surface`가 유지되므로 Shell과 다른 표현을 가질 수 있음. 이것은 Shell의 3단 기본 깊이와 별개의 *내부 UI Surface 역할*임.
- 정확한 HEX는 시각 검토용 매핑이며 사용자 승인된 새로운 숫자 정본이 아님. 클랜 섹션 간 `--canvas` 간격, Safe Area, RootFontSize, Active/Inactive 아이콘을 그대로 둔다.

> v5.3 접근성 보정: Light Bottom Nav 선택 텍스트만 공식 `#DB3040` 사용 (흰색 위 약 **4.66:1**). 기존 legacy `#F04E5B`는 흰색에서 약 **3.53:1**이라 작은 탭 라벨에 부족했다. Dark는 기존 `#F45C68` / `#242329` (약 **4.88:1**) 유지. 전역 브랜드/다른 버튼 색은 변경하지 않음.


## 2026-11-08 후속 · Dark 3단 배경 대비 강화 (웹 시연 한정)

사용자 지시: 섹션 간격 `canvas`와 일반 `shellSurface`의 HEX 밝기를 낮추고 Bottom Nav는 현재보다 상대적으로 밝게 유지.

| 역할 | 직전 trial | 새 trial | 범위 |
|---|---|---|---|
| `canvas` (간격/최하위 면) | `#17181D` | **`#111216`** | `--canvas` |
| `shellSurface` (상단·콘텐츠 면) | `#1D1D22` | **`#18191E`** | `--shell-surface` |
| `bottomNavSurface` | `#242329` | **유지 `#242329`** | `--nav-surface` |

이 변경은 **`new/web/preview`용 실험값**으로, D-011 헤더 구조와 D-012의 기존 `color.dark.canvas`/`surface` 승인값을 바꾸는 결정이 아니다. 일반 카드·버튼·보상 아트·텍스트·SVG·Safe Area는 그대로 둔다. 색 대비의 높낮이/토큰 일치 정적 확인과 실제 기기 스크린샷 검수를 구분한다.


## 2026-11-08 · 원본 이미지 기반 Depth 조정 (WEB PREVIEW ONLY, proposed)

11444.jpg(당근 크롭)의 일반 바탕 샘플 `#16171B`, 11449.jpg(현생패치 화면)의 콘텐츠 샘플 `#18191E`, 간격 약 `#121317`, 하단 네비 `#242329`를 대조. 이미지 압축·표시 장치·다크 모드 차이가 있으므로 이를 당근 SEED의 공식값이라고 **주장하지 않음**.

이번 시연은 `--canvas:#101115` / `--shell-surface:#16171B` / `--nav-surface:#242329`로 시각 조합을 시험. 상단·일반 콘텐츠·클랜 Flat Section은 shell, 섹션 간 거터는 canvas, 하단 내비게이션은 nav 전용 색. **승인된 D-012 color.dark.canvas `#17181D`, surface `#242329`는 보존**한다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K11 색·아트의 보존 경계

**정본 소유자:** `foundations/color-surfaces.md`, `assets/reward-art.md`.

브랜드/보상/중립/성공/정보/주의/특별의 의미를 유지한다. 색·치수는 분야 정본에서만 소유하며 지역 CSS의 새 HEX와 글자 크기 예외를 누적하지 않는다. 동일 톤의 변화가 필요하면 실제 대비 실패와 사용처를 리드에게 제시한다.

이번 개편은 기존 JSON의 `color.previewShellLayers`를 웹 역할 매핑으로 유지한다: canvas는 바탕/간격, shell은 헤더+Plain 콘텐츠, bottomNav는 하단바, 기존 `--surface`는 독립 면/시트/컨트롤이다. 기존 `--brand`를 승인 브랜드값으로 전역 일괄 교체하지 않는다. 새 공통 액션이 승인 Brand 규격을 사용하면 별도의 의미 변수로 승인값을 참조하고 기존 홈에 연결된 `--brand`의 전역 의미를 바꾸지 않는다.

새 공통 표현의 CSS 바인딩은 아래로 고정한다. JSON과 F-COLOR가 같은 이름에서 다르면 표에 지정한 소스를 사용한다. 기존 전역 변수는 변경하지 않고 A가 이 별칭을 소유한다.

| CSS 별칭 | 단일 값 소스 | 적용 |
|---|---|---|
| `--seed-fg` | JSON color.[theme].text | 공통 본문·neutral-weak/outline 전경 |
| `--seed-fg-secondary` | F-COLOR fg.secondary | 공통 support·caption·ghost 전경 |
| `--seed-muted` | F-COLOR bg.muted | neutral-weak 면·기본 Progress track·미디어 placeholder |
| `--seed-stroke` | F-COLOR stroke.control | neutral-outline의 식별 경계 |
| `--seed-neutral-solid` / `--seed-on-neutral` | F-COLOR neutral.solid / neutral.onSolid | neutral-solid |
| `--seed-brand` / `--seed-on-brand` | JSON color.[theme].brand / 기존 white 값 `#FFFFFF` | brand-solid, 기본 Progress fill(전경 없음) |
| `--seed-reward` / `--seed-on-reward` | JSON color.[theme].secondaryCTA.bg / .on | reward-secondary |
| `--seed-focus` | F-COLOR focus.ring | 키보드 focus |
| `--seed-disabled-bg` / `--seed-disabled-fg` | F-COLOR disabled.bg / disabled.fg | 공통 disabled 상태. loading과 의미를 합치지 않음 |

선택 이유: 기존 Light secondaryText `#777079`를 Light shell `#F8F7F9`에 사용하면 불투명 sRGB 계산 대비 약4.49:1로 작은 일반 글자의4.5 기준에 못 미친다. 새 공통 보조 글자는 이미 존재하는 F-COLOR fg.secondary `#5E5A65`를 사용하면 같은 배경에서 약6.29:1이다. 기존 JSON 값·레거시 `--sub`를 전역 수정하지 않고 공통 바인딩에서 해결한다. 실제 반투명·이미지 합성은 후속 렌더 검증 대상이다.

팔레트는 기존 semanticPalette의 theme별 foreground/background/accent로 연결한다. 오브젝트의 금속 금색을 Progress·버튼·전체 배경에 전파하지 않는다. 보상 원본의 입체감과 실제 물체 인지를 유지한다. image10의 유료 패스·레이드·음식 보상을 새 기능으로 가져오지 않는다.
