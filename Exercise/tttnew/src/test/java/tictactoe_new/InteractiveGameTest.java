package tictactoe_new;

import org.junit.jupiter.api.BeforeEach;

/**
 * Interactive game tests (Type B: with user input)
 *
 * PREREQUISITE: HumanPlayer must be refactored to accept BufferedReader as constructor param
 * instead of hardcoding System.in. See TESTING_GUIDE.md Option B1.
 *
 * These tests are TEMPLATES — they will NOT compile until the refactoring is complete.
 */
public class InteractiveGameTest {

    @BeforeEach
    void setUp() {
    }

    // Note: Tests will capture output directly; this method is a template reference
    // private String captureGameOutput() {
    //     return outputBuffer.toString(StandardCharsets.UTF_8);
    // }

    // ========== TEMPLATE TESTS (Uncomment after refactoring HumanPlayer) ==========

    /*
    @Test
    void testHumanValidMove_FirstCell() {
        // Simulate: Human (Player#1) plays cell 1, computer plays cell 2, etc.
        String gameInput = "1\n";  // Human chooses cell 1
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            // REQUIRES: HumanPlayer(int marker, String name, BufferedReader input)
            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);  // Human starts

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("Hello!"), "Should print welcome message");
            assertTrue(output.contains("Player#1"), "Should show player 1 turn");
            assertTrue(output.contains("1"), "Should show cell 1 marked");
        } finally {
            tearDown();
        }
    }

    @Test
    void testHumanOccupiedCell_Retry() {
        // Simulate: Human plays cell 1, then tries cell 1 again, then cell 2
        String gameInput = "1\n1\n2\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("The cell is occupied!"),
                      "Should reject second attempt at same cell");
            assertTrue(output.contains("Player#1's turn"),
                      "Should re-prompt for move after rejection");
        } finally {
            tearDown();
        }
    }

    @Test
    void testHumanInvalidInput_NonInteger() {
        // Simulate: Human types "x", then a valid move "1"
        String gameInput = "x\n1\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("Please, input a valid number [1-9]"),
                      "Should reject non-integer input");
        } finally {
            tearDown();
        }
    }

    @Test
    void testHumanInvalidInput_OutOfRange() {
        // Simulate: Human types "0", then "10", then valid "1"
        String gameInput = "0\n10\n1\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("Please, input a valid number [1-9]"),
                      "Should reject out-of-range input");
        } finally {
            tearDown();
        }
    }

    @Test
    void testHumanQuit() {
        // Simulate: Human types "q"
        String gameInput = "q\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("End of the game"),
                      "Should print end message when human types 'q'");
        } finally {
            tearDown();
        }
    }

    @Test
    void testHumanWins_Row() {
        // Set up board where human has two in a row and can win
        // Simulating: cells 1, 2, then 3 (completes row)
        String gameInput = "1\n2\n3\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            // Note: This depends on computer strategy (first free cell)
            // The computer will take cell 4 after human's first move, etc.
            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("Player#1 won!"),
                      "Should declare Player#1 winner");
        } finally {
            tearDown();
        }
    }

    @Test
    void testComputerWins() {
        // Simulate: Human makes moves, computer eventually wins
        // This requires careful sequencing to ensure computer can win
        String gameInput = "1\n5\n9\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, false);  // Computer starts!

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("Player#2 won!"),
                      "Should declare Player#2 (computer) winner");
        } finally {
            tearDown();
        }
    }

    @Test
    void testDraw() {
        // Sequence that results in full board, no winner
        // (Depends on board size and win condition)
        String gameInput = "1\n3\n5\n7\n9\n";
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, true);

            game.play();

            String output = captureGameOutput();
            assertTrue(output.contains("It is a draw!"),
                      "Should declare a draw when board is full with no winner");
        } finally {
            tearDown();
        }
    }

    @Test
    void testComputerStrategy_FirstFreeCell() {
        // Verify computer always chooses first available cell 1..9
        String gameInput = "5\n";  // Human takes center
        BufferedReader mockInput = new BufferedReader(new StringReader(gameInput));

        try {
            System.setOut(testOut);

            Player human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", mockInput);
            Player computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            Board board = new Board2D();
            GameLogic game = new GameLogic(board, human, computer, false);  // Computer starts first

            game.play();

            String output = captureGameOutput();
            // Computer should have taken cell 1 (first available)
            // Board output should show 2 at position 1
            assertTrue(output.contains("2"),
                      "Computer should mark a cell");
            // More robust: parse board and verify cell 1 == 2
        } finally {
            tearDown();
        }
    }
    */

    // ========== NEXT STEPS ==========
    // ✅ Refactoring Complete!
    // To enable these tests:
    // 1. Uncomment the test methods above (remove /* and */)
    // 2. Update test references:
    //    - Replace "testOut" with "System.out"
    //    - Replace "captureGameOutput()" with output capture in each test
    //    - Replace "tearDown()" with finally block to restore System.out
    //    - Add StringReader import: import java.io.StringReader
    // 3. Update player creation to pass playerNumber:
    //    - new HumanPlayer(marker, name, 1, inputStream)
    //    - new Computer(marker, name, 2)
    // 4. Run: mvn test -Dtest=InteractiveGameTest
}
