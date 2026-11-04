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
Badge: Container + Label + 선택적 Icon, tone(neutral/positive/warning/critical/info/custom), emphasis(weak/outline).
Progress: Track + Indicator + Value label + 선택적 목표 설명, mode(determinate/indeterminate), value/max. 현재 값·목표·비교 기준을 슬롯 데이터로 받는다. 제품 재화·XP를 기본값으로 고정하지 않는다.

## Tokens
Badge는 type.caption/support, radius.pill, badge.paddingX/Y 사용. 무채색이 기본, 의미 있을 때 semantic 색 사용. Badge의 작은 면과 글자가 주변 제목보다 튀지 않게 한다. Progress는 progress.height, radius.pill, bg.muted track과 accent.solid indicator를 기본 제안한다. 이벤트의 금색·보라는 A-ART의 맥락별 표현이며 전역 진행률 기본색이 아니다.

## States / Behavior
Badge는 정보 표시라 focus/pressed를 갖지 않는다. Progress는 zero / partial / complete / indeterminate / unavailable을 구분한다. 데이터가 없거나 오프라인 값이 오래되었으면 완주처럼 채우지 않는다. value는 유효 범위로 표시하되 이상 데이터 자체를 숨기지 않고 별도 오류를 남긴다. complete는 실제 완료 상태와 일치할 때만 표시한다.

## Guidelines
색만으로 진행·착용·제한을 구분하지 않는다. 보유 캐시와 획득 캐시는 별도 라벨을 둔다. 컨셉의 XP/시즌 레벨/HP/보석/가격 표시는 표현 예시이며 제품 요구를 추가하는 근거가 아니다. 진행 막대만으로 완료/수락 버튼 역할을 대신하지 않는다.

## Accessibility
Progress의 이름·현재 값·최댓값을 전달한다. indeterminate는 알 수 없는 퍼센트를 만들지 않는다. 짧은 Badge도 읽을 수 있는 대비를 갖는다. 지속 변화는 스크린리더에 과도하게 반복 알리지 않는다.

## Evidence & Confidence / Impact
진행·수치·작은 태그는 관찰 high. 정확한 상태 및 토큰은 proposed. 기본 값·모양 변경은 목록·배너·수집 화면의 비교 가능성에 영향이 있다.

