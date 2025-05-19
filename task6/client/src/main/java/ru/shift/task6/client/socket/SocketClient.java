package ru.shift.task6.client.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.ChatMessage;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.UserInfo;
import ru.shift.task6.alt.commons.protocol.abstracts.Notification;
import ru.shift.task6.alt.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.alt.commons.protocol.impl.requests.AuthRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.JoinRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.MessageRequest;
import ru.shift.task6.alt.commons.protocol.impl.requests.UserListRequest;
import ru.shift.task6.alt.commons.protocol.impl.responses.AuthResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.JoinResponse;
import ru.shift.task6.alt.commons.protocol.impl.responses.UserListResponse;
import ru.shift.task6.client.socket.response.handlers.ErrorResponseHandler;
import ru.shift.task6.client.socket.response.handlers.ResponseHandler;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;
import ru.shift.task6.commons.exceptions.SocketConnectionException;


@Slf4j
@RequiredArgsConstructor
public class SocketClient implements Closeable {

    @Getter
    @Setter
    private UserInfo user;

    private final SocketConnection connection;

    public SocketClient(Socket socket) {
        this.connection = new SocketConnection(socket);
    }

    public void sendAuthRequest(
            String nickname,
            ResponseHandler<AuthResponse> onSuccess,
            ErrorResponseHandler onError
    ) {
        log.debug("Sending auth request");
        var request = new AuthRequest(new UserInfo(nickname, null));
        connection.sendAwaitResponse(
                        request,
                        MessageType.AUTH_RS
                )
                .thenAccept(resp -> {
                    var authResp = (AuthResponse) resp;
                    onSuccess.handle(authResp);
                    user = authResp.getUser();
                })
                .exceptionally(onError::handle);
    }

    public void sendJoinRequest(
            ResponseHandler<JoinResponse> onSuccess,
            ErrorResponseHandler onError
    ) {
        log.debug("Sending join request");
        connection.sendAwaitResponse(
                        new JoinRequest(),
                        MessageType.JOIN_RS
                )
                .thenAccept(onSuccess::handle)
                .exceptionally(onError::handle);
    }

    public void sendUserListRequest(
            ResponseHandler<UserListResponse> onSuccess,
            ErrorResponseHandler onError
    ) {
        log.debug("Sending user list request");
        connection.sendAwaitResponse(
                        new UserListRequest(),
                        MessageType.USER_LIST_RS
                )
                .thenAccept(onSuccess::handle)
                .exceptionally(onError::handle);
    }

    public void sendMessage(
            String message
    ) {
        log.debug("Sending message");
        connection.sendAwaitResponse(
                new MessageRequest(new ChatMessage(user, Instant.now(), message)),
                MessageType.MESSAGE_RS
        )
                .thenAccept(rsp -> {})
                .exceptionally(ex -> {
                    new ErrorWindowImpl(ex.getMessage(), false);
                    return null;
                });
    }

    public <T extends Notification> void addListener(MessageType messageType, Consumer<T> onMessage) {
        connection.addPermanentMessageListener(messageType, onMessage);
    }


    public void sendDisconnection(String reason) {
        log.debug("Sending disconnection");
        try {
            connection.send(new DisconnectNotification(reason));
        } catch (SocketConnectionException e) {
            log.warn("Ошибка при отключении: ", e);
        }
    }


    @Override
    public void close() throws IOException {
        sendDisconnection("Client turning off");
        connection.close();
    }
}
