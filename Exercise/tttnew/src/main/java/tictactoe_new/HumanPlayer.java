package tictactoe_new;
import java.io.InputStream;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private final Scanner scanner;

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
        while(true){
            System.out.println("Player#" + playerNumber + "'s turn");
            try {
                String input = scanner.nextLine().trim();

                // Handle quit command
                if("q".equals(input)){
                    System.out.println("End of the game");
                    System.exit(0);
                }

                // Parse input as integer
                int chosenCell = Integer.parseInt(input);

                // Validate range [1-9]
                if(chosenCell < 1 || chosenCell > 9){
                    System.out.println("Please, input a valid number [1-9]");
                    continue;
                }

                // Get position and check if occupied
                Position position = board.getCellPosition(chosenCell);
                if(!board.isCellEmpty(position)){
                    System.out.println("The cell is occupied!");
                    continue;
                }

                return position;

            } catch(NumberFormatException e){
                // Non-integer input (but not "q")
                System.out.println("Please, input a valid number [1-9]");
            }
        }
    }
}