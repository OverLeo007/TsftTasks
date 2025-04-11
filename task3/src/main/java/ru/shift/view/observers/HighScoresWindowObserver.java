package ru.shift.view.observers;

import java.util.Map;
import ru.shift.model.GameDifficulty;
import ru.shift.model.listeners.MV_OnScoresUpdatedListener;
import ru.shift.model.scores.Score;
import ru.shift.view.windows.HighScoresWindow;

public class HighScoresWindowObserver implements MV_OnScoresUpdatedListener {

    private final HighScoresWindow window;

    public HighScoresWindowObserver(HighScoresWindow window) {
        this.window = window;
    }

    @Override
    public void onScoresLoaded(Map<GameDifficulty, Score> scores) {
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            Score score = scores.get(difficulty);
            onScoreUpdated(difficulty, score);
        }
    }

    @Override
    public void onScoreUpdated(GameDifficulty difficulty, Score score) {
        if (score == null) {
            return;
        }
        switch (difficulty) {
            case NOVICE -> window.setNoviceRecord(score.name(), score.time());
            case MEDIUM -> window.setMediumRecord(score.name(), score.time());
            case EXPERT -> window.setExpertRecord(score.name(), score.time());
        }

    }
}
