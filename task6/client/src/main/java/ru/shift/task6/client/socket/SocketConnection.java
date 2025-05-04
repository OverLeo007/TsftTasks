package ru.shift.task6.client.socket;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.JsonSerializer;
import ru.shift.commons.exceptions.DeserializationException;
import ru.shift.commons.exceptions.SerializationException;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.Header;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.ShutdownNotice;
import ru.shift.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.client.exceptions.SocketConnectionException;

@Slf4j
public class SocketConnection implements Closeable {
        private final Socket socket;
    private final PrintWriter writer;
    private Thread listener;

    private final Map<PayloadType, Consumer<Envelope<?>>> callbackMap = new ConcurrentHashMap<>();
    private final Map<PayloadType, Consumer<Envelope<ErrorResponse>>> errorMap = new ConcurrentHashMap<>();

    public SocketConnection(Socket socket)
            throws SocketConnectionException {
        this.socket = socket;
        try {
            listen(new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)));
            this.writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            throw new SocketConnectionException("Error while connection creation", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void listen(BufferedReader reader) {
        listener = new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    val envelope = JsonSerializer.deserialize(line);
                    log.trace("Catch incoming message: {}", envelope);

                    if (envelope.getHeader().getPayloadType() == PayloadType.ERROR) {
                        val errorCallback = errorMap.get(envelope.getHeader().getPayloadType());
                        if (errorCallback != null) {
                            errorCallback.accept((Envelope<ErrorResponse>) envelope);
                            return;
                        }
                    }

                    val callback = callbackMap.get(envelope.getHeader().getPayloadType());
                    if (callback != null) {
                        callback.accept(envelope);
                    }
                }
            } catch (DeserializationException e) {
                throw new SocketConnectionException("Deserialization incoming message error", e);
            } catch (IOException e) {
                callbackMap.getOrDefault(PayloadType.SHUTDOWN, env -> {
                        })
                        .accept(new Envelope<>(new Header(PayloadType.SHUTDOWN, null),
                                new ShutdownNotice("Server disconnected")));
            }
        }, "MessageListener");
        listener.start();
    }

    public void send(PayloadType type, Payload payload) {
        val envelope = new Envelope<>(
                new Header(type, Instant.now()),
                payload
        );
        try {
            writer.println(JsonSerializer.serialize(envelope));
            if (writer.checkError()) {
                throw new SocketConnectionException("Error while sending message");
            }
        } catch (SerializationException e) {
            throw new SocketConnectionException("Error while sending message", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> void sendAwaitResponse(
            PayloadType requestType,
            Payload payload,
            PayloadType responseType,
            Consumer<Envelope<T>> onResponse,
            Consumer<Envelope<ErrorResponse>> onError
    ) {
        callbackMap.put(responseType, env -> {
            onResponse.accept((Envelope<T>) env);
            callbackMap.remove(responseType);
        });

        errorMap.put(responseType, env -> {
            onError.accept(env);
            errorMap.remove(responseType);
        });
        send(requestType, payload);
    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> void addPermanentMessageListener(
            PayloadType messageType,
            Consumer<Envelope<T>> onMessage
    ) {
        callbackMap.put(messageType, env -> onMessage.accept((Envelope<T>) env));
    }

    @Override
    public void close() throws IOException {
        listener.interrupt();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
