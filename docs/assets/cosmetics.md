# 임시 꾸미기 원본과 적용 규칙

사용자 제공 `hyunsaengpatch_realistic_accessories_9.zip`의 투명 PNG 9개를 변형 없이 `app/src/main/res/drawable-nodpi/cosmetic_*.png`에 등록했다. 번호 접두사만 제거했다. 중복 원본/ZIP은 저장소에 넣지 않는다. 기존 도장 얼굴 원본은 유지한다.

- 상품 ID/이름/가격: `cosmetics/CosmeticCatalog.kt`. 기존 ID 0..5와 가격을 유지하고 신규 헤드폰/이어버드는 1003/1004를 사용한다.
- 이미지 연결/시연 착장: `ui/CosmeticArt.kt`의 `CosmeticArtwork`. UI마다 리소스 ID를 복사하지 않는다.
- 상품 그림: `CosmeticImage`. 얼굴과 합성: `DressedAvatar`. 동일 좌표계를 크기에 비례해 사용한다. 마이/꾸미기는 실제 저장된 착장, 클랜/DM 친구는 이름별 고정 시연 착장이다.
- 제공되지 않은 라임 별(1002)과 도장 그림은 계속 빈칸이다. 기존 구매는 보존한다.
- 사용하지 않는 구형 DecorPanel, Canvas 모자 그림, 중복 가격표와 구매 API를 제거했다. 재화 처리는 검증된 chooseCosmetic/Room 트랜잭션 한 경로로 유지한다.

확인 기준: 9개 원본 바이트 동일, 전체 리소스 연결, 마이/꾸미기 착장 ID 일치, 친구별 안정적인 임의 착장, 기존 구매 정책 테스트 유지. 실제 기기에서의 위치/겹침/터치와 재실행은 사용자 확인 대상이며 에뮬레이터는 실행하지 않는다.

최종 검증: assembleDebug/testDebugUnitTest 성공, 단위 테스트 12개 실패·오류·건너뜀 0개, APK 서명 검사와 diff 공백 검사 성공. 별도 소스 검토에서 중요 결함 없음. 최종 로그는 개발 환경 `/workspace/accessories-final.log`. 기기 실행은 하지 않았다.
