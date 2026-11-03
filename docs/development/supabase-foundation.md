# 개인 데이터 인프라 · 2026-11-03

이 문서는 최초 개인 동기화 구현의 기록이다. 현재 기준은 [교정 명세와 이관 문서](corrective-database.md)이며, 아래 ‘확정 범위’ 중 소셜 제외·5개 테이블 구성은 교정으로 대체된다. 기존 인증·Room·Outbox·event 원장은 보존한다. 최초 인증 Edge 배포 및 실제 가입·로그인·갱신은 확인했으며 실기기는 별도 검증 대상이다.

## 확정 범위

- UI·알림의 원본은 Room. 개인 목표·체크·완료·외관 변경은 오프라인에서 먼저 저장한다.
- 이메일 없이 영문 소문자·숫자·밑줄 ID 3~24자와 비밀번호 8~128자로 가입/로그인한다. 비밀번호를 직접 DB에 저장하거나 앱에서 자체 해시하지 않는다. HTTPS로 Auth에 전달하고 GoTrue가 안전하게 해시한다.
- 별도 서버 없음. Supabase Edge `questyourlife-account`가 가입/로그인에만 관리 권한을 사용한다. 서버 전용 HMAC 키로 ID를 내부 Auth 주소에 매핑한다. 공유 프로젝트의 이메일 인증 설정은 변경하지 않는다.
- 최초 연결은 활성 게스트 DB를 한 계정에 결합한다. 이후 계정은 별도 DB 파일을 사용한다. 로그아웃 시 이전 데이터를 보존하고 새 게스트 파일로 이동한다. 다른 계정으로 이전 기록을 자동 합치지 않는다.
- 기존 캐쉬/구매/이력은 legacy로 보존, 서버 확정 잔액은 0부터 시작. legacy 구매를 서버 소유권으로 자동 인정하지 않는다.
- 클랜·채팅·랭킹·상자 수령·Gemini 서버 기능은 이번 개인 인프라 범위 밖이다. 시연 데이터는 업로드하지 않는다.

## 서버 구조와 권한

`public.questyourlife_` 접두사를 가진 개인 업무 테이블 4개와 기술 테이블 1개:

| 테이블 | 역할 |
| --- | --- |
| user | Auth UID, ID, 서버 잔액/XP, 재고, 장착, 설정, 커서 |
| questbook | 비공개 book→chap→main/daily 정의, UUID, 버전, tombstone |
| quest_join | 자기 참여, 버전, 단계 완료 상태 |
| daily_quest | 실행/회차, 완료 이력, 서버 보상, 비신뢰 클라이언트 발생 시각 |
| event | 불변 명령 영수증·원장·증분 after-image, 별도 가입 제한 감사 |

모든 클라이언트 테이블 쓰기는 금지. RLS는 자기 행 조회만 허용한다. 공개·클랜 멤버 접근 정책은 없다. security-definer 함수는 빈 search_path와 명시적 권한을 사용한다.

- `questyourlife_bootstrap`: 신뢰된 Auth app_metadata를 확인하고 자기 프로필 생성.
- `questyourlife_apply_batch`: 최대 50개/256KB. 사용자 행 잠금 아래 소유권·버전·명령 해시·회차·재고·가격을 검사하고 도메인/원장/잔액/커서를 원자적으로 반영.
- `questyourlife_pull_changes`: 개인 순번 cursor/watermark 기반 최대 200개, 삭제 포함. 전역 sequence의 미커밋 간격을 사용하지 않는다.
- `questyourlife_signup_gate`: service role만 호출, 가입/로그인 시도 제한. 비밀번호는 RPC/업무 테이블/로그에 넣지 않는다.

정책 v1: 인정 실행 1회 20 C 및 20 XP, 서버 접수일 **Asia/Seoul 기준 하루 100 C 상한**. 연결된 단계/일일 실행은 한 번만 지급한다. 가져온 과거 완료·간단 체크리스트 토글은 지급하지 않는다. 클라이언트 시각/금액/구매 가격은 보상 판정 근거가 아니다. 실천 자체를 입증하는 시스템은 아니며 자기 신고와 정책 한도를 사용한다.

## Room v5와 동기화

기존 v1→v4 migration을 유지한다. v4→v5는 모든 행에 고정 UUID와 인덱스를 추가한다. 기존 `goal_steps` 테이블을 보존하면서 `stable_goal_steps`에 복사하고 UUID를 PK로 사용한다. 표시 위치를 재사용해도 완료 단계가 대체되지 않는다. 기존 일일 실행에 step UUID를 연결하고 구매 출처/legacy 잔액을 보존한다.

추가 기술 테이블은 `sync_meta`, `sync_outbox`, `sync_remote_records`, `confirmed_events`뿐이다. 도메인 수정과 Outbox를 한 트랜잭션으로 기록한다. 처음 보낸 요청은 봉인하고 동일 event UUID와 내용으로 재시도한다. 자기 ACK만 후속 미전송 명령의 기준 버전을 갱신한다.

Worker는 네트워크 제약, 30초부터 지수 backoff, 기본 25개/240KB 배치, 한 작업 8분 예산을 사용한다. pull 페이지 적용·원장 영수증·커서를 Room에 함께 커밋한다. 미전송 수정이 있으면 원격 스냅샷만 보관하고 화면 값을 덮어쓰지 않는다. 계정 화면에서 충돌을 로컬 재적용 또는 서버 선택으로 해결한다. 앱 시작/부팅에서 미완료 작업을 복구한다. 주기적 전체 테이블 조회/상시 소켓은 없다.

