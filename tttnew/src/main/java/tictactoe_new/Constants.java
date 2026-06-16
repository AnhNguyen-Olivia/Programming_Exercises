package tictactoe_new;

/**
 * A class to hold constant values for the tic-tac-toe game.
 * This is also where you change the port number
 */
public class Constants {
    public static final int ROW = 3;
    public static final int COL = 3;
    public static final int DEPTH = 3;
    public static final int WIN_LENGTH = 3;
    public static final char HUMAN_MARKER = '1';
    public static final char COMPUTER_MARKER = '2';

    /*
    Port num: 9000 = single server
              9010 = multithread server
              9020 = threadpool server
    */
    public static final int PORT = 9020; // The test need to manually change
}