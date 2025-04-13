package ru.shift;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import ru.shift.computer.Computer;
import ru.shift.computer.SeriesComputer;
import ru.shift.computer.TimedSeriesComputer;
import ru.shift.utils.InputUtil;

@Slf4j
public class Main {

    private static final long SERIES_START = 1L;
    private static final Function<Long, Double> SERIES_FUNCTION = n ->
            (n % 2 == 1 ? 1.0 / n : -1.0 / n);
    private static final double EXPECTED_RESULT = Math.log(2);

    public static void main(String[] args) {
        var isTimed = System.getProperty("timed") != null;
        Computer seriesComputer;

        var n = InputUtil.scanPositiveLong();

        if (isTimed) {
            seriesComputer = new TimedSeriesComputer(SERIES_FUNCTION, SERIES_START, n);
        } else {
            seriesComputer = new SeriesComputer(SERIES_FUNCTION, SERIES_START, n);
        }

        var result = seriesComputer.compute();
        log.info("Result: {}", result);
        log.info("Expected result: {}", EXPECTED_RESULT);
    }

}