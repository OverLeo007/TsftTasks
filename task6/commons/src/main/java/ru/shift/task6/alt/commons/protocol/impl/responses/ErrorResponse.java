package ru.shift.task6.alt.commons.protocol.impl.responses;

import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;

public class ErrorResponse extends Response {

    public ErrorResponse(String errorMessage) {
        super(generateId(), errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR_RS;
    }
}
