package tictactoe_new;
import java.io.*;

/**
 * A 1D Tic Tac Toe board.
 */
public class Board1D extends Board {
    private char[] board;
    private PrintStream out;

    /**
     * Constructor for Board1D class.
     */
    public Board1D(PrintStream out) {
        this.out = out;
        board = new char[Constants.ROW * Constants.COL];
        for (int i = 0; i < board.length; i++) {
            board[i] = '0';
        }
    }

    /**
     * Constructor for Board1D class with PrintStream.
     * @param out
     */
    @Override
    public void print() {
        for (int i = 0; i < board.length; i++) {
            out.printf("| %s ", "" + board[i]);
        }
        out.println("|");
    }

    /**
     * Returns the total number of cells on the board.
     * @return the total number of cells
     */
    @Override
    public int getTotalCells() {
        return board.length;
    }

    /**
     * Checks if the board is full.
     * @return true if the board is full, false otherwise
     */
    @Override
    public boolean isBoardFull() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '0') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a cell is empty.
     * @param pos the position of the cell to check
     * @return true if the cell is empty, false otherwise
     */
    @Override
    public boolean isCellEmpty(Position pos) {
        int index = pos.getCoordinate(0);
        return board[index] == '0';
    }

    /**
     * Gets the position of a cell based on its box number.
     * @param boxNumber the number of the cell
     * @return the Position object representing the cell's position
     */
    @Override
    public Position getCellPosition(int boxNumber) {
        int index = boxNumber - 1;
        return new Position(index);
    }

    /**
     * Places a marker on the board at the specified position.
     * @param pos the position where the marker should be placed
     * @param marker the marker to place on the board
     */
    @Override
    public void placeMarker(Position pos, char marker) {
        int index = pos.getCoordinate(0);
        board[index] = marker;
    }

    /**
     * Checks if there is a winner on the board.
     * @return the marker of the winning player, or '0' if there is no winner
     */
    @Override
    public char checkWinner() {
        for (int i = 0; i <= board.length - Constants.WIN_LENGTH; i++) {
            if (checkLine(new Position(i), 1)) {
                return board[i];
            }
        }
        return '0';
    }

    /**
     * Checks if a line of markers is valid.
     * @param startPos the starting position of the line
     * @param steps the steps to move along the line
     * @return true if the line is valid, false otherwise
     */
    @Override
    protected boolean checkLine(Position startPos, int... steps) {
        int startIndex = startPos.getCoordinate(0);
        int stride = steps[0];
        char marker = board[startIndex];

        if (marker == '0') {
            return false;
        }

        for (int k = 1; k < Constants.WIN_LENGTH; k++) {
            int index = startIndex + k * stride;
            if (index < 0 || index >= board.length) {
                return false;
            }
            if (board[index] != marker) {
                return false;
            }
        }
        return true;
    }
}