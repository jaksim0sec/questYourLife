---
id: C-IMAGE
category: component
status: proposed
read_when: 아이템/미디어/배너 공통 이미지 컨테이너와 이미지 상태
depends_on: [F-GEOMETRY, F-COLOR]
references: [S2-03, S2-05, S2-06, S2-07, S2-08]
---

# Image Frame

## Purpose / Non-goals
콘텐츠 이미지가 안전하게 들어갈 **모양/비율/로딩/폴백 컨테이너**. 이벤트 조명 자체나 브랜드 색상 정의는 A-ART.

## Anatomy / Properties
Frame + Image + optional Status/Badge + Fallback; fit=contain|cover, role=item|thumbnail|hero, ratio=content-defined. 배경을 가진 콘텐츠 썸네일은 cover, 투명 아이템/마스코트는 contain.

## States
Loading: 같은 종횡비 placeholder; Loaded; Error: 폴백+필요한 설명. Owned/Selected/Equipped는 외부 상태 라벨로, 모든 이미지 위에 배지 억지 부착 금지.

## Tokens
radius.image, bg.muted/surface, stroke.subtle. 같은 그리드의 이미지 프레임은 일관된 비율과 안전 영역을 유지한다. 단, 문구가 많은 홍보 이미지의 일률적 crop 금지.

## Guidelines
3D 물체의 금색 광택은 오직 물체/특별 배너에만 사용. 일반 카드 경계나 버튼을 금색/네온으로 칠하지 않음. S2-06은 화려한 보상 순간, S2-08은 일상 진열이라는 서로 다른 맥락이다.

## Accessibility
이미지 대체 텍스트는 중요한 시각 정보만, 가짜 내장 글자는 실제 텍스트로 중복 제공. zoom에서는 그리드 열 감소·줄바꿈 허용.

## Evidence / Impact
S2-03/05/06/07/08 5개 자료를 이미지 목적별로 비교. A-ART·C-SURFACE·P-HIERARCHY 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K06 미디어와 아트

**정본 소유자:** 프레임은 `components/image-frame.md`, 아트 강도는 `assets/reward-art.md`.

투명 아이템·마스코트는 contain, 배경이 있는 썸네일·장면은 안전영역을 확인한 cover를 사용한다. item의 기본 프레임은 1:1이다. thumbnail/scene의 비율은 원본과 콘텐츠 목적에서 정하고 같은 모음 안에서 일관되게 사용한다. 기존 3D 원본·얼굴·장비 좌표를 바꾸지 않는다.

미디어 상태 loading/loaded/error와 소유/장착/선택 상태는 서로 독립이다. 로딩/오류에서도 프레임 비율과 외부 텍스트를 유지한다. 이미지 실패 때문에 객체 전체를 지우거나 빈 회색 박스로만 남기지 않는다.

**우리 개편의 Overlay 기본 한도**는 모서리 슬롯당 1개·전체 2개다. 외부 서비스의 보편적 강제 규칙이라는 뜻은 아니다. 선택은 외곽선/체크, 장착은 짧은 외부 상태, 희귀도는 보조 프레임/메타를 우선한다. 표시가 충돌하면 현재 조작/사용 가능성 → 장착 사실 → 희귀도/이벤트 순으로 정리하고 나머지는 Information으로 옮긴다. 부족한 정보는 삭제하지 않는다.


아트 강도 A1/A2/A3는 [A-ART](../assets/reward-art.md)의 현행 적용 절을 따른다.

## 기존 C-IMAGE의 웹 Item Frame 계약 보완 (2026-11-10)

**새로운 아이템 카드/이미지 컴포넌트를 양산하지 않는다.** 이미 있는 `C-IMAGE`를 공통 **Image Frame**으로 사용한다. 프레임은 이미지의 비율·피팅·로딩/오류·기본 Overlay 슬롯을 소유하고 **상품명·가격·설명·구매/장착 액션은 프레임 외부의 정보 객체**가 소유한다.

| 역할 · `data-media-kind` | 권장 fit | 비율 | 사용할 때 |
|---|---|---|---|
| `item` | `contain` | 기본 **1:1** | 투명 PNG/3D 오브젝트, 보상 상자, 장착 아이템, 책장/격자형 진열 |
| `thumbnail` | `cover` | 콘텐츠별, 같은 모음에서 일관 | 퀘스트북 표지, 배경이 있는 콘텐츠 미리보기 |
| `scene` (`hero` 표기와 의미 대응) | 배경 있는 장면은 `cover`, 단일 오브젝트면 `contain` | **페이지 구도에 맞게 지정** | 이벤트 패널·소개·보상 히어로, 이미지 내 텍스트 안전영역 필수 |

**기본 웹 API**: `.seed-media[data-media-kind="item|thumbnail|scene"][data-media-fit="contain|cover"][data-media-state="loading|loaded|error"]`, 하위 `.seed-media__image`, 선택 `.seed-media__badge`, `.seed-media__fallback`, `--media-ratio`. 표준 이미지는 외부에서 이미지/의미를 공급한다. 원본 경로는 `assets/art/` 정본을 재사용한다.

- 상태 `loading/loaded/error`는 프레임 안에서 해결. **상태 전환 중에도 ratio, 모서리, 바깥 Item Title/Price/Action, grid 위치를 유지**한다. 네트워크 실패 시 폴백을 노출하고 오류를 숨기지 않는다.
- `owned/selected/equipped/locked`는 별도 **도메인 상태**. 프레임 자체의 image loading과 혼동 금지. Badge는 의미 있는 위치에 모서리당 1개, 전체 기본 2개 이하. 선택 체크/가격/장착 상태를 겹겹이 올려 정보 중복 금지.
- 투명 PNG는 잘리지 않게 기존 `contain` + 안전 padding 사용. `A1` 아이템을 크게 늘려 A2 이벤트 장면이라 주장하지 않는다. A2/A3 아트의 연출 강도는 A-ART로 넘긴다.
- 아이템 진열 예시: `C-IMAGE` 1:1 Frame → 외부 Item Name → 별도 C-CREDIT 가격/상태. 같은 Grid의 썸네일 크기·이미지 점유율·메타 기준선을 정렬. `thumbnail`/Scene은 각기 다른 원본 목적을 가진다.
- image `alt`는 기능 정보일 때 제공하되 바깥 텍스트가 정확히 반복 설명하는 경우 빈 `alt=""`를 사용한다. 로딩·실패 시 별도 접근성 설명.
- **단일 공통 스타일:** `web/preview/seed-standard.css`에 프레임 규칙을 둔다. Lab이 이 규칙을 보여주되 페이지별 임시 CSS로 프레임의 기본 크기·Radius·fit을 재정의하지 않는다.

- 기존 `data-media-kind="hero"`는 `scene`과 같은 비율/표현 맥락의 **호환 별칭**이다. 이미 쓰는 컨테이너 이름을 대량 교체하지 않는다.
