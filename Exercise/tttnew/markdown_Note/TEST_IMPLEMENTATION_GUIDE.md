# Test Implementation Strategy

## Phase 1: Startup Validation (Ready Now)

These tests run **immediately** without code changes.

### TS-003 & TS-004: Invalid Startup Arguments

**Status:** ✅ Already implemented in `MainGameTest.java`

```bash
mvn test -Dtest=MainGameTest
```

**Results:**
```
testStartupWithoutArgument           ✅ PASS
testStartupWithInvalidArgument_Zero  ✅ PASS
testStartupWithInvalidArgument_Three ✅ PASS
testStartupWithInvalidArgument_Negative ✅ PASS
testStartupWithNonNumericArgument    ✅ PASS
testStartupWithQuotedArgument        ✅ PASS
```

---

## Phase 2: Code Refactoring (5 Tasks)

Complete in order. **After each task, run `mvn test -Dtest=MainGameTest` to verify nothing breaks.**

### Task 1: Fix Occupied Cell Message
**File:** `src/main/java/tictactoe_new/HumanPlayer.java`, line 29

**Current:**
```java
System.out.println("The position have been taken. Try again.");
```

**Change to:**
```java
System.out.println("The cell is occupied!");
```

**Enables:** TS-012

---

### Task 2: Add "Hello!" Startup Message
**File:** `src/main/java/tictactoe_new/MainGame.java`, line 37 (before `logic.play()`)

**Current:**
```java
if (logic != null) {
    logic.play();
}
```

**Change to:**
```java
if (logic != null) {
    System.out.println("Hello!");
    logic.play();
}
```

**Enables:** TS-001, TS-002, TS-022

---

### Task 3: Fix Win/Draw Messages
**File:** `src/main/java/tictactoe_new/GameLogic.java`, lines 40-48

**Current:**
```java
char winnerMarker = board.checkWinner();
if(winnerMarker != '0'){
    if(player_1.getMarker() == winnerMarker){
        System.out.println(player_1.getName() + " wins!");
    }else{
        System.out.println(player_2.getName() + " wins!");
    }
}else{
    System.out.println("Draw!");
}
```

**Change to:**
```java
char winnerMarker = board.checkWinner();
if(winnerMarker != '0'){
    if(player_1.getMarker() == winnerMarker){
        System.out.println(player_1.getName() + " won!");
    }else{
        System.out.println(player_2.getName() + " won!");
    }
}else{
    System.out.println("It is a draw!");
}
```

**Enables:** TS-013, TS-014, TS-015, TS-016, TS-022

---

### Task 4: Add PlayerNumber Field to Player Class
**File:** `src/main/java/tictactoe_new/Player.java`

**Add field to abstract class:**
```java
protected int playerNumber;

public Player(int marker, String name, int playerNumber) {
    this.marker = marker;
    this.name = name;
    this.playerNumber = playerNumber;
}

public int getPlayerNumber() {
    return playerNumber;
}
```

**Update child class constructors:**

In `HumanPlayer.java`:
```java
public HumanPlayer(char marker, String name, int playerNumber) {
    super(marker, name, playerNumber);
    this.scanner = new Scanner(System.in);
}

public HumanPlayer(char marker, String name, int playerNumber, InputStream inputStream) {
    super(marker, name, playerNumber);
    this.scanner = new Scanner(inputStream);
}
```

In `Computer.java` (similar pattern):
```java
public Computer(char marker, String name, int playerNumber) {
    super(marker, name, playerNumber);
}
```

**Update MainGame.java to pass playerNumber:**
```java
HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", 1);
Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER", 2);
```

**Enables:** Turn messages with correct player number

---

### Task 5: Refactor HumanPlayer.makeMove() for Error Handling
**File:** `src/main/java/tictactoe_new/HumanPlayer.java`

**Replace entire makeMove method:**

