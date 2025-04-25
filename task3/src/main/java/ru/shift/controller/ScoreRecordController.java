package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import ru.shift.external.api.ScoreRecorder;

@RequiredArgsConstructor
public class ScoreRecordController {

    private final ScoreRecorder scoreRecorder;

    public void recordNewScoreWithName(String name) {
        scoreRecorder.recordNewScore(name);
    }
}
