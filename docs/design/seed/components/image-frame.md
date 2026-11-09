---
id: C-IMAGE
category: component
status: proposed
read_when: 아이템/미디어/배너 공통 이미지 컨테이너와 이미지 상태
depends_on: [F-GEOMETRY, F-COLOR]
references: [S2-03, S2-05, S2-06, S2-07, S2-08]
---

# Image Frame

## 현행 계약

프레임은 fit/ratio/loading/fallback/overlay만 소유한다. 아트 표현 강도는 [A-ART](../assets/reward-art.md), 아이템의 Identity/State/Action은 [O-ITEM](../objects/item.md)이 소유한다. A1(일상 물체) 확대가 A2(전용 장면)가 되지 않는다.

### Selection

| 조건 | 선택 | 배제 |
|---|---|---|
| 투명 아이템·마스코트 | square 기반 contain + 충분한 safe zone | cover로 모자·귀 절단 |
| 배경이 있는 썸네일 | 맥락 비율 + 중요 부분 안전 crop을 확인한 cover | 무차별 정사각 crop |
| 이벤트 메시지·전용 장면 | A2 art의 safe zone 분할 + 독립 실제 텍스트 | A1 상자 단독 57% 확대 |
| 획득/기념 결과 | A3는 명시적 결과 장면에서만 | 일상 Card 안의 파티클 |
| 장식 없는 수치/사람 목록 | 이미지 자체 생략 후보 | 강제 빈 프레임 |

**Overlay 범위:** 한 모서리 슬롯당 **최대 1개**, 프레임 전체 **2개 권장**은 별도 조건. 전체 2개는 절대 법칙이 아니며 예외는 O-ITEM 상태 우선순위 설명 필요.

### Don't

1. 상자가 배너 면적 57%를 차지해 텍스트와 겹침. A1 물체를 키웠을 뿐 A2 콘텐츠 목적 장면이 아니다.
2. 카피 안전영역 52% 안에 상자 아트가 침범하거나 box/object crop이 생기는 이미지 cover.
3. 선택·장착·희귀도·이벤트 라벨을 모두 네 모서리에 붙여 정작 제품 이름이 읽히지 않는 경우.

### Relations

- **부모:** C-SURFACE Media, O-ITEM Media 슬롯, C-AVATAR(실사/장비 부분), 목적형 A2 이벤트 배너.
- **형제:** Identity·Value·State는 이미지 바깥 O-* 슬롯; 실패에도 그대로 유지.
- **자식:** 원본 이미지 + loading/fallback + 조건부 overlay. 외부 Action을 이미지 위 독립 nested button으로 넣지 않는다.
- **금지:** 일반 Surface의 색상/광원을 C-IMAGE가 재정의하거나, A3 연출을 일상 A1 카드의 자식으로 도입.

### Verification

- **B01–B09 하위 체크:** B01 목적 A1/A2/A3 판정, B02 원본·정체성 유지, B03 contain/cover, B04 텍스트 safe zone, B05 물체 crop, B06 상태 overlay, B07 2단계 오류/폴백, B08 light/dark 대비, B09 320/390/430 및 200%/상호작용.
- 이미지 로드 실패에도 프레임 비율과 Identity/Value가 유지되는지, contain이 실제 bounding box를 자르지 않는지 Playwright image load+computed style로 확인.
- A1 확대/안전영역 침범/Overlay 3개 반례는 실제 시각적으로 정상 예와 달라야 한다.

## 이력

- P2 이전 정본 전문은 이 아래에 보존. 구 승인값의 권위는 [D-012](../implementation/v015.md)·[토큰](../implementation/v015-tokens.json)으로 판정하며, 이 아래의 옛 제안·K/r1 문단은 새 현행 규칙이 아니다.

#### Purpose / Non-goals
콘텐츠 이미지가 안전하게 들어갈 **모양/비율/로딩/폴백 컨테이너**. 이벤트 조명 자체나 브랜드 색상 정의는 A-ART.

#### Anatomy / Properties
Frame + Image + optional Status/Badge + Fallback; fit=contain|cover, role=item|thumbnail|hero, ratio=content-defined. 배경을 가진 콘텐츠 썸네일은 cover, 투명 아이템/마스코트는 contain.

#### States
Loading: 같은 종횡비 placeholder; Loaded; Error: 폴백+필요한 설명. Owned/Selected/Equipped는 외부 상태 라벨로, 모든 이미지 위에 배지 억지 부착 금지.

#### Tokens
radius.image, bg.muted/surface, stroke.subtle. 같은 그리드의 이미지 프레임은 일관된 비율과 안전 영역을 유지한다. 단, 문구가 많은 홍보 이미지의 일률적 crop 금지.

#### Guidelines
3D 물체의 금색 광택은 오직 물체/특별 배너에만 사용. 일반 카드 경계나 버튼을 금색/네온으로 칠하지 않음. S2-06은 화려한 보상 순간, S2-08은 일상 진열이라는 서로 다른 맥락이다.

#### Accessibility
이미지 대체 텍스트는 중요한 시각 정보만, 가짜 내장 글자는 실제 텍스트로 중복 제공. zoom에서는 그리드 열 감소·줄바꿈 허용.

#### Evidence / Impact
S2-03/05/06/07/08 5개 자료를 이미지 목적별로 비교. A-ART·C-SURFACE·P-HIERARCHY 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

#### K06 미디어와 아트

**정본 소유자:** 프레임은 `components/image-frame.md`, 아트 강도는 `assets/reward-art.md`.

투명 아이템·마스코트는 contain, 배경이 있는 썸네일·장면은 안전영역을 확인한 cover를 사용한다. item의 기본 프레임은 1:1이다. thumbnail/scene의 비율은 원본과 콘텐츠 목적에서 정하고 같은 모음 안에서 일관되게 사용한다. 기존 3D 원본·얼굴·장비 좌표를 바꾸지 않는다.

미디어 상태 loading/loaded/error와 소유/장착/선택 상태는 서로 독립이다. 로딩/오류에서도 프레임 비율과 외부 텍스트를 유지한다. 이미지 실패 때문에 객체 전체를 지우거나 빈 회색 박스로만 남기지 않는다.

**우리 개편의 Overlay 기본 한도**는 모서리 슬롯당 1개·전체 2개다. 외부 서비스의 보편적 강제 규칙이라는 뜻은 아니다. 선택은 외곽선/체크, 장착은 짧은 외부 상태, 희귀도는 보조 프레임/메타를 우선한다. 표시가 충돌하면 현재 조작/사용 가능성 → 장착 사실 → 희귀도/이벤트 순으로 정리하고 나머지는 Information으로 옮긴다. 부족한 정보는 삭제하지 않는다.


아트 강도 A1/A2/A3는 [A-ART](../assets/reward-art.md)의 현행 적용 절을 따른다.