```java
@Override
public Position makeMove(Board board) {
    while(true){
        System.out.println("Player#" + playerNumber + "'s turn");
        try {
            String input = scanner.nextLine().trim();
            
            // Handle quit command
            if("q".equals(input)){
                System.out.println("End of the game");
                System.exit(0);
            }
            
            // Parse input as integer
            int chosenCell = Integer.parseInt(input);
            
            // Validate range [1-9]
            if(chosenCell < 1 || chosenCell > 9){
                System.out.println("Please, input a valid number [1-9]");
                continue;
            }
            
            // Get position and check if occupied
            Position position = board.getCellPosition(chosenCell);
            if(!board.isCellEmpty(position)){
                System.out.println("The cell is occupied!");
                continue;
            }
            
            return position;
            
        } catch(NumberFormatException e){
            // Non-integer input (but not "q")
            System.out.println("Please, input a valid number [1-9]");
        }
    }
}
```

**Enables:** TS-007, TS-008, TS-009, TS-010, TS-011, TS-012, TS-019

---

## Verification After Each Task

After completing each task, verify MainGameTest still passes:

```bash
mvn test -Dtest=MainGameTest -q
```

Expected output: All 6 tests pass ✅

---

## Phase 3: Interactive Game Tests

After completing **all 5 refactoring tasks**, uncomment and run interactive tests:

```bash
mvn test -Dtest=InteractiveGameTest -q
```

### Test Groups to Implement

**Group A: Startup & Format (TS-001, TS-002, TS-006, TS-022)**
```java
@Test
void startGameWithHumanFirst() throws IOException { }

@Test
void startGameWithComputerFirst() throws IOException { }

@Test
void boardRendersWithStateValuesOnly() throws IOException { }

@Test
void outputConsistencyWithExactStrings() throws IOException { }
```

**Group B: Valid Moves (TS-007, TS-017, TS-018)**
```java
@Test
void acceptValidHumanMoveAndUpdateBoard() throws IOException { }

@Test
void computerChoosesFirstAvailableCell() throws IOException { }

@Test
void boardIntegrityAfterEveryMove() throws IOException { }
```

**Group C: Invalid Input Handling (TS-008, TS-009, TS-010, TS-011)**
```java
@Test
void handleNonIntegerInput() throws IOException { }

@Test
void quitGameWithQ() throws IOException { }

@Test
void verifyQCaseSensitivity() throws IOException { }

@Test
void rejectIntegerOutsideRange() throws IOException { }
```

**Group D: Occupied Cell & Turn Management (TS-012, TS-019)**
```java
@Test
void rejectMoveToOccupiedCell() throws IOException { }

@Test
void turnPromptSequenceCorrectness() throws IOException { }
```

**Group E: Win/Draw Detection (TS-013, TS-014, TS-015, TS-016)**
```java
@Test
void humanWinDetectionOnRow() throws IOException { }

@Test
void computerWinDetection() throws IOException { }

@Test
void drawDetectionWhenBoardFullAfterHumanMove() throws IOException { }

@Test
void drawDetectionWhenBoardFullAfterComputerMove() throws IOException { }
```

**Group F: Robustness (TS-020, TS-021)**
```java
@Test
void programTerminationBehaviorOnFinalStates() throws IOException { }

@Test
void inputRobustnessUnderRapidInvalidRetries() throws IOException { }
```

---

## Testing Automation Patterns

### Pattern 1: Simple Startup Test (No Game Loop)
**Use:** TS-003, TS-004, TS-005

```java
private String runMainAndCapture(String[] args) throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        MainGame.main(args);
        return outputBuffer.toString(StandardCharsets.UTF_8);
    } finally {
        System.setOut(originalOut);
    }
}

@Test
void testInvalidStartup() throws IOException {
    String output = runMainAndCapture(new String[]{"3"});
    assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
}
```

### Pattern 2: Interactive Game Test (With Input)
**Use:** TS-001, TS-007, TS-013, etc.

