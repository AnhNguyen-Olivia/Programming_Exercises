package tictactoe_new;

/**
 * Represents a player in the tic-tac-toe game.
 */
public abstract class Player {
    protected char marker;
    protected String name;

    /**
     * Constructor for Player class.
     * @param marker the character representing the player's marker
     * @param name the name of the player
     */
    public Player(char marker, String name){
        this.marker = marker;
        this.name = name;
    }

    /**
     * Returns the marker of the player.
     * @return
     */
    public char getMarker(){
        return marker;
    }
    
    /**
     * Returns the name of the player.
     * @return
     */
    public String getName(){
        return name;
    };

    /**
     * Abstract method to make a move for the player.
     * @param board the game board
     * @return the position where the player wants to place their marker
     */
    public abstract Position makeMove(Board board);
}