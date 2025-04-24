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

    public void put(Resource resource, String producerName) throws InterruptedException {
        log.info("{} хочет положить {}", producerName, resource);
        synchronized (monitor) {
            while (storageQueue.size() >= capacity) {
                log.info("{} ожидает, хранилище заполнено", producerName);
                monitor.wait();
            }
            storageQueue.offer(resource);
            log.info("{} положил {}, состояние хранилища: {}/{}", producerName, resource,
                    storageQueue.size(), capacity);
            monitor.notifyAll();
        }
    }

    public Resource get(String consumerName) throws InterruptedException {
        log.info("{} хочет забрать ресурс", consumerName);
        synchronized (monitor) {
            while (storageQueue.isEmpty()) {
                log.info("{} ожидает, хранилище пусто", consumerName);
                monitor.wait();
            }
            Resource resource = storageQueue.poll();
            log.info("{} взял {}, состояние хранилища: {}/{}", consumerName, resource,
                    storageQueue.size(), capacity);
            monitor.notifyAll();
            return resource;
        }
    }
}

