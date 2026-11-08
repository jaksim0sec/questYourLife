# UI evidence review · 2026-11-08

Scope: existing `new/web/preview` and its reusable SEED rules. Requested by user: research, classify references, compare top to bottom, diagnose, improve SEED and preview, verify. No Android/backend/economy changes. New numbers remain review proposals; D-012 is not retroactively rewritten.

## Approach

1. Color-only adjustment: rejected. Latest baseline already uses #16171B, yet icon alignment, small text and unclear hierarchy persist.
2. Rebuild navigation/features: rejected. Unnecessary scope and data-contract changes.
3. Correct existing shell and clan composition: chosen implementation judgment within requested scope. Use shared spacing/type/color roles, centered icon hit targets, readable social rows and one compact reward story. Preserve SVG geometry, transparent avatars and gold chest.

## Concrete contract

- Header and content share #16171B in dark; gutters #101115; bottom nav #202226, with neutral gray text. Light mapping remains independent.
- Shared page inset 1rem; header actions each 3rem, glyph 1.5rem, no translations inside touch targets. Root title 1.5rem/700, Standard 1rem/700. Keep established Root/Standard structure.
- Keep current responsive rem baseline; do not shrink the app globally to fit more content.
- Clan name 1.125rem/700, primary row .875rem/600, supporting text .8125rem/400. Distinguish identity, activity and progress roles.
- Reward keeps existing 3D art. Remove theatrical full-panel amber glow; neutral themed stage, small gold kicker, one 1.5rem heading, neutral preview action. No new reward claims.
- At <=360px or increased text sizes, stack content rather than shrink required text below role sizes. Header title ellipsizes; body names wrap. Existing route/event behavior preserved.

## Completion

Capture actual baseline and revised source at equal CSS viewports. Check 320/390/430/768, Light/Dark, joined/guest, long names and 200% root text. Assert no document overflow, no target overlap, image decode and existing tab/filter/form/navigation behavior. Separate local browser evidence from real Android and published Pages.

## Reference authority

- SEED Action Button: https://v3.seed-design.io/components/action-button — emphasis and restrained brand usage.
- SEED Top Navigation: https://v3.seed-design.io/components/top-navigation — Root/Standard and action slots.
- TDS ListRow: https://tossmini-docs.toss.im/tds-mobile/components/ListRow/list-row-overview/ — left/contents/right, density and interaction.
- TDS Typography: https://tossmini-docs.toss.im/tds-mobile/foundation/typography/ — semantic roles and enlarged text.
- Brenden CHZZK: https://brenden.kr/project/naver-chzzk — brand identity, frequency-inspired graphics, scalable visual system. This is brand evidence, not a public UI token spec or proof of teen preference.
