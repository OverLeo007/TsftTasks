package ru.shift.task6.presenter;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import ru.shift.task6.models.User;
import ru.shift.task6.socket.SocketConnection;
import ru.shift.task6.exceptions.AuthenticationException;
import ru.shift.task6.exceptions.SocketConnectionException;
import ru.shift.task6.view.connection.ConnectionView;

@RequiredArgsConstructor
public class ConnectionPresenter {
    private final ConnectionView view;
    private final SocketConnection socketConnection;
    private final Consumer<User> onFinish;
    private User user;


    public void setAddress(String address) {
        String[] parts = address.split(":");
        try {
            socketConnection.connect(parts[0], Integer.parseInt(parts[1]));
            view.onAddressSuccess();
        } catch (SocketConnectionException e) {
            view.showAddressError(e.getMessage());
        }
    }

    public void setNickname(String nickname) {
        try {
            user = socketConnection.authenticate(nickname);
            view.onNicknameSuccess();
        } catch (AuthenticationException e) {
            view.showNicknameError(e.getMessage());
        } catch (SocketConnectionException e) {
            view.showNicknameError("Ошибка соединения: " + e.getMessage());
        }
    }

    public void finish() {
        view.dispose();
        onFinish.accept(user);
    }
}
