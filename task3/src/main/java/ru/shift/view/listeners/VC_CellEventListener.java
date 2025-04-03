package ru.shift.view.listeners;

import ru.shift.model.cellEvent.listeners.MV_CellEventListener;
import ru.shift.view.ButtonType;

/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "VC" обозначает, что ивент вызывается во View и ловится в Controller.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_CellEventListener}<br>
 * - {@link MV_CellEventListener}
 */
public interface VC_CellEventListener {

    void onMouseClick(int x, int y, ButtonType buttonType);
}
