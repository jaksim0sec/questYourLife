---
id: C-ACTION
category: component
status: proposed
read_when: 일반·아이콘·텍스트 버튼, 핵심·보조 행동, 취소·수락
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-005, REF-006, REF-007, SRC-SEED-BUTTON, SRC-TDS-BUTTON]
---

# Action Button family

명시적 실행에 사용하며 탐색·선택·읽기 전용 상태와 구분한다. Container + Label + 선택적 Prefix/Suffix 하나 + Loading. [C-ICON](icon-button.md)의 단독 아이콘 버튼과 [P-BTN-ICON](../patterns/button-icon-layout.md)의 내부 정렬을 재사용한다.

## 기존 승인 표현

| Variant | 강조 | 역할 |
|---|---|---|
| brand-solid | High | 대표 실행 |
| neutral-solid | High | 중립적 확정 |
| neutral-weak | Medium | 일반·보조 실행 |
| neutral-outline | Low | 투명 면, 중립 전경/단일 stroke.control |
| ghost | Low | 무테·무배경의 보조 행동/링크 |
| reward-secondary | Medium, 대표 수령 시 High | 실제 보상 관련 행동 |

Brand Weak/Outline 및 Critical solid/ghost는 별도 제안이며 기본 6종 승인과 구별한다. 위험·파괴에만 Critical을 검토하고 대비 검증 후 채택한다. Gold CTA는 Light #FFCC4D / #352713, Dark #957027 / #FFFFFF(라벨·아이콘) 유지. 일반 Outline에 Brand Red를 자동 상속하지 않는다.

## 크기와 조합 (D-012 / K07)

표시 높이 Small/Default/Large = 34/43/48, radius.control 16, glyph 16–18(기본 17), gap 6–8(기본 7). F-GEOMETRY/F-TYPE의 size별 padding과 label을 사용한다. 실제 링크/버튼이 비중첩 target 최소 48(웹은 48 CSS px와 3rem 중 큰 값)을 소유하고 face를 중앙 정렬한다. 확대 시 높이·그룹 배치는 유연하게, 기본 width는 hug/fill 중 목적에 맞게 선택한다.

한 행동 영역에서 High 하나가 기본이며 조회 화면은 없어도 된다. 좁거나 3개 이상이면 [P-ACTIONS](../patterns/action-layout.md)의 재배치/메뉴를 고려한다. 섹션의 더보기/전체보기는 [P-PAGE-COMPOSE](../patterns/page-composition.md)의 무테 텍스트/선택적 Chevron, 실제 링크 또는 ghost. 가짜 클릭·동일 목적지 중복·독립 행동 중첩 금지. 정적 아트에는 눌림을 부여하지 않는다.

## 상태 및 표현

| 상태 | 규격 |
|---|---|
| Enabled / Hover | 명확한 동사, 포인터 hover는 의미 면/색만 |
| Pressed | [F-ICON-MOTION](../foundations/icons-motion.md)의 face-only 0.98 후보, 색·opacity 반응 |
| Focus | 레이아웃을 밀지 않는 ring |
| Loading | Label/Icon slot과 중심 유지, spinner overlay, busy 의미·중복 실행 방지 |
| Disabled | 실행 불가, 필요한 경우 이유 안내 |
| Success/Error | 실제 결과 표시, 실패 시 원래 상태/재시도 복원 |

Pressed/Loading에서 **outer scale·font-weight·letter-spacing·padding·border-width 변경 금지**. face-only 시각 변환은 외곽/중심을 바꾸지 않는 명시적 예외 후보이며 Busy/Disabled에는 적용하지 않는다. 같은 버튼 상태의 outer rect와 label center 이동은 1 논리 단위 이하(확대에 따른 의도된 재흐름 제외). 200% 글자·대비·키보드·스크린리더 의미·동작 감소를 고려한다.

[패밀리 지도](../implementation/component-families.md) · [이전 상세·검증 이력](https://github.com/jaksim0sec/questYourLife/blob/07bdd23ac0da46033b45508767fa5e62643a1931/seed/components/action-button.md). 승인 값과 새로운 모션 후보는 구분하고 실제 앱 구현에서 검증한다.
