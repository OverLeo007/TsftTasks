package ru.shift.task6.client.socket.response.handlers;

import ru.shift.task6.commons.protocol.abstracts.Response;

public interface ResponseHandler<T> {

    void handle(T response);

    @SuppressWarnings("unchecked")
    default void handle(Response response) {
        handle((T) response);
    }
}
