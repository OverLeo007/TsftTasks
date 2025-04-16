package ru.shift.view;

import lombok.Getter;
import ru.shift.app.bus.EventBus;
import ru.shift.external.events.ScoreUpdatedEvent;
import ru.shift.external.events.ScoresLoadedEvent;
import ru.shift.external.events.TimerUpdatedEvent;
import ru.shift.model.events.BatchOfCellChangeEvent;
import ru.shift.model.events.BombsCountChangeEvent;
import ru.shift.model.events.CellChangeEvent;
import ru.shift.model.events.FieldSetupEvent;
import ru.shift.model.events.GameStateChangeEvent;
import ru.shift.model.events.NewGameDifficultyEvent;
import ru.shift.model.events.NewHighScoreEvent;
import ru.shift.view.observers.HighScoresWindowObserver;
import ru.shift.view.observers.LoseWindowObserver;
import ru.shift.view.observers.MainWindowObserver;
import ru.shift.view.observers.RecordWindowObserver;
import ru.shift.view.observers.WinWindowObserver;
import ru.shift.view.windows.HighScoresWindow;
import ru.shift.view.windows.LoseWindow;
import ru.shift.view.windows.MainWindow;
import ru.shift.view.windows.SettingsWindow;
import ru.shift.view.windows.WinWindow;

public class GameView {

    @Getter
    private final MainWindow mainWindow = new MainWindow();
    private final MainWindowObserver mainWindowObserver = new MainWindowObserver(mainWindow);
    @Getter
    private final WinWindow winWindow = new WinWindow(mainWindow);
    private final WinWindowObserver winWindowObserver = new WinWindowObserver(winWindow);
    @Getter
    private final LoseWindow loseWindow = new LoseWindow(mainWindow);
    private final LoseWindowObserver loseWindowObserver = new LoseWindowObserver(loseWindow);
    @Getter
    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private final HighScoresWindowObserver highScoresWindowObserver =
            new HighScoresWindowObserver(highScoresWindow);
    @Getter
    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

    @Getter
    private final RecordWindowObserver recordWindowObserver = new RecordWindowObserver(mainWindow);

    {
        var eventSubscriber = EventBus.getEventSubscriber();
        eventSubscriber.subscribe(BatchOfCellChangeEvent.class,
                mainWindowObserver::onBatchOfCellChange);

        eventSubscriber.subscribe(CellChangeEvent.class,
                mainWindowObserver::onChangeCellState);

        eventSubscriber.subscribe(FieldSetupEvent.class,
                mainWindowObserver::onFieldSetup);

        eventSubscriber.subscribe(BombsCountChangeEvent.class,
                mainWindowObserver::onChangeBombsCount);

        eventSubscriber.subscribe(NewGameDifficultyEvent.class,
                settingsWindow::onNewGameDifficulty);

        eventSubscriber.subscribe(NewHighScoreEvent.class,
                recordWindowObserver::onNewHighScore);

        eventSubscriber.subscribe(ScoresLoadedEvent.class,
                highScoresWindowObserver::onScoresLoaded);

        eventSubscriber.subscribe(ScoreUpdatedEvent.class,
                highScoresWindowObserver::onScoreUpdated);

        eventSubscriber.subscribe(TimerUpdatedEvent.class,
                mainWindowObserver::onTimeUpdated);

        eventSubscriber.subscribe(GameStateChangeEvent.class,
                loseWindowObserver::onChangeState, 0);

        eventSubscriber.subscribe(GameStateChangeEvent.class,
                winWindowObserver::onChangeState, 0);
    }
}
