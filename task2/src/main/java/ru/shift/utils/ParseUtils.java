package ru.shift.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ParseUtils {

    private ParseUtils() {
        throw new UnsupportedOperationException("Класс утилит не может быть создан");
    }

    public static double parsePositiveDouble(String str) throws IllegalArgumentException {
        log.trace("Попытка преобразования \"{}\" в положительное число", str);
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("Строка не может быть пустой");
        }
        try {
            double value = Double.parseDouble(str);
            if (value < 1) {
                throw new IllegalArgumentException("Значение должно быть >= 1");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значение должно быть числом, но получено: \"%s\"".formatted(str));
        }
    }
}
