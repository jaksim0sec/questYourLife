# 갓생패치 디자인 시드 재정렬 · 설계안

> 상태: proposed / not approved. 날짜: 2026-11-05. 현재 목표는 설계와 실행 순서 확정이며 UI 구현·기존 시드 값 변경을 승인한 문서가 아니다.
> 적용 저장소: jaksim0sec/questYourLife, 문서 전용 new 브랜치.
> 독해 순서: docs/AGENTS.md → docs/DECISIONS.md → docs/OPEN.md → docs/product/README.md → docs/design/seed/INDEX.md → 필요한 정본만.
> 외부 근거: https://seed-design.io/components / https://seed-design.io/components/action-button / https://seed-design.io/components/list / https://seed-design.io/components/top-navigation / https://seed-design.io/components/bottom-navigation / https://seed-design.io/patterns/loading / https://tossmini-docs.toss.im/tds-mobile/

## 1. 목적과 원점

제품 원칙은 사용자의 표현을 보존한다. “내 인생이 공략 대상이자 게임임”, “퀘스트는 수단임. 단, 동시에 이 앱의 본질임”, “궁극적으로 내 마이페이지와 내 마스코트를 내가 원하는대로 가꾸는것이 재밌는것”은 하나를 지우고 요약할 수 없는 세 가지 축이다. 현실 목표→실제 퀘스트 수행→캐시·상자·장식 등의 인앱 보상→마스코트/마이페이지와 소셜 관계로 이어지는 경험을 만든다. 퀘스트북 공유·참여·수정·탐색·커뮤니티는 사용자 생성 트렌드를 위한 콘텐츠 층이다.

**진짜 목적:** 컨셉 이미지에 나온 각 화면을 복제하는 게 아니라, 다른 에이전트가 새 화면을 만들 때도 동일한 시각 문법, 계층, 아이콘, 컴포넌트 상태, 조합 규칙을 재현하도록 재사용 가능한 정본을 만들고 실제 웹에서 검증한다.

**유지:** 기존 Android/DB/Auth/API/에셋, main, 문서 전용 new의 범위, 제품 원문, 확정 D-011 Root/Standard Top Navigation, 현재 F-* 토큰 소유 구조와 INDEX의 선택적 독해 방식. 이름/숫자/마이페이지/클랜/보상 세부 내용 자체는 이미지를 근거로 승인하지 않는다.

**금지:** 복제한 화면을 컴포넌트 정본으로 간주하기, 불필요하게 카드·파일·새 시스템 증가, 전역적 그라데이션/3D/금속/글래스, 모든 버튼에 아이콘 강제, 대형 마스코트/과한 보조 헤드라인, 강제 공개·감시, XP·보석·유료 패스·보스 HP·가상 별도 게임·확률형 구매 구조의 무단 추가. 가상의 컨셉을 현행 기능으로 보고하지 않는다. 본 디자인 산출물에 대화체·작업 지시용 욕설을 섞지 않는다.

## 2. 이 설계에서 검증한 현재 상태

- new 브랜치는 문서만 있고, docs/design/seed 아래에 INDEX / F-* 6 / C-* 7 / P-* 3 / S-PAGE / A-ART / References / Validation이 있다.
- 현재 컴포넌트 수를 파일 수로 계산하면 안 된다. 한 파일에 여러 서로 다른 컨트롤이 섞여 있어 검색과 상태 정의가 부족한 것이 문제다.
- P-ACTIONS는 선택의 관계·반응형 분기 규칙이 약하고, P-STATES는 상태 목록 중심이며, P-HIERARCHY는 실제 조합 검증이 부족하다.
- D-011 Top Navigation의 좌측 Root 타이틀·우측 Outline 액션, Standard Back/Close + 중앙 제목은 승인. 하단 탭 수·위치·활성형·전역 다크 테마·정확 치수는 미결.
- 기존 reference REF-005 01의 “50:50” 교정 의미가 '기계적인 50:50 금지'로 잘못 일반화되어 있다. **그 특정 하단 두 버튼의 균등한 너비와 정렬**이 원래 교정 의도다. 같은 너비는 전역 규칙이 아니며 의미상 비대칭인 별도 사례에서는 다른 비율이 가능하다.
- 아바타는 원형 흰 배경을 반드시 갖지 않는다. 투명 배경 캐릭터 자체가 기본 표현 후보. 모자·귀·소품 crop 금지.
- 제안 토큰은 정량화되어 있으나 실제 컴포넌트 렌더·기기 접근성 검증 전이다. approved로 올리지 않는다.

