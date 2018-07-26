package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.figure.Tetromino;

import java.awt.*;

/**
 * Определяет интерфейс стратегий для отрисовки фигуры
 * в параметрах объек графики, координаты, фигура
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public interface DrawSquareStyleStrategy {

    /**
     * Метод отрисоки фигуры тетриса на игровом поле
     *
     * @param g     - объект графики.
     * @param x     - координа по Ox.
     * @param y     - координата по Oy.
     * @param shape - фигура для отрисовки.
     */
    void drawSquare(Graphics g, int x, int y, int pointSize, Tetromino shape);
}
