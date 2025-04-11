package ru.shift.model.listeners;

import ru.shift.model.GameDifficulty;

/**
 * Интерфейс сценария инициализации новой игры.
 * Префиксы "MV" обозначает, что ивент вызывается во Model и ловится во View.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_NewGameListener}<br>
 * - {@link ru.shift.view.listeners.VC_NewGameListener}
 */
public interface MV_NewGameListener {
    void onGameTypeSelected(GameDifficulty gameDifficulty);
}
