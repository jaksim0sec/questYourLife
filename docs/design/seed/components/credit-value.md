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

## 웹 공통 구현 · 실제 코인/금액/패널 사용

**정본은 기존 C-CREDIT이며 신규 재화/패널 컴포넌트가 아니다.** 웹 공유 스타일 `web/preview/seed-standard.css`가 `.seed-credit`의 내부 코인 이미지·금액 정렬과 기존 `small/medium/large`, `available/pending/unavailable`를 소유한다. Lab의 실제 소비 예시는 `web/seed-lab/index.html`의 `#family-credit-panel`이다.

- **이미지:** 보존된 원본 `assets/art/rewards/cash-coin-single.png` 하나를 `.seed-credit__coin`에 사용한다. 기존 3D 코인 원본을 `coins.svg`, Emoji, 글자 C, 신규 코인 도형으로 대체하지 않는다.
- **수치:** `.seed-credit__value`가 실제 값·천 단위 구분·tabular 숫자를 표시하고 `C-CREDIT`에서 `small=17/12`, `medium=21/14`, `large=26/18`(코인/글자, 기본 16px root 기준) 비율을 재사용한다. 가격·보유·획득·미확인은 외부 Label/데이터로 구분한다. 화면 캡처의 `3,240`은 **시연값**이며 실제 잔액을 뜻하지 않는다.
- **패널 삽입:** '보유 캐시'처럼 하나의 정보 그룹을 별도 표면에 넣어야 한다면 **기존 `C-SURFACE`(`seed-surface[data-surface="subtle"]` 등) 내부에 `C-TEXT` 라벨 + `C-CREDIT`을 함께 배치**한다. 독립 패널을 항상 강제하지 않고, 상단 내비의 짧은 읽기 전용 정보 슬롯에서는 표면 없이 단일 `C-CREDIT`을 사용할 수 있다. '라벨/금액을 그냥 문자 두 개로 둔 것'은 원본 코인 패널 구현이 아니다.
- **접근성·상태:** 코인 이미지는 장식(`alt=""`)이며 단일 표시 그룹에 '보유 캐시 3,240'처럼 의미를 부여한다. 읽기 전용 잔액은 클릭/화살표/버튼 hover를 부여하지 않는다. 값이 미확인이면 0으로 대체하지 않고 `unavailable`와 '조회 불가'를 사용한다.

```html
<div class="seed-surface" data-surface="subtle">
  <span class="seed-text" data-text-role="caption" data-text-style="support">내 보유 캐시 · 시연값</span>
  <span class="seed-credit" data-size="medium" data-state="available"
        role="img" aria-label="예시 보유 캐시 3,240">
    <img class="seed-credit__coin" src="../../assets/art/rewards/cash-coin-single.png" alt="">
    <span class="seed-credit__value">3,240</span>
  </span>
</div>
```

> 경로는 `web/seed-lab/`을 기준으로 한 실제 상대 경로다. 다른 위치의 화면은 **자신의 URL 위치를 기준으로** 공통 에셋을 올바르게 인용해야 한다. 카드/배너/레이아웃 모양과 화폐 정책은 여기서 추가 확정하지 않는다.

## K08 재화 표시의 맥락

금액은 가격·보유·예상 획득·실제 획득을 라벨로 구분한다. null을 0으로 바꾸지 않는다. 실제 캐시 래스터를 쓰고 SVG·이모지로 새 코인을 만들지 않는다. 이번 제품 보상 구성에는 미정 가격을 생성하지 않는다. 검수 fixture에 기존 캐시 원본을 준비하지 못했다면 단위 텍스트만 쓰고 이미지 검증을 미완료로 남긴다.
