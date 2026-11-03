# Live Clan and One-Time Invitation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans and superpowers:test-driven-development task-by-task.

**Goal:** Replace separately rendered demo/member clan pages with the same illustrated real-data clan view and make the existing one-time server invitation token reach the user instead of being discarded.

**Architecture:** Render a unified `PatchClanPage` from `DailyQuestViewModel.social` and `SocialSnapshot`, with a consistent empty/onboarding state and action sheets. All membership updates remain through `SocialStore.command` / Outbox; token creation is a special direct authenticated RPC because `questyourlife_social_command(clan_invite)` returns the raw secret once, outside the event receipt. Consume it in memory and expose only on a user-triggered copy/share action. Server remains authority for weekly rank and chest claims. No new tables, migrations or provider.

**Stack:** Android Kotlin/Compose, Room v5, existing Supabase social RPC, JUnit/Robolectric/CI.

**Spec:** `docs/superpowers/specs/2026-11-03-live-product-integration-design.md` Gate 4 / R08–R10,R12. Prioritize clan before Gemini Gate 3 because live clan UX regression is explicitly reported, while the AI hosting boundary conflicts with `AGENT.md` and requires separate decision.

## Global Constraints

- Read `AGENTS.md`, `AGENT.md`, `IDEA.md`. Don't modify public app name or backend schema.
- Do not show the demo clan, fake member names, fake rank, fake currency, or `PATCH-DEMO` to normal users.
- Keep the original screenshot hierarchy: flag/name → dark chest panel → avatar row → weekly progress/rank → contextual action sheets.
- Account is optional; unregistered users get a coherent invitation to join but no private social data.
- Only actual `SocialSnapshot` values may populate member count, outfits, rank and score. Missing values stay unknown, never 0 or fabricated.
- Server chest policy in migration 003 is `chest_policy.threshold` and `reward`; the `chests` object keys are settled-week Monday dates, not a scalar `target` field.
- Rejected or repeated claims must never increase balance or display "received" unless a server receipt authorizes it. Eligibility survives leaving a clan.
- Raw invitation token is transient and can only be obtained once; never log it, put it in Room snapshot/Outbox, or save it in preferences. It may be copied/shared only by user action and expires per server timestamp.
- Existing creation/join/leave RPCs continue through the Outbox. `clan_invite` is **not** a background-only event because the response must be consumed synchronously.
- PR branch only; don't merge `main`, don't deploy SQL, and don't publish PR APK as public build.

## Review Focus

1. Guest/empty member sees a designed create/join CTA, never demo members.
2. Existing member sees actual server profiles, week score and rank; missing score must display `—`/not yet available, not fabricated `0`.
3. Invite token is returned exactly once and not lost in a Worker receipt or persisted in the public `social_snapshot`; rejection returns no token.
4. Server chest `threshold` comes from `chest_policy`, and qualifying former member receives a discoverable entitlement despite no active clan.
5. Unavailable network and duplicate taps don't issue extra invitations/claims; membership remains locally cached while true server result is pending.

---

### Task 1: Parse authoritative clan policy and rights

**Files**
- Modify: `app/src/main/java/com/questyourlife/app/companion/SocialSnapshot.kt`
- Test: `app/src/test/java/com/questyourlife/app/companion/SocialSnapshotTest.kt`

**Interfaces**
- `data class SocialChestRight(val clanId: String, val clanName: String, val weekStart: String, val reward: Long)`
- `SocialSnapshot.chestRights: List<SocialChestRight>` from `clans[].chests[week].eligible[viewer]`, including former members.
- `SocialClan.target` from `clan.chest_policy.threshold`, nullable when policy absent, not from `chests.target`.

- [ ] Write failing tests `policyTargetComesFromServer`, `formerMemberCanSeeSettledChestRight`, `unrelatedMemberCannotSeeChestRight`.
- [ ] Verify RED via CI `:app:testDebugUnitTest`, expected assertion/missing property failures.
- [ ] Implement small parser only; avoid exposing private `eligible` member lists in UI model.
- [ ] Verify full unit suite; commit.

### Task 2: Deliver raw invitation synchronously

**Files**
- Create: `app/src/main/java/com/questyourlife/app/companion/ClanInvitation.kt`
- Modify: `app/src/main/java/com/questyourlife/app/companion/SocialStore.kt`
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestViewModel.kt`
- Test: `app/src/test/java/com/questyourlife/app/companion/ClanInvitationTest.kt`

**Interfaces**
- `data class ClanInvitation(val token: String, val clanId: String, val expiresAt: String)` with `fun parse(response: JSONObject, requestedClanId: String): ClanInvitation`.
- `suspend fun SocialStore.createInvitation(clanId: String): ClanInvitation` calls existing authenticated `questyourlife_social_command` with a UUID event, applies exactly one receipt via `SyncApply.event`, and uses the *outer* `response.invitation` secret.
- ViewModel exposes ephemeral `clanInvitation`, `clanInvitationBusy`, `clanInvitationError` and `requestClanInvitation(clanId)`/clear. No persistence. Keep status as actual server result.

- [ ] Test valid receipt/secret, missing token, rejected receipt, wrong clan id. Verify RED.
- [ ] Implement parser and direct RPC method; verify full suite. Commit.

### Task 3: One clan screen preserving the screenshot design

**Files**
- Replace: `app/src/main/java/com/questyourlife/app/ui/redesign/ClanPage.kt` (same exported `PatchClanPage`)
- Modify: `app/src/main/java/com/questyourlife/app/ui/DailyQuestApp.kt` to pass an `onAccount` callback.
- Modify: `app/src/main/java/com/questyourlife/app/ui/redesign/SocialPages.kt` to delete orphaned `SignedInClanPage` after confirming no call sites.

**Interfaces**
- `@Composable fun PatchClanPage(model: DailyQuestViewModel, onProfile: (String) -> Unit, onAccount: () -> Unit)`.
- Body uses exactly `model.signedIn`, `model.social` and `SocialClan`. Guest and signed-in membership share hierarchy. Create and join inputs live in a bottom sheet, not two permanent forms.
- Existing action names `clan_create`, `clan_join`, `clan_leave` use `model.socialCommand`. Invite is direct ViewModel synchronous action. Rank/member profiles use real IDs and equipped item IDs. No demo profile fallback.
- Member chest panel shows server threshold/score if present. Server-cached previously settled eligible rights are separately listed even if member left; claim action uses `clan_chest_claim` with week_start and warns about pending confirmation. No speculative balance.
- Advanced ownership transfer/unlock may stay in contextual sheet, but does not pretend to work if not connected.

- [ ] Write/extend pure projection test for missing score/target, verify RED.
- [ ] Implement using existing `ClanFlag`, `QuestChest`, `PatchSheet`, `DressedAvatar`, `PatchTokens` and rounded dark panel. Preserve hierarchy.
- [ ] Run PR CI, review unchanged UI shared navigation. Commit.

### Task 4: Verify safe transitions

- [ ] PR CI build and full JVM suite green. Examine actual logs.
- [ ] Scan `ClanPage.kt` and active Social UI for `LocalClanPreview`, `PATCH-DEMO`, unknown ranks or free rewards; expected no active fallback.
- [ ] Verify offline cache and failed create/invite/claim state; no cross-account private profile leak.
- [ ] Screenshot/manual emulator comparison if available; mark real-device QA unverified otherwise.
- [ ] Draft PR remains unmerged until visual verification and subsequent DM AI Gate 3 decision.
