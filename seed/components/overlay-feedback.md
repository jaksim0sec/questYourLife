---
id: C-OVERLAY
category: component
status: proposed
read_when: Sheet·Dialog·Snackbar·Skeleton·빈 상태·오류 피드백
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION, C-ACTION]
references: [REF-003, SRC-TDS-SYSTEM]
---

# Overlay와 피드백

**표현:** 일상 완료는 가벼운 상태 변화·Snackbar, 중요한 결정은 중앙 Dialog, 맥락 내 선택은 Bottom Sheet, 초기 로딩은 Skeleton. 보상 연출은 A-ART에 맡긴다. 갓생알리미의 절제된 박스·타이포·연속적인 표시/퇴장 모션을 참고하되 기능과 상태는 기존 계약을 유지한다.

| 재료 | 구성 |
|---|---|
| Sheet | [C-SHEET](bottom-sheet.md) / [C-GESTURE](gesture.md) 정본 |
| Dialog | Title? + Body? + Close? + Actions? (Title/Body 중 하나 필수) |
| Snackbar | Message + 선택적 한 Action |
| Skeleton | 실제 콘텐츠 슬롯의 윤곽, dark skel.a/b; 모션 감소 시 정적 |
| Result/Empty | 상태 설명과 필요한 다음 행동 |

표면·타이포·여백은 F-COLOR/F-TYPE/F-GEOMETRY와 C-SURFACE/C-TEXT, 버튼은 C-ACTION/P-ACTIONS를 재사용한다. 새 Dialog family나 Page Type을 만들지 않는다.

## Dialog · S08 제안

Title/Body 정렬(left/center), Close, Actions는 독립 선택. 없는 슬롯에 빈 공간을 예약하지 않는다. 제목/본문에 맞춘 높이와 가용 폭을 사용하고 긴 본문만 스크롤, Close/Footer는 접근 가능한 상태로 고정한다. 버튼은 의미·우선순위에 맞추며 기본 High 하나, 대등 선택만 동등, 위험 실행만 Critical. 320/360/390/430 폭과 200% 글자에서 잘림 없이 필요 시 Actions 세로 배치, 최소 48px target 유지.

| 검수 fixture | 조합 |
|---|---|
| A | 왼쪽 제목+본문+X, 취소/확정 |
| B | 중앙 제목+본문, 단일 확인, X 선택 |
| C | 제목만, 단일 Action, X 선택 |
| D | 본문만, 단일 Action, X 선택 |
| E | 긴 본문 스크롤, Footer Action 고정 |
| F | 제목/본문 중 하나 이상, X/Action 없음, 실제 이탈 경로 필수 |

A~F는 검수 조합이지 전용 프리셋이 아니다.

## 동작·접근성

Opening → Open → Busy/Error → Closing. 실제 완료 전 성공 표시 금지, 중복 실행 차단, 실패 시 입력 보존·재시도/안전한 취소. Dismiss는 확정이나 삭제가 아니다. 일반 안내는 목적에 따라 X/Backdrop/Escape/Back, 위험·busy는 우발적 닫기를 제한할 수 있으나 합당한 탈출 경로는 필수다. Snackbar 종료 뒤 중요한 오류·미처리 상태를 원래 영역에 남긴다.

모달을 열 때 트리거 기억, 배경 비활성화, 설명 또는 안전한 조작으로 초점 이동(위험 확정 자동 초점 금지). 실제 존재하는 Title/Body ID로 aria-labelledby/describedby 또는 이름 연결; 장문 전체를 이름으로 중복 낭독하지 않는다. Tab/Shift+Tab 범위 유지, 닫으면 트리거 또는 안전한 이전 맥락으로 초점 복귀. 중첩 모달은 되도록 피하고 필요 시 닫기 순서를 확인한다. 결과 전달은 연출과 독립적이며, 동작 감소와 TalkBack/키보드 접근을 지원한다.

## 검증

A~F의 독립 슬롯·정렬·dismiss·ARIA·초점·빈 공간을 검사하고 A/B/E/F는 320/360/390/430 × 100/200% × Light/Dark, 긴 문구·스크롤·Footer·오버플로를 비교한다. 실제 native dialog의 showModal/Cancel과 Android Back은 별도 검증. 별도 fixture나 기존 Sheet 검증은 실제 Dialog 구현의 증거가 아니다.

S08은 기존 제안 상태(proposed) 유지. REF-003 보상 관찰 medium, 미노출 동작 low. D-012/013/015 및 Shell/Sheet 규격·기존 에셋/데이터 보존. 이전 상세 설명은 Git 이력에 남아 있으며 새 디자인 승인이나 기능 구현을 뜻하지 않는다.

## 개편 표현 · UI-001 (제안)

Expressive Dialog: radius 24, inset 20, dominant title 24/800, readable supporting text and clear action hierarchy. Peer actions may use the C-ACTION expressive pill treatment; ordinary confirmations keep the default. Snackbar는 짧은 pill을 기본으로 하고 메시지가 길면 자연스럽게 확장한다. 진입/퇴장과 reduced motion은 F-ICON-MOTION. [후보 수치](../implementation/v015-tokens.json)의 `refinements`. 닫기/포커스/실제 처리 결과는 연출과 독립적이며 신규 알림이 이전 알림의 삭제 예약을 취소한다. 브라우저별 native dialog 전환 방식은 유물의 구현 참고이며 플랫폼 규칙이 아니다.
