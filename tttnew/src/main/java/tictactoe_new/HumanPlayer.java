package tictactoe_new;
import java.io.*;
import java.util.*;

/**
 * A human player for the Tic Tac Toe game.
 */
public class HumanPlayer extends Player {
    private PrintStream out;
    private Scanner scanner;

    /**
     * Constructor for HumanPlayer class.
     * @param marker
     * @param name
     * @param in
     * @param out
     */
    public HumanPlayer(char marker, String name, InputStream in, PrintStream out) {
        super(marker, name);
        this.scanner = new Scanner(in);
        this.out = out;
    }

    /**
     * Returns the name of the human player.
     * @return the name of the player
     */
    @Override
    public String getName(){
        return name;
    }

    /**
     * Makes a move for the human player.
     * @param board the game board
     * @return the position where the player wants to place their marker
     */
    @Override
    public Position makeMove(Board board) {
        do{
            out.println("Enter cell (1-" + board.getTotalCells() + "): ");
            String input = scanner.nextLine();

            if("q".equals(input)){
                out.println("All players exploded, game over. :D\n");
                throw new RuntimeException("Game quit by user.\n");
            }
            try{
                int chosenCell = Integer.parseInt(input);
                //Check range
                if(chosenCell < 1 || chosenCell > board.getTotalCells()){
                    out.println("Invalid cell number. Please enter a number between 1 and " + board.getTotalCells() + ".\n");
                    continue;
                }

                Position position = board.getCellPosition(chosenCell);
                
                //Check if cell is empty
                if(!board.isCellEmpty(position)){
                    out.println("The position have been taken. Try again.\n");
                    continue;
                }else{
                    return position;
                }


            } catch (NumberFormatException e) {
                out.println("Invalid input. Please enter a number.\n");
            }
        }while(true);
    }
}