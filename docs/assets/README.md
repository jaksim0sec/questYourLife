# 에셋 관리 계약 · new 통합 개발 전환

> 상태: **SVG 정본·preview 참조 이관 완료 / 보상 PNG 8종 등록 완료 / 기타 래스터 이관 미완료** (2026-11-09). 이 문서는 자산 경로와 소유권의 이관 기준이지, 현재 파일이 이미 이동되었거나 앱·preview 코드가 수정되었다는 증거가 아니다.
>
> 적용: `new`의 SEED/웹 preview, 향후 이 브랜치에 도입될 앱 코드, 이후 플랫폼 간 아트 재사용. 디자인 표현 강도는 [A-ART](../design/seed/assets/reward-art.md), 이미지 프레임 동작은 [C-IMAGE](../design/seed/components/image-frame.md), 아이콘 규격은 [F-ICON-MOTION](../design/seed/foundations/icons-motion.md)가 소유한다.

## 1. 기본 원칙

1. **한 자산, 한 원본.** 시연과 앱이 같은 그림을 사용하면 그 그림의 **관리 정본**은 한 곳에 둔다. 소비자별 사본이나 변환본을 정본으로 승격하지 않는다.
2. **UI 아이콘과 3D/래스터 아트는 서로 다른 계열.** `currentColor`를 쓰는 Soft SVG 56종과 금색 상자·캐시·마스코트·장비 이미지에 같은 재질·스트로크 규칙을 강제하지 않는다.
3. **소스와 배포물을 구분.** 웹에서는 원본 SVG/PNG/WebP를 직접 참조할 수 있다. Android는 필요할 때 SVG→VectorDrawable 또는 래스터→`drawable-nodpi` 등으로 연결·변환한다. 네이티브 리소스 위치는 플랫폼 규칙이므로 무조건 루트 경로를 직접 참조하라고 하지 않는다.
4. **기존 사용처 보존.** 파일 이동과 참조 수정은 같은 실행 단계에서 수행한다. 안전한 이관·검증 전까지 기존 파일 삭제 금지. 파일의 이름, ID, 포맷을 바꿨다는 이유로 기존 구매·장착·화면 연결을 변경하지 않는다.
5. **불필요한 도구·추상화 금지.** 단순한 정적 경로가 충분하면 별도 CDN, 동적 에셋 서비스, 대규모 빌드 파이프라인이나 이중 registry를 도입하지 않는다.

## 2. 공통 관리 위치 (SVG 실구현, 이미지 예정)

```text
assets/                                  # SVG 공통 원본 생성됨, 래스터는 준비 중
  icons/
    soft/
      v0.14/                             # 이전 완료: 승인 SVG 56종 + manifest/QA/원본 메타
  icons/preview/                          # 웹 시연 고유 책갈피 Fill·해·달 (별도)
  art/
    rewards/                             # 보상 PNG 8종 GitHub 정본 등록 완료
    mascot/                              # 얼굴·마스코트 원본
    cosmetics/                           # 모자·장비 원본
```

- `assets/icons/`는 현재 `new`에 존재한다. `assets/art/rewards/`는 보상 PNG 8종을 등록해 실제 생성되었다. 나머지 래스터 위치는 이관 확인 전 변경하지 않는다. 현재의 단순 디렉터리 구조를 불필요하게 확장하지 않는다.
- `docs/design/seed/assets/`는 **디자인 설명/표현 계약**을 맡는다. 바이너리 배포용 `assets/`와 혼동하거나 같은 이미지를 중복 보관하지 않는다.
- 이름은 의미가 드러나는 `kebab-case`를 기본으로 하고 확장자는 실제 포맷을 따른다. 이미 배포된 이름이나 ID가 있다면 호환 맵 없이 임의 변경하지 않는다.
- 벡터 파일 자체의 메타는 현행 `manifest.json`/README/QA를 유지한다. 모든 이미지를 위해 새 데이터베이스나 별도 manifest를 먼저 만들지 않는다. 나중에 에셋 수가 실제로 증가할 때만 간단한 목록을 검토한다.

## 3. 현재 실물과 이관 목표

