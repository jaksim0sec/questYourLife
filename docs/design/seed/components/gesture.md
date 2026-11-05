---
id: C-GESTURE
category: component
status: proposed
read_when: 드래그·탭·스와이프·스크롤 및 키보드 대체 조작
depends_on: [F-ICON-MOTION, F-GEOMETRY]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Gesture Contract

## Purpose
동작을 컴포넌트처럼 **입력·임계값·상태·충돌·취소 규칙**으로 정의한다. 제스처는 보이지 않는 단독 행동으로만 제공하지 않으며 반드시 명시적 버튼/키보드 대안이 있어야 한다.

## Gesture Anatomy
`pointerTarget`, `axis`, `threshold`, `velocity`, `snap`, `cancelCondition`, `fallback`, `feedback`. start → tracking → committed/cancelled; PointerCancel은 이전 안정 상태 복원.

## Bottom Sheet Gesture Proposal
- Handle에서 상향 swipe **55px 이상 또는 0.55px/ms 이상**: 한 단계 확장(min→mid→full).
- Handle에서 하향 swipe: 한 단계 축소(full→mid→min), 최소 상태에서 한 번 더 아래로 swipe하면 닫기.
- Body의 세로 스크롤은 **Handle 스냅과 분리**. OS back gesture·화면 가장자리 내비 제스처 가로채기 금지.
- Backdrop tap / Esc / Close CTA: 닫기. Focus는 시트 오픈 전 버튼으로 복귀. 홈 바/IME·동작 줄이기를 존중.
- 임계값은 v0.6 실험값으로 실기기 감도 검증 후 결정; 자동 상태 확정·실데이터 변경을 수행하지 않음.

## Accessibility
Handle에 동작 설명 접근성 이름, keyboard ArrowUp/ArrowDown 및 보이는 상태 변경 컨트롤. 포커스 트랩, 타깃 48dp, 동작 줄이기. 중요한 변경사항을 제스처 하나로만 제공하지 않는다.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
