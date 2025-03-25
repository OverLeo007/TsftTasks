package ru.shift;

import picocli.CommandLine;
import ru.shift.cli.CliApp;

/**
 * Программа печати характеристик геометрических фигур полученных из файла
 * в заданный поток вывода.
 *
 * @author Lev Sokolov
 * @version 1.0
 * @since 2025.20.03
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(
                "Уровень логирования (изменяется параметром JVM -Dlog.level={level-name}): "
                        + System.getProperty("log.level"));
        int exitCode = new CommandLine(new CliApp()).execute(args);
        System.exit(exitCode);
    }
}
