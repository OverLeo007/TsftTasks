package ru.shift.task6.commons.protocol.impl.notifications;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.abstracts.Notification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DisconnectNotification extends Notification {
    private String reason;

    public DisconnectNotification(String reason) {
        this.reason = reason;
    }

    @Override
    public MessageType getType() {
        return MessageType.DISCONNECT_NF;
    }
}
