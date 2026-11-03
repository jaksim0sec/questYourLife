# Onboarding & Identity Integration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add an integrated first-run/onboarding and optional login journey without changing the five-tab app, losing guest data, or silently binding a guest profile to an account.

**Architecture:** Existing `MainActivity` owns the first-run gate; `DailyQuestApp` remains the only main app. Onboarding reuses `PatchTheme`, mascot and `PatchAccountPage`. Room remains the one UI read source; the existing `AccountRepository` and `ProfileRegistry` own credentials and account-specific DB binding. No new backend services or tables.

**Tech Stack:** Kotlin 2.2.20, Compose BOM 2025.09.00, Room 2.8.5 v5, WorkManager, JUnit/Robolectric, Android SDK 36/JDK 17/Gradle 8.13.

**Spec:** `docs/superpowers/specs/2026-11-03-live-product-integration-design.md` (Gates 0–1, R03–R05 and R10–R12). Later Gates 2–4 require separate implementation plans. Main implementation code is not part of this plan.

## Global Constraints

- Read `AGENTS.md`, `AGENT.md`, `IDEA.md` before edits. Keep public app label unchanged pending name decision.
- Do not alter production Supabase, its existing 9 tables, old migrations, package IDs, keystore or the established data directory.
- Onboarding is optional and must not block existing users, offline guest checklist or navigation on next launch.
- No server request or irreversible mutation occurs until a user explicitly confirms signup/signin or goal creation.
- No automatic merge of pre-existing guest records into a different account. Guest data must never disappear when login fails or a user chooses not to import.
- Keep encrypted tokens under `SessionVault`; no password persistence.
- Preserve existing UI design, five tabs, first-run stamp and accessibility. Add only routes and reusable product components.
- Regression baseline: no completed tests or APK behavior are inferred from a successful compile.

## Review Focus

1. Old installed user with existing `quests.db` and no onboarding preference enters home without a compulsory signup gate. Test: `EntryPolicyTest.oldInstallSkipsOnboarding`.
2. Fresh install process killed midway through onboarding must return to onboarding, not skip it after a DB file was created. Test: `EntryPolicyTest.incompleteFirstRunStillOpens`.
3. Guest account with saved goals and login to existing UID must not silently transfer or overwrite data. Test: `ProfileBindingTest.existingUserKeepsGuestUnbound`.
4. Wrong-password/offline sign-in must not switch active DB or persist wrong session. Test: `AccountRepository` domain regression `RoomSyncTest` plus explicit unsuccessful login manual test.
5. Switching logged-in accounts must not show other account's Room data, cached social messages or pending notifications. Test: `ProfileBindingTest.twoUsersHaveSeparateDatabases` plus UI/instrumented verification.

---

### Task 1: Deterministic first-run gate

**Files:**
- Create: `app/src/main/java/com/questyourlife/app/ui/OnboardingEntry.kt`
- Create: `app/src/test/java/com/questyourlife/app/ui/EntryPolicyTest.kt`
- Modify: `app/src/main/java/com/questyourlife/app/MainActivity.kt` only when Task 3 wires the gate.

**Interfaces:**
- `object EntryPolicy { fun showOnboarding(completed: Boolean?, existedBeforeLaunch: Boolean): Boolean }`
- `class OnboardingEntry(context: Context) { fun shouldShow(existingDatabaseBeforeLaunch: Boolean): Boolean; fun complete() }`
- Preferences `qyl_onboarding` with key `finished`. `null` means no recorded decision. Record `false` once a truly fresh first run begins, BEFORE `DailyQuestViewModel` creates its DB. Do not use `quests.db` existence after initialization for this decision.

- [ ] **Step 1: Write failing test** `EntryPolicyTest`: null+existing → false; null+fresh → true; false+existing → true; true+fresh → false.
- [ ] **Step 2: Run** `gradle --no-daemon :app:testDebugUnitTest --tests '*EntryPolicyTest*'`; Expected: FAIL because types are absent.
- [ ] **Step 3: Implement** the two classes with only preferences and a pure policy. `MainActivity` must capture preexisting `databaseList().any { it.startsWith("quests") }` before creating a view model, not while composables are rendering.
- [ ] **Step 4: Rerun** same command; Expected: PASS, all four cases.
- [ ] **Step 5: Commit** `test/onboarding: distinguish first run from existing installs`.

