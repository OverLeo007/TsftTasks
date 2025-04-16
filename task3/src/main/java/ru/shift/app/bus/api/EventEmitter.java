package ru.shift.app.bus.api;

import ru.shift.app.bus.base.Event;

public interface EventEmitter {
     <T extends Event> void emit(T event);
}
