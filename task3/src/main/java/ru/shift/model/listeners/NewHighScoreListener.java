package ru.shift.model.listeners;

import ru.shift.model.events.NewHighScoreEvent;

public interface NewHighScoreListener {
    void onNewHighScore(NewHighScoreEvent event);

}
