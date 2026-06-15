package tictactoe_new;
import java.io.*;

/**
 * Manages the game logic for the tic-tac-toe game.
 */
public class GameLogic {
    private PrintStream out;

    private Board board;

    private Player player_1;
    private Player player_2;

    private Player currentPlayer;

    /**
     * Constructor for GameLogic class.
     * @param board
     * @param p1
     * @param p2
     * @param isPlayer1goFirst
     * @param out
     */
    public GameLogic(Board board, Player p1, Player p2, boolean isPlayer1goFirst, PrintStream out){
        this.board = board;
        this.player_1 = p1;
        this.player_2 = p2;
        this.currentPlayer = isPlayer1goFirst ? p1 : p2;
        this.out = out;
        printStartingPlayer();
    }

    /**
     * Plays the game.
     */
    public void play(){
        board.print();
        while(!isGameOver()){
            out.print("\n" + currentPlayer.getName() + "'s turn:\n");
            Position move = currentPlayer.makeMove(board);
            board.placeMarker(move, currentPlayer.getMarker());
            board.print();

            //switch player
            currentPlayer = (currentPlayer == player_1) ? player_2 : player_1;
        }
        
        //Game over
        char winnerMarker = board.checkWinner();
        if(winnerMarker != '0'){
            if(player_1.getMarker() == winnerMarker){
                out.println(player_1.getName() + " wins!");
            }else{
                out.println(player_2.getName() + " wins!");
            }
        }else{
            out.println("Draw!");
        }
    }

    /**
     * Checks if the game is over.
     * @return
     */
    public boolean isGameOver(){
        return board.checkWinner() != '0' || board.isBoardFull();
    }
    
    /**
     * Prints the name of the starting player.
     */
    public void printStartingPlayer(){
        out.println(currentPlayer.getName() + " starts");
    }
}