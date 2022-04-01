package by.azzibom.tetris.model.figure;

/**
 * перечисление возможных фигур
 * т е прототипы...
 *
 * @author Ihar Misevich
 */
public enum Tetromino implements Polymino {

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

    @Override
    public int getSquareCoord(int i, Coord coord) {
        return this.coord[i][coord.getValue()];
    }

    @Override
    public int getSize() {
        return coord.length;
    }

    @Override
    public boolean isRotate() {
        return !(this == Tetromino.O);
    }
}
