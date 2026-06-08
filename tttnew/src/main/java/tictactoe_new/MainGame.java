package tictactoe_new;

public class MainGame 
{
    private static boolean isPlayer1goFirst = false;
    public static void main( String[] args ){
 
        HumanPlayer human = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN");
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
        GameLogic logic = null;

        if(args.length == 0){
            System.out.println("Usage: <1|2> [1d|2d|]");
            System.out.println("Example: 1 3d  (human starts on 3D board)");
            return;
        }

        Board selectedBoard = null;
        if (args.length >= 2 && args[1].equalsIgnoreCase("1d")) {
            selectedBoard = new Board1D();
        }else if(args.length >= 2 && args[1].equalsIgnoreCase("2d")){
             selectedBoard = new Board2D();
        }

        try{
            if(args[0].equals("1")){
                isPlayer1goFirst = true;
                logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
            } else if(args[0].equals("2")){
                isPlayer1goFirst = false;
                logic = new GameLogic(selectedBoard, human, computer, isPlayer1goFirst);
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