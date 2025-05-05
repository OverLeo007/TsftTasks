package ru.shift.task6.server.exceptions.client;

public class UnauthorizedException extends AbstractClientFaultException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
