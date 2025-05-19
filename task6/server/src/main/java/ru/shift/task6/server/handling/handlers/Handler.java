package ru.shift.task6.server.handling.handlers;

import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.server.handling.provider.HandlerContext;

@FunctionalInterface
public interface Handler<T> {

    void handle(T request, HandlerContext context);

    @SuppressWarnings("unchecked")
    default void handle(Message message, HandlerContext context) {
        handle((T) message, context);
    }
}
