package ru.shift.view.listeners;

import ru.shift.model.GameDifficulty;

public interface VC_NewGameListener {

    void onGameTypeChanged(GameDifficulty gameDifficulty);

    void onNewGame();
}
