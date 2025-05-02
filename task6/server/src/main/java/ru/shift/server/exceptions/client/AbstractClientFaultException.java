package ru.shift.server.exceptions.client;

abstract public class AbstractClientFaultException extends RuntimeException {

    protected AbstractClientFaultException(String message) {
        super(message);
    }

    protected AbstractClientFaultException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AbstractClientFaultException(Throwable cause) {
        super(cause);
    }

    protected AbstractClientFaultException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected AbstractClientFaultException() {
    }
}
