package ru.shift.task6.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.exceptions.AuthenticationException;
import ru.shift.task6.exceptions.SocketConnectionException;
import ru.shift.task6.models.Message;
import ru.shift.task6.models.MessageType;
import ru.shift.task6.models.User;

@Slf4j
public class SocketConnection {

    public static final User CLIENT_USER = new User("CLIENT", Instant.now());

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()
            .registerModule(new JavaTimeModule());

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private User user;
    @Setter
    private Consumer<Message> onMessageListener;
    @Setter
    private Consumer<Message> onDisconnectListener;

    public void connect(String host, int port) throws SocketConnectionException {
        try {
            this.socket = new Socket(host, port);
            this.reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    log.error("Ошибка при закрытии сокета", e);
                }
            }, "SocketConnection-ShutdownHook"));

        } catch (IOException e) {
            log.error("Connection error", e);
            throw new SocketConnectionException(e.getMessage(), e);
        }
    }

    public User authenticate(String nickname)
            throws AuthenticationException, SocketConnectionException {
        val user = new User(nickname, null);
        try {
            writer.println(mapper.writeValueAsString(user));
            String serverResponse = reader.readLine();
            if (serverResponse == null) {
                log.error("Сервер закрыл соединение");
                throw new SocketConnectionException("Сервер закрыл соединение");
            }
            Message responseMessage = mapper.readValue(serverResponse, Message.class);


            if (responseMessage.messageType() == MessageType.SUCCESS) {
                this.user = responseMessage.sender();
                return this.user;
            }
            if (responseMessage.messageType() == MessageType.ERROR) {
                log.warn("Ошибка регистрации: {}", responseMessage.text());
                throw new AuthenticationException(responseMessage.text());
            } else {
                log.warn("Неизвестный ответ сервера: {}", responseMessage);
                throw new AuthenticationException("Неизвестный ответ сервера: " + responseMessage);
            }

        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Ошибка чтения данных пользователя");
        } catch (IOException e) {
            throw new SocketConnectionException(e.getMessage(), e);
        }

    }

    public void sendMessage(String text) throws JsonProcessingException {
        Message message = new Message(user, MessageType.TEXT, text, Instant.now());
        writer.println(mapper.writeValueAsString(message));
    }

    public void startMessageAccepting() {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    Message message = mapper.readValue(serverMessage, Message.class);
                    if (message.fin()) {
                        onDisconnectListener.accept(message);
                    }
                    if (onMessageListener != null) {
                        onMessageListener.accept(message);
                    }
                }
            } catch (SocketException e) {
                onDisconnectListener.accept(new Message(
                        CLIENT_USER,
                        MessageType.ERROR,
                        "Сервер закрыл соединение",
                        Instant.now()
                ));
            } catch (IOException e) {
                log.error("Ошибка чтения сообщений", e);
            }
        }, "MessageListener").start();
    }
}
