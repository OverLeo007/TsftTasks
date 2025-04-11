package ru.shift.model.listeners;

import java.util.Map;
import ru.shift.model.GameDifficulty;
import ru.shift.model.scores.Score;

public interface MV_OnScoresUpdatedListener {
    void onScoresLoaded(Map<GameDifficulty, Score> scores);
    void onScoreUpdated(GameDifficulty difficulty, Score score);
}
