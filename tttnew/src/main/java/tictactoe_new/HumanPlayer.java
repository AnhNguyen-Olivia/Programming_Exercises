package tictactoe_new;
import java.io.InputStream;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private final Scanner scanner;

    public static class QuitGameException extends RuntimeException {
        public QuitGameException() {
            super("Player quit the game");
        }
    }

    public HumanPlayer(char marker, String name) {
        this(marker, name, 0);
    }

    public HumanPlayer(char marker, String name, int playerNumber) {
        this(marker, name, playerNumber, System.in);
    }

    public HumanPlayer(char marker, String name, InputStream inputStream) {
        this(marker, name, 0, inputStream);
    }

    public HumanPlayer(char marker, String name, int playerNumber, InputStream inputStream) {
        super(marker, name, playerNumber);
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public Position makeMove(Board board) {
        int maxCell = board.getTotalCells();
        while(true){
            try {
                String input = scanner.nextLine().trim();

                if("q".equals(input)){
                    System.out.println("End of the game");
                    throw new QuitGameException();
                }

                int chosenCell = Integer.parseInt(input);

                if(chosenCell < 1 || chosenCell > maxCell){
                    System.out.println("Please, input a valid number [1-" + maxCell + "]");
                    continue;
                }

                Position position = board.getCellPosition(chosenCell);
                if(!board.isCellEmpty(position)){
                    System.out.println("The cell is occupied!");
                    continue;
                }

                return position;

            } catch(NumberFormatException e){
                System.out.println("Please, input a valid number [1-" + board.getTotalCells() + "]");
            }
        }
    }
}