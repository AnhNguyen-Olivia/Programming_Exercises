# Testing Quick Reference

## Files at a Glance

| File | Read First? | Purpose |
|------|-------------|---------|
| **TEST_SUMMARY.md** | ✅ YES | Start here (5 min overview) |
| **TESTING_GUIDE.md** | If curious | Deep dive into testing patterns |
| **REFACTORING_CHECKLIST.md** | Before coding | Exact changes needed |
| **InteractiveGameTest.java** | After refactoring | Example tests to uncomment |
| **MainGameTest.java** | To verify | Run: `mvn test -Dtest=MainGameTest` |

---

## The Problem (In One Sentence)
MainTest2.java tries to read from an output pipe, so the game hangs waiting for input that never comes.

## The Solution (In One Sentence)
Use `ByteArrayOutputStream` for startup tests, refactor HumanPlayer to accept input streams for interactive tests.

---

## Current Test Status

```
✅ Startup tests (6 tests)        MainGameTest.java
   Working, tests validation only

❌ Interactive tests (10 tests)   InteractiveGameTest.java
   Commented out, need refactoring first

⚠️  Old pattern (4 tests)          MainTest2.java
   Disabled, shows wrong approach
```

---

## Refactoring Checklist (Quick)

- [ ] Update HumanPlayer error messages (3 strings)
- [ ] Add playerNumber field to Player
- [ ] Update GameLogic win/draw messages (2 strings)
- [ ] Add "Hello!" to MainGame startup
- [ ] Uncomment InteractiveGameTest tests
- [ ] Run full test suite: `mvn test`

**Time estimate:** ~30 minutes for all 5 tasks

---

## One Diagram: Why Pipes Failed

```
❌ WRONG (MainTest2.java):
   Test creates outputStream
   outputStream ← game's System.out writes here
   Test reads from inputStream ← reads what game printed ✓
   But game also reads from System.in ← NEVER SET UP ✗
   Result: Game hangs forever waiting for input

✅ RIGHT (InteractiveGameTest.java):
   Test creates ByteArrayInputStream with "1\n5\n9\n"
   System.setIn(inputStream) ← game will read from here ✓
   Test creates ByteArrayOutputStream to capture output
   System.setOut(outputStream) ← game will write here ✓
   Result: Game reads input, tests capture output, no hang
```

---

## Commands to Know

```bash
# Compile only
mvn clean compile

# Run startup tests only (fast, no input needed)
mvn test -Dtest=MainGameTest

# Run all tests (will hang on interactive tests until refactored)
mvn test

# Skip tests, build jar
mvn clean package -DskipTests

# Run specific test method
mvn test -Dtest=MainGameTest#testStartupWithoutArgument
```

---

## Key Files to Change

```
src/main/java/tictactoe_new/
  └── HumanPlayer.java      ← Main refactoring
  └── GameLogic.java        ← Message updates
  └── MainGame.java         ← Add "Hello!" + use System.in param
  └── Player.java           ← Add playerNumber field
```

---

## Test Scenario Mapping

From your original 25 scenarios:

**P0 - Critical (Testable Now)**
- ✅ Startup Without Argument → MainGameTest
- ✅ Startup With Invalid Argument → MainGameTest (5 variants)
- ⏸️ Board display & win detection → Will test after refactoring

**P1 - High (Testable After Refactoring)**
- ⏸️ Human input validation (3 scenarios)
- ⏸️ Computer strategy & win detection (3 scenarios)

**P2 - Medium**
- ⏸️ Edge cases, whitespace handling, EOF handling

---

## Pro Tips

1. **Run tests frequently** as you refactor (after each change, run `mvn test -Dtest=MainGameTest`)
2. **Start with one refactoring task** and verify it doesn't break startup tests
3. **Uncomment tests one at a time** in InteractiveGameTest and verify they pass
4. **Save these docs** in your project (they're in /IMPLEMENTATION_PLAN.md etc.)

---

## Still Confused?

1. **"Where do I start?"** → Read TEST_SUMMARY.md
2. **"Why is my test hanging?"** → See TESTING_GUIDE.md "Problem 2"
3. **"What do I change in the code?"** → Read REFACTORING_CHECKLIST.md
4. **"What do the tests look like?"** → See InteractiveGameTest.java
5. **"How do I run tests?"** → This page, "Commands to Know" section

---

## Success Criteria

You'll know you're done when:
```bash
$ mvn test
...
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

(6 startup tests + 10 interactive tests = 16 total)
