package ru.shift.task6.commons.protocol.abstracts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Request extends AbstractMessageImpl {

    public abstract Response error(String message);

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return false;
    }
}
