package week12;

import java.net.http.*;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;

public class httpTttClient {
    public static void main(String[] args) throws IOException, InterruptedException {

        String boardState = "000000000";
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String status = null;

        // flag for q
        boolean quit = false;

        HttpClient client = HttpClient.newHttpClient();

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

            String body = response.body();
            GameMessage result = gson.fromJson(body, GameMessage.class);
            status = result.status;
            boardState = result.boardState;

            if(status.equals("Cell is occupied!")){
                System.out.println(status);
                continue;  // go back to top of loop, ask again
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