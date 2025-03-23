package ru.shift.figures;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Figure {

    static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    static final String EOL = System.lineSeparator();
    static final String UNITS = " см";
    static final String SQ_UNITS = UNITS + "²";

    StringBuilder getFigureData() {
        var builder = new StringBuilder();
        builder.append("Тип фигуры: ")
                .append(getType()).append(EOL);
        builder.append("Площадь: ")
                .append(DECIMAL_FORMAT.format(computeArea())).append(SQ_UNITS).append(EOL);
        builder.append("Периметр: ")
                .append(DECIMAL_FORMAT.format(computePerimeter())).append(UNITS).append(EOL);
        return builder;
    }

    void writeFigureData(BufferedWriter writer, String figureData) throws IOException {
        log.debug("Печать данных фигуры в заданный поток вывода");
        writer.write(figureData);
    }

    public abstract void writeFigureData(BufferedWriter writer) throws IOException;

    public abstract FigureType getType();

    public abstract double computePerimeter();

    public abstract double computeArea();

}
