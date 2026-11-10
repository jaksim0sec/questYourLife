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
