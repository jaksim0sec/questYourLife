# SEED Web Lab · Implementation Plan

> **For agentic workers:** Use Superpowers executing-plans, test-driven-development, and verification-before-completion. This plan records the approved experiment scope and the actual verification boundary.

**Goal:** Build a web-only SEED experiment environment to test multiple composition patterns and shared component contracts before implementing any clan or product page.

**Architecture:** The original `web/preview/seed-standard.css` remains the one shared web stylesheet. The isolated `web/seed-lab/` fixture imports it and has only fixture composition rules in `lab.css`. Layout selection, progress state, SVG manifest, and bottom-sheet behavior are implemented in browser JS without backend coupling.

**Tech stack:** Static HTML, vanilla CSS/JavaScript, Node.js built-in `node:test`.

**Spec:** `docs/design/seed/implementation/v015.md`, `docs/design/seed/patterns/layout-composition.md`, `docs/design/seed/patterns/page-composition.md`, `docs/design/seed/implementation/shared-style-contract.md`.

## Global constraints

- Single existing work branch `feat/seed-layout-composition-20261110`; do not touch `new`, `main` or Android code.
- Approved 56 SVG files and registered 8 PNG remain unchanged. Do not invent icon geometry or rescale source assets.
- Approved v0.15 sizes and colors are the lab baseline, rather than legacy preview overrides.
- A1 item art is not an A2 promotional scene. Keep real state values and ARIA tied to the same input.
- No clan page, app navigation, fake server behavior, reward grant, user account, new component hierarchy or generator.

## Task ledger

| ID | Work | Acceptance | Status |
| --- | --- | --- | --- |
| L1 | Shared component CSS adapter | Standard classes consume approved tokens; old declaration body retained; lab imports this CSS | Implemented; browser visual QA pending |
| L2 | Pattern fixtures | Grid / Rail / Rows from the same object data; Plain / Solid comparison | Implemented; browser visual QA pending |
| L3 | Interaction fixtures | Progress edge cases, thickness, actions, selection, field, sheet handle | Implemented; sheet body-boundary handoff pending |
| L4 | Asset validation | 8 existing PNG + SVG manifest 56; no duplicate binary assets | Implemented; live image decoding/404 QA pending |
| L5 | Automated verification | Zero-dependency `node --test web/seed-lab/*.test.mjs` and independent browser QA | Static source checks passed in connector; Node project-run and browser QA pending |

## Review focus

1. Browser 320/360/390/430, then 768/1440: no clipped content or horizontal document overflow.
2. Light and Dark official v0.15 contrast and reward CTA white on dark gold.
3. Interaction and keyboard focus: segmented choice, tab arrow keys, disabled/unavailable state, sheet open/resize/close.
4. SVG/PNG real network resolution from repository root static server.
5. Body scrolling at sheet boundary: do not infer full C-SHEET gesture compliance from working handle drag.

## Execution notes

- The experiment uses a new independent fixture page, but only one shared stylesheet for component appearance.
- Previous preview is not a visual preservation target; it is not deleted.
- Passing source inspection and pure progress unit tests does not establish pixel-level browser correctness.
- New variants not documented in v0.15 are not adopted. Where detailed Surface skins are absent from the contract, the visual here is an experimental comparison, not a new design approval.
