# 현생패치 (questYourLife)

현실적인 목표를 퀘스트로 나누고, 앱 밖에서도 확인·완료할 수 있는 Android 우선 할 일 앱.

## 현재 개발 범위

**동작하는 기능**
- 크림·레드 브랜드 홈과 기존 도장 마스코트, 홈 / 퀘스트 / 기록 / 설정
- 매일 3개의 오프라인 일일 퀘스트 제안. *Gemini가 생성한 퀘스트가 아님*
- 사용자 **수락** 이후 퀘스트 진행 알림 발행, 앱 화면 또는 알림의 **완료** 버튼으로만 완료
- 완료 시 기기 내 캐시(C) 누적 및 완료 기록 반영. 같은 퀘스트 중복 보상 방지
- 알림에서 30분 미루기, 사용자 설정 시각(기본 07:00), 알림 on/off
- Room v1 -> v2 마이그레이션 (기존 `quests` 테이블 보존)
- 전원 재시작 및 시간/시간대 변경 시 다음 제안 알림 재예약
- GitHub Actions 자동 Debug APK 빌드

**아직 구현 안 됨**
- Gemini 목표 분해, 메인/챕터 구조, Supabase 로그인/동기화·랭킹
- 홈 위젯, 집중 타이머, 활동 조건(위치·걸음 수), 알림 커스텀 디자인
- 친구/지역 커뮤니티, 실제 캐시 교환 및 실시간 시즌 랭킹

## 알림 동작

1. 앱 설치 후 실행. Android 13 이상에서는 **설정 → 알림 권한 허용**.
2. 기본 오전 7시 이후 시스템이 알림을 전달하면 **오늘의 일일 퀘스트 제안**이 표시됨. **정시 보장은 안 됨**.
3. 앱에서 제안을 **수락**하면 진행 알림이 표시됨.
4. 알림에서 **완료**하면 Room에 기록되고 캐시가 한 번 지급됨.
5. **30분 후**는 알림을 숨겼다가 다시 보여주는 동작이며 완료가 아님.
6. 시스템에서 알림을 강제로 끄거나 앱을 강제 중지한 경우 전달 보장 안 됨.

**설치 직후 7시까지 기다릴 필요 없음.** 홈에 오늘의 로컬 제안을 생성해 바로 수락/완료 테스트 가능.

Android에서는 일반 예약 알림을 지연할 수 있어 `AlarmManager.setAndAllowWhileIdle` 사용. 정확 알람 권한을 요구하지 않음. 진행 알림에 `setOngoing`을 사용해도 Android 14+에서 사용자가 닫을 수 있음.

## 모바일 빌드

1. [GitHub Actions](https://github.com/jaksim0sec/questYourLife/actions/workflows/android-apk.yml) 열기
2. 가장 최근 성공한 Android Debug APK → Artifacts → `hyunsaengpatch-debug-apk`
3. ZIP 다운로드 → `app-debug.apk` 설치. Termux 불필요
4. 같은 앱에 덮어 설치하려면 **서명 키 일치**가 필요함. Actions 캐시 키는 장기 보존 보장 없음

개발: Kotlin, Jetpack Compose, Room, AlarmManager, Notification API, Gradle 8.13, GitHub Actions.

## 규칙 및 기획

- [IDEA.md](IDEA.md)
- [AGENT.md](AGENT.md)
- [AGENTS.md](AGENTS.md)
