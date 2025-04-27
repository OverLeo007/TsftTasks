package ru.shift.external;

import lombok.Getter;
import ru.shift.app.bus.EventBus;
import ru.shift.external.scores.ScoreRepository;
import ru.shift.external.scores.ScoreRepositoryObserver;
import ru.shift.external.timer.Timer;
import ru.shift.model.api.GameDifficultyHolder;
import ru.shift.model.events.GameStateChangeEvent;

@Getter
public class External {
    private final ScoreRepository scoreRepository;

    public External(GameDifficultyHolder gameDifficultyHolder) {
        scoreRepository = new ScoreRepository(gameDifficultyHolder);
        Timer timer = new Timer();
        ScoreRepositoryObserver scoreRepositoryObserver = new ScoreRepositoryObserver(
                scoreRepository, timer);

        var subscriber = EventBus.getEventSubscriber();
        subscriber.subscribe(GameStateChangeEvent.class, scoreRepositoryObserver::onChangeState, 1);
        subscriber.subscribe(GameStateChangeEvent.class, timer::onChangeState, 2);
    }

}
