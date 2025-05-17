package ru.shift.task6.server.handling.provider;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.ChatMessage;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.commons.models.payload.requests.AuthRequest;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.task6.commons.models.payload.responses.JoinNotification;
import ru.shift.task6.commons.models.payload.responses.JoinResponse;
import ru.shift.task6.commons.models.payload.responses.SuccessAuthResponse;
import ru.shift.task6.commons.models.payload.responses.UserListResponse;
import ru.shift.task6.server.exceptions.NoHandlerFoundException;
import ru.shift.task6.server.exceptions.client.AbstractClientFaultException;
import ru.shift.task6.server.exceptions.client.ForbiddenException;
import ru.shift.task6.server.exceptions.client.UserRegistrationException;

@Slf4j
public class HandlerProvider {

    private static final HandlerProvider INSTANCE = new HandlerProvider();

    private final Map<PayloadType, Handler> handlers = new EnumMap<>(PayloadType.class);

    public static void handleWithContext(Envelope<?> envelope, HandlerContext context) {
        var preparedHandler = INSTANCE.getPreparedHandler(envelope);
        try {
            preparedHandler.handleWithContext(context);
        } catch (AbstractClientFaultException e) {
            context.errorResponseSender()
                    .sendErrorResponse(envelope.getHeader().getPayloadType(), Fault.CLIENT, e);
        } catch (Exception e) {
            context.errorResponseSender()
                    .sendErrorResponse(envelope.getHeader().getPayloadType(), Fault.SERVER, e);
        }
    }

    private HandlerProvider() {
        createHandlers();
    }

    private PreparedHandler getPreparedHandler(Envelope<?> envelope) {
        var type = envelope.getHeader().getPayloadType();
        if (!handlers.containsKey(type)) {
            log.error("No handler found for {}", type);
            return new PreparedHandler(envelope, createErrorHandler(type,
                    new NoHandlerFoundException("No handler found for " + type)
            ));
        }
        return new PreparedHandler(envelope, handlers.get(type));
    }

    private void createHandlers() {
        handlers.put(PayloadType.MESSAGE, createMessageHandler());
        handlers.put(PayloadType.AUTH, createAuthRequestHandler());
        handlers.put(PayloadType.JOIN_RQ, createJoinRequestHandler());
        handlers.put(PayloadType.USER_LIST_RQ, createUserListRequestHandler());
        handlers.put(PayloadType.SHUTDOWN, createClientDisconnectionHandler());
    }

    private Handler createMessageHandler() {
        return (env, hCtxt) -> {
            final var chatMessage = (ChatMessage) env.getPayload();
            hCtxt.clientContext().checkAuthorized("Нельзя отправить сообщения без ввода имени");
            hCtxt.clientContext()
                    .checkJoined("Перед отправкой сообщения необходимо присоединиться к чату");
            if (!chatMessage.getSender().getNickname()
                    .equals(hCtxt.clientContext().getUser().getNickname())) {
                throw new ForbiddenException("Нельзя отправлять сообщения от чужого имени");
            }
            hCtxt.broadcaster().broadcast(PayloadType.MESSAGE, chatMessage);
        };
    }

    private Handler createAuthRequestHandler() {
        return (env, hCtxt) -> {
            UserInfo newUser = ((AuthRequest) env.getPayload()).getUser();
            log.info("New auth request from {}", newUser.getNickname());
            validateNewUser(newUser);
            try {
                hCtxt.clientContext()
                        .setUser(hCtxt.service().addClient(newUser, hCtxt.clientContext()));
            } catch (IllegalStateException e) {
                throw new UserRegistrationException("Имя пользователя уже занято");
            }
            hCtxt.responseSender().sendResponse(PayloadType.SUCCESS,
                    new SuccessAuthResponse(hCtxt.clientContext().getUser()));
        };
    }

    private Handler createJoinRequestHandler() {
        return (env, hCtxt) -> {
            log.info("User wants to join chat");
            hCtxt.clientContext()
                    .checkAuthorized("Перед тем как войти в чат, необходимо ввести никнейм");
            hCtxt.clientContext().setJoined(true);
            hCtxt.responseSender().sendResponse(PayloadType.JOIN_RS,
                    new JoinResponse(hCtxt.clientContext().getUser()));
            hCtxt.broadcaster().broadcast(PayloadType.JOIN_NOTIFICATION,
                    new JoinNotification(hCtxt.clientContext().getUser()));
        };
    }

    private Handler createUserListRequestHandler() {
        return (env, hCtxt) -> {
            log.info("User wants to get active users list");
            hCtxt.clientContext()
                    .checkAuthorized("Для совершения этого запроса необходимо ввести никнейм");
            hCtxt.clientContext()
                    .checkJoined("Вы должны быть в чате чтобы получить список пользователей.");
            hCtxt.responseSender().sendResponse(
                    PayloadType.USER_LIST_RS,
                    new UserListResponse(hCtxt.service().getAllUsers())
            );
        };
    }

    private Handler createClientDisconnectionHandler() {
        return (env, hCtxt) -> {
            log.info("Client disconnected");
            try {
                hCtxt.clientContext().close();
            } catch (IOException e) {
                log.warn("Error while closing client connection: ", e);
            }
        };
    }


    private Handler createErrorHandler(PayloadType correctResponseType, Throwable cause) {
        return (env, hCtxt) -> hCtxt.errorResponseSender()
                .sendErrorResponse(correctResponseType, Fault.SERVER, cause);
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

    private record PreparedHandler(Envelope<?> envelope, Handler handler) {
        public void handleWithContext(HandlerContext context) {
            handler.handle(envelope, context);
        }
    }

    @FunctionalInterface
    private interface Handler {
        void handle(Envelope<?> envelope, HandlerContext handlerContext);
    }
}
