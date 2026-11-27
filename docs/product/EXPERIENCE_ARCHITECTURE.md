# Experience architecture · product-page design brief

Scope: active new2 web demonstration. User agreed to Home / Quests / Explore / Clan / My and DM as a supporting destination, then requested all page purposes and flow design in one pass. This is the coordinated working proposal for page production, not approval of every section, backend policy or final production route. Product principles remain in the six product documents and D-001/006–009; visual grammar remains in SEED.

## Product objective

Turn a real intention into an achievable action, make today's next step quick, connect optional discovery and cooperation to real action, and let earned results become personal expression. The app should be useful without social participation; social, content and customization add meaning without turning life into compulsory competition. No unrelated mini-games, compulsory check-ins or automatic exposure of private goals. Completing a real action is self-confirmed; AI drafts and user accepts/edits. No invented payout rules, real-money equivalence or randomized acquisition policy.

## Root purposes and content order (review proposal)

| Root | User's question / successful outcome | First view → supporting content | Destination boundaries |
|---|---|---|---|
| 홈 Home | What should I do now? Can I act immediately? | Today summary with remaining actions → short actionable quest list → relevant shared challenge or friend update → a small discovery entry if useful | Owns daily execution, not complete goals, feed, inventory or rankings. One-tap self-confirm/undo; empty day offers create or browse, not guilt. |
| 퀘스트 Quests | What am I trying to achieve, and how do I organize it? | Active personal goals/books and their next steps → add/create from a real intention → participated books → completed/paused history | Owns personal planning and lifecycle; public discovery is Explore. AI creation is a dedicated task, review before accept; not automatic completion. |
| 탐색 Explore | What could I try next and what are others creating? | Search/discovery entry → featured user-created books or events → comparable book list with author and meaningful participation info → categories as needed | Owns public book discovery, not personal checklist. Book details explain scope, creator, expected effort and participation; preview/modify before joining. No invented popularity algorithm. |
| 클랜 Clan | What are we doing together and how can I contribute? | My clan identity and shared challenge/progress → next contribution → members and optional ranking/community entries | Owns group participation and belonging. No member: find/join/create entry with privacy-aware explanation. No automatic sharing of private tasks, no punitive inactivity. |
| 마이 My | What have I achieved, and how do I express myself? | Large reusable mascot/identity with equipped items → personal achievements and earned assets → wardrobe/shop and activity record entries | Owns self-expression, not settings. Use real art assets, consistent avatar composition; separate owned/equipped/available. Cash, boxes and prices remain policy-open. |

Home vs Quests: daily actions vs the durable plan. Quests vs Explore: my participation vs content discovery. Clan vs Explore: established relationships vs open content. My vs Home: accumulated identity vs next action. DM is a communication tool, not a separate feed or primary destination.

## Cross-page flow and ownership

| Scenario | Route and decision points | What changes |
|---|---|---|
| First use / no goals | Home empty → Quests create OR Explore book → review → confirm → Home next action | User-approved personal plan only |
| Personal goal | Quests → explain intention/context → AI draft (service boundary) → edit and accept → goal detail → Home | A draft is not a committed plan; self-confirmed completion separate |
| Daily execution | Home → check/undo a task or open its detail → optional earned result → My | Same personal quest state across Home/Quests. Do not manufacture currency amounts. |
| Public book | Explore → book detail → preview/adapt personal version → confirm participation → Quests/Home | Original remains separate; public/private defaults and version policy still open |
| Clan challenge | Clan → challenge detail → opt in → eligible personal next action → group progress when consented | Personal record remains private by default in demo; actual eligibility/share policy open |
| Expression | My → wardrobe → choose owned item → preview → apply/undo → identity reused on social surfaces | Demo appearance only until persistent inventory service exists |
| Social contact | Root hamburger menu → DM inbox → conversation; contextual profile links may later open same destination | DM preserves caller; no message transmission in mock. Block/report/permissions required before real use. |
| Notifications/settings | Root bell → notification notice/list; hamburger → DM/display settings | No invented unread counts; settings separate from My. |

## Screen inventory and shell types

Global roots are the five approved slots. Standard Global Inner keeps owning root active for goal detail/history, book detail/search, clan detail/discovery, wardrobe/record/shop browsing and public profile views. Non-Global hides the bar during goal draft/review, joining confirmation if it is an independent flow, DM inbox/conversation, or other focused editing. A Bottom Sheet is for limited selections/confirmation/settings over the parent, not a substitute for a substantial content page. Back restores parent state. Exact deep-link and system Back handling remain O-003.

## Visual and interaction direction

Use the current approved common header/footer and achromatic UI, brand CTA only where it communicates meaningful emphasis, original mascot and reward art where relevant. Home is a mixed but action-first layout; Quests is organized retrieval; Explore is discovery-led; Clan is group-context first; My is identity-led. Do not force identical cards, a repeated hero or generic dashboard across all roots. Keep actual content before section decorations. Use existing typography/surface/row/CTA patterns, no new design-system family. The public book catalog, clan members, messages, inventory and rewards are explicitly representative fixture data until connected.

## Production order and acceptance

1. Establish approved IA and shared DM route (current shell).
2. Home + personal quest state contract, then Quests plan/detail; one coherent execution flow.
3. Explore + book detail/participation route; preserve origin and personal plan boundary.
4. Clan + group challenge/member states, optional participation.
5. My + shared avatar/wardrobe, earned-result connection.
6. Cross-root navigation, empty/error/loading, accessibility, visual review; connected services and native integration remain separate.

Each page design should be judged by its own primary task, not merely by whether it uses SEED. Check at mobile widths including 320 and 390, light/dark, text enlargement, accessible target names, no broken destinations, no fabricated backend success. Real economic, privacy, AI, data and messaging contracts remain open. User review before final visual or production approval.

Related: [Vision](VISION.md), [Quests](QUESTS.md), [Questbooks](QUESTBOOKS.md), [Social](SOCIAL.md), [Rewards](REWARDS.md), [Avatar](AVATAR.md), [Decisions](../DECISIONS.md), [Open](../OPEN.md), [Page composition](../../seed/patterns/page-composition.md).
