package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.figure.Tetromino;

import java.awt.*;

/**
 * @author azzibom
 * */
public interface DrawSquareStyle {

    void drawSquare(Graphics g, int x, int y, Tetromino shape);
}