### Task 2: Preserve account boundaries unless explicit import

**Files:**
- Modify: `app/src/main/java/com/questyourlife/app/sync/ProfileRegistry.kt`
- Modify: `app/src/main/java/com/questyourlife/app/sync/AccountRepository.kt`
- Create: `app/src/test/java/com/questyourlife/app/sync/ProfileBindingTest.kt`

**Interfaces:**
- `suspend fun ProfileRegistry.bind(userId: String, profile: JSONObject? = null, importGuest: Boolean = false): Pair<QuestDatabase, String>`.
- `suspend fun AccountRepository.signIn(name: String, password: String, create: Boolean, importGuest: Boolean = false)`.
- If account UID is already mapped to a DB, use that mapping. Otherwise create/reuse `quests-user-$userId.db` with unbound metadata; never select the current anonymous `quests.db`/`quests-guest-*.db` without `importGuest == true`. If import is requested but current DB is already bound, reject; never move rows automatically between bound DBs.
- Validate remote `user.id` and session first; only change active file after successful binding, preserving original DB as a recoverable source. Existing Outbox order, legacy rewards, other user data stay intact.

- [ ] **Step 1: Write failing tests** `existingUserKeepsGuestUnbound`, `explicitFirstTimeImportRetainsLegacyData`, `twoUsersHaveSeparateDatabases`, `repeatedBindIsIdempotent`. Use Robolectric `@Config(sdk=[28], manifest=Config.NONE)`; create unique DB names, assert `syncDao().meta()?.userId` and original goals/records.
- [ ] **Step 2: Run** `gradle --no-daemon :app:testDebugUnitTest --tests '*ProfileBindingTest*'`; Expected: FAIL in at least the no-import branch before implementation.
- [ ] **Step 3: Implement** the overloads and enforce import preconditions. Never modify current file until an authenticated successful bind. Preserve `ProfileRegistry.guest` behavior for a separate guest after logout, without deleting any DB.
- [ ] **Step 4: Rerun** the targeted tests and `RoomSyncTest`; Expected: PASS.
- [ ] **Step 5: Commit** `fix/auth: require consent before binding guest records`.

### Task 3: Product-quality onboarding and account navigation

**Files:**
- Create: `app/src/main/java/com/questyourlife/app/ui/redesign/OnboardingPage.kt`
- Modify: `app/src/main/java/com/questyourlife/app/MainActivity.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/AccountPage.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestApp.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/QuestMainScreens.kt`

**Interfaces:**
- `@Composable fun PatchOnboardingPage(onGuest: (String) -> Unit, onLogin: (String, Boolean) -> Unit)`. Maintain a goal draft string in `rememberSaveable`; no actual goal creation until the user submits `NewGoalPanel`.
- `@Composable fun PatchAccountPage(onBack: () -> Unit, onAuthenticated: () -> Unit = {}, importGuest: Boolean = false)`; supply a confirmation step before optional import when guest data exists.
- `@Composable fun DailyQuestApp(model: DailyQuestViewModel, onOpenClassicChecklist: () -> Unit, initialGoalDraft: String? = null)`.
- `@Composable fun NewGoalPanel(onBack: () -> Unit, onCreate: (String) -> Unit, initialTitle: String = "")`.
- When onboarding finishes: mark `OnboardingEntry.complete()`, return to single `DailyQuestApp`; if nonblank draft use the existing `create` route with prefilled text; keep original home, five-tab nav, Account menu route, system bars and brand opening. Successful login returns to originating app route, never an account-only home.
- For account mode, keep the existing `PatchAccountPage` sync/conflict details reachable from account settings; first-run authentication should not show those backend details in place of onboarding.
- **Account import UI:** separate switch/confirmation with plain language. Import to a never-used UID only. Already-mapped UID displays "기기 기록은 이 기기에 남아요" and does not offer unsafe automatic merge. No ambiguous silent import.

