package ru.shift.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.shift.factory.FactoriesRegistry;
import ru.shift.factory.FigureFactory;
import ru.shift.figures.Figure;
import ru.shift.utils.FileSystemUtils;

@Command(
        name = "Фигуры",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description =
                "Консольное приложение с объектно-ориентированной архитектурой, выводящее "
                        + "характеристики заданной геометрической фигуры")
@Slf4j
@SuppressWarnings("unused")
public class CliApp implements Runnable {

    @Option(
            names = {"-i", "--input"},
            required = true,
            description = "Путь до входного файла")
    private String inputFilePath;

    @ArgGroup(multiplicity = "1")
    Exclusive outputType;

    static class Exclusive {

        @Option(
                names = {"-o", "--output"},
                description = "Имя выходного файла")
        private String outputFileName;

        @Option(
                names = {"-co", "--console-output"},
                description = "Вывод в консоль")
        private boolean consoleOutput;
    }

    private boolean validateParameters() {
        log.debug("Проверка параметров входного файла и выходного файла");
        if (outputType.outputFileName != null && outputType.outputFileName.startsWith("-")) {
            log.error("Некорректное значение для пути выходного файла");
            return false;
        }
        if (inputFilePath.startsWith("-")) {
            log.error("Некорректное значение для пути входного файла");
            return false;
        }
        return true;
    }


    private BufferedWriter createWriter() throws IOException {
        log.debug("Создание потока вывода");
        log.debug("Тип потока: {}", outputType.consoleOutput ? "console" : "file");
        return new BufferedWriter(
                new OutputStreamWriter(
                        outputType.consoleOutput
                                ? System.out
                                : new FileOutputStream(outputType.outputFileName),
                        StandardCharsets.UTF_8
                )
        );
    }

    private BufferedReader createReader() throws IOException {
        log.debug("Создание потока ввода");
        return new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFilePath),
                        StandardCharsets.UTF_8
                )
        );
    }


    @Override
    public void run() {
        if (!validateParameters()) {
            return;
        }

        try {
            FileSystemUtils.createDirForFileIfNotExist(outputType.outputFileName);
            BufferedWriter writer = createWriter();
            try (
                    BufferedReader reader = createReader()
            ) {
                log.info("Создание фигуры из данных файла {}", inputFilePath);
                FigureFactory<?> factory = FactoriesRegistry.getFactory(reader);
                Figure figure = factory.createFigure(reader);
                log.info("Фигура {} создана, печать значений...", figure.getType());
                figure.writeFigureData(writer);
            } finally {
                if (!outputType.consoleOutput) {
                    log.debug("Закрытие потока вывода");
                    writer.close();
                } else {
                    /* Только при использовании консольного вывода
                       так как close() и так вызывает flush(),
                       а нам не нужно закрывать поток, но нужно его очистить */
                    log.debug("Очистка буфера консольного вывода");
                    writer.flush();
                }
            }

        } catch (IOException | InvalidPathException e) {
            log.error(
                    "При работе с файлами произошла ошибка {}: {}",
                    e.getClass().getSimpleName(),
                    e.getMessage());
            log.debug("Подробности ошибки", e);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            log.error("Работа программы не возможна из-за "
                    + "некорректного содержимого входного файла");
            log.debug("Подробности ошибки", e);
            System.exit(2);
        }
        log.info("Работа программы завершена успешно");

    }
}
