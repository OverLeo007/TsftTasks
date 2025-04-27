package ru.shift;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
        log.info("{} начал работу", id);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                val resource = storage.get(id);
                log.info("{} потребляет {}", id, resource);
                //noinspection BusyWait
                Thread.sleep(consumeTimeMs);
                log.info("{} потребил {}", id, resource);
            }
        } catch (InterruptedException e) {
            log.error("Работа потока была прервана, так как", e);
            Thread.currentThread().interrupt();
        }
    }

}
