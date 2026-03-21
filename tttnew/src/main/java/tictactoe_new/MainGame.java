package tictactoe_new;

public class MainGame 
{
    private static boolean isPlayer1goFirst = false;
    public static void main( String[] args ){
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER);
        Computer computer = new Computer(Constants.COMPUTER_MARKER); 
        GameLogic logic = null;
        try{
            switch(args[0]){
                case "1" :
                    isPlayer1goFirst = true;
                    System.out.println(isPlayer1goFirst ? "Human Starts" : "Computer starts");
                    logic = new GameLogic(human, computer, isPlayer1goFirst);
                    break;
                case "2":
                    System.out.println("Computer start\n"); 
                    System.out.println(isPlayer1goFirst ? "Human Starts" : "Computer starts");
                    logic = new GameLogic(human, computer, isPlayer1goFirst);
                    break;
            }
        }catch(Exception e){
            System.out.println("Enter 1 for human first or 2 for computer first");
            e.printStackTrace();
        } 
    }
}
