package tictactoe_new;

public class MainGame 
{
    private static boolean isPlayer1goFirst = false;
    public static void main( String[] args ){
        GameLogic logic = new GameLogic();        
        switch(args[0]){
            case "1" :
                isPlayer1goFirst = true;
                System.out.println(isPlayer1goFirst ? "Human Starts" : "Computer starts");
                logic.StartGame(new HumanPlayer(Constants.HUMAN_MARKER), new Computer(Constants.COMPUTER_MARKER), isPlayer1goFirst);
                break;
            case "2":
                System.out.println("Computer start\n"); 
                System.out.println(isPlayer1goFirst ? "Human Starts" : "Computer starts");
                logic.StartGame(new HumanPlayer(Constants.HUMAN_MARKER), new Computer(Constants.COMPUTER_MARKER), isPlayer1goFirst);
                break;
        } 
    }
}
