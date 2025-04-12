package ru.shift;

import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Task implements IdentifiedCallable<Double> {
    @Getter
    private final int id;

    private final long start;
    private final long stop;

    private final Function<Long, Double> seriesFunction;


    @Override
    public Double call() throws RuntimeException {

        log.info("{} Задача №{} запущена с границами {} и {}", "|".repeat(id), id, start, stop);
        double accumulator = 0.0;
        for (long i = start; i <= stop; i++) {
            accumulator += seriesFunction.apply(i);
        }

        log.info("{} Задача №{} завершила работу с результатом: {}", "|".repeat(id), id,
                accumulator);
        return accumulator;
    }
}
