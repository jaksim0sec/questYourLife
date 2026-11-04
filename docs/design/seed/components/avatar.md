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
