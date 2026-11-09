---
id: O-PERSON-ACTIVITY
category: object
status: proposed
read_when: 멤버·활동·응원 목록
depends_on: [O-README, C-AVATAR, C-LIST, C-TEXT]
references: [D-007, D-013]
---

# O-PERSON-ACTIVITY · 사람 중심의 활동 행

## 현행 계약

| 슬롯 | 책임 |
|---|---|
| Avatar | 멤버를 식별하는 마스코트/아바타 |
| Identity | 사람 이름 |
| Activity Meta | 동의된 활동 정보, 상태·내용 |
| Time | 공유된 활동의 실제 시간, Caption |
| Cheer | 가능한 응원 1개, 독립 행동 |

**목록 전용**이다. 사람·활동에는 물체 Grid/행별 Surface 그림자/아이템 희소도 문법을 쓰지 않는다. 1줄 메타면 Avatar와 텍스트는 **center**, 여러 줄이면 **top**. Identity가 먼저 읽히고 활동 시간은 뒤에 표시된다.

대표 행 링크는 1개이고 그 옆 응원은 최대 1개 **형제 버튼**이다. 링크 내부에 버튼 넣기 금지. 실제 소셜 공유 승인 없이 상세 사생활 내용을 임의 표시하지 않는다.

Avatar Stack의 순서는 데이터 순서를 따른다. **`+N = 총원 - 표시원`**, 전체 인원과 예시 일부가 혼동되지 않게 라벨·스크린리더 순서를 지정한다.

**반례 (보고서 6.5):** 사람·활동에 상점의 3열 물체 타일·그림자 문법을 적용하면 이름/활동 메타/시간/응원의 관계가 무너진다. 응원을 nested anchor 안에 넣거나 `+N`을 잘못 표시한 경우에도 불합격.

## 이력

- 기존 사람과 활동의 임의 아바타 나열을 정보 객체로 승격해 O-ITEM과 구분했다.