토큰만 Android Keystore AES-GCM으로 보호하며 자동 백업에서 제외한다. 비밀번호는 지속 저장하지 않는다. 서버 service role, ID HMAC 키, 관리 토큰은 APK에 넣지 않는다. 이메일/전화가 없어 비밀번호 복구와 계정 삭제 UI는 후속 출시 준비 항목이다.

## 배포

대상은 `xpqjjfcyiirospxzykqd`에 고정한다. 관리 토큰은 환경 비밀값 `SUPABASE_ACCESS_TOKEN`으로만 제공한다. 현재 저장한 환경 초안의 Supabase 도메인 허용과 비밀값 구성을 게시/적용해야 이 환경에서 배포할 수 있다. 토큰을 대화나 소스 파일에 붙여 넣지 않는다.

필요 목적지: `api.supabase.com`, `supabase.com`, `xpqjjfcyiirospxzykqd.supabase.co` 및 기존 npm 도메인. 관리 권한은 해당 프로젝트 DB 쿼리·Edge 배포·서버 secret 관리에 한정한다.

```sh
python3 tools/deploy_supabase.py         # 읽기 전용 연결/접두사 점검
python3 tools/deploy_supabase.py --apply # 개인 SQL + ID secret + Edge 배포
```

배포 도구는 advisory lock 아래 앱 접두사 충돌을 확인한다. 기존 미확인 앱 객체가 있으면 덮어쓰지 않고 중단한다. 동일 migration SHA256 marker만 재실행 no-op으로 인정한다. DDL은 한 트랜잭션이며 다른 앱/Auth 전역 설정을 변경하지 않는다. 다음 스키마 변경은 새 forward migration으로 작성해야 한다. 배포 동시 실행은 피한다.

최초에만 32바이트 무작위 `QUESTYOURLIFE_ID_SECRET`을 Supabase secret에 생성한다. **후속 배포에서 이 키를 재생성/교체/삭제하면 기존 ID 로그인이 깨진다.** 기존 secret은 항상 유지한다. 프로젝트 소유자는 안전한 비밀 저장소에 별도 복구 절차를 마련해야 한다. Edge에는 Supabase가 제공하는 service role/anon 환경값도 필요하다. 관리 토큰을 Edge secret으로 넣지 않는다.

Edge는 공개 가입/로그인 진입점이므로 JWT 검증을 끄고 입력 제한·RPC 제한을 적용한다. Edge 생성 후 실제 가입/로그인, 재로그인, 세션 갱신을 반드시 확인한다. SQL 배포 후 Edge만 실패하면 기존 데이터/동일 secret을 유지하고 배포 도구를 다시 실행한다. 테이블 삭제나 앱 삭제를 복구 절차로 쓰지 않는다.

## 검증

```sh
gradle --no-daemon --max-workers=2 :app:assembleDebug :app:testDebugUnitTest
python3 tools/test_supabase.py
python3 tools/test_deploy_supabase.py
npx --yes deno test --allow-env --deny-net supabase/functions/questyourlife-account/index_test.ts
```

SQL 테스트는 포트를 공개하지 않는 일회성 PostgreSQL 컨테이너에서 실제 RPC/RLS/트랜잭션을 실행한다. 실제 Auth 서버는 아니다. 잘못된 원격 접두사 차단과 동일 배포 재실행, UUID 충돌 동시성, 재전달/변조된 이벤트, 다른 사용자 접근, 원장 합계, 가격/보상 위조, cursor 페이지, 연결 완료 중복을 검증한다.

JVM/Robolectric는 모든 이전 버전 migration, 기존 금액/구매/완료 보존, UUID 위치 재사용, 도메인+Outbox 롤백, 중복 영수증, 계정 불일치 롤백, dirty 충돌 보호를 확인한다. Edge 테스트는 네트워크를 막고 입력 크기·비밀번호 전달 경계·시도 제한을 검증한다.

기기/호스팅 미검증: 실제 가입/로그인/토큰 갱신, 오프라인 완료→종료→재접속, 서버 성공 직후 연결 단절/중복 전달, 두 기기 충돌 선택, 기기 재부팅·계정 전환 중 알림, 기존 설치본 덮어쓰기. 실제 인증·네트워크 환경에서 확인 전에는 배포 또는 런타임 완료로 간주하지 않는다.

## 이번 검증 결과와 남은 경계

- Android debug 빌드 성공, JVM/Robolectric 45개 실패 0. Edge 3개 실패 0. 실제 PostgreSQL RPC/RLS 및 세 가지 사용자 간 UUID 동시성 충돌, 배포 접두사 충돌 차단/동일 migration 재실행 검증 성공.
- 수신 알림은 캡처한 DB에 고정하고 계정 전환과 처리를 직렬화한다. 거절/충돌 대상의 후속 명령만 보류하며 독립 작업은 계속한다. 연결된 미전송 단계 완료는 일일 완료도 보호한다. 업그레이드 직후 알림 완료도 실행 정의를 먼저 Outbox에 기록한다. 장착 변경은 즉시 Worker를 예약한다.
- 자기 보상/구매 ACK의 서버 이전 사용자 버전이 일치할 때만 후속 미전송 설정의 기준 버전을 잇는다. 오래된 영수증이나 다른 기기의 변경은 자동 재기반하지 않는다.
- 별도 검토자가 발견한 주요 문제는 재현 테스트 후 수정했다. 검토 세션이 최종 판정 전에 종료되어 전체 독립 승인 판정은 없다. 원격 배포·호스팅 통합·실기기 검증도 아직 남아 있다.
