# Icon source of truth

The navigation and header SVGs under `svg/` originate from the user-supplied
`svg-icons-final (1).zip` (2026-11-02). In response to device feedback, inactive
paths use #222222 strokes at width 2; active paths use width 1.8. Their geometry is unchanged. These
SVGs remain the authoritative source for
main navigation and header icons; `invite.svg` is the supplied invite source. The attachment's preview HTML is reference
material, not executable build instructions.

Run `python3 tools/sync_svg_icons.py` after changing these SVGs. The generated
`res/drawable/ic_patch_*.xml` files preserve path geometry, circle/line primitives,
rounded caps/joins, stroke widths and multi-color active states. Do not hand-edit
the generated files or replace these icons with a library look-alike.

The user explicitly approved the compass in `nav-*-clan.svg` as the new clan
icon (2026-11-02), superseding the people icon in earlier mockups. The pack contains
no chest, mascot raster, hat, or other reward illustration.

Home/quest/chapter reference surfaces add `speech`, `shoe`, `lock`, `chest`,
`route`, `chevron-down` and `more-vertical` as generic rounded SVGs. Outline
icons use the same 24×24 viewBox and 1.8 rounded strokes; vertical menu dots
are filled circles. Generate their `ic_patch_*` vectors with the same compiler.
The navigation's red active states are retained alongside yellow task/reward actions.
