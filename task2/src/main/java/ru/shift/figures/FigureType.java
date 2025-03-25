package ru.shift.figures;

import lombok.Getter;
import ru.shift.cli.MyUtils;

@Getter
public enum FigureType {
    CIRCLE(1),
    RECTANGLE(2),
    TRIANGLE(3);

    /* Для каждой фигуры длина может меняться в зависимости
    от требований к параметрам (см FigureType.getFigureParamLineMaxLenFigureType type) */
    public static final int CIRCLE_PARAM_MAX_LINE_LEN =
            FigureType.CIRCLE.expectedParamsCount * String.valueOf(Double.MAX_VALUE).length();

    public static final int RECTANGLE_PARAM_LINE_LEN =
            FigureType.RECTANGLE.expectedParamsCount * String.valueOf(Double.MAX_VALUE).length();

    public static final int TRIANGLE_PARAM_LINE_LEN =
            FigureType.TRIANGLE.expectedParamsCount * String.valueOf(Double.MAX_VALUE).length();

    private final int expectedParamsCount;

    /**
     * Максимальная длина строки с названием
     * фигуры с учетом символа(ов) перевода строки
     */
    public static final int MAX_NAME_LINE_LEN;

    static {
        int maxNameLen = 0;
        for (FigureType type : values()) {
            maxNameLen = Math.max(maxNameLen, type.toString().length());
        }
        MAX_NAME_LINE_LEN = maxNameLen + MyUtils.EOL.length();
    }

    FigureType(int expectedParamsCount) {
        this.expectedParamsCount = expectedParamsCount;
    }

    public void validateParamCount(Object[] params) {
        if (params.length != expectedParamsCount) {
            throw new IllegalArgumentException(
                    String.format("%s должен иметь %d параметров", this, expectedParamsCount));
        }
    }

    public int getParamLineMaxLen() {
        return switch (this) {
            case CIRCLE -> CIRCLE_PARAM_MAX_LINE_LEN;
            case RECTANGLE -> RECTANGLE_PARAM_LINE_LEN;
            case TRIANGLE -> TRIANGLE_PARAM_LINE_LEN;
        };
    }

    public static FigureType fromString(String type) {
        return switch (type) {
            case "CIRCLE" -> CIRCLE;
            case "RECTANGLE" -> RECTANGLE;
            case "TRIANGLE" -> TRIANGLE;
            default -> throw new IllegalArgumentException("Неизвестный тип фигуры: " + type);
        };
    }

}
