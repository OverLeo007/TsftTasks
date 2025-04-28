package ru.shift.task;

import java.util.function.Function;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task implements IdentifiedCallable<Double> {
    @Getter
    private final int id;

    private final long start;
    private final long stop;

    private final Function<Long, Double> seriesFunction;

    public Task(int id, long start, long stop, Function<Long, Double> seriesFunction) {
        this.id = id;
        this.start = start;
        this.stop = stop;
        this.seriesFunction = seriesFunction;
    }

    @Override
    public Double call() throws RuntimeException {

        log.debug("T{} START with bounds {} to {}", id, start, stop);
        double accumulator = 0.0;
        for (long i = start; i <= stop; i++) {
            accumulator += seriesFunction.apply(i);
        }

        log.debug("T{} END with result: {}", id,
                accumulator);
        return accumulator;
    }
}
