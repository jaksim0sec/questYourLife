---
id: V-CHECKS
category: governance
status: needs-validation
read_when: 문서 검증·승인 범위·실제 UI 검수·후속 인수인계
depends_on: [R-INPUTS]
references: [U-02, D-002, D-003, D-004, D-005, D-011]
---

# 검증과 미결 경계

## 기존 v0.1 납품 범위 (아래 이력은 당시 결과)
컨셉 4장과 사전 자료 3종에서 재사용 문법·토큰 제안·컴포넌트·조합·페이지 골격·아트 표시 규칙을 추출한 v0.1 문서 묶음이다. 앱 코드·DB·기존 에셋·main은 수정 대상이 아니다. 색·치수·새 상태를 최종 승인한 것으로 간주하지 않는다.

## 요구 추적

| 요청 ID | 완료 기준 | 산출물 |
|---|---|---|
| S1 | 원본·영역·의도·관찰·확신도와 교정 근거를 분리 | R-INPUTS의 REF-001~007, U-01~03, 나쁜 예 01~10 |
| S2 | 문법·철학·정보 계층·범용 슬롯·상태·조합·Shell·3D 경계를 정의 | F-* / C-* / P-* / S-PAGE / A-ART |
| S3 | 역할별 분리, 실제 경로·의존성·상태를 INDEX에서 선택, 확정 정본 보존 | INDEX + 이 문서 + OPEN의 O-022 |

## 문서 수준 검증
정적 검사는 경로·의존성·등록 누락·토큰 참조·문서 원본 보존·기본 색 조합을 확인한다. 정적 통과와 실제 시각 품질을 구분한다. 검사 결과의 기준은 동일 커밋의 문서이며 새 파일·토큰을 추가하면 필요한 검사만 다시 수행한다.

- INDEX에 모든 시드 문서가 등록되고 링크가 실존한다. 상단 정본은 참조만 한다.
- ID 중복·없는 dependency·순환 dependency·미등록 reference가 없다.
- 각 Component에 Purpose, Anatomy, Variants, States, Tokens, Guidelines, Accessibility, Evidence, Impact에 대응하는 내용이 있다.
- 새 HEX는 F-COLOR, 숫자·간격은 F-TYPE/F-GEOMETRY 정본에 둔다. 아트의 상대 비율·SVG 규격·모션 시간은 역할별 정본에 둔다.
- 대표 텍스트·버튼·semantic·조작 경계·focus 조합의 불투명 sRGB 대비를 계산한다. 반투명 overlay·이미지·pressed 합성의 실제 렌더는 후속 검증이다.
- 기존 DECISIONS, TOP_NAVIGATION, AUTHORING_GUIDE, 제품 문서 원본을 보존한다. README에는 라우팅만, OPEN에는 제안·충돌만 추가한다.
- 기능·경제·내비게이션의 미결을 컨셉 근거만으로 승인하지 않는다.

## 이번 정적 검사 결과 · 2026-11-05
- 시드 문서 20개, INDEX 등록 20개(외부 상단 정본 포함), Foundation 토큰 95개를 확인했다.
- 경로·등록·직접 의존성·순환·메타데이터·컴포넌트 필수 내용·토큰 참조 검사에서 오류 0개.
- 불투명 sRGB 대비 56개 조합 검사: 일반 텍스트/버튼/의미색은 4.5:1 이상, 필수 경계/focus는 3:1 이상. Light 낮은 강조 글자색은 muted 표면 위 대비 실패를 발견해 보정했다.
- 원본 첨부 4개의 SHA-256과 REF-005의 교정 10개 추적을 확인했다. 제품 AVATAR 링크는 로컬 부분 스냅샷이 아닌 원격 new 트리의 실제 경로와 대조했다.
- 별도 문서 검토에서 확정 승격·컴포넌트/상태·INDEX 구조의 필수 수정 사항은 발견되지 않았다.
- 기존 상단/가이드/확정사항/제품 정본은 보존했다. 문서 검사는 렌더·실기기·동작 검수의 통과를 뜻하지 않는다.

## U-04 후속 조정의 검증 범위
버튼 높이를 유지하며 글자/줄높이/내부 아이콘을 소폭 축소하고 padding을 늘렸다. 역할별 radius와 브랜드 색상 매핑을 조정했다. v0.1 이후 사용자 후속값의 당시 대비는 Light Solid 4.59:1, Light Weak 6.45:1, Dark Solid 7.35:1, Dark Weak 7.79:1이다. 문서 경로·토큰 참조와 전체 기본 대비 조합을 재검사한다. 기존 검수판은 v0.1의 시각 자료이며 이 후속 조정의 렌더 검증 근거가 아니다.

