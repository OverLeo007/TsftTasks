package ru.shift.figures;

import java.io.BufferedWriter;
import java.io.IOException;
import ru.shift.factory.FigureType;

public class Rectangle extends Figure {

    private final static FigureType FIGURE_TYPE = FigureType.RECTANGLE;

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void writeFigureData(BufferedWriter writer) throws IOException {
        var figureData = computeFigureDataStr();
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
        return FIGURE_TYPE;
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
        return Math.min(width, height);
    }

    public double getLength() {
        return Math.max(width, height);
    }
}
