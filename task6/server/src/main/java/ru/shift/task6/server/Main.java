package ru.shift.task6.server;

import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.server.config.Config;
import ru.shift.task6.server.config.RunProperties;
import ru.shift.task6.server.exceptions.ConfigurationLoadException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Loading configuration...");
        val properties = loadConfig();
        val port = properties.getServer().getPort();
        log.info("Starting server at port: {}", port);
        startExitListener();
        new Server(properties).start();
    }

    private static RunProperties loadConfig() {
        try {
            return Config.loadProperties();
        } catch (ConfigurationLoadException e) {
            log.error("Error while loading configuration: {}", e.getMessage());
            System.exit(1);
            throw new IllegalStateException("Unreachable");
        } catch (Exception e) {
            log.error("Unknown error: ", e);
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
                    log.info("Exit command handled. Shutting down...");
                    System.exit(0);
                }
            }
        });
        exitThread.setName("ServerExitListener");
        exitThread.setDaemon(true);
        exitThread.start();
    }

}