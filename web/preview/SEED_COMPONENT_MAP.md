# SEED 전수 구성·클랜 활용 매핑 (2026-11-08)

> 기준: `new/docs/design/seed/INDEX.md`, `implementation/v015.md`, `v015-tokens.json`, v0.14 공식 아이콘 manifest, 개별 Foundation·Component·Pattern·Shell·Art, AUDIT/COVERAGE/VALIDATION. **UI 스타일 정본과 실제 제품 기능/데이터 정책은 다름.**

## 1. SEED의 전체 역할 정의

- **Foundation 5개**: 디자인 철학, 테마/의미색, 텍스트, 여백/형태, 아이콘/모션. 토큰/사용 기준의 소유자.
- **Component 21개**: 각 요소의 anatomy, states, 의미, 크기, 접근성. *하나의 컴포넌트가 하나의 화면을 뜻하지 않음.*
- **Pattern 5개**: 컴포넌트 간 행동·정보 계층·상태/바텀시트·버튼 아이콘 결합.
- **Shell 1개 + 승인 TopNav 1개**: Root/Detail/Utility/Immersive/Overlay의 헤더·스크롤·Safe Area.
- **Art 1개**: A1 진열 / A2 콘텐츠 강조 / A3 획득·기념의 표현 강도. 별도의 새 재화 체계 아님.
- **Reference + Validation**: 이미지 출처·원본 보존, 검수·승인 범위 기록.
- **현재 우선 규격**: `v0.15` 승인 표의 항목만 옛 v0.13/v0.14 후보보다 우선. `INDEX.md`의 개별 proposed 상태가 실제 경제나 IA 승인으로 바뀌는 것은 아님.

## 2. 전 항목 ID·역할·실제 적용 (INDEX 36/36)

