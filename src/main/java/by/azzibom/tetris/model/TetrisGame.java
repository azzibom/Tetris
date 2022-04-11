package by.azzibom.tetris.model;

import by.azzibom.tetris.model.figure.Shape;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс игры тетриса.
 *
 * @author Ihar Misevich
 */
public class TetrisGame {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // настройки по умолчанию
    private static final String DEFAULT_NAME = "Tetris";
    private static final int DEFAULT_FIELD_WIDTH = 10;
    private static final int DEFAULT_FIELD_HEIGHT = 20;
    private static final int DEFAULT_SPEED = 1;
    private boolean dropDown = false;

    public State getState() {
        return this.state;
    }

    private int score; // количество очков набранных игроком

    private int removedLines; // количество удаленных игроком линий
    private int speed; // скорость падения фигур (т е игрового цикла)

    // игровое поле (состоит из тетромин для того что бы знать квадрат какой фигуры находится в ячейке поля)
    private final Shape[][] field;

    private Shape shape; // текущая(падающая) фигура

    // координаты падающей фигуры
    private int xShapePos;
    private int yShapePos;
    private boolean endMove; // флаг конца хода

    private Shape nextShape; // следующая фигура

    private final String name; // имя игры

    private final Timer timer = new Timer("gameTimer", true);

    private State state = State.NEW;

    /**
     * конструктор инициализации игры
     *
     * @param name        - имя игры
     * @param fieldWidth  - ширина поля
     * @param fieldHeight - высота поля
     * @param speed       - начальная скорость падения фигур
     */
    public TetrisGame(String name, int fieldWidth, int fieldHeight, int speed) {
        this.name = name;
        field = new Shape[fieldWidth][fieldHeight];

        this.speed = speed; // скорость = 1
        score = 0; // очки = 0
        removedLines = 0; // удаленных линий = 0

        endMove = true; // конец хода = да
    }

    /**
     * конструктор с инициализацией по умолчанию
     */
    public TetrisGame() {
        this(
                DEFAULT_NAME,
                DEFAULT_FIELD_WIDTH,
                DEFAULT_FIELD_HEIGHT,
                DEFAULT_SPEED);
    }

