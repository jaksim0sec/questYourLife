# SEED · Component Family Integration / 2026-11-10

## 범위·목표
- 브랜치: `feat/seed-layout-composition-20261110`.
- 요청: 승인된 디자인을 재설계하지 않고 Typography의 정밀 사용 규칙, Action Variant/Size/State/보조 브랜드 Gold, 나머지 Component 슬롯 디테일을 **같은 계열에 묶어 정본 규격과 실제 공통 스타일**에 연결.
- 우선 순서: **승인/수정 프로토콜 확인 → 당근 SEED 공식 사례 조사 → 기존 C-*/K03/K07/K09에 규격 반영 → `seed-standard.css` 단일 소유권 구현 → 사용처 시연/검증.**
- 금지: 새 Component 계층/컴포넌트 에디터, 새 폰트 배포, SVG 56 원본·PNG 8 원본 변경, 기존 브랜드 Red/Gold·토큰·버튼 visible height 변경, Android/main/new/DB/클랜 페이지 재구성.

## 근거 자료와 소유권
- 기존 정본: `docs/DECISIONS.md` D-011/D-012/D-013; `components/text-hierarchy.md` K03; `components/action-button.md` K07; `components/list-item.md` K09; `implementation/v015.md` K12; `implementation/shared-style-contract.md`의 2026-11-10 CSS ownership/RED→GREEN.
- 참고 자료: https://seed-design.io/foundations/typography (semantic vs scale), https://seed-design.io/react/components/typography/text (as/style/weight 분리), https://seed-design.io/components/action-button (Variant/Size/Layout/State/Width 분리), https://seed-design.io/updates/how-seed-evolved (불필요한 스타일 수를 줄이면서 역할 명확화).
- 원칙: 당근의 값/폰트/색/컴포넌트 전체를 복제하지 않음. 우리 정본의 목적과 수치가 우선.

## 변경 내용

| ID | 바뀐 것 | 정본/구현 | 승인 경계 |
|---|---|---|---|
| T1 | 5개 의미 역할/10개 style 스케일을 font-size/line-height/weight CSS 변수 매핑으로 연결. 헤더 root/standard, item/body/support/caption/number 구분 | C-TEXT + F-TYPE → `seed-standard.css` | 기존 수치만 사용, 과대 Hero clamp 미사용 |
| T2 | `data-text-weight` 강조와 `data-text-tone=secondary`, number tabular, opt-in 2행 ellipsis | C-TEXT / 공통 CSS | 필요 데이터 접근 경로 없는 truncation 금지 |
| A1 | K07 Action 6종 유지. Small34/Default43/Large48, target48, icon17/gap7/Radius16 보존 | C-ACTION / CSS | 기존 색/크기 변경 없음 |
| A2 | 기존 Gold `reward-secondary`를 서브브랜드 계열로 명시; `aria-busy=true` spinner overlay로 Label/Icon 측정 폭 보존 | C-ACTION / CSS | 호출자 중복 실행 차단·오류 복구 별도 |
| A3 | Brand Weak/Outline의 CSS를 명시적 `data-seed-family-preview` scope로만 구현 | 기존 C-ACTION 후보 / 실험 CSS | K07 승인 6종 확대 아님; Critical 미구현 |
| C1 | Field Label/Help 그룹, readOnly/disabled와 기존 단일 focus border 계약 | C-FIELD / CSS | 새로운 Field 시각값/토큰 없음 |
| C2 | List Prefix/Content/Suffix, top/center alignment, 최소56px, 정적 행과 링크의 상호작용 경계 | C-LIST / CSS | 화면/콘텐츠 전용 List 컴포넌트 없음 |
| C3 | Icon Action 48px target + 기존 24px SVG glyph, 기존 Badge7/Progress3/Surface10/Media 재사용 | C-ICON / 기존 C-*/CSS | SVG 원본·아트/경제 UI 변경 없음 |
| P1 | `web/seed-lab/families.html`에 계열별 비교 사례 | fixture HTML + `families.css/js` | Preview/제품 화면이 아니라 실험 |
| V1 | 공통 CSS 테스트 확장, Family 정적 테스트/모의 DOM 조작 확인 | `*.test.mjs` + 독립 검증 | 브라우저/실기기 통과 아님 |

