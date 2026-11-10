---
id: C-AVATAR
category: component
status: proposed
read_when: 마스코트·친구·클랜 구성원의 투명 캐릭터와 신원 표시
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR]
references: [S2-04, S2-07, S2-08]
---

# Avatar / Mascot Identity

## Purpose / Non-goals
신원과 개인화된 마스코트를 표현한다. **원형 흰 사진 프레임이 필수 조건이 아니다.** 기본 마스코트는 투명 캐릭터 자체를 배치하는 `transparent` 방식이 우선이며 일반 사진 프로필은 `framed` 표현을 선택할 수 있다.

## Anatomy / Properties
Identity Asset + optional Equipment layers + optional status adornment + Fallback. `presentation=transparent|framed`, `scale=row|profile|hero`, `fit=contain` (마스코트), assetAlt(이미지 대표성에 따라).

## States
Loaded / Loading / Fallback / Missing equipment. 보유/착용 상태는 C-SURFACE/아이템 상태와 별개. 실제 캐릭터와 아이템 크기를 자체 이미지 crop으로 맞추지 않는다.

## Tokens
size.avatarRow 40 / avatarProfile 64 / avatarHeroMax 104 v0.2 **제안**. 부모 영역의 텍스트 제목·결과 정보가 주 목적이면 캐릭터는 그보다 부차적으로 보이게 할 수 있다.

## Guidelines
S2-08의 현실 소품과 붉은 도장 정체성 유지. 모자·귀·헤드폰은 사각 contain 프레임에서 완전히 보이게, 흰 원/원형 crop을 기본으로 하지 않음. S2-04 반례 06의 마이 아바타 과대/보조 장식선 과대 금지.

## Accessibility
인물 이름과 이미지 정보가 중복되지 않도록 label과 alt 조정. 해당 캐릭터에 적용된 실제 착용 정보만 보조 설명. 배경이 투명해도 상호작용 target은 별도 확보.

## Evidence / Impact
S2-08는 투명/모자/소품 정체성의 최우선 시각 근거, S2-04는 과대 반례. 기존 마스코트 리소스는 변경하지 않는다. A-ART와 AVATAR 제품 문서 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.

## 2026-11-10 · C-AVATAR 웹 착장 합성 구현

**역할:** 마스코트는 사용자·친구·클랜 구성원을 나타내는 **프로필 사진에 상응하는 신원 요소**다. My에서 강조 아바타, 채팅/클랜의 작은 프로필, 출석·친구 목록의 아이덴티티 표시 모두 같은 컴포넌트를 사용한다. 상점의 단독 상품 썸네일은 C-IMAGE이고 착장 아바타와 구분한다. 원형 흰 사진 프레임은 필수가 아니며 기본은 `transparent`, 선택적으로 `framed`다.

**공통 웹 소유권:**
- 구현: `web/preview/seed-mascot-avatar.mjs`의 `composeMascotAvatar(equipmentIds,placementById)` / `mountMascotAvatar(element, options)`.
- 외형: `web/preview/seed-standard.css`의 `.seed-mascot-avatar`, `__layer`, `__fallback`. 각 화면은 이 클래스와 함수만 사용하고 레이어 좌표·z-index를 자체 CSS로 복사하지 않는다.
- 공통 아트 원본: `assets/art/mascot/mascot_face_stamp.png`, `assets/art/cosmetics/cosmetic_*.png` 9종. `main`의 Android 이미지 Git blob SHA와 **완전히 동일**. 데이터 ID는 기존 `CosmeticCatalog.kt`를 유지.
- 기본 좌표: `main/app/src/main/java/com/questyourlife/app/cosmetics/AvatarComposition.kt`의 현재 보정값을 웹에 그대로 이식했다. Android 자체는 변경하지 않음.

### Anatomy / API

