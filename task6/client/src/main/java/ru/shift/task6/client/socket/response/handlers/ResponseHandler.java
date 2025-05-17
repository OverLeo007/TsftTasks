package ru.shift.task6.client.socket.response.handlers;

import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.Payload;

public interface ResponseHandler<T extends Payload> {

    @SuppressWarnings("unchecked")
    default void handle(Envelope<?> envelope) {
        handle(((Envelope<T>) envelope).getPayload());
    }

    void handle(T payload);

}
