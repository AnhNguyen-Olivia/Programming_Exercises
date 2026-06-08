package tictactoe_new;

public class Board2D extends Board {
    private char board[][];

    // Construct board object
    public Board2D() {
        board = new char[Constants.ROW][Constants.COL];
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                board[i][j] = '0';
            }
        }
    }
    
    // Print board
    @Override
    public void print(){
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                System.out.printf("| %s ", "" + board[i][j]);
            }
            System.out.println("|");
        }
    }

    @Override
    public int getTotalCells(){
        return Constants.ROW * Constants.COL;
    }

    // Check if board is full
    @Override
    public boolean isBoardFull(){
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                if(board[i][j] == '0'){
                    return false;
                }
            }
        }
        return true;
    }
    
    // Check if board position is empty
    @Override
    public boolean isCellEmpty(Position pos){
        return board[pos.getRow()][pos.getCol()] == '0';
    }

    @Override
    public Position getCellPosition(int boxNumber){
        int row = (boxNumber - 1) / Constants.COL;
        int col = (boxNumber - 1) % Constants.COL;
        return new Position(row, col);
    }

    @Override
    public void placeMarker(Position pos, char marker){
        board[pos.getRow()][pos.getCol()] = marker;
    }
    
    @Override
    public char checkWinner(){
        // Left -> Right
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j <= Constants.COL - Constants.WIN_LENGTH; j++){
                if(checkLine(new Position(i, j), 0, 1)){
                    return board[i][j];
                }
            }
        }
        // Top -> Bottom
        for(int j = 0; j < Constants.COL; j++){
            for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
                if(checkLine(new Position(i, j), 1, 0)){
                    return board[i][j];
                }
            }
        }

        // Diagonal
        for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
            for(int j = 0; j <= Constants.COL - Constants.WIN_LENGTH; j++){
                if(checkLine(new Position(i, j), 1, 1)){
                    return board[i][j];
                }
            }
        }

        // Anti-diagonal
        for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
            for(int j = Constants.WIN_LENGTH - 1; j < Constants.COL; j++){
                if(checkLine(new Position(i, j), 1, -1)){
                    return board[i][j];
                }
            }
        }
        return '0';
    }
    
    // Check if a line have the same marker
    @Override
    protected boolean checkLine(Position startPos, int... steps){
        int startRow = startPos.getRow();
        int startCol = startPos.getCol();
        int rowStep = steps[0];
        int colStep = steps[1];
        char marker = board[startRow][startCol];
        if(marker == '0'){
            return false;
        }

        for(int k = 1; k < Constants.WIN_LENGTH; k++){
            int row = startRow + k * rowStep;
            int col = startCol + k * colStep;
            
            // Out of Bound
            if(row < 0 || row >= Constants.ROW || col < 0 || col >= Constants.COL){
                return false;
            }

            // Not the same marker
            if(board[row][col] != marker){
                return false;
            }
        }
        return true;
    }
}
