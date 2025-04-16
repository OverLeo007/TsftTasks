package ru.shift.external.listeners;

import ru.shift.external.events.TimerUpdatedEvent;

public interface TimerUpdateListener {
    void onTimeUpdated(TimerUpdatedEvent event);
}
