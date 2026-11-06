# 갓생패치 · 문서 전용 new 브랜치

이 브랜치는 **문서만 보관**한다. 기존 Android 코드·API·데이터·에셋은 [main](https://github.com/jaksim0sec/questYourLife/tree/main)에 남아 있다.

## 다음 작업에서 먼저 볼 곳

갓생패치는 AI가 현실 목표를 퀘스트로 바꾸고, 클랜 협업과 랭킹 경쟁, 유저가 만드는 퀘스트북·이벤트의 유행을 통해 사람들과 계속 연결되는 소셜 퀘스트 플랫폼이다. 치지직·당근·유튜브·인스타처럼 새 활동과 트렌드가 살아 있는 느낌을 지향한다. 현실의 행동과 성취는 손에 잡힐 듯한 상자·캐시·트렌디한 이어폰·패션 소품으로 이어지고, 사용자는 붉은 도장 마스코트를 꾸며 자신을 투영한다. 깔끔하고 절제된 일상 UI와 강한 보상 표현의 균형으로 게임하는 듯한 경험을 만든다. 구체적인 피드·실시간 갱신·랭킹 정책은 관련 미결 문서에서 확인한다.

| 작업 | 필요한 문서부터 읽기 |
|---|---|
| 제품 의도·화면 흐름 | [DECISIONS](./DECISIONS.md) → [OPEN](./OPEN.md) → [제품 색인](./product/README.md)의 관련 항목. 앱 정체성이나 전체 UX를 다루면 [VISION](./product/VISION.md)도 읽는다. |
| 기존 시드로 화면·컴포넌트 구현 | [DECISIONS](./DECISIONS.md) → [OPEN](./OPEN.md) → [v0.15 적용 계약](./design/seed/implementation/v015.md)과 [토큰](./design/seed/implementation/v015-tokens.json) → [INDEX](./design/seed/INDEX.md)의 직접 관련 항목만. 헤더는 [TOP_NAVIGATION](./design/TOP_NAVIGATION.md), 아이콘은 [Soft SVG 원본 56종](./design/seed/assets/icons/v0.14/README.md). |
| 컨셉 이미지에서 규칙 추출 | [DECISIONS](./DECISIONS.md) → [OPEN](./OPEN.md) → [작성 가이드](./design/SEED_AUTHORING_GUIDE.md) → [INDEX](./design/seed/INDEX.md)에서 기존 규칙과 대조. |
| Android 코드 수정 | 먼저 `main`의 [AGENT.md](https://github.com/jaksim0sec/questYourLife/blob/main/AGENT.md)와 [IDEA.md](https://github.com/jaksim0sec/questYourLife/blob/main/IDEA.md), 이어서 관련 코드와 해당 작업 문서. |

SEED는 Foundations(색·타이포·간격·형태·아이콘·모션), Components(재사용 부품과 상태), Patterns(부품 조합), Shells(화면 골격과 안전영역), Assets(아트와 SVG)로 나뉜다. 수치와 시각 규칙은 v0.15 적용 계약과 토큰에서 확인한다. 화면별 데이터·하단 탭 목적지·테마 기본값·글래스 채택·네이티브 적용은 결정 및 구현 상태를 따로 확인한다. 리서치·이전 시드·감사 기록은 근거를 추적할 때 연다.

## 컨셉 이미지 4장 · 디자인 감성

**차분한 앱 안에서 현실의 행동은 손쉽게 읽히고, 내가 모은 것은 실제 물건처럼 느껴진다.** 굵고 짧은 한글 제목, 낮은 대비의 보조 정보, 둥근 중립 표면과 얇고 부드러운 선 아이콘이 공통 골격이다. 따뜻한 붉은색은 선택·실행·진행과 도장 마스코트에 집중한다. 평소에는 사진·일러스트·소품이 표현을 맡고, 금빛·보라·리본·광원은 특별한 보상 장면에서 강해진다.

| 참조 | 화면이 주는 느낌과 가져올 원리 |
|---|---|
| 라이트 클랜·마이·꾸미기 (S2-08) | 따뜻하고 또렷한 생활 앱. 거친 붉은 도장 얼굴에 골지 비니나 헤드폰처럼 손에 잡힐 듯한 소품을 겹친다. 클랜에서는 함께 쌓은 성취, 마이에서는 내 기록과 취향, 꾸미기에서는 모으고 싶은 물건이 먼저 보인다. 아이템 진열에서는 실루엣이 잘리지 않게 한다. |
| 다크 홈·라이트 퀘스트 (S2-05) | 홈은 책상 조명·운동기구·밤하늘처럼 각기 다른 분위기의 썸네일을 둘러보는 재미가 있다. 퀘스트는 오늘 할 일·남은 분량·다음 행동이 바로 읽히는 실행 화면이다. 같은 시각 문법 안에서 콘텐츠 역할에 따라 밀도를 조절한다. |
| 다크 탐색·클랜 피드·퀘스트북 상세 (S2-07) | 어두운 바탕에서 썸네일과 활동 사진이 먼저 눈에 들어온다. 상세는 큰 대표 이미지에서 목표·기간·진행·참여로 읽는 순서가 이어진다. 사람의 활동이 보이고, 보조 수치와 액션은 그 흐름을 받친다. |
| 시즌·레이드·획득 이미지 (S2-06) | 금속 테두리, 보석, 광원과 그림자로 상자와 획득물에 무게가 생긴다. 획득 연출에서 허용할 표현 강도의 상한을 보여준다. |

이미지의 탭 구성, 화면별 명암 배정, 문구·수치·시즌 패스·가격·레이드·XP·보석은 시안의 예시다. 적용 범위는 [DECISIONS](./DECISIONS.md)와 [OPEN](./OPEN.md), 참조 추적은 [입력 기록](./design/seed/references/inputs.md)의 S2-05~08을 따른다.

## 전체 문서 색인

- [확정사항](./DECISIONS.md) / [미결사항](./OPEN.md)
- [제품 철학·퀘스트·소셜·보상·마스코트](./product/README.md)
- [컨셉 이미지 → 디자인 시드 추출·분산 가이드](./design/SEED_AUTHORING_GUIDE.md)
- [Superpowers 디자인 시드 정합화 제안](./superpowers/specs/2026-11-05-design-seed-reconciliation-design.md) / [단계별 실행 계획](./superpowers/plans/2026-11-05-design-seed-reconciliation.md)
- [디자인 시드 · 선택 독해 INDEX](./design/seed/INDEX.md) — 영역별 문서와 v0.15 확정 범위
- [첨부 8장 전수 감사](./design/seed/AUDIT.md) / [SEED 컴포넌트 커버리지](./design/seed/COVERAGE.md) / [실제 검증·미결](./design/seed/VALIDATION.md)
- [디자인 확정 · Top Navigation](./design/TOP_NAVIGATION.md)
- [디자인 리서치 요약 및 출처](./research/SUMMARY.md)
- [2026-11-04 GPT 리서치 원문 보관](./research/2026-11-04-gpt-design-research.md)
- [작업별 문서 참고 지침](./AGENTS.md)

## 이전 브랜치에서 가져온 문서 무결성 기록

기존 출처: `docs/design-seed-research-2026-11-04`. 아래 SHA는 **이전 브랜치에서 확인한 Git blob SHA**이며, 경로가 달라진 경우 또는 링크를 새 브랜치에 맞춰 수정한 경우에도 원본을 식별할 수 있다.

| 기존 파일 | 현재 경로 | 기존 Blob SHA |
|---|---|---|
| `AGENTS.md` | `docs/AGENTS.md` | `1e82959a750a1b34c12149ac6cf0e7c95e62614d` |
| `docs/DECISIONS.md` | `docs/DECISIONS.md` | `6340f31192c16dc03e16c1afaad05b3074fdd354` |
| `docs/OPEN.md` | `docs/OPEN.md` | `ab3d849f512c8a1a16213259951536b3923e8d2d` |
| `docs/design/TOP_NAVIGATION.md` | `docs/design/TOP_NAVIGATION.md` | `de9a15b6e8bb9232a582955939283f04c7e6cdc3` |
| `docs/product/AVATAR.md` | `docs/product/AVATAR.md` | `41c64dfda834c8923a7c8a12808ec8be5d21d2b4` |
| `docs/product/QUESTBOOKS.md` | `docs/product/QUESTBOOKS.md` | `991b43dfa61c9ac8ffb28ca3d3e7a293dfe2350a` |
| `docs/product/QUESTS.md` | `docs/product/QUESTS.md` | `1a8718dc12b4fde13da93dce29f34d25889d0442` |
| `docs/product/README.md` | `docs/product/README.md` | `b02d8ebf731f59b04402e8f081f181799b7eec2e` |
| `docs/product/REWARDS.md` | `docs/product/REWARDS.md` | `e769740dc489997154c8ab92b4960186e36e1fa4` |
| `docs/product/SOCIAL.md` | `docs/product/SOCIAL.md` | `d7280ce8f09107b469ddd4929cab5793fb129f6f` |
| `docs/product/VISION.md` | `docs/product/VISION.md` | `abae409c2e76771f13ec7755b5ca19800cf6c5a7` |
| `docs/research/2026-11-04-gpt-design-research.md` | `docs/research/2026-11-04-gpt-design-research.md` | `081a0b5184498e10ad0f425893bf3feb5016fef2` |
| `docs/research/SUMMARY.md` | `docs/research/SUMMARY.md` | `98e1d4912bb76fec4976a82f5e3bd860242eadfa` |

- `docs/research/2026-11-04-gpt-design-research.md`는 원문을 수정하지 않고 옮긴다.
- 원래 루트 `AGENTS.md`는 [원본 아카이브](./archive/AGENTS-original-2026-11-04.md)에 그대로 보존했고, 현재 `docs/AGENTS.md`는 문서 전용 경로에 맞게 링크만 보정했다.
- 링크 목적지만 변경한 문서도 원래 제품 의미·인용문은 유지한다.

