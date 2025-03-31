package ru.shift.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import ru.shift.figures.Rectangle;
import ru.shift.utils.InputUtils;
import ru.shift.utils.ParseUtils;

@Slf4j
public class RectangleFactory extends FigureFactory<Rectangle> {

    private static final int PARAMS_COUNT = 2;

    private static final int PARAM_LINE_MAX_LEN =
            PARAMS_COUNT * String.valueOf(Double.MAX_VALUE).length();

    @Override
    public FigureType getType() {
        return FigureType.RECTANGLE;
    }

    @Override
    public Rectangle createFigure(BufferedReader reader) throws IOException {
        log.debug("Создание фигуры {}", getType());
        try {
            var paramStrs = InputUtils.readParamsFromLine(reader, PARAM_LINE_MAX_LEN);
            validateParamsCount(paramStrs);
            var params = Arrays.stream(paramStrs).mapToDouble(ParseUtils::parsePositiveDouble)
                    .toArray();
            return new Rectangle(params[0], params[1]);
        } catch (IllegalArgumentException e) {
            log.error("При создании {} произошла ошибка: {}", getType(), e.getMessage());
            log.debug("Подробности ошибки: ", e);
            throw e;
        }
    }

    @Override
    public int getParamsCount() {
        return PARAMS_COUNT;
    }
}
