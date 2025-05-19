package ru.shift.task6.commons.protocol.impl.responses;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Response;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse extends Response {
    private UserInfo user;

    public AuthResponse(String id, UserInfo user) {
        super(id);
        this.user = user;
    }

    public AuthResponse(@NonNull String id, String errorMessage) {
        super(id, errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.AUTH_RS;
    }

}
