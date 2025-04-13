package ru.shift.computer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task.TimedTask;

@Slf4j
public class TimedSeriesComputer extends Computer {

    private double executionTimeMs = -1;

    public TimedSeriesComputer(Function<Long, Double> seriesBody, long seriesStart, long seriesEnd) {
        super(seriesBody, seriesStart, seriesEnd);
    }

    public TimedSeriesComputer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        super(seriesBody, seriesStart, seriesEnd, isMultiThread);
    }

    public double compute() {
        log.info("Start execution with {} workers", workersCount);
        var tasks = tasksFactory.initTasks(TimedTask::new, workersCount);
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
            executorService.shutdown();
            long endTime = System.nanoTime();
            double durationMillis = (endTime - startTime) / 1_000_000.0;
            log.info("Execution finished");
            log.info("Execution time: {} ms", durationMillis);
            executionTimeMs = durationMillis;
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
