---
id: F-ICON-MOTION
category: foundation
status: proposed
read_when: SVG 선·광학 크기·활성 아이콘·모션·동작 줄이기
depends_on: [F-COLOR, F-GEOMETRY]
references: [REF-001, REF-002, REF-004, REF-005]
---

# 아이콘과 모션

## SVG 제안 규격
최종 채택된 사용자 제공 UI 아이콘 56종은 `viewBox=0 0 24 24`, `strokeWidth=1.8`, 기본 `fill=none`, round cap/join, `currentColor`를 사용한다. 이전 v0.6의 1.95 수치는 이 자산군에 적용하지 않는다. 기본 실제 도형은 약 18–20 범위에 담아 24 슬롯 안에서 선 굵기·면적·시각 중심을 맞춘다. glyph마다 빈 공간이 달라 같은 width만 맞추는 것으로 끝내지 않는다. 기본 슬롯은 F-GEOMETRY를 참조한다.

한 화면에서 다른 아이콘 라이브러리의 굵기·각진 모서리·그림자 스타일을 섞지 않는다. 검색·편집·추가·선물·메뉴는 같은 Outline family다. 페이지 제목보다 아이콘이 과도하게 커지지 않게 한다. 3D 코인·마스코트는 SVG UI 아이콘과 다른 에셋 family다.

## S2-04/05/07/08 아이콘 오류 교정
- 헤더·동일 아이콘 버튼·하단바의 glyph는 **같은 24 viewBox와 같은 stroke/cap/join 정본**을 사용하고, 보이는 크기만 역할에 따라 정한다. 임의의 각진 편집 아이콘, 넘치게 큰 gift/plus, 새 미니 획 두께는 로컬 SVG에서 금지.
- 하단의 **해당 Fill 자산이 있는 아이콘**은 active 시 독립 Fill + 강조색 + 라벨, inactive 시 Outline + 중립 전경. 추가 Fill이 없는 자산은 자동 채우지 않는다. 최종 탭 목적지는 O-003 미결.
- 자산 자체(금속 코인·캐릭터 얼굴·실물 모자)는 이 stroke 규격을 적용하지 않는다.
- 비교 검수: Search·Bell·Menu·Edit·Gift·Plus 6개를 동일 canvas에서 16/20/24로 놓고 선 굵기·광학적 중심·baseline을 확인한다.

## v0.6 Outline / Fill 자산 구분
- C-SVG가 실제 별도 경로 `home`/`home-fill`, `check-circle`/`check-circle-fill`, `chat`/`chat-fill`, `users`/`users-fill`, `user`/`user-fill`의 도형 계약을 정의한다.
- 하단 Root에서 비활성은 Outline·활성은 Fill. 모든 상단 SVG는 기본 Outline 유지(D-011). Fill을 Outline에 CSS만 입힌 것으로 대체 금지.
- Menu의 세 가로선은 사용자 제공 원본의 y=6/12/18에 맞춘다, Home 외곽은 둥근 corner. 글자에 비해 지나치게 크게 만들지 않고 기능별 optical size를 검사한다.
- 버튼에 SVG가 들어갈 경우 P-BTN-ICON의 안정적인 슬롯·라벨 규칙을 반드시 따른다.

## 상태와 의미
기본은 Outline. 선택/활성은 색 + 밑줄/체크/라벨 상태로 먼저 표시한다. Fill 활성은 컨셉에서 관찰되지만 전역 승인 규칙은 아니며 하단바는 미결이다. 새로 모든 아이콘의 Fill 버전을 만들지 않는다. 상단 액션은 D-011에 따라 Outline을 보존한다.

장식 아이콘은 읽기에서 숨기고, 단독 조작은 접근 가능한 이름을 부여한다. '더보기'·'뒤로'·'닫기'는 실제 동작이 다르다. 크기 보정이 터치영역을 줄여서는 안 된다.

## 모션 제안
| Token | 시간 | 목적 |
|---|---:|---|
| motion.press | 120ms | 버튼·아이콘의 눌림 색 전환 |
| motion.switch | 160ms | 탭·선택 상태 변화 |
| motion.feedback | 240ms | 작고 일회적인 결과 표시 |
| motion.reward | 600–1200ms | 명시적 보상 장면의 선택적 연출 |

기본 easing은 부드러운 ease-out. 누른 순간 색/상태 반응을 먼저 제공하며 연출 종료까지 행동을 막지 않는다. 자동 반복 광선·흔들림·보상 파티클을 일상 목록에 두지 않는다. reward 연출은 건너뛰기·동작 줄이기에서 정적 결과로 대체하고, 피드백 메시지는 유지한다. 로딩은 진행 중임을 알리되 완료처럼 보이지 않는다.

## 근거·영향
선 중심 아이콘은 반복 관찰 high 및 D-011 확정 방향. SVG 수치·모션 시간·미노출 focus/loading은 설계 제안이다. 아이콘 교체 시 슬롯·이름·선의 무게를 함께 확인한다. 연출 품질은 아직 구현·측정하지 않았다.


**에셋 경로와 사용처:** 정본은 실제 `assets/icons/soft/v0.14/`로 옮겼다. preview의 SHA 일치 SVG 18개는 제거하고 참조를 바꿨다. [공통 에셋 관리 계약](../../../assets/README.md)은 이관 기록과 래스터 후속 작업을 소유한다. 56종의 승인 도형·규칙은 그대로 유지한다.

## v0.15 원본 우선순위

[사용자 제공 Soft SVG 56종](../../../../assets/icons/soft/v0.14/README.md)은 시드의 **유일한 현행 UI 아이콘 정본**. 기존 37개 assistant-drawn SVG 및 고정 1.95/2.15 stroke 후보를 우선 사용하지 않는다. 다른 브랜드의 SVG를 찾아 대체하지 않는다. 보상 코인 이미지는 별도 art이고 `coins.svg`는 일반 pictogram임. Native 앱의 이미 승인된 클랜 나침반 등은 자동 변경 금지.

## 2026-11-10 · 웹 Navigation 아이콘 채택 범위 (D-015)

- Top Navigation 렌더 박스는 `1.59rem`, Bottom Navigation은 기존 `1.56938rem`으로 **각 슬롯의 기준을 유지**한다. 원본 24 viewBox, 획 1.8, round cap/join은 보존. 같은 파일이라도 렌더 크기와 optical area는 다를 수 있다.
- 웹 전역 하단 클랜 활성은 `assets/icons/preview/clan-bookmark-fill.svg`, 마이 비활성/활성은 `assets/icons/preview/me-smile.svg`/`me-smile-fill.svg`를 사용자 시연 승인에 따라 사용한다. 추가 웹 소스는 **기존 Soft SVG 56종을 바꾼 것이 아니다.** 파일·상태 연결은 [C-BOTTOM](../components/bottom-navigation.md), 공통 에셋 관리 계약에서 정의한다.
- 상단은 계속 Outline이고 화면별 아이콘 수·행동은 맥락에 따라 선택한다. Android의 기존 네비 이미지 및 IA는 자동 변경하지 않는다.
