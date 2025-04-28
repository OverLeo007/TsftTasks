package ru.shift.task6;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.models.User;

@Slf4j
public class ClientRepository {

    private final Map<User, ClientHandler> clients = new ConcurrentHashMap<>();

    public void addClient(User user, ClientHandler handler) {
        clients.putIfAbsent(user, handler);
    }

    public void removeClient(User user) {
        clients.remove(user);
    }

    public Collection<ClientHandler> getAllClients() {
        return clients.values();
    }

    public boolean contains(User user) {
        return clients.keySet().stream().anyMatch(u -> u.name().equals(user.name()));
    }

    public void closeAll() throws IOException {
        for (ClientHandler handler : clients.values()) {

            handler.close();
        }
    }
}
