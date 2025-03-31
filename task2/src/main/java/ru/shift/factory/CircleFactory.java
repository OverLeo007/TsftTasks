package ru.shift.factory;

import java.io.BufferedReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.shift.figures.Circle;
import ru.shift.utils.InputUtils;
import ru.shift.utils.ParseUtils;

@Slf4j
public class CircleFactory extends FigureFactory<Circle> {

    private static final int PARAMS_COUNT = 1;

    private static final int PARAM_LINE_MAX_LEN =
            PARAMS_COUNT * String.valueOf(Double.MAX_VALUE).length();

    @Override
    public FigureType getType() {
        return FigureType.CIRCLE;
    }

    @Override
    public Circle createFigure(BufferedReader reader) throws IOException {
        log.debug("Создание фигуры {}", getType());
        try {
            var params = InputUtils.readParamsFromLine(reader, PARAM_LINE_MAX_LEN);
            validateParamsCount(params);
            return new Circle(ParseUtils.parsePositiveDouble(params[0]));
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
