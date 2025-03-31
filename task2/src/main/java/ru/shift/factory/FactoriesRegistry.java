package ru.shift.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.shift.utils.InputUtils;

@Slf4j
public class FactoriesRegistry {

    private static final Map<FigureType, FigureFactory<?>> factories;

    static {
        factories = ServiceLoader.load(FigureFactory.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(FigureFactory::getType, f -> (FigureFactory<?>) f));
    }

    public static FigureFactory<?> getFactory(BufferedReader reader) throws IOException {
        try {
            var type = FigureType.fromString(InputUtils.readFigType(reader));
            return Optional.ofNullable(factories.get(type))
                    .orElseThrow(() -> new IllegalArgumentException("Неизвестный тип фигуры"));
        } catch (IllegalArgumentException e) {
            log.error("При чтении данных из файла произошла ошибка: {}", e.getMessage());
            throw e;
        }
    }

}
