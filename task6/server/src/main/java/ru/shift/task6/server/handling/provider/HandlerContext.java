package ru.shift.task6.server.handling.provider;

import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.handling.senders.Broadcaster;
import ru.shift.task6.server.handling.senders.CorrectResponseSender;
import ru.shift.task6.server.handling.senders.ErrorResponseSender;
import ru.shift.task6.server.services.ClientService;

public record HandlerContext(
        ClientContext clientContext,
        ClientService service,
        CorrectResponseSender responseSender,
        Broadcaster broadcaster,
        ErrorResponseSender errorResponseSender
) {}
