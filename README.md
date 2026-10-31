# 현생패치 (questYourLife)

**현생패치**는 AI가 목표를 현실적인 챕터와 퀘스트로 나누고, 위젯과 알림으로도 체크할 수 있게 만드는 Android 앱이다.

## 지금 설치할 수 있는 버전

현재는 **앱 껍데기 + 도장 마스코트 아이콘 + 기존 로컬 체크리스트**를 갖춘 미리보기다. Gemini, Supabase, 랭킹, 위젯, 알림 액션은 아직 작동하지 않는다. 구체적인 범위는 [IDEA.md](IDEA.md).

### 모바일에서 APK 설치

1. [GitHub Actions](https://github.com/jaksim0sec/questYourLife/actions/workflows/android-apk.yml)로 들어간다.
2. 가장 최근 성공한 **Android Debug APK** 실행을 연다. 없으면 **Run workflow → main → Run workflow**로 수동 실행한다.
3. 페이지 아래 **Artifacts → hyunsaengpatch-debug-apk** 다운로드.
4. ZIP을 압축 해제하여 `app-debug.apk`를 설치한다.
5. 설치 후 앱 목록에 **현생패치** 이름과 빨간 도장 아이콘이 표시된다.

> 휴대폰에서 코드 수정·커밋하면 GitHub Actions가 빌드한다. Termux 및 로컬 Android SDK는 필요 없다.

### 업데이트와 서명 주의

- 이 빌드는 Play 스토어 출시용이 아닌 **테스트용 debug APK**다.
- CI는 앱의 `versionCode`를 Actions 실행 번호로 설정한다.
- 동일한 debug 서명 키로 만든 APK는 새 파일을 내려받아 덮어 설치할 수 있다. Actions 캐시에 키를 재사용하지만 **캐시는 만료되거나 삭제될 수 있으므로 영구 보장을 의미하지 않는다**.
- 향후 지속 가능한 자동 업데이트에는 고정된 서명 키를 GitHub Secrets에 보관하고 배포 채널을 구성해야 한다.
- APK 설치 업데이트가 **서명 불일치**로 거부되면 기존 앱을 삭제하고 다시 설치해야 할 수 있다. 이 경우 로컬 데이터가 지워질 수 있으므로 미리 백업 경로부터 마련한다.

## 개발 구성

| 영역 | 기술 | 상태 |
| --- | --- | --- |
| Android | Kotlin + Jetpack Compose | 설치용 미리보기 화면 |
| 저장 | Room | 기존 오프라인 체크리스트 유지 |
| 아이콘 | 사용자 제공 도장 이미지, WebP 리소스 | 적용 |
| 홈 위젯 | Jetpack Glance | 계획 |
| 알림 | Android Notification API | 계획 |
| 백그라운드 | WorkManager | 계획 |
| 계정·랭킹 | Supabase Auth / Postgres / RLS | 계획 |
| AI | Gemini via Supabase Edge Functions | 계획 |
| APK 빌드 | Gradle 8.13 + GitHub Actions | CI |

고정 버전: AGP 8.11.1 / Kotlin 2.2.20 / JDK 17 / compile SDK 36 / min SDK 26 / target SDK 35.

## 프로젝트 메모

- [IDEA.md](IDEA.md) · 제품 기획, UI, 우선순위
- [AGENT.md](AGENT.md) · 개발 규칙
- [AGENTS.md](AGENTS.md) · 에이전트 진입점
- 사용자 제공 아이콘을 리사이즈해 `app/src/main/res/drawable-nodpi/launcher_stamp.webp`로 사용한다. 디자인을 새로 생성하지 않는다.
- `MainActivity`에서 미리보기 홈과 기존 `QuestHome`을 전환한다. 기존 패키지명과 Room 스키마를 변경하지 않는다.
