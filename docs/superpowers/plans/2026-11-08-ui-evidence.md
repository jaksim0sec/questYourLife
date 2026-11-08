# UI evidence implementation plan

Goal: implement the existing preview correction described in ../specs/2026-11-08-ui-evidence-design.md.
Architecture: plain HTML/CSS/JS, existing routes/assets. No dependencies added to product.

- [x] Capture unmodified 2cbc03f baseline and reproduce header glyph/hit-center mismatch.
- [x] Modify existing style.css declarations: shared shell roles, header spacing/type, remove positional hacks. Correct theme-color mapping in app.js.
- [x] Modify existing clan.css declarations and identity markup: shared insets, readable roles, neutral reward stage; responsive reflow without text shrinking.
- [x] Update F-COLOR/F-TYPE/F-GEOMETRY/C-ICON/C-SURFACE and INDEX with a single linked implementation proposal. Keep D-012 accepted baseline intact.
- [x] Run browser matrix and existing interactions; visually inspect full screenshots, then create visual review with classified attachments, measured colors, top-down checklist, before/after and limitations.

Review focus: long Korean clan names; 200% text; 320px action collisions; Light reward contrast; menu/theme and local tabs surviving style changes.

검토 판단: 200% 피드/랭킹 실패는 자동 overflow 검사만으로 드러나지 않아 시각 확인과 본문 최소폭 검사를 추가했다. 홈 갤러리 확대 결함과 미구현 route 본문은 범위 밖으로 명시했다.
