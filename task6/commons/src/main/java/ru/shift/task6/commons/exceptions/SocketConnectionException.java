package ru.shift.task6.commons.exceptions;

public class SocketConnectionException extends RuntimeException {

    public SocketConnectionException(String message) {
        super(message);
    }

    public SocketConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
