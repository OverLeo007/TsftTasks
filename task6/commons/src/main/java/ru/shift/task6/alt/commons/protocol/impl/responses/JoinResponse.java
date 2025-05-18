package ru.shift.task6.alt.commons.protocol.impl.responses;

import lombok.NonNull;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;

public class JoinResponse extends Response {

    public JoinResponse(@NonNull String id) {
        super(id);
    }

    public JoinResponse(@NonNull String id, String errorMessage) {
        super(id, errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_RS;
    }
}
