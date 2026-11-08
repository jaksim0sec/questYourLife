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

### Selection
- 의미 역할은 **Display / Title / Section / Body / Caption** 오직 5개. F-TYPE은 숫자 크기, C-TEXT는 정보의 목적과 배열만 판단한다.
- 공동 기록의 대표 수치는 Display, 클랜명은 Title, 사람 섹션은 Section, 이름은 Body, 시간/활동 메타는 Caption. 가격/획득조건/장착정보를 무조건 Caption으로 내리지 않는다.
- Root Title은 기존 21.4/850/letter-spacing -0.05em 측정, 새 26/700 후보, 외부 18/700 비교값을 **실측 병치**하여 판단한다. 후보는 승인 수치가 아니다. 모든 실제 텍스트 최소12px, <14px 무게≤600은 F-TYPE 강제.

### Don't
1. 기록 주연 620/1000을 13.9px로 숨기고 무의미한 Hero 제목을 22.4px/810으로 키움: 주연·보조 역전.
2. 9.4px 아이템 라벨 또는 9.7px/790 상태 배지로 많은 정보를 우겨 넣음: 축소 대신 2줄/구조 조정.
3. Title을 3개 중첩하고 사람 목록의 Identity보다 '장착 가능' Meta만 강하게 만듦.

### Relations
- **허용:** F-TYPE가 스케일과 최소값 소유, F-GEOMETRY가 200% 흐름, O-PROGRESS가 대표값/파생값, O-ITEM이 Identity/State 우선순위.
- **금지:** C-TEXT가 Root pixel 결정·광원/아트/색의 새 숫자 소유, 데이터 상태를 이미지 내 래스터 텍스트에 숨김.

### Verification
- 정상 대표값>파생값 hierarchy vs Don't Hero copy 우세, 정상 12px/600 vs Don't 9.7px/790 병치.
- 320/390/430 light/dark에서 computed font-size/weight/line-height 수집, 긴 클랜명·이미지 실패 시 identity와 value 생존, 200% 확대 폭 넘침 검사.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
