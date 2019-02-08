package com.example.maxpayne.mytodoapp.db;

import java.util.ArrayList;

public interface DbObservable {
    ArrayList<DbObserver> observers = new ArrayList<>();

    boolean addObserver(DbObserver observer);
    boolean removeObserver(DbObserver observer);
    void notifyObservers();
}
