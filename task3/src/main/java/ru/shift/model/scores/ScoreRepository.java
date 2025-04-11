package ru.shift.model.scores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_ScoreRecordListener;
import ru.shift.model.GameDifficulty;
import ru.shift.model.listeners.MV_OnScoresUpdatedListener;

@Slf4j
public class ScoreRepository implements CM_ScoreRecordListener {
    public static final String SCORES_FILE_NAME = "scores.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<GameDifficulty, Score> scores;

    private int heldTime;
    private GameDifficulty heldGameDifficulty;

    private final MV_OnScoresUpdatedListener onScoresUpdatedListener;

    {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public ScoreRepository(MV_OnScoresUpdatedListener onScoresUpdatedListener) {
        this.onScoresUpdatedListener = onScoresUpdatedListener;
        this.scores = loadScores();
        onScoresUpdatedListener.onScoresLoaded(scores);
    }

    public Score getScore(GameDifficulty difficulty) {
        return scores.get(difficulty);
    }

    public ScoreComparisonResult compareAndHoldTime(int time, GameDifficulty difficulty) {
        var prevTime = scores.get(difficulty);
        if (prevTime == null || prevTime.time() >= time) {
            heldTime = time;
            heldGameDifficulty = difficulty;
            return ScoreComparisonResult.BEATEN;
        }
        return ScoreComparisonResult.BIGGER;
    }

    private void addScore(Score score, GameDifficulty difficulty) {
        if (isUninitialized()) {
            return;
        }
        if (scores.containsKey(difficulty)) {
            Score existingScore = scores.get(difficulty);
            if (score.time() <= existingScore.time()) {
                scores.put(difficulty, score);
                saveScores();
                onScoresUpdatedListener.onScoreUpdated(difficulty, score);
            }
        } else {
            scores.put(difficulty, score);
            saveScores();
            onScoresUpdatedListener.onScoreUpdated(difficulty, score);
        }

    }

    private Map<GameDifficulty, Score> loadScores() {
        log.debug("Loading score state");
        File file = new File(SCORES_FILE_NAME);
        if (file.exists()) {
            try {
                return objectMapper.readValue(
                        file, new TypeReference<EnumMap<GameDifficulty, Score>>() {}
                );
            } catch (IOException e) {
                log.error("Error loading scores: {}", e.getMessage());
            }
        }
        return new EnumMap<>(GameDifficulty.class);
    }

    private void saveScores() {
        if (isUninitialized()) {
            return;
        }
        try {
            objectMapper.writeValue(new File(SCORES_FILE_NAME), scores);
        } catch (IOException e) {
            log.error("Error saving scores: {}", e.getMessage());
        }
    }

    private boolean isUninitialized() {
        if (scores == null) {
            log.warn("Score repository is not initialized");
            return true;
        }
        return false;
    }

    @Override
    public void onNewScore(String name) {
        log.debug("New score: {} {}", name, heldTime);
        if (!isValidName(name)) {
            log.warn("Invalid name: {}", name);
            return;
        }
        if (heldGameDifficulty != null) {
            Score newScore = new Score(name, heldTime);
            addScore(newScore, heldGameDifficulty);
            heldGameDifficulty = null;
            heldTime = 0;
        }

    }

    private boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return name.length() <= 120;
    }
}
