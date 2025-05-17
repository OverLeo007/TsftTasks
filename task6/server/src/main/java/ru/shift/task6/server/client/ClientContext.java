package ru.shift.task6.server.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.channel.ChatChannel;
import ru.shift.task6.commons.channel.ChatReader;
import ru.shift.task6.commons.exceptions.SocketConnectionException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.commons.models.payload.responses.LeaveNotification;
import ru.shift.task6.server.exceptions.client.ForbiddenException;
import ru.shift.task6.server.exceptions.client.UnauthorizedException;

import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.function.Consumer;

@Slf4j
public class ClientContext implements AutoCloseable {

    @Getter
    private final MessageSender sender;

    private final ChatChannel chatChannel;
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
            throws SocketConnectionException {
        chatChannel = new ChatChannel(socket);
        this.sender = new MessageSender(broadcastConsumer, chatChannel);
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
        if (chatChannel.isClosed()) {
            log.debug("Socket already closed");
            return;
        }
        if (authorized) {
            log.debug("Closing clientContext for user: {}", user.getNickname());
            if (joined) {
                sender.broadcast(PayloadType.LEAVE_NOTIFICATION, new LeaveNotification(user));
            }
            onCloseOp.accept(user);

        } else {
            log.debug("Closing clientContext for socket: {}", chatChannel);
        }
        chatChannel.close();
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

    public ChatReader getReader() {
        return chatChannel;
    }

}
