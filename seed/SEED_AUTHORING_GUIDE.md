# 컨셉 이미지 → 디자인 시드 추출·문서화 가이드

> **목적:** 컨셉 이미지·스크린샷·Figma 시안을 받은 에이전트가, 특정 페이지를 복제하는 대신 **재사용 가능한 디자인 문법**을 추출하고 필요한 문서만 빠르게 찾아 쓰게 한다.
> **범위:** 추출·분류·문서 분산·라우팅·검증 절차만 정의한다. 이 가이드는 색상·토큰·컴포넌트 사양의 최종 승인안이 아니다.
> **선행:** [확정사항](../docs/DECISIONS.md) / [미결사항](../docs/OPEN.md) / [제품 문서 색인](../docs/product/README.md).
> **구조 참고:** [SEED Foundations](https://seed-design.io/foundations), [Components](https://seed-design.io/components), [Patterns](https://seed-design.io/patterns). 컴포넌트별 Anatomy / Properties / States / Guidelines / Specification 방식은 참고하되, 시드의 시각적 결과를 SEED에 종속시키지 않는다.

## 1. 우선 지켜야 할 원칙

1. **이미지는 증거이지 완성 사양이 아니다.** 예쁜 단일 화면의 문구·카드 개수·레이아웃을 전역 규칙으로 고정하지 않는다.
2. **콘텐츠보다 기본 재료를 먼저 정의한다.** Button, Surface, List, Input, Badge, Image Frame, Progress 등을 먼저 추출하고 화면 특화 구조는 그 조합으로 만든다.
3. **역할, 강조도, 색의 의미, 상태를 분리한다.** 브랜드색 ≠ 모든 주요 액션 ≠ 성공·위험 의미 ≠ 보상 일러스트의 고유색.
4. **여러 이미지의 공통 요소와 국소적 예외를 구분한다.** 반복된 요소는 범용 후보, 한정 이벤트/특수 아트는 별도 Pattern 또는 Asset 범위.
5. **직접 관찰 / 추론 / 설계 제안 / 사용자 승인**을 섞지 않는다. 이미지에 없는 로딩·탭 동작을 관찰 사실로 쓰지 않는다.
6. **기존 확정 규칙을 보존한다.** 새 이미지의 시각적 차이는 충돌로 기록한다. 현재 [Top Navigation 확정안](TOP_NAVIGATION.md)은 정본이며, 새 시안 하나로 대체하지 않는다.
7. **읽기 비용을 최소화한다.** INDEX로 먼저 라우팅하고 필요한 Foundation + Component + Shell/Pattern만 읽는다. 모두 읽게 만드는 문서 구조는 실패다.

## 2. 레퍼런스 입력 기록

원본 자료가 무엇이든 우선 Reference ID를 부여한다. 출처 파일이 없다면 있다고 꾸미지 않는다.

| 필드 | 기록 내용 |
|---|---|
| ID / Version | REF-001처럼 안정적인 참조 ID, 수정 시 버전 |
| Source | 실제 파일 경로·링크·첨부 근거. 없으면 '대화 첨부, 저장소 미등록' |
| Provenance | AI 생성 컨셉 / 실앱 스크린샷 / Figma / 공식 문서 등 |
| Intent | 사용자가 **채택·수정·제외**를 명시한 요소. 표현·제외 사유 보존 |
| Region | 화면별 Header / Actions / List / Bottom / Art 등 관찰 위치 |
| Evidence | 무엇을 봤는가, 다른 시안에서도 반복되는가, 무엇이 불확실한가 |
| Fidelity | 해상도·축소/압축·기울기·생성 이미지 오류·조작 상태 미노출 |

**주의:** AI 이미지의 가짜 문구, 불가능한 버튼, 시각적으로 매력적인 오배치까지 그대로 코드화하지 않는다. 캡처에서 샘플링한 px·HEX는 표시 장치/압축/스케일의 영향을 받으므로 **추정값**으로 표시하고, 확인 가능한 원본·프로토타입에서 검증한 후에만 확정한다.

## 3. 시각 분해 대상과 분석 질문

### A · 화면 골격 (Layout & Shell)
- OS 상태바 / Root·Detail Header / 콘텐츠 영역 / Tabs·Filter / 고정 CTA / Bottom Navigation / Safe Area로 나눈다.
- 무엇이 스크롤되는가, 어떤 부분이 고정되는가, 헤더의 유형은 무엇인가.
- Root, Detail, Utility, Immersive, Overlay 등 **동작과 네비게이션 가시성을 기준**으로 분류한다. 최종 탭 목적지 자체는 별도 결정.
- 보이는 것과 실제 동작을 분리한다. 스크린샷만으로 숨김·전환 애니메이션·뒤로가기 동작을 확정하지 않는다.

### B · 정보 계층 (Hierarchy)
- 1차 주목 / 2차 주목 / 보조 정보를 크기·굵기·색 대비·위치·여백·이미지 비중으로 분석.
- Headline, Section Title, Body, Metadata, Caption, Numeric Emphasis 역할을 분리.
- Surface / Section / List / Banner / Floating Overlay를 구별. 시각적으로 경계가 있어도 무조건 Card가 아님.

### C · Foundation (표현의 바탕)
- 색: Brand Primary·Secondary, Neutral, Semantic, Custom Theme, Illustration Asset을 분리.
- 표면: Canvas, Default, Muted, Floating과 Foreground, Stroke, Shadow 등 **의미 기반 토큰** 추출.
- 타이포: 서체·사이즈·굵기·줄높이·한글 가독성·위계.
- 공간:  간격 스케일, 정렬, 콘텐츠 폭, 밀도, 라운딩, 선 두께, 그림자·깊이.
- 동작: pressed/selected/focus/loading, 전환 속도, 피드백, 동작 줄이기.
- 아이콘: 뷰박스·실제 도형 면적·stroke/cap/join·활성 스타일·광학적 균형.

**색상 주의:** 클랜 사용자 지정 컬러나 아이템의 금색, 특정 이벤트의 보라색은 브랜드 전역 토큰이 아니다. 3D 아이템 재질과 UI 버튼 소재를 억지로 통일하지 않는다.

### D · 컴포넌트 식별 및 강조 계층 (최우선)

특히 **버튼·행동·선택 요소를 화면에서 전부 수집한 뒤 역할을 분류**한다. 둥근 모양이라는 이유로 같은 컴포넌트로 합치지 않는다.

| 검토축 | 에이전트가 확인할 것 |
|---|---|
| Semantic role | Action Button / Icon Button / Text Button / 링크 / 선택 / 탐색 중 무엇인가 |
| Emphasis | 핵심 CTA(High) / 일반 행동(Medium) / 부가 행동(Low) 중 어느 계층인가 |
| Tone | Neutral / Brand / Positive / Warning / Critical 등 **의미** |
| Treatment | Solid / Weak / Outline / Ghost 등의 **시각 표현** |
| Anatomy | Container / Label / Prefix / Suffix / Badge / Icon 등의 교체 가능한 슬롯 |
| Size & Layout | 높이·너비(hug/fill)·패딩·터치 면적·정렬·인접 요소 간격 |
| States | Enabled / Pressed / Focus / Selected / Loading / Disabled (관찰된 상태와 설계할 상태 구분) |
| Behavior | 이동·확인·실행·취소 등 동작, 피드백과 오류 처리 |

예: **Brand Solid와 Neutral Solid는 둘 다 High일 수 있음.** 브랜드색만으로 버튼의 중요도를 결정하지 않는다. 화면/행동 맥락별 CTA 개수와 경쟁 정도를 평가한다. 특정 Variant가 적합한 상황과 금지 사례를 함께 기록한다.

- 비슷하게 생긴 Tabs / Segmented Control / Filter Chip은 **탐색·뷰 변경·필터링** 등 행동이 다르면 분리.
- 같은 List Item이라도 Prefix, Title, Detail, Suffix와 상태·정렬만 다르면 하나의 범용 컴포넌트 및 Variant로 처리.
- 특정 제품 데이터(퀘스트, 친구, 상자)를 채운 조합은 기본 컴포넌트의 정의가 아님.
- 이미지에 없는 상태·접근성 처리는 플랫폼의 검증 가능한 원칙으로 **추가 설계**할 수 있으나 이미지에서 발견했다고 주장하지 않음.

### E · 범용 콘텐츠 재료
- List / List Header / Divider / Avatar / Badge / Chip / Tag / Image Frame / Progress / Field / Textarea / Sheet / Dialog / Snackbar / Skeleton 등의 **역할과 슬롯**을 추출.
- 반복되는 구성은 공통 Pattern 후보로 분리. 결합 자체가 독립된 행동·상태·기능을 제공하지 않는다면 무작정 새 컴포넌트를 만들지 않는다.
- 콘텐츠의 문구·상품·아이템 수량·목록 정렬 순서는 별도 화면 데이터이며 시드 규칙이 아님.

### F · 3D 에셋과 이벤트 표현
- 물체의 시점, 광원 방향, 재질, 배경, 그림자, 비율, 안전 여백, 축소 시 읽힘을 **Asset Presentation**으로 분리.
- 일상용 작은 아이템 / 강조 배너 / 획득 연출의 표현 강도를 별도로 정한다.
- 3D 보상에 사용하는 빛·금속 느낌·입체감을 일반 UI의 표면·버튼으로 전파하지 않는다.
- 디지털 마스코트와 패션 소품이 서로 다른 아트 톤이어도 되는지 등은 **사용자의 명시적 기준**으로 검증한다.

## 4. 문서 분산 구조 (제안, 지금은 빈 파일 생성 금지)

```text
docs/design/
  SEED_AUTHORING_GUIDE.md          # 이 문서, 작업 방식만
  TOP_NAVIGATION.md                # 기존 확정 명세, 정본 유지
  seed/                            # 실제 추출/승인 작업 때만 생성
    INDEX.md                       # 단일 검색·라우팅 진입점
    foundations/                   # colors, typography, spacing, radius, iconography, motion...
    components/                    # action-button, list, chip, badge, input, tabs...
    patterns/                      # 여러 컴포넌트의 반복 결합·피드백
    shells/                        # Root/Detail/Utility/Immersive/Overlay, nav visibility
    assets/                        # 3D/2D 에셋 표시·일러스트 레벨
    references/                    # 입력 출처·채택·제외 및 근거
```

분리 기준은 **무엇이 바뀌었을 때 어떤 파일만 고치면 되는가**임.

- **Foundations:** 다수 컴포넌트가 공유하는 토큰. 각 컴포넌트에서 색상·Radius 재선언 금지.
- **Components:** 슬롯·상태·조작을 가진 범용 재료. 같은 역할이 재사용될 때 독립 문서로.
- **Patterns:** 반복되는 조합/배치/상호작용. 새로운 종류의 버튼이나 데이터 모델을 뜻하지 않음.
- **Shells:** 글로벌/상세 헤더와 하단바 가시성, 뒤로가기, 페이지 스크롤 계층. **홈 콘텐츠를 고정하지 않음**.
- **Assets:** 3D 물성과 일러스트를 어떻게 담을지. 상점·퀘스트라는 제품 콘텐츠 파일은 아님.
  - 이 폴더의 `assets/`는 **아트 표현 명세의 문서 위치**다. 앱·preview가 재사용할 SVG/PNG/WebP의 실제 정본 위치는 [공통 에셋 관리 계약](../docs/assets/README.md)을 따른다. 문서 경로와 배포 자산을 혼동하거나 같은 바이너리 파일을 여러 곳에 복제하지 않는다.
- **References:** 원본 첨부 근거·관찰 기록. 전체 시안과 새 시스템 규칙을 동일한 문서에 반복하지 않음.
- **DECISIONS / OPEN:** 확정과 미결 기록은 기존 파일에서 관리. 시드 문서가 승인 주체를 대체하지 않는다.

**파일 증가 억제:** 단일 페이지에서만 발생하는 배치는 로컬 조합으로 두고, 재사용되거나 공통 API가 필요한 경우만 컴포넌트로 승격한다. 폴더/파일을 먼저 채워놓지 않는다.

## 5. INDEX 설계: 어떤 에이전트도 필요한 것만 열게 하기

seed/INDEX.md는 **짧은 라우터(권장 1~2페이지)**다. 시드 전체를 복사하지 않는다.

모든 등록 항목은 다음 열을 가진다.

| Key | 포함 항목 |
|---|---|
| ID / Name / Aliases | 안정적인 ID와 검색용 동의어 |
| Category | foundation / component / pattern / shell / asset |
| Read when | 어떤 작업·UI 문제에서 읽어야 하는지 명시 |
| Path | 실제 존재하는 파일의 상대 경로 |
| Depends on | 직접 선행 항목 ID만 |
| Status | approved / proposed / needs-validation / deprecated |
| References | 참조 ID·합의 결정 ID. 없는 근거 생성 금지 |

### 선택적 독해 절차

1. 요청을 **화면 유형 / 사용자 동작 / 정보 표현 / 필요한 에셋**으로 분해.
2. INDEX만 먼저 읽어 관련 항목 검색(이름/별칭/read_when).
3. **Shell → 관련 Component → 해당 Foundation/Pattern 의존성**만 열기.
4. 아이템의 시각 품질이 중요할 때만 Asset 문서 추가.
5. 미확정 판단이 필요한 경우만 OPEN을 확인. 전체 문서를 매번 정독하지 않는다.
6. 디자인 토큰의 전역 변경처럼 영향이 큰 경우에만 관련 카테고리 전체를 넓혀 읽는다.

예: 단순 폼 페이지는 Detail Shell, Field, Button, 입력/색 토큰이면 충분하다. '퀘스트/클랜/상점' 전체 문서를 읽을 필요가 없다.

INDEX는 **없는 파일을 가리키지 말 것**, 파일 이동·삭제 시 같은 커밋에서 갱신. 중복된 원본 사양을 만들지 않고 정본을 링크한다. 이미 확정된 [Top Navigation](TOP_NAVIGATION.md)은 재작성 대신 참조.

## 6. 개별 문서 작성 규격

각 컴포넌트 문서는 간단한 메타데이터와 공통 구조를 사용한다. **예시는 템플릿이지 확정된 컴포넌트 파일 생성 지시가 아니다.**

```md
---
id: <stable-id>
category: component
status: proposed
read_when: <검색 트리거/사용 상황>
depends_on: [<필요한 id만>]
references: [REF-001]
---
# <범용 컴포넌트 이름>
## Purpose / Non-goals
## Anatomy                 # 고정 슬롯과 교체 가능 슬롯
## Properties / Variants   # tone, emphasis, size, width 등 필요한 축만
## States / Behavior       # 관찰 / 새 제안 분리
## Tokens                  # Foundation 정본을 참조
## Guidelines              # Do / Don't / 권장 사용·금지 사용
## Accessibility           # 명칭·대비·터치·키보드·동적 글자
## Evidence & Confidence   # 참조 이미지 ID/영역, 근거의 강도
## Dependencies & Impact   # 변형/수정이 영향을 주는 곳
```

별도의 화면별 문서와 컴포넌트 자체의 명세를 혼동하지 않는다. 사용 예시는 데모 데이터로만 두고, 문구·보상·멤버명·퀘스트 의미를 컴포넌트 API에 고정하지 않는다.

## 7. 승인, 충돌, 품질

**두 가지 평가 축을 별도로 관리**
- **승인 상태**: approved(명시적 승인), proposed(제안), needs-validation(확인 필요), deprecated(폐기).
- **증거 확신도**: high(직접·반복 관찰), medium(부분 확인), low(한 컷/생성 시안 의존).

**이미지 속에 선명하게 보인다는 이유로 승인된 디자인이 되는 것은 아님.**

충돌 시: 기존 명시 결정 → 새 사용자 피드백 → 다수 이미지의 공통성/맥락 차이 → Variant·Pattern 분리 가능 여부 → 미결 OPEN 기록 순서로 검토한다. 충돌을 임의로 메우지 않고 출처와 영향을 남긴다.

### 최종 검증 체크

- [ ] 원본 및 '좋다/제외'라는 사용자 평가가 Reference ID에 연결됨
- [ ] 직접 관찰 / 추정 / 제안 / 승인 사항을 구분함
- [ ] 화면 예쁜 부분과 재사용 가능한 Foundation·Component 규칙을 분리함
- [ ] 버튼의 **역할·강조·톤·표면·슬롯·상태·동작**을 구분함
- [ ] Tabs / Segmented / Chip, List / Card, Surface / Overlay 등 의미 차이를 검토함
- [ ] 클랜 커스텀색·보상 에셋색·의미색을 전역 브랜드색으로 승격하지 않음
- [ ] 이미지에 없는 기능이나 XP·재화·게임 시스템을 새 요구사항으로 만들지 않음
- [ ] 아이콘/헤더/하단바/안전영역의 사용 규칙과 접근성·반응형 고려
- [ ] INDEX의 ID·경로·의존성이 실존 문서로 연결되고 중복 정본 없음
- [ ] 합의된 헤더 및 기존 DECISIONS와 충돌이 없거나 OPEN에 기록됨
- [ ] 시각 재현 시 비교: 동일한 컴포넌트가 여러 화면에서 일관적인가? 라이트/다크·빈/로딩/선택 상태는 구분됐나?
- [ ] 요청이 가이드 작성뿐이면, 실제 시드 토큰 확정·앱 구현·불필요한 파일 생성은 하지 않음

**성공 기준:** 어떤 새 이미지로부터 추출했어도 화면별 임시 예외 대신 재사용 가능한 원리와 컴포넌트 라이브러리가 남고, 다음 에이전트가 전체 문서를 읽지 않고 관련 요소만 정확히 선택할 수 있다.
