package ru.shift.server.handling;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.JsonSerializer;
import ru.shift.commons.exceptions.SerializationException;
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

    public void send(Envelope<? extends Payload> envelope) {
        checkWriter();
        try {
            val json = JsonSerializer.serialize(envelope);
            writer.println(json);
        } catch (SerializationException e) {
            log.error("Message serialization error", e);
            sendError(Fault.SERVER, "Ошибка при сериализации ответа");
        }
    }

    public void sendError(Fault fault, String errorMessage) {
        checkWriter();
        val response = new ErrorResponse(fault, errorMessage);
        try {
            val header = new Header(PayloadType.ERROR, Instant.now());
            val envelope = new Envelope<>(header, response);
            val json = JsonSerializer.serialize(envelope);
            writer.println(json);
        } catch (Exception e) {
            log.error("Ошибка при отправке ошибки клиенту", e);
        }
    }

    public <T extends Payload> void broadcast(PayloadType payloadType, T payload) {
        checkWriter();
        val header = new Header(payloadType, Instant.now());
        val envelope = new Envelope<>(header, payload);
        broadcastConsumer.accept(envelope);
    }

    private void checkWriter() {
        Objects.requireNonNull(writer, "Нет потока записи, невозможно отправить сообщение(я)");
    }
}
