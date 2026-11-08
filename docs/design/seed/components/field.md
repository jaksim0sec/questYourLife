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

### Selection
- 사용자가 문자열/선택값을 **입력**할 때만 C-FIELD. Label+Control+Help/Error를 기본으로, Placeholder는 Label을 대체하지 않는다. 단일 검색은 Enter 동작, 폼은 실제 Submit 책임 명시.
- Focus에서는 승인된 **Control border 한 번만** 명확히 변경, outline/shadow 중복 금지. ReadOnly는 Disabled와 다르며, 오류는 필드 곁에 원인/복구 표현.
- 장문/2줄은 auto height, 200% 글자에서 고정 height 금지. 기존 입력 보존 우선.

### Don't
1. Placeholder만 있는 이름 입력, blur 순간 입력 목적 소실.
2. 검증 오류를 Snackbar에만 띄워 오류가 사라진 뒤 수정 대상도 알 수 없음.
3. Focus에서 두꺼운 outline+shadow+border 세 겹 또는 Disabled처럼 ReadOnly 값 흐림.

### Relations
- **허용:** Page Intent 폼 과업, C-ACTION 제출 버튼, C-OVERLAY 지속 오류, S-PAGE IME safe/scroll.
- **금지:** C-FIELD가 Account/Auth/신규 Server Validation 발명, 입력값을 닫기 제스처로 조용히 파기.

### Verification
- 정상 label/aria-describedby/인라인 error vs Don't placeholder-only/Toast-only. 320/390/430 light/dark, focus/change/submit, 200% 글자·keyboard·스크롤 접근성; 서버 저장은 별도 검증.

## 이력

> 과거 규격의 수치/사례/출처 원문을 보존한다. 위 4계약과 후보 상태를 우선 적용하며 이전 수치를 자동 승인하지 않는다.

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
