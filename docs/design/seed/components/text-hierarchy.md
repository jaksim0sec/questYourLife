---
id: C-TEXT
category: component
status: proposed
read_when: 범용 5단계 텍스트 배치/굵기·줄바꿈
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Text Hierarchy / Typography Roles

## Anatomy / Roles
다른 페이지에서도 같은 의미가 같은 시각 계층을 갖도록 **5개 역할**을 제공. 별도 폰트 가족을 만들지 않으며 F-TYPE에서 값을 소유한다.

| Role | v0.6 Preview 후보 | 쓰임 |
|---|---|---|
| Display | 22–24sp / 800–850 | 페이지의 가장 중요한 목표·성취 |
| Section | 17–18sp / 750–800 | 목록·콘텐츠 그룹 제목 |
| Item | 14–16sp / 650–700 | 반복 카드/리스트 제목 |
| Body | 13–16sp / 400–500 | 설명·행동 맥락 |
| Meta | 11–13sp / 450–600 | 시간·단위·날짜·보조 정보 |

## Usage
화면 한 영역의 주목도는 Display 한 개 또는 비슷한 높이의 Hero Art 한 개가 선도. Icon Button의 glyph는 인접 title보다 더 큰 인지적 시선을 유도하지 않게 맞춘다. 버튼 label은 이 5단과 별도 Action Button size token을 따름.

## States / Accessibility
Loading에도 예상 역할 보존, Error/Success 색 변경만으로 의미를 넘기지 않음. 200% 확대에서는 줄높이·컨테이너 성장 허용. 청소년용 UI에서도 의미 없는 보조 인사말에 Display 금지. Heading semantics와 실제 구조 일치.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
