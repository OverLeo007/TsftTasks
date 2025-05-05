package ru.shift.task6.commons.misc;

public interface TriConsumer <T, U, V> {
    void accept(T t, U u, V v);
}
