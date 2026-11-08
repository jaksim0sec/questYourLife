# SEED r3 규칙·수치 원문 LEDGER

## 현행 계약

- 이 파일은 [SEED r3 실행 계획]에 따른 **입력 추적 장부**이며, 자체로 승인되지 않은 시각 수치를 승인하지 않는다.
- 원문 출처: 사용자 제공 `Hyunsaengpatch_SEED_integrated_final_report_2026-11-08.md` (2026-11-08, 통합본, 1130줄).
- 모든 `원문 문장(인용)` 셀은 원본 줄을 **그대로 인용**한다. `\|`은 Markdown 테이블 이스케이프만을 위한 표시다.
- 한 줄에 숫자가 여러 개 있으면 각 수치를 별도 행에 기재하되 해당 행에는 원문 줄을 반복 보존한다.
- 측정 조건은 원문에 표시된 390/17, 355/16.5, 355/16을 섞지 않는다. 수치가 없는 절은 '미명시'로 기록한다.
- 분류 `검토/반례/보류`와 상태 `proposed/candidate/history`는 P1/P2/P3/P4 판정 이전의 입력 상태다. 정본에 반영됐다는 뜻이 아니다.
- 원문에 나온 과거 r1 및 v0.x 규칙의 승인 여부는 반드시 D-011/D-012 및 이 실행 계획의 게이트와 대조한다.
- 계획서 3장 L01–L38은 최초 **핵심 항목 예시**이며, 아래 전수 장부는 원문을 더 잘게 추적하는 확장이다. 번호 재매핑은 기계적으로 대응할 수 없다.

## 절별 커버리지

| 원문 절 | 행 수 | 상태 |
|---|---:|---|
| 3.3 | 24 | 충족 |
| 3.4 | 6 | 충족 |
| 4.1 | 18 | 충족 |
| 4.2 | 9 | 충족 |
| 4.3 | 63 | 충족 |
| 4.4 | 14 | 충족 |
| 4.5 | 36 | 충족 |
| 5.1 | 6 | 충족 |
| 5.2 | 8 | 충족 |
| 5.3 | 7 | 충족 |
| 6.1 | 10 | 충족 |
| 6.5 | 8 | 충족 |
| 6.6 | 15 | 충족 |
| 7.1 | 18 | 충족 |
| 7.2 | 9 | 충족 |
| 7.3 | 6 | 충족 |
| 7.4 | 3 | 충족 |
| 7.5 | 15 | 충족 |
| 7.6 | 4 | 충족 |
| 7.7 | 7 | 충족 |
| 7.8 | 3 | 충족 |
| 7.9 | 8 | 충족 |
| 7.10 | 7 | 충족 |
| 7.11 | 3 | 충족 |
| 7.12 | 10 | 충족 |
| 7.13 | 7 | 충족 |
| 7.14 | 3 | 충족 |
| 7.15 | 3 | 충족 |
| 7.16 | 7 | 충족 |
| 7.17 | 2 | 충족 |
| 7.18 | 3 | 충족 |
| 7.19 | 11 | 충족 |
| 7.20 | 7 | 충족 |
| 7.21 | 9 | 충족 |
| 8.2 | 8 | 충족 |
| 8.3 | 8 | 충족 |
| 8.4 | 6 | 충족 |
| 8.5 | 9 | 충족 |
| 8.6 | 12 | 충족 |
| 9.3 | 14 | 충족 |
| 9.5 | 8 | 충족 |
| 10.2 | 22 | 충족 |
| 10.3 | 10 | 충족 |
| A.1 | 35 | 충족 |
| A.2 | 40 | 충족 |
| A.3 | 18 | 충족 |
| A.4 | 26 | 충족 |

**합계:** 585행 / 47절; 47개 지정 절 모두 1행 이상.

## 수치·반례·판단 원문 전체

