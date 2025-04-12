package ru.shift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimedTask implements IdentifiedCallable<Double> {
    private final IdentifiedCallable<Double> wrappedTask;

    public TimedTask(IdentifiedCallable<Double> wrappedTask) {
        this.wrappedTask = wrappedTask;
    }

    @Override
    public Double call() throws Exception {
        long startTime = System.nanoTime();

        try {
            return wrappedTask.call();
        } finally {
            long endTime = System.nanoTime();
            double durationMillis = (endTime - startTime) / 1_000_000.0;
            log.info("Время выполнения задачи №{}: {} мс", getId(), String.format("%.2f", durationMillis));
        }
    }

    @Override
    public int getId() {
        return wrappedTask.getId();
    }
}
