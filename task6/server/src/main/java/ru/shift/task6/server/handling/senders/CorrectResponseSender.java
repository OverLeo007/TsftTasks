package ru.shift.task6.server.handling.senders;

import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@FunctionalInterface
public interface CorrectResponseSender {
    void sendResponse(PayloadType payloadType, Payload payload);

}
