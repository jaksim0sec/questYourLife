# Design Seed · Open Questions / Hypotheses

> Discussion space, **not** an implementation directive. Updated: 2026-11-04.
> Each hypothesis must be tested in a working web prototype before promotion to `DECISIONS.md`.

| ID | Topic / hypothesis | Evidence / risk | Decision needed |
|---|---|---|---|
| O-001 | Product UI identity: **social quest platform** vs. current repo's restrained checklist product | Current `AGENT.md` and `IDEA.md` conflict with the new brief | Approve new product direction and version existing requirements deliberately |
| O-002 | Naming: repository's official **갓생패치** vs. discussion's **현생패치** | Existing app identifiers must stay unchanged during exploration | Final public brand and whether/when documents change |
| O-003 | Navigation IA: root destinations, number of items, persistent DM entry, profile position | SEED/Apple specify semantics, not app-specific tab labels | Flow-tested mobile information architecture |
| O-004 | Homepage: personal quest progress vs. friends/clan presence vs. next action | Checklist-only, dashboard-only and feed-only homes each miss something | Compare coherent variations with **identical underlying data** |
| O-005 | Visual concept: content-led flat UI + restrained expressive glass + concentrated reward animation | Apple discourages Liquid Glass in content layers; Haze costs real GPU time | Test against at least two alternative visual seeds |
| O-006 | Brand tokens: colors, Korean typography, radius, spacing, content density, icon grammar | Existing cream/red is a legacy implementation guideline, not a newly agreed seed | Approve tokens *after* prototype and accessibility review |
| O-007 | Content and microcopy: standard navigational words vs. branded names for activities/rewards | Personality cannot outrank comprehension or accessibility | Define vocabulary and test comprehension |
| O-008 | Social UX: friend activity visibility, clan shared goals, questbook sharing, DM entry | Teens' privacy, consent and accidental disclosure are high-impact | Agree on user-controlled sharing boundaries and safe defaults |
| O-009 | Motion rules: utility feedback vs. major achievements and reduced-motion modes | Haze/SEED document distinct motion/effect costs; excessive effects distract | Approve timing and animation budget after device-like tests |
| O-010 | Web → Android fidelity: rendering, font metrics, glass and navigation behavior | CSS glass and Haze/Compose are not pixel-perfect interchangeable | Define visual-diff acceptance, perf targets and fallbacks |
| O-011 | Prototype scope and demo content: which screen sequence, empty/offline/error states, real-vs-fixture data | Static mockups concealed broken state flows in prior attempts | Approve fixed test script + interactions + test dataset |
| O-012 | Source coverage: first-party app screenshots and Korean teen research | Some previous named-app claims are weakly sourced or changed after updates | Collect dated examples/screenshots and verify claims individually |

## Current research hypotheses (not approved)
1. **Social Quest × Expressive Minimal** is a candidate, not the chosen design seed.
2. Keep **content** visually dominant; restrict glass mainly to selected controls/navigation.
3. Favor familiar root navigation labels, inventive reward/quest language only when its meaning is clear.
4. One consistent design system can support different densities and layouts for Home / Questbook / Clan / DM.
5. Test **content-first**, **social-first**, and **game-expression-first** variants under the same flows before choosing.

## Explicitly unresolved
- How quests, friends and clans are combined on Home.
- Which social content is public, friend-only, clan-only, or private by default.
- Whether a current legacy restriction (e.g. no extensive game systems) remains a binding requirement.
- The extent of HIG/SEED/Haze documentation that has been fully inspected; do not claim exhaustive coverage.
