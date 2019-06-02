package by.azzibom.tetris.model;

public class TetrisEvent<T> {

    private String name;
    private T oldValue;
    private T newValue;

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
