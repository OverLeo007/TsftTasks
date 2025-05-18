package ru.shift.task6.alt.commons.protocol.impl.requests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.abstracts.Request;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;
import ru.shift.task6.alt.commons.protocol.impl.responses.JoinResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinRequest extends Request {

    @Override
    public MessageType getType() {
        return MessageType.JOIN_RQ;
    }

    public Response success() {
        return new JoinResponse(id);
    }

    @Override
    public Response error(String message) {
        return new JoinResponse(id, message);
    }
}
