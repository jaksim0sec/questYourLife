# Agent instructions · document routing

> 적용 범위: 문서 전용 `new` 브랜치의 `docs/`. 기존 코드 저장소의 개발 규칙은 `main`의 `AGENT.md`에서 따로 확인. 이 파일은 **작업별 읽기 경로를 지정**한다. 개발 규칙을 재정의하지 않는다.
> 디자인·제품 정의 문서는 문서 전용 `new` 브랜치에 있다. `main`에는 별도로 병합되지 않았다.

## 1. 작업 시작 순서

1. **코드를 수정하는 모든 작업:** 반드시 [AGENT.md](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md) → [IDEA.md](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md)를 읽는다. `AGENT.md`가 기술·보안·마이그레이션·테스트의 권위 있는 규칙이다.
2. **제품/UX/디자인을 논의·설계·수정하는 모든 작업:** [docs/DECISIONS.md](./DECISIONS.md) → [docs/OPEN.md](./OPEN.md)를 확인한다. 확정과 가설을 섞지 않는다.
3. **제품 의미나 사용자 경험에 영향을 주는 작업:** [docs/product/README.md](./product/README.md)에서 주제별로 찾아 읽는다. 정체성을 새로 정의하거나 화면 전반을 설계할 때는 [docs/product/VISION.md](./product/VISION.md)도 필수다.
4. **관련된 파일만 추가로 읽는다.** 단순 버그·빌드 작업에 모든 디자인 리서치를 읽지 않는다. 여러 영역이 연결되면 해당 영역의 문서를 함께 읽는다.
5. 구현 전 **기존 코드와 문서의 현재 상태**를 확인한다. 문서의 미래 지향점, 기존 구현 상태, 미합의 사항을 같은 것으로 취급하지 않는다.

## 2. 어떤 파일을 언제 읽는가

| 파일 | 읽어야 하는 작업 | 취급 방식 |
|---|---|---|
| [AGENT.md](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md) | **모든 코드 수정**, Android·백엔드·배포·에셋 연결 | 기존 개발 규칙 원본. 수정 전에 필수 |
| [IDEA.md](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md) | **모든 코드 수정**, 현재 기능·범위·우선순위 확인 | 현행 제품/구현 문서. 최신 제품 철학과 충돌 가능 |
| [docs/DECISIONS.md](./DECISIONS.md) | 디자인·기획·UX·제품 관련 구현 전부 | **합의된 사실만**. 세부 기능까지 확정했다는 뜻 아님 |
| [docs/OPEN.md](./OPEN.md) | 제품·UX 결정, 새로운 화면·동작 설계, 기존 규칙과 충돌할 때 | **미결/가설**. 임의로 확정하거나 구현 지시로 쓰지 않음 |
| [docs/product/README.md](./product/README.md) | 제품 이해, 대규모 UI 작업, 기획 온보딩, 누락 확인 | 제품 문서 색인·원 발언 대응표 |
| [docs/product/VISION.md](./product/VISION.md) | 제품 정의, 새 기능 평가, 전체 홈·정보구조·디자인 시드 | **현실이 게임이고 퀘스트는 수단이자 본질**이라는 제품 철학 |
| [docs/product/QUESTS.md](./product/QUESTS.md) | AI 계획·퀘스트 생성·완료·진행 흐름 | 현실 목표와 실제 행동의 의미·검증 한계 |
| [docs/product/QUESTBOOKS.md](./product/QUESTBOOKS.md) | 퀘스트북 생성·공유·리믹스·참여·발견·랭킹 | 사용자 제작 콘텐츠·트렌드의 목적. 동작 명세는 미확정 |
| [docs/product/SOCIAL.md](./product/SOCIAL.md) | 친구·클랜·DM·경쟁·피드·공개 범위 | 관계의 목적과 공유·감시의 경계 |
| [docs/product/REWARDS.md](./product/REWARDS.md) | 캐시·상자·보상·장식 구매/획득·경제 UI | **현실 수행 → 인앱 보상 → 꾸미기**. 지급 산식은 미정 |
| [docs/product/AVATAR.md](./product/AVATAR.md) | 마이페이지·마스코트·프로필·아이템·꾸미기 | 단순한 도장 얼굴 + 현실적인 패션이라는 의도·기존 에셋 경로 |
| [docs/design/SEED_AUTHORING_GUIDE.md](./design/SEED_AUTHORING_GUIDE.md) | **컨셉 이미지에서 디자인 시드·컴포넌트 문서화 시** | 추출·분산·INDEX 라우팅·검증 규칙. 실제 시드의 승인 사양은 아님 |
| [docs/design/TOP_NAVIGATION.md](./design/TOP_NAVIGATION.md) | **새 화면·헤더·내비게이션 구현 및 웹 시연** | 확정된 Root/Standard 시각 기준. 하단 내비게이션은 미확정 |
| [docs/research/SUMMARY.md](./research/SUMMARY.md) | 내비게이션·디자인 시스템·글래스·모션·텍스트 규칙 선정 | 검증된 외부 근거, **확신도 및 연구 공백** |
| [docs/research/2026-11-04-gpt-design-research.md](./research/2026-11-04-gpt-design-research.md) | 원래 리서치 표현/맥락 복원, 출처 추적, 재검증 | **수정하지 않는 원문 아카이브**. 채팅 인용 태그는 독립 URL 아님 |

