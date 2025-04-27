package ru.shift;

import lombok.Getter;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Resource {
    private static int lastId = 0;
    @Getter
    private final String id;

    public Resource() {
        this.id = getNextId();
    }

    private static synchronized String getNextId() {
        return "Ресурс-" + ++lastId;
    }

    @Override
    public String toString() {
        return id;
    }
}
