---
id: C-GESTURE
category: component
status: proposed
read_when: 드래그·탭·스와이프·스크롤 및 키보드 대체 조작
depends_on: [F-ICON-MOTION, F-GEOMETRY]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Gesture Contract


## 현행 계약

Gesture는 명시적 과업의 입력 방식이지 독립 제품 행동이 아니다. Pointer/Keyboard/Visible Control을 동등하게 제공한다. C-SHEET의 높이·Snap 의미는 Sheet가 소유하고 C-GESTURE는 물리적 추적·취소/충돌만 소유한다.

### Selection

- Scroll 가능한 본문에서 단순 위/아래 이동이면 **native scroll 우선**. Handle drag는 C-GESTURE를 사용.
- Sheet Handle에서 pointer move는 **손가락 이동량만큼 매 프레임 높이 연속 변경**. release 시 nearest valid snap.
- 손실 위험이 있는 드래그 닫기는 허용하지 않고 Close/Confirm 제어를 제공.
- 한 손가락 스와이프만으로 가능한 기능은 keyboard Arrow/visible action을 반드시 같은 과업으로 제공.

### Don't

1. 사용자가 Body를 스크롤하려는데 첫 움직임을 가로채 Sheet가 닫히거나 snap이 튄다.
2. PointerCancel/TouchCancel 후 중간 높이에 방치하고 이전 안정 Snap이 복원되지 않음.
3. 퀘스트 완료/보상 획득을 되돌릴 수 없는 스와이프만으로 실행하고 키보드 대체가 없음.

### Relations

- **부모:** C-SHEET Handle/Scroll Boundary, C-LIST의 실제 Swipe Action(필요시), UI의 접근 가능한 키보드 명령.
- **자식:** pointerTarget, axis, threshold, state, cancelCondition, keyboard fallback, action label. 디자인 장식의 애니메이션이 아닌 사용자 명령만 처리.
- **형제:** C-OVERLAY의 Focus/ESC, C-ACTION의 버튼 대체 경로. OS Back edge·IME·텍스트 선택과 충돌하지 않는다.
- **금지:** Sheet 드래그가 두 손가락 Zoom/가로 Back/본문 tap을 가로채거나 성공 상태를 추정.

### Verification

- pointerdown/move/up 연속 height trace + nearest snap, pointercancel 복원, native scroll 경계에서 남는 delta만 인계 확인.
- 키보드 ArrowUp/Down·Escape와 보이는 버튼의 동등 결과, reduced-motion, 320/390/430 대응.
- 손실 과업을 실제 이벤트로 완료 처리하지 않는지 확인하고 실기기 촉각 입력은 별도 미검증 표기.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose
동작을 컴포넌트처럼 **입력·임계값·상태·충돌·취소 규칙**으로 정의한다. 제스처는 보이지 않는 단독 행동으로만 제공하지 않으며 반드시 명시적 버튼/키보드 대안이 있어야 한다.

### Gesture Anatomy
`pointerTarget`, `axis`, `threshold`, `velocity`, `snap`, `cancelCondition`, `fallback`, `feedback`. start → tracking → committed/cancelled; PointerCancel은 이전 안정 상태 복원.

### Bottom Sheet Gesture · v0.15 승인 디자인 규칙
- Handle은 즉시 드래그를 시작하며 **손가락 위치만큼 시트 높이가 매 프레임 연속 변경**된다. release 시 현 높이에서 가장 가까운 유효 `closed/min/mid/full` snap으로 정착. 옛 55px/속도 기반 '한 단계씩 변경'은 더 이상 정본이 아님.
- Body의 스크롤 가능 구간에서는 **브라우저/OS Native 스크롤을 우선**한다. 내부가 맨 위에서 아래로 더 당겨지면 시트 축소, 맨 아래에서 위로 더 밀면 시트 확장. 같은 드래그 도중 경계에 닿으면 *잔여 이동량만* Sheet로 인계한다.
- Wheel/trackpad은 손가락 위치가 없으므로 끝점에서 이산적인 snap 전환이 가능. 가로 OS Back gesture·다중 터치·본문 탭·선택 입력 가로채기 금지.
- PointerCancel/TouchCancel은 이전 안정 snap 복구, backdrop/Esc/Back/Close는 닫기 및 Focus 복귀. Keyboard ArrowUp/ArrowDown과 보이는 snap 버튼을 제공한다.
- 이 규칙은 **시각 상호작용**을 승인한 것이며 실제 체크/보상 등 도메인 상태를 제스처만으로 확정하지 않는다. Android 실기기 물리 터치/IME/TalkBack은 후속 검증.

### Accessibility
Handle에 동작 설명 접근성 이름, keyboard ArrowUp/ArrowDown 및 보이는 상태 변경 컨트롤. 포커스 트랩, 타깃 48dp, 동작 줄이기. 중요한 변경사항을 제스처 하나로만 제공하지 않는다.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
