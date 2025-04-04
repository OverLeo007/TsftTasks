package ru.shift.model.listeners;

import java.util.List;
import ru.shift.controller.listeners.CM_FieldEventListener;
import ru.shift.model.field.CellState;
import ru.shift.model.field.CellStateChange;
import ru.shift.view.GameType;
import ru.shift.view.listeners.VC_FieldEventListener;

/**
 * Интерфейс сценария клика по клетке поля.
 * Префикс "MV" обозначает, что ивент вызывается в Model и ловится во View.
 * Другие интерфейсы этого сценария:<br>
 * - {@link CM_FieldEventListener}<br>
 * - {@link VC_FieldEventListener}
 */
public interface MV_FiledEventListener {

    void onChangeCellState(int x, int y, CellState cellState);

    void onFieldSetup(GameType gameType);

    void onChangeBombsCount(int count);

    void onBatchChangeCellState(List<CellStateChange> changes);
}
