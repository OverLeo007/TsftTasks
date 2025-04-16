package ru.shift.model.state;

import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.bus.EventBus;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.app.GameDifficulty;
import ru.shift.model.events.GameStateChangeEvent;

@Slf4j
public class GameStateMachine {

    private GameState state = GameState.STOP;
    @Getter
    private GameDifficulty gameDifficulty = GameDifficulty.NOVICE;

    private final EventEmitter eventEmitter = EventBus.getEventEmitter();


    public void setState(GameState state) {
        log.debug("Set game state to {}", state);
        this.state = state;
        eventEmitter.emit(new GameStateChangeEvent(state));
//        sideEffect(state);
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        log.debug("Set game difficulty to {}", gameDifficulty);
        this.gameDifficulty = gameDifficulty;
    }

    public GameState getState() {
        log.debug("Get game state {}", state);
        return state;
    }

    public boolean compareState(Predicate<GameState> predicate) {
        return predicate.test(state);
    }

}
