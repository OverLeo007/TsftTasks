package ru.shift.task6.alt.commons.protocol.abstracts;

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
