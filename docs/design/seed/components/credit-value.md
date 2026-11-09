---
id: C-CREDIT
category: component
status: proposed
read_when: 캐시 금액/가격/획득 표시용 코인+수치
depends_on: [F-TYPE, F-COLOR, F-GEOMETRY, C-IMAGE]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Credit Value


## 현행 계약

C-CREDIT은 **실제 캐시 단위의 수치와 원본 코인 이미지** 표현만 소유한다. 도메인 지급·가격 정책을 새로 만들지 않는다. O-ITEM Value가 가격과 보유/예상/획득의 **의미**를 소유하며 표시 포맷만 C-CREDIT에 위임한다.

### Selection

| 원장 상태 | 표시 | 배제 |
|---|---|---|
| confirmed balance | '보유 240 캐시' + 원본 코인 | 그냥 '240' |
| expected earn | '예상 획득 +240 캐시' | 실제 지급처럼 금색 성공 Badge |
| confirmed earned | '획득 +240 캐시' | pending와 구별 없는 결과 |
| price (정해진 실제 가격) | '가격 240 캐시' | 보유액과 동일 라벨 |
| pending/unavailable/null | 확인 중/정보 없음 | null→0 캐시 강제 변환 |

검수에서 기존 코인 래스터가 없으면 **코인 자리만 대체 그림으로 만들지 말고 텍스트로만 표시해 미검증** 처리한다. Small 17/12·Medium 21/14·Large 26/18은 이전 실험 후보로 보존하되 렌더 결과 없이 승인하지 않는다.

### Don't

1. 상점 가격 '240'과 보유 수치 '240'을 둘 다 금색 코인만 붙여 내 계좌인지 가격인지 구별 불가.
2. 아직 확정되지 않은 예상 보상을 '+240 획득 완료'로 쓰거나 null을 `0`으로 변환.
3. 캐시 원본이 없다는 이유로 새 SVG/이모지 코인을 만들어 사용자 승인 자산인 척 사용.

### Relations

- **부모:** O-ITEM Value, Reward Result의 실제 캐시 행, Page Summary의 원장값.
- **자식:** 검증된 기존 코인 이미지(있을 때만) + locale 숫자 + 단위/부호. 이미지 alt는 중복 읽기 방지.
- **형제:** O-ITEM State(보유/장착/획득) 및 별도 가격/조건 라벨. Text semantics는 C-TEXT.
- **금지:** C-RARITY/Surface로 임의 금색 경제 UI 확산, C-CREDIT 안에 구매 버튼/새 재화 타입 정의.

### Verification

- 0/999/1,000,000/null/pending/expected/confirmed/price 값 각각 의미 라벨 검수.
- 320/390/430×light/dark, 텍스트 확대200%·원본 코인 실패시 수치·단위·Identity 유지.
- pending를 소유 완료로 오인하지 않는지, 원본 코인 파일 검증 결과를 별도 기록.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Anatomy
**기존 캐시** 표시를 Coin Image + Numeric Value + optional Unit 또는 +Sign 한 덩어리로 렌더. 새 화폐를 만들지 않는다. 첨부된 투명 골드 마스코트 코인 이미지를 그대로 쓰며 SVG·이모지·임의 C 문양으로 치환하지 않는다. 이미지 바이너리는 본 docs 커밋에 포함되지 않았으므로 실제 Android 자산 경로는 연결 전에 확인한다.

### Properties
`amount:number`, `sign:neutral|earned`, `size:small|medium|large`, `showUnit:boolean`, `state:available|pending|unavailable`. 가격, 보유량, 주간 획득량의 **의미 있는 라벨**은 외부 슬롯에서 제공하며 합치지 않는다.

### Sizing & Typography
Small 코인 17 / 글자 12·800, Medium 21 / 14·800, Large 26 / 18·800 (dp/sp 후보). 아이콘–값 gap=5. 숫자 tabular-nums 사용, 텍스트와 기준선 광학 정렬, 로딩/증감 시 위치 점프 금지.

### States & Constraints
Default/Pending/Unavailable; 실제 확정 수령 전 지급 완료 문구 금지. 0과 미확인 값은 다르다. 큰 숫자는 locale grouping/ellipsis가 아닌 reflow로 처리. 이미지는 꾸미기 아이템 그리드와 달리 텍스트 주변 장식이다.

### Accessibility / QA
대체 텍스트는 장식 이미지로 숨기고 요소 전체 accessible name에 '240 캐시' 등 단위를 제공. 320/390 폭, dark/light, 숫자 0/999/1000000, 200% 폰트, 이미지 폴백 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

### K08 재화 표시의 맥락

금액은 가격·보유·예상 획득·실제 획득을 라벨로 구분한다. null을 0으로 바꾸지 않는다. 실제 캐시 래스터를 쓰고 SVG·이모지로 새 코인을 만들지 않는다. 이번 제품 보상 구성에는 미정 가격을 생성하지 않는다. 검수 fixture에 기존 캐시 원본을 준비하지 못했다면 단위 텍스트만 쓰고 이미지 검증을 미완료로 남긴다.
