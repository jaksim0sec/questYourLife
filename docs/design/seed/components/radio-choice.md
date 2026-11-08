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

### Selection
- 한 그룹에서 **정확히 하나**를 선택하는 문제는 C-RADIO. 복수 독립 선택은 C-CHECK, 시점 보기 전환은 C-SEGMENT. Fieldset/Legend+Option Label/Indicator, native radio 이름과 value를 유지.
- indicator 21dp/ring6dp/row48dp/radius12dp는 제안 수치, 렌더 전 candidate. 선택 시 행 크기·줄바꿈 변화 금지.

### Don't
1. 복수 선택 대상에 Radio를 둬 두 번째 클릭 때 이전 선택을 지워 사용자 요구 위반.
2. 선택되지 않은 Radio와 Disabled/읽기 전용을 모두 동일 회색으로 렌더해 상태 불분명.
3. 실제 완료 버튼을 Radio로 대체해 사용자의 수동 확인·취소 행위를 누락.

### Relations
- **허용:** C-SELECTION 입력 라우터, C-FIELD 그룹 label/help, C-TEXT 상태 설명.
- **금지:** C-RADIO가 보상 지급이나 완료 명령 실행, Radio 그룹 안에 다른 클릭 부모 중첩.

### Verification
- 정상 동일 name exclusive/Arrow/Space 변경 vs Don't 독립 체크 무시 사례. 320/390/430 light/dark target48, keyboard focus·error·200% text와 panel 실제 값 검증.

## 이력

> 과거 규격의 수치/사례/출처 원문을 보존한다. 위 4계약과 후보 상태를 우선 적용하며 이전 수치를 자동 승인하지 않는다.

### Purpose / Anatomy
하나의 값만 고르는 Radio Group. Fieldset/Legend + Option(Target + Indicator + Label + optional Description). Checkbox와 역할이 다르며 완료 버튼을 대신하지 않는다.

### Properties / States
`name`, `value`, `options`, `disabled`, `required`, `invalid`. Unchecked/Checked/Focused/Pressed/Disabled/Error. 고유 id와 네이티브 radio 의미를 보존; 선택 시 같은 그룹 내 이전 항목은 해제.

### Tokens
Indicator 21dp 원형, 체크 시 브랜드 6dp ring + 중앙의 읽히는 음영/구멍, 행 최소 48dp, radius 12dp. 외부 텍스트 13~14sp/600. 체크 여부로 행 height/padding/weight 이동 금지.

### Rules / QA
선택 상태는 원+라벨+배경으로 표현하고 색에만 의존 금지. Choice Card가 필요한 경우 독립 동작을 증명할 때만 분리. 320dp, dark/light, Tab/Arrow/Space, 200% 폰트, 각 라벨 길이별 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
