package tictactoe_new;

/**
 * An abstract class representing the game board for tic-tac-toe.
 */
public abstract class Board {

    /**
     * Prints the current state of the board.
     */
    public abstract void print();

    /**
     * Returns the total number of cells on the board.
     * @return
     */
    public abstract int getTotalCells();
    
    /**
     * Checks if the board is full.
     * @return
     */
    public abstract boolean isBoardFull();
    
    /**
     * Checks if a cell is empty.
     * @param pos the position to check
     * @return
     */
    public abstract boolean isCellEmpty(Position pos);
    
    /**
     * Returns the position of a cell based on its box number.
     * @param boxNumber the box number
     * @return
     */
    public abstract Position getCellPosition(int boxNumber);
    
    /**
     * Places a marker on the board at the specified position.
     * @param pos the position to place the marker
     * @param marker the marker to place
     */
    public abstract void placeMarker(Position pos, char marker);
    
    /**
     * Checks for a winner on the board.
     * @return the marker of the winning player, or '\0' if no winner
     */
    public abstract char checkWinner();
    
    /**
     * Checks if there is a line of markers starting from a given position.
     * @param startPos the starting position
     * @param steps the steps to move in each direction
     * @return true if there is a line of markers, false otherwise
     */
    protected abstract boolean checkLine(Position startPos, int... steps);
}