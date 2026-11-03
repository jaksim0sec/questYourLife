# Live Quest UI Integration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make home, questbook list, and illustrated chapter journey display and act on actual Room goals, stable steps, daily executions and confirmed social snapshots without any separate demo journey.

**Architecture:** Reuse the existing five-tab Compose navigation and branded visuals. Convert Room `GoalEntity` + `GoalStepEntity` (+ linked daily quests) to a small immutable display projection with stable IDs; bind both list and chapter screen to that projection. Continue writing through existing `DailyQuestViewModel` / repositories / `SyncCapture`, never through `QuestPreviewStore`. Read popular books and clan from `SocialStore`, not fabricated catalogs. No new tables.

**Tech Stack:** Kotlin 2.2.20, Jetpack Compose, Room v5, WorkManager, Supabase existing RPC; JUnit/Robolectric and GitHub Actions.

**Spec:** `docs/superpowers/specs/2026-11-03-live-product-integration-design.md`, Gate 2 / R01 R02 R06 R10 R11.

## Global Constraints

- Read `AGENTS.md` → `AGENT.md` and `IDEA.md`. Preserve existing design language, nav and package.
- Work only in `feat/superpowers-live-onboarding` / draft PR #6; don't merge into main or deploy Supabase.
- Room Flow and `SocialStore` snapshot own live content. Personal cash and server-confirmed clan scores are never combined.
- A finished *main quest* is a manual user action, not an animation/timer side effect; never invent a chapter chest claim entitlement.
- Local step grouping uses stored `GoalStepEntity.chapter` and stable `syncId`. If a chapter title isn't materialized, label it `챕터 N` instead of making one up.
- No more `QuestPreviewStore` on the live home/questbook/chapter paths. Historical preview records may remain stored but must not appear as real progress.
- Existing goals/completions, Outbox idempotency, migration history and clock behavior stay unchanged.
- The private user's goals are never uploaded to public popularity or clan ranking surfaces.
- Build+unit-test pass alone is not UI screenshot or real device verification.

## Review Focus

1. Empty new guest renders an intentional goal CTA, no fake clan, coins, book or daily challenge.
2. A current Room goal with two chapters and stable completed/incomplete steps draws exactly those chapters, correct next action and progress, after restart.
3. A shared book joined from server is projected into Room and appears under the same quest path as a personal goal; no duplicate preview.
4. Completing a step routes to the existing transactional `completeStep` method; never changes `QuestPreviewStore` or issues fake free cosmetic rewards.
5. An existing member with offline cached `SocialSnapshot` sees truthful last-known clan/progress; a guest never sees fake members, rank or target.

---

### Task 1: Real quest path projection

**Files**
- Create: `app/src/main/java/com/questyourlife/app/ui/redesign/LiveQuestPath.kt`
- Create: `app/src/test/java/com/questyourlife/app/ui/redesign/LiveQuestPathTest.kt`

**Interfaces**
- `data class LivePathMission(val id: String, val title: String, val completed: Boolean, val reward: Int, val dailyId: String?, val dailyAccepted: Boolean)`
- `data class LivePathChapter(val number: Int, val title: String, val missions: List<LivePathMission>)`
- `data class LivePathBook(val id: String, val title: String, val chapters: List<LivePathChapter>)` with `val done: Int`, `val total: Int` and `fun current(): LivePathMission?`.
- `object LiveQuestPath { fun from(goal: GoalEntity, steps: List<GoalStepEntity>, daily: List<DailyQuestEntity> = emptyList()): LivePathBook }`
- Use exactly the goal's existing nondeleted stable steps sorted by chapter+position, and only associated `daily.stepSyncId`/legacy goalId+stepPosition. No assumed durations or rewards.

- [ ] Write failing `LiveQuestPathTest`: grouping, exclusion of deleted steps, status from completedAt, current noncompleted, stable IDs, null daily, empty goal.
- [ ] Run `gradle --no-daemon :app:testDebugUnitTest --tests '*LiveQuestPathTest*'`; Expected RED: unresolved projection.
- [ ] Implement immutable projection and pure model tests.
- [ ] Rerun targeted and full unit suite; Expected PASS. Commit `feat(quest): project real room journey without preview state`.

### Task 2: Single actual-data home

