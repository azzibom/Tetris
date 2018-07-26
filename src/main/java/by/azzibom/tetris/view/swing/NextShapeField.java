package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.TetrisGame;
import by.azzibom.tetris.model.figure.Shape;

import java.awt.*;

/**
 * панель для отображения следующей фигуры
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public class NextShapeField extends TetrisGamePanel {

    private int countPoints = 4;//

    NextShapeField(TetrisGame game, DrawSquareStyleStrategy drawSquareStyleStrategy) {
        super(game, drawSquareStyleStrategy);
        super.setPreferredSize(new Dimension(countPoints * pointSize, countPoints * pointSize));
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);
        for (int i = 0; i < countPoints; i++) {
            for (int j = 0; j < countPoints; j++) {
                drawSquare(g, i, j, null);
            }
        }
        Shape nextShape = game.getNextShape();
        for (int i = 0; i < 4; i++) {
            int x = ((countPoints / 2) + nextShape.getX(i));
            int y = ((countPoints / 2) - 1 + nextShape.getY(i));
            drawSquare(g, x, y, nextShape.getTetromino());
        }
    }
}
