package tictactoe_new;
import tictactoe_new.Constants.*;

public abstract class Player {
    private char marker;

    public Player(char marker){
        this.marker = marker;
    }

    public char getMarker(){
        return marker;
    }

    public abstract int[] makeMove(Board board);
    public abstract String getName();
    public abstract PlayerType getType();
}
