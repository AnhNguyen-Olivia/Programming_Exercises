package tictactoe_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class StatelessClient {
    /**
     * Main method to start the client and connect to the server.
     * This is to test stateless sever (not secure)
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{

        // start with a clean state :p
        String boardState = "000000000";
        //User input
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        // status
        String status = null;
        // flag for q
        boolean quit = false;
        
        while (true){
            try(Socket socket = new Socket("localhost", 9030)){
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream(), true);
               
               // Display board
               //set board up. Assuming I only need to play board 2d
               Board2D board = new Board2D(System.out);
               board.loadString(boardState);
               board.print();

               // asking the user move 
               System.out.println("Enter your move (1-9): ");
               String move = userInput.readLine();

               if(move.equals("q")){
                quit = true;
                System.out.println("Game quit by player");
                break;
               }

               //send to the server
               out.println(boardState);
               out.println(move);

               //Check status
               status = in.readLine();
               //receive new board
               boardState = in.readLine();
            }

            if(status.equals("Cell is occupied!")){
                System.out.println(status);
                continue;
            }

            if(!status.equals("Computer turns")){
                System.out.println(status);
                break;
            }
        }

        if(!quit){
            Board2D finalBoard = new Board2D(System.out);
            finalBoard.loadString(boardState);
            finalBoard.print();
        }
    }
}
