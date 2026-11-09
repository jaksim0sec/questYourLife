---
id: C-RARITY
category: component
status: proposed
read_when: 아이템 등급별 미세 그라데이션·선택·다크 상태
depends_on: [F-COLOR, F-GEOMETRY, C-IMAGE, A-ART]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Item Rarity Surface


## 현행 계약

C-RARITY는 **O-ITEM 미디어 슬롯의 보조 프레임**이다. 실제 보유/선택/장착은 O-ITEM State, 원본 아트는 A-ART, fit/ratio는 C-IMAGE가 담당한다. 등급·색상 후보를 제품의 실제 확률·지급 정책으로 승격하지 않는다.

### Selection

| 맥락 | 선택 | 배제 |
|---|---|---|
| 보관함·꾸미기 선택 | 희귀도 프레임 후보 + 명확한 Owned/Equipped/Selected 텍스트 | 등급 색만으로 장착 표시 |
| 보상 미리보기 | 아이템 contain + 획득 조건이 주연, 등급은 낮은 보조 | 잠김을 '보유'처럼 표시 |
| 획득 결과 | A3 결과 장면에서 한정된 강조 | 일상 A1에서 파티클/광선 |
| 알려지지 않은 등급 | Plain/C-IMAGE만 | 임의 Rare/Legendary 라벨 |

Common~Special 6색 그라데이션은 **proposed 후보**. 실제 색 구별성과 명도는 light/dark 개별로 확인. Selected는 outline만, 상태/희귀도 겹치면 현재 조작→장착 사실→희귀도 순으로 구분.

### Don't

1. 선택된 Blue Cap에 희귀도 프레임·체크·장착·할인 리본·광선을 동시에 올려 Identity를 가림.
2. Item 등급 그라데이션을 C-SURFACE의 일반 Card로 퍼뜨려 모든 화면을 금색으로 만듦.
3. 물체의 크롭과 위치가 아이템마다 달라 같은 1:1 진열에서 모자 끝이 잘리거나 그림자 높이가 튐.

### Relations

- **부모:** O-ITEM의 Media 프레임 (보상/상점/보관함/꾸미기).
- **자식:** C-IMAGE(contain), 희귀도 border/배경, 선택 outline, 최대 조건부 상태 표시.
- **형제:** 바깥 Identity/State/Value/Action. 등급 label은 텍스트로 제공하고 색만으로 의미 전달 금지.
- **금지:** C-RARITY 안에 독립 Button/가격 텍스트, A3 전용 이펙트를 일반 surface에 도입.

### Verification

- 동일 Blue Cap의 보상 preview(획득 조건) vs 꾸미기(보유/장착+실제 action)는 다른 슬롯을 보여야 한다.
- rarity 색·selected·equipped·disabled 조합 normal/Don't 비교, 320/390/430×light/dark 안전영역/crop.
- Image fail 시 Identity/Value/State 유지, 색만으로 등급 구별 금지.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

### Purpose / Separation
**일반 Card가 아닌 아이템 아트 전용 프레임**. 이미지 자체는 C-IMAGE, 광원·재질은 A-ART. 이 문서는 배경/경계/선택만 소유하며 등급 이름·표기 정책·실제 아이템 희소도 수치가 확정된 것은 아니다.

### Anatomy
One Frame + one Item art (contain) + optional Selected/Equipped corner badge **outside image slot**. 프레임 안에 등급명 텍스트 삽입하지 않는다. 기본 radius 16dp. 10–15% 안전 여백, 비니/헤드폰/캡의 돌출부 crop 금지.

### Candidate Color Families (Light)
| Tier placeholder | Gradient Start | End | Border |
|---|---|---|---|
| Common | #FAFAFA | #EDEDF1 | #E4E4E8 |
| Uncommon | #F5FCF7 | #E2F2E8 | #CCE7D6 |
| Rare | #F6F9FF | #E0ECFF | #CBDCF8 |
| Epic | #FCF8FF | #EDDEFA | #DFCEF4 |
| Legendary | #FFFCF3 | #FFE9BD | #EFD5A0 |
| Special | #FFF7F7 | #FFE2E7 | #F3CBD2 |

모든 그라데이션은 145–148deg로 2색 사이의 작은 명도 차이. 네온/파티클 기본 금지. Dark는 채도·명도 보정 별도 매핑(일률 brightness 필터 금지). Root 브랜드색 변경은 등급의 **고유 색**을 바꾸지 않으며 Selected outline·focus만 브랜드 토큰을 사용.

### States / Animation
Default, Hover/Pressed(110~180ms 색 변화), Selected(동일 border box + 외곽 outline), Owned/Equipped(독립 의미 badge), Disabled/Unavailable. A1 진열은 정적, 획득 결과 A3만 700~1000ms 선택적 모션. Selected에서 border-width 변경으로 아이템이 튀지 않아야 한다.

### Accessibility
등급·장착·선택은 배경색만으로 전달하지 않고 컨테이너 외부 텍스트/accessible label로 표현. 사용자가 요구하지 않은 확률·가치·유료 구매를 디자인에서 도입하지 않음.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
