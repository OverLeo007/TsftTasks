package ru.shift.task6.server.handling.senders;

import ru.shift.task6.alt.commons.protocol.abstracts.Notification;

@FunctionalInterface
public interface Broadcaster {
    void broadcast(Notification notification);
}
