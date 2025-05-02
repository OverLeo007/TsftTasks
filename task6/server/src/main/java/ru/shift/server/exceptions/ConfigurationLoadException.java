package ru.shift.server.exceptions;

public class ConfigurationLoadException extends RuntimeException {

    public ConfigurationLoadException(String message) {
        super(message);
    }

    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
