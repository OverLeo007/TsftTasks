package ru.shift.factory;

import lombok.Getter;

@Getter
public enum FigureType {
    CIRCLE,
    RECTANGLE,
    TRIANGLE;
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
        MAX_NAME_LINE_LEN = maxNameLen + System.lineSeparator().length();
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
