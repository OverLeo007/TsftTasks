package ru.shift.task6.client.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.channel.ChatChannel;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;
import ru.shift.task6.alt.commons.protocol.abstracts.Notification;
import ru.shift.task6.alt.commons.protocol.abstracts.Request;
import ru.shift.task6.alt.commons.protocol.abstracts.Response;
import ru.shift.task6.alt.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.client.exceptions.ResponseException;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;
import ru.shift.task6.commons.exceptions.SocketConnectionException;

@Slf4j
public class SocketConnection implements Closeable {

    private final Map<MessageType, CompletableFuture<Response>> pendingResponses = new ConcurrentHashMap<>();
    private final Map<MessageType, List<Consumer<Notification>>> notificationListeners = new ConcurrentHashMap<>();

    private final ExecutorService listenerPool = Executors.newSingleThreadExecutor(r -> new Thread(r, "MessageListener"));

    private final ChatChannel chatChannel;

    public SocketConnection(Socket socket) {
        chatChannel = new ChatChannel(socket);
        listenerPool.submit(this::listen);
    }

    private void listen() {
        try {
            Message message;
            while ((message = chatChannel.readMessage()) != null) {
                log.trace("Catch incoming message: {}", message);
                Optional.of(message)
                        .flatMap(this::handleSuccessResponse)
                        .flatMap(this::handleErrorResponse)
                        .flatMap(this::handleNotification)
                        .ifPresent(msg ->
                                new ErrorWindowImpl(
                                        "Unknown message catch",
                                        false
                                ).setEnabled(true)
                        );
            }
        } catch (ProtocolException e) {
            log.error("Deserialization incoming message error", e);
            throw new SocketConnectionException("Deserialization incoming message error", e);
        } catch (IOException e) {
            handleDisconnectionWhileListen();
        }
    }

    private Optional<Message> handleSuccessResponse(Message message) {
        if (!message.isResponse()) {
            return Optional.of(message);
        }
        var response = (Response) message;
        if (response.isError()) {
            return Optional.of(message);
        }
        CompletableFuture<Response> future = pendingResponses.remove(message.getType());
        if (future != null) {

            future.complete(response);
            return Optional.empty();
        }
        return Optional.of(message);
    }

    private Optional<Message> handleErrorResponse(Message message) {
        if (!message.isResponse()) {
            return Optional.of(message);
        }
        var response = (Response) message;
        var errorFuture = pendingResponses.remove(message.getType());
        if (errorFuture != null) {
            errorFuture.completeExceptionally(new ResponseException(response.getErrorMessage()));
            return Optional.empty();
        }
        if (response.getType() == MessageType.ERROR_RS) {
            new ErrorWindowImpl(
                    response.getErrorMessage(),
                    false
            ).setEnabled(true);
            return Optional.empty();
        }
        return Optional.of(message);
    }

    private Optional<Message> handleNotification(Message message) {
        if (message.isRequest() || message.isResponse()) {
            return Optional.of(message);
        }
        var notification = (Notification) message;
        var listener = notificationListeners.get(notification.getType());
        if (listener != null) {
            listener.forEach(l -> l.accept(notification));
            return Optional.empty();
        }
        return Optional.of(message);
    }

    private void handleDisconnectionWhileListen() {
        var disconnectionMessage = new DisconnectNotification("Server closed");

        var disconnectionListeners = notificationListeners.remove(MessageType.DISCONNECT_NF);
        if (disconnectionListeners != null) {
            disconnectionListeners.forEach(lst -> lst.accept(disconnectionMessage));
        }
    }


    public void send(Message message) {
        try {
            if (chatChannel.isClosed()) {
                log.warn("Socket is closed, cannot send message");
                return;
            }
            chatChannel.sendMessage(message);
            if (chatChannel.checkWriterError()) {
                throw new SocketConnectionException("Error while sending message");
            }
        } catch (ProtocolException e) {
            throw new SocketConnectionException("Error while sending message", e);
        }

    }

    public CompletableFuture<Response> sendAwaitResponse(
            Request request,
            MessageType responseType
    ) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        pendingResponses.put(responseType, future);

        try {
            send(request);
        } catch (SocketConnectionException e) {
            pendingResponses.remove(responseType);
            future.completeExceptionally(new ResponseException(e.getMessage()));
        }

        return future;
    }

    @SuppressWarnings("unchecked")
    public <T extends Notification> void addNotificationListener(MessageType messageType,
            Consumer<T> onMessage) {
        notificationListeners.computeIfAbsent(messageType, __ -> new CopyOnWriteArrayList<>())
                .add((Consumer<Notification>) onMessage);

    }

    @Override
    public void close() throws IOException {
        log.debug("Closing socket connection");
        listenerPool.shutdown();
        chatChannel.close();
    }
}
