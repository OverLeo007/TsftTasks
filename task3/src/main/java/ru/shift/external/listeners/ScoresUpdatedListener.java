package ru.shift.external.listeners;

import ru.shift.external.events.ScoreUpdatedEvent;
import ru.shift.external.events.ScoresLoadedEvent;

public interface ScoresUpdatedListener {
    void onScoresLoaded(ScoresLoadedEvent event);
    void onScoreUpdated(ScoreUpdatedEvent event);
}
