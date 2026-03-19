package tictactoe_new;

public class Board {
    private char board[][];
    
    //Construc board object
    public Board(){
        board = new char[Constants.ROW][Constants.COL];
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                board[i][j] = (char)('0');
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

     

    
}
