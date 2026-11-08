---
id: F-TYPE
category: foundation
status: proposed
read_when: 제목·본문·숫자 위계, 한글 줄바꿈, 글자 확대
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-002, REF-004, REF-005]
---

# F-TYPE · 수치 스케일

## 현행 계약

텍스트의 **의미 역할**은 C-TEXT만이 소유한다. F-TYPE은 수치 스케일 및 가독성 하한만 소유한다.

| 스케일 | font/line px · 루트16 기준 | weight | 상태 |
|---|---|---:|---|
| root | 26/34 | 700 | candidate (P2 비교) |
| detail | 18/26 | 600 | candidate |
| hero / number | 24/32 | 700 | candidate |
| section | 18/26 | 700 | candidate |
| item / numberSmall | 16/24 | 600 | candidate |
| body | 16/24 | 400 | candidate |
| support | 14/20 | 400 | candidate |
| caption | 12/18 | 500 | candidate |
| button | 14/20 | 600 | candidate |
| buttonCompact | 12/18 | 600 | candidate |

**r3 approved 필수 가독성:** 계산된 글자 크기는 최소 **12px**. `.57rem`처럼 10px 미만을 만들 수 있는 표현 금지. **14px 미만 글자의 font-weight은 600 이하**. 작은 글자를 790/850으로 보상할 수 없다. 필요하면 행 높이와 구조를 바꾼다.

Root Title/Standard Title/Header 높이/Bottom 높이의 기존 후보는 P2 병치 실측 전 확정 금지. 기존 RootFont 확대 시험은 웹 preview 전용이며 Android 승인값이 아니다. **반례 R-10:** 기여 Badge 9.7px/790(390/17), 아이템 라벨 9.4px(355/16.5). 측정 조건 390/17, 355/16.5, 355/16은 혼합 금지.

### P2 비교 fixture 판정 입력
- **Root:** 기존 21.4/850/-0.05em(측정), 제안 26/700(후보), 당근 18/700(외부 비교). 디자인 승인값으로 전환 전 320/390/430의 header wrap/주연 위치를 측정.
- **Standard:** 기존 14.8/850(측정)은 과중량 반례 후보, detail 18/600은 후보. Hero 22~24·설명12~13·배지12/600·Meta12~13을 일괄 복제하지 말고 역할과 12px 하한으로 개별 판정.
- 200% 글자 확대 시 고정 height로 자르지 않는다. 후보값을 `approved`로 바꾸려면 before/after computed proof가 필요.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
