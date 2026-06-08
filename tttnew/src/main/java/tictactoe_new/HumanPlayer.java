package tictactoe_new;
import java.io.InputStream;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private final Scanner scanner;

    public HumanPlayer(char marker, String name) {
        this(marker, name, System.in);
    }

    public HumanPlayer(char marker, String name, InputStream inputStream) {
        super(marker, name);
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public String getName(){
        return name;
    }
    @Override
    public Position makeMove(Board board) {
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