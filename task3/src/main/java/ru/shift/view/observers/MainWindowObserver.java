package ru.shift.view.observers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.field.CellState;
import ru.shift.model.field.CellStateChange;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.model.listeners.MV_TimerListener;
import ru.shift.view.GameImage;
import ru.shift.model.GameDifficulty;
import ru.shift.view.windows.MainWindow;

@RequiredArgsConstructor
@Slf4j
public class MainWindowObserver implements MV_FiledEventListener,
        MV_TimerListener {

    private final MainWindow window;

    @Override
    public void onChangeCellState(int x, int y, CellState cellState) {
        window.setCellImage(x, y, GameImage.fromCellState(cellState));
    }

    @Override
    public void onFieldSetup(GameDifficulty gameDifficulty) {
        log.debug("Create game field with {} rows and {} cols", gameDifficulty.rows, gameDifficulty.cols);
        window.createGameField(gameDifficulty.rows, gameDifficulty.cols);
        window.setBombsCount(gameDifficulty.bombsN);
    }

    @Override
    public void onChangeBombsCount(int count) {
        window.setBombsCount(count);
    }

    @Override
    public void onBatchChangeCellState(List<CellStateChange> changes) {
        for (CellStateChange change : changes) {
            onChangeCellState(change.x(), change.y(), change.cellState());
        }
    }

    @Override
    public void onTimeUpdated(int timerValue) {
        window.setTimerValue(timerValue);
    }

}
