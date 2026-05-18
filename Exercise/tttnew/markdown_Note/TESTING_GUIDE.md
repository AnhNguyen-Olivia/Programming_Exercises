# Tic-Tac-Toe Testing Guide

## Current Testing Issues

### Problem 1: Piped Streams Backwards
**MainTest2.java** has a critical flaw:
```java
outputStream = new PipedOutputStream();
PipedInputStream inputStream = new PipedInputStream(outputStream);  // WRONG
System.setOut(new PrintStream(outputStream));  // Redirects stdout to outputStream
scanner = new BufferedReader(new InputStreamReader(inputStream));  // Tries to read from output
```

- `PipedOutputStream` is for WRITING data
- `PipedInputStream` reads from that same stream
- Setting stdout to write to `outputStream` means the test can read what the game prints via `inputStream`
- **But the test then calls `MainGame.main()` which tries to READ from System.in** — and System.in is NOT connected, so the game hangs

### Problem 2: Interactive Input Not Provided
When `MainGame.main()` calls `GameLogic.play()`, it creates a `HumanPlayer` that reads from `System.in`:
- The test provides no input to System.in
- The game blocks waiting for player moves
- Test times out

### Problem 3: Threading Complexity
If the game uses background threads (noted in original code), stdio interception becomes even more fragile:
- Threads may write output in arbitrary order
- Pipe buffering can cause deadlocks
- Output capture depends on timing

---

## Solutions by Test Type

### Type A: Startup Validation (No Game Loop)
**Use: `ByteArrayOutputStream` — simple output capture**

These tests verify startup behavior and exit before entering the game loop.

```java
@Test
void testStartupWithoutArgument() throws IOException {
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        MainGame.main(new String[]{});
        
        String output = outputBuffer.toString(StandardCharsets.UTF_8);
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    } finally {
        System.setOut(originalOut);
    }
}

@Test
void testStartupWithInvalidArgument() throws IOException {
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        MainGame.main(new String[]{"3"});
        
        String output = outputBuffer.toString(StandardCharsets.UTF_8);
        assertEquals("Please, input a valid option [1-2]" + System.lineSeparator(), output);
    } finally {
        System.setOut(originalOut);
    }
}
```

**Advantage:** No threading, no input needed, fast, deterministic.

---

### Type B: Interactive Game Tests (With Input)
**Use: Refactor `GameLogic` to accept injectable input stream, OR use `ByteArrayInputStream`**

#### Option B1: Dependency Injection (Recommended)
Refactor `GameLogic` or `HumanPlayer` to accept input from a constructor parameter:

```java
class HumanPlayer extends Player {
    private BufferedReader inputReader;
    
    public HumanPlayer(int marker, String name, BufferedReader inputReader) {
        super(marker, name);
        this.inputReader = inputReader;
    }
    
    @Override
    public Move getMove(Board board) throws IOException {
        // Use inputReader instead of hardcoded System.in
        String input = inputReader.readLine();
        // ... parse and validate
    }
}
```

Then test becomes:
```java
@Test
void testHumanValidMove() throws IOException {
    String gameInput = "1\n";  // Player chooses cell 1
    BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));
    
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        
        // Use Board and GameLogic directly, passing mock input
        Board board = new Board2D();
        Player human = new HumanPlayer(1, "HUMAN", mockInput);
        Player computer = new Computer(2, "COMPUTER");
        GameLogic game = new GameLogic(board, human, computer, true);
        
        game.play();  // Runs to completion with predefined input
        
        String output = outputBuffer.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("Hello!");
        assertThat(output).contains("Player#1 won!");  // or "It is a draw!"
    } finally {
        System.setOut(originalOut);
    }
}
```

#### Option B2: ByteArrayInputStream (Simpler for simple tests)
Without refactoring, inject input into System.in for the test:

