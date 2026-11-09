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

Sheet는 화면을 떠나지 않고 제한된 선택/정보를 다룬다. 4유형의 '바텀시트페이지'는 [page-types](../pages/page-types.md) 소유이며, Dialog는 C-OVERLAY 소유다. 드래그와 스크롤 이전 규격은 [C-GESTURE](gesture.md)를 따른다.

### Selection

| 질문 | 선택 | 배제 |
|---|---|---|
| 짧고 현재 화면 위에서 끝나는 과업인가 | Sheet(필요 최소 높이) | 별도 독립 Page |
| 장문의 입력·미저장 정보가 있나 | Non-global Page 또는 손실 확인 Sheet | 드래그 즉시 닫기 |
| 중대한 파괴 결정을 확인하나 | Dialog | Snap Sheet로 숨김 |
| 배경 맥락 보존이 중요한 짧은 선택인가 | 바텀시트페이지 | 글로벌이너 라우트 남발 |
| 콘텐츠가 하나의 높이로 충분한가 | **single snap** | 무조건 min/mid/full 세 단계 |

기존 `min` floor 185px·상한 40vh/380px, `mid` ~65%, `full`은 safe inset을 제외한 가용 공간이라는 웹 시연 조건을 유지. **Full 약 90%, Snap 2~3개는 후보**로 실측해 선택하고 자동 승인하지 않는다.

### Don't

1. 짧은 선택지를 위한 Sheet에 Full·Mid·Min을 무조건 모두 제공해 공간과 드래그를 낭비.
2. 편집 중 입력 손실 위험이 있는데 backdrop/드래그 한 번으로 닫혀 내용이 사라짐.
3. 시트 고정 Footer가 마지막 필드/OS safe area를 가려 실제 CTA로 접근할 수 없음.

### Relations

- **부모:** C-OVERLAY의 modal/nonmodal Layer, 바텀시트페이지의 배경 S-PAGE. 기존 하단바 활성 상태를 Sheet가 바꾸지 않는다.
- **자식:** Handle + Header + Scroll Body + optional Action Footer. Footer는 C-ACTION, drag는 C-GESTURE만 소유.
- **형제:** Background와 Backdrop/스크림, 필요한 Focus return target. 신호가 다른 Dialog/Snackbar와 동시에 과다 노출 금지.
- **금지:** Scroll Body 안에 Footer sticky를 중첩, 여러 snap을 data 없이 강제, Backdrop click을 보상 수령으로 전환.

### Verification

- 320/390/430×light/dark에서 snap 시작→드래그 중 높이 연속 반영→가장 가까운 유효 snap, PointerCancel 이전 안정 상태 복구.
- Sheet Open/Close/Escape/Back/Backdrop, Focus trap·복귀, 텍스트200%·IME·긴 글자·본문 스크롤과 Footer 보호.
- 위험 입력에서는 드래그 dismiss가 제한되는지, 사용자가 보이는 Close/Cancel 수단을 갖는지 별도 판정.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

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
