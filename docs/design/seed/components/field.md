---
id: C-FIELD
category: component
status: proposed
read_when: 입력·검색·여러 줄 텍스트·오류·도움말
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-005, SRC-TDS-SYSTEM]
---

# Field와 Textarea


## 현행 계약

C-FIELD는 사용자가 직접 입력하는 Text/Number/Search/Textarea를 소유한다. 진짜 저장 성공은 페이지 데이터 계층에서 확인하며 Field의 'valid' 시각 표시가 서버 확정 근거가 아니다. D-012의 **border-only 단일 focus affordance**를 유지한다.

### Selection

- 사용자가 내용을 **입력/편집**할 때 Field. 여러 값 중 택일이면 C-RADIO, 상태 완료라면 C-CHECK, 목록 탐색 조건이면 C-SELECTION Filter를 우선.
- Label은 항상 보이는 이름, optional Hint, Error, Validated/Pending 상태. Form 제출은 C-ACTION이 소유.
- 긴 내용은 Textarea, 문장 검색은 Search. placeholder는 label의 대체가 아니다.

### Don't

1. Label 없이 placeholder만 둬 사용자가 입력 뒤 필드 의미를 확인할 수 없음.
2. 오류 상태를 붉은 보더 하나로만 표시하고 설명/복구 수단이 없음.
3. Focus 상태에 원형 글로우/두꺼운 그림자를 중첩하거나 200% 확대에서 입력 텍스트를 잘라먹음.

### Relations

- **부모:** Form/Page Section/C-SHEET(IME/키보드 보호 필수).
- **자식:** Label + Control + optional Help/Error/Count. 실제 상태는 aria-invalid/description으로 연결.
- **형제:** C-ACTION submit, C-RADIO/C-CHECK(직접 선택). 입력 보호/뒤로가기는 Page Type이 소유.
- **금지:** Field 안 nested Button(명시적 아이콘 행동 외), Placeholder-only, 지역 색상으로 검증 오류 숨기기.

### Verification

- 320/390/430×light/dark, 입력 focus/keyboard, 200%/긴 한글/오류/disabled/readonly 계산 스타일·레이아웃 확인.
- 실제 label 연결, aria-invalid+error, 자동 저장을 성공으로 위장하지 않는지, Tab 순서 확인.
- 기존 D-012 border-only focus 외곽 박스 변화/중복 glow 0 검사.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Non-goals
이미지에 입력 상태가 충분히 없으므로 재사용 라이브러리의 누락을 채우는 **설계 제안**이다. 직접 관찰한 컴포넌트로 주장하지 않는다. 대화·온보딩·계정 플로우 자체를 새로 확정하지 않는다.

### Anatomy / Properties
Label + 선택적 Description + Control(Value/Placeholder + 선택적 Prefix/Suffix) + Help 또는 Error. singleLine / multiline / search, required, readOnly, disabled, validation, maxLength 등 필요한 속성만 사용한다. Placeholder는 Label을 대체하지 않는다.

### Tokens
radius.control, bg.surface 또는 bg.muted, stroke.control, fg.primary/secondary, type.body/support, field.paddingX/Y와 size.actionLarge 기준 최소 높이. 여러 줄은 내용·줄 수에 따라 높이가 늘어난다. 임의 테두리색·강한 inset shadow·유리 표면을 새로 만들지 않는다.

### States / Behavior
Default → Focus → Filled; Error / Success / Disabled / ReadOnly는 서로 구분한다. Error는 critical 경계+아이콘+구체 문구, 성공은 필요할 때만 표시한다. Loading/validating은 값·라벨·높이를 보존하고 검증 전 성공을 단정하지 않는다. 읽기 전용은 disabled처럼 값을 흐리게 하지 않는다. clear·show/hide 버튼은 독립 이름과 target을 가진다.

### Guidelines
한 그룹 안 입력·확인·취소의 높이와 정렬을 맞춘다. 오류를 Toast에만 두지 않고 필드 가까이에 남긴다. 글자 수·필수 항목 설명은 작은 회색으로 숨기지 않는다. 긴 문구를 억지로 한 줄에 끼우지 않는다. 검색과 텍스트 입력은 공통 스타일을 쓰되 Enter 동작 의미를 명시한다.

### Accessibility
외부 Label 연결, 도움말·오류 연결, 실제 input 종류·자동완성·키보드 동작을 플랫폼에 맞춘다. 첫 오류 위치로 이동하되 입력 중 임의로 포커스를 훔치지 않는다. 200% 글자·키보드 노출 시 입력과 오류가 가려지지 않게 한다.

### Evidence & Confidence / Dependencies & Impact
시각적 직접 근거 low; REF-005의 padding·타이포·줄바꿈 교정 원칙을 적용한 proposed 보완. 입력 상태·동작은 웹 시연 전 needs-validation. 필드 변경은 폼 조합·하단 키보드 inset에 영향이 있다.


### v0.15 Field Focus 룰 · 확정 표현

Focused 입력은 Control 테두리에서 **한 번만** 명확한 상태 변화를 주고, 두꺼운 반투명 `box-shadow` / `outline`을 중복하지 않는다. 시연의 Control focus는 외부 outline=0, border 기반; 키보드 focus 표시 자체를 제거하라는 의미가 아니며 명도 대비와 상태 인지는 접근성 테스트를 요구한다. Disabled/ReadOnly/Error와 각 상태의 우선순위를 분리한다.
