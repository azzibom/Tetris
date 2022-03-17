package by.azzibom.tetris.model;

/**
 * перечисление очков начисляемых за удаление определенного количества линий
 * (подумать над изменением если вдруг будет 3-мино)
 *
 * @author Ihar Misevich
 */
public  enum Scores {

    DELETED_1_lINE(100),
    DELETED_2_lINES(400),
    DELETED_3_lINES(700),
    DELETED_4_lINES(1500);

    private final int score;

    Scores(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}