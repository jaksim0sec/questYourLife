---
id: C-STATUS
category: component
status: proposed
read_when: Badge·Tag·진행률·숫자·완료 상태 표시
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004]
---

# 상태 표시: Badge와 Progress

## Purpose / Non-goals
Badge는 짧은 속성·상태, Progress는 기준에 대한 진행을 표시한다. 둘은 동작이 다른 별도 재료이며 이 문서에 작은 상태 표시 family로 함께 문서화한다. 눌러 필터가 바뀌면 Badge가 아니라 C-SELECTION이다.

## Anatomy / Properties
Badge: Container + Label + 선택적 Icon, tone(neutral/brand/success/info/warning/danger/reward), emphasis(weak/outline).
Progress: Track + Indicator + Value label + 선택적 목표 설명, mode(determinate/indeterminate), value/max. 현재 값·목표·비교 기준을 슬롯 데이터로 받는다. 제품 재화·XP를 기본값으로 고정하지 않는다.

## Tokens
Badge는 type.caption/support, radius.pill, badge.paddingX/Y 사용. 무채색이 기본, 의미 있을 때 semantic 색 사용. Badge의 작은 면과 글자가 주변 제목보다 튀지 않게 한다. Progress는 progress.height, radius.pill, bg.muted track과 accent.solid indicator를 기본 제안한다. 이벤트의 금색·보라는 A-ART의 맥락별 표현이며 전역 진행률 기본색이 아니다.

## Progress v0.6 정리
독립 컴포넌트 **Progress 한 개**가 `thickness=micro(6)|default(10)|featured(12)`을 속성으로 제공. Track과 Indicator의 높이·rounded cap을 함께 맞춘다. 0/49/99/100%, indeterminate, offline stale의 퍼센트 표기를 의미에 따라 구별. 등급색을 진행률 기본색에 혼입하지 않는다.

Credit Value는 [C-CREDIT](credit-value.md)에서 이미지를 포함한 숫자 슬롯을 정의한다. Badge/Progress를 코인+수치의 정본으로 겸용하지 않는다.

## States / Behavior
Badge는 정보 표시라 focus/pressed를 갖지 않는다. Progress는 zero / partial / complete / indeterminate / unavailable을 구분한다. 데이터가 없거나 오프라인 값이 오래되었으면 완주처럼 채우지 않는다. value는 유효 범위로 표시하되 이상 데이터 자체를 숨기지 않고 별도 오류를 남긴다. complete는 실제 완료 상태와 일치할 때만 표시한다.

## S2-06/08 상태 강조의 선
- 일상적인 '진행 중/완료/착용 중'은 작은 Badge와 실제 text로, 한정된 보상 순간의 강조는 A-ART로 분리한다. 아이템의 금색·보라를 모든 일반 UI의 semantic status로 복제하지 않음.
- 퍼센트나 보유 캐시는 실제 데이터의 label을 붙이고 비교 가능해야 한다. 이미지를 보고 가상의 XP/Level을 만들지 않는다.

## Guidelines
색만으로 진행·착용·제한을 구분하지 않는다. 보유 캐시와 획득 캐시는 별도 라벨을 둔다. 컨셉의 XP/시즌 레벨/HP/보석/가격 표시는 표현 예시이며 제품 요구를 추가하는 근거가 아니다. 진행 막대만으로 완료/수락 버튼 역할을 대신하지 않는다.

## Accessibility
Progress의 이름·현재 값·최댓값을 전달한다. indeterminate는 알 수 없는 퍼센트를 만들지 않는다. 짧은 Badge도 읽을 수 있는 대비를 갖는다. 지속 변화는 스크린리더에 과도하게 반복 알리지 않는다.

## Evidence & Confidence / Impact
진행·수치·작은 태그는 관찰 high. 정확한 상태 및 토큰은 proposed. 기본 값·모양 변경은 목록·배너·수집 화면의 비교 가능성에 영향이 있다.


## v0.15 Badge/Progress 확정 시각 규격 (D-012)

