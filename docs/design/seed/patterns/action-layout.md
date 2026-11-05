---
id: P-ACTIONS
category: pattern
status: proposed
read_when: 버튼 그룹·수락/거절·CTA 비율·고정 하단 행동
depends_on: [C-ACTION, C-SELECTION, F-GEOMETRY]
references: [REF-005, REF-006, REF-007, SRC-TDS-BOTTOM]
---

# 행동 조합과 배치

## 판단 순서
동작 의미 → 되돌릴 수 있는지 → 대표 행동 → 인접 관계 → 버튼 family → 크기 → 폭을 정한다. 너비·브랜드색부터 결정하지 않는다. 같은 그룹의 버튼 높이·baseline·radius는 동일 size 정본을 쓴다.

## 재사용 패턴
| 상황 | 권장 조합 | 폭·위치 판단 | 금지 |
|---|---|---|---|
| 대표 행동 하나 | High 하나 | 영역 폭을 채울 실익이 있으면 fill | 중요도 낮은 버튼까지 모두 fill |
| 핵심 실행 + 부가 행동 | High + Medium/Low | 보조 hug + 핵심 fill 또는 조건별 equal/stack, 최소 폭 보장 | 콘텐츠와 의미를 보지 않고 임의 비율 고정 |
| 대등한 두 선택 | 같은 size의 두 선택 또는 동등 버튼 | 의미가 실제 대등할 때만 동일 폭 | 한쪽이 명백한 핵심인데 같은 강조 |
| 수락 + 거절/넘기기 | 수락 High/Medium + 거절 Low/Neutral | 짧고 명확한 라벨, 관련 콘텐츠 바로 아래; 좁으면 세로 | 수락/거절을 Segmented로 모델링 |
| 목록의 보조 실행 | Small/Medium Neutral Weak 또는 Ghost | suffix hug, 행 텍스트와 균형 | 검정 알약·새 radius로 단독 예외 |
| 위험 확정 + 취소 | 실제 파괴 행동 Critical + 취소 Neutral | 결과·복구 가능성을 먼저 설명 | 단순 넘기기를 빨간 위험 버튼 처리 |
| 폼 완료 | 한 대표 제출 + 필요한 취소 | 필드와 같은 gutter | 모든 입력 옆에 제각각 임시 버튼 |

**REF-005 반례 01은 특정 두 하단 버튼을 50:50으로 교정하라는 명시적 지시**다. 이 사례는 동일한 가로 너비·높이·radius·수직 기준선을 사용해야 한다. 다른 상황의 핵심/보조 폭은 내용 최소 폭과 중요도에서 결정한다. 50:50을 모든 쌍의 기본으로 강제하거나 금지하지 않는다. 두 버튼의 라벨·아이콘·padding이 들어갈 수 없으면 같은 폭을 억지로 유지하지 않고 세로 스택으로 바꾼다. 콘텐츠를 줄이거나 아이콘을 과대하게 키워 빈 공간을 메우지 않는다.

## Decision Rules · 실제 사용 상황별 분기

| 문제·조건 | 선택 | 예외·재검토 |
|---|---|---|
| 참조 S2-04 반례 01의 **같은 하단 두 버튼** | **equal(1fr/1fr)**. 두 요소의 visible width/height/radius/baseline을 맞춘다 | 라벨이 길면 텍스트 축소 대신 세로 배치. 다른 모든 두 버튼에 의무화하지 않음 |
| 실행/보조가 구별되고 두 라벨 모두 짧음 | 보조 hug + 주행동 fill 또는 의미를 보존한 비대칭 | 임의 30:70 숫자 고정, 보조를 과소 폭으로 압축 금지 |
| '수락 / 넘기기'처럼 선택의 의미가 상이 | 단일 액션 영역에서 수락 High(또는 Medium), 넘기기 Low/Neutral, 순서·시선·정렬 명확화 | 선택 도중 item 텍스트가 줄바꿈돼야 하면 CTA 두 개를 그 행에서 분리 또는 세로로 이동 |
| 상단 행동 + 리스트 내부 보조 | 작은 Neutral Weak / Ghost | 같은 기능의 버튼만 독자적인 검정 배경/둥근 모양을 얻지 않음 |
| Root + Bottom Navigation | 우선 inline/맥락 액션 검토 | 모든 Root에 대형 sticky CTA 동시 표시 금지 |
| Detail/Utility 최종 확정 | 필요할 때 fixed 단일 CTA 또는 의미상 Double | OS inset/keyboard/scroll은 S-PAGE 한 곳에서 소유 |

**상태 전환:** ready → pressed → pending → 성공/오류. pending 동안 버튼의 폭과 그룹 정렬은 유지한다. 성공 전에 UI를 완료로 단정하지 않고, 오류 시 사용자 입력을 보존해 다시 시도할 수 있어야 한다.

**반례 검증:** 좁은 320 폭, '이 퀘스트 제안 수락하기' 같은 긴 라벨, 200% 글자, 비활성·로딩을 개별 fixture로 비교한다. <50:50 교정>과 <의미상 비대칭>은 서로 다른 테스트다.

## v0.6 Button + SVG Pattern
[**P-BTN-ICON**](button-icon-layout.md)이 인라인 SVG와 텍스트의 분량·간격·로딩 상태를 정의한다. SVG 24 viewBox, 버튼 inline 보이는 크기 16~18dp, gap 6~8dp 후보. 로딩 시 Label 위치가 변경되면 실패. Pressed/Disabled에서 font-weight·border-width·padding을 변경하지 않는다. 50:50 교정 규칙은 기존 특정 사례로 유지.

## Bottom CTA
Single/Double과 fixed/inline은 서로 다른 축이다. 토스 공식 자료는 이 구분과 safe-area 처리의 근거이며 특정 비율·색상·수치는 이 시드의 제안이다.

Root에서 하단바와 경쟁하는 대형 fixed CTA는 기본 지양한다. 생성 액션은 우선 콘텐츠·헤더·맥락 안에서 의미에 맞게 배치한다. 집중 작업의 지속 실행이면 Detail/Utility에서 fixed CTA를 검토한다. 반드시 S-PAGE의 실제 높이·safe inset·키보드 규칙으로 공간을 예약한다. Bottom Navigation 위에 단순히 큰 버튼을 덧붙이는 것으로 끝내지 않는다.

## 상태·접근성
한 버튼이 Loading이어도 그룹 폭·순서·높이를 유지한다. 결과가 아직 확정되지 않았다면 다른 액션으로 중복 요청할 수 있는지 검토한다. Focus 순서는 읽는 순서·시각적 순서와 일치한다. 한 버튼만 비활성이면 이유를 가까이 제공한다. 320 폭·200% 글자에서 줄바꿈, target 비중첩, 마지막 콘텐츠 접근을 확인한다.

## 근거·영향
REF-005의 01/02/03/07/10 교정, REF-006의 제안, REF-007의 Primary/Weak 대비에서 도출한 조합 제안이다. 어떤 특정 화면의 CTA 문구·고정 위치·최종 탭 구성을 확정하지 않는다. S-PAGE와 함께 변경한다.

