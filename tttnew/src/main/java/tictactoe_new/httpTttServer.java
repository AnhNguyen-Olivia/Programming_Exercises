package tictactoe_new;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;

public class httpTttServer {
    public static void main(String[] args) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(9040), 0);
        server.createContext("/move", new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            // CORS headers first
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

            // Handle preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            System.out.println("Handler called! Method: " + exchange.getRequestMethod());

            try {
                // set up Json
                String body = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Raw body received: " + body);

                Gson gson = new Gson();
                GameMessage fromClient = gson.fromJson(body, GameMessage.class);
                System.out.println("JSON parsed: " + fromClient.boardState + " " + fromClient.move);

                // set up computer
                Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
                
                // Create board
                Board board = new Board2D(System.out);
                
                // Set up message
                GameMessage message = new GameMessage();
                
                // read input
                String boardString = fromClient.boardState;
                System.out.println("Board received: " + boardString);
                //Receive client string
                board.loadString(boardString);
                            
                //Take player move, change from int -> position
                Integer playerMove = Integer.parseInt(fromClient.move);
                Position playerPos = board.getCellPosition(playerMove);
                System.out.println("Move received: " + playerMove);
                
                if(board.isCellEmpty(playerPos)){
                    board.placeMarker(playerPos, Constants.HUMAN_MARKER);
                } else {
                    //String response = "Cell is occupied!\n" + boardString;
                    String status = "Cell is occupied!";
                    message.status = status;
                    message.boardState = boardString;
                    String json = gson.toJson(message);
                    exchange.sendResponseHeaders(200, json.length());
                    OutputStream out = exchange.getResponseBody();
                    out.write(json.getBytes());
                    out.close();
                    return;
                }

                //Check winners, continue if don't
                String status = getGameStatus(board);

                if(status.equals("Computer turns")){
                    board.placeMarker(computer.makeMove(board), Constants.COMPUTER_MARKER);
                    status = getGameStatus(board);
                }
                
                // write board
                String boardState = board.networkString();

                // String response = status + "\n" + board.networkString();

                message.status = status;
                message.boardState = boardState;
                String json = gson.toJson(message);
                exchange.sendResponseHeaders(200, json.length());
                OutputStream out = exchange.getResponseBody();
                out.write(json.getBytes());
                out.close();
                } catch (Throwable t){
                    System.out.println("ERROR: " + t.getMessage());
                    t.printStackTrace();
                }
            }
        });
    server.start();
    System.out.println("Server started on port 9040");
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