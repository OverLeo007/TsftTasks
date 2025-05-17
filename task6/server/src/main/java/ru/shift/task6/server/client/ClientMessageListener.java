package ru.shift.task6.server.client;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.task6.server.handling.RequestHandler;
import ru.shift.task6.server.services.ClientService;

import java.net.Socket;

@Slf4j
public class ClientMessageListener implements Runnable {

    private final Socket socket;
    private final ClientService service;

    public ClientMessageListener(Socket socket, ClientService clientService) {
        this.socket = socket;
        this.service = clientService;
        log.info("New socket connection opened");
    }

    @Override
    public void run() {
        try (ClientContext context = new ClientContext(socket, service::sendAll, service::removeClient)) {

            var requestHandler = new RequestHandler(context, service);
            try {
                Envelope<?> envelope;
                while (!context.isClosed() && (envelope = context.getReader().readEnvelope()) != null) {
                    requestHandler.dispatch(envelope);
                }
            } catch (DeserializationException e) {
                requestHandler.createAndSendErrorResponse(PayloadType.ERROR, Fault.CLIENT, e);
            }

        } catch (Exception e) {
            log.warn("Client disconnected with error", e);
        } finally {
            log.info("Connection closed");
        }
    }

}

