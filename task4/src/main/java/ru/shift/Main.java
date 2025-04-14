package ru.shift;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.shift.computer.Computer;
import ru.shift.computer.SeriesComputer;
import ru.shift.computer.TimedSeriesComputer;
import ru.shift.utils.InputUtil;

@Slf4j
@Command(
        name = "SeriesComputer",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Calculates the sum of the series with the ability "
                + "to detect the execution time, select execution in single-threaded "
                + "or multithreaded modes, and specify the threshold value "
                + "at which the multithreaded mode will start."
)
public class Main implements Runnable{

    private static final long SERIES_START = 1L;
    private static final Function<Long, Double> SERIES_FUNCTION = n ->
            (n % 2 == 1 ? 1.0 / n : -1.0 / n);
    private static final double EXPECTED_RESULT = Math.log(2);

    @Option(names = {"-t", "--timed"}, description = "Enable timed execution")
    private boolean timed = false;

    @ArgGroup(multiplicity = "1")
    Exclusive multiThreadDecision;

    static class Exclusive {

        @Option(names = {"-th", "--threshold"}, description = "Set threshold for using multi-threading decision")
        private Long threshold;
        @Option(
                names = {"-mt", "--multi-thread"},
                negatable = true,
                description = "Enable multi-threading (true by default)"
        )
        private Boolean multiThread;

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        Computer seriesComputer;
        var n = InputUtil.scanPositiveLong();
        if (multiThreadDecision.threshold != null) {
            System.setProperty("multiThread.threshold", String.valueOf(multiThreadDecision.threshold));
        }

        if (multiThreadDecision.multiThread == null) {
            seriesComputer = timed
                    ? new TimedSeriesComputer(SERIES_FUNCTION, SERIES_START, n)
                    : new SeriesComputer(SERIES_FUNCTION, SERIES_START, n);
        } else {
            seriesComputer = timed
                    ? new TimedSeriesComputer(SERIES_FUNCTION, SERIES_START, n, multiThreadDecision.multiThread)
                    : new SeriesComputer(SERIES_FUNCTION, SERIES_START, n, multiThreadDecision.multiThread);
        }

        var result = seriesComputer.compute();
        log.info("Result: {}", result);
        log.info("Expected result: {}", EXPECTED_RESULT);

    }

}