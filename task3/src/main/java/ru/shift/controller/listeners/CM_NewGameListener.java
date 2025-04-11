package ru.shift.controller.listeners;

import ru.shift.model.GameDifficulty;

/**
 * Интерфейс сценария инициализации новой игры.
 * Префикс "CM" обозначает, что ивент вызывается во Controller и ловится в Model.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.view.listeners.VC_NewGameListener}<br>
 * - {@link ru.shift.model.listeners.MV_NewGameListener}
 */
public interface CM_NewGameListener {
    void onGameTypeSelected(GameDifficulty gameDifficulty);
    void onNewGame();
}