## 3. 8개 입력 이미지의 순차 분석

현재 대화 업로드 파일은 저장소의 바이너리로 등록된 것이 아니다. ID는 이 설계서 내부에만 사용하며 기존 REF-001~007을 재배정하지 않는다.

| ID | 파일 (크기) | 직접 보이는 역할 | 통일/분리/교정 판정 | 채택하지 않을 것 |
|---|---|---|---|---|
| S2-01 | 11059.jpg (709×1536) | 4단계 버튼 강조 시안, 밝은 색·Weak·문자 중심 계층 | C-ACTION 단일 family로 흡수. emphasis/tone/treatment/size를 독립시켜 원래 토큰으로 매핑. 시연용 수치는 제안 | 스크린샷의 검은 배경을 기본 앱 테마로 해석, 글꼴·버튼 치수 확정 |
| S2-02 | 11060.jpg (709×1536) | BackendX 광고의 마케팅 문구와 예시 제품 UI | 화면을 실제 사용 가능한 프로토타입으로 검증한다는 제작 접근법만 참고. 시드의 시각 근거로 사용하지 않음 | 광고 UI·카피·제품 기능·툴 사용을 갓생패치 요구로 가져오기 |
| S2-03 | 11054.jpg (709×1536) | 토스의 3D 중심 대형 프로모션, 단색 강한 CTA, 반복 중립 목록 | C-SURFACE의 Banner/Image Frame, C-ACTION의 강조, A-ART의 물성으로 분해 | 파랑을 브랜드색으로 승격, 금융/결제 기능, 실물 상품 구조 |
| S2-04 | 11053.png (986×1536) | 사용자가 지정한 나쁜 예 10건을 구조화한 보드 | 신규 스타일 원본이 아닌 회귀 검증 목록. 각 반례를 정본/패턴 테스트로 연결 | 요약 보드가 사용자 손글씨의 의미를 대체한다는 가정 |
| S2-05 | 11036.png (769×824) | 다크 발견형 홈 + 라이트 퀘스트 목록: 미디어 카드, 간결 진행, 리스트 | Image Frame/List/Tabs/Progress 공통 재료, 발견형 vs 실행형 밀도 Pattern. 두 테마는 후보로 공존 | 전역 다크 강제, 고정 '퀘스트북 만들기' 버튼과 Bottom Nav 무조건 동시 배치, 탭 수 복제 |
| S2-06 | 11019.png (1281×640) | 보라·금색 상자/코인/리본, 시즌 이벤트의 강한 연출 | A-ART에 hero/reveal 강도와 조명·스케일·안전 영역 관리. 일반 UI와 분리 | 전역 이벤트 UI, XP·보석·유료 패스·레이드·가격·랜덤 보상 시스템 추가 |
| S2-07 | 11024.png (1246×947) | 다크 콘텐츠 탐색/클랜 피드/퀘스트북 상세, 이미지와 텍스트 간 위계 | C-LIST/Image Frame/Badge/Tab 재사용, Social/Discovery/Detail composition. 커뮤니티 상호작용은 실제 정책 확인 필요 | 임의의 공개 범위·피드 데이터·중앙 + 탭·전역 형광/보라 |
| S2-08 | 11022.png (1422×1032) | 밝은 클랜/마이/꾸미기, 붉은 도장 마스코트, 현실 소품, 보상 타일 | **일상 UI의 우선 시각 근거**. Surface/Avatar/Collection/Asset Frame 등으로 분해. 캐릭터 크기·카드 밀도·버튼/아이콘 비율 교정 | 상세 화면 배치·내용·가격·탭 수의 그대로 복사 |

