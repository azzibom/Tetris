package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.figure.Shape;

import java.awt.*;

/**
 * @author azzibom
 */
public interface DrawSquareStyle {

    void drawSquare(Graphics g, int x, int y, Shape shape);
}
