package ru.shift;

import lombok.Getter;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Resource {
    private static int lastId = 0;
    @Getter
    private final int id;

    public Resource() {
        this.id = getNextId();
    }

    private static synchronized int getNextId() {
        return lastId++;
    }

    @Override
    public String toString() {
        return "Ресурс-" + (id + 1);
    }
}
