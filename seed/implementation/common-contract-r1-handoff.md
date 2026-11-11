# 갓생패치 SEED 공통 계약 r1 · 후속 웹 개편 실행 인수인계

> 작성 기준: 2026-11-08 · 저장소 `jaksim0sec/questYourLife` · 문서 기준 브랜치 `new`
>
> 문서 성격: **실행 인수인계(runbook)**. 새로운 디자인 정본, 제품 정책, 컴포넌트 계층이 아니다.
>
> 대상: 공통 작업자 A, 클랜 작업자 B, 독립 검수자 C, 계약 결정 리드. 현재 제품 코드 개편은 미착수.

## 0. 다음 에이전트가 우선 알아야 할 것

**공통 계약은 이미 확정·적용됐다. 계약을 다시 설계하지 말고, 기존 SEED 정본을 읽어 웹 시연에 구현한 뒤 검증한다.**

- `new`의 문서 **22개**가 수정본과 동일한 Git blob으로 적용됨. 적용 커밋: [`eb909f7535c712b98eaedc630686803ee0e66da7`](https://github.com/jaksim0sec/questYourLife/commit/eb909f7535c712b98eaedc630686803ee0e66da7). 직전 `new` HEAD는 `1e30837380366502e36ace63720a96813a8e2fa7`.
- 패키지 `SEED_common_contract_r1.zip`의 `updated/docs/`와 22개 수정본 결과 SHA가 일치했고, 원본 blob 충돌은 0건. 원격 적용 직후 compare 결과 **문서 22개만 수정**, 제품 코드·자산·`main` 변경 없음.
- 정본 진입: [`docs/DECISIONS.md` D-013](../../docs/DECISIONS.md) → [`docs/OPEN.md`](../../docs/OPEN.md) → [`../INDEX.md`](../INDEX.md) → 해당 분야 정본 → [`v015.md` K12](v015.md) → [`../VALIDATION.md` K13](../VALIDATION.md).
- 기존 D-011/D-012 승인 규격은 유지. 웹 수치 시작값과 Native 확정값은 구분. 제품의 하단 목적지, 경제·지급, 공개 범위, 새로운 기능, Android 정책은 **미결 상태 유지**.
- K01~K13 전체 결정 설명은 ZIP의 `Hyunsaengpatch_SEED_common_contract_r1_2026-11-08.md` 또는 분야별 기존 정본에 있다. 이 인수인계는 해당 규격을 다시 복제하지 않는다.

### 브랜치·권한 주의

`docs/AGENTS.md`는 `new`를 **문서 전용**이라고 정의하지만, 실제 조회한 `new`에는 `web/preview/app.js`, `clan.js`, `style.css`, `clan.css`, `index.html`이 존재한다. **파일이 있다는 사실은 그 브랜치에서 코드 수정이 승인됐다는 뜻이 아니다.** 웹 코드 개편 시작 시 현재 HEAD와 작업 브랜치/PR 대상을 먼저 확정한다. `new`→`main` 직접 병합, Android 수정, 임의 배포는 하지 않는다.

코드를 수정하는 에이전트는 반드시 `main`의 [`AGENT.md`](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md)와 [`IDEA.md`](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md)를 읽는다. 제품 의미에 영향이 있으면 `docs/product/`의 관련 문서도 읽는다. 이들 사이의 이전 체크리스트 중심 설명과 D-001 제품 정체성 차이는 `OPEN.md` O-001을 따르며, 암묵적으로 한쪽으로 덮지 않는다.

## 1. 작업 ID와 완료 기준

| ID | 소유 | 수행·완료 기준 | 선행 | 현재 |
|---|---|---|---|---|
| H00 | 리드 | 작업 브랜치·HEAD·지침·현행 코드를 확인하고 A/B/C의 파일 소유권을 확정. 추가 제품 결정이 필요하면 `OPEN` 경계에서 중단해 결정 요청 | D-013 문서 적용 | **0% · 후속** |
| H10 | A | `style.css`/`index.html`의 공통 텍스트·액션·미디어·Progress 마크업/스타일을 K03~K12에 맞게 연결. 레거시 홈 동작 보존 | H00 | **0%** |
| H11 | A | `app.js`의 Shell/헤더·해시 라우팅·history entry별 scroll/focus·클랜 지역 상태 복원을 K10/K12에 맞게 구현 | H00, B와 이벤트 계약 일치 | **0%** |
| H20 | B | `clan.js`/`clan.css`의 클랜 정보 객체·Reward 구성·행동 의미와 Progress helper를 적용. 기존 탭·필터·응원·생성 동작 회귀 방지 | H00, K12 공통 인터페이스 | **0%** |
| H21 | B | `captureViewState`/`restoreViewState`, 지역 상태 변경 이벤트, 안정적인 focus 키를 K12대로 제공 | H00, H11과 연동 검증 | **0%** |
| H30 | C | 제품 소스 수정 없이 독립 fixture/브라우저 검수. 5축 결과·반례·미검증 항목을 재현 증거로 기록 | H10~H21 통합본 | **0%** |
| H40 | 리드 | 시각/기능/회귀 판정, 예외 해결·승인 경계 확인. 실제 통과한 범위만 완료 기록 | H30 | **0%** |

**완료의 의미:** H10~H21의 코드 작성만으로 H30/H40 통과를 선언할 수 없다. 문서 22개 적용 완료는 제품 구현 완료와 별개다. P2/P3라는 표기는 원 공통 계약에서 *후속 제품 구현 단계*를 지칭하며, 이 표만으로 새로운 제품 정책·마일스톤을 임의 확정하지 않는다.

## 2. 작업 파일 소유권 및 바로 확인할 현행 결함

| 소유 | 경로 | 2026-11-08 조회 기준 현황·주의 |
|---|---|---|
| A | `web/preview/style.css` | `--sub`, `--brand`, `.progress-track span` 등 레거시 표현이 존재. 공통 별칭은 K11 지정 소스로 연결하고 기존 전역 변수를 일괄 교체하지 말 것. |
| A | `web/preview/index.html` | 기존 홈 정적 마크업과 하단 네비게이션 포함. 공통 표현 연결 시 실제 라우트·기존 홈을 보존. |
| A | `web/preview/app.js` | `render()`에서 `window.scrollTo({top: 0})` 무조건 실행, `previousPage` 기준 Back. `hashchange`와 기존 링크를 함께 고려해 중복 `pushState` 금지. |
| B | `web/preview/clan.js` | 진행률 `620/1000`, `62%` 등 하드코딩, `rerender()`에서도 scroll 강제 0. 현재 `PreviewClan`의 `supports/render/previewMenu` 보존. |
| B | `web/preview/clan.css` | 클랜 Progress 별도 fill 색/높이 규칙 존재. `.progress-fill`은 공통 스타일이 소유하고 지역 CSS는 레이아웃만 담당. |
| C | 독립 fixture·검수 결과 | 제품 파일 동시 수정 금지. 원본/수정본 비교와 스크린샷·동작 증거로 검증. |
| 리드 | `docs/DECISIONS.md`, 관련 `seed/**` | 값·역할·선택의 의미 충돌만 정본 개정. 구현 실패를 덮는 예외 누적 금지. |

이 표의 현행 소스 blob은 `app.js 308f814a`, `clan.js 461c5372`, `style.css 26070868`, `clan.css acba1068`, `index.html 28b585d6`으로 확인했다. **구현 직전 SHA를 다시 확인**하고 차이가 있으면 새로운 코드를 기준으로 재분석한다.

## 3. A에게 전달할 구현 계약

1. **공통 표현:** 기존 `style.css`를 중심으로 `v015.md` K12의 정확한 이름을 사용한다. `.seed-text[data-text-role][data-text-style]`, `.seed-action[data-variant][data-size]` 및 `__face/__label/__icon`, `.seed-media[data-media-kind][data-media-state]` 및 `__image/__fallback`, `.progress-track[data-thickness][data-progress-state] > .progress-fill`. 선언만 하고 실제 마크업을 연결하지 않는 방식은 불합격.
2. **숫자/의미/색:** 텍스트 의미 5역할과 K03 스케일, K11의 `--seed-*` 별칭 소스는 분야별 정본을 따른다. 기존 `--brand`, `--sub`를 전역 변경해 다른 화면을 회귀시키지 않는다. 새 `ItemCard`/`Composite` 컴포넌트나 별도 전역 런타임 JS 모듈을 만들지 않는다.
3. **실제 타깃:** 버튼 face 높이와 클릭 target을 분리해 실제 링크/버튼이 최소 `max(48px, 3rem)`의 비중첩 영역을 갖게 한다. 확대 시 레이블·높이 증가를 허용. 링크 안 독립 버튼 중첩 금지.
4. **Shell/탐색:** `--page-gutter`와 `--page-max`는 A만 소유. Root/Standard 헤더와 본문 가용 폭·시작선 통일. `history.state.seedPreview`는 A가 소유하고 entryId/route/parent/scrollY/focusKey/clan을 현재 entry에 보존한다. 기존 다른 `history.state` 필드를 제거하지 않는다.
5. **왕복·복원:** 떠나는 entry의 유효 상태를 이탈 **전** 저장하고, 도착 entry를 기준으로 `restoreViewState → render → focus({preventScroll:true}) → scrollY` 순서 복원. `hashchange`에서 새 `pushState`를 중복 생성하지 않는다. `focusin`으로 안정적 `id:`/`key:` 타깃 저장. 누락 시 활성 탭 또는 main 첫 heading으로 복구하며 포커스가 가려지지 않게 한다. 하단 고정 높이와 safe inset은 Shell에서 실제 측정.
6. **B 통신:** 클랜 지역 상태 변경 이벤트 `seed:clan-viewstate`를 받으면 `PreviewClan.captureViewState()`로 현재 entry 스냅샷만 `replaceState`한다. B의 내부 click/key 코드를 A가 복제하지 않는다.

A 통과 조건: 기존 홈·라우트가 렌더되고, 새 표현이 화면에 실제 적용되며, 헤더/하단 네비가 기존 동작을 유지하고 클랜 왕복 시 현재 history entry별 scroll/탭/포커스가 복원됨.

## 4. B에게 전달할 구현 계약

1. **정보 구조:** `P-HIERARCHY` K02/K05부터 페이지 목적을 결정한다. 한 Hero에서 상자 홍보, 공동 기여 수치, 구성 아이템, 수령 행동을 무조건 경쟁시키지 않는다. 공동 기록은 숫자+Progress, 홍보는 목적 있는 A2 후보, 보상 구성은 Media/Identity/조건부 수량·상태·상세, 실제 획득 결과만 A3 후보. **예시 상점 가격·장착·지급을 실제 기능으로 구현하지 않는다.**
2. **Progress helper:** 기존 단순 `progress({compact})`를 `progress(input)`으로 확장하고 `label/value/max/state/freshness/thickness/compact`는 `v015.md` K12 타입을 정확히 따른다. 620/1000은 채움과 ARIA가 같은 입력에서 도출돼야 한다. `0`, 목표 초과, loading, unavailable, stale, NaN/Infinity/음수/잘못된 max를 K08대로 구별. 잘못된 label/enum은 오류로 남기며 range를 위장하지 않는다.
3. **Media/행동:** 로딩·오류에도 프레임/이름/행동 보존. 소유·장착·선택·원격 작업 Pending/Confirmed/Failed는 서로 다른 의미. 선택 ARIA가 실제 상태를 나타내야 하고, 클릭 가능한 외형은 실제 행동과 연결. 탭/필터는 기존 의미 유지, 가로 탭 키보드는 Left/Right/Home/End만 처리하며 Up/Down 가로채기 금지.
4. **클랜 로컬 상태:** 기존 `supports/render/previewMenu` 유지하면서 `PreviewClan.captureViewState()`는 `{myTab, clanFilter}`의 복사본 반환, `restoreViewState(partial|null)`는 허용값만 적용. restore로 DOM·해시·scroll·깃발/응원/이름 등 변경 금지. 정상 사용자 탭/필터 값 **실제 변경 시에만** `document.dispatchEvent(new Event('seed:clan-viewstate'))`, restore에서는 dispatch 금지.
5. **분업:** B는 `clan.js`, `clan.css`만 수정. A의 공통 CSS 크기·색·반경을 지역 CSS로 덮지 않는다. 클릭/탭/필터의 유효한 `id` 또는 안정적 `data-focus-key`를 두고 텍스트나 배열 인덱스로 만들지 않는다. 지역 rerender에서 무조건 scroll을 0으로 만들지 않는다.

B 통과 조건: 클랜 홈과 보상 구성의 계층이 분리되고, 620/1000 및 경계값이 일관되며, 필터·탭·응원·생성·게스트 시연이 회귀하지 않고 A의 상태 복원과 연결됨.

## 5. C 검수와 리드의 수락 기준

**5개 축을 분리해 판정:** 실행, 규격, 정보 객체·조합, 페이지 목적, 행동·회귀. CSS 컴파일/렌더 성공은 목적 충족과 같지 않다.

- **화면 매트릭스:** 320/360/390/430px × 라이트/다크 × 선정한 핵심 3화면 = **24조합**. 핵심 3화면 선택 이유를 리드와 기록하고, 768/1440px smoke 추가.
- **상태 경계:** `0/1000`, `620/1000`, `1000/1000`, `1200/1000`, loading, unavailable, stale, 잘못된 label/enum, 이미지 error. 실제 Progress fill은 계산값에서 반올림 1 CSS px 이내, ARIA도 동일한 입력 기반.
- **가독·조작:** 긴 객체 이름, 사용자 글자 200% 확대, 48px 비중첩 타깃, 텍스트/값 누락과 가로 오버플로 없음, 키보드 포커스와 탭 패널 동기화, 상세 왕복 후 scroll/포커스/지역 상태 복원.
- **시각 판독:** 2초 이내 첫 시선의 중심, 아트 A1/A2/A3 역할, 아이템의 이미지·이름·값·상태 이해, 중복 CTA/배지 남발 여부. 자동 검사와 별도로 화면 비교 기록. 실제 사용자 테스트라고 주장하지 않는다.
- **회귀:** 홈·클랜·보상 상세·게스트/가입 시연, Root/Standard 헤더, 하단 아이콘, 메뉴/테마/시트, 기존 링크, 기존 응원·필터·탭, 데모 상태를 확인한다. 없는 서버 기능은 동작한 척하지 않는다.
- **실제 Android·IME·TalkBack·물리 터치**는 본 웹 구현/검수만으로 통과 처리하지 않는다. 해당 검증이 수행됐을 때만 별도 기록.

결과에는 각 ID별 **완료/실패/미검증/차단**, 실행한 브랜치·커밋·브라우저·화면 폭·증거, 남은 결함의 소유자(A/B/리드)를 명시한다. 실패가 계약 탓이면 리드에게, 공통 표현이면 A에게, 클랜 지역 구현이면 B에게 되돌린다.

## 6. 절대 범위를 확장하지 말 것

- 기능 정책 미확정: 실제 상자 지급·수령, 구매 가격, 장착 DB, 클랜 개인정보, 새로운 랭킹 산식, 새 하단 내비게이션 목적지, 유료 패스·레이드·음식 보상.
- 플랫폼 미검증: 실기기 APK, Compose 이식, TalkBack, IME, 서버 RPC/DB, 네이티브 모션. 이번 웹 범위를 넘어가면 별도 승인·계획.
- 구조 확장 금지: 새 전역 ItemCard·Composite 레이어, 타입스크립트 빌드 체계, 별도 공통 runtime JS, 기존 자산 재제작, 레거시 규칙을 묻는 지역 CSS 예외 누적.
- 기존 승인 보존: Soft SVG, 실물 캐시/보상 래스터, 마스코트 자산, D-011/D-012, 현행 Root/Standard 구조, 현재 제품 기능·데이터.

## 7. 착수 순서 및 에이전트 간 전달 방법

1. **리드/H00:** 현재 브랜치·지침·소스·미결 범위를 확인하고 작업용 브랜치/PR을 확정. 첫 화면 3개와 실제 기능/fixture의 구분을 기록.
2. **A/H10 + B/H20:** K12 이름/역할을 재협상하지 않고 **각자 소유 파일**에서 구현. A는 공통 클래스 마크업 예시와 CSS 완성을, B는 `progress(input)`/클랜 DOM 연결을 서로 검토할 수 있게 제공.
3. **A/H11 + B/H21:** A가 이력을 소유하고 B는 지역 상태·이벤트만 제공. 계약을 충족하는 상태 복원을 실제 왕복으로 함께 검증.
4. **C/H30:** 제품 코드 변경 없이 fixture와 스크린샷/인터랙션 검증. 통과하지 않은 조건을 모호하게 `pass`로 승격하지 않음.
5. **리드/H40:** 실패를 A/B/계약으로 분류해 최소 수정 후 필요한 항목만 재검증. 전체 통과 전 Android 이식·main 병합·출시 승인 선언 금지.

### 후속 에이전트에게 전달할 한 문장

> `new`의 D-013 및 K02~K13은 이미 정본에 반영됐다. 제품 코드는 아직 그 계약으로 개편되지 않았다. 새 컴포넌트 체계나 정책을 만들지 말고 `web/preview`의 기존 A/B 소유 파일에서 K12 인터페이스를 충족시키고, K13의 실제 브라우저 결과를 확인한 뒤 남은 미검증 항목을 명시하라.