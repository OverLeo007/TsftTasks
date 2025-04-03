package ru.shift.model.cellEvent.listeners;

import ru.shift.model.cellEvent.CellState;

/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "MV" обозначает, что ивент вызывается в Model и ловится во View.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.controller.listeners.CM_CellEventListener}<br>
 * - {@link ru.shift.view.listeners.VC_CellEventListener}
 */
public interface MV_CellEventListener {

    void setCellState(int x, int y, CellState cellState);

}
