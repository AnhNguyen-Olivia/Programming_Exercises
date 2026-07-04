package week12;

import java.net.http.*;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;

public class httpTttClient {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Initial board state: all cells are empty (represented by '0')
        String boardState = "000000000";
        // Create a BufferedReader to read user input from the console
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        // Variable to hold the status message from the server
        String status = null;

        // flag for q
        boolean quit = false;
        // Create an HttpClient to send requests to the server
        HttpClient client = HttpClient.newHttpClient();
        // Main game loop
        while (true) {
            Board2D board = new Board2D(System.out);
            board.loadString(boardState);
            board.print();

            System.out.println("Enter your move (1-9): ");
            String move = userInput.readLine();

            if(move.equals("q")){
                quit = true;
                System.out.println("Game quit by player");
                break;
            }

            //Building the JSON
            Gson gson = new Gson();
            GameMessage message = new GameMessage();
            message.boardState = boardState;
            message.move = move;
            String json = gson.toJson(message);

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9040/move"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            // Send and get response
            HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

            // Parse the response JSON
            String body = response.body();
            GameMessage result = gson.fromJson(body, GameMessage.class);
            status = result.status;
            boardState = result.boardState;
            // Check if the cell is occupied
            if(status.equals("Cell is occupied!")){
                System.out.println(status);
                continue;  // go back to top of loop, ask again
            }
            // Check if the move is invalid
            if(!status.equals("Computer turns")){
                System.out.println(status);
                break;
            }
        }
        // If the game is not quit, print the final board state
        if(!quit){
            Board2D finalBoard = new Board2D(System.out);
            finalBoard.loadString(boardState);
            finalBoard.print();
        }
    }
}