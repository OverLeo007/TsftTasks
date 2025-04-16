package ru.shift.view.observers;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.GameDifficulty;
import ru.shift.external.events.ScoreUpdatedEvent;
import ru.shift.external.events.ScoresLoadedEvent;
import ru.shift.external.listeners.ScoresUpdatedListener;
import ru.shift.external.scores.Score;
import ru.shift.view.windows.HighScoresWindow;

@Slf4j
public class HighScoresWindowObserver implements ScoresUpdatedListener {

    private final HighScoresWindow window;

    public HighScoresWindowObserver(HighScoresWindow window) {
        this.window = window;
    }

    @Override
    public void onScoresLoaded(ScoresLoadedEvent event) {
        log.debug("Scores loaded: {}", event.scores());
        setupScores(event.scores());
    }

    @Override
    public void onScoreUpdated(ScoreUpdatedEvent event) {
        setupScore(event.difficulty(), event.score());
    }

    private void setupScores(Map<GameDifficulty, Score> scores) {
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            Score score = scores.get(difficulty);
            setupScore(difficulty, score);
        }
    }

    private void setupScore(GameDifficulty difficulty, Score score) {
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