**증거 위계:** 사용자 직접 지시 > DECISIONS 확정 > 실제 자료의 관찰 > 모델 추론 > 설계 제안. 사진이 예쁘다는 판단과 모든 표면·동작이 승인됐다는 판단은 다르다. S2-04 원본에는 순서/설명 재편집이 들어 있으므로 원래 주석과 사용자의 후속 교정을 우선한다.

## 4. 나쁜 예 10건을 시스템 규칙으로 바꾸기

| 문제 | 근본 원인 | 소유 정본 / 검증 조건 |
|---|---|---|
| 01 하단 두 버튼 균등 너비·정렬 실패 | 쌍 버튼의 동일 컨테이너 그리드 부재 | P-ACTIONS: **해당 사례 50:50**, 공통 높이/베이스라인/라운딩. 다른 행동 관계까지 강제하지 않음 |
| 02 fixed CTA와 Bottom Navigation 경쟁 | 둘이 각각 하단 공간을 점유 | S-PAGE + P-ACTIONS: 공존은 명시적으로 허용한 때만, safe inset 한 번 |
| 03 이어하기만 튀는 형태 | 기능별 임의 variant | C-ACTION: role/size/tone/variant 정본 |
| 04 클랜 채팅 행이 다른 스타일 | 행에 대한 별도 디자인 | C-LIST: Prefix/Content/Suffix 슬롯, density 일치 |
| 05 편집·상단 SVG 이질감 | stroke/radius/광학 면적 불균형 | F-ICON-MOTION: round Outline, cap/join·24 viewBox·광학 밸런스 검수 |
| 06 마이의 아바타 과대 | 주목 우선순위/hero 남용 | F-TYPE + P-HIERARCHY: 아바타는 역할별 크기, 텍스트와 정보 균형, 투명 캐릭터 허용 |
| 07 수락/넘기기 억지 배치 | 의미상 선택 흐름과 최소 라벨 폭 무시 | P-ACTIONS: 같은 size, 줄바꿈/폭 부족 시 세로 전환; 일반적 수락·넘기기 위계 |
| 08 출석의 보조 인사말이 지나치게 큼 | 역할 기반 타이포 부재 | F-TYPE: 인사/장식은 보조, 주목도 최상위보다 크지 않음 |
| 09 Tabs/Segment/정렬의 밀집 | 서로 다른 탐색 층 혼동 | C-SELECTION + P-HIERARCHY: 각 영역과 영향 범위 분리 |
| 10 버튼 3종 비율·라운딩·내부 요소 불일치 | size/token 정본을 공유하지 않음 | C-ACTION + F-GEOMETRY: 같은 size는 외곽 크기·radius·내부 요소 비례 일치 |

## 5. 설계 방식의 선택

A. **SEED 카탈로그 전체 복제:** 누락은 줄지만 미사용 컴포넌트·문서가 과도하게 늘고 실제 앱에 불필요. 배제.
B. **기존 7개 문서에 문단만 추가:** 변경은 작지만 선택적 독해 및 독립 state/anatomy 문제가 계속됨. 배제.
C. **기존 정본 유지 + 필요한 항목만 독립화 + 실제 UI 검증:** 추천. 각 종류가 다른 사용 맥락·상태·API를 가질 때만 독립 컴포넌트 승격. 파일 수보다 사용 규칙의 명확함 우선.

## 6. 목표 시스템 구조와 변경 경계

