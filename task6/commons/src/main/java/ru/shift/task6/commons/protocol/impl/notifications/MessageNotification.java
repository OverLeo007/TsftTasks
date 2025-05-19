package ru.shift.task6.commons.protocol.impl.notifications;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.ChatMessage;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.abstracts.Notification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MessageNotification extends Notification {
    private ChatMessage message;

    public MessageNotification(ChatMessage message) {
        this.message = message;
    }

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE_NF;
    }
}
