package ru.shift.task6.server.handling;

import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.client.MessageSender;
import ru.shift.task6.server.handling.provider.HandlerContext;
import ru.shift.task6.server.handling.provider.HandlerProvider;
import ru.shift.task6.server.services.ClientService;

public class RequestHandler {

    private final MessageSender sender;

    private final HandlerContext handlerContext;

    public RequestHandler(ClientContext context, ClientService service) {
        sender = context.getSender();

        handlerContext = new HandlerContext(
                context,
                service,
                this::createAndSendResponse,
                sender::broadcast,
                this::createAndSendErrorResponse
        );
    }

    public void dispatch(Message message) {
        HandlerProvider.handleWithContext(message, handlerContext);
    }

    private void createAndSendResponse(Response response) {
        sender.send(response);
    }

    public void createAndSendErrorResponse(Response response) {
        createAndSendResponse(response);
    }
}
