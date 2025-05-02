package ru.shift.server.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.payload.UserInfo;
import ru.shift.server.client.ClientContext;

@Slf4j
public class ClientService {

    private final Map<UserInfo, ClientContext> clients = new ConcurrentHashMap<>();

    public UserInfo addClient(UserInfo user, ClientContext context) {
        val userToAdd = new UserInfo(user.getNickname(), Instant.now());
        clients.putIfAbsent(user, context);
        return userToAdd;
    }

    public void removeClient(UserInfo user) {
        if (!clients.containsKey(user)) {
            return;
        }
        try {
            clients.remove(user).close();
        } catch (IOException e) {
            log.error("Error while closing the client Context");
        }
    }

    public Collection<ClientContext> getAllClients() {
        return clients.values();
    }

    public void sendAll(Envelope<?> envelope) {
        clients.values().forEach(context -> context.getSender().send(envelope));
    }

    public List<UserInfo> getAllUsers() {
        return clients.keySet().stream().toList();
    }

    public boolean contains(UserInfo user) {
        return clients.keySet().stream().anyMatch(u -> u.getNickname().equals(user.getNickname()));
    }

    public void closeAll() throws IOException {
        for (ClientContext context : clients.values()) {
            context.close();
        }
    }
}
