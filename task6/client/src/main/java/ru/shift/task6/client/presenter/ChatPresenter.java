package ru.shift.task6.client.presenter;

import lombok.extern.slf4j.Slf4j;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.responses.JoinNotification;
import ru.shift.commons.models.payload.responses.LeaveNotification;
import ru.shift.task6.client.socket.SocketClient;
import ru.shift.task6.client.view.windowImpl.ChatWindowImpl;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;

@Slf4j
public class ChatPresenter {

    private final ChatWindowImpl window;
    private final SocketClient client;

    public ChatPresenter(ChatWindowImpl window, SocketClient client) {
        this.window = window;
        this.client = client;

        client.addListener(PayloadType.MESSAGE, window::appendMessage);
        client.<JoinNotification>addListener(PayloadType.JOIN_NOTIFICATION,
                rsp -> window.onJoin(rsp.getUser()));
        client.<LeaveNotification>addListener(PayloadType.LEAVE_NOTIFICATION,
                rsp -> window.onLeave(rsp.getUser()));
        client.addListener(PayloadType.SHUTDOWN, window::onDisconnect);
    }

    public void joinChat() {
        client.sendJoinRequest(
                response -> {
                    window.run();
                    getActiveUsers();
                },
                error -> {
                    new ErrorWindowImpl(window, error.getMessage(), true);
                    log.error("Error while joining chat: {}", error.getMessage());
                }
        );
    }

    private void getActiveUsers() {
        client.sendUserListRequest(
                window::updateSidebar,
                error -> {
//                    new ErrorWindowImpl(window, error.getMessage(), false);
                    log.error("Error while getting user list: {}", error.getMessage());
                }
        );
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

}
