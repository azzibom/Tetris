package by.azzibom.observer;

import java.util.ArrayList;
import java.util.List;

public class ObservableImpl<T> implements Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    @Override
    public void notifyObservers(T arg) {
        for (Observer<T> o : observers) {
            o.update(arg);
        }
    }

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }
}
