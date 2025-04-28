package ru.shift.task6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.models.Message;
import ru.shift.task6.models.MessageType;
import ru.shift.task6.models.User;

@Slf4j
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientRepository clientRepository;
    private final Consumer<Message> broadcast;
    private final ObjectMapper mapper;

    private PrintWriter writer;
    private BufferedReader reader;
    private User user;

    public ClientHandler(Socket socket, ClientRepository clientRepository,
            Consumer<Message> broadcast, ObjectMapper mapper) {
        this.socket = socket;
        this.clientRepository = clientRepository;
        this.broadcast = broadcast;
        this.mapper = mapper;

        log.info("Новый пользователь подключается");
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            authenticate();
            acceptMessages();
        } catch (SocketException e) {
            if (user != null) {
                log.info("Пользователь {} отключился", user.name());
            } else {
                log.info("Пользователь отключился не пройдя аутентификацию");
            }
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода", e);
        } catch (Exception e) {
            log.warn("Пользователь {} отключился с ошибкой", user.name(), e);
        } finally {
            disconnect();
        }
    }

    private void authenticate() throws IOException {
        while (true) {
            val userJson = reader.readLine();
            if (userJson == null) {
                throw new IOException("Клиент отключился во время аутентификации");
            }

            User newUser;
            try {
                newUser = mapper.readValue(userJson, User.class);
            } catch (JsonProcessingException e) {
                log.warn("Некорректный JSON: {}", userJson, e);
                sendError("Некорректный формат JSON");
                continue;
            }

            if (newUser.name() == null || newUser.name().isBlank()) {
                sendError("Имя пользователя не может быть пустым");
                continue;
            } else {
                val nameF = newUser.name().strip();
                if (nameF.length() > 20 || nameF.length() < 3) {
                    sendError("Имя пользователя должно быть от 3 до 20 символов в длину");
                    continue;
                }
            }
            if (clientRepository.contains(newUser)) {
                sendError("Имя пользователя уже занято");
                continue;
            }

            this.user = new User(newUser.name(), Instant.now());
            clientRepository.addClient(this.user, this);
            send(new Message(
                    this.user,
                    MessageType.SUCCESS,
                    "Вы успешно вошли в чат как '" + user.name() + "'",
                    Instant.now()));
            Thread.currentThread().setName("ClientHandler-" + user.name());

            log.info("Пользователь '{}' подключился", user);
            broadcast.accept(new Message(user, MessageType.JOIN, user + " присоединился к чату",
                    Instant.now()));

            break;
        }
    }

    private void acceptMessages() throws IOException {
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            try {
                val message = mapper.readValue(inputLine, Message.class);
                log.info("Сообщение от {}: {}", user.name(), message.text());
                broadcast.accept(message);
            } catch (JsonProcessingException e) {
                log.warn("Некорректное сообщение от {}: {}", user.name(), inputLine, e);
                sendError("Некорректный формат сообщения");
            }
        }
    }

    public void send(Message message) {
        try {
            val json = mapper.writeValueAsString(message);
            writer.println(json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации сообщения", e);
        }
    }

    private void sendError(String errorMessage) {
        try {
            send(new Message(
                    Server.SERVER_USER,
                    MessageType.ERROR,
                    errorMessage,
                    Instant.now()));
        } catch (Exception e) {
            log.error("Ошибка при отправке ошибки клиенту", e);
        }
    }

    private void disconnect() {
        if (user != null) {
            log.debug("Обработка отключения '{}'", user);
            clientRepository.removeClient(user);
            broadcast.accept(new Message(user, MessageType.LEAVE, user + " покинул чат",
                    Instant.now()));
        }

        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}

