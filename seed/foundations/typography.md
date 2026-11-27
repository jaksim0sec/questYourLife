---
id: F-TYPE
category: foundation
status: proposed
read_when: 제목·본문·숫자 위계, 한글 줄바꿈, 글자 확대
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-002, REF-004, REF-005]
---

# 타이포그래피와 정보 위계

크기·굵기·명도·여백으로 한 화면의 중심을 정한다. 강한 제목과 수치, 낮은 보조 위계가 기본이며 아트가 중심이면 글자를 낮춘다. [C-TEXT](../components/text-hierarchy.md)는 의미 역할, 이 문서는 수치 스케일을 소유한다. Header는 [NAV-TOP](../TOP_NAVIGATION.md) 승인 기준을 별도로 사용한다.

## 기준 스케일 (논리 크기 / 줄높이 / Weight)

| Token | 값 | 역할 |
|---|---|---|
| type.root | 26 / 34 / 700 | 범용 최상위 제목, NAV-TOP 제외 |
| type.detail | 18 / 26 / 600 | 범용 상세 제목 |
| type.hero | 24 / 32 / 700 | 한정된 강조 제목 |
| type.section | 18 / 26 / 700 | 섹션 제목 |
| type.item | 16 / 24 / 600 | 목록·카드 이름 |
| type.body | 16 / 24 / 400 | 본문 |
| type.support | 14 / 20 / 400 | 보조 설명·시간·단위 |
| type.caption | 12 / 18 / 500 | 짧고 부차적인 라벨 |
| type.button | 14 / 20 / 600 | 기본·큰 버튼 |
| type.buttonCompact | 12 / 18 / 600 | 작은 버튼 |
| type.number | 24 / 32 / 700 | 핵심 수치 |
| type.numberSmall | 16 / 24 / 600 | 반복 수치·가격 |

수치는 기존 제안/r1 시작 기준이다. 서체는 가독성·실제 글리프 폭으로 판단하고 사용자 글자 크기를 존중한다. 웹의 16px 기준 rem과 Android sp는 플랫폼 대응 참고이지 물리 px 매핑이 아니다.

## 개편 표현 · UI-001 (제안)

[후보 수치](../implementation/v015-tokens.json)의 `refinements`. Section 20/1.3/740, Hero·Number 28/1.25/780, Support 줄높이 1.55(크기·weight 유지). 제목·숫자 자간은 후보 토큰대로 절제한다. 모든 글자를 굵게 만들지 않으며 본문·버튼·Header 기본 스케일을 덮어쓰지 않는다.

## 사용과 접근성

- 핵심 제목·숫자·아트가 서로 경쟁하지 않게 하고 보조 인사말은 item/body. 마스코트 신원은 P-HIERARCHY의 표현과 조합한다. 가격·보유량·획득량을 구별하고 수치 비교에는 tabular 숫자, 단위는 support 이상과 기준선 정렬.
- 한글 어절을 보존하되 좁으면 비상 줄바꿈. 목록 제목 기본 2줄, 전체 정보 확인 경로가 없으면 생략 금지. 중요한 날짜·오류를 작은 caption으로 숨기지 않는다. 긴 배지·버튼은 글자 축소보다 행 증가/그룹 재배치를 택한다.
- 200% 확대, 스크린리더 제목 순서, 로딩 시 슬롯·의미 유지, 색·굵기 외 선택 상태 표시를 고려한다. 이미지의 장식 글씨를 실제 UI로 복제하지 않는다.

웹 Root Font trial과 이전 D-015 수치 이력은 [이전 상세·검증 이력](https://github.com/jaksim0sec/questYourLife/blob/07bdd23ac0da46033b45508767fa5e62643a1931/seed/foundations/typography.md) 및 [v0.15 토큰](../implementation/v015-tokens.json)의 `rootFontSize`에 보존한다. 새 앱에 자동 적용하지 않는다.
