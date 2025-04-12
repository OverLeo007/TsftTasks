package ru.shift;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static final long SERIES_START = 1L;
    private static final Function<Long, Double> SERIES_FUNCTION = n ->
            (n % 2 == 1 ? 1.0 / n : -1.0 / n);
    private static final double EXPECTED_RESULT = Math.log(2);

    public static void main(String[] args) {
//        var n = InputUtil.scanPositiveLong();
        var n = 1_000_000L;
        var seriesComputer = new SeriesComputer(SERIES_FUNCTION, SERIES_START, n, true);
        var result = seriesComputer.compute();
        log.info("Result: {}", result);
        log.info("Expected result: {}", EXPECTED_RESULT);


    }
}