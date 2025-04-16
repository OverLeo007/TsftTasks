package ru.shift.model.listeners;

import ru.shift.model.events.CellChangeEvent;

@FunctionalInterface
public interface CellChangeListener {
    void onChangeCellState(CellChangeEvent event);
}
