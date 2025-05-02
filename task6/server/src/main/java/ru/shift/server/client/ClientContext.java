package ru.shift.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.UserInfo;
import ru.shift.server.handling.MessageSender;
import ru.shift.server.exceptions.client.UnauthorizedException;

public class ClientContext implements AutoCloseable {

    private final Socket socket;
    @Getter
    private final MessageSender sender;
    @Getter
    private final BufferedReader reader;
    @Getter
    private UserInfo user;
    @Getter
    private boolean authorized;
    @Getter
    @Setter
    private boolean joined;

    public ClientContext(Socket socket, Consumer<Envelope<? extends Payload>> broadcastConsumer)
            throws IOException {
        this.socket = socket;
        this.sender = new MessageSender(
                broadcastConsumer,
                new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                        true
                )
        );
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

    }

    public void setUser(UserInfo user) {
        if (user.getLogTime() == null) {
            user.setLogTime(Instant.now());
        }
        this.user = user;
        this.authorized = true;
    }

    public void close() throws IOException {
        socket.close();
    }

    public void checkAuthorized(String msg) {
        if (!authorized) {
            throw new UnauthorizedException(msg);
        }
    }
}
