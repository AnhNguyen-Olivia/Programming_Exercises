package tictactoe_new;

public class Board {
    private char board[][];

    //Construc board object
    public Board(){
        board = new char[Constants.ROW][Constants.COL];
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                board[i][j] = '0';
            }
        }
    }
    
    //print board
    public void print(){
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                System.out.printf("| %s ", "" + board[i][j]);
            }
            System.out.println("|");
        }
    }

    //Check if board is full
    public boolean isBoardFull(){
        for(int i = 0; i < Constants.COL; i++){
            for(int j = 0; j < Constants.ROW; j++){
                if(board[i][j] == '0'){
                    return false;
                }
            }
        }
        return true;
    }
    
    // Check if board position is empty
    public boolean isCellEmpty(int row, int col){
        return board[row][col] == '0';
    }

    public int[] getCellPosition(int boxNumber){
        int row = (boxNumber - 1)/Constants.COL;
        int col = (boxNumber - 1)%Constants.COL;
        return new int[]{row, col};
    }

    public void placeMarker(int row, int col, char marker){
        board[row][col] = marker;
    }
    
    public char checkWinner(){
        //left -> right
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j<= Constants.COL - Constants.WIN_LENGTH; j++){
                if(checkLine(i, j, 0, 1)){
                    return board[i][j];
                }
            }
        }
        //top -> bottom
        for(int j = 0; j < Constants.COL; j++){
            for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
                if(checkLine(i, j, 1 , 0)){
                    return board[i][j];
                }
            }
        }

        //diagonal
        for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
            for(int j = 0; j <= Constants.COL - Constants.WIN_LENGTH; j++){
                if(checkLine(i, j, 1, 1)){
                    return board[i][j];
                }
            }
        }

        //anti diagonal
        for(int i = 0; i <= Constants.ROW - Constants.WIN_LENGTH; i++){
            for(int j = Constants.WIN_LENGTH - 1; j < Constants.COL; j++){
                if(checkLine(i, j, 1, -1)){
                    return board[i][j];
                }
            }
        }
        return '0';
    }

    private boolean checkLine(int startRow, int startCol, int rowStep, int colStep){
        char marker = board[startRow][startCol];
        if(marker == '0'){
            return false;
        }

        for(int k = 1; k < Constants.WIN_LENGTH; k++){
            int row = startRow + k * rowStep;
            int col = startCol + k * colStep;
            
            //Out of Bound
            if(row < 0 || row >= Constants.ROW || col < 0 || col > Constants.COL){
                return false;
            }

            //Not the same marker
            if(board[row][col] != marker){
                return false;
            }
        }
        return true;
    }

}


