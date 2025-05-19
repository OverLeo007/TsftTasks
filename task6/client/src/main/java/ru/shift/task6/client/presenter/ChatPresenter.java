package ru.shift.task6.client.presenter;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.commons.protocol.impl.notifications.JoinNotification;
import ru.shift.task6.commons.protocol.impl.notifications.LeaveNotification;
import ru.shift.task6.commons.protocol.impl.notifications.MessageNotification;
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

        client.<MessageNotification>addListener(MessageType.MESSAGE_NF,
                nf -> window.appendMessage(nf.getMessage()));

        client.<JoinNotification>addListener(MessageType.JOIN_NF,
                nf -> window.onJoin(nf.getUser()));

        client.<LeaveNotification>addListener(MessageType.LEAVE_NF,
                nf -> window.onLeave(nf.getUser()));

        client.<DisconnectNotification>addListener(MessageType.DISCONNECT_NF,
                nf -> window.onDisconnect(nf.getReason()));
    }

    public void joinChat() {
        client.sendJoinRequest(
                response -> {
                    window.run();
                    getActiveUsers();
                },
                error -> {
                    new ErrorWindowImpl(window, error, true);
                    log.error("Error while joining chat: {}", error);
                }
        );
    }

    private void getActiveUsers() {
        client.sendUserListRequest(
                response -> window.updateSidebar(response.getUsersOnline()),
                error -> {
                    new ErrorWindowImpl(window, error, false);
                    log.error("Error while getting user list: {}", error);
                }
        );
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

}
