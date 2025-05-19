package ru.shift.task6.server.handling.handlers;


import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.protocol.impl.requests.JoinRequest;
import ru.shift.task6.server.handling.provider.HandlerContext;

@Slf4j
public class JoinRequestHandler implements Handler<JoinRequest> {

    @Override
    public void handle(JoinRequest request, HandlerContext context) {
        log.info("User wants to join chat");
        context.clientContext()
                .checkAuthorized("Перед тем как войти в чат, необходимо ввести никнейм");
        context.clientContext().setJoined(true);
        context.responseSender()
                .sendResponse(request.success());
        context.broadcaster()
                .broadcast(request.notification(context.clientContext().getUser()));
    }
}
