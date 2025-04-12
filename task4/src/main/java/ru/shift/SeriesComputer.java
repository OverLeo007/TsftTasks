package ru.shift;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeriesComputer {

    private final TasksFactory tasksFactory;
    private final int workersCount;

    public SeriesComputer(Function<Long, Double> seriesBody, long seriesStart, long seriesEnd) {
        var threshold = System.getProperty("multiThread.threshold");
        var multiThreadDecisionThreshold =
                (threshold == null) ? 1000000 : Long.parseLong(threshold);
        if (seriesEnd - seriesStart > multiThreadDecisionThreshold) {
            workersCount = Runtime.getRuntime().availableProcessors();
        } else {
            workersCount = 1;
        }
        tasksFactory = new TasksFactory(seriesBody, seriesStart, seriesEnd);
    }

    public SeriesComputer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        this.workersCount = isMultiThread ? Runtime.getRuntime().availableProcessors() : 1;
        tasksFactory = new TasksFactory(seriesBody, seriesStart, seriesEnd);
    }

    public double compute() {
        log.info("Запускаем вычисление с {} потоками", workersCount);
        var tasks = tasksFactory.initTasks(workersCount);

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
        }
        return result;

    }
}
