# TRACE 검증 정책

| 범위 | 증명할 것 | 예 |
|---|---|---|
| V1 구조·데이터 | 링크·작업 등록·스키마·에셋 경로 | node tools/check-trace.mjs |
| V2 계약 | SEED/페이지/상태/데이터 보존 | node --test seed/styles/seed-quality.test.mjs seed/runtime/seed-mascot-avatar.test.mjs |
| V3 기능 | 실제 사용 흐름 및 상태 전이 | 실제 앱 단위·통합·기기 동작 검사; 유물 테스트는 참고만 |
| V4 품질 | 버튼·타이포·일관성·모션·접근성·온보딩 | [SEED 검증](../../seed/VALIDATION.md), 동일 상태 캡처/실기기 |
| V5 통합 | 대상 커밋의 코드·데이터·배포 | 기존 main 기술 규칙/CI, new2 이관 이후 실제 계약 |

웹 Preview/Lab은 D-018에 따라 활성 검증 대상에서 제외한다. 과거 웹 테스트·미검증 이력은 보존한다.

T마다 필요한 최소 검증만 지정한다. 전체 최종 게이트는 필요에 따라 합치되 합의된 필수 실기기·데이터 검증을 없애지 않는다. 관련 실패 또는 선행 계약 불충족만 후속을 차단한다. 기록에는 명령·환경·기준 SHA·결과 PASS/FAIL/PARTIAL/NOT RUN·한계를 남긴다. 사용자 승인과 기계 검사, 시연과 실제 서비스, APK 빌드와 설치·데이터 보존은 다르다.

TRACE 검사 자체: `node --test tools/check-trace.test.mjs` 및 `node tools/check-trace.mjs`. 구조·등록·등록된 범위 충돌만 검사하고 전체 제품 문서·실제 Git 동시 수정·런타임을 보증하지 않는다.
