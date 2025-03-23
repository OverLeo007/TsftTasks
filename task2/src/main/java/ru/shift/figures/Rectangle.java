package ru.shift.figures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import ru.shift.cli.MyUtils;

public class Rectangle extends Figure {

    private final static FigureType TYPE = FigureType.RECTANGLE;

    private final double width;
    private final double height;

    private Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public static Rectangle createFromReader(BufferedReader reader) throws IOException {
        var paramStrs = MyUtils.parseParamsFromLine(reader, TYPE);
        try {
            var params = Arrays.stream(paramStrs).mapToDouble(MyUtils::parsePositiveDouble)
                    .toArray();
            return new Rectangle(params[0], params[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("При попытке преобразования аргументов "
                    + "в число произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public void writeFigureData(BufferedWriter writer) throws IOException {
        var figureData = getFigureData();
        figureData.append("Диагональ: ")
                .append(DECIMAL_FORMAT.format(computeDiagonal())).append(UNITS).append(EOL);
        figureData.append("Длина: ")
                .append(DECIMAL_FORMAT.format(getLength())).append(UNITS).append(EOL);
        figureData.append("Ширина: ")
                .append(DECIMAL_FORMAT.format(getWidth())).append(UNITS).append(EOL);
        super.writeFigureData(writer, figureData.toString());
    }

    @Override
    public FigureType getType() {
        return TYPE;
    }

    @Override
    public double computePerimeter() {
        return 2 * (width + height);
    }

    @Override
    public double computeArea() {
        return width * height;
    }

    public double computeDiagonal() {
        return Math.sqrt(width * width + height * height);
    }

    public double getWidth() {
        return Math.max(width, height);
    }

    public double getLength() {
        return Math.min(width, height);
    }


}
