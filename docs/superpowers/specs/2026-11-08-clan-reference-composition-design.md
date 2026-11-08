# Clan Preview Reference-led Composition Design

**Goal:** User-supplied 6-reference audit and a focused redesign of the existing `new/web/preview` clan root, without adding product features or changing approved D-012 tokens.

**Source:** `11444/11446/11447/11448` are Danggeun references; `11445/11449` are this preview. See [audit](../../design/seed/CLAN_REFERENCE_AUDIT_2026-11-08.md).

**Design:** Keep D-011 root nav and existing 13 routes. Top: compact fabric emblem+name+metadata, compact invitation, avatar social row, chat action. Middle: one A2 reward hero with readable copy and clearly visible chest; below: a single Progress. Below fold: text-first clan activity and separate clan exploration rail. No new backend actions.

**Chosen approach:** Intent-led composition. Rejected: pure color replacement (does not fix hierarchy), and literal Danggeun duplication (does not fit clan/reward identity and violates source boundaries).

**Non-goals:** New clan RPCs, native Android, extra reward types, added menu route, new SVG set, approved v0.15 token rewrite.

**Rules:** Existing `#clan` navigation/guest mode must work; creating/inviting/member-tab behavior preserved. Responsive 320/390/430 widths and Light mode checked separately from static source contract.

**QA target:** [Design audit](../../design/seed/CLAN_REFERENCE_AUDIT_2026-11-08.md) and design system `VALIDATION.md`.
