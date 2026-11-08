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

### Selection
- 드래그 가능한 시트 등 명시적 연속 조작에만 Gesture 적용. 단순 선택·퀘스트 완료·응원은 버튼이 기본이다. 제스처는 입력 좌표/축/임계값/취소/대체 동작을 함께 정의.
- 시트 Handle은 손가락 위치에 **매 프레임 높이 추종**, release 위치와 가까운 유효 snap으로 이동. 본문 native scroll 우선, 끝 지점에서 **남은 델타만 시트로 인계**. 키보드 화살표·보이는 조작 대안 필수.

### Don't
1. swipe 하나만으로 퀘스트가 확정 완료되거나 보상이 지급되는 설계: 사용자의 명시적 완료 행동과 시스템 상태 분리 실패.
2. 내부 리스트가 중간에 있는데 시트가 스크롤을 가로채거나 PointerCancel 뒤 중간 높이에 고립.
3. 가로 OS Back을 세로 시트 gesture로 해석하여 페이지를 닫기.

### Relations
- **허용:** C-SHEET의 Handle/Body 및 C-OVERLAY Close, C-ACTION의 명시적 대체 버튼.
- **금지:** C-GESTURE 자체가 서버 상태·Route history·보상 지급 소유, pointercancel을 성공 전환으로 사용.

### Verification
- 정상 native scroll/edge-transfer와 Don't scroll-hijack fixture, pointermove 높이 연속성·Cancel 복원·nearest snap.
- 320/390/430 light/dark에서 키보드·Escape·Reduced Motion, 다중 터치/가로-back 간섭 없음 확인. Android physical gesture 미검증 표기.

## 이력

> P2 이전 제안·구버전·수치 근거를 원문 보존한다. 현행 판단은 위 네 계약에서만 수행하며 이전 후보를 자동 승인하지 않는다.

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
