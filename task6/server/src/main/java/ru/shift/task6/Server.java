package ru.shift.task6;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.config.RunProperties;
import ru.shift.task6.models.Message;
import ru.shift.task6.models.MessageType;
import ru.shift.task6.models.User;

@Slf4j
public class Server {

    private final RunProperties properties;
    private final ClientRepository clientRepository = new ClientRepository();
    private final ObjectMapper mapper;

    public static final User SERVER_USER = new User("Server", Instant.now());


    public Server(RunProperties properties) {
        this.properties = properties;
        this.mapper = new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void start() {
        AtomicBoolean isClosed = new AtomicBoolean(false);
        try (ServerSocket serverSocket = new ServerSocket(properties.getServer().getPort())) {
            log.info("Сервер запущен на порту {}", properties.getServer().getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Остановка сервера...");
                try {
                    broadcast(new Message(SERVER_USER, MessageType.SYSTEM, "Сервер отключается", Instant.now()));
                    clientRepository.closeAll();
                    serverSocket.close();
                    isClosed.set(true);
                } catch (IOException e) {
                    log.error("Ошибка при завершении работы сервера", e);
                }
            }, "SocketCloseShutdownHook"));

            while (!isClosed.get()) {
                Socket socket = serverSocket.accept();
                val thread = new Thread(new ClientHandler(socket, clientRepository, this::broadcast,
                        mapper));
                thread.start();

            }
        } catch (IOException e) {
            log.error("Ошибка сервера", e);
        }
    }

    private void broadcast(Message message) {
        clientRepository.getAllClients()
                .forEach(handler -> handler.send(message));
    }
}

