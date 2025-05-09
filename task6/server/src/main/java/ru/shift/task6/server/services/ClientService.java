package ru.shift.task6.server.services;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.UserInfo;
import ru.shift.task6.server.client.ClientContext;

@Slf4j
public class ClientService {

    private final Map<UserInfo, ClientContext> clients = new ConcurrentHashMap<>();

    public UserInfo addClient(UserInfo user, ClientContext context) {
        log.debug("Adding new user: {}", user.getNickname());
        val userToAdd = new UserInfo(user.getNickname(), Instant.now());
        ClientContext existing = clients.putIfAbsent(userToAdd, context);
        if (existing != null) {
            throw new IllegalStateException("User already connected: " + user.getNickname());
        }
        return userToAdd;
    }

    public void removeClient(UserInfo user) {
        log.debug("Removing user: {}", user);
        if (!clients.containsKey(user)) {
            return;
        }
        clients.remove(user);
    }

    public void sendAll(Envelope<?> envelope) {
        log.debug("Broadcast {}", envelope);
        clients.values().forEach(context -> context.getSender().send(envelope));
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
