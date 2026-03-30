package tictactoe_new;

public abstract class Board {
    public abstract void print();

    public abstract boolean isBoardFull();

    public abstract boolean isCellEmpty(Position position);

    public abstract Position getCellPosition(int boxNumber);

    public abstract void placeMarker(Position position, char marker);

    public abstract char checkWinner();
}