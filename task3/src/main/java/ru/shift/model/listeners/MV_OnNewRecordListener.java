package ru.shift.model.listeners;

import ru.shift.model.scores.Score;

public interface MV_OnNewRecordListener {
    void onNewRecord(int newTime, Score oldScore);

}
