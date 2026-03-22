package tictactoe_new;
import tictactoe_new.Constants.*;

public abstract class Player {
    protected char marker;
    protected String name;

    public Player(char marker, String name){
        this.marker = marker;
        this.name = name;
    }

    public char getMarker(){
        return marker;
    }
    
    public String getName(){
        return name;
    };

    public abstract int[] makeMove(Board board);
    
    public abstract PlayerType getType();
}
