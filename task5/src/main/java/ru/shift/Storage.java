package ru.shift;

import java.util.ArrayDeque;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Storage {
    private final Object monitor = new Object();

    private final Queue<Resource> storageQueue;
    private final int capacity;

    public Storage(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Вместимость склада должна быть больше 0");
        }
        this.capacity = capacity;
        this.storageQueue = new ArrayDeque<>(capacity);
    }

    public void put(Resource resource, String producerId) throws InterruptedException {
        log.info("{} хочет положить {}", producerId, resource);
        synchronized (monitor) {
            while (storageQueue.size() >= capacity) {
                log.info("{} ожидает, хранилище заполнено", producerId);
                monitor.wait();
            }
            storageQueue.offer(resource);
            log.info("{} положил {}, состояние хранилища: {}/{}", producerId, resource,
                    storageQueue.size(), capacity);
            monitor.notifyAll();
        }
    }

    public Resource get(String consumerId) throws InterruptedException {
        log.info("{} хочет забрать ресурс", consumerId);
        synchronized (monitor) {
            while (storageQueue.isEmpty()) {
                log.info("{} ожидает, хранилище пусто", consumerId);
                monitor.wait();
            }
            Resource resource = storageQueue.poll();
            log.info("{} взял {}, состояние хранилища: {}/{}", consumerId, resource,
                    storageQueue.size(), capacity);
            monitor.notifyAll();
            return resource;
        }
    }
}

