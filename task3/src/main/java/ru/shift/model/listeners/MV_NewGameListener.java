package ru.shift.model.listeners;

import ru.shift.model.GameDifficulty;

public interface MV_NewGameListener {
    void onGameTypeSelected(GameDifficulty gameDifficulty);

}
