package ru.shift.task6.server.exceptions.client;

public class UserRegistrationException extends AbstractClientFaultException {

    public UserRegistrationException(String message) {
        super(message);
    }
}
