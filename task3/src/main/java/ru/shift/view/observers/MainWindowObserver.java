package ru.shift.view.observers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.field.CellState;
import ru.shift.model.field.CellStateChange;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.view.GameImage;
import ru.shift.model.GameType;
import ru.shift.view.windows.MainWindow;

// TODO: Заменить наследование на композицию
@Slf4j
public class MainWindowObserver extends MainWindow implements MV_FiledEventListener {

    @Override
    public void onChangeCellState(int x, int y, CellState cellState) {
        cellButtons[y][x].setIcon(GameImage.fromCellState(cellState).getImageIcon());
    }

    @Override
    public void onFieldSetup(GameType gameType) {
        log.debug("Create game field with {} rows and {} cols", gameType.rows, gameType.cols);
        createGameField(gameType.rows, gameType.cols);
        setBombsCount(gameType.bombsN);
    }

    @Override
    public void onChangeBombsCount(int count) {
        setBombsCount(count);
    }

    @Override
    public void onBatchChangeCellState(List<CellStateChange> changes) {
        for (CellStateChange change : changes) {
            onChangeCellState(change.x(), change.y(), change.cellState());
        }
    }
}
