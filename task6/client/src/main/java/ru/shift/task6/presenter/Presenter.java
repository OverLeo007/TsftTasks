package ru.shift.task6.presenter;

import lombok.val;
import ru.shift.task6.socket.SocketConnection;
import ru.shift.task6.view.View;

public class Presenter {

    public Presenter(View view, SocketConnection connection) {
        val chatPresenter = new ChatPresenter(view.getChatView(), connection);
        view.setChatPresenter(chatPresenter);
        val connectionPresenter = new ConnectionPresenter(
                view.getConnectionView(),
                connection,
                chatPresenter::start
        );
        view.setConnectionPresenter(connectionPresenter);
    }
}
