package ru.shift.view.observers;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.shift.controller.ScoreRecordController;
import ru.shift.external.scores.Score;
import ru.shift.model.events.NewHighScoreEvent;
import ru.shift.model.listeners.NewHighScoreListener;
import ru.shift.view.windows.MainWindow;
import ru.shift.view.windows.RecordsWindow;

@RequiredArgsConstructor
public class RecordWindowObserver implements NewHighScoreListener {

    private final MainWindow mainWindow;

    @Setter
    private ScoreRecordController scoreRecordListener;


    @Override
    public void onNewHighScore(NewHighScoreEvent event) {
        Score oldScore = event.oldScore();
        if (oldScore == null) {
            new RecordsWindow(mainWindow, scoreRecordListener, event.newTime());
        } else {
            new RecordsWindow(mainWindow, scoreRecordListener,
                    oldScore.name(), oldScore.time(), event.newTime());
        }
    }

}
