---
id: C-TEXT
category: component
status: proposed
read_when: 범용 5단계 텍스트 배치/굵기·줄바꿈
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Text Hierarchy / Typography Roles


> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K03 텍스트 역할과 수치 스케일

**정본 소유자:** 역할·슬롯 매핑은 `components/text-hierarchy.md`, 수치 스케일은 `foundations/typography.md`.

의미 역할은 **Display / Title / Section / Body / Caption**의 5개다. Item과 Meta를 추가 의미 역할로 유지하지 않는다. `type.item`·`type.support`는 기존 수치 스케일 이름으로 존속한다. HTML heading 레벨은 문서 구조로 결정하며 글자 크기와 동일시하지 않는다.

| 실제 슬롯 | 의미 역할 | 사용 스케일 | 웹 어댑터 style |
|---|---|---|---|
| Root 화면명 | Title | type.root | root |
| Standard 화면명 | Title | type.detail | standard |
| 한정된 강조 제목 | Display | type.hero | hero |
| 화면의 핵심 수치 | Display | type.number | number |
| 정보 그룹명 | Section | type.section | section |
| 반복 객체·목록 이름 | Body | type.item | item |
| 본문 | Body | type.body | body |
| 시간·기간·보조 설명 | Caption | type.support | support |
| 짧고 부차적인 상태 라벨 | Caption | type.caption | caption |
| 반복 수치·가격 | Body | type.numberSmall | number-small |

이번 웹 개편은 기존 F-TYPE 스케일을 사용한다: root 26/34·700, detail 18/26·600, hero와 number 24/32·700, section 18/26·700, item과 numberSmall 16/24·600, body 16/24·400, support 14/20·400, caption 12/18·500, button 14/20·600, buttonCompact 12/18·600. 단위는 기본 16 기준 rem이며 실제 RootFont 실험이 적용되면 최종 CSS 크기는 달라진다. 이 선택은 웹 개편의 검수 시작 기준이다.

작은 글자를 더 굵게 만들어 필수 정보를 욱여넣지 않는다. 이름·가격·조건·오류는 읽을 공간을 확보하고 확대 시 높이를 늘린다. 반복 객체 이름은 기본 2줄 공간을 확보하되, 긴 이름을 생략했다면 같은 목적지의 상세나 명시적인 펼침 조작으로 전체 이름을 확인할 수 있어야 한다. 정적 객체에 그런 경로가 없으면 이름 전체를 표시한다. 설명·기간을 Caption이라는 이유로 모두 12로 낮추지 않는다.

## 2026-11-10 · 같은 Typography 계열의 표현 책임 (웹 보완)

[컴포넌트 패밀리 지도](../implementation/component-families.md)의 Typography 표를 사용한다. 이미 K03에 확정된 **5개 의미 역할과 10개 사용 스케일**을 그대로 보존한다. `data-text-role`은 의미, `data-text-style`은 해당 스케일, HTML heading은 문서 구조다. 이 셋을 자동으로 동일시하지 않는다.

공통 CSS의 `.seed-text`는 승인된 F-TYPE의 26/34·18/26·24/32·16/24·14/20·12/18을 실제 스타일에 대응시킨다. `data-text-weight`는 선택적 강조 축(숫자/크기 변경 없음), `data-text-tone=secondary`는 보조 전경 축이다. 별도 폰트나 새로운 Text 계층을 만들지 않는다. 실험용 Hero 남발은 정본의 사례가 될 수 없다.

새 패밀리 속성은 표현 단위의 연결·사용법 보완이며 D-012의 기존 시각 규격을 바꾸는 승인이 아니다. 기존 `data-text-style=strong`은 호환용 별칭으로 남긴다.

## S01 · 일반 아이콘 옆 텍스트의 의미 역할 (C-SVG 조합 보완)

**버튼 바깥의 아이콘+텍스트**는 별도 Typography 타입이 아니라 현재 **Body/Caption 5역할** 및 `item/body/support/caption` 사용 스케일을 목적대로 선택한 결과다. 정확한 아이콘 20/16 출력과 `inline.gap=8`, 한 줄/여러 줄 광학 정렬, 레이블·후행 토글의 분리는 [C-SVG S01](svg-icon.md)이 소유한다. C-TEXT는 오직 **읽는 위계/weight/줄바꿈**을 선택한다.

| 소비 맥락 | 텍스트 기준 | 위계·상태 |
|---|---|---|
| `☾ 다크 모드 [토글]`의 짧은 설정명 | 주요 설정 이름이면 **Body + item(16/24·600)** 또는 일반 Body(16/24·400). 주변 항목과 중요도를 일관되게 결정 | 토글 상태는 텍스트 `weight`만으로 표현하지 않고 실제 컨트롤 상태로 알림 |
| `ⓘ 계정 정보`의 정적 레이블 또는 실제 링크 | 메뉴에서 반복 객체 이름이면 **Body + item**, 설명/보조 텍스트는 의미에 맞게 **Body + body** 또는 **Caption + support** | 실제 이동인 경우 링크이고, 읽기 전용이면 가짜 클릭·눌림 효과 없음 |
| 한글 두세 줄 + 오른쪽 독립 Action | **주 정보 제목의 가독성 보존**, 부가 정보만 Support로 보완 | 가변 행 높이·후행 조작 재배치. 이름/상태를 Caption으로 압축하지 않음 |

본문의 기본 `font-weight=400`, item `600`, support `400`은 현행 F-TYPE의 **텍스트 역할**을 따른다. 일반 아이콘을 굵게 보이게 하려고 글자를 모두 `700`으로 올리거나, 레이블의 weight 때문에 원본 SVG `stroke-width=1.8`을 수정하지 않는다. 다크/라이트 모두 기존 의미색 대비와 타이포 구분을 확인한다.

**적용 경계:** 일반 정보 행에서는 `C-SVG + C-TEXT + C-LIST`가 관계를 조합한다. **실제 버튼 내부** 아이콘·Label의 16–18·6–8 간격·로딩 시 중심 유지는 기존 [P-BTN-ICON](../patterns/button-icon-layout.md)과 C-ACTION이 소유한다. D-015 Root/Standard Header 수치, C-BOTTOM, C-ICON의 48px 타깃도 그대로. `rem/em`과 Root Font 상속 구조를 새로 설계하는 과제는 **U09**로 넘기며, 글자를 바꿨다는 이유로 전역 CSS를 수정하지 않는다.

## D-015 · 헤더 전용 표현과 일반 Title 역할 분리

Root·Standard 화면명은 의미상 여전히 `Title`이다. 다만 **Top Navigation 컴포넌트가 소유하는 승인 웹 시각값**은 Root `1.30rem / 650`, Standard `1.08rem / 620`, Pretendard Variable 우선이며 범용 텍스트 스타일의 `26/34·700`, `18/26·600`을 일괄 바꿔서는 안 된다. 일반 화면 본문·Section·Hero·리스트의 기존 F-TYPE/이 컴포넌트 규칙은 유지한다. [NAV-TOP](../../TOP_NAVIGATION.md)이 폰트·자간·Glyph 크기·정렬 수치의 유일한 헤더 정본이다.
