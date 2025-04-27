package ru.shift.model.listeners;

import ru.shift.model.events.GameStateChangeEvent;

public interface ChangeGameStateListener {
    void onChangeState(GameStateChangeEvent event);
}
