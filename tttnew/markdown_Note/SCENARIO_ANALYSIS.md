# Test Scenarios Implementation Guide

## Scenario Analysis & Status

### By Implementation Phase

#### Phase 1: Ready Now (No Code Changes)
These scenarios can be implemented immediately:

| Scenario | Title | Status | Why |
|----------|-------|--------|-----|
| **TS-003** | Reject missing startup argument | ✅ Testable | MainGameTest.java covers this |
| **TS-004** | Reject invalid startup argument value | ✅ Testable | MainGameTest.java covers this (0, 3, -1, abc) |
| **TS-005** | Validate startup argument strictness | ⚠️ Partial | "01" fails in current code; "1 2" accepted (gap) |
| **TS-022** | Output consistency with exact required strings | ✅ Testable | Can verify error message format |

**Action:** These can run against MainGameTest.java right now.

#### Phase 2: Ready After Refactoring (Code Changes from REFACTORING_CHECKLIST.md)
These require fixes to game logic and message formatting:

| Scenario | Title | Requires | Tasks |
|----------|-------|----------|-------|
| **TS-001** | Start game with human first | "Hello!" message, turn format | Add "Hello!", fix turn message format |
| **TS-002** | Start game with computer first | "Hello!" message, turn format | Same as TS-001 |
| **TS-006** | Board renders as 3x3 with state values only | Board display logic review | Review Board2D.print() format |
| **TS-007** | Accept valid human move and update board | Error handling, turn flow | Refactor HumanPlayer.makeMove() |
| **TS-008** | Handle non-integer input as invalid | Error handling | Add try/catch in HumanPlayer |
| **TS-009** | Quit game with q | Error handling, new feature | Add "q" handling in HumanPlayer |
| **TS-010** | Verify q case sensitivity | Error handling | Part of TS-009 implementation |
| **TS-011** | Reject integer outside 1-9 | Error handling, validation | Add range check in HumanPlayer |
| **TS-012** | Reject move to occupied cell | Message format | Change "The position..." → "The cell is occupied!" |
| **TS-013** | Human win detection | Win logic check | Verify Board.checkWinner() |
| **TS-014** | Computer win detection | Win logic check | Verify Board.checkWinner() |
| **TS-015** | Draw detection after human move | Draw logic check | Verify Board.isBoardFull() |
| **TS-016** | Draw detection after computer move | Draw logic check | Same as TS-015 |
| **TS-017** | Computer chooses first available cell | Computer strategy | Verify Computer.makeMove() |
| **TS-018** | Board integrity after every move | Board state tracking | Integration test |
| **TS-019** | Turn prompt sequence correctness | Turn message format | Fix "Player#X's turn" format |
| **TS-020** | Program termination behavior | All end states | Integration test |
| **TS-021** | Input robustness under rapid invalid retries | Input loop resilience | Stress test |

**Action:** These unlock when you complete REFACTORING_CHECKLIST.md tasks.

#### Phase 3: Gaps/Clarifications (TS-023 to TS-026)
These require specification clarification:

| Scenario | Gap | Recommendation |
|----------|-----|-----------------|
| **TS-023** | OOP compliance non-testable | Add code review checklist to grading rubric |
| **TS-024** | Board visual format ambiguous | Get stakeholder approval for exact format (spacing, separators) |
| **TS-025** | Extra args behavior undefined | Decision: reject extra args OR accept only first arg? |
| **TS-026** | Input parsing normalization | Decision: trim spaces before validation OR reject with spaces? |

**Action:** Clarify these before final submission.

---

## Cross-Reference: Scenarios ↔ Code Changes

### Refactoring Task 1: Fix Error Messages
**Affected Scenarios:**
- TS-012: "The cell is occupied!" (currently "The position have been taken")
- TS-022: Message format verification

**Code Location:** `HumanPlayer.java:29`

```java
// Current
System.out.println("The position have been taken. Try again.");

// Required
System.out.println("The cell is occupied!");
```

---

### Refactoring Task 2: Add PlayerNumber & Fix Turn Format
**Affected Scenarios:**
- TS-001, TS-002: "Player#X's turn" format
- TS-008, TS-009, TS-010, TS-011, TS-012, TS-019: Turn re-prompt format

**Code Locations:**
- `Player.java` — add `playerNumber` field
- `GameLogic.java:29` — turn message format
- `HumanPlayer.java` — error messages should show "Player#1's turn"

