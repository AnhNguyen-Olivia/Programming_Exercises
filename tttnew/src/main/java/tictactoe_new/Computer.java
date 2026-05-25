package tictactoe_new;
public class Computer extends Player {

    public Computer(char marker, String name) {
        this(marker, name, 0);
    }

    public Computer(char marker, String name, int playerNumber) {
        super(marker, name, playerNumber);
    }

    @Override
    public Position makeMove(Board board) {
        for(int cell = 1; cell <= board.getTotalCells(); cell++){
            Position position = board.getCellPosition(cell);
            if(board.isCellEmpty(position)){
                return position;
            }
        }
        return null; // if board is full
    }
}