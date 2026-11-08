---
id: S-PAGE
category: shell
status: proposed
read_when: Root·Detail·Utility·Immersive·Overlay, 헤더·하단바·키보드·스크롤
depends_on: [F-GEOMETRY, F-TYPE, F-COLOR, P-ACTIONS, NAV-TOP]
references: [REF-001, REF-002, REF-004, REF-005, D-011, SRC-TDS-BOTTOM]
---

# S-PAGE · Shell과 안전영역

## 현행 계약

- **페이지 유형, 하단바 노출·활성 탭, Back/Close 정책**의 단일 소유자는 [pages/page-types.md](../pages/page-types.md)다. S-PAGE는 상단 Shell 구조·고정 영역 공간·safe-area·scroll 맥락을 구현한다.
- Header 제목과 본문 첫 요소의 수평 시작선은 [F-GEOMETRY](../foundations/geometry.md)의 **단일 `page.inset`**을 공유한다. 각 x 좌표 차이는 1px 이하.
- Header 버튼의 실제 클릭 타깃 중심과 Glyph 박스의 중심을 가로·세로 각각 1px 이내로 일치시킨다. 개별 `translateX` 금지. 클릭 타깃 48px 하한은 F-GEOMETRY 소유.
- Root/Standard 외형은 확정된 [NAV-TOP](../../TOP_NAVIGATION.md) 기준. Shell 이름 Root/Detail/Utility/Immersive/Overlay는 콘텐츠 목적이나 하단바 유형을 뜻하지 않는다.
- 실제 하단 고정 내비·Safe Insets·키보드·Fixed CTA를 한 소유 위치에서 합산하고 이중 Padding 금지.
- Back은 유효한 진입 이력, 딥링크면 page-types의 부모 글로벌. 해당 history entry별 스크롤·포커스 맥락은 복원 대상이다. 탭/필터 내부 변화로 스크롤을 임의 0 초기화하지 않는다.
- 긴 제목·글자 200%에서 Header가 자랄 수 있어야 한다. 시트는 닫은 뒤 원래 바탕 맥락을 보존한다. Dialog는 페이지 유형이 아니다.

**현재 코드 위반:** `clan.css`에서 `global-inner`의 하단바를 숨긴다. P3 단계에서 제거·회귀검증 예정. 문서만 수정하고 구현 완료를 주장하지 않는다.

## 이력

- 이전 정본 원문은 [HISTORY.md](../HISTORY.md)에서 전체 보존하며 현행 조항으로 사용하지 않는다.
