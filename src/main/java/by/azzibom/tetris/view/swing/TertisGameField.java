package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.controller.GameKeyController;
import by.azzibom.tetris.model.TetrisGame;

import java.awt.*;

/**
 * панель игрового поля
 *
 * @author Ihar Misevich
 */
public class TertisGameField extends TetrisGamePanel {

    TertisGameField(TetrisGame game, DrawSquareStyle drawSquareStyleStrategy) {
        super(game, drawSquareStyleStrategy, 20);

        Dimension dimension = new Dimension((game.getFieldWidth() + 4) * pointSize, game.getFieldHeight() * pointSize);
        super.setPreferredSize(dimension);// если указать это поле, то размер окна будет нужным при использовании функции pack

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
//        // рисуем падающую фигуру
//        Shape shape = game.getShape();
//
//        int xShapePos = game.getXShapePos();
//        int yShapePos = game.getYShapePos();
//
//        drawShape(g, xShapePos, yShapePos, shape);
    }
}
