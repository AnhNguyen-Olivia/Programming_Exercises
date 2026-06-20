package tictactoe_new;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;

public class httpTttServer {
    public static void main(String[] args) throws IOException 
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(9040), 0);
        server.createContext("/move", new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // set up the stream
            BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

            // set up computer
            Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
            // Create board
            Board board = new Board2D(System.out);
            
            // read input
            String boardString = in.readLine();
            System.out.println("Board received: " + boardString);
            //Receive client string
            board.loadString(boardString);
                        
            //Take player move, change from int -> position
            Integer playerMove = Integer.parseInt(in.readLine());
            Position playerPos = board.getCellPosition(playerMove);
            System.out.println("Move received: " + playerMove);
            
            if(board.isCellEmpty(playerPos)){
                board.placeMarker(playerPos, Constants.HUMAN_MARKER);
            } else {
                String response = "Cell is occupied!\n" + boardString;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream out = exchange.getResponseBody();
                out.write(response.getBytes());
                out.close();
                return;
            }

            //Check winners, continue if don't
            String status = getGameStatus(board);

            if(status.equals("Computer turns")){
                board.placeMarker(computer.makeMove(board), Constants.COMPUTER_MARKER);
                status = getGameStatus(board);
            }

            String response = status + "\n" + board.networkString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream out = exchange.getResponseBody();
            out.write(response.getBytes());
            out.close();
            }
        });
    server.start();
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