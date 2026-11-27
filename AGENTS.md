# 갓생패치 · TRACE 작업 진입점

통합 대상은 이번 재개 작업의 `new2`다. `new`는 디자인/웹 시연 기준, `main`은 기존 Android 구현 기준으로 보존한다. 요청 없이 다른 브랜치에 쓰거나 병합·배포하지 않는다. 시작 시 대상 브랜치 HEAD, 작업 트리, 관련 최근 변경을 확인하고 기존 작업을 보존한다.

TRACE(Task Routing & Agent Continuity Environment)는 저장소에 지식과 작업 맥락을 축적·계승하는 개발 규약이다. [현재 상태](docs/core/CURRENT_STATE.md), [작업 색인](docs/tasks/INDEX.md), [전역 TODO](docs/core/TODO.md)를 진입점으로 사용한다. 작업 주소(예: UI-001/T1)가 주어지면 기록과 실제 코드 및 마지막 검증을 대조한다.

| 작업 | 먼저 읽을 곳 |
|---|---|
| 제품·UX·기능 | [확정](docs/DECISIONS.md), [미결](docs/OPEN.md), [제품 색인](docs/product/README.md) |
| 디자인·버튼·타이포·온보딩·알림·모션 | [SEED](seed/INDEX.md) → 관련 디자인 규격·구현·검증만 선택 |
| 웹 시연 | [웹 시연 계약](web/preview/README.md), [SEED](seed/INDEX.md) |
| 기존 Android·서버 코드 | [기술 지침](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md), [기존 기획](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md), [상세 라우터](docs/AGENTS.md) |
| 에셋 | [공통 에셋 계약](docs/assets/README.md) |
| TRACE 자체 | [인프라 규약](docs/core/INFRASTRUCTURE_SPEC.md), [검증 정책](docs/validation/VALIDATION_POLICY.md), [지식 색인](docs/knowledge/INDEX.md) |

**디자인 작업 진입:** [SEED INDEX](seed/INDEX.md)가 디자인의 단일 색인이다. 디자인 작업은 제품 [확정](docs/DECISIONS.md)/[미결](docs/OPEN.md)과 관련 제품 의도를 확인한 뒤 SEED로 이동한다. 화면 단위가 아니라 필요한 규격·직접 의존성만 읽는다. 제품 결정, 작업 주소·진행·검증·종결·브랜치 규칙은 이 AGENTS와 TRACE에서 관리하며 SEED에 복제하지 않는다. 디자인 추출·규격·시각 검증은 SEED가 소유한다. 정본 경로·ID 변경 시 색인을 함께 갱신한다.

모든 코드 수정은 기존 AGENT/IDEA 기술·보안·데이터 보존 규칙 및 관련 상세 라우터를 먼저 읽는다. 제품 방향 충돌은 O-001에 따라 해결한다. 작은 수정은 최소 직접 검증으로 충분하며, 여러 단계·세션·병렬 작업은 시작 시 작업 기록과 JSON 범위를 등록한다. T는 독립 기능 결과이지 조사·코딩·검증·보고의 분할이 아니다. 실패/미실행을 통과로 기록하지 않는다. UI 승인과 실제 동작 검증, 웹과 Android, 모의 데이터와 실제 서비스는 독립적으로 판단한다.

작업 종료 전 관련 정본과 작업 기록, TODO만 갱신하고 `node tools/check-trace.mjs`로 등록·범위·링크를 검사한다. 원격 대상 브랜치에 커밋 반영 및 필요한 검증 근거를 확인한 범위만 종결한다. 미완료 보고 상태는 지속가능/개입필요/대기 중 실제 하나로 표시한다. 자세한 문서 선택은 [docs/AGENTS.md](docs/AGENTS.md).
