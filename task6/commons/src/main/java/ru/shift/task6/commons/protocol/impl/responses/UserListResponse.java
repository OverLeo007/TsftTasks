package ru.shift.task6.commons.protocol.impl.responses;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Response;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserListResponse extends Response {
    private List<UserInfo> usersOnline;

    public UserListResponse(@NonNull String id, List<UserInfo> usersOnline) {
        super(id);
        this.usersOnline = usersOnline;
    }

    public UserListResponse(@NonNull String id, String errorMessage) {
        super(id, errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.USER_LIST_RS;
    }
}
