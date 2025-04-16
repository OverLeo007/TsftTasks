package ru.shift.model.listeners;

import ru.shift.model.events.BatchOfCellChangeEvent;

@FunctionalInterface
public interface BatchOfCellChangeListener {
    void onBatchOfCellChange(BatchOfCellChangeEvent evt);
}
