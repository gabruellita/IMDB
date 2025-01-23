package org.example;

import java.io.IOException;

public interface Subject {
    public void addObserver(User user);
    public void removeObserver(User user);
    void notifyObservers(Object object) throws IOException;
}
