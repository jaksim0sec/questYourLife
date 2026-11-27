# TRACE · 축적·계승형 개발 아키텍처

세른벨드 [TRACE 규약](https://github.com/jaksim0sec/sernbeld/blob/main/docs/core/INFRASTRUCTURE_SPEC.md)을 갓생패치에 맞춰 적용한다. 별도 런타임이나 디자인 시스템이 아니다.

- 축적: 확인한 기술 사실·실패 원인·사용자 승인·검증 근거를 각각 해당 정본에 남긴다.
- 계승: 작업 주소, 기준 SHA, 실제 변경, 검증, 의존성, 다음 행동으로 새 세션이 복원한다.
- 소유권: 루트 AGENTS는 라우터, 제품/SEED/에셋은 기존 정본, CURRENT_STATE는 구현 지도, TODO는 남은 일, tasks는 실행 이력, knowledge는 재사용 근거의 색인이다. Superpowers 명세와 계획은 과거 근거 또는 관련 작업의 입력이며 별도 진행 원장이 아니다.
- 작업 흐름: 요구·보존 기준 → 관련 기록과 코드 → 최소 계획 → 구현 → 직접 검증 → 해당 정본 갱신 → 대상 브랜치 반영 확인.
- 규모: S 0~1T, M 1~3T, L 2~4T, XL은 근거 있는 분할. T마다 독립 결과를 요구하며 전체 CI를 반복 강제하지 않는다.
- **T = one work turn.** Complete implementation, necessary verification, documentation and integration in that turn whenever feasible. Do not split routine steps into additional turns. Continue unless a real blocker or required user decision prevents completion. English/Korean may coexist in documents.
- 승인·검증·구현은 분리한다. 확인하지 못한 환경은 NOT RUN/PARTIAL로 표시한다. 기술적 해결 가능 여부와 사용자의 디자인 승인 필요 여부를 구분한다.
- 병렬 작업은 등록된 경로 점유 및 최신 HEAD를 함께 확인한다. 검사기는 등록 경로 충돌만 판정한다.
- 현재 통합 브랜치는 new2이며 main/new의 변경·병합·배포는 별도 요청이 필요하다.

[작업 규약](../tasks/INDEX.md) · [검증 정책](../validation/VALIDATION_POLICY.md) · [현재 상태](CURRENT_STATE.md).
