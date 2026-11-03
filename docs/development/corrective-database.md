# 데이터 구조 교정 · 2026-11-03

기준: 사용자가 전달한 **Corrective Database Implementation Specification**. 기존 개인 동기화만의 범위 선언을 대체한다. 목표는 8개 도메인 테이블과 기존 `event` 기술 원장, 총 9개다. 기존 인증·Room v1→v5·Outbox·불변 원장은 유지한다. 사용자의 “바로잡아서 적용” 지시에 따라 검증 후 지정 프로젝트에 forward migration으로 반영한다.

## 변경 전 불일치

| 파일/경로 | 불일치 | 교정 경로 |
| --- | --- | --- |
| `202611030001_personal_foundation.sql / goal_put` | 작성자 참여 ID가 book ID와 같음, 전체 트리 직접 수정 | 참여 ID 분리, draft 편집과 참여 overlay 편집 분리 |
| 같은 파일 / questbook SELECT policy | 자식의 기본 `is_public=false`로 공개 루트 자식 조회 차단 | 루트 권한으로 자식 접근 판정 |
| 같은 파일 / daily_put, step_complete | 물리 노드 FK 및 `once` 고정 | 참여 트리 논리 ID 검증, 고유 회차 ID, 별개 실행 |
| 같은 파일 / 보상 | 취소·재완료 순환과 지출 후 취소의 부채 미지원 | 기존 불변 원장에 상쇄 이벤트, cash_debt, score_delta |
| `SyncCapture.goal`, `SyncApply.projectBook` | 작성자 트리만 전송·표시, join ID 결합 | 독립 참여 조회 및 base+overlay 공통 resolver |
| `LocalCompanionGateway`, 각 화면 | 서버 알림·클랜·대화 저장 경로 부재 | 네 개 도메인 테이블과 회원 권한 기반 RPC, Room 원격 캐시 |

## 스키마와 JSON 계약

기존 migration 001은 수정하지 않는다. 002는 퀘스트·실행·경제를 교정하고, 003은 누락된 소셜 도메인을 추가한다. 004는 일일 정의에서 제한된 기간의 실제 실행을 생성하며 요청 종료 시 지연 제약 검증 권한을 보장한다. 모든 앱 리소스는 `questyourlife_` 접두사를 사용한다.

| 테이블 | 저장 책임 / 관계 |
| --- | --- |
| user | Auth UID PK, login_name unique, 이름·avatar·inventory·equipped·settings·privacy JSONB, cash/cash_debt/xp, version, 개인 sync_seq/epoch, timestamps |
| questbook | physical UUID PK, logical UUID, versioned root book_id, head parent FK, book/chap/main/daily, owner FK, root publication/edition, tags nullable, data JSONB, sort/version/timestamps |
| quest_join | 독립 UUID PK, user FK, root book FK, active/paused/completed/left, overrides JSONB, node_state JSONB, edit_version/version/timestamps |
| daily_quest | UUID PK, user FK, nullable join FK, source/main logical IDs, immutable occurrence key, scheduled_date, snapshot JSONB, status/reward/completion/version/timestamps |
| alarm | UUID PK, 수신자 user FK, type, payload JSONB, read 0/1, created_at |
| clan | UUID PK, owner FK, name/icon, members/invites/chests/settings JSONB, root questbook_ids JSONB, version/timestamps |
| chat | UUID PK, ai/direct/group, UUID-keyed members JSONB, canonical unique channel_key, metadata/name/timestamps |
| post | UUID PK, chat FK, nullable author FK, user/assistant/system actor, content/payload, nullable same-chat parent FK, timestamps |
| event | 기존 불변 영수증·원장·변경 커서. score_delta, clan_id_at_event, week_start 추가. 새 경제 테이블 없음 |

`quest_join.overrides={patch:{logical_uuid:{name,head,tags,data,sort_order}},added:[{id:logical_uuid,head:logical_uuid,type,name,sort_order,data}],hidden:[logical_uuid]}`. 패치는 논리 ID만 사용하고 원본 물리 행을 복사하지 않는다. 숨긴 노드 하위는 유효 트리에서 제외하되 실행 기록은 보존한다. NULL tags는 상속, 빈 배열은 명시적 제거다. SQL resolver와 Kotlin `QuestTreeResolver`가 같은 계약을 검증한다.

