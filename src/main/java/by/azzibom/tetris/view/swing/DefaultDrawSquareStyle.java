package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.figure.Shape;

import java.awt.*;

public class DefaultDrawSquareStyle implements DrawSquareStyle {

    @Override
    public void drawSquare(Graphics g, int x, int y, Shape shape, int pointSize) {
        int a = 2, b = 3;
        if (shape != null)
            g.setColor(Color.GRAY);
        else
            g.setColor(new Color(209, 209, 209));

        g.drawRect((x * pointSize) + a, (y * pointSize) + a, pointSize - b, pointSize - b);
        g.fillRect((x * pointSize) + (2 * a), (y * pointSize) + (2 * a), pointSize - (2 * b), pointSize - (2 * b));
    }

}
