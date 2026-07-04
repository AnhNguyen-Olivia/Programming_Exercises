package week12;
import java.io.*;

/**
 * The Board2D class represents a 2D tic-tac-toe board. 
 * It provides methods for printing the board, checking if the board is full, checking if a cell is empty, 
 * getting the position of a cell based on its number, placing a marker on the board, and checking for a winner. 
 * The class uses a 2D array to represent the board and implements the necessary logic to determine the game state.
 */
public class Board2D extends Board {
    private char board[][];
    private PrintStream out;

    /**
     * Constructor for Board2D class. 
     * Initializes the board as a 2D array of characters and fills it with '0' to represent empty cells.
     * @param out
     */
    public Board2D(PrintStream out){
        this.out = out;        
        board = new char[Constants.ROW][Constants.COL];
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                board[i][j] = '0';
            }
        }
    }
    
    /**
     * Prints the current state of the board.
     */
    @Override
    public void print(){
        for(int i = 0; i < Constants.ROW; i++){
            for(int j = 0; j < Constants.COL; j++){
                out.printf("| %s ", "" + board[i][j]);
            }
            out.println("|");
        }
    }

    /**
     * Returns the total number of cells on the board.
     * @return the total number of cells
     */
    @Override
    public int getTotalCells(){
        return Constants.ROW * Constants.COL;
    }

    /**
     * Checks if the board is full.
     * @return true if the board is full, false otherwise
     */
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
    
    /**
     * Checks if a cell is empty.
     * @param pos the position of the cell to check
     * @return true if the cell is empty, false otherwise
     */
    @Override
    public boolean isCellEmpty(Position pos){
        return board[pos.getRow()][pos.getCol()] == '0';
    }

    /**
     * Gets the position of a cell based on its box number.
     * The box number is a 1-based index that corresponds to the cell's position on the board.
     * @param boxNumber the number of the cell (1-based index)
     * @return the Position object representing the cell's position on the board
     */
    @Override
    public Position getCellPosition(int boxNumber){
        int row = (boxNumber - 1) / Constants.COL;
        int col = (boxNumber - 1) % Constants.COL;
        return new Position(row, col);
    }

    /**
     * Places a marker on the board at the specified position.
     * @param pos the position where the marker should be placed
     * @param marker the marker to place on the board
     */
    @Override
    public void placeMarker(Position pos, char marker){
        board[pos.getRow()][pos.getCol()] = marker;
    }
    
    /**
     * Checks if there is a winner on the board.
     * @return the marker of the winning player, or '0' if there is no winner
     */
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
    
    /**
     * Checks if a line has the same marker.
     * @param startPos the starting position of the line
     * @param steps the steps to move in each direction
     * @return true if the line has the same marker, false otherwise
     */
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

    /**
     * Take the board and make it into a string for transportation in a network
     * Example, which I hope will look like this: 000010002
     * @return a string of characters represent each cell's state in order from box 1 - the final cell
     * which is 9 btw ;)
     */
    public String networkString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 1; i <= getTotalCells(); i++){
            Position position = getCellPosition(i);
            char marker = board[position.getRow()][position.getCol()];
            stringBuilder.append(marker);
        }
        return stringBuilder.toString();
    }
    
    /**
     * The reverse of networkString method, this methid take the string from the network
     * and then rebuild it
     * @param string a 9-character-ish (can be more but well this is a 3 x 3 board) 
     * containing the marker for boxes 1-9
     */
    public void loadString(String string){
        for (int i = 0; i < string.length(); i++){
            char marker = string.charAt(i);
            int boxNumber = i + 1;
            Position position = getCellPosition(boxNumber);

            placeMarker(position, marker);
        }
    }
}
