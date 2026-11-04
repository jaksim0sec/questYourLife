---
id: F-COLOR
category: foundation
status: proposed
read_when: 색상 역할, 라이트·다크, 배경·경계·강조·대비
depends_on: [F-PRINCIPLES]
references: [REF-001, REF-002, REF-004, REF-006, SRC-SEED-BUTTON, SRC-TDS-BUTTON, SRC-WCAG]
---

# 색상과 표면

## 범위·지위
아래 HEX는 원본 픽셀 추출값이 아니라 **검토 가능한 제안값**이다. 라이트·다크를 서로 다른 화면의 로컬 예외가 아닌 같은 의미 토큰의 두 매핑으로 둔다. 어떤 테마를 기본으로 제공할지는 미결이다. 외부 시스템의 색을 복사하지 않았다.

## 토큰 정본

| Token | Light | Dark | 용도 |
|---|---|---|---|
| bg.canvas | #FAF9F8 | #17171B | 페이지 바탕, Light를 조금 더 중립적으로 교정 |
| bg.surface | #FFFFFF | #232329 | 기본 그룹 면 |
| bg.muted | #F1F0F3 | #303038 | 약한 그룹·보조 행동 |
| bg.floating | #FFFFFF | #2C2C34 | 시트·팝오버 |
| fg.primary | #17171B | #F4F2F5 | 본문·중요 제목 |
| fg.secondary | #5E5A65 | #B8B5BF | 설명·메타데이터 |
| fg.muted | #6E6A74 | #ABA7B2 | 낮은 강조, 읽혀야 하는 정보 |
| stroke.subtle | #E8E4E7 | #44414B | 장식·그룹 구분, 조작 식별에 단독 사용 금지 |
| stroke.control | #827A87 | #938B9C | 입력·선택의 필수 경계 |
| accent.solid | #D93B3B | #FF8173 | 핵심 Brand 행동, 더 따뜻한 레드 v0.2 후보 |
| accent.onSolid | #FFFFFF | #17171B | Brand Solid 라벨·아이콘 |
| accent.weak | #FFF1ED | #3B2522 | 브랜드 보조 면 |
| accent.onWeak | #A52A24 | #FFAA9C | 브랜드 보조 전경·현재 선택 텍스트 |
| neutral.solid | #33313A | #E6E2EB | 중립적 High 행동, 필요할 때만 |
| neutral.onSolid | #FFFFFF | #17171B | Neutral Solid 전경 |
| positive.fg / .bg | #176C47 / #E9F6EE | #89DFB1 / #18382A | 긍정 결과 |
| warning.fg / .bg | #7B5100 / #FFF4D8 | #F3CF76 / #3C311B | 주의 |
| critical.fg / .bg | #A92135 / #FFF0F2 | #FF9CA7 / #3A2228 | 오류·파괴 행동 |
| info.fg / .bg | #245AA5 / #EAF2FF | #A6C9FF / #203149 | 안내 |
| focus.ring | #245AA5 | #A6C9FF | 키보드 포커스 |
| disabled.bg / .fg | #EFEDF0 / #827D88 | #302D35 / #8E8896 | 비활성 전용 |
| overlay.scrim | #000000, 40% | #000000, 56% | 모달 뒤 배경 |

브랜드 빨강은 오류·미완료를 뜻하지 않는다. critical과 브랜드가 비슷한 색을 써도 아이콘·문구·맥락으로 구분한다. 캐시 코인 금색·클랜 지정색·이벤트색은 이 표의 전역 역할을 덮어쓰지 않는다.

사용자 후속 피드백 U-04에 따라 브랜드 빨강에 주황기를 소폭 더한 따뜻한 매핑을 제안한다. 오류·파괴 행동의 critical 색은 독립된 의미 토큰으로 유지한다. 새 수치는 방향 조정에 대한 후보이며 최종 시각 승인을 뜻하지 않는다.

## 기존안 대비 개선 · S2-01/03/05/06/08
- v0.1 브랜드 후보 `#D63D36` → v0.2 `#D93B3B`: 조금 더 밝고 따뜻한 붉은색. 계산상 흰 글자 대비 약 **4.53:1**, 최소 대비를 유지하는 범위에서만 테스트한다. 더 밝은 `#F0444B`와 흰 글자 조합은 약 3.73:1로 일반 텍스트에 실패하므로 기본 Primary 제안에서 제외한다.
- Neutral Solid는 브랜드 기본 행동을 대신하지 않으며, 일반 업무/리스트의 대부분은 Neutral Weak 또는 약한 Text/Outline이 먼저다.
- 배경·카드·버튼의 그라데이션을 기본 구성에서 제거한다. A2/A3의 보상 이미지 자체에만 3D 조명·광택을 보존한다.
- 새로운 HEX 매핑은 모두 proposed. 실제 웹 테마·장시간 화면 사용·브랜드 일관성 검증 후 승인한다.

## 표면·깊이
Surface는 정보 그룹이고 그 자체로 버튼이 아니다. 카드 여부는 독립된 콘텐츠 단위·클릭 동작으로 판단한다. 그룹 내부 행마다 별도 카드·그림자를 중첩하지 않는다.
- elevation.flat: 그림자 없음, 기본.
- elevation.floating: 검정 10% / y=4 / blur=16의 제안. 모달·팝오버에서만 사용하며 경계·위치로도 구분한다.
- 일반 버튼은 그림자·그라데이션 없음. 보상 물체의 금속광·발광은 A-ART 범위.
- 글래스·블러는 채택하지 않은 후보다. 기본 정본은 불투명 표면이며 D-011의 상단 무채색 면을 우선한다. 다크 헤더를 전역으로 쓰는 것은 현재 확정안과 충돌하므로 O-022에서 보류한다.

## 상태 색
Pressed는 원래 tone을 유지한 채 Light 검정 6%, Dark 흰색 8% overlay의 제안. Hover는 포인터 환경에서만 4% overlay. Focus는 색 변경과 독립된 외곽 2 논리 단위 ring + 2 offset. Disabled는 전용 토큰을 쓰며 부모 전체 opacity를 내려 본문까지 흐리게 하지 않는다. 선택은 강조 배경·체크·밑줄 중 해당 컴포넌트의 규칙으로 표현한다.

## 접근성·근거
일반 텍스트 4.5:1, 큰 텍스트 3:1, 필요한 조작 경계·상태 표시 3:1을 검증 목표로 한다. 장식 경계와 disabled 제외를 접근 가능한 조작 경계의 핑계로 쓰지 않는다. 아래는 불투명 sRGB 매핑 계산 결과이며 실제 합성·압축·실기기 검증은 별도다.

| 조합 | 계산 대비 |
|---|---:|
| Light accent.onSolid / accent.solid | 약 4.53:1 (v0.2 계산값) |
| Light accent.onWeak / accent.weak | 6.45:1 |
| Dark accent.onSolid / accent.solid | 7.35:1 |
| Dark accent.onWeak / accent.weak | 7.79:1 |

배경 이미지 위 텍스트는 별도 불투명 받침 또는 충분한 명암 영역을 확보한다. 직접 관찰 high: 두 테마의 중립 면과 빨강. 매핑·상태 overlay는 제안이며 렌더 검증 전이다.

## 영향
여기서만 전역 색을 변경한다. 컴포넌트 문서에서 HEX를 재정의하지 않는다. 대비가 실패하면 tone을 유지하며 역할 토큰을 조정하고 전체 사용처를 확인한다.

