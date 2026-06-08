package tictactoe_new;
public class GameLogic {
    private Board board;

    private Player player_1;
    private Player player_2;

    private Player currentPlayer;

    public GameLogic(Player p1, Player p2, boolean isPlayer1goFirst){
        this.board = new Board2D();
        this.player_1 = p1;
        this.player_2 = p2;
        this.currentPlayer = isPlayer1goFirst ? p1 : p2;
        printStartingPlayer();
    }

    public GameLogic(Board board, Player p1, Player p2, boolean isPlayer1goFirst){
        this.board = board;
        this.player_1 = p1;
        this.player_2 = p2;
        this.currentPlayer = isPlayer1goFirst ? p1 : p2;
        printStartingPlayer();
    }

    public void play(){
        board.print();
        while(!isGameOver()){
            System.out.print("\n" + currentPlayer.getName() + "'s turn:\n");
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
                System.out.println(player_1.getName() + " wins!");
            }else{
                System.out.println(player_2.getName() + " wins!");
            }
        }else{
            System.out.println("Draw!");
        }
    }

    public boolean isGameOver(){
        return board.checkWinner() != '0' || board.isBoardFull();
    }
    
    public void printStartingPlayer(){
        System.out.println(currentPlayer.getName() + " starts");
    }
}