package ru.shift.model.events;

import ru.shift.app.bus.base.Event;
import ru.shift.model.field.CellState;

public record CellChangeEvent(int x, int y, CellState cellState) implements Event {}
