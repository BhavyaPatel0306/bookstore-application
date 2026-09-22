# Portfolio improvement notes

This branch preserves the original COE 528 Java Swing/NetBeans project and its State Design Pattern.

## Implemented
- Reject non-finite prices in the owner inventory form.
- Mask new-customer password entry and stop displaying stored passwords in the owner customer table.
- Validate numeric inventory and customer records when loading files.

## Verification still required
- Compile with JDK 17 using the README command or NetBeans.
- Manually exercise owner and customer screens, purchasing, redemption, and save/restart.
- Check that malformed records do not prevent valid later records from loading.
- Confirm that sample data contains only demonstration credentials.

## Next improvements
- Process malformed file records individually, reporting line numbers and continuing to load valid entries.
- Handle save failures explicitly and use safer file replacement.
- Define and test exact money/loyalty rounding rules before replacing floating-point calculations.
- Add automated tests and CI after verifying the current build.
- Add real screenshots and a verified demonstration workflow; do not fabricate screenshots or test results.

This is an academic demonstration. The current text-file authentication remains unsuitable for real credentials.