```js
import {mountMascotAvatar} from '../preview/seed-mascot-avatar.mjs';
mountMascotAvatar(document.querySelector('#profile-mascot'), {
  equipmentIds: [1003, 2], // 블랙 헤드폰 + 블루 캡
  scale: 'fluid',
  presentation: 'transparent',
  label: '내 프로필, 블랙 헤드폰과 블루 캡',
  placementById: {2: {x: 0.06, y: 0.01, width: 0.88, height: 0.59, zIndex: 45}}
});
```

`equipmentIds`는 기존 보유 ID 값의 배열이며 실제 소유·장착 확정·Room 동기화는 앱 데이터 계층 소유다. 호출만으로 아이템이 지급되거나 실제 장착 상태가 저장되는 일은 없다. `placementById`는 **개별 오브젝트별 사용자 보정 좌표**로 x/y/width/height/zIndex를 선택적으로 지정하고, 다른 아이템의 정본 좌표를 변형하지 않는다. x/y 음수는 오브젝트의 캔버스 밖 자연스러운 확장에 허용한다. width/height는 양수, zIndex는 정수. 모르는 ID(미제공 라임 별 1002 등)는 임의 이미지 생성 없이 무시한다.

### 정규화 캔버스와 기본 레이어

전체 아바타는 원본 비율을 보존한 **정사각형 1:1 논리 캔버스**다. 얼굴 박스는 `(x=0,y=.233,w=1,h=.734,z=10)`. 각 아이템은 아래 정규화 박스를 이용해 `left/top/width/height = 좌표 × 100%`로 배치하며 `object-fit:contain`으로 원본 왜곡을 막는다. CSS pixel 위치를 따로 지정하지 않으므로 **40px 프로필을 240px으로 확대하거나 다시 축소해도 좌표 비율 및 오브젝트 중심이 변하지 않는다.**

| 기존 ID | 오브젝트 | 슬롯 | x / y / w / h | 웹 기본 zIndex |
|---|---|---|---|---:|
| 0 | 골지 비니 | headwear | .075 / .055 / .85 / .612 | 40 |
| 1 | 크림 캡 | headwear | .045 / .060 / .91 / .582 | 44 |
| 2 | 블루 캡 | headwear | .065 / .015 / .87 / .582 | 45 |
| 3 | 차콜 캡 | headwear | .045 / .060 / .91 / .582 | 43 |
| 4 | 백워드 캡 | headwear | .105 / -.020 / .78 / .612 | 42 |
| 5 | 버킷 햇 | headwear | .0675 / .012 / .865 / .656 | 46 |
| 1001 | 크림 헤드폰 | earwear | .030 / .175 / .94 / .685 | 20 |
| 1003 | 블랙 헤드폰 | earwear | .030 / .175 / .94 / .685 | 21 |
| 1004 | 화이트 이어버드 | earwear | .025 / .480 / .95 / .360 | 32 |

**겹침 정책:** 모든 소품은 개별 기본 zIndex를 소유한다. 기본 표시는 얼굴(10) → 귀 장비(20~32) → 모자(40~46)이며 **같은 슬롯에 여러 아이템을 선택하면 배열에서 마지막 장착한 아이템만 유지**한다. 다른 슬롯은 함께 착용할 수 있다. 개별 의상에 적절한 선후관계는 `placementById[id].zIndex`로 명시적으로 조정한다. 이 zIndex는 *웹의 초기 우선순위*로서 실제 시각 합성 검수에서 보정 가능하며 장비끼리 겹치지 않는다는 보장은 하지 않는다.

### 크기·상태·접근성

