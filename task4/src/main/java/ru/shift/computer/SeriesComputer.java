package ru.shift.computer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task.Task;

@Slf4j
public class SeriesComputer extends Computer {

    public SeriesComputer(Function<Long, Double> seriesBody, long seriesStart, long seriesEnd) {
        super(seriesBody, seriesStart, seriesEnd);
    }

    public SeriesComputer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        super(seriesBody, seriesStart, seriesEnd, isMultiThread);
    }

    public double compute() {
        log.info("Start execution with {} workers", workersCount);
        var tasks = tasksFactory.initTasks(workersCount);
        double result;
        if (workersCount == 1) {
            result = computeSingleThread(tasks.get(0));
        } else {
            result = computeMultiThread(tasks);
        }
        log.info("Execution finished");
        return result;
    }

    private double computeSingleThread(Task task) {
        return task.call();
    }

    private double computeMultiThread(List<Task> tasks) {
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
