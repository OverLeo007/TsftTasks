package ru.shift.task6.server.handling;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.server.client.ClientContext;
import ru.shift.task6.server.handling.raw.RawRequestHandler;
import ru.shift.task6.server.services.ClientService;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientService service;
    private final Consumer<Envelope<? extends Payload>> broadcastConsumer;

    public ClientHandler(Socket socket, ClientService clientService,
            Consumer<Envelope<? extends Payload>> broadcastConsumer) {
        this.socket = socket;
        this.service = clientService;
        this.broadcastConsumer = broadcastConsumer;
        log.info("New socket connection opened");
    }

    @Override
    public void run() {
        try (ClientContext context = new ClientContext(socket, broadcastConsumer, service::removeClient)) {
            val requestHandler = new RawRequestHandler(context, service);

            String line;
            while ((line = context.getReader().readLine()) != null) {
                requestHandler.dispatch(line);
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            log.error("IO error", e);
        } catch (Exception e) {
            log.warn("Client disconnected with unknown error", e);
        } finally {
            log.info("Connection closed");
        }
    }

}

