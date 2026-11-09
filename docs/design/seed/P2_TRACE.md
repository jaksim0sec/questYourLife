# SEED r3 P2 · LEDGER 전수 추적표

> **추적은 실행 결과가 아니다.** 원문 전부는 [LEDGER](LEDGER.md) L0001–L0585에 보존. 아래는 P2에 분류된 380행의 담당·판정 방향을 전부 연결한다. 출처에서 candidate/proposed였던 수치는 이 표로 승인되지 않는다.
>
> 실행된 브라우저 렌더 증거와 규칙 적합 판정은 CI 및 [P2_STATUS](P2_STATUS.md)에 별도 기록. P2 계약을 실제 페이지에 적용한 것은 아니며 P3 범위에 손대지 않는다.

## 담당 정본별 분포

| 담당 문서 | 원문 행 수 | 처리 기준 |
|---|---:|---|
| [foundations/typography.md](foundations/typography.md#현행-계약) | 66 | 수치 출처별 측정조건 구별, 의미 역할/12px/14미만 굵기 하한 유지, 후보 실측 대기 |
| [components/tabs.md](components/tabs.md#현행-계약) | 55 | Tabs와 Filter/Segment 구별, hit-area 연속/패널 연동 |
| [foundations/geometry.md](foundations/geometry.md#현행-계약) | 35 | 48×48 hit/단일 gutter/헤더 58.8·60·56 비교 |
| [components/bottom-navigation.md](components/bottom-navigation.md#현행-계약) | 33 | Page Type별 보임·숨김/높이후보/부모 활성 |
| [components/action-button.md](components/action-button.md#현행-계약) | 19 | 행동 역할→Variant, High 단일/34·43·48 표시/48 타깃 |
| [foundations/color-surfaces.md](foundations/color-surfaces.md#현행-계약) | 18 | 색상 비교 입력과 시연 팔레트 승인/후보 구분, 임의 HEX 확정 금지 |
| [objects/person-activity.md](objects/person-activity.md#현행-계약) | 18 | 사람·활동 슬롯/정렬/독립 응원과 +N |
| [components/badge-progress.md](components/badge-progress.md#현행-계약) | 17 | Badge 용도/두께 6·10·12/상태와 ARIA |
| [components/image-frame.md](components/image-frame.md#현행-계약) | 15 | 이미지 역할/크롭/텍스트 안전영역/상태 overlay 선택·반례 |
| [components/bottom-sheet.md](components/bottom-sheet.md#현행-계약) | 14 | Sheet/Page/Dialog 비교/보존·Snap 후보 |
| [components/selection-controls.md](components/selection-controls.md#현행-계약) | 9 | Chip/Filter/Tabs/Segment/Radio 사용·배제 구분 |
| [components/overlay-feedback.md](components/overlay-feedback.md#현행-계약) | 9 | 피드백 유형/성공 암시 금지/복구 행동 |
| [components/segmented-control.md](components/segmented-control.md#현행-계약) | 8 | 한 Track·한 Thumb·2~4 विकल्प, 페이지당 하나 |
| [patterns/action-layout.md](patterns/action-layout.md#현행-계약) | 8 | 대표 High/대등 50:50/주·부행동 대비 |
| [components/icon-button.md](components/icon-button.md#현행-계약) | 7 | Glyph 24/Target48 중심/Right slot 2권장 3최대 |
| [components/checkbox.md](components/checkbox.md#현행-계약) | 7 | 선택과 사용자 완료 명령/Pending·Confirmed·Failed 구분 |
| [components/text-hierarchy.md](components/text-hierarchy.md#현행-계약) | 7 | 5개 의미역할/Root·Standard 후보 구분 |
| [components/item-rarity-surface.md](components/item-rarity-surface.md#현행-계약) | 7 | 보상 미리보기 vs 꾸미기 상태/등급 과장 제한 |
| [components/list-item.md](components/list-item.md#현행-계약) | 6 | Flat List/align(center/top)/타깃 소유/경계 선택 |
| [components/surface-media.md](components/surface-media.md#현행-계약) | 4 | 10역할 의미·사용하지 않을 조건·중첩 반례 |
| [components/field.md](components/field.md#현행-계약) | 4 | Form/Field 역할과 라벨/오류 우선/입력 검증 |
| [components/avatar.md](components/avatar.md#현행-계약) | 3 | 투명 마스코트/Stack +N/장비 가림 |
| [components/credit-value.md](components/credit-value.md#현행-계약) | 3 | 가격/보유/예상/획득 구별, 원본 코인만 |
| [components/radio-choice.md](components/radio-choice.md#현행-계약) | 3 | 상호배타 선택·label·그룹/대안 Sheet |
| [components/gesture.md](components/gesture.md#현행-계약) | 3 | 제스처 선택 허용 조건/Cancel/키보드 대안 |
| [components/svg-icon.md](components/svg-icon.md#현행-계약) | 2 | 56개 원본/24 viewBox/1.8/활성 별도 Fill |

## P2 원문 행별 대응

| ID | 원문 절 | 소유 정본 | 종류 | P2 판정 경계 | 기존 상태 |
|---|---|---|---|---|---|
| L0005 | 3.3 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0006 | 3.3 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0007 | 3.3 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0024 | 3.3 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0031 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | history |
| L0032 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | history |
| L0033 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | history |
| L0034 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | history |
| L0035 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0036 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0037 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0038 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0039 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0040 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0041 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0042 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0043 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0044 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0045 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0046 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0047 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0048 | 4.1 | [foundations/color-surfaces.md](../seed/foundations/color-surfaces.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0058 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0059 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0060 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0061 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0062 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0063 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 후보 | 미승인 측정값은 후보 유지 | candidate |
| L0064 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0065 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0066 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0067 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0068 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0069 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0070 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0071 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0072 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0073 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0074 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0075 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0076 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0077 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0078 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0079 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0080 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0081 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0082 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0083 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0084 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0085 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0086 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0087 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0088 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0093 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0094 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0095 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0096 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0097 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0100 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0101 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0102 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0103 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0104 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0105 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0106 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0107 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0108 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0109 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0112 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0113 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0114 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0115 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0116 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0117 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0118 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0119 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0120 | 4.3 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 반례 | 반례 보존·기준 충돌 확인 | candidate |
| L0142 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0143 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0144 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0145 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0146 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0147 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0148 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0149 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0150 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0151 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0152 | 4.5 | [foundations/typography.md](../seed/foundations/typography.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0156 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0157 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0158 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0159 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0160 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0161 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0162 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0163 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0164 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0165 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0166 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0167 | 4.5 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0225 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0226 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0227 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0228 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0229 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0230 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0231 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0232 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0233 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0234 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0235 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0236 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0237 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0238 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0239 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0240 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0241 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0242 | 7.1 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0243 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0244 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0245 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0246 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0247 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0248 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0249 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0250 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0251 | 7.2 | [components/selection-controls.md](../seed/components/selection-controls.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0252 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0253 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0254 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 반례 | 후보 유지·시각 승인 별도 | candidate |
| L0255 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 반례 | 후보 유지·시각 승인 별도 | candidate |
| L0256 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 반례 | 후보 유지·시각 승인 별도 | candidate |
| L0257 | 7.3 | [components/list-item.md](../seed/components/list-item.md#현행-계약) | 반례 | 후보 유지·시각 승인 별도 | candidate |
| L0258 | 7.4 | [components/surface-media.md](../seed/components/surface-media.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0259 | 7.4 | [components/surface-media.md](../seed/components/surface-media.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0260 | 7.4 | [components/surface-media.md](../seed/components/surface-media.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0261 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0262 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0263 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0264 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0265 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0266 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0267 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0268 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0269 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0270 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0271 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0272 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0273 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0274 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0275 | 7.5 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0276 | 7.6 | [components/field.md](../seed/components/field.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0277 | 7.6 | [components/field.md](../seed/components/field.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0278 | 7.6 | [components/field.md](../seed/components/field.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0279 | 7.6 | [components/field.md](../seed/components/field.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0280 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0281 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0282 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0283 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0284 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0285 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0286 | 7.7 | [components/icon-button.md](../seed/components/icon-button.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0287 | 7.8 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0288 | 7.8 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0289 | 7.8 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0290 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0291 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0292 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0293 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0294 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0295 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0296 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0297 | 7.9 | [components/segmented-control.md](../seed/components/segmented-control.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0298 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0299 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0300 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0301 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0302 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0303 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0304 | 7.10 | [components/checkbox.md](../seed/components/checkbox.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0305 | 7.11 | [components/avatar.md](../seed/components/avatar.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0306 | 7.11 | [components/avatar.md](../seed/components/avatar.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0307 | 7.11 | [components/avatar.md](../seed/components/avatar.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0308 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0309 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0310 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0311 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0312 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0313 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0314 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0315 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0316 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0317 | 7.12 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0318 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0319 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0320 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0321 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0322 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0323 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0324 | 7.13 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0325 | 7.14 | [components/credit-value.md](../seed/components/credit-value.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0326 | 7.14 | [components/credit-value.md](../seed/components/credit-value.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0327 | 7.14 | [components/credit-value.md](../seed/components/credit-value.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0328 | 7.15 | [components/radio-choice.md](../seed/components/radio-choice.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0329 | 7.15 | [components/radio-choice.md](../seed/components/radio-choice.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0330 | 7.15 | [components/radio-choice.md](../seed/components/radio-choice.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0331 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0332 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0333 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0334 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0335 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0336 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0337 | 7.16 | [components/text-hierarchy.md](../seed/components/text-hierarchy.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0338 | 7.17 | [components/svg-icon.md](../seed/components/svg-icon.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0339 | 7.17 | [components/svg-icon.md](../seed/components/svg-icon.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0340 | 7.18 | [components/gesture.md](../seed/components/gesture.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0341 | 7.18 | [components/gesture.md](../seed/components/gesture.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0342 | 7.18 | [components/gesture.md](../seed/components/gesture.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0343 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0344 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0345 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0346 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0347 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0348 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0349 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0350 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0351 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0352 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0353 | 7.19 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0354 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0355 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0356 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0357 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0358 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0359 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0360 | 7.20 | [components/item-rarity-surface.md](../seed/components/item-rarity-surface.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0361 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0362 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0363 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0364 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0365 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0366 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0367 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0368 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0369 | 7.21 | [components/overlay-feedback.md](../seed/components/overlay-feedback.md#현행-계약) | 반례 | Don't 검출 기준 | history |
| L0415 | 9.3 | [components/surface-media.md](../seed/components/surface-media.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0416 | 9.3 | [components/image-frame.md](../seed/components/image-frame.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0417 | 9.3 | [components/action-button.md](../seed/components/action-button.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0419 | 9.3 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0420 | 9.3 | [components/badge-progress.md](../seed/components/badge-progress.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0421 | 9.3 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0422 | 9.3 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0423 | 9.3 | [components/bottom-sheet.md](../seed/components/bottom-sheet.md#현행-계약) | 반례 | Don't 검출 기준 | proposed |
| L0427 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0428 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0429 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0430 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0431 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0432 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0433 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0434 | 9.5 | [patterns/action-layout.md](../seed/patterns/action-layout.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0467 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0468 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0469 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | history |
| L0470 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0471 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0472 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0473 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0474 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0475 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0476 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0477 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0478 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0479 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0480 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0481 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0482 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0483 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0484 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0485 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0486 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0487 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0488 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0489 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0490 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0491 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0492 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0493 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0494 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0495 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | proposed |
| L0496 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0497 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0498 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0499 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0500 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0501 | A.1 | [foundations/geometry.md](../seed/foundations/geometry.md#현행-계약) | 검토 | 미승인 측정값은 후보 유지 | candidate |
| L0502 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0503 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0504 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0505 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0506 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0507 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0508 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0509 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0510 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0511 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0512 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0513 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0514 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0515 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0516 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0517 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0518 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0519 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0520 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0521 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0522 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0523 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0524 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0525 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0526 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0527 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0528 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0529 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0530 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0531 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0532 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0533 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0534 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0535 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0536 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0537 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0538 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0539 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0540 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0541 | A.2 | [components/tabs.md](../seed/components/tabs.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0542 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0543 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0544 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0545 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0546 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0547 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0548 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0549 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0550 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0551 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0552 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0553 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0554 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0555 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0556 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0557 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0558 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0559 | A.3 | [objects/person-activity.md](../seed/objects/person-activity.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0560 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0561 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0562 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0563 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0564 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0565 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0566 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0567 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0568 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0569 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | history |
| L0570 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0571 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0572 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0573 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0574 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0575 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0576 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0577 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0578 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0579 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0580 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0581 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0582 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 선택/관계·반례에 연결 | proposed |
| L0583 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0584 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |
| L0585 | A.4 | [components/bottom-navigation.md](../seed/components/bottom-navigation.md#현행-계약) | 검토 | 후보 유지·시각 승인 별도 | candidate |

## 검증 범위

- 21개 Component: Selection/Don't/Relations/Verification의 구체적 계약, 반례 2개 이상. 사용·배제·관계 규칙을 처음부터 검수한다.
- 21개 브라우저 Fixture: Normal/Don't 분리, 320/390/430 × light/dark에 실제 computed style·interactions를 측정한다.
- Foundation/Pattern/Object로 분류된 원문은 해당 정본의 소유를 보존. **측정 수치의 자동 채택이나 P3 화면 구현을 의미하지 않는다.**
