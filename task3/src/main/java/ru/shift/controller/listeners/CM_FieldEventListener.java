package ru.shift.controller.listeners;


import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.view.listeners.VC_FieldEventListener;

/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "CM" обозначает, что ивент вызывается во Controller и ловится в Model.
 * Другие интерфейсы этого сценария:<br>
 * - {@link VC_FieldEventListener}<br>
 * - {@link MV_FiledEventListener}
 */
public interface CM_FieldEventListener {

    void onOpenCell(int x, int y);

    void onToggleFlag(int x, int y);

    void onOpenCellsAround(int x, int y);
}
