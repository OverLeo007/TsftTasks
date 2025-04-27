package ru.shift.task6;

import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.config.Config;
import ru.shift.task6.config.RunProperties;
import ru.shift.task6.exceptions.ConfigurationLoadException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Загрузка конфигурации...");
        val properties = loadConfig();
        val port = properties.getServer().getPort();
        log.info("Запуск сервера на порту: {}", port);
        startExitListener();
        new Server(properties).start();
    }

    private static RunProperties loadConfig() {
        try {
            return Config.loadProperties();
        } catch (ConfigurationLoadException e) {
            log.error("Ошибка загрузки конфигурации: {}", e.getMessage());
            System.exit(1);
            throw new IllegalStateException("Unreachable");
        } catch (Exception e) {
            log.error("Неизвестная ошибка: ", e);
            System.exit(1);
            throw new IllegalStateException("Unreachable");
        }
    }

    private static void startExitListener() {
        Thread exitThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command.trim())) {
                    log.info("Команда exit получена. Завершение работы сервера...");
                    System.exit(0);
                }
            }
        });
        exitThread.setName("ServerExitListener");
        exitThread.setDaemon(true);
        exitThread.start();
    }

}