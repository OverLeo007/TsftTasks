package ru.shift.app.bus.api;

import ru.shift.app.bus.base.Event;
import ru.shift.app.bus.base.EventListener;

public interface EventSubscriber {
    <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener);
    <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener, int priority);
}
