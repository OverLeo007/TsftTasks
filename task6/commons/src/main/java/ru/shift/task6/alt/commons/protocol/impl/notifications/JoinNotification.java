package ru.shift.task6.alt.commons.protocol.impl.notifications;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shift.task6.alt.commons.protocol.MessageType;
import ru.shift.task6.alt.commons.protocol.UserInfo;
import ru.shift.task6.alt.commons.protocol.abstracts.Notification;

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
