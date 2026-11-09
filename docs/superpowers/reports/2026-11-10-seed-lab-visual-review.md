# SEED Lab · Visual Quality Pass / 2026-11-10

## Scope and evidence

- **Branch:** `feat/seed-layout-composition-20261110` only. Do not touch `main`, `new`, Android, data models, 56 SVG sources, or eight PNG files.
- **Visual evidence:** the five mobile screenshots supplied in this chat; floating anime content is excluded from the evaluation. Source compared: current `web/seed-lab/index.html`, `lab.css`, `lab.js`, canonical `web/preview/seed-standard.css`.
- **Canonical references:** D-011/D-012/D-013, `implementation/v015.md`, `F-COLOR`, `C-ACTION`, `C-CHECK`, `C-RADIO`, `C-SHEET`, `P-ACTIONS`, `P-PAGE-COMPOSE`, `S-PAGE`.
- **Purpose:** improve the coherence and credibility of an experimental design-system preview. This is not final product UI approval nor Android implementation.

## Change ledger

| ID | Screenshot issue / root cause | Implementation | Canonical owner / constraint | Remaining QA |
|---|---|---|---|---|
| V01 | Neutral Outline appeared nearly borderless because decorative `--line:#F0E8E5` was used for an interactive border (1.21:1 against white). | Introduce `--seed-stroke-control:#827A87` Light / `#938B9C` Dark in lab theme, use on neutral Outline and Field. Calculated border contrast approx 4.13:1 on white and 4.76:1 on dark surface. | F-COLOR control border role. No brand palette change. | Browser computed styles, touch/focus |
| V02 | Button showcase claimed equal size while default Brand Solid and small neutral Outline were paired. | Both shown with `data-size=default`; type scale and geometry remain 14/20 and 43px face. | C-ACTION / P-ACTIONS. | Actual computed bounding boxes |
| V03 | Sheet Footer gave the destructive-looking brand emphasis to a redundant Close, put a Min status badge in CTA row and neglected valid Footer compositions. | Four demonstrations: `none` (no fixed actions), `single` (brand next-step), `paired` (neutral close + brand next-step), `stacked` (same Actions vertically). Move snap label to header state; no separate badge. | C-SHEET / P-ACTIONS. The layout is a slot choice, not a new Action variant. | True mobile drag, screen reader, IME |
| V04 | Fixed `body height = 100% - 155px` reserved space even when actions were absent, and a vertical pair could overlap body. | Use a sheet grid with handle/header, independently scrolling body, optional footer. Calculate minimum snap height from measured handle+header+footer and a minimum visible body. | C-SHEET / S-PAGE. Safe inset assigned once to footer. | Browser overflow, dynamic viewport and 200% fonts |
| V05 | Default browser Checkbox/Radio stood out against otherwise custom SEED elements. | Render 21px neutral controls with 48px row target, brand selected state and focus outline, preserving real native input/label semantics. | C-CHECK / C-RADIO. No selection status implied by decorative badge. | TalkBack / keyboard / forced-colors |
| V06 | The image label used the same weak fill as its frame, making the badge unreadable; `특별` was rendered in Reward Gold, which conflated status and economic reward. | Give neutral media badges an independent surface/stroke and change the unapproved illustrative `특별` to neutral `고급`, reserving Gold for reward semantics. | C-IMAGE / C-STATUS / C-RARITY; no new `special` variant invented. | Actual image decode/light-dark |
| V07 | A mobile heading broke the word `관계` into a final isolated syllable as its experiment badge consumed horizontal width. | Keep the experiment index in a mobile top-right slot while the section heading below uses full width. Keep typography tokens unchanged. | P-PAGE-COMPOSE / F-TYPE. | 320/360/390/430 width and 200% fonts |
| V08 | A three-object demo in a two-column grid left one orphan and reduced perceived balance. | Add one existing registered art specimen (`02_capsule_gacha.png`) to form a balanced 2×2 test; preserve three original objects and the full eight-art gallery. | P-LAYOUT / C-IMAGE. | Item visual footprint across assets |
| V09 | An accessibility description mislabeled purple locker as mint and a gold chest as blue. | Correct the `alt` strings based on supplied visual references. | C-IMAGE accessibility. | Actual asset comparison |
| V10 | The large Lab introduction pushed experimental content below the first mobile viewport. | Reduce the descriptive copy, keeping a clear identity and first call to experiment. | P-PAGE-COMPOSE. | Live mobile screenshot |
| V11 | Page structure could only be described in documents; no reusable page-geometry source could be seen or reused in Lab. | Add `seed-page-shell`, `__header`, `__inner`, `__content`, `__section`, `__bottom` to canonical CSS; add an interactive Root/Detail/Utility fixture showing a header, plain metrics, divided rows and bottom boundary. | Existing S-PAGE + P-LAYOUT. **Not a new Composite, Page Type or navigation implementation.** | Real history/navigation/keyboard/OS insets |

