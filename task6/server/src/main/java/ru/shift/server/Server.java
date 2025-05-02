package ru.shift.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.server.config.RunProperties;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.Header;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.ShutdownNotice;
import ru.shift.server.handling.ClientHandler;
import ru.shift.server.services.ClientService;

@Slf4j
public class Server {
    private final RunProperties properties;
    private final ClientService clientService = new ClientService();
    public Server(RunProperties properties) {
        this.properties = properties;
    }

    public void start() {
        AtomicBoolean isClosed = new AtomicBoolean(false);
        try (ServerSocket serverSocket = new ServerSocket(properties.getServer().getPort())) {
            log.info("Сервер запущен на порту {}", properties.getServer().getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Остановка сервера...");
                try {
                    onShutdownBroadcast();
                    clientService.closeAll();
                    serverSocket.close();
                    isClosed.set(true);
                } catch (IOException e) {
                    log.error("Ошибка при завершении работы сервера", e);
                }
            }, "SocketCloseShutdownHook"));

            while (!isClosed.get()) {
                Socket socket = serverSocket.accept();
                val thread = new Thread(new ClientHandler(socket, clientService, this::broadcast));
                thread.start();
            }
        } catch (IOException e) {
            log.error("Ошибка сервера", e);
        }
    }

    private void broadcast(Envelope<? extends Payload> envelope) {
        clientService.sendAll(envelope);
    }

    private void onShutdownBroadcast() {
        val header = new Header(PayloadType.SHUTDOWN, Instant.now());
        val payload = new ShutdownNotice("Сервер завершил работу");
        broadcast(new Envelope<>(header, payload));
    }
}

