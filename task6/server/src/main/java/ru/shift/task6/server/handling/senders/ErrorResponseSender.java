package ru.shift.task6.server.handling.senders;

import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;

@FunctionalInterface
public interface ErrorResponseSender {
    void sendErrorResponse(PayloadType correctResponseType, Fault fault, Throwable cause);

}
