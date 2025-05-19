package ru.shift.task6.commons.protocol.impl.responses;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.abstracts.Response;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageResponse extends Response {

    public MessageResponse(@NonNull String id) {
        super(id);
    }

    public MessageResponse(@NonNull String id, String errorMessage) {
        super(id, errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE_RS;
    }
}
