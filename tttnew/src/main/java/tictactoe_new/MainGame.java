package tictactoe_new;

public class MainGame 
{
    private static boolean human = false;
    private static boolean computer = false;
    public static void main( String[] args ){
/*     switch(args[0]){
        case "1" :
            System.out.println("Human start\n");
            MainGame.human = true;
            break;
        case "2":
            System.out.println("Computer start\n"); 
            MainGame.computer = true;
            break;
        } */
        
        Board board = new Board();
        board.print();
        
    }
}