### 기존 기술·시연 문서로 이어질 때

- **DB·RLS·RPC·동기화:** [docs/development/corrective-database.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/development/corrective-database.md), [docs/development/supabase-foundation.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/development/supabase-foundation.md) 및 실제 스키마/코드 확인.
- **꾸미기 에셋·합성:** [docs/assets/cosmetics.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/assets/cosmetics.md)와 현재 리소스·관련 코드를 확인. 제품 의도는 `docs/product/AVATAR.md`.
- **기존 홈/클랜/프로필 시연을 조사할 때:** [docs/home-quests-reference.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/home-quests-reference.md), [docs/development/clan-reference-checklist.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/development/clan-reference-checklist.md), [docs/development/profile-decor-reference.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/development/profile-decor-reference.md). **과거 시안이 새 디자인 시드의 확정안은 아님.**
- **새 내비게이션을 설계할 때:** 제품 문서 + `DECISIONS`/`OPEN` + [docs/navigation-development-draft.md](https://github.com/jaksim0sec/questYourLife/blob/main/docs/navigation-development-draft.md)(기존 참고안) + `research/SUMMARY.md`를 구분해서 확인.
- **그 밖의 기존 작업 명세:** 관련 `docs/development/` 또는 `docs/superpowers/`의 최신 **관련 파일만** 선택적으로 읽고 실제 코드·현재 브랜치와 대조한다.

## 3. 자주 발생할 작업별 최소 독해 세트

| 요청 | 기본 경로 |
|---|---|
| **컨셉 이미지 → 새 시드 추출** | `DECISIONS` → `OPEN` → `design/SEED_AUTHORING_GUIDE` → (생성된 경우) `design/seed/INDEX` → 필요한 요소 문서만 |
| **웹 디자인 시드·홈·전체 UX** | `DECISIONS` → `OPEN` → `product/README` → `VISION` → 관련 제품 문서 → `research/SUMMARY` |
| **AI 퀘스트 기능** | `DECISIONS` → `OPEN` → `QUESTS` + `QUESTBOOKS`(공유·참여 시) |
| **친구·클랜·퀘스트북 소셜** | `DECISIONS` → `OPEN` → `SOCIAL` + `QUESTBOOKS` |
| **보상/상자/캐시/마이페이지** | `DECISIONS` → `OPEN` → `REWARDS` + `AVATAR` |
| **디자인 근거 확인** | `research/SUMMARY` → 필요할 때 원문 아카이브 → 공식 1차 문서 재확인 |
| **기존 Android/DB 버그 수정** | `AGENT` → `IDEA` → 관련 기술 문서·코드 (사용자 경험·제품 의미가 바뀌면 `DECISIONS`/`OPEN` 및 관련 `product/` 추가) |
| **웹 시연의 Android 이식** | `AGENT` + `IDEA` + `DECISIONS` + `OPEN` + 관련 `product/` + 합의된 최신 시연 사양(존재할 때만) |

> 위 표의 `AGENT`/`IDEA` 표기는 **모든 코드 수정 시 항상 먼저 읽는다는 규칙을 생략 표시**한 것이다.

## 4. 충돌·변경 규칙

- **현재 코드 작업의 안전·기술 규칙:** `AGENT.md`를 따른다. **제품의 새로운 방향:** `DECISIONS.md`와 관련 `product/` 문서에 기록된 합의를 참고한다. 기존 `IDEA.md`의 '절제된 체크리스트'·크림 기반 UI 등과 새 소셜 퀘스트 철학이 충돌하면 **조용히 어느 한쪽을 택하지 않는다**. [OPEN O-001](./OPEN.md)의 명시적 이관/합의 이전에 영향을 받는 구현을 독단적으로 진행하지 않는다.
- `OPEN.md`의 가설이나 외부 리서치를 **승인된 디자인·기능 명세로 승격하지 않는다**. 사용자와 합의하면 `DECISIONS.md`의 짧은 결정 항목을 갱신하고, 필요한 상세 문서에 기록.
- **시각 시드 미확정:** 색상, 탭 구조, 글래스 재질, 컴포넌트 치수, 마이페이지·홈 배치는 마음대로 잠그지 않는다. 합의는 모바일 웹 시연을 통해 진행한다.
- 원문 발언은 제품 문서에서 보존하고, 새 해석은 `[해석]` 등으로 구분한다. 연구 원문 아카이브는 수정하지 않는다.
- 기존 시스템·데이터·에셋 보존, 문서화 브랜치의 `main` 병합 금지 규칙은 `DECISIONS.md`를 참조. **이 AGENTS.md 변경 자체는 코드 구현을 허용하지 않는다.**

## 문서 전용 브랜치 안내

- `new`는 앱 코드가 없는 문서 작업 공간이다. `AGENT.md`, `IDEA.md` 및 구형 `docs/development/` 등은 기존 **main**에만 있다. 링크된 파일이 이 브랜치에 존재한다고 가정하지 않는다.
- 이 파일은 `docs/AGENTS.md`에 위치하므로 **루트 `AGENTS.md` 자동 탐색을 대신한다고 가정하지 않는다**. 새 작업에서는 명시적으로 읽는다.
- 콘텐츠 원본은 `docs/archive/AGENTS-original-2026-11-04.md`에도 변경 없이 보존한다.
