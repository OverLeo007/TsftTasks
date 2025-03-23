package ru.shift.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import ru.shift.figures.FigureType;

@Slf4j
public class MyUtils {

  public static void createDirForFileIfNotExist(String filePath) throws IOException {
    if (filePath == null) {
      return;
    }
    Path outPath = Path.of(filePath);
    Path dir = outPath.getParent();
    if (dir != null && !Files.exists(dir)) {
      log.debug("Попытка создания директории для файла {}", filePath);
      Files.createDirectories(dir);
      log.debug("Создана директория {} для файла {}", dir, outPath.getFileName());
    }
  }

  public static String[] parseParamsFromLine(BufferedReader reader, FigureType figureType)
      throws IOException, IllegalArgumentException {
    log.debug("Парсинг параметров фигуры из строки");
    var paramsLine = reader.readLine();
    if (paramsLine == null) {
      throw new IllegalArgumentException("Строка с параметрами фигуры не найдена");
    }
    paramsLine = paramsLine.trim();
    if (paramsLine.isBlank()) {
      throw new IllegalArgumentException("Строка с параметрами фигуры не может быть пустой");
    }
    var params = paramsLine.split(" ");

    figureType.validateParamCount(params);

    return params;
  }

  public static double parsePositiveDouble(String str) throws IllegalArgumentException {
    log.trace("Попытка преобразования \"{}\" в положительное число", str);
    if (str == null || str.isBlank()) {
      throw new IllegalArgumentException("Строка не может быть пустой");
    }
    try {
      double value = Double.parseDouble(str);
      if (value < 1) {
        throw new IllegalArgumentException("Значение должно быть >= 1");
      }
      return value;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Значение должно быть числом, но получено: " + str);
    }
  }

}
