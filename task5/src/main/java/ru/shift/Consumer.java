package ru.shift;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer implements Runnable {

    private static int lastId = 0;
    @Getter
    private final String id;
    private final long consumeTimeMs;
    private final Storage storage;


    public Consumer(long consumeTimeMs, Storage storage) {
        this.consumeTimeMs = consumeTimeMs;
        this.storage = storage;
        this.id = getNextId();
    }

    private static synchronized String getNextId() {
        return "Потребитель-" + ++lastId;
    }

    @Override
    public void run() {
        log.info("{} начал работу", this);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                var resource = storage.get(toString());
                log.info("{} потребляет {}", this, resource);
                //noinspection BusyWait
                Thread.sleep(consumeTimeMs);
                log.info("{} потребил {}", this, resource);
            }
        } catch (InterruptedException e) {
            log.error("Работа потока была прервана, так как", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return id;
    }

}
