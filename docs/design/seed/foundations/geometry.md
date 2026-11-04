---
id: F-GEOMETRY
category: foundation
status: proposed
read_when: 패딩·크기·라운딩·콘텐츠 폭·터치영역·안전영역
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-004, REF-005, REF-006]
---

# 간격·크기·형태

## 제안 토큰 정본
단위는 논리 단위다. 웹 간격은 16 기준 rem, Android는 dp로 옮겨 시각 비교한다. 물리 px나 스크린샷 측정값이 아니다.

| Token | 값 | 용도 |
|---|---:|---|
| space.1 / .2 / .3 / .4 / .5 / .6 / .8 | 4 / 8 / 12 / 16 / 20 / 24 / 32 | 공통 간격 스케일 |
| page.inset | 20 | 기본 좌우 여백 |
| page.insetCompact | 16 | 360 미만 폭에서 공통 전환 |
| content.maxWidth | 560 | 단일 모바일 읽기 열, 넓으면 중앙 정렬 |
| section.gap | 24 | 큰 그룹 사이 |
| group.gap | 16 | 관련된 하위 블록 사이 |
| item.gap | 12 | 목록·카드 사이 |
| inline.gap | 8 | 짧은 아이콘·라벨 사이 |
| surface.padding | 16 | 기본 그룹 내부 |
| row.paddingY / .paddingX | 12 / 16 | 목록 내부 |
| radius.control | 14 | 일반 버튼·입력 |
| radius.surface | 18 | 카드·그룹 면 |
| radius.image | 14 | 콘텐츠 썸네일 |
| radius.overlay | 26 | 시트·다이얼로그 |
| radius.pill | 999 | 의미상 칩·세그먼트 thumb |
| stroke.default | 1 | 경계 |
| stroke.selected | 2 | 선택 경계 |
| size.actionSmall / .actionMedium / .actionLarge | 32 / 40 / 48 | 기본 글자 크기에서 보이는 최소 버튼 높이 |
| size.touchMin | 48 | 클릭·터치 영역 최소 |
| size.rowMin | 56 | 목록 행의 최소 높이 |
| size.topBarMin / .bottomBarMin | 56 / 56 | OS inset 제외한 상·하단 content 최소 높이 |
| action.paddingXSmall / .paddingXMedium / .paddingXLarge | 12 / 16 / 20 | 버튼 size별 좌우 padding |
| action.paddingYSmall / .paddingYMedium / .paddingYLarge | 7 / 9 / 13 | 버튼 size별 상하 padding, 줄높이를 줄인 만큼 여백 확보; 버튼 높이는 유지 |
| segment.paddingX / .trackHeight | 12 / 36 | 세그먼트 좌우 padding·보이는 기본 높이 |
| badge.paddingX / .paddingY | 8 / 4 | 배지 내부 padding |
| field.paddingX / .paddingY | 16 / 12 | 입력 내부 padding |
| progress.height | 8 | 기본 진행 막대 높이 |
| size.iconSmall / .iconDefault / .iconNav | 16 / 20 / 24 | 일반 UI 아이콘 슬롯, 버튼 내부와 구분 |
| size.iconButtonContent / .iconButtonCompact | 18 / 14 | 텍스트 버튼 내부 아이콘, 일반/작은 size |
| size.avatarRow / .avatarProfile / .avatarHeroMax | 40 / 80 / 120 | 행 / 일반 프로필 / 명시적 몰입 영역 상한 |

## 적용 규칙
같은 size는 tone·treatment가 바뀌어도 높이·padding·radius가 같다. radius는 의미별 family 토큰을 공유하며 같은 숫자를 모든 모양에 강제하지 않는다. 버튼과 카드 내부 패딩을 개별 화면에서 임의로 줄이지 않는다.

최소 높이는 글자 확대 시 증가할 수 있다. 32·40 높이 컨트롤은 주변을 포함한 비중첩 48 터치영역을 확보한다. 공간이 부족하면 큰 size/줄바꿈 배치로 바꾸며 투명 hitbox를 겹치지 않는다. 작은 버튼을 빽빽하게 놓고 모두 48 target이라고 주장하지 않는다.

모든 콘텐츠의 시작선은 page.inset 또는 같은 surface.padding에 맞춘다. 제목·아이콘·숫자의 실제 보이는 경계는 광학적으로 미세 보정할 수 있으나 로컬 padding 토큰을 늘리지 않는다.

## 반응형·안전영역
320/360/390/430 논리 폭과 200% 글자를 검토 기준으로 제안한다. 고정 열 개수를 이미지에서 가져오지 않는다. 2차원 에셋은 contain/crop 역할을 명시하고 텍스트는 폭과 함께 재흐름시킨다. 가로 목록은 넘치는 단서를 남기고 잘린 버튼을 주 조작으로 쓰지 않는다.

OS safe inset은 페이지 spacing과 별개다. 아래 inset·키보드·하단바는 S-PAGE가 한 번만 합산한다. iPhone 프레임·노치·OS 시계를 앱의 padding으로 추출하지 않는다.

## 상태·근거·영향
Pressed/selected/loading에서도 외곽 크기를 유지한다. 긴 라벨·오류·확대에서는 높이 증가를 허용한다. 둥근 면의 반복은 관찰 high, 숫자·폭 전환 기준은 proposed이며 실기기 미검증이다. 전역 변경 시 버튼·목록·시트·하단 고정 영역을 같이 확인한다.

