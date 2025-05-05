package ru.shift.task6.server.handling.handlers;

import lombok.RequiredArgsConstructor;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.ChatMessage;
import ru.shift.task6.server.handling.handlers.annotations.BroadcastResponse;
import ru.shift.task6.server.handling.handlers.annotations.Handler;
import ru.shift.task6.server.handling.handlers.annotations.RequestType;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.exceptions.client.ForbiddenException;

@Handler
@RequiredArgsConstructor
public class MessageHandler {

    private final ClientContext context;

    @RequestType(type = PayloadType.MESSAGE)
    @BroadcastResponse(PayloadType.MESSAGE)
    private ChatMessage handleMessage(ChatMessage chatMessage) {
        context.checkAuthorized("Нельзя отправить сообщения без ввода имени");
        context.checkJoined("Перед отправкой сообщения необходимо присоединиться к чату");
        if (!chatMessage.getSender().getNickname().equals(context.getUser().getNickname())) {
            throw new ForbiddenException("Нельзя отправлять сообщения от чужого имени");
        }
        return chatMessage;
    }


}