`questbook.data`는 해당 유형에 필요한 description/criteria/required_count/recurrence/estimated_minutes만 가진다. criteria는 완료 조건, required_count는 1회 완료에 필요한 반복량, recurrence는 실행 발생 규칙이다. 실행 snapshot은 당시 유효 이름·설명·조건·시간을 보존하며 원본 수정으로 덮어쓰지 않는다.

`user.inventory`는 기존 Kotlin catalog의 item ID를 키로 소유권 근거 이벤트와 획득 시각만 보관한다. 가격은 catalog에만 존재한다. 캐쉬·XP·부채는 원장과 같은 트랜잭션에서 갱신한다. 구매는 XP/경쟁 점수를 올리지 않는다. 지출 뒤 보상 취소는 부채로 기록하고 차후 수익에서 차감한다.

`clan.members`, `chat.members`는 사용자 UUID를 키로 역할·가입/탈퇴/읽음 시각을 저장한다. 이름·외관 복사본이나 메시지 배열을 넣지 않는다. 클랜 초대는 해시와 만료/사용 제한을 저장한다. 상자는 고정 자격 스냅샷과 원장 award key로 중복 수령을 방지한다. 경쟁 점수는 event에서 한국 시각 월요일 경계로 집계한다.

## Room 매핑과 이관

기존 Room 스키마와 파일별 계정 격리를 유지한다. `sync_remote_records`는 이미 있는 서버 원문 캐시다. 참여·overlay·소셜 원문을 여기에 보관하고 기존 UI 모델로 투영한다. 별도의 페이지별 테이블을 추가하지 않는다. `goals.syncId`는 버전 루트 식별자, `stable_goal_steps.syncId`는 단계 논리 식별자로 사용한다. join ID는 캐시의 book_id로 조회하고 신규 작성자 참여에 별도 UUID를 보낸다.

기존 봉인 Outbox 요청은 서버 호환 경로로 수신한다. 신규 요청은 독립 join ID와 고정 occurrence key를 명시한다. 미전송 overlay는 기존 Outbox와 도메인 수정을 같은 Room 트랜잭션에 기록한다. 충돌 선택 시 edit_version을 갱신한다. focus_sessions는 계속 로컬 전용이다. 시연 목표/친구/클랜은 서버로 이관하지 않는다.

서버 이관은 기존 join 및 source 참조를 변환하고 완료·구매·원장 데이터는 보존한다. 이미 적용된 migration을 수정하거나 DB를 초기화하지 않는다. 배포 전후 실제 스키마와 migration SHA를 확인한다.

## 검증과 남은 경계

격리 PostgreSQL에서 두 사용자 공개 책 참여·단일 노드 overlay·추가/이동/숨김·버전 고정·반복 회차·미루기·취소/재완료·클랜 단일 가입·상자 중복·채널 권한을 검증한다. Android에서는 공통 resolver, 기존 Room 이관, 계정 격리, Outbox 및 projection 회귀를 검증한다.

실기기 설치/재부팅/알림과 두 실제 기기 간 네트워크 장애 실험은 자동 검증과 구별한다. Gemini 자체 응답 생성은 이번 DB 교정의 구현 대상이 아니며 AI 메시지/계획 제안 저장 및 명시적 적용의 데이터 경로를 마련한다. 테스트 결과와 배포 결과는 완료 시 아래에 기록한다.

## 공개 RPC와 보안 경계

