# INFRA-001 · TRACE 초기 구축

기준 new 038436f88437861d8faaddda2af91d4124aaf0b9. 대상 new2. 기존 제품, SEED, 앱 기능·데이터·에셋과 main/new 보존.

## T1 재개 가능한 문서·작업 인프라

- 완료 기준: 루트 AGENTS, 현재 상태/TODO, 작업 주소·등록/템플릿, 검증·지식 색인 및 기존 문서 라우팅 정리. 경로/범위 검사기와 부정 사례.
- 구현 범위: 문서 및 tools/check-trace.mjs, tools/check-trace.test.mjs. 제품 코드 변경 없음.
- 직접 검증: 커밋 전 구성·등록·링크 및 검사 로직 확인. 실제 Node CLI/테스트 결과와 원격 반영은 확인 후 기록한다.
- 상태: INTEGRATED 판정 전 검증/반영 근거 필요. 미실행 항목은 NOT RUN.
- 다음: Node 검사 및 테스트, 원격 diff 검토 후 종결. 새 세션의 실제 계승 효과는 후속 작업에서 검증.

## 최종 게이트

기존 정본 보존·new2 반영·필수 검사. Android/웹 UI 실행 검증은 이번 변경의 범위 밖.
