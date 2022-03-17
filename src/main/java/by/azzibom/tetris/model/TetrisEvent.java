package by.azzibom.tetris.model;

/**
 * @author Ihar MIsevich
 * */
public class TetrisEvent<T> {

    private final String name;
    private final T oldValue;
    private final T newValue;

    public TetrisEvent(String name, T oldValue, T newValue) {
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getName() {
        return name;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
