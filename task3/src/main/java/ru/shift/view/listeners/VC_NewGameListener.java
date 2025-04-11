package ru.shift.view.listeners;

import ru.shift.model.GameDifficulty;

/**
 * Интерфейс сценария инициализации новой игры.
 * Префикс "VC" обозначает, что ивент вызывается во View и ловится в Controller.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_NewGameListener}<br>
 * - {@link ru.shift.model.listeners.MV_NewGameListener}
 */
public interface VC_NewGameListener {

    void onGameTypeChanged(GameDifficulty gameDifficulty);

    void onNewGame();
}
