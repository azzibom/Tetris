package by.azzibom.tetris.model;

import by.azzibom.tetris.model.figure.Shape;
import by.azzibom.tetris.model.figure.Tetromino;

import java.util.Observable;
import java.util.Observer;

/**
 * Класс игры тетриса.
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public class TetrisGame {

    // настройки по умолчанию
    private static final String DEFAULT_NAME = "Tetris";
    private static final int DEFAULT_FIELD_WIDTH = 10;
    private static final int DEFAULT_FIELD_HEIGHT = 20;
    private static final int DEFAULT_SPEED = 1;

    /**
     * перечисление очков начисляемых за удаление определенного количества линий
     * (подумать над изменением если вдркг будет 3-мино)
     *
     * @author Ihar Misevich
     * @version 1.0
     */
    private enum Scores {
        DELETED_1_lINE(100),
        DELETED_2_lINES(400),
        DELETED_3_lINES(700),
        DELETED_4_lINES(1500);

        private int score;

        Scores(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }


    }

    private int score; // количество очков набранных игроком

    private int removedLines; // количество удаленных игроком линий
    private int speed; // скорость падения фигур (т е игрового цикла)

    // игровое поле (состоит из тетромин для того что бы знать квадрат какой фигуры находится в ячейке поля)
    private Tetromino[][] field;
    private Shape shape; // текущая(падающая) фигура

    // координаты падающей фигуры
    private int xShapePos;
    private int yShapePos;
    private boolean endMove; // флаг конца хода

    private boolean gameOver; // флаг конца игры
    private Shape nextShape; // следующая фигура

    private boolean pause; // флаг паузы
//    private enum Rotate {
//        LEFT, RIGHT;
//
//    }
//    private Rotate rotate;

    private String name; // имя игры
    private TetrisObservable observer = new TetrisObservable(); // объект издателя

    private Thread gameThread; // покок игры

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
     * метод запуса игры
     */
    public void start() {
        setGameOver(false); // не конец игры
        setPause(false); // не пауза

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

    /**
     * метод установки стартовой позиции для текущей фигуры
     */
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

    /**
     * метод определяющий логику хода
     */
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

    /**
     * метод удаления всех заполненых строк
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
     * метод проверки заполнености линии
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
    public void setScore(int score) {
        this.score = score;

        observer.notifyObservers("score");
    }

    /**
     * метод установки количества удаленных фигур
     */
    public void setRemovedLines(int removedLines) {
        this.removedLines = removedLines;

        observer.notifyObservers("removedLines");
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
//        setChanged();
//        notifyObservers("removeLine");
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
     * сбрс фигуры на дно
     */
    public void dropDown() {
        while (!endMove) {
            oneLineDown();
        }
    }

    /**
     * метод перемещения фигуры
     */
    private void moveShape(int dx, int dy) {
        if (tryMove(shape, dx, dy)) {
            xShapePos += dx;
            yShapePos += dy;
        }
        observer.notifyObservers();
    }

    /**
     * метод проверки возможности хода
     */
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
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return true;
    }

    /**
     * метод проверки конца игры
     */
    private boolean checkGameEnd() {
        for (int i = 0; i < 4; i++) {
            if (field[xShapePos + shape.getX(i)][yShapePos + shape.getY(i)] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * метод поорота фигуры
     */
    public void rotate() {
        Shape newShape;
        newShape = shape.rotateLeft();
        if (tryMove(newShape, 0, 0)) {
            this.shape = newShape;
        }
        observer.notifyObservers();
    }

    /**
     * метод полчения ширины игрового поля
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
    public Tetromino getField(int i, int j) {
        return field[i][j];
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

    /**
     * метод установки паузы
     */
    public void setPause(boolean pause) {
        this.pause = pause;

        observer.notifyObservers("pause");
    }

    /**
     * метод получения статуса паузы
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * метод полученя статуса конца игры
     */
    public boolean isGameOver() {
        return gameOver;
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
     * метод получния скорости игры
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
     * добавление слушателей
     */
    public void addObserver(Observer o) {
        observer.addObserver(o);
    }

    /**
     * метод установки скорости
     */
    public void setSpeed(int speed) {
        this.speed = speed;

        observer.notifyObservers("speed");
    }

    /**
     * метод установки конца игры
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;

        observer.notifyObservers("gameOver");
    }

    /**
     * внутрений класс издателя для игры
     *
     * @author Ihar Misevich
     * @version 1.0
     */
    private class TetrisObservable extends Observable {

        @Override
        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }
    }
}
