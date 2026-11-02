# Icon source of truth

The 12 SVGs under `svg/` are the exact user-supplied files from
`svg-icons-final (1).zip` (2026-11-02). They are the authoritative source for
main navigation and header icons. The attachment's preview HTML is reference
material, not executable build instructions.

Run `python3 tools/sync_svg_icons.py` after changing these SVGs. The generated
`res/drawable/ic_patch_*.xml` files preserve path geometry, circle/line primitives,
rounded caps/joins, stroke widths and multi-color active states. Do not hand-edit
the generated files or replace these icons with a library look-alike.

The user explicitly approved the compass in `nav-*-clan.svg` as the new clan
icon (2026-11-02), superseding the people icon in earlier mockups. The pack contains
no chest, mascot raster, hat, or other reward illustration.