| 분류 | 현재 실제 위치 | 전환 원칙 |
|---|---|---|
| **공식 Soft SVG 56종** | `assets/icons/soft/v0.14/` | 2026-11-09 기존 Git blob SHA 그대로 이전 완료. 기존 docs 경로에는 안내 README만 유지. D-012의 32 Outline + 5 Fill + 19 Outline 유지 |
| **웹 preview SVG 사본 18종** | 이전 `web/preview/assets/seed-icons/` | 56종 정본과 SHA 일치 확인 후 사본 18개 삭제 완료. `clan.js`에서 공통 SVG를 CSS mask로 직접 참조 |
| **기타 preview 전용 SVG** | `assets/icons/preview/` | 기존 책갈피 Fill을 이전하고 sun/moon을 독립 SVG로 분리. HTML inline SVG 15개를 공통 파일 참조로 교체. 동적으로 색상을 조합하는 클랜 깃발 SVG는 코드에 유지하며 공식 56종으로 승격하지 않음 |
| **기존 preview 아트** | `web/preview/assets/box*.webp`, `mascot*.png`, `cosmetic_*.png` | 기존 `main/app/src/main/res/drawable-nodpi/` 등과 바이트·사용 목적 확인 후 같은 원본은 하나로 관리. 콘텐츠/장비의 고유 ID·표시 위치는 유지 |
| **Android 앱 아이콘/이미지** | `main/app/src/main/res/drawable/`, `drawable-nodpi/` | 이미 사용 중인 VectorDrawable/래스터를 무단 제거하거나 승인된 클랜 나침반을 새 `users` SVG로 교체 금지. 구현 시 원본→네이티브 변환/리소스 매핑을 명확히 구분 |

**중요:** `new`에는 현재 앱 코드가 없음. 위 Android 경로는 비교 대상 `main`의 실제 경로이며 `new`에 있다고 가정하지 않는다.

## 4. preview SVG 이관 현황 (2026-11-09)

- 적용 커밋: `c0d360adb025bf4bb5156e307fd908a00d87a680`
- 정본 `assets/icons/soft/v0.14/`: 기존 56개 원본 Git blob 재사용, manifest/QA/STYLE 함께 보존.
- preview 18개 완전 중복 SVG는 삭제하고, `clan.js`의 CSS mask URL을 `../../assets/icons/soft/v0.14/<name>.svg`로 변경.
- 독립 `clan-bookmark-fill.svg`, 메뉴의 `sun.svg`와 `moon.svg`는 `assets/icons/preview/`에 둔다. 공식 56종의 Fill 목록은 변경하지 않는다.
- `index.html` inline SVG 15개를 참조 기반 마스크로 치환. `app.js`의 하단 네비 아이콘 정의도 실제 SVG 파일 선택으로 변경. 커스텀 색으로 렌더하는 `clan.js`의 동적 깃발 도형은 코드에 유지.
- **새 preview 실행 방식:** 저장소 루트를 정적 서버로 띄우고 `/web/preview/#home` 접근. `web/preview/`만 별도 서버 루트로 삼는 옛 방식은 공통 `/assets/`가 노출되지 않아 더 이상 지원하지 않는다.
- 상대 URL은 HTML 페이지 URL 기준으로 해석된다. GitHub Pages의 저장소 base path가 있는 경우도 `../../assets/`로 동일 저장소 내부를 참조한다.
- **실행 검증 경계:** Git blob/코드 참조의 정적 검증과 실제 브라우저·Pages 시각/404 검증은 구분한다. 브라우저 결과가 확인되지 않은 경우 완료라 하지 않는다.
- **미완료:** preview 래스터 원본 중복 정리·Android VectorDrawable 변환 및 보상 PNG 8종의 실제 브라우저 404/디코드 검증. PNG 바이너리 8종 업로드는 완료.

## 5. 새 보상 아트 등록·품질 게이트

- 보상 이미지 PNG 8개가 **2026-11-10 `feat/seed-layout-composition-20261110` 브랜치의 `assets/art/rewards/`에 등록되었다.** GitHub 디렉터리와 최신 커밋 `40cfbd33678ffff208c55ceccc7f86c7b2ae853b`에서 8개 파일을 확인했다. 기존 ZIP 생성 이력과 실제 업로드 커밋을 구분한다. 파일의 이름·내용·비율은 변경하지 않는다.
- 개별 아트는 가능한 단일 물체 파일로 등록. 스프라이트 시트에서 자른 경우 주위 다른 그림/배경 색 잔상·체커보드 무늬가 실제 픽셀에 들어가지 않도록 검증한다. 금빛 글로우는 의도한 내부 효과만 보존.
- A1 작은 진열은 투명 원본 + `contain`, A2 배너는 맥락 있는 전용 장면, A3 획득 연출은 별도 연출/상태와 결합한다. **같은 A1 그림을 확대한다고 A2가 되지 않는다.**
- 결과 검증: 파일 존재·alpha/크기·시트 분리 상태 → 모든 참조 연결 및 404 없음 → 실제 브라우저/Android 축소·라이트/다크 → 이전 기능/리소스 회귀 확인. 미확인 단계는 완료로 표시하지 않는다.

## 6. 이관 완료 조건

