package tictactoe_new;

public class Board1D extends Board {
    private char[] board;

    public Board1D() {
        board = new char[Constants.ROW * Constants.COL];
        for (int i = 0; i < board.length; i++) {
            board[i] = '0';
        }
    }

    @Override
    public void print() {
        for (int i = 0; i < board.length; i++) {
            System.out.printf("| %s ", "" + board[i]);
        }
        System.out.println("|");
    }

    @Override
    public int getTotalCells() {
        return board.length;
    }

    @Override
    public boolean isBoardFull() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '0') {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCellEmpty(Position pos) {
        int index = pos.getCoordinate(0);
        return board[index] == '0';
    }

    @Override
    public Position getCellPosition(int boxNumber) {
        int index = boxNumber - 1;
        return new Position(index);
    }

    @Override
    public void placeMarker(Position pos, char marker) {
        int index = pos.getCoordinate(0);
        board[index] = marker;
    }

    @Override
    public char checkWinner() {
        for (int i = 0; i <= board.length - Constants.WIN_LENGTH; i++) {
            if (checkLine(new Position(i), 1)) {
                return board[i];
            }
        }
        return '0';
    }

    @Override
    protected boolean checkLine(Position startPos, int... steps) {
        int startIndex = startPos.getCoordinate(0);
        int stride = steps[0];
        char marker = board[startIndex];

        if (marker == '0') {
            return false;
        }

        for (int k = 1; k < Constants.WIN_LENGTH; k++) {
            int index = startIndex + k * stride;
            if (index < 0 || index >= board.length) {
                return false;
            }
            if (board[index] != marker) {
                return false;
            }
        }
        return true;
    }
}