package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_NewGameListener;
import ru.shift.model.GameDifficulty;
import ru.shift.view.listeners.VC_NewGameListener;

@RequiredArgsConstructor
@Slf4j
public class NewGameController implements VC_NewGameListener {

    private final CM_NewGameListener newGameListener;

    @Override
    public void onGameTypeChanged(GameDifficulty gameDifficulty) {
        log.debug("Game type changed to: {}", gameDifficulty);
        newGameListener.onGameTypeSelected(gameDifficulty);
    }

    @Override
    public void onNewGame() {
        log.debug("New game started");
        newGameListener.onNewGame();
    }
}
