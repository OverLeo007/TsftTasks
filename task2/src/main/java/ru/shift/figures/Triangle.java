package ru.shift.figures;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import ru.shift.cli.MyUtils;
import ru.shift.exceptions.IllegalTriangleSidesLenException;

@Slf4j
public class Triangle extends Figure {

    private static final String DEG = "°";
    private static final String DELIMITER = " - ";

    private final static FigureType FIGURE_TYPE = FigureType.TRIANGLE;

    private final double AB;
    private final double ABSq;

    private final double BC;
    private final double BCSq;

    private final double AC;
    private final double ACSq;

    public static Triangle createFromReader(String[] paramStrs) {
        try {
            var params = Arrays.stream(paramStrs).mapToDouble(MyUtils::parsePositiveDouble)
                    .toArray();
            return new Triangle(params[0], params[1], params[2]);
        } catch (IllegalTriangleSidesLenException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("При попытке преобразования аргументов "
                    + "в число произошла ошибка: " + e.getMessage());
        }
    }

    private Triangle(double AB, double BC, double AC) {
        if (!((AB + BC > AC) && (AB + AC > BC) && (BC + AC > AB))) {
            throw new IllegalTriangleSidesLenException(
                    "Треугольник со сторонами %.2f %.2f %.2f невозможен".formatted(AB, BC, AC)
            );
        }
        this.AB = AB;
        this.ABSq = AB * AB;

        this.BC = BC;
        this.BCSq = BC * BC;

        this.AC = AC;
        this.ACSq = AC * AC;
    }

    @Override
    public void writeFigureData(BufferedWriter writer) throws IOException {
        /*
         * AB - AC^BC
         * BC - AB^AC
         * AC - AB^BC
         */
        var figureData = computeFigureDataStr();
        figureData.append("Сторона - Длина - Противолежащий угол: ").append(EOL);
        figureData
                .append("AB")
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(AB)).append(UNITS)
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(computeBcAcAngle())).append(DEG)
                .append(EOL);
        figureData
                .append("BC")
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(BC)).append(UNITS)
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(computeAbAcAngle())).append(DEG)
                .append(EOL);
        figureData
                .append("AC")
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(AC)).append(UNITS)
                .append(DELIMITER)
                .append(DECIMAL_FORMAT.format(computeAbBcAngle())).append(DEG)
                .append(EOL);
        super.writeFigureData(writer, figureData.toString());
    }

    @Override
    public FigureType getType() {
        return FIGURE_TYPE;
    }

    @Override
    public double computePerimeter() {
        return AB + BC + AC;
    }

    @Override
    public double computeArea() {
        // Формула Герона
        double s = (AB + BC + AC) / 2; // Полупериметр
        return Math.sqrt(s * (s - AB) * (s - BC) * (s - AC));
    }

    public double computeAbBcAngle() {
        return Math.toDegrees(Math.acos((ABSq + BCSq - ACSq) / (2 * AB * BC)));
    }

    public double computeBcAcAngle() {
        return Math.toDegrees(Math.acos((BCSq + ACSq - ABSq) / (2 * BC * AC)));
    }

    public double computeAbAcAngle() {
        return Math.toDegrees(Math.acos((ACSq + ABSq - BCSq) / (2 * AC * AB)));
    }

}
