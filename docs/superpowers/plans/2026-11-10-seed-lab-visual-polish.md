# SEED Lab visual polish & shell composition · Implementation Plan

> Existing branch only. No new Composite/Page Type and no changes to Android, main, new, source PNG/SVG, routes or backend.

**Goal:** Turn the existing SEED Lab into a convincing, interactive style-quality reference showing reusable S-PAGE geometry, meaningful component states and multiple C-SHEET footer compositions.

**Architecture:** Keep the canonical visual rules in `web/preview/seed-standard.css`, page-specific arrangement in `web/seed-lab/lab.css`, markup/fixtures in `web/seed-lab/index.html`, and state control in `web/seed-lab/lab.js`. Reuse the existing C-ACTION, C-SHEET and S-PAGE contracts. The new page-shell primitive only owns common header/body/footer geometry and optional slots, not a screen category or a new navigation system.

**Scope & design calls**
- Sheet footer: none, single primary, paired primary + neutral, stacked primary + neutral. These are arrangements of existing Actions, not new button variants. Remove the misleading badge from the action row. Keep the handle, native scroll body, close and focus behavior.
- Component polish: distinguish control strokes from decorative dividers; align C-CHECK/C-RADIO inputs, correct image badge contrast and alt labels, fix mobile section-title wrapping and showcase rhythm. Do not change approved red, gold or icon source.
- S-PAGE demonstration: existing Root/Detail/Utility shell roles visualized using one reusable frame primitive with common gutter/slots and a toggled preview. No actual route, bottom navigation IA or Compose implementation.
- Presentation: concise Lab introduction, organized cases and a report that maps issue to canonical owner, change and QA evidence.

**Files**
- Modify `web/preview/seed-standard.css`: generic visual component and S-PAGE geometry.
- Modify `web/seed-lab/index.html`: sheet footer chooser, shell showcase and existing element corrections.
- Modify `web/seed-lab/lab.js`: chooser wiring, no page-state leak.
- Modify `web/seed-lab/lab.css`: Lab-only composition and responsive rhythm.
- Create `web/seed-lab/visual-polish.test.mjs`: contract regression tests.
- Modify `web/seed-lab/README.md`: demonstrations and verification boundaries.
- Create `docs/superpowers/reports/2026-11-10-seed-lab-visual-review.md`: evidence and handoff.

**Tasks**
1. [ ] Record failing tests for S-PAGE slots, sheet footer variants, contrast/semantics and mobile headings. Verify RED against current source.
2. [ ] Implement shared tokens, fields/choices, badge and shell geometry, preserving legacy preview isolation.
3. [ ] Implement sheet/footer variants and Page Shell interactive showcase, with existing file structure.
4. [ ] Improve demonstration density, sample image metadata and responsive section headings without new global types.
5. [ ] Verify static contract/tests and actual served Pages where accessible; report browser/physical touch/IME limits accurately.

**Review focus:** 320px width with long Korean labels, 200% typography and stacked footer; absent footer does not reserve space; sheet min height and footer do not overlap; surface/badge contrast in both themes; actual image and SVG file references stay unchanged.