- `questyourlife_apply_batch`: 기존 봉인 이벤트 계약을 보존. `book_join`, `join_edit`/`join_patch`, `join_upgrade`, `join_status`, `book_publish`, `book_edition`, `daily_put`, `daily_complete`, `daily_undo`, 구매/착장/설정을 검증한다. 사용자 행 잠금 아래 도메인·원장·경제·커서가 함께 커밋된다.
- `questyourlife_resolve_tree`: 자기 참여의 원본+overlay 유효 트리. `questyourlife_joined_books`는 이력 포함, `questyourlife_my_books`는 활성 참여만 반환한다. 공개 루트 자식은 루트 접근 판정으로 읽는다. 신규 판본은 새 물리 ID를 사용하며 명시적 업그레이드와 충돌 해결 전까지 참여 판본을 바꾸지 않는다.
- `questyourlife_generate_upcoming`: 자기 활성 참여의 daily 정의만 처리한다. 기본 7일 뒤까지, 최대 14일, 한 번에 최대 25개. 일/주/월 주기 및 간격을 판정하고 `(join, logical source, original recurrence date)`에서 고정 실행 UUID·회차 키를 만든다. 미루거나 취소한 실행도 존재 검사에 포함하므로 원래 날짜에 새 실행을 만들지 않는다. `has_more`이면 다음 제한 배치로 계속한다.
- `questyourlife_social_command`: 소셜 명령의 멱등 영수증. 클랜 가입/탈퇴/양도/초대/공개 책 연결, 상자 확정/수령, AI·DM·그룹 채널, 친구 요청/차단, 메시지 작성/수정/삭제, 명시적 계획 적용, 알림 읽음을 검증한다.
- `questyourlife_social_snapshot`, `questyourlife_posts`, `questyourlife_public_profiles`: 계정별·회원별 제한 조회. 메시지는 created_at/id 복합 커서로 페이지를 나눈다. 프로필은 동의된 이름·avatar의 허용 키·착장만 반환한다. 개인 cash/inventory/settings/login_name은 타인에게 반환하지 않는다.
- `questyourlife_assistant_post`: service_role 전용. 일반 회원은 assistant/system 메시지를 가장할 수 없다. AI payload는 제안일 뿐이며 `plan_confirm`의 명시적 동의와 실제 구조 검증이 있어야 목표가 생성된다.

모든 9개 테이블에 RLS를 적용한다. 회원은 자기 user/join/daily/event/alarm 및 허용된 공개 정의·채널 메시지만 읽는다. clan/chat 원문은 회원에게도 직접 SELECT 권한을 주지 않고 초대 해시·개인 차단 메타데이터를 걸러낸 RPC만 허용한다. 클라이언트 직접 쓰기와 내부 경제 helper 실행 권한은 없다. security-definer 함수는 빈 search_path 및 명시적 실행 권한을 사용한다. 자동 보안 진단의 ‘authenticated SECURITY DEFINER 실행’ 경고는 이 검증된 RPC 진입점에 대해 의도된 구조이며, RLS만으로 사용자 소유권을 생략하지 않는다.

클랜 초대의 원문 토큰은 최초 응답의 `invitation.token`에서 한 번만 반환한다. 클랜 JSON·알림·원장 영수증에는 해시/비밀 제외 메타데이터만 남긴다. 응답을 잃으면 새 event ID로 초대를 발급해야 한다. 일반 Outbox 소비자는 이 일회성 응답을 저장하지 않는다.

주요 인덱스는 정의의 book/head/order, 작성자 루트, 활성 user/book 참여 unique, user/date/status 실행, join/source/occurrence unique(취소 포함), user/event unique, user/award unique, reverses_id unique, 주별 clan/user 경쟁 원장, alarm 수신자/read/date, clan/chat members GIN, post chat/date/id 커서다. 진행률·인원수·순위·주간 점수의 별도 권위 테이블/카운터는 만들지 않는다.

## 명시적 미지원 및 기기 검증 범위

