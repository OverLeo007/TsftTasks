package ru.shift.task6.client.socket;

import java.io.Closeable;
import java.util.function.Consumer;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.responses.ErrorResponse;

interface Connection extends Closeable {

    void send(PayloadType type, Payload payload);

    <T extends Payload> void sendAwaitResponse(
            PayloadType requestType,
            Payload payload,
            PayloadType responseType,
            Consumer<Envelope<T>> onResponse,
            Consumer<Envelope<ErrorResponse>> onError
    );

    <T extends Payload> void addPermanentMessageListener(
            PayloadType messageType,
            Consumer<Envelope<T>> onMessage
    );


}
