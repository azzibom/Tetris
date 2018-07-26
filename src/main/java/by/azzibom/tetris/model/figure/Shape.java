package by.azzibom.tetris.model.figure;

import java.util.Random;

/**
 * класс представляющий фигуры на игровом поле
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public class Shape {

    private int[][] coord; // координаты точек(квадратов) фигуры
    private Tetromino tetromino; // фигура
    private Random r; // для генерации случайной фигуры

    public Shape() {
        coord = new int[4][2];
        r = new Random();
    }

    public Shape(Tetromino tetromino) {
        this();
        setTetromino(tetromino);
    }

    public void setTetromino(Tetromino tetromino) {
        for (int i = 0; i < tetromino.getSize(); i++) {
            for (int j = 0; j < 2; j++) {
                this.coord[i][j] = tetromino.getSquareCoord(i, j);
            }
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

    public Shape rotateLeft() {
        if (tetromino == Tetromino.O)
            return this;

        Shape newShape = new Shape();
        newShape.tetromino = this.tetromino;

        for (int i = 0; i < 4; i++) {
            newShape.setX(i, this.getY(i));
            newShape.setY(i, -this.getX(i));
        }
        return newShape;
    }

    public Shape rotateRight() {
        if (tetromino == Tetromino.O)
            return this;

        Shape newShape = new Shape();
        newShape.tetromino = this.tetromino;

        for (int i = 0; i < 4; i++) {
            newShape.setX(i, -this.getY(i));
            newShape.setY(i, this.getX(i));
        }
        return newShape;
    }

    private void setY(int i, int y) {
        coord[i][1] = y;
    }

    private void setX(int i, int x) {
        coord[i][0] = x;
    }

    public void setRandomTetromino() {
        setTetromino(Tetromino.values()[r.nextInt(Tetromino.values().length)]);
    }

    public Tetromino getTetromino() {
        return tetromino;
    }
}
