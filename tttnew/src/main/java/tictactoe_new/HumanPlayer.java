package tictactoe_new;
import java.util.Scanner;
import tictactoe_new.Constants.*;

public class HumanPlayer extends Player {

    public HumanPlayer(char marker, String name) {
        super(marker, name);
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public PlayerType getType(){
        return PlayerType.HUMAN;
    }

    @Override
    public int[] makeMove(Board board) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int choosenCell;
        do{
            System.out.println("Enter cell (1-" + Constants.TOTAL_CELL + "): ");
            choosenCell = scanner.nextInt();
            int[] position = board.getCellPosition(choosenCell);
            if(!board.isCellEmpty(position[0], position[1])){
                System.out.println("The position have been taken. Try again.");
            }else{
                return position;
            }
        }while(true);
    }
}