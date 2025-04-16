package ru.shift;

import java.util.LinkedList;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Storage {

    private final Object PUT_LOCK = new Object();
    private final Object GET_LOCK = new Object();

    private final Queue<Resource> storage = new LinkedList<>();
    private final int capacity;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public void put(Resource resource) throws InterruptedException {
        synchronized (PUT_LOCK) {
            while (storage.size() >= capacity) {
                log.info("Storage is full, waiting to put resource...");
                PUT_LOCK.wait();
            }
            storage.offer(resource);
            log.info("Resource {} added to storage", resource.getId());
            GET_LOCK.notify();
        }
    }

    public void get() throws InterruptedException {
        synchronized (GET_LOCK) {
            while (storage.isEmpty()) {
                log.info("Storage is empty, waiting to get resource...");
                GET_LOCK.wait();
            }
            Resource resource = storage.poll();
            log.info("Resource {} removed from storage", resource.getId());
            PUT_LOCK.notify();
        }
    }
}
