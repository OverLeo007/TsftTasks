package ru.shift.utils;

import java.io.BufferedReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.shift.factory.FigureType;

@Slf4j
public class InputUtils {
    public static final int EOF_RETURN_CODE = -1;

    private InputUtils() {
        throw new UnsupportedOperationException("Класс утилит не может быть создан");
    }

    private static String readAmountOrEolOrEof(
            BufferedReader reader, int amountChars) throws IOException {
        StringBuilder sb = new StringBuilder(amountChars);
        int curChar;
        while (sb.length() < amountChars && (curChar = reader.read()) != EOF_RETURN_CODE) {
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
     * Метод чтения параметров фигуры из файла.
     *
     * @param reader     поток ввода на моменте чтения строки с параметрами фигуры (строка с типом
     *                   фигуры уже считана)
     * @param paramLineMaxLen максимальная длина строки с параметрами фигуры
     * @return строковый массив параметров фигуры без приведения типа
     * @throws IOException              ошибка при чтении строки
     * @throws IllegalArgumentException если строка с параметрами не найдена или пустая
     */
    public static String[] readParamsFromLine(BufferedReader reader, int paramLineMaxLen)
            throws IOException, IllegalArgumentException {

        log.debug("Парсинг параметров фигуры из строки");
        log.debug("Максимальная длина строки с параметрами: {}", paramLineMaxLen);

        String paramsLine = readAmountOrEolOrEof(reader, paramLineMaxLen);
        if (paramsLine.isBlank()) {
            throw new IllegalArgumentException("Строка с параметрами фигуры не может быть пустой");
        }

        return paramsLine.split(" ");
    }

}
