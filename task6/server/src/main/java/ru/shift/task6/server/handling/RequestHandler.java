package ru.shift.task6.server.handling;

import java.time.Instant;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
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

    public void dispatch(Envelope<? extends Payload> envelope) {
        HandlerProvider.handleWithContext(envelope, handlerContext);
    }

    private void createAndSendResponse(PayloadType responseType, Payload payload) {
        Envelope<? extends Payload> responseEnvelope = new Envelope<>(
                new Header(responseType, Instant.now()),
                payload
        );
        sender.send(responseEnvelope);
    }

    public void createAndSendErrorResponse(PayloadType correctResponseType, Fault fault, Throwable cause) {
        createAndSendResponse(PayloadType.ERROR, new ErrorResponse(correctResponseType, fault, cause.getMessage()));
    }
}