- [x] 현재 정본 56개와 메타 파일의 내용·개수·바이트 보존 검증
- [x] preview 18개 중 중복/차이 판정 및 예외 기록
- [x] preview 독립 SVG/inline 도형 분류 및 소유권 확정
- [ ] preview 래스터와 Android 기존 리소스 간 원본 중복 확인
- [x] 모든 SVG 웹 경로·서버 실행 방식·GitHub Pages 상대경로 계약 수정 (실제 브라우저 렌더 검증 별도)
- [ ] 필요한 Android VectorDrawable 변환·매핑의 동등성 확인 (Android 편입 시)
- [ ] 에셋별 실제 투명 알파, 렌더 크기, 필수 접근성/대체 설명 점검
- [ ] 사용처가 없어진 사본만 삭제하고 회귀/404 검증

SVG 경로 전환과 보상 PNG 8종 등록은 완료했다. 기존 preview 래스터·Android 자산은 충분한 검증과 변경 승인 전까지 기존 경로를 보존한다.

## 2026-11-10 · D-015 웹 하단 Navigation 추가 SVG 채택

- 기존 `assets/icons/soft/v0.14/` 공식 56종과 `manifest.json`은 변경하지 않는다.
- 사용자가 웹 시연에서 승인한 **클랜 활성** `assets/icons/preview/clan-bookmark-fill.svg`, **마이 비활성/활성** `assets/icons/preview/me-smile.svg` 및 `me-smile-fill.svg`는 현재 위치 그대로 **웹 Navigation 추가 정본**으로 취급한다. `preview`라는 디렉터리명은 기존 경로이며 승인되지 않은 임시 시안이라는 뜻이 아니다.
- `web/preview/seed-page-contract.mjs`의 `WEB_NAV_ITEMS`가 Lab의 마이/클랜 표시 파일을 선택하고, `web/preview/app.js`도 같은 원본 경로를 참조한다. 별도 파일 복제·불필요한 manifest 신설 없이 사용처를 유지한다.
- 기존 Android `main`의 클랜 나침반, 공식 Soft 56 원본, 보상·마스코트와 인앱 자산 ID는 그대로. 실제 Native 변환/사용은 별도 승인 작업이다.

### 알림 Bell 연결형 웹 원본 (2026-11-10)

`assets/icons/preview/bell-connected.svg`를 헤더 알림 아이콘의 웹 정본으로 추가했다. 공식 Soft 56종 `bell.svg`는 수정하지 않았고, `web/preview/index.html`과 `web/seed-lab/seed-visual.mjs`가 새 파일을 참조한다. 종 본체의 밑선과 하단 추 곡선의 양 끝을 연결했으며 도형·색·터치 슬롯 규격은 기존 헤더와 같다.

## 2026-11-10 · main 마스코트·임시 꾸미기 아트 공통 정본 등록

- 등록 브랜치 `feat/seed-layout-composition-20261110`, Git 객체 복사 커밋 `8b72124b76ab9d8783223784206bb2059c3f14cb`. 원본 `main/app/src/main/res/drawable-nodpi/`에서 **바이너리를 디코딩하거나 재압축하지 않고 동일 Git blob SHA를 재사용**했다.
- 마스코트 얼굴 1개: `assets/art/mascot/mascot_face_stamp.png` (SHA `96c85bd1c12b5aba5795a19daec969970b7ec68d`).
- 임시 장비 PNG 9개: `assets/art/cosmetics/` 아래 `cosmetic_beanie`, `cosmetic_cream_cap`, `cosmetic_blue_cap`, `cosmetic_charcoal_cap`, `cosmetic_backward_cap`, `cosmetic_bucket_hat`, `cosmetic_cream_headphones`, `cosmetic_black_headphones`, `cosmetic_white_airpods_pair` (모두 `.png`).
- 기존 `web/preview/assets/mascot_face_stamp.png`와 일부 `cosmetic_*.png`는 **레거시 참조 호환을 위해 유지**한다. 웹용 새로운 조합은 `web/preview/seed-mascot-avatar.mjs`에서 공통 `../../assets/art/` 경로만 사용한다. 뒤늦은 중복 제거는 기존 사용처 전환·실제 렌더 검증 후 별개 작업이다.
- 데이터상 `1002` 라임 별은 현재 원본 PNG가 없으므로 가짜 아트 파일이나 임의 대체 이미지를 추가하지 않는다. 상품명·아이템 ID·장착 동작은 기존 `main`의 `CosmeticCatalog.kt` / `CosmeticArt.kt`와 구분해 보존한다.
- 공통 C-AVATAR의 좌표/zIndex/용도는 `docs/design/seed/components/avatar.md`. 원본 이미지에 별도 크롭·리사이즈·품질 보정은 적용하지 않았다.
