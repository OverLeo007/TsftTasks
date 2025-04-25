package ru.shift.external.timer;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.bus.EventBus;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.external.events.TimerUpdatedEvent;
import ru.shift.model.events.GameStateChangeEvent;
import ru.shift.model.listeners.ChangeGameStateListener;

@Slf4j
public class Timer implements ChangeGameStateListener {
    private ScheduledExecutorService executor;
    private Instant startTime;
    @Getter
    private volatile int secondsPassed;

    private final EventEmitter eventEmitter = EventBus.getEventEmitter();

    public void start() {
        log.debug("Timer started");
        if (executor != null && !executor.isShutdown()) {
            stop();
        }

        executor = Executors.newSingleThreadScheduledExecutor();
        startTime = Instant.now();
        secondsPassed = 0;

        executor.scheduleAtFixedRate(() -> {
            try {
                long elapsedMillis = Instant.now().toEpochMilli() - startTime.toEpochMilli();
                int newSeconds = (int) (elapsedMillis / 1000);

                if (newSeconds > secondsPassed) {
                    secondsPassed = newSeconds;
                    eventEmitter.emit(new TimerUpdatedEvent(secondsPassed));
                    log.trace("Timer tick: {}s ({}ms)", secondsPassed, elapsedMillis);
                }
            } catch (Exception e) {
                log.error("Timer error", e);
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        log.debug("Timer stopped");
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public void reset() {
        stop();
        log.debug("Timer reset");
        secondsPassed = 0;
        eventEmitter.emit(new TimerUpdatedEvent(secondsPassed));
    }

    @Override
    public void onChangeState(GameStateChangeEvent event) {
        switch (event.gameState()) {
            case PLAY -> start();
            case WIN, LOSE -> stop();
            case STOP -> reset();
        }
    }
}