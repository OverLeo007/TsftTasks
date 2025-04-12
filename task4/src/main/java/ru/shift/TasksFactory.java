package ru.shift;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TasksFactory {
    private final long globalStart;
    private final long globalStop;
    private final Function<Long, Double> seriesFunction;

    public TasksFactory(
            Function<Long, Double> seriesFunction,
            long globalStart,
            long globalStop
    ) {
        this.globalStart = globalStart;
        this.globalStop = globalStop;
        this.seriesFunction = seriesFunction;
    }

    public <T extends IdentifiedCallable<Double>> List<T> initTasks(
            Function<Task, T> decorator, int workersCount
    ) {
        var chunkSize = (globalStop - globalStart) / workersCount;
        List<T> tasks = new ArrayList<>();
        for (int i = 0; i < workersCount; i++) {
            long curStart = globalStart + i * chunkSize;
            long curEnd = (i == workersCount - 1) ? globalStop : curStart + chunkSize - 1;
            Task task = new Task(i + 1, curStart, curEnd, seriesFunction);
            tasks.add(decorator.apply(task));
        }
        return tasks;
    }

    public List<Task> initTasks(int workersCount) {
        return initTasks(d -> d, workersCount);
    }
}
