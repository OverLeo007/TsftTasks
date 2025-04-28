package ru.shift;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.shift.computer.Computer;
import ru.shift.computer.SeriesComputer;
import ru.shift.computer.TimedSeriesComputer;
import ru.shift.series.Series;
import ru.shift.series.SeriesRepository;
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


    @Option(names = {"-t", "--timed"}, description = "Enable timed execution")
    private boolean timed = false;

    @ArgGroup
    Exclusive multiThreadDecision;

    static class Exclusive {

        @Option(names = {"-th", "--threshold"}, description = "Set threshold for using multi-threading decision")
        private Long threshold;
        @Option(
                names = {"-mt", "--multi-thread"},
                negatable = true,
                description = "Enable multi-threading (true by default)"
        )
        private Boolean multiThread = true;

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        Computer seriesComputer;
        if (multiThreadDecision == null) {
            log.warn("No multi-threading decision provided, using default");
            multiThreadDecision = new Exclusive();
        }

        log.info("Multi-threading decision: {}", multiThreadDecision.multiThread);

        var series = pickSeries();

        System.out.print("Enter a positive long type number: ");
        var n = InputUtil.scanPositiveLong();
        series.setEnd(n);
        if (multiThreadDecision.threshold != null) {
            System.setProperty("multiThread.threshold", String.valueOf(multiThreadDecision.threshold));
        }

        if (multiThreadDecision.multiThread == null) {
            seriesComputer = timed
                    ? new TimedSeriesComputer(series)
                    : new SeriesComputer(series);
        } else {
            seriesComputer = timed
                    ? new TimedSeriesComputer(series, multiThreadDecision.multiThread)
                    : new SeriesComputer(series, multiThreadDecision.multiThread);
        }

        var result = seriesComputer.compute();
        log.info("Result: {}", result);
        log.info("Expected result: {}", series.getExpectedResult());
    }

    private Series pickSeries() {
        System.out.println("Select series function:");
        var repo = SeriesRepository.getInstance();
        var seriesNames = repo.getSeriesNames();
        for (int i = 0; i < seriesNames.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, seriesNames.get(i));
        }
        int choice = InputUtil.scanPositiveInt();
        if (choice < 1 || choice > seriesNames.size()) {
            System.out.println("Invalid choice. Please try again.");
            return pickSeries();
        }
        var seriesName = seriesNames.get(choice - 1);
        var series = repo.getSeries(seriesName);
        System.out.println("Selected series: " + seriesName);
        return series;
    }

}