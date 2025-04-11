package ru.shift.view.listeners;

import ru.shift.view.ButtonType;

/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "VC" обозначает, что ивент вызывается во View и ловится в Controller.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_FieldEventListener}<br>
 * - {@link ru.shift.model.listeners.MV_FiledEventListener}
 */
public interface VC_FieldEventListener {

    void onMouseClick(int x, int y, ButtonType buttonType);
    void onHackStateChanged(boolean isHack);

}
