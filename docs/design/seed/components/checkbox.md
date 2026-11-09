---
id: C-CHECK
category: component
status: proposed
read_when: 일일퀘스트·목록 선택 등 수동 체크·혼합 선택
depends_on: [F-GEOMETRY, F-COLOR, F-ICON-MOTION]
references: [S2-04, S2-05, D-011]
---

# Checkbox


## 현행 계약

C-CHECK는 **수동 완료 명령**과 **일반 선택 입력**을 구분한다. 사용자가 눌렀을 때 Pending/Confirmed/Failed의 결과 상태는 도메인이 소유하며, 단순 체크 애니메이션이 실제 완료를 확정하지 않는다.

### Selection

| 상황 | 선택 | 배제 |
|---|---|---|
| 여러 항목 독립 선택 | Checkbox group | Radio |
| 사용자가 실제 일일 퀘스트 완료 요청 | Check **명령**→Pending→실제 Confirmed | 탭/타이머/AI 응답으로 자동 완료 |
| 일부 옵션 선택 | indeterminate Mixed | 3상태를 단일 boolean 저장 |
| 한 값만 선택 | Radio | Checkbox 다중 선택처럼 처리 |

기존 21dp indicator/48dp label hit target과 D-012 외형을 유지. 선택 결과가 실패하면 원래 상태 복구·오류를 남긴다.

### Don't

1. 체크 UI가 바로 완료 보상 지급 완료를 표시하지만 서버/Room에서 Confirmed를 받지 않음.
2. 목록 행 전체와 checkbox가 중첩 이벤트를 받아 완료 명령이 두 번 실행됨.
3. disabled/indeterminate를 색상 하나만 바꾸어 접근성 checked/mixed state와 불일치.

### Relations

- **부모:** C-LIST의 실제 완료/선택 행, Form의 독립 선택 그룹.
- **자식:** native checkbox indicator + Label + optional pending/error message. 표시와 input checked/indeterminate/disabled 일치.
- **형제:** O-PROGRESS 결과 업데이트는 Confirmed 후에만, C-ACTION Undo는 실제 되돌릴 수 있을 때만.
- **금지:** 이미 선택한 상태를 서버 확정 성공으로 재해석, 행 link 내부 nested checkbox, 단순 UI toggle로 도메인 지급 완료.

### Verification

- Unchecked→Pending→Confirmed/Failed 각각 실제 이벤트·DOM state 확인, 실패 원상 복구, 중복 명령 0.
- 320/390/430×light/dark, 200%·48 label hit, indicator21, Mixed 접근성 및 keyboard Space.
- 정상 선택/Don't 자동 완료를 실제 상태 텍스트로 구분하고 보상 지급을 시뮬레이션 성공이라고 주장하지 않는다.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Non-goals
사용자의 **실제 완료 확인** 또는 선택 여부를 표현한다. 퀘스트 완료의 근거는 도메인 상태이며 타이머/AI/애니메이션이 자동 완료시키면 안 된다.

### v0.6 시각적 선택 계약
기본 브라우저 Checkbox를 스타일을 지정하지 않은 채 노출하지 않는다. 21dp 체크 도형 / 선택 시 선명한 브랜드 채움 + 대비되는 체크 / Mixed는 브랜드 면 위 짧은 수평선 / Disabled는 중립 약한 색상. Label까지 포함한 최소 터치 타깃 48dp, native input의 checked/indeterminate/disabled와 시각 상태가 반드시 같아야 한다. 상태 변화 중 indicator의 outer width/height와 text baseline 유지.

### Anatomy / Properties
Indicator + Label + optional Description; checked / unchecked / mixed, disabled, pending. 실제 퀘스트 완료 액션과 UI 일반 선택을 동일한 서버 연산으로 가정하지 않는다.

### States
Unchecked → User Action → Pending → Confirmed checked / Failed(원 상태 복원 + 오류). Mixed는 그룹 일부 선택을 나타낼 때만. 완결 취소가 가능한지는 제품 동작 정본 검토.

### Tokens
stroke.control, positive/neutral 의미색, round Outline, 48 비중첩 터치 영역.

### Guidelines
목록 행 전체와 Checkbox를 동시에 누르면 중복 실행되지 않도록 조작 범위를 분리. 완료할 수 없는 항목은 이유를 표시. S2-05 실행형 리스트에 체크 아이콘이 큰 CTA를 압도하지 않아야 함.

### Accessibility
role checkbox, checked/mixed 상태, group name, label 연결. 오류 후 포커스 유지, 진행·완료를 색만으로 표시하지 않음.

### Evidence / Impact
S2-05 퀘스트 리스트, 기존 제품 사용자 수동 완료 확정. API 구현 상태는 별도 검증.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.