## 실제 UI 검증: v0.1 당시 미수행이었음 · v0.2 웹 갤러리에서 일부 수행

| 검증 | 후속 확인 기준 |
|---|---|
| 320/360/390/430 폭·200% 글자 | 버튼 글자 축소·억지 줄바꿈·padding 붕괴·가로 넘침 없음 |
| 동일 컴포넌트의 여러 화면 재사용 | 같은 tone/size/state가 같은 높이·radius·글자·아이콘 비례 |
| 라이트·다크·긴 한글·긴 숫자 | 역할별 대비·줄바꿈·정렬 유지, 다크 헤더는 D-011 충돌 해소 전 보류 |
| Loading·Disabled·Focus·Selected·Error·Empty·Offline | 실제 state fixture와 문서 표현이 일치 |
| fixed CTA·하단바·키보드·safe-area | inset 이중 계산 없음, 마지막 항목·포커스 target 가림 없음 |
| Outline SVG | 실제 glyph 면적·선·cap/join·기준선 일치, 상단은 Outline 유지 |
| UI/3D 분리 | 일상 버튼 단색, 보상 물성·광원·프레임 유지, 아트가 문자를 침범하지 않음 |
| 스크린리더·키보드·동작 줄이기 | 이름·순서·현재 상태·결과·초점 복귀 확인 |
| 실제 기능·보상·동기화 | 기존 코드·API의 확인된 결과만 표현; 이번 문서 작업에서 기능 검증 없음 |

## 보류·승인 주체
기본 테마, 정확 토큰, 글래스, 하단 목적지/활성 스타일, 새로운 상태 동작, 원본 이미지의 저장소 바이너리 보관은 미확정 또는 후속 작업이다. [OPEN O-022](../../OPEN.md)에서 관리한다. 특정 값이 문서에 적혔다고 approved로 승격하지 않는다. 실험 결과와 사용자 명시 합의를 DECISIONS 정본에 기록한 뒤 해당 문서 status만 갱신한다.

## 문서 증가·변경 원칙
Foundation은 전역 시각 토큰, Component는 슬롯·행동·상태, Pattern은 조합·비중, Shell은 글로벌 프레임, Asset은 물체 표현을 소유한다. 한 페이지의 콘텐츠 개수·문구·배치 요구는 해당 화면 설계로 남긴다. 같은 구조를 제품명으로 복제하지 않는다. 정본 변경은 직접 의존성까지 확인하고 INDEX·근거·검증 기록을 함께 갱신한다.


## v0.2 · 2026-11-05 실제 실행 및 검증 결과

**수행 범위:** 사용자 첨부 S2-01~08 총 8장 원본의 파일명·크기·SHA-256을 대조해 R-INPUTS에 식별표 등록. **원본 자체는 GitHub에 올리지 않았음.** S2-02(광고)는 UI로 복제하지 않고 검증 방법에만 참고, S2-04는 잘못된 UI 반례로만 사용.

### 실제로 수정한 부분
- R-INPUTS의 **50:50 지시 뒤집힘 교정**, 기존 C-ACTION과 P-ACTIONS의 모순 문구 제거. 해당 두 하단 버튼의 동일 너비·높이·정렬은 의도적 교정이며 전역 강제 규칙이 아님.
- 기존 F-TYPE의 section/item/button/hero 과장 수치, F-GEOMETRY 내부 icon/프로필 비율, F-ICON-MOTION의 stroke 후보, F-COLOR의 좀 더 따뜻한 레드를 수정. 정확 수치는 proposed이며 D-011 수치는 덮어쓰지 않음.
- 기존 C-ACTION / C-SELECTION / C-SURFACE의 내용 중 독립 동작이 있는 Icon Button, Tabs, Segmented, Checkbox, Avatar, Image Frame, Bottom Navigation을 별도 계약으로 분리. 나머지 가족 문서를 삭제하지 않고 공통/원칙 라우팅으로 축소.
- P-ACTIONS·P-HIERARCHY·P-STATES·S-PAGE에 **상황→선택 규칙→전환→예외**를 보강. A-ART는 일반 UI와 3D 보상 재질의 경계 보존.
- [AUDIT](AUDIT.md)로 **8장 모두의 Keep/Refine/Split/Remove 판단**, [COVERAGE](COVERAGE.md)로 SEED 카탈로그 대응 및 미사용/후순위 구분.

