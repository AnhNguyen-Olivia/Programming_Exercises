package tictactoe_new;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(char marker, String name) {
        super(marker, name);
    }

    @Override
    public String getName(){
        return name;
    }
    @Override
    public Position makeMove(Board board) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int choosenCell;
        do{
            System.out.println("Enter cell (1-" + board.getTotalCells() + "): ");
            choosenCell = scanner.nextInt();
            Position position = board.getCellPosition(choosenCell);
            if(!board.isCellEmpty(position)){
                System.out.println("The position have been taken. Try again.");
            }else{
                return position;
            }
        }while(true);
    }
}