| ID | 원문 절 | 원문 문장(인용) | 측정 조건 | 종류 | 분류 | 소유 정본#섹션 | 처리 단계 | 상태 |
|---|---|---|---|---|---|---|---|---|
| L0001 | 3.3 | 근거 위치는 `web/preview/clan.js`의 `rewardPanel()`과 `web/preview/clan.css`의 `.clan-reward-*`다. 아래 값은 **CSS viewport 390px / root 17px 가정**의 환산값이며 실기기 측정이 아니다. [S4] | 390/17 | 측정/수치 390px | 검토 | objects/progress.md#현행 계약 | P1 | candidate |
| L0002 | 3.3 | 근거 위치는 `web/preview/clan.js`의 `rewardPanel()`과 `web/preview/clan.css`의 `.clan-reward-*`다. 아래 값은 **CSS viewport 390px / root 17px 가정**의 환산값이며 실기기 측정이 아니다. [S4] | 390/17 | 측정/수치 17px | 검토 | objects/progress.md#현행 계약 | P1 | candidate |
| L0003 | 3.3 | \| 요소 \| 당시 보고된 값·구조 \| 문제 \| | 390/17 | 비교표 원문 | 검토 | objects/progress.md#현행 계약 | P1 | history |
| L0004 | 3.3 | \| `rewardPanel()` \| 홍보·진행·탐색 동시 구성 \| 정보 책임 혼합 \| | 390/17 | 비교표 원문 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0005 | 3.3 | \| `.clan-reward-hero` \| 커스텀 갈색 그라데이션 \| 전용 이벤트 장면 대신 표면 효과로 특별함 대체 \| | 390/17 | 비교표 원문 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0006 | 3.3 | \| `.clan-chest-frame` \| Hero 폭 약 57%, `pointer-events:none` \| 작은 정면 에셋 확대, 큰 이미지 비클릭 \| | 390/17 | 측정/수치 57% | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0007 | 3.3 | \| `.clan-reward-hero-copy` \| 폭 약 52% \| 이미지와 텍스트가 겹치며 경쟁 \| | 390/17 | 측정/수치 52% | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0008 | 3.3 | \| Hero `h2` \| 약 22.4px / 810 \| 홍보 카피가 진행값보다 강함 \| | 390/17 | 측정/수치 22.4px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0009 | 3.3 | \| Hero `h2` \| 약 22.4px / 810 \| 홍보 카피가 진행값보다 강함 \| | 390/17 | 측정/수치 810 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0010 | 3.3 | \| Hero 설명 `p` \| 약 11.1px \| 필요한 설명은 지나치게 작음 \| | 390/17 | 측정/수치 11.1px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0011 | 3.3 | \| `.clan-reward-cta` \| 약 11.6px / 710 \| 실제 이동이 작은 부차 문구처럼 보임 \| | 390/17 | 측정/수치 11.6px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0012 | 3.3 | \| `.clan-reward-cta` \| 약 11.6px / 710 \| 실제 이동이 작은 부차 문구처럼 보임 \| | 390/17 | 측정/수치 710 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0013 | 3.3 | \| 기여 Badge \| 약 9.7px / 790 \| 작은 글자를 굵기로 보상하는 과밀 표현 \| | 390/17 | 측정/수치 9.7px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0014 | 3.3 | \| 기여 Badge \| 약 9.7px / 790 \| 작은 글자를 굵기로 보상하는 과밀 표현 \| | 390/17 | 측정/수치 790 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0015 | 3.3 | \| Progress 수치 \| 약 13.9px \| `620 / 1,000`이 보조 정보처럼 보임 \| | 390/17 | 측정/수치 13.9px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0016 | 3.3 | \| Progress 수치 \| 약 13.9px \| `620 / 1,000`이 보조 정보처럼 보임 \| | 390/17 | 측정/수치 620 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0017 | 3.3 | \| Progress 수치 \| 약 13.9px \| `620 / 1,000`이 보조 정보처럼 보임 \| | 390/17 | 측정/수치 1,000 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0018 | 3.3 | \| Progress Track \| 약 8px \| v0.15 기본 10px과 불일치 \| | 390/17 | 측정/수치 8px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0019 | 3.3 | \| Progress Track \| 약 8px \| v0.15 기본 10px과 불일치 \| | 390/17 | 측정/수치 15 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0020 | 3.3 | \| Progress Track \| 약 8px \| v0.15 기본 10px과 불일치 \| | 390/17 | 측정/수치 10px | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0021 | 3.3 | \| Progress 위치 \| Hero 내부 \| 관리 데이터가 홍보에 종속 \| | 390/17 | 비교표 원문 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0022 | 3.3 | \| `보상 구성 보기`·`미리보기` \| 모두 `#clan-reward` \| 같은 목적지의 중복 진입 \| | 390/17 | 비교표 원문 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0023 | 3.3 | \| 꾸미기 아이템 3개 \| 정적 `span` \| 진열이 유도하는 선택·상세 기대와 실제 행동 불일치 가능 \| | 390/17 | 측정/수치 3개 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0024 | 3.3 | 가장 크게 보이는 상자는 작동하지 않고 작은 링크 두 개가 같은 곳으로 이동한다. 모든 이미지를 버튼으로 바꾸라는 뜻은 아니다. **시각적 강조·클릭 기대·실제 행동이 대응해야 한다**는 지적이다. | 390/17 | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P1 | proposed |
| L0025 | 3.4 | - `이번 주 6명 참여`와 `6명 기여 중`은 같은 인원 정보를 다른 형태로 반복했다 | 390/17 | 측정/수치 6명 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0026 | 3.4 | - `이번 주 6명 참여`와 `6명 기여 중`은 같은 인원 정보를 다른 형태로 반복했다 | 390/17 | 측정/수치 6명 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0027 | 3.4 | - `보상 구성 보기`와 바로 아래 3개 아이템 진열은 보상 노출을 반복했다 | 390/17 | 측정/수치 3개 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0028 | 3.4 | - 동일한 `#clan-reward` 링크가 추가로 반복되어 행동 위계가 약해졌다 | 390/17 | 문장/사례 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0029 | 3.4 | - 큰 Hero 아래 비슷한 크기의 카드 3개가 이어져 리듬은 단조롭고 보상 면적은 커졌다 | 390/17 | 측정/수치 3개 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0030 | 3.4 | 데이터 의미가 실제로 다르다면 구별해 표시할 수 있다. 문제는 차이를 정의하지 않은 채 표현만 늘리는 것이다. | 390/17 | 문장/사례 | 검토 | objects/progress.md#Counterexamples | P1 | proposed |
| L0031 | 4.1 | S3는 709×1536px JPEG 2장을 비교해 두 화면의 기본 배경에서 모두 `#16171B`가 관찰됐다고 기록한다. 아래는 JPEG 대표 픽셀의 WCAG 상대 휘도 대비이며 **공식 토큰·접근성 합격점·디자인 품질 점수가 아니다.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 709 | 검토 | foundations/typography.md#현행 계약 | P1 | history |
| L0032 | 4.1 | S3는 709×1536px JPEG 2장을 비교해 두 화면의 기본 배경에서 모두 `#16171B`가 관찰됐다고 기록한다. 아래는 JPEG 대표 픽셀의 WCAG 상대 휘도 대비이며 **공식 토큰·접근성 합격점·디자인 품질 점수가 아니다.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1536px | 검토 | foundations/typography.md#현행 계약 | P1 | history |
| L0033 | 4.1 | S3는 709×1536px JPEG 2장을 비교해 두 화면의 기본 배경에서 모두 `#16171B`가 관찰됐다고 기록한다. 아래는 JPEG 대표 픽셀의 WCAG 상대 휘도 대비이며 **공식 토큰·접근성 합격점·디자인 품질 점수가 아니다.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/typography.md#현행 계약 | P1 | history |
| L0034 | 4.1 | S3는 709×1536px JPEG 2장을 비교해 두 화면의 기본 배경에서 모두 `#16171B`가 관찰됐다고 기록한다. 아래는 JPEG 대표 픽셀의 WCAG 상대 휘도 대비이며 **공식 토큰·접근성 합격점·디자인 품질 점수가 아니다.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 16171 | 검토 | foundations/typography.md#현행 계약 | P1 | history |
| L0035 | 4.1 | \| 영역 \| 대표 색 \| 공통 바탕 대비 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0036 | 4.1 | \| 두 화면 기본 배경 \| `#16171B` \| 1.00:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 16171 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0037 | 4.1 | \| 두 화면 기본 배경 \| `#16171B` \| 1.00:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.00:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0038 | 4.1 | \| 당근 비선택 필터 \| `#2B2E35` \| 1.32:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0039 | 4.1 | \| 당근 비선택 필터 \| `#2B2E35` \| 1.32:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.32:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0040 | 4.1 | \| 현생패치 아이템 타일 \| `#2A292E` \| 1.23:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0041 | 4.1 | \| 현생패치 아이템 타일 \| `#2A292E` \| 1.23:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.23:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0042 | 4.1 | \| 현생패치 하단바 \| `#242329` \| 1.15:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 242329 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0043 | 4.1 | \| 현생패치 하단바 \| `#242329` \| 1.15:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.15:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0044 | 4.1 | \| 당근 하단바 \| `#1D2025` \| 1.09:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0045 | 4.1 | \| 당근 하단바 \| `#1D2025` \| 1.09:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.09:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0046 | 4.1 | \| 현생패치 보상 배너 어두운 부분 \| 약 `#3A322F` \| 1.43:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0047 | 4.1 | \| 현생패치 보상 배너 어두운 부분 \| 약 `#3A322F` \| 1.43:1 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.43:1 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0048 | 4.1 | 당근은 상품 목록 배경을 공통 바탕으로 유지하고 필터만 약하게 띄웠다. 실제 상품 사진·제목·가격에 대비가 집중된다. 현생패치는 갈색 배너와 개별 회색 타일의 **면 자체에 강조가 분산**됐다. 배경을 더 어둡게 바꾸는 것만으로는 콘텐츠 역할이 선명해지지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0049 | 4.2 | \| 대상 \| 당시 측정·환산 \| 해석 \| | 355/16.5 | 비교표 원문 | 검토 | foundations/typography.md#현행 계약 | P1 | history |
| L0050 | 4.2 | \| 현생패치 Reward Hero \| 약 641×614 JPEG px → 321×307 CSS px \| 모바일에서 거의 정사각형인 단일 보상 구역 \| | 355/16.5 | 측정/수치 641 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0051 | 4.2 | \| 현생패치 Reward Hero \| 약 641×614 JPEG px → 321×307 CSS px \| 모바일에서 거의 정사각형인 단일 보상 구역 \| | 355/16.5 | 측정/수치 614 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0052 | 4.2 | \| 현생패치 Reward Hero \| 약 641×614 JPEG px → 321×307 CSS px \| 모바일에서 거의 정사각형인 단일 보상 구역 \| | 355/16.5 | 측정/수치 321 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0053 | 4.2 | \| 현생패치 Reward Hero \| 약 641×614 JPEG px → 321×307 CSS px \| 모바일에서 거의 정사각형인 단일 보상 구역 \| | 355/16.5 | 측정/수치 307 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0054 | 4.2 | \| 당근 상품 이미지 \| 약 127×127 CSS px \| 이미지·제목·메타·가격을 한 행으로 묶어 여러 독립 콘텐츠 노출 \| | 355/16.5 | 측정/수치 127 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0055 | 4.2 | \| 당근 상품 이미지 \| 약 127×127 CSS px \| 이미지·제목·메타·가격을 한 행으로 묶어 여러 독립 콘텐츠 노출 \| | 355/16.5 | 측정/수치 127 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0056 | 4.2 | 큰 Hero 자체가 결함은 아니다. **점유 면적에 비해 정보·행동·가치가 부족하고 클랜 활동을 아래로 밀어내는 것**이 문제다. 원문이 ‘30% 이상’, ‘거의 절반’, ‘50%’로 표현한 비중은 측정 범위와 설명 맥락이 달라 하나의 정밀 실측값으로 합치지 않는다. | 355/16.5 | 측정/수치 30% | 반례 | foundations/typography.md#현행 계약 | P1 | history |
| L0057 | 4.2 | 큰 Hero 자체가 결함은 아니다. **점유 면적에 비해 정보·행동·가치가 부족하고 클랜 활동을 아래로 밀어내는 것**이 문제다. 원문이 ‘30% 이상’, ‘거의 절반’, ‘50%’로 표현한 비중은 측정 범위와 설명 맥락이 달라 하나의 정밀 실측값으로 합치지 않는다. | 355/16.5 | 측정/수치 50% | 반례 | foundations/typography.md#현행 계약 | P1 | history |
| L0058 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 709px | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0059 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 355px | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0060 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 16.5px | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0061 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 3.3 | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0062 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 390px | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0063 | 4.3 | 다음은 스크린샷 폭 709px을 **CSS 약 355px / root 약 16.5px**로 환산한 S3의 진단·목표값이다. 3.3절의 390px/root 17px 값과 구별한다. 목표값은 미승인 후보다. | 390/17 | 측정/수치 17px | 보류 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0064 | 4.3 | \| 요소 \| 당시 관찰 \| 개선 목표 후보 \| | 355/16.5 | 비교표 원문 | 검토 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0065 | 4.3 | \| Root Header \| `1.3rem`, 850 \| 22px / 750 전후 \| | 355/16.5 | 측정/수치 1.3rem | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0066 | 4.3 | \| Root Header \| `1.3rem`, 850 \| 22px / 750 전후 \| | 355/16.5 | 측정/수치 850 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0067 | 4.3 | \| Root Header \| `1.3rem`, 850 \| 22px / 750 전후 \| | 355/16.5 | 측정/수치 22px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0068 | 4.3 | \| Root Header \| `1.3rem`, 850 \| 22px / 750 전후 \| | 355/16.5 | 측정/수치 750 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0069 | 4.3 | \| 클랜 프로필 깃발 \| `3.15rem` \| 깃발 40–44px, 행 68–76px \| | 355/16.5 | 측정/수치 3.15rem | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0070 | 4.3 | \| 클랜 프로필 깃발 \| `3.15rem` \| 깃발 40–44px, 행 68–76px \| | 355/16.5 | 측정/수치 40 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0071 | 4.3 | \| 클랜 프로필 깃발 \| `3.15rem` \| 깃발 40–44px, 행 68–76px \| | 355/16.5 | 측정/수치 44px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0072 | 4.3 | \| 클랜 프로필 깃발 \| `3.15rem` \| 깃발 40–44px, 행 68–76px \| | 355/16.5 | 측정/수치 68 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0073 | 4.3 | \| 클랜 프로필 깃발 \| `3.15rem` \| 깃발 40–44px, 행 68–76px \| | 355/16.5 | 측정/수치 76px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0074 | 4.3 | \| Reward Hero \| 321×307px \| 321×200–220px \| | 355/16.5 | 측정/수치 321 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0075 | 4.3 | \| Reward Hero \| 321×307px \| 321×200–220px \| | 355/16.5 | 측정/수치 307px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0076 | 4.3 | \| Reward Hero \| 321×307px \| 321×200–220px \| | 355/16.5 | 측정/수치 321 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0077 | 4.3 | \| Reward Hero \| 321×307px \| 321×200–220px \| | 355/16.5 | 측정/수치 200 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0078 | 4.3 | \| Reward Hero \| 321×307px \| 321×200–220px \| | 355/16.5 | 측정/수치 220px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0079 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 21.8px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0080 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 810 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0081 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 22 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0082 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 24px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0083 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 700 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0084 | 4.3 | \| Hero 제목 \| 약 21.8px / 810 \| 22–24px / 700–750 \| | 355/16.5 | 측정/수치 750 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0085 | 4.3 | \| Hero 설명 \| 약 10.7px \| 12–13px / 400 \| | 355/16.5 | 측정/수치 10.7px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0086 | 4.3 | \| Hero 설명 \| 약 10.7px \| 12–13px / 400 \| | 355/16.5 | 측정/수치 12 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0087 | 4.3 | \| Hero 설명 \| 약 10.7px \| 12–13px / 400 \| | 355/16.5 | 측정/수치 13px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0088 | 4.3 | \| Hero 설명 \| 약 10.7px \| 12–13px / 400 \| | 355/16.5 | 측정/수치 400 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0089 | 4.3 | \| 기여 Badge \| 약 9.4px / 790 \| 12px / 600 \| | 355/16.5 | 측정/수치 9.4px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0090 | 4.3 | \| 기여 Badge \| 약 9.4px / 790 \| 12px / 600 \| | 355/16.5 | 측정/수치 790 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0091 | 4.3 | \| 기여 Badge \| 약 9.4px / 790 \| 12px / 600 \| | 355/16.5 | 측정/수치 12px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0092 | 4.3 | \| 기여 Badge \| 약 9.4px / 790 \| 12px / 600 \| | 355/16.5 | 측정/수치 600 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0093 | 4.3 | \| 꾸미기 제목 \| 약 13.7px / 760 \| 15–16px / 700 \| | 355/16.5 | 측정/수치 13.7px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0094 | 4.3 | \| 꾸미기 제목 \| 약 13.7px / 760 \| 15–16px / 700 \| | 355/16.5 | 측정/수치 760 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0095 | 4.3 | \| 꾸미기 제목 \| 약 13.7px / 760 \| 15–16px / 700 \| | 355/16.5 | 측정/수치 15 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0096 | 4.3 | \| 꾸미기 제목 \| 약 13.7px / 760 \| 15–16px / 700 \| | 355/16.5 | 측정/수치 16px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0097 | 4.3 | \| 꾸미기 제목 \| 약 13.7px / 760 \| 15–16px / 700 \| | 355/16.5 | 측정/수치 700 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0098 | 4.3 | \| 아이템 라벨 \| 약 9.4px \| 최소 12px \| | 355/16.5 | 측정/수치 9.4px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0099 | 4.3 | \| 아이템 라벨 \| 약 9.4px \| 최소 12px \| | 355/16.5 | 측정/수치 12px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0100 | 4.3 | \| 일반 정보·메타 \| 주요·보조의 비중 불균형 \| 주요 16–24px, 메타 12–13px \| | 355/16.5 | 측정/수치 16 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0101 | 4.3 | \| 일반 정보·메타 \| 주요·보조의 비중 불균형 \| 주요 16–24px, 메타 12–13px \| | 355/16.5 | 측정/수치 24px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0102 | 4.3 | \| 일반 정보·메타 \| 주요·보조의 비중 불균형 \| 주요 16–24px, 메타 12–13px \| | 355/16.5 | 측정/수치 12 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0103 | 4.3 | \| 일반 정보·메타 \| 주요·보조의 비중 불균형 \| 주요 16–24px, 메타 12–13px \| | 355/16.5 | 측정/수치 13px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0104 | 4.3 | \| 클랜 활동 행 \| 반복 행 표현 보강 필요 \| 아바타 40–44px, 행 76–92px, 구분선 1px \| | 355/16.5 | 측정/수치 40 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0105 | 4.3 | \| 클랜 활동 행 \| 반복 행 표현 보강 필요 \| 아바타 40–44px, 행 76–92px, 구분선 1px \| | 355/16.5 | 측정/수치 44px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0106 | 4.3 | \| 클랜 활동 행 \| 반복 행 표현 보강 필요 \| 아바타 40–44px, 행 76–92px, 구분선 1px \| | 355/16.5 | 측정/수치 76 | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0107 | 4.3 | \| 클랜 활동 행 \| 반복 행 표현 보강 필요 \| 아바타 40–44px, 행 76–92px, 구분선 1px \| | 355/16.5 | 측정/수치 92px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0108 | 4.3 | \| 클랜 활동 행 \| 반복 행 표현 보강 필요 \| 아바타 40–44px, 행 76–92px, 구분선 1px \| | 355/16.5 | 측정/수치 1px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0109 | 4.3 | \| 헤더 아이콘 \| 개별 위치 이동 \| Glyph 24px, 공통 슬롯·중심축 \| | 355/16.5 | 측정/수치 24px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0110 | 4.3 | 작은 글자를 과한 굵기로 보상하는 경향이 반복된다. `.57rem`이 최종적으로 10px 미만이 되는지 확인해야 하며, 토큰을 참조했다는 이유만으로 가독성을 통과시켜서는 안 된다. 메타는 조용하게, 가격·핵심 수치는 필요에 따라 강조한다. | 355/16.5 | 측정/수치 57rem | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0111 | 4.3 | 작은 글자를 과한 굵기로 보상하는 경향이 반복된다. `.57rem`이 최종적으로 10px 미만이 되는지 확인해야 하며, 토큰을 참조했다는 이유만으로 가독성을 통과시켜서는 안 된다. 메타는 조용하게, 가격·핵심 수치는 필요에 따라 강조한다. | 355/16.5 | 측정/수치 10px | 검토 | foundations/typography.md#현행 계약 | P1 | proposed |
| L0112 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 44 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0113 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 48px | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0114 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 4 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0115 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 8px | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0116 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 48dp | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0117 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 44 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0118 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 16171 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0119 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 242329 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0120 | 4.3 | S3의 터치 영역 제안은 44–48px, 슬롯 간격은 4–8px다. **현생패치의 기존 최소 48dp 계약을 44로 낮추는 승인으로 해석하지 않는다.** `#16171B` 유지·추가 암전 금지와 비강조 Surface의 `#242329`, 선택 필터의 `#2B2E35` 제안 역시 해당 비교 화면의 후보이며 `previewShellLayers`나 승인 다크 토큰을 대체하지 않는다. | 355/16.5 | 측정/수치 2 | 반례 | foundations/typography.md#현행 계약 | P1 | candidate |
| L0121 | 4.4 | 데이터는 `620 / 1,000`인데 화면 막대는 전부 회색이었다는 분석이다. S3는 JS가 내부 `span`에 `width:62%`를 지정하지만 CSS가 인라인 `span`을 폭 적용 가능한 요소로 만들지 않은 것을 원인으로 제시했다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 620 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0122 | 4.4 | 데이터는 `620 / 1,000`인데 화면 막대는 전부 회색이었다는 분석이다. S3는 JS가 내부 `span`에 `width:62%`를 지정하지만 CSS가 인라인 `span`을 폭 적용 가능한 요소로 만들지 않은 것을 원인으로 제시했다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1,000 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0123 | 4.4 | 데이터는 `620 / 1,000`인데 화면 막대는 전부 회색이었다는 분석이다. S3는 JS가 내부 `span`에 `width:62%`를 지정하지만 CSS가 인라인 `span`을 폭 적용 가능한 요소로 만들지 않은 것을 원인으로 제시했다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 62% | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0124 | 4.4 | 아래 코드는 **원문에 제시된 수정 후보**이며 이 보고서에서 적용·런타임 검증하지 않았다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | candidate |
| L0125 | 4.4 | .clan-progress-track { | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0126 | 4.4 |   overflow: hidden; | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0127 | 4.4 |   border-radius: 999px; | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 999px | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0128 | 4.4 | } | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0129 | 4.4 | .clan-progress-track > span { | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0130 | 4.4 |   display: block; | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0131 | 4.4 |   height: 100%; | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 100% | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0132 | 4.4 |   border-radius: inherit; | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0133 | 4.4 | } | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/progress.md#현행 계약 | P0 | proposed |
| L0134 | 4.4 | 실제 수정 전 현재 DOM·셀렉터를 확인하고 부모 높이·배경·채움색·CSS 충돌까지 검증해야 한다. 원인 진단이 맞다면 이는 장식 결함이 아니라 **데이터의 시각적 전달 실패**다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | objects/progress.md#현행 계약 | P0 | proposed |
| L0135 | 4.5 | S5의 별도 계산 조건은 **CSS viewport 355px / 브라우저 기본 글자 16px**다. 아래 수치는 S3와 환산 설명이 다르므로 독립 자료로 보존한다. | 355/16 | 측정/수치 355px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0136 | 4.5 | S5의 별도 계산 조건은 **CSS viewport 355px / 브라우저 기본 글자 16px**다. 아래 수치는 S3와 환산 설명이 다르므로 독립 자료로 보존한다. | 355/16 | 측정/수치 16px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0137 | 4.5 | \| 비교 항목 \| 원문이 인용한 당근 기준 \| 현생패치 당시 계산 \| | 355/16 | 비교표 원문 | 검토 | shells/page-shells.md#현행 계약 | P1 | history |
| L0138 | 4.5 | \| Header와 콘텐츠 시작선 \| 동일한 16px \| Header 약 12.4px, 본문 `.clan-section` 약 17px → 4.6px 차이 \| | 355/16 | 측정/수치 16px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0139 | 4.5 | \| Header와 콘텐츠 시작선 \| 동일한 16px \| Header 약 12.4px, 본문 `.clan-section` 약 17px → 4.6px 차이 \| | 355/16 | 측정/수치 12.4px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0140 | 4.5 | \| Header와 콘텐츠 시작선 \| 동일한 16px \| Header 약 12.4px, 본문 `.clan-section` 약 17px → 4.6px 차이 \| | 355/16 | 측정/수치 17px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0141 | 4.5 | \| Header와 콘텐츠 시작선 \| 동일한 16px \| Header 약 12.4px, 본문 `.clan-section` 약 17px → 4.6px 차이 \| | 355/16 | 측정/수치 4.6px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0142 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 18 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0143 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 24px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0144 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 700 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0145 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 21.4px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0146 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 850 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0147 | 4.5 | \| Root Title \| 18/24px, 700 \| 약 21.4px, 850, 줄높이 상속, 자간 -0.05em \| | 355/16 | 측정/수치 0.05 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0148 | 4.5 | \| Standard Title \| 18/24px, 700 \| 약 14.8px, 850 \| | 355/16 | 측정/수치 18 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0149 | 4.5 | \| Standard Title \| 18/24px, 700 \| 약 14.8px, 850 \| | 355/16 | 측정/수치 24px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0150 | 4.5 | \| Standard Title \| 18/24px, 700 \| 약 14.8px, 850 \| | 355/16 | 측정/수치 700 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0151 | 4.5 | \| Standard Title \| 18/24px, 700 \| 약 14.8px, 850 \| | 355/16 | 측정/수치 14.8px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0152 | 4.5 | \| Standard Title \| 18/24px, 700 \| 약 14.8px, 850 \| | 355/16 | 측정/수치 850 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0153 | 4.5 | \| 관련 텍스트 사이 \| 6px \| 클랜명↔메타 약 3px \| | 355/16 | 측정/수치 6px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0154 | 4.5 | \| 관련 텍스트 사이 \| 6px \| 클랜명↔메타 약 3px \| | 355/16 | 측정/수치 3px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0155 | 4.5 | \| 섹션 간격 \| 관계에 따라 선택 \| 일부 섹션 일괄 약 8px \| | 355/16 | 측정/수치 8px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0156 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 44px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0157 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 16 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0158 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 22px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0159 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 700 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0160 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 49.5px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0161 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 13.4px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0162 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 690 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0163 | 4.5 | \| Line Tabs 높이·라벨 \| 44px, 16/22px, 700 \| 약 49.5px, 13.4px, 기본 690/선택 820 \| | 355/16 | 측정/수치 820 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0164 | 4.5 | \| Tab 가로 패딩·간격 \| 패딩 10px, 간격은 배치에 따름 \| 패딩 약 2.3px, 간격 약 26.4px \| | 355/16 | 측정/수치 10px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0165 | 4.5 | \| Tab 가로 패딩·간격 \| 패딩 10px, 간격은 배치에 따름 \| 패딩 약 2.3px, 간격 약 26.4px \| | 355/16 | 측정/수치 2.3px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0166 | 4.5 | \| Tab 가로 패딩·간격 \| 패딩 10px, 간격은 배치에 따름 \| 패딩 약 2.3px, 간격 약 26.4px \| | 355/16 | 측정/수치 26.4px | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0167 | 4.5 | 탭의 실제 패딩보다 빈 간격이 커 `멤버 / 기록 / 소식`이 조작 가능한 탭보다 떨어진 텍스트처럼 보일 수 있다. 탭은 바로 아래 패널을 바꾸고, 상세 링크는 다른 화면으로 이동한다는 책임을 분명히 해야 한다. | 355/16 | 문장/사례 | 검토 | shells/page-shells.md#현행 계약 | P1 | proposed |
| L0168 | 4.5 | `translateX(0.5rem / 1rem / 1.5rem)`으로 아이콘 Glyph를 개별 이동한 분석도 있다. 기존 Grid 유무보다 **공통 Right Slot 안에서 Glyph 중심과 실제 Hit Target 중심이 일치하는지**가 핵심이다. 같은 x축 기준선·역할별 타이포·슬롯 정렬을 우선 교정한다. | 355/16 | 측정/수치 0.5rem | 검토 | shells/page-shells.md#현행 계약 | P1 | history |
| L0169 | 4.5 | `translateX(0.5rem / 1rem / 1.5rem)`으로 아이콘 Glyph를 개별 이동한 분석도 있다. 기존 Grid 유무보다 **공통 Right Slot 안에서 Glyph 중심과 실제 Hit Target 중심이 일치하는지**가 핵심이다. 같은 x축 기준선·역할별 타이포·슬롯 정렬을 우선 교정한다. | 355/16 | 측정/수치 1rem | 검토 | shells/page-shells.md#현행 계약 | P1 | history |
| L0170 | 4.5 | `translateX(0.5rem / 1rem / 1.5rem)`으로 아이콘 Glyph를 개별 이동한 분석도 있다. 기존 Grid 유무보다 **공통 Right Slot 안에서 Glyph 중심과 실제 Hit Target 중심이 일치하는지**가 핵심이다. 같은 x축 기준선·역할별 타이포·슬롯 정렬을 우선 교정한다. | 355/16 | 측정/수치 1.5rem | 검토 | shells/page-shells.md#현행 계약 | P1 | history |
| L0171 | 5.1 | \| 원문 관찰 대상 \| 표현 \| 진단 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | history |
| L0172 | 5.1 | \| 1번 현재 웹 시연 \| 어두운 단일 카드 중앙 이미지 + 아래 작은 이름 \| 구매·소유·장착·보상 중 어떤 맥락인지 모호한 단순 진열 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0173 | 5.1 | \| 2번 기존 시안 \| 상단 미디어 + 하단 이름·코인 가격·착용 상태 \| 속성과 상태가 있는 상품/소유물로 읽힘 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | objects/item.md#현행 계약 | P1 | history |
| L0174 | 5.1 | \| 3번 당근 Image Frame \| Ratio·Radius·Border·Fallback·Overlay Slot \| 표준 미디어 재료이며 완성 상품 카드는 아님 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0175 | 5.1 | \| 4번 경계 표시 도식 \| 미디어와 이름·가격/상태 슬롯 구분 \| 색·선보다 책임 경계와 정렬이 중요함 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0176 | 5.1 | 이 사례는 단순히 이름을 키우거나 가격을 추가하라는 요청이 아니다. **각 프레임을 해석 가능한 하나의 객체로 조립하지 못했다**는 문제다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0177 | 5.2 | \| 슬롯 \| 책임 \| 예시 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0178 | 5.2 | \| Media \| 물체·콘텐츠 자체 \| 블루 캡 이미지 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0179 | 5.2 | \| Identity \| 무엇인지 식별 \| 아이템명·분류·짧은 설명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0180 | 5.2 | \| Value \| 가격·획득 가치 \| `80 C`, 코인 이미지+금액 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 80 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0181 | 5.2 | \| State \| 현재 의미와 가능 상태 \| 보유·선택·장착·잠금 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0182 | 5.2 | \| Action \| 사용자가 실제로 할 일 \| 상세·구매·장착 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0183 | 5.2 | Media Slot과 Information Slot을 분리하되 반드시 선이나 다른 배경색으로 나눌 필요는 없다. 여백으로도 충분할 수 있다. **필요한 슬롯을 고르는 기준과 위치·정렬·상호작용의 일관성**을 표준화한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0184 | 5.2 | 전체 타일, 이미지, 별도 버튼 중 무엇을 누르는지도 계약에 포함한다. `Image Frame`은 비율·크롭·로딩·실패·오버레이를 책임지며, 이미지 밖 이름·가격의 객체 조립은 다른 책임이다. 모든 메타데이터를 이미지 Badge에 몰아넣지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | objects/item.md#현행 계약 | P1 | proposed |
| L0185 | 5.3 | \| 맥락 \| 우선 정보 \| 주요 행동 \| 줄이거나 생략할 정보 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0186 | 5.3 | \| 클랜 상자 미리보기 \| 이미지·이름·획득 조건/가능성 \| 보상 구성 확인 \| 구매·장착 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0187 | 5.3 | \| 상점 \| 이미지·이름·가격·구매 가능 여부 \| 구매·상세 \| 장착 정보 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0188 | 5.3 | \| 내 보관함 \| 이미지·이름·소유·장착 상태 \| 장착·상세 \| 구매 가격 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0189 | 5.3 | \| 프로필 꾸미기 \| 이미지·선택 상태·장착 가능성 \| 선택·장착 \| 불필요한 판매 메타 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0190 | 5.3 | \| 보상 획득 결과 \| 획득 사실·아트·이름 \| 확인·다음 단계 \| 반복 목록용 메타 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0191 | 5.3 | 공유할 것은 에셋·기초 미디어 프레임·텍스트·상태·가격 표현·정렬 원리다. 화면별로 바꿀 것은 슬롯·밀도·행동·표현 강도다. 모든 항목에 가격을 넣거나 모든 화면을 동일 상품 카드로 만들면 또 다른 목적 오류가 생긴다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | objects/item.md#현행 계약 | P1 | proposed |
| L0192 | 6.1 | \| 대상 \| 이미 있는 규칙 \| 적용 실패 또는 부족한 계약 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0193 | 6.1 | \| `P-HIERARCHY` \| 주목적·첫 시선·실제 데이터·다음 행동, 클랜 사람·활동·현재 상태 우선 \| 규칙을 적용하지 않았고 목적별 표현 선택·실제 비중 검수도 약함 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0194 | 6.1 | \| `C-SURFACE` \| Plain 포함 10역할, Content Card의 Media+Title+Detail+Metadata+Action \| 역할별 허용 슬롯·대표 행동·금지 조합·경계 강도 선택 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10 | 반례 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0195 | 6.1 | \| `C-IMAGE` \| item/thumbnail/hero, contain/cover, 비율·상태 \| 원본 용도·구도·점유율·크롭 안전영역 및 외부 정보 관계 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0196 | 6.1 | \| `A-ART` \| A1 작은 진열 / A2 강조 / A3 획득 \| 실제 주연 역할과 페이지 전체 밀어내기 효과 검수 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0197 | 6.1 | \| `C-TEXT`·`F-TYPE` \| 5계층과 토큰 \| 역할명 충돌, 슬롯별 의미 매핑 부족, 지역 스타일 우회 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0198 | 6.1 | \| `C-ACTION`·`P-ACTIONS` \| Emphasis·Tone·Treatment·Size \| 화면 전체 강조 경쟁·동일 역할 코드 재사용·예외 판단 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0199 | 6.1 | \| `C-STATUS` \| Badge·Progress 역할과 두께 \| 상태 중복·과밀·핵심 수치 주목도 및 실제 렌더 검사 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0200 | 6.1 | \| `S-PAGE` \| Root·Detail·Utility·Immersive·Overlay의 외곽 구조 \| 내부 콘텐츠의 목적별 유형과 우선순위는 별도 설계 필요 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0201 | 6.1 | \| `VALIDATION` \| 토큰·상태·반응형·접근성·오류 검사 \| 정보 객체 완결성·선택 이유·행동 예측·목적 적합성 검사 부족 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0202 | 6.5 | \| 관계 \| 필요한 계약 \| 사례 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0203 | 6.5 | \| `NAV-TOP + S-PAGE` \| Root/Standard별 제목·좌우 기준선·Right Slot·스크롤 관계 \| Header 12.4px와 본문 17px 시작선 차이 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 12.4px | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0204 | 6.5 | \| `NAV-TOP + S-PAGE` \| Root/Standard별 제목·좌우 기준선·Right Slot·스크롤 관계 \| Header 12.4px와 본문 17px 시작선 차이 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 17px | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0205 | 6.5 | \| `C-TABS + C-SELECTION` \| 상위 범주·하위 필터·보기 전환의 위치·순서·영향 범위 \| 멤버/기록/소식은 패널 전환, 기간은 같은 데이터 보기 전환 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0206 | 6.5 | \| `C-LIST + C-SURFACE` \| Flat List 우선 조건, 독립 Surface 필요 조건, Row/Group 경계 \| 사람·활동에 아이템 타일 문법 일괄 적용 금지 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0207 | 6.5 | \| `F-GEOMETRY + F-TYPE` \| 고정 Shell과 root 글자 확대의 충돌 검사 \| 확대 시 헤더·하단바·라벨·Safe Area 유지 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0208 | 6.5 | \| `C-IMAGE + C-CREDIT + C-RARITY + C-TEXT` \| 정보 객체의 슬롯·정렬·상태·행동 \| 블루 캡의 상점/보관함/미리보기 차이 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0209 | 6.5 | 당근의 **Header=탐색 맥락, Chip=조건, List=결과, Bottom Nav=루트 이동**은 관계가 화면에 드러나는 사례다. 클랜 홈의 ‘정체성→보상→진열’ 나열은 관계와 행동이 약했다. 두 페이지의 목적이 다르므로 클랜 홈에 당근 필터를 추가하는 복제로 해결하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0210 | 6.6 | \| 판단 \| 우선 후보 \| 배제하거나 주의할 후보 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | patterns/content-hierarchy.md#현행 계약 | P1 | candidate |
| L0211 | 6.6 | \| 간격만으로 구분 가능한가 \| Plain / Flat Section \| 의미 없는 Card \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0212 | 6.6 | \| 같은 그룹 내부 경계가 필요한가 \| 필요한 위치의 Divider \| 반복 목록 전체의 과도한 선·마지막 불필요한 Divider \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0213 | 6.6 | \| 독립된 정보 묶음인가 \| Solid 등 적합 Surface \| 텍스트만 둘러싼 임시 박스 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0214 | 6.6 | \| 이미지 자체가 중심인가 \| Media \| 정보 관리 영역을 아트로 종속 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0215 | 6.6 | \| 특별한 보상 강조인가 \| Reward + 적합한 A-ART 수준 \| Reward 안 Reward, Raised 안 Raised \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0216 | 6.6 | \| 반복되는 사람·활동·설정인가 \| 공통 슬롯의 List \| 모든 항목의 타일·그림자 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0217 | 6.6 | \| 짧은 실제 상태인가 \| Badge 또는 메타 \| 장식 Badge·선택 버튼 대용 Badge \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0218 | 6.6 | \| 상위 콘텐츠 범주 전환인가 \| Tabs \| 기간 Segmented로 대체 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0219 | 6.6 | \| 같은 데이터의 기간·보기 전환인가 \| Segmented \| 상위 내비게이션으로 오용 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0220 | 6.6 | \| 목록 결과의 조건 변경인가 \| Filter Chip·Filter Bar \| 영향받는 목록에서 떨어진 조작 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0221 | 6.6 | Surface 강도는 Inset Divider=그룹 내부의 약한 경계, Full-width Divider=그룹/행동 영역 사이 중간 경계, Surface Gap=의미가 다른 그룹의 강한 분리로 참고할 수 있다. **박스 유무보다 의미 경계의 강도를 선택하는 문제**다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0222 | 6.6 | #### 이름이 같아도 목적이 다르면 대응 컴포넌트가 아니다 | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | proposed |
| L0223 | 6.6 | 당근 `Page Banner`는 페이지에 영향을 주는 상태·안내 메시지에 초점을 두며 원문에서는 상단 원칙적 1개로 설명한다. 현생패치 게임 이벤트 아트 배너와 이름이 같다는 이유로 1:1 대응시키지 않는다. 선형 Progress를 당근 원형 Progress Circle과 병합하거나, 현생패치 캐시·등급·제스처를 당근의 비슷한 외형에 억지로 맞추지도 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | history |
| L0224 | 6.6 | 당근 `Page Banner`는 페이지에 영향을 주는 상태·안내 메시지에 초점을 두며 원문에서는 상단 원칙적 1개로 설명한다. 현생패치 게임 이벤트 아트 배너와 이름이 같다는 이유로 1:1 대응시키지 않는다. 선형 Progress를 당근 원형 Progress Circle과 병합하거나, 현생패치 캐시·등급·제스처를 당근의 비슷한 외형에 억지로 맞추지도 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1:1 | 검토 | patterns/content-hierarchy.md#현행 계약 | P1 | history |
| L0225 | 7.1 | **현재 장점:** 형태·강조도·의미색·터치 영역·상태 전환 중 레이아웃 안정성. 문서는 `proposed`이며 v0.15에서 일부 수치가 승인됐다고 기록되어 있다. 당근 참고점은 주변 버튼·콘텐츠와의 경쟁을 제한하는 조합 규칙이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 15 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0226 | 7.1 | \| 비교점 \| 현생패치 \| 당근 비교 내용 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0227 | 7.1 | \| 강조도 \| High / Medium / Low \| 동일 구분 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0228 | 7.1 | \| 대표 CTA \| 한 행동 영역에 기본 1개 \| 화면 전체 기본 1개 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0229 | 7.1 | \| 대표 CTA \| 한 행동 영역에 기본 1개 \| 화면 전체 기본 1개 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0230 | 7.1 | \| Brand \| 대표 행동·브랜드 맥락 \| 서비스 핵심 가치·연결 행동에 집중 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0231 | 7.1 | \| 버튼 조합 \| 의미에 맞는 가변 폭·비율 \| 권장·금지 조합을 더 구체화 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | components/action-button.md#현행 계약 | P2 | proposed |
| L0232 | 7.1 | \| 아이콘 \| 기본 1개·크기·간격 관리 \| Prefix/Suffix 분리, 양쪽 동시 사용 지양 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0233 | 7.1 | \| Pressed \| 크기 변화 금지 \| Scale 반응 허용 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | components/action-button.md#현행 계약 | P2 | proposed |
| L0234 | 7.1 | **실패 사례:** 같은 강도의 버튼 여러 개, 특히 가로 3개 이상 과밀·4개 이상 금지 사례; 모든 중요 버튼을 Brand Solid로 채우기; 불필요한 아이콘; 맥락 없는 `다음`; 긴 라벨을 좁은 가로 버튼에 유지하기. 선택이 어려워지고 클릭 유도력·가독성·접근성이 약해진다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 반례 | components/action-button.md#현행 계약 | P2 | proposed |
| L0235 | 7.1 | **실패 사례:** 같은 강도의 버튼 여러 개, 특히 가로 3개 이상 과밀·4개 이상 금지 사례; 모든 중요 버튼을 Brand Solid로 채우기; 불필요한 아이콘; 맥락 없는 `다음`; 긴 라벨을 좁은 가로 버튼에 유지하기. 선택이 어려워지고 클릭 유도력·가독성·접근성이 약해진다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4개 | 반례 | components/action-button.md#현행 계약 | P2 | proposed |
| L0236 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0237 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0238 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 34 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0239 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 43 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0240 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 48 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0241 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 48 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0242 | 7.1 | **개선:** 영역별 High 1개만 세지 말고 화면 대표 행동이 무엇인지 검수한다. 가로 액션 3개 이상이면 메뉴·다른 위치로 분산을 검토한다. Brand Solid는 퀘스트 수락·핵심 생성 등에 우선하고 단순 확인은 Neutral Solid를 검토한다. 높이 34/43/48, 최소 터치 48, 기본 SVG 17 및 Pressed 안정성을 보존한다. 당근 Scale을 자동 이식하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 17 | 검토 | components/action-button.md#현행 계약 | P2 | proposed |
| L0243 | 7.2 | **현재 장점:** Tabs·Segmented·Chip·Radio·Switch를 의미로 구분한다. 당근 참고점은 같은 Chip도 사용자 문제별로 나누는 것이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0244 | 7.2 | \| 역할 \| 의미·그룹 행동 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0245 | 7.2 | \| Suggestion \| 추천 항목을 선택 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0246 | 7.2 | \| Selection \| 단일·복수 선택, 다중 선택은 약한 강조 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0247 | 7.2 | \| Filter \| 결과 조건 적용·해제, Filter Bar·초기화와 연결 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0248 | 7.2 | \| Input \| 이미 입력한 값, 제거 조작 제공 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0249 | 7.2 | \| Navigation \| 카테고리 전환, Chip 자체보다 Tabs의 Chip 변형 책임 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0250 | 7.2 | **실패 사례:** 알약 모양만 보고 역할을 섞기, 활성 필터를 전부 강한 Solid로 강조하기, 비활성 필터뿐인데 초기화 노출하기. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0251 | 7.2 | **개선:** 필터 그룹의 단일/복수 선택·초기화·가로 스크롤/줄바꿈 기준을 보강한다. 실제 필요가 있을 때만 FilterBar 패턴을 구체화한다. 알약 형태 자체를 금지하는 것이 아니라 **용도 구분 없는 알약형 UI 남용**을 제한한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/selection-controls.md#현행 계약 | P2 | proposed |
| L0252 | 7.3 | **현재 장점:** Prefix / Content / Suffix로 행 슬롯을 통일한다. 당근 참고점은 콘텐츠 길이·상호작용 수·부모 Container 관계까지 제어하는 것이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/list-item.md#현행 계약 | P2 | proposed |
| L0253 | 7.3 | **실패 사례:** 같은 목록에서 행마다 정렬이 다름; 한 행에 4개 이상 상호작용; 체크와 별도 버튼의 타깃 충돌; 부모와 내부 눌림 영역에 동일 Radius를 기계적으로 적용하기. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4개 | 반례 | components/list-item.md#현행 계약 | P2 | proposed |
| L0254 | 7.3 | **개선:** `align=center\|top`을 콘텐츠 길이에 따라 선택한다. `클랜 멤버 + 이번 주 기여도 320` 같은 짧은 행은 Center, 여러 줄 활동 기록은 Top이 후보다. 부모 반경 16px·패딩 6px이면 내부 반경 10px을 쓰는 **동심 반경** 원리도 참고한다. 상호작용 밀도·정적 행의 비클릭 표현·부모 반경 대응을 명시하고 행별 그림자 금지 원칙을 유지한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 320 | 반례 | components/list-item.md#현행 계약 | P2 | candidate |
| L0255 | 7.3 | **개선:** `align=center\|top`을 콘텐츠 길이에 따라 선택한다. `클랜 멤버 + 이번 주 기여도 320` 같은 짧은 행은 Center, 여러 줄 활동 기록은 Top이 후보다. 부모 반경 16px·패딩 6px이면 내부 반경 10px을 쓰는 **동심 반경** 원리도 참고한다. 상호작용 밀도·정적 행의 비클릭 표현·부모 반경 대응을 명시하고 행별 그림자 금지 원칙을 유지한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 16px | 반례 | components/list-item.md#현행 계약 | P2 | candidate |
| L0256 | 7.3 | **개선:** `align=center\|top`을 콘텐츠 길이에 따라 선택한다. `클랜 멤버 + 이번 주 기여도 320` 같은 짧은 행은 Center, 여러 줄 활동 기록은 Top이 후보다. 부모 반경 16px·패딩 6px이면 내부 반경 10px을 쓰는 **동심 반경** 원리도 참고한다. 상호작용 밀도·정적 행의 비클릭 표현·부모 반경 대응을 명시하고 행별 그림자 금지 원칙을 유지한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 6px | 반례 | components/list-item.md#현행 계약 | P2 | candidate |
| L0257 | 7.3 | **개선:** `align=center\|top`을 콘텐츠 길이에 따라 선택한다. `클랜 멤버 + 이번 주 기여도 320` 같은 짧은 행은 Center, 여러 줄 활동 기록은 Top이 후보다. 부모 반경 16px·패딩 6px이면 내부 반경 10px을 쓰는 **동심 반경** 원리도 참고한다. 상호작용 밀도·정적 행의 비클릭 표현·부모 반경 대응을 명시하고 행별 그림자 금지 원칙을 유지한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10px | 반례 | components/list-item.md#현행 계약 | P2 | candidate |
| L0258 | 7.4 | **우선 개선 대상.** 10개 역할과 Plain이 이미 있다. Flat Section을 새로 발명할 문제가 아니라 선택 순서와 경계 강도의 문제다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10개 | 검토 | components/surface-media.md#현행 계약 | P2 | proposed |
| L0259 | 7.4 | **실패 사례:** 서로 다른 정보를 동일 카드·패딩·굵은 제목·테두리로 표현해 위계 상실; 반복 Divider; 마지막 불필요한 Divider; Raised 안 Raised·Reward 안 Reward; 내용 없이 문구만 둘러싼 박스. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/surface-media.md#현행 계약 | P2 | proposed |
| L0260 | 7.4 | **개선:** 6.6절의 Plain → 필요 Divider → 독립 Surface → Media/Reward 목적 판단을 적용한다. 허용 슬롯·대표 행동·자식 컴포넌트·금지 조합을 보강한다. Page Banner는 상태 메시지의 목적·배치 제약을 참고하되 이벤트 아트 배너와 동일시하지 않는다. 종류 추가보다 **아무 Surface도 쓰지 않을 조건**을 검수한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 6.6 | 반례 | components/surface-media.md#현행 계약 | P2 | proposed |
| L0261 | 7.5 | **현재 장점:** Badge 7톤과 선형 Progress 6/10/12px을 갖췄다. 당근 참고점은 톤 개수보다 강조 강도와 정보량 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 7 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0262 | 7.5 | **현재 장점:** Badge 7톤과 선형 Progress 6/10/12px을 갖췄다. 당근 참고점은 톤 개수보다 강조 강도와 정보량 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 6 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0263 | 7.5 | **현재 장점:** Badge 7톤과 선형 Progress 6/10/12px을 갖췄다. 당근 참고점은 톤 개수보다 강조 강도와 정보량 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0264 | 7.5 | **현재 장점:** Badge 7톤과 선형 Progress 6/10/12px을 갖췄다. 당근 참고점은 톤 개수보다 강조 강도와 정보량 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 12px | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0265 | 7.5 | \| Badge 표현 \| 목적 후보 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/badge-progress.md#현행 계약 | P2 | candidate |
| L0266 | 7.5 | \| Solid \| 이미지 위 등 복잡한 배경·높은 주목 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0267 | 7.5 | \| Outline \| 본문·상세의 중간 강조 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0268 | 7.5 | \| Weak \| 반복 목록의 낮은 강조 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0269 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0270 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4개 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0271 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0272 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2개 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0273 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0274 | 7.5 | **실패 사례:** 객체 하나에 배지 3–4개, 일반 상태까지 Brand, 긴 문장, 선택 버튼 대용. 원문 당근 권장 수준은 객체당 1–2개 이하·짧은 2–3단어다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 반례 | components/badge-progress.md#현행 계약 | P2 | history |
| L0275 | 7.5 | **개선:** `활동 중 / 주간 1위 / 가입됨`을 전부 Badge로 만들지 말고 핵심 상태만 남긴다. 나머지는 메타를 검토한다. 실제 상태값·중복·시선 비중을 점검하고 Progress는 표시값과 채움 길이까지 검증한다. 당근 원형 Progress와 선형 컴포넌트를 병합하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | components/badge-progress.md#현행 계약 | P2 | proposed |
| L0276 | 7.6 | **현재 장점:** 라벨·입력·오류·상태를 정의한다. 당근 참고점은 **Form=제출·검증 / Fieldset=관련 필드 묶음 / Field=라벨·설명·오류·글자 수 / Text Input=실제 편집**의 책임 분리다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/field.md#현행 계약 | P2 | proposed |
| L0277 | 7.6 | **개선할 정책:** 오류와 Helper가 동시에 있으면 오류 우선; 로컬 검증과 서버 검증 시점 분리; 값 손실 경고의 조건; 필수/선택 표기의 일관성. 원문은 필수 필드가 2/3 이상이면 선택 항목을 표시하고 그보다 적으면 필수를 표시하는 기준을 참고로 제시한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/field.md#현행 계약 | P2 | history |
| L0278 | 7.6 | **개선할 정책:** 오류와 Helper가 동시에 있으면 오류 우선; 로컬 검증과 서버 검증 시점 분리; 값 손실 경고의 조건; 필수/선택 표기의 일관성. 원문은 필수 필드가 2/3 이상이면 선택 항목을 표시하고 그보다 적으면 필수를 표시하는 기준을 참고로 제시한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | components/field.md#현행 계약 | P2 | history |
| L0279 | 7.6 | 수정 이력이 없거나 자동 저장이면 이탈 경고를 생략할 수 있다. 단일 입력 화면의 Underline 권장도 비교 아이디어이며 자동 채택하지 않는다. 기존 단일 Focus Border를 보존하면서 Field 표현과 Form 정책을 구별한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/field.md#현행 계약 | P2 | history |
| L0280 | 7.7 | **현재 장점:** SVG·Stroke·Glyph와 터치 타깃을 분리한다. `24×24`, Stroke 1.8, 최소 터치 48을 유지한다. 당근 참고점은 액션의 배치·수량·주변 정보 공존이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 24 | 검토 | components/icon-button.md#현행 계약 | P2 | proposed |
| L0281 | 7.7 | **현재 장점:** SVG·Stroke·Glyph와 터치 타깃을 분리한다. `24×24`, Stroke 1.8, 최소 터치 48을 유지한다. 당근 참고점은 액션의 배치·수량·주변 정보 공존이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 24 | 검토 | components/icon-button.md#현행 계약 | P2 | proposed |
| L0282 | 7.7 | **현재 장점:** SVG·Stroke·Glyph와 터치 타깃을 분리한다. `24×24`, Stroke 1.8, 최소 터치 48을 유지한다. 당근 참고점은 액션의 배치·수량·주변 정보 공존이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1.8 | 검토 | components/icon-button.md#현행 계약 | P2 | proposed |
| L0283 | 7.7 | **현재 장점:** SVG·Stroke·Glyph와 터치 타깃을 분리한다. `24×24`, Stroke 1.8, 최소 터치 48을 유지한다. 당근 참고점은 액션의 배치·수량·주변 정보 공존이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 48 | 검토 | components/icon-button.md#현행 계약 | P2 | proposed |
| L0284 | 7.7 | **실패 사례:** 헤더 아이콘을 계속 추가; 목록 액션이 제목·메타를 밀어냄; 뜻이 불명확한 Icon-only 행동. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/icon-button.md#현행 계약 | P2 | proposed |
| L0285 | 7.7 | **개선:** 승인 `NAV-TOP` 슬롯·우선순위를 따른다. 원문 당근 우측 액션은 기본 2개 권장·최대 3개 허용이다. 초과 기능은 더보기 등을 검토하고 모호하면 텍스트로 명시한다. 새 그림을 만드는 것보다 **발견 가능성과 액션 밀도**를 개선한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2개 | 검토 | components/icon-button.md#현행 계약 | P2 | history |
| L0286 | 7.7 | **개선:** 승인 `NAV-TOP` 슬롯·우선순위를 따른다. 원문 당근 우측 액션은 기본 2개 권장·최대 3개 허용이다. 초과 기능은 더보기 등을 검토하고 모호하면 텍스트로 명시한다. 새 그림을 만드는 것보다 **발견 가능성과 액션 밀도**를 개선한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 검토 | components/icon-button.md#현행 계약 | P2 | history |
| L0287 | 7.8 | **현재 장점:** Line 중심의 상위 콘텐츠 탐색. 당근은 Line/Chip의 목적별 선택·영향 범위를 더 구체화한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/tabs.md#현행 계약 | P2 | proposed |
| L0288 | 7.8 | **실패 사례:** Chip Tabs 옆 Filter Chip을 섞어 역할 혼동; 실제 패널과 연결되지 않는 탭; 부분 패널 전환인데 전체 화면 전환처럼 과도하게 강조. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/tabs.md#현행 계약 | P2 | proposed |
| L0289 | 7.8 | **개선:** Line 기본을 유지하고 실제 필요할 때 Chip 변형을 검토한다. 원문 기준 Line의 Fill 배치는 최대 5개, 많으면 Hug+가로 스크롤 검토다. 알림은 일반 상태 Badge 대신 Notification Badge를 사용한다. `멤버 / 기록 / 소식`은 아래 패널 전환, 상세 링크는 화면 이동으로 구분한다. 기간 필터를 비슷한 알약 형태로 연달아 배치해 혼동시키지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5개 | 검토 | components/tabs.md#현행 계약 | P2 | history |
| L0290 | 7.9 | **현재 장점:** 2–4개 선택지·짧은 라벨·즉시 변경, Track 1개+Thumb 1개와 Hug 너비. 당근 참고점은 반복과 선택지 수의 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/segmented-control.md#현행 계약 | P2 | proposed |
| L0291 | 7.9 | **현재 장점:** 2–4개 선택지·짧은 라벨·즉시 변경, Track 1개+Thumb 1개와 Hug 너비. 당근 참고점은 반복과 선택지 수의 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4개 | 검토 | components/segmented-control.md#현행 계약 | P2 | proposed |
| L0292 | 7.9 | **현재 장점:** 2–4개 선택지·짧은 라벨·즉시 변경, Track 1개+Thumb 1개와 Hug 너비. 당근 참고점은 반복과 선택지 수의 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/segmented-control.md#현행 계약 | P2 | proposed |
| L0293 | 7.9 | **현재 장점:** 2–4개 선택지·짧은 라벨·즉시 변경, Track 1개+Thumb 1개와 Hug 너비. 당근 참고점은 반복과 선택지 수의 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/segmented-control.md#현행 계약 | P2 | proposed |
| L0294 | 7.9 | **실패 사례:** 한 화면에 여러 Segmented 나열, 5개 이상 선택지, 상위 탐색을 기간·보기 컨트롤로 대체. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5개 | 반례 | components/segmented-control.md#현행 계약 | P2 | proposed |
| L0295 | 7.9 | **개선:** 기본 화면 1개를 후보로 두고 2개 이상은 영향 범위가 명확할 때만 검토한다. 5개 이상은 Radio/Tabs/Filter를 비교한다. 기존 구조를 보존하며 ‘겹치지 않는다’보다 구체적인 반복 조건을 적는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/segmented-control.md#현행 계약 | P2 | candidate |
| L0296 | 7.9 | **개선:** 기본 화면 1개를 후보로 두고 2개 이상은 영향 범위가 명확할 때만 검토한다. 5개 이상은 Radio/Tabs/Filter를 비교한다. 기존 구조를 보존하며 ‘겹치지 않는다’보다 구체적인 반복 조건을 적는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2개 | 검토 | components/segmented-control.md#현행 계약 | P2 | candidate |
| L0297 | 7.9 | **개선:** 기본 화면 1개를 후보로 두고 2개 이상은 영향 범위가 명확할 때만 검토한다. 5개 이상은 Radio/Tabs/Filter를 비교한다. 기존 구조를 보존하며 ‘겹치지 않는다’보다 구체적인 반복 조건을 적는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5개 | 검토 | components/segmented-control.md#현행 계약 | P2 | candidate |
| L0298 | 7.10 | **제품 고유 차이 보존.** 당근 일반 Checkbox는 복수 선택·약관·그룹·부모 Indeterminate와 행 전체 선택을 다룬다. 현생패치 퀘스트 체크는 실제 수행 완료 명령이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0299 | 7.10 | \| 구분 \| 일반 Checkbox \| 퀘스트 완료 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0300 | 7.10 | \| 의미 \| 선택값 변경 \| 수행 완료 확정 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0301 | 7.10 | \| 상태 \| Checked / Mixed \| Pending / Confirmed / Failed \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0302 | 7.10 | \| 실패 \| 선택 상태 복원 \| 완료 실패 표시·복구 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0303 | 7.10 | \| 영향 \| 주로 선택 \| 보상·기록과 연결 가능 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0304 | 7.10 | **개선:** 시각 상태는 재사용해도 도메인 명령 계약을 분리한다. 사용자가 체크했다는 이유로 서버 보상까지 성공한 것처럼 표시하지 않는다. 범용 체크의 편의성을 완료 확정 의미에 무조건 적용하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/checkbox.md#현행 계약 | P2 | proposed |
| L0305 | 7.11 | **현재 장점:** 투명 도장 마스코트·장비 레이어·실루엣 보존. 당근은 사진 신원·Fallback·Badge·Avatar Stack을 다룬다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/avatar.md#현행 계약 | P2 | proposed |
| L0306 | 7.11 | **부족한 관계:** 여러 명이 모일 때 겹침 순서·최대 노출·초과 인원·장비 가림·레이어 충돌. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/avatar.md#현행 계약 | P2 | proposed |
| L0307 | 7.11 | **개선:** Stack 표시 순서와 `+N`을 정의한다. 당근이 표시 수를 줄일 때 앞쪽부터 감추는 규칙과 Badge Mask를 둔 사례는 비교 자료다. 적용 여부는 마스코트 장비·실루엣 조건과 검토한다. 투명 마스코트를 원형 사용자 사진으로 바꾸지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/avatar.md#현행 계약 | P2 | proposed |
| L0308 | 7.12 | **현재 장점:** contain/cover·Ratio·로딩·실패·Fallback. 당근 참고점은 Overlay 종류·슬롯·배치 제한이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0309 | 7.12 | \| Overlay \| 책임 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0310 | 7.12 | \| Badge \| 상태·분류 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0311 | 7.12 | \| Indicator \| 이미지 수·영상 길이 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0312 | 7.12 | \| Reaction Button \| 좋아요 등의 독립 행동 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0313 | 7.12 | \| Icon \| 콘텐츠 유형 설명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0314 | 7.12 | 원문에는 **모서리 슬롯당 1개** 규칙[S2]과 **이미지 전체 Overlay 최대 2개 권장**[S6]이 함께 인용된다. 서로 다른 범위의 주장으로 보존하고 ‘공식적으로 총 몇 개’라는 하나의 확인값으로 단정하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1개 | 검토 | components/image-frame.md#현행 계약 | P2 | history |
| L0315 | 7.12 | 원문에는 **모서리 슬롯당 1개** 규칙[S2]과 **이미지 전체 Overlay 최대 2개 권장**[S6]이 함께 인용된다. 서로 다른 범위의 주장으로 보존하고 ‘공식적으로 총 몇 개’라는 하나의 확인값으로 단정하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2개 | 검토 | components/image-frame.md#현행 계약 | P2 | history |
| L0316 | 7.12 | **실패 사례:** 착용 중·등급·수량·잠금·선택·이벤트 리본을 전부 이미지에 중첩. 핵심 물체를 가리거나 행동과 상태를 혼동한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0317 | 7.12 | **개선:** 종류·위치·우선순위를 정하고 초과 정보를 밖으로 이동한다. 투명 마스코트·장비는 contain, 배경이 있는 썸네일은 cover를 유지한다. 사진용 Radius·크롭을 그대로 복제하지 않는다. Hero 사용 전 원본 용도·구도·안전영역도 확인한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/image-frame.md#현행 계약 | P2 | proposed |
| L0318 | 7.13 | **제품 차이 유지.** 현생패치는 활성 Fill/비활성 Outline 및 색 변화, 당근은 Fill 통일+색 구분으로 기록된다. 당근 비교 기준은 최대 5탭·한글 라벨 5자 이내 권장·Android 내부 컨테이너 최대 480px·숫자/점 알림 구분·Small/Large Badge다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0319 | 7.13 | **제품 차이 유지.** 현생패치는 활성 Fill/비활성 Outline 및 색 변화, 당근은 Fill 통일+색 구분으로 기록된다. 당근 비교 기준은 최대 5탭·한글 라벨 5자 이내 권장·Android 내부 컨테이너 최대 480px·숫자/점 알림 구분·Small/Large Badge다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5자 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0320 | 7.13 | **제품 차이 유지.** 현생패치는 활성 Fill/비활성 Outline 및 색 변화, 당근은 Fill 통일+색 구분으로 기록된다. 당근 비교 기준은 최대 5탭·한글 라벨 5자 이내 권장·Android 내부 컨테이너 최대 480px·숫자/점 알림 구분·Small/Large Badge다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 480px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0321 | 7.13 | **실패 사례:** 루트 목적지 과다; 모든 탭에 알림 장식; 3개 이상 탭의 Badge 과밀; 페이지마다 다른 하단바. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 반례 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0322 | 7.13 | **개선:** 승인 SVG 원본을 보존하고 탭 수·Badge 밀도·넓은 화면 배치·Safe Area·선택 상태를 전역 계약으로 검수한다. 당시 시연의 5개 목적지를 최종 IA 확정으로 해석하지 않는다. 실제 높이 약 65.2px+Safe Area와 `F-GEOMETRY` 76px 제안값의 차이는 책임을 확인해야 하며 어느 한쪽을 자동 정답으로 삼지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5개 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |
| L0323 | 7.13 | **개선:** 승인 SVG 원본을 보존하고 탭 수·Badge 밀도·넓은 화면 배치·Safe Area·선택 상태를 전역 계약으로 검수한다. 당시 시연의 5개 목적지를 최종 IA 확정으로 해석하지 않는다. 실제 높이 약 65.2px+Safe Area와 `F-GEOMETRY` 76px 제안값의 차이는 책임을 확인해야 하며 어느 한쪽을 자동 정답으로 삼지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 65.2px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |
| L0324 | 7.13 | **개선:** 승인 SVG 원본을 보존하고 탭 수·Badge 밀도·넓은 화면 배치·Safe Area·선택 상태를 전역 계약으로 검수한다. 당시 시연의 5개 목적지를 최종 IA 확정으로 해석하지 않는다. 실제 높이 약 65.2px+Safe Area와 `F-GEOMETRY` 76px 제안값의 차이는 책임을 확인해야 하며 어느 한쪽을 자동 정답으로 삼지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 76px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |
| L0325 | 7.14 | **직접 대응하는 당근 재화 컴포넌트 없음.** Badge는 간접 참고일 뿐이다. 현생패치는 Coin Image + Amount + Optional Unit과 보유·획득·미확인 상태를 다룬다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/credit-value.md#현행 계약 | P2 | proposed |
| L0326 | 7.14 | **실패 사례:** 같은 `240 캐시`를 상단 잔액·퀘스트 보상·결과에서 항상 같은 크기·강한 금색으로 표시해 강조 경쟁; 보유액·예상 보상·수령 완료의 의미 혼동. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 240 | 반례 | components/credit-value.md#현행 계약 | P2 | proposed |
| L0327 | 7.14 | **개선:** C-CREDIT은 이미지와 값의 정렬·서식·상태 의미를 책임진다. 강조 강도와 화면 비중은 부모가 결정한다. 도메인 재화 표현을 당근 Badge로 대체하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/credit-value.md#현행 계약 | P2 | proposed |
| L0328 | 7.15 | **현재 장점:** 단일 선택·Focus·접근성. 당근 참고점은 개수·정보량을 보고 Radio가 적합한지부터 판단하는 것이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/radio-choice.md#현행 계약 | P2 | proposed |
| L0329 | 7.15 | **개선:** 원문 기준 2–6개 선택지에 주로 적합하고 많으면 Input Button+Sheet를 검토한다. 세로 배열을 기본으로, 가로는 라벨 소속 혼동을 확인한다. 자주 쓰거나 추천 근거가 있는 값은 기본 선택을 고려하되 중요한 결정·근거 없는 추천에는 강제로 기본값을 정하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/radio-choice.md#현행 계약 | P2 | history |
| L0330 | 7.15 | **개선:** 원문 기준 2–6개 선택지에 주로 적합하고 많으면 Input Button+Sheet를 검토한다. 세로 배열을 기본으로, 가로는 라벨 소속 혼동을 확인한다. 자주 쓰거나 추천 근거가 있는 값은 기본 선택을 고려하되 중요한 결정·근거 없는 추천에는 강제로 기본값을 정하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 6개 | 검토 | components/radio-choice.md#현행 계약 | P2 | history |
| L0331 | 7.16 | **우선 개선: 역할명 충돌.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0332 | 7.16 | \| 출처 \| 5역할 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 5 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0333 | 7.16 | \| C-TEXT \| Display / Section / Item / Body / Meta \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0334 | 7.16 | \| v0.15 \| Display / Title / Section / Body / Caption \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 15 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0335 | 7.16 | `Item↔Title`, `Meta↔Caption`을 구현자가 임의 해석할 위험이 있다. 당근은 크기·줄높이·굵기 조합인 **Scale Style**(예: `t5Regular`)과 의미인 **Semantic Style**(예: `screenTitle`, `articleBody`)을 구분한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0336 | 7.16 | **개선:** 역할명 하나로 정리하고 Scale과 Semantic을 분리하며 슬롯별 예시를 둔다. 후보 매핑은 페이지명=Title, 중요한 보상 수치=Display 또는 강조 숫자, 목록 그룹=Section, 반복 목록 제목=Body 기반 강조, 시간·설명=Caption이다. **후보 매핑이며 이 보고서에서 새 승인으로 확정하지 않는다.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/text-hierarchy.md#현행 계약 | P2 | candidate |
| L0337 | 7.16 | **실패 사례:** 모든 제목 Bold; 섹션마다 Hero급 제목; 비슷한 크기·굵기의 제목 연속 경쟁. 수치 스케일 준수만으로 실제 위계를 합격시키지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/text-hierarchy.md#현행 계약 | P2 | proposed |
| L0338 | 7.17 | **현재 장점:** 56개 원본·Stroke·라운딩. 당근 참고점은 아이콘을 의미 전달의 공통 언어로 관리하는 것이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 56개 | 검토 | components/svg-icon.md#현행 계약 | P2 | proposed |
| L0339 | 7.17 | **개선 순서:** 기존 유사 아이콘 확인 → 의미 적합성 → 같은 화면의 혼동 가능성 → 새 자산 필요성 → 형태 검토·승인 후 공용 편입. 직관적 메타포·기존 라이브러리 재사용·기능명보다 형상 중심 이름을 참고한다. 페이지 전용 아이콘의 자동 추가를 허용하지 않는다. 기존 원본을 변경할 근거는 이 분석에 없다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/svg-icon.md#현행 계약 | P2 | history |
| L0340 | 7.18 | **직접 대응하는 당근 통합 컴포넌트 없음.** Bottom Sheet 상호작용은 간접 참고다. 현생패치는 Pointer 추적·축·임계값·Scroll↔Drag 소유권·PointerCancel 복구·키보드 대체가 오히려 상세하다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/gesture.md#현행 계약 | P2 | proposed |
| L0341 | 7.18 | **본질적 질문:** 가능한 제스처인가보다 **이 상황에서 허용해야 하는가**가 먼저다. 중요한 입력이 사라질 위험이 있는 Sheet에서는 Handle보다 명시 Close가 적절한 사례가 있다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/gesture.md#현행 계약 | P2 | proposed |
| L0342 | 7.18 | **개선:** 클랜 생성 폼의 미저장 값 등 손실 위험에 따라 Drag dismiss 제한·확인을 검토한다. 기존 기술 계약을 보존하고 사용 허용/금지 조건을 보강한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/gesture.md#현행 계약 | P2 | history |
| L0343 | 7.19 | **우선 개선: Sheet/Page/Dialog 선택.** | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0344 | 7.19 | \| 비교점 \| 현생패치 \| 당근 비교 내용 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0345 | 7.19 | \| 높이 \| Content 기반 Min/Mid/Full \| 콘텐츠 기반 자동 높이 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0346 | 7.19 | \| Snap \| 열린 높이 최대 3개 지원 \| 필요 시 2–3개 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0347 | 7.19 | \| Snap \| 열린 높이 최대 3개 지원 \| 필요 시 2–3개 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0348 | 7.19 | \| Snap \| 열린 높이 최대 3개 지원 \| 필요 시 2–3개 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0349 | 7.19 | \| Full \| Safe Area 제외 전체 \| 최대 약 90% 권장 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 90% | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0350 | 7.19 | \| 긴 콘텐츠 \| Native 내부 Scroll \| 많으면 별도 Page 검토 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0351 | 7.19 | \| 닫기 \| Drag/Backdrop/Button \| 손실·오류 위험에 따라 선택 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0352 | 7.19 | \| Scroll \| Native 우선 \| Content Scroll·Scroll Fog \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0353 | 7.19 | **개선:** 짧은 선택은 단일 높이, 확장형 탐색은 필요한 Snap만, 긴 입력·다단계 작업은 Page 우선, 위험한 확정은 Confirmation Dialog 우선 검토. 당근의 Snap 사용 시 Handle 요구도 참고한다. 최대 3개 지원은 매번 Min/Mid/Full을 제공하라는 뜻이 아니다. 일반 메뉴에 불필요한 Snap을 넣지 말고 엔진보다 콘텐츠 담는 위치를 먼저 판단한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3개 | 검토 | components/bottom-sheet.md#현행 계약 | P2 | proposed |
| L0354 | 7.20 | **직접 대응하는 당근 컴포넌트 없음.** Image Frame은 간접 참고이며 게임 경험의 고유 등급 표현을 보존한다. 현행은 Common~Special 후보 그라데이션·Selected/Equipped/Disabled·크롭 안전영역을 다룬다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | candidate |
| L0355 | 7.20 | \| 맥락 \| 우선순위 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0356 | 7.20 | \| 일반 진열 \| 물체가 주연, 등급색은 보조 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0357 | 7.20 | \| 등급 비교 \| 색+텍스트로 희귀도 식별 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0358 | 7.20 | \| 선택·착용 \| 등급보다 현재 조작 상태 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0359 | 7.20 | \| 보상 획득 \| 이때만 A-ART의 강한 연출 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0360 | 7.20 | 모든 등급색이 강하면 목록이 광고 배너처럼 되고 모두 무채색이면 수집 가치가 약해진다. **희귀도·선택 상태·획득 연출의 시각 우선순위를 분리**하며 게임 표현을 당근에 맞춰 제거하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/item-rarity-surface.md#현행 계약 | P2 | proposed |
| L0361 | 7.21 | **우선 개선: 대체하면 안 되는 상황 명시.** 기존 Sheet/Dialog/Snackbar/Skeleton/Empty 구분을 유지한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | history |
| L0362 | 7.21 | \| 상황 \| 적합 후보 \| 지양 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | candidate |
| L0363 | 7.21 | \| 짧은 작업 완료 \| Snackbar \| 매번 확인 Dialog \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0364 | 7.21 | \| 지속 경고 \| Page Banner / Callout \| 곧 사라지는 Snackbar \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0365 | 7.21 | \| 삭제·초기화 확인 \| Alert Dialog \| 일반 정보처럼 표시 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0366 | 7.21 | \| 복잡하고 중요한 입력 \| 적합한 Dialog 또는 Page \| 좁은 경고창에 복잡한 UI \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0367 | 7.21 | \| 목록 로딩 \| Skeleton \| 같은 위치 Skeleton+Spinner 중복 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0368 | 7.21 | \| 특정 저장 처리 \| Progress Circle \| 불필요한 페이지 전체 Skeleton \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/overlay-feedback.md#현행 계약 | P2 | proposed |
| L0369 | 7.21 | Snackbar는 중요한 결정·지속 경고의 대체물이 아니다. Alert Dialog는 단순 경고·확인의 명확한 구조를 유지한다. **Skeleton은 회색 애니메이션이 아니라 곧 나타날 콘텐츠 구조의 예고**다. 상황→타입→금지 타입을 기존 문서에 통합한다. 퀘스트 완료마다 Dialog를 띄우지 말고 특별 획득 순간에 강한 연출을 사용한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | components/overlay-feedback.md#현행 계약 | P2 | history |
| L0370 | 8.2 | \| 단계 \| 반드시 결정할 것 \| 최소 산출물 \| 통과 조건 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0371 | 8.2 | \| 01 목적 정의 \| 사용자 과업·화면 주연·최우선 행동·다른 화면 책임 \| Page Intent \| 목적·행동·범위를 짧고 명료하게 설명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 01 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0372 | 8.2 | \| 02 콘텐츠 모델링 \| 데이터 의미·중요도·조회/수정 행동·중복/파생 관계 \| 콘텐츠·행동 인벤토리 \| 중복과 의미 충돌의 필요성 판정 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 02 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0373 | 8.2 | \| 03 정보 구조 \| 정보 객체·그룹 경계·섹션 배치·화면 간 이동 \| 객체·섹션 구조 \| 결합·분리에 이유가 있고 책임 명확 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 03 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0374 | 8.2 | \| 04 패턴·컴포넌트 매핑 \| 기존 Pattern/Component/Variant와 제외 후보 \| 선택·제외 근거표 \| 모양이 아닌 역할 적합성으로 선택 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 04 | 검토 | PROCESS.md#현행 계약 | P1 | candidate |
| L0375 | 8.2 | \| 05 시각 위계 \| 첫 시선·면적·밀도·표면·텍스트·CTA 상대 강조 \| 레이아웃·시각 우선순위 메모 \| 핵심과 보조의 주목도 대응, 승인 토큰 준수 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 05 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0376 | 8.2 | \| 06 구현·시연 \| 데이터·상태·반응형·터치·이동 \| 작동하는 화면 \| 설계 근거와 구현이 대응, 기본 행동 확인 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 06 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0377 | 8.2 | \| 07 검증·피드백 \| 목적·레퍼런스·규격·사용성·회귀 \| 결과·미결·수정 위치 \| 실제 화면 근거 확보, 미해결을 완료로 처리하지 않음 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 07 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0378 | 8.3 | \| 항목 \| 설명용 예시 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0379 | 8.3 | \| 목적 \| 소속감과 함께하는 활동 확인 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0380 | 8.3 | \| 핵심 대상 \| 내 클랜·구성원 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0381 | 8.3 | \| 핵심 데이터 \| 참여 현황·멤버 활동·공동 기록 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0382 | 8.3 | \| 보조 데이터 \| 주간 보상 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0383 | 8.3 | \| 주요 행동 \| 클랜 대화·활동 확인 및 관련 이동 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0384 | 8.3 | \| 다른 화면 책임 \| 보상 상세·전체 멤버·기록 상세 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0385 | 8.3 | 이는 확정 화면 명세가 아니다. 이런 기준이 있으면 보상 Hero가 절반 가까운 면적을 차지할 때 목적 대비 과잉인지 판단할 수 있다. **고정 비율이 아니라 배분 이유가 핵심**이며 특별 이벤트의 일시적 예외도 목적·기간을 명시한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0386 | 8.4 | \| 표현하려는 것 \| 채택 후보 \| 제외·분리 근거 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | candidate |
| L0387 | 8.4 | \| 이번 주 공동 성취 \| 숫자+공유 Progress \| 이벤트 카피의 부속 데이터로 만들지 않음 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0388 | 8.4 | \| 짧은 이벤트 안내 \| A2 배너 \| 장문의 보상 구성·실시간 수치를 모두 넣지 않음 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0389 | 8.4 | \| 멤버의 활동 \| 투명 배경 List·Avatar·메타 \| 물체 진열용 3열 Reward Grid와 역할 다름 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0390 | 8.4 | \| 같은 기록의 기간 변경 \| Segmented \| 상위 멤버/기록/소식 Tabs와 영향 범위 다름 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0391 | 8.4 | \| 보상 구성 이동 \| Low Action 또는 적합 Interactive Surface \| 실제 목적지가 같은 링크를 여러 개 반복할 이유가 있는지 검사 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0392 | 8.5 | \| 판단 축 \| 질문 \| 실패 사례 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0393 | 8.5 | \| 첫 시선 \| 첫 2초에 알아야 하는 것은 텍스트·숫자·아트 중 무엇인가 \| 활동보다 상자 광고가 먼저 보임 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0394 | 8.5 | \| 면적 \| 점유에 정보·행동 가치의 근거가 있는가 \| 보조 콘텐츠가 뷰포트 독점 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0395 | 8.5 | \| 표면 \| Plain·카드·배너 중 왜 이 경계인가 \| 모든 정보를 같은 박스에 가둠 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0396 | 8.5 | \| 타이포 \| 승인 역할·토큰과 상대적 위계가 맞는가 \| 큰 제목들이 같은 강도로 경쟁 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0397 | 8.5 | \| 행동 \| High/Medium/Low가 실제 과업 중요도와 맞는가 \| 영역마다 강한 CTA \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0398 | 8.5 | \| 밀도 \| 문구·수치·링크를 중복 없이 이해하는가 \| 제목·설명·Badge·CTA에 같은 정보 반복 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0399 | 8.5 | \| 아트 \| 초점을 만드는가, 작은 장식이거나 무리한 확대인가 \| A1 존재만으로 A2 합격 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0400 | 8.5 | 필요한 값이 없다면 페이지 안에서 임의 HEX·font-size·variant를 늘리기 전에 개선 제안·영향을 검토한다. 역할·상태가 같으면 같은 공통 계약을 기본으로 사용하고 차이가 필요하면 이유를 남긴다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | PROCESS.md#현행 계약 | P1 | candidate |
| L0401 | 8.6 | \| 게이트 \| 실패 조건 \| 돌아갈 단계 \| 수정 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0402 | 8.6 | \| G1 목적 \| 주목적·주행동·다른 화면 책임 불명확 \| 01 \| 역할·범위 결정 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 01 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0403 | 8.6 | \| G2 콘텐츠 \| 불필요한 정보 반복·의미 충돌 \| 02 \| 데이터 의미·중복 정리 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 02 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0404 | 8.6 | \| G3 구조 \| 묶음 이유가 없거나 객체로 읽히지 않음 \| 03 \| 슬롯·그룹 경계 재설계 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 03 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0405 | 8.6 | \| G4 선택 \| 모양 유사성 외 선택 이유 없음 \| 04 \| 역할에 맞는 기존 패턴 비교 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 04 | 검토 | PROCESS.md#현행 계약 | P1 | history |
| L0406 | 8.6 | \| G5 시각 \| 보조가 핵심보다 강함 \| 05, 필요 시 01 \| 면적·강조·역할 재검토 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 05 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0407 | 8.6 | \| G5 시각 \| 보조가 핵심보다 강함 \| 05, 필요 시 01 \| 면적·강조·역할 재검토 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 01 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0408 | 8.6 | \| G6 계약·구현 \| 같은 역할을 임의 스타일로 분기·승인 규격 우회 \| 04–06 \| 공통 계약 적용·지역 분기 제거 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 04 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0409 | 8.6 | \| G6 계약·구현 \| 같은 역할을 임의 스타일로 분기·승인 규격 우회 \| 04–06 \| 공통 계약 적용·지역 분기 제거 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 06 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0410 | 8.6 | \| G7 검증 \| 렌더됐지만 실제 비교·행동 확인 없음 \| 07 \| 화면·상호작용 근거 확보 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 07 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0411 | 8.6 | 실패하면 해당 결정을 성공 처리하지 않고 원인을 낳은 단계로 돌아간다. 테스트·프로토타입의 합의된 예외는 범위와 이유를 기록할 수 있다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0412 | 8.6 | **상자 사례의 복귀:** ‘너무 크다’는 관찰 → G5 실패 → G1에서 주연 확인 → 정체성·활동·공동 기록과 보상 홍보의 책임 분리 → 적합 패턴 재선택 → 크기·배치 수정 → 실제 화면 검증. 원인이 역할 오판이면 CSS height만 줄여서는 해결되지 않는다. 치수 수정 자체가 항상 잘못인 것은 아니다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 반례 | PROCESS.md#현행 계약 | P1 | history |
| L0413 | 9.3 | \| 대상 \| 최소 보강 \| 대응 원문 아이디어 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | history |
| L0414 | 9.3 | \| P-HIERARCHY \| 홍보/실용 정보/탐색/수령 결과 분류, 실제 데이터·주행동·면적 이유 \| S4 R1, S6 H02, S7 D01–D04 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0415 | 9.3 | \| C-SURFACE \| 허용 슬롯·대표 행동·자식·금지 조합·Plain 우선 조건 \| S4 R2, S6 H01/H06 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0416 | 9.3 | \| A-ART+C-IMAGE \| A1 확대 실패 반례, A2 전용 구도·광원·안전영역·실제 주목도 \| S4 R3 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0417 | 9.3 | \| C-ACTION+P-ACTIONS \| 역할·Emphasis→공통 Variant·화면 대표 CTA·금지 조합 \| S4 R4 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0418 | 9.3 | \| C-TEXT+F-TYPE \| 역할명 충돌 해소·Scale/Semantic·슬롯 매핑·최종 가독성 \| S2 16, S3 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 16 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0419 | 9.3 | \| C-LIST+C-STATUS \| 정렬·행동 밀도·부모 반경·배지 정보량·실제 Progress \| S2 03/05 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 03 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0420 | 9.3 | \| C-LIST+C-STATUS \| 정렬·행동 밀도·부모 반경·배지 정보량·실제 Progress \| S2 03/05 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 05 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0421 | 9.3 | \| C-SHEET+C-OVERLAY+C-GESTURE \| 담을 위치·손실 위험·피드백 대체 금지 조건 \| S2 18/19/21 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 18 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0422 | 9.3 | \| C-SHEET+C-OVERLAY+C-GESTURE \| 담을 위치·손실 위험·피드백 대체 금지 조건 \| S2 18/19/21 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 19 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0423 | 9.3 | \| C-SHEET+C-OVERLAY+C-GESTURE \| 담을 위치·손실 위험·피드백 대체 금지 조건 \| S2 18/19/21 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 21 | 반례 | PROCESS.md#현행 계약 | P1 | proposed |
| L0424 | 9.3 | \| S-PAGE \| Shell과 콘텐츠 목적의 책임 구분 \| S6 H03 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0425 | 9.3 | \| INDEX \| 새 페이지에서 목적·Pattern·객체의 읽기/선택 경로 \| S6 H04, S7 D07 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0426 | 9.3 | \| VALIDATION \| 객체 완결성·목적·중복·클릭·계층·에셋 용도·실제 비교 \| S4 R5, S6 H05, S7 D06 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | PROCESS.md#현행 계약 | P1 | proposed |
| L0427 | 9.5 | \| 역할 \| 기본 후보 \| 조건 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | candidate |
| L0428 | 9.5 | \| 수락·확정·실제 수령 \| C-ACTION High / Solid \| 의미 Tone, 화면 대표 행동 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0429 | 9.5 | \| 일반 실행·초대 \| C-ACTION Medium / Neutral Weak 등 \| 일반 행동의 비중 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0430 | 9.5 | \| 자세히·전체 보기·미리보기 \| C-ACTION Low / Ghost+Chevron 등 \| 일관된 이동 표현 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0431 | 9.5 | \| 이미지 선택·구성 이동 \| Interactive Surface·C-IMAGE와 행동 계약 \| 실제 클릭 기능이 있을 때만 타깃·피드백 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0432 | 9.5 | \| 기여 중·참여 인원·완료 상태 \| Badge 또는 일반 메타 \| 행동이 없으면 버튼처럼 꾸미지 않음 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0433 | 9.5 | 당시 지역 스타일 예시는 `.clan-more`, `.clan-reward-cta`, `.clan-reward-items-head a`, `.clan-inline-invite`, `.clan-btn`이다. 같은 역할·강조도·상태는 공통 Variant를 기본으로 쓴다. 차이가 필요하면 근거를 남긴다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | patterns/action-layout.md#현행 계약 | P2 | history |
| L0434 | 9.5 | 불필요한 홍보 카피를 늘리지 않고 용어·데이터 의미를 고정한다. `F-TYPE/C-TEXT` 역할, `F-COLOR` 의미색, `C-STATUS` 두께·색, `C-ACTION` Variant, `C-IMAGE` 용도를 공유한다. **보상 오브젝트의 금색은 주변 버튼·Progress·표면의 의미색과 같지 않다.** 아트의 물성을 전역 UI 강조색으로 무단 확장하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | patterns/action-layout.md#현행 계약 | P2 | proposed |
| L0435 | 10.2 | \| 범주 \| 조건·확인 항목 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0436 | 10.2 | \| 화면 폭 \| 원문 공통 320/360/390/430 CSS px, S3 비교 재현 시 355px도 추가 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 320 | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0437 | 10.2 | \| 화면 폭 \| 원문 공통 320/360/390/430 CSS px, S3 비교 재현 시 355px도 추가 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 360 | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0438 | 10.2 | \| 화면 폭 \| 원문 공통 320/360/390/430 CSS px, S3 비교 재현 시 355px도 추가 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 390 | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0439 | 10.2 | \| 화면 폭 \| 원문 공통 320/360/390/430 CSS px, S3 비교 재현 시 355px도 추가 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 430 | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0440 | 10.2 | \| 화면 폭 \| 원문 공통 320/360/390/430 CSS px, S3 비교 재현 시 355px도 추가 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 355px | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0441 | 10.2 | \| 테마 \| 라이트·다크, 승인 토큰과 실험값 구분 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0442 | 10.2 | \| 글자·콘텐츠 \| 텍스트 200% 확대, 긴 제목·2줄 이상 아이템명·큰 수치·배지 줄바꿈·최종 폰트 크기 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 200% | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0443 | 10.2 | \| 글자·콘텐츠 \| 텍스트 200% 확대, 긴 제목·2줄 이상 아이템명·큰 수치·배지 줄바꿈·최종 폰트 크기 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0444 | 10.2 | \| 첫 시선 \| 첫 2초에 핵심 내용 인지, 텍스트·수치·아트의 주연 식별 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0445 | 10.2 | \| 정보 중복 \| 제목·설명·Badge·CTA·미리보기에서 같은 내용·목적지 반복 여부 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0446 | 10.2 | \| 공간 \| 보상·진열이 사람·활동을 밀어내는지, 면적에 정보/행동 가치가 있는지 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0447 | 10.2 | \| Surface \| 배경·테두리를 제거해도 의미 유지 시 삭제 가능한지, 중첩·동일 카드 반복 여부 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0448 | 10.2 | \| 타이포 \| 큰 제목 경쟁, 작은 글자+과한 굵기, 실제 10px 미만 렌더 등 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10px | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0449 | 10.2 | \| 이미지 \| A1/A2/A3 목적, contain/cover·축소·크롭·안전영역·Overlay 충돌 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0450 | 10.2 | \| Progress \| 데이터 수치와 채움 길이·두께·색·상태 일치 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0451 | 10.2 | \| 조작 \| 타일·이미지·버튼의 클릭/비클릭 구별, 실제 목적지·로딩·실패·포커스·터치 영역 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0452 | 10.2 | \| 헤더 \| x축 기준선·Glyph와 Hit Target 중심·Root/Standard 역할·긴 제목·우측 슬롯 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0453 | 10.2 | \| 탐색 \| Tabs↔Panel·Filter↔List·Segment↔같은 데이터 연결, Root/Detail 전환·Back 이력 복원 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0454 | 10.2 | \| Shell \| Safe Area·고정 하단바·스크롤 복귀·키보드·root 글자 확대 충돌 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0455 | 10.2 | \| 회귀 \| 클랜 깃발·마스코트·장비·Avatar·승인 SVG·상하단 내비·기존 스크롤/상호작용 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | history |
| L0456 | 10.2 | \| 비교 근거 \| 승인 레퍼런스와 실제 모바일 렌더를 나란히 비교, 확인 범위·차이·미확인 기록 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0457 | 10.3 | \| 사례 \| 부족한 판정 \| 필요한 판정 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0458 | 10.3 | \| 상자 Hero \| 높이를 줄였다 \| 목적 분리·진행 가독성·활동 노출·아트 적합성·행동 기대까지 확인 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0459 | 10.3 | \| 아이템 \| 이미지·가격을 추가했다 \| 맥락에 필요한 슬롯·긴 이름·상태·행동이 일관된 객체로 읽힘 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0460 | 10.3 | \| Progress \| CSS에 62%가 있다 \| `620/1000`과 실제 채움 일치 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 62% | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0461 | 10.3 | \| Progress \| CSS에 62%가 있다 \| `620/1000`과 실제 채움 일치 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 620 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0462 | 10.3 | \| Progress \| CSS에 62%가 있다 \| `620/1000`과 실제 채움 일치 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1000 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0463 | 10.3 | \| 헤더 \| 아이콘 위치가 예뻐졌다 \| 보이는 중심과 실제 타깃·본문 시작선·긴 제목 정합 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0464 | 10.3 | \| Tabs \| 선택색이 바뀐다 \| 해당 패널이 바뀌고 상세 이동과 구별됨 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0465 | 10.3 | \| SEED 개정 \| 문서를 더 썼다 \| 반복 실패를 판정할 조건·반례·관계·검증 기준이 생김 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 반례 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0466 | 10.3 | \| PCP \| 7단계 양식이 있다 \| 판단을 생략하지 않고 실패 단계로 복귀한 근거가 남음 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 7단계 | 반례 | VALIDATION.md#현행 계약 | P1 | proposed |
| L0467 | A.1 | S5의 조건: CSS viewport 355px / 브라우저 기본 글자 16px 가정. | 355/16 | 측정/수치 355px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0468 | A.1 | S5의 조건: CSS viewport 355px / 브라우저 기본 글자 16px 가정. | 355/16 | 측정/수치 16px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0469 | A.1 | \| 속성 \| 당근 공개 규격으로 인용된 값 \| 현생패치 당시 코드 환산 \| | 355/16 | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0470 | A.1 | \| Android Header \| 높이 56px \| 약 58.8px \| | 355/16 | 측정/수치 56px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0471 | A.1 | \| Android Header \| 높이 56px \| 약 58.8px \| | 355/16 | 측정/수치 58.8px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0472 | A.1 | \| iOS Header \| 높이 44px \| 플랫폼 분리 없음 \| | 355/16 | 측정/수치 44px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0473 | A.1 | \| Root 좌우 Padding \| 16px \| 약 12.4px \| | 355/16 | 측정/수치 16px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0474 | A.1 | \| Root 좌우 Padding \| 16px \| 약 12.4px \| | 355/16 | 측정/수치 12.4px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0475 | A.1 | \| Root Title \| 18px / 24px / 700 \| 약 21.4px / 줄높이 상속 / 850 \| | 355/16 | 측정/수치 18px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0476 | A.1 | \| Root Title \| 18px / 24px / 700 \| 약 21.4px / 줄높이 상속 / 850 \| | 355/16 | 측정/수치 24px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0477 | A.1 | \| Root Title \| 18px / 24px / 700 \| 약 21.4px / 줄높이 상속 / 850 \| | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0478 | A.1 | \| Root Title \| 18px / 24px / 700 \| 약 21.4px / 줄높이 상속 / 850 \| | 355/16 | 측정/수치 21.4px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0479 | A.1 | \| Root Title \| 18px / 24px / 700 \| 약 21.4px / 줄높이 상속 / 850 \| | 355/16 | 측정/수치 850 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0480 | A.1 | \| Root 자간 \| 별도 지정 없음 \| -0.05em \| | 355/16 | 측정/수치 0.05 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0481 | A.1 | \| Standard Title \| 18 / 24 / 700 \| 약 14.8px / 850 \| | 355/16 | 측정/수치 18 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0482 | A.1 | \| Standard Title \| 18 / 24 / 700 \| 약 14.8px / 850 \| | 355/16 | 측정/수치 24 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0483 | A.1 | \| Standard Title \| 18 / 24 / 700 \| 약 14.8px / 850 \| | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0484 | A.1 | \| Standard Title \| 18 / 24 / 700 \| 약 14.8px / 850 \| | 355/16 | 측정/수치 14.8px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0485 | A.1 | \| Standard Title \| 18 / 24 / 700 \| 약 14.8px / 850 \| | 355/16 | 측정/수치 850 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0486 | A.1 | \| 부제형 Title \| 16 / 22 / 700 \| 별도 계약 없음 \| | 355/16 | 측정/수치 16 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0487 | A.1 | \| 부제형 Title \| 16 / 22 / 700 \| 별도 계약 없음 \| | 355/16 | 측정/수치 22 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0488 | A.1 | \| 부제형 Title \| 16 / 22 / 700 \| 별도 계약 없음 \| | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0489 | A.1 | \| Subtitle \| 12 / 16 / 400 \| 별도 계약 없음 \| | 355/16 | 측정/수치 12 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0490 | A.1 | \| Subtitle \| 12 / 16 / 400 \| 별도 계약 없음 \| | 355/16 | 측정/수치 16 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0491 | A.1 | \| Subtitle \| 12 / 16 / 400 \| 별도 계약 없음 \| | 355/16 | 측정/수치 400 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0492 | A.1 | \| Glyph \| 별도 아이콘 규격 \| 약 24.7px \| | 355/16 | 측정/수치 24.7px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0493 | A.1 | \| 아이콘 버튼 Slot \| 조합별 Slot \| 약 49.5×49.5px \| | 355/16 | 측정/수치 49.5 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0494 | A.1 | \| 아이콘 버튼 Slot \| 조합별 Slot \| 약 49.5×49.5px \| | 355/16 | 측정/수치 49.5px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0495 | A.1 | Left/Title은 페이지 정체성·선택 동작, Flexible은 가변 공간, Right Slot은 현재 화면 보조 액션을 담당한다. 긴 제목이 Right Slot을 침범하지 않게 분리한다. Root는 텍스트 또는 실제 선택 가능한 제목, Standard는 Back/Close+중앙 Title을 사용한다. | 355/16 | 문장/사례 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0496 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 26px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0497 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0498 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 60px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0499 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 18px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0500 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0501 | A.1 | 별도 제안인 현생패치 `F-TYPE` Root Title 26px/700, `F-GEOMETRY` Top Bar 최소 60px은 당근 18px/700·56px과 다르다. 수치 차이가 곧 오류는 아니다. 브랜드 비례로 의도됐는지 검증되지 않은 확대인지 판단해야 하며, 제안값을 승인값으로 바꾸지 않는다. | 355/16 | 측정/수치 56px | 검토 | foundations/geometry.md#현행 계약 | P2 | candidate |
| L0502 | A.2 | \| 항목 \| 원문 인용값 \| 해석 \| | 355/16 | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0503 | A.2 | \| `global-gutter` \| 16px \| 헤더·일반 콘텐츠 시작선 \| | 355/16 | 측정/수치 16px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0504 | A.2 | \| `nav-to-title` \| 20px \| Nav 아래 별도 Page Title이 있을 때. 모든 헤더 아래 고정 간격 아님 \| | 355/16 | 측정/수치 20px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0505 | A.2 | \| `component-default` \| 12px \| 별도 계약 없는 컴포넌트 사이 \| | 355/16 | 측정/수치 12px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0506 | A.2 | \| `between-text` \| 6px \| 같은 정보 그룹 텍스트 사이 \| | 355/16 | 측정/수치 6px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0507 | A.2 | \| `between-chips` \| 8px \| Chip 사이 \| | 355/16 | 측정/수치 8px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0508 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 44 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0509 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 16 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0510 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 22 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0511 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 700 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0512 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 49.5 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0513 | A.2 | \| Medium Line Tabs \| 높이 44, Label 16/22, 700 \| 현생패치 당시 높이 약 49.5, Label 13.4, 줄높이 별도 없음 \| | 355/16 | 측정/수치 13.4 | 검토 | foundations/geometry.md#현행 계약 | P2 | history |
| L0514 | A.2 | \| Tab 패딩 \| 좌우 10px \| 현생패치 약 2.3px, Tab 간격 약 26.4px \| | 355/16 | 측정/수치 10px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0515 | A.2 | \| Tab 패딩 \| 좌우 10px \| 현생패치 약 2.3px, Tab 간격 약 26.4px \| | 355/16 | 측정/수치 2.3px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0516 | A.2 | \| Tab 패딩 \| 좌우 10px \| 현생패치 약 2.3px, Tab 간격 약 26.4px \| | 355/16 | 측정/수치 26.4px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0517 | A.2 | \| Tab Indicator·하단 Stroke \| 2px·1px \| 현생패치도 각각 2px·1px \| | 355/16 | 측정/수치 2px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0518 | A.2 | \| Tab Indicator·하단 Stroke \| 2px·1px \| 현생패치도 각각 2px·1px \| | 355/16 | 측정/수치 1px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0519 | A.2 | \| Tab Indicator·하단 Stroke \| 2px·1px \| 현생패치도 각각 2px·1px \| | 355/16 | 측정/수치 2px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0520 | A.2 | \| Tab Indicator·하단 Stroke \| 2px·1px \| 현생패치도 각각 2px·1px \| | 355/16 | 측정/수치 1px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0521 | A.2 | \| Small Chip \| 높이 32 / 가로 Padding 6px \| 역할에 맞는 크기 선택 \| | 355/16 | 측정/수치 32 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0522 | A.2 | \| Small Chip \| 높이 32 / 가로 Padding 6px \| 역할에 맞는 크기 선택 \| | 355/16 | 측정/수치 6px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0523 | A.2 | \| Medium Chip \| 높이 36 / 가로 Padding 8px \| 동일 \| | 355/16 | 측정/수치 36 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0524 | A.2 | \| Medium Chip \| 높이 36 / 가로 Padding 8px \| 동일 \| | 355/16 | 측정/수치 8px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0525 | A.2 | \| Large Chip \| 높이 40 / 가로 Padding 10px \| 동일 \| | 355/16 | 측정/수치 40 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0526 | A.2 | \| Large Chip \| 높이 40 / 가로 Padding 10px \| 동일 \| | 355/16 | 측정/수치 10px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0527 | A.2 | \| Chip Label·Group Gap \| 14/19px·8px \| 텍스트·그룹 관계 \| | 355/16 | 측정/수치 14 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0528 | A.2 | \| Chip Label·Group Gap \| 14/19px·8px \| 텍스트·그룹 관계 \| | 355/16 | 측정/수치 19px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0529 | A.2 | \| Chip Label·Group Gap \| 14/19px·8px \| 텍스트·그룹 관계 \| | 355/16 | 측정/수치 8px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0530 | A.2 | \| List 가로·세로 Padding \| 16px·12px \| 반복 행의 공통 슬롯 \| | 355/16 | 측정/수치 16px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0531 | A.2 | \| List 가로·세로 Padding \| 16px·12px \| 반복 행의 공통 슬롯 \| | 355/16 | 측정/수치 12px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0532 | A.2 | \| List Title \| 16/22px / 400 \| 반복 항목 제목 \| | 355/16 | 측정/수치 16 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0533 | A.2 | \| List Title \| 16/22px / 400 \| 반복 항목 제목 \| | 355/16 | 측정/수치 22px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0534 | A.2 | \| List Title \| 16/22px / 400 \| 반복 항목 제목 \| | 355/16 | 측정/수치 400 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0535 | A.2 | \| List Detail \| 13/18px / 400 \| 메타·설명 \| | 355/16 | 측정/수치 13 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0536 | A.2 | \| List Detail \| 13/18px / 400 \| 메타·설명 \| | 355/16 | 측정/수치 18px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0537 | A.2 | \| List Detail \| 13/18px / 400 \| 메타·설명 \| | 355/16 | 측정/수치 400 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0538 | A.2 | \| Title↔Detail Gap \| 2px \| 행 내부 관련성 \| | 355/16 | 측정/수치 2px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0539 | A.2 | \| Prefix 우측 Padding \| 12px \| 미디어와 본문 관계 \| | 355/16 | 측정/수치 12px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0540 | A.2 | \| Suffix Icon \| 18px \| 보조 이동/상태 슬롯 \| | 355/16 | 측정/수치 18px | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0541 | A.2 | 당근의 Header와 Filter Bar는 같은 탐색 영역처럼 이어지고 목록에서 콘텐츠 성격이 바뀌는 예시다. 여백은 단순 빈 공간이 아니라 **무엇이 무엇에 속하는지 보여주는 관계 신호**다. 같은 페이지의 헤더·일반 콘텐츠는 같은 좌우 기준선과 동일 계열 표면으로 연결하며 간격·구분선·콘텐츠 형태로 단위를 구분한다. | 355/16 | 문장/사례 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0542 | A.3 | \| 장치 \| 배치·책임 \| 현생패치 예시 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0543 | A.3 | \| Line Tabs \| 전환할 상위 콘텐츠 바로 위 \| 내 클랜의 멤버/기록/소식 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0544 | A.3 | \| Chip Tabs \| 실제 2차 범주가 있을 때 관련 콘텐츠 위 \| 필요 시 Line 아래 2단 탐색 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0545 | A.3 | \| Chip Tabs \| 실제 2차 범주가 있을 때 관련 콘텐츠 위 \| 필요 시 Line 아래 2단 탐색 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0546 | A.3 | \| Filter Bar+Chip \| 영향을 받는 결과 목록 바로 위 \| 공개 클랜 검색 결과 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0547 | A.3 | \| Segmented \| 같은 데이터의 수치·그래프 바로 위 \| 주간/월간 기록 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0548 | A.3 | \| Section Link \| 제목 우측, 큰 정보 영역으로 이동 \| 전체 보기/기록 상세 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0549 | A.3 | ‘내 클랜’ 예시는 Standard Nav → `느슨한 갓생단 / 조금씩이라도 함께 꾸준히 / 멤버 8명` 정체성 → 멤버/기록/소식 Tabs → 해당 패널이다. 멤버 패널의 설명용 데이터는 `이모리 · 1일 활동`, `밤산책 · 2일 활동`, `모찌 · 3일 활동`이었다. 정보·이름·기능의 최종 확정을 뜻하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 8명 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0550 | A.3 | ‘내 클랜’ 예시는 Standard Nav → `느슨한 갓생단 / 조금씩이라도 함께 꾸준히 / 멤버 8명` 정체성 → 멤버/기록/소식 Tabs → 해당 패널이다. 멤버 패널의 설명용 데이터는 `이모리 · 1일 활동`, `밤산책 · 2일 활동`, `모찌 · 3일 활동`이었다. 정보·이름·기능의 최종 확정을 뜻하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0551 | A.3 | ‘내 클랜’ 예시는 Standard Nav → `느슨한 갓생단 / 조금씩이라도 함께 꾸준히 / 멤버 8명` 정체성 → 멤버/기록/소식 Tabs → 해당 패널이다. 멤버 패널의 설명용 데이터는 `이모리 · 1일 활동`, `밤산책 · 2일 활동`, `모찌 · 3일 활동`이었다. 정보·이름·기능의 최종 확정을 뜻하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0552 | A.3 | ‘내 클랜’ 예시는 Standard Nav → `느슨한 갓생단 / 조금씩이라도 함께 꾸준히 / 멤버 8명` 정체성 → 멤버/기록/소식 Tabs → 해당 패널이다. 멤버 패널의 설명용 데이터는 `이모리 · 1일 활동`, `밤산책 · 2일 활동`, `모찌 · 3일 활동`이었다. 정보·이름·기능의 최종 확정을 뜻하지 않는다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0553 | A.3 | 공개 클랜 발견 예시는 `전체 / 공부 / 습관 / 운동` 조건과 바로 아래 결과 목록이다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0554 | A.3 | \| 설명용 클랜 \| 분류·규모 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0555 | A.3 | \| 갓생 스터디단 \| 공부·멤버 24명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 24명 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0556 | A.3 | \| 매일 1% 더 \| 습관·멤버 18명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1% | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0557 | A.3 | \| 매일 1% 더 \| 습관·멤버 18명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 18명 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0558 | A.3 | \| 새벽 루틴 모임 \| 생활·멤버 12명 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 12명 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0559 | A.3 | 필터는 실제 결과를 바꿔야 하며 클랜 홈에 이 기능을 새로 추가하라는 확정 지시가 아니다. 3열 Reward Grid는 물체 진열에, 투명 배경 Flat List는 사람·클랜·활동에 우선 검토한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | foundations/geometry.md#현행 계약 | P2 | proposed |
| L0560 | A.4 | \| 계층 \| 원문 역할명·예시 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | history |
| L0561 | A.4 | \| Global 0 \| `layer-basement`, 전체 페이지 바닥 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0562 | A.4 | \| Global 1 \| `layer-default`, Top Nav/List/Tabs가 놓인 페이지 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0563 | A.4 | \| Global 2 \| Bottom Sheet/Menu Sheet \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0564 | A.4 | \| Global 3 \| Alert Dialog 등 최상위 Modal \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0565 | A.4 | \| Local 1 \| List/Tabs/Callout 기본 콘텐츠 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0566 | A.4 | \| Local 2 \| Floating Action \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0567 | A.4 | \| Local 3 \| Snackbar 등 일시 피드백 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 3 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0568 | A.4 | 원문 당근 설명에서 Elevation은 Surface Color+Shadow+Stroke를 조합하고 다크의 높은 계층은 밝아지는 규칙을 쓴다. 다크에서는 그림자 식별이 어려워 필요한 요소에 제한적으로 사용한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | history |
| L0569 | A.4 | \| Shadow \| 인용값 \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 비교표 원문 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | history |
| L0570 | A.4 | \| `shadow.s1` \| `0 1px 4px #00000014` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0571 | A.4 | \| `shadow.s1` \| `0 1px 4px #00000014` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 1px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0572 | A.4 | \| `shadow.s1` \| `0 1px 4px #00000014` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0573 | A.4 | \| `shadow.s1` \| `0 1px 4px #00000014` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 00000014 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0574 | A.4 | \| `shadow.s2` \| `0 2px 10px #0000001A` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0575 | A.4 | \| `shadow.s2` \| `0 2px 10px #0000001A` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 2px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0576 | A.4 | \| `shadow.s2` \| `0 2px 10px #0000001A` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 10px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0577 | A.4 | \| `shadow.s2` \| `0 2px 10px #0000001A` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0000001 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0578 | A.4 | \| `shadow.s3` \| `0 4px 16px #0000001F` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0579 | A.4 | \| `shadow.s3` \| `0 4px 16px #0000001F` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 4px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0580 | A.4 | \| `shadow.s3` \| `0 4px 16px #0000001F` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 16px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0581 | A.4 | \| `shadow.s3` \| `0 4px 16px #0000001F` \| | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 0000001 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0582 | A.4 | 이는 현생패치에 그대로 추가할 토큰 목록이 아니다. Reward Hero의 특별 표면과 일반 클랜·멤버·활동의 기본 표면처럼 **계층의 의미를 먼저 정의**한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 문장/사례 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | proposed |
| L0583 | A.4 | 원문 `app.js` 분석은 `#clan`이 Root Nav를 유지하고 `#clan-my`, `#clan-record` 등은 Standard Header+하단바 숨김으로 동작한다고 기록한다. `S-PAGE` Root/Detail과 대체로 맞지만 Back이 실제 이력을 일관되게 복원하는지는 별도 테스트 대상이다. 하단바 약 65.2px+Safe Area와 76px 제안값은 7.13절처럼 구분한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 65.2px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |
| L0584 | A.4 | 원문 `app.js` 분석은 `#clan`이 Root Nav를 유지하고 `#clan-my`, `#clan-record` 등은 Standard Header+하단바 숨김으로 동작한다고 기록한다. `S-PAGE` Root/Detail과 대체로 맞지만 Back이 실제 이력을 일관되게 복원하는지는 별도 테스트 대상이다. 하단바 약 65.2px+Safe Area와 76px 제안값은 7.13절처럼 구분한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 76px | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |
| L0585 | A.4 | 원문 `app.js` 분석은 `#clan`이 Root Nav를 유지하고 `#clan-my`, `#clan-record` 등은 Standard Header+하단바 숨김으로 동작한다고 기록한다. `S-PAGE` Root/Detail과 대체로 맞지만 Back이 실제 이력을 일관되게 복원하는지는 별도 테스트 대상이다. 하단바 약 65.2px+Safe Area와 76px 제안값은 7.13절처럼 구분한다. | 미명시(390/17·355/16.5·355/16 혼용 금지) | 측정/수치 7.13 | 검토 | components/bottom-navigation.md#현행 계약 | P2 | candidate |

## 이력

- 2026-11-09 P0-3: 통합 보고서 47개 지정 절(부록 A 전체 표 포함)에서 원문 줄을 보존해 장부를 생성했다. 실측과 시각 승인, P1/P2 반영 여부는 별도 게이트다.
