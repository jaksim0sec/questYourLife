# 현재 구현 지도 · 2026-11-27

기준: [new 038436f](https://github.com/jaksim0sec/questYourLife/commit/038436f88437861d8faaddda2af91d4124aaf0b9). 아래는 해당 트리와 문서에서 확인한 구조이며 새 실기기·렌더 검증 결과가 아니다.

| 영역 | 현재 구현 또는 자료 | 기준/검증 |
|---|---|---|
| 제품 | 소셜 퀘스트 플랫폼 방향 | [확정](../DECISIONS.md), [미결](../OPEN.md), [제품](../product/README.md) |
| 디자인 | SEED v0.15 및 이후 결정, 공통 CSS·아이콘·런타임 | [SEED](../../seed/INDEX.md), [검증 이력](../../seed/VALIDATION.md) |
| New web demo | Shared page shell and D-020 active IA (Explore replaces DM root; DM via menu); Home and Quests session-only prototype integrated (UI-004); Explore, Clan and My remain fixtures | [web/demo](../../web/demo/README.md) · UI-002/T1 |
| 웹 유물 | Preview와 SEED Lab 동결·보관. 활성 개발/배포 대상 아님 | [유물 색인](../../archive/README.md); 실제 서버 아님 |
| Android/서버 | 기존 main에 별도 존재, 아직 new2에 이관하지 않음 | [기존 기술](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md), [기존 기획](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md), [통합 한계](https://github.com/jaksim0sec/questYourLife/blob/main/docs/development/2026-11-04-main-integration-limitations.md) |
| 에셋 | 공통 assets 및 과거 시연 보관 | [관리 계약](../assets/README.md) |
| TRACE | 이 브랜치의 문서 라우팅·작업 등록·검사 | [규약](INFRASTRUCTURE_SPEC.md), [작업 색인](../tasks/INDEX.md) |

갓생알리미 기술 비교와 웹 후보 구현은 기록했다. 실제 웹 렌더/Native 적용·승인은 미검증. 현재 목표는 SEED 자체의 조정·개편 완결이며 Android 정본 구축·적용은 장기 후속 작업이다. [TODO](TODO.md).
