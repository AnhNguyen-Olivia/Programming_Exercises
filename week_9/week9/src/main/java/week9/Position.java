package week9;

import java.util.Arrays;

/**
 * Represents a position on the game board.
 */
public class Position {
    private int[] coordinates;
    /**
     * Constructor for Position class.
     * @param coords
     */
    public Position(int... coords) {
        this.coordinates = coords;
    }

    /**
     * Returns the coordinate value for a specific dimension.
     * @param dimension
     * @return
     */
    public int getCoordinate(int dimension) {
        return coordinates[dimension];
    }

    /**
     * Returns the coordinates of the position.
     * @return
     */
    public int[] getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the row index of the position (for 2D board).
     * @return
     */
    public int getRow() {
        return coordinates[0];
    }

    /**
     * Returns the column index of the position (for 2D board).
     * @return
     */
    public int getCol() {
        return coordinates[1];
    }

    /**
     * Returns the number of dimensions of the position.
     * @return
     */
    public int getDimensions() {
        return coordinates.length;
    }

    /**
     * Returns a string representation of the position.
     * @return
     */
    @Override
    public String toString() {
        return "Position" + Arrays.toString(coordinates);
    }
}