- Gemini 호출/생성 엔진, 첨부 업로드, 스레드 답글(parent_id는 현재 NULL), 실시간 구독/푸시는 포함하지 않는다. 채널/페이지 진입·수동 갱신·동기화 작업 완료 시 저장된 서버 자료를 갱신한다.
- 상자 수령·클랜 초대 발급/공유·권한 양도·그룹 관리 RPC는 구현하고 검증했으나 전용 Android 관리 UI는 추가하지 않는다. 일반 메시지·클랜 생성/초대 참여·알림 읽음·프로필·인기 책 참여·계획 동의는 영속 경로를 사용한다.
- UI에서 직접 정의를 작성·발행하거나 고급 overlay를 편집하는 전체 편집기는 이번 최소 교정에 추가하지 않는다. 해당 RPC·resolver와 충돌 검증은 구현한다. 기존 기기 목표의 제목/단계 변경은 바뀐 필드만 참여 overlay로 보내고 원본 daily 정의/criteria를 덮어쓰지 않는다.
- 실기기 설치·계정 전환 중 알림·재부팅·두 실제 기기 통신 단절 검증은 미실시다. 기존 Room migration/계정 격리/Outbox 재개는 JVM/Robolectric로 별도 검증한다. focus_sessions는 로컬에만 남는다.

## 최종 적용 및 검증 기록

- 대상 `xpqjjfcyiirospxzykqd`에 forward migration `202611030002`, `202611030003`, `202611030004` 적용 완료. 기존 001 SQL과 인증 Edge/ID 시크릿은 변경하지 않았다. 실제 public 앱 테이블은 9개이며 모두 RLS 활성이다.
- migration SHA256: 002 `040f6a6627c4fa6945c2a3555b7e6c9a0bdb20f95ae0945c2ac015586df0bba7`, 003 `2e5e51908cbaa0f8a8091de93b944cc4a9ecd627a3fc7806246609236bf92df6`, 004 `0f6e4a1f4e835bc46c7334ef0c381fd1f710dce389596982bdd02f8fd0ee6857`. 실제 Supabase migration history의 해시와 대조했다. 동일 migration 재실행은 no-op이며 다른 해시는 거절한다.
- `tools/test_corrections.py`: 격리 PostgreSQL 17에서 기존 foundation 보안 테스트, 세 forward migration 및 동일 재적용, 데이터가 있는 join 이관, 기존 불변 영수증 보존, quest/social/recurrence 교정 테스트 모두 통과. 공개 책 공동 참여·부분 overlay·판본 고정·회차·미루기·완료 취소/복원·부채·클랜 단일 가입·상자 중복·메시지 권한·초대 토큰 비저장·AI 명시 동의 검증을 포함한다.
- `assembleDebug` 및 JVM/Robolectric 전체 **67개, 실패 0·오류 0·건너뜀 0**. 추가 overlay의 불필요한 전송 방지, 단일 단계 수정만 저장, 참여 판본 교체 시 동일 Room 목표/역사 유지, 완료 취소의 로컬 반영, 기존 모든 Room migration/계정 격리, durable social Outbox와 계획 동의를 포함한다. 배포 도구 단위 테스트 2개 통과, `git diff --check` 통과.
- 실제 Supabase에서 독립 참여·resolver·기간 제한 반복 생성/재시도·비공개 AI 채널/메시지·RPC 권한을 트랜잭션으로 확인 후 전체 rollback했다. 검증 후 업무 user/book/join/daily/post는 0개이며, 기존 signup 감사 event 2개가 그대로 보존됨을 확인했다. 테스트 계정이나 메시지를 실제 데이터로 남기지 않았다.
- 보안 advisor 확인: 앱 관련 결과는 의도한 인증 회원용 security-definer RPC 진입점 경고 13개다. 권한 검사·내부 helper 실행 금지·빈 search_path를 별도로 검증했다. 공유 프로젝트의 다른 앱에 대한 기존 INFO 항목은 변경하지 않았다. [진단 설명](https://supabase.com/docs/guides/database/database-linter?lint=0029_authenticated_security_definer_function_executable).

재검증 명령: `python3 tools/test_corrections.py`, `python3 tools/test_deploy_supabase.py`, `gradle --no-daemon --max-workers=2 :app:assembleDebug :app:testDebugUnitTest`. SQL 테스트는 Docker의 격리 PostgreSQL에서만 실행하며 호스팅 프로젝트에 테스트 파일을 보내지 않는다. 실제 적용은 환경 시크릿을 사용한 `python3 tools/deploy_supabase.py --apply --database-only`로 수행했다.
