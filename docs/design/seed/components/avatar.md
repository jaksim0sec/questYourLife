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

Avatar는 **신원·마스코트·장비 합성**의 미디어 계약이다. 보상 아트의 광원은 A-ART가, 사람 활동의 이름/시간/반응은 O-PERSON-ACTIVITY가 소유한다. 실제 도장 얼굴·장비 비율/좌표는 기존 원본 그대로 유지한다.

### Selection

| 맥락 | 선택 | 배제 |
|---|---|---|
| 도장 마스코트·장비 착용 | Transparent + contain | 무조건 흰 원형 crop |
| 실제 사람 사진 | Framed Avatar | 3D 아이템 프레임 |
| 사람·활동 목록 | Avatar + 이름/시간/행위, O-PERSON-ACTIVITY | 아이템 Grid |
| 여러 참가자 요약 | Avatar Stack + 순서·`+N` | 얼굴 중첩으로 중요한 장비 가림 |
| 정체성 아닌 보상 아이템 | O-ITEM/C-IMAGE | C-AVATAR로 가격/장착 행위 전달 |

Stack은 대표 사람 정렬을 데이터 의미(기여/참가/최근)에 맞춰 명시하고 초과 수는 `+N`으로 표현, **총인원**과 Stack 표시 수를 혼동하지 않는다. 40/64/104 avatar 수치는 P2 후보이며 원본 에셋 재가공 지시가 아니다.

### Don't

1. 투명 도장 마스코트를 흰 원에 강제 crop해 귀/헤드폰/모자 끝이 잘림.
2. Avatar Stack 순서가 사용자에게 설명되지 않고 `+N`을 별도의 실제 한 사람처럼 화면 읽기에 제공.
3. 작은 프로필 아바타를 결과 대표 수치보다 더 키워 의미 위계를 뒤집음.

### Relations

- **부모:** C-LIST/O-PERSON-ACTIVITY, Identity Header, User Profile, 장비 미리보기.
- **자식:** 투명 Identity Media + 조건부 Equipment(원본 z-index 유지) + Badge 최대 한 의미 + fallback.
- **형제:** 사람 이름·메타는 C-TEXT, 장착 사실은 O-ITEM State; Stack+N은 별도 읽기 이름을 제공.
- **금지:** Avatar 안에 구매/장착 버튼, 원본 캐릭터 얼굴 재해석, C-RARITY를 인물의 등급으로 사용.

### Verification

- Transparent/Framed 병치, 원본 얼굴과 장비 보존, contain 이미지 좌표 및 crop 0, image 실패 이름 유지.
- Stack 순서·겹침 z-index·`+N` 계산, 사용자 접근 가능한 전체 참가자 수/목록 연계.
- 320/390/430×light/dark, 장비 돌출/긴 이름/200% 확대에 대한 실제 렌더 검사.

## 이력

- P2 이전 전문 원본을 아래에 보존. 이전 제안·r1 문단이 현재 규칙과 다르면 현행 계약/승인 D-012가 우선한다.

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
