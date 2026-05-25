package tictactoe_new;

public abstract class Player {
    protected char marker;
    protected String name;
    protected int playerNumber;

    public Player(char marker, String name){
        this(marker, name, 0);
    }

    public Player(char marker, String name, int playerNumber){
        this.marker = marker;
        this.name = name;
        this.playerNumber = playerNumber;
    }

    public char getMarker(){
        return marker;
    }

    public String getName(){
        if(playerNumber > 0){
            return "Player#" + playerNumber;
        }
        return name;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public abstract Position makeMove(Board board);
}