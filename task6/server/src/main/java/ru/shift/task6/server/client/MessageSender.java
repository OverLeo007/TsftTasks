package ru.shift.task6.server.client;

import java.util.Objects;
import java.util.function.Consumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.channel.ChatWriter;
import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.alt.commons.protocol.abstracts.Notification;
import ru.shift.task6.alt.commons.protocol.impl.responses.ErrorResponse;

@Slf4j
public class MessageSender {

    @Setter
    private ChatWriter writer;
    private final Consumer<Notification> broadcastConsumer;

    public MessageSender(Consumer<Notification> broadcastConsumer, ChatWriter writer) {
        this.broadcastConsumer = broadcastConsumer;
        this.writer = writer;
        Objects.requireNonNull(writer, "Writer is null");
    }

    public void send(Message message) {
        try {
            writer.sendMessage(message);
        } catch (ProtocolException e) {
            log.error("Message serialization error", e);
            sendError("Ошибка при сериализации ответа");
        }
    }

    public void sendError(String errorMessage) {
        final var response = new ErrorResponse(errorMessage);
        try {
            writer.sendMessage(response);
        } catch (Exception e) {
            log.error("Ошибка при отправке ошибки клиенту", e);
        }
    }

    public void broadcast(Notification notification) {
        broadcastConsumer.accept(notification);
    }

}
