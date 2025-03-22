package ru.shift;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import ru.shift.cli.CliApp;

@Slf4j
public class Main {

  public static void main(String[] args) {
    String logLevel = System.getProperty("log.level");
    System.out.println(
        "Уровень логирования (изменяется параметром JVM -Dlog.level={level-name}): " + logLevel);
    int exitCode = new CommandLine(new CliApp()).execute(args);
    System.exit(exitCode);
  }
}
