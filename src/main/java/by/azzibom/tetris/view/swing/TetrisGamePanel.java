package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.State;
import by.azzibom.tetris.model.TetrisGame;
import by.azzibom.tetris.model.figure.Shape;

import javax.swing.*;
import java.awt.*;

/**
 * абстрактный класс панель с данными для игрового поля и поля следующей фигуры
 *
 * @author Ihar Misevich
 * @version 1.0
 */
abstract class TetrisGamePanel extends JPanel {

    int pointSize;
    TetrisGame game;
    private DrawSquareStyle drawSquareStyleStrategy;

    TetrisGamePanel(TetrisGame game, DrawSquareStyle drawSquareStyleStrategy, int pointSize) {
        this.game = game;
        this.drawSquareStyleStrategy = drawSquareStyleStrategy;
        this.pointSize = pointSize;
    }

    void drawSquare(Graphics g, int x, int y, Shape shape) {
        // делегат... (делегируем обязанность)
        drawSquareStyleStrategy.drawSquare(g, x, y, shape, pointSize);
    }

    void drawShape(Graphics g, int xShapePos, int yShapePos, Shape shape) {
        if (game.getState() != State.NEW && !game.isGameOver()) {

            for (int i = 0; i < shape.getSize(); i++) {
                int x = xShapePos + shape.getX(i);
                int y = yShapePos + shape.getY(i);
                drawSquare(g, x, y, shape);
            }
        }
    }
}