package ru.shift.view.observers;

import lombok.RequiredArgsConstructor;
import ru.shift.model.listeners.MV_OnNewRecordListener;
import ru.shift.model.scores.Score;
import ru.shift.view.listeners.VC_ScoreRecordListener;
import ru.shift.view.windows.MainWindow;
import ru.shift.view.windows.RecordsWindow;

@RequiredArgsConstructor
public class RecordWindowObserver implements MV_OnNewRecordListener {

    private final MainWindow mainWindow;

    private final VC_ScoreRecordListener scoreRecordListener;

    @Override
    public void onNewRecord(int newTime, Score oldScore) {
        if (oldScore == null) {
            new RecordsWindow(mainWindow, scoreRecordListener, newTime);
        } else {
            new RecordsWindow(mainWindow, scoreRecordListener,
                    oldScore.name(), oldScore.time(), newTime);
        }
    }
}
