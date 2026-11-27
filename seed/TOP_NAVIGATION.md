# 갓생패치 · Top Navigation 시각 기준

**Active demo: D-019 applies only to Standard headers.** Global pages retain the expressive Root header with large left-aligned title and clear outline actions. Global Inner and Non-Global share the centered title and circular neutral buttons. Earlier numeric approvals below remain historical. 웹 시연은 유물이며, 이 명세는 재사용할 디자인 계약이다. [이전 상세·검증 이력](https://github.com/jaksim0sec/questYourLife/blob/07bdd23ac0da46033b45508767fa5e62643a1931/seed/TOP_NAVIGATION.md).

## Root / Standard

| 역할 | 배치 | 제목 |
|---|---|---|
| Root | 좌측 제목, 우측 맥락별 Outline 조작 | 현재 최상위 목적지 |
| Standard | 좌측 Back/Close, 중앙 제목(D-019), 우측 선택적 조작·정보 | 상세·하위 화면 |

Back은 이전 화면, Close는 독립 흐름/레이어 닫기다. 제목을 선택 버튼으로 표현할 때 실제 기능과 표시가 있어야 한다. 검색·알림·메뉴는 필요한 것만 둔다. Header는 본문과 자연스럽게 이어지는 중립 표면이며 글래스·새 배경색을 강제하지 않는다. Global Inner/Non-Global의 하단바 의미는 [S-PAGE](shells/page-shells.md), Sheet는 [C-SHEET](components/bottom-sheet.md)를 따른다.

## D-015 승인 기본값 (2026-11-12 보정 포함)

| 항목 | Root | Standard |
|---|---|---|
| Font | Pretendard Variable 우선, 한글 시스템 폴백 | 동일 |
| 크기 / 굵기 | 1.30rem / 680 | 1.08rem / 650 |
| 자간 | -.025em | -.018em |
| 최소 높이 | 3.5rem | 3.5rem |
| Glyph / target | 1.59rem / 3rem(48px 이상) | 동일 |

Root 줄높이 1.35. 좌우 gutter는 360 CSS px 미만 16px, 이상 20px 기준이며 본문과 맞춘다. 24 viewBox/1.8 round stroke 원본을 보호한다. Glyph와 target 중심을 맞추고 외곽의 공통 optical inset `(target-glyph)/2`만 적용(좁은 화면에서 gutter 이하); 아이콘별 이동 금지. 확대 시 행이 자라게 한다. Pretendard CDN 로딩은 보장되지 않으며 미래 앱의 실제 font는 별도 검증한다. 범용 Title 스케일과 NAV-TOP 수치를 혼동하지 않는다.

## S07 / D-017 Standard 슬롯

Center와 Start는 동일 Header의 배치 선택이며 화면 작성자가 명시한다(자동 전환·새 Page Type 없음). 기존 웹 어댑터의 `data-title-align="start"`와 기본 center가 참고다. Start의 제목은 Back 타깃 끝에서 .125rem 뒤, 열 간격 .5rem. Back 타깃 3rem과 광학 보정은 유지한다. 우측은 실제 Action, 짧은 읽기 전용 정보(C-CREDIT), 또는 비움 중 선택한다. 비조작 정보는 가짜 버튼처럼 꾸미지 않으며 빈 슬롯은 공간을 불필요하게 예약하지 않는다. 긴 제목/큰 잔액/200% 확대에서는 줄바꿈이나 정보의 본문 이동으로 대응한다. 좁은 상점은 본문에 전체 잔액을 한 번 표시한다. 숫자 잘림·타깃/글자 축소 금지. 채팅의 Middle 아바타/신원은 P-HIERARCHY의 역할이며 새 헤더 유형이 아니다.

알림 아이콘은 기존 웹 승인 `assets/icons/preview/bell-connected.svg`를 참고한다. Soft 56종의 공식 bell 및 SVG 원본은 변경하지 않는다.

## Active Root / Standard · D-019 correction

Global uses the expressive Root: H76, left-aligned 28/700 title (26 below 360), leading 1.22 and tracking -.04em. Clear outline actions remain unfilled, with a 1.75rem (28px at the base font size) glyph and 3rem target. No Back on a root. Root review: 30→28 title, 800→700 weight, 25.44→28 glyph box and 2.1 outline stroke candidate. The Root bell uses a derived variant with unchanged paths; original approved SVG stays intact. Apply the common outer optical inset (10px at the base font size) to the trailing action slot, retaining the full 48px target and page containment. Standard and bottom navigation are unchanged.

Global Inner and Non-Global use the reference Standard header: centered title 18/750, leading 1.3, H76 minimum, neutral circular 48 minimum actions with 26 glyph. Keep balanced empty slots where appropriate; do not fabricate actions. Start alignment is historical for the active demo. Shared gutter 16/20. Bottom Sheet retains its own header. Bottom navigation is unchanged. [Tokens](implementation/v015-tokens.json) → `refinements.navigation`.
