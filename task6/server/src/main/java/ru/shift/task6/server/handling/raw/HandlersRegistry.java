package ru.shift.task6.server.handling.raw;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.misc.TriConsumer;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.ChatMessage;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.commons.models.payload.requests.AuthRequest;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.task6.commons.models.payload.responses.JoinNotification;
import ru.shift.task6.commons.models.payload.responses.JoinResponse;
import ru.shift.task6.commons.models.payload.responses.SuccessAuthResponse;
import ru.shift.task6.commons.models.payload.responses.UserListResponse;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.exceptions.NoHandlerFoundException;
import ru.shift.task6.server.exceptions.client.ForbiddenException;
import ru.shift.task6.server.exceptions.client.UserRegistrationException;
import ru.shift.task6.server.services.ClientService;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class HandlersRegistry {
    private final Map<PayloadType, Consumer<Envelope<?>>> handlers = new EnumMap<>(PayloadType.class);

    private final ClientContext context;
    private final ClientService service;

    private final BiConsumer<PayloadType, Payload> responseSender;
    private final BiConsumer<PayloadType, Payload> broadcaster;
    private final TriConsumer<PayloadType, Fault, Throwable> errorResponseSender;


    public HandlersRegistry(
            ClientContext context,
            ClientService service,
            BiConsumer<PayloadType, Payload> responseSender,
            BiConsumer<PayloadType, Payload> broadcaster,
            TriConsumer<PayloadType, Fault, Throwable> errorResponseSender
    ) {
        this.responseSender = responseSender;
        this.errorResponseSender = errorResponseSender;
        this.broadcaster = broadcaster;
        this.context = context;
        this.service = service;

        createHandlers();
    }

    public Consumer<Envelope<?>> getHandler(PayloadType type) {
        if (!handlers.containsKey(type)) {
            log.error("No handler found for {}", type);
            return createErrorHandler(type,
                    new NoHandlerFoundException("No handler found for " + type)
            );
        }
        return handlers.get(type);
    }

    private void createHandlers() {
        handlers.put(PayloadType.MESSAGE, createMessageHandler());
        handlers.put(PayloadType.AUTH, createAuthRequestHandler());
        handlers.put(PayloadType.JOIN_RQ, createJoinRequestHandler());
        handlers.put(PayloadType.USER_LIST_RQ, createUserListRequestHandler());
        handlers.put(PayloadType.SHUTDOWN, createClientDisconnectionHandler());
    }

    private Consumer<Envelope<?>> createMessageHandler() {
        return env -> {
            final var chatMessage = (ChatMessage) env.getPayload();
            context.checkAuthorized("Нельзя отправить сообщения без ввода имени");
            context.checkJoined("Перед отправкой сообщения необходимо присоединиться к чату");
            if (!chatMessage.getSender().getNickname().equals(context.getUser().getNickname())) {
                throw new ForbiddenException("Нельзя отправлять сообщения от чужого имени");
            }
            broadcaster.accept(PayloadType.MESSAGE, chatMessage);
        };
    }

    private Consumer<Envelope<?>> createAuthRequestHandler() {
        return env -> {
            UserInfo newUser = ((AuthRequest) env.getPayload()).getUser();
            log.info("New auth request from {}", newUser.getNickname());
            validateNewUser(newUser);
            try {
                context.setUser(service.addClient(newUser, context));
            } catch (IllegalStateException e) {
                throw new UserRegistrationException("Имя пользователя уже занято");
            }
            responseSender.accept(PayloadType.SUCCESS, new SuccessAuthResponse(context.getUser()));
        };
    }

    private Consumer<Envelope<?>> createJoinRequestHandler() {
        return env -> {
            log.info("User wants to join chat");
            context.checkAuthorized("Перед тем как войти в чат, необходимо ввести никнейм");
            context.setJoined(true);
            responseSender.accept(PayloadType.JOIN_RS, new JoinResponse(context.getUser()));
            broadcaster.accept(PayloadType.JOIN_NOTIFICATION, new JoinNotification(context.getUser()));
        };
    }

    private Consumer<Envelope<?>> createUserListRequestHandler() {
        return env -> {
            log.info("User wants to get active users list");
            context.checkAuthorized("Для совершения этого запроса необходимо ввести никнейм");
            context.checkJoined("Вы должны быть в чате чтобы получить список пользователей.");
            responseSender.accept(PayloadType.USER_LIST_RS, new UserListResponse(service.getAllUsers()));
        };
    }

    private Consumer<Envelope<?>> createClientDisconnectionHandler() {
        return env -> {
            log.info("Client disconnected");
            try {
                context.close();
            } catch (IOException e) {
                log.warn("Error while closing client connection: ", e);
            }
        };
    }


    private Consumer<Envelope<?>> createErrorHandler(PayloadType correctResponseType, Throwable cause) {
        return  env -> errorResponseSender.accept(correctResponseType, Fault.SERVER, cause);
    }

    private void validateNewUser(UserInfo newUser) {
        if (newUser.getNickname() == null || newUser.getNickname().isBlank()) {
            throw new UserRegistrationException("Имя пользователя не может быть пустым");
        } else {
            final var nameF = newUser.getNickname().strip();
            if (nameF.length() > 20 || nameF.length() < 3) {
                throw new UserRegistrationException("Имя пользователя должно быть от 3 до 20 символов в длину");
            }
        }
    }
}
