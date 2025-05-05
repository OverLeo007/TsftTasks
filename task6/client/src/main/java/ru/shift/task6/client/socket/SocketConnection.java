package ru.shift.task6.client.socket;

import java.io.BufferedReader;
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
import ru.shift.task6.commons.JsonSerializer;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.Header;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.ShutdownNotice;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.task6.client.exceptions.SocketConnectionException;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;

@Slf4j
public class SocketConnection implements Connection {

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
                    log.trace("Catch raw message: {}", line);
                    val envelope = JsonSerializer.deserialize(line);
                    log.trace("Catch incoming message: {}", envelope);

                    if (envelope.getHeader().getPayloadType() == PayloadType.ERROR) {
                        val typedPayload = (ErrorResponse) envelope.getPayload();

                        if (typedPayload.getCorrectResponseType() == PayloadType.ERROR) {
                            new ErrorWindowImpl(
                                    typedPayload.getMessage(),
                                    false
                            ).setEnabled(true);
                        }

                        val errorCallback = errorMap.get(typedPayload.getCorrectResponseType());
                        if (errorCallback != null) {
                            log.debug("Calling error callback for {}",
                                    envelope.getHeader().getPayloadType());
                            errorCallback.accept((Envelope<ErrorResponse>) envelope);
                            return;
                        }
                    }

                    val callback = callbackMap.get(envelope.getHeader().getPayloadType());
                    if (callback != null) {
                        log.debug("Calling callback for {}", envelope.getHeader().getPayloadType());
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

    @Override
    public void send(PayloadType type, Payload payload) {
        val envelope = new Envelope<>(
                new Header(type, Instant.now()),
                payload
        );
        try {
            if (socket.isClosed()) {
                log.warn("Socket is closed, cannot send message");
                return;
            }
            writer.println(JsonSerializer.serialize(envelope));
            if (writer.checkError()) {
                throw new SocketConnectionException("Error while sending message");
            }
        } catch (SerializationException e) {
            throw new SocketConnectionException("Error while sending message", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Payload> void sendAwaitResponse(
            PayloadType requestType,
            Payload payload,
            PayloadType responseType,
            Consumer<Envelope<T>> onResponse,
            Consumer<Envelope<ErrorResponse>> onError
    ) {
        callbackMap.put(responseType, env -> {
            log.debug("Adding callback for {}", responseType);
            onResponse.accept((Envelope<T>) env);
            callbackMap.remove(responseType);
            errorMap.remove(responseType);
        });

        errorMap.put(responseType, env -> {
            log.debug("Adding error callback for {}", responseType);
            onError.accept(env);
            errorMap.remove(responseType);
            callbackMap.remove(responseType);
        });
        try {
            send(requestType, payload);
        } catch (SocketConnectionException e) {
            onError.accept(
                    new Envelope<>(
                            new Header(
                                    PayloadType.ERROR, Instant.now()
                            ),
                            new ErrorResponse(responseType, Fault.CLIENT, e.getMessage())
                    )
            );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Payload> void addPermanentMessageListener(
            PayloadType messageType,
            Consumer<Envelope<T>> onMessage
    ) {
        log.debug("Adding permanent listener for {}", messageType);
        callbackMap.put(messageType, env -> onMessage.accept((Envelope<T>) env));
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing socket connection");
        listener.interrupt();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
