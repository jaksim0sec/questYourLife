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

### Selection
- 투명 아이템/캐릭터=A1 **contain**, 콘텐츠 장면 썸네일=**cover + 핵심 피사체 안전영역**. A2는 한정 홍보 목적에 맞게 제작된 별도 구도의 장면, A3는 실제 획득 결과의 별도 경험. **A1 상자 57% 확대 + 갈색 gradient ≠ A2**.
- 프레임은 비율·fallback·crop만 소유, 물성·광원은 A-ART, 이름/가격/상태는 O-ITEM의 외부 슬롯. 모자·헤드폰·캡의 뾰족한 부분까지 10~15% 안전 여백은 후보 시작점이며 실제 실루엣으로 조정.
- Overlay 한도는 **동일 모서리 슬롯당 1개**와 **프레임 전체 2개 권장**을 서로 다른 범위로 유지. 현재 조작·사용 가능성 > 장착 사실 > 희귀도/이벤트. 원본 보고서 B01~B09 체크는 아래 검수에 연결.

### Don't
1. 투명 상자 PNG를 57% 점유율로 키우고 glow를 붙인 뒤 이벤트 장면 A2라고 선언: 피사체 목적·서사와 독립 구도 부재.
2. 보상 미리보기 아이템에 '장착 중'과 가격·선택 체크를 한 번에 프레임 위로 겹침: 맥락 불일치와 슬롯 초과.
3. `cover`로 캡 챙을 자르거나 이미지 실패 시 이름·값까지 함께 숨김: 필수 정보 손실.

### Relations
- **허용:** O-ITEM Media → C-IMAGE contain → C-RARITY 보조 배경, A-ART가 A1/A2/A3 레벨 결정, Identity/Value/State는 이미지 형제 슬롯.
- **금지:** 이미지 내부 라스터 가격/수치/버튼, 이미지가 객체 대표 클릭을 단독 소유, A3 자동 일상 반복, C-SURFACE가 image-fit를 재규정.

### Verification
- B01 purpose, B02 fit/crop, B03 inset, B04 overlay corner count, B05 total overlay count, B06 selection/equipped precedence, B07 fallback labels, B08 light/dark contrast, B09 320/390/430·200% text를 각각 fixture로 검사.
- 정상 contain 캡 이미지의 visible bounding box 보존, Don't cover 잘림, error시 외부 title/value 생존, static image에 click affordance 없는지 점검.

## 이력

> P2 이전 제안·구버전·수치 근거를 원문 보존한다. 현행 판단은 위 네 계약에서만 수행하며 이전 후보를 자동 승인하지 않는다.

### Purpose / Non-goals
콘텐츠 이미지가 안전하게 들어갈 **모양/비율/로딩/폴백 컨테이너**. 이벤트 조명 자체나 브랜드 색상 정의는 A-ART.

### Anatomy / Properties
Frame + Image + optional Status/Badge + Fallback; fit=contain|cover, role=item|thumbnail|hero, ratio=content-defined. 배경을 가진 콘텐츠 썸네일은 cover, 투명 아이템/마스코트는 contain.

### States
Loading: 같은 종횡비 placeholder; Loaded; Error: 폴백+필요한 설명. Owned/Selected/Equipped는 외부 상태 라벨로, 모든 이미지 위에 배지 억지 부착 금지.

### Tokens
radius.image, bg.muted/surface, stroke.subtle. 같은 그리드의 이미지 프레임은 일관된 비율과 안전 영역을 유지한다. 단, 문구가 많은 홍보 이미지의 일률적 crop 금지.

### Guidelines
3D 물체의 금색 광택은 오직 물체/특별 배너에만 사용. 일반 카드 경계나 버튼을 금색/네온으로 칠하지 않음. S2-06은 화려한 보상 순간, S2-08은 일상 진열이라는 서로 다른 맥락이다.

### Accessibility
이미지 대체 텍스트는 중요한 시각 정보만, 가짜 내장 글자는 실제 텍스트로 중복 제공. zoom에서는 그리드 열 감소·줄바꿈 허용.

### Evidence / Impact
S2-03/05/06/07/08 5개 자료를 이미지 목적별로 비교. A-ART·C-SURFACE·P-HIERARCHY 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

### K06 미디어와 아트

**정본 소유자:** 프레임은 `components/image-frame.md`, 아트 강도는 `assets/reward-art.md`.

투명 아이템·마스코트는 contain, 배경이 있는 썸네일·장면은 안전영역을 확인한 cover를 사용한다. item의 기본 프레임은 1:1이다. thumbnail/scene의 비율은 원본과 콘텐츠 목적에서 정하고 같은 모음 안에서 일관되게 사용한다. 기존 3D 원본·얼굴·장비 좌표를 바꾸지 않는다.

미디어 상태 loading/loaded/error와 소유/장착/선택 상태는 서로 독립이다. 로딩/오류에서도 프레임 비율과 외부 텍스트를 유지한다. 이미지 실패 때문에 객체 전체를 지우거나 빈 회색 박스로만 남기지 않는다.

**우리 개편의 Overlay 기본 한도**는 모서리 슬롯당 1개·전체 2개다. 외부 서비스의 보편적 강제 규칙이라는 뜻은 아니다. 선택은 외곽선/체크, 장착은 짧은 외부 상태, 희귀도는 보조 프레임/메타를 우선한다. 표시가 충돌하면 현재 조작/사용 가능성 → 장착 사실 → 희귀도/이벤트 순으로 정리하고 나머지는 Information으로 옮긴다. 부족한 정보는 삭제하지 않는다.


아트 강도 A1/A2/A3는 [A-ART](../assets/reward-art.md)의 현행 적용 절을 따른다.
