package ru.shift.task6.server.handling.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.protocol.impl.requests.UserListRequest;
import ru.shift.task6.server.handling.provider.HandlerContext;

@Slf4j
public class UserListRequestHandler implements Handler<UserListRequest> {

    @Override
    public void handle(UserListRequest request, HandlerContext context) {
        log.info("User wants to get active users list");
        context.clientContext()
                .checkAuthorized("Для совершения этого запроса необходимо ввести никнейм");
        context.clientContext()
                .checkJoined("Вы должны быть в чате чтобы получить список пользователей.");
        context.responseSender().sendResponse(request.success(context.service().getAllUsers()));
    }
}
