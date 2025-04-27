package ru.shift.external.scores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.bus.EventBus;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.external.api.ScoreRecorder;
import ru.shift.app.GameDifficulty;
import ru.shift.external.events.ScoreUpdatedEvent;
import ru.shift.external.events.ScoresLoadedEvent;
import ru.shift.model.api.GameDifficultyHolder;

@Slf4j
public class ScoreRepository implements ScoreRecorder {

    public static final String SCORES_FILE_NAME = "scores.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<GameDifficulty, Score> scores;

    private int heldTime;
    private final EventEmitter eventEmitter = EventBus.getEventEmitter();

    private final GameDifficultyHolder gameDifficultyHolder;

    {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public ScoreRepository(GameDifficultyHolder gameDifficultyHolder) {
        this.gameDifficultyHolder = gameDifficultyHolder;
        this.scores = loadScores();
    }

    public Score getScore() {
        return scores.get(gameDifficultyHolder.getGameDifficulty());
    }

    public ScoreComparisonResult compareAndHoldTime(int time) {
        var difficulty = gameDifficultyHolder.getGameDifficulty();
        var prevTime = scores.get(difficulty);
        if (prevTime == null || prevTime.time() >= time) {
            heldTime = time;
            return ScoreComparisonResult.BEATEN;
        }
        return ScoreComparisonResult.BIGGER;
    }

    public void onListenersSubscribed() {
        eventEmitter.emit(new ScoresLoadedEvent(scores));
    }

    private void addScore(Score score) {
        var difficulty = gameDifficultyHolder.getGameDifficulty();
        if (isUninitialized()) {
            return;
        }
        if (scores.containsKey(difficulty)) {
            Score existingScore = scores.get(difficulty);
            if (score.time() <= existingScore.time()) {
                scores.put(difficulty, score);
                saveScores();
                eventEmitter.emit(new ScoreUpdatedEvent(difficulty, score));
            }
        } else {
            scores.put(difficulty, score);
            saveScores();
            eventEmitter.emit(new ScoreUpdatedEvent(difficulty, score));
        }
    }

    private Map<GameDifficulty, Score> loadScores() {
        log.debug("Loading score state");
        File file = new File(SCORES_FILE_NAME);
        if (file.exists()) {
            try {
                return objectMapper.readValue(
                        file, new TypeReference<EnumMap<GameDifficulty, Score>>() {
                        }
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
    public void recordNewScore(String championName) {
        log.debug("New score: {} {}", championName, heldTime);
        if (!isValidName(championName)) {
            log.warn("Invalid name: {}", championName);
            return;
        }
        Score newScore = new Score(championName, heldTime);
        addScore(newScore);
        heldTime = 0;


    }

    private boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return name.length() <= 120;
    }

}
