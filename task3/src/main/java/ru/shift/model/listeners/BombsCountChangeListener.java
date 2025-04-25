package ru.shift.model.listeners;

import ru.shift.model.events.BombsCountChangeEvent;

@FunctionalInterface
public interface BombsCountChangeListener {
    void onChangeBombsCount(BombsCountChangeEvent evt);
}
