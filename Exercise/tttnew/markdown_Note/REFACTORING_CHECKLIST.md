# Refactoring Checklist for Testable Tic-Tac-Toe

## Good News: HumanPlayer Already Supports Dependency Injection!

```java
public HumanPlayer(char marker, String name, InputStream inputStream) {
    super(marker, name);
    this.scanner = new Scanner(inputStream);
}
```

✅ Already allows tests to inject `ByteArrayInputStream` or other input sources
✅ This is the key enabler for testable interactive scenarios

**Example:** Tests can now do:
```java
String gameInput = "1\n5\n9\n";
ByteArrayInputStream inputBuffer = new ByteArrayInputStream(gameInput.getBytes());
HumanPlayer human = new HumanPlayer('1', "HUMAN", inputBuffer);
```

---

## Required Changes (Gap Between Implementation & Spec)

### 1. MainGame.main() Constructor Signature
**Current (MainGame.java:8-9):**
```java
HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN");
```

**Issue:** Uses hardcoded System.in, not testable

**Fix:** Change to:
```java
HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", System.in);
```

---

### 2. HumanPlayer.makeMove() - Error Messages & Handling
**Current (HumanPlayer.java:22-34):**
```java
@Override
public Position makeMove(Board board) {
    int choosenCell;
    do{
        System.out.println("Enter cell (1-" + board.getTotalCells() + "): ");
        choosenCell = scanner.nextInt();  // ← Throws InputMismatchException on non-integer
        Position position = board.getCellPosition(choosenCell);
        if(!board.isCellEmpty(position)){
            System.out.println("The position have been taken. Try again.");
        }else{
            return position;
        }
    }while(true);
}
```

**Issues:**
1. Prompt message format doesn't match spec (should be just "Player#X's turn", not "Enter cell...")
2. Exception on non-integer input; spec requires graceful error message
3. Occupied cell message is "The position have been taken. Try again." but spec requires "The cell is occupied!"
4. No handling for "q" quit command
5. Doesn't validate range [1-9]; spec requires "Please, input a valid number [1-9]"

**Fix: Refactor to:**
```java
@Override
public Position makeMove(Board board) {
    while(true){
        System.out.println("Player#1's turn");  // ← Moved here from GameLogic
        try {
            String input = scanner.nextLine().trim();
            
            // Handle quit
            if("q".equals(input)){
                System.out.println("End of the game");
                System.exit(0);  // Or throw custom exception
            }
            
            // Parse integer
            int choosenCell = Integer.parseInt(input);
            
            // Validate range [1-9]
            if(choosenCell < 1 || choosenCell > 9){
                System.out.println("Please, input a valid number [1-9]");
                continue;
            }
            
            // Check occupancy
            Position position = board.getCellPosition(choosenCell);
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

### 3. GameLogic.play() - Message Format
**Current (GameLogic.java:26-36):**
```java
while(!isGameOver()){
    System.out.print("\n" + currentPlayer.getName() + "'s turn:\n");
    Position move = currentPlayer.makeMove(board);
    // ...
}
```

**Issue:** Prints player name from Player object; spec requires specific format "Player#1's turn"

**Fix:** Either:
- **Option A:** Pass player number to Player, have Player.getName() return "Player#1"
- **Option B:** Move turn printing from GameLogic to HumanPlayer (cleaner: HumanPlayer prints prompt when it needs input)
- **Option C:** Pass a player ID to makeMove() so HumanPlayer knows which player it is

**Recommended: Option A** — Keep Player responsible for its own identity:
```java
// In Player base class or HumanPlayer:
private int playerNumber;  // 1 or 2

@Override
public String getName(){
    return "Player#" + playerNumber;
}
```

Then GameLogic can keep its current line:
```java
System.out.println(currentPlayer.getName() + "'s turn");
// Output: "Player#1's turn"
```

---

### 4. GameLogic.play() - Win/Draw Messages
**Current (GameLogic.java:40-48):**
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

**Issues:**
1. Output format is "HUMAN wins!" but spec requires "Player#1 won!"
2. Draw message is "Draw!" but spec requires "It is a draw!"

**Fix:**
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

---

### 5. MainGame.main() - Startup Flow
**Current (MainGame.java:26-43):**
```java
if(args[0].equals("1")){
    isPlayer1goFirst = true;
    logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
} else if(args[0].equals("2")){
    isPlayer1goFirst = false;
    logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
}else{
    System.out.println("Please, input a valid option [1-2]");
}
if (logic != null) {
    logic.play();
}
```

**Missing:** Must print "Hello!" before board

**Fix:**
```java
if(args[0].equals("1")){
    isPlayer1goFirst = true;
    logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
} else if(args[0].equals("2")){
    isPlayer1goFirst = false;
    logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
}else{
    System.out.println("Please, input a valid option [1-2]");
}
if (logic != null) {
    System.out.println("Hello!");
    logic.play();
}
```

---

## Summary of Changes

| File | Method | Change | Priority |
|------|--------|--------|----------|
| MainGame.java | main() | Add "Hello!" before play() | P0 |
| MainGame.java | main() | Use `HumanPlayer(..., System.in)` | P0 |
| HumanPlayer.java | makeMove() | Refactor error handling (non-int, range, quit) | P0 |
| HumanPlayer.java | makeMove() | Change message "The position..." → "The cell is occupied!" | P0 |
| Player.java | (base) | Add playerNumber field, update getName() | P0 |
| GameLogic.java | play() | Change "wins!" → "won!" and "Draw!" → "It is a draw!" | P0 |
| GameLogic.java | play() | Update turn prompt format if needed | P1 |
| MainGame.java | main() | Update constructor calls to pass playerNumber | P0 |

---

## Test Enablement

Once these changes are made:

✅ MainGameTest.java tests will pass (startup validation)
✅ InteractiveGameTest.java tests can be uncommented and will pass (full game scenarios)
✅ New tests can verify each error condition independently

**Testing the refactor:**
```bash
# Run startup tests (no input needed)
mvn test -Dtest=MainGameTest

# After uncommenting: run interactive tests (will use injected input)
mvn test -Dtest=InteractiveGameTest
```

---

## Refactoring Order

1. **First:** Update error messages and validation in HumanPlayer.makeMove()
2. **Second:** Add playerNumber to Player class
3. **Third:** Update GameLogic win/draw messages
4. **Fourth:** Add "Hello!" to MainGame
5. **Fifth:** Uncomment InteractiveGameTest and verify all pass

Each step is independent and testable via MainGameTest startup tests.
