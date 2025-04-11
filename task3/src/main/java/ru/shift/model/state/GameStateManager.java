package ru.shift.model.state;

import java.util.function.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.GameDifficulty;
import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.model.listeners.MV_OnNewRecordListener;
import ru.shift.model.scores.Score;
import ru.shift.model.scores.ScoreComparisonResult;
import ru.shift.model.scores.ScoreRepository;
import ru.shift.model.timer.Timer;

@Slf4j
public class GameStateManager {
    private GameState state = GameState.STOP;
    @Getter
    private GameDifficulty gameDifficulty = GameDifficulty.NOVICE;

    private final Timer timer;
    private final MV_EndGameListener winListener;
    private final MV_EndGameListener loseListener;
    private final MV_OnNewRecordListener onNewRecordListener;


    private final ScoreRepository scoreRepository;

    @Builder
    private GameStateManager(
            Timer timer,
            MV_EndGameListener winListener,
            MV_EndGameListener loseListener,
            MV_OnNewRecordListener onNewRecordListener,
            ScoreRepository scoreRepository
    ) {
        this.timer = timer;
        this.winListener = winListener;
        this.loseListener = loseListener;
        this.onNewRecordListener = onNewRecordListener;
        this.scoreRepository = scoreRepository;
    }

    public void setState(GameState state) {
        log.debug("Set game state to {}", state);
        this.state = state;
        sideEffect(state);
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        log.debug("Set game difficulty to {}", gameDifficulty);
        this.gameDifficulty = gameDifficulty;
    }

    private void sideEffect(GameState state) {
        switch (state) {
            case STOP -> timer.reset();
            case WIN -> onWinState();
            case LOSE -> {
                timer.stop();
                loseListener.onGameEnd();
            }
            case PLAY -> timer.start();
        }
    }

    public GameState getState() {
        log.debug("Get game state {}", state);
        return state;
    }

    public boolean compareState(Predicate<GameState> predicate) {
        return predicate.test(state);
    }

    private void onWinState() {
        timer.stop();
        int curTime = timer.getSecondsPassed();

        Score savedScore = scoreRepository.getScore(gameDifficulty);

        if (scoreRepository.compareAndHoldTime(curTime, gameDifficulty) == ScoreComparisonResult.BEATEN) {
            onNewRecordListener.onNewRecord(curTime, savedScore);
        }
        winListener.onGameEnd();
    }
}
