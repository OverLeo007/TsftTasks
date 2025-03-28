package ru.shift.figures;

import java.io.BufferedWriter;
import java.io.IOException;
import ru.shift.factory.FigureType;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Circle extends Figure {

    private final static FigureType FIGURE_TYPE = FigureType.CIRCLE;

    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void writeFigureData(BufferedWriter writer) throws IOException {
        var figureData = computeFigureDataStr();
        figureData.append("Радиус: ")
                .append(DECIMAL_FORMAT.format(getRadius())).append(UNITS).append(EOL);
        figureData.append("Диаметр: ")
                .append(DECIMAL_FORMAT.format(computeDiameter())).append(UNITS).append(EOL);
        super.writeFigureData(writer, figureData.toString());
    }
    @Override
    public FigureType getType() {
        return FIGURE_TYPE;
    }

    public double getRadius() {
        return radius;
    }

    public double computeDiameter() {
        return 2 * radius;
    }

    @Override
    public double computePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double computeArea() {
        return Math.PI * radius * radius;
    }


}
