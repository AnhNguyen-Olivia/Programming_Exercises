package tictactoe_new;
/**
 * Represents a computer player in the tic-tac-toe game.
 */
public class Computer extends Player {
    
    /**
     * Constructor for Computer class.
     * @param marker
     * @param name
     */
    public Computer(char marker, String name) {
        super(marker, name);
    }

    /**
     * Makes a move for the computer player.
     * @param board the game board
     * @return the position where the computer places its marker
     */
    @Override
    public Position makeMove(Board board) {
        for(int cell = 1; cell <= board.getTotalCells(); cell++){
            Position position = board.getCellPosition(cell);
            if(board.isCellEmpty(position)){
                return position;
            }
        }
        return null; // if board is full
    }

    /**
     * Returns the name of the computer player.
     * @return the name of the computer player
     */
    @Override
    public String getName() {
        return name;
    }
}