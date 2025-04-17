package ru.shift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer implements Runnable {

    private static int lastId = 0;
    private final int id;
    private final long consumeTimeMs;
    private final Storage storage;


    public Consumer(long consumeTimeMs, Storage storage) {
        this.consumeTimeMs = consumeTimeMs;
        this.storage = storage;
        this.id = getNextId();
    }

    private static synchronized int getNextId() {
        return lastId++;
    }

    @Override
    public void run() {
        log.info("{} {} работу", this, VerbColoring.getCV("начал"));

        try {
            while (!Thread.currentThread().isInterrupted()) {
                var resource = storage.get(toString());
                log.info("{} {} {}", this, VerbColoring.getCV("потребляет"), resource);
                //noinspection BusyWait
                Thread.sleep(consumeTimeMs);
                log.info("{} {} {}", this, VerbColoring.getCV("потребил"), resource);
            }
        } catch (InterruptedException e) {
            log.error("Работа {} была прервана, так как {}", this, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "\t".repeat(12) + "Потребитель-" + (id + 1);
    }

}
