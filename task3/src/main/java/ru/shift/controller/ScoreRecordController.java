package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import ru.shift.controller.listeners.CM_ScoreRecordListener;
import ru.shift.view.listeners.VC_ScoreRecordListener;

@RequiredArgsConstructor
public class ScoreRecordController implements VC_ScoreRecordListener {

    private final CM_ScoreRecordListener scoreRecordListener;

    @Override
    public void onRecordNameEntered(String name) {
        scoreRecordListener.onNewScore(name);
    }
}
