package ru.shift;

import picocli.CommandLine;
import ru.shift.cli.CliApp;

public class Main {

    public static void main(String[] args) {
        System.out.println(
                "Уровень логирования (изменяется параметром JVM -Dlog.level={level-name}): "
                        + System.getProperty("log.level"));
        int exitCode = new CommandLine(new CliApp()).execute(args);
        System.exit(exitCode);
    }
}
