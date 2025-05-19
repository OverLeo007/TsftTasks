package ru.shift.task6.commons.protocol.impl.requests;

import java.util.List;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Request;
import ru.shift.task6.commons.protocol.abstracts.Response;
import ru.shift.task6.commons.protocol.impl.responses.UserListResponse;

@NoArgsConstructor
public class UserListRequest extends Request {

    public Response success(List<UserInfo> usersOnline) {
        return new UserListResponse(id, usersOnline);
    }

    @Override
    public Response error(String message) {
        return new UserListResponse(id, message);
    }

    @Override
    public MessageType getType() {
        return MessageType.USER_LIST_RQ;
    }
}
