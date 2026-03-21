package tictactoe_new;

import tictactoe_new.Constants.PlayerType;

public class Computer extends Player {

    public Computer(char marker) {
        super(marker);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int[] makeMove(Board board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
    }

    @Override
    public String getName() {
        return "COMPUTER";
    }

    @Override
    public PlayerType getType() {
        return PlayerType.COMPUTER;
    }
    
}
