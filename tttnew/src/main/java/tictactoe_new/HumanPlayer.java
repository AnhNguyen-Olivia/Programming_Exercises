package tictactoe_new;

import java.util.Scanner;

import tictactoe_new.Constants.PlayerType;

public class HumanPlayer extends Player {

    private String name;

    public HumanPlayer(char marker) {
        super(marker);
        this.name = "Human";
    }

    @Override
    public String getName(){
        return "HUMAN";
    }

    @Override
    public PlayerType getType(){
        return PlayerType.HUMAN;
    }

    @Override
    public int[] makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        int choosenCell;
        do{
            System.out.println("Ener cell (1-9): ");
            cell
        }
    }
}
