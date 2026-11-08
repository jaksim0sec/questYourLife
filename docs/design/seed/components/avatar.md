---
id: C-AVATAR
category: component
status: proposed
read_when: 마스코트·친구·클랜 구성원의 투명 캐릭터와 신원 표시
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR]
references: [S2-04, S2-07, S2-08]
---

# Avatar / Mascot Identity

## 현행 계약

### Selection
- 사람·마스코트 Identity는 C-AVATAR. 사용자 제공 도장·장착 파츠는 transparent/contain 우선, 사진만 framed 가능. 진열 아이템은 O-ITEM/C-IMAGE.
- Avatar Stack은 실제 목록 순서를 유지하고 표시에 생략된 수만 +N. size row40/profile64/hero104는 candidate. 귀·캡 챙·헤드폰이 잘리면 불합격.

### Don't
1. 투명 캐릭터를 흰 원으로 crop해 귀/헤드폰을 잘라버림.
2. 6명 중 네 명 표시하면서 +4 표시, 또는 Stack/실제 명단 순서 불일치.
3. 장식용 대형 Avatar가 클랜 기록과 이름을 첫 화면에서 밀어냄.

### Relations
- **허용:** O-PERSON-ACTIVITY Avatar/Name, O-ITEM 장착 상태는 형제 Text 슬롯, C-IMAGE contain.
- **금지:** Avatar가 Owned/Equipped의 source of truth, 원본 장비 파츠 좌표 무단 변경, 사람을 상품 타일로 표시.

### Verification
- 정상 Transparent+4/6(+2) vs Don't crop/잘못된 +N, 실제 픽셀 경계·명단 순서·fallback. 320/390/430 light/dark에서 이름 중복 읽기·hitbox·계층 검사.

## 이력

> P2 이전 버전과 후보 원문을 아래 보존하며 위 4계약만 현행 규범이다.

### Purpose / Non-goals
신원과 개인화된 마스코트를 표현한다. **원형 흰 사진 프레임이 필수 조건이 아니다.** 기본 마스코트는 투명 캐릭터 자체를 배치하는 `transparent` 방식이 우선이며 일반 사진 프로필은 `framed` 표현을 선택할 수 있다.

### Anatomy / Properties
Identity Asset + optional Equipment layers + optional status adornment + Fallback. `presentation=transparent|framed`, `scale=row|profile|hero`, `fit=contain` (마스코트), assetAlt(이미지 대표성에 따라).

### States
Loaded / Loading / Fallback / Missing equipment. 보유/착용 상태는 C-SURFACE/아이템 상태와 별개. 실제 캐릭터와 아이템 크기를 자체 이미지 crop으로 맞추지 않는다.

### Tokens
size.avatarRow 40 / avatarProfile 64 / avatarHeroMax 104 v0.2 **제안**. 부모 영역의 텍스트 제목·결과 정보가 주 목적이면 캐릭터는 그보다 부차적으로 보이게 할 수 있다.

### Guidelines
S2-08의 현실 소품과 붉은 도장 정체성 유지. 모자·귀·헤드폰은 사각 contain 프레임에서 완전히 보이게, 흰 원/원형 crop을 기본으로 하지 않음. S2-04 반례 06의 마이 아바타 과대/보조 장식선 과대 금지.

### Accessibility
인물 이름과 이미지 정보가 중복되지 않도록 label과 alt 조정. 해당 캐릭터에 적용된 실제 착용 정보만 보조 설명. 배경이 투명해도 상호작용 target은 별도 확보.

### Evidence / Impact
S2-08는 투명/모자/소품 정체성의 최우선 시각 근거, S2-04는 과대 반례. 기존 마스코트 리소스는 변경하지 않는다. A-ART와 AVATAR 제품 문서 의존.

> 숫자 및 신규 상태는 사용자 미승인 후보. 제품 데이터·탭 목적지·도메인 기능을 이 문서에서 발명하지 않는다.
