package tictactoe_new;

import java.io.*;
import java.net.*;

public class multithreadingServer {
        
    /**
     * Main method to start the server and listen for client connections.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(9020)) {
            while(true){    
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                // Create a new thread for each request. This part is the difference in the single code
                new Thread(() -> {
                    try {
                    handleClient(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }}).start();
            }
        }
    }

    /**
     * Handles a single client connection, setting up the game and managing the game logic for that client.
     * @param socket
     * @throws IOException
     */
    private static void handleClient(Socket socket) throws IOException {
        // set up streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream(), true);
        out.println("Bonjour! Connected to the server\n");

        //read who go first
        String firstPlayer = in.readLine();
        boolean first_player = Integer.parseInt(firstPlayer) == 1;

        //set up players
        HumanPlayer humanPlayer = new HumanPlayer(Constants.HUMAN_MARKER, "HUMAN", socket.getInputStream(), out);
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");

        Board board = new Board2D(out);
        GameLogic gamelogic = new GameLogic(board, humanPlayer, computer, first_player, out);
        
        /**
         * Starts the game and handles the game loop.
         * If the player press "q", the game will be quit and a message will be sent to the client.
         */
        try{gamelogic.play();}catch(HumanPlayer.QuitGameException e){
            out.println("Game quit by user. Adieu!");
        }

    }
}
