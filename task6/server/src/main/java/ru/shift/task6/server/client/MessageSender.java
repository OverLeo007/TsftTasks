package ru.shift.task6.server.client;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.channel.ChatWriter;
import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;

@Slf4j
public class MessageSender {

    @Setter
    private ChatWriter writer;
    private final Consumer<Envelope<? extends Payload>> broadcastConsumer;

    public MessageSender(Consumer<Envelope<? extends Payload>> broadcastConsumer, ChatWriter writer) {
        this.broadcastConsumer = broadcastConsumer;
        this.writer = writer;
        Objects.requireNonNull(writer, "Writer is null");
    }

    public void send(Envelope<? extends Payload> envelope) {
        try {
            writer.sendEnvelope(envelope);
        } catch (SerializationException e) {
            log.error("Message serialization error", e);
            sendError(envelope.getHeader().getPayloadType(), Fault.SERVER, "Ошибка при сериализации ответа");
        }
    }

    public void sendError(PayloadType correctResponseType, Fault fault, String errorMessage) {
        final var response = new ErrorResponse(correctResponseType, fault, errorMessage);
        try {
            final var header = new Header(PayloadType.ERROR, Instant.now());
            final var envelope = new Envelope<>(header, response);
            writer.sendEnvelope(envelope);
        } catch (Exception e) {
            log.error("Ошибка при отправке ошибки клиенту", e);
        }
    }

    public <T extends Payload> void broadcast(PayloadType payloadType, T payload) {
        final var header = new Header(payloadType, Instant.now());
        final var envelope = new Envelope<>(header, payload);
        broadcastConsumer.accept(envelope);
    }

}
