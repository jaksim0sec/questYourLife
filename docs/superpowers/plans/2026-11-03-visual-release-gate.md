# Final Visual and Functional Release Gate · 현생패치

Status: **unverified / PR #6 / do not merge yet**

Reference assets (user-provided): screenshot mosaic `10889.png` (DM, clan, member sheet, rank sheet, profile, customization) and `10902.png`, `10903.png`, `10904.png`, `10905.png` (home, quest, chapter, reward, my). They are reference illustrations, not fake data sources. Existing `docs/development/clan-reference-checklist.md` is earlier implementation history, not verified screenshot evidence.

## R01 · Structural match

- [ ] Clan main page: green flag, name/member count, outlined invite, dark gold-chest panel and yellow progress, real avatar row, contribution panel, summary rank, shared nav.
- [ ] Clan sheets: real invite token, grouped 5-column member avatar grid, actual nearby rank sheet, no fake names, arbitrary badge or PATCH-DEMO.
- [ ] Other tabs: home hero + main quest CTA, questbook/chapter hierarchy, DM and My reward aesthetics comparable to user references.
- [ ] Loading/no clan/offline: same visual language with honest state. No raw debug/“시연입니다” notices.
- [ ] Android emulator screenshots of reference-sized phone and large font inspected against images; no code-only visual approval.

## R02 · Real data functionality

- [ ] First launch, onboarding, guest, existing account and return to original route.
- [ ] Goal→chapter→daily accept→manual completion→restart persistence (no duplicate coin).
- [ ] Two real accounts: clan create, invite, join, members and weekly rank; expired/reused invitation rejected.
- [ ] Confirm server score eligibility, settled weekly chest unlock/claim, replay no double payout.
- [ ] Token shown exactly once and not saved to Room, logs, UI snapshot or debug artifact.
- [ ] Offline cached clan state, resume and account isolation without leaked private goals.
- [ ] Assistant DM returns a real generated response only if backend deployed, otherwise remains explicitly incomplete.

## R03 · Build, regression and delivery

- [ ] GitHub Action JVM tests 0 failures + assembleDebug success on exact merged candidate SHA.
- [ ] Real instrumented emulator launch and screenshot audit; note if only test-fixture screenshots were possible.
- [ ] Check preview signing identity and versionCode update semantics.
- [ ] No unapproved Supabase migration or real user data used as a UI fixture.
- [ ] Review diff, secure privacy, regressions and README/AGENTS instructions.
- [ ] All required gates pass => merge PR #6 to main with expected-head lease; verify main CI on merge SHA.
- [ ] After successful main check, delete development branch (if GitHub connector lacks deletion action, report blocker, do not falsely claim removed).

Release rule: **test pass != same visual design != real account integration**. Any missing requirement keeps the PR draft. Do not promote an incomplete demonstration.
