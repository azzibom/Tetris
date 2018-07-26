package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.controller.GameKeyController;
import by.azzibom.tetris.model.TetrisGame;
import by.azzibom.tetris.model.figure.Shape;

import java.awt.*;

/**
 * панель игрового поля
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public class TertisGameField extends TetrisGamePanel {

    TertisGameField(TetrisGame game, DrawSquareStyleStrategy drawSquareStyleStrategy) {
        super(game, drawSquareStyleStrategy);

        Dimension dimension = new Dimension((game.getFieldWidth() + 4) * pointSize, game.getFieldHeight() * pointSize);
        super.setPreferredSize(dimension);// если указать это поле то размер окна будет нужным прииспользовании функции pack

        super.setFocusable(true);

        super.addKeyListener(new GameKeyController(game));
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);
        for (int i = 0; i < game.getFieldWidth(); i++) {
            for (int j = 0; j < game.getFieldHeight(); j++) {
                game.getField(i, j);
                if (game.getField(i, j) != null) {
                    drawSquare(g, i, j, game.getField(i, j));
                } else {
                    drawSquare(g, i, j, null);
                }
            }
        }
        // рисуем падающую фигуру
        int xShapePos = game.getXShapePos();
        int yShapePos = game.getYShapePos();
        Shape shape = game.getShape();
        for (int i = 0; i < 4; i++) {
            int x = (xShapePos + shape.getX(i));
            int y = (yShapePos + shape.getY(i));
            drawSquare(g, x, y, shape.getTetromino());
        }
    }
}
