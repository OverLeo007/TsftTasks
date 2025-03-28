package ru.shift.factory;

import java.io.BufferedReader;
import java.io.IOException;
import ru.shift.figures.Figure;

public abstract class FigureFactory<T extends Figure> {

    public abstract FigureType getType();

    public abstract T createFigure(BufferedReader reader) throws IOException;

    void validateParamsCount(Object[] params) {
        if (params.length != getParamsCount()) {
            throw new IllegalArgumentException(
                    String.format("%s должен иметь %d параметров", getType(), getParamsCount()));
        }
    }

    public abstract int getParamsCount();

}
