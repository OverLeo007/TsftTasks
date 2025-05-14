package ru.shift.task6.client.presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import ru.shift.task6.client.socket.SocketClient;
import ru.shift.task6.client.view.windowImpl.ConnectionWindowImpl;

@RequiredArgsConstructor
public class ConnectionPresenter {
    private final ConnectionWindowImpl window;
    private final Consumer<SocketClient> onFinish;

    public void tryToConnect(String address) {
        final var split = address.split(":");
        try {
            final var socket = new Socket(split[0], Integer.parseInt(split[1]));
            window.onAddressSuccess();
            onFinish.accept(new SocketClient(socket));
        } catch (IOException e) {
            window.showAddressError(e.getMessage());
        }
    }
}
