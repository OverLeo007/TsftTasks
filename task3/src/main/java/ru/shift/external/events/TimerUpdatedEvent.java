package ru.shift.external.events;

import ru.shift.app.bus.base.Event;

public record TimerUpdatedEvent(int timerValue) implements Event {}
