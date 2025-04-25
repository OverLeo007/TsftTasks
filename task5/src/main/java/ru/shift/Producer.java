package ru.shift;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer implements Runnable {

    private static int lastId = 0;
    @Getter
    private final String id;
    private final long produceTimeMs;
    private final Storage storage;


    public Producer(long produceTimeMs, Storage storage) {
        this.produceTimeMs = produceTimeMs;
        this.storage = storage;
        this.id = getNextId();
    }

    private static synchronized String getNextId() {
        return "Производитель-" + ++lastId;
    }


    @Override
    public void run() {
        log.info("{} начал работу", id);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("{} производит ресурс", id);
                //noinspection BusyWait
                Thread.sleep(produceTimeMs);

                var resource = new Resource();
                log.info("{} произвел {}", id, resource);
                storage.put(resource, id);
            }
        } catch (InterruptedException e) {
            log.error("Работа потока была прервана, так как", e);
            Thread.currentThread().interrupt();
        }
    }

//    @Override
//    public String toString() {
//        return id;
//    }
}
