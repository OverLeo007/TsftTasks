package ru.shift.task6.commons.protocol.abstracts;

public abstract class Notification extends AbstractMessageImpl {

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return false;
    }
}