```java
// Current (GameLogic.java:29)
System.out.print("\n" + currentPlayer.getName() + "'s turn:\n");

// Required
System.out.println("Player#" + playerNumber + "'s turn");
```

---

### Refactoring Task 3: Add "Hello!" Startup Message
**Affected Scenarios:**
- TS-001, TS-002: Startup sequence
- TS-022: Message format verification

**Code Location:** `MainGame.java` before `logic.play()`

```java
// Add this line
System.out.println("Hello!");
```

---

### Refactoring Task 4: Refactor HumanPlayer Input Handling
**Affected Scenarios:**
- TS-007: Valid move acceptance
- TS-008: Non-integer rejection
- TS-009, TS-010: Quit command
- TS-011: Range validation
- TS-012: Occupied cell rejection
- TS-019: Turn re-prompting
- TS-021: Robustness test

**Code Location:** `HumanPlayer.java:22-34` (makeMove method)

**Current Issues:**
1. Uses `scanner.nextInt()` which throws exception on non-integer
2. No "q" quit command support
3. No range validation [1-9]
4. Messages don't match spec

**Required Changes:**
```java
@Override
public Position makeMove(Board board) {
    while(true){
        System.out.println("Player#1's turn");  // Hardcoded for now; use playerNumber later
        try {
            String input = scanner.nextLine().trim();
            
            // Handle quit
            if("q".equals(input)){
                System.out.println("End of the game");
                System.exit(0);
            }
            
            // Parse integer
            int chosenCell = Integer.parseInt(input);
            
            // Validate range [1-9]
            if(chosenCell < 1 || chosenCell > 9){
                System.out.println("Please, input a valid number [1-9]");
                continue;
            }
            
            // Check occupancy
            Position position = board.getCellPosition(chosenCell);
            if(!board.isCellEmpty(position)){
                System.out.println("The cell is occupied!");
                continue;
            }
            
            return position;
            
        } catch(NumberFormatException e){
            System.out.println("Please, input a valid number [1-9]");
        }
    }
}
```

---

### Refactoring Task 5: Update Win/Draw Messages
**Affected Scenarios:**
- TS-013: "Player#1 won!" format
- TS-014: "Player#2 won!" format
- TS-015, TS-016: "It is a draw!" format
- TS-022: Message format verification

**Code Location:** `GameLogic.java:39-48`

```java
// Current
if(winnerMarker != '0'){
    if(player_1.getMarker() == winnerMarker){
        System.out.println(player_1.getName() + " wins!");  // ← "HUMAN wins!"
    }else{
        System.out.println(player_2.getName() + " wins!");  // ← "COMPUTER wins!"
    }
}else{
    System.out.println("Draw!");  // ← Should be "It is a draw!"
}

// Required
if(winnerMarker != '0'){
    if(player_1.getMarker() == winnerMarker){
        System.out.println(player_1.getName() + " won!");  // ← "Player#1 won!"
    }else{
        System.out.println(player_2.getName() + " won!");  // ← "Player#2 won!"
    }
}else{
    System.out.println("It is a draw!");
}
```

---

## Scenario Testing Strategy by Type

### Functional - Positive (Happy Path)
**Scenarios:** TS-001, TS-002, TS-007, TS-013, TS-014, TS-015, TS-016, TS-017, TS-018

**Test Approach:**
- Use `InteractiveGameTest.java` pattern (inject input, capture output)
- Verify expected output sequences
- Track board state changes
- Use `assertThat(output).contains()` for flexible matching

**Example (TS-001):**
```java
@Test
void startGameWithHumanFirst() throws IOException {
    String gameInput = "5\n";  // Human plays center
    BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    // ... run game with input
    
    String result = output.toString();
    assertThat(result).contains("Hello!");
    assertThat(result).contains("Player#1's turn");
    assertThat(result).contains("Player#2's turn");
}
```

### Functional - Negative (Error Handling)
**Scenarios:** TS-003, TS-004, TS-008, TS-009, TS-010, TS-011, TS-012

**Test Approach:**
- Test invalid input paths with expected error messages
- Verify game doesn't advance on invalid moves
- Use `MainGameTest.java` pattern for startup; `InteractiveGameTest.java` for in-game

**Example (TS-008):**
```java
@Test
void handleNonIntegerInput() throws IOException {
    String gameInput = "abc\n1\n";  // Invalid, then valid
    BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));
    // ... run game
    
    assertThat(output).contains("Please, input a valid number [1-9]");
    assertThat(output).contains("Player#1's turn");
}
```