- [ ] **Step 1: Add deterministic state/restart tests** to `EntryPolicyTest` and a Robolectric `ProfileBindingTest` for the onboarding-import choice; assert draft is not converted to a goal until explicit create.
- [ ] **Step 2: Run** targeted tests; Expected: FAIL before the state wiring.
- [ ] **Step 3: Implement** screens and routes with existing `PatchTokens` / `MascotArt`, 48dp targets, one primary CTA per step; do not introduce a full-page sign-in default.
- [ ] **Step 4: Run** `gradle --no-daemon :app:assembleDebug :app:testDebugUnitTest`; Expected: BUILD SUCCESSFUL, 0 failed unit tests. Use emulator screenshot separately for visual validation.
- [ ] **Step 5: Commit** `feat/onboarding: connect first goal, optional login and existing home`.

### Task 4: Stop leaking demo UI into real accounts

**Files:**
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestViewModel.kt`
- Modify: `app/src/main/java/com/questyourlife/app/daily/GoalRepository.kt`
- Modify: `app/src/main/java/com/questyourlife/app/daily/DailyQuestRepository.kt`
- Create/modify: `app/src/test/java/com/questyourlife/app/daily/DailyQuestRepositoryTest.kt`

**Interfaces:**
- Disable automatic `seedDemoIfNew()` call on first entry. Keep historical seed rows/data intact; do not erase them or relabel their progress.
- Daily generation must prioritize real accepted plan occurrences, carrying unfinished items forward; the `if(countForDay==0) insert three arbitrary local templates` branch should not run automatically for new empty users. New guest can still add a genuine manual goal/quest.
- Do NOT change server recurrence behavior. Exact application-level daily suggestion policy is covered by separate Gate 2 plan, so this task removes misleading unconditional three item seeding only; no new AI placeholder generation.

- [ ] **Step 1: Add failing test** `emptyGuestHasNoFabricatedDailyRewards` and `existingPendingDailySurvivesRefresh`; ensure no automatic demo goal, no additional fixed three daily entries when none were created.
- [ ] **Step 2: Run** test; Expected: FAIL before removing seeding.
- [ ] **Step 3: Remove** production calls to automatic demo fill only; do not change accepted quest completion or migrations.
- [ ] **Step 4: Run** targeted + complete Android JVM suite; Expected: PASS.
- [ ] **Step 5: Commit** `fix/quests: remove automatic fake daily and seed generation`.

### Task 5: Gate 1 verification

**Files:**
- Test: `app/src/test/java/com/questyourlife/app/sync/ProfileBindingTest.kt`
- Test: `app/src/test/java/com/questyourlife/app/ui/EntryPolicyTest.kt`
- Existing: `app/src/test/java/com/questyourlife/app/sync/RoomSyncTest.kt`, `CorrectionRoomTest.kt`
- Optional: `.github/workflows/android-apk.yml` if PR job does not currently run tests.

**Interfaces:** No new APIs. Verify the gate's existing interfaces.

- [ ] **Step 1: Run** `gradle --no-daemon --max-workers=2 :app:assembleDebug :app:testDebugUnitTest`; Expected: BUILD SUCCESSFUL, zero failures.
- [ ] **Step 2: Inspect** changed-file diff and test that credential data is never logged or stored in preferences/Room; Expected: no secret key/password persistence.
- [ ] **Step 3: Manually verify** brand opening → onboarding → guest → original five tabs, return after restart, existing database bypass, online sign-in and failure. Expected: screenshots show matched visual hierarchy, no mandatory login. If emulator is not available mark `UI NOT VERIFIED`, not complete.
- [ ] **Step 4: Commit** any only-necessary fixes, and record test/verification evidence.

## After Gate 1

Gate 2 (`R01,R02,R06,R10,R11`) unifies home/quest/chapter actual data and removes `QuestPreviewCatalog` from production surfaces. Gate 3 (`R07`) connects persistent Gemini DM to a server-side approved host; Gate 4 (`R08,R09`) maps real `SocialClan`/ledger/inventory into the same illustrated clan/profile/chest UI; Gate 5 (`R12`) performs screenshot, offline, multi-account and release evidence. They each require a separate, reviewable plan; never mark them complete because Gate 1 builds.

**Implementation method:** User requested direct native execution, not a Codex handoff. Never force-push or merge to `main` without reviewing CI and user-visible impact.
