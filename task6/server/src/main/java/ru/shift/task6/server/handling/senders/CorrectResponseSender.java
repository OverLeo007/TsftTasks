package ru.shift.task6.server.handling.senders;

import ru.shift.task6.commons.protocol.abstracts.Response;

@FunctionalInterface
public interface CorrectResponseSender {
    void sendResponse(Response response);

}
