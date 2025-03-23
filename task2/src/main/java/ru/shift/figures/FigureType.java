package ru.shift.figures;

public enum FigureType {
    CIRCLE(1),
    RECTANGLE(2),
    TRIANGLE(3);

    private final int expectedParams;

    FigureType(int expectedParams) {
        this.expectedParams = expectedParams;
    }

    public void validateParamCount(Object[] params) {
        if (params.length != expectedParams) {
            throw new IllegalArgumentException(
                    String.format("%s должен иметь %d параметров", this, expectedParams));
        }
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
