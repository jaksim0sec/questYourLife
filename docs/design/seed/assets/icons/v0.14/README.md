# SVG Icon Pack · v0.14 (56 assets)

**Current source of truth for the proposed icon pack.** Supplied by the user on 2026-11-05 as `soft-icons-v014-56.zip`. This replaces the earlier assistant-drawn 37-icon v0.14 files **at the same paths**. SVG content is byte-identical to the supplied core/additional files; the filenames are flattened for direct access.

## Inventory

- **32 core outline**: names in [manifest.json](manifest.json) `base`.
- **5 active fill**: `home-fill`, `chat-fill`, `users-fill`, `user-fill`, `check-circle-fill`.
- **19 additional outline**: names in [manifest.json](manifest.json) `additional`.
- Total: **56 standalone `*.svg` files**, 24×24 viewBox, 24px native dimensions, `stroke-width=1.8`, rounded caps/joins, `currentColor`.
- All source shapes are preserved; nothing was retraced, restyled or simplified during import.

## Integration rules

1. Use standalone SVG files directly for web/visual review. For Android, explicitly convert to VectorDrawable and validate geometry; this folder is **not** deployed app resources.
2. Active Fill variants exist only for the five named icons. Do not force `fill` on outline versions.
3. `check-circle-fill.svg` contains a standalone SVG `mask`: standard VectorDrawable conversion cannot assume mask support. Rebuild the check cutout with equivalent vector paths if needed. Inline multiple copies require unique IDs.
4. `settings.svg` intentionally preserves the supplied long 3.9KB curved outline. It renders but may be a conversion/maintenance optimization candidate later; no unapproved changes.
5. `coins.svg` is a generic action pictogram, **not** the canonical cash coin artwork. Preserve Android's existing cash imagery for balances and reward UI.
6. **Product conflict:** the existing Android app's *clan navigation* uses an approved **compass**. `users.svg` is only a proposed group symbol. Do not replace app navigation without explicit agreement.
7. `chart.svg` has the provided two-building/vertical-bar silhouette; `ranking.svg` is a separate podium-like pictogram. Do not silently reinterpret the provided path as a trend line.

## Provenance & validation

- Original ZIP SHA-256: `247ed44a7ba18920a7fd5c575c38cb7e6d7d00ac47eb1800bba56d2b87891ee9`.
- Import validated 56/56 XML and rasterized all 56 at 24px using CairoSVG; none rendered blank or exceeded canvas.
- All 56 Git blob SHA-1 IDs match the original ZIP SVG bytes (Git tree aggregate double-check).
- [Original STYLE.md](STYLE.md) · [Original QA.json](QA.json) · [Original manifest](manifest.json).
- Other design contracts: [v0.14 integration handoff](../../../implementation/v014.md) · [C-SVG](../../../components/svg-icon.md) · [Seed INDEX](../../../INDEX.md).
- `v0.13` remains available for rollback and comparison. Reference screenshots and HTML previews are not committed.

**Status: user-supplied approved-to-use asset proposal; not validated in Compose or on Android devices.**
