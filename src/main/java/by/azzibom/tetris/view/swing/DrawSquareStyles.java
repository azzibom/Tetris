package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.tetris.figure.Tetromino;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * @author azzibom
 * */
public enum DrawSquareStyles implements DrawSquareStyle {

    DEFAULT_STYLE {
        @Override
        public void drawSquare(Graphics g, int x, int y, Tetromino shape) {
            int a = 2, b = 3;
            if (shape != null)
                g.setColor(Color.GRAY);
            else
                g.setColor(new Color(222, 222, 222));
            g.drawRect((x * pointSize) + a, (y * pointSize) + a, pointSize - b, pointSize - b);
            g.fillRect((x * pointSize) + (2 * a), (y * pointSize) + (2 * a), pointSize - (2 * b), pointSize - (2 * b));
        }
    },
    COLOR_STYLE {

        private HashMap<Tetromino, Color> shapeColorMap = new HashMap<>();
        {
            // клетка без фигуры белая
            shapeColorMap.put(null, Color.WHITE);
        }
        Random r = new Random();

        @Override
        public void drawSquare(Graphics g, int x, int y, Tetromino shape) {
            if (!shapeColorMap.containsKey(shape)) {
                Color color = null;
                do {
                    color = new Color(Math.abs(r.nextInt()) / shape.getSize() * shape.ordinal());
                } while (shapeColorMap.containsValue(color));

                shapeColorMap.put(shape, color);
            }
//            System.out.println(shapeColorMap.get(shape).getRGB());
            g.setColor(shapeColorMap.get(shape));
            g.fillRect((x * pointSize) + 1, (y * pointSize) + 1, pointSize - 1, pointSize - 1);
        }
    };

    int pointSize = 20;
}
