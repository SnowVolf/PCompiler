package ru.SnowVolf.girl.reactive;

import java.util.Observable;

/**
 * Created by Snow Volf on 24.09.2017, 0:38
 */

public class SimpleObservable extends Observable {
    @Override
    public synchronized boolean hasChanged() {
        return true;
    }
}
