package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_GameTypeListener;
import ru.shift.model.GameType;
import ru.shift.view.listeners.VC_GameTypeListener;

@RequiredArgsConstructor
@Slf4j
public class SettingsController implements VC_GameTypeListener {

    private final CM_GameTypeListener gameTypeListener;

    @Override
    public void onGameTypeChanged(GameType gameType) {
        log.debug("Game type changed to: {}", gameType);
        gameTypeListener.onGameTypeSelected(gameType);
    }
}
