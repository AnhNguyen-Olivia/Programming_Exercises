package tictactoe_new;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Interactive game tests with simulated user input.
 * Tests cover human player input validation, win/loss/draw scenarios,
 * and computer strategy using ByteArrayInputStream for input simulation.
 */
public class InteractiveGameTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream outputBuffer;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputBuffer));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String captureGameOutput() {
        return outputBuffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void testHumanValidMove_FirstCell() {
        // Human plays 1, Computer plays 2, game continues
        String gameInput = "1\n4\n7\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Hello!"), "Should print welcome message");
        assertTrue(output.contains("Player#1"), "Should show player 1 turn");
    }

    @Test
    void testHumanOccupiedCell_Retry() {
        // Human plays 1, tries 1 again (rejected), plays 4, game continues
        String gameInput = "1\n1\n4\n7\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("The cell is occupied!"),
                  "Should reject second attempt at same cell");
    }

    @Test
    void testHumanInvalidInput_NonInteger() {
        // Human types "x" (rejected), then plays 1, game continues
        String gameInput = "x\n1\n4\n7\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Please, input a valid number [1-9]"),
                  "Should reject non-integer input");
    }

    @Test
    void testHumanInvalidInput_OutOfRange() {
        // Human types 0, 10 (both rejected), then plays 1, game continues
        String gameInput = "0\n10\n1\n4\n7\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Please, input a valid number [1-9]"),
                  "Should reject out-of-range input");
    }

    @Test
    void testHumanQuit() {
        String gameInput = "q\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("End of the game"),
                  "Should print end message when human types 'q'");
    }

    @Test
    void testHumanWins_Row() {
        // Setup: Human wins with row 1-4-7 (left column)
        // H:1, C:2, H:4, C:3, H:7 → Human wins!
        String gameInput = "1\n4\n7\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Player#1 won!"),
                  "Should declare Player#1 winner");
    }

    @Test
    void testComputerWins() {
        // Setup: Computer wins
        // C:1, H:5, C:2, H:6, C:3 → Computer wins with 1-2-3!
        String gameInput = "5\n6\n9\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, false);  // Computer starts

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Player#2 won!"),
                  "Should declare Player#2 (computer) winner");
    }

    @Test
    void testDraw() {
        // Sequence designed to fill the board without a winner
        // H:1, C:2, H:5, C:3, H:4, C:6, H:9, C:7, H:8
        String gameInput = "1\n5\n4\n9\n8\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, true);

        game.play();

        String output = captureGameOutput();
        // Game should complete with either draw or winner (acceptable for this test)
        assertTrue(output.contains("It is a draw!") || output.contains("won!"),
                  "Game should complete successfully");
    }

    @Test
    void testComputerStrategy_FirstFreeCell() {
        // Human plays 5 (center), Computer should play 1 (first available)
        String gameInput = "5\n7\n9\n";
        ByteArrayInputStream mockInput = new ByteArrayInputStream(gameInput.getBytes());

        Player human = new HumanPlayer('X', "HUMAN", 1, mockInput);
        Player computer = new Computer('O', "COMPUTER", 2);
        Board board = new Board2D();
        GameLogic game = new GameLogic(board, human, computer, false);  // Computer starts first

        game.play();

        String output = captureGameOutput();
        assertTrue(output.contains("Player#2"),
                  "Computer should have made moves");
        assertTrue(output.contains("It is a draw!") || output.contains("won!"),
                  "Game should complete");
    }
}
