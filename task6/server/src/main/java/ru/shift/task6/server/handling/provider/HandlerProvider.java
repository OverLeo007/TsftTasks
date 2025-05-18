package ru.shift.task6.server.handling.provider;

import static ru.shift.task6.alt.commons.protocol.MessageType.DISCONNECT_NF;

import java.util.EnumMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.alt.commons.protocol.abstracts.Request;
import ru.shift.task6.server.handling.handlers.AuthRequestHandler;
import ru.shift.task6.server.handling.handlers.DisconnectNotificationHandler;
import ru.shift.task6.server.handling.handlers.Handler;
import ru.shift.task6.server.handling.handlers.JoinRequestHandler;
import ru.shift.task6.server.handling.handlers.MessageRequestHandler;
import ru.shift.task6.server.handling.handlers.UnknownHandler;
import ru.shift.task6.server.handling.handlers.UserListRequestHandler;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerProvider {

    private static final HandlerProvider INSTANCE = new HandlerProvider();

    private final Map<MessageType, Handler<?>> handlers = new EnumMap<>(MessageType.class);
    private final Handler<?> errorHandler = new UnknownHandler();

    {
        handlers.put(MessageType.AUTH_RQ, new AuthRequestHandler());
        handlers.put(MessageType.JOIN_RQ, new JoinRequestHandler());
        handlers.put(MessageType.MESSAGE_RQ, new MessageRequestHandler());
        handlers.put(MessageType.USER_LIST_RQ, new UserListRequestHandler());
        handlers.put(DISCONNECT_NF, new DisconnectNotificationHandler());
    }

    public static void handleWithContext(Message message, HandlerContext context) {
        var handler = INSTANCE.handlers.get(message.getType());
        try {
            if (handler == null) {
                INSTANCE.errorHandler.handle(message, context);
                return;
            }
            handler.handle(message, context);

        } catch (Exception e) {
            if (message.isRequest()) {
                ((Request) message).error(e.getMessage());
            }
        }
    }
}
