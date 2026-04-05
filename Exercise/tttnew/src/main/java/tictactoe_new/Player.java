package tictactoe_new;

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

    public abstract Position makeMove(Board board);
}