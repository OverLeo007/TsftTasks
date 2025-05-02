package ru.shift.server.exceptions;

public class NoHandlerFoundException extends RuntimeException {

    public NoHandlerFoundException(String message) {
        super(message);
    }
}
