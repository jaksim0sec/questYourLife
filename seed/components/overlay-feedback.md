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
| Dialog | Title? + Message? + Close X? + Actions? (Title/Message 중 최소 1개) | 중요한 결정·확인·안내 |
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


## S08 · 공통 Dialog의 선택 슬롯·정렬·종료 계약

> **성격:** 기존 C-OVERLAY의 Dialog 역할을 보완하는 S08 제안. 새로운 DialogAlert/DialogConfirm/DialogSelect 컴포넌트, Page Type, Dialog 전용 Action Variant는 만들지 않는다. Bottom Sheet(C-SHEET/P-SHEET)의 Snap·Handle·Drag·Footer, Snackbar/Toast 역할은 변경하지 않는다. **규격 작성은 웹 구현·디자인 승인과 다르다.**

### 1. 독립적인 Anatomy

- **Title? / Body? / X? / Actions?**는 각기 선택할 수 있다. **제목+본문 / 제목만 / 본문만** 모두 허용한다. 제목과 본문이 모두 없는 무의미한 모달은 만들지 않는다.
- **Title align과 Body align은 각각 left 또는 center**를 선택한다. 제목 왼쪽+본문 중앙, 제목 중앙+본문 왼쪽도 가능하다. 정렬, X, 버튼 수를 한 세트의 고정 프리셋으로 묶지 않는다.
- **X(닫기)**는 선택이며 제목의 유무와 독립이다. 제목이 없다면 X를 위해 빈 제목행을 만들지 않는다. X는 C-ICON의 이름과 최소 48px 터치 타깃을 사용한다.
- **Actions는 0개도 가능**하고 존재할 때만 기존 [P-ACTIONS](../patterns/action-layout.md)와 C-ACTION의 의미·너비·상태·접근성 규칙을 사용한다. 주 실행 High는 기본 최대 1개, 대등 선택은 실제로 동등할 때만, 위험 실행은 승인된 Critical/의미색 여부 확인 후 표현한다. REF-005의 특정 50:50 교정 사례를 Dialog의 전역 기본값으로 만들지 않는다.
- 존재하지 않는 Title/Body/Close/Footer의 고정 높이·더미 공백·불필요한 Divider를 예약하지 않는다. 슬롯은 기존 C-TEXT·C-ICON·C-ACTION의 재료이며 새 컴포넌트 패밀리가 아니다.

| 조합 | 제목 | 본문 | X | Actions | 정렬/의미 |
|---|---|---|---|---|---|
| **A** | 있음 | 있음 | 있음 | 2개 | 왼쪽 제목·본문, 취소/확정 |
| **B** | 있음 | 있음 | 없음 가능 | 1개 | 중앙 제목·본문, 안내 확인 |
| **C** | 있음 | 없음 | 선택 | 1개 | Title-only, 본문 빈 영역 없음 |
| **D** | 없음 | 있음 | 선택 | 1개 | Body-only, 제목 빈 영역 없음 |
| **E** | 선택 | 긴 본문 | 선택 | 있음 | **본문 내부 스크롤**, Action 접근 유지 |
| **F** | 없음/있음 | 있음/없음 | 없음 | 없음 | Escape/Backdrop/자동 종료 등 **실제 탈출 경로** 필수 |

A~F는 **최소 검수 fixture**이고 실제 앱에 6개 Dialog 종류나 전용 프리셋을 만들라는 지시가 아니다. F에서도 제목과 본문 중 하나는 있어야 한다.

### 2. 표면·크기·스크롤

- Dialog는 페이지 위에 집중해서 뜨는 **중앙형 모달**이고 Bottom Sheet의 높이/Snap 변형이 아니다. 기존 C-SURFACE Overlay, F-GEOMETRY의 폭·여백·Radius·scrim 및 C-TEXT 위계를 재사용한다. 원본 손그림의 테두리는 실제 CSS border 지시가 아니다.
- 기본 높이는 **content-sized height**다. 짧은 Title-only/Body-only/Actions 없음에서 남는 빈 공간을 허용하지 않는다. 긴 본문에서는 가용 화면 높이·OS safe inset·200% 글자를 고려해 Dialog의 상한 높이를 정하고 **본문만 overflow-y:auto**로 스크롤하며 Footer Actions와 X는 내부 스크롤에 밀려나지 않도록 한다.
- 320/360/390/430 폭 및 200% 글자 확대에서 긴 한글 문구와 버튼을 자르거나 축소하지 않는다. Actions의 가로 공간이 부족하면 기존 P-ACTIONS에 따라 세로로 재배치한다. **터치 타깃 최소 48px** 및 가로 오버플로 0·컨트롤 비중첩을 검증한다.
- 다른 Modal/Sheet 위에 다시 Dialog를 중첩하기 전에 포커스·배경 조작 차단·닫기 순서를 설계한다. 일반 피드백을 매번 강제 Dialog로 바꾸지 않는다.

