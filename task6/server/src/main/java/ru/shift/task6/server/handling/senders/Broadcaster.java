package ru.shift.task6.server.handling.senders;

import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@FunctionalInterface
public interface Broadcaster {
    void broadcast(PayloadType payloadType, Payload payload);
}
