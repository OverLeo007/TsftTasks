package ru.shift.task6.commons.protocol.impl.notifications;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.protocol.MessageType;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Notification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class JoinNotification extends Notification {
    private UserInfo user;

    public JoinNotification(UserInfo user) {
        this.user = user;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_NF;
    }
}
