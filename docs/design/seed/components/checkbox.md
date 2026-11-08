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

### Selection
- 사용자가 **직접 완료를 확인**하거나 복수 독립 항목을 선택할 때만 C-CHECK. 실제 도메인 완료는 별도 사용자 명령으로 확정하고 AI/타이머/제스처/애니메이션이 자동 완료하지 않는다.
- 21dp 체크/혼합 dash와 48dp label target 후보/기존 규칙을 보존. Native checked/indeterminate/disabled와 시각 표현 항상 동기. 대기·실패 시 기존 값 복구.

### Don't
1. 타이머 10분 도달 시 자동 Checked와 보상 지급으로 사용자 확인을 건너뜀.
2. 실제 체크되지 않았는데 CSS만 체크 모양으로 바뀌어 aria-checked 불일치.
3. 체크박스 자체/행 전체가 둘 다 완료 처리해 한 번 탭에 두 번 명령 발생.

### Relations
- **허용:** C-SELECTION 수동 완료 라우터, C-LIST row target 하나, C-OVERLAY 실패 설명, 도메인 원본 상태.
- **금지:** C-CHECK가 실제 DB 완료/재화 지급 owner, 중첩 onClick·세그먼트/라디오 대체.

### Verification
- 정상 native 체크·mixed·disabled vs Don't CSS-only selection/이중 클릭. 320/390/430 light/dark에서 target48, aria-checked, keyboard Space, 대기 실패 rollback과 200% text 확인.

## 이력

> 과거 규격의 수치/사례/출처 원문을 보존한다. 위 4계약과 후보 상태를 우선 적용하며 이전 수치를 자동 승인하지 않는다.

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
