package ru.shift.task6.server.handling.raw;

import java.time.Instant;
import ru.shift.task6.commons.JsonSerializer;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.task6.server.handling.MessageSender;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.services.ClientService;

public class RawRequestHandler {

    private final MessageSender sender;

    private final HandlersRegistry handlersRegistry;

    public RawRequestHandler(ClientContext context, ClientService service) {
        sender = context.getSender();

        handlersRegistry = new HandlersRegistry(
                context,
                service,
                this::createAndSendResponse,
                sender::broadcast,
                this::createAndSendErrorResponse
        );
    }

    private void dispatch(Envelope<? extends Payload> envelope) {
        handlersRegistry.getHandler(envelope.getHeader().getPayloadType()).accept(envelope);
    }

    public void dispatch(String json) {
        try {
            dispatch(JsonSerializer.deserialize(json));
        } catch (DeserializationException e) {
            createAndSendErrorResponse(PayloadType.ERROR, Fault.CLIENT, e);
        }
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
