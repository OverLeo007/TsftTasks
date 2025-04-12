package ru.shift;

import java.util.concurrent.Callable;

public interface IdentifiedCallable<T> extends Callable<T> {
    int getId();
}
