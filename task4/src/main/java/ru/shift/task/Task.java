package ru.shift.task;

import java.util.function.Function;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task implements IdentifiedCallable<Double> {
    @Getter
    private final int id;
    private final String tab;

    private final long start;
    private final long stop;

    private final Function<Long, Double> seriesFunction;

    public Task(int id, long start, long stop, Function<Long, Double> seriesFunction) {
        this.id = id;
        this.start = start;
        this.stop = stop;
        this.seriesFunction = seriesFunction;
        this.tab = "─".repeat(id * 2);
    }

    @Override
    public Double call() throws RuntimeException {

        log.info("{} T{} START with bounds {} to {}", tab, id, start, stop);
        double accumulator = 0.0;
        for (long i = start; i <= stop; i++) {
            accumulator += seriesFunction.apply(i);
        }

        log.info("{} T{} END with result: {}", tab, id,
                accumulator);
        return accumulator;
    }
}
