package ru.shift.figures;


import java.io.BufferedReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.shift.cli.MyUtils;

@Slf4j
public class FigureFactory {

    public static Figure createFigureFromFile(BufferedReader reader)
            throws IOException, IllegalArgumentException {
        log.debug("Создание фигуры");
        try {
            var figureType = FigureType.fromString(MyUtils.readFigType(reader));
            log.debug("Тип фигуры: {}", figureType);
            var figureParamStrs = MyUtils.readParamsFromLine(reader, figureType);
            return switch (figureType) {
                case CIRCLE -> Circle.createFromReader(figureParamStrs);
                case RECTANGLE -> Rectangle.createFromReader(figureParamStrs);
                case TRIANGLE -> Triangle.createFromReader(figureParamStrs);
            };
        } catch (IllegalArgumentException e) {
            log.error("При создании фигуры произошла ошибка: {}", e.getMessage());
            log.debug("Подробности ошибки: ", e);
            throw e;
        }
    }
}
