package tictactoe_new;

public class GameHost {
    private Board board;
    private final Computer computer;
    private boolean gameOver;
    private String gameStatus;

    public GameHost() {
        this.board = new Board2D();
        this.computer = new Computer(Constants.COMPUTER_MARKER, "COMPUTER", 2);
        this.gameOver = false;
        this.gameStatus = "";
    }

    public MoveResult processMove(String message) {
        if (message.equalsIgnoreCase("reset")) {
            reset();
            return new MoveResult("Game reset.\n" + board.render() + "\nYour turn\n", false);
        } else if (message.equalsIgnoreCase("board")) {
            return new MoveResult(board.render() + "\nYour turn\n", false);
        } else {
            try {
                int cellNumber = Integer.parseInt(message);
                return makePlayerMove(cellNumber);
            } catch (NumberFormatException e) {
                return new MoveResult(
                        "Invalid input. Enter a cell number (1-9) or 'reset' or 'board'\n"
                                + board.render()
                                + "\nYour turn\n",
                        false);
            }
        }
    }

    private MoveResult makePlayerMove(int cellNumber) {
        if (gameOver) {
            return new MoveResult("Game is over. Type 'reset' for a new game.\n" + board.render() + "\n", false);
        }

        if (cellNumber < 1 || cellNumber > board.getTotalCells()) {
            return new MoveResult(
                    "Invalid cell number. Use 1-" + board.getTotalCells() + "\n"
                            + board.render()
                            + "\nYour turn\n",
                    false);
        }

        Position position = board.getCellPosition(cellNumber);
        if (!board.isCellEmpty(position)) {
            return new MoveResult("The cell is occupied!\n" + board.render() + "\nYour turn\n", false);
        }

        board.placeMarker(position, Constants.HUMAN_MARKER);

        char winner = board.checkWinner();
        if (winner == Constants.HUMAN_MARKER) {
            gameOver = true;
            gameStatus = "You won!";
            return new MoveResult(
                board.render()
                    + "\n"
                    + gameStatus
                    + "\nGame finished. Type 'reset' for a new game or 'quit' to disconnect.\n",
                true);
        }

        if (board.isBoardFull()) {
            gameOver = true;
            gameStatus = "It is a draw!";
            return new MoveResult(
                board.render()
                    + "\n"
                    + gameStatus
                    + "\nGame finished. Type 'reset' for a new game or 'quit' to disconnect.\n",
                true);
        }

        Position computerMove = computer.makeMove(board);
        if (computerMove != null) {
            board.placeMarker(computerMove, Constants.COMPUTER_MARKER);
        }

        winner = board.checkWinner();
        if (winner == Constants.COMPUTER_MARKER) {
            gameOver = true;
            gameStatus = "Computer won!";
            return new MoveResult(
                board.render()
                    + "\n"
                    + gameStatus
                    + "\nGame finished. Type 'reset' for a new game or 'quit' to disconnect.\n",
                true);
        }

        if (board.isBoardFull()) {
            gameOver = true;
            gameStatus = "It is a draw!";
            return new MoveResult(
                board.render()
                    + "\n"
                    + gameStatus
                    + "\nGame finished. Type 'reset' for a new game or 'quit' to disconnect.\n",
                true);
        }

        return new MoveResult(board.render() + "\nYour turn\n", false);
    }

    public void reset() {
        this.board = new Board2D();
        this.gameOver = false;
        this.gameStatus = "";
    }

    public String getBoardState() {
        return board.render();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public static class MoveResult {
        private final String response;
        private final boolean finishedGame;

        public MoveResult(String response, boolean finishedGame) {
            this.response = response;
            this.finishedGame = finishedGame;
        }

        public String getResponse() {
            return response;
        }

        public boolean isFinishedGame() {
            return finishedGame;
        }
    }
}
