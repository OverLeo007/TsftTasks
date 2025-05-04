package ru.shift.task6.client.presenter;

import ru.shift.commons.models.PayloadType;
import ru.shift.task6.client.socket.SocketClient;
import ru.shift.task6.client.view.windowImpl.ChatWindowImpl;
import ru.shift.task6.client.view.windowImpl.ErrorWindowImpl;

public class ChatPresenter {

    private final ChatWindowImpl window;
    private final SocketClient client;

    public ChatPresenter(ChatWindowImpl window, SocketClient client) {
        this.window = window;
        this.client = client;

        client.addListener(PayloadType.MESSAGE, window::appendMessage);
        client.addListener(PayloadType.JOIN_RS, window::onJoin);
        client.addListener(PayloadType.LEAVE_RS, window::onLeave);
        client.addListener(PayloadType.SHUTDOWN, window::onDisconnect);
    }

    public void joinChat() {
        client.sendJoinRequest(
                response -> {
                    window.show(response.getUser());
                    getActiveUsers();
                },
                error -> new ErrorWindowImpl(window, error.getMessage(), true)
        );
    }

    private void getActiveUsers() {
        client.sendUserListRequest(
                window::updateSidebar,
                error -> new ErrorWindowImpl(window, error.getMessage(), false)
        );
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

}
