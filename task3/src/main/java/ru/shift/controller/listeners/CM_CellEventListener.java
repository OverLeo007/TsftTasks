package ru.shift.controller.listeners;


/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "CM" обозначает, что ивент вызывается во Controller и ловится в Model.
 * Другие интерфейсы этого сценария:<br>
 * - {@link ru.shift.view.listeners.VC_CellEventListener}<br>
 * - {@link ru.shift.model.cellEvent.listeners.MV_CellEventListener}
 */
public interface CM_CellEventListener {

    void openCell(int x, int y);

    void toggleFlag(int x, int y);

    void openCellsAround(int x, int y);
}
