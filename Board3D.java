package tictactoe_new;

public class Board3D extends Board {
    private char[][][] board;

    public Board3D() {
        board = new char[Constants.DEPTH][Constants.ROW][Constants.COL];
        for (int d = 0; d < Constants.DEPTH; d++) {
            for (int r = 0; r < Constants.ROW; r++) {
                for (int c = 0; c < Constants.COL; c++) {
                    board[d][r][c] = '0';
                }
            }
        }
    }

    @Override
    public void print() {
        for (int d = 0; d < Constants.DEPTH; d++) {
            System.out.println("Layer " + (d + 1) + ":");
            for (int r = 0; r < Constants.ROW; r++) {
                for (int c = 0; c < Constants.COL; c++) {
                    System.out.printf("| %s ", "" + board[d][r][c]);
                }
                System.out.println("|");
            }
            System.out.println();
        }
    }

    @Override
    public int getTotalCells() {
        return Constants.DEPTH * Constants.ROW * Constants.COL;
    }

    @Override
    public boolean isBoardFull() {
        for (int d = 0; d < Constants.DEPTH; d++) {
            for (int r = 0; r < Constants.ROW; r++) {
                for (int c = 0; c < Constants.COL; c++) {
                    if (board[d][r][c] == '0') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isCellEmpty(Position pos) {
        int depth = pos.getCoordinate(0);
        int row = pos.getCoordinate(1);
        int col = pos.getCoordinate(2);
        return board[depth][row][col] == '0';
    }

    @Override
    public Position getCellPosition(int boxNumber) {
        int index = boxNumber - 1;
        int cellsPerLayer = Constants.ROW * Constants.COL;
        int depth = index / cellsPerLayer;
        int inLayerIndex = index % cellsPerLayer;
        int row = inLayerIndex / Constants.COL;
        int col = inLayerIndex % Constants.COL;
        return new Position(depth, row, col);
    }

    @Override
    public void placeMarker(Position pos, char marker) {
        int depth = pos.getCoordinate(0);
        int row = pos.getCoordinate(1);
        int col = pos.getCoordinate(2);
        board[depth][row][col] = marker;
    }

    @Override
    public char checkWinner() {
        // Unique direction vectors for 3D lines.
        int[][] directions = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 0},
            {1, -1, 0},
            {1, 0, 1},
            {1, 0, -1},
            {0, 1, 1},
            {0, 1, -1},
            {1, 1, 1},
            {1, 1, -1},
            {1, -1, 1},
            {1, -1, -1}
        };

        for (int d = 0; d < Constants.DEPTH; d++) {
            for (int r = 0; r < Constants.ROW; r++) {
                for (int c = 0; c < Constants.COL; c++) {
                    for (int i = 0; i < directions.length; i++) {
                        if (checkLine(new Position(d, r, c), directions[i][0], directions[i][1], directions[i][2])) {
                            return board[d][r][c];
                        }
                    }
                }
            }
        }

        return '0';
    }

    @Override
    protected boolean checkLine(Position startPos, int... steps) {
        int startDepth = startPos.getCoordinate(0);
        int startRow = startPos.getCoordinate(1);
        int startCol = startPos.getCoordinate(2);

        int depthStep = steps[0];
        int rowStep = steps[1];
        int colStep = steps[2];

        char marker = board[startDepth][startRow][startCol];
        if (marker == '0') {
            return false;
        }

        for (int k = 1; k < Constants.WIN_LENGTH; k++) {
            int depth = startDepth + k * depthStep;
            int row = startRow + k * rowStep;
            int col = startCol + k * colStep;

            if (depth < 0 || depth >= Constants.DEPTH || row < 0 || row >= Constants.ROW || col < 0 || col >= Constants.COL) {
                return false;
            }

            if (board[depth][row][col] != marker) {
                return false;
            }
        }
        return true;
    }
}