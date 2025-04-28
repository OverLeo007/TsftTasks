package ru.shift.computer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import ru.shift.series.Series;
import ru.shift.task.TimedTask;

@Slf4j
public class TimedSeriesComputer extends Computer {

    private double executionTimeMs = -1;


    public TimedSeriesComputer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        super(seriesBody, seriesStart, seriesEnd, isMultiThread);
    }

    public TimedSeriesComputer(
            Series series
    ) {
        super(series.getSeriesFunction(), series.getStart(), series.getEnd());
    }

    public TimedSeriesComputer(
            Series series,
            boolean isMultiThread
    ) {
        super(series.getSeriesFunction(), series.getStart(), series.getEnd(), isMultiThread);
    }

    public double compute() {
        log.info("Start execution with {} workers", workersCount);
        var tasks = tasksFactory.initTasks(TimedTask::new, workersCount);
        double result;
        if (workersCount == 1) {
            result = computeSingleThread(tasks.get(0));
        } else {
            result = computeMultiThread(tasks);
        }
        log.info("Execution time: {} ms", executionTimeMs);
        log.info("Execution finished");
        return result;
    }

    private double computeSingleThread(TimedTask task) {
        long startTime = System.nanoTime();
        try {
            double result = task.call();
            long endTime = System.nanoTime();
            executionTimeMs = (endTime - startTime) / 1_000_000.0;

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error while executing task: " + e.getMessage(), e);
        }
    }

    private double computeMultiThread(List<TimedTask> tasks) {
        long startTime = System.nanoTime();
        double result = 0.0;
        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);
        try {
            List<Future<Double>> futures = executorService.invokeAll(tasks);
            for (Future<Double> future : futures) {
                result += future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while executing task: " + e.getMessage(), e);
        } finally {
            long endTime = System.nanoTime();
            executionTimeMs = (endTime - startTime) / 1_000_000.0;
            executorService.shutdown();
        }
        return result;
    }

    public double getExecutionTimeMs() {
        if (executionTimeMs == -1) {
            throw new IllegalStateException("Execution time is not available. Please call compute() first.");
        }
        return executionTimeMs;
    }

}
