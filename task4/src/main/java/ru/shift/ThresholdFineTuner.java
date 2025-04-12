package ru.shift;

import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import ru.shift.computer.TimedSeriesComputer;
import ru.shift.task.Task;
import ru.shift.task.TimedTask;
import ru.shift.utils.LogControl;

@Slf4j
public class ThresholdFineTuner {

    private static final long SERIES_START = 1L;
    private static final Function<Long, Double> SERIES_FUNCTION = n ->
            (n % 2 == 1 ? 1.0 / n : -1.0 / n);
    private static final double EXPECTED_RESULT = Math.log(2);

    private static final int MAX_EXECUTION_TIME_MS = 5000;
    private static final double SPEEDUP_THRESHOLD = 0.95;
    private static final int AVERAGE_RUNS = 3;
    private static final int MIN_N = 10;

    public static void main(String[] args) {
        disableRealisationLogging();
        var tuner = new ThresholdFineTuner();
//        var n = InputUtil.scanPositiveLong();
        var res = tuner.findMultiThreadEfficiencyThreshold(4);
        log.info("Threshold found: {}", res);
    }

    private static void disableRealisationLogging() {
        LogControl.disableLogsForClass(TimedSeriesComputer.class);
        LogControl.disableLogsForClass(TimedTask.class);
        LogControl.disableLogsForClass(Task.class);
    }

    private TimedSeriesComputer getSingleThreadComputer(long n) {
        return new TimedSeriesComputer(SERIES_FUNCTION, SERIES_START, n, false);
    }

    private TimedSeriesComputer getMultiThreadComputer(long n) {
        return new TimedSeriesComputer(SERIES_FUNCTION, SERIES_START, n, true);
    }

    private long findMultiThreadEfficiencyThreshold(int startPower) {
        long curN = (long) Math.pow(10, startPower);
        long curStep = curN;
        int stepDirection = 1;

        do {
            if (isMultiThreadFaster(curN) ) {
                log.info("switching direction");
                curStep /= 10;
                stepDirection *= -1;
            }
            curN += curStep * stepDirection;

            if (curN > 100_000_000 || curN < MIN_N) {
                log.warn("Threshold not found for n = {}", curN);
                return curN;
            }
        } while (curStep >= MIN_N);
        return curN;
    }

    private boolean isMultiThreadFaster(long n) {
        double singleThreadTime = measureAvgTime(() -> getSingleThreadComputer(n));
        double multiThreadTime = measureAvgTime(() -> getMultiThreadComputer(n));
        ensureTimeLimitNotExceeded(singleThreadTime, multiThreadTime, n);
        log.info("For {} - Single-threaded time: {} ms, Multi-threaded time: {} ms", n, singleThreadTime, multiThreadTime);
        return multiThreadTime < singleThreadTime * SPEEDUP_THRESHOLD;
    }

    private double measureAvgTime(Supplier<TimedSeriesComputer> supplier) {
        double total = 0;
        for (int i = 0; i < AVERAGE_RUNS; i++) {
            TimedSeriesComputer comp = supplier.get();
            comp.compute();
            total += comp.getExecutionTimeMs();
        }
        return total / AVERAGE_RUNS;
    }

    private void ensureTimeLimitNotExceeded(double singleTime, double multiTime, long n) {
        if (singleTime > MAX_EXECUTION_TIME_MS || multiTime > MAX_EXECUTION_TIME_MS) {
            throw new RuntimeException("Время выполнения превысило лимит на n = " + n);
        }
    }
}
