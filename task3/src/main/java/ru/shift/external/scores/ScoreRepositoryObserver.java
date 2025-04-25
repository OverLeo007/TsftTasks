package ru.shift.external.scores;

import lombok.RequiredArgsConstructor;
import ru.shift.app.bus.EventBus;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.external.timer.Timer;
import ru.shift.model.events.GameStateChangeEvent;
import ru.shift.model.events.NewHighScoreEvent;
import ru.shift.model.listeners.ChangeGameStateListener;
import ru.shift.model.state.GameState;

@RequiredArgsConstructor
public class ScoreRepositoryObserver implements ChangeGameStateListener {
    private final EventEmitter eventEmitter = EventBus.getEventEmitter();
    private final ScoreRepository scoreRepository;
    private final Timer timer;

    @Override
    public void onChangeState(GameStateChangeEvent event) {
        if (event.gameState() == GameState.WIN) {
            int curTime = timer.getSecondsPassed();
            Score savedScore = scoreRepository.getScore();
            if (scoreRepository.compareAndHoldTime(curTime)
                    == ScoreComparisonResult.BEATEN) {
                eventEmitter.emit(new NewHighScoreEvent(curTime, savedScore));
            }
        }
    }
}