## 실제 결과·정적 검증
- GitHub 파일·SHA로 반영 확인. 작업 기준 커밋 이후 수정은 **docs, `web/preview/seed-standard.css` 및 해당 테스트, 새로운 `web/seed-lab/families.*`**에 한정.
- HTML 태그 닫힘/고유 ID 검사 성공; 두 CSS(공통/fixture) 괄호 균형 정상; JS 모듈 import를 제거한 본문이 V8 구문 검사 통과.
- 기준 Variant6, Typography10, Progress thickness3/cases6, approved 색 및 Size34/43/48 소스 정적 계약 검사 통과.
- 실제 입력/시트와 혼동 방지: 진행률 fixture는 **native Radio group**을 사용. `0`, `620`, `1000`, `1200`, `loading`, `unavailable` 상태 조작.
- 모의 DOM에서 JS 실행 오류 없음. Size/Width 그룹 변환, 초과값 표기 + fill100 + aria-now1000, unavailable 숨김 + ARIA 제거, 620 복귀, 라디오 선택, 다른 보기 전환, checkbox, theme, icon 시연 등 **10개 시나리오 통과**. 브라우저 렌더 및 실제 DOM 엔진의 computed style 테스트는 아니다.
- `web/preview/index.html`, `web/preview/style.css`, `web/preview/clan.css` 직접 편집 없음. 따라서 **모든 legacy Preview 요소가 자동으로 새 표준을 따른다고 주장하지 않는다.** 표준 `.seed-*` 소비자에만 CSS가 자동 반영된다.

## 미검증·보류
1. **Node 프로젝트 전체 테스트:** 저장소를 체크아웃하여 `node --test web/preview/seed-standard.test.mjs web/seed-lab/*.test.mjs`를 실제 실행하지 못함. 위 검사는 GitHub에서 읽은 소스와 V8 모의 실행 기준.
2. **브라우저 실제 UI:** Pages 배포 완료/개별 경로 200 응답, 320/360/390/430/768/1440 폭×Light/Dark, 200% font, 버튼 hit target의 겹침/측정 크기·spinner label 흔들림, 색 대비 및 레이아웃 overflow는 별도 검증 필요.
3. **Native:** Compose, TalkBack, 실제 모바일 포커스/시트 제스처/IME 검증하지 않음.
4. **연결되지 않은 소비자:** legacy 갤러리의 `.gallery-button` 등은 의도적으로 분리되어 있으며 CSS 통일을 가장하기 위한 추가 override 없음.
5. **기존 승인 대비 검토:** Dark Gold CTA의 `#FFFFFF` on `#957027`은 sRGB 근사 대비 약 **4.12:1**로 보통 크기 텍스트 4.5:1 기준에 못 미칠 수 있다. 이것은 이번 작업에서 색을 바꿔 해결하지 않았으며, D-012 승인값 변경은 별도 사용자 결정 대상이다.
6. **새 후보의 승인:** Brand Weak/Outline·계열 시연장 결과가 기존 K07의 허용 조합을 자동 확대하지 않음. 사용자가 검토하기 전 제품에 배치 금지.
7. **사용 예시의 임의값:** 섹션 내부의 샘플 명칭·숫자는 fixture 예제이고 사용자/클랜/결제의 실제 데이터가 아님.

## 다음 수정 절차
1. 실제 스타일 문제가 나오면 `global token` / `Component variant-state` / `Pattern layout` / `Page content`로 분류.
2. 원래 K03/K07/F-TYPE/F-GEOMETRY와 구현 CSS 진입점 비교. 기존 계열에 있으면 새 컴포넌트 계층/override 만들지 않음.
3. 실패 재현 및 영향 소비자(기존 Preview, Family Lab)를 확인한 뒤 RED test부터 보강.
4. 공통 소유자 `seed-standard.css` 변경과 GREEN/legacy 검사. 실측 검증 여부는 구분.
5. 해당 수정이 D-012 숫자·색·SVG 원본을 바꾸는 경우 **승인 없이 진행하지 않고 보고**.

이 결과는 **SEED 패밀리 구현 1차 개편**이며 21개 전체 Composable 구현, 제품 시연 검증이나 Android 이관 완료를 의미하지 않는다.
