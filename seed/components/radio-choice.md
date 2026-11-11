---
id: C-RADIO
category: component
status: proposed
read_when: 상호 배타적 선택·라벨·그룹 초점
depends_on: [F-COLOR, F-GEOMETRY, F-TYPE, C-SELECTION]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Radio Choice

## Purpose / Anatomy
하나의 값만 고르는 Radio Group. Fieldset/Legend + Option(Target + Indicator + Label + optional Description). Checkbox와 역할이 다르며 완료 버튼을 대신하지 않는다.

## Properties / States
`name`, `value`, `options`, `disabled`, `required`, `invalid`. Unchecked/Checked/Focused/Pressed/Disabled/Error. 고유 id와 네이티브 radio 의미를 보존; 선택 시 같은 그룹 내 이전 항목은 해제.

## Tokens
Indicator 21dp 원형, 체크 시 브랜드 6dp ring + 중앙의 읽히는 음영/구멍, 행 최소 48dp, radius 12dp. 외부 텍스트 13~14sp/600. 체크 여부로 행 height/padding/weight 이동 금지.

## Rules / QA
선택 상태는 원+라벨+배경으로 표현하고 색에만 의존 금지. Choice Card가 필요한 경우 독립 동작을 증명할 때만 분리. 320dp, dark/light, Tab/Arrow/Space, 200% 폰트, 각 라벨 길이별 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
