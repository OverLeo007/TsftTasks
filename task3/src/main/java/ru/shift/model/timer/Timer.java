package ru.shift.model.timer;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.listeners.MV_TimerListener;

@Slf4j
@RequiredArgsConstructor
public class Timer {
    private ScheduledExecutorService executor;
    private Instant startTime;
    @Getter
    private volatile int secondsPassed;
    private final MV_TimerListener timerListener;

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
                    timerListener.onTimeUpdated(secondsPassed);
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
        timerListener.onTimeUpdated(secondsPassed);
    }
}