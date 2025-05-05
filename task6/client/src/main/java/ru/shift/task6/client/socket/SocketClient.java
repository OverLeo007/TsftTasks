package ru.shift.task6.client.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.ChatMessage;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.ShutdownNotice;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.commons.models.payload.requests.AuthRequest;
import ru.shift.task6.commons.models.payload.requests.JoinRequest;
import ru.shift.task6.commons.models.payload.requests.UserListRequest;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.JoinResponse;
import ru.shift.task6.commons.models.payload.responses.SuccessAuthResponse;
import ru.shift.task6.commons.models.payload.responses.UserListResponse;
import ru.shift.task6.client.exceptions.SocketConnectionException;

@Slf4j
@RequiredArgsConstructor
public class SocketClient implements Closeable {

    @Getter
    @Setter
    private UserInfo user;

    private final Connection connection;

    public SocketClient(Socket socket) {
        this.connection = new SocketConnection(socket);
    }

    public void sendAuthRequest(
            String nickname,
            Consumer<SuccessAuthResponse> onSuccess,
            Consumer<ErrorResponse> onError
    ) {
        log.debug("Sending auth request");
        connection.sendAwaitResponse(
                PayloadType.AUTH,
                new AuthRequest(new UserInfo(nickname, null)),
                PayloadType.SUCCESS,
                responseEnv -> {
                    val payload = (SuccessAuthResponse) responseEnv.getPayload();
                    onSuccess.accept(payload);
                    user = payload.getAuthUser();
                },
                errorEnv -> onError.accept(errorEnv.getPayload())

        );
    }

    public void sendJoinRequest(
            Consumer<JoinResponse> onSuccess,
            Consumer<ErrorResponse> onError
    ) {
        log.debug("Sending join request");
        connection.sendAwaitResponse(
                PayloadType.JOIN_RQ,
                new JoinRequest(),
                PayloadType.JOIN_RS,
                responseEnv -> onSuccess.accept((JoinResponse) responseEnv.getPayload()),
                errorEnv -> onError.accept(errorEnv.getPayload())
        );
    }

    public void sendUserListRequest(
            Consumer<List<UserInfo>> onSuccess,
            Consumer<ErrorResponse> onError
    ) {
        log.debug("Sending user list request");
        connection.sendAwaitResponse(
                PayloadType.USER_LIST_RQ,
                new UserListRequest(),
                PayloadType.USER_LIST_RS,
                responseEnv -> {
                    val typedPayload = (UserListResponse) responseEnv.getPayload();
                    onSuccess.accept(typedPayload.getUsers());
                },
                errorEnv -> onError.accept(errorEnv.getPayload())
        );
    }

    public void sendMessage(String message) {
        log.debug("Sending message");
        connection.send(PayloadType.MESSAGE,
                new ChatMessage(user, message, Instant.now()));
    }

    @SuppressWarnings("unchecked")
    public <T extends Payload> void addListener(PayloadType messageType, Consumer<T> onMessage) {

        connection.addPermanentMessageListener(messageType,
                envelope -> onMessage.accept((T) envelope.getPayload()));
    }


    public void sendDisconnection(String reason) {
        log.debug("Sending disconnection");
        try {
            connection.send(PayloadType.SHUTDOWN, new ShutdownNotice(reason));
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
