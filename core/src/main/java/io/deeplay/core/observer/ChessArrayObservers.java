package io.deeplay.core.observer;


import java.util.ArrayList;
import java.util.List;

// TODO: во всех реализациях просто пробегает по каждому слушателю
public class ChessArrayObservers implements ChessObserver {
    private final List<ChessObserver> observers = new ArrayList<>();

    public ChessObserver getObserverAtIndex(int index) {
        return observers.get(index);
    }

    public void addObserver(ChessObserver observer) {
        this.observers.add(observer);
    }
}
