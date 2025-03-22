package ru.shift.cli;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MyIoUtils {
  private final String outputPath;
  private final boolean isConsoleOutput;

  public MyIoUtils(Args args) {
    this.outputPath = args.outputFilePath();
    this.isConsoleOutput = args.consoleOutput();
  }

  public static String readFromFile(String inputFilePath) {
    // FIXME: Предусмотреть что должно быть ровно две строки и читать только их
    // и ваще вынести в класс фигуры
    log.debug("Чтение из файла: {}", inputFilePath);
    try (FileInputStream inputStream = new FileInputStream(inputFilePath)) {
      return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error(
          "При попытке чтения из файла произошла ошибка {}: {}",
          e.getClass().getSimpleName(),
          e.getMessage());
      log.debug("Подробности ошибки", e);
    }
    return null;
  }

  public void write(String content) {
    if (isConsoleOutput) {
      writeToConsole(content);
    } else {
      writeToFile(content);
    }
  }

  private void writeToConsole(String content) {
    log.debug("Вывод в консоль");
    System.out.println(content);
  }

  private void writeToFile(String content) {
    log.debug("Запись в файл: {}", outputPath);
    try {
      Path outPath = Path.of(outputPath);
      Path dir = outPath.getParent();
      if (dir != null && !Files.exists(dir)) {
        Files.createDirectories(dir);
        log.debug("Создана директория {} для файла {}", dir, outPath.getFileName());
      }

      try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
        IOUtils.write(content, outputStream, StandardCharsets.UTF_8);
      }
      log.info("Данные успешно записаны в файл {}", outputPath);
    } catch (IOException | InvalidPathException e) {
      log.error(
          "При попытке записи в файл произошла ошибка {}: {}",
          e.getClass().getSimpleName(),
          e.getMessage());
      log.debug("Подробности ошибки", e);
    }
  }
}
