package ru.shift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer implements Runnable {

    private static int lastId = 0;
    private final int id;
    private final long produceTimeMs;
    private final Storage storage;


    public Producer(long produceTimeMs, Storage storage) {
        this.produceTimeMs = produceTimeMs;
        this.storage = storage;
        this.id = getNextId();
    }

    private static synchronized int getNextId() {
        return lastId++;
    }


    @Override
    public void run() {
        log.info("{} {} работу", this, VerbColoring.getPV("начал"));
        try {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("{} {} ресурс", this, VerbColoring.getPV("производит"));
                //noinspection BusyWait
                Thread.sleep(produceTimeMs);

                var resource = new Resource();
                log.info("{} {} {}", this, VerbColoring.getPV("произвел"), resource);
                storage.put(resource, toString());
            }
        } catch (InterruptedException e) {
            log.error("Работа {} была прервана, так как {}", this, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "Производитель-" + (id + 1);
    }
}
