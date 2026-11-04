---
id: S-PAGE
category: shell
status: proposed
read_when: Root·Detail·Utility·Immersive·Overlay, 헤더·하단바·키보드·스크롤
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP]
references: [REF-001, REF-002, REF-004, REF-005, D-011, SRC-TDS-BOTTOM]
---

# 페이지 골격과 안전영역

## 정본
상단 시각·슬롯은 [TOP_NAVIGATION](../../TOP_NAVIGATION.md)의 D-011 정본을 직접 참조한다. 이 문서는 이를 재정의하지 않는다. Root 좌측 굵은 제목/Outline 액션, Standard Back 또는 Close/중앙 제목/맥락별 보조 액션을 보존한다. 컨셉의 좌측 상세 제목·로고 헤더·다크 헤더는 그 자체로 승인된 예외가 아니다.

## Shell 선택
| 유형 | 헤더·탐색 | 스크롤·행동 | 지위 |
|---|---|---|---|
| Root | 확정 Root 상단, 하단 목적지는 IA 합의 시에만 | 한 세로 스크롤, 생성 등 행동은 우선 inline | 상단 구조 approved / 나머지 proposed |
| Detail | 확정 Standard 상단, Back은 이전 맥락 복귀 | 콘텐츠 + 필요 시 지속 CTA, 하단바 숨김은 플로우별 검토 | proposed, 실제 history 보존 필요 |
| Utility | 작업 맥락의 Standard, Back/Close 의미 분리 | 폼·설정, 키보드와 CTA의 관계 명시 | proposed |
| Immersive | 보상 아트 등 목적이 있는 집중 표현, 명료한 닫기 | 시각 연출과 결과 텍스트 분리, 필요 시 건너뛰기 | proposed, 새 게임 모드 의미 아님 |
| Overlay | 원래 페이지 맥락 위 Sheet/Dialog | modal 여부·초점·dismiss 규칙 명시 | proposed |

Detail/Utility를 무조건 모든 탭의 바깥으로 빼는 정책을 확정하지 않는다. 최종 정보구조·딥링크·DM 접근은 O-003에 남아 있다. 특정 화면 이름이 Root인지 Detail인지 컨셉만 보고 잠그지 않는다.

## 높이·공간 제안
상단 content 최소 높이는 size.topBarMin, 하단바 content는 size.bottomBarMin을 출발점으로 쓰되 동적 글자·플랫폼에서 실제 렌더 높이를 사용한다. Header와 OS status inset을 분리한다. ROOT 제목·오른쪽 액션의 높이를 독립 조절해 기준선이 흔들리게 하지 않는다.

콘텐츠는 page.inset과 content.maxWidth를 쓰고 화면은 한 주 세로 scroll을 기본으로 한다. 탭 패널마다 스크롤·필터 상태를 유지할지 실제 플로우에서 검토한다. 독립 horizontal rail은 허용하되 읽기·접근 순서를 유지한다.

## 하단 공간 계산의 책임
Shell 한 곳이 Bottom Navigation·fixed CTA·OS bottom inset을 소유한다.
- Root + 하단바만: 실제 하단바 content 높이 + OS bottom inset.
- fixed CTA만: 실제 CTA wrapper 높이 + OS bottom inset.
- 승인된 공존 사례: 실제 하단바 content + 실제 CTA wrapper + OS bottom inset 한 번. 마지막 콘텐츠와 포커스 target도 이 영역 위로 스크롤 가능하게 예약한다.
- 두 wrapper가 각각 safe inset을 추가하지 않는다. Root의 상시 대형 CTA 공존을 기본값으로 만들지 않는다.
- 키보드가 열리면 실제 visual viewport/IME inset을 기준으로 재계산한다. 화면이 이미 줄어드는 resize 모드에서 IME 높이를 다시 더하지 않는다. 입력과 오류·확정 조작이 보이는지 검토한다.
- Snackbar·Sheet는 CTA·키보드와 겹치지 않게 하고 빈 safe-area를 장식 콘텐츠로 채우지 않는다.

## Shell 검증 매트릭스 · 하단 간섭 문제

| Shell | Bottom Nav | Fixed CTA | 실제 콘텐츠 패딩 계산 |
|---|---|---|---|
| Root | 표시 후보 | 일반적으로 inline | bottom bar content height + OS safe inset |
| Detail | 플로우별 숨김 후보 | 중요 완료 행동이 있으면 | CTA wrapper height + OS inset |
| Utility / 입력 | 필요 맥락에서만 | 키보드 위에서 접근 가능할 때만 | VisualViewport/IME resize 모드 중 하나를 기준으로 1회 |
| Root + 예외적 Fixed CTA | 승인된 경우만 | 있으면 | bar + CTA + OS inset **한 번** |
| Immersive / 보상 | 숨김 후보 | 이벤트 결과 받기 등 필요한 경우 | 실제 action height + inset, 모션 건너뛰기 제공 |

S2-04 반례 02와 S2-05의 하단 CTA 충돌은 Shell 책임 문제다. 버튼 테두리·라운딩만 수정해 해결하지 않는다. 하단 둘의 동시 공존 자체를 자동 규칙으로 만들지 말고 콘텐츠 목적과 safe-area에서 판단한다. 하단 아이콘·활성 상태의 독립 스펙은 [C-BOTTOM](../components/bottom-navigation.md)이다.

## Bottom Navigation 제안 경계
기본은 간단한 Outline glyph + 짧은 Label + 의미 있는 selected 표시. active Fill은 승인 전 후보이며 탭 수·목적지·중앙 +·라벨·최종 표시 정책은 모두 미결이다. 생성 +는 목적지와 실행의 의미가 달라 중간 탭으로 자동 채택하지 않는다. 아트 속 4탭·5탭을 공통 시스템으로 복사하지 않는다.

## 충돌·검증
다크 테마 콘텐츠 후보와 D-011의 밝은 중립 헤더 사이의 차이는 O-022에 기록했다. 기존 확정안은 보존하고 다크 전역 헤더 적용은 보류한다. 웹/네이티브 inset 처리·scroll restoration·키보드·deep link는 미검증이다. 320–430 폭·200% 글자·긴 제목·Back/Close·마지막 항목·fixed CTA 조건을 실제 웹 시연에서 확인한다.

