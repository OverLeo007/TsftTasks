package ru.shift.controller.listeners;

import ru.shift.model.GameDifficulty;

public interface CM_NewGameListener {
    void onGameTypeSelected(GameDifficulty gameDifficulty);
    void onNewGame();
}
