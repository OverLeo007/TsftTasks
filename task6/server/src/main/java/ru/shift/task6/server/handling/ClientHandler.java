package ru.shift.task6.server.handling;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.handling.raw.RawRequestHandler;
import ru.shift.task6.server.services.ClientService;

import java.net.Socket;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientService service;

    public ClientHandler(Socket socket, ClientService clientService) {
        this.socket = socket;
        this.service = clientService;
        log.info("New socket connection opened");
    }

    @Override
    public void run() {
        try (ClientContext context = new ClientContext(socket, service::sendAll, service::removeClient)) {
            final var requestHandler = new RawRequestHandler(context, service);

            String line;
            while (!context.isClosed() && (line = context.getReader().readline()) != null) {
                requestHandler.dispatch(line);
            }
        } catch (Exception e) {
            log.warn("Client disconnected with error", e);
        } finally {
            log.info("Connection closed");
        }
    }

}

