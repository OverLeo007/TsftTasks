package ru.shift.model.api;

import ru.shift.app.GameDifficulty;

public interface NewGameInitializer {
    void initNewGameWithDifficulty(GameDifficulty gameDifficulty);
    void initNewGame();
}