- Foundation: 기존 F-PRINCIPLES, F-COLOR, F-TYPE, F-GEOMETRY, F-ICON-MOTION 소유 유지. 새 색·치수를 컴포넌트 파일마다 선언하지 않는다. 정확 값은 시각 실험 전 proposed.
- Component: 이미 존재하는 family를 먼저 쓰되 별도 anatomy/state가 필요한 Action Button, Icon Button, Tabs, Segmented, Chip, Checkbox/Radio/Switch, List Item, Avatar, Image Frame, Badge, Progress, Field, Sheet/Dialog 등을 각각 독립 spec 여부 검토한다. 항목이 비슷하다고 거대 만능 컴포넌트로 만들지 않는다.
- Pattern: 버튼 조합, 정보 밀도/섹션, 로딩·빈 상태·오류·오프라인·복구, 고정 CTA와 Safe Area, 탐색/검색/필터, 아트의 주목 강도는 **문제→적용 조건→컴포넌트 조합→전환 규칙→예외→금지→검증**으로 작성한다.
- Shell: Root/Standard의 D-011을 링크로만 재사용. Detail/Utility/Immersive/Overlay의 하단바 가시성/scroll/keyboard는 S-PAGE에서. Bottom Navigation은 별도 후보 정본으로 다루되 목적지·활성 스타일은 O-003까지 proposed.
- Asset Presentation: 일반 UI 면과 3D 물체의 재질을 분리. 마스코트는 투명 캐릭터·패션 아이템의 레이어 규칙과 아트 여백. 작은 타일 / 대표 배너 / 드문 획득 결과에서 차등 표현.

**추가 생성 판정:** 기존 컴포넌트의 슬롯/variant 조합으로 설명할 수 있으면 새 파일 생성 금지. 독립적인 인터랙션·상태 또는 접근성 계약이 있으면 신규 컴포넌트 후보. 여러 재료의 배치라면 Pattern. 특정 화면 콘텐츠는 페이지 fixture. 일러스트 자체는 Asset.

## 7. 명시적 미결·위험

- 기본 테마 Light/Dark, 정확한 레드·옐로우 HEX/폰트/라운딩·높이, 새로운 글래스 효과: O-005/O-006.
- 하단 탭 목적지/개수·중앙 +의 의미·상세 화면의 하단바 정책: O-003.
- 캐시 표시·아이템 상태/금액·클랜 공개·퀘스트북 참여/소셜 접근성: O-008/O-013~020.
- 50:50 문제 외 원본 교정 의도가 불명확한 경우, 판단하지 않은 내용을 approved로 만들지 않는다.
- 원본 업로드는 현재 대화에만 있어 다른 에이전트가 원본 픽셀을 재확인하려면 첨부 접근이나 별도 에셋 전달이 필요하다. SHA/파일명만으로 GitHub 바이너리 접근 가능하다고 주장하지 않는다.

## 8. 실제 승인 게이트

1. 디자인 시스템 INDEX에서 특정 역할(수락/넘기기, List, Header 등)을 2단계 내에 찾아 열 수 있어야 한다.
2. 서로 다른 페이지에서 같은 component/tone/size/state가 동일 외곽·텍스트·아이콘 비율을 유지해야 한다.
3. 320/360/390/430 폭, 긴 한글, 확대 글자, 다크/라이트, loading/error/empty에서 가려짐·잘림·겹침이 없어야 한다.
4. fixed CTA / Bottom Navigation / keyboard / safe-area 관계를 한 Shell이 소유하고 마지막 콘텐츠까지 접근 가능해야 한다.
5. 웹 플레이그라운드의 실제 렌더링과 시드 토큰/Variant가 맞아야 한다. 문서 검사 통과가 시각 합의를 대신하지 않는다.
6. 사용자 명시 승인 후에만 DECISIONS 업데이트 및 시드 approved 승격. main/Android/DB 변경은 이 단계 범위가 아니다.

선행 원칙: docs/design/SEED_AUTHORING_GUIDE.md를 변경하지 않고 이 설계에 적용한다. 기존 승인 TOP_NAVIGATION.md는 정본 유지.