| ID | SEED 정본 이름 / 책임 | 이번 클랜 적용 | 문서 상태 |
|---|---|---|---|
| F-PRINCIPLES | 표현 철학·감성·게임성 | 사용: 일상 UI/보상 영역의 표현 분리 | proposed |
| F-COLOR | 의미색·표면·다크·대비 | 사용: surface·canvas 및 light/dark. 글로벌 브랜드색 조정은 보류 | proposed |
| F-TYPE | 글자 계층·한글·숫자 | 사용: 제목/메타/수치의 크기·굵기 분리 | proposed |
| F-GEOMETRY | 간격·padding·radius·크기 | 사용: 버튼 터치 48px, section 간격, 둥근 제어 | proposed |
| F-ICON-MOTION | Outline SVG·stroke·모션 | 사용: 56종 중 필요한 20종 원본 참조, 스케일/감소 모션 | proposed |
| C-ACTION | 버튼·CTA·텍스트 액션 | 사용: 생성·초대/상세/보상 행동 위계 | proposed |
| C-SELECTION | 선택 family 라우터·Chip·Radio·Switch | 사용: 시연 가입/미가입 상태 선택만 | proposed |
| C-LIST | List Item·Header·순위·행 | 사용: 소식·주간 기여·공개 랭킹 반복 행 | proposed |
| C-SURFACE | Surface·Card·Banner 공통 표면 | 사용: Plain section / Reward hero / Media preview | proposed |
| C-STATUS | Badge·Tag·Progress·값 | 사용: 주간 진행 10px·숫자·메타·Badge | proposed |
| C-FIELD | Field·Input·검색·Textarea | 사용: 클랜 만들기 이름/소개, single-border focus | proposed |
| C-ICON | Icon Button | 사용: 하트 응원·메뉴 기존 조작 | proposed |
| C-TABS | Tabs | 보류: 새 콘텐츠 탭 도입하지 않음 | proposed |
| C-SEGMENT | Segmented Control | 사용: 메뉴 내부 시연 상태 변경 단일 트랙 | proposed |
| C-CHECK | Checkbox | 비사용: 클랜은 수동 퀘스트 완료 화면 아님 | proposed |
| C-AVATAR | Avatar / Transparent Mascot | 사용: 원본 도장 + Android 장착 좌표 | proposed |
| C-IMAGE | Image Frame | 사용: 상자 contain·마스코트 contain | proposed |
| C-BOTTOM | Bottom Navigation (proposed) | 보존: 글로벌 책갈피 active Fill / inactive Outline | proposed |
| C-CREDIT | Credit Value | 비사용: 금액/Coin 실 데이터 부재 | proposed |
| C-RADIO | Radio Choice | 비사용: 생성 폼에 radio 결정 없음 | proposed |
| C-TEXT | Text Hierarchy | 사용: Display/Title/Section/Body/Caption 분리 | proposed |
| C-SVG | SVG Icon Contract | 사용: 원본 단독 SVG, draw/retrace 금지 | proposed |
| C-GESTURE | Gesture Contract | 보존: 기존 Bottom Sheet 제스처 담당 | proposed |
| C-SHEET | Bottom Sheet | 보존: 기존 메뉴 시트 재사용 | proposed |
| C-RARITY | Item Rarity Surface | 비사용: 실제 등급/장비 선택 아닌 업적 배지는 별도 | proposed |
| C-OVERLAY | Sheet·Dialog·Snackbar·Skeleton | 보존: 기존 메뉴 Overlay. 가짜 수령 Dialog 추가 안 함 | proposed |
| P-ACTIONS | 버튼 그룹·수락/거절·Bottom CTA | 사용: 주요 행동 하나/보조 행동, 자세한 레이아웃 | proposed |
| P-HIERARCHY | 콘텐츠 조합·비중·밀도 | 사용: 사람→보상→기여→소식→성취 순서 | proposed |
| P-STATES | 빈·오류·오프라인·대기·복구 | 사용: 미가입/가입 fixture 구분. 실제 지급 상태 미구현 | proposed |
| P-SHEET | Bottom Sheet Flow | 보존: 시연 상태 변경의 기존 시트 | proposed |
| P-BTN-ICON | Button + SVG Layout | 사용: 단일 inline 17px icon + Label | proposed |
| S-PAGE | Root·Detail·Utility·Overlay | 보존: Root/Detail + safe area | proposed |
| A-ART | 3D·보상·마스코트·광원 | 사용: 기존 상자 A2, 장비/마스코트 A1. 신규 A3 없음 | proposed |
| NAV-TOP | 확정 상단·Root/Standard | 보존: 승인 Root/Standard 헤더 슬롯 | approved |
| R-INPUTS | 원본·평가·나쁜 예·출처 | 참조: 레퍼런스와 컨셉은 기능 요건이 아님 | needs-validation |
| V-CHECKS | 검증·제안/미결·변경 영향 | 적용: 코드·라우트·에셋 SHA 검증 및 미검증 명시 | needs-validation |

## 3. v0.15의 유형별 실제 계약

- **Surface 10종**: `plain`(간격), `solid`(독립 그룹), `outlined`(선 구분), `subtle`(부드러운 보조 면), `inset`(2차 내용), `raised`(한 단계 그림자), `interactive`(전체 클릭), `media`(이미지 프레임), `reward`(보상 한정), `overlay`(시트/대화상자). Raised→Raised 및 Reward→Reward 중첩 금지.
- **텍스트 5역할**: `display,title,section,body,caption`. 한 화면의 모든 문구를 같은 굵기·크기로 만들지 않음.
- **Badge 7톤**: `neutral,brand,success,info,warning,danger,reward`, 기본 26px/XS 21px; *표시*와 *선택 컨트롤*을 혼동하지 않음.
- **Progress 1종**: 6/10/12px 두께만 변형. 실측되지 않은 달성률 확정 금지.
- **Action**: small 34 / normal 43 / large 48; 터치 목표 최소 48, 내부 SVG 기본 17, 16–18 범위. 다크 Reward CTA = `#957027` 면 + 흰 전경, Light = `#FFCC4D` + `#352713`.
- **Bottom Sheet**: closed/min/mid/full 중 필요한 스냅, 실시간 따라가는 드래그, 중간에서 Native Scroll 우선, Footer 분리.
- **Avatar / Media**: 얼굴과 장비 좌표의 투명 합성 / 보상 아이템 contain / 장면 썸네일 cover. 원형 사진 크롭과 혼동 금지.
- **SVG**: 현행 56개 = Outline 32 + active Fill 5 + 추가 Outline 19, viewBox 24, stroke 1.8, round cap/join, currentColor. **v0.13의 37개 도형으로 대체 금지**.
- **Root Font Trial**: CSS viewport 기반 모바일 390px 약 17px, 브라우저의 초기 font-size 설정을 기준으로 하고 Native 설정은 별도.

