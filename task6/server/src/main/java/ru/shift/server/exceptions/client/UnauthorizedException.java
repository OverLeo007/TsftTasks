package ru.shift.server.exceptions.client;

public class UnauthorizedException extends AbstractClientFaultException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
