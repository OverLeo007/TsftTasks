package ru.shift.model.events;

import java.util.List;
import ru.shift.app.bus.base.Event;
import ru.shift.model.field.CellStateChange;

public record BatchOfCellChangeEvent(List<CellStateChange> changes) implements Event {}
