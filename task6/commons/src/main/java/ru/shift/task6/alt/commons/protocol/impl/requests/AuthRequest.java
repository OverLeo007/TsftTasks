package ru.shift.task6.alt.commons.protocol.impl.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.UserInfo;
import ru.shift.task6.alt.commons.protocol.abstracts.Request;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;
import ru.shift.task6.alt.commons.protocol.impl.responses.AuthResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest extends Request {
    private UserInfo user;

    public AuthRequest(UserInfo user) {
        this.user = user;
    }

    @Override
    public MessageType getType() {
        return MessageType.AUTH_RQ;
    }

    public Response success(UserInfo user) {
        return new AuthResponse(id, user);
    }

    @Override
    public Response error(String message) {
        return new AuthResponse(id, message);
    }
}
