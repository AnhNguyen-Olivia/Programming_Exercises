package tictactoe_new;

public class MainGame 
{
    private static boolean isPlayer1goFirst = false;
    public static void main( String[] args ){
 
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN");
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
        GameLogic logic = null;

        if(args.length == 0){
            System.out.println("Enter 1 for human first or 2 for computer first");
            return;
        }

        try{
            if(args[0].equals("1")){
                isPlayer1goFirst = true;
                logic = new GameLogic(human, computer, isPlayer1goFirst);
            } else if(args[0].equals("2")){
                isPlayer1goFirst = false;
                logic = new GameLogic(human, computer, isPlayer1goFirst);
            }else{
                System.out.println("Invalid input. Please enter 1 or 2.");
            }

            logic.play();
        }catch(Exception e){
            System.out.println("Something went wrong");
            e.printStackTrace();
        } 
    }
}
