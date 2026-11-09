---
id: C-RADIO
category: component
status: proposed
read_when: 상호 배타적 선택·라벨·그룹 초점
depends_on: [F-COLOR, F-GEOMETRY, F-TYPE, C-SELECTION]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Radio Choice


## 현행 계약

C-RADIO는 **한 그룹에서 하나만 선택**하도록 한다. 완료 체크는 C-CHECK, 기간/보기는 C-SEGMENT가 소유한다. 21dp 원형/브랜드 링은 기존 실험 후보이며 Android 승인 수치로 바꾸지 않는다.

### Selection

- 상호 배타적인 2개 이상의 **데이터 값**을 입력할 때 Radio Group. 단일 boolean은 Checkbox/Switch; 실제 제출은 별도 Action.
- Fieldset/Legend + 각 option(Label/Value, 필요하면 Description)로 그룹을 명시.
- 단순 페이지 이동을 Radio로 위장하지 않으며, 선택 즉시 실제 결과 변경이 필요한 때만 반응을 즉시 적용.

### Don't

1. 한 그룹에서 둘을 동시에 Checked로 둬 '단일 선택' 의미가 깨짐.
2. 화면의 전역 목적지를 Radio Card 5개로 렌더하여 Navigation을 입력 컨트롤처럼 만듦.
3. 옵션마다 미세한 radio 클릭만 있고 label은 눌리지 않거나 48px hit target보다 작은 사례.

### Relations

- **부모:** Fieldset/Form 또는 설정의 실제 선택 Group.
- **자식:** Radio input + unique id + Label + optional Description.
- **형제:** C-FIELD 설명과 submit C-ACTION, 선택 상태에 따른 실제 데이터 영역.
- **금지:** nested Link/Button, Checkbox 완료 명령, tablist 역할과 중복 role 선언.

### Verification

- Click/Space/Arrow 한 번의 선택에 한 항목만 checked, group/name/label 연결, disabled/required/error/Focus 상태.
- 320/390/430×light/dark, 200% 긴 라벨·48 hit target, 후보 indicator 21dp는 명확한 상태로 실제 판정.
- Don't의 다중 Checked/label 클릭 실패를 인터랙션으로 검출.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Anatomy
하나의 값만 고르는 Radio Group. Fieldset/Legend + Option(Target + Indicator + Label + optional Description). Checkbox와 역할이 다르며 완료 버튼을 대신하지 않는다.

### Properties / States
`name`, `value`, `options`, `disabled`, `required`, `invalid`. Unchecked/Checked/Focused/Pressed/Disabled/Error. 고유 id와 네이티브 radio 의미를 보존; 선택 시 같은 그룹 내 이전 항목은 해제.

### Tokens
Indicator 21dp 원형, 체크 시 브랜드 6dp ring + 중앙의 읽히는 음영/구멍, 행 최소 48dp, radius 12dp. 외부 텍스트 13~14sp/600. 체크 여부로 행 height/padding/weight 이동 금지.

### Rules / QA
선택 상태는 원+라벨+배경으로 표현하고 색에만 의존 금지. Choice Card가 필요한 경우 독립 동작을 증명할 때만 분리. 320dp, dark/light, Tab/Arrow/Space, 200% 폰트, 각 라벨 길이별 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
