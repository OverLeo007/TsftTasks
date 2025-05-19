package ru.shift.task6.commons.channel;

public class SocketConnectionException extends RuntimeException {

    public SocketConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
