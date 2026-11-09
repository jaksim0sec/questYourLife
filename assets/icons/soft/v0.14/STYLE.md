# Soft outline icon pack / v0.14 inventory

56 icons: core 32 outline + 5 active fill; additional 19 outline.

## Fixed rules
- Approved explore, chat, book, and user geometry is preserved; home doorway is narrowed by request.
- Stroke reduced from 2.1 to 1.8 (14.3% thinner).
- viewBox 0 0 24 24; width and height default to 24; color inherited through currentColor.
- Round caps and joins; explicit curves for the home roof and doorway, compass corners, book spine, and chat tail.
- No gradients, shadows, background plates, or decorative status dots in individual assets.
- Fill variants preserve the main silhouette; user-fill uses a circular outline with filled head and shoulders for clear identity.
- chevron points right; chevron-down points down; left is a full left arrow.
- chart uses the supplied two-building/bar silhouette; ranking uses a three-height podium.
- Additional icons are original extensions from the accepted construction rules; they are separate from the core inventory.
- Masks in standalone fill SVGs use icon-specific IDs. When inserting multiple copies inline, prefix mask IDs per instance; img usage is isolated automatically.

## Validation
- All 56 SVGs parse as XML.
- Review sheets render actual asset paths in both enlarged and 24 px sizes.
- Style consistency is visually inspected; this does not claim pixel identity with the small reference screenshots.
- Product integration and state wiring are outside this asset task.

## Detail refinement revision 3
Only share, more, calendar, send changed. Share radius increased 1.8 to 2.3 while centers moved inward; 240px ink envelope verified unchanged. More uses three 0.6-unit horizontal strokes with 1.8-unit round caps. Calendar date dash removed. Send uses explicit curves at all outer corners. Remaining 52 SVGs are byte-identical to revision 2.
