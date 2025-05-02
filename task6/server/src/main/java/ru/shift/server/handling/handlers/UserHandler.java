package ru.shift.server.handling.handlers;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.ShutdownNotice;
import ru.shift.commons.models.payload.UserInfo;
import ru.shift.commons.models.payload.requests.AuthRequest;
import ru.shift.commons.models.payload.requests.JoinRequest;
import ru.shift.commons.models.payload.requests.UserListRequest;
import ru.shift.commons.models.payload.responses.JoinResponse;
import ru.shift.commons.models.payload.responses.LeaveResponse;
import ru.shift.commons.models.payload.responses.SuccessAuthResponse;
import ru.shift.commons.models.payload.responses.UserListResponse;
import ru.shift.server.handling.handlers.annotations.BroadcastResponse;
import ru.shift.server.handling.handlers.annotations.Handler;
import ru.shift.server.handling.handlers.annotations.RequestType;
import ru.shift.server.handling.handlers.annotations.ResponseType;
import ru.shift.server.client.ClientContext;
import ru.shift.server.services.ClientService;
import ru.shift.server.exceptions.client.UserRegistrationException;

@Handler
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final ClientContext context;
    private final ClientService service;

    @RequestType(
            type = PayloadType.AUTH,
            accessLevel = Envelope.class
    )
    @ResponseType(PayloadType.SUCCESS)
    private SuccessAuthResponse handleAuthRequest(Envelope<AuthRequest> envelope) {
        UserInfo newUser = envelope.getPayload().getUser();
        log.info("New auth request from {}", newUser.getNickname());
        if (newUser.getNickname() == null || newUser.getNickname().isBlank()) {
            throw new UserRegistrationException("Имя пользователя не может быть пустым");
        } else {
            val nameF = newUser.getNickname().strip();
            if (nameF.length() > 20 || nameF.length() < 3) {
                throw new UserRegistrationException("Имя пользователя должно быть от 3 до 20 символов в длину");
            }
        }
        if (service.contains(newUser)) {
            throw new UserRegistrationException("Имя пользователя уже занято");
        }
        context.setUser(service.addClient(newUser, context));
        return new SuccessAuthResponse(context.getUser());
    }

    @RequestType(type = PayloadType.JOIN_RQ)
    @BroadcastResponse(PayloadType.JOIN_RS)
    private JoinResponse handleJoinRequest(JoinRequest request) {
        log.info("User wants to join chat");
        context.checkAuthorized("Перед тем как войти в чат, необходимо ввести никнейм");
        context.setJoined(true);
        return new JoinResponse(context.getUser());
    }

    @RequestType(type = PayloadType.USER_LIST_RQ)
    @ResponseType(PayloadType.USER_LIST_RS)
    private UserListResponse handleUserListRequest(UserListRequest request) {
        log.info("User wants to get active users list");
        context.checkAuthorized("Для совершения этого запроса необходимо ввести никнейм");
        return new UserListResponse(service.getAllUsers());
    }

    @RequestType(type = PayloadType.SHUTDOWN)
    @BroadcastResponse(PayloadType.LEAVE_RS)
    private LeaveResponse handleClientDisconnection(ShutdownNotice notice) throws IOException {
        log.info("Client disconnected");
        if (!context.isAuthorized()) {
            context.close();
            return null;
        }
        service.removeClient(context.getUser());
        if (!context.isJoined()) {
            return null;
        }
        return new LeaveResponse(context.getUser());
    }
}
