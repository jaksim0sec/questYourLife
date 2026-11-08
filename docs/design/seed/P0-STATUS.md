# SEED r3 P0 기록

| 작업 | 상태 | 근거/다음 게이트 |
|---|---|---|
| P0-1 | 작성 | `seed-r3`를 `new`에서 분기. `93db235`는 Header Inset·Glyph 중심 코드 및 QA 2개만 수동 선별 적용, 나머지 SEED 문서 변경 미이식 |
| P0-2 | 작성 | `docs/DECISIONS.md` D-013 `proposed` / `docs/OPEN.md` r3 결정 경계 |
| P0-3 | 작성 | `seed/LEDGER.md` 원문 47절·coverage, 개별 행의 최종 정본 반영은 P1/P2 소관 |
| P0-4 | 게이트 대기 | `web/preview/qa/progress.cjs` Chromium 320/390/430 light/dark, 비 0.62±0.01/ARIA 620 |
| P0-5 | 게이트 대기 | `docs/design/evidence/baseline/`의 PNG 40장 + manifest.json 생성 및 검증 |
| G-P0 | 게이트 대기 | P0-4와 P0-5 실제 Action 검증 후에만 P1 시작 |

## 실패/복귀

- 최초 P0 커밋에서는 브라우저 실행 결과를 합격으로 쓰지 않는다. Workflow 실패 시 G-P0 실패로 기록하고 원인 해결·재검증 전 다음 단계 금지.
- 테스트 가능한 브라우저는 Actions Chromium이며 Android·TalkBack은 검증 범위 밖.
