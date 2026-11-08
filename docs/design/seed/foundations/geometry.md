---
id: F-GEOMETRY
category: foundation
status: proposed
read_when: 패딩·크기·라운딩·콘텐츠 폭·터치영역·안전영역
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-004, REF-005, REF-006]
---

# F-GEOMETRY · 기준선과 최소 조작

## 현행 계약

- `page.inset`은 Header/본문이 공유하는 **단일 gutter 토큰**이다. CSS web preview 구현 이름은 `--page-inset`. px 임의 padding 추가 금지. 기존 r1에서 360px 미만 1rem, 이상 1.25rem 제안했지만 P0 웹에서는 1rem으로 우선 연결되어 있다. 최종 임계·값은 P2/P3 실측 확인 전 **candidate**이며 동일 페이지에서 토큰만은 같아야 한다.
- **최소 Hit Target 48×48px(웹), 48dp(Android)**, 비중첩. 표시 버튼 높이 34/43/48과 실제 클릭 면적은 별도. 44px로 승인 없이 하향 금지.
- Header Glyph 24px, 버튼 Target 최소48px, 두 중심의 가로·세로 차이 **1px 이하**. Glyph를 `translateX`로 옮기는 꼼수 금지.
- 글자 **200% 확대에서 Header / Tabs / Bottom Nav 라벨 고정 높이 금지**, 줄바꿈·필요한 행 높이 증가 허용. 사용자가 입력한 이름/상태가 잘리거나 가로 overflow 발생하면 실패.
- Radius는 공통 의미 Variant로 결정하고 지역 자의적 16/6/10 병기 금지. OS safe-area/키보드 inset은 S-PAGE 소유, gutter와 별도.
- Header와 본문 외곽 max-width가 서로 다른 경우에도 실제 왼쪽 시작선이 같은지 확인한다. 헤더 높이 58.8/60/56 비교, Bottom 65.2+safe/76 비교는 P2 후보 기록이다.

### P2 높이·기준선 후보 판정 입력
- Header 58.8(기존 실측) / 60(제안) / 56(외부 비교)를 상단 타깃과 title/gutter 기준선 관점에서 병치. 채택 전까지 높이 제안은 candidate.
- Radius 16/6/10은 의미 있는 Surface/Inset/Row의 동심 관계에 한한 candidate, 무작위 지역 radius 추가 허가가 아니다.
- 48px 목표와 200% 동적 행 높이는 승인된 필수 기준이며 비교 후 축소할 수 없다.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
