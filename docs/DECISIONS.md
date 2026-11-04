# Design Seed · Confirmed Decisions

> Status: confirmed only. Updated: 2026-11-04. Keep this file within 1–2 pages.
> Scope: **new design exploration**. This document does not silently supersede `AGENT.md` or `IDEA.md` for existing application development. Conflicts require explicit approval before implementation.

## D-001 · Product direction (confirmed in current design discussion)
- Primary audience: Korean teenagers; emphasize **real-life goals as quests**, friends, clans, shared participation, entertainment, and game-like rewards.
- Do not reduce the new experience to a standard to-do checklist plus decorative game elements.
- Clarity of core actions and user autonomy still matter. Quest completion is explicitly user-confirmed, not automatically asserted by AI or a timer.

## D-002 · Preserve existing system assets
- Keep the current Android app, database, authentication, API/RPC, local data/sync infrastructure and already approved graphical assets intact during design exploration.
- No destructive migration, main-branch redesign, architecture rewrite, or speculative backend expansion as part of this research task.
- A later UX implementation must evaluate compatibility and migration effects separately.

## D-003 · Web-first design agreement
- Explore and agree on the design through an **interactive, mobile-first web prototype**, not static mockups alone.
- The agreed interactive prototype and its design-token/component specifications become the **design reference** for eventual Android reproduction; the web prototype is not automatically the production app.
- Do not treat any proposed colors, typography, tabs, navigation destinations, glass intensity, screen composition, or visual style as approved until explicitly agreed.

## D-004 · Research and decision governance
- `docs/DECISIONS.md`: confirmed outcomes only; short, dated, with stable IDs.
- `docs/OPEN.md`: hypotheses, proposals, conflicting assumptions and questions.
- `docs/research/2026-11-04-gpt-design-research.md`: original prior research response, preserved without rewriting its body.
- `docs/research/SUMMARY.md`: checked takeaways, source URLs, confidence and documented gaps.
- Promoting an item from OPEN to DECISIONS requires explicit user agreement and a traceable change; research findings are **evidence**, not product approval.

## D-005 · Scope of this branch
- Documentation/research only. Keep existing source code, assets and runtime configuration unchanged.
- No merge to `main` is implied by creating these documents.

## Important naming/strategy conflicts
- Repository `AGENT.md` / `IDEA.md` currently say the public display name is **갓생패치** and emphasize a restrained checklist product, while this design discussion uses **현생패치** and a richer social-quest direction. **Neither the app name nor source-of-truth precedence is changed by this document.** See `OPEN.md`.
