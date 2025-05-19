package ru.shift.task6.commons.protocol.impl.requests;

import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Notification;
import ru.shift.task6.commons.protocol.abstracts.Request;
import ru.shift.task6.commons.protocol.abstracts.Response;
import ru.shift.task6.commons.protocol.impl.notifications.JoinNotification;
import ru.shift.task6.commons.protocol.impl.responses.JoinResponse;

@NoArgsConstructor
public class JoinRequest extends Request {

    @Override
    public MessageType getType() {
        return MessageType.JOIN_RQ;
    }

    public Response success() {
        return new JoinResponse(id);
    }

    public Notification notification(UserInfo joinedUser) {
        return new JoinNotification(joinedUser);
    }

    @Override
    public Response error(String message) {
        return new JoinResponse(id, message);
    }
}
