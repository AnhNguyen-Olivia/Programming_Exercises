package tictactoe_new;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void testBoard1DNotFullAtCreation() {
        // Intent: verify that a new 1D board starts with available cells.
        Board1D board = new Board1D();
        assertFalse(board.isBoardFull());
    }

    @Test
    public void testBoard1DTotalCellsMatchesBoardSize() {
        // Intent: verify that the 1D board reports the expected number of cells.
        Board1D board = new Board1D();
        assertEquals(Constants.ROW * Constants.COL, board.getTotalCells());
    }

    @Test
    public void testBoard1DCellPositionMapping() {
        // Intent: verify that user-facing box numbers map to the expected 1D index.
        Board1D board = new Board1D();

        Position first = board.getCellPosition(1);
        Position last = board.getCellPosition(9);

        assertEquals(0, first.getCoordinate(0));
        assertEquals(8, last.getCoordinate(0));
    }

    @Test
    public void testBoard1DCellBecomesNonEmptyAfterPlaceMarker() {
        // Intent: verify that placing a marker changes a 1D cell from empty to occupied.
        Board1D board = new Board1D();
        Position position = board.getCellPosition(3);

        assertTrue(board.isCellEmpty(position));
        board.placeMarker(position, Constants.HUMAN_MARKER);
        assertFalse(board.isCellEmpty(position));
    }

    @Test
    public void testBoard1DDetectsWinnerForThreeInARow() {
        // Intent: verify that the 1D board detects a winner when three consecutive cells match.
        Board1D board = new Board1D();

        board.placeMarker(board.getCellPosition(1), Constants.HUMAN_MARKER);
        board.placeMarker(board.getCellPosition(2), Constants.HUMAN_MARKER);
        board.placeMarker(board.getCellPosition(3), Constants.HUMAN_MARKER);

        assertEquals(Constants.HUMAN_MARKER, board.checkWinner());
    }

    @Test
    public void testBoard1DReturnsNoWinnerWhenNoThreeConsecutiveMarkers() {
        // Intent: verify that the 1D board returns no winner when the win pattern is absent.
        Board1D board = new Board1D();

        board.placeMarker(board.getCellPosition(1), Constants.HUMAN_MARKER);
        board.placeMarker(board.getCellPosition(3), Constants.HUMAN_MARKER);
        board.placeMarker(board.getCellPosition(5), Constants.HUMAN_MARKER);

        assertEquals('0', board.checkWinner());
    }

    @Test
    public void testBoard1DBecomesFullAfterAllCellsOccupied() {
        // Intent: verify that the 1D board reports full after all cells are filled.
        Board1D board = new Board1D();

        for (int cell = 1; cell <= board.getTotalCells(); cell++) {
            board.placeMarker(board.getCellPosition(cell), Constants.HUMAN_MARKER);
        }

        assertTrue(board.isBoardFull());
    }

    @Test
    public void testBoard2DNotFullAtCreation() {
        // Intent: verify that a new 2D board starts with available cells.
        Board2D board = new Board2D();
        assertFalse(board.isBoardFull());
    }

    @Test
    public void testBoard2DTotalCellsMatchesBoardSize() {
        // Intent: verify that the 2D board reports the expected number of cells.
        Board2D board = new Board2D();
        assertEquals(Constants.ROW * Constants.COL, board.getTotalCells());
    }

    @Test
    public void testBoard2DCellPositionMapping() {
        // Intent: verify that user-facing box numbers map to correct row and column in 2D.
        Board2D board = new Board2D();

        Position first = board.getCellPosition(1);
        Position center = board.getCellPosition(5);
        Position last = board.getCellPosition(9);

        assertEquals(0, first.getRow());
        assertEquals(0, first.getCol());
        assertEquals(1, center.getRow());
        assertEquals(1, center.getCol());
        assertEquals(2, last.getRow());
        assertEquals(2, last.getCol());
    }

    @Test
    public void testBoard2DCellBecomesNonEmptyAfterPlaceMarker() {
        // Intent: verify that placing a marker changes a 2D cell from empty to occupied.
        Board2D board = new Board2D();
        Position position = board.getCellPosition(4);

        assertTrue(board.isCellEmpty(position));
        board.placeMarker(position, Constants.COMPUTER_MARKER);
        assertFalse(board.isCellEmpty(position));
    }

    @Test
    public void testBoard2DDetectsHorizontalWinner() {
        // Intent: verify that the 2D board detects a horizontal winning line.
        Board2D board = new Board2D();

        board.placeMarker(new Position(1, 0), Constants.HUMAN_MARKER);
        board.placeMarker(new Position(1, 1), Constants.HUMAN_MARKER);
        board.placeMarker(new Position(1, 2), Constants.HUMAN_MARKER);

        assertEquals(Constants.HUMAN_MARKER, board.checkWinner());
    }

    @Test
    public void testBoard2DDetectsVerticalWinner() {
        // Intent: verify that the 2D board detects a vertical winning line.
        Board2D board = new Board2D();

        board.placeMarker(new Position(0, 2), Constants.COMPUTER_MARKER);
        board.placeMarker(new Position(1, 2), Constants.COMPUTER_MARKER);
        board.placeMarker(new Position(2, 2), Constants.COMPUTER_MARKER);

        assertEquals(Constants.COMPUTER_MARKER, board.checkWinner());
    }

    @Test
    public void testBoard2DDetectsMainDiagonalWinner() {
        // Intent: verify that the 2D board detects a main diagonal winning line.
        Board2D board = new Board2D();

        board.placeMarker(new Position(0, 0), Constants.HUMAN_MARKER);
        board.placeMarker(new Position(1, 1), Constants.HUMAN_MARKER);
        board.placeMarker(new Position(2, 2), Constants.HUMAN_MARKER);

        assertEquals(Constants.HUMAN_MARKER, board.checkWinner());
    }

    @Test
    public void testBoard2DDetectsAntiDiagonalWinner() {
        // Intent: verify that the 2D board detects an anti-diagonal winning line.
        Board2D board = new Board2D();

        board.placeMarker(new Position(0, 2), Constants.COMPUTER_MARKER);
        board.placeMarker(new Position(1, 1), Constants.COMPUTER_MARKER);
        board.placeMarker(new Position(2, 0), Constants.COMPUTER_MARKER);

        assertEquals(Constants.COMPUTER_MARKER, board.checkWinner());
    }

    @Test
    public void testBoard2DReturnsNoWinnerOnEmptyBoard() {
        // Intent: verify that a fresh 2D board has no winner.
        Board2D board = new Board2D();
        assertEquals('0', board.checkWinner());
    }

    @Test
    public void testBoard2DBecomesFullAfterAllCellsOccupied() {
        // Intent: verify that the 2D board reports full after all cells are filled.
        Board2D board = new Board2D();

        for (int cell = 1; cell <= board.getTotalCells(); cell++) {
            board.placeMarker(board.getCellPosition(cell), Constants.HUMAN_MARKER);
        }

        assertTrue(board.isBoardFull());
    }
}
