package ru.shift.task6.server.client;

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
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.commons.models.payload.responses.LeaveNotification;
import ru.shift.task6.server.exceptions.client.ForbiddenException;
import ru.shift.task6.server.handling.MessageSender;
import ru.shift.task6.server.exceptions.client.UnauthorizedException;

@Slf4j
public class ClientContext implements AutoCloseable {

    private final Socket socket;
    @Getter
    private final MessageSender sender;
    @Getter
    private final BufferedReader reader;
    @Getter
    private UserInfo user;

    private boolean authorized;

    @Getter
    @Setter
    private boolean joined;

    private final Consumer<UserInfo> onCloseOp;

    @Getter
    private boolean closed = false;

    public ClientContext(
            Socket socket,
            Consumer<Envelope<? extends Payload>> broadcastConsumer,
            Consumer<UserInfo> onCloseOp
    )
            throws IOException {
        this.socket = socket;
        this.sender = new MessageSender(
                broadcastConsumer,
                new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        );
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.onCloseOp = onCloseOp;

    }

    public void setUser(UserInfo user) {
        if (user.getLogTime() == null) {
            user.setLogTime(Instant.now());
        }
        this.user = user;
        this.authorized = true;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        if (socket.isClosed()) {
            log.debug("Socket already closed");
            return;
        }
        if (authorized) {
            log.debug("Closing context for user: {}", user.getNickname());
            if (joined) {
                sender.broadcast(PayloadType.LEAVE_NOTIFICATION, new LeaveNotification(user));
            }
            onCloseOp.accept(user);

        } else {
            log.debug("Closing context for socket: {}", socket);
        }
        socket.close();
    }

    public void checkAuthorized(String msg) {
        if (!authorized) {
            throw new UnauthorizedException(msg);
        }
    }

    public void checkJoined(String msg) {
        if (!joined) {
            throw new ForbiddenException(msg);
        }
    }
}
