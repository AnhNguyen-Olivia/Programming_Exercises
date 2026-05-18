package tictactoe_new;
import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 12345;

    private Board board;
    private final Computer computer;
    private boolean gameOver;
    private String gameStatus;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public Server() {
        this.board = new Board2D();
        this.computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER", 2);
        this.gameOver = false;
        this.gameStatus = "";
    }

    private void start() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            printBoardToConsole();

            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                String response;

                if (clientMessage.equalsIgnoreCase("reset")) {
                    resetGame();
                    response = "Game reset.\n" + board.render() + "Your turn";
                } else if (clientMessage.equalsIgnoreCase("board")) {
                    response = board.render();
                } else {
                    try {
                        int cellNumber = Integer.parseInt(clientMessage);
                        response = makeClientMove(cellNumber);
                    } catch (NumberFormatException e) {
                        response = "Invalid input. Enter a cell number (1-9) or 'reset' or 'board'";
                    }
                }

                System.out.println("Received: " + clientMessage);
                printBoardToConsole();

                byte[] sendBuffer = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer, sendBuffer.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
        }
    }

    private String makeClientMove(int cellNumber) {
        if (gameOver) {
            return "Game is over. Start a new game.";
        }

        if (cellNumber < 1 || cellNumber > board.getTotalCells()) {
            return "Invalid cell number. Use 1-" + board.getTotalCells();
        }

        Position position = board.getCellPosition(cellNumber);
        if (!board.isCellEmpty(position)) {
            return "The cell is occupied!";
        }

        board.placeMarker(position, Constants.HUMAN_MARKER);

        char winner = board.checkWinner();
        if (winner == Constants.HUMAN_MARKER) {
            gameOver = true;
            gameStatus = "You won!";
            return board.render() + "\n" + gameStatus;
        }

        if (board.isBoardFull()) {
            gameOver = true;
            gameStatus = "It is a draw!";
            return board.render() + "\n" + gameStatus;
        }

        Position computerMove = computer.makeMove(board);
        if (computerMove != null) {
            board.placeMarker(computerMove, Constants.COMPUTER_MARKER);
        }

        winner = board.checkWinner();
        if (winner == Constants.COMPUTER_MARKER) {
            gameOver = true;
            gameStatus = "Computer won!";
            return board.render() + "\n" + gameStatus;
        }

        if (board.isBoardFull()) {
            gameOver = true;
            gameStatus = "It is a draw!";
            return board.render() + "\n" + gameStatus;
        }

        return board.render() + "\nYour turn";
    }

    private void resetGame() {
        this.board = new Board2D();
        this.gameOver = false;
        this.gameStatus = "";
    }

    private void printBoardToConsole() {
        System.out.print(board.render());
    }
}