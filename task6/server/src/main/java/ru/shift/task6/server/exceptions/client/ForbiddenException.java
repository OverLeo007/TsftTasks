package ru.shift.task6.server.exceptions.client;

public class ForbiddenException extends AbstractClientFaultException {

    public ForbiddenException(String message) {
        super(message);
    }
}
