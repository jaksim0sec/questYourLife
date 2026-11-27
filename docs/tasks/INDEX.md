# TRACE 작업 색인

작업 주소는 `AREA-NNN` 또는 `AREA-NNN/Tn`. ID는 고유하며 시작한 T는 재번호화하거나 다른 의미로 재사용하지 않는다. ID가 생략된 단계는 현재 작업이 유일할 때만 해석한다.

| ID | 현재 범위 | 기록 |
|---|---|---|
| INFRA-001 | TRACE 도입 | [상세](active/INFRA-001.md) · [등록](active/INFRA-001.json) |
| UI-001 | 디자인 기술 계승 (진행: T2) | [상세](active/UI-001.md) · [등록](active/UI-001.json) |

장기/병렬 작업은 [템플릿](templates/task.md)과 같은 ID의 JSON을 등록한다. 상태 planned/in_progress/blocked/done, scope는 저장소 상대 경로, contracts는 정본 경로, dependencies는 작업 ID, baseline은 시작 SHA, detail은 상세 경로, next는 다음 행동 또는 주소다. in_progress/blocked끼리 동일·상하위 경로 점유 금지. done은 상세와 JSON을 archive로 이동하고 색인을 갱신한다.

각 T에는 목표·보존·완료 기준·구현·검증·승인·잔여 범위를 기록한다. 구현 상태 PROPOSED/DESIGNED/PROTOTYPED/REGISTERED/INTEGRATED, 결과 PASS/FAIL/PARTIAL/NOT RUN, 승인 대상은 별도다. S는 0~1T, M 1~3T, L 2~4T 기본; 초과는 실제 위험/의존성 근거를 남긴다. 조사/구현/검증/커밋만으로 T를 나누지 않는다. 직접 검증을 완료하지 못한 T는 열어 두되 독립적인 다음 T는 진행 가능하다. 전체 완료에는 필수 최종 게이트와 대상 브랜치 반영이 필요하다.

재개: 최신 new2 → 루트 AGENTS → 이 색인 → 상세 T/JSON → 실제 코드·검증. 종료: 결과와 근거, 잔여·next, 관련 정본 및 TODO 갱신. 간단한 수정은 별도 작업 ID 강제하지 않는다.

[검증 정책](../validation/VALIDATION_POLICY.md) · [전역 TODO](../core/TODO.md).
