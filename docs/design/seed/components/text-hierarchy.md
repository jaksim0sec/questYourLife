---
id: C-TEXT
category: component
status: proposed
read_when: 범용 5단계 텍스트 배치/굵기·줄바꿈
depends_on: [F-TYPE, F-GEOMETRY, F-COLOR]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# C-TEXT · 의미 역할과 슬롯

## 현행 계약

의미 역할은 **Display / Title / Section / Body / Caption** 다섯 개뿐이다. **Item/Meta는 의미 역할이 아니라 F-TYPE 숫자 스케일 이름**으로만 남긴다. HTML heading 레벨은 페이지 시맨틱 구조이며 글자 크기와 동일하지 않다.

| 슬롯 | 역할 | F-TYPE scale |
|---|---|---|
| Root 화면명 | Title | root |
| Standard 화면명 | Title | detail |
| 한정된 강조 제목 | Display | hero |
| 대표 기록 수치 | Display | number |
| 섹션 제목 | Section | section |
| 반복 객체·목록 이름 | Body | item |
| 본문 | Body | body |
| 시간·기간·보조 설명 | Caption | support |
| 부차적인 실제 상태 | Caption | caption |
| 반복 수치·가격 | Body | numberSmall |

가격·조건·오류·이름을 모두 Caption으로 내리지 않는다. 반복 아이템의 Identity는 기본 두 줄, 전체 이름에 접근 가능한 대체 경로 없이 잘라내지 않는다. **반례:** `620/1000`보다 `완료까지 380`이 강하면 O-PROGRESS 대표 수치 주연 실패. 실제 렌더 최소 크기·굵기는 F-TYPE의 단일 규범을 참조한다.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
