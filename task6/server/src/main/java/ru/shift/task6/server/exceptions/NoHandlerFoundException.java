package ru.shift.task6.server.exceptions;

public class NoHandlerFoundException extends RuntimeException {

    public NoHandlerFoundException(String message) {
        super(message);
    }
}
