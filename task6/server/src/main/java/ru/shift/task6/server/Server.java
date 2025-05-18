package ru.shift.task6.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.server.client.ClientMessageListener;
import ru.shift.task6.server.config.RunProperties;
import ru.shift.task6.server.services.ClientService;

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
                final var thread = new Thread(new ClientMessageListener(socket, clientService));
                thread.start();
            }
        } catch (IOException e) {
            log.error("Ошибка сервера", e);
        }
    }

    private void onShutdownBroadcast() {
        clientService.sendAll(new DisconnectNotification("Сервер завершил работу"));
    }
}

