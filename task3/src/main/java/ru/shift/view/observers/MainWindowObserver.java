package ru.shift.view.observers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.GameDifficulty;
import ru.shift.model.events.BatchOfCellChangeEvent;
import ru.shift.model.events.BombsCountChangeEvent;
import ru.shift.model.events.CellChangeEvent;
import ru.shift.model.events.FieldSetupEvent;
import ru.shift.external.events.TimerUpdatedEvent;
import ru.shift.model.field.CellStateChange;
import ru.shift.external.listeners.TimerUpdateListener;
import ru.shift.model.listeners.BatchOfCellChangeListener;
import ru.shift.model.listeners.BombsCountChangeListener;
import ru.shift.model.listeners.CellChangeListener;
import ru.shift.model.listeners.FieldSetupListener;
import ru.shift.view.GameImage;
import ru.shift.view.windows.MainWindow;

@RequiredArgsConstructor
@Slf4j
public class MainWindowObserver implements
        BatchOfCellChangeListener,
        CellChangeListener,
        BombsCountChangeListener,
        FieldSetupListener,
        TimerUpdateListener {

    private final MainWindow window;

    @Override
    public void onChangeCellState(CellChangeEvent evt) {
        window.setCellImage(evt.x(), evt.y(), GameImage.fromCellState(evt.cellState()));
    }

    @Override
    public void onFieldSetup(FieldSetupEvent evt) {
        GameDifficulty gameDifficulty = evt.gameDifficulty();
        log.debug("Create game field with {} rows and {} cols", gameDifficulty.rows,
                gameDifficulty.cols);
        window.createGameField(gameDifficulty.rows, gameDifficulty.cols);
        window.setBombsCount(gameDifficulty.bombsN);
    }

    @Override
    public void onChangeBombsCount(BombsCountChangeEvent evt) {
        window.setBombsCount(evt.count());
    }

    @Override
    public void onBatchOfCellChange(BatchOfCellChangeEvent evt) {
        for (CellStateChange change : evt.changes()) {
            window.setCellImage(change.x(), change.y(), GameImage.fromCellState(change.cellState()));
        }
    }

    @Override
    public void onTimeUpdated(TimerUpdatedEvent event) {
        window.setTimerValue(event.timerValue());
    }
}