```java
@Test
void testHumanValidMove() throws IOException {
    String gameInput = "1\n";  // Simulates user typing "1" and pressing Enter
    ByteArrayInputStream inputBuffer = new ByteArrayInputStream(gameInput.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    
    PrintStream originalOut = System.out;
    InputStream originalIn = System.in;
    
    try {
        System.setOut(new PrintStream(outputBuffer, true, StandardCharsets.UTF_8));
        System.setIn(inputBuffer);
        
        MainGame.main(new String[]{"1"});  // Human starts
        
        String output = outputBuffer.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("Hello!");
        assertThat(output).contains("Please, input a valid number");  // Expected due to one input
    } finally {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}
```

**Caveat:** This only works if:
- The game completes before reading beyond the input buffer
- The input sequence you provide is sufficient for the test scenario
- No deadlock between input/output buffering

---

## Recommended Testing Architecture

### Refactor for Testability (Best Long-term Solution)

1. **Extract GameLogic from main()**
   - Move validation logic from MainGame to GameLogic
   - Allow GameLogic to accept input/output streams as parameters

2. **Inject Dependencies**
   ```java
   public class GameLogic {
       private final Board board;
       private final Player human;
       private final Player computer;
       private final PrintStream out;
       private final BufferedReader in;
       
       public GameLogic(Board board, Player human, Player computer, 
                        boolean humanFirst, PrintStream out, BufferedReader in) {
           this.board = board;
           this.human = human;
           this.computer = computer;
           this.out = out;
           this.in = in;
       }
   }
   ```

3. **Test Categories**
   - **Startup tests:** Verify args, no game loop entry
   - **Unit tests:** Board logic, win/draw detection, computer AI
   - **Integration tests:** Full game sequences with injected input

---

## Test Scenarios (Prioritized)

### P0 - Critical (Startup, No Input Needed)
- `testStartupWithoutArgument()` — empty args → "Please, input a valid option [1-2]"
- `testStartupWithInvalidArgument()` — arg "0" or "3" → same message
- `testStartupWithNonNumericArgument()` — arg "x" → same message

### P0 - Critical (With Input)
- `testHumanValidMove()` — input "1" on empty board → cell marked, display board
- `testHumanOccupiedCell()` — input cell already taken → "The cell is occupied!"
- `testHumanWin()` — set board state, input winning move → "Player#1 won!"
- `testComputerWin()` — computer gets winning move → "Player#2 won!"
- `testDraw()` — full board, no winner → "It is a draw!"

### P1 - High (Input Validation)
- `testHumanNonIntegerInput()` — input "x" → "Please, input a valid number [1-9]"
- `testHumanOutOfRangeInput()` — input "0" or "10" → same message
- `testHumanQuitCommand()` — input "q" → "End of the game"

---

## Thread Safety Notes

If you add threading to the game loop:

1. **Avoid mixing System.out redirection with threads**
   - Threads writing to stdout after main thread has mocked it = chaos
   - Solution: Use a synchronized output wrapper or test framework built for concurrency (e.g., Hamcrest matchers for eventually-consistent assertions)

2. **For NOW:** Keep game synchronous in main thread
   - Let the commented-out thread code remain commented
   - Tests will be predictable and fast

---

## Summary

| Test Type | Tool | Input | Output | When to Use |
|-----------|------|-------|--------|------------|
| Startup validation | ByteArrayOutputStream | None | Capture stdout | Quick P0 tests, exit before game loop |
| Interactive (refactored) | ByteArrayInputStream + StringReader | Inject in constructor | ByteArrayOutputStream | After dependency injection |
| Interactive (no refactor) | ByteArrayInputStream | System.setIn() | ByteArrayOutputStream | Quick prototypes, risk of deadlock |
| Full game sim | Custom TestGame class | Mock Player subclass | Custom output handler | Complex scenarios, highest control |

**Recommendation:** Refactor to inject streams (Option B1), then write tests against the refactored interface. This gives you the best of all worlds: testability, maintainability, and no threading surprises.