**Files**
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/HomePage.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/SocialPages.kt` only to remove unused `SocialHomePage` duplicate entry if safe.
- Test: `app/src/test/java/com/questyourlife/app/ui/redesign/LiveQuestPathTest.kt` for home presentation empties.

**Interfaces**
- Existing `PatchHomePage(model, onDaily, onQuests, onClan, onPreview, onMy)` retained. Reads `model.goals, model.steps, model.quests, model.cash, model.social, model.ownProfile`.
- Always draw same reference hierarchy: real profile avatar/name/cash, editorial hero and optional real clan chest, resume CTA using actual goal ID, server popular books only if present, real daily CTA and clan access.
- No `if(signedIn) SocialHomePage`; no `QuestPreviewCatalog`, `model.clanPreview`, `QuestFaces` with fake members.
- A guest with no goal -> '첫 목표 만들기' linking to `onQuests`; clan absent -> join CTA; no invented rank/target.

- [ ] Write or extend a failing pure test proving zero fake progress for empty data and actual completed fraction.
- [ ] Verify RED; then replace *data bindings* within existing layout instead of generic cards/forms.
- [ ] Compile/full suite via PR CI; expected PASS. Commit `feat(home): use actual goals and social snapshot in shared design`.

### Task 3: Actual-data quest list and join

**Files**
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/QuestsPage.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestViewModel.kt` if moving direct Composable DB transaction to event.
- Test: `app/src/test/java/com/questyourlife/app/ui/redesign/LiveQuestPathTest.kt` / existing `CorrectionRoomTest`.

**Interfaces**
- Existing `PatchQuestsPage(model, onPreview, onChapter, onGoal, onDaily, onCreate)` retained. `onPreview` now receives actual `GoalEntity.id`, `onChapter` gets the same ID and chapter number string.
- Popular discovery comes from `model.social?.popularBooks`. `model.joinPublicBook(id: String)` owns existing `book_join` Outbox + scheduler, validates ID and prevents duplicate requests. Composable only emits intent.
- Preserve main/daily/completed tabs and timeline/chapter chips, but no `QuestPreviewCatalog`, preview rewards, `ChapterJourney` or repeated '시연' disclaimers. Empty states should keep one salient CTA.
- Signed-in, cached offline / no results: don't invent popular participants.

- [ ] Test real quest progress/empty state; verify expected RED if newly introduced pure mapping is missing.
- [ ] Wire actual goals and stable steps into cards and chapter timeline; move join transaction into ViewModel.
- [ ] Compile/full suite via PR CI; expected PASS. Commit `feat(quests): render real joined and personal books`.

### Task 4: Actual-data chapter path

**Files**
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/ChapterPage.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestApp.kt` only for route names/book ID semantics.

**Interfaces**
- Existing `PatchChapterPage(model, bookId, initialChapterId, onBack, onMenu)` retained but `bookId` resolves Room `GoalEntity.id`; `initialChapterId` is optional `chapter-<number>`.
- Reuse the reference geometry, 5-tab nav, yellow dashed path, dark timeline, chest artwork and state sheets. Mission click: if linked daily exists and not accepted, `model.accept(dailyId)`; otherwise `model.completeStep(stableSyncId)` only after explicit completion click. No preview-store completion or free cosmetic claim.
- Completed chapter shows actual achievement and progress; reward only where server grants it. In absence of server entitlement, chest art is decorative with neutral label and no 'claim' CTA.
- `QuestRouteSheet` and `ChapterJourney` must use real `LivePathBook` or be removed when their preview-only implementation becomes unused.

- [ ] Test path selection and next mission from `LiveQuestPathTest`. Verify RED for any missing view-state predicate.
- [ ] Implement by adapting existing path components without introducing new data source.
- [ ] PR CI compile/full unit suite; expected PASS. Commit `feat(chapter): bind illustrated path to real quest IDs`.

### Task 5: Regression and audit

**Files:** previous test files, UI files above, no backend migration.
- [ ] Run full PR CI `gradle --no-daemon --build-cache :app:assembleDebug :app:testDebugUnitTest`, expect zero failures and read job logs.
- [ ] Audit for remaining preview refs on live home/quest/chapter, security-sensitive data in public, and double credit on rapid taps.
- [ ] Verify empty, guest and signed-in screens by emulator screenshot (or mark NOT VERIFIED); check large fonts and TalkBack minimum 48dp.
- [ ] Keep PR draft until product UI is visually checked and Gate 3–4 are separately planned/executed. Log any unverified item.

## Gate 3 and Gate 4 next

Gate 3 needs a separate plan for authenticated persisted DM / Gemini plan draft (existing `AGENT.md` currently requires Supabase Edge; user previously preferred Vercel). Gate 4 needs a separate plan to unify `ClanPage` and `SignedInClanPage` with real clan members/weekly scoring/chest rights and actual `CosmeticCatalog` inventory, without fake member data. Only run them after Gate 2 is independently verified.
