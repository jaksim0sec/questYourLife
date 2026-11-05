---
id: C-OVERLAY
category: component
status: proposed
read_when: Sheet·Dialog·Snackbar·Skeleton·빈 상태·오류 피드백
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION, C-ACTION]
references: [REF-003, SRC-TDS-SYSTEM]
---

# Overlay와 피드백 재료

## Purpose / Non-goals
컨셉의 보상 장면과 일반 조작 피드백을 분리한다. Sheet·Dialog·Snackbar·Skeleton은 **이미지에서 확인되지 않은 보완 제안**이며 새로운 기능 요구가 아니다.

## v0.6 Bottom Sheet 독립 정본
Modal, Snackbar, Skeleton의 공통 역할만 여기에서 관리하고 **min/mid/full Snap, Body Scroll, Footer Sticky, Drag Handle, Close Gesture는 [C-SHEET](bottom-sheet.md)와 [C-GESTURE](gesture.md)**가 독립 소유한다. 중복된 높이·스크롤 정책은 이 문서에서 재선언하지 않는다.
- Dark Skeleton은 표면에서 구분되는 dark `skel.a/b` 토큰 사용, 라이트 회색 그라데이션 상속 금지.
- Dialog는 콘텐츠 중간의 승인/위험 제어, Bottom Sheet는 하단에서 맥락을 유지하는 보조 플로우. 둘을 같은 height/padding만 다른 Variant로 보지 않는다.

## Anatomy / Properties
| 재료 | Anatomy | 목적 |
|---|---|---|
| Sheet | Handle? + Title + Body + Actions? + Close | 현재 맥락의 보조 선택·정보 |
| Dialog | Title + Message + Actions | 중요한 결정·확인 |
| Snackbar | Message + 선택적 한 Action | 방금 행동 결과·되돌리기 |
| Skeleton | 해당 슬롯과 같은 기본 도형 | 초기 로딩 |
| Result / Empty | 짧은 상태 설명 + 필요 시 다음 행동 | 영역의 빈·오류·접근불가 상태 |

Modal/nonModal과 dismissible 여부를 실제 동작별로 명시한다. 이미지에 없는 보상 모달을 기본 진입 플로우로 강제하지 않는다.

## Tokens
bg.floating, radius.overlay, surface.padding/section.gap, overlay.scrim, type.section/body/support를 참조한다. 미리 정한 표면·글자 위계를 재사용하며 독립 배경 그라데이션을 만들지 않는다. Actions는 C-ACTION, 조합은 P-ACTIONS를 따른다. Skeleton은 bg.muted, F-ICON-MOTION의 동작 줄이기에서는 정적 표시다.

## States / Behavior
Opening / Open / Busy / Error / Closing을 구분한다. 확정 전 서버 결과를 성공으로 표시하지 않는다. busy 중 닫기·취소 가능 여부는 실제 처리 취소 가능성과 맞춘다. dismiss는 취소/실행을 암묵 수행하지 않는다. Snackbar가 사라져도 중요한 오류·미처리 상태는 원래 영역에 남는다. 같은 메시지 중복 노출·여러 modal 중첩을 피한다.

## Guidelines
일상 완료 피드백은 작은 메시지·상태 변화로 충분한지 먼저 판단한다. 명시적으로 보상을 확인하는 장면만 A-ART의 강한 표현을 쓴다. 자동으로 축하 모달을 열어 매번 실행 흐름을 끊지 않는다. 재시도 가능한 오류에는 실행 가능한 행동을 둔다.

## Accessibility
Modal에서는 적절한 초점 진입·범위 유지·닫은 후 원래 조작으로 복귀한다. 제목·설명·닫기 이름을 연결한다. 중요한 Snackbar action은 충분히 유지하거나 다른 곳에서도 접근 가능하게 한다. 결과는 시각 연출과 별도로 알린다. 키보드·TalkBack·모션 감소 검증은 구현 시 필요하다.

## Evidence & Confidence / Dependencies & Impact
보상 장면은 REF-003 관찰 medium, 실제 modal 전환·미노출 상태는 low/proposed. Overlay 행동 변경은 페이지 shell과 P-STATES에 영향이 있다. 현 단계는 앱 동작 검증을 대체하지 않는다.

