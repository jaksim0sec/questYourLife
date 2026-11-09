# 에셋 관리 계약 · new 통합 개발 전환

> 상태: **관리 방향 확정 / 실제 경로 이관 미실행** (2026-11-09). 이 문서는 자산 경로와 소유권의 이관 기준이지, 현재 파일이 이미 이동되었거나 앱·preview 코드가 수정되었다는 증거가 아니다.
>
> 적용: `new`의 SEED/웹 preview, 향후 이 브랜치에 도입될 앱 코드, 이후 플랫폼 간 아트 재사용. 디자인 표현 강도는 [A-ART](../design/seed/assets/reward-art.md), 이미지 프레임 동작은 [C-IMAGE](../design/seed/components/image-frame.md), 아이콘 규격은 [F-ICON-MOTION](../design/seed/foundations/icons-motion.md)가 소유한다.

## 1. 기본 원칙

1. **한 자산, 한 원본.** 시연과 앱이 같은 그림을 사용하면 그 그림의 **관리 정본**은 한 곳에 둔다. 소비자별 사본이나 변환본을 정본으로 승격하지 않는다.
2. **UI 아이콘과 3D/래스터 아트는 서로 다른 계열.** `currentColor`를 쓰는 Soft SVG 56종과 금색 상자·캐시·마스코트·장비 이미지에 같은 재질·스트로크 규칙을 강제하지 않는다.
3. **소스와 배포물을 구분.** 웹에서는 원본 SVG/PNG/WebP를 직접 참조할 수 있다. Android는 필요할 때 SVG→VectorDrawable 또는 래스터→`drawable-nodpi` 등으로 연결·변환한다. 네이티브 리소스 위치는 플랫폼 규칙이므로 무조건 루트 경로를 직접 참조하라고 하지 않는다.
4. **기존 사용처 보존.** 파일 이동과 참조 수정은 같은 실행 단계에서 수행한다. 안전한 이관·검증 전까지 기존 파일 삭제 금지. 파일의 이름, ID, 포맷을 바꿨다는 이유로 기존 구매·장착·화면 연결을 변경하지 않는다.
5. **불필요한 도구·추상화 금지.** 단순한 정적 경로가 충분하면 별도 CDN, 동적 에셋 서비스, 대규모 빌드 파이프라인이나 이중 registry를 도입하지 않는다.

## 2. 제안된 관리 원본 위치 (미생성)

```text
assets/                                  # 향후 공통 원본. 현재 생성/이동하지 않음
  icons/
    soft/
      v0.14/                             # 기존 승인 SVG 56종 + manifest/QA/원본 메타
  art/
    rewards/                             # 상자·캐시 등 보상 오브젝트
    mascot/                              # 얼굴·마스코트 원본
    cosmetics/                           # 모자·장비 원본
```

- 이 트리는 **예정 경로**이며 현재 `new`의 파일 시스템이 아니다. 카테고리에 실제 등록할 에셋이 생길 때만 디렉터리를 만든다.
- `docs/design/seed/assets/`는 **디자인 설명/표현 계약**을 맡는다. 바이너리 배포용 `assets/`와 혼동하거나 같은 이미지를 중복 보관하지 않는다.
- 이름은 의미가 드러나는 `kebab-case`를 기본으로 하고 확장자는 실제 포맷을 따른다. 이미 배포된 이름이나 ID가 있다면 호환 맵 없이 임의 변경하지 않는다.
- 벡터 파일 자체의 메타는 현행 `manifest.json`/README/QA를 유지한다. 모든 이미지를 위해 새 데이터베이스나 별도 manifest를 먼저 만들지 않는다. 나중에 에셋 수가 실제로 증가할 때만 간단한 목록을 검토한다.

## 3. 현재 실물과 이관 목표

| 분류 | 현재 실제 위치 | 전환 원칙 |
|---|---|---|
| **공식 Soft SVG 56종** | `docs/design/seed/assets/icons/v0.14/` | 승인된 원본 바이트와 `manifest.json`, `QA.json`, `STYLE.md`, `README.md`를 보존해 `assets/icons/soft/v0.14/`로 **원본 정본을 이전**. D-012의 32 Outline + 5 Fill + 19 Outline 유지 |
| **웹 preview SVG 사본 18종** | `web/preview/assets/seed-icons/` | 공식 원본과 이름·SHA/도형을 비교. 일치하는 파일은 중복 저장을 끝내고 preview를 공통 원본 경로로 참조 변경. 차이가 있으면 원인을 분석한 뒤 승인되지 않은 변경을 정본에 덮어쓰지 않음 |
| **기타 preview 전용 SVG** | `web/preview/assets/clan-bookmark-fill.svg`, `index.html`/JS의 inline SVG | 기존 공식 5개 Fill에 없는 도형임을 구분. 실제 필요성이 입증된 전용 자산만 소유 경로로 분리; 동적 도형/마크업은 그 성격에 따라 코드로 유지. 자동으로 '공식 아이콘'이라 부르지 않음 |
| **기존 preview 아트** | `web/preview/assets/box*.webp`, `mascot*.png`, `cosmetic_*.png` | 기존 `main/app/src/main/res/drawable-nodpi/` 등과 바이트·사용 목적 확인 후 같은 원본은 하나로 관리. 콘텐츠/장비의 고유 ID·표시 위치는 유지 |
| **Android 앱 아이콘/이미지** | `main/app/src/main/res/drawable/`, `drawable-nodpi/` | 이미 사용 중인 VectorDrawable/래스터를 무단 제거하거나 승인된 클랜 나침반을 새 `users` SVG로 교체 금지. 구현 시 원본→네이티브 변환/리소스 매핑을 명확히 구분 |

