package ru.shift.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LogControl {
    public static void disableLogsForClass(Class<?> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);
        logger.setLevel(Level.OFF);
    }

    public static void enableLogsForClass(Class<?> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);

        logger.setLevel(Level.toLevel(System.getProperty("log.level", "INFO")));
    }
}
