package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.tetris.figure.Tetromino;

import java.awt.*;

/**
 * @author azzibom
 * */
public interface DrawSquareStyle {

    public void drawSquare(Graphics g, int x, int y, Tetromino shape);
}
