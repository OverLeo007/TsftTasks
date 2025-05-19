package ru.shift.task6.commons.protocol.impl.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.ChatMessage;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.abstracts.Notification;
import ru.shift.task6.commons.protocol.abstracts.Request;
import ru.shift.task6.commons.protocol.abstracts.Response;
import ru.shift.task6.commons.protocol.impl.notifications.MessageNotification;
import ru.shift.task6.commons.protocol.impl.responses.MessageResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MessageRequest extends Request {
    private ChatMessage message;

    public MessageRequest(ChatMessage message) {
        this.message = message;
    }

    public Response success() {
        return new MessageResponse(id);
    }

    public Notification notification() {
        return new MessageNotification(message);
    }

    @Override
    public Response error(String message) {
        return new MessageResponse(id, message);
    }

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE_RQ;
    }
}
