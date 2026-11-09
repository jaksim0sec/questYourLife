# Web-only SEED Shared Style Integration Plan

> For agentic workers: Superpowers execution. Based on approved direction in `docs/design/seed/implementation/shared-style-contract.md`.

**Goal:** Extract the already-rendered shared web styling into one reused stylesheet and connect real HTML classes without modifying content, ratios, imagery or business data.

**Architecture:** Keep `web/preview/style.css` for preview/page-specific layout; introduce `web/preview/seed-standard.css` for existing foundation, shell, shared action/media/status/progress/SVG rules. Use aliases to existing classes so layout and computed values remain unchanged. Do not implement Android, app code, new components or new visual treatments.

**Tech:** Static HTML, CSS and existing JS; no new dependencies.

**Source:** `docs/design/seed/implementation/shared-style-contract.md`; existing `implementation/v015.md` and confirmed D-011/D-012.

## Constraints

- Every original CSS declaration value must be preserved for migrated selectors.
- Existing HTML text, navigation targets, displayed numeric data, src and image URLs unchanged.
- Do not change grid columns, gaps, image ratios, offsets or Android code.
- Existing `new` and the prior design branch remain untouched. Isolated branch only.
- Document actual web implementation vs the accepted v0.15 target; no false claim of full v0.15 compliance.

### Task 1: Define/verify stylesheet contract
- [ ] Baseline test: current code lacks standalone stylesheet/canonical classes (RED).
- [ ] Create `web/preview/seed-standard.css` using **existing exact declarations** for foundation/shell plus shared media/action/badge/progress/svg/text.
- [ ] Keep unsupported variant designs out of the stylesheet; no numerical invention.

### Task 2: Connect preview, preserve existing rendering
- [ ] Link shared stylesheet before `style.css`, retain `clan.css` order.
- [ ] Update only classes/variant attributes in `index.html` (no content or URLs).
- [ ] Remove migrated declarations from `style.css` while retaining all page-specific rules and numeric values.
- [ ] Retain existing class aliases to avoid breaking dynamic markup.

### Task 3: Web documentation and focused checks
- [ ] Amend the shared-style contract with the real stylesheet path, mapped component selectors and explicit outstanding gaps.
- [ ] Verify original declarations occur in shared stylesheet without edits, CSS has balanced braces and no duplicate component owners.
- [ ] Compare `new` and feature diff: only web CSS/HTML/tests + appropriate docs, no images/JS/data/Android changes.
- [ ] Report static checks distinctly from browser visual testing; no generic completion claims from mere parsing.

## Review focus

- Same class means same actual presentation, not merely renaming.
- Source data and asset URLs untouched, including CSS-mask SVG URLs.
- Moving rules has no unintended cascade effect.
- Legacy preview values not silently replaced with approved but different v0.15 numbers.
- No claim of pixel-perfect regression without a before/after browser comparison.
