package tictactoe_new;

import java.io.*;

/**
 * The main class for the Tic Tac Toe game.
 */
public class MainGame 
{   
    private static boolean isPlayer1goFirst = false;
    /**
     * The main method to start the game.
     * @param args
     */
    public static void main( String[] args ){
        // Use System.out and System.in for console input and output, 
        // but can be easily switched to other streams for testing or other purposes.
        PrintStream out = System.out;
        InputStream in = System.in;

        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", in, out);
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
        GameLogic logic = null;

        if(args.length == 0){
            out.println("Usage: <1|2> [1d|2d|]");
            out.println("Example: 1 2d  (human starts on 2D board)");
            return;
        }

        Board selectedBoard = null;
        if (args.length >= 2 && args[1].equalsIgnoreCase("1d")) {
            selectedBoard = new Board1D(out);
        }else if(args.length >= 2 && args[1].equalsIgnoreCase("2d")){
             selectedBoard = new Board2D(out);
        }

        try{
            if(args[0].equals("1")){
                isPlayer1goFirst = true;
                logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst, out);
            } else if(args[0].equals("2")){
                isPlayer1goFirst = false;
                logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst, out);
            }else{
                out.println("Invalid input. Please enter 1 or 2.");
            }

            logic.play();
        }catch(Exception e){
            out.println("Something went wrong");
            e.printStackTrace();
        } 
    }
}