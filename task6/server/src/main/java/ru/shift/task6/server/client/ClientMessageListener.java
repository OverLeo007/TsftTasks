package ru.shift.task6.server.client;

import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.exceptions.ProtocolException;
import ru.shift.task6.commons.protocol.abstracts.Message;
import ru.shift.task6.commons.protocol.impl.responses.ErrorResponse;
import ru.shift.task6.server.handling.RequestHandler;
import ru.shift.task6.server.services.ClientService;

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
                Message message;
                while (!context.isClosed() && (message = context.getReader().readMessage()) != null) {
                    requestHandler.dispatch(message);
                }
            } catch (ProtocolException e) {
                requestHandler.createAndSendErrorResponse(new ErrorResponse(e.getMessage()));
            }

        } catch (Exception e) {
            log.warn("Client disconnected with error", e);
        } finally {
            log.info("Connection closed");
        }
    }

}

