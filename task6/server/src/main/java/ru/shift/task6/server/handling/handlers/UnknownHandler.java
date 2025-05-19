package ru.shift.task6.server.handling.handlers;

import ru.shift.task6.commons.protocol.abstracts.Message;
import ru.shift.task6.commons.protocol.impl.responses.ErrorResponse;
import ru.shift.task6.server.handling.provider.HandlerContext;

public class UnknownHandler implements Handler<Message> {

    @Override
    public void handle(Message request, HandlerContext context) {
        context.errorResponseSender().sendErrorResponse(new ErrorResponse("Unknown message"));
    }
}
