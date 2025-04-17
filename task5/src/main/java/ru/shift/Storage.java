package ru.shift;

import java.util.LinkedList;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Storage {
    private final Object monitor = new Object();

    private final Queue<Resource> storageQueue = new LinkedList<>();
    private final int capacity;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public void put(Resource resource, String producerName) throws InterruptedException {
        log.info("{} {} {}", producerName, VerbColoring.getPV("хочет положить"), resource);
        synchronized (monitor) {
            while (storageQueue.size() >= capacity) {
                log.info("{} {}, хранилище заполнено", producerName, VerbColoring.getPV("ожидает"));
                monitor.wait();
            }

            storageQueue.offer(resource);
            log.info("{} {} {}, состояние хранилища: {}/{}", producerName, VerbColoring.getPV("положил"), resource,
                    storageQueue.size(), capacity);

            monitor.notifyAll();
        }
    }

    public Resource get(String consumerName) throws InterruptedException {
        log.info("{} {} ресурс", consumerName, VerbColoring.getCV("хочет забрать"));
        synchronized (monitor) {
            while (storageQueue.isEmpty()) {
                log.info("{} {}, хранилище пусто", consumerName, VerbColoring.getCV("ожидает"));
                monitor.wait();
            }

            Resource resource = storageQueue.poll();
            log.info("{} {} {}, состояние хранилища: {}/{}", consumerName, VerbColoring.getCV("взял"), resource,
                    storageQueue.size(), capacity);

            monitor.notifyAll();

            return resource;
        }
    }
}

