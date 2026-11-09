# SEED r3 P0 기록

| 작업 | 상태 | 근거/다음 게이트 |
|---|---|---|
| P0-1 | 통과 | `seed-r3`를 `new`에서 분기. `93db235`는 Header Inset·Glyph 중심 코드 및 QA 2개만 수동 선별 적용, 나머지 SEED 문서 변경 미이식 |
| P0-2 | 통과 | `docs/DECISIONS.md` D-013 `proposed` / `docs/OPEN.md` r3 결정 경계 |
| P0-3 | 통과 | `seed/LEDGER.md` 원문 47절·coverage, 개별 행의 최종 정본 반영은 P1/P2 소관 |
| P0-4 | 통과 | `web/preview/qa/progress.cjs` Chromium 320/390/430 light/dark, 비 0.62±0.01/ARIA 620 |
| P0-5 | 통과 | `docs/design/evidence/baseline/`의 PNG 40장 + manifest.json 생성 및 검증 |
| G-P0 | 통과 | P0-4와 P0-5 실제 Action 검증 후에만 P1 시작 |

## 실패/복귀

- 최초 P0 커밋에서는 브라우저 실행 결과를 합격으로 쓰지 않는다. Workflow 실패 시 G-P0 실패로 기록하고 원인 해결·재검증 전 다음 단계 금지.
- 테스트 가능한 브라우저는 Actions Chromium이며 Android·TalkBack은 검증 범위 밖.

## G-P0 검증 기록 [approved]

- 실행: [Actions run 42501506946](https://github.com/jaksim0sec/questYourLife/actions/runs/42501506946) 성공, 검사 job `128795053757` 전체 success.
- P0-4: Playwright Chromium `320/390/430 × light/dark` 6조건, 실제 fill/track `0.61998–0.61999`, ARIA `620` / max `1000`, 실제 높이 `10px`. 별도 Header glyph/target 중심 및 48px 검사 PASS.
- P0-5: `new@3e59cdac4594a552aaa6bf903a7935ef71eebf21`를 별도 checkout, 5상태 × 4폭 × 2테마 = **40장의 PNG 실제 렌더**를 `docs/design/evidence/baseline/`에 commit `2732fd25182d8f831452c2804b3263c8e22a45a0`로 저장. 최솟값 42,350B, 최댓값 312,323B. `manifest.json` 포함.
- P0-3: 47절 / 원문 인용 585행, 각 절 행 수 1 이상. 섹션별 coverage는 LEDGER 정본 참조.
- 실패/복귀: 서버 준비 중 초기 curl 1회 연결 실패 후 재시도 성공, 검증 실행 자체는 0실패. 스크린샷과 Playwright 외의 Android/TalkBack은 미검증. **P1 착수 허용, P2는 G-P1 통과 전 금지.**

## 연결 복구 후 P0-3 원문 추적 보강

- 기존 원문 47절·585행 외에 **계획서 원래 L01–L38 38행**을 ID 변경 없이 별도 표에 추가. 기존 전수 장부의 네 자리 ID와 별도임. 이전 '번호 재매핑 불가' 진술을 원문 행 소실로 오해하지 않도록 구분함.
- P0 베이스라인 촬영 대상 `new@3e59cdac4594a552aaa6bf903a7935ef71eebf21`을 checkout **커밋 SHA로 고정**해 재실행 때 원본이 달라지지 않도록 수정.
- 실제 CI 재시험 이전에는 P0-3 증거 보강을 새로 통과 처리하지 않음. 기존 [P0 Action 42501506946](https://github.com/jaksim0sec/questYourLife/actions/runs/42501506946)은 별도 유지.
