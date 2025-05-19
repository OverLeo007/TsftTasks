package ru.shift.task6.client.exceptions;

public class ResponseException extends RuntimeException {

    public ResponseException(String errorMessage) {
        super(errorMessage, null, false, false);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
