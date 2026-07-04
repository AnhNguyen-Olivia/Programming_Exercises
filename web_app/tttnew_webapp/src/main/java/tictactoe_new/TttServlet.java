package tictactoe_new;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class TttServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(204);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        Gson gson = new Gson();
        GameMessage fromClient = gson.fromJson(body, GameMessage.class);

        Computer computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER");
        Board board = new Board2D(System.out);
        GameMessage message = new GameMessage();

        board.loadString(fromClient.boardState);

        Integer playerMove = Integer.parseInt(fromClient.move);
        Position playerPos = board.getCellPosition(playerMove);

        if(board.isCellEmpty(playerPos)){
            board.placeMarker(playerPos, Constants.HUMAN_MARKER);
        } else {
            message.status = "Cell is occupied!";
            message.boardState = fromClient.boardState;
            String json = gson.toJson(message);
            resp.getWriter().println(json);
            return;
        }

        String status = getGameStatus(board);
        if(status.equals("Computer turns")){
            board.placeMarker(computer.makeMove(board), Constants.COMPUTER_MARKER);
            status = getGameStatus(board);
        }

        message.status = status;
        message.boardState = board.networkString();
        String json = gson.toJson(message);
        resp.getWriter().println(json);
    }

    private String getGameStatus(Board board){
        char winner = board.checkWinner();
        if (winner == Constants.HUMAN_MARKER) return "You wins!";
        if (winner == Constants.COMPUTER_MARKER) return "Computer wins!";
        if (board.isBoardFull()) return "Draw!";
        return "Computer turns";
    }
}