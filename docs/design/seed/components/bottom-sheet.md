---
id: C-SHEET
category: component
status: proposed
read_when: 부분/중간/전체 펼침 및 Footer/Body/드래그
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR, C-GESTURE, C-ACTION]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Bottom Sheet · adaptive snap points

## 현행 계약

### Selection
- 바탕을 유지하며 짧은 선택·설명만 보조하면 C-SHEET **바텀시트페이지**. 중요한 최종 확인은 Dialog(C-OVERLAY), 긴 입력 또는 이력 독립 과업은 논글로벌 Page. C-SHEET는 페이지 유형/하단바 정책을 재정의하지 않는다.
- Snap은 **필요한 경우에만**: 내용이 짧으면 min 하나+close로 충분. 여러 유효 높이일 때만 mid/full; 겹치는 snap 제거. 기존 min 185px floor / 40vh 또는 380px cap, mid 약65%, full safe inset 차감은 제안/시연 수치로 유지, 전역 승인 아님.
- 변경 손실 위험 입력은 Swipe dismiss 제한·Close 확인. Body만 스크롤, Footer는 고정하되 safe inset 한 번 계산.

### Don't
1. '초대 방식 선택' 두 항목에 무조건 min/mid/full 3단계를 만들고 배경 스크롤을 막기.
2. 클랜명 입력 중 손가락 드래그로 시트가 즉시 닫혀 값 손실. 논글로벌 폼이나 확인 절차가 필요.
3. Footer CTA가 키보드에 가려지거나 손가락 동작만 남아 키보드 대안이 없음.

### Relations
- **허용:** page-types → sheet overlay type, C-GESTURE → drag/cancel, C-OVERLAY → dialog/scrim, C-ACTION → footer. Backdrop은 바탕의 탭 활성 유지.
- **금지:** Modal/Dialog의 포커스와 Sheet의 Snap 정책 혼합, C-SHEET가 새 전역 라우트 또는 보상 서버 동작 생성.

### Verification
- 정상 간단 선택 vs Don't 불필요 삼중 snap 병치. 실제 사용중 기준 스크롤 끝·드래그 인계·pointercancel 이전 안정상태 복원.
- 320/390/430 light/dark, Escape/Close/Backdrop, Tab/Arrow, 200% 글자/키보드/Reduced Motion; 값 손실·중첩 scroll 금지.

## 이력

> P2 이전 제안·구버전·수치 근거를 원문 보존한다. 현행 판단은 위 네 계약에서만 수행하며 이전 후보를 자동 승인하지 않는다.

### Purpose & Source
기존 C-OVERLAY에서 독립 상태·제스처·Footer 계약을 분리. SEED [Bottom Sheet](https://seed-design.io/components/bottom-sheet)의 Backdrop/Container/Header/Close/Footer 및 snap point 철학을 참조하되, 이 프로젝트의 동작 수치는 자체 제안이다.

### Anatomy
Backdrop + Container + optional Header(title/description) + Handle + Scrollable Body + optional Fixed Footer(Action Group) + optional Close. Body만 scroll, CTA/Footer는 본문 스크롤에서 제외. CTA가 없으면 빈 하단 액션 영역 금지.

### Snap States
`closed`, `min`(내용 기반; v0.15 시연은 185px floor·40vh/380px cap), `mid`(가용 높이의 약 65%), `full`(가용 높이에서 OS inset을 제외한 전체). 실기기에선 내용 높이·키보드·safe inset을 다시 계산한다. 짧은 콘텐츠에서는 불필요하게 3개 단계를 모두 강제하지 않음. 경계가 겹치면 중복 snap 삭제; 중간/전체/최소보다 콘텐츠 조건 우선. Full에서도 시스템 상태바·OS navigation inset 보호.

### Interactions
Handle은 항상 Drag, Scrollable Body는 **최상단↓/최하단↑에서 drag로 연속 인계**하며 손가락을 실시간 추적, 놓은 높이의 nearest snap으로 정착한다. 정확한 동작·Cancel은 [C-GESTURE](gesture.md)가 정본. Backdrop/Close/Esc/Back 닫기. 입력 중 변경사항이 사라지는 고위험 플로우는 확인/Close 제어를 제공하고 임의 Swipe-to-dismiss 비활성화 검토.

### Fixed CTA / Text
Footer는 primary+neutral secondary 또는 단일 primary, label은 명확한 동사. 16dp 컨트롤 radius, 버튼 높이 같고 Safe Area **한 번만** 계산. 텍스트 계층은 Header Section → Body/Meta → CTA. Footer 위 컨텐츠가 뒤에 가려지지 않도록 padding 책임 소유.

### QA
min→mid→full→mid→min→closed, pointercancel, backdrop, Escape, keyboard Tab loop, long Korean text, 200% font, IME, scroll to last field, reduced motion. **현재 Web 시연을 Android 작동 검증으로 간주하지 않음.**

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