**중요:** `new`에는 현재 앱 코드가 없음. 위 Android 경로는 비교 대상 `main`의 실제 경로이며 `new`에 있다고 가정하지 않는다.

## 4. preview의 직접 참조 이관

현재 preview는 `web/preview/clan.js`에서 `assets/seed-icons/<name>.svg`를 CSS mask로 참조하고, `web/preview/assets/` 아래 래스터를 상대경로로 참조한다. 기존 실행 명령은 `web/preview/` 자체를 웹 서버 루트로 사용한다.

**파일을 옮기기만 하면 404가 난다.** 실제 이관 시 다음을 **같은 변경으로** 처리한다.

1. 원본의 SHA/내용·기존 사용처 목록 수집. `seed-icons` 18개와 독립 `clan-bookmark-fill.svg`, `index.html`/JS의 inline SVG를 각각 분류한다.
2. 정본을 루트 `assets/`에 배치하고 preview의 `<img>`, `mask-image`, CSS/JS에서 사용하는 경로를 **실제 문서 URL 기준**으로 수정한다. 예: `web/preview/index.html`을 `/web/preview/`에서 열 경우 `../../assets/icons/soft/v0.14/gift.svg`는 `/assets/icons/soft/v0.14/gift.svg`로 해석된다. URL은 JS 소스 파일 위치가 아니라 **페이지 URL** 기준임에 주의한다.
3. 로컬 서버는 저장소 **루트**에서 서빙하고 `/web/preview/`로 접속하도록 `web/preview/README.md`와 검증 명령을 함께 수정한다. 기존 `web/preview`만 루트로 서빙하는 방식은 새 공통 경로를 노출하지 못한다. GitHub Pages의 저장소 base path도 확인한다.
4. preview의 모든 경로에서 SVG 마스크 색상(`currentColor`/CSS), Outline/Fill, PNG/WebP의 투명도·contain, 라이트/다크 렌더를 검증한다. 이전 참조가 모두 제거된 뒤에만 preview 사본을 삭제한다.

**현재 단계에서는 1~4 실행 금지.** 웹 코드는 지금의 상대경로로 정상 동작할 수 있어야 한다. 이 문서의 예정 경로를 즉시 import에 사용하지 않는다.

## 5. 새 보상 아트 등록·품질 게이트

- 이 대화에서 만든 보상 상자 PNG는 **신규 에셋 후보**다. 사용자가 승인한 최종 원본과 실제 투명 알파를 검증하기 전 자동으로 저장소 정본에 등록했다고 주장하지 않는다.
- 개별 아트는 가능한 단일 물체 파일로 등록. 스프라이트 시트에서 자른 경우 주위 다른 그림/배경 색 잔상·체커보드 무늬가 실제 픽셀에 들어가지 않도록 검증한다. 금빛 글로우는 의도한 내부 효과만 보존.
- A1 작은 진열은 투명 원본 + `contain`, A2 배너는 맥락 있는 전용 장면, A3 획득 연출은 별도 연출/상태와 결합한다. **같은 A1 그림을 확대한다고 A2가 되지 않는다.**
- 결과 검증: 파일 존재·alpha/크기·시트 분리 상태 → 모든 참조 연결 및 404 없음 → 실제 브라우저/Android 축소·라이트/다크 → 이전 기능/리소스 회귀 확인. 미확인 단계는 완료로 표시하지 않는다.

## 6. 이관 완료 조건

- [ ] 현재 정본 56개와 메타 파일의 내용·개수·바이트 보존 검증
- [ ] preview 18개 중 중복/차이 판정 및 예외 기록
- [ ] preview 독립 SVG/inline 도형 분류 및 소유권 확정
- [ ] preview 래스터와 Android 기존 리소스 간 원본 중복 확인
- [ ] 모든 웹 경로·서버 실행 방식·GitHub Pages 참조 동시 수정
- [ ] 필요한 Android VectorDrawable 변환·매핑의 동등성 확인 (Android 편입 시)
- [ ] 에셋별 실제 투명 알파, 렌더 크기, 필수 접근성/대체 설명 점검
- [ ] 사용처가 없어진 사본만 삭제하고 회귀/404 검증

이관 완료 전에는 승인된 기존 아이콘·아트·코드 및 웹 preview를 그대로 유지한다.
