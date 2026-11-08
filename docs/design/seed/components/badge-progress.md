---
id: C-STATUS
category: component
status: proposed
read_when: Badge·Tag·진행률·숫자·완료 상태 표시
depends_on: [F-COLOR, F-TYPE, F-GEOMETRY]
references: [REF-001, REF-002, REF-003, REF-004]
---

# C-STATUS · Badge 및 단일 Progress

## 현행 계약

D-012 승인 디자인 기준을 보존한다. Badge는 정보 상태, Progress는 목표 대비 진행이다. 선택/필터 기능은 C-SELECTION이 소유한다. C-STATUS가 **트랙 두께의 유일한 구현 정본**이다.

### Badge

- 승인 톤: neutral / brand / success / info / warning / danger / reward (**7종**).
- 승인 표시값: 기본 높이 **26px**, XS **21px**, radius pill, 기본 horizontal padding 11/XS 8, icon 13. 기본 정보만 표시하며 눌리지 않는 상태는 focus/pressed를 갖지 않는다.
- 객체당 1~2개, 라벨 2~3단어는 밀도 **candidate**이며 P2 비교·반례 시험 전 새 강제 규격으로 확정하지 않는다. 사용자에게 필수인 사실을 배지를 줄인다는 이유로 삭제하지 않는다.
- 동일 상태의 값·원인을 다시 본문/Badge/버튼에 반복하지 않는다. 색상만으로 보유/선택/완료를 전달하지 않는다. 화려한 금색은 A-ART 보상 순간의 표현이지 일반 Status의 기본색이 아니다.

### Progress · 두께 소유

**공통 Progress는 한 종류**이고 두께 Variant는 **`thickness=micro(6px) | default(10px) | featured(12px)`**이다. 별도 클랜 전용 Progress를 추가하거나 같은 타입의 트랙을 다른 정본에서 재정의하지 않는다. O-PROGRESS는 의미 객체의 Label/대표값/파생값/Deadline을 소유한다.

| 유효한 입력 상태 | Track/값 표시 | ARIA/부가 의미 |
|---|---|---|
| ready, 0/1000 | 0%, 값 0/1000 | now 0, max1000 |
| ready, 620/1000 | 62%, 값 620/1000 | now 620, max1000 |
| ready, 1000/1000 | 100%, 값 1000/1000 | 목표 도달, 실제 보상 지급 완료 암시 금지 |
| ready, 1200/1000 | 100%, **실제 1200/1000** 표기 | **aria-valuenow=1000**, aria-valuemax=1000, **aria-valuetext로 1200/1000 보존** |
| loading | 결정되지 않은 표시 또는 진행 상태 안내 | now 미제공; 진척 0으로 위장 금지 |
| unavailable, 유효하지 않은 숫자·max | 기록 확인 불가 및 유효한 복구 경로 | Progressbar/유효 range **생성 금지** |
| ready + stale | 마지막 확인 시점과 실제 유효 값을 표시 | 방금 완료·지급 가능으로 단정 금지 |

**입력 판정:** label·enum 유효성 → 명시 unavailable → 명시 loading → ready의 유한 `number`형 `value>=0`, `max>0` 검증. 음수·NaN·Infinity·문자 숫자 묵시 변환 금지. stale은 유효 ready 값에만 적용한다.

**단일 소스:** `current/target`은 대표값·시각 fill·남은 양·실제 값 텍스트/ARIA의 공통 출처다. 실제 비율 `Math.min(value,max) / max`로 계산하여 초과 유한값의 곱셈 오버플로를 막고 0~100%로 제한한다. 실제 초과 수치는 숨기지 않는다. `aria-valuenow`는 max 초과 금지. 진행바 내부 fill은 계산된 box를 갖고 표시 두께는 variant와 일치해야 한다.

**기존 기능 보존:** Progress는 사용자가 직접 누르는 완료 버튼이 아니며 보상 지급, 퀘스트 체크, 실제 DB 완료를 대신하지 않는다.

## 이력

- 과거 v0.6 제안/확정 규격의 반복 문단과 D-013 r1 K08은 [HISTORY](../HISTORY.md)에 전체 원문을 그대로 보존했다. 역사적 승인 근거 D-012는 [v015 승인 시드](../implementation/v015.md)에 보존.
- P2에서는 Selection / Don't / Relations / Verification 4섹션과 배지 밀도 후보·실제 렌더 fixture를 검증한다. 현재 문서 상태는 proposed.
