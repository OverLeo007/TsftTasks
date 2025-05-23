package ru.shift.task6.server.services;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.protocol.UserInfo;
import ru.shift.task6.commons.protocol.abstracts.Notification;
import ru.shift.task6.server.client.ClientContext;

@Slf4j
public class ClientService {

    private final Map<UserInfo, ClientContext> clients = new ConcurrentHashMap<>();

    public synchronized UserInfo addClient(UserInfo user, ClientContext context) {
        log.debug("Adding new user: {}", user.getNickname());
        final var userToAdd = new UserInfo(user.getNickname(), Instant.now());
        if (clients.containsKey(userToAdd)) {
            log.debug("Попытка использовать занятое имя пользователя");
            throw new IllegalStateException("User already connected: " + user.getNickname());
        }
        clients.put(userToAdd, context);
        return userToAdd;
    }

    public void removeClient(UserInfo user) {
        log.debug("Removing user: {}", user);
        if (!clients.containsKey(user)) {
            return;
        }
        clients.remove(user);
    }

    public void sendAll(Notification notification) {
        log.debug("Broadcast {}", notification);
        clients.values().forEach(context -> context.getSender().send(notification));
    }

    public List<UserInfo> getAllUsers() {
        return clients.keySet().stream().toList();
    }


    public void closeAll() throws IOException {
        log.debug("Closing all user connections");
        for (ClientContext context : clients.values()) {
            context.close();
        }
    }
}