## Demonstrated Bottom Sheet action patterns

| Pattern | Footer | Meaning / design |
|---|---|---|
| none | absent | Content viewing or handle-driven interaction; no artificial CTA |
| single | one high action | Advance demo snap. One actual action, no redundant neutral button |
| paired | neutral Close + brand Next | Same Action face size; neutral Outline stroke uses control token; High limited to the primary action |
| stacked | same pair, vertical | Longer labels / narrow layouts without collisions; **not** a new button type |

The header Close remains available in all four cases, so omission of Footer does not strand the user. No real reward, quest or account mutation occurs. The background is a static test harness, not an app feature.

## S-PAGE reusable primitive

```html
<div class="seed-page-shell" data-shell-kind="root">
  <header class="seed-page-shell__header">
    <div class="seed-page-shell__inner">...</div>
  </header>
  <div class="seed-page-shell__content">
    <section class="seed-page-shell__section">...</section>
    <section class="seed-page-shell__section">...</section>
  </div>
  <div class="seed-page-shell__bottom">...</div>
</div>
```

This owns *only* container width, shared gutter, content flow and separation of the header / scrolling body / bottom region. `data-shell-kind` is illustrative mode selection by the **existing** S-PAGE contract; it does not route, make a fixed CTA required, or settle Root/Detail/Utility navigation policy. Actual Top Navigation/Bottom Navigation/keyboard implementations remain separate approval and verification work.

## Color interpretation and contrast

| Role | Current exemplar | Rationale |
|---|---|---|
| Brand | `#DB3040` | Preserve accepted identity; white on red approx 4.66:1 |
| Canvas | `#FCFBFA` | Neutral soft background for plain sections |
| Soft media | `#FFF7F2` | Object framing, not a button/Badge outline |
| Decorative line | `#F0E8E5` | Subtle divisions only, white contrast approx 1.21:1 |
| Light control stroke | `#827A87` | Higher contrast for interactive boundary, white contrast approx 4.13:1 |
| Dark control stroke | `#938B9C` | Visible on `#242329` surface, approx 4.76:1 |
| Secondary text | `#777079` | On canvas approx 4.64:1 |

Ratios are computed from opaque sRGB hex inputs; **browser focus overlays, display dimming and actual mobile UI are not certified by those calculations**. Do not darken every surface indiscriminately.

## Architectural safety / single ownership

- Global tokens, Action face geometry, control state, sheet and page geometry: `web/preview/seed-standard.css`.
- Experimental arrangements, column balance and demonstration-only alignment: `web/seed-lab/lab.css`.
- Local state transitions of the Lab preview: `web/seed-lab/lab.js`.
- Existing approved SVG, art, reward economics, application state, Android Compose and main/new unaffected.
- Do not revive the former `.gallery-button, .seed-action` alias collision. Style ownership fixes must be made at the canonical component rather than by raising page-selector specificity.

## Verification claims and outstanding gates

- A new `visual-polish.test.mjs` captures six contracts. The RED condition was checked against the previous source, then after implementation all six were evaluated against the fetched current files using equivalent JS assertions.
- Additional structural checks confirmed page-shell slots, action ownership, four Footer states, 4 specimen assets and JS syntax parsing.
- **Not yet confirmed:** full Node `node --test` run inside a checked-out repo, actual browser visual/computed CSS at 320/390/430 widths, 200% text, mobile sheet gesture boundary, IME, TalkBack and final user visual approval.
- GitHub Pages *deployment status* must be verified independently of a source commit. A successful Pages action is not proof that every visual expectation or mobile gesture passed.

## Next browser QA procedure

1. Open `/web/seed-lab/` on a phone; verify Footer none/single/paired/stacked at Min/Mid/Full, Body scroll and no content overlap.
2. Compare Shell Root/Detail/Utility: header title alignment, common gutter, plain sections and bottom boundary.
3. Check neutral Outline vs white, 21px checkbox/radio selected state and default Field border in Light/Dark.
4. Check grid, rail, rows and all original assets, ensuring no cropped art, missing glyph or one-letter heading orphan at 320px, 390px and enlarged text.
5. Recheck the original `/web/preview/` independently for legacy gallery and clan regressions. Any mismatch belongs in the evidence log before release or Android transfer.
