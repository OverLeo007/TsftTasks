package ru.shift.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import ru.shift.figures.FigureType;

@Slf4j
public final class MyUtils {

    public static final int EOF_RETURN_CODE = -1;
    public static final String EOL = System.lineSeparator();

    private MyUtils() {
        throw new UnsupportedOperationException("Класс утилит не может быть создан");
    }

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

    private static String readAmountOrEolOrEof(BufferedReader reader, int readLen) throws IOException {
        StringBuilder sb = new StringBuilder();
        int curChar;
        while (sb.length() < readLen && (curChar = reader.read()) != EOF_RETURN_CODE) {
            log.trace("Прочитан символ с id={} - \"{}\"", curChar, (char) curChar);
            if (curChar == '\n') {
                break;
            }
            if (curChar != '\r') {
                sb.append((char) curChar);
            }
        }
        return sb.toString().trim();
    }


    /**
     * Метод чтения фигуры из файла с защитой от утечки памяти при аномально длинной первой строке в
     * файле
     *
     * @param reader поток ввода
     * @return название фигуры
     * @throws IOException              если произошла ошибка ввода/вывода, в том числе внезапный
     *                                  конец файла
     * @throws IllegalArgumentException если название фигуры пустое
     */
    public static String readFigType(BufferedReader reader) throws IOException {
        log.debug("Чтение типа фигуры из файла");

        String figureTypeLine = readAmountOrEolOrEof(reader, FigureType.MAX_NAME_LINE_LEN);

        if (figureTypeLine.isBlank()) {
            throw new IllegalArgumentException("Название фигуры не может быть пустым");
        }
        return figureTypeLine;
    }

    /**
     * Метод чтения параметров фигуры из файла. Общая длина параметров получается из констант
     * {@link FigureType}
     *
     * @param reader     поток ввода на моменте чтения строки с параметрами фигуры (строка с типом
     *                   фигуры уже считана)
     * @param figureType тип фигуры
     * @return строковый массив параметров фигуры без приведения типа
     * @throws IOException              ошибка при чтении строки
     * @throws IllegalArgumentException если строка с параметрами не найдена или пустая
     */
    public static String[] readParamsFromLine(BufferedReader reader, FigureType figureType)
            throws IOException, IllegalArgumentException {

        log.debug("Парсинг параметров фигуры из строки");
        var paramMaxLen = figureType.getParamLineMaxLen();
        log.debug("Максимальная длина строки с параметрами: {}", paramMaxLen);

        String paramsLine = readAmountOrEolOrEof(reader, paramMaxLen);
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
