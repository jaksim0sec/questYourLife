# SEED 컴포넌트 커버리지 / 갓생패치 적용 판단

> 기준: [SEED 공식 컴포넌트 카탈로그](https://seed-design.io/components). 이름을 복제한다고 자동으로 필요해지는 것은 아님. `covered` = 명세가 있음; `split` = 기존 뭉친 명세를 독립화; `later` = 실제 기능 확인 시; `out` = 사용성 또는 플랫폼 범위에서 불필요.

| SEED 종류 | 갓생패치 대응 | 판정·근거 |
|---|---|---|
| Action Button | C-ACTION | covered/refined. 강조·크기·상태 개선 |
| Input Button / Icon Button | C-ACTION / C-ICON | split. 역할/접근성 다름 |
| Bottom Navigation | C-BOTTOM | split/proposed. 탭 목적지 미확정 |
| Top Navigation | NAV-TOP | approved, 기존 D-011 정본 |
| Tabs / Segmented Control | C-TABS / C-SEGMENT | split. 탐색과 보기 전환 분리 |
| Chip / Tag Group | C-SELECTION / C-STATUS | covered; 실제 필터·태그 사용 시 |
| Checkbox / Radio / Switch | C-CHECK / C-RADIO / C-SELECTION | covered/split. 완료·선택·설정의 의미 구별 |
| Avatar | C-AVATAR | split. 투명 캐릭터와 사진 프레임 분리 |
| Image Frame | C-IMAGE | split. contain/cover·폴백 |
| List / Divider / Footer | C-LIST / F-GEOMETRY / S-PAGE | covered. 리스트 중첩 카드 개선 |
| Badge / Notification Badge | C-STATUS / C-BOTTOM | covered. 내용 vs 알림 의미 구분 |
| Progress Circle / Progress Bar | C-STATUS / P-STATES | covered; 상태·로딩과 진행 구별 |
| Field / Text Input / Textarea | C-FIELD | covered. 실제 계정/목표 화면에서 보강 |
| Select / Select Box | C-FIELD + C-SELECTION | later. 실제 항목 선택·접근성 계약 필요 시 분리 |
| Date Picker / Time Picker | C-FIELD + S-PAGE | later. 실제 예약 일정 작업에서만 |
| Alert Dialog / Dialog | C-OVERLAY | covered (bundled), 실제 확인 플로우 전 독립 계약 필요성 판단 |
| Bottom Sheet / Menu Sheet / Menu | C-SHEET / C-OVERLAY | split. Bottom Sheet Snap·Footer·Gesture 별도 정본 |
| Snackbar / Callout / Result Section | C-OVERLAY / P-STATES | covered (bundled). 상태 전환부터 개선 |
| Skeleton / Content Placeholder / Identity Placeholder | C-OVERLAY / P-STATES | covered (bundled); 로딩 위치별 조건 필요 |
| Page Banner | C-SURFACE / A-ART | covered. 과한 UI 스타일 전염 금지 |
| Floating Action Button / Contextual FAB | C-ACTION / S-PAGE | later. 고정 CTA와 하단바 충돌 해결 후 |
| Accordion | C-SURFACE / C-ACTION | later. 실제 접고 펼칠 콘텐츠 필요 시 |
| Attachment Input | C-FIELD / C-IMAGE | later. 실제 업로드 플로우 필요 시 |
| Reaction Button | C-ACTION / C-SELECTION | later. 소셜 반응 상태 정책 확정 후 |
| Slider / Quantity Picker / Wheel Picker | C-SELECTION / C-FIELD | later. 실제 기능의 값 연속 조절 필요 시 |
| Scroll Fog | S-PAGE | later. 수평 스크롤 표시 정당성 확인 후 |
| Pagination / Table Pagination / Side Navigation / Side Panel | — | out. 이번 모바일-first 시드에는 불필요 |
| Manner Temp & Manner Temp Badge | — | out. 당근 고유 도메인 기능 |
| Help Bubble | C-OVERLAY | later. 별도 상태·레이블 필요 시 |

**문서 수 목표 없음.** 새로운 화면을 만들기 전에 INDEX에서 이미 있는 컴포넌트와 조합으로 충분한지 판단. 미흡한 기존 정의를 Refine/Merge로 해결한 뒤에만 Add. 독립된 동작 계약을 증명하지 못하면 통합 문서 그대로 둔다.

## v0.6 추가 정본
C-CREDIT(실제 코인 이미지+숫자), C-RARITY(등급별 미세 그라데이션), C-TEXT(5개 텍스트 배치 역할), C-SVG(Outline/Fill 자산 별개), C-GESTURE(명시적 제스처 계약), C-SHEET(3단 snap), C-RADIO(라디오 그룹), P-SHEET(시트 Footer/본문 조합), P-BTN-ICON(버튼+SVG 위치 안정성). 새 기능의 추가가 아니라 기존 디자인 시연 재사용 규칙의 보완임.
