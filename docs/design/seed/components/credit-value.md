---
id: C-CREDIT
category: component
status: proposed
read_when: 캐시 금액/가격/획득 표시용 코인+수치
depends_on: [F-TYPE, F-COLOR, F-GEOMETRY, C-IMAGE]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Credit Value

## Purpose / Anatomy
**기존 캐시** 표시를 Coin Image + Numeric Value + optional Unit 또는 +Sign 한 덩어리로 렌더. 새 화폐를 만들지 않는다. 첨부된 투명 골드 마스코트 코인 이미지를 그대로 쓰며 SVG·이모지·임의 C 문양으로 치환하지 않는다. 이미지 바이너리는 본 docs 커밋에 포함되지 않았으므로 실제 Android 자산 경로는 연결 전에 확인한다.

## Properties
`amount:number`, `sign:neutral|earned`, `size:small|medium|large`, `showUnit:boolean`, `state:available|pending|unavailable`. 가격, 보유량, 주간 획득량의 **의미 있는 라벨**은 외부 슬롯에서 제공하며 합치지 않는다.

## Sizing & Typography
Small 코인 17 / 글자 12·800, Medium 21 / 14·800, Large 26 / 18·800 (dp/sp 후보). 아이콘–값 gap=5. 숫자 tabular-nums 사용, 텍스트와 기준선 광학 정렬, 로딩/증감 시 위치 점프 금지.

## States & Constraints
Default/Pending/Unavailable; 실제 확정 수령 전 지급 완료 문구 금지. 0과 미확인 값은 다르다. 큰 숫자는 locale grouping/ellipsis가 아닌 reflow로 처리. 이미지는 꾸미기 아이템 그리드와 달리 텍스트 주변 장식이다.

## Accessibility / QA
대체 텍스트는 장식 이미지로 숨기고 요소 전체 accessible name에 '240 캐시' 등 단위를 제공. 320/390 폭, dark/light, 숫자 0/999/1000000, 200% 폰트, 이미지 폴백 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K08 재화 표시의 맥락

금액은 가격·보유·예상 획득·실제 획득을 라벨로 구분한다. null을 0으로 바꾸지 않는다. 실제 캐시 래스터를 쓰고 SVG·이모지로 새 코인을 만들지 않는다. 이번 제품 보상 구성에는 미정 가격을 생성하지 않는다. 검수 fixture에 기존 캐시 원본을 준비하지 못했다면 단위 텍스트만 쓰고 이미지 검증을 미완료로 남긴다.
