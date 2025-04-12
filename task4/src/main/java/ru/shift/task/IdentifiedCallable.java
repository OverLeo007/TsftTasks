package ru.shift.task;

import java.util.concurrent.Callable;

public interface IdentifiedCallable<T> extends Callable<T> {
    int getId();
}
