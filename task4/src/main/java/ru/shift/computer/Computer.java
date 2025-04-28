package ru.shift.computer;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task.TasksFactory;

@Slf4j
public abstract class Computer {
    private static final long DEFAULT_THRESHOLD = 1_000_000L;

    protected final TasksFactory tasksFactory;
    protected final int workersCount;

    protected Computer(Function<Long, Double> seriesBody, long seriesStart, long seriesEnd) {

        var threshold = System.getProperty("multiThread.threshold");
        var multiThreadDecisionThreshold =
                (threshold == null) ? DEFAULT_THRESHOLD : Long.parseLong(threshold);
        log.debug("Create computer with threshold: {}", multiThreadDecisionThreshold);
        if (seriesEnd - seriesStart > multiThreadDecisionThreshold) {
            workersCount = Runtime.getRuntime().availableProcessors();
        } else {
            workersCount = 1;
        }
        tasksFactory = new TasksFactory(seriesBody, seriesStart, seriesEnd);
    }

    protected Computer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        this.workersCount = isMultiThread ? Runtime.getRuntime().availableProcessors() : 1;
        log.debug("Create computer with workers count: {}", workersCount);
        tasksFactory = new TasksFactory(seriesBody, seriesStart, seriesEnd);
    }

    public abstract double compute();

}
