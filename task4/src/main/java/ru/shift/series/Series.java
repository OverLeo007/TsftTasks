package ru.shift.series;

import java.util.function.Function;
import lombok.Data;

@Data
public class Series {
    private long start;
    private long end;
    private Function<Long, Double> seriesFunction;
    private Double expectedResult;

    public Series(long start, Function<Long, Double> seriesFunction, Double expectedResult) {
        this.start = start;
        this.end = -1;
        this.seriesFunction = seriesFunction;
        this.expectedResult = expectedResult;
    }


}
