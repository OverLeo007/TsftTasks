package ru.shift.app.bus.base;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
