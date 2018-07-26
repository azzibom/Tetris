package by.azzibom.tetris.model.figure;

/**
 * перечисление возмонх фигур
 * т е прототипы...
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public enum Tetromino {

    I(new int[][]{{-2, 0}, {-1, 0}, {0, 0}, {1, 0}}),
    S(new int[][]{{-1, 1}, {0, 1}, {0, 0}, {1, 0}}),
    Z(new int[][]{{-1, 0}, {0, 0}, {0, 1}, {1, 1}}),
    L(new int[][]{{-1, 1}, {-1, 0}, {0, 0}, {1, 0}}),
    J(new int[][]{{-1, 0}, {0, 0}, {1, 0}, {1, 1}}),
    O(new int[][]{{-1, 1}, {-1, 0}, {0, 0}, {0, 1}}),
    T(new int[][]{{-1, 0}, {0, 0}, {1, 0}, {0, 1}});

    private final int[][] coord;

    Tetromino(int[][] coord) {
        this.coord = coord;
    }

    public int getSquareCoord(int i, int j) {
        return this.coord[i][j];
    }

    public int getSize() {
        return coord.length;
    }
}
