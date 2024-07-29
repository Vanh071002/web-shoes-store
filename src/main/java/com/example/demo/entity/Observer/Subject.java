package com.example.demo.entity.Observer;


public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}
