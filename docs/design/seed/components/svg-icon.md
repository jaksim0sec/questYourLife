---
id: C-SVG
category: component
status: proposed
read_when: 기본 Outline·활성 Fill SVG와 크기·텍스트 비례 검수
depends_on: [F-ICON-MOTION, F-GEOMETRY, F-TYPE, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# SVG Icon / Optical Contract

## Purpose
UI 픽토그램의 **한 군(群)의 도형 규격**, outline과 fill의 독립 자산 경로, 사용 맥락별 출력 크기를 정의. 보상 코인·마스코트·3D 소품은 C-SVG가 아님.

## Anatomy / Properties
`name`, `mode:outline|fill`, `size:badge|inline|row|header|bottom`, `semanticColor`, `decorative`. 현행 **56개 사용자 제공 아이콘**은 모두 `viewBox=0 0 24 24`, 기본 `fill=none` / `stroke=currentColor` / **`stroke-width=1.8`** / `stroke-linecap=round` / `stroke-linejoin=round`이다. 이전 1.95/2.15 후보는 이 자산군에 적용하지 않는다. Fill 자산은 별도의 `*-fill` 경로로 작성하고 기존 outline에 단순 CSS 채우기를 강제하지 않는다.

## Context / Output Size
Badge 12–14, Inline Button 16–18, Row 20, Top 24, Bottom 27dp (같은 24 viewBox). 실제 도형의 optical bounding box·기준선·획 면적이 모든 픽토그램에서 가까워야 한다. Icon Button hit target은 48dp. 하단 inactive는 Outline, active는 Fill 후보로 검수; 실제 탭 목적지는 O-003 미결. 하단 외 페이지의 모든 selected에 Fill을 강제하지 않는다.

## SVG Shape Rules
3개의 햄버거 가로선은 사용자 제공 원본의 y=6/12/18에 맞춘다; Home 외곽은 둥근 코너/지붕, 기호 의미가 사라질 정도의 젤리형 단순화 금지. 검색/알림/메뉴/홈/체크/DM/클랜/마이 8개를 동일 24그리드에 배열해 판단.

## States & Accessibility
Default/Active/Disabled/Pressed/Focused. 장식 SVG는 읽기에서 숨기고, 단독 클릭 버튼에는 행동 기반 accessible label. 활성은 색 외에도 Fill/weight/label로 구분. 스크린리더/키보드/광학적 중심/다크 대비·200% 검수.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.

## v0.13 SVG source (proposed)

[37개 실제 SVG 파일](../assets/icons/v0.13/README.md)을 제공한다. v0.13 `home/users`의 Outline·Fill은 각각 같은 지붕/인물 실루엣을 공유한다. 모든 아이콘을 같은 모양으로 만들기보다 24×24 그리드에서 시각적 크기·여백을 개별 보정. 기존 앱의 확정 나침반 클랜 네비는 **변경하지 않음**. `stroke-width=2.0`은 해당 아이콘 소스의 후보값이며 기존 Android 자산의 확정치와 혼동하지 않는다.

## v0.14 user-supplied authoritative icon source (proposed)

[실제 SVG 56개](../../../../assets/icons/soft/v0.14/README.md) = Core Outline 32 + Fill 5 + Additional Outline 19. 사용자 제공 ZIP 원본을 **바이트 단위 그대로** 사용한다. `viewBox=0 0 24 24`, 기본 `stroke-width=1.8`, 둥근 cap/join, `currentColor`. 이 단락이 이전의 v0.6 `1.95` 후보 및 과거 v0.14 `2.15` 실험값보다 **본 아이콘 세트에 한해서** 우선한다. 이름/경로는 유지하고 별도 Fill이 없는 아이콘은 임의로 채우지 않는다. `check-circle-fill`의 mask는 Android 변환 시 검증 필수. `main`의 승인된 클랜 나침반 아이콘을 `users`로 자동 교체하지 않는다.

## v0.15 최종 채택

정본 [Soft SVG 56종](../../../../assets/icons/soft/v0.14/README.md) · [name/Fill manifest](../../../../assets/icons/soft/v0.14/manifest.json) · [적용·검증](../implementation/v015.md). 32개 기본 Outline + 5개 Fill + 19개 추가 Outline. 다섯 Fill은 `home, chat, users, user, check-circle`이며 별도 파일로 상태 변경. `check-circle-fill`의 `mask`는 Android VectorDrawable에 바로 복사 불가; 실제 색/negative space를 유지하는 대응 구현과 렌더 검증 필수.

## D-015 · 웹 하단 네비게이션 추가 원본

공식 Soft SVG 56종(원본 SHA·manifest)은 그대로이다. 사용자 승인된 웹 시연은 `assets/icons/preview/`의 독립 파일 `clan-bookmark-fill.svg`, `me-smile.svg`, `me-smile-fill.svg`를 **C-BOTTOM에서 지정한 일부 탭 슬롯에만** 사용한다. 파일 경로의 `preview`는 현행 원본 위치이며 임시 A/B 디자인 상태를 의미하지 않는다.
세 파일 모두 24 viewBox의 단독 경로이고 활성 Fill은 Outline에 CSS fill을 강제한 것이 아니라 별도 SVG다. 하단의 active/outline 상태·스크린리더 라벨은 [C-BOTTOM](bottom-navigation.md) 소유, 원본 파일의 등록·사용처는 `docs/assets/README.md`가 소유한다.

## 2026-11-10 · 마이 Smile 활성 SVG 광학적 크기 교정

`assets/icons/preview/me-smile-fill.svg`는 비활성 `me-smile.svg`에 CSS `fill`을 적용한 것이 아니라 별도로 그린 **독립적인 Fill 경로**다. 이전 활성 경로는 바깥쪽 획을 고려하지 않은 반지름 `9.2`의 면만 사용해, Outline의 `r=9.2`에 포함된 양쪽 `stroke=1.8`보다 바깥 실루엣이 작아졌다. 얼굴 입도 지나치게 얇은 면으로 구성돼 표시 상태 변경 시 표정이 달라졌다.
수정된 활성형은 외곽 반지름 `10.1 (= 9.2 + 1.8/2)`의 면과 눈·웃는 입의 **evenodd 투명 컷아웃**을 한 독립 SVG 경로에 명시한다. 바깥 실루엣은 비활성과 같고, 얼굴 음각 형태를 CSS mask에서도 유지한다. 두 상태 모두 viewBox `0 0 24 24`, 외부 출력 크기와 버튼/라벨 위치는 불변.
비슷한 Fill 자산을 추가할 때는 좌표 숫자만 같다고 가정하지 말고 Outline **획을 포함한 실제 도형 외곽**과 얼굴/기호의 내부 여백까지 대조한다. 공식 Soft 56종 원본·탭 매핑·다른 화면은 수정하지 않는다.

## S01 · 버튼 바깥 일반 UI 아이콘+텍스트 조합 (새 컴포넌트 아님)

**적용 범위:** 메뉴·설정·읽기 전용 행·상세 메타 등에서 `SVG 아이콘 + 텍스트`가 **정보의 한 묶음**으로 읽히는 경우. 아이콘 자체의 그림체/경로를 재정의하지 않으며 [C-TEXT](text-hierarchy.md)가 텍스트 역할을, [F-GEOMETRY](../foundations/geometry.md)가 **기존 출력 크기·간격**을 소유한다. **버튼 내부** 아이콘+Label은 [P-BTN-ICON](../patterns/button-icon-layout.md) 및 C-ACTION의 기존 16–18 아이콘·6–8 간격·Loading/Pressed 규칙을 우선한다. 새 일반 UI 규칙으로 버튼 안쪽을 덮어쓰지 않는다.

### 기존 토큰으로 조합 판단

| 일반 UI 상황 | 기존 크기·간격 출발점 | 텍스트/정렬 판단 | 실제 조작 |
|---|---|---|---|
| 한 줄짜리 일반 정보 · 설정 레이블 | `size.iconDefault = 20` · 같은 의미 묶음 안 `inline.gap = 8` | C-TEXT의 Body/Item 중 **실제 정보 역할**을 선택. 아이콘과 첫 글줄을 시각적으로 가운데 정렬 | 읽기 전용이면 조작 affordance 없음 |
| 작은 보조 정보·짧은 메타 | 필요하면 기존 `size.iconSmall = 16` · `inline.gap = 8` | C-TEXT Support/Caption이 적합한지 먼저 확인. 읽기 필요한 핵심 라벨을 Caption으로 억지 축소하지 않음 | 정보 자체를 가짜 Button으로 감싸지 않음 |
| 여러 줄 설명이 딸린 설정/목록 행 | C-LIST Prefix/Content/Suffix의 **행 슬롯 간 item.gap=12**와 아이콘+짧은 Label **내부 inline.gap=8**은 서로 다른 간격 | 첫 텍스트 줄의 광학적 중심에 아이콘을 맞추고 본문은 자연 줄바꿈. 전체 행의 수직 중앙에 아이콘을 박아 첫 줄과 분리하지 않음 | 정적 행과 전체 이동/독립 후행 Action을 C-LIST 계약으로 구별 |
| 행 전체가 실제 버튼/링크 | 독립 일반 정보 규칙 **대신** 기존 C-ACTION/P-BTN-ICON 또는 C-LIST의 actionable/navigate | 아이콘·라벨 한 그룹의 중심, 누름/로딩 전후 정렬·라벨 유지 | **실제 target 최소 48**·키보드·명확한 이름; 부모 클릭 영역+자식 Button 중첩 금지 |

아래 `☾`, `ⓘ`는 원본 S01 손그림의 **의미를 나타낸 도식**이지 새로운 SVG 소스·이모지 자산 지시가 아니다.

```text
설정 레이블 + 독립 컨트롤             정보 또는 탐색 행
[☾] 다크 모드       [토글]          [ⓘ] 계정 정보
 └─ Prefix + Content + Suffix ─┘      └─ Prefix + Content
```

- **다크 모드:** 아이콘과 `다크 모드`는 설정 **레이블/정보**이며 후행 토글은 실제 독립 조작이다. 장식 아이콘은 접근성 트리에서 숨기고 레이블·토글 상태를 정확히 연결한다. 반대로 기존 `web/preview/app.js`의 테마 메뉴는 **행 전체가 실제 `button.menu-row`인 기존 시연**이므로 일반 정적 레이블 사례로 잘못 분류하지 않는다. 그 소비자를 시각 조정한다면 먼저 버튼/C-LIST 상호작용 계약과 회귀를 확인한다.
- **계정 정보:** 단순 표시라면 정적 레이블, 탭으로 상세 이동한다면 C-LIST `navigate`의 실제 링크/키보드 target이다. Chevron·`cursor:pointer`·Pressed는 진입이 있을 때만. 같은 아이콘+텍스트 구성이더라도 **행동 유무가 역할을 결정**한다.
- **아이콘과 글씨의 비율:** 아이콘만 크게 키우거나 Label을 작게 줄여 존재감을 맞추지 않는다. `type.item` 16/24·600(명료한 반복 객체명), `type.body` 16/24·400(일반 정보), `type.support` 14/20·400(부가 설명)처럼 **기존 글자 역할에 맞추고** 20/16 SVG 출력 크기와 `inline.gap`을 조합한다. `font-weight`는 텍스트 중요도에 따른 속성이며 SVG 자체의 `stroke-width=1.8`을 글씨 굵기에 맞춰 바꾸지 않는다.
- **광학적 정렬:** 단일 줄은 아이콘 박스와 레이블의 보이는 줄높이/중심이 맞도록, 여러 줄은 **첫 줄 기준**으로 놓는다. 실제 glyph 획의 optical box/한글 baseline을 검증하고 아이콘마다 CSS `translateY`·margin·고정 높이를 다르게 만드는 방식으로 맞추지 않는다. 읽기 순서는 **Prefix → Label/설명 → 독립 Suffix 조작·값**으로 유지한다.
- **확대·긴 이름:** 폭 320/360/390/430, 글자 100%·200%, 두세 줄 이상의 긴 한글·후행 토글 유무에서 레이블이 주행동을 밀거나 잘리지 않아야 한다. `min-width:0`, 높이 유동·본문 줄바꿈/후행 컨트롤 재배치를 활용하고, 글씨/터치 target 강제 축소나 임의 한 줄 말줄임으로 해결하지 않는다. RTL 언어를 지원한다는 가정을 만들지 않으며 현재 한글 다중행 검증을 우선한다.
- **소유권/검증 경계:** 웹에서는 이미 `.seed-icon`이 기본 `20px` 출력, `.seed-text`가 글자 역할, `.seed-list__item`이 행 관계를 소유한다. **새 CSS 클래스·SVG 파일·Variant·Font token을 만들지 않는다.** U08은 이 기존 결합의 **사용 의미 보완**이며, 반복 소비자가 동일한 실패를 낳는 실제 증거 없이 공통 CSS의 간격을 바꾸지 않는다. `rem/em/vw`와 Root Font 연결·단위 전환 여부는 **U09(S02a)**의 소유권이다. 실제 화면 200% 픽셀 확인과 버튼·행 클릭 회귀는 별도다.

### 웹 알림 Bell 예외 (2026-11-10)

헤더 알림은 사용자 피드백에 따라 `assets/icons/preview/bell-connected.svg`로 교체했다. 종 하단의 독립된 추 곡선이 몸체에서 분리되지 않도록 양 끝을 본체 아래의 밑선 `y=17.5`에 연결한다. 공식 Soft 56개 파일에는 변경 없이, 사용처는 [NAV-TOP](../../TOP_NAVIGATION.md)에 기록한다.
