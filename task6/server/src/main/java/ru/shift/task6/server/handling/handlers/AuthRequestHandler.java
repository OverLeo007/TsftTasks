package ru.shift.task6.server.handling.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.UserInfo;
import ru.shift.task6.alt.commons.protocol.impl.requests.AuthRequest;
import ru.shift.task6.server.exceptions.client.UserRegistrationException;
import ru.shift.task6.server.handling.provider.HandlerContext;

@Slf4j
public class AuthRequestHandler implements Handler<AuthRequest> {

    @Override
    public void handle(AuthRequest request, HandlerContext context) {
        var newUser = request.getUser();
        log.info("New auth request from {}", newUser.getNickname());
        validateNewUser(newUser);
        try {
            var addedUser = context.service().addClient(newUser, context.clientContext());
            context.clientContext()
                    .setUser(addedUser);
            context.responseSender().sendResponse(request.success(addedUser));
        } catch (IllegalStateException e)  {
            throw new UserRegistrationException("Имя пользователя уже занято");
        }

    }
    private void validateNewUser(UserInfo newUser) {
        if (newUser.getNickname() == null || newUser.getNickname().isBlank()) {
            throw new UserRegistrationException("Имя пользователя не может быть пустым");
        } else {
            final var nameF = newUser.getNickname().strip();
            if (nameF.length() > 20 || nameF.length() < 3) {
                throw new UserRegistrationException(
                        "Имя пользователя должно быть от 3 до 20 символов в длину");
            }
        }
    }
}
