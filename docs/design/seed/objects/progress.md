---
id: O-PROGRESS
category: object
status: proposed
read_when: 일일·공동·보상 기록의 하나의 대표 수치
depends_on: [O-README, C-STATUS, C-TEXT]
references: [D-012, D-013]
---

# O-PROGRESS · 같은 값에서 표현하는 진행

## 현행 계약

| 슬롯 | 책임 |
|---|---|
| Label | 무엇의 현재 진행인지 |
| **대표값** | **현재/목표 한 쌍**이 주연 |
| Bar | 값과 같은 입력의 실제 DOM fill |
| Deadline | 유효한 마감이 있을 때만 |
| Derived | %, 남은 양 등 파생 정보. 대표값보다 약하게 |
| Action | 실제 상세가 있으면 Low 링크 1개 |

`current/target` 데이터 하나에서 텍스트, fill 비율, 남은 양, `aria-valuemin/max/now` + `aria-valuetext`를 생성한다. fill은 `target>0 ? clamp(current/target,0,1) : loading/unavailable`이다. 목표 초과는 **실제 current**를 화면 글자와 `aria-valuetext`에 유지하지만, ARIA Progress의 `aria-valuenow`와 바는 `min(current,target)`으로 제한한다. `aria-valuemax=target`을 넘는 now값은 불허한다. 자동 지급 완료를 암시하지 않는다.

**공용 트랙의 기본/마이크로/강조 두께는 C-STATUS만이 소유**한다. O-PROGRESS는 그 Variant를 선택하며 클랜 전용 트랙 두께 CSS 신설 금지. 내부 span을 `display:block;height:100%`로 실제 측정한다.

대표값이 **파생값보다 약하면 실패**. O-PROGRESS를 이벤트/홍보 A2 배너 **안에 넣지 않는다**. 로딩, 값 0, 조회 실패, stale은 같은 0%가 아니다. 비클릭 Progress는 눌리는 표현을 갖지 않는다.

**R-01 image7:** '완료까지 380'이 `620 / 1,000`보다 크게 보이는 위계 역전. **R-04:** 수치는 620인데 bar 회색으로 비어 있음. P0-4 검증은 실제 Chromium fill/track `0.62±.01`·ARIA 620(320/390/430 × light/dark)이었지만, 0/1000·1000/1000·1200/1000·loading 등 P4 검증을 대체하지 않는다.

## 이력

- r1의 진행 슬롯은 C-STATUS Badge와 별도 새 Progress 컴포넌트를 만들지 않고 의미 객체로 승격했다.
