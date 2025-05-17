package ru.shift.task6.client.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.client.exceptions.ResponseException;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;
import ru.shift.task6.commons.channel.ChatChannel;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.exceptions.SocketConnectionException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.ShutdownNotice;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;

@Slf4j
public class SocketConnection implements Closeable {

    private final Map<PayloadType, CompletableFuture<Envelope<?>>> pendingResponses = new ConcurrentHashMap<>();
    private final Map<PayloadType, List<Consumer<Envelope<?>>>> permanentListeners = new ConcurrentHashMap<>();

    private final ExecutorService listenerPool = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "MessageListener");
        t.setDaemon(true);
        return t;
    });

    private final ChatChannel chatChannel;

    public SocketConnection(Socket socket) {
        chatChannel = new ChatChannel(socket);
        listenerPool.submit(this::listen);
    }

    private void listen() {
        try {
            Envelope<?> envelope;
            while ((envelope = chatChannel.readEnvelope()) != null) {
                log.trace("Catch incoming message: {}", envelope);
                if (envelope.getHeader().getPayloadType() == PayloadType.ERROR) {
                    handleErrorResponse(envelope);
                    continue;
                }
                handleSuccessResponse(envelope);
            }
        } catch (DeserializationException e) {
            throw new SocketConnectionException("Deserialization incoming message error", e);
        } catch (IOException e) {
            handleDisconnectionWhileListen();
        }
    }

    private void handleSuccessResponse(Envelope<?> envelope) {
        PayloadType type = envelope.getHeader().getPayloadType();
        CompletableFuture<Envelope<?>> future = pendingResponses.remove(type);
        if (future != null) {
            future.complete(envelope);
        }

        var permanent = permanentListeners.get(type);
        if (permanent != null) {
            permanent.forEach(l -> l.accept(envelope));
        }
    }

    private void handleErrorResponse(Envelope<?> envelope) {
        var typedPayload = (ErrorResponse) envelope.getPayload();
        var originalType = typedPayload.getCorrectResponseType();

        var errorFuture = pendingResponses.remove(originalType);
        if (errorFuture != null) {
            errorFuture.completeExceptionally(new ResponseException(typedPayload));
        } else if (originalType == PayloadType.ERROR) {
            new ErrorWindowImpl(
                    typedPayload.getMessage(),
                    false
            ).setEnabled(true);
        }
    }

    private void handleDisconnectionWhileListen() {
        var disconnectionEnvelope = new Envelope<>(
                new Header(PayloadType.SHUTDOWN, null),
                new ShutdownNotice("Server disconnected")
        );

        CompletableFuture<Envelope<?>> shutdown = pendingResponses.remove(PayloadType.SHUTDOWN);
        if (shutdown != null) {
            shutdown.complete(disconnectionEnvelope);
        }
        var permanent = permanentListeners.get(PayloadType.SHUTDOWN);
        if (permanent != null) {
            permanent.forEach(listener -> listener.accept(disconnectionEnvelope));
        }
    }


    public void send(PayloadType type, Payload payload) {
        var envelope = new Envelope<>(
                new Header(type, Instant.now()),
                payload
        );
        try {
            if (chatChannel.isClosed()) {
                log.warn("Socket is closed, cannot send message");
                return;
            }
            chatChannel.sendEnvelope(envelope);
            if (chatChannel.checkWriterError()) {
                throw new SocketConnectionException("Error while sending message");
            }
        } catch (SerializationException e) {
            throw new SocketConnectionException("Error while sending message", e);
        }

    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> CompletableFuture<Envelope<T>> sendAwaitResponse(
            PayloadType requestType, Payload payload,
            PayloadType responseType
    ) {
        CompletableFuture<Envelope<?>> future = new CompletableFuture<>();
        pendingResponses.put(responseType, future);

        try {
            send(requestType, payload);
        } catch (SocketConnectionException e) {
            pendingResponses.remove(responseType);
            future.completeExceptionally(
                    new ResponseException(
                            new ErrorResponse(responseType, Fault.CLIENT, e.getMessage())
                    )
            );
        }

        return (CompletableFuture<Envelope<T>>) (CompletableFuture<?>) future;
    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> void addPermanentMessageListener(PayloadType messageType,
            Consumer<Envelope<T>> onMessage) {
        permanentListeners.computeIfAbsent(messageType, __ -> new CopyOnWriteArrayList<>())
                .add(env -> onMessage.accept((Envelope<T>) env));

    }

    @Override
    public void close() throws IOException {
        log.debug("Closing socket connection");
        listenerPool.shutdown();
        chatChannel.close();
    }
}
