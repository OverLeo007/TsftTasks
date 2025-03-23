package ru.shift.figures;


import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FigureFactory {

    public static Figure createFigureFromFile(BufferedReader reader)
            throws IOException, IllegalArgumentException {
        log.debug("Создание фигуры");
        try {
            var figureType = FigureType.fromString(readFigType(reader));
            log.debug("Тип фигуры: {}", figureType);
            return switch (figureType) {
                case CIRCLE -> Circle.createFromReader(reader);
                case RECTANGLE -> Rectangle.createFromReader(reader);
                case TRIANGLE -> Triangle.createFromReader(reader);
            };
        } catch (IllegalArgumentException e) {
            log.error("При создании фигуры произошла ошибка: {}", e.getMessage());
            log.debug("Подробности ошибки: ", e);
            throw e;
        }

    }

    private static String readFigType(BufferedReader reader) throws IOException {
        log.debug("Чтение типа фигуры из файла");
        var figureTypeLine = reader.readLine();
        if (figureTypeLine == null) {
            throw new EOFException("Файл пуст");
        }
        figureTypeLine = figureTypeLine.trim();
        if (figureTypeLine.isBlank()) {
            throw new IllegalArgumentException("Название фигуры не может быть пустым");
        }
        return figureTypeLine;
    }
}