### 웹 갤러리 검증 (현재 대화에서 생성된 별도 단일 HTML)
**아티팩트:** `gatsaengpatch-seed-v02-review.html` (대화 샌드박스 배포 파일, **new 브랜치에는 코드로 커밋하지 않음**). 원본 8장 축소 자료를 HTML에 직접 내장해 네트워크 없이 참조 가능하게 함. 임의 화면 fixture는 제품 구현 완료나 최종 탭 정보구조가 아님.

| 검사 | 실제 실행/관찰 결과 | 범위·한계 |
|---|---|---|
| 원본 입력 | 8개 S2 reference-card + 8개 이미지 naturalWidth > 0 | 원본 저장소 바이너리는 별도 미등록 |
| Chromuim 렌더 320/360/390/430 | viewport 4개에서 DOM 가로 스크롤 초과 0 | 화면의 모든 시각적 미세 정렬 보증은 아님 |
| 글자 배율 100%, 200% | 양 극단에서 페이지 문서 폭 초과 0 | TalkBack, 실제 OS 글자 배율 검증은 아님 |
| 조작 | 브랜드색 변경, 토큰 변경, Tabs 선택, 라이트/다크 전환, JSON 생성 확인 | 실데이터/서버 기능 아님 |
| 색상 접근성 | v0.2 Light `#D93B3B` + 흰 라벨 ≈ 4.53:1. `#F0444B` 선택 시 약 3.73:1 위험 경고 구현 | 이미지 위 텍스트·반투명 합성·전체 semantic 조합 별도 |
| 다크 | Weak는 dark 전용 배경색으로 전환하도록 웹 갤러리 로직 수정 | **완전한 F-COLOR Dark palette 매핑 대조는 미완료** |
| 50:50 교정 | 웹 그룹을 동일 폭(1fr/1fr) fixture로 렌더링 | 수락/넘기기 그룹의 실제 Android 레이아웃은 미검증 |
| Root vs Detail | Root에는 Bottom Nav, Detail에는 fixed CTA만 표시하는 fixture | IME 키보드/OS safe-area 실기기 미검증 |

