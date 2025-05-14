package ru.shift.task6.server;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.ShutdownNotice;
import ru.shift.task6.server.config.RunProperties;
import ru.shift.task6.server.handling.ClientHandler;
import ru.shift.task6.server.services.ClientService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

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
                final var thread = new Thread(new ClientHandler(socket, clientService));
                thread.start();
            }
        } catch (IOException e) {
            log.error("Ошибка сервера", e);
        }
    }

    private void onShutdownBroadcast() {
        final var header = new Header(PayloadType.SHUTDOWN, Instant.now());
        final var payload = new ShutdownNotice("Сервер завершил работу");
        clientService.sendAll(new Envelope<>(header, payload));
    }
}

