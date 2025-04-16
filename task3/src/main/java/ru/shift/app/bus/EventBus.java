package ru.shift.app.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.app.bus.api.EventSubscriber;
import ru.shift.app.bus.base.Event;
import ru.shift.app.bus.base.EventListener;

public class EventBus implements EventEmitter, EventSubscriber {

    private static final int DEFAULT_PRIORITY = 0;

    private final Map<Class<? extends Event>, List<ListenerWrapper<? extends Event>>> listeners = new HashMap<>();

    private static final EventBus instance = new EventBus();

    private EventBus() {
    }

    public static EventEmitter getEventEmitter() {
        return instance;
    }

    public static EventSubscriber getEventSubscriber() {
        return instance;
    }

    private record ListenerWrapper<T extends Event>(EventListener<T> listener, int priority) { }

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        subscribe(eventType, listener, DEFAULT_PRIORITY);
    }

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener,
            int priority) {
        listeners
                .computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(new ListenerWrapper<>(listener, priority));
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void emit(T event) {
        List<ListenerWrapper<? extends Event>> wrappers = listeners.get(event.getClass());

        if (wrappers != null) {
            List<ListenerWrapper<? extends Event>> sorted = new ArrayList<>(wrappers);
            sorted.sort((a, b) -> Integer.compare(b.priority, a.priority));

            for (ListenerWrapper<? extends Event> wrapper : sorted) {
                ((EventListener<T>) wrapper.listener).onEvent(event);
            }
        }
    }
}