    /**
     * метод запуска игры
     */
    public void start() {
        shape = new Shape();
        nextShape = new Shape();

        setPause(false); // не пауза

        state = State.GAME;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPause()) {
                    render();
                }
            }
        }, 0, 1000 / speed);
    }

    /**
     * метод установки стартовой позиции для текущей фигуры
     */
    private void setStartPos() {
        setShape(nextShape);
        setNextShape(new Shape());
        xShapePos = (getFieldWidth() / 2);
        yShapePos = 0;
    }

    private void setShape(Shape newShape) {
        Shape old = this.shape;
        this.shape = newShape;
        pcs.firePropertyChange("shape", old, newShape);
    }

    private void setNextShape(Shape newShape) {
        final Shape old = this.nextShape;
        this.nextShape = newShape;
        pcs.firePropertyChange("nextShape", old, newShape);
    }

    /**
     * метод определяющий логику хода
     */
    private void render() {
        if (endMove) {
            removeFullLines();
            endMove = false;
            setStartPos();

            if(checkGameEnd())
                setGameOver();

        } else {
            oneLineDown();
        }
    }

    /**
     * метод удаления всех заполненных строк
     */
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
                setSpeed(speed + 1);
            }
        }
        return countRemoveLine;
    }

    /**
     * метод проверки заполненности линии
     */
    private boolean checkFullLine(int num) {
        for (int j = 0; j < getFieldWidth(); j++) {
            if (field[j][num] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * метод установки очков
     */
    private void setScore(int score) {
        final int old = this.score;
        this.score = score;

        pcs.firePropertyChange("score", old, score);
    }


    /**
     * метод установки количества удаленных фигур
     */
    private void setRemovedLines(int removedLines) {
        final int old = this.removedLines;
        this.removedLines = removedLines;
        pcs.firePropertyChange("removedLines", old, removedLines);
    }

    /**
     * метод удаления линии
     */
    private void removeLine(int numRemoveLine) {
        for (int i = numRemoveLine; i > 0; i--) {
            for (int j = 0; j < DEFAULT_FIELD_WIDTH; j++) {
                field[j][i] = field[j][i - 1];
            }
        }
    }

    /**
     * передвижение фигуры на 1 линию вниз
     */
    public void oneLineDown() {
        moveShape(0, 1);
    }

    /**
     * передвижение фигуры на 1 линию влево
     */
    public void oneLineLeft() {
        moveShape(-1, 0);
    }

    /**
     * передвижение фигуры на 1 линию вправо
     */
    public void oneLineRight() {
        moveShape(1, 0);
    }

    /**
     * сброс фигуры на дно
     */
    public void dropDown() {
        final Point old = getShapePos();
        dropDown = true;
        while (!endMove) {
            oneLineDown();
        }
        dropDown = false;
        final Point newShapePos = getShapePos();
        pcs.firePropertyChange("dropDown", old, newShapePos);
    }

    /**
     * метод перемещения фигуры
     */
    private void moveShape(int dx, int dy) {
        final Point old = getShapePos();
        if (tryMove(shape, dx, dy)) {
            xShapePos += dx;
            yShapePos += dy;
        }
        if (!dropDown) {
            final Point newShapePos = getShapePos();
            pcs.firePropertyChange("moveShape", old, newShapePos);
        }
    }

    private Point getShapePos() {
        return new Point(xShapePos, yShapePos);
    }

    /**
     * метод проверки возможности хода
     */
    private boolean tryMove(Shape shape, int dx, int dy) {
        for (int i = 0; i < shape.getSize(); i++) { // проходим по квадратам фигуры
            // вычисляем новые координаты квадрата на поле
            final int newX = xShapePos + dx + shape.getX(i);
            final int newY = yShapePos + dy + shape.getY(i);
            // проверяем не сдвигаемся ли фигура за края вправо и влево
            if (newX < 0 || newX > getFieldWidth() - 1 /*|| newY < 0*/) {
                return false;
            }
            try {
                // проверяем не на дне ли фигура
                if (newY > getFieldHeight() - 1) {
                    endMove(shape);
                    return false;
                }

                if (field[newX][yShapePos + shape.getY(i)] != null) {
                    return false;
                }

                // проверим не наткнемся ли мы на лежащую фигуру
                if (field[xShapePos + shape.getX(i)][newY] != null) {
                    endMove(shape);
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return true;
    }


    private void endMove(Shape shape) {
        endMove = true;
        setShapeOnField(shape);
    }

    private void setShapeOnField(Shape shape) {
        for (int j = 0; j < shape.getSize(); j++) {
            field[xShapePos + shape.getX(j)][yShapePos + shape.getY(j)] = shape;
        }
    }

    private boolean getShapeOnField(Shape shape, int x, int y) {
        if (shape != null) {
            for (int i = 0; i < shape.getSize(); i++) {
                final int pointX = xShapePos + shape.getX(i);
                final int pointY = yShapePos + shape.getY(i);
                if (x == pointX && y == pointY) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * метод проверки конца игры
     */
    private boolean checkGameEnd() {
        for (int i = 0; i < 4; i++) {
            if (field[xShapePos + shape.getX(i)][yShapePos + shape.getY(i)] != null) {
                timer.cancel();
                return true;
            }
        }
        return false;
    }

    /**
     * метод поворота фигуры
     */
    public void rotate() {
        final Shape old = shape;
        final Shape newShape = new Shape(shape);
        newShape.rotateLeft();

        if (tryMove(newShape, 0, 0)) {
            this.shape = newShape;
        }
        pcs.firePropertyChange("rotate", old, this.shape);
    }

    /**
     * метод получения ширины игрового поля
     */
    public int getFieldWidth() {
        return field.length;
    }

    /**
     * метод получения высоты игрового поля
     */
    public int getFieldHeight() {
        return field[0].length;
    }

    /**
     * метод получения игрового поля
     */
    public Shape getField(int i, int j) {
        Shape shape = field[i][j];
        if (shape == null) {
            if (getShapeOnField(this.shape, i, j)) {
                shape = this.shape;
            }
        }
        return shape;
    }

    /**
     * метод получения текущей фигуры
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * метод получения координаты фигуры по оси Ox
     */
    public int getXShapePos() {
        return xShapePos;
    }

    /**
     * метод получения координаты фигуры по оси Oy
     */
    public int getYShapePos() {
        return yShapePos;
    }

    private void setState(State newState) {
        final State old = state;
        state = newState;
        pcs.firePropertyChange("state", old, newState);
    }

    /**
     * метод получения статуса паузы
     */
    public boolean isPause() {
        return state == State.PAUSED;
    }

    /**
     * метод установки паузы
     */
    public void setPause(boolean pause) {
        if (isGameOver()) {
            throw new IllegalStateException("game is over");
        }
        setState(pause ? State.PAUSED : State.GAME);
    }

    /**
     * метод получения статуса конца игры
     */
    public boolean isGameOver() {
        return state == State.GAME_OVER;
    }

    /**
     * метод установки конца игры
     */
    private void setGameOver() {
        setState(State.GAME_OVER);
    }

    /**
     * метод получения следующей фигуры
     */
    public Shape getNextShape() {
        return nextShape;
    }

    /**
     * метод получения набранных очков
     */
    public int getScore() {
        return score;
    }

    /**
     * метод получения количества удаленных линий
     */
    public int getRemovedLines() {
        return removedLines;
    }

    /**
     * метод получения скорости игры
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * метод получения имени игры
     */
    public String getName() {
        return name;
    }

    /**
     * метод установки скорости
     */
    public void setSpeed(int newSpeed) {
        final int old = this.speed;
        this.speed = newSpeed;
        pcs.firePropertyChange("speed", old, newSpeed);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
