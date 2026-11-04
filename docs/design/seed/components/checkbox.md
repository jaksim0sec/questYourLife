---
id: C-CHECK
category: component
status: proposed
read_when: 일일퀘스트·목록 선택 등 수동 체크·혼합 선택
depends_on: [F-GEOMETRY, F-COLOR, F-ICON-MOTION]
references: [S2-04, S2-05, D-011]
---

# Checkbox

## Purpose / Non-goals
사용자의 **실제 완료 확인** 또는 선택 여부를 표현한다. 퀘스트 완료의 근거는 도메인 상태이며 타이머/AI/애니메이션이 자동 완료시키면 안 된다.

## Anatomy / Properties
Indicator + Label + optional Description; checked / unchecked / mixed, disabled, pending. 실제 퀘스트 완료 액션과 UI 일반 선택을 동일한 서버 연산으로 가정하지 않는다.

## States
Unchecked → User Action → Pending → Confirmed checked / Failed(원 상태 복원 + 오류). Mixed는 그룹 일부 선택을 나타낼 때만. 완결 취소가 가능한지는 제품 동작 정본 검토.

## Tokens
stroke.control, positive/neutral 의미색, round Outline, 48 비중첩 터치 영역.

## Guidelines
목록 행 전체와 Checkbox를 동시에 누르면 중복 실행되지 않도록 조작 범위를 분리. 완료할 수 없는 항목은 이유를 표시. S2-05 실행형 리스트에 체크 아이콘이 큰 CTA를 압도하지 않아야 함.

## Accessibility
role checkbox, checked/mixed 상태, group name, label 연결. 오류 후 포커스 유지, 진행·완료를 색만으로 표시하지 않음.

## Evidence / Impact
S2-05 퀘스트 리스트, 기존 제품 사용자 수동 완료 확정. API 구현 상태는 별도 검증.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.
