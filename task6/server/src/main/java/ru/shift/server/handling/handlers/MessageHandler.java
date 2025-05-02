package ru.shift.server.handling.handlers;

import lombok.RequiredArgsConstructor;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.ChatMessage;
import ru.shift.server.handling.handlers.annotations.BroadcastResponse;
import ru.shift.server.handling.handlers.annotations.Handler;
import ru.shift.server.handling.handlers.annotations.RequestType;
import ru.shift.server.client.ClientContext;
import ru.shift.server.exceptions.client.ForbiddenException;

@Handler
@RequiredArgsConstructor
public class MessageHandler {

    private final ClientContext context;

    @RequestType(type = PayloadType.MESSAGE)
    @BroadcastResponse(PayloadType.MESSAGE)
    private ChatMessage handleMessage(ChatMessage chatMessage) {
        context.checkAuthorized("Нельзя отправить сообщения без ввода имени");
        if (!chatMessage.getSender().getNickname().equals(context.getUser().getNickname())) {
            throw new ForbiddenException("Нельзя отправлять сообщения от чужого имени");
        }
        return chatMessage;
    }


}
