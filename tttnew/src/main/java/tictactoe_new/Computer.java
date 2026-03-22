package tictactoe_new;

import tictactoe_new.Constants.PlayerType;

public class Computer extends Player {
    
    public Computer(char marker, String name) {
        super(marker, name);
    }

    @Override
    public int[] makeMove(Board board) {
        int totalCells = Constants.ROW * Constants.COL;
        for(int cell = 1; cell <= totalCells; cell++){
            int[] position = board.getCellPosition(cell);
            if(board.isCellEmpty(position[0], position[1])){
                return position;
            }
        }
        return null; //if board is full
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.COMPUTER;
    }
    
}
