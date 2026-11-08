---
id: S-PAGE-TYPES
category: page
status: proposed
read_when: 하단바·상단 Shell·Back/Close 정책
depends_on: [S-PAGE]
references: [D-011, D-012, D-013]
---

# 네 가지 페이지 유형

## 현행 계약

| 유형 | 하단바 | 상단 Shell | 돌아가기 | 활성 탭 |
|---|---|---|---|---|
| 글로벌 | 보임 | Root | 없음 | 자기 탭 |
| 글로벌이너 | 보임 | Standard + Back | 진입 이력, 딥링크면 부모 글로벌 | 부모 글로벌 |
| 논글로벌 | 숨김 | Standard + Back / Immersive Close | 진입 이력 | 없음 |
| 바텀시트페이지 | 바탕 따름 | Sheet Header | 닫으면 바탕 그대로 | 바탕 유지 |

**판정 Q1→Q4:** (1) 하단 목적지 자체인가→글로벌 (2) 한 글로벌 아래 상세이며 맥락 유지인가→글로벌이너 (3) 여러 화면 진입/독립 과업/입력 손실 위험인가→논글로벌 (4) 짧고 바탕 맥락 유지인가→바텀시트페이지. 모순이면 입력 손실·실제 사용 과업부터 재판정.

**기존 Shell 대응:** Root→글로벌, Detail→Q2/Q3, Utility→논글로벌 기본, Immersive→논글로벌 + Close, Overlay의 Sheet→바텀시트페이지. Dialog는 C-OVERLAY 소유, 페이지 유형 아님.

### web/preview/app.js 실제 24개 라우트 분류

| 페이지 유형 | Route (24개 전체) | 상위 글로벌 |
|---|---|---|
| 글로벌 | home, quests, dm, clan, me | 자기 탭 |
| 글로벌이너 (16) | clan-my, clan-record, clan-activity, clan-discover, clan-rank, clan-reward, clan-war, clan-shop, clan-detail, rank | clan |
| 글로벌이너 (기타) | explore | home |
| 글로벌이너 (기타) | daily, book/focus, book/move, book/sleep | quests |
| 글로벌이너 (기타) | friend-profile | me |
| 논글로벌 | clan-create, clan-created | clan |
| 바텀시트페이지 | clan-invite | clan |

기존 코드 `clan-create/clan-created/clan-invite`는 현재 모두 global-inner로 구현되어 문서 분류와 불일치, P3 구현 검증 전 미완료. `clan.css`의 global-inner nav 숨김은 P3에서 제거. **새 라우트는 추가하지 않는다.**

## 이력

- 기존 Root/Detail/Utility/Immersive/Overlay를 하단바 정책과 혼동하던 상태를 분리했다. Dialog는 독립 Page Type으로 승격하지 않는다.
