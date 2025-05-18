package ru.shift.task6.server.handling.handlers;

import ru.shift.task6.alt.commons.protocol.impl.requests.MessageRequest;
import ru.shift.task6.server.exceptions.client.ForbiddenException;
import ru.shift.task6.server.handling.provider.HandlerContext;

public class MessageRequestHandler implements Handler<MessageRequest> {

    @Override
    public void handle(MessageRequest request, HandlerContext context) {
        context.clientContext().checkAuthorized("Нельзя отправить сообщения без ввода имени");
        context.clientContext()
                .checkJoined("Перед отправкой сообщения необходимо присоединиться к чату");
        if (!request.getMessage().getSender().getNickname()
                .equals(context.clientContext().getUser().getNickname())) {
            throw new ForbiddenException("Нельзя отправлять сообщения от чужого имени");
        }
        context.responseSender().sendResponse(request.success());
        context.broadcaster().broadcast(request.notification());
    }
}
