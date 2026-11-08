# P0 이전 시각 베이스라인

- 소스 `new` @ `3e59cdac4594a552aaa6bf903a7935ef71eebf21`; 40개의 **실측 Chromium PNG**를 P0-5에서 생성해 이 폴더에 저장한다.
- 클랜 홈(가입/미가입), 내 클랜, 공동 보상, 이번 주 기록의 5개 상태 × 320/360/390/430 × light/dark.
- 빈 PNG·모의 이미지·기존 사진을 실제 베이스라인으로 대체하지 않는다.
- GitHub Actions `seed-r3-p0.yml`이 성공해 `manifest.json`과 PNG 40개가 커밋되기 전까지 **G-P0 미통과**.
- Android, 실기기 및 TalkBack 검증은 여기서 수행하지 않는다.
