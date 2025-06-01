package com.rapizz.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandeObserver {
    private static CommandeObserver instance;
    private List<Runnable> observers;

    private CommandeObserver() {
        observers = new ArrayList<>();
    }

    public static CommandeObserver getInstance() {
        if (instance == null) {
            instance = new CommandeObserver();
        }
        return instance;
    }

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    public void removeObserver(Runnable observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }
} 