package ru.shift.task6.commons.protocol.impl.responses;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.abstracts.Response;

@NoArgsConstructor
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