**검증 실행 방식:** Python Playwright + headless Chromium의 `page.set_content()` 사용(샌드박스 보안상 file:// 직접 접근 거부). HTML 내부 image 12개가 정상 디코드됨. 전용 QA에서 합격한 범위만 기록한다.

### 아직 검증하지 않은 것
- 실제 Android 앱, 에셋의 투명 배경 원본 재조립, TalkBack, IME·OS Safe Area, 실제 네트워크/오프라인/보상 E2E.
- 사용자 최종 시각 승인. HTML의 '기존안'은 **나쁜 예를 재현한 실험 대조군**이지 v0.1 시드를 픽셀 동일하게 렌더링한 증거가 아님.
- 320 폭에서 200%의 모든 컴포넌트 개별 요소가 최적의 정보량을 보여주는지(문서 폭 검사가 단어 단위 미학 검수는 아님).
- 미확정: 브랜드/색상 라운딩 실제 값, bottom 목적지/활성 스타일, 테마 채택, UI 모션 수치.

**승인 게이트:** 위 결과는 기술 검증의 일부 통과만 의미한다. 사용자 시각 합의 전 기존 토큰과 새 컴포넌트 `status: proposed`를 유지한다. DECISIONS와 D-011은 수정하지 않는다.

## v0.6 시연 및 문서 검증 · 2026-11-05

### 사용자 피드백 보정
- 기본 Checkbox/Radio를 커스텀 Native Input/Indicator로 교체. checked, unchecked, mixed, disabled, focus 상태.
- **Progress 2종 중복 제거:** C-STATUS 한 개 + thickness 6/10/12 변형. Track/Indicator 함께 증가.
- 첨부 사용자 코인 이미지(투명 PNG)를 디코딩·축소하여 오프라인 HTML에 내장. 가격·보유량·획득량을 C-CREDIT 하나로 표시. **GitHub docs-only에는 원본 바이너리 저장하지 않음.**
- 햄버거 SVG 세로 높이, 둥근 Home 외형, 하단 아이콘 27dp; 하단 기본 Outline과 활성 Fill을 각각 별도 SVG symbol로 관리.
- 등급별 프레임은 미세 그라데이션, 내부 등급 이름 없음, 브랜드색 변경은 선택 outline에만 반영. 등급/아이템 경제 정책 확정 아님.
- Brand Weak, Brand Outline, Secondary Gold와 다크 mode 대응, Skeleton/Switch Off/Badge의 테마별 색 분리.
- Segmented를 중첩 Chip 기반에서 단일 트랙 + selected Thumb으로 변경.
- C-TEXT의 배치 5종, P-BTN-ICON의 인라인 SVG 규칙, C-GESTURE와 P-SHEET의 min/mid/full Snap/고정 Footer/닫기·스크롤 규칙 등록.
- 밝은 프로모션 아트의 전경 텍스트가 Dark Theme 본문색을 잘못 상속하던 문제 보정.

### 브라우저 실행 확인
- 단일 HTML v0.6 디자인 실험에서 Chromium headless: 스크립트 오류 없음, **컴포넌트 30개/패턴 10개**, 시연 화면 7개 보존.
- Checkbox 선택/Radio 배타 선택/Segmented 단일 active/coin 이미지 디코드/Progress 10·6·12 높이/다크 테마 토큰/Sheet min→mid→full→닫기/Bottom Nav Fill-Outline 분리/Detail Bottom Nav 숨김/Backdrop 닫힘, 320·360·390·430·768px 가로 넘침 0 검사.
- **29/29 브라우저 검수 항목 통과**, 포인터 Grip swipe 상향→mid / 하향→min 추가 확인.
- 실제 웹 렌더와 Android Native 동작·접근성은 다르다. 200% 글자·TalkBack·실제 OS/IME safe-area·성능·데이터 E2E 및 사용자 최종 시각 승인 **미완료**. 수치/스타일 status=proposed 유지.
- 설계 원칙 검증: [SEED Bottom Sheet](https://seed-design.io/components/bottom-sheet) / [SEED Segmented](https://seed-design.io/components/segmented-control) 참조. SEED 숫자를 그대로 복제했다는 뜻은 아니다.

## v0.15 최종 검증 · 2026-11-05

**아티팩트**: `gatsaengpatch-seed-v15-final-review.html` (대화 첨부, 저장소 미등록), SHA-256 `172670119092e8a3246cdad0ca9e112efbbb401fd3e5f8cd062ecc44baac96f3`. **SVG 공식 원본**: `assets/icons/v0.14/*.svg` 56개, 사용자 ZIP SHA-256 `247ed44a7ba18920a7fd5c575c38cb7e6d7d00ac47eb1800bba56d2b87891ee9`.

| 검증 ID | 내용·범위 | 결과 |
|---|---|---|
| V15-01 | SVG 56개 ZIP 원본 대비 Git SHA-1 일치, XML·CairoSVG 렌더 | 통과 |
| V15-02 | Chromium 4 폭(320·390·430·1440) × 2 테마 × 7 카탈로그 화면 | 56 조합, 가로 넘침 0, JS pageerror 0 |
| V15-03 | SVG 필터 Fill 5 / Outline 51, 이름 검색, 독립 소스 생성 | 통과 |
| V15-04 | Sheet handle 드래그 후 근접 snap, ArrowUp Min→Mid→Full, Escape 닫기 | 통과 |
| V15-05 | 긴 본문에서 native wheel로 콘텐츠 스크롤 시 시트 고정; 스크롤 최상단에서 아래로 드래그→Min, 최하단에서 위로 드래그→Full | 통과, Chromium 마우스 |
| V15-06 | Dark Secondary CTA 글자·아이콘 `#FFF`, 배경 dark gold, Field Focus 두꺼운 외곽 링 없음 | CSS 계산값 검증 |
| V15-07 | Android APK 빌드·Compose 실제 렌더·물리 터치·IME·TalkBack·데이터·네트워크 | **미검증/범위 외** |

**정책:** 사용자 D-012에 따라 이 시드의 규격은 디자인 측면에서 채택. 정확한 앱 품질 검증은 네이티브 적용 후 별도로 진행. 특히 `check-circle-fill.svg` mask는 VectorDrawable 변환 불가 가능성이 있어 검증 필요. 이전 v0.1~v0.6 보고를 최신 검증처럼 재사용하지 않는다.


## 2026-11-08 · 6장 사진 분류와 시연 확인 게이트 (SEED-PROPOSAL)

- 11444 당근 헤더/칩, 11446 당근 미디어 목록, 11447 당근 상세/댓글, 11448 당근 커뮤니티 텍스트 피드, 11445 우리 헤더/정체성 크롭, 11449 우리 클랜 홈 전체.
- 비교시 `P-HIERARCHY`의 주연(텍스트/숫자/아트)을 각 화면에서 먼저 식별. 레퍼런스를 여백·색만 추출하지 말고 컨텐츠형 · 행동형 · 미디어형으로 구분한다.
- `A-ART`는 `img`의 존재만으로 합격 처리하지 않는다. 시각적인 물체 중심성, 텍스트/CTA 충돌, 실제 이미지 해상도/크롭을 캡처로 따진다.
- 검증: JS 페이지 생성 성공 != 브라우저 픽셀 검증 != Android Compose QA. 스크린샷 판독과 앱 동작 판독을 분리.
- 세부 비교 및 테스트 상태는 [CLAN_REFERENCE_AUDIT_2026-11-08.md](CLAN_REFERENCE_AUDIT_2026-11-08.md)를 기준으로 함.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K13 검증·반례·인수 조건

**정본 소유자:** `VALIDATION.md`.

| 검수 사례 | 계약이 요구하는 결과 | 실제 실행 상태 |
|---|---|---|
| 큰 상자+홍보+진행+구성 링크가 한 Hero에서 경쟁 | 목적별 정보 그룹으로 분리, 기존 아트 유지, 홈/상세 책임 구분 | 후속 P2/P3 검증 |
| 이미지+작은 이름뿐인 아이템 | Media/Identity/필요한 State·Value·Action이 맥락상 해석 가능 | 후속 fixture 검증 |
| 620/1000인데 회색 막대 | 같은 입력으로 62% 실제 fill·ARIA 생성 | 후속 브라우저 검증 |
| 목표보다 많이 기여 | 초과값 보존·fill 상한100·지급완료 암시 없음 | 후속 경계값 검증 |
| 0·조회 불가·로딩이 같은 모양 | 유효0 / 조회불가텍스트 / indeterminate를 구분 | 후속 상태 검증 |
| 타이틀·Caption만 모두 Bold | 역할과 스케일 매핑 준수, 필수 정보 가독성 | 후속 화면 판독 |
| 버튼 Glyph만 우측 이동 | Glyph 박스/target 중심 일치, 비중첩 최소48 | 후속 geometry 검증 |
| 내 클랜에서 탭 후 상세 왕복 | 해당 history entry의 탭·scroll·포커스 맥락 복원 | 후속 동작 검증 |
| 보상·상점·보관함의 같은 아이템 | 같은 자산/기초 표현, 다른 정보·행동의 의미 유지 | 후속 독립 fixture 검증 |

검증은 **실행 / 규격 / 객체·조합 / 페이지 목적 / 행동·회귀** 5축을 따로 판정한다. 320/360/390/430 × 2테마의 핵심 3화면24조합, 대표 조건의200%글자·긴 이름·오류·키보드·상세왕복, 768/1440 smoke를 기존 계획대로 실행한다. 필요 없는 상태의 전조합이나 문서 문구를 그대로 되풀이하는 테스트는 만들지 않는다.

정량 기준은 실제 fill 비율(반올림1 CSS px 이내), 비중첩 타깃, 가로 넘침·필수 정보 잘림, 실제 패널/목적지 일치다. 첫2초의 중심·아트 존재감·객체 해석은 독립 화면 판독으로 기록하며 사용자 실험을 했다고 쓰지 않는다.

실패 시 계약에 문제가 있으면 리드, 공통 표현은 A, 지역 구성은 B로 복귀한다. 후속 작업자가 임의 예외를 누적하지 않으며 근거와 영향 범위를 리드에게 전달한다. 시각 부적합이 입증되면 계약은 개정할 수 있으나 이미 검증한 다른 규칙까지 다시 열지 않는다.

## 2026-11-10 · 공간 구성 규격의 문서 검토와 실물 검증 경계

**범위:** [공간 배치](patterns/layout-composition.md)와 [페이지 구성](patterns/page-composition.md)의 설계 계약. 문서 등록·링크 검증은 실제 페이지의 시각 품질을 증명하지 않는다.

| 사례 | 설계 문서가 요구할 판정 | 실제 화면에서 나중에 볼 것 |
|---|---|---|
| 클랜 수치 랭킹 vs 웹툰 미디어 순위 | 콘텐츠명이 아닌 비교/발견 목적에 따라 서로 다른 배치 가능 | 수치 기준선, 미디어 크기, 스크롤 단서 |
| 동일 모자의 상점 Grid vs 보관함 Rail | Image Frame·상태·가격 슬롯은 유지, 위치 배치만 변경 | 제목/가격/장착 상태 손실 여부 |
| 클랜 공동 보상 Hero | 홍보·진행·수령의 목적과 실제 행동 관계 먼저 분리 | 첫 시선, 멤버 활동이 밀리는지, 아트 A1/A2 |
| 이미지 모서리 상태 배지 | C-IMAGE 기존 슬롯을 우선 사용하고 Layer 중복 추상화 금지 | Overlay 충돌·크롭·대체 텍스트 |
| 상세 하단 고정 CTA | S-PAGE가 키보드·safe-area·하단바 책임 단독 소유 | 실제 기기 키보드/스크롤/탭 가림 |
| 세로 List와 Grid의 320 폭·200% 글자 | 열·가로 배치 전환 및 정보 슬롯 유지 요구 | 이름/값/행동 잘림, 터치 목표 중첩 |

**문서 단계에서만 확인 가능한 것:** 기존 승인 계약과 충돌 없음, 선택/배제 이유와 소유권 명료성, INDEX 진입 경로, 링크 유효성, 기존 파일·자산 보존.

**이번 작업으로 확인하지 않은 것:** 실제 HTML/Compose 렌더, 원본 에셋 표시, 320/390/430 픽셀 품질, 클릭·상태·스크롤 동작, 사용자 선호, 최종 디자인 승인. 그 결과를 새로 '통과'로 기록하지 않는다.

검증을 매 단계에 무차별 추가하는 대신, **어느 판단이 실패했는가**부터 분리한다. 목적 오판이면 페이지·섹션으로, 내부 슬롯 문제면 기존 Component로, 실제 CSS/상태 문제면 구현으로 복귀한다.

## D-015 · 2026-11-10 헤더 A/B 승인 및 SEED 동기화

**범위:** 사용자가 웹 SEED Lab에서 비교한 얇은 Top Navigation 버전을 승인. Root `1.30rem/650`, Standard `1.08rem/620`으로 정식 선택; 상단 Pretendard Variable, 자간, glyph `1.59rem`, target `3rem`, 공통 gutter를 NAV-TOP과 공통 CSS·기계 토큰에 동기화했다. Lab A/B 조작과 override는 제거했다.

| 확인 구분 | 결과 |
|---|---|
| 승인 출처·경계 | DECISIONS `D-015` 반영. O-006은 Top Nav 세부만 종료, O-003 실제 앱 IA는 미결 유지 |
| 구현/SEED 값 대조 | 기존 preview `.head`와 Lab `.seed-top-nav`의 Root·Standard 크기·굵기·자간·Glyph 일치 확인 |
| Gutter/Surface | 웹 Root 헤더와 Home/Clan 콘텐츠의 동일 `--seed-page-gutter` 사용, Shell의 `--canvas` 연결 유지 |
| 아이콘 연결 | 클랜 활성 Fill, 마이 비활성/활성 Smile 자산 경로 확인; `WEB_NAV_ITEMS`·preview 연결과 HTML 초기 경로 대조 |
| 자산 보존 | `assets/icons/soft/v0.14/manifest.json` Git blob SHA가 `new` 브랜치와 동일, Soft SVG 56종 규격·승인 상태 유지 |
| 문서 동기화 | NAV-TOP / C-BOTTOM / F-TYPE / F-GEOMETRY / F-ICON-MOTION / C-SVG / C-TEXT / S-PAGE / v015 / Shared Style / Assets / INDEX / Lab README의 역할·숫자·예외 기록 |
| 정적 계약 확인 | 코드/토큰/문서 20개 핵심 항목 일치 확인. 기존 `web/seed-lab/page-type.test.mjs`에 승인값과 gutter·A/B 제거 검사 반영 |
| 실행 상태 | **Node 테스트 실제 실행, Chromium 픽셀/모바일 터치, Pages 배포 동기화, Android Compose/실기기 검증은 이번 작업에서 수행하지 않음** |

**경계:** 정적 일치와 사용자 시연 선택은 기록할 수 있으나, 새 커밋의 웹 렌더링이 새로 검증됐다고 주장하지 않는다. 실기기 가독성·200% 확대·폰트 CDN 실패 fallback 및 Pages 배포 여부는 해당 단계에서 확인한다. 일반 UI `type.root` 등 범용 타이포·기존 Android 리소스는 이번 승인으로 바꾸지 않는다.
