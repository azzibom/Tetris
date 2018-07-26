package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.TetrisGame;
import by.azzibom.tetris.model.figure.Tetromino;

import javax.swing.*;
import java.awt.*;

/**
 * абстрактный класс панель с данными для игрового поля и поля следующей фигуры
 *
 * @author Ihar Misevich
 * @version 1.0
 */
abstract class TetrisGamePanel extends JPanel {

    int pointSize = 20;
    TetrisGame game;
    private DrawSquareStyleStrategy drawSquareStyleStrategy;

    TetrisGamePanel(TetrisGame game, DrawSquareStyleStrategy drawSquareStyleStrategy) {
        this.game = game;
        this.drawSquareStyleStrategy = drawSquareStyleStrategy;
    }

    void drawSquare(Graphics g, int x, int y, Tetromino shape) {
        // делегат... (делегируем обязанность)
        drawSquareStyleStrategy.drawSquare(g, x, y, pointSize, shape);
    }
}