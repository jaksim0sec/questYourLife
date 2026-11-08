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

### Selection
- C-RARITY는 **아이템 이미지 프레임의 부수적 등급 배경·경계**만 담당. 보유/선택/장착 상태는 O-ITEM State, 실제 물체는 C-IMAGE contain. 고유색은 의미 확인 후 사용, candidate 색상군을 승인된 실제 확률·등급으로 오인 금지.
- Selected outline은 border box를 변경하지 않으며 장착은 외부 Text로 별도 표시. 원본 item art는 안전영역 10~15% candidate 기준으로 보정.

### Don't
1. 희귀도 금색을 버튼·헤더·일반 Surface에 퍼뜨려 모든 것을 보상처럼 표현.
2. 선택 체크·장착·희귀도/이벤트 배지를 프레임 모서리에 3~4개 중첩해 상태 읽기 불가.
3. selected border-width 변화로 그리드가 흔들리거나 cap 챙이 clip됨.

### Relations
- **허용:** O-ITEM Media→C-IMAGE + C-RARITY frame; C-STATUS 장착 상태 외부 label; A-ART A1 진열.
- **금지:** C-RARITY가 소유 여부·구매 가격·보상 확률 결정, A3 획득 이펙트를 일상 Tile에 적용.

### Verification
- 정상 별도 Selected+Equipped 텍스트 vs Don't 상태 뱃지 3중 중첩/clip. 320/390/430 light/dark에서 object-fit, focus, border box 전후 폭과 label 전달·image failure 확인.

## 이력

> 과거 규격의 수치/사례/출처 원문을 보존한다. 위 4계약과 후보 상태를 우선 적용하며 이전 수치를 자동 승인하지 않는다.

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
