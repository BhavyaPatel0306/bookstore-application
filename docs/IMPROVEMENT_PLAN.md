# Portfolio improvement notes

This branch preserves the original COE 528 Java Swing/NetBeans project and its State Design Pattern.

## Implemented
- Reject non-finite prices in the owner inventory form.
- Mask new-customer password entry and stop displaying stored passwords in the owner customer table.
- Validate numeric inventory and customer records when loading files.
- Continue past malformed numeric records rather than abandoning the rest of the file.
- Use decimal arithmetic for purchases and loyalty redemption, with whole points rounded down.
- Add dependency-free regression checks and Java 17 GitHub Actions compilation.

## Verification still required
- Compile with JDK 17 using the README command or NetBeans.
- Manually exercise owner and customer screens, purchasing, redemption, and save/restart.
- Check that malformed records do not prevent valid later records from loading.
- Confirm GitHub Actions completes successfully and manually test the Swing interface.
- Confirm that sample data contains only demonstration credentials.

## Next improvements
- Process malformed file records individually, reporting line numbers and continuing to load valid entries.
- Handle save failures explicitly and use safer file replacement.
- Add persistence regression tests and confirm that files are saved safely.
- Consider storing money in cents throughout the model instead of retaining the double-based public API.
- Add real screenshots and a verified demonstration workflow; do not fabricate screenshots or test results.

This is an academic demonstration. The current text-file authentication remains unsuitable for real credentials.
