# v0.13 · SVG source assets

Source: user-reviewed 37-icon set from the v0.13 design lab. Individual raw SVG files are the authoritative **candidate** assets for design experiments; no HTML preview is committed.

- 37 files, 24 × 24 viewBox, rounded geometry.
- Outline is default; active variants only when explicit `-fill.svg` exists.
- Fill pairs: `home`, `check-circle`, `chat`, `users`, `user`.
- v0.13 refined shapes: `home` and `users` pairs.
- Color: `currentColor`. White negative-space details inside filled glyphs use `#ffffff`.
- Do not auto-overwrite the Android `nav-*-clan` compass icon, which remains an approved app resource.
- Icons requiring transform or rect/circle must be compiled carefully for Android VectorDrawable rather than passed blindly into the existing minimal compiler.

See [implementation handoff](../../implementation/v013.md) and [C-SVG](../../components/svg-icon.md).
