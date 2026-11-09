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


C-TEXT는 **Display / Title / Section / Body / Caption** 5개 의미 역할의 소유자다. F-TYPE만 크기·두께·최소값을, Object만 Identity/State/Value 의미를 소유한다. 'Item'과 'Meta'는 F-TYPE **스케일 별칭**이지 여섯째 의미 역할이 아니다.

### Selection

| 콘텐츠 의미 | 역할 | 대체 시 위반 |
|---|---|---|
| 목표 기록 대표 수치·획득 주요 결과 | Display(number) | Caption로 낮춰 보조 값보다 작게 |
| Root/Standard 화면명 | Title(root/detail) | 임의 Card Headline |
| 실제 섹션 제목 | Section | 아이템 상태를 섹션 제목으로 |
| 이름·본문·실제 가격/조건 | Body(item/body/numberSmall) | 모두 10px 메타 처리 |
| 날짜·마감·보조 메타 | Caption(support/caption) | 보조를 Display로 과장 |

**P2 비교 후보:** Hero 22–24, 보조 12–13, Badge 12/600, 아이템 ≥12, 메타 12–13은 실제 목적별로 각각 평가하며 일괄 고정하지 않는다. Root 21.4/850(실측), 26/700(후보), 18/700(레퍼런스)은 *비교 조건*이지 동시 적용 현행 토큰이 아니다.

### Don't

1. `620/1000` 대표값보다 '완료까지 380' 파생값의 크기·굵기를 높여 주목도를 역전(R-01).
2. 실제 가격·장착·획득 조건을 9.4px/790 배지로 축소해 정보처럼 보이지 않게 함(R-10).
3. 모든 섹션 제목을 Display로 강조해 계층을 없애거나 한 아이템 안에 서로 다른 3종 `font-weight:850`과 화면별 임의값 사용.

### Relations

- **부모:** S-PAGE Title, O-ITEM Identity/Value/State, O-PROGRESS 대표/파생값, C-LIST Item/Meta.
- **자식:** 본문 텍스트 및 단위, 필요하면 실제 상태별 분리된 Inline Info. 같은 의미 정보의 중복 타이포 강조 금지.
- **형제:** C-STATUS Badge와 Action label의 의미를 넘겨받지 않고, F-TYPE 스케일만 참조.
- **금지:** 5역할 정의를 F-TYPE/C-LIST에 다시 복제하거나 Header typographic scale을 지역 CSS 임의 수치로 바꿈.

### Verification

- 실제 computed font-size 최소12px, **14px 미만 font-weight ≤600**, 200%에서도 중요한 이름·값·조건 생략/overlap 0.
- 320/390/430×light/dark에서 동일 역할/스케일 매핑, root·detail title 비교안은 fixture로 측정 후 결정 상태 기록.
- '대표값 ≥ 파생값'은 단순 폰트 크기뿐 아니라 위치·weight·색의 결합으로 수동/이미지 검사.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
