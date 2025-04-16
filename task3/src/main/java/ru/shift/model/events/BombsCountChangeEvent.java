package ru.shift.model.events;

import ru.shift.app.bus.base.Event;


public record BombsCountChangeEvent(int count) implements Event {}
