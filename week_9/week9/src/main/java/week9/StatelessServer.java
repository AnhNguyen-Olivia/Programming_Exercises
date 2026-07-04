package week9;

import java.io.*;
import java.net.*;
/**
 * A simple server for the tic-tac-toe game that listens for client connections and allows 
 * a multi client to play the game against a computer opponent.
 */
public class StatelessServer {
        
    /**
     * Main method to start the server and listen for client connections.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(9030)) {
            while(true){  
                try (Socket socket = serverSocket.accept()){
                    System.out.println("Client connected: " + socket.getInetAddress());
                    handleClient(socket);
                }catch(IOException e){
                    System.out.println("Client disconnected: " + e.getMessage());
                }   
                System.out.println("Client disconnected");
                
                /*  at the end of the try block, 
                    the client socket will be closed automatically
                    This is equal to socket.close() in the finally block, 
                    but more elegant and less error-prone.
                */
            }
        }catch(IOException e){
            System.out.println("Client disconnected: " + e.getMessage());

        }   
        System.out.println("Server disconnected");
        /*  at the end of the try block, 
            the server socket will be closed automatically
            This is equal to socket.close() in the finally block, 
            but more elegant and less error-prone.
        */
    }

    /**
     * Handles client connection, check and then reply.
     * @param socket
     * @throws IOException
     */
    private static void handleClient(Socket socket) throws IOException {
        // set up streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream(), true);

        //set up computer
        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");

        //Create board
        Board board = new Board2D(System.out);
        
        //Receive client string
        board.loadString(in.readLine());
        
        //Take player move, change from int -> position

        // Check if the client wants to quit
        String clientInput = in.readLine();
        if (clientInput.equals("quit")) {
            System.out.println("Client requested to quit the game.");
            return; // Exit the method to close the connection
        }else{
            // If not quitting, treat the input as a move
            Integer playerMove = Integer.parseInt(clientInput);
            Position playerPos = board.getCellPosition(playerMove);
            
            //Check player move :')
            if(board.isCellEmpty(playerPos)){
                //place player move :D 
                board.placeMarker(playerPos, Constants.HUMAN_MARKER);
            }else{
                out.println("Cell is occupied!");
                out.println(board.networkString());
                return;
            }
        }

        //Check winners, continue if don't
        String status = getGameStatus(board);

        if(status.contains("Computer turns")){
            board.placeMarker(computer.makeMove(board), Constants.COMPUTER_MARKER);
            status = getGameStatus(board);
        }

        out.println(status);
        out.println(board.networkString());
    }

    // This is me again, get tired of writing long if else :') so I create a helper
    private static String getGameStatus(Board board){
        char winner = board.checkWinner();
        if (winner == Constants.HUMAN_MARKER) return "You wins!";
        if (winner == Constants.COMPUTER_MARKER) return "Computer wins!";
        if (board.isBoardFull()) return "Draw!";
        return "Computer turns";
    }
}
