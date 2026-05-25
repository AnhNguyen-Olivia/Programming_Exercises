package tictactoe_new;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlayerTest {

    @Test
    public void testComputerReturnsFirstAvailableCell() {
        // Intent: verify that the computer chooses the first empty cell in scanning order.
        Board2D board = new Board2D();
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "CPU");

        board.placeMarker(board.getCellPosition(1), Constants.HUMAN_MARKER);
        board.placeMarker(board.getCellPosition(2), Constants.HUMAN_MARKER);

        Position chosen = computer.makeMove(board);

        assertNotNull(chosen);
        assertEquals(0, chosen.getRow());
        assertEquals(2, chosen.getCol());
    }

    @Test
    public void testComputerReturnsNullWhenBoardIsFull() {
        // Intent: verify that the computer returns null when no legal move exists.
        Board2D board = new Board2D();
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "CPU");

        for (int cell = 1; cell <= board.getTotalCells(); cell++) {
            board.placeMarker(board.getCellPosition(cell), Constants.HUMAN_MARKER);
        }

        assertNull(computer.makeMove(board));
    }

    @Test
    public void testPlayerGettersForComputer() {
        // Intent: verify that marker and name configured in the constructor are returned correctly.
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "HAL");

        assertEquals(Constants.COMPUTER_MARKER, computer.getMarker());
        assertEquals("HAL", computer.getName());
    }

    @Test
    public void testHumanPlayerReturnsChosenEmptyCell() {
        // Intent: verify that a human player's valid byte-based input is converted into a board position.
        Board2D board = new Board2D();
        byte[] inputBytes = new byte[] { '5', '\n' };
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "Alice", new ByteArrayInputStream(inputBytes));

        Position chosen = human.makeMove(board);

        assertNotNull(chosen);
        assertEquals(1, chosen.getRow());
        assertEquals(1, chosen.getCol());
    }

    @Test
    public void testHumanPlayerRetriesWhenCellIsOccupied() {
        // Intent: verify that a human player retries based on byte-based fake input when the first cell is occupied.
        Board2D board = new Board2D();
        byte[] inputBytes = new byte[] { '1', '\n', '2', '\n' };
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "Alice", new ByteArrayInputStream(inputBytes));

        board.placeMarker(board.getCellPosition(1), Constants.COMPUTER_MARKER);

        Position chosen = human.makeMove(board);

        assertNotNull(chosen);
        assertEquals(0, chosen.getRow());
        assertEquals(1, chosen.getCol());
    }

    @Test
    public void testPlayerGettersForHuman() {
        // Intent: verify that marker and name configured in the human player constructor are returned correctly.
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "Bob");

        assertEquals(Constants.HUMAN_MARKER, human.getMarker());
        assertEquals("Bob", human.getName());
    }
}
