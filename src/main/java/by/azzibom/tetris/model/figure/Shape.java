package by.azzibom.tetris.model.figure;

import java.util.Arrays;
import java.util.Random;

/**
 * класс представляющий положение фигур в пространстве
 * <p>
 * <pre>
 * ..... -> ..... | ..... -> .....
 * ..#.. -> ..#.. | .##.. -> .##..
 * .###. -> ..##. | .##.. -> .##..
 * ..... -> ..#.. | ..... -> .....
 * ..... -> ..... | ..... -> .....
 * ---------------+---------------
 * ..... -> ..... | ..#.. -> .....
 * ..#.. -> ..... | ..#.. -> .....
 * ..#.. -> .###. | ..#.. -> .####
 * ..##. -> .#... | ..#.. -> .....
 * ..... -> ..... | ..... -> .....
 *  </pre>
 *
 * @author Ihar Misevich
 */
public class Shape {

    private static final Random r = new Random(); // для генерации случайной фигуры

    private int[][] coord; // координаты точек(квадратов) фигуры
    private Tetromino tetromino; // фигура

    public Shape() {
        coord = new int[4][2];
        setRandomTetromino();
    }

    public Shape(Shape shape) {
        this();
        for (int i = 0; i < shape.getSize(); i++) {
            this.coord[i] = Arrays.copyOf(shape.coord[i], shape.coord[i].length);
        }
        this.tetromino = shape.tetromino;
    }

    public void setMino(Tetromino tetromino) {
        for (int i = 0; i < tetromino.getSize(); i++) {
            this.coord[i][0] = tetromino.getSquareCoord(i, Polimino.Coord.X);
            this.coord[i][1] = tetromino.getSquareCoord(i, Polimino.Coord.Y);
        }
        this.tetromino = tetromino;
    }

    public int getX(int i) {
        return this.coord[i][0];
    }

    public int getY(int i) {
        return this.coord[i][1];
    }

    public int getSize() {
        return coord.length;
    }

    public void rotateLeft() {
        if (!this.tetromino.isRotate()) {
            return;
        }

        int[][] newCoord = new int[coord.length][2];
        for (int i = 0; i < coord.length; i++) {
            newCoord[i][0] = coord[i][1];
            newCoord[i][1] = -coord[i][0];
        }
        this.coord = newCoord;
    }

    public void rotateRight() {
        int[][] newCoord = new int[coord.length][2];
        for (int i = 0; i < coord.length; i++) {
            newCoord[i][0] = -coord[i][1];
            newCoord[i][1] = coord[i][0];
        }
        this.coord = newCoord;
    }

    public void setRandomTetromino() {
        setMino(Tetromino.values()[r.nextInt(Tetromino.values().length)]);
    }
}
