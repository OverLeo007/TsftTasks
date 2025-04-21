package ru.shift.series;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class SeriesRepository {
    private final Map<String, Series> seriesMap = new LinkedHashMap<>();

    @Getter
    private static final SeriesRepository instance = new SeriesRepository();

    public SeriesRepository() {
        seriesMap.put(
                "∑(0 to ∞) (1/2)^n = 2",
                new Series(0, n -> Math.pow(0.5, n), 2.0)
        );

        seriesMap.put(
                "∑(0 to ∞) (-1)^n / (2n + 1) = π / 4",
                new Series(0, n -> Math.pow(-1, n) / (2 * n + 1), Math.PI / 4)
        );

        seriesMap.put(
                "∑(1 to ∞) (-1)^(n+1) / n = ln(2)",
                new Series(1, n -> Math.pow(-1, n + 1) / n, Math.log(2))
        );

        seriesMap.put(
                "∑(1 to ∞) 1 / n² = π² / 6",
                new Series(1, n -> 1.0 / (n * n), Math.PI * Math.PI / 6)
        );

        seriesMap.put(
                "∑(1 to ∞) 1 / (n(n + 1)) = 1",
                new Series(1, n -> 1.0 / (n * (n + 1)), 1.0)
        );
    }


    public List<String> getSeriesNames() {
        return List.copyOf(seriesMap.keySet());
    }

    public Series getSeries(String name) {
        return seriesMap.get(name);
    }
}
