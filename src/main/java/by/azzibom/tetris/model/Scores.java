package by.azzibom.tetris.model;

/**
 * перечисление очков начисляемых за удаление определенного количества линий
 * (подумать над изменением если вдркг будет 3-мино)
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public  enum Scores {

    DELETED_1_lINE(100),
    DELETED_2_lINES(400),
    DELETED_3_lINES(700),
    DELETED_4_lINES(1500);

    private int score;

    Scores(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}