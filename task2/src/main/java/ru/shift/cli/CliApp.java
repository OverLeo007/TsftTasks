package ru.shift.cli;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "Фигуры",
    mixinStandardHelpOptions = true,
    version = "1.0",
    description =
        "Консольное приложение с объектно-ориентированной архитектурой, выводящее "
            + "характеристики заданной геометрической фигуры")
@Slf4j
@SuppressWarnings("unused")
public class CliApp implements Runnable {

  @Option(
      names = {"-i", "--input"},
      required = true,
      description = "Путь до входного файла")
  private String inputFilePath;

  @ArgGroup(multiplicity = "1")
  Exclusive outputType;

  static class Exclusive {
    @Option(
        names = {"-o", "--output"},
        description = "Имя выходного файла")
    private String outputFileName;

    @Option(
        names = {"-co", "--console-output"},
        description = "Вывод в консоль")
    private boolean consoleOutput;
  }


  private boolean validateParameters() {
    if (outputType.outputFileName != null && outputType.outputFileName.startsWith("-")) {
      log.error("Некорректное значение для пути выходного файла");
      return false;
    }
    if (inputFilePath.startsWith("-")) {
      log.error("Некорректное значение для пути входного файла");
      return false;
    }
    return true;
  }

  @Override
  public void run() {
    if (!validateParameters()) {
      return;
    }
    // TODO: Потоки IO создавать тут и закрывать тут (внешний уровень)
    Args args = new Args(inputFilePath, outputType.outputFileName, outputType.consoleOutput);

    MyIoUtils ioUtils = new MyIoUtils(args);


    ioUtils.write("bebebe");
    log.info("Входной файл: {}", inputFilePath);
    log.info("Выходной файл: {}", outputType.outputFileName);
    log.info("Вывод в консоль: {}", outputType.consoleOutput);
  }
}
