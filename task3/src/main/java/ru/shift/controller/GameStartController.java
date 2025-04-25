package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.api.NewGameInitializer;
import ru.shift.app.GameDifficulty;

@RequiredArgsConstructor
@Slf4j
public class GameStartController {

    private final NewGameInitializer newGameInitializer;

    public void startNewGameWithDifficulty(GameDifficulty gameDifficulty) {
        log.debug("Game type changed to: {}", gameDifficulty);
        newGameInitializer.initNewGameWithDifficulty(gameDifficulty);
    }

    public void startNewGame() {
        log.debug("New game started");
        newGameInitializer.initNewGame();
    }
}
