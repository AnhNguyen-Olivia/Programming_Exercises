package tictactoe_new;

import java.io.*;
import java.net.*;

public class SingleClientServer {
    
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            while(true){    
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                handleClient(socket);
            }
        }

    }

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
        
        try{gamelogic.play();}catch(RuntimeException e){
            out.println("Adieu goodfriend, I will see you in the other side.");
        }

    }
}
