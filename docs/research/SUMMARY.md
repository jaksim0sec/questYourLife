# Design Research · Verified Summary & Gap Audit

**As of:** 2026-11-04 (Asia/Seoul)  
**Status:** evidence and design hypotheses, **not an approved design seed**.  
**Original response:** [2026-11-04-gpt-design-research.md](./2026-11-04-gpt-design-research.md) (verbatim archival copy, including chat-only `<Cite>` / `<Entity>` / UI tags).  
**Governance:** [DECISIONS](../DECISIONS.md) / [OPEN](../OPEN.md).

## Confidence and provenance

- **High (H):** directly verified on first-party Apple / SEED / Haze / Android documentation. This establishes the documentation's recommendation, **not** the suitability of a particular product design.
- **Medium (M):** interpretation supported by several relevant examples, awaiting in-product usability or accessibility tests.
- **Low (L):** named-app generalization, media report, aesthetic preference or claim not independently corroborated with a durable primary reference. Never promote to a fact or decision on this basis alone.
- **Not verified (NV):** needs an original screenshot, dated release note, experiment or source accessible outside the chat.

## 1. Verified findings and their design meaning

| ID | Evidence / finding | Confidence | Implication, not a decision | Source |
|---|---|---|---|---|
| R-01 | Apple distinguishes Liquid Glass (controls/navigation floating above content) from standard content-layer materials; warns against indiscriminate use inside content. | H | Evaluate glass for a limited number of navigation/functional surfaces, not every quest card. | [Apple Materials](https://developer.apple.com/design/human-interface-guidelines/materials) |
| R-02 | SEED defines **Tabs for navigation**, **Segmented Control for manipulating/filtering the current view**. | H | Do not use a segmented control to simulate unrelated full sections; keep it adjacent to the content it affects. | [SEED Segmented Control](https://seed-design.io/components/segmented-control), [Tabs](https://seed-design.io/components/tabs) |
| R-03 | SEED Segmented Control guideline: **2–4 choices**, short labels; avoid multiple such controls on one screen. | H | A week/month filter might fit; four or more parallel content destinations likely need another pattern. | [SEED Segmented Control](https://seed-design.io/components/segmented-control) |
| R-04 | SEED Bottom Navigation serves root-level navigation, supports selected state/badges and recommends concise Korean labels (<=5 Korean characters). | H | A real root-destination model is required before choosing the number or labels. SEED's component anatomy is not an instruction to force exactly five tabs. | [SEED Bottom Navigation](https://seed-design.io/components/bottom-navigation) |
| R-05 | SEED Top Navigation separates root and deeper screens, with **Back** (previous screen) distinct from **Close** (exit a layer or flow); history and hierarchy back differ. | H | Prototype real back behavior, modal dismissal, notifications/deep links and interrupted quests, not just header visuals. | [SEED Top Navigation](https://seed-design.io/components/top-navigation) |
| R-06 | Haze 2.x provides separate Blur/Glass modules, refraction, optics, accessible transparency fallbacks and glass container merging; supports Android API 23+, but effective blur/fallback depends on platform and configuration. | H | Feasible native candidate, **not** evidence of CSS-equivalent pixel fidelity or smoothness on target devices. | [Haze Overview](https://chrisbanes.github.io/haze/latest/), [Glass](https://chrisbanes.github.io/haze/latest/effects/glass/), [Containers](https://chrisbanes.github.io/haze/latest/effects/glass/containers/) |
| R-07 | Haze performance costs grow with affected area, effect count and animated/scrolled sources. Its 2026-11-02 changelog describes stable glass modules, new containers and reduced-transparency support. | H | Decide glass budget only after mobile-like web and **physical Android** benchmarks. Don't hardcode a version without compatibility checks. | [Haze Performance](https://chrisbanes.github.io/haze/latest/performance/), [Changelog](https://chrisbanes.github.io/haze/latest/changelog/) |
| R-08 | SEED's writing guidance favors familiar words, user-action wording, concise text and helpful recovery instructions; its voice is clear and considerate. | H | Inventive game vocabulary should be concentrated in understood experiences, not obscure essential navigation, destructive actions or errors. | [Voice & Tone](https://seed-design.io/foundations/voice-and-tone), [Writing](https://seed-design.io/foundations/writing) |
| R-09 | SEED distinguishes brief micro-motion from page-level macro-motion, and includes reduced-motion guidance and skipping extended effects. | H | Measure utility feedback and major reward moments separately; animations must not block completion or accessibility. | [Motion](https://seed-design.io/foundations/motion), [Inclusive Design](https://seed-design.io/foundations/inclusive-design) |
| R-10 | Safe area insets vary by edge, orientation and system UI, including after the screen is shown. | H | Fixed top/bottom bars and popovers must account for dynamic insets, including web phone previews. | [SEED Safe Area](https://seed-design.io/patterns/safe-area) |
| R-11 | Material 3 Expressive adds more expressive themes, shapes, typography and motion; Android Compose supports native adaptable UI. | H | The Android adaptation may use Material behavior under custom visual tokens; expressive does not require adopting every Material aesthetic. | [Android Material 3](https://developer.android.com/develop/ui/compose/designsystems/material3) |
| R-12 | Apple tab bars support navigation among top-level sections, not actions; state is preserved as people move between sections. | H (available indexed English excerpt; dynamic page access limited) | Keep 'create quest' or 'complete' as a distinct action, not a fake navigation destination unless explicitly designed as one. | [Apple Tab Bars](https://developer.apple.com/design/human-interface-guidelines/tab-bars) |
| R-13 | 치지직 uses branded **치즈** as a streaming-support item, confirming that distinctive product vocabulary can have functional meaning. | H for '치즈' only | Develop original quest/reward vocabulary only where meaning is taught and consistent. | [Naver CHZZK Help · 치즈](https://help.naver.com/service/23946/contents/22580?osType=COMMONOS) |

## 2. Product-level analysis (inferences, **not** verified universal rules)

| Hypothesis | Confidence | What would validate it |
|---|---|---|
| People open entertainment/social apps to encounter content or friends, whereas transactional apps prioritize task completion. User intent may explain IA better than demographic labels alone. | M | Compare real app entry states and run tasks with Korean teen testers; do not claim all products obey this split. |
| Social quest experiences need **a visible personal journey + next useful action + sense of friends/clan presence**. | M | Test home variants with identical quest, clan and friend content; assess comprehension and first-action speed. |
| **Social Quest × Expressive Minimal** is a plausible candidate seed, combining content-led flat surfaces with restrained glass and strong reward moments. | M | Compare against content-first, social-first and game-expression-first alternatives under the same test script. |
| Familiar labels for core navigation and distinct game terminology for quest/reward content could provide novelty without disorientation. | M | Test interpretation of labels and first-use success; evaluate accessibility language. |
| Excessive cards, repeated progress metrics and fake manual buttons make the app feel like a dashboard rather than a living social platform. | M (prior design feedback, not a quantified industry fact) | Prototype audit for repeated facts, tap counts and missing states. |

## 3. Earlier research claims requiring correction or tighter framing

1. **setlog July 2026 layout reversal:** the archived response describes a specific backlash and rollback. A durable dated original source was **not re-established in this verification pass**; treat the claim as **L/NV**, not as proven evidence. Keep the original text unchanged and do not circulate the anecdote as a fact without the actual source.
2. **Bump, setlog, Instagram, Threads, Naver Webtoon, POSTYPE, TVING, KakaoTalk, Toss, YPT, etc.:** the archived comparisons are *qualitative working interpretations*, **not** an audited current-screen inventory. For each app, collect a dated first-party reference, actual screenshot, user task, labels, navigation map and differences across platforms before claiming design consensus.
3. **CHZZK terminology:** '치즈' verified directly; names such as '통나무 파워' or '같이보기+' were not independently verified in this pass. Do not elevate them to confirmed findings.
4. **Apple 2026 WWDC speaker claims:** archived references to specific talks and subjective wording were not independently resolved to a direct video/transcript URL here. Apple Materials and Tab Bars guidance was separately corroborated; don't conflate them with unverified talk quotations.
5. **'2026 navigation fundamentally changed':** evidence supports changes in **presentation, materials, motion and interaction**, not proof that navigation's basic semantic roles were replaced. Design must still follow destination/action/selection intent.
6. **Haze latest:** Haze's versioned site and cached search excerpts sometimes surfaced different dates/versions; the **current opened** `/latest/changelog/` lists 2.1.0 dated 2026-11-02. Pin a release version only during actual Android implementation, after Gradle/Compose compatibility review.
7. **'All documents read':** the original answer explicitly did **not** claim full coverage. This pass inspected the available official component trees and selected relevant guides; no exhaustive Apple HIG / SEED component-by-component audit is claimed. Apple's Korean HIG route is JavaScript-gated; some English pages have a Markdown fallback that the web reader rejects as `text/markdown`.

## 4. Missing coverage discovered (audit)

| Gap ID | Missing evidence / validation | Severity | Follow-up criterion |
|---|---|---|---|
| G-01 | **Complete screen-level IA and behavior**: root vs nested nav, tab state restoration, back/close, deep links, DM entry, quest flow and modal behavior | Critical | One agreed end-to-end clickable web flow, not screenshots |
| G-02 | **UI grammar**: typography ramps, color roles, spacing and density, icon stroke/radius, tokens and component states | Critical | Token source + visual reference states + usage rules *after design agreement* |
| G-03 | **States and data**: new user, empty, offline, loading, error, invite, expired, rewards-pending, cancellation, undo and completion confirmation | Critical | Named fixture and state matrix; no pretend live backend data |
| G-04 | **Audience research**: current 2026 Korean youth-facing apps, first-party screenshots, permission changes, dated UX examples, actual teen comprehension | High | Evidence catalog with URL, capture date, screen purpose and explicit observation vs interpretation |
| G-05 | **Privacy/safety**: minors' friend/clan visibility and consent, profile/quest exposure, chat controls, social competition pressure | High | User-controlled sharing policy with private defaults and safety review |
| G-06 | **Visual fidelity** web → Android: font metrics, line breaks, gradients, glass alternatives, icons/mascot/currency consistency | High | Device-like screenshot comparisons and audited asset mapping |
| G-07 | **Accessibility**: touch target, screen reader order, contrast, reduced motion/transparency, text scaling, safe areas | High | Explicit keyboard/TalkBack/manual checklists and accessible state audit |
| G-08 | **Performance**: low-end Android, Haze blur/glass scrolling and first render, memory/GPU cost, 60/120 Hz | High | Real-device release-like measurements; screenshots alone insufficient |
| G-09 | **Brand language**: natural teen Korean without forced slang, game terminology, reward labels, clan and notification voices | Medium | Terminology glossary + blind comprehension check |
| G-10 | **Repository contradiction**: existing `AGENT.md` / `IDEA.md` prioritize restrained checklist/old colors/brand; new brief emphasizes social-game platform | Critical before implementation | Explicit owner approval and documented precedence/migration path |
| G-11 | **Design-system sources not fully inspected**: full HIG and SEED component coverage, motion/typography detail, Haze target-version compatibility | Medium | Targeted reading when selecting corresponding components, with dated source snapshot |

## 5. Next prototype evaluation protocol (proposal)

- Compare **three presentation directions** with the **same fixtures, wording, paths and tasks**, changing only intended design dimensions.
- Minimum script: landing/Home → choose or accept quest → explicit user completion → earned reward/undo → questbook/chapter progress → clan contribution → friend's shared activity → DM → return to Home.
- Capture state and view hierarchy at identical viewport sizes. Track navigation path, duplicate information, important actions, visual hierarchy, first-use comprehension, accessibility and motion comfort.
- Separate **functional acceptance** (correct state transitions, no false completion) from **visual acceptance** (agreed spacing, weight, icon/asset and motion fidelity).
- Final web design should produce a **locked specification** of tokens, component anatomy, variants, interactions, states and reference screenshots; Android work must not interpret ambiguous mockup pixels as product decisions.
- Preserve existing assets and system throughout exploration, as recorded in DECISIONS; no branch merge without explicit agreement.

## 6. Primary source index (verified opened / inspected)

**Apple**  
- [Human Interface Guidelines, Korean entry (JS-gated)](https://developer.apple.com/kr/design/human-interface-guidelines/)  
- [Materials · Liquid Glass guidance](https://developer.apple.com/design/human-interface-guidelines/materials)  
- [Tab bars · navigation intent](https://developer.apple.com/design/human-interface-guidelines/tab-bars)  
- [Segmented controls](https://developer.apple.com/design/human-interface-guidelines/segmented-controls) — Markdown/dynamic page not fully retrievable in this pass

**SEED · Daangn**  
- [Components index](https://seed-design.io/components) · [Foundations index](https://seed-design.io/foundations) · [Patterns index](https://seed-design.io/patterns)  
- [Bottom Navigation](https://seed-design.io/components/bottom-navigation) · [Top Navigation](https://seed-design.io/components/top-navigation) · [Tabs](https://seed-design.io/components/tabs) · [Segmented Control](https://seed-design.io/components/segmented-control)  
- [Writing](https://seed-design.io/foundations/writing) · [Voice and Tone](https://seed-design.io/foundations/voice-and-tone) · [Motion](https://seed-design.io/foundations/motion) · [Inclusive Design](https://seed-design.io/foundations/inclusive-design) · [Safe Area](https://seed-design.io/patterns/safe-area)

**Haze & Android**  
- [Haze root](https://chrisbanes.github.io/haze/latest/) · [Glass](https://chrisbanes.github.io/haze/latest/effects/glass/) · [Glass containers](https://chrisbanes.github.io/haze/latest/effects/glass/containers/) · [Performance](https://chrisbanes.github.io/haze/latest/performance/) · [Changelog](https://chrisbanes.github.io/haze/latest/changelog/)  
- [Android Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)  
- [Naver CHZZK '치즈' first-party support](https://help.naver.com/service/23946/contents/22580?osType=COMMONOS)

## 7. Repository context verified on 2026-11-04

- Reviewed `AGENTS.md`, `AGENT.md`, `IDEA.md`, `README.md`, and existing `docs/` tree on `main`.
- `main` keeps production/design contradictions; this research branch does **not** rewrite old policy or implementation code.
- The original response contains chat-specific citation identifiers rather than portable URLs. They were **intentionally left untouched** in the archive to preserve provenance. Use the primary URLs above instead for follow-up reading.
- No live UI, task usability, screenshot comparison, device runtime or production backend was tested during **this documentation task**. This is explicitly a research/source audit, not an app release gate.