### Non-Functional (Reliability & Robustness)
**Scenarios:** TS-020, TS-021

**Test Approach:**
- TS-020: End each game type and verify clean exit
- TS-021: Rapid-fire invalid inputs then valid move

**Example (TS-021):**
```java
@Test
void inputRobustnessUnderRapidInvalidRetries() throws IOException {
    String gameInput = "x\n!\n999\n \n0\n10\n-5\n@#$\n1\n";
    BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));
    // ... run game
    
    // Should complete without crash
    assertTrue(gameCompleted);
    assertThat(output).contains("Please, input a valid number [1-9]");  // Multiple times
}
```

### Gaps/Untestable (TS-023 to TS-026)
**Approach:**
- Document answers to clarification questions
- Update requirements based on stakeholder feedback
- Create acceptance rubric for OOP review

---

## Test Scenario Distribution

### By Priority
- **P1 (Critical):** 13 scenarios (TS-001, 002, 003, 004, 006, 007, 008, 009, 011, 012, 013, 014, 015, 016, 017, 018, 020, 022) — 18 total
- **P2 (High):** 5 scenarios (TS-005, 010, 019, 021)
- **P3 (Low):** 3 scenarios (TS-024, 025, 026)

### By Testability
- **Immediately Testable** (Phase 1): 4 scenarios
- **Testable After Refactoring** (Phase 2): 18 scenarios
- **Needs Clarification** (Phase 3): 4 scenarios

---

## Recommendation: Implementation Order

**Week 1: Foundation**
1. Implement TS-003, TS-004 tests (already passing in MainGameTest)
2. Complete REFACTORING_CHECKLIST.md tasks (Tasks 1-5)
3. Verify MainGameTest still passes

**Week 2: Core Gameplay**
4. Implement TS-001, TS-002, TS-007 (startup + basic move)
5. Implement TS-008, TS-009, TS-010, TS-011, TS-012 (error handling)
6. Implement TS-013, TS-014, TS-015, TS-016 (win/draw detection)

**Week 3: Advanced**
7. Implement TS-017, TS-018, TS-019, TS-020, TS-021 (AI + robustness + integrity)
8. Implement TS-006, TS-022 (format verification)
9. Clarify TS-023, TS-024, TS-025, TS-026 gaps

**Completion:** All 26 scenarios documented, 22 automated, 4 clarified.

---

## Observations & Questions

### Minor Gaps in Your Scenarios (Opportunities to Enhance)

1. **TS-018 (Board Integrity):** Current scenario is vague on how to assert board state.
   - **Suggestion:** Create a `BoardSnapshot` class to capture expected state after each move.

2. **TS-013 & TS-014 (Win Detection):** Require specific move sequences.
   - **Suggestion:** Pre-compute 3 deterministic sequences (row, column, diagonal) and document expected move order.

3. **TS-006 (Board Format):** Mentions "no reference numbering" but doesn't specify exact spacing.
   - **Suggestion:** Add sample board output to expected section.

### Ambiguities to Clarify

1. **TS-024:** What is the exact board format?
   ```
   Option A:  | 0 | 1 | 2 |
              | 3 | 4 | 5 |
              | 6 | 7 | 8 |
   
   Option B:  0 1 2
              3 4 5
              6 7 8
   
   Option C:  0|1|2
              -----
              3|4|5
              -----
              6|7|8
   ```

2. **TS-025:** Current behavior: `MainGame.main(new String[]{"1", "extra"})` starts game (extra args ignored).
   - **Decision:** Accept this or enforce single arg only?

3. **TS-026:** Current behavior: `HumanPlayer` uses `scanner.nextLine()` (after my refactoring).
   - **Decision:** Should " 5" (leading space) be treated as invalid or trimmed to valid?
   - **Recommendation:** Trim spaces (more user-friendly).

---

## Summary

Your 26 test scenarios are **excellent and comprehensive**. They cover:
- ✅ All functional requirements
- ✅ All error cases
- ✅ Edge cases (q, spaces, rapid retries)
- ✅ Non-functional reliability
- ✅ Gaps/ambiguities (honest and well-documented)

**Next Step:** Implement the REFACTORING_CHECKLIST.md tasks, then systematically convert these scenarios into JUnit tests using the patterns in InteractiveGameTest.java.

**Timeline:** 2-3 weeks to go from 4 testable scenarios → 22 automated tests ✅
