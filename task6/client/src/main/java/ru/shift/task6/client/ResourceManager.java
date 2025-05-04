package ru.shift.task6.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceManager implements Closeable {

    private final List<Closeable> resources = new ArrayList<>();

    public void register(Closeable closeable) {
        resources.add(closeable);
    }

    @Override
    public void close() {
        resources.forEach(c -> {
            try {
                c.close();
            } catch (IOException e) {
                log.error("Ошибка при закрытии ресурса", e);
            }
        });
    }
}
