package by.azzibom.tetris.model.figure;

public interface Polymino {

    enum Coord {
        X(0), Y(1);

        private final int value;

        Coord(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }

    int getSquareCoord(int i, Coord coord);

    int getSize();

    boolean isRotate();
}
