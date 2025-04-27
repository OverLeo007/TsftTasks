package ru.shift.model.listeners;

import ru.shift.model.events.BombsGeneratedEvent;

public interface BombsGeneratedListener {
    void onBombsGenerated(BombsGeneratedEvent evt);
}
