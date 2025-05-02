package ru.shift.server.handling;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.payload.Payload;
import ru.shift.server.client.ClientContext;
import ru.shift.server.services.ClientService;
import ru.shift.server.handling.raw.RawRequestHandler;

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
        log.info("Новый пользователь подключается");
    }

    @Override
    public void run() {
        try (ClientContext context = new ClientContext(socket, broadcastConsumer)) {
            val requestHandler = new RawRequestHandler(context, service);

            String line;
            while ((line = context.getReader().readLine()) != null) {
                requestHandler.dispatch(line);
            }

        } catch (SocketException e) {
            log.info("Пользователь отключился");
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода", e);
        } catch (Exception e) {
            log.warn("Пользователь отключился с ошибкой", e);
        }
    }

}

