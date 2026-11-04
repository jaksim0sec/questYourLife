# 8장 전수 판정 · 개선 우선 감사표

> 2026-11-05 / 제안 상태. 이 파일의 S2-01~08 식별과 SHA는 [원본 등록부](references/inputs.md) 정본이다. 시안을 그대로 코딩하거나 승인된 제품 기능으로 간주하지 않는다.
> 판정: Keep / Refine / Merge·Remove / Split / Add. 기본값은 **기존 규칙 개선(Refine)**이지 신규 컴포넌트 증식이 아니다.

| 참조 | 기존 시스템에 매핑 | 최종 판정 | 검증 포인트 |
|---|---|---|---|
| S2-01 버튼 계층 | C-ACTION, F-COLOR, F-GEOMETRY | **Refine** | 높은/중간/낮은 강조가 같은 size에서도 명확하고 아이콘이 작음 |
| S2-02 BackendX 광고 | 제작·검증 절차 | **Remove** UI 근거에서 제외 | 시각 요소·기능 차용 없음, 플레이그라운드는 작동 |
| S2-03 토스 프로모션 | C-SURFACE, C-ACTION, A-ART | **Refine + Keep** 단순 CTA | 3D 강조와 일반 컨트롤의 시각적 분리 |
| S2-04 나쁜 예 10개 | P-ACTIONS / F-TYPE / F-ICON 등 | **Refine/Remove** 반례 | 50:50·과한 인사말·수락/넘기기·하단 충돌·SVG 등 10개 재현 없음 |
| S2-05 홈/퀘스트 | P-HIERARCHY, S-PAGE, C-LIST | **Refine** 구성 밀도 | 발견형 화면과 실행형 화면은 같은 재료로 다른 밀도, 탭/CTA 불필요한 겹침 없음 |
| S2-06 시즌/상자 | A-ART | **Keep** 특별 표현, UI로 확장 금지 | A3 아트 밖 일반 버튼은 단색, 제품에 없는 XP/유료 요소 제거 |
| S2-07 탐색/클랜/상세 | C-LIST, C-SURFACE, P-HIERARCHY | **Refine** 미디어 조합 | 콘텐츠의 독립 시각 존재감, 리스트/탭/하단바 일관 |
| S2-08 라이트 마이/꾸미기 | C-SURFACE → Avatar·ImageFrame, A-ART | **Split + Refine** | 프레임 없는 투명 아바타 허용, 아이템은 contain, 카드 밀도/비율 개선 |

## 10개 나쁜 예 → 비교 fixture
1. 같은 두 하단 버튼: 의도된 **50:50** vs 실수로 30:70.
2. fixed CTA와 Bottom Navigation: 가려지는 예 vs 인셋 한 번 계산·필요 시 inline.
3. 이어하기: 독립 검정 알약 vs C-ACTION same variant.
4. 클랜 채팅 행: 독립 테두리·과장 vs 공통 List Item prefix/title/detail/suffix.
5. 편집 아이콘: 각진 큰 stroke vs 공통 round Outline와 optical size.
6. 마이 캐릭터: 주 텍스트를 압도 vs 정보 그룹과 맞는 아바타 크기·투명 표시.
7. 수락·넘기기: 긴 문구 강제 압축·불균형 vs 자연스러운 선택 그룹·좁은 화면 세로 전환.
8. 출석 인사말: 화면 목적보다 큰 보조 글씨 vs 역할에 맞는 보조 heading.
9. 상위 탭·기간·정렬: 한 줄 밀집 vs 콘텐츠 영향 범위 기준으로 층 분리.
10. 버튼 3종: 아이콘/그라데이션/radius/패딩 불일치 vs 같은 size family.

## 개선 전/후 검증의 의미
이미지와 문서로 추정한 문제를 고친 것과, 실제 브라우저 화면에서 같은 컴포넌트가 일관된 것은 별도 검증이다. 특정 파일 수·토큰 개수의 증가는 성공 지표가 아니다. QA에서 width 320/360/390/430, 200% 글자, light/dark, pending/error/empty, 가로 넘침·safe-area를 확인한다.
