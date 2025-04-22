package ru.shift.app.bus;

import java.util.ArrayList;
import java.util.Collections;
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

    private record ListenerWrapper<T extends Event>(EventListener<T> listener, int priority) {

    }

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        subscribe(eventType, listener, DEFAULT_PRIORITY);
    }

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener,
            int priority) {
        List<ListenerWrapper<? extends Event>> wrappers =
                listeners.computeIfAbsent(eventType, k -> new ArrayList<>());

        ListenerWrapper<T> newWrapper = new ListenerWrapper<>(listener, priority);
        int idx = Collections.binarySearch(wrappers, newWrapper,
                (a, b) -> Integer.compare(b.priority, a.priority));
        if (idx >= 0) {
            wrappers.add(idx, newWrapper);
        } else {
            wrappers.add((-idx) - 1, newWrapper);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void emit(T event) {
        var wrappers = listeners.get(event.getClass());
        if (wrappers == null) {
            return;
        }
        wrappers.forEach(wrapper -> ((EventListener<T>) wrapper.listener).onEvent(event));
    }

}

