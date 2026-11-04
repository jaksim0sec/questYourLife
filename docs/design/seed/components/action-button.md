---
id: C-ACTION
category: component
status: proposed
read_when: 일반·아이콘·텍스트 버튼, 핵심·보조 행동, 취소·수락
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION]
references: [REF-005, REF-006, REF-007, SRC-SEED-BUTTON, SRC-TDS-BUTTON]
---

# Action Button family

## Purpose / Non-goals
명시적 행동을 실행한다. 선택·탐색 탭·단순 상태 배지는 버튼 variant가 아니다. 다른 페이지로 이동하는 링크는 플랫폼의 link 의미를 유지한다. HTML 태그나 라이브러리 설치는 이 문서의 구현 지시가 아니다.

## Anatomy
Container + Label + 선택적 Prefix 또는 Suffix + Loading indicator. 아이콘은 의미를 돕는 하나만 기본으로 사용한다. 장식 배지·코인·카운터를 모두 버튼에 밀어 넣지 않는다. Icon Button의 독립 size/target·Badge·상단 규칙은 [C-ICON](icon-button.md)을 따른다. Text Button은 배경 면 없이 같은 행동 의미와 터치 기준을 유지한다.

## Properties / Variants
role(action/iconAction/textAction/link), emphasis(high/medium/low), tone(neutral/brand/critical), treatment(solid/weak/outline/ghost), size(small/medium/large), width(hug/fill)을 별개 축으로 본다. 모든 조합을 만들지 않고 아래를 기본 허용 조합으로 제한한다.

| 이름·별칭 | Emphasis | Tone / Treatment | 배경 / 전경 토큰 | 사용 |
|---|---|---|---|---|
| Primary / Brand Solid | High | brand / solid | accent.solid / accent.onSolid | 화면 목적을 완결하는 대표 행동 |
| Neutral Solid | High | neutral / solid | neutral.solid / neutral.onSolid | 중립적 확정이 대표 행동일 때, 강제 기본 아님 |
| Secondary | Medium | neutral / weak | bg.muted / fg.primary | 대부분의 일반·보조 실행 |
| Brand Weak | Medium 후보 | brand / weak | accent.weak / accent.onWeak | 브랜드 맥락의 보조 실행, REF-006 제안 유지 |
| Neutral Outline | Low | neutral / outline | transparent / fg.primary, stroke.control | 면이 많을 때 부가 행동 |
| Tertiary / Ghost | Low | neutral / ghost | transparent / fg.secondary | 전체 보기·미루기·추가 설명 등 맥락별 보조 |
| Critical | 문맥별 High 또는 Low | critical / solid 또는 ghost | critical.fg / bg.surface 또는 transparent / critical.fg | 실제 파괴·위험 행동에 한정 |

Critical solid의 전경은 해당 테마 bg.surface를 사용하며 대비 검증 후 채택한다. Critical ghost는 투명 배경 위 대비를 확인한다. 중요하지 않은 '거절/넘기기'에 자동 Critical을 쓰지 않는다.

SEED 현행 문서에는 Brand Weak가 기본 variant로 나열되지 않는다. 이 시드는 사용자 REF-006 제안 및 TDS weak의 구조를 참고한 별도 후보이며 SEED를 그대로 구현했다고 주장하지 않는다. Brand Solid와 Neutral Solid 모두 High가 될 수 있다.

## Size & Tokens
높이·padding은 F-GEOMETRY의 size.action* 및 action.padding* 정본, label은 F-TYPE의 type.button/buttonCompact, radius는 radius.control을 쓴다. Small label은 buttonCompact와 size.iconButtonCompact, Medium/Large는 button과 size.iconButtonContent를 사용한다. 아이콘을 강조하려고 label보다 과대하게 키우지 않는다. Icon Button은 [C-ICON](icon-button.md) 정본을 따르며 상단은 D-011 우선이다.

사용자 후속 피드백 U-04에 따라 Container의 높이·좌우 padding·최소 터치 영역은 유지하고 글자·아이콘을 소폭 축소한다. 줄높이 감소분은 상하 padding으로 돌려 버튼 내부의 숨 쉴 공간을 늘린다. 단독 Icon Button·상단·본문 아이콘의 크기는 함께 축소하지 않는다.

너비는 내용과 맥락에 따라 hug/fill, 내부 정렬은 중앙이다. glyph와 label의 광학 중심을 맞춘다. 로딩·아이콘 유무·tone 변경으로 버튼 외곽을 바꾸지 않는다.

## States / Behavior
| 상태 | 표현·동작 |
|---|---|
| Enabled | 역할별 기본 토큰, 명확한 동사 라벨 |
| Hover | 포인터 환경에만 F-COLOR overlay, 모바일에 가정하지 않음 |
| Pressed | 같은 면·크기에 press overlay, 중복 연출 없음 |
| Focus-visible | 외곽 focus ring, pressed·loading과 독립 |
| Loading | 폭·라벨 위치 유지, 진행 표시와 busy 의미, 중복 실행 방지 |
| Disabled | disabled 토큰·실행 불가 의미, 필요 시 가까운 이유 설명 |
| Success / Error | 완료 여부는 피드백으로 표시, 오류 후 재시도 허용 시 원래 액션 복귀 |

선택은 일반 실행 버튼의 지속 상태가 아니다. 토글 동작은 C-SELECTION을 사용한다. loading은 disabled와 동일한 의미가 아니며 스크린리더에 진행 중을 알린다.

## Guidelines
High는 한 행동 영역에서 하나를 기본으로 한다. 화면의 대표 CTA가 여러 지역 High와 경쟁하는지도 확인한다. 의미 없는 전체 너비, 맥락 없는 비대칭/대칭 강제, 모든 액션의 붉은 Solid, 버튼마다 다른 둥글기·그라데이션·선물 아이콘 확대를 금한다. 버튼 조합은 P-ACTIONS를 읽는다.

## Accessibility
텍스트 대비·48 최소 조작 영역은 Foundation을 따른다. Icon Button 이름은 동작을 설명한다. 긴 한글·200% 글자에서 padding 유지, 높이 확장 또는 그룹 재배치. loading/busy·disabled를 색만으로 알리지 않는다. 키보드 실행·포커스 이동은 의미에 맞춘다.

## Evidence & Confidence
REF-006은 제안, REF-007은 실앱 한 화면 관찰 medium. REF-005는 명시 교정 근거 high. 상태·수치·조합 API는 proposed이며 원본 화면에서 직접 관찰했다고 쓰지 않는다.

## Dependencies & Impact
Foundation만 토큰을 소유한다. 강조 규칙 변경은 P-ACTIONS, 목록 suffix, 고정 하단 CTA에 영향을 준다. 이름이 다른 기능별 버튼을 별도 컴포넌트로 만들지 않는다.

