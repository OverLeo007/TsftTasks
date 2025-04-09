package ru.shift.model.state;

import java.util.function.Predicate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.model.timer.Timer;

@Slf4j
public class GameStateManager {
    private GameState state = GameState.STOP;

    private final Timer timer;
    private final MV_EndGameListener winListener;
    private final MV_EndGameListener loseListener;

    @Builder
    private GameStateManager(Timer timer, MV_EndGameListener winListener,
            MV_EndGameListener loseListener) {
        this.timer = timer;
        this.winListener = winListener;
        this.loseListener = loseListener;
    }

    public void setState(GameState state) {
        log.debug("Set game state to {}", state);
        this.state = state;
        sideEffect(state);
    }

    private void sideEffect(GameState state) {
        switch (state) {
            case STOP -> timer.reset();
            case WIN -> {
                timer.stop();
                winListener.onGameEnd();
            }
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
}
