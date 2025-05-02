package ru.shift.server.exceptions.client;

public class ForbiddenException extends AbstractClientFaultException {

    public ForbiddenException(String message) {
        super(message);
    }
}