- `scale=row|profile|hero|fluid`: 공통 CSS 기준 2.5rem(40), 4rem(64), 6.5rem(104) 또는 **부모가 정하는 유동폭**. My의 집중된 큰 아바타는 `fluid`로 배치 영역을 확장한다. 이것은 기존 Row/Profile/Hero 수치를 바꾸거나 대형 아바타에 104px 상한을 강제한다는 뜻이 아니다.
- `presentation=transparent|framed`. 기본은 자유형 투명 오브젝트이며 얼굴·액세서리에 둥근 프레임 crop을 적용하지 않는다.
- `mountMascotAvatar`는 1개의 `role=img`와 `aria-label`로 합성 결과를 읽어주고 개별 장비 이미지는 장식 처리한다. 인접한 프로필 이름이 이미 설명하면 `decorative:true`로 중복 읽기를 방지한다.
- 이미지가 없으면 빈 장비만 감추고, 얼굴 로드 실패일 때 `data-state=error`와 대체 텍스트를 보인다. `Loading/Fallback/Missing equipment` 의미 상태는 각각 분리한다. 클릭/편집/장착은 아바타 안에서 자동 제공하지 않으며 부모 링크·버튼이 동작과 48px 타깃을 소유한다.
- `web/seed-lab/?panel=components#mascot-avatar`에서 착장 4종 및 Feature/Profile/Row 크기를 비교할 수 있다. 디바이스 브라우저 합성·200% 확대 최종 육안 검수는 별도다.

## S05 · Big/Middle/Small과 아바타 이미지 scale의 경계

S05의 **Big / Middle / Small은 프로필에 어떤 정보를 얼마나 보여줄지**를 결정하는 [P-HIERARCHY의 정보 역할](../patterns/content-hierarchy.md)이다. **C-AVATAR는 프로필 전체 레이아웃이 아니라 얼굴·장비를 합성한 한 개의 Identity 이미지**만 소유한다. 이름·보조 정보·역할·헤더 위치·버튼과의 간격은 이 이미지에 합쳐 넣지 않는다.

| 독립 축 | 선택·책임 | 하지 않을 것 |
|---|---|---|
| **프로필 정보 역할** | P-HIERARCHY에서 `Big / Middle / Small`의 Identity/선택적 메타/선택적 Action과 공간 관계 결정 | 각 역할을 고정된 카드 높이·패딩·아바타 크기로 매핑 |
| **아바타 이미지 크기** | 기존 `scale=row|profile|hero|fluid`로 정사각형 합성 캔버스의 출력 크기 조절 | `Big=hero, Middle=profile, Small=row` 강제. 정보 역할 이름을 C-AVATAR 옵션으로 추가 |
| **이미지 표현** | 기존 `presentation=transparent|framed`; 마스코트는 `contain`과 원본 장비의 전체 형태 보존 | 캡슐형 프로필·원형 crop·흰 프레임을 기본값으로 강제 |
| **행/헤더의 상호작용** | C-LIST는 반복 행의 Prefix/Content/Suffix, S-PAGE/NAV-TOP은 헤더 Back/Title/우측 슬롯 소유 | 아바타 안에 내비게이션/장착/구매 버튼을 자동 삽입 |

**서로 다른 예:** 큰 정보 영역에서도 화면 과업에 따라 `row` 이미지가 적합할 수 있고, 이미지가 시각적 중심인 짧은 소개에서는 `hero`로 보여도 정보는 Small(이름 중심)일 수 있다. `scale=fluid`는 Big만을 위한 별도 프로필 타입이 아니다. 실제 출력 크기와 정보 역할은 해당 화면의 첫 시선·가용 폭으로 독립 선택한다.

**표시·검수:** `web/seed-lab/?panel=components#mascot-avatar`의 Feature/Profile/Row 및 착장 전환은 기존 C-AVATAR의 **크기·레이어 합성 검증**이며 Big/Middle/Small 정보 패턴의 완성된 화면 검증이 아니다. 프로필 구성 시 320~430 폭, 긴 한글 이름, 보조 정보 유무, 200% 확대와 원본 장비 2종 이상의 합성에서 이름·추가 정보·동작을 별도 검수한다. 단일 원본 이미지·ID·좌표·zIndex·접근성 대체 설명은 현행 정본 그대로 유지한다. **NonGlobal 채팅방 헤더의 Middle 정보 배치와 실제 Back/Title 충돌 검증은 U06의 책임**이다.