Badge 톤 = `neutral|brand|success|info|warning|danger|reward` 7종. `label` 필수, `icon|dot`은 선택적. 높이 26px, XS 21px, Radius Pill; 기본 paddingX=11, XS=8, 아이콘 13px, 미세 outline(시연 기준 Light 약 12% / Dark 약 24% 전경색 혼합). 의미색 토큰을 적용하고 한 화면의 모든 Badge를 Brand로 채우지 않음. Badge는 정보 표시, 필터/선택은 C-SELECTION을 사용.

Progress는 **공통 컴포넌트 하나**, thickness=6/10(default)/12(feature) 속성. 같은 도메인 값을 다른 Progress component로 중복 선언 금지. 실재 수치·완료 여부 및 스크린리더 Value는 앱 데이터에서 전달.

> **2026-11-08 공통 계약 r1 적용:** 아래 절은 사용자 위임에 따라 이번 웹 개편의 기준으로 확정했다. 문서 전체의 과거 실험·제품 미결·Native 미검증을 일괄 승인한 것은 아니다.

## K08 상태·Progress·재화

**정본 소유자:** 상태/Progress는 `components/badge-progress.md`, 금액은 `components/credit-value.md`.

Badge는 정보 표시이며 필터·선택 버튼이 아니다. 톤은 승인된 neutral/brand/success/info/warning/danger/reward 7종을 사용한다. 객체당 기본 1–2개로 제한하고 일반 인원수·설명을 모두 Badge로 바꾸지 않는다. 이는 정보량 기본 기준이며 필수 사실을 지우는 규칙이 아니다.

Progress는 공통 표현 하나에 thickness micro6/default10/featured12를 사용한다. 값·목표·표시값·남은 값·채움·ARIA는 같은 입력에서 만든다. 숫자와 채움 퍼센트를 별도 하드코딩하지 않는다.

| 입력/상태 | 실제 표현 | 접근성/판정 |
|---|---|---|
| ready, value0, max1000 | 0 / 1,000, 채움0 | 유효한 0 |
| ready, 620/1000 | 620 / 1,000, 채움62%, 필요한 경우 남은380 | min0/max1000/now620 |
| ready, 1000/1000 | 채움100%, 목표 달성 | 지급·수령 완료로 해석하지 않음 |
| ready, 1200/1000 | 실제1,200 / 1,000, 채움100%, 목표 초과 의미 | now1000, 실제 초과값은 외부 텍스트와 valuetext로 전달 |
| loading | 퍼센트 없는 진행 중 표시 | now 생략, 무모션 대안 |
| unavailable, 값 없음, NaN/Infinity/음수, max≤0 | ‘기록 확인 불가’와 가능한 복구 경로 | 0%나 완료로 위장하지 않고 range 위젯을 노출하지 않음 |
| ready + stale, 마지막 유효 값 | 마지막 확인 기록임을 밝히고 해당 값 표시 | 현재 완료·새 수령 가능으로 단정하지 않음 |

목표 초과는 정상적인 초과 성취일 수 있으므로 비정상 데이터와 구분한다. 외부 값은 숨기지 않고 시각 채움만 100%로 제한한다. loading은 처리 상태이며 데이터 오류를 indeterminate로 무한 대기시키지 않는다. 이 표가 이전 계획의 ‘범위 초과는 모두 오류’로 읽힐 수 있는 부분을 대체한다.

판정 순서는 **label과 enum 유효성 확인 → 명시 unavailable → 명시 loading → ready의 숫자 검증**이다. loading은 value/max가 null이어도 정상이며 숫자값을 표시하지 않는다. 숫자 누락·오류 행은 ready에 적용한다. ready는 number 타입의 유한 value≥0과 max>0을 요구하고 문자열 숫자를 묵시 변환하지 않는다. freshness=stale은 ready의 유효 값에만 적용한다. 비율은 `min(value,max) / max × 100`으로 계산하여 매우 큰 유한 초과값도 중간 곱셈에서 Infinity가 되지 않게 한다. 잘못된 label/enum은 개발·검수 오류로 남기고 이름 없는 range를 만들지 않는다.
