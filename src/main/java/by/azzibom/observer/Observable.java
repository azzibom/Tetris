package by.azzibom.observer;

public interface Observable<T> {

    void notifyObservers(T arg);

    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);
}
