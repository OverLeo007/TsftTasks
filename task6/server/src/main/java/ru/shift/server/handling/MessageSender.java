package ru.shift.server.handling;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.JsonSerializer;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.Header;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.responses.ErrorResponse;
import ru.shift.commons.models.payload.responses.ErrorResponse.Fault;

@Slf4j
public class MessageSender {

    @Setter
    private PrintWriter writer;
    private final Consumer<Envelope<? extends Payload>> broadcastConsumer;

    public MessageSender(Consumer<Envelope<? extends Payload>> broadcastConsumer, PrintWriter writer) {
        this.broadcastConsumer = broadcastConsumer;
        this.writer = writer;
    }

    public <T extends Payload> void send(PayloadType payloadType, T payload) {
        checkWriter();
        val header = new Header(payloadType, Instant.now());
        val envelope = new Envelope<>(header, payload);
        send(envelope);
    }

    public void send(Envelope<? extends Payload> envelope) {
        checkWriter();
        try {
            val json = JsonSerializer.serialize(envelope);
            writer.println(json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации сообщения", e);
            sendClientError("Некорректный Json");
        }
    }

    public void sendClientError(String errorMessage) {
        checkWriter();
        sendError(Fault.CLIENT, errorMessage);
    }

    public void sendError(Fault fault, String errorMessage) {
        checkWriter();
        val response = new ErrorResponse(fault, errorMessage);
        try {
            send(PayloadType.ERROR, response);
        } catch (Exception e) {
            log.error("Ошибка при отправке ошибки клиенту", e);
        }
    }

    public <T extends Payload> void broadcast(PayloadType payloadType, T payload) {
        checkWriter();
        val header = new Header(payloadType, Instant.now());
        broadcast(header, payload);
    }

    public <T extends Payload> void broadcast(Header header, T payload) {
        checkWriter();
        val envelope = new Envelope<>(header, payload);
        broadcastConsumer.accept(envelope);
    }

    public <T extends Payload> void broadcast(Envelope<T> envelope) {
        checkWriter();
        envelope.getHeader().setSendTime(Instant.now());
        broadcastConsumer.accept(envelope);
    }

    private void checkWriter() {
        Objects.requireNonNull(writer, "Нет потока записи, невозможно отправить сообщение(я)");
    }
}
