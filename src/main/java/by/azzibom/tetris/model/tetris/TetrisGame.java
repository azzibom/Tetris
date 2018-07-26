package by.azzibom.tetris.model.tetris;

import by.azzibom.tetris.model.tetris.figure.Shape;
import by.azzibom.tetris.model.tetris.figure.Tetromino;

import java.util.Observable;
import java.util.Observer;

/**
 * @author azzibom
 * */
public class TetrisGame {

    private static final String DEFAULT_NAME = "Tetris";
    private static final int DEFAULT_FIELD_WIDTH = 10;
    private static final int DEFAULT_FIELD_HEIGHT = 20;
    private static final int DEFAULT_SPEED = 1;

    private enum Scores {
        DELETED_1_lINE(100),
        DELETED_2_lINES(400),
        DELETED_3_lINES(700),
        DELETED_4_lINES(1500);

        private int score;

        private Scores(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }


    }
    private int score;

    private int removedLines;
    private int speed;

    private Tetromino[][] field;
    private Shape shape;

    private int xShapePos;
    private int yShapePos;
    private boolean endMove;

    private boolean gameOver;
    private Shape nextShape;

    private boolean pause;
//    private enum Rotate {
//        LEFT, RIGHT;
//
//    }
//    private Rotate rotate;

    private String name;
    private TetrisObservable observer = new TetrisObservable();

    public TetrisGame(String name, int fieldWidth, int fieldHeight, int speed) {
        this.name = name;
        field = new Tetromino[fieldWidth][fieldHeight];

        this.speed = speed; // скорость = 1
        score = 0; // очки = 0
        removedLines = 0; // удаленных линий = 0

        endMove = true; // конец хода = да
        gameOver = true; // конец игры = да

        shape = new Shape();
        nextShape = new Shape();

        //        rotate = Rotate.LEFT;
    }

    public TetrisGame() {
        this(
                DEFAULT_NAME,
                DEFAULT_FIELD_WIDTH,
                DEFAULT_FIELD_HEIGHT,
                DEFAULT_SPEED);
    }

    Thread gameThread;
    public void start() {
        setGameOver(false);
        setPause(false);

        if (gameThread != null) return;

        gameThread = new Thread(() -> {
            while (!gameOver) {
                if (!pause) {
                    render();
                }

                observer.notifyObservers();
                try {
                    Thread.sleep(1000 / speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    private void setStartPos() {
        if (nextShape.getTetromino() != null) {
            shape.setTetromino(nextShape.getTetromino());
        } else {
            shape.setRandomTetromino();
        }
        nextShape.setRandomTetromino();
        xShapePos = (getFieldWidth() / 2);
        yShapePos = 0;
    }

    private void render() {
        if (endMove) {
            removeFullLines();
            endMove = false;
            setStartPos();
            setGameOver(checkGameEnd());
        } else {
            oneLineDown();
        }
    }

    private int removeFullLines() {
        int countRemoveLine = 0;
        for (int i = getFieldHeight() - 1; i >= 0; i--) {
            if (checkFullLine(i)) {
                countRemoveLine++;
                removeLine(i);
                i++;
            }
        }
        // вынести в рендер
        if (countRemoveLine > 0) {
            setScore(score + Scores.values()[countRemoveLine - 1].getScore());
            setRemovedLines(removedLines + countRemoveLine);
            if (removedLines == 20 * speed && speed <= 12) {
                setSpeed(speed+1);
            }
        }
        return countRemoveLine;
    }

    private boolean checkFullLine(int num) {
        for (int j = 0; j < getFieldWidth(); j++) {
            if (field[j][num] == null) {
                return false;
            }
        }
        return true;
    }

    public void setScore(int score) {
        this.score = score;

        observer.notifyObservers("score");
    }

    public void setRemovedLines(int removedLines) {
        this.removedLines = removedLines;

        observer.notifyObservers("removedLines");
    }

    private void removeLine(int numRemoveLine) {
        for (int i = numRemoveLine; i > 0; i--) {
            for (int j = 0; j < DEFAULT_FIELD_WIDTH; j++) {
                field[j][i] = field[j][i - 1];
            }
        }
//        setChanged();
//        notifyObservers("removeLine");
    }

    public void oneLineDown() {
        moveShape(0, 1);
    }

    public void oneLineLeft() {
        moveShape(-1, 0);
    }

    public void oneLineRight() {
        moveShape(1, 0);
    }

    public void dropDown() {
        while (!endMove) {
            oneLineDown();
        }
    }

    private void moveShape(int dx, int dy) {
        if (tryMove(shape, dx, dy)) {
            xShapePos += dx;
            yShapePos += dy;
        }
        observer.notifyObservers();
    }

    private boolean tryMove(Shape shape, int dx, int dy) {
        for (int i = 0; i < 4; i++) { // проходим по квадратам фигуры
            // вычисляем новые координаты квадрата на поле
            int newX = xShapePos + dx + shape.getX(i);
            int newY = yShapePos + dy + shape.getY(i);
            // проверяем не сдвигаемся ли фигура за края вправо и в влево
            if (newX < 0 || newX > getFieldWidth() - 1 /*|| newY < 0*/) {
                return false;
            }
            try {
                // проверяем не на дне ли фигура
                if (newY > getFieldHeight() - 1) {
                    endMove = true;
                    for (int j = 0; j < 4; j++) {
                        field[xShapePos + shape.getX(j)][yShapePos + shape.getY(j)] = shape.getTetromino();
                    }
                    return false;
                }

                if (field[newX][yShapePos + shape.getY(i)] != null) {
                    return false;
                }

                // проверим не наткнемся ли мы на лежащую фигуру
                if (field[xShapePos + shape.getX(i)][newY] != null) {
                    endMove = true;
                    for (int j = 0; j < 4; j++) {
                        field[xShapePos + shape.getX(j)][yShapePos + shape.getY(j)] = shape.getTetromino();
                    }
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException e){}
        }
        return true;
    }

    private boolean checkGameEnd() {
        for (int i = 0; i < 4; i++) {
            if (field[xShapePos + shape.getX(i)][yShapePos + shape.getY(i)] != null) {
                return true;
            }
        }
        return false;
    }

    public void rotate() {
        Shape newShape;
            newShape = shape.rotateLeft();
        if (tryMove(newShape, 0, 0)) {
            this.shape = newShape;
        }
        observer.notifyObservers();
    }

    public int getFieldWidth() {
        return field.length;
    }

    public int getFieldHeight() {
        return field[0].length;
    }

    public Tetromino getField(int i, int j) {
        return field[i][j];
    }

    public Shape getShape() {
        return shape;
    }

    public int getXShapePos() {
        return xShapePos;
    }

    public int getYShapePos() {
        return yShapePos;
    }

    public void setPause(boolean pause) {
        this.pause = pause;

        observer.notifyObservers("pause");
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public int getScore() {
        return score;
    }

    public int getRemovedLines() {
        return removedLines;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public void addObserver(Observer o) {
        observer.addObserver(o);
    }

    public void setSpeed(int speed) {
        this.speed = speed;

        observer.notifyObservers("speed");
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;

        observer.notifyObservers("gameOver");
    }

    private class TetrisObservable extends Observable {

        @Override
        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }
    }
}
