---
id: F-ICON-MOTION
category: foundation
status: proposed
read_when: SVG 선·광학 크기·활성 아이콘·모션·동작 줄이기
depends_on: [F-COLOR, F-GEOMETRY]
references: [REF-001, REF-002, REF-004, REF-005]
---

# 아이콘과 모션

## 아이콘

[Soft SVG 56종](../../assets/icons/soft/v0.14/README.md)은 사용자 승인 원본이다. 24 viewBox, stroke 1.8, round cap/join, 기본 fill none, currentColor. 광학 중심과 실제 도형(약 18–20)을 맞추되 원본을 임의 수정하거나 다른 스타일·획을 섞지 않는다. 3D 보상·마스코트는 별도 아트다.

상단은 Outline(D-011), 하단의 승인 매핑은 별도 Fill/Outline + 강조/라벨(D-015, [C-BOTTOM](../components/bottom-navigation.md)). Fill이 없는 아이콘을 강제 채우지 않는다. 상단 1.59rem, 하단 1.56938rem은 승인 웹 렌더 기준이며 기존 Native 아이콘·탭 목적지는 별도다. 일반 아이콘과 버튼 크기·슬롯은 F-GEOMETRY/C-SVG/P-BTN-ICON을 따른다. 장식은 읽기에서 제외하고 단독 조작의 의미 있는 이름과 고정 터치영역을 보호한다.

## 모션

| 역할 | 기준 | 표현 |
|---|---|---|
| Press | 120ms | 즉각적인 면/색 반응 |
| Selection / Switch | 160ms | 선택 면·thumb 이동 |
| Snackbar | 240ms 등장, 180ms 퇴장 후보 | 짧은 이동+fade |
| Sheet / Dialog | 320ms 등장, 180ms 퇴장 후보 | Sheet 이동, Dialog 미세 scale, scrim fade |
| Reward | 600–1200ms 후보 | 실제 성취에서 선택적 연출 |

기본 조작은 `cubic-bezier(.2,.8,.2,1)` 감속 곡선 후보. 버튼의 **보이는 face만 0.98**, Filter Chip face 0.96, 아이콘 glyph 0.94로 미세 축소할 수 있다. 외곽 layout/target·label 중심·padding·border·weight는 고정한다. 클릭 영역 자체나 로딩 중에는 scale 금지. 새 수치는 [후보 수치](../implementation/v015-tokens.json)의 `refinements`; 기존 Press/Selection 시간은 유지한다.

결과와 입력 상태는 연출보다 먼저 확정하고 중복 실행을 차단한다. 닫기·초점 회복을 애니메이션 종료까지 지연하지 않는다. 로딩은 완료로 위장하지 않고 동작 감소 시 즉시 상태/정적 결과를 제공한다. 일상 목록에 반복 광선·흔들림·파티클을 두지 않고 보상 연출은 건너뛰기 가능하게 한다. 사용자가 이해할 정보는 모션 없이도 전달한다.

표현 후보는 갓생알리미 소스 비교 및 이전 웹 코드에 근거한다. 새 앱의 실제 접근성·렌더 승인 아님. [이전 상세·검증 이력](https://github.com/jaksim0sec/questYourLife/blob/07bdd23ac0da46033b45508767fa5e62643a1931/seed/foundations/icons-motion.md); [에셋 계약](../../docs/assets/README.md).

**v5 visual review:** Match the perceived weight and optical size of icons to adjacent type; small/thin isolated symbols are not acceptable. Original approved SVG paths and Fill pairs remain unchanged. The review's 2.05 outline stroke is a presentation candidate, not a rewrite of the 56 source assets. The new shell uses the approved asset masks and their role-specific sizes; further icon stroke review is not implied by this implementation.

**D-019 active navigation:** Standard header glyph box 26, Root 1.75rem (28px at base font size), and bottom navigation 30 are working review dimensions. Use existing approved SVG paths and independent Fill assets, no per-icon optical hacks. Root outline stroke 2.1 is a scoped review candidate implemented by a derived bell variant, with the original 1.8 source retained; all visible bottom icons are achromatic. Earlier D-015 label and size values remain historical.
