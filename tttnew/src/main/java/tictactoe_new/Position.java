package tictactoe_new;

import java.util.Arrays;

public class Position {
    private int[] coordinates;

    public Position(int... coords) {
        this.coordinates = coords;
    }

    public int getCoordinate(int dimension) {
        return coordinates[dimension];
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public int getRow() {
        return coordinates[0];
    }

    public int getCol() {
        return coordinates[1];
    }

    // Get dimension count
    public int getDimensions() {
        return coordinates.length;
    }

    @Override
    public String toString() {
        return "Position" + Arrays.toString(coordinates);
    }
}
