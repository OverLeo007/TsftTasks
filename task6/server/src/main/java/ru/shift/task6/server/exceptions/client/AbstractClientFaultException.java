package ru.shift.task6.server.exceptions.client;

abstract public class AbstractClientFaultException extends RuntimeException {

    protected AbstractClientFaultException(String message) {
        super(message);
    }
}
