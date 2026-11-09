# SEED Lab · 웹 패턴 검증장

실제 클랜/상점/홈 페이지가 아닌 **디자인 SEED의 구성·컴포넌트·상태를 검증하는 임시 웹 실험 환경**이다. 앱 데이터, 보상 지급, 계정, Android 소스를 사용하지 않는다.

## 실행

저장소 루트에서 정적 서버를 실행한다.

```bash
python3 -m http.server 8000
```

브라우저에서 `http://localhost:8000/web/seed-lab/`로 접속한다. SVG와 PNG의 상대 경로가 루트의 `assets/`를 가리키므로 `web/seed-lab/` 자체를 별도 서버 루트로 열면 안 된다.

Node.js 22 이상이라면 정적 검사:

```bash
node --test web/seed-lab/*.test.mjs
```

## 시험 중인 규칙

| 실험 | 구성 | 관찰할 계약 |
| --- | --- | --- |
| 01 | 동일한 오브젝트의 Grid/Rail/Rows 변경 | C-IMAGE 소유 영역과 레이아웃 책임 분리, 콘텐츠 보존 |
| 02 | 동일 데이터의 Plain/Solid 섹션 | 섹션 ≠ 카드, 정보 계층 |
| 03 | 0/620/1000/1200/조회 불가/로딩 + 6/10/12 진행률 | K08 실제 수치와 ARIA, 초과값 보존·fill 상한 |
| 04 | 액션/체크/라디오/필드/시트 | 컴포넌트 상태, 명확한 동작 및 포커스 |
| 05 | 공식 PNG 8종 + manifest SVG 56종 | 원본 단일 출처, 투명 이미지 contain, 활성 Fill 분리 |
| 06 | Surface 역할 10종 | 표면과 공간 배치의 책임 분리 |

공통 표현은 `../preview/seed-standard.css`가 소유하고, `lab.css`는 테스트 콘텐츠의 배치만 소유한다. 새 전용 컴포넌트, JSON UI 빌더, 임의 아이콘 원본, 앱 기능은 만들지 않았다. 테마의 승인 토큰은 실험 환경에만 적용해 구형 preview의 변형된 색과 섞지 않는다.

## 검증 구분

- **정적 검사**: 참조 파일과 아이콘 수, 허용 마크업, 수치 상태 테스트
- **브라우저 검증 필요**: 320/360/390/430 × Light/Dark, 200% 글자, overflow, 키보드, 최소 터치 타깃, PNG/SVG 디코드, 상태·레이아웃 조작 및 시트 핸들 제스처
- **별도 미완료**: 시트 Body 스크롤 경계에서 drag로 이어지는 동작, 실제 모바일 터치/IME/Screen Reader, SEED 21개 전체 Variant/State 조합, 시각 승인

실험 화면을 보여 준 사실만으로 디자인 승인이나 21개 전부 구현 완료로 표시하지 않는다. 오류가 나오면 페이지별 override보다 기존 SEED 계약과 단일 공통 CSS 소유권부터 확인한다.

관련 정본: `docs/design/seed/implementation/v015.md`, `docs/design/seed/patterns/layout-composition.md`, `docs/design/seed/patterns/page-composition.md`.