### 3. 목적별 종료 정책

| 상황 | X / 바깥 탭 | Escape / Android Back | Action·상태 |
|---|---|---|---|
| **일반 안내·되돌릴 수 있는 선택** | X와 Backdrop 각각 목적에 따라 허용 | 취소/닫기 의미로 허용 가능 | Action 0~2, 단순 dismiss를 확정 처리하지 않음 |
| **위험 변경·필수 확인** | 우발적인 Backdrop 닫기 제한 가능 | 취소 또는 명시된 보호 정책. 제한 시 대체 경로 명확화 | 취소와 확정 역할을 구분하며 삭제/수령을 숨은 닫기에 연결 금지 |
| **Busy/Pending** | 실제 취소 가능한 작업인지에 따라 제한 | 강제 닫기가 데이터 손실로 이어지는지 점검 | 중복 실행 차단, 처리 중 안내, 실패 시 **Error + Retry 또는 안전한 취소** |
| **X와 Actions가 모두 없음** | 필요하면 Backdrop 허용 | **Escape/Android Back 또는 정당한 자동 종료 중 최소 하나가 실제로 동작해야 함** | 영구적으로 갇힌 Modal 금지 |

- **닫기 정책은 화면/행동 목적별 결정**이다. Escape는 브라우저 native dialog의 cancel 이벤트 등으로 제어·실측한다. Android Back은 WebView/Native 연결이 달라서 Chromium Escape 통과만으로 검증 완료로 간주하지 않는다.
- X·Footer Action이 없어도 **합당한 이탈 경로**가 필수다. 변경 손실 확인에서는 Backdrop dismiss를 제한할 수 있으나 X/Actions/시스템 뒤로까지 모두 막아 초점이 갇히게 하지 않는다.
- **상태는 Opening → Open → Busy 또는 Error → Closing** 중 현재 단계로 관리한다. Busy/Loading 동안 버튼 영역·라벨 위치를 보존하고 동일 실행을 중복 요청하지 않는다. 성공은 실제 결과 확정 전 선언 금지. 실패에서는 이전 입력·선택을 유지하고 재시도 또는 취소가 가능해야 한다.

### 4. 초점·접근성·읽기

1. **열 때** 트리거를 기억하고 배경을 모달 범위 밖으로 비활성화한다. Dialog 안의 첫 **안전한** 조작 또는 설명 시작점으로 초점을 이동하며, 위험 확인의 확정 버튼을 초기 자동 초점하지 않는다. 웹에서 native dialog를 사용한다면 showModal()의 실제 modal/Focus Trap을 검증하고 단순 show()를 modal 성공으로 취급하지 않는다.
2. **읽기** 실제 Title이 있으면 aria-labelledby, Body 설명이 있으면 aria-describedby 등 연결하되 **없는 슬롯의 ID는 참조 금지**. Body-only라면 본문 또는 적절한 aria-label로 **Dialog의 이름**을 제공한다. 장문 전체를 Accessible Name으로 중복 낭독하지 않는다.
3. **열린 동안** Tab/Shift+Tab은 Dialog 범위에 남아야 하고 배경의 탐색과 클릭은 차단한다. X/Actions 둘 다 없는 F는 읽기 시작점으로 포커스할 수 있게 tabindex=-1 등을 고려한다. 키보드·포인터·스크린리더 접근을 별도로 검증한다.
4. **닫을 때** 트리거가 남아 있으면 **원래 초점 복구**, 사라졌다면 의미상 이전 맥락의 안전한 요소에 초점을 둔다. dismiss는 성공/완료/삭제 신호가 아니며, 결과는 별도 상태로 전달한다.

### 5. 실제 검증 게이트와 보존 규칙

- A~F 여섯 조합의 **독립 슬롯·정렬·닫기 동작·ARIA ID 조건·포커스 복귀·부족한 슬롯의 빈 공간 없음**을 검증한다. 대표 A/B/E/F는 **320·360·390·430 × 100·200% × Light·Dark**로 좁은 폭/긴 문장/Action Footer 보존/오버플로·스크롤을 확인한다.
- 기존 Seed Lab의 **C-SHEET native dialog 시연은 S08 Dialog의 구현/검증 근거가 아니다**. 독립 fixture를 사용했다면 정본 웹 CSS/JS와 실제 소비 페이지의 검증을 대체할 수 없음을 명시한다.
- 기존 D-012의 버튼 Variant·기하 값, D-013의 Composite/Page Type 계층 제한, D-015 Navigation, S-PAGE와 C-SHEET의 스크롤/포커스/백드롭 소유권, 원본 SVG/이미지, main 앱과 데이터·경제는 보존한다.

