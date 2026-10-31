# questYourLife

목표를 작은 퀘스트로 나누고, 일상에서 체크하고, 보상을 받는 **Android 우선 목표 관리 앱**.

> 현재 상태: Kotlin / Compose / Room 기반의 **로컬 체크리스트 초기 골격**. Gemini, Supabase, 위젯, 알림, 시즌 랭킹은 아직 미구현. 구현 우선순위와 제품 규격은 [IDEA.md](IDEA.md) 참고.

## 기술 구성

| 영역 | 기술 | 현재 상태 |
| --- | --- | --- |
| Android | Kotlin + Jetpack Compose | 초기 앱 구현 |
| 로컬 데이터 | Room | 체크리스트 저장 |
| 홈 위젯 | Jetpack Glance | 계획 |
| 알림 액션 | Notification + BroadcastReceiver | 계획 |
| 백그라운드 | WorkManager | 계획 |
| 계정·동기화·랭킹 | Supabase (Auth / Postgres / RLS) | 계획 |
| AI 계획 생성 | Gemini API via Supabase Edge Functions | 계획 |
| 빌드 | Gradle + GitHub Actions | CI 설정 |

**기본 빌드:** AGP 8.11.1 / Gradle 8.13 / Kotlin 2.2.20 / JDK 17 / compile SDK 36 / min SDK 26 / target SDK 35.

## 휴대전화만으로 개발하기

1. GitHub 모바일 또는 Codespaces에서 파일 수정 후 push.
2. 저장소의 **Actions → Android Debug APK** 실행 결과 확인. main에 push하거나 `workflow_dispatch`로 수동 실행 가능.
3. 성공한 실행의 **Artifacts → questyourlife-debug-apk** ZIP 다운로드.
4. 압축을 풀고 `app-debug.apk`를 갤럭시에 설치. Android에서 요청하는 설치 권한은 직접 확인.
5. 화면이나 동작을 검증하고 다시 수정·push.

**Termux / 로컬 Android SDK는 필요 없음.** CI가 Android SDK와 Gradle을 설치한다. 현재 저장소에는 Gradle Wrapper 바이너리를 넣지 않았으므로 CI는 `gradle/actions/setup-gradle`이 제공하는 **고정된 Gradle 8.13**을 사용한다.

### 주의

- Actions의 APK는 **디버그 테스트용**. Play 스토어 배포용이 아님.
- CI 실행기에서 매번 만드는 임시 debug 서명 키는 달라질 수 있어 APK 업데이트 설치가 막힐 수 있음. 안정적인 업데이트/데이터 보존이 필요해지면 GitHub Secrets에 **고정 디버그 서명 키**를 설정한다. 키를 저장소에 커밋하지 않는다.
- GitHub 비공개 저장소 Actions는 계정별 무료 제공량과 요금/제약이 적용될 수 있다.
- Gemini 키나 Supabase service_role/secret 키는 **앱 코드에 절대 넣지 않는다**.

## 프로젝트 구성

```text
app/src/main/java/com/questyourlife/app/
  MainActivity.kt
  data/QuestDatabase.kt
  data/QuestDao.kt
  data/QuestEntity.kt
  ui/QuestHome.kt
  ui/QuestViewModel.kt
.github/workflows/android-apk.yml
IDEA.md
AGENT.md
AGENTS.md
```

## 다음 순서

1. CI에서 `assembleDebug` 통과 확인, 실제 갤럭시 설치 확인
2. 위젯·알림 완료/미루기 구현 (Room을 유일한 로컬 데이터 원본으로)
3. 로그인과 Supabase 동기화·RLS
4. Edge Functions 기반 Gemini 챕터/미니 퀘스트 생성
5. 포인트·시즌 랭킹 및 운영 정책

현재 체크리스트의 XP는 **화면용 임시 계산**이며 서버 보상/시즌 점수가 아니다.
