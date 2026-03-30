package tictactoe_new;

public abstract class Board {

    public abstract void print();

    public abstract int getTotalCells();
    
    public abstract boolean isBoardFull();
    
    public abstract boolean isCellEmpty(Position pos);
    
    public abstract Position getCellPosition(int boxNumber);
    
    public abstract void placeMarker(Position pos, char marker);
    
    public abstract char checkWinner();
    
    // Protected helper method for subclasses
    protected abstract boolean checkLine(Position startPos, int... steps);
}