package ru.shift.computer;

import java.util.function.Function;
import ru.shift.task.TasksFactory;

public abstract class Computer {
    protected final TasksFactory tasksFactory;
    protected final int workersCount;

    public Computer(Function<Long, Double> seriesBody, long seriesStart, long seriesEnd) {
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

    public Computer(
            Function<Long, Double> seriesBody,
            long seriesStart,
            long seriesEnd,
            boolean isMultiThread
    ) {
        this.workersCount = isMultiThread ? Runtime.getRuntime().availableProcessors() : 1;
        tasksFactory = new TasksFactory(seriesBody, seriesStart, seriesEnd);
    }

    public abstract double compute();

}
