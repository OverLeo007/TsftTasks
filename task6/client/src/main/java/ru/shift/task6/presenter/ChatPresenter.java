package ru.shift.task6.presenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import ru.shift.task6.models.Message;
import ru.shift.task6.models.MessageType;
import ru.shift.task6.models.User;
import ru.shift.task6.socket.SocketConnection;
import ru.shift.task6.view.chat.ChatView;

@RequiredArgsConstructor
public class ChatPresenter {
    private final ChatView view;
    private final SocketConnection socketConnection;

    public void start(User user) {
        socketConnection.setOnMessageListener(this::acceptMessage);
        socketConnection.setOnDisconnectListener(view::onDisconnect);
        view.show(user);
    }

    public void sendMessage(String text) {
        try {
            socketConnection.sendMessage(text);
        } catch (JsonProcessingException e) {
            view.addMessage(new Message(
                    SocketConnection.CLIENT_USER,
                    MessageType.ERROR,
                    "Ошибка отправки сообщения: " + e.getMessage(),
                    Instant.now()
            ));
        }
    }

    private void acceptMessage(Message message) {
        switch (message.messageType()) {
            case TEXT, ERROR, SYSTEM -> view.addMessage(message);
            case JOIN -> {
                view.joinUser(message.sender());
                view.addMessage(message);
            }
            case LEAVE -> {
                view.leaveUser(message.sender());
                view.addMessage(message);
            }
            case SUCCESS -> {}
        }

    }
}