```java
private void runGameAndAssert(String input, String[] args, 
                               String... expectedOutputs) throws IOException {
    ByteArrayInputStream inputBuffer = new ByteArrayInputStream(
        input.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    
    PrintStream originalOut = System.out;
    InputStream originalIn = System.in;
    
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        System.setIn(inputBuffer);
        MainGame.main(args);
        
        String output = outputBuffer.toString(StandardCharsets.UTF_8);
        for(String expected : expectedOutputs) {
            assertThat(output).contains(expected);
        }
    } finally {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}

@Test
void testStartGame() throws IOException {
    runGameAndAssert("1\n", new String[]{"1"}, 
        "Hello!",
        "Player#1's turn",
        "Player#2's turn");
}
```

### Pattern 3: Complex Move Sequences
**Use:** TS-013, TS-014, TS-017, TS-018

```java
@Test
void computerWinDetection() throws IOException {
    // Sequence: Computer starts, plays cells 1,2,3 to win
    // Human must cooperate by not blocking and playing away
    String moves = "5\n7\n";  // Human plays 5, then 7
    String[] args = new String[]{"2"};  // Computer starts
    
    ByteArrayInputStream input = new ByteArrayInputStream(
        moves.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    PrintStream originalOut = System.out;
    InputStream originalIn = System.in;
    
    try {
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        System.setIn(input);
        MainGame.main(args);
        
        String result = output.toString(StandardCharsets.UTF_8);
        assertThat(result).contains("Player#2 won!");
    } finally {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}
```

---

## Test Data: Deterministic Move Sequences

For reproducible tests, pre-compute these sequences:

### Sequence 1: Computer Wins (Starts First)
```
Computer: 1 (auto)
Human:    4
Computer: 2 (auto)
Human:    5
Computer: 3 (auto, wins!)
→ Expect: "Player#2 won!"
```

### Sequence 2: Human Wins
```
Computer starts = 2
Human:    1
Computer: 4 (auto)
Human:    2
Computer: 5 (auto)
Human:    3 (auto, wins!)
→ Expect: "Player#1 won!"
```

### Sequence 3: Draw
```
Computer starts = 2
[sequence of 9 moves resulting in full board, no winner]
→ Expect: "It is a draw!"
```

Document these in a test constants class for reuse.

---

## Running Tests By Priority

### P1 (Critical) Tests
```bash
mvn test -Dtest=MainGameTest
mvn test -Dtest=InteractiveGameTest#startGameWithHumanFirst
mvn test -Dtest=InteractiveGameTest#startGameWithComputerFirst
mvn test -Dtest=InteractiveGameTest#acceptValidHumanMoveAndUpdateBoard
# ... etc
```

### All Tests
```bash
mvn clean test
```

Expected final result:
```
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Checklist for Implementation

- [ ] Complete Task 1: Fix occupied cell message
- [ ] Complete Task 2: Add "Hello!" message
- [ ] Complete Task 3: Fix win/draw messages
- [ ] Complete Task 4: Add playerNumber field
- [ ] Complete Task 5: Refactor HumanPlayer.makeMove()
- [ ] Verify MainGameTest passes after each task
- [ ] Uncomment Group A tests in InteractiveGameTest
- [ ] Uncomment Group B tests
- [ ] Uncomment Group C tests
- [ ] Uncomment Group D tests
- [ ] Uncomment Group E tests (win/draw detection — most complex)
- [ ] Uncomment Group F tests (robustness)
- [ ] Run `mvn clean test` — all pass ✅
- [ ] Clarify gaps TS-023 to TS-026 with stakeholder
- [ ] Final submission ready

---

## Timeline Estimate

- **Tasks 1-5 (Refactoring):** 1-2 hours
- **Groups A-B (Basic gameplay):** 2-3 hours
- **Group C (Input validation):** 1-2 hours
- **Group D (Turn management):** 1 hour
- **Group E (Win/Draw detection):** 2-3 hours (requires careful sequences)
- **Group F (Robustness):** 1 hour
- **Gaps & Final Review:** 1 hour

**Total:** ~10-15 hours of implementation → 26 passing tests ✅
