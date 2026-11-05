---
id: C-SHEET
category: component
status: proposed
read_when: 부분/중간/전체 펼침 및 Footer/Body/드래그
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR, C-GESTURE, C-ACTION]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Bottom Sheet · adaptive snap points

## Purpose & Source
기존 C-OVERLAY에서 독립 상태·제스처·Footer 계약을 분리. SEED [Bottom Sheet](https://seed-design.io/components/bottom-sheet)의 Backdrop/Container/Header/Close/Footer 및 snap point 철학을 참조하되, 이 프로젝트의 동작 수치는 자체 제안이다.

## Anatomy
Backdrop + Container + optional Header(title/description) + Handle + Scrollable Body + optional Fixed Footer(Action Group) + optional Close. Body만 scroll, CTA/Footer는 본문 스크롤에서 제외. CTA가 없으면 빈 하단 액션 영역 금지.

## Snap States
`closed`, `min`(내용 기반; v0.15 시연은 185px floor·40vh/380px cap), `mid`(가용 높이의 약 65%), `full`(가용 높이에서 OS inset을 제외한 전체). 실기기에선 내용 높이·키보드·safe inset을 다시 계산한다. 짧은 콘텐츠에서는 불필요하게 3개 단계를 모두 강제하지 않음. 경계가 겹치면 중복 snap 삭제; 중간/전체/최소보다 콘텐츠 조건 우선. Full에서도 시스템 상태바·OS navigation inset 보호.

## Interactions
Handle은 항상 Drag, Scrollable Body는 **최상단↓/최하단↑에서 drag로 연속 인계**하며 손가락을 실시간 추적, 놓은 높이의 nearest snap으로 정착한다. 정확한 동작·Cancel은 [C-GESTURE](gesture.md)가 정본. Backdrop/Close/Esc/Back 닫기. 입력 중 변경사항이 사라지는 고위험 플로우는 확인/Close 제어를 제공하고 임의 Swipe-to-dismiss 비활성화 검토.

## Fixed CTA / Text
Footer는 primary+neutral secondary 또는 단일 primary, label은 명확한 동사. 16dp 컨트롤 radius, 버튼 높이 같고 Safe Area **한 번만** 계산. 텍스트 계층은 Header Section → Body/Meta → CTA. Footer 위 컨텐츠가 뒤에 가려지지 않도록 padding 책임 소유.

## QA
min→mid→full→mid→min→closed, pointercancel, backdrop, Escape, keyboard Tab loop, long Korean text, 200% font, IME, scroll to last field, reduced motion. **현재 Web 시연을 Android 작동 검증으로 간주하지 않음.**

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