## 4. 공식 SVG 전체 목록 (manifest 동일)

- Core Outline 32: `home, explore, chat, users, user, grid, check, check-circle, book, bookmark, calendar, clock, target, activity, plus, edit, search, share, chevron, left, menu, more, settings, gift, trophy, flame, sparkle, heart, bell, shirt, dumbbell, chart`.
- Fill 5: `home-fill, chat-fill, users-fill, user-fill, check-circle-fill`.
- Additional Outline 19: `flag, repeat, pause, play, skip, user-plus, link, send, ranking, shopping-bag, coins, package, lock, close, chevron-down, refresh, warning, info, eye`.
- 클랜 화면에서 쓰는 **20개 고유 원본 SVG**를 `assets/seed-icons/`에 Git blob 단위 동일한 사본으로 연결. 별칭: `message→chat`, `shop→shopping-bag`, `userPlus→user-plus`, `star→sparkle` (별 아이콘 부재, 표현은 비슷한 공식 광채로 교체). 다른 36개는 정본 경로 그대로 보존하고 웹 시연에 불필요한 복제 금지.
- `coins.svg`는 **실제 캐시 코인 이미지가 아님**. 클랜 미리보기에서 임의 C문자 코인·지급 금액을 만들지 않음.

## 5. v3에서 발견된 위반 → 교정

| 이전 문제 | 영향 | v4 교정 |
|---|---|---|
| SEED를 썼다고 기록했지만 JS에서 21개 모양을 직접 그려 1.8 규격만 흉내냄 | 광학 무게/형태 불일치·원본 소실 | 공식 SVG를 원본 SHA 그대로 복제, CSS mask로 `currentColor` 적용. JS에는 키 매핑만 존재 |
| `#ffcf59` + 어두운 글자를 다크 보상 CTA에도 강제 | v0.15 Reward Secondary 대비·테마 위반 | `--reward / --on-reward` 기존 테마 변수 재사용, 48px 터치 면 확보 |
| 직접 그린 Heart를 CSS fill, 공식 Heart Fill 없음 | 정본에 없는 임의 Fill | 공식 Heart Outline 유지, 활성은 색·수치로 표시 |
| 버튼·아이콘 조작 2.0~2.85rem, 필드 일부 2.9rem | 좁은 폭(16px root)에서 48px 터치 미달 | 메뉴·버튼·색상 선택·반응 타깃 최소 3rem |
| 필드 Focus에 두꺼운 외부 outline 중첩 | C-FIELD single-border 원칙과 충돌 | 기존 필드 Border 상태만 변경, 키보드 포커스 가시성 유지 |
| 메뉴 시연 상태 선택을 두 개 outline pill로 처리 | C-SEGMENT 단일 트랙 원칙 위반 | 하나의 트랙 + selected thumb로 정돈 |

## 6. 의도적으로 하지 않은 것

- 신규 화면/경제/데이터 테이블, 실제 수령/구독/뽑기·레이드, 제품 클랜 상점 구현, 기존 Root 네비게이션·마스코트 교체 없음.
- 임시 클랜 멤버/기여/랭킹/수치가 실제 검증된 보상·공개 동의 데이터라는 주장 안 함.
- 전체 화면에 3D 보상 배너·카드·그라데이션 확산 없음. 부정확한 시연 콘텐츠일 수 있어 사용자 최종 시각 검수는 필요.

## 7. 검증 및 승인 경계

- GitHub에서 ICON SHA 확인, 12개의 클랜 해시 렌더/링크 검증, 원본 AvatarComposer 위치값·RootFontSize·Header·Bottom Nav 보존 검사.
- 실제 GitHub Pages 렌더, Android 실기기 TalkBack·IME, 200% 텍스트·보상/초대 서버 E2E는 **별도 검증 전 완료로 선언하지 않음**.
- 이 매핑은 **기존 SEED를 클랜에 정확히 적용하기 위한 설명서**이지 v0.15 확정 시드 파일을 수정하거나 새 제품 정책으로 승격하는 문서가 아님.